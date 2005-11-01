/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.learningdesign.dto.TransitionDTO;

/**
 * @author Manpreet Minhas
 * @hibernate.class table="lams_learning_transition"
 *  
 */
public class Transition implements Serializable {

	/** identifier field */
	private Long transitionId;

	/** nullable persistent field */
	private Integer transitionUIID;

	/** nullable persistent field */
	private Integer toUIID;

	/** nullable persistent field */
	private Integer fromUIID;

	/** nullable persistent field */
	private String description;

	/** nullable persistent field */
	private String title;

	/** persistent field */
	private Date createDateTime;

	/** persistent field */
	Activity toActivity;

	/** persistent field */
	Activity fromActivity;

	/** persistent field */
	LearningDesign learningDesign;

	/*
	 * If the value for createDateTime is null then it will
	 * be assigned the default value equal to the current datetime 
	 */
	/** full constructor */
	public Transition(
			Long transitionId,
			Integer id,
			String description,
			String title,
			Date createDateTime,
			Activity toActivity,
			Activity fromActivity,
			LearningDesign learningDesign,
			Integer toUIID,
			Integer fromUIID) {
		this.transitionId = transitionId;
		this.transitionUIID = id;
		this.description = description;
		this.title = title;
		this.createDateTime = createDateTime != null ? createDateTime : new Date();
		this.toActivity = toActivity;
		this.fromActivity = fromActivity;
		this.learningDesign = learningDesign;
		this.toUIID = toUIID;
		this.fromUIID = fromUIID;
	}

	/** default constructor */
	public Transition() {
	    this.createDateTime = new Date(); //default value is set to when the Transition object is created
	}

	/** minimal constructor */
	public Transition(
			Long transitionId,
			Date createDateTime,
			Activity toActivity,
			Activity fromActivity,
			LearningDesign learningDesign) {
		this.transitionId = transitionId;
		this.createDateTime = createDateTime != null ? createDateTime : new Date();
		this.toActivity = toActivity;
		this.fromActivity = fromActivity;
		this.learningDesign = learningDesign;
	}

	/**
	 * Makes a copy of the Transition for authoring, preview and monitoring enviornment
	 * 
	 * @param originalTransition The transition to be deep-copied
	 * @return Transition Returns a deep-copy o fthe originalTransition
	 */
	public static Transition createCopy(Transition originalTransition){
		Transition newTransition = new Transition();
		
		newTransition.setTransitionUIID(originalTransition.getTransitionUIID());
		newTransition.setDescription(originalTransition.getDescription());
		newTransition.setTitle(originalTransition.getTitle());
		newTransition.setCreateDateTime(new Date());
		newTransition.setToUIID(originalTransition.getToUIID());
		newTransition.setFromUIID(originalTransition.getFromUIID());		
		return newTransition;
	}

	/**
	 * @hibernate.transitionUIID generator-class="identity"
	 *                           type="java.lang.Long" column="transition_id"
	 *  
	 */
	public Long getTransitionId() {
		return this.transitionId;
	}

	public void setTransitionId(Long transitionId) {
		this.transitionId = transitionId;
	}

	/**
	 * @hibernate.property column="transitionUIID" length="11"
	 *  
	 */
	public Integer getTransitionUIID() {
		return this.transitionUIID;
	}

	public void setTransitionUIID(Integer id) {
		this.transitionUIID = id;
	}

	/**
	 * @hibernate.property column="description" length="65535"
	 *  
	 */
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @hibernate.property column="title" length="255"
	 *  
	 */
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @hibernate.property column="create_date_time" length="19" not-null="true"
	 *  
	 */
	public Date getCreateDateTime() {
		return this.createDateTime;
	}

	public void setCreateDateTime(Date createDateTime) {
		this.createDateTime = createDateTime != null ? createDateTime : new Date();
	}

	/**
	 * @hibernate.many-to-one not-null="true"
	 * @hibernate.column name="to_activity_id"
	 *  
	 */
	public org.lamsfoundation.lams.learningdesign.Activity getToActivity() {
		return this.toActivity;
	}

	public void setToActivity(
			org.lamsfoundation.lams.learningdesign.Activity toActivity) {
		this.toActivity = toActivity;
	}

	/**
	 * @hibernate.many-to-one not-null="true"
	 * @hibernate.column name="from_activity_id"
	 *  
	 */
	public org.lamsfoundation.lams.learningdesign.Activity getFromActivity() {
		return this.fromActivity;
	}

	public void setFromActivity(
			org.lamsfoundation.lams.learningdesign.Activity fromActivity) {
		this.fromActivity = fromActivity;
	}

	/**
	 * @hibernate.many-to-one not-null="true"
	 * @hibernate.column name="learning_design_id"
	 *  
	 */
	public org.lamsfoundation.lams.learningdesign.LearningDesign getLearningDesign() {
		return this.learningDesign;
	}

	public void setLearningDesign(
			org.lamsfoundation.lams.learningdesign.LearningDesign learningDesign) {
		this.learningDesign = learningDesign;
	}

	public String toString() {
		return new ToStringBuilder(this).append("transitionId",
				getTransitionId()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Transition))
			return false;
		Transition castOther = (Transition) other;
		return new EqualsBuilder().append(this.getTransitionId(),
				castOther.getTransitionId()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getTransitionId()).toHashCode();
	}

	public Integer getFromUIID() {
		return fromUIID;
	}

	public void setFromUIID(Integer fromUIID) {
		this.fromUIID = fromUIID;
	}

	public Integer getToUIID() {
		return toUIID;
	}

	public void setToUIID(Integer toUIID) {
		this.toUIID = toUIID;
	}
	public TransitionDTO getTransitionDTO(){
		return new TransitionDTO(this);
	}
}