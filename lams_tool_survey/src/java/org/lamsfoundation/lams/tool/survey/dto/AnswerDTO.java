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
package org.lamsfoundation.lams.tool.survey.dto;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.survey.model.SurveyAnswer;
import org.lamsfoundation.lams.tool.survey.model.SurveyOption;
import org.lamsfoundation.lams.tool.survey.model.SurveyQuestion;
import org.lamsfoundation.lams.tool.survey.model.SurveyUser;
import org.lamsfoundation.lams.tool.survey.util.SurveyOptionComparator;

public class AnswerDTO extends SurveyQuestion {

    
    // ***********************************************
    // DTO fields:
    // this is DTO field. For answer, which is user and session level. For question, which is content level.
    private SurveyAnswer answer;
    // Open text entry response percentage if this question has open text entry.
    private double openResponse;
    private String openResponseFormatStr;
    private int openResponseCount;

    // this field could have value even this user does not reply this question (the answer is null)
    private SurveyUser replier;

    private static Logger logger = Logger.getLogger(AnswerDTO.class.getName());

    public AnswerDTO(SurveyQuestion question) {
	try {
	    PropertyUtils.copyProperties(this, question);
	} catch (Exception e) {
	    logger.error("Error occurs during creating AnswerDTO");
	}

	// clone options
	Set<SurveyOption> optList = question.getOptions();
	if (optList != null) {
	    SortedSet<SurveyOption> newOptions = new TreeSet<SurveyOption>(new SurveyOptionComparator());
	    for (SurveyOption option : optList) {
		SurveyOption newOption = (SurveyOption) option.clone();
		// clone does not copy the UID, here copy it back
		newOption.setUid(option.getUid());
		newOptions.add(newOption);
	    }
	    this.setOptions(newOptions);
	}

	this.updateShortTitleFromDescription();

    }

    // ****************************************************************
    // DTO fields
    // ****************************************************************
    public SurveyAnswer getAnswer() {
	return answer;
    }

    public void setAnswer(SurveyAnswer answer) {
	this.answer = answer;
    }

    public double getOpenResponse() {
	return openResponse;
    }

    public void setOpenResponse(double openResponse) {
	this.openResponse = openResponse;
    }

    public int getOpenResponseCount() {
	return openResponseCount;
    }

    public void setOpenResponseCount(int openResponseCount) {
	this.openResponseCount = openResponseCount;
    }

    public String getOpenResponseFormatStr() {
	return openResponseFormatStr;
    }

    public void setOpenResponseFormatStr(String openResponseFormatStr) {
	this.openResponseFormatStr = openResponseFormatStr;
    }

    public SurveyUser getReplier() {
	return replier;
    }

    public void setReplier(SurveyUser replier) {
	this.replier = replier;
    }

}
