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


package org.lamsfoundation.lams.tool.qa.dto;

import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <p>
 * DTO that holds question and user attempts data for jsp purposes
 * </p>
 *
 * @author Ozgur Demirtas
 */
public class QaMonitoredAnswersDTO implements Comparable {
    private String sessionId;

    private String sessionName;

    private String questionUid;

    private String question;

    private String feedback;

    private Map questionAttempts;

    /**
     * @return Returns the question.
     */
    public String getQuestion() {
	return question;
    }

    /**
     * @param question
     *            The question to set.
     */
    public void setQuestion(String question) {
	this.question = question;
    }

    /**
     *
     * @return Returns the feedback.
     */
    public String getFeedback() {
	return feedback;
    }

    /**
     * @param feedback
     *            The feedback to set.
     */
    public void setFeedback(String feedback) {
	this.feedback = feedback;
    }

    /**
     * @return Returns the questionUid.
     */
    public String getQuestionUid() {
	return questionUid;
    }

    /**
     * @param questionUid
     *            The questionUid to set.
     */
    public void setQuestionUid(String questionUid) {
	this.questionUid = questionUid;
    }

    /**
     * @return Returns the questionAttempts.
     */
    public Map getQuestionAttempts() {
	return questionAttempts;
    }

    /**
     * @param questionAttempts
     *            The questionAttempts to set.
     */
    public void setQuestionAttempts(Map questionAttempts) {
	this.questionAttempts = questionAttempts;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("sessionName:", sessionName).append("sessionId:", sessionId)
		.append("questionUid:", questionUid).append("question:", question)
		.append("questionAttempts:", questionAttempts).toString();
    }

    @Override
    public int compareTo(Object o) {
	QaMonitoredAnswersDTO qaMonitoredAnswersDTO = (QaMonitoredAnswersDTO) o;

	if (qaMonitoredAnswersDTO == null) {
	    return 1;
	} else {
	    return (int) (new Long(questionUid).longValue() - new Long(qaMonitoredAnswersDTO.questionUid).longValue());
	}
    }

    /**
     * @return Returns the sessionId.
     */
    public String getSessionId() {
	return sessionId;
    }

    /**
     * @param sessionId
     *            The sessionId to set.
     */
    public void setSessionId(String sessionId) {
	this.sessionId = sessionId;
    }

    /**
     * @return Returns the sessionName.
     */
    public String getSessionName() {
	return sessionName;
    }

    /**
     * @param sessionName
     *            The sessionName to set.
     */
    public void setSessionName(String sessionName) {
	this.sessionName = sessionName;
    }
}
