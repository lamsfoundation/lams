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
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Object toolContentObject,
	    int definitionType) throws ToolException {
	SortedMap<String, ToolOutputDefinition> definitionMap = new TreeMap<String, ToolOutputDefinition>();
	Class stringArrayClass = new String[] {}.getClass();
	switch (definitionType) {
	    case ToolOutputDefinition.DATA_OUTPUT_DEFINITION_TYPE_CONDITION:
		if (toolContentObject != null) {
		    ToolOutputDefinition chatMessagesDefinition = buildComplexOutputDefinition(
			    ChatConstants.USER_MESSAGES_DEFINITION_NAME, stringArrayClass);
		    Chat chat = (Chat) toolContentObject;
		    // adding all existing conditions
		    chatMessagesDefinition.setConditions(new ArrayList<BranchCondition>(chat.getConditions()));
		    definitionMap.put(ChatConstants.USER_MESSAGES_DEFINITION_NAME, chatMessagesDefinition);
		}
		ToolOutputDefinition numberOfPostsDefinition = buildRangeDefinition(
			ChatConstants.LEARNER_NUM_POSTS_DEFINITION_NAME, new Long(0), null);
		definitionMap.put(ChatConstants.LEARNER_NUM_POSTS_DEFINITION_NAME, numberOfPostsDefinition);
		break;
	    case ToolOutputDefinition.DATA_OUTPUT_DEFINITION_TYPE_DATA_FLOW:
		ToolOutputDefinition allUsersMessagesDefinition = buildComplexOutputDefinition(
			ChatConstants.ALL_USERS_MESSAGES_DEFINITION_NAME, stringArrayClass);
		definitionMap.put(ChatConstants.ALL_USERS_MESSAGES_DEFINITION_NAME, allUsersMessagesDefinition);
		break;
	}

	return definitionMap;
    }

    /**
     * Follows {@link ChatService#getToolOutput(List, Long, Long)}.
     *
     */
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, IChatService chatService, Long toolSessionId,
	    Long learnerId) {

	TreeMap<String, ToolOutput> outputs = new TreeMap<String, ToolOutput>();
	// tool output cache
	TreeMap<String, ToolOutput> baseOutputs = new TreeMap<String, ToolOutput>();

	if (names == null) {
	    // output will be set for all the existing conditions
	    Chat chat = chatService.getSessionBySessionId(toolSessionId).getChat();
	    Set<ChatCondition> conditions = chat.getConditions();
	    for (ChatCondition condition : conditions) {
		String name = condition.getName();
		String[] nameParts = splitConditionName(name);
		if (baseOutputs.get(nameParts[0]) != null) {
		    outputs.put(name, baseOutputs.get(nameParts[0]));
		} else {
		    ToolOutput output = getToolOutput(name, chatService, toolSessionId, learnerId);
		    if (output != null) {
			outputs.put(name, output);
			baseOutputs.put(nameParts[0], output);
		    }
		}
	    }
	} else {
	    for (String name : names) {
		String[] nameParts = splitConditionName(name);
		if (baseOutputs.get(nameParts[0]) != null) {
		    outputs.put(name, baseOutputs.get(nameParts[0]));
		} else {
		    ToolOutput output = getToolOutput(name, chatService, toolSessionId, learnerId);
		    if (output != null) {
			outputs.put(name, output);
			baseOutputs.put(nameParts[0], output);
		    }
		}
	    }
	}
	if ((names == null) || names.contains(ChatConstants.LEARNER_NUM_POSTS_DEFINITION_NAME)) {
	    outputs.put(ChatConstants.LEARNER_NUM_POSTS_DEFINITION_NAME,
		    getNumPosts(chatService, learnerId, toolSessionId));
	}
	return outputs;

    }

    public ToolOutput getToolOutput(String name, IChatService chatService, Long toolSessionId, Long learnerId) {
	String[] nameParts = splitConditionName(name);
	if (ChatConstants.USER_MESSAGES_DEFINITION_NAME.equals(nameParts[0])) {
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

	    return new ToolOutput(name, getI18NText(ChatConstants.USER_MESSAGES_DEFINITION_NAME, true), textMessages,
		    false);
	} else if (ChatConstants.LEARNER_NUM_POSTS_DEFINITION_NAME.equals(nameParts[0])) {
	    return getNumPosts(chatService, learnerId, toolSessionId);
	} else if (ChatConstants.ALL_USERS_MESSAGES_DEFINITION_NAME.equals(nameParts[0])) {
	    Set<ChatUser> users = chatService.getSessionBySessionId(toolSessionId).getChatUsers();
	    String[] usersMessages = new String[users.size()];
	    int userIndex = 0;
	    for (ChatUser user : users) {
		List<ChatMessage> messages = chatService.getMessagesSentByUser(user.getUid());

		if (messages != null) {
		    StringBuilder messagesBuilder = new StringBuilder();
		    for (ChatMessage message : messages) {
			messagesBuilder.append(message.getBody()).append(ChatConstants.MESSAGE_SEPARATOR);
		    }
		    usersMessages[userIndex] = messagesBuilder.toString();
		}
		userIndex++;
	    }
	    return new ToolOutput(name, getI18NText(ChatConstants.ALL_USERS_MESSAGES_DEFINITION_NAME, true),
		    usersMessages, false);
	}
	return null;
    }

    private ToolOutput getNumPosts(IChatService chatService, Long learnerId, Long toolSessionId) {
	int num = chatService.getTopicsNum(learnerId, toolSessionId);
	return new ToolOutput(ChatConstants.LEARNER_NUM_POSTS_DEFINITION_NAME,
		getI18NText(ChatConstants.LEARNER_NUM_POSTS_DEFINITION_NAME, true), new Long(num));
    }

    @Override
    protected String[] splitConditionName(String conditionName) {
	return super.splitConditionName(conditionName);
    }

    protected String buildUserMessagesConditionName(String uniquePart) {
	return super.buildConditionName(ChatConstants.USER_MESSAGES_DEFINITION_NAME, uniquePart);
    }
}