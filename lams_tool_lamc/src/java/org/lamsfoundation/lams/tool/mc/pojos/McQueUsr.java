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

package org.lamsfoundation.lams.tool.mc.pojos;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <p>
 * Persistent object/bean that defines the user for the MCQ tool. Provides accessors and mutators to get/set attributes
 * It maps to database table: tl_lamc11_que_usr
 * </p>
 *
 * @author Ozgur Demirtas
 */
public class McQueUsr implements Serializable, Comparable<McQueUsr> {

    /** identifier field */
    private Long uid;

    /** persistent field */
    private Long queUsrId;

    /** nullable persistent field */
    private String username;

    /** nullable persistent field */
    private String fullname;

    private boolean responseFinalised;

    //please pay attention this is *sessionUid* and not sessionId (this is due to Ozgur gave wrong name to this field)
    private Long mcSessionId;

    /** nullable persistent field */
    private McSession mcSession;

    private Integer numberOfAttempts;

    private Integer lastAttemptTotalMark;

    /** default constructor */
    public McQueUsr() {
    }

    /** full constructor */
    public McQueUsr(Long queUsrId, String username, String fullname, McSession mcSession, Set mcUsrAttempts) {
	this.queUsrId = queUsrId;
	this.username = username;
	this.fullname = fullname;
	this.mcSession = mcSession;
	this.numberOfAttempts = 0;
    }

    /**
     * @return Returns the responseFinalised.
     */
    public boolean isResponseFinalised() {
	return responseFinalised;
    }

    /**
     * @param responseFinalised
     *            The responseFinalised to set.
     */
    public void setResponseFinalised(boolean responseFinalised) {
	this.responseFinalised = responseFinalised;
    }

    public Long getUid() {
	return this.uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public Long getQueUsrId() {
	return this.queUsrId;
    }

    public void setQueUsrId(Long queUsrId) {
	this.queUsrId = queUsrId;
    }

    public String getUsername() {
	return this.username;
    }

    public void setUsername(String username) {
	this.username = username;
    }

    public String getFullname() {
	return this.fullname;
    }

    public void setFullname(String fullname) {
	this.fullname = fullname;
    }

    public McSession getMcSession() {
	return this.mcSession;
    }

    public void setMcSession(McSession mcSession) {
	this.mcSession = mcSession;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("uid", getUid()).append("queUsrId", getQueUsrId())
		.append("username", getUsername()).append("fullname", getFullname())
		.append("responseFinalised", isResponseFinalised()).append("mcSessionId", getMcSessionId())
		.append("numberOfAttempts", getNumberOfAttempts())
		.append("lastAttemptTotalMark", getLastAttemptTotalMark()).toString();
    }

    /**
     * Please pay attention this is *sessionUid* and not sessionId (this is due to Ozgur gave wrongly name to this
     * field)
     *
     * @return Returns the mcSessionId.
     */
    public Long getMcSessionId() {
	return mcSessionId;
    }

    /**
     * @param mcSessionId
     *            The mcSessionId to set.
     */
    public void setMcSessionId(Long mcSessionId) {
	this.mcSessionId = mcSessionId;
    }

    public Integer getNumberOfAttempts() {
	return numberOfAttempts;
    }

    public void setNumberOfAttempts(Integer numberOfAttempts) {
	this.numberOfAttempts = numberOfAttempts;
    }

    public Integer getLastAttemptTotalMark() {
	return lastAttemptTotalMark;
    }

    public void setLastAttemptTotalMark(Integer lastAttemptTotalMark) {
	this.lastAttemptTotalMark = lastAttemptTotalMark;
    }

    /**
     * Is the latest attempt a pass? True if and only if passmark is applicable for the related content and the user's
     * lastAttemptTotalMark >= passmark.
     */
    public boolean isLastAttemptMarkPassed() {
	return isMarkPassed(lastAttemptTotalMark);
    }

    /**
     * Does this mark count as a pass? True if and only if passmark is applicable for the related content and the given
     * mark >= passmark. Used to calculate if the user has passed before setting up the learner's attempts.
     */
    public boolean isMarkPassed(Integer mark) {
	McContent content = mcSession.getMcContent();
	if (mark != null && content.isPassMarkApplicable()) {
	    Integer passMark = content.getPassMark();
	    return passMark != null && (mark.compareTo(passMark) >= 0);
	}
	return false;
    }

    @Override
    public int compareTo(McQueUsr other) {
	// if the object does not exist yet, then just return any one of 0, -1, 1. Should not make a difference.
	if (uid == null) {
	    return 1;
	} else {
	    return (int) (uid.longValue() - other.uid.longValue());
	}
    }

}
