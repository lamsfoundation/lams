/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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

package org.lamsfoundation.lams.tool.daco.web.form;

/**
 * Daco Question Form.
 *
 *
 * @author Marcin Cieslak
 *
 * @version $Revision$
 */
public class DacoQuestionForm {
    private String questionIndex;
    private String sessionMapID;

    private short questionType;
    private String min;
    private String max;
    private String digitsDecimal;
    private Short summary;
    private String description;

    private boolean questionRequired;

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getQuestionIndex() {
	return questionIndex;
    }

    public void setQuestionIndex(String questionIndex) {
	this.questionIndex = questionIndex;
    }

    public short getQuestionType() {
	return questionType;
    }

    public void setQuestionType(short type) {
	questionType = type;
    }

    public String getMin() {
	return min;
    }

    public void setMin(String min) {
	this.min = min;
    }

    public String getMax() {
	return max;
    }

    public void setMax(String max) {
	this.max = max;
    }

    public boolean isQuestionRequired() {
	return questionRequired;
    }

    public void setQuestionRequired(boolean isRequired) {
	questionRequired = isRequired;
    }

    public String getSessionMapID() {
	return sessionMapID;
    }

    public void setSessionMapID(String sessionMapID) {
	this.sessionMapID = sessionMapID;
    }

    public Short getSummary() {
	return summary;
    }

    public void setSummary(Short summary) {
	this.summary = summary;
    }

    public String getDigitsDecimal() {
	return digitsDecimal;
    }

    public void setDigitsDecimal(String digitsDecimal) {
	this.digitsDecimal = digitsDecimal;
    }
}