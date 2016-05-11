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
 *
 * @hibernate.class table="tl_laasse10_session"
 *
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
     * @hibernate.id generator-class="native" type="java.lang.Long" column="uid"
     * @return Returns the learnerID.
     */
    public Long getUid() {
	return uid;
    }

    public void setUid(Long uuid) {
	this.uid = uuid;
    }

    /**
     * @hibernate.property column="session_end_date"
     * @return
     */
    public Date getSessionEndDate() {
	return sessionEndDate;
    }

    public void setSessionEndDate(Date sessionEndDate) {
	this.sessionEndDate = sessionEndDate;
    }

    /**
     * @hibernate.property column="session_start_date"
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
     * @hibernate.property
     * @return
     */
    public int getStatus() {
	return status;
    }

    public void setStatus(int status) {
	this.status = status;
    }

    /**
     * @hibernate.many-to-one column="assessment_uid" cascade="none"
     * @return
     */
    public Assessment getAssessment() {
	return assessment;
    }

    public void setAssessment(Assessment assessment) {
	this.assessment = assessment;
    }

    /**
     * @hibernate.property column="session_id"
     * @return
     */
    public Long getSessionId() {
	return sessionId;
    }

    public void setSessionId(Long sessionId) {
	this.sessionId = sessionId;
    }

    /**
     * @hibernate.property column="session_name" length="250"
     * @return Returns the session name
     */
    public String getSessionName() {
	return sessionName;
    }

    /**
     *
     * @param sessionName
     *            The session name to set.
     */
    public void setSessionName(String sessionName) {
	this.sessionName = sessionName;
    }

    /**
     *
     *
     * @hibernate.set lazy="true" inverse="false" cascade="all" order-by="create_date desc"
     * @hibernate.collection-key column="session_uid"
     * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestion"
     *
     * @return
     */
    public Set getAssessmentQuestions() {
	return assessmentQuestions;
    }

    public void setAssessmentQuestions(Set assessmentQuestions) {
	this.assessmentQuestions = assessmentQuestions;
    }

    /**
     * @hibernate.set lazy="true" inverse="false" cascade="all" order-by="last_name asc"
     * @hibernate.collection-key column="session_uid"
     * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.tool.assessment.model.AssessmentUser"
     *
     * @return
     */
    public Set<AssessmentUser> getAssessmentUsers() {
	return assessmentUsers;
    }

    public void setAssessmentUsers(Set<AssessmentUser> assessmentUsers) {
	this.assessmentUsers = assessmentUsers;
    }

    /**
     * @hibernate.many-to-one column="group_leader_uid" cascade="all"
     * @return
     */
    public AssessmentUser getGroupLeader() {
	return this.groupLeader;
    }

    public void setGroupLeader(AssessmentUser groupLeader) {
	this.groupLeader = groupLeader;
    }

}
