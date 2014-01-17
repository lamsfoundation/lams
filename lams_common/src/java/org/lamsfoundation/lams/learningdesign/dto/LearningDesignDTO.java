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
import org.lamsfoundation.lams.learningdesign.Competence;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.Transition;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.ActivityDAO;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.GroupingDAO;
import org.lamsfoundation.lams.util.wddx.WDDXTAGS;

/**
 * @author Manpreet Minhas
 */
public class LearningDesignDTO extends BaseDTO {

    private Long learningDesignID;
    private Integer learningDesignUIID;
    private String description;
    private String title;
    private Long firstActivityID;
    private Integer firstActivityUIID;
    private Long floatingActivityID;
    private Integer floatingActivityUIID;
    private Integer maxID;
    private Boolean validDesign;
    private Boolean readOnly;
    private Boolean editOverrideLock;
    private Date dateReadOnly;
    private Integer userID;
    private Integer originalUserID;
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

    /* Groupings Array which contain the groupings objects, which had been created by the grouping activities
     * in this learning design.
     */
    private ArrayList groupings;
    private ArrayList activities;
    private ArrayList transitions;
    private ArrayList branchMappings;
    private ArrayList<CompetenceDTO> competences;

    public LearningDesignDTO() {
    }

    public LearningDesignDTO(LearningDesign learningDesign, ActivityDAO activityDAO, GroupingDAO groupingDAO,
	    String languageCode) {
	this.learningDesignID = learningDesign.getLearningDesignId();
	this.learningDesignUIID = learningDesign.getLearningDesignUIID();
	this.description = learningDesign.getDescription();
	this.title = learningDesign.getTitle();
	this.firstActivityID = learningDesign.getFirstActivity() != null ? learningDesign.getFirstActivity()
		.getActivityId() : null;
	this.firstActivityUIID = learningDesign.getFirstActivity() != null ? learningDesign.getFirstActivity()
		.getActivityUIID() : null;
	this.floatingActivityID = learningDesign.getFirstActivity() != null ? learningDesign.getFirstActivity()
		.getActivityId() : null;
	this.floatingActivityUIID = learningDesign.getFloatingActivity() != null ? learningDesign.getFloatingActivity()
		.getActivityUIID() : null;

	this.maxID = learningDesign.getMaxID();
	this.validDesign = learningDesign.getValidDesign();
	this.designVersion = learningDesign.getDesignVersion();
	this.readOnly = learningDesign.getReadOnly();
	this.editOverrideLock = learningDesign.getEditOverrideLock();
	this.dateReadOnly = learningDesign.getDateReadOnly();

	this.userID = learningDesign.getUser() != null ? learningDesign.getUser().getUserId() : null;
	this.originalUserID = learningDesign.getOriginalUser() != null ? learningDesign.getOriginalUser().getUserId() : null;
	
	this.editOverrideUserID = learningDesign.getEditOverrideUser() != null ? learningDesign.getEditOverrideUser()
		.getUserId() : null;
	this.editOverrideUserFullName = learningDesign.getEditOverrideUser() != null ? learningDesign
		.getEditOverrideUser().getFullName() : null;

	this.helpText = learningDesign.getHelpText();
	this.copyTypeID = learningDesign.getCopyTypeID();
	this.contentFolderID = learningDesign.getContentFolderID();
	this.createDateTime = learningDesign.getCreateDateTime();
	this.version = learningDesign.getVersion();

	this.originalLearningDesignID = learningDesign.getOriginalLearningDesign() != null ? learningDesign
		.getOriginalLearningDesign().getLearningDesignId() : null;

	this.workspaceFolderID = learningDesign.getWorkspaceFolder() != null ? learningDesign.getWorkspaceFolder()
		.getWorkspaceFolderId() : null;

	this.duration = learningDesign.getDuration();
	this.licenseText = learningDesign.getLicenseText();

	this.licenseID = learningDesign.getLicense() != null ? learningDesign.getLicense().getLicenseID() : null;

	this.lastModifiedDateTime = learningDesign.getLastModifiedDateTime();
	this.branchMappings = new ArrayList(); // data will be set up by populateGroupings
	this.groupings = populateGroupings(learningDesign, groupingDAO);
	this.activities = populateActivities(learningDesign, languageCode);
	this.transitions = populateTransitions(learningDesign);
	this.competences = populateCompetences(learningDesign);

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
     * @return Returns the floatingActivityID.
     */
    public Long getFloatingActivityID() {
	return floatingActivityID;
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

    public Integer getOriginalUserID() {
        return originalUserID;
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
    public ArrayList getActivities() {
	return activities;
    }

    /**
     * Gets all the grouping objects for a learning design. Can't do it via
     * activities as the grouping related to a teacher chosen grouping does not
     * have a related grouping activity.
     * 
     * Don't set up the userlist if this is to be sent to authoring.
     * 
     * @param design
     * @param groupingDAO
     *                DAO to directory get the grouping objects (no direct link
     *                from learning design possible).
     * @param setupUserList
     * @return ArrayList the array of groupingDTOs
     */
    public ArrayList populateGroupings(LearningDesign design, GroupingDAO groupingDAO) {
	ArrayList<GroupingDTO> groupingList = new ArrayList<GroupingDTO>();
	List dbGroupings = groupingDAO.getGroupingsByLearningDesign(design.getLearningDesignId());
	if (dbGroupings != null) {
	    Iterator groupingIter = dbGroupings.iterator();
	    while (groupingIter.hasNext()) {
		Grouping grouping = (Grouping) groupingIter.next();
		groupingList.add(grouping.getGroupingDTO(false));
	    }
	}
	return groupingList;
    }

    public ArrayList populateActivities(LearningDesign design, String languageCode) {
	//ArrayList childActivities = null;
	//To use sorted set again: in getAuthoringActivityDTOSet() it will already be sorted, here is just double warranty
	Set<AuthoringActivityDTO> dtoSet = new TreeSet<AuthoringActivityDTO>(new ActivityDTOOrderComparator());
	Iterator parentIterator = design.getParentActivities().iterator();
	while (parentIterator.hasNext()) {
	    Activity object = (Activity) parentIterator.next();
	    //getAuthoringActivityDTOSet() method will:
	    //for complex activity: It already populate its children activities.
	    //for other activity: only get itself DTO object.
	    dtoSet.addAll(object.getAuthoringActivityDTOSet(branchMappings, languageCode));
	}
	return new ArrayList<AuthoringActivityDTO>(dtoSet);
    }

    public ArrayList populateTransitions(LearningDesign design) {
	ArrayList<TransitionDTO> transitions = new ArrayList<TransitionDTO>();
	if (design.getTransitions() != null) {
	    Iterator iterator = design.getTransitions().iterator();
	    while (iterator.hasNext()) {
		Transition trans = (Transition) iterator.next();
		transitions.add(trans.getTransitionDTO());
	    }
	}
	return transitions;
    }

    public ArrayList<CompetenceDTO> populateCompetences(LearningDesign design) {
	ArrayList<CompetenceDTO> competenceDTOs = new ArrayList<CompetenceDTO>();
	if (design.getCompetences() != null) {
	    Iterator iterator = design.getCompetences().iterator();
	    while (iterator.hasNext()) {
		Competence competence = (Competence) iterator.next();
		CompetenceDTO competenceDTO = new CompetenceDTO(competence);
		competenceDTOs.add(competenceDTO);
	    }
	}
	return competenceDTOs;
    }

    public ArrayList getTransitions() {
	return this.transitions;
    }

    /**
     * @param activities
     *                The activities to set.
     */
    public void setActivities(ArrayList activities) {
	this.activities = activities;
    }

    /**
     * 
     * @param contentFolderID
     *                The contentFolderID to set.
     */
    public void setContentFolderID(String contentFolderID) {
	this.contentFolderID = contentFolderID;
    }

    /**
     * @param copyTypeID
     *                The copyTypeID to set.
     */
    public void setCopyTypeID(Integer copyTypeID) {
	this.copyTypeID = copyTypeID;
    }

    /**
     * @param createDateTime
     *                The createDateTime to set.
     */
    public void setCreateDateTime(Date createDateTime) {
	if (!createDateTime.equals(WDDXTAGS.DATE_NULL_VALUE))
	    this.createDateTime = createDateTime;
    }

    /**
     * @param dateReadOnly
     *                The dateReadOnly to set.
     */
    public void setDateReadOnly(Date dateReadOnly) {
	if (!WDDXTAGS.DATE_NULL_VALUE.equals(dateReadOnly))
	    this.dateReadOnly = dateReadOnly;
    }

    /**
     * @param description
     *                The description to set.
     */
    public void setDescription(String description) {
	if (!description.equals(WDDXTAGS.STRING_NULL_VALUE))
	    this.description = description;
    }

    /**
     * @param duration
     *                The duration to set.
     */
    public void setDuration(Long duration) {
	if (!duration.equals(WDDXTAGS.NUMERIC_NULL_VALUE_LONG))
	    this.duration = duration;
    }

    /**
     * @param firstActivityID
     *                The firstActivityID to set.
     */
    public void setFirstActivityID(Long firstActivityID) {
	if (!firstActivityID.equals(WDDXTAGS.NUMERIC_NULL_VALUE_LONG))
	    this.firstActivityID = firstActivityID;
    }

    /**
     * @param floatingActivityID
     *                The floatingActivityID to set.
     */
    public void setFloatingActivityID(Long floatingActivityID) {
	if (!floatingActivityID.equals(WDDXTAGS.NUMERIC_NULL_VALUE_LONG))
	    this.floatingActivityID = floatingActivityID;
    }

    /**
     * @param helpText
     *                The helpText to set.
     */
    public void setHelpText(String helpText) {
	if (!helpText.equals(WDDXTAGS.STRING_NULL_VALUE))
	    this.helpText = helpText;
    }

    /**
     * @param lastModifiedDateTime
     *                The lastModifiedDateTime to set.
     */
    public void setLastModifiedDateTime(Date lastModifiedDateTime) {
	if (!lastModifiedDateTime.equals(WDDXTAGS.DATE_NULL_VALUE))
	    this.lastModifiedDateTime = lastModifiedDateTime;
    }

    /**
     * @param learningDesignID
     *                The learningDesignID to set.
     */
    public void setLearningDesignID(Long learningDesignId) {
	if (!learningDesignId.equals(WDDXTAGS.NUMERIC_NULL_VALUE_LONG))
	    this.learningDesignID = learningDesignId;
    }

    /**
     * @param learningDesignUIID
     *                The learningDesignUIID to set.
     */
    public void setLearningDesignUIID(Integer learningDesignUIID) {
	if (!learningDesignUIID.equals(WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER))
	    this.learningDesignUIID = learningDesignUIID;
    }

    /**
     * @param licenseID
     *                The licenseID to set.
     */
    public void setLicenseID(Long licenseID) {
	if (!licenseID.equals(WDDXTAGS.NUMERIC_NULL_VALUE_LONG))
	    this.licenseID = licenseID;
    }

    /**
     * @param licenseText
     *                The licenseText to set.
     */
    public void setLicenseText(String licenseText) {
	if (!licenseText.equals(WDDXTAGS.STRING_NULL_VALUE))
	    this.licenseText = licenseText;
    }

    /**
     * @param maxID
     *                The maxID to set.
     */
    public void setMaxID(Integer maxID) {
	if (!maxID.equals(WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER))
	    this.maxID = maxID;
    }

    /**
     * @param parentLearningDesignID
     *                The parentLearningDesignID to set.
     */
    public void setOriginalLearningDesignID(Long originalLearningDesignID) {
	if (!originalLearningDesignID.equals(WDDXTAGS.NUMERIC_NULL_VALUE_LONG))
	    this.originalLearningDesignID = originalLearningDesignID;
    }

    /**
     * @param readOnly
     *                The readOnly to set.
     */
    public void setReadOnly(Boolean readOnly) {
	this.readOnly = readOnly;
    }

    /**
     * @param title
     *                The title to set.
     */
    public void setTitle(String title) {
	if (!title.equals(WDDXTAGS.STRING_NULL_VALUE))
	    this.title = title;
    }

    /**
     * @param transitions
     *                The transitions to set.
     */
    public void setTransitions(ArrayList transitions) {
	this.transitions = transitions;
    }

    /**
     * @param userID
     *                The userID to set.
     */
    public void setUserID(Integer userID) {
	if (!userID.equals(WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER))
	    this.userID = userID;
    }

    public void setOriginalUserID(Integer originalUserID) {
	if (!originalUserID.equals(WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER)) {
	    this.originalUserID = originalUserID;
	}
    }

    /**
     * @param userID
     *                The userID to set.
     */
    public void setEditOverrideUserID(Integer editOverrideUserID) {
	if (!editOverrideUserID.equals(WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER))
	    this.editOverrideUserID = editOverrideUserID;
    }

    /**
     * @param userID
     *                The userID to set.
     */
    public void setEditOverrideUserFullName(String editOverrideUserFullName) {
	if (!editOverrideUserFullName.equals(WDDXTAGS.STRING_NULL_VALUE))
	    this.editOverrideUserFullName = editOverrideUserFullName;
    }

    /**
     * @param validDesign
     *                The validDesign to set.
     */
    public void setValidDesign(Boolean validDesign) {
	this.validDesign = validDesign;
    }

    /**
     * @param version
     *                The version to set.
     */
    public void setVersion(String version) {
	if (!version.equals(WDDXTAGS.STRING_NULL_VALUE))
	    this.version = version;
    }

    /**
     * @param designVersion
     *                The design version to set.
     */
    public void setVersion(Integer designVersion) {
	if (!designVersion.equals(WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER))
	    this.designVersion = designVersion;
    }

    /**
     * @param workspaceFolderID
     *                The workspaceFolderID to set.
     */
    public void setWorkspaceFolderID(Integer workspaceFolderID) {
	if (!workspaceFolderID.equals(WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER))
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
     * @return Returns the floatingActivityUIID.
     */
    public Integer getFloatingActivityUIID() {
	return floatingActivityUIID;
    }

    /**
     * @param firstActivityUIID
     *                The firstActivityUIID to set.
     */
    public void setFirstActivityUIID(Integer firstActivityUIID) {
	if (!firstActivityUIID.equals(WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER))
	    this.firstActivityUIID = firstActivityUIID;
    }

    /**
     * @param floatingActivityUIID
     *                The floatingActivityUIID to set.
     */
    public void setFloatingActivityUIID(Integer floatingActivityUIID) {
	if (!floatingActivityUIID.equals(WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER))
	    this.floatingActivityUIID = floatingActivityUIID;
    }

    /**
     * @return Returns the groupings.
     */
    public ArrayList getGroupings() {
	return groupings;
    }

    /**
     * @param groupings
     *                The groupings to set.
     */
    public void setGroupings(ArrayList groupings) {
	this.groupings = groupings;
    }

    public ArrayList getBranchMappings() {
	return branchMappings;
    }

    public void setBranchMappings(ArrayList branchMappings) {
	this.branchMappings = branchMappings;
    }

    public ArrayList<CompetenceDTO> getCompetences() {
	return competences;
    }

    public void setCompetences(ArrayList<CompetenceDTO> competences) {
	this.competences = competences;
    }

}
