/*
 * Created on Mar 30, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.learningdesign.dto;

import java.util.Date;
import java.util.Hashtable;

import org.lamsfoundation.lams.learningdesign.Transition;
import org.lamsfoundation.lams.util.wddx.WDDXTAGS;


/**
 * @author Minhas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TransitionDTO extends BaseDTO{
	
	private Long transitionId;	
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
		this.transitionId = transitionId;
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
		this.transitionId = transition.getTransitionId();
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
	public TransitionDTO(Hashtable transitionDetails){
		if(transitionDetails.containsKey("transitionId"))
			transitionId= convertToLong(transitionDetails.get("transitionId"));
		if(transitionDetails.containsKey("transitionUIID"))
			transitionUIID=convertToInteger(transitionDetails.get("transitionUIID"));
		if(transitionDetails.containsKey("toUIID"))
			toUIID=convertToInteger(transitionDetails.get("toUIID"));
		if(transitionDetails.containsKey("fromUIID"))
			fromUIID=convertToInteger(transitionDetails.get("fromUIID"));
		if(transitionDetails.containsKey("description"))
			description=(String)transitionDetails.get("description");
		if(transitionDetails.containsKey("title"))
			title=(String)transitionDetails.get("title");
		if(transitionDetails.containsKey("createDateTime"))
			createDateTime=(Date)transitionDetails.get("createDateTime");
		if(transitionDetails.containsKey("toActivityID"))
			toActivityID=convertToLong(transitionDetails.get("toActivityID"));
		if(transitionDetails.containsKey("fromActivityID"))
			fromActivityID=convertToLong(transitionDetails.get("fromActivityID"));
		if(transitionDetails.containsKey("learningDesignID"))
			learningDesignID=convertToLong(transitionDetails.get("learningDesignID"));
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
	 * @return Returns the transitionId.
	 */
	public Long getTransitionId() {
		return transitionId!=null?transitionId:WDDXTAGS.NUMERIC_NULL_VALUE_LONG;
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
	 * @param transitionId The transitionId to set.
	 */
	public void setTransitionId(Long transitionId) {
		if(!transitionId.equals(WDDXTAGS.NUMERIC_NULL_VALUE_LONG))
			this.transitionId = transitionId;
	}
	/**
	 * @param transitionUIID The transitionUIID to set.
	 */
	public void setTransitionUIID(Integer transitionUIID) {
		if(!transitionUIID.equals(WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER))
			this.transitionUIID = transitionUIID;
	}
}
