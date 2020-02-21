package org.lamsfoundation.lams.tool.assessment.dto;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * DTO that hols user marks. Used only for monitor export MCQ.
 *
 * @author Ozgur Demirtas
 */
public class UserMarkDTO implements Comparable {
    private String queUsrId; // mc user uid
    private String userId; // LAMS userId
    private String portraitId;
    private String userName;
    private String fullName;
    private boolean isUserLeader;
    private Date attemptTime;
    private Float[] marks;
    private String[] answeredOptions;
    private Float totalMark;

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("Listing UserMarkDTO:").append("queUsrId", queUsrId)
		.append("userName", userName).append("fullName", fullName).append("marks", marks)
		.append("totalMark", totalMark).append("attemptTime", attemptTime).toString();
    }

    /**
     * @return Returns the marks.
     */
    public Float[] getMarks() {
	return marks;
    }

    /**
     * @param marks
     *            The marks to set.
     */
    public void setMarks(Float[] marks) {
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
	UserMarkDTO userMarkDTO = (UserMarkDTO) o;

	if (userMarkDTO == null) {
	    return 1;
	} else {
	    return (int) (new Long(queUsrId).longValue() - new Long(userMarkDTO.queUsrId).longValue());
	}
    }

    /**
     * @return Returns the totalMark.
     */
    public Float getTotalMark() {
	return totalMark;
    }

    /**
     * @param totalMark
     *            The totalMark to set.
     */
    public void setTotalMark(Float totalMark) {
	this.totalMark = totalMark;
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
