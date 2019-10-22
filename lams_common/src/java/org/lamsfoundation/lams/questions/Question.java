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

package org.lamsfoundation.lams.questions;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.builder.HashCodeBuilder;

public class Question {
    // just for convenience when returning from methods
    public static final Question[] QUESTION_ARRAY_TYPE = new Question[] {};

    public static final String QUESTION_TYPE_MULTIPLE_CHOICE = "mc";
    public static final String QUESTION_TYPE_MULTIPLE_RESPONSE = "mr";
    public static final String QUESTION_TYPE_TRUE_FALSE = "tf";
    public static final String QUESTION_TYPE_ESSAY = "es";
    public static final String QUESTION_TYPE_MATCHING = "mt";
    public static final String QUESTION_TYPE_FILL_IN_BLANK = "fb";
    public static final String QUESTION_TYPE_MARK_HEDGING = "mh";
    public static final Set<String> QUESTION_TYPES = new TreeSet<>(Arrays
	    .asList(new String[] { Question.QUESTION_TYPE_MULTIPLE_CHOICE, Question.QUESTION_TYPE_MULTIPLE_RESPONSE,
		    Question.QUESTION_TYPE_TRUE_FALSE, Question.QUESTION_TYPE_ESSAY, Question.QUESTION_TYPE_MATCHING,
		    Question.QUESTION_TYPE_FILL_IN_BLANK, Question.QUESTION_TYPE_MARK_HEDGING }));

    private String type;
    private String title;
    private String label;
    private String text;
    private String feedback;
    private List<Answer> answers;
    private List<Answer> matchAnswers;
    private Map<Integer, Integer> matchMap;
    private String resourcesFolderPath;

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

    public String getTitle() {
	return title;
    }

    public String getLabel() {
	return label;
    }

    public void setLabel(String label) {
	this.label = label;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getText() {
	return text;
    }

    public void setText(String text) {
	this.text = text;
    }

    public String getFeedback() {
	return feedback;
    }

    public void setFeedback(String feedback) {
	this.feedback = feedback;
    }

    public List<Answer> getAnswers() {
	return answers;
    }

    public void setAnswers(List<Answer> answers) {
	this.answers = answers;
    }

    public List<Answer> getMatchAnswers() {
	return matchAnswers;
    }

    public void setMatchAnswers(List<Answer> matchAnswers) {
	this.matchAnswers = matchAnswers;
    }

    public Map<Integer, Integer> getMatchMap() {
	return matchMap;
    }

    public void setMatchMap(Map<Integer, Integer> matchMap) {
	this.matchMap = matchMap;
    }

    public String getResourcesFolderPath() {
	return resourcesFolderPath;
    }

    public void setResourcesFolderPath(String resourcesFolderPath) {
	this.resourcesFolderPath = resourcesFolderPath;
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(text).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (!(obj instanceof Question)) {
	    return false;
	}
	Question other = (Question) obj;
	if (text == null) {
	    if (other.text != null) {
		return false;
	    }
	} else if (!text.equals(other.text)) {
	    return false;
	}
	return true;
    }
}