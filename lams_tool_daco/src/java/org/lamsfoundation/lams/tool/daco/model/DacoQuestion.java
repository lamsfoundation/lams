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
/* $$Id$$ */
package org.lamsfoundation.lams.tool.daco.model;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * DacoQuestion
 *
 * @author Marcin Cieslak
 *
 * @hibernate.class table="tl_ladaco10_questions"
 *
 */
public class DacoQuestion implements Cloneable {
    private static final Logger log = Logger.getLogger(DacoQuestion.class);

    private Long uid;

    private short type;

    private Float min;

    private Float max;

    private Short digitsDecimal;

    private Short summary;

    private String description;

    private String initialQuestion;

    private Set<DacoAnswerOption> answerOptions = new LinkedHashSet<DacoAnswerOption>();

    private boolean isRequired;

    private Date createDate;
    private DacoUser createBy;

    private Daco daco;

    @Override
    public Object clone() {
	DacoQuestion obj = null;
	try {
	    obj = (DacoQuestion) super.clone();
	    // clone answer options
	    obj.setAnswerOptions(new LinkedHashSet<DacoAnswerOption>(answerOptions.size()));
	    for (DacoAnswerOption answerOption : answerOptions) {
		obj.getAnswerOptions().add((DacoAnswerOption) answerOption.clone());
	    }
	    obj.setUid(null);
	    // clone ReourceUser as well
	    if (createBy != null) {
		obj.setCreateBy((DacoUser) createBy.clone());
	    }

	} catch (CloneNotSupportedException e) {
	    DacoQuestion.log.error("When clone " + DacoQuestion.class + " failed");
	}

	return obj;
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
    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     * @hibernate.property column="description"
     * @return
     */
    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    /**
     * @hibernate.many-to-one cascade="none" column="create_by" foreign-key="QuestionToUser"
     * 
     * @return
     */
    public DacoUser getCreateBy() {
	return createBy;
    }

    public void setCreateBy(DacoUser createBy) {
	this.createBy = createBy;
    }

    /**
     * @hibernate.property column="create_date"
     * @return
     */
    public Date getCreateDate() {
	return createDate;
    }

    public void setCreateDate(Date createDate) {
	this.createDate = createDate;
    }

    /**
     * @hibernate.property column="is_required"
     * @return
     */
    public boolean isRequired() {
	return isRequired;
    }

    public void setRequired(boolean isRequired) {
	this.isRequired = isRequired;
    }

    /**
     * @hibernate.property column="question_type"
     * @return
     */
    public short getType() {
	return type;
    }

    public void setType(short type) {
	this.type = type;
    }

    /**
     * @hibernate.property column="min_constraint"
     * @return
     */
    public Float getMin() {
	return min;
    }

    public void setMin(Float min) {
	this.min = min;
    }

    /**
     * @hibernate.property column="max_constraint"
     * @return
     */
    public Float getMax() {
	return max;
    }

    public void setMax(Float max) {
	this.max = max;
    }

    /**
     * @hibernate.property column="digits_decimal"
     * @return
     */
    public Short getDigitsDecimal() {
	return digitsDecimal;
    }

    public void setDigitsDecimal(Short digitsDecimal) {
	this.digitsDecimal = digitsDecimal;
    }

    /**
     * @hibernate.property column="summary"
     * @return
     */
    public Short getSummary() {
	return summary;
    }

    public void setSummary(Short summary) {
	this.summary = summary;
    }

    /**
     * @hibernate.set cascade="all" order-by="sequence_num asc"
     * @hibernate.collection-key column="question_uid"
     * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.tool.daco.model.DacoAnswerOption"
     * @return
     */
    public Set<DacoAnswerOption> getAnswerOptions() {
	return answerOptions;
    }

    public void setAnswerOptions(Set<DacoAnswerOption> options) {
	answerOptions = options;
    }

    /*
     * public Long getContentUid() {
     * return contentUid;
     * }
     * 
     * public void setContentUid(Long contentUid) {
     * this.contentUid = contentUid;
     * }
     */

    /**
     * @hibernate.many-to-one column="content_uid" cascade="none" foreign-key="QuestionToDaco" insert="false"
     *                        update="false"
     * @return
     */
    public Daco getDaco() {
	return daco;
    }

    public void setDaco(Daco daco) {
	this.daco = daco;
    }

}