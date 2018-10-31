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

package org.lamsfoundation.lams.tool.forum.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import org.lamsfoundation.lams.learningdesign.BranchCondition;
import org.lamsfoundation.lams.tool.OutputFactory;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.forum.ForumConstants;
import org.lamsfoundation.lams.tool.forum.dto.MessageDTO;
import org.lamsfoundation.lams.tool.forum.model.Forum;
import org.lamsfoundation.lams.tool.forum.model.ForumCondition;
import org.lamsfoundation.lams.tool.forum.model.ForumToolSession;
import org.lamsfoundation.lams.tool.forum.model.ForumUser;
import org.lamsfoundation.lams.tool.forum.model.Message;

/**
 * Creates the output definitions for forum. Currently it provides two types of output - an user answers represented by
 * an map "topic creation date" -> "all learner's answers to that topic" (output type "OUTPUT_COMPLEX") and range
 * definition for number of words entered by an user.
 */
public class ForumOutputFactory extends OutputFactory {

    /**
     * @see org.lamsfoundation.lams.tool.OutputDefinitionFactory#getToolOutputDefinitions(java.lang.Object)
     */
    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Object toolContentObject,
	    int definitionType) throws ToolException {

	SortedMap<String, ToolOutputDefinition> definitionMap = new TreeMap<String, ToolOutputDefinition>();

	Class stringArrayClass = String[].class;

	switch (definitionType) {
	    case ToolOutputDefinition.DATA_OUTPUT_DEFINITION_TYPE_CONDITION:
		if (toolContentObject != null) {
		    Class topicDatesToAnswersClass = (new HashMap<Date, Set<String>>()).getClass();
		    ToolOutputDefinition chosenTopicAnswersDefinition = buildComplexOutputDefinition(
			    ForumConstants.TOPIC_DATE_TO_ANSWERS_DEFINITION_NAME, topicDatesToAnswersClass);
		    Forum forum = (Forum) toolContentObject;

		    // adding all existing conditions
		    chosenTopicAnswersDefinition.setConditions(new ArrayList<BranchCondition>(forum.getConditions()));

		    definitionMap.put(ForumConstants.TOPIC_DATE_TO_ANSWERS_DEFINITION_NAME,
			    chosenTopicAnswersDefinition);
		}
		ToolOutputDefinition numberOfPostsDefinition = buildRangeDefinition(
			ForumConstants.LEARNER_NUM_POSTS_DEFINITION_NAME, new Long(0), null);
		definitionMap.put(ForumConstants.LEARNER_NUM_POSTS_DEFINITION_NAME, numberOfPostsDefinition);
		break;
	    case ToolOutputDefinition.DATA_OUTPUT_DEFINITION_TYPE_DATA_FLOW:
		ToolOutputDefinition allUsersAnswersDefinition = buildComplexOutputDefinition(
			ForumConstants.ALL_USERS_ANSWERS_DEFINITION_NAME, stringArrayClass);
		definitionMap.put(ForumConstants.ALL_USERS_ANSWERS_DEFINITION_NAME, allUsersAnswersDefinition);
		break;
	}

	return definitionMap;
    }

    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, IForumService forumService,
	    Long toolSessionId, Long learnerId) {
	TreeMap<String, ToolOutput> outputs = new TreeMap<String, ToolOutput>();
	// tool output cache
	TreeMap<String, ToolOutput> baseOutputs = new TreeMap<String, ToolOutput>();

	if (names == null) {
	    // output will be set for all the existing conditions
	    ForumToolSession session = forumService.getSessionBySessionId(toolSessionId);
	    Forum forum = session.getForum();

	    Set<ForumCondition> conditions = forum.getConditions();
	    for (ForumCondition condition : conditions) {
		String name = condition.getName();
		String[] nameParts = splitConditionName(name);
		if (baseOutputs.get(nameParts[0]) != null) {
		    outputs.put(name, baseOutputs.get(nameParts[0]));
		} else {
		    ToolOutput output = getToolOutput(name, forumService, toolSessionId, learnerId);
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
		    ToolOutput output = getToolOutput(name, forumService, toolSessionId, learnerId);
		    if (output != null) {
			outputs.put(name, output);
			baseOutputs.put(nameParts[0], output);
		    }
		}
	    }
	}

	if (names == null || names.contains(ForumConstants.LEARNER_NUM_POSTS_DEFINITION_NAME)) {
	    outputs.put(ForumConstants.LEARNER_NUM_POSTS_DEFINITION_NAME,
		    getNumPosts(forumService, learnerId, toolSessionId));
	}
	return outputs;

    }

    public ToolOutput getToolOutput(String name, IForumService forumService, Long toolSessionId, Long learnerId) {
	String[] nameParts = splitConditionName(name);
	if (ForumConstants.TOPIC_DATE_TO_ANSWERS_DEFINITION_NAME.equals(nameParts[0])) {
	    // a map of "topic creation date" -> "all learner's answers to that topic"
	    Map<Date, Set<String>> answers = new HashMap<Date, Set<String>>();

	    ForumUser user = forumService.getUserByUserAndSession(learnerId, toolSessionId);
	    List<MessageDTO> userMessages = user == null ? null
		    : forumService.getMessagesByUserUid(user.getUid(), toolSessionId);

	    if (userMessages != null) {
		for (MessageDTO messageDTO : userMessages) {
		    Message message = messageDTO.getMessage();

		    Message parentMessage = message;
		    // we're looking for the top parent i.e. topic
		    while (parentMessage.getParent() != null) {
			parentMessage = parentMessage.getParent();
		    }
		    // topics are recognised by creation date and not by order ID as in other tools
		    Date createdDate = parentMessage.getCreated();
		    Set<String> textAnswers = answers.get(createdDate);
		    if (textAnswers == null) {
			textAnswers = new TreeSet<String>();
		    }
		    textAnswers.add(message.getBody());
		    answers.put(createdDate, textAnswers);
		}
	    }
	    return new ToolOutput(name, getI18NText(ForumConstants.TOPIC_DATE_TO_ANSWERS_DEFINITION_NAME, true),
		    answers, false);
	} else if (ForumConstants.LEARNER_NUM_POSTS_DEFINITION_NAME.equals(nameParts[0])) {
	    return getNumPosts(forumService, learnerId, toolSessionId);
	} else if (ForumConstants.ALL_USERS_ANSWERS_DEFINITION_NAME.equals(nameParts[0])) {
	    List<ForumUser> users = forumService.getUsersBySessionId(toolSessionId);
	    String[] usersAnswers = new String[users.size()];
	    List<MessageDTO> userMessages = null;
	    int userIndex = 0;
	    for (ForumUser user : users) {
		StringBuilder answersBuilder = new StringBuilder();
		userMessages = forumService.getMessagesByUserUid(user.getUid(), toolSessionId);
		if (userMessages != null) {
		    for (MessageDTO messageDTO : userMessages) {
			Message message = messageDTO.getMessage();
			answersBuilder.append(message.getBody()).append(ForumConstants.ANSWERS_SEPARATOR);
		    }
		    usersAnswers[userIndex] = answersBuilder.toString();
		}
		userIndex++;
	    }
	    return new ToolOutput(name, getI18NText(ForumConstants.ALL_USERS_ANSWERS_DEFINITION_NAME, true),
		    usersAnswers, false);

	}
	return null;

    }

    private ToolOutput getNumPosts(IForumService forumService, Long learnerId, Long toolSessionId) {
	int num = forumService.getTopicsNum(learnerId, toolSessionId);
	return new ToolOutput(ForumConstants.LEARNER_NUM_POSTS_DEFINITION_NAME,
		getI18NText(ForumConstants.LEARNER_NUM_POSTS_DEFINITION_NAME, true), new Long(num));
    }

    @Override
    protected String[] splitConditionName(String conditionName) {
	return super.splitConditionName(conditionName);
    }

    protected String buildTopicDatesToAnswersConditionName(String uniquePart) {
	return super.buildConditionName(ForumConstants.TOPIC_DATE_TO_ANSWERS_DEFINITION_NAME, uniquePart);
    }
}