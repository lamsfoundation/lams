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


package org.lamsfoundation.lams.tool.forum.model;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.lamsfoundation.lams.learningdesign.BranchCondition;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.TextSearchCondition;
import org.lamsfoundation.lams.tool.OutputType;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputFormatException;
import org.lamsfoundation.lams.tool.ToolOutputValue;
import org.lamsfoundation.lams.tool.forum.dto.ForumConditionDTO;
import org.lamsfoundation.lams.tool.forum.util.ConditionTopicComparator;
import org.lamsfoundation.lams.util.WebUtil;

/**
 *  A text search condition with a set of topics on answers to which the search should be performed.
 *
 *
 * @author Marcin Cieslak
 *
 */
@Entity
@Table(name = "tl_lafrum11_conditions")
public class ForumCondition extends TextSearchCondition {
    /**
     * Topics linked to this condition. Answers to them will be scanned for the words that make the condition's
     * parameters.
     */
    @ManyToMany
    @Cascade({ CascadeType.SAVE_UPDATE })
    @JoinTable(name = "tl_lafrum11_condition_topics", joinColumns = @JoinColumn(name = "condition_id"), inverseJoinColumns = @JoinColumn(name = "topic_uid"))
    @OrderBy("uid ASC")
    private Set<Message> topics;

    public ForumCondition() {
	this.topics =  new TreeSet<Message>(new ConditionTopicComparator());
    }

    public ForumCondition(ForumConditionDTO conditionDTO) {
	super(conditionDTO);
	this.topics = new TreeSet<Message>(new ConditionTopicComparator());
	for (Message topic : conditionDTO.getTopics()) {
	    Message topicCopy = new Message();
	    topicCopy.setCreated(topic.getCreated());
	    topicCopy.setSubject(topic.getSubject());
	    topics.add(topicCopy);
	}
    }

    public ForumCondition(Long conditionId, Integer conditionUIID, Integer orderId, String name, String displayName,
	    String allWords, String phrase, String anyWords, String excludedWords, Set<Message> questions) {
	super(conditionId, conditionUIID, orderId, name, displayName, BranchCondition.OUTPUT_TYPE_COMPLEX, null, null,
		null, allWords, phrase, anyWords, excludedWords);
	setTopics(questions);
    }

    @Override
    public boolean isMet(ToolOutput output) throws ToolOutputFormatException {
	boolean result = false;
	if (output != null) {
	    ToolOutputValue value = output.getValue();
	    if (value != null) {
		if (OutputType.OUTPUT_COMPLEX.equals(value.getType())) {
		    // the condition "knows" it's a map of "topic creation date" -> "all learner's answers to that
		    // topic"
		    Map<Date, Set<String>> messages = (Map<Date, Set<String>>) value.getValue();
		    result = true;

		    /*
		     * We want to check if for each topic all answer don't contain excluded words and at least one
		     * answer contains the desired (all, any, phrase) words. That is why we check all, any and phrase
		     * conditions using the "matches" method, but we check excluded words manually.
		     */
		    parseConditionStrings();
		    List<String> excludedWordsCopy = getExcludedWordsCondition();
		    setExcludedWordsCondition(null);

		    for (Message topic : getTopics()) {
			boolean singleTopicResult = false;
			Set<String> answers = messages.get(topic.getCreated());
			if (answers == null) {
			    singleTopicResult = getAllWordsCondition() == null && getAnyWordsCondition() == null
				    && getPhraseCondition() == null;
			} else {
			    for (String answer : answers) {
				answer = WebUtil.removeHTMLtags(answer);
				if (!singleTopicResult) {
				    singleTopicResult = matches(answer);
				}
				if (matchExcludedWordsOnly(excludedWordsCopy, answer)) {
				    singleTopicResult = false;
				    break;
				}
			    }
			}

			result &= singleTopicResult;
			// if at least one topic does not satisfy the condition, there
			// is no need to look further
			if (!result) {
			    break;
			}
		    }
		} else {
		    throw new ToolOutputFormatException("Forum tool produced a non-complex tool output.");
		}
	    }
	}
	return result;
    }

    public Set<Message> getTopics() {
	return topics;
    }

    public void setTopics(Set<Message> questions) {
	topics = questions;
    }

    /**
     * Notice that the original topics are assigned to the copy.
     */
    @Override
    public Object clone() {
	Set<Message> topicsCopy = new TreeSet<Message>(new ConditionTopicComparator());
	topicsCopy.addAll(topics);
	return new ForumCondition(null, null, orderId, name, displayName, allWords, phrase, anyWords, excludedWords,
		topicsCopy);
    }

    /**
     * Notice that topics are copied with very little information and then they are assigned to the cloned object. This
     * method is used when assigning BranchActivityEntry needs, so only basic information (in fact, only creation date)
     * is needed. Also, there should be no link to Forum content.
     */
    @Override
    public ForumCondition clone(int uiidOffset) {
	Integer newConditionUIID = LearningDesign.addOffset(conditionUIID, uiidOffset);
	Set<Message> topicsCopy = new TreeSet<Message>(new ConditionTopicComparator());

	for (Message topic : getTopics()) {
	    Message topicCopy = new Message();
	    topicCopy.setCreated(topic.getCreated());
	    topicCopy.setSubject(topic.getSubject());
	    topicsCopy.add(topicCopy);
	}
	return new ForumCondition(null, newConditionUIID, orderId, name, displayName, allWords, phrase, anyWords,
		excludedWords, topicsCopy);
    }

    /**
     * Notice that topics from the cloned (and not the original) tool content are assigned to the cloned condition. This
     * method is used for cloning tool content.
     */
    public ForumCondition clone(Forum forum) {

	Set<Message> topicsCopy = new TreeSet<Message>(new ConditionTopicComparator());
	for (Message conditionTopic : getTopics()) {
	    for (Message contentTopic : forum.getMessages()) {
		if (contentTopic.getIsAuthored() && contentTopic.getCreated().equals(conditionTopic.getCreated())) {
		    topicsCopy.add(contentTopic);
		}
	    }
	}

	return new ForumCondition(null, null, orderId, name, displayName, allWords, phrase, anyWords, excludedWords,
		topicsCopy);
    }

    /**
     * The condition must be bound with at least one topic.
     */
    @Override
    protected boolean isValid() {
	return getTopics() != null && !getTopics().isEmpty();
    }

    /**
     * It filters the given text in order to find any of the unwanted words.
     *
     * @param excludedWords
     *            words to search for
     * @param textToMatch
     *            string to be filtered
     * @return <code>true</code> if at least one of the words from the list is found in the text
     */
    private boolean matchExcludedWordsOnly(List<String> excludedWords, String textToMatch) {
	if (textToMatch == null || excludedWords == null) {
	    return false;
	}
	StringBuilder stringPattern = new StringBuilder();
	for (String excludedWord : excludedWords) {
	    stringPattern.append("(?:").append(TextSearchCondition.NON_WORD_DELIMITER_REGEX)
		    .append(Pattern.quote(excludedWord)).append(TextSearchCondition.NON_WORD_DELIMITER_REGEX)
		    .append(")|");
	}
	stringPattern.deleteCharAt(stringPattern.length() - 1);
	Pattern regexPattern = Pattern.compile(stringPattern.toString(), TextSearchCondition.PATTERN_MATCHING_OPTIONS);
	Matcher matcher = regexPattern.matcher(textToMatch);
	return matcher.find();
    }

    @Override
    public ForumConditionDTO getBranchConditionDTO(Integer toolActivityUIID) {
	return new ForumConditionDTO(this, toolActivityUIID);
    }
}