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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */

package org.lamsfoundation.lams.tool.chat.web.actions;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.authoring.web.AuthoringConstants;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.chat.model.Chat;
import org.lamsfoundation.lams.tool.chat.model.ChatAttachment;
import org.lamsfoundation.lams.tool.chat.service.ChatServiceProxy;
import org.lamsfoundation.lams.tool.chat.service.IChatService;
import org.lamsfoundation.lams.tool.chat.util.ChatConstants;
import org.lamsfoundation.lams.tool.chat.util.SessionMap;
import org.lamsfoundation.lams.tool.chat.web.forms.AuthoringForm;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.AttributeNames;

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

	public IChatService chatService;

	// Authoring SessionMap key names
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

		// set up chatService
		if (chatService == null) {
			chatService = ChatServiceProxy.getChatService(this.getServlet()
					.getServletContext());
		}

		// retrieving Chat with given toolContentID
		Chat chat = chatService.getChatByContentId(toolContentID);
		if (chat == null) {
			chat = chatService.copyDefaultContent(toolContentID);
			chat.setCreateDate(new Date());
			chatService.saveOrUpdateChat(chat);
			// TODO NOTE: this causes DB orphans when LD not saved.
		}

		// check if content in use is set
		if (chat.getContentInUse()) {
			// Cannot edit, send to message page.
			request.setAttribute(ChatConstants.ATTR_MESSAGE, getResources(
					request).getMessage("error.content.locked"));
			return mapping.findForward("message_page");
		}

		// Set the defineLater flag so that learners cannot use content while we
		// are editing. This flag is released when updateContent is called.
		chat.setDefineLater(true);
		chatService.saveOrUpdateChat(chat);

		// Set up sessionMap
		SessionMap map = new SessionMap();
		initSessionMap(map, request);
		updateSessionMap(map, chat);

		// Set up the authForm.
		AuthoringForm authForm = (AuthoringForm) form;
		updateAuthForm(authForm, chat);

		// add the sessionMapID to form
		authForm.setSessionMapID(map.getSessionID());

		// add the sessionMap to HTTPSession.
		request.getSession().setAttribute(map.getSessionID(), map);

		// add the sessionMap to the HttpServletRequest
		// TODO workaround until we can figure out how to get request
		// attributes using dynamic attributes in jsps.
		request.setAttribute(ChatConstants.ATTR_SESSION_MAP, map);

		return mapping.findForward("success");
	}

	public ActionForward updateContent(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO need error checking.

		// get authForm and session map.
		AuthoringForm authForm = (AuthoringForm) form;
		SessionMap map = getSessionMap(request, authForm);

		// get chat content.
		Chat chat = chatService.getChatByContentId(authForm.getToolContentID());

		// update chat content using form inputs.
		updateChat(chat, authForm);

		// remove attachments marked for deletion.
		Set attachments = chat.getChatAttachments();
		if (attachments == null) {
			attachments = new HashSet();
		}

		for (ChatAttachment att : getAttList(KEY_DELETED_FILES, map)) {
			// remove from repository and db
			chatService.deleteFromRepository(att.getFileUuid(), att
					.getFileVersionId());
			attachments.remove(att);
		}
		
		// add unsaved attachments
		attachments.addAll(getAttList(KEY_UNSAVED_ONLINE_FILES, map));
		attachments.addAll(getAttList(KEY_UNSAVED_OFFLINE_FILES, map));
		
		// set attachments in case it didn't exist
		chat.setChatAttachments(attachments);

		// set the update date
		chat.setUpdateDate(new Date());

		// releasing defineLater flag so that learner can start using the tool.
		chat.setDefineLater(false);

		chatService.saveOrUpdateChat(chat);

		request.setAttribute(AuthoringConstants.LAMS_AUTHORING_SUCCESS_FLAG,
				Boolean.TRUE);

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
		SessionMap map = getSessionMap(request, authForm);

		FormFile file;
		List<ChatAttachment> unsavedFiles;
		List<ChatAttachment> savedFiles;
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
		ChatAttachment newAtt = chatService.uploadFileToContent(authForm
				.getToolContentID(), file, type);

		// Add attachment to unsavedFiles
		// check to see if file with same name exists
		ChatAttachment currAtt;
		Iterator iter = savedFiles.iterator();
		while (iter.hasNext()) {
			currAtt = (ChatAttachment) iter.next();
			if (StringUtils.equals(currAtt.getFileName(), newAtt.getFileName())) {
				// move from this this list to deleted list.
				getAttList(KEY_DELETED_FILES, map).add(currAtt);
				iter.remove();
				break;
			}
		}
		unsavedFiles.add(newAtt);

		request.setAttribute(ChatConstants.ATTR_SESSION_MAP, map);
		request.setAttribute("unsavedChanges", new Boolean(true));

		return mapping.findForward("success");
	}

	private ActionForward deleteFile(ActionMapping mapping,
			AuthoringForm authForm, String type, HttpServletRequest request) {
		SessionMap map = getSessionMap(request, authForm);

		List fileList;
		if (StringUtils.equals(IToolContentHandler.TYPE_OFFLINE, type)) {
			fileList = getAttList(KEY_OFFLINE_FILES, map);
		} else {
			fileList = getAttList(KEY_ONLINE_FILES, map);
		}

		Iterator iter = fileList.iterator();

		while (iter.hasNext()) {
			ChatAttachment att = (ChatAttachment) iter.next();

			if (att.getFileUuid().equals(authForm.getDeleteFileUuid())) {
				// move to delete file list, deleted at next updateContent
				getAttList(KEY_DELETED_FILES, map).add(att);

				// remove from this list
				iter.remove();
				break;
			}
		}

		request.setAttribute(ChatConstants.ATTR_SESSION_MAP, map);
		request.setAttribute("unsavedChanges", new Boolean(true));

		return mapping.findForward("success");
	}

	private ActionForward removeUnsaved(ActionMapping mapping,
			AuthoringForm authForm, String type, HttpServletRequest request) {
		SessionMap map = getSessionMap(request, authForm);

		List unsavedFiles;

		if (StringUtils.equals(IToolContentHandler.TYPE_OFFLINE, type)) {
			unsavedFiles = getAttList(KEY_UNSAVED_OFFLINE_FILES, map);
		} else {
			unsavedFiles = getAttList(KEY_UNSAVED_ONLINE_FILES, map);
		}

		Iterator iter = unsavedFiles.iterator();
		while (iter.hasNext()) {
			ChatAttachment att = (ChatAttachment) iter.next();

			if (att.getFileUuid().equals(authForm.getDeleteFileUuid())) {
				// delete from repository and list
				chatService.deleteFromRepository(att.getFileUuid(), att
						.getFileVersionId());
				iter.remove();
				break;
			}
		}

		request.setAttribute(ChatConstants.ATTR_SESSION_MAP, map);
		request.setAttribute("unsavedChanges", new Boolean(true));

		return mapping.findForward("success");
	}

	/**
	 * Updates Chat content using AuthoringForm inputs.
	 * 
	 * @param authForm
	 * @return
	 */
	private void updateChat(Chat chat, AuthoringForm authForm) {
		chat.setTitle(authForm.getTitle());
		chat.setInstructions(authForm.getInstructions());
		chat.setOfflineInstructions(authForm.getOnlineInstruction());
		chat.setOnlineInstructions(authForm.getOfflineInstruction());
		chat.setLockOnFinished(authForm.isLockOnFinished());
		chat.setFilteringEnabled(authForm.isFilteringEnabled());
		chat.setFilterKeywords(authForm.getFilterKeywords());
	}

	/**
	 * Updates AuthoringForm using Chat content.
	 * 
	 * @param chat
	 * @param authForm
	 * @return
	 */
	private void updateAuthForm(AuthoringForm authForm, Chat chat) {
		authForm.setToolContentID(chat.getToolContentId());
		authForm.setTitle(chat.getTitle());
		authForm.setInstructions(chat.getInstructions());
		authForm.setOnlineInstruction(chat.getOnlineInstructions());
		authForm.setOfflineInstruction(chat.getOfflineInstructions());
		authForm.setLockOnFinished(chat.getLockOnFinished());
		authForm.setFilteringEnabled(chat.getFilteringEnabled());
		authForm.setFilterKeywords(chat.getFilterKeywords());
		// TODO add the rest.
	}

	/**
	 * Updates SessionMap using Chat content.
	 * 
	 * @param map
	 * @param chat
	 */
	private void updateSessionMap(SessionMap map, Chat chat) {

		getAttList(KEY_UNSAVED_OFFLINE_FILES, map).clear();
		getAttList(KEY_UNSAVED_ONLINE_FILES, map).clear();
		getAttList(KEY_DELETED_FILES, map).clear();
		getAttList(KEY_OFFLINE_FILES, map).clear();
		getAttList(KEY_ONLINE_FILES, map).clear();

		Iterator iter = chat.getChatAttachments().iterator();
		while (iter.hasNext()) {
			ChatAttachment attachment = (ChatAttachment) iter.next();
			String type = attachment.getFileType();
			if (type.equals(IToolContentHandler.TYPE_OFFLINE)) {
				getAttList(KEY_OFFLINE_FILES, map).add(attachment);
			}
			if (type.equals(IToolContentHandler.TYPE_ONLINE)) {
				getAttList(KEY_ONLINE_FILES, map).add(attachment);
			}
		}
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
	 * Set up SessionMap for first use. Creates empty lists and sets the access
	 * mode.
	 * 
	 * @param map
	 * @param request
	 */
	private void initSessionMap(SessionMap map, HttpServletRequest request) {
		map.put(KEY_MODE, getAccessMode(request));
		map.put(KEY_ONLINE_FILES, new LinkedList<ChatAttachment>());
		map.put(KEY_OFFLINE_FILES, new LinkedList<ChatAttachment>());
		map.put(KEY_UNSAVED_ONLINE_FILES, new LinkedList<ChatAttachment>());
		map.put(KEY_UNSAVED_OFFLINE_FILES, new LinkedList<ChatAttachment>());
		map.put(KEY_DELETED_FILES, new LinkedList<ChatAttachment>());
	}

	/**
	 * Retrieves a List of attachments from the map using the key.
	 * 
	 * @param key
	 * @param map
	 * @return
	 */
	private List<ChatAttachment> getAttList(String key, SessionMap map) {
		List<ChatAttachment> list = (List<ChatAttachment>) map.get(key);
		return list;
	}

	/**
	 * Retrieve the SessionMap from the HttpSession.
	 * 
	 * @param request
	 * @param authForm
	 * @return
	 */
	private SessionMap getSessionMap(HttpServletRequest request,
			AuthoringForm authForm) {
		return (SessionMap) request.getSession().getAttribute(
				authForm.getSessionMapID());
	}
}