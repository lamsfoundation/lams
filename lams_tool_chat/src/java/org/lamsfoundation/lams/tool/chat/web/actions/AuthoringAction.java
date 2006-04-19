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
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.tool.chat.beans.AuthoringSessionBean;
import org.lamsfoundation.lams.tool.chat.model.Chat;
import org.lamsfoundation.lams.tool.chat.model.ChatAttachment;
import org.lamsfoundation.lams.tool.chat.service.ChatServiceProxy;
import org.lamsfoundation.lams.tool.chat.service.IChatService;
import org.lamsfoundation.lams.tool.chat.util.ChatConstants;
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
 */
public class AuthoringAction extends LamsDispatchAction {

	private static Logger logger = Logger.getLogger(AuthoringAction.class);

	public IChatService chatService;

	/**
	 * Default method when no dispatch parameter is specified. It is expected
	 * that the parameter <code>toolContentID</code> will be passed in. This
	 * will be used to retrieve content for this tool.
	 * 
	 */
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		// set up the form.
		AuthoringForm authForm = (AuthoringForm) form;

		// Extract toolContentID from parameters.
		Long toolContentID = new Long(WebUtil.readLongParam(request,
				AttributeNames.PARAM_TOOL_CONTENT_ID));
		if (logger.isDebugEnabled()) {
			logger.debug("entering method unspecified: toolContentID = "
					+ toolContentID);
		}

		// create a new authoringSessionBean and add to session
		AuthoringSessionBean authSession = new AuthoringSessionBean();
		String id = ChatConstants.AUTH_SESSION_ID
				+ createAuthSessionId(request.getSession());
		authSession.setAuthSessionId(id);
		request.getSession().setAttribute(id, authSession);

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
			chatService.saveOrUpdateContent(chat);
			// TODO NOTE: this causes DB orphans when LD not saved.
		}

		// populating the AuthoringForm using Chat content
		populateAuthForm((AuthoringForm) form, chat);
		resetAuthSession(authSession, chat);
		authForm.setAuthSession(authSession);
		authForm.setAuthSessionId(authSession.getAuthSessionId());
		return mapping.findForward("success");
	}

	private synchronized Long createAuthSessionId(HttpSession httpSession) {
		Long authSessionId = (Long) httpSession
				.getAttribute(ChatConstants.AUTH_SESSION_ID_COUNTER);
		if (authSessionId == null) {
			authSessionId = 1L;
			httpSession.setAttribute(ChatConstants.AUTH_SESSION_ID_COUNTER,
					authSessionId);
		} else {
			authSessionId++;
		}
		return authSessionId;
	}

	public ActionForward updateContent(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// Local History 10:29, started change.
		// TODO need error checking.
		AuthoringForm authForm = (AuthoringForm) form;

		// retrieving authoring session bean
		AuthoringSessionBean authSession = (AuthoringSessionBean) request
				.getSession().getAttribute(authForm.getAuthSessionId());

		// retrieve the content.
		if (chatService == null) {
			chatService = ChatServiceProxy.getChatService(this.getServlet()
					.getServletContext());
		}
		Chat content = chatService.getChatByContentId(authForm
				.getToolContentID());

		// copy form inputs to content
		populateChat(content, authForm);

		// adding unsaved uploaded files.
		Set attachments = content.getChatAttachments();
		if (attachments == null) {
			attachments = new HashSet();
		}
		attachments.addAll(authSession.getUnsavedOnlineFilesList());
		attachments.addAll(authSession.getUnsavedOfflineFilesList());

		// Removing attachments marked for deletion.
		List<ChatAttachment> deletedAttachments = authSession.getDeletedFilesList();
		for(ChatAttachment delAtt:deletedAttachments) {
			// remove from repository
			chatService.deleteFromRepository(delAtt.getFileUuid(), delAtt
					.getFileVersionId());
			
			// remove from ChatAttachments
			Iterator attIter = attachments.iterator();
			while (attIter.hasNext()) {
				ChatAttachment att = (ChatAttachment) attIter.next();
				if (delAtt.getUid().equals(att.getUid())) {
					attIter.remove();
					break;
				}
			}
		}

		// set attachments in case it didnt exist
		content.setChatAttachments(attachments);

		// saving changes.
		content.setUpdateDate(new Date());
		chatService.saveOrUpdateContent(content);

		request.setAttribute("updateContentSuccess", new Boolean(true));

		// update form and return to page.
		resetAuthSession(authSession, content);
		authForm.setAuthSession(authSession);
		return mapping.findForward("success");
	}

	public ActionForward uploadOnline(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return uploadFile(mapping, form, IToolContentHandler.TYPE_ONLINE,
				request);
	}

	public ActionForward uploadOffline(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return uploadFile(mapping, form, IToolContentHandler.TYPE_OFFLINE,
				request);
	}

	public ActionForward deleteOnline(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		AuthoringForm au = (AuthoringForm)form;
		
		Long myLong = au.getDeleteFileUuid();
		
		return deleteFile(mapping, form, IToolContentHandler.TYPE_ONLINE, request);
	}

	public ActionForward deleteOffline(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		return deleteFile(mapping, form, IToolContentHandler.TYPE_OFFLINE, request);
	}

	public ActionForward removeUnsavedOnline(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		return removeUnsaved(mapping, form, IToolContentHandler.TYPE_ONLINE, request);
	}



	public ActionForward removeUnsavedOffline(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		return removeUnsaved(mapping, form, IToolContentHandler.TYPE_OFFLINE, request);
	}

	/* ========== Private Methods ********** */
	
	private ActionForward uploadFile(ActionMapping mapping, ActionForm form,
			String type, HttpServletRequest request) {

		AuthoringForm authForm = (AuthoringForm) form;

		// retrieving authoring session bean
		AuthoringSessionBean authSession = (AuthoringSessionBean) request
				.getSession().getAttribute(authForm.getAuthSessionId());

		// setting up variable to be used.
		FormFile file;
		List<ChatAttachment> unsavedFilesList;
		List<ChatAttachment> savedFilesList;
		if (StringUtils.equals(IToolContentHandler.TYPE_OFFLINE, type)) {
			file = (FormFile) authForm.getOfflineFile();
			unsavedFilesList = authSession.getUnsavedOfflineFilesList();
			savedFilesList = authSession.getOfflineFilesList();
		} else {
			file = (FormFile) authForm.getOnlineFile();
			unsavedFilesList = authSession.getUnsavedOnlineFilesList();
			savedFilesList = authSession.getOnlineFilesList();
		}

		// upload file to repository
		ChatAttachment newAttachment = chatService.uploadFileToContent(authForm
				.getToolContentID(), file, type);

		// Add attachment to unsavedFileList
		// checking to see if file with same name exists
		ChatAttachment currentAttachment;
		Iterator iter = savedFilesList.iterator();
		while (iter.hasNext()) {
			currentAttachment = (ChatAttachment) iter.next();
			if (StringUtils.equals(currentAttachment.getFileName(),
					newAttachment.getFileName())) {
				// move from this this list to deleted list.
				authSession.getDeletedFilesList().add(currentAttachment);
				iter.remove();
				break;
			}
		}
		unsavedFilesList.add(newAttachment);

		authForm.setAuthSession(authSession);
		request.setAttribute("unsavedChanges", new Boolean(true));
		return mapping.findForward("success");
	}

	private ActionForward deleteFile(ActionMapping mapping, ActionForm form, String type, HttpServletRequest request) {
		AuthoringForm authForm = (AuthoringForm) form;

		// retrieving authoring session bean
		AuthoringSessionBean authSession = (AuthoringSessionBean) request
				.getSession().getAttribute(authForm.getAuthSessionId());
		
		List fileList;
		if (StringUtils.equals(IToolContentHandler.TYPE_OFFLINE, type)) {
			fileList = authSession.getOfflineFilesList();
		} else {
			fileList = authSession.getOnlineFilesList();
		}
		
		Iterator iter = fileList.iterator();
		
		while(iter.hasNext()) {
			ChatAttachment att = (ChatAttachment) iter.next();
			
			if (att.getFileUuid().equals(authForm.getDeleteFileUuid())) {
				// move to delete file list, at next updateContent it will be deleted
				authSession.getDeletedFilesList().add(att);
				
				// remove from this list
				iter.remove();
				break;
			}
		}
		
		authForm.setAuthSession(authSession);
		request.setAttribute("unsavedChanges", new Boolean(true));
		return mapping.findForward("success");
	}
	private ActionForward removeUnsaved(ActionMapping mapping, ActionForm form, String type, HttpServletRequest request) {
		AuthoringForm authForm = (AuthoringForm) form;

		// retrieving authoring session bean
		AuthoringSessionBean authSession = (AuthoringSessionBean) request
				.getSession().getAttribute(authForm.getAuthSessionId());
		
		List unsavedAttachments;
		
		if (StringUtils.equals(IToolContentHandler.TYPE_OFFLINE, type)) {
			unsavedAttachments = authSession.getUnsavedOfflineFilesList();
		} else {
			unsavedAttachments = authSession.getUnsavedOnlineFilesList();
		}
		
		Iterator iter = unsavedAttachments.iterator();
		while(iter.hasNext()) {
			ChatAttachment remAtt = (ChatAttachment) iter.next();
			
			if (remAtt.getFileUuid().equals(authForm.getDeleteFileUuid())) {
				// delete from repository
				chatService.deleteFromRepository(remAtt.getFileUuid(), remAtt
						.getFileVersionId());
				
				// remove from session list 
				iter.remove();
				break;
			}			
		}	
		
		authForm.setAuthSession(authSession);
		request.setAttribute("unsavedChanges", new Boolean(true));
		return mapping.findForward("success");
	}
	
	/**
	 * Populates a Chat using inputs in AuthoringForm.
	 * 
	 * @param authForm
	 * @return
	 */
	private void populateChat(Chat chat, AuthoringForm authForm) {
		// Extract data from AuthoringForm
		String title = (String) authForm.getTitle();
		String instructions = (String) authForm.getInstructions();
		String online_instruction = (String) authForm.getOnlineInstruction();
		String offline_instruction = (String) authForm.getOfflineInstruction();
		String value = (String) authForm.getLockOnFinished();
		boolean lock_on_finished = StringUtils.isEmpty(value) ? false : true;

		// update chat object.
		chat.setTitle(title);
		chat.setInstructions(instructions);
		chat.setOfflineInstructions(offline_instruction);
		chat.setOnlineInstructions(online_instruction);
		chat.setLockOnFinished(lock_on_finished);
	}

	/**
	 * Populates the AuthoringForm with content from Chat
	 * 
	 * @param chat
	 * @param authForm
	 * @return
	 */
	private void populateAuthForm(AuthoringForm authForm, Chat chat) {
		authForm.setToolContentID(chat.getToolContentId());
		authForm.setTitle(chat.getTitle());
		authForm.setInstructions(chat.getInstructions());
		authForm.setOnlineInstruction(chat.getOnlineInstructions());
		authForm.setOfflineInstruction(chat.getOfflineInstructions());
		// TODO add the rest.
	}

	private void resetAuthSession(AuthoringSessionBean authSession, Chat chat) {
		// clear the lists in session.
		authSession.getUnsavedOfflineFilesList().clear();
		authSession.getUnsavedOnlineFilesList().clear();
		authSession.getDeletedFilesList().clear();
		authSession.getOnlineFilesList().clear();
		authSession.getOfflineFilesList().clear();

		Iterator iter = chat.getChatAttachments().iterator();
		while (iter.hasNext()) {
			ChatAttachment attachment = (ChatAttachment) iter.next();
			String type = attachment.getFileType();
			if (type.equals(IToolContentHandler.TYPE_OFFLINE)) {
				authSession.getOfflineFilesList().add(attachment);
			}
			if (type.equals(IToolContentHandler.TYPE_ONLINE)) {
				authSession.getOnlineFilesList().add(attachment);
			}
		}
	}
}