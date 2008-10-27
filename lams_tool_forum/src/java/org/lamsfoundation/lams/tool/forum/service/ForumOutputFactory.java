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
package org.lamsfoundation.lams.tool.forum.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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
import org.lamsfoundation.lams.tool.forum.dto.MessageDTO;
import org.lamsfoundation.lams.tool.forum.persistence.Forum;
import org.lamsfoundation.lams.tool.forum.persistence.ForumCondition;
import org.lamsfoundation.lams.tool.forum.persistence.ForumToolSession;
import org.lamsfoundation.lams.tool.forum.persistence.ForumUser;
import org.lamsfoundation.lams.tool.forum.persistence.Message;
import org.lamsfoundation.lams.tool.forum.util.ForumConstants;

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
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Object toolContentObject)
	    throws ToolException {

	SortedMap<String, ToolOutputDefinition> definitionMap = new TreeMap<String, ToolOutputDefinition>();
	if (toolContentObject != null) {
	    ToolOutputDefinition chosenTopicAnswersDefinition = buildComplexOutputDefinition(ForumConstants.TEXT_SEARCH_DEFINITION_NAME);
	    Forum forum = (Forum) toolContentObject;
	    // adding all existing conditions
	    chosenTopicAnswersDefinition.setDefaultConditions(new ArrayList<BranchCondition>(forum.getConditions()));
	    // if no conditions were created in the tool instance, a default condition is added; the condition is
	    // persisted in ForumService.
	    if (chosenTopicAnswersDefinition.getDefaultConditions().isEmpty() && !forum.getMessages().isEmpty()) {
		Set<Message> messages = new HashSet<Message>();
		for (Message message : (Set<Message>) forum.getMessages()) {
		    if (message.getIsAuthored()) {
			messages.add(message);
			break;
		    }
		}

		String name = buildConditionName(ForumConstants.TEXT_SEARCH_DEFINITION_NAME, forum.getContentId()
			.toString());
		// Default condition checks if the answers for the first topic contain word "LAMS"
		ForumCondition defaultCondition = new ForumCondition(null, null, 1, name, getI18NText(
			ForumConstants.TEXT_SEARCH_DEFAULT_CONDITION_DISPLAY_NAME_KEY, false), "LAMS", null, null,
			null, messages);
		forum.getConditions().add(defaultCondition);
		chosenTopicAnswersDefinition.getDefaultConditions().add(defaultCondition);
	    }
	    chosenTopicAnswersDefinition.setShowConditionNameOnly(true);
	    definitionMap.put(ForumConstants.TEXT_SEARCH_DEFINITION_NAME, chosenTopicAnswersDefinition);
	}

	ToolOutputDefinition numberOfPostsDefinition = buildRangeDefinition(
		ForumConstants.LEARNER_NUM_POSTS_DEFINITION_NAME, new Long(0), null);
	definitionMap.put(ForumConstants.LEARNER_NUM_POSTS_DEFINITION_NAME, numberOfPostsDefinition);

	return definitionMap;
    }

    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, IForumService forumService,
	    Long toolSessionId, Long learnerId) {
	TreeMap<String, ToolOutput> outputs = new TreeMap<String, ToolOutput>();
	// cached tool output for all text search conditions
	ToolOutput chosenTopicAnswersOutput = null;

	if (names == null) {
	    // output will be set for all the existing conditions
	    ForumToolSession session = forumService.getSessionBySessionId(toolSessionId);
	    Forum forum = session.getForum();
	    Set<ForumCondition> conditions = forum.getConditions();
	    for (ForumCondition condition : conditions) {
		String name = condition.getName();
		if (isTextSearchConditionName(name) && chosenTopicAnswersOutput != null) {
		    outputs.put(name, chosenTopicAnswersOutput);
		} else {
		    ToolOutput output = getToolOutput(name, forumService, toolSessionId, learnerId);
		    if (output != null) {
			outputs.put(name, output);
			if (isTextSearchConditionName(name)) {
			    chosenTopicAnswersOutput = output;
			}
		    }
		}
	    }
	} else {
	    for (String name : names) {
		if (isTextSearchConditionName(name) && chosenTopicAnswersOutput != null) {
		    outputs.put(name, chosenTopicAnswersOutput);
		} else {
		    ToolOutput output = getToolOutput(name, forumService, toolSessionId, learnerId);
		    if (output != null) {
			outputs.put(name, output);
			if (isTextSearchConditionName(name)) {
			    chosenTopicAnswersOutput = output;
			}
		    }
		}
	    }
	}

	if (names == null || names.contains(ForumConstants.LEARNER_NUM_POSTS_DEFINITION_NAME)) {
	    outputs.put(ForumConstants.LEARNER_NUM_POSTS_DEFINITION_NAME, getNumPosts(forumService, learnerId,
		    toolSessionId));
	}
	return outputs;

    }

    public ToolOutput getToolOutput(String name, IForumService forumService, Long toolSessionId, Long learnerId) {
	ToolOutput toolOutput = null;
	if (isTextSearchConditionName(ForumConstants.TEXT_SEARCH_DEFINITION_NAME)) {
	    // a map of "topic creation date" -> "all learner's answers to that topic"
	    Map<Date, Set<String>> answers = new HashMap<Date, Set<String>>();

	    ForumUser user = forumService.getUserByUserAndSession(learnerId, toolSessionId);
	    List<MessageDTO> userMessages = forumService.getMessagesByUserUid(user.getUid(), toolSessionId);

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
	    toolOutput = new ToolOutput(name, getI18NText(ForumConstants.TEXT_SEARCH_DEFINITION_NAME, true), answers,
		    false);
	} else if (name != null && name.equals(ForumConstants.LEARNER_NUM_POSTS_DEFINITION_NAME)) {
	    toolOutput = getNumPosts(forumService, learnerId, toolSessionId);
	}
	return toolOutput;

    }

    private ToolOutput getNumPosts(IForumService forumService, Long learnerId, Long toolSessionId) {
	int num = forumService.getTopicsNum(learnerId, toolSessionId);
	return new ToolOutput(ForumConstants.LEARNER_NUM_POSTS_DEFINITION_NAME, getI18NText(
		ForumConstants.LEARNER_NUM_POSTS_DEFINITION_NAME, true), new Long(num));
    }

    @Override
    protected String[] splitConditionName(String conditionName) {
	return super.splitConditionName(conditionName);
    }

    protected String buildTextSearchConditionName(String uniquePart) {
	return super.buildConditionName(ForumConstants.TEXT_SEARCH_DEFINITION_NAME, uniquePart);
    }

    private boolean isTextSearchConditionName(String name) {
	return name != null && name.startsWith(ForumConstants.TEXT_SEARCH_DEFINITION_NAME);
    }
}
