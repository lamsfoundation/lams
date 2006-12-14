/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */

package org.lamsfoundation.lams.tool.notebook.web.actions;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.authoring.web.AuthoringConstants;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.notebook.model.Notebook;
import org.lamsfoundation.lams.tool.notebook.model.NotebookAttachment;
import org.lamsfoundation.lams.tool.notebook.service.INotebookService;
import org.lamsfoundation.lams.tool.notebook.service.NotebookServiceProxy;
import org.lamsfoundation.lams.tool.notebook.util.NotebookConstants;
import org.lamsfoundation.lams.tool.notebook.web.forms.AuthoringForm;
import org.lamsfoundation.lams.util.FileValidatorUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 * @author
 * @version
 * 
 * @struts.action path="/authoring" name="authoringForm" parameter="dispatch"
 *                scope="request" validate="false"
 * 
 * @struts.action-forward name="success" path="tiles:/authoring/main"
 * @struts.action-forward name="message_page" path="tiles:/generic/message"
 */
public class AuthoringAction extends LamsDispatchAction {

	private static Logger logger = Logger.getLogger(AuthoringAction.class);

	public INotebookService notebookService;

	// Authoring SessionMap key names
	private static final String KEY_TOOL_CONTENT_ID = "toolContentID";

	private static final String KEY_CONTENT_FOLDER_ID = "contentFolderID";
	
	private static final String KEY_MODE = "mode";

	private static final String KEY_ONLINE_FILES = "onlineFiles";

	private static final String KEY_OFFLINE_FILES = "offlineFiles";

	private static final String KEY_UNSAVED_ONLINE_FILES = "unsavedOnlineFiles";

	private static final String KEY_UNSAVED_OFFLINE_FILES = "unsavedOfflineFiles";

	private static final String KEY_DELETED_FILES = "deletedFiles";

	/**
	 * Default method when no dispatch parameter is specified. It is expected
	 * that the parameter <code>toolContentID</code> will be passed in. This
	 * will be used to retrieve content for this tool.
	 * 
	 */
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		// Extract toolContentID from parameters.
		Long toolContentID = new Long(WebUtil.readLongParam(request,
				AttributeNames.PARAM_TOOL_CONTENT_ID));

		String contentFolderID = WebUtil.readStrParam(request,
				AttributeNames.PARAM_CONTENT_FOLDER_ID);

		ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, "mode",
				true);

		// set up notebookService
		if (notebookService == null) {
			notebookService = NotebookServiceProxy.getNotebookService(this
					.getServlet().getServletContext());
		}

		// retrieving Notebook with given toolContentID
		Notebook notebook = notebookService
				.getNotebookByContentId(toolContentID);
		if (notebook == null) {
			notebook = notebookService.copyDefaultContent(toolContentID);
			notebook.setCreateDate(new Date());
			notebookService.saveOrUpdateNotebook(notebook);
			// TODO NOTE: this causes DB orphans when LD not saved.
		}

		// check if content in use is set
		if (notebook.isContentInUse()) {
			// Cannot edit, send to message page.
			request.setAttribute(NotebookConstants.ATTR_MESSAGE, getResources(
					request).getMessage("error.content.locked"));
			return mapping.findForward("message_page");
		}

		if (mode != null && mode.isTeacher()) {
			// Set the defineLater flag so that learners cannot use content
			// while we
			// are editing. This flag is released when updateContent is called.
			notebook.setDefineLater(true);
			notebookService.saveOrUpdateNotebook(notebook);
		}

		// Set up the authForm.
		AuthoringForm authForm = (AuthoringForm) form;
		updateAuthForm(authForm, notebook);

		// Set up sessionMap
		SessionMap<String, Object> map = createSessionMap(notebook,
				getAccessMode(request), contentFolderID, toolContentID);
		authForm.setSessionMapID(map.getSessionID());

		// add the sessionMap to HTTPSession.
		request.getSession().setAttribute(map.getSessionID(), map);
		request.setAttribute(NotebookConstants.ATTR_SESSION_MAP, map);

		return mapping.findForward("success");
	}

	public ActionForward updateContent(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO need error checking.

		// get authForm and session map.
		AuthoringForm authForm = (AuthoringForm) form;
		SessionMap<String, Object> map = getSessionMap(request, authForm);

		// get notebook content.
		Notebook notebook = notebookService.getNotebookByContentId((Long) map
				.get(KEY_TOOL_CONTENT_ID));

		// update notebook content using form inputs.
		updateNotebook(notebook, authForm);

		// remove attachments marked for deletion.
		Set<NotebookAttachment> attachments = notebook.getNotebookAttachments();
		if (attachments == null) {
			attachments = new HashSet<NotebookAttachment>();
		}

		for (NotebookAttachment att : getAttList(KEY_DELETED_FILES, map)) {
			// remove from db, leave in repository
			attachments.remove(att);
		}

		// add unsaved attachments
		attachments.addAll(getAttList(KEY_UNSAVED_ONLINE_FILES, map));
		attachments.addAll(getAttList(KEY_UNSAVED_OFFLINE_FILES, map));

		// set attachments in case it didn't exist
		notebook.setNotebookAttachments(attachments);

		// set the update date
		notebook.setUpdateDate(new Date());

		// releasing defineLater flag so that learner can start using the tool.
		notebook.setDefineLater(false);

		notebookService.saveOrUpdateNotebook(notebook);

		request.setAttribute(AuthoringConstants.LAMS_AUTHORING_SUCCESS_FLAG,
				Boolean.TRUE);

		// add the sessionMapID to form
		authForm.setSessionMapID(map.getSessionID());

		request.setAttribute(NotebookConstants.ATTR_SESSION_MAP, map);

		return mapping.findForward("success");
	}

	public ActionForward uploadOnline(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return uploadFile(mapping, (AuthoringForm) form,
				IToolContentHandler.TYPE_ONLINE, request);
	}

	public ActionForward uploadOffline(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return uploadFile(mapping, (AuthoringForm) form,
				IToolContentHandler.TYPE_OFFLINE, request);
	}

	public ActionForward deleteOnline(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return deleteFile(mapping, (AuthoringForm) form,
				IToolContentHandler.TYPE_ONLINE, request);
	}

	public ActionForward deleteOffline(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return deleteFile(mapping, (AuthoringForm) form,
				IToolContentHandler.TYPE_OFFLINE, request);
	}

	public ActionForward removeUnsavedOnline(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		return removeUnsaved(mapping, (AuthoringForm) form,
				IToolContentHandler.TYPE_ONLINE, request);
	}

	public ActionForward removeUnsavedOffline(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		return removeUnsaved(mapping, (AuthoringForm) form,
				IToolContentHandler.TYPE_OFFLINE, request);
	}

	/* ========== Private Methods ********** */

	private ActionForward uploadFile(ActionMapping mapping,
			AuthoringForm authForm, String type, HttpServletRequest request) {
		SessionMap<String, Object> map = getSessionMap(request, authForm);

		FormFile file;
		List<NotebookAttachment> unsavedFiles;
		List<NotebookAttachment> savedFiles;
		if (StringUtils.equals(IToolContentHandler.TYPE_OFFLINE, type)) {
			file = (FormFile) authForm.getOfflineFile();
			unsavedFiles = getAttList(KEY_UNSAVED_OFFLINE_FILES, map);

			savedFiles = getAttList(KEY_OFFLINE_FILES, map);
		} else {
			file = (FormFile) authForm.getOnlineFile();
			unsavedFiles = getAttList(KEY_UNSAVED_ONLINE_FILES, map);

			savedFiles = getAttList(KEY_ONLINE_FILES, map);
		}

		
		//validate file max size
		ActionMessages errors = new ActionMessages();
		FileValidatorUtil.validateFileSize(file, true, errors );
		if(!errors.isEmpty()){
			request.setAttribute(NotebookConstants.ATTR_SESSION_MAP, map);
			this.saveErrors(request, errors);
			return mapping.findForward("success");
		}
		
		if (file.getFileName().length() != 0) {

			// upload file to repository
			NotebookAttachment newAtt = notebookService.uploadFileToContent(
					(Long) map.get(KEY_TOOL_CONTENT_ID), file, type);

			// Add attachment to unsavedFiles
			// check to see if file with same name exists
			NotebookAttachment currAtt;
			Iterator iter = savedFiles.iterator();
			while (iter.hasNext()) {
				currAtt = (NotebookAttachment) iter.next();
				if (StringUtils.equals(currAtt.getFileName(), newAtt
						.getFileName())) {
					// move from this this list to deleted list.
					getAttList(KEY_DELETED_FILES, map).add(currAtt);
					iter.remove();
					break;
				}
			}
			unsavedFiles.add(newAtt);

			request.setAttribute(NotebookConstants.ATTR_SESSION_MAP, map);
			request.setAttribute("unsavedChanges", new Boolean(true));
		}
		return mapping.findForward("success");
	}

	private ActionForward deleteFile(ActionMapping mapping,
			AuthoringForm authForm, String type, HttpServletRequest request) {
		SessionMap<String, Object> map = getSessionMap(request, authForm);

		List fileList;
		if (StringUtils.equals(IToolContentHandler.TYPE_OFFLINE, type)) {
			fileList = getAttList(KEY_OFFLINE_FILES, map);
		} else {
			fileList = getAttList(KEY_ONLINE_FILES, map);
		}

		Iterator iter = fileList.iterator();

		while (iter.hasNext()) {
			NotebookAttachment att = (NotebookAttachment) iter.next();

			if (att.getFileUuid().equals(authForm.getDeleteFileUuid())) {
				// move to delete file list, deleted at next updateContent
				getAttList(KEY_DELETED_FILES, map).add(att);

				// remove from this list
				iter.remove();
				break;
			}
		}

		request.setAttribute(NotebookConstants.ATTR_SESSION_MAP, map);
		request.setAttribute("unsavedChanges", new Boolean(true));

		return mapping.findForward("success");
	}

	private ActionForward removeUnsaved(ActionMapping mapping,
			AuthoringForm authForm, String type, HttpServletRequest request) {
		SessionMap<String, Object> map = getSessionMap(request, authForm);

		List unsavedFiles;

		if (StringUtils.equals(IToolContentHandler.TYPE_OFFLINE, type)) {
			unsavedFiles = getAttList(KEY_UNSAVED_OFFLINE_FILES, map);
		} else {
			unsavedFiles = getAttList(KEY_UNSAVED_ONLINE_FILES, map);
		}

		Iterator iter = unsavedFiles.iterator();
		while (iter.hasNext()) {
			NotebookAttachment att = (NotebookAttachment) iter.next();

			if (att.getFileUuid().equals(authForm.getDeleteFileUuid())) {
				// delete from repository and list
				notebookService.deleteFromRepository(att.getFileUuid(), att
						.getFileVersionId());
				iter.remove();
				break;
			}
		}

		request.setAttribute(NotebookConstants.ATTR_SESSION_MAP, map);
		request.setAttribute("unsavedChanges", new Boolean(true));

		return mapping.findForward("success");
	}

	/**
	 * Updates Notebook content using AuthoringForm inputs.
	 * 
	 * @param authForm
	 * @return
	 */
	private void updateNotebook(Notebook notebook, AuthoringForm authForm) {
		notebook.setTitle(authForm.getTitle());
		notebook.setInstructions(authForm.getInstructions());
		notebook.setOfflineInstructions(authForm.getOnlineInstruction());
		notebook.setOnlineInstructions(authForm.getOfflineInstruction());
		notebook.setLockOnFinished(authForm.isLockOnFinished());
		notebook.setAllowRichEditor(authForm.isAllowRichEditor());
	}

	/**
	 * Updates AuthoringForm using Notebook content.
	 * 
	 * @param notebook
	 * @param authForm
	 * @return
	 */
	private void updateAuthForm(AuthoringForm authForm, Notebook notebook) {
		authForm.setTitle(notebook.getTitle());
		authForm.setInstructions(notebook.getInstructions());
		authForm.setOnlineInstruction(notebook.getOnlineInstructions());
		authForm.setOfflineInstruction(notebook.getOfflineInstructions());
		authForm.setLockOnFinished(notebook.isLockOnFinished());
		authForm.setAllowRichEditor(notebook.isAllowRichEditor());
	}

	/**
	 * Updates SessionMap using Notebook content.
	 * 
	 * @param notebook
	 * @param mode
	 */
	private SessionMap<String, Object> createSessionMap(Notebook notebook,
			ToolAccessMode mode, String contentFolderID, Long toolContentID) {

		SessionMap<String, Object> map = new SessionMap<String, Object>();

		map.put(KEY_MODE, mode);
		map.put(KEY_CONTENT_FOLDER_ID, contentFolderID);
		map.put(KEY_TOOL_CONTENT_ID, toolContentID);
		map.put(KEY_ONLINE_FILES, new LinkedList<NotebookAttachment>());
		map.put(KEY_OFFLINE_FILES, new LinkedList<NotebookAttachment>());
		map.put(KEY_UNSAVED_ONLINE_FILES, new LinkedList<NotebookAttachment>());
		map
				.put(KEY_UNSAVED_OFFLINE_FILES,
						new LinkedList<NotebookAttachment>());
		map.put(KEY_DELETED_FILES, new LinkedList<NotebookAttachment>());

		Iterator iter = notebook.getNotebookAttachments().iterator();
		while (iter.hasNext()) {
			NotebookAttachment attachment = (NotebookAttachment) iter.next();
			String type = attachment.getFileType();
			if (type.equals(IToolContentHandler.TYPE_OFFLINE)) {
				getAttList(KEY_OFFLINE_FILES, map).add(attachment);
			}
			if (type.equals(IToolContentHandler.TYPE_ONLINE)) {
				getAttList(KEY_ONLINE_FILES, map).add(attachment);
			}
		}

		return map;
	}

	/**
	 * Get ToolAccessMode from HttpRequest parameters. Default value is AUTHOR
	 * mode.
	 * 
	 * @param request
	 * @return
	 */
	private ToolAccessMode getAccessMode(HttpServletRequest request) {
		ToolAccessMode mode;
		String modeStr = request.getParameter(AttributeNames.ATTR_MODE);
		if (StringUtils.equalsIgnoreCase(modeStr, ToolAccessMode.TEACHER
				.toString()))
			mode = ToolAccessMode.TEACHER;
		else
			mode = ToolAccessMode.AUTHOR;
		return mode;
	}

	/**
	 * Retrieves a List of attachments from the map using the key.
	 * 
	 * @param key
	 * @param map
	 * @return
	 */
	private List<NotebookAttachment> getAttList(String key,
			SessionMap<String, Object> map) {
		List<NotebookAttachment> list = (List<NotebookAttachment>) map.get(key);
		return list;
	}

	/**
	 * Retrieve the SessionMap from the HttpSession.
	 * 
	 * @param request
	 * @param authForm
	 * @return
	 */
	private SessionMap<String, Object> getSessionMap(
			HttpServletRequest request, AuthoringForm authForm) {
		return (SessionMap<String, Object>) request.getSession().getAttribute(
				authForm.getSessionMapID());
	}
}