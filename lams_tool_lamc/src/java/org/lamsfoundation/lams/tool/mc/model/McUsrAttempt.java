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

package org.lamsfoundation.lams.tool.mc.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.Hibernate;
import org.lamsfoundation.lams.qb.model.QbOption;
import org.lamsfoundation.lams.qb.model.QbToolAnswer;

/**
 * <p>
 * Persistent object/bean that defines the user attempt for the MCQ tool. Provides accessors and mutators to get/set
 * attributes It maps to database table: tl_lamc11_usr_attempt
 * </p>
 *
 * @author Ozgur Demirtas
 */
@Entity
@Table(name = "tl_lamc11_usr_attempt")
//in this entity's table primary key is "uid", but it references "answer_uid" in lams_qb_tool_answer
@PrimaryKeyJoinColumn(name = "uid")
public class McUsrAttempt extends QbToolAnswer implements Serializable, Comparable<McUsrAttempt> {
    private static final long serialVersionUID = 4514268732673337338L;

    @Column(name = "attempt_time")
    private Date attemptTime;

    @Column
    private Integer mark;

    @Column(name = "isAttemptCorrect")
    private boolean attemptCorrect;

    @Column
    private boolean passed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "que_usr_id")
    private McQueUsr mcQueUsr;

    @Column(name = "confidence_level")
    private int confidenceLevel;

    @Transient
    private McOptsContent mcOptionsContent;

    public McUsrAttempt(Date attemptTime, McQueContent mcQueContent, McQueUsr mcQueUsr, QbOption qbOption, Integer mark,
	    boolean passed, boolean attemptCorrect, int confidenceLevel) {
	this.attemptTime = attemptTime;
	this.qbToolQuestion = mcQueContent;
	this.mcQueUsr = mcQueUsr;
	this.qbOption = qbOption;
	this.mark = mark;
	this.passed = passed;
	this.attemptCorrect = attemptCorrect;
	this.confidenceLevel = confidenceLevel;
    }

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

    public Date getAttemptTime() {
	return this.attemptTime;
    }

    public void setAttemptTime(Date attemptTime) {
	this.attemptTime = attemptTime;
    }

    public McQueContent getMcQueContent() {
	return (McQueContent) Hibernate.unproxy(qbToolQuestion);
    }

    public void setMcQueContent(McQueContent mcQueContent) {
	this.qbToolQuestion = mcQueContent;
    }

    public org.lamsfoundation.lams.tool.mc.model.McQueUsr getMcQueUsr() {
	return this.mcQueUsr;
    }

    public void setMcQueUsr(org.lamsfoundation.lams.tool.mc.model.McQueUsr mcQueUsr) {
	this.mcQueUsr = mcQueUsr;
    }

    public McOptsContent getMcOptionsContent() {
	if (mcOptionsContent == null) {
	    mcOptionsContent = new McOptsContent();
	    mcOptionsContent.setQbOption(qbOption);
	}
	return mcOptionsContent;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("uid", getUid()).toString();
    }

    public Integer getMark() {
	return mark;
    }

    public void setMark(Integer mark) {
	this.mark = mark;
    }

    public boolean isPassed() {
	return passed;
    }

    public void setPassed(boolean isPassed) {
	this.passed = isPassed;
    }

    public boolean isAttemptCorrect() {
	return attemptCorrect;
    }

    public void setAttemptCorrect(boolean attemptCorrect) {
	this.attemptCorrect = attemptCorrect;
    }

    public int getConfidenceLevel() {
	return confidenceLevel;
    }

    public void setConfidenceLevel(int confidenceLevel) {
	this.confidenceLevel = confidenceLevel;
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

    @Override
    public int compareTo(McUsrAttempt other) {
	// if the object does not exist yet, then just return any one of 0, -1, 1. Should not make a difference.
	if (uid == null) {
	    return 1;
	} else {
	    return (int) (uid.longValue() - other.uid.longValue());
	}
    }
}