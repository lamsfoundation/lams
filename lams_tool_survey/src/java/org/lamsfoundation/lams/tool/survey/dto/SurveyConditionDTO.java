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


package org.lamsfoundation.lams.tool.survey.dto;

import java.util.Set;
import java.util.TreeSet;

import org.lamsfoundation.lams.learningdesign.dto.TextSearchConditionDTO;
import org.lamsfoundation.lams.tool.survey.model.SurveyCondition;
import org.lamsfoundation.lams.tool.survey.model.SurveyQuestion;
import org.lamsfoundation.lams.tool.survey.util.QuestionsComparator;

public class SurveyConditionDTO extends TextSearchConditionDTO {
    private Set<SurveyQuestion> questions = new TreeSet<SurveyQuestion>(new QuestionsComparator());

    public SurveyConditionDTO(SurveyCondition condition, Integer toolActivityUIID) {
	super(condition, toolActivityUIID);
	for (SurveyQuestion question : condition.getQuestions()) {
	    SurveyQuestion questionCopy = new SurveyQuestion();
	    questionCopy.setSequenceId(question.getSequenceId());
	    questionCopy.setShortTitle(question.getShortTitle());
	    questionCopy.setType(question.getType());
	    getQuestions().add(questionCopy);
	}
    }

    public Set<SurveyQuestion> getQuestions() {
	return questions;
    }

    public void setQuestions(Set<SurveyQuestion> questions) {
	this.questions = questions;
    }

    @Override
    public SurveyCondition getCondition() {
	return new SurveyCondition(this);
    }
}