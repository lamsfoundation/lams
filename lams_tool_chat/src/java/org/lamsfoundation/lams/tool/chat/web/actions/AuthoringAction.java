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


package org.lamsfoundation.lams.tool.chat.web.actions;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.authoring.web.AuthoringConstants;
import org.lamsfoundation.lams.learningdesign.TextSearchConditionComparator;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.chat.model.Chat;
import org.lamsfoundation.lams.tool.chat.model.ChatCondition;
import org.lamsfoundation.lams.tool.chat.service.ChatServiceProxy;
import org.lamsfoundation.lams.tool.chat.service.IChatService;
import org.lamsfoundation.lams.tool.chat.util.ChatConstants;
import org.lamsfoundation.lams.tool.chat.web.forms.AuthoringForm;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 */
public class AuthoringAction extends LamsDispatchAction {

    private static Logger logger = Logger.getLogger(AuthoringAction.class);

    public IChatService chatService;

    // Authoring SessionMap key names
    private static final String KEY_TOOL_CONTENT_ID = "toolContentID";

    private static final String KEY_CONTENT_FOLDER_ID = "contentFolderID";

    private static final String KEY_MODE = "mode";

    /**
     * Default method when no dispatch parameter is specified. It is expected that the parameter
     * <code>toolContentID</code> will be passed in. This will be used to retrieve content for this tool.
     */
    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	// Extract toolContentID from parameters.
	Long toolContentID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);

	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);

	// set up chatService
	if (chatService == null) {
	    chatService = ChatServiceProxy.getChatService(this.getServlet().getServletContext());
	}

	// retrieving Chat with given toolContentID
	Chat chat = chatService.getChatByContentId(toolContentID);
	if (chat == null) {
	    chat = chatService.copyDefaultContent(toolContentID);
	    chat.setCreateDate(new Date());
	    chatService.saveOrUpdateChat(chat);
	    // TODO NOTE: this causes DB orphans when LD not saved.
	}

	if (mode.isTeacher()) {
	    // Set the defineLater flag so that learners cannot use content
	    // while we
	    // are editing. This flag is released when updateContent is called.
	    chat.setDefineLater(true);
	    chatService.saveOrUpdateChat(chat);
	    
	    //audit log the teacher has started editing activity in monitor
	    chatService.auditLogStartEditingActivityInMonitor(toolContentID);
	}

	// Set up the authForm.
	AuthoringForm authForm = (AuthoringForm) form;
	updateAuthForm(authForm, chat);

	// Set up sessionMap
	SessionMap<String, Object> map = createSessionMap(chat, mode, contentFolderID, toolContentID);
	authForm.setSessionMapID(map.getSessionID());

	// add the sessionMap to HTTPSession.
	request.getSession().setAttribute(map.getSessionID(), map);
	request.setAttribute(ChatConstants.ATTR_SESSION_MAP, map);

	return mapping.findForward("success");
    }

    public ActionForward updateContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	// TODO need error checking.

	// get authForm and session map.
	AuthoringForm authForm = (AuthoringForm) form;
	SessionMap<String, Object> map = getSessionMap(request, authForm);

	// get chat content.
	Chat chat = chatService.getChatByContentId((Long) map.get(AuthoringAction.KEY_TOOL_CONTENT_ID));

	// update chat content using form inputs
	updateChat(chat, authForm);

	chatService.releaseConditionsFromCache(chat);

	Set<ChatCondition> conditions = chat.getConditions();
	if (conditions == null) {
	    conditions = new TreeSet<ChatCondition>(new TextSearchConditionComparator());
	}
	SortedSet<ChatCondition> conditionSet = (SortedSet<ChatCondition>) map.get(ChatConstants.ATTR_CONDITION_SET);
	conditions.addAll(conditionSet);

	List<ChatCondition> deletedConditionList = (List<ChatCondition>) map
		.get(ChatConstants.ATTR_DELETED_CONDITION_LIST);
	if (deletedConditionList != null) {
	    for (ChatCondition condition : deletedConditionList) {
		// remove from db, leave in repository
		conditions.remove(condition);
		chatService.deleteCondition(condition);
	    }
	}

	// set conditions in case it didn't exist
	chat.setConditions(conditionSet);
	// set the update date
	chat.setUpdateDate(new Date());

	// releasing defineLater flag so that learners can start using the tool
	chat.setDefineLater(false);

	chatService.saveOrUpdateChat(chat);

	request.setAttribute(AuthoringConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);

	// add the sessionMapID to form
	authForm.setSessionMapID(map.getSessionID());

	request.setAttribute(ChatConstants.ATTR_SESSION_MAP, map);

	return mapping.findForward("success");
    }

    /* ========== Private Methods ********** */

    /**
     * Updates Chat content using AuthoringForm inputs.
     *
     * @param authForm
     * @param mode
     * @return
     */
    private void updateChat(Chat chat, AuthoringForm authForm) {
	chat.setTitle(authForm.getTitle());
	chat.setInstructions(authForm.getInstructions());
	chat.setLockOnFinished(authForm.isLockOnFinished());
	chat.setReflectOnActivity(authForm.isReflectOnActivity());
	chat.setReflectInstructions(authForm.getReflectInstructions());
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
	authForm.setTitle(chat.getTitle());
	authForm.setInstructions(chat.getInstructions());
	authForm.setLockOnFinished(chat.isLockOnFinished());
	authForm.setReflectOnActivity(chat.isReflectOnActivity());
	authForm.setReflectInstructions(chat.getReflectInstructions());
	authForm.setFilteringEnabled(chat.isFilteringEnabled());
	authForm.setFilterKeywords(chat.getFilterKeywords());
    }

    /**
     * Updates SessionMap using Chat content.
     *
     * @param chat
     * @param mode
     */
    private SessionMap<String, Object> createSessionMap(Chat chat, ToolAccessMode mode, String contentFolderID,
	    Long toolContentID) {

	SessionMap<String, Object> sessionMap = new SessionMap<String, Object>();

	sessionMap.put(AuthoringAction.KEY_MODE, mode);
	sessionMap.put(AuthoringAction.KEY_CONTENT_FOLDER_ID, contentFolderID);
	sessionMap.put(AuthoringAction.KEY_TOOL_CONTENT_ID, toolContentID);
	
	SortedSet<ChatCondition> set = new TreeSet<ChatCondition>(new TextSearchConditionComparator());
	if (chat.getConditions() != null) {
	    set.addAll(chat.getConditions());
	}
	sessionMap.put(ChatConstants.ATTR_CONDITION_SET, set);
	return sessionMap;
    }

    /**
     * Retrieve the SessionMap from the HttpSession.
     *
     * @param request
     * @param authForm
     * @return
     */
    private SessionMap<String, Object> getSessionMap(HttpServletRequest request, AuthoringForm authForm) {
	return (SessionMap<String, Object>) request.getSession().getAttribute(authForm.getSessionMapID());
    }
}