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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.authoring.template;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Creates Assessment Multiple Choice Answer. Use the appropriate constructor to set up the minimum fields
 * and then setters to set correct & feedback - left as the default values otherwise.
 */

public class AssessMCAnswer {

    Integer displayOrder; // mandatory
    Float grade; // mandatory
    Boolean correct; // optional
    Float acceptedError; // needed if answerFloat is used
    String feedback; // optional
    String answerText; // either this or answerFloat is needed
    Float answerFloat; // either this or answerText is needed

    public AssessMCAnswer(int displayOrder, String answerText, Float grade) {
	this.displayOrder = displayOrder;
	this.grade = grade;
	this.answerText = answerText;
    }

    public AssessMCAnswer(int displayOrder, Float answerFloat, Float acceptedError, Float grade) {
	this.displayOrder = displayOrder;
	this.grade = grade;
	this.answerFloat = answerFloat;
	this.acceptedError = acceptedError;
    }

    public ObjectNode getAsObjectNode() {
	return JsonNodeFactory.instance.objectNode().put("displayOrder", displayOrder).put("grade", grade)
		.put("correct", correct).put("acceptedError", acceptedError).put("feedback", feedback)
		.put("answerText", answerText).put("answerFloat", answerFloat);
    }

    public Integer getSequenceId() {
	return displayOrder;
    }

    public void setSequenceId(Integer sequenceId) {
	this.displayOrder = sequenceId;
    }

    public Float getGrade() {
	return grade;
    }

    public void setGrade(Float grade) {
	this.grade = grade;
    }

    public Boolean getCorrect() {
	return correct;
    }

    public void setCorrect(Boolean correct) {
	this.correct = correct;
    }

    public Float getAcceptedError() {
	return acceptedError;
    }

    public void setAcceptedError(Float acceptedError) {
	this.acceptedError = acceptedError;
    }

    public String getFeedback() {
	return feedback;
    }

    public void setFeedback(String feedback) {
	this.feedback = feedback;
    }

    public String getAnswerText() {
	return answerText;
    }

    public void setAnswerText(String answerText) {
	this.answerText = answerText;
    }

    public Float getAnswerFloat() {
	return answerFloat;
    }

    public void setAnswerFloat(Float answerFloat) {
	this.answerFloat = answerFloat;
    }


}
