/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0 
 * as published by the Free Software Foundation.
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
package org.lamsfoundation.lams.learningdesign.dto;


import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ActivityDTOOrderComparator;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.Transition;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.ActivityDAO;
import org.lamsfoundation.lams.util.wddx.WDDXTAGS;

/**
 * @author Manpreet Minhas
 */
public class LearningDesignDTO extends BaseDTO{
	
	private Long learningDesignID;
	private Integer learningDesignUIID;
	private String description;
	private String title;	
	private Long firstActivityID;
	private Integer firstActivityUIID;
	private Integer maxID;	
	private Boolean validDesign;
	private Boolean readOnly;
	private Boolean editOverrideLock;
	private Date dateReadOnly;
	private Integer userID;
	private Integer editOverrideUserID;
	private String editOverrideUserFullName;
	private String helpText;
	private Integer copyTypeID;
	private Date createDateTime;
	private String version;	
	private Integer designVersion;
	private Long originalLearningDesignID;
	private Integer workspaceFolderID;
	private Long duration;
	private String licenseText;
	private Long licenseID;
	private Date lastModifiedDateTime;
	private String contentFolderID;
	private String offlineInstructions;
	private String onlineInstructions;
	
	/* Groupings Array which contain the groupings objects, which had been created by the grouping activities
	 * in this learning design.
	 */
	private ArrayList groupings;
	private ArrayList activities;
	private ArrayList transitions;
	
	public LearningDesignDTO(){		
	}
	public LearningDesignDTO(Long learningDesignId,
							 Integer learningDesignUIID,
							 String description,
							 String title,
							 Long firstActivityID,
							 Integer firstActivityUIID,
							 Integer maxId,
							 Boolean validDesign, 
							 Boolean readOnly,
							 Boolean editOverrideLock,
							 Date dateReadOnly,
							 Integer userID,
							 Integer editOverrideUserID,
							 String editOverrideUserFullName,
							 String helpText,
							 Integer copyTypeID,
							 Date createDateTime,
							 String version,
							 Integer designVersion,
							 Long originalLearningDesignID,
							 Integer workspaceFolderID,
							 Long duration, 
							 String licenseText,
							 Long licenseID,
							 Date lastModifiedDateTime,
							 String offlineInstructions, 
							 String onlineInstructions) {
		super();
		this.learningDesignID = learningDesignId;
		this.learningDesignUIID = learningDesignUIID;
		this.description = description;
		this.title = title;
		this.firstActivityID = firstActivityID;
		this.firstActivityUIID = firstActivityUIID;
		this.maxID = maxId;
		this.validDesign = validDesign;
		this.readOnly = readOnly;
		this.editOverrideLock = editOverrideLock;
		this.dateReadOnly = dateReadOnly;
		this.offlineInstructions = offlineInstructions;
		this.onlineInstructions = onlineInstructions;
		this.userID = userID;
		this.editOverrideUserID = editOverrideUserID;
		this.editOverrideUserFullName = editOverrideUserFullName;
		this.helpText = helpText;
		this.copyTypeID = copyTypeID;
		this.createDateTime = createDateTime;
		this.version = version;
		this.designVersion = designVersion;
		this.originalLearningDesignID = originalLearningDesignID;
		this.workspaceFolderID = workspaceFolderID;
		this.duration = duration;
		this.licenseText = licenseText;
		this.licenseID = licenseID;
		this.lastModifiedDateTime = lastModifiedDateTime;
		this.groupings = new ArrayList();
		this.activities = new ArrayList();
		this.transitions = new ArrayList();
	}	
	public LearningDesignDTO(LearningDesign learningDesign, ActivityDAO activityDAO){
		this.learningDesignID = learningDesign.getLearningDesignId();
		this.learningDesignUIID = learningDesign.getLearningDesignUIID();
		this.description = learningDesign.getDescription();
		this.title = learningDesign.getTitle();
		this.firstActivityID = learningDesign.getFirstActivity()!=null?
							   learningDesign.getFirstActivity().getActivityId():
							   null;
		this.firstActivityUIID = learningDesign.getFirstActivity()!=null?
								 learningDesign.getFirstActivity().getActivityUIID():
								 null;
		this.maxID = learningDesign.getMaxID();
		this.validDesign = learningDesign.getValidDesign();
		this.designVersion = learningDesign.getDesignVersion();
		this.readOnly = learningDesign.getReadOnly();
		this.editOverrideLock = learningDesign.getEditOverrideLock();
		this.dateReadOnly = learningDesign.getDateReadOnly();
		
		this.offlineInstructions = learningDesign.getOfflineInstructions();	
		this.onlineInstructions = learningDesign.getOnlineInstructions();
		
		this.userID = learningDesign.getUser()!=null?
					  learningDesign.getUser().getUserId():
					  null;
					  
		this.editOverrideUserID = learningDesign.getEditOverrideUser()!=null?
							   learningDesign.getEditOverrideUser().getUserId():
							   null;
		this.editOverrideUserFullName = learningDesign.getEditOverrideUser()!=null?
				   learningDesign.getEditOverrideUser().getFullName():
					   null;
		
					  this.helpText = learningDesign.getHelpText();
		this.copyTypeID = learningDesign.getCopyTypeID();
		this.contentFolderID = learningDesign.getContentFolderID();
		this.createDateTime = learningDesign.getCreateDateTime();
		this.version = learningDesign.getVersion();
		
		this.originalLearningDesignID = learningDesign.getOriginalLearningDesign()!=null?
									  learningDesign.getOriginalLearningDesign().getLearningDesignId():
									  null;
		
	    this.workspaceFolderID = learningDesign.getWorkspaceFolder()!=null?
								 learningDesign.getWorkspaceFolder().getWorkspaceFolderId():
								 null;
								 
		this.duration = learningDesign.getDuration();
		this.licenseText = learningDesign.getLicenseText();
		
		this.licenseID = learningDesign.getLicense()!=null?
						 learningDesign.getLicense().getLicenseID():
						 null;
		
		this.lastModifiedDateTime = learningDesign.getLastModifiedDateTime();
		this.groupings = populateGroupings(learningDesign,activityDAO);
		this.activities = populateActivities(learningDesign);
		this.transitions = populateTransitions(learningDesign);
		
	}
	
	/**
	 * 
	 * @return Returns the contentFolderID
	 */
	public String getContentFolderID() {
		return contentFolderID;
	}

	/**
	 * @return Returns the copyTypeID.
	 */
	public Integer getCopyTypeID() {
		//return copyTypeID!=null?copyTypeID:WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
	    return copyTypeID;
	}
	/**
	 * @return Returns the createDateTime.
	 */
	public Date getCreateDateTime() {
		//return createDateTime!=null?createDateTime:WDDXTAGS.DATE_NULL_VALUE;
	    return createDateTime;
	}
	/**
	 * @return Returns the dateReadOnly.
	 */
	public Date getDateReadOnly() {
		//return dateReadOnly!=null?dateReadOnly:WDDXTAGS.DATE_NULL_VALUE;
	    return dateReadOnly;
	}
	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		//return description!=null?description:WDDXTAGS.STRING_NULL_VALUE;
	    return description;
	}
	/**
	 * @return Returns the duration.
	 */
	public Long getDuration() {
		//return duration!=null?duration:WDDXTAGS.NUMERIC_NULL_VALUE_LONG;
	    return duration;
	}
	/**
	 * @return Returns the firstActivityID.
	 */
	public Long getFirstActivityID() {
		//return firstActivityID!=null?firstActivityID:WDDXTAGS.NUMERIC_NULL_VALUE_LONG;
	    return firstActivityID;
	}
	/**
	 * @return Returns the helpText.
	 */
	public String getHelpText() {
		//return helpText!=null?helpText:WDDXTAGS.STRING_NULL_VALUE;
	    return helpText;
	}
	/**
	 * @return Returns the lastModifiedDateTime.
	 */
	public Date getLastModifiedDateTime() {
		//return lastModifiedDateTime!=null?lastModifiedDateTime:WDDXTAGS.DATE_NULL_VALUE;
	    return lastModifiedDateTime;
	}
	/**
	 * @return Returns the learningDesignID.
	 */
	public Long getLearningDesignID() {
		//return learningDesignID!=null?learningDesignID:WDDXTAGS.NUMERIC_NULL_VALUE_LONG;
	    return learningDesignID;
	}
	/**
	 * @return Returns the learningDesignUIID.
	 */
	public Integer getLearningDesignUIID() {
		//return learningDesignUIID!=null?learningDesignUIID:WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
	    return learningDesignUIID;
	}
	/**
	 * @return Returns the licenseID.
	 */
	public Long getLicenseID() {
		//return licenseID!=null?licenseID:WDDXTAGS.NUMERIC_NULL_VALUE_LONG;
	    return licenseID;
	}
	/**
	 * @return Returns the licenseText.
	 */
	public String getLicenseText() {
		//return licenseText!=null?licenseText:WDDXTAGS.STRING_NULL_VALUE;
	    return licenseText;
	}
	/**
	 * @return Returns the maxID.
	 */
	public Integer getMaxID() {
		//return maxID!=null?maxID:WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
	    return maxID;
	}
	/**
	 * @return Returns the originalLearningDesignID.
	 */
	public Long getOriginalLearningDesignID() {
		//return parentLearningDesignID!=null?parentLearningDesignID:WDDXTAGS.NUMERIC_NULL_VALUE_LONG;
	    return originalLearningDesignID;
	}
	/**
	 * @return Returns the readOnly.
	 */
	public Boolean getReadOnly() {
		//return readOnly!=null?readOnly:WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
	    return readOnly;
	}
	/**
	 * @return Returns the editLock.
	 */
	public Boolean getEditOverrideLock() {
	    return editOverrideLock;
	}
	
	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		//return title!=null?title:WDDXTAGS.STRING_NULL_VALUE;
	    return title;
	}
	/**
	 * @return Returns the userID.
	 */
	public Integer getUserID() {
		//return userID!=null?userID:WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
	    return userID;
	}
	/**
	 * @return Returns the editOnFlyUserID.
	 */
	public Integer getEditOverrideUserID() {
		//return userID!=null?userID:WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
	    return editOverrideUserID;
	}
	/**
	 * @return Returns the editOnFlyUserFullName.
	 */
	public String getEditOverrideUserFullName() {
	    return editOverrideUserFullName;
	}
	/**
	 * @return Returns the validDesign.
	 */
	public Boolean getValidDesign() {
		//return validDesign!=null?validDesign:WDDXTAGS.BOOLEAN_NULL_VALUE;
	    return validDesign;
	}
	/**
	 * @return Returns the version.
	 */
	public String getVersion() {
		//return version!=null?version:WDDXTAGS.STRING_NULL_VALUE;
	    return version;
	}
	/**
	 * @return Returns the design version.
	 */
	public Integer getDesignVersion() {
	    return designVersion;
	}
	/**
	 * @return Returns the workspaceFolderID.
	 */
	public Integer getWorkspaceFolderID() {
		//return workspaceFolderID!=null?workspaceFolderID:WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
	    return workspaceFolderID;
	}	
	/**
	 * @return Returns the activities.
	 */
	public ArrayList getActivities(){
		return activities;
	}
	
	/**
	 * In order to get the Grouping objects, it will go through the parent 
	 * activities in the learning design and for all grouping activities, it will 
	 * then retrieve the Grouping object which was created by the GroupingActivity.
	 * @param design
	 * @param groupingDAO DAO to reget grouping objects due to the hibernate cglib casting problems.
	 * @return ArrayList the array of groupingDTOs
	 */
	public ArrayList populateGroupings(LearningDesign design, ActivityDAO activityDAO)
	{
	    // Unfortunately, we can't just go through all the activities via design.getParentActivities()
	    // as the activities returned won't cast to GroupingActivity, so we would
	    // have to reget every activity to get the right type. So the easiest way
	    // is to get them all in a list directly via a HQL call.
	    ArrayList<GroupingDTO> groupingList = new ArrayList<GroupingDTO>();
	    List groupingActivities = activityDAO.getGroupingActivitiesByLearningDesignId(design.getLearningDesignId());
	    Iterator parentIterator = groupingActivities.iterator();
	    while (parentIterator.hasNext())
	    {
	        GroupingActivity groupingActivity = (GroupingActivity) parentIterator.next();			
		    Grouping grouping = groupingActivity.getCreateGrouping();
		    groupingList.add(grouping.getGroupingDTO());			    
	    }
	    return groupingList;
	}

	public ArrayList populateActivities(LearningDesign design) {
		//ArrayList childActivities = null;
		//To use sorted set again: in getAuthoringActivityDTOSet() it will already be sorted, here is just double warranty
		Set<AuthoringActivityDTO> dtoSet = new TreeSet<AuthoringActivityDTO>(new ActivityDTOOrderComparator());
		Iterator parentIterator = design.getParentActivities().iterator();
		while(parentIterator.hasNext()){
			Activity object = (Activity) parentIterator.next();
			//getAuthoringActivityDTOSet() method will:
			//for complex activity: It already populate its children activities.
			//for other activity: only get itself DTO object.
			dtoSet.addAll(object.getAuthoringActivityDTOSet());
		}
		
		return new ArrayList<AuthoringActivityDTO>(dtoSet);
	}	
	public ArrayList populateTransitions(LearningDesign design){
		ArrayList<TransitionDTO> transitions = new ArrayList<TransitionDTO>();
		if(design.getTransitions()!=null){
			Iterator iterator = design.getTransitions().iterator();
			while(iterator.hasNext()){
				Transition trans  =(Transition) iterator.next();
				transitions.add(trans.getTransitionDTO());
			}
		}
		return transitions;
	}
	public ArrayList getTransitions(){
		return this.transitions;
	}	
	/**
	 * @param activities The activities to set.
	 */
	public void setActivities(ArrayList activities) {
		this.activities = activities;
	}
	
	/**
	 * 
	 * @param contentFolderID The contentFolderID to set.
	 */
	public void setContentFolderID(String contentFolderID) {
		this.contentFolderID = contentFolderID;
	}
	
	/**
	 * @param copyTypeID The copyTypeID to set.
	 */
	public void setCopyTypeID(Integer copyTypeID) {		
		this.copyTypeID = copyTypeID;
	}
	/**
	 * @param createDateTime The createDateTime to set.
	 */
	public void setCreateDateTime(Date createDateTime) {
		if(!createDateTime.equals(WDDXTAGS.DATE_NULL_VALUE))
			this.createDateTime = createDateTime;
	}
	/**
	 * @param dateReadOnly The dateReadOnly to set.
	 */
	public void setDateReadOnly(Date dateReadOnly) {
		if(!WDDXTAGS.DATE_NULL_VALUE.equals(dateReadOnly) )
			this.dateReadOnly = dateReadOnly;
	}
	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		if(!description.equals(WDDXTAGS.STRING_NULL_VALUE))
			this.description = description;
	}
	/**
	 * @param duration The duration to set.
	 */
	public void setDuration(Long duration) {
		if(!duration.equals(WDDXTAGS.NUMERIC_NULL_VALUE_LONG))
			this.duration = duration;
	}
	/**
	 * @param firstActivityID The firstActivityID to set.
	 */
	public void setFirstActivityID(Long firstActivityID) {
		if(!firstActivityID.equals(WDDXTAGS.NUMERIC_NULL_VALUE_LONG))
			this.firstActivityID = firstActivityID;
	}
	/**
	 * @param helpText The helpText to set.
	 */
	public void setHelpText(String helpText) {
		if(!helpText.equals(WDDXTAGS.STRING_NULL_VALUE))
			this.helpText = helpText;
	}
	/**
	 * @param lastModifiedDateTime The lastModifiedDateTime to set.
	 */
	public void setLastModifiedDateTime(Date lastModifiedDateTime) {
		if(!lastModifiedDateTime.equals(WDDXTAGS.DATE_NULL_VALUE))
			this.lastModifiedDateTime = lastModifiedDateTime;
	}
	/**
	 * @param learningDesignID The learningDesignID to set.
	 */
	public void setLearningDesignID(Long learningDesignId) {
		if(!learningDesignId.equals(WDDXTAGS.NUMERIC_NULL_VALUE_LONG))
			this.learningDesignID = learningDesignId;
	}
	/**
	 * @param learningDesignUIID The learningDesignUIID to set.
	 */
	public void setLearningDesignUIID(Integer learningDesignUIID) {
		if(!learningDesignUIID.equals(WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER))
			this.learningDesignUIID = learningDesignUIID;
	}
	/**
	 * @param licenseID The licenseID to set.
	 */
	public void setLicenseID(Long licenseID) {
		if(!licenseID.equals(WDDXTAGS.NUMERIC_NULL_VALUE_LONG))
			this.licenseID = licenseID;
	}
	/**
	 * @param licenseText The licenseText to set.
	 */
	public void setLicenseText(String licenseText) {
		if(!licenseText.equals(WDDXTAGS.STRING_NULL_VALUE))
			this.licenseText = licenseText;
	}
	/**
	 * @param maxID The maxID to set.
	 */
	public void setMaxID(Integer maxID) {
		if(!maxID.equals(WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER))
			this.maxID = maxID;
	}
	/**
	 * @param parentLearningDesignID The parentLearningDesignID to set.
	 */
	public void setOriginalLearningDesignID(Long originalLearningDesignID) {
		if(!originalLearningDesignID.equals(WDDXTAGS.NUMERIC_NULL_VALUE_LONG))
			this.originalLearningDesignID = originalLearningDesignID;
	}
	/**
	 * @param readOnly The readOnly to set.
	 */
	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
	}
	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title) {
		if(!title.equals(WDDXTAGS.STRING_NULL_VALUE))
			this.title = title;
	}
	/**
	 * @param transitions The transitions to set.
	 */
	public void setTransitions(ArrayList transitions) {
		this.transitions = transitions;
	}
	/**
	 * @param userID The userID to set.
	 */
	public void setUserID(Integer userID) {
		if(!userID.equals(WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER))
			this.userID = userID;
	}
	/**
	 * @param userID The userID to set.
	 */
	public void setEditOverrideUserID(Integer editOverrideUserID) {
		if(!editOverrideUserID.equals(WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER))
			this.editOverrideUserID = editOverrideUserID;
	}
	/**
	 * @param userID The userID to set.
	 */
	public void setEditOverrideUserFullName(String editOverrideUserFullName) {
		if(!editOverrideUserFullName.equals(WDDXTAGS.STRING_NULL_VALUE))
			this.editOverrideUserFullName = editOverrideUserFullName;
	}
	/**
	 * @param validDesign The validDesign to set.
	 */
	public void setValidDesign(Boolean validDesign) {
		this.validDesign = validDesign;
	}
	/**
	 * @param version The version to set.
	 */
	public void setVersion(String version) {
		if(!version.equals(WDDXTAGS.STRING_NULL_VALUE))
			this.version = version;
	}
	/**
	 * @param designVersion The design version to set.
	 */
	public void setVersion(Integer designVersion) {
		if(!designVersion.equals(WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER))
			this.designVersion = designVersion;
	}
	/**
	 * @param workspaceFolderID The workspaceFolderID to set.
	 */
	public void setWorkspaceFolderID(Integer workspaceFolderID) {
		if(!workspaceFolderID.equals(WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER))
			this.workspaceFolderID = workspaceFolderID;
	}
	/**
	 * @return Returns the firstActivityUIID.
	 */
	public Integer getFirstActivityUIID() {		
		//return firstActivityUIID!=null?firstActivityUIID:WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
	    return firstActivityUIID;
	}
	/**
	 * @param firstActivityUIID The firstActivityUIID to set.
	 */
	public void setFirstActivityUIID(Integer firstActivityUIID) {
		if(!firstActivityUIID.equals(WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER))
			this.firstActivityUIID = firstActivityUIID;
	}	
	/**
	 * @return Returns the onlineInstructions.
	 */
	public String getOnlineInstructions() {
		return onlineInstructions;
	}
	/**
	 * @param onlineInstructions The onlineInstructions to set.
	 */
	public void setOnlineInstructions(String onlineInstructions) {
		if(!onlineInstructions.equals(WDDXTAGS.STRING_NULL_VALUE))
			this.onlineInstructions = onlineInstructions;
	}
	/**
	 * @param offlineInstructions The offlineInstructions to set.
	 */
	public void setOfflineInstructions(String offlineInstructions) {
		if(!offlineInstructions.equals(WDDXTAGS.STRING_NULL_VALUE))
			this.offlineInstructions = offlineInstructions;
	}
	/**
	 * @return Returns the offlineInstructions.
	 */
	public String getOfflineInstructions() {
		return offlineInstructions;
	}

    /**
     * @return Returns the groupings.
     */
    public ArrayList getGroupings() {
        return groupings;
    }
    /**
     * @param groupings The groupings to set.
     */
    public void setGroupings(ArrayList groupings) {
        this.groupings = groupings;
    }
}
