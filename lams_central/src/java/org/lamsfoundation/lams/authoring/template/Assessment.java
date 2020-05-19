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
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

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
    String text = null;
    Boolean required = false;
    int defaultGrade = 1;
    boolean multipleAnswersAllowed = false; // only used if type == 1
    Set<AssessMCAnswer> answers = null; // only used if type == 1
    String uuid = null; // used when QTI gets imported and it contains QB question UUID

    public void setType(short type) {
	this.type = type;
	if (type == 1 && this.answers == null) {
	    // JSP template pages expect this list to be in correct order
	    this.answers = new TreeSet<>(Comparator.comparingInt(AssessMCAnswer::getSequenceId));
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

    public void setText(String text) {
	this.text = text;
    }

    public String getText() {
	return text;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public int getDefaultGrade() {
	return defaultGrade;
    }

    public void setDefaultGrade(int defaultGrade) {
	this.defaultGrade = defaultGrade;
    }

    public Set<AssessMCAnswer> getAnswers() {
	return answers;
    }

    public boolean isMultipleAnswersAllowed() {
	return multipleAnswersAllowed;
    }

    public void setMultipleAnswersAllowed(boolean multipleAnswersAllowed) {
	this.multipleAnswersAllowed = multipleAnswersAllowed;
    }

    public String getUuid() {
	return uuid;
    }

    public void setUuid(String uuid) {
	this.uuid = uuid;
    }

    public ObjectNode getAsObjectNode(int displayOrder) {
	ObjectNode json = JsonNodeFactory.instance.objectNode();
	json.put(RestTags.QUESTION_TITLE, title != null ? title : "");
	json.put(RestTags.QUESTION_TEXT, text != null ? text : "");
	json.put(RestTags.QUESTION_UUID, uuid != null ? uuid : "");
	json.put(RestTags.DISPLAY_ORDER, displayOrder);
	json.put("answerRequired", required);
	json.put("defaultGrade", defaultGrade);
	if (type == ASSESSMENT_QUESTION_TYPE_MULTIPLE_CHOICE) {
	    json.put("type", ASSESSMENT_QUESTION_TYPE_MULTIPLE_CHOICE);
	    ArrayNode answersJSON = JsonNodeFactory.instance.arrayNode();
	    for (AssessMCAnswer answer : answers) {
		answersJSON.add(answer.getAsObjectNode());
	    }
	    json.set(RestTags.ANSWERS, answersJSON);
	    // if multiple answers are allowed then the mark should only apply if no incorrect answers are selected
	    json.put("multipleAnswersAllowed", multipleAnswersAllowed);
	    json.put("incorrectAnswerNullifiesMark", multipleAnswersAllowed);
	} else {
	    json.put("type", ASSESSMENT_QUESTION_TYPE_ESSAY);
	}
	return json;
    }

    public boolean validate(List<String> errorMessages, ResourceBundle appBundle, MessageFormat formatter,
	    Integer applicationExerciseNumber, String applicationExerciseTitle, Integer questionNumber) {
	boolean errorsExist = false;
	if (text == null || text.length() == 0) {
	    errorMessages.add(TextUtil.getText(appBundle, formatter,
		    "authoring.error.application.exercise.question.must.not.be.blank.num",
		    new Object[] { applicationExerciseTitle, title }));
	    errorsExist = true;
	}
	if (type == ASSESSMENT_QUESTION_TYPE_MULTIPLE_CHOICE) {
	    if (answers.size() == 0) {
		errorMessages.add(TextUtil.getText(appBundle, formatter,
			"authoring.error.application.exercise.must.have.answer.num",
			new Object[] { applicationExerciseTitle, "\"" + title + "\"" }));
		errorsExist = true;
	    } else if (!multipleAnswersAllowed) {
		// multiple answers -> no validation, single answer -> must have one with 100%
		boolean found100percent = false;
		for (AssessMCAnswer answer : answers) {
		    if (answer.getGrade() == 1) {
			found100percent = true;
			break;
		    }
		}
		if (!found100percent) {
		    errorMessages.add(TextUtil.getText(appBundle, formatter,
			    "authoring.error.application.exercise.must.have.100.percent",
			    new Object[] { applicationExerciseTitle, "\"" + title + "\"" }));
		    errorsExist = true;
		}
	    }
	}
	return errorsExist;
    }
}
