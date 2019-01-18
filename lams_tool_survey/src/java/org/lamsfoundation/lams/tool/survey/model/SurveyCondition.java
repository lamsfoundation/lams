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

package org.lamsfoundation.lams.tool.survey.model;

import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.SortComparator;
import org.lamsfoundation.lams.learningdesign.BranchCondition;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.TextSearchCondition;
import org.lamsfoundation.lams.tool.OutputType;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputFormatException;
import org.lamsfoundation.lams.tool.ToolOutputValue;
import org.lamsfoundation.lams.tool.survey.dto.SurveyConditionDTO;
import org.lamsfoundation.lams.tool.survey.util.QuestionsComparator;

/**
 * A text search condition with a set of questions on answers to which the search should be performed.
 *
 * @author Marcin Cieslak
 */
@Entity
@Table(name = "tl_lasurv11_conditions")
public class SurveyCondition extends TextSearchCondition {
    /**
     * Questions linked to this condition. Answers to them will be scanned for the words that make the condition's
     * parameters.
     */
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "tl_lasurv11_condition_questions", joinColumns = @JoinColumn(name = "condition_id"), inverseJoinColumns = @JoinColumn(name = "question_uid"))
    @SortComparator(QuestionsComparator.class)
    private Set<SurveyQuestion> questions = new TreeSet<SurveyQuestion>(new QuestionsComparator());

    public SurveyCondition() {

    }

    public SurveyCondition(SurveyConditionDTO conditionDTO) {
	super(conditionDTO);

	for (SurveyQuestion question : conditionDTO.getQuestions()) {
	    SurveyQuestion questionCopy = new SurveyQuestion();
	    questionCopy.setSequenceId(question.getSequenceId());
	    questionCopy.setShortTitle(question.getShortTitle());
	    questionCopy.setType(question.getType());
	    getQuestions().add(questionCopy);
	}
    }

    public SurveyCondition(Long conditionId, Integer conditionUIID, Integer orderId, String name, String displayName,
	    String allWords, String phrase, String anyWords, String excludedWords, Set<SurveyQuestion> questions) {
	super(conditionId, conditionUIID, orderId, name, displayName, BranchCondition.OUTPUT_TYPE_COMPLEX, null, null,
		null, allWords, phrase, anyWords, excludedWords);
	setQuestions(questions);
    }

    @Override
    public boolean isMet(ToolOutput output) throws ToolOutputFormatException {
	boolean result = false;
	if (output != null) {
	    ToolOutputValue value = output.getValue();
	    if (value != null) {
		if (OutputType.OUTPUT_COMPLEX.equals(value.getType())) {
		    // the condition "knows" it's an array of strings, i.e. user's answers
		    String[] answers = (String[]) value.getValue();
		    result = true;

		    for (int i = 0; i < answers.length; i++) {
			String textToMatch = answers[i];

			result &= matches(textToMatch);
			// if at least one answer does not satisfy the condition, there is no need to look further
			if (!result) {
			    break;
			}
		    }

		} else {
		    throw new ToolOutputFormatException("Survey tool produced a non-complex tool output.");
		}
	    }
	}
	return result;
    }

    public Set<SurveyQuestion> getQuestions() {
	return questions;
    }

    public void setQuestions(Set<SurveyQuestion> questions) {
	this.questions = questions;
    }

    /**
     * Notice that the original questions are assigned to the copy.
     */
    @Override
    public Object clone() {
	Set<SurveyQuestion> questionsCopy = new TreeSet<SurveyQuestion>(new QuestionsComparator());
	questionsCopy.addAll(questions);
	return new SurveyCondition(null, null, orderId, name, displayName, allWords, phrase, anyWords, excludedWords,
		questionsCopy);
    }

    /**
     * Notice that questions are copied with very little information and then they are assigned to the cloned object.
     * This method is used when assigning BranchActivityEntry needs, so only basic information (in fact, only order ID)
     * is needed. Also, there should be no link to Survey content.
     */
    @Override
    public SurveyCondition clone(int uiidOffset) {
	Integer newConditionUIID = LearningDesign.addOffset(conditionUIID, uiidOffset);
	Set<SurveyQuestion> questionsCopy = new TreeSet<SurveyQuestion>(new QuestionsComparator());

	for (SurveyQuestion question : getQuestions()) {
	    SurveyQuestion questionCopy = new SurveyQuestion();
	    questionCopy.setSequenceId(question.getSequenceId());
	    questionCopy.setShortTitle(question.getShortTitle());
	    questionCopy.setType(question.getType());
	    questionsCopy.add(questionCopy);
	}
	return new SurveyCondition(null, newConditionUIID, orderId, name, displayName, allWords, phrase, anyWords,
		excludedWords, questionsCopy);
    }

    /**
     * Notice that questions from the cloned (and not the original) tool content are assigned to the cloned condition.
     * This method is used for cloning tool content.
     */
    public SurveyCondition clone(Survey survey) {

	Set<SurveyQuestion> questionsCopy = new TreeSet<SurveyQuestion>(new QuestionsComparator());
	for (SurveyQuestion conditionQuestion : getQuestions()) {
	    for (SurveyQuestion contentQuestion : survey.getQuestions()) {
		if (conditionQuestion.getSequenceId() == contentQuestion.getSequenceId()) {
		    questionsCopy.add(contentQuestion);
		}
	    }
	}

	return new SurveyCondition(null, null, orderId, name, displayName, allWords, phrase, anyWords, excludedWords,
		questionsCopy);
    }

    /**
     * The condition must be bound with at least one question.
     */
    @Override
    protected boolean isValid() {
	return getQuestions() != null && !getQuestions().isEmpty();
    }

    @Override
    public SurveyConditionDTO getBranchConditionDTO(Integer toolActivityUIID) {
	return new SurveyConditionDTO(this, toolActivityUIID);
    }
}