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

/* $$Id$$ */
package org.lamsfoundation.lams.tool.qa;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.TextSearchCondition;
import org.lamsfoundation.lams.learningdesign.dto.BranchConditionDTO;
import org.lamsfoundation.lams.tool.OutputType;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputFormatException;
import org.lamsfoundation.lams.tool.ToolOutputValue;
import org.lamsfoundation.lams.tool.qa.util.QaQueContentComparator;
import org.lamsfoundation.lams.tool.qa.util.QaQuestionContentDTOComparator;

/**
 * A text search condition with a set of questions on answers to which the search should be performed.
 * 
 * @author Marcin Cieslak
 * 
 */
public class QaCondition extends TextSearchCondition {
    /**
     * Questions linked to this condition. Answers to them will be scanned for the words that make the condition's
     * parameters.
     */
    private Set<QaQueContent> questions = new TreeSet<QaQueContent>(new QaQueContentComparator());

    public SortedSet<QaQuestionContentDTO> temporaryQuestionDTOSet = new TreeSet<QaQuestionContentDTO>(
	    new QaQuestionContentDTOComparator());

    public QaCondition() {

    }

    public QaCondition(BranchConditionDTO conditionDTO) {
	super(conditionDTO);
    }

    public QaCondition(Long conditionId, Integer conditionUIID, Integer orderId, String name, String displayName,
	    String type, String startValue, String endValue, String exactMatchValue, String allWords, String phrase,
	    String anyWords, String excludedWords, Set<QaQueContent> questions) {
	super(conditionId, conditionUIID, orderId, name, displayName, type, startValue, endValue, exactMatchValue,
		allWords, phrase, anyWords, excludedWords);
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
		    for (QaQueContent question : questions) {
			String textToMatch = answers[question.getDisplayOrder() - 1];
			result &= matches(textToMatch);
			// if at least one answer does not satisfy the condition, there is no need to look further
			if (!result) {
			    break;
			}
		    }
		} else {
		    throw new ToolOutputFormatException("Q&A produced a non-complex tool output.");
		}
	    }
	}
	return result;
    }

    public Set<QaQueContent> getQuestions() {
	return questions;
    }

    public void setQuestions(Set<QaQueContent> questions) {
	this.questions = questions;
    }

    @Override
    public Object clone() {
	Set<QaQueContent> questionsCopy = new TreeSet<QaQueContent>(new QaQueContentComparator());
	questionsCopy.addAll(questions);
	return new QaCondition(null, null, orderId, name, displayName, type, null, null, null, allWords, phrase,
		anyWords, excludedWords, questionsCopy);
    }

    @Override
    public QaCondition clone(int uiidOffset) {
	Integer newConditionUIID = LearningDesign.addOffset(conditionUIID, uiidOffset);
	Set<QaQueContent> questionsCopy = new TreeSet<QaQueContent>(new QaQueContentComparator());
	questionsCopy.addAll(questions);
	return new QaCondition(null, newConditionUIID, orderId, name, displayName, type, startValue, endValue,
		exactMatchValue, allWords, phrase, anyWords, excludedWords, questionsCopy);
    }

}