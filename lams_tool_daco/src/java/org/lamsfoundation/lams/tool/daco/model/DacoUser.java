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

package org.lamsfoundation.lams.tool.daco.model;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;

/**
 * Daco
 *
 * @author Dapeng Ni
 *
 * @hibernate.class table="tl_ladaco10_users"
 *
 */
public class DacoUser implements Cloneable {
    private static final long serialVersionUID = -7043502180037866257L;
    private static Logger log = Logger.getLogger(DacoUser.class);

    private Long uid;
    private Long userId;
    private String firstName;
    private String lastName;
    private String loginName;
    private boolean sessionFinished;

    private DacoSession session;
    private Daco daco;
    // =============== NON Persisit value: for display use ===========
    // the user access some reousrce question date time. Use in monitoring summary page
    private Date accessDate;
    private Set<DacoAnswer> answers = new LinkedHashSet<DacoAnswer>();

    public DacoUser() {
    }

    public DacoUser(UserDTO user, DacoSession session) {
	userId = new Long(user.getUserID().intValue());
	firstName = user.getFirstName();
	lastName = user.getLastName();
	loginName = user.getLogin();
	this.session = session;
	daco = null;
	sessionFinished = false;
    }

    public DacoUser(UserDTO user, Daco content) {
	userId = new Long(user.getUserID().intValue());
	firstName = user.getFirstName();
	lastName = user.getLastName();
	loginName = user.getLogin();
	session = null;
	daco = content;
	sessionFinished = false;
    }

    /**
     * Clone method from <code>java.lang.Object</code>
     */
    @Override
    public Object clone() {

	DacoUser user = null;
	try {
	    user = (DacoUser) super.clone();
	    user.setUid(null);
	    // never clone session
	    user.setAnswers(new LinkedHashSet<DacoAnswer>(answers.size()));
	    for (DacoAnswer answer : answers) {
		user.getAnswers().add((DacoAnswer) answer.clone());
	    }
	    user.setSession(null);
	} catch (CloneNotSupportedException e) {
	    DacoUser.log.error("When clone " + DacoUser.class + " failed");
	}

	return user;
    }

    // **********************************************************
    // Get/Set methods
    // **********************************************************
    /**
     * @hibernate.id generator-class="native" type="java.lang.Long" column="uid"
     * @return Returns the uid.
     */
    public Long getUid() {
	return uid;
    }

    /**
     * @param uid
     *            The uid to set.
     */
    public void setUid(Long userID) {
	uid = userID;
    }

    /**
     * @hibernate.property column="user_id" length="20"
     * @return Returns the userId.
     */
    public Long getUserId() {
	return userId;
    }

    /**
     * @param userId
     *            The userId to set.
     */
    public void setUserId(Long userID) {
	userId = userID;
    }

    /**
     * @hibernate.property length="255" column="last_name"
     * @return
     */
    public String getLastName() {
	return lastName;
    }

    public void setLastName(String lastName) {
	this.lastName = lastName;
    }

    /**
     * @hibernate.property length="255" column="first_name"
     * @return
     */
    public String getFirstName() {
	return firstName;
    }

    public void setFirstName(String firstName) {
	this.firstName = firstName;
    }

    /**
     * @hibernate.property column="login_name"
     * @return
     */
    public String getLoginName() {
	return loginName;
    }

    public void setLoginName(String loginName) {
	this.loginName = loginName;
    }

    /**
     * @hibernate.many-to-one column="session_uid" cascade="none" foreign-key="UserToSession"
     * @return
     */
    public DacoSession getSession() {
	return session;
    }

    public void setSession(DacoSession session) {
	this.session = session;
    }

    /**
     * @hibernate.many-to-one column="content_uid" cascade="none" foreign-key="UserToDaco"
     * @return
     */
    public Daco getDaco() {
	return daco;
    }

    public void setDaco(Daco content) {
	daco = content;
    }

    /**
     * @hibernate.property column="session_finished"
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
	if (!(obj instanceof DacoUser)) {
	    return false;
	}

	final DacoUser user = (DacoUser) obj;

	return new EqualsBuilder().append(uid, user.uid).append(firstName, user.firstName)
		.append(lastName, user.lastName).append(loginName, user.loginName).isEquals();

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

    /**
     * @hibernate.set cascade="none" inverse="true" order-by="record_id asc, uid asc" outer-join="true"
     * @hibernate.collection-key column="user_uid"
     * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.tool.daco.model.DacoAnswer"
     */

    public Set<DacoAnswer> getAnswers() {
	return answers;
    }

    public void setAnswers(Set<DacoAnswer> answers) {
	this.answers = answers;
    }

    public String getFullName() {
	return new StringBuilder(getLastName()).append(" ").append(getFirstName()).toString();
    }
}