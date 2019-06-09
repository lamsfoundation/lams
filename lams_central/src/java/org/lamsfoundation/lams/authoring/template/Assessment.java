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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import org.lamsfoundation.lams.rest.RestTags;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/** Simple assessment object used for parsing survey data before conversion to JSON */
public class Assessment {

    // assessment type - copied from ResourceConstants
    // question type;
    public static final short ASSESSMENT_QUESTION_TYPE_MULTIPLE_CHOICE = 1;
//    public static final short ASSESSMENT_QUESTION_TYPE_MATCHING_PAIRS = 2;
//    public static final short ASSESSMENT_QUESTION_TYPE_SHORT_ANSWER = 3;
//    public static final short ASSESSMENT_QUESTION_TYPE_NUMERICAL = 4;
//    public static final short ASSESSMENT_QUESTION_TYPE_TRUE_FALSE = 5;
    public static final short ASSESSMENT_QUESTION_TYPE_ESSAY = 6;
//    public static final short ASSESSMENT_QUESTION_TYPE_ORDERING = 7;

    short type = 6;
    String title = null;
    String questionText = null;
    Boolean required = false;
    List<AssessMCAnswer> answers = null; // only used if type == 1

    public void setType(short type) {
	this.type = type;
	if (type == 1 && this.answers == null) {
	    this.answers = new LinkedList<AssessMCAnswer>();
	}
    }

    public void setType(String type) {
	if (type != null && type.equalsIgnoreCase("mcq")) {
	    setType(ASSESSMENT_QUESTION_TYPE_MULTIPLE_CHOICE);
	} else {
	    setType(ASSESSMENT_QUESTION_TYPE_ESSAY);
	}
    }

    public short getType() {
	return type;
    }

    public void setRequired(Boolean required) {
	this.required = required;
    }

    public Boolean getRequired() {
	return required;
    }

    public void setQuestionText(String questionText) {
	this.questionText = questionText;
    }

    public String getQuestionText() {
	return questionText;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public List<AssessMCAnswer> getAnswers() {
	return answers;
    }

    public ObjectNode getAsObjectNode(int displayOrder) {
	ObjectNode json = JsonNodeFactory.instance.objectNode();
	json.put(RestTags.QUESTION_TITLE, title != null ? title : "");
	json.put(RestTags.QUESTION_TEXT, questionText != null ? questionText : "");
	json.put(RestTags.DISPLAY_ORDER, displayOrder);
	json.put("answerRequired", required);
	if (type == ASSESSMENT_QUESTION_TYPE_MULTIPLE_CHOICE) {
	    json.put("type", ASSESSMENT_QUESTION_TYPE_MULTIPLE_CHOICE);
	    ArrayNode answersJSON = JsonNodeFactory.instance.arrayNode();
	    for (AssessMCAnswer answer : answers) {
		answersJSON.add(answer.getAsObjectNode());
	    }
	    json.set(RestTags.ANSWERS, answersJSON);
	} else {
	    json.put("type", ASSESSMENT_QUESTION_TYPE_ESSAY);
	}
	return json;
    }

    private List<String> addError(List<String> errorMessages, String errorMessage) {
	if (errorMessages == null) {
	    errorMessages = new ArrayList<String>();
	}
	errorMessages.add(errorMessage);
	return errorMessages;
    }

    /** If no errors exist, returns null */
    public List<String> validate(ResourceBundle appBundle, MessageFormat formatter, Integer applicationExerciseNumber, Integer questionNumber) {
	List<String> errorMessages = null;
	if (questionText == null || questionText.length() == 0) {
	    errorMessages = addError(errorMessages, TextUtil.getText(appBundle, formatter,
		    "authoring.error.application.exercise.question.must.not.be.blank.num", new Object[] { questionNumber }));
	}
	if (type == ASSESSMENT_QUESTION_TYPE_MULTIPLE_CHOICE) {
	    if (answers.size() == 0) {
		errorMessages = addError(errorMessages, TextUtil.getText(appBundle, formatter,
			"authoring.error.application.exercise.must.have.answer.num", new Object[] { applicationExerciseNumber, questionNumber }));
	    } else {
		boolean found100percent = false;
		for (AssessMCAnswer answer : answers) {
		    if (answer.getGrade() == 1) {
			found100percent = true;
			break;
		    }
		}
		if (!found100percent) {
		    errorMessages = addError(errorMessages,
			    TextUtil.getText(appBundle, formatter,
				    "authoring.error.application.exercise.must.have.100.percent",
				    new Object[] { applicationExerciseNumber, questionNumber }));
		}
	    }
	}
	return errorMessages;
    }
}
