/**************************************************************** 
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org) 
 * ============================================================= 
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/ 
 * 
 * This program is free software; you can redistribute it and/or modify 
 * it under the terms of the GNU General Public License version 2.0 
 * as published by the Free Software Foundation. 
 * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU General Public License for more details. 
 * 
 * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA 
 * 
 * http://www.gnu.org/licenses/gpl.txt 
 * **************************************************************** 
 */

/* $Id$ */
package org.lamsfoundation.lams.tool.chat.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.lamsfoundation.lams.learningdesign.BranchCondition;
import org.lamsfoundation.lams.tool.OutputFactory;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.chat.model.Chat;
import org.lamsfoundation.lams.tool.chat.model.ChatCondition;
import org.lamsfoundation.lams.tool.chat.model.ChatMessage;
import org.lamsfoundation.lams.tool.chat.model.ChatUser;
import org.lamsfoundation.lams.tool.chat.util.ChatConstants;
import org.lamsfoundation.lams.tool.exception.ToolException;

/**
 * Output factory for Chat tool. Currently it provides only one type of output - all messages that a user sent.
 * 
 * @author Marcin Cieslak
 */
public class ChatOutputFactory extends OutputFactory {

    /**
     * {@inheritDoc}
     */
    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Object toolContentObject)
	    throws ToolException {
	SortedMap<String, ToolOutputDefinition> definitionMap = new TreeMap<String, ToolOutputDefinition>();
	if (toolContentObject != null) {
	    ToolOutputDefinition chatMessagesDefinition = buildComplexOutputDefinition(ChatConstants.TEXT_SEARCH_DEFINITION_NAME);
	    Chat chat = (Chat) toolContentObject;
	    // adding all existing conditions
	    chatMessagesDefinition.setDefaultConditions(new ArrayList<BranchCondition>(chat.getConditions()));
	    // if no conditions were created in the tool instance, a default condition is added; the condition is
	    // persisted in ChatService.
	    if (chatMessagesDefinition.getDefaultConditions().isEmpty()) {
		String name = buildConditionName(ChatConstants.TEXT_SEARCH_DEFINITION_NAME, chat.getToolContentId()
			.toString());
		// Default condition checks if messages contain word "LAMS"
		ChatCondition defaultCondition = new ChatCondition(null, null, 1, name, getI18NText(
			ChatConstants.TEXT_SEARCH_DEFAULT_CONDITION_DISPLAY_NAME_KEY, false), "LAMS", null, null, null);
		chat.getConditions().add(defaultCondition);
		chatMessagesDefinition.getDefaultConditions().add(defaultCondition);
	    }
	    chatMessagesDefinition.setShowConditionNameOnly(true);
	    definitionMap.put(ChatConstants.TEXT_SEARCH_DEFINITION_NAME, chatMessagesDefinition);
	}

	return definitionMap;
    }

    /**
     * Follows {@link ChatService#getToolOutput(List, Long, Long)}.
     * 
     */
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, IChatService chatService,
	    Long toolSessionId, Long learnerId) {

	TreeMap<String, ToolOutput> outputs = new TreeMap<String, ToolOutput>();
	// cached tool output for all text search conditions
	ToolOutput chatMessagesOutput = null;
	if (names == null) {
	    // output will be set for all the existing conditions
	    Chat chat = chatService.getSessionBySessionId(toolSessionId).getChat();
	    Set<ChatCondition> conditions = chat.getConditions();
	    for (ChatCondition condition : conditions) {
		String name = condition.getName();
		if (isTextSearchConditionName(name) && chatMessagesOutput != null) {
		    outputs.put(name, chatMessagesOutput);
		} else {
		    ToolOutput output = getToolOutput(name, chatService, toolSessionId, learnerId);
		    if (output != null) {
			outputs.put(name, output);
			if (isTextSearchConditionName(name)) {
			    chatMessagesOutput = output;
			}
		    }
		}
	    }
	} else {
	    for (String name : names) {
		if (isTextSearchConditionName(name) && chatMessagesOutput != null) {
		    outputs.put(name, chatMessagesOutput);
		} else {
		    ToolOutput output = getToolOutput(name, chatService, toolSessionId, learnerId);
		    if (output != null) {
			outputs.put(name, output);
			if (isTextSearchConditionName(name)) {
			    chatMessagesOutput = output;
			}
		    }
		}
	    }
	}
	return outputs;

    }

    public ToolOutput getToolOutput(String name, IChatService chatService, Long toolSessionId, Long learnerId) {
	if (isTextSearchConditionName(name)) {
	    // entry is loaded from DB
	    Chat chat = chatService.getSessionBySessionId(toolSessionId).getChat();

	    ChatUser user = chatService.getUserByUserIdAndSessionId(learnerId, toolSessionId);
	    List<ChatMessage> messages = chatService.getMessagesSentByUser(user.getUid());

	    String[] textMessages = null;
	    if (messages != null) {
		textMessages = new String[messages.size()];
		int messageIndex = 0;
		for (ChatMessage message : messages) {
		    textMessages[messageIndex++] = message.getBody();
		}
	    }

	    return new ToolOutput(name, getI18NText(ChatConstants.TEXT_SEARCH_DEFINITION_NAME, true), textMessages,
		    false);
	}
	return null;
    }

    @Override
    protected String[] splitConditionName(String conditionName) {
	return super.splitConditionName(conditionName);
    }

    protected String buildConditionName(String uniquePart) {
	return super.buildConditionName(ChatConstants.TEXT_SEARCH_DEFINITION_NAME, uniquePart);
    }

    private boolean isTextSearchConditionName(String name) {
	return name != null && name.startsWith(ChatConstants.TEXT_SEARCH_DEFINITION_NAME);
    }
}