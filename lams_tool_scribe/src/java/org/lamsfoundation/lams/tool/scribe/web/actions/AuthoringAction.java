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
/* $$Id$ */

package org.lamsfoundation.lams.tool.scribe.web.actions;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.authoring.web.AuthoringConstants;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.scribe.model.Scribe;
import org.lamsfoundation.lams.tool.scribe.model.ScribeAttachment;
import org.lamsfoundation.lams.tool.scribe.model.ScribeHeading;
import org.lamsfoundation.lams.tool.scribe.service.IScribeService;
import org.lamsfoundation.lams.tool.scribe.service.ScribeServiceProxy;
import org.lamsfoundation.lams.tool.scribe.util.ScribeConstants;
import org.lamsfoundation.lams.tool.scribe.web.forms.AuthoringForm;
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
 * @struts.action-forward name="heading_form"
 *                        path="tiles:/authoring/headingForm"
 * @struts.action-forward name="heading_response"
 *                        path="tiles:/authoring/headingResponse"
 */
public class AuthoringAction extends LamsDispatchAction {

	private static Logger logger = Logger.getLogger(AuthoringAction.class);

	public IScribeService scribeService;

	// Authoring SessionMap key names
	private static final String KEY_TOOL_CONTENT_ID = "toolContentID";

	private static final String KEY_CONTENT_FOLDER_ID = "contentFolderID";

	private static final String KEY_MODE = "mode";

	private static final String KEY_ONLINE_FILES = "onlineFiles";

	private static final String KEY_OFFLINE_FILES = "offlineFiles";

	private static final String KEY_UNSAVED_ONLINE_FILES = "unsavedOnlineFiles";

	private static final String KEY_UNSAVED_OFFLINE_FILES = "unsavedOfflineFiles";

	private static final String KEY_DELETED_FILES = "deletedFiles";

	private static final String KEY_HEADINGS = "headings";

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

		// set up scribeService
		if (scribeService == null) {
			scribeService = ScribeServiceProxy.getScribeService(this
					.getServlet().getServletContext());
		}

		// retrieving Scribe with given toolContentID
		Scribe scribe = scribeService.getScribeByContentId(toolContentID);
		if (scribe == null) {
			scribe = scribeService.copyDefaultContent(toolContentID);
			scribe.setCreateDate(new Date());
			scribeService.saveOrUpdateScribe(scribe);
			// TODO NOTE: this causes DB orphans when LD not saved.
		}

		// check if content in use is set
		if (scribe.getContentInUse()) {
			// Cannot edit, send to message page.
			request.setAttribute(ScribeConstants.ATTR_MESSAGE, getResources(
					request).getMessage("error.content.locked"));
			return mapping.findForward("message_page");
		}

		// Set the defineLater flag so that learners cannot use content while we
		// are editing. This flag is released when updateContent is called.
		scribe.setDefineLater(true);
		scribeService.saveOrUpdateScribe(scribe);

		// Set up the authForm.
		AuthoringForm authForm = (AuthoringForm) form;
		updateAuthForm(authForm, scribe);

		// Set up sessionMap
		SessionMap<String, Object> map = createSessionMap(scribe,
				getAccessMode(request), contentFolderID, toolContentID);
		authForm.setSessionMapID(map.getSessionID());

		// add the sessionMap to HTTPSession.
		request.getSession().setAttribute(map.getSessionID(), map);
		request.setAttribute(ScribeConstants.ATTR_SESSION_MAP, map);

		return mapping.findForward("success");
	}

	public ActionForward updateContent(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO need error checking.

		// get authForm and session map.
		AuthoringForm authForm = (AuthoringForm) form;
		SessionMap<String, Object> map = getSessionMap(request, authForm);

		// get scribe content.
		Scribe scribe = scribeService.getScribeByContentId((Long) map
				.get(KEY_TOOL_CONTENT_ID));

		// update scribe content using form inputs.
		updateScribe(scribe, authForm);

		// remove attachments marked for deletion.
		Set<ScribeAttachment> attachments = scribe.getScribeAttachments();
		if (attachments == null) {
			attachments = new HashSet<ScribeAttachment>();
		}

		for (ScribeAttachment att : getAttList(KEY_DELETED_FILES, map)) {
			// remove from repository and db
			scribeService.deleteFromRepository(att.getFileUuid(), att
					.getFileVersionId());
			attachments.remove(att);
		}

		// add unsaved attachments
		attachments.addAll(getAttList(KEY_UNSAVED_ONLINE_FILES, map));
		attachments.addAll(getAttList(KEY_UNSAVED_OFFLINE_FILES, map));

		// set attachments in case it didn't exist
		scribe.setScribeAttachments(attachments);

		// update headings.
		List<ScribeHeading> updatedHeadings = getHeadingList(map);
		Set currentHeadings = scribe.getScribeHeadings();
		currentHeadings.clear();		
		for (ScribeHeading heading : updatedHeadings) {
			heading.setUid(null);
			currentHeadings.add(heading);			
		}

		// set the update date
		scribe.setUpdateDate(new Date());

		// releasing defineLater flag so that learner can start using the tool.
		scribe.setDefineLater(false);

		scribeService.saveOrUpdateScribe(scribe);

		request.setAttribute(AuthoringConstants.LAMS_AUTHORING_SUCCESS_FLAG,
				Boolean.TRUE);

		// add the sessionMapID to form
		authForm.setSessionMapID(map.getSessionID());

		request.setAttribute(ScribeConstants.ATTR_SESSION_MAP, map);

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

	public ActionForward loadHeadingForm(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		String sessionMapID = WebUtil.readStrParam(request, "sessionMapID");
		Integer headingIndex = WebUtil.readIntParam(request, "headingIndex", true);

		AuthoringForm authForm = ((AuthoringForm) form);
		
		if (headingIndex == null) {
			headingIndex = -1;
		}
		
		authForm.setHeadingIndex(headingIndex);
		authForm.setSessionMapID(sessionMapID);

		return mapping.findForward("heading_form");
	}

	public ActionForward addOrUpdateHeading(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		AuthoringForm authForm = (AuthoringForm) form;
		SessionMap<String, Object> map = getSessionMap(request, authForm);

		String headingText = authForm.getHeading();
		Integer headingIndex = authForm.getHeadingIndex();
		Long toolContentID = getToolContentID(map);

		Scribe scribe = scribeService.getScribeByContentId(toolContentID);

		if (headingIndex == -1) {
			// create a new heading
			List<ScribeHeading> headings = getHeadingList(map);
			
			ScribeHeading scribeHeading = new ScribeHeading(headings.size());
			scribeHeading.setScribe(scribe);
			scribeHeading.setHeadingText(headingText);

			headings.add(scribeHeading);
		} else {
			// update the existing heading
			ScribeHeading heading = getHeadingList(map).get(headingIndex);
			heading.setHeadingText(headingText);			
		}

		request.setAttribute("sessionMapID", map.getSessionID());
		return mapping.findForward("heading_response");
	}
	
	public ActionForward moveHeading(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		AuthoringForm authForm = (AuthoringForm) form;
		SessionMap<String, Object> map = getSessionMap(request, authForm);
		
		int headingIndex = WebUtil.readIntParam(request, "headingIndex");
		String direction = WebUtil.readStrParam(request, "direction");
		
		ListIterator<ScribeHeading> iter =  getHeadingList(map).listIterator(headingIndex);
		
		ScribeHeading heading = iter.next();
		iter.remove();
		
		// move to correct location
		if (direction.equals("up")) {
			if (iter.hasPrevious())
				iter.previous();			
		} else if (direction.equals("down")) {
			if (iter.hasNext())
				iter.next();
		} else {
			// invalid direction, don't move anywhere.
			log.error("moveHeading: received invalid direction : " + direction);
		}
		
		// adding heading back into list
		iter.add(heading);		
		
		// update the displayOrder
		int i = 0;
		for(ScribeHeading elem : getHeadingList(map)) {
			elem.setDisplayOrder(i);
			i++;
		}
		
		request.setAttribute("sessionMapID", map.getSessionID());
		return mapping.findForward("heading_response");
	}

	public ActionForward deleteHeading(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		AuthoringForm authForm = (AuthoringForm) form;
		SessionMap<String, Object> map = getSessionMap(request, authForm);
		
		Integer headingIndex = authForm.getHeadingIndex();
		
		getHeadingList(map).remove(headingIndex.intValue());		
		
		request.setAttribute("sessionMapID", map.getSessionID());
		return mapping.findForward("heading_response");
				
	}
	
	/* ========== Private Methods ********** */

	private ActionForward uploadFile(ActionMapping mapping,
			AuthoringForm authForm, String type, HttpServletRequest request) {
		SessionMap<String, Object> map = getSessionMap(request, authForm);

		FormFile file;
		List<ScribeAttachment> unsavedFiles;
		List<ScribeAttachment> savedFiles;
		if (StringUtils.equals(IToolContentHandler.TYPE_OFFLINE, type)) {
			file = (FormFile) authForm.getOfflineFile();
			unsavedFiles = getAttList(KEY_UNSAVED_OFFLINE_FILES, map);

			savedFiles = getAttList(KEY_OFFLINE_FILES, map);
		} else {
			file = (FormFile) authForm.getOnlineFile();
			unsavedFiles = getAttList(KEY_UNSAVED_ONLINE_FILES, map);

			savedFiles = getAttList(KEY_ONLINE_FILES, map);
		}

		// upload file to repository
		ScribeAttachment newAtt = scribeService.uploadFileToContent((Long) map
				.get(KEY_TOOL_CONTENT_ID), file, type);

		// Add attachment to unsavedFiles
		// check to see if file with same name exists
		ScribeAttachment currAtt;
		Iterator iter = savedFiles.iterator();
		while (iter.hasNext()) {
			currAtt = (ScribeAttachment) iter.next();
			if (StringUtils.equals(currAtt.getFileName(), newAtt.getFileName())) {
				// move from this this list to deleted list.
				getAttList(KEY_DELETED_FILES, map).add(currAtt);
				iter.remove();
				break;
			}
		}
		unsavedFiles.add(newAtt);

		request.setAttribute(ScribeConstants.ATTR_SESSION_MAP, map);
		request.setAttribute("unsavedChanges", new Boolean(true));

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
			ScribeAttachment att = (ScribeAttachment) iter.next();

			if (att.getFileUuid().equals(authForm.getDeleteFileUuid())) {
				// move to delete file list, deleted at next updateContent
				getAttList(KEY_DELETED_FILES, map).add(att);

				// remove from this list
				iter.remove();
				break;
			}
		}

		request.setAttribute(ScribeConstants.ATTR_SESSION_MAP, map);
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
			ScribeAttachment att = (ScribeAttachment) iter.next();

			if (att.getFileUuid().equals(authForm.getDeleteFileUuid())) {
				// delete from repository and list
				scribeService.deleteFromRepository(att.getFileUuid(), att
						.getFileVersionId());
				iter.remove();
				break;
			}
		}

		request.setAttribute(ScribeConstants.ATTR_SESSION_MAP, map);
		request.setAttribute("unsavedChanges", new Boolean(true));

		return mapping.findForward("success");
	}

	/**
	 * Updates Scribe content using AuthoringForm inputs.
	 * 
	 * @param authForm
	 * @return
	 */
	private void updateScribe(Scribe scribe, AuthoringForm authForm) {
		scribe.setTitle(authForm.getTitle());
		scribe.setInstructions(authForm.getInstructions());
		scribe.setOfflineInstructions(authForm.getOnlineInstruction());
		scribe.setOnlineInstructions(authForm.getOfflineInstruction());
		scribe.setLockOnFinished(authForm.isLockOnFinished());
		scribe.setReflectOnActivity(authForm.isReflectOnActivity());
		scribe.setReflectInstructions(authForm.getReflectInstructions());
		scribe.setAutoSelectScribe(authForm.isAutoSelectScribe());
	}

	/**
	 * Updates AuthoringForm using Scribe content.
	 * 
	 * @param scribe
	 * @param authForm
	 * @return
	 */
	private void updateAuthForm(AuthoringForm authForm, Scribe scribe) {
		authForm.setTitle(scribe.getTitle());
		authForm.setInstructions(scribe.getInstructions());
		authForm.setOnlineInstruction(scribe.getOnlineInstructions());
		authForm.setOfflineInstruction(scribe.getOfflineInstructions());
		authForm.setLockOnFinished(scribe.getLockOnFinished());
		authForm.setReflectOnActivity(scribe.getReflectOnActivity());
		authForm.setReflectInstructions(scribe.getReflectInstructions());
		authForm.setAutoSelectScribe(scribe.getAutoSelectScribe());
	}

	/**
	 * Updates SessionMap using Scribe content.
	 * 
	 * @param scribe
	 * @param mode
	 */
	private SessionMap<String, Object> createSessionMap(Scribe scribe,
			ToolAccessMode mode, String contentFolderID, Long toolContentID) {

		SessionMap<String, Object> map = new SessionMap<String, Object>();

		map.put(KEY_MODE, mode);
		map.put(KEY_CONTENT_FOLDER_ID, contentFolderID);
		map.put(KEY_TOOL_CONTENT_ID, toolContentID);
		map.put(KEY_ONLINE_FILES, new LinkedList<ScribeAttachment>());
		map.put(KEY_OFFLINE_FILES, new LinkedList<ScribeAttachment>());
		map.put(KEY_UNSAVED_ONLINE_FILES, new LinkedList<ScribeAttachment>());
		map.put(KEY_UNSAVED_OFFLINE_FILES, new LinkedList<ScribeAttachment>());
		map.put(KEY_DELETED_FILES, new LinkedList<ScribeAttachment>());
		map.put(KEY_HEADINGS, new LinkedList<ScribeHeading>());

		// adding attachments
		Iterator iter = scribe.getScribeAttachments().iterator();
		while (iter.hasNext()) {
			ScribeAttachment attachment = (ScribeAttachment) iter.next();
			String type = attachment.getFileType();
			if (type.equals(IToolContentHandler.TYPE_OFFLINE)) {
				getAttList(KEY_OFFLINE_FILES, map).add(attachment);
			}
			if (type.equals(IToolContentHandler.TYPE_ONLINE)) {
				getAttList(KEY_ONLINE_FILES, map).add(attachment);
			}
		}
		
		// adding headings
		iter = scribe.getScribeHeadings().iterator();
		while (iter.hasNext()) {
			ScribeHeading element = (ScribeHeading) iter.next();
			getHeadingList(map).add(element);
		}
		
		// sorting headings according to displayOrder.
		Collections.sort(getHeadingList(map));

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
	private List<ScribeAttachment> getAttList(String key,
			SessionMap<String, Object> map) {
		return (List<ScribeAttachment>) map.get(key);
	}

	private List<ScribeHeading> getHeadingList(SessionMap<String, Object> map) {
		return (List<ScribeHeading>) map.get(KEY_HEADINGS);
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

	private Long getToolContentID(SessionMap map) {
		return (Long) map.get(KEY_TOOL_CONTENT_ID);
	}
}