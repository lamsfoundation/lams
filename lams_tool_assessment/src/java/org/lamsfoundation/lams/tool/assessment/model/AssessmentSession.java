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

package org.lamsfoundation.lams.tool.assessment.model;

import java.util.Date;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * Assessment session
 *
 * @author Andrey Balan
 */
public class AssessmentSession {

    private static Logger log = Logger.getLogger(AssessmentSession.class);

    private Long uid;
    private Long sessionId;
    private String sessionName;
    private Assessment assessment;
    private Date sessionStartDate;
    private Date sessionEndDate;
    // finish or not
    private int status;
    // assessment Questions
    private Set assessmentQuestions;
    private Set<AssessmentUser> assessmentUsers;
    private AssessmentUser groupLeader;

    // **********************************************************
    // Get/Set methods
    // **********************************************************
    /**
     *
     * @return Returns the learnerID.
     */
    public Long getUid() {
	return uid;
    }

    public void setUid(Long uuid) {
	this.uid = uuid;
    }

    /**
     *
     * @return
     */
    public Date getSessionEndDate() {
	return sessionEndDate;
    }

    public void setSessionEndDate(Date sessionEndDate) {
	this.sessionEndDate = sessionEndDate;
    }

    /**
     *
     *
     * @return
     */
    public Date getSessionStartDate() {
	return sessionStartDate;
    }

    public void setSessionStartDate(Date sessionStartDate) {
	this.sessionStartDate = sessionStartDate;
    }

    /**
     *
     * @return
     */
    public int getStatus() {
	return status;
    }

    public void setStatus(int status) {
	this.status = status;
    }

    /**
     *
     * @return
     */
    public Assessment getAssessment() {
	return assessment;
    }

    public void setAssessment(Assessment assessment) {
	this.assessment = assessment;
    }

    /**
     *
     * @return
     */
    public Long getSessionId() {
	return sessionId;
    }

    public void setSessionId(Long sessionId) {
	this.sessionId = sessionId;
    }

    /**
     *
     * @return Returns the session name
     */
    public String getSessionName() {
	return sessionName;
    }

    /**
     * @param sessionName
     *            The session name to set.
     */
    public void setSessionName(String sessionName) {
	this.sessionName = sessionName;
    }

    /**
     * @return
     */
    public Set getAssessmentQuestions() {
	return assessmentQuestions;
    }

    public void setAssessmentQuestions(Set assessmentQuestions) {
	this.assessmentQuestions = assessmentQuestions;
    }

    /**
     * @return
     */
    public Set<AssessmentUser> getAssessmentUsers() {
	return assessmentUsers;
    }

    public void setAssessmentUsers(Set<AssessmentUser> assessmentUsers) {
	this.assessmentUsers = assessmentUsers;
    }

    /**
     *
     * @return
     */
    public AssessmentUser getGroupLeader() {
	return this.groupLeader;
    }

    public void setGroupLeader(AssessmentUser groupLeader) {
	this.groupLeader = groupLeader;
    }

}
