/***************************************************************************
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
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.mc.dto;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <p>
 * DTO that hols user marks
 * </p>
 *
 * @author Ozgur Demirtas
 */
public class McUserMarkDTO implements Comparable {
    private String sessionId;
    private String sessionName;
    private String queUsrId; // mc user uid
    private String userId; // LAMS userId
    private String portraitId;
    private String userName;
    private String fullName;
    private boolean isUserLeader;
    private Date attemptTime;
    private Integer[] marks;
    private String[] answeredOptions;
    private Long totalMark;

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("Listing UserMarkDTO:").append("sessionId", sessionId)
		.append("queUsrId", queUsrId).append("userName", userName).append("fullName", fullName)
		.append("marks", marks).append("totalMark", totalMark).append("attemptTime", attemptTime).toString();
    }

    /**
     * @return Returns the marks.
     */
    public Integer[] getMarks() {
	return marks;
    }

    /**
     * @param marks
     *            The marks to set.
     */
    public void setMarks(Integer[] marks) {
	this.marks = marks;
    }

    /**
     * @return Returns the answeredOptions - sequencial letter of the option that was chosen.
     */
    public String[] getAnsweredOptions() {
	return answeredOptions;
    }

    /**
     * @param answeredOptions
     *            The answeredOptions to set.
     */
    public void setAnsweredOptions(String[] answeredOptions) {
	this.answeredOptions = answeredOptions;
    }

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
     * @return Returns the userName.
     */
    public String getFullName() {
	return fullName;
    }

    /**
     * @param userName
     *            The userName to set.
     */
    public void setFullName(String fullName) {
	this.fullName = fullName;
    }

    public boolean setUserGroupLeader(boolean isUserLeader) {
	return this.isUserLeader = isUserLeader;
    }

    public boolean isUserGroupLeader() {
	return isUserLeader;
    }

    @Override
    public int compareTo(Object o) {
	McUserMarkDTO mcUserMarkDTO = (McUserMarkDTO) o;

	if (mcUserMarkDTO == null) {
	    return 1;
	} else {
	    return (int) (new Long(queUsrId).longValue() - new Long(mcUserMarkDTO.queUsrId).longValue());
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
     * @return Returns the totalMark.
     */
    public Long getTotalMark() {
	return totalMark;
    }

    /**
     * @param totalMark
     *            The totalMark to set.
     */
    public void setTotalMark(Long totalMark) {
	this.totalMark = totalMark;
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

    public String getUserId() {
	return userId;
    }

    public void setUserId(String userId) {
	this.userId = userId;
    }

    public String getPortraitId() {
	return portraitId;
    }

    public void setPortraitId(String portraitId) {
	this.portraitId = portraitId;
    }
}
