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

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

import org.lamsfoundation.lams.rest.RestTags;
import org.lamsfoundation.lams.util.JsonUtil;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/** Simple survey question used for parsing survey data before conversion to JSON */
public class SurveyQuestion {
    short type;
    String questionText;
    Boolean allowOtherTextEntry;
    Boolean required;
    Map<Integer, String> answers;

    public SurveyQuestion(String text) {
	type = (short) 1;
	questionText = text;
	allowOtherTextEntry = false;
	required = true;
	answers = new TreeMap<Integer, String>();
    }

    public Map<Integer, String> getAnswers() {
	return answers;
    }

    public void setType(short type) {
	this.type = type;
    }

    public void setQuestionText(String questionText) {
	this.questionText = questionText;
    }

    public void setAllowOtherTextEntry(Boolean allowOtherTextEntry) {
	this.allowOtherTextEntry = allowOtherTextEntry;
    }

    public void setRequired(Boolean required) {
	this.required = required;
    }

    public String getQuestionText() {
	return questionText;
    }

    public ObjectNode getAsObjectNode() throws IOException {
	return (ObjectNode) JsonNodeFactory.instance.objectNode().put("type", type)
		.put(RestTags.QUESTION_TEXT, questionText).put("allowOtherTextEntry", allowOtherTextEntry)
		.put("required", required).set(RestTags.ANSWERS, JsonUtil.readArray(answers.values()));
    }

    public boolean validate(List<String> errorMessages, ResourceBundle appBundle, MessageFormat formatter, Integer questionNumber) {
	boolean errorsExist = false;
	if (questionText == null || questionText.length() == 0) {
	    errorMessages.add(TextUtil.getText(appBundle, formatter, "error.question.num", new Object[] { questionNumber }));
	    errorsExist = true;
	}
	if (answers.size() == 0) {
	    errorMessages.add(TextUtil.getText(appBundle, formatter, "error.question.must.have.answer.num",
		    new Object[] { questionNumber }));
	    errorsExist = true;
	}
	return errorsExist;
    }
}
