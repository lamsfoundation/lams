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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.apache.log4j.Logger;

/**
 * DacoQuestion
 *
 * @author Marcin Cieslak
 *
 *
 *
 */
@Entity
@Table(name = "tl_ladaco10_questions")
public class DacoQuestion implements Cloneable {
    private static final Logger log = Logger.getLogger(DacoQuestion.class);

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(name = "question_type")
    private short type;

    @Column(name = "min_constraint")
    private Float min;

    @Column(name = "max_constraint")
    private Float max;

    @Column(name = "digits_decimal")
    private Short digitsDecimal;

    @Column
    private Short summary;

    @Column
    private String description;

    @OneToMany(fetch = FetchType.EAGER,
	    cascade = CascadeType.ALL)
    @JoinColumn(name = "question_uid")
    @OrderBy("sequenceNumber ASC")
    private Set<DacoAnswerOption> answerOptions = new LinkedHashSet<DacoAnswerOption>();

    @Column(name = "is_required")
    private boolean isRequired;

    @Column(name = "create_date")
    private Date createDate;
    
    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "create_by") 
    private DacoUser createBy;

    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "content_uid") 
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

    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public DacoUser getCreateBy() {
	return createBy;
    }

    public void setCreateBy(DacoUser createBy) {
	this.createBy = createBy;
    }

    public Date getCreateDate() {
	return createDate;
    }

    public void setCreateDate(Date createDate) {
	this.createDate = createDate;
    }

    public boolean isRequired() {
	return isRequired;
    }

    public void setRequired(boolean isRequired) {
	this.isRequired = isRequired;
    }

    public short getType() {
	return type;
    }

    public void setType(short type) {
	this.type = type;
    }

    public Float getMin() {
	return min;
    }

    public void setMin(Float min) {
	this.min = min;
    }

    public Float getMax() {
	return max;
    }

    public void setMax(Float max) {
	this.max = max;
    }

    public Short getDigitsDecimal() {
	return digitsDecimal;
    }

    public void setDigitsDecimal(Short digitsDecimal) {
	this.digitsDecimal = digitsDecimal;
    }

    public Short getSummary() {
	return summary;
    }

    public void setSummary(Short summary) {
	this.summary = summary;
    }

    public Set<DacoAnswerOption> getAnswerOptions() {
	return answerOptions;
    }

    public void setAnswerOptions(Set<DacoAnswerOption> options) {
	answerOptions = options;
    }

    public Daco getDaco() {
	return daco;
    }

    public void setDaco(Daco daco) {
	this.daco = daco;
    }

}