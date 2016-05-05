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

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <p>
 * DTO that holds users attempt history data for jsp purposes
 * </p>
 *
 * @author Ozgur Demirtas
 */
public class VoteMonitoredUserDTO implements Comparable {
    private String queUsrId;

    private String uid;

    //private String attemptTime;

    private Date attemptTime;

    private String timeZone;

    private String userName;

    private String isCorrect;

    private String response;

    private String userEntry;

    private String visible;

    private String sessionId;

    private String questionUid;

    private Map usersAttempts;

    private String responsePresentable;

    /**
     * @return Returns the queUsrId.
     */
    public String getQueUsrId() {
	return queUsrId;
    }

    /**
     * @param queUsrId
     *            The queUsrId to set.
     */
    public void setQueUsrId(String queUsrId) {
	this.queUsrId = queUsrId;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("queUsrId and username", getQueUsrId() + " and " + getUserName())
		.toString();
    }

    /**
     * @return Returns the usersAttempts.
     */
    public Map getUsersAttempts() {
	return usersAttempts;
    }

    /**
     * @param usersAttempts
     *            The usersAttempts to set.
     */
    public void setUsersAttempts(Map usersAttempts) {
	this.usersAttempts = usersAttempts;
    }

    @Override
    public int compareTo(Object o) {
	VoteMonitoredUserDTO VoteMonitoredUserDTO = (VoteMonitoredUserDTO) o;

	if (VoteMonitoredUserDTO == null) {
	    return 1;
	} else {
	    return (int) (new Long(queUsrId).longValue() - new Long(VoteMonitoredUserDTO.queUsrId).longValue());
	}
    }

    /**
     * @return Returns the isCorrect.
     */
    public String getIsCorrect() {
	return isCorrect;
    }

    /**
     * @param isCorrect
     *            The isCorrect to set.
     */
    public void setIsCorrect(String isCorrect) {
	this.isCorrect = isCorrect;
    }

    /**
     * @return Returns the response.
     */
    public String getResponse() {
	return response;
    }

    /**
     * @param response
     *            The response to set.
     */
    public void setResponse(String response) {
	this.response = response;
    }

    /**
     * @return Returns the timeZone.
     */
    public String getTimeZone() {
	return timeZone;
    }

    /**
     * @param timeZone
     *            The timeZone to set.
     */
    public void setTimeZone(String timeZone) {
	this.timeZone = timeZone;
    }

    /**
     * @return Returns the uid.
     */
    public String getUid() {
	return uid;
    }

    /**
     * @param uid
     *            The uid to set.
     */
    public void setUid(String uid) {
	this.uid = uid;
    }

    /**
     * @return Returns the userName.
     */
    public String getUserName() {
	return userName;
    }

    /**
     * @param userName
     *            The userName to set.
     */
    public void setUserName(String userName) {
	this.userName = userName;
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
     * @return Returns the userEntry.
     */
    public String getUserEntry() {
	return userEntry;
    }

    /**
     * @param userEntry
     *            The userEntry to set.
     */
    public void setUserEntry(String userEntry) {
	this.userEntry = userEntry;
    }

    /**
     * @return Returns the visible.
     */
    public String getVisible() {
	return visible;
    }

    /**
     * @param visible
     *            The visible to set.
     */
    public void setVisible(String visible) {
	this.visible = visible;
    }

    /**
     * @return Returns the attemptTime.
     */
    public Date getAttemptTime() {
	return attemptTime;
    }

    /**
     * @param attemptTime
     *            The attemptTime to set.
     */
    public void setAttemptTime(Date attemptTime) {
	this.attemptTime = attemptTime;
    }

    /**
     * @return Returns the responsePresentable.
     */
    public String getResponsePresentable() {
	return responsePresentable;
    }

    /**
     * @param responsePresentable
     *            The responsePresentable to set.
     */
    public void setResponsePresentable(String responsePresentable) {
	this.responsePresentable = responsePresentable;
    }
}
