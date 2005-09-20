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
package org.lamsfoundation.lams.learningdesign.dto;

import java.util.Date;

import org.lamsfoundation.lams.learningdesign.Transition;
import org.lamsfoundation.lams.util.wddx.WDDXTAGS;


/**
 * @author Manpreet Minhas
 */
public class TransitionDTO extends BaseDTO{
	
	private Long transitionID;	
	private Integer transitionUIID;
	private Integer toUIID;
	private Integer fromUIID;
	private String description;
	private String title;
	private Date createDateTime;
	private Long toActivityID;
	private Long fromActivityID;
	private Long learningDesignID;
	
	public TransitionDTO(){
		
	}
	public TransitionDTO(Long transitionId, Integer transitionUIID,
			Integer toUIID, Integer fromUIID, String description, String title,
			Date createDateTime, Long toActivityID, Long fromActivityID,
			Long learningDesignID) {
		super();
		this.transitionID = transitionId;
		this.transitionUIID = transitionUIID;
		this.toUIID = toUIID;
		this.fromUIID = fromUIID;
		this.description = description;
		this.title = title;
		this.createDateTime = createDateTime;
		this.toActivityID = toActivityID;
		this.fromActivityID = fromActivityID;
		this.learningDesignID = learningDesignID;
	}
	public TransitionDTO(Transition transition){
		this.transitionID = transition.getTransitionId();
		this.transitionUIID = transition.getTransitionUIID();
		this.toUIID = transition.getToUIID();
		this.fromUIID = transition.getFromUIID();
		this.description = transition.getDescription();
		this.title = transition.getTitle();
		this.createDateTime = transition.getCreateDateTime();
		this.toActivityID = transition.getToActivity().getActivityId();
		this.fromActivityID = transition.getFromActivity().getActivityId();							  
		this.learningDesignID = transition.getLearningDesign().getLearningDesignId();
	}
	/**
	 * @return Returns the createDateTime.
	 */
	public Date getCreateDateTime() {
		return createDateTime!=null?createDateTime:WDDXTAGS.DATE_NULL_VALUE;
	}
	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description!=null?description:WDDXTAGS.STRING_NULL_VALUE;
	}
	/**
	 * @return Returns the fromActivityID.
	 */
	public Long getFromActivityID() {
		return fromActivityID!=null?fromActivityID:WDDXTAGS.NUMERIC_NULL_VALUE_LONG;
	}
	/**
	 * @return Returns the fromUIID.
	 */
	public Integer getFromUIID() {
		return fromUIID!=null?fromUIID:WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
	}
	/**
	 * @return Returns the learningDesignID.
	 */
	public Long getLearningDesignID() {
		return learningDesignID!=null?learningDesignID:WDDXTAGS.NUMERIC_NULL_VALUE_LONG;
	}
	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title!=null?title:WDDXTAGS.STRING_NULL_VALUE;
	}
	/**
	 * @return Returns the toActivityID.
	 */
	public Long getToActivityID() {
		return toActivityID!=null?toActivityID:WDDXTAGS.NUMERIC_NULL_VALUE_LONG;
	}
	/**
	 * @return Returns the toUIID.
	 */
	public Integer getToUIID() {
		return toUIID!=null?toUIID:WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
	}
	/**
	 * @return Returns the transitionID.
	 */
	public Long getTransitionID() {
		return transitionID!=null?transitionID:WDDXTAGS.NUMERIC_NULL_VALUE_LONG;
	}
	/**
	 * @return Returns the transitionUIID.
	 */
	public Integer getTransitionUIID() {
		return transitionUIID!=null?transitionUIID:WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
	}
	/**
	 * @param createDateTime The createDateTime to set.
	 */
	public void setCreateDateTime(Date createDateTime) {
		if(!createDateTime.equals(WDDXTAGS.DATE_NULL_VALUE))
			this.createDateTime = createDateTime;
	}
	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		if(!description.equals(WDDXTAGS.STRING_NULL_VALUE))
			this.description = description;
	}
	/**
	 * @param fromActivityID The fromActivityID to set.
	 */
	public void setFromActivityID(Long fromActivityID) {
		if(!fromActivityID.equals(WDDXTAGS.NUMERIC_NULL_VALUE_LONG))
			this.fromActivityID = fromActivityID;
	}
	/**
	 * @param fromUIID The fromUIID to set.
	 */
	public void setFromUIID(Integer fromUIID) {
		if(!fromUIID.equals(WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER))
			this.fromUIID = fromUIID;
	}
	/**
	 * @param learningDesignID The learningDesignID to set.
	 */
	public void setLearningDesignID(Long learningDesignID) {
		if(!learningDesignID.equals(WDDXTAGS.NUMERIC_NULL_VALUE_LONG))
			this.learningDesignID = learningDesignID;
	}
	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title) {
		if(!title.equals(WDDXTAGS.STRING_NULL_VALUE))
			this.title = title;
	}
	/**
	 * @param toActivityID The toActivityID to set.
	 */
	public void setToActivityID(Long toActivityID) {
		if(!toActivityID.equals(WDDXTAGS.NUMERIC_NULL_VALUE_LONG))
			this.toActivityID = toActivityID;
	}
	/**
	 * @param toUIID The toUIID to set.
	 */
	public void setToUIID(Integer toUIID) {
		if(!toUIID.equals(WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER))
			this.toUIID = toUIID;
	}
	/**
	 * @param transitionID The transitionID to set.
	 */
	public void setTransitionID(Long transitionId) {
		if(!transitionId.equals(WDDXTAGS.NUMERIC_NULL_VALUE_LONG))
			this.transitionID = transitionId;
	}
	/**
	 * @param transitionUIID The transitionUIID to set.
	 */
	public void setTransitionUIID(Integer transitionUIID) {
		if(!transitionUIID.equals(WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER))
			this.transitionUIID = transitionUIID;
	}
}
