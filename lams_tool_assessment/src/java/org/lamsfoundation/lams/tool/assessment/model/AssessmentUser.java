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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserDetails;

/**
 * Assessment User
 *
 * @author Andrey Balan
 */
@Entity
@Table(name = "tl_laasse10_user")
public class AssessmentUser implements Cloneable, IUserDetails {
    private static Logger log = Logger.getLogger(AssessmentUser.class);

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "login_name")
    private String loginName;

    @Column(name = "session_finished")
    private boolean sessionFinished;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_uid")
    private AssessmentSession session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessment_uid")
    private Assessment assessment;

    // *************** NON Persist Fields ********************

    // the user access some reousrce question date time. Use in monitoring summary page
    @Transient
    private Date accessDate;

    public AssessmentUser() {
    }

    public AssessmentUser(UserDTO user, AssessmentSession session) {
	this.userId = user.getUserID().longValue();
	this.firstName = user.getFirstName();
	this.lastName = user.getLastName();
	this.loginName = user.getLogin();
	this.session = session;
	this.assessment = null;
	this.sessionFinished = false;
    }

    public AssessmentUser(UserDTO user, Assessment content) {
	this.userId = user.getUserID().longValue();
	this.firstName = user.getFirstName();
	this.lastName = user.getLastName();
	this.loginName = user.getLogin();
	this.session = null;
	this.assessment = content;
	this.sessionFinished = false;
    }

    /**
     * Clone method from <code>java.lang.Object</code>
     */
    @Override
    public Object clone() {

	AssessmentUser user = null;
	try {
	    user = (AssessmentUser) super.clone();
	    user.setUid(null);
	    // never clone session
	    user.setSession(null);
	} catch (CloneNotSupportedException e) {
	    log.error("When clone " + AssessmentUser.class + " failed");
	}

	return user;
    }

    // **********************************************************
    // Get/Set methods
    // **********************************************************

    /**
     * @return Returns the uid.
     */
    public Long getUid() {
	return uid;
    }

    /**
     * @param uid
     * 	The uid to set.
     */
    public void setUid(Long userID) {
	this.uid = userID;
    }

    /**
     * @return Returns the userId.
     */
    public Long getUserId() {
	return userId;
    }

    /**
     * @param userId
     * 	The userId to set.
     */
    public void setUserId(Long userID) {
	this.userId = userID;
    }

    /**
     * @return
     */
    public String getLastName() {
	return lastName;
    }

    public void setLastName(String lastName) {
	this.lastName = lastName;
    }

    /**
     * @return
     */
    public String getFirstName() {
	return firstName;
    }

    public void setFirstName(String firstName) {
	this.firstName = firstName;
    }

    /**
     * @return
     */
    public String getLoginName() {
	return loginName;
    }

    public String getLogin() {
	return getLoginName();
    }

    public void setLoginName(String loginName) {
	this.loginName = loginName;
    }

    /**
     * @return
     */
    public AssessmentSession getSession() {
	return session;
    }

    public void setSession(AssessmentSession session) {
	this.session = session;
    }

    /**
     * @return
     */
    public Assessment getAssessment() {
	return assessment;
    }

    public void setAssessment(Assessment content) {
	this.assessment = content;
    }

    /**
     * @return
     */
    public boolean isSessionFinished() {
	return sessionFinished;
    }

    public void setSessionFinished(boolean sessionFinished) {
	this.sessionFinished = sessionFinished;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (!(obj instanceof AssessmentUser)) {
	    return false;
	}

	final AssessmentUser user = (AssessmentUser) obj;

	return new EqualsBuilder().append(this.uid, user.uid).append(this.firstName, user.firstName)
		.append(this.lastName, user.lastName).append(this.loginName, user.loginName).isEquals();

    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(uid).append(firstName).append(lastName).append(loginName).toHashCode();
    }

    public Date getAccessDate() {
	return accessDate;
    }

    public void setAccessDate(Date accessDate) {
	this.accessDate = accessDate;
    }

    public String getFullName() {
	if (firstName == null || lastName == null) {
	    return null;
	} else {
	    return lastName + " " + firstName;
	}
    }

}