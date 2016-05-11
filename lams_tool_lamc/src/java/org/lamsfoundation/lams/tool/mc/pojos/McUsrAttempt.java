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
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <p>
 * Persistent object/bean that defines the user attempt for the MCQ tool. Provides accessors and mutators to get/set
 * attributes It maps to database table: tl_lamc11_usr_attempt
 * </p>
 *
 * @author Ozgur Demirtas
 */
public class McUsrAttempt implements Serializable {

    private static final long serialVersionUID = 4514268732673337338L;

    /** identifier field */
    private Long uid;

    /** nullable persistent field */
    private Date attemptTime;

    private Integer mark;

    private boolean attemptCorrect;

    private boolean passed;

    private Long queUsrId;

    private Long mcQueContentId;

    /** persistent field */
    private McQueContent mcQueContent;

    /** persistent field */
    private McQueUsr mcQueUsr;

    /** persistent field */
    private McOptsContent mcOptionsContent;

    public McUsrAttempt(Date attemptTime, McQueContent mcQueContent, McQueUsr mcQueUsr, McOptsContent mcOptionsContent,
	    Integer mark, boolean passed, boolean attemptCorrect) {
	this.attemptTime = attemptTime;
	this.mcQueContent = mcQueContent;
	this.mcQueUsr = mcQueUsr;
	this.mcOptionsContent = mcOptionsContent;
	this.mark = mark;
	this.passed = passed;
	this.attemptCorrect = attemptCorrect;
    }

    /** default constructor */
    public McUsrAttempt() {
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((uid == null) ? 0 : uid.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	McUsrAttempt other = (McUsrAttempt) obj;
	if (uid == null) {
	    if (other.uid != null) {
		return false;
	    }
	} else if (!uid.equals(other.uid)) {
	    return false;
	}
	return true;
    }

    public Long getUid() {
	return this.uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public Date getAttemptTime() {
	return this.attemptTime;
    }

    public void setAttemptTime(Date attemptTime) {
	this.attemptTime = attemptTime;
    }

    public org.lamsfoundation.lams.tool.mc.pojos.McQueContent getMcQueContent() {
	return this.mcQueContent;
    }

    public void setMcQueContent(org.lamsfoundation.lams.tool.mc.pojos.McQueContent mcQueContent) {
	this.mcQueContent = mcQueContent;
    }

    public org.lamsfoundation.lams.tool.mc.pojos.McQueUsr getMcQueUsr() {
	return this.mcQueUsr;
    }

    public void setMcQueUsr(org.lamsfoundation.lams.tool.mc.pojos.McQueUsr mcQueUsr) {
	this.mcQueUsr = mcQueUsr;
    }

    public org.lamsfoundation.lams.tool.mc.pojos.McOptsContent getMcOptionsContent() {
	return this.mcOptionsContent;
    }

    public void setMcOptionsContent(org.lamsfoundation.lams.tool.mc.pojos.McOptsContent mcOptionsContent) {
	this.mcOptionsContent = mcOptionsContent;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("uid", getUid()).toString();
    }

    /**
     * @return Returns the mark.
     */
    public Integer getMark() {
	return mark;
    }

    /**
     * @param mark
     *            The mark to set.
     */
    public void setMark(Integer mark) {
	this.mark = mark;
    }

    /**
     * @return Returns the passed.
     */
    public boolean isPassed() {
	return passed;
    }

    /**
     * @param passed
     *            The passed to set.
     */
    public void setPassed(boolean isPassed) {
	this.passed = isPassed;
    }

    /**
     * @return Returns the queUsrId.
     */
    public Long getQueUsrId() {
	return queUsrId;
    }

    /**
     * @param queUsrId
     *            The queUsrId to set.
     */
    public void setQueUsrId(Long queUsrId) {
	this.queUsrId = queUsrId;
    }

    /**
     * @return Returns the mcQueContentId.
     */
    public Long getMcQueContentId() {
	return mcQueContentId;
    }

    /**
     * @param mcQueContentId
     *            The mcQueContentId to set.
     */
    public void setMcQueContentId(Long mcQueContentId) {
	this.mcQueContentId = mcQueContentId;
    }

    /**
     * @return Returns the attemptCorrect.
     */
    public boolean isAttemptCorrect() {
	return attemptCorrect;
    }

    /**
     * @param attemptCorrect
     *            The attemptCorrect to set.
     */
    public void setAttemptCorrect(boolean attemptCorrect) {
	this.attemptCorrect = attemptCorrect;
    }

    /**
     * Get the mark for displaying to the user. If retries or passmark is off, then just check whether or not answer is
     * correct If retries and passmark is on, then we only want the marks if the user has passed!
     */
    public Integer getMarkForShow(boolean allowRetries) {
	//TODO check if we really allowed to return full mark
//	if (isAttemptCorrect() && (!allowRetries || (allowRetries && isPassed()))) {
//	    return getMark();
//	} else {
//	    return new Integer(0);
//	}

	return getMark();
    }

}
