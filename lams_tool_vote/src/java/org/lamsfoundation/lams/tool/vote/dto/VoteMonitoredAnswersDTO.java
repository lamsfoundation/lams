/***************************************************************************
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
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.vote.dto;

//import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <p>
 * DTO that holds question/candidate answers data as well as user attempts data for jsp purposes
 * </p>
 *
 * @author Ozgur Demirtas
 */
public class VoteMonitoredAnswersDTO implements Comparable<Object> {
    protected String questionUid;

    private String question;

    private String attemptUid;

    //  private List candidateAnswers;

    @SuppressWarnings("rawtypes")
    private Map questionAttempts;

    private String sessionId;

    private String sessionName;

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

//    /**
//     * @return Returns the candidateAnswers.
//     */
//    public List getCandidateAnswers() {
//	return candidateAnswers;
//    }
//
//    /**
//     * @param candidateAnswers
//     *            The candidateAnswers to set.
//     */
//    public void setCandidateAnswers(List candidateAnswers) {
//	this.candidateAnswers = candidateAnswers;
//    }

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
    @SuppressWarnings("rawtypes")
    public Map getQuestionAttempts() {
	return questionAttempts;
    }

    /**
     * @param questionAttempts
     *            The questionAttempts to set.
     */
    @SuppressWarnings("rawtypes")
    public void setQuestionAttempts(Map questionAttempts) {
	this.questionAttempts = questionAttempts;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("question", getQuestion()).toString();
    }

    @Override
    public int compareTo(Object o) {
	VoteMonitoredAnswersDTO voteMonitoredAnswersDTO = (VoteMonitoredAnswersDTO) o;

	if (voteMonitoredAnswersDTO == null) {
	    return 1;
	} else {
	    return (int) (new Long(questionUid).longValue()
		    - new Long(voteMonitoredAnswersDTO.questionUid).longValue());
	}
    }

    /**
     * @return Returns the attemptUid.
     */
    public String getAttemptUid() {
	return attemptUid;
    }

    /**
     * @param attemptUid
     *            The attemptUid to set.
     */
    public void setAttemptUid(String attemptUid) {
	this.attemptUid = attemptUid;
    }
}
