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

package org.lamsfoundation.lams.tool.chat.web.controller;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.lamsfoundation.lams.learningdesign.TextSearchConditionComparator;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.chat.model.Chat;
import org.lamsfoundation.lams.tool.chat.model.ChatCondition;
import org.lamsfoundation.lams.tool.chat.service.IChatService;
import org.lamsfoundation.lams.tool.chat.util.ChatConstants;
import org.lamsfoundation.lams.tool.chat.web.forms.AuthoringForm;
import org.lamsfoundation.lams.util.CommonConstants;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/authoring")
public class AuthoringController {

    @Autowired
    private IChatService chatService;

    @Autowired
    @Qualifier("chatMessageService")
    private MessageService messageService;

    // Authoring SessionMap key names
    private static final String KEY_TOOL_CONTENT_ID = "toolContentID";

    private static final String KEY_CONTENT_FOLDER_ID = "contentFolderID";

    private static final String KEY_MODE = "mode";

    /**
     * Default method when no dispatch parameter is specified. It is expected that the parameter
     * <code>toolContentID</code> will be passed in. This will be used to retrieve content for this tool.
     */
    @RequestMapping("/authoring")
    public String unspecified(@ModelAttribute AuthoringForm authoringForm, HttpServletRequest request) {
	// Extract toolContentID from parameters.
	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);

	// retrieving Chat with given toolContentID
	Chat chat = chatService.getChatByContentId(toolContentID);
	if (chat == null) {
	    chat = chatService.copyDefaultContent(toolContentID);
	    chat.setCreateDate(new Date());
	    chatService.saveOrUpdateChat(chat);
	    // TODO NOTE: this causes DB orphans when LD not saved.
	}

	return readDatabaseData(authoringForm, chat, request, mode);
    }

    /**
     * Set the defineLater flag so that learners cannot use content while we are editing. This flag is released when
     * updateContent is called.
     */
    @RequestMapping(path = "/definelater", method = RequestMethod.POST)
    public String definelater(@ModelAttribute AuthoringForm authoringForm, HttpServletRequest request) {
	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Chat chat = chatService.getChatByContentId(toolContentID);
	chat.setDefineLater(true);
	chatService.saveOrUpdateChat(chat);

	//audit log the teacher has started editing activity in monitor
	chatService.auditLogStartEditingActivityInMonitor(toolContentID);

	return readDatabaseData(authoringForm, chat, request, ToolAccessMode.TEACHER);
    }
    
    /**
     * Common method for "unspecified" and "defineLater"
     */
    private String readDatabaseData(AuthoringForm authoringForm, Chat chat, HttpServletRequest request, ToolAccessMode mode) {
	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	
	// Set up the authForm.
	updateAuthForm(authoringForm, chat);

	// Set up sessionMap
	SessionMap<String, Object> map = createSessionMap(chat, mode, contentFolderID, toolContentID);
	authoringForm.setSessionMapID(map.getSessionID());

	// add the sessionMap to HTTPSession.
	request.getSession().setAttribute(map.getSessionID(), map);
	request.setAttribute(ChatConstants.ATTR_SESSION_MAP, map);

	return "pages/authoring/authoring";
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(path = "/updateContent", method = RequestMethod.POST)
    public String updateContent(@ModelAttribute AuthoringForm authoringForm, HttpServletRequest request) {
	// TODO need error checking.

	// get session map.
	SessionMap<String, Object> map = getSessionMap(request, authoringForm);

	// get chat content.
	Chat chat = chatService.getChatByContentId((Long) map.get(AuthoringController.KEY_TOOL_CONTENT_ID));

	// update chat content using form inputs
	updateChat(chat, authoringForm);

	chatService.releaseConditionsFromCache(chat);

	Set<ChatCondition> conditions = chat.getConditions();
	if (conditions == null) {
	    conditions = new TreeSet<>(new TextSearchConditionComparator());
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

	request.setAttribute(CommonConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);

	// add the sessionMapID to form
	authoringForm.setSessionMapID(map.getSessionID());

	request.setAttribute(ChatConstants.ATTR_SESSION_MAP, map);

	return "pages/authoring/authoring";
    }

    /* ========== Private Methods ********** */

    /**
     * Updates Chat content using AuthoringForm inputs.
     */
    private void updateChat(Chat chat, AuthoringForm authoringForm) {
	chat.setTitle(authoringForm.getTitle());
	chat.setInstructions(authoringForm.getInstructions());
	chat.setLockOnFinished(authoringForm.isLockOnFinished());
	chat.setFilteringEnabled(authoringForm.isFilteringEnabled());
	chat.setFilterKeywords(authoringForm.getFilterKeywords());
    }

    /**
     * Updates AuthoringForm using Chat content.
     */
    private void updateAuthForm(AuthoringForm authoringForm, Chat chat) {
	authoringForm.setTitle(chat.getTitle());
	authoringForm.setInstructions(chat.getInstructions());
	authoringForm.setLockOnFinished(chat.isLockOnFinished());
	authoringForm.setFilteringEnabled(chat.isFilteringEnabled());
	authoringForm.setFilterKeywords(chat.getFilterKeywords());
    }

    /**
     * Updates SessionMap using Chat content.
     */
    private SessionMap<String, Object> createSessionMap(Chat chat, ToolAccessMode mode, String contentFolderID,
	    Long toolContentID) {

	SessionMap<String, Object> sessionMap = new SessionMap<>();

	sessionMap.put(AuthoringController.KEY_MODE, mode);
	sessionMap.put(AuthoringController.KEY_CONTENT_FOLDER_ID, contentFolderID);
	sessionMap.put(AuthoringController.KEY_TOOL_CONTENT_ID, toolContentID);

	SortedSet<ChatCondition> set = new TreeSet<>(new TextSearchConditionComparator());
	if (chat.getConditions() != null) {
	    set.addAll(chat.getConditions());
	}
	sessionMap.put(ChatConstants.ATTR_CONDITION_SET, set);
	return sessionMap;
    }

    /**
     * Retrieve the SessionMap from the HttpSession.
     */
    @SuppressWarnings("unchecked")
    private SessionMap<String, Object> getSessionMap(HttpServletRequest request, AuthoringForm authoringForm) {
	return (SessionMap<String, Object>) request.getSession().getAttribute(authoringForm.getSessionMapID());
    }
}