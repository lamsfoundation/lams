/*
 * Created on Mar 15, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.learningdesign.dto;


import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ComplexActivity;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.Transition;
import org.lamsfoundation.lams.util.wddx.WDDXTAGS;

/**
 * @author Minhas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LearningDesignDTO {
	
	private Long learningDesignId;
	private Integer learningDesignUIID;
	private String description;
	private String title;	
	private Long firstActivityID;
	private Integer firstActivityUIID;
	private Integer maxID;	
	private Boolean validDesign;
	private Boolean readOnly;	
	private Date dateReadOnly;
	private Integer userID;
	private String helpText;
	private Integer copyTypeID;
	private Date createDateTime;
	private String version;	
	private Long parentLearningDesignID;
	private Integer workspaceFolderID;
	private Long duration;
	private String licenseText;
	private Long licenseID;
	private Long lessonOrgID;
	private String lessonOrgName;
	private Long lessonID;
	private String lessonName;
	private Date lessonStartDateTime;
	private Date lastModifiedDateTime;
	
	private Hashtable activities;
	private Hashtable transitions;
	
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
							 Date dateReadOnly,
							 Integer userID,
							 String helpText,
							 Integer copyTypeID,
							 Date createDateTime,
							 String version,
							 Long parentLearningDesignID,
							 Integer workspaceFolderID,
							 Long duration, 
							 String licenseText,
							 Long licenseID,
							 Long lessonOrgID,
							 String lessonOrgName,
							 Long lessonID,
							 String lessonName, 
							 Date lessonStartDateTime,
							 Date lastModifiedDateTime) {
		super();
		this.learningDesignId = learningDesignId;
		this.learningDesignUIID = learningDesignUIID;
		this.description = description;
		this.title = title;
		this.firstActivityID = firstActivityID;
		this.firstActivityUIID = firstActivityUIID;
		this.maxID = maxId;
		this.validDesign = validDesign;
		this.readOnly = readOnly;
		this.dateReadOnly = dateReadOnly;
		this.userID = userID;
		this.helpText = helpText;
		this.copyTypeID = copyTypeID;
		this.createDateTime = createDateTime;
		this.version = version;
		this.parentLearningDesignID = parentLearningDesignID;
		this.workspaceFolderID = workspaceFolderID;
		this.duration = duration;
		this.licenseText = licenseText;
		this.licenseID = licenseID;
		this.lessonOrgID = lessonOrgID;
		this.lessonOrgName = lessonOrgName;
		this.lessonID = lessonID;
		this.lessonName = lessonName;
		this.lessonStartDateTime = lessonStartDateTime;
		this.lastModifiedDateTime = lastModifiedDateTime;
		this.activities = new Hashtable();
		this.transitions = new Hashtable();
	}	
	public LearningDesignDTO(LearningDesign learningDesign){
		this.learningDesignId = learningDesign.getLearningDesignId();
		this.learningDesignUIID = learningDesign.getLearningDesignUIID();
		this.description = learningDesign.getDescription();
		this.title = learningDesign.getTitle();
		this.firstActivityID = learningDesign.getFirstActivity()!=null?
							   learningDesign.getFirstActivity().getActivityId():
							   WDDXTAGS.NUMERIC_NULL_VALUE_LONG;
		this.firstActivityUIID = learningDesign.getFirstActivity()!=null?
								 learningDesign.getFirstActivity().getActivityUIID():
								 WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
		this.maxID = learningDesign.getMaxId();
		this.validDesign = learningDesign.getValidDesign();
		this.readOnly = learningDesign.getReadOnly();
		this.dateReadOnly = learningDesign.getDateReadOnly();
		
		this.userID = learningDesign.getUser()!=null?
					  learningDesign.getUser().getUserId():
					  WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
		
					  this.helpText = learningDesign.getHelpText();
		this.copyTypeID = learningDesign.getCopyTypeID();
		this.createDateTime = learningDesign.getCreateDateTime();
		this.version = learningDesign.getVersion();
		
		this.parentLearningDesignID = learningDesign.getParentLearningDesign()!=null?
									  learningDesign.getParentLearningDesign().getLearningDesignId():
									  WDDXTAGS.NUMERIC_NULL_VALUE_LONG;
		
	    this.workspaceFolderID = learningDesign.getWorkspaceFolder()!=null?
								 learningDesign.getWorkspaceFolder().getWorkspaceFolderId():
								 WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
								 
		this.duration = learningDesign.getDuration();
		this.licenseText = learningDesign.getLicenseText();
		
		this.licenseID = learningDesign.getLicense()!=null?
						 learningDesign.getLicense().getLicenseID():
						 WDDXTAGS.NUMERIC_NULL_VALUE_LONG;
		
		this.lessonOrgID = learningDesign.getLessonOrgID();
		
		this.lessonOrgName = learningDesign.getLessonOrgName();
		this.lessonID = learningDesign.getLessonID();
		this.lessonName = learningDesign.getLessonName();
		this.lessonStartDateTime = learningDesign.getLessonStartDateTime();
		this.lastModifiedDateTime = learningDesign.getLastModifiedDateTime();
		this.activities = populateActivities(learningDesign);
		this.transitions = populateTransitions(learningDesign);
	}	
	/**
	 * @return Returns the copyTypeID.
	 */
	public Integer getCopyTypeID() {
		return copyTypeID!=null?copyTypeID:WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
	}
	/**
	 * @return Returns the createDateTime.
	 */
	public Date getCreateDateTime() {
		return createDateTime!=null?createDateTime:WDDXTAGS.DATE_NULL_VALUE;
	}
	/**
	 * @return Returns the dateReadOnly.
	 */
	public Date getDateReadOnly() {
		return dateReadOnly!=null?dateReadOnly:WDDXTAGS.DATE_NULL_VALUE;
	}
	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description!=null?description:WDDXTAGS.STRING_NULL_VALUE;
	}
	/**
	 * @return Returns the duration.
	 */
	public Long getDuration() {
		return duration!=null?duration:WDDXTAGS.NUMERIC_NULL_VALUE_LONG;
	}
	/**
	 * @return Returns the firstActivityID.
	 */
	public Long getFirstActivityID() {
		return firstActivityID!=null?firstActivityID:WDDXTAGS.NUMERIC_NULL_VALUE_LONG;
	}
	/**
	 * @return Returns the helpText.
	 */
	public String getHelpText() {
		return helpText!=null?helpText:WDDXTAGS.STRING_NULL_VALUE;
	}
	/**
	 * @return Returns the lastModifiedDateTime.
	 */
	public Date getLastModifiedDateTime() {
		return lastModifiedDateTime!=null?lastModifiedDateTime:WDDXTAGS.DATE_NULL_VALUE;
	}
	/**
	 * @return Returns the learningDesignId.
	 */
	public Long getLearningDesignId() {
		return learningDesignId!=null?learningDesignId:WDDXTAGS.NUMERIC_NULL_VALUE_LONG;
	}
	/**
	 * @return Returns the learningDesignUIID.
	 */
	public Integer getLearningDesignUIID() {
		return learningDesignUIID!=null?learningDesignUIID:WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
	}
	/**
	 * @return Returns the lessonID.
	 */
	public Long getLessonID() {
		return lessonID!=null?lessonID:WDDXTAGS.NUMERIC_NULL_VALUE_LONG;
	}
	/**
	 * @return Returns the lessonName.
	 */
	public String getLessonName() {
		return lessonName!=null?lessonName:WDDXTAGS.STRING_NULL_VALUE;
	}
	/**
	 * @return Returns the lessonOrgID.
	 */
	public Long getLessonOrgID() {
		return lessonOrgID!=null?lessonOrgID:WDDXTAGS.NUMERIC_NULL_VALUE_LONG;
	}
	/**
	 * @return Returns the lessonOrgName.
	 */
	public String getLessonOrgName() {
		return lessonOrgName!=null?lessonOrgName:WDDXTAGS.STRING_NULL_VALUE;
	}
	/**
	 * @return Returns the lessonStartDateTime.
	 */
	public Date getLessonStartDateTime() {
		return lessonStartDateTime!=null?lessonStartDateTime:WDDXTAGS.DATE_NULL_VALUE;
	}
	/**
	 * @return Returns the licenseID.
	 */
	public Long getLicenseID() {
		return licenseID!=null?licenseID:WDDXTAGS.NUMERIC_NULL_VALUE_LONG;
	}
	/**
	 * @return Returns the licenseText.
	 */
	public String getLicenseText() {
		return licenseText!=null?licenseText:WDDXTAGS.STRING_NULL_VALUE;
	}
	/**
	 * @return Returns the maxID.
	 */
	public Integer getMaxID() {
		return maxID!=null?maxID:WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
	}
	/**
	 * @return Returns the parentLearningDesignID.
	 */
	public Long getParentLearningDesignID() {
		return parentLearningDesignID!=null?parentLearningDesignID:WDDXTAGS.NUMERIC_NULL_VALUE_LONG;
	}
	/**
	 * @return Returns the readOnly.
	 */
	public Boolean getReadOnly() {
		return readOnly!=null?readOnly:WDDXTAGS.BOOLEAN_NULL_VALUE;
	}
	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title!=null?title:WDDXTAGS.STRING_NULL_VALUE;
	}
	/**
	 * @return Returns the userID.
	 */
	public Integer getUserID() {
		return userID!=null?userID:WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
	}
	/**
	 * @return Returns the validDesign.
	 */
	public Boolean getValidDesign() {
		return validDesign!=null?validDesign:WDDXTAGS.BOOLEAN_NULL_VALUE;
	}
	/**
	 * @return Returns the version.
	 */
	public String getVersion() {
		return version!=null?version:WDDXTAGS.STRING_NULL_VALUE;
	}
	/**
	 * @return Returns the workspaceFolderID.
	 */
	public Integer getWorkspaceFolderID() {
		return workspaceFolderID!=null?workspaceFolderID:WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
	}	
	public Hashtable getActivities(){
		return activities;
	}
	/**
	 * @return Returns the activities.
	 */

	public Hashtable populateActivities(LearningDesign design) {
		Hashtable activities = new Hashtable();		
		Hashtable childActivities = null;
		Iterator parentIterator = design.getParentActivities().iterator();
		while(parentIterator.hasNext()){
			Object object = parentIterator.next();
			
			if(object instanceof ComplexActivity){
				ComplexActivity complexActivity = (ComplexActivity)object;
				Iterator childIterator = complexActivity.getActivities().iterator();
				childActivities = new Hashtable();
				while(childIterator.hasNext()){
					Activity activity =(Activity)childIterator.next();
					childActivities.put(activity.getActivityId(),activity.getAuthoringActivityDTO());					
				}				
				activities.put(((Activity)complexActivity).getActivityId(),((Activity)complexActivity).getAuthoringActivityDTO());
				activities.putAll(childActivities);
			}else{
				Activity activity = (Activity)object;
				activities.put(activity.getActivityId(),activity.getAuthoringActivityDTO());
			}			
		}
		return activities;
	}	
	public Hashtable populateTransitions(LearningDesign design){
		Hashtable transitions = new Hashtable();
		if(design.getTransitions()!=null){
			Iterator iterator = design.getTransitions().iterator();
			while(iterator.hasNext()){
				Transition trans  =(Transition) iterator.next();
				transitions.put(trans.getTransitionId(),trans.getTransitionDTO());
			}
		}
		return transitions;
	}
	public Hashtable getTransitions(){
		return this.transitions;
	}	
	/**
	 * @param activities The activities to set.
	 */
	public void setActivities(Hashtable activities) {
		this.activities = activities;
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
		if(!dateReadOnly.equals(WDDXTAGS.DATE_NULL_VALUE))
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
	 * @param learningDesignId The learningDesignId to set.
	 */
	public void setLearningDesignId(Long learningDesignId) {
		if(!learningDesignId.equals(WDDXTAGS.NUMERIC_NULL_VALUE_LONG))
			this.learningDesignId = learningDesignId;
	}
	/**
	 * @param learningDesignUIID The learningDesignUIID to set.
	 */
	public void setLearningDesignUIID(Integer learningDesignUIID) {
		if(!learningDesignUIID.equals(WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER))
			this.learningDesignUIID = learningDesignUIID;
	}
	/**
	 * @param lessonID The lessonID to set.
	 */
	public void setLessonID(Long lessonID) {
		if(!lessonID.equals(WDDXTAGS.NUMERIC_NULL_VALUE_LONG))
		this.lessonID = lessonID;
	}
	/**
	 * @param lessonName The lessonName to set.
	 */
	public void setLessonName(String lessonName) {
		if(!lessonName.equals(WDDXTAGS.STRING_NULL_VALUE))
			this.lessonName = lessonName;
	}
	/**
	 * @param lessonOrgID The lessonOrgID to set.
	 */
	public void setLessonOrgID(Long lessonOrgID) {
		if(!lessonOrgID.equals(WDDXTAGS.NUMERIC_NULL_VALUE_LONG))
			this.lessonOrgID = lessonOrgID;
	}
	/**
	 * @param lessonOrgName The lessonOrgName to set.
	 */
	public void setLessonOrgName(String lessonOrgName) {
		if(!lessonOrgName.equals(WDDXTAGS.STRING_NULL_VALUE))
			this.lessonOrgName = lessonOrgName;
	}
	/**
	 * @param lessonStartDateTime The lessonStartDateTime to set.
	 */
	public void setLessonStartDateTime(Date lessonStartDateTime) {
		if(!lessonStartDateTime.equals(WDDXTAGS.DATE_NULL_VALUE))
			this.lessonStartDateTime = lessonStartDateTime;
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
	public void setParentLearningDesignID(Long parentLearningDesignID) {
		if(!parentLearningDesignID.equals(WDDXTAGS.NUMERIC_NULL_VALUE_LONG))
			this.parentLearningDesignID = parentLearningDesignID;
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
	public void setTransitions(Hashtable transitions) {
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
		return firstActivityUIID!=null?firstActivityUIID:WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
	}
	/**
	 * @param firstActivityUIID The firstActivityUIID to set.
	 */
	public void setFirstActivityUIID(Integer firstActivityUIID) {
		if(!firstActivityUIID.equals(WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER))
			this.firstActivityUIID = firstActivityUIID;
	}	
	public static LearningDesign extractLearningDesign(LearningDesignDTO learningDesignDTO){
		LearningDesign learningDesign = new LearningDesign();
		if(!learningDesignDTO.getLearningDesignUIID().equals(WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER))
			learningDesign.setLearningDesignUIID(learningDesignDTO.getLearningDesignUIID());
		if(!learningDesignDTO.getDescription().equals(WDDXTAGS.STRING_NULL_VALUE))
			learningDesign.setDescription(learningDesignDTO.getDescription());
		if(!learningDesignDTO.getTitle().equals(WDDXTAGS.STRING_NULL_VALUE))
			learningDesign.setTitle(learningDesignDTO.getTitle());
		if(!learningDesignDTO.getMaxID().equals(WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER))
			learningDesign.setMaxId(learningDesignDTO.getMaxID());
		
		learningDesign.setValidDesign(learningDesignDTO.getValidDesign());
		learningDesign.setReadOnly(learningDesignDTO.getReadOnly());
			
		if(!learningDesignDTO.getCopyTypeID().equals(WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER))
			learningDesign.setCopyTypeID(learningDesignDTO.getCopyTypeID());		
		if(!learningDesignDTO.getHelpText().equals(WDDXTAGS.STRING_NULL_VALUE))
			learningDesign.setHelpText(learningDesignDTO.getHelpText());
		if(!learningDesignDTO.getVersion().equals(WDDXTAGS.STRING_NULL_VALUE))
			learningDesign.setVersion(learningDesignDTO.getVersion());
		if(!learningDesignDTO.getLicenseText().equals(WDDXTAGS.STRING_NULL_VALUE))
			learningDesign.setLicenseText(learningDesignDTO.getLicenseText());
		if(!learningDesignDTO.getLessonOrgName().equals(WDDXTAGS.STRING_NULL_VALUE))
			learningDesign.setLessonOrgName(learningDesignDTO.getLessonOrgName());
		if(!learningDesignDTO.getLessonName().equals(WDDXTAGS.STRING_NULL_VALUE))
			learningDesign.setLessonName(learningDesignDTO.getLessonName());					
		if(!learningDesignDTO.getDateReadOnly().equals(WDDXTAGS.DATE_NULL_VALUE))
			learningDesign.setDateReadOnly(learningDesignDTO.getDateReadOnly());
		if(!learningDesignDTO.getCreateDateTime().equals(WDDXTAGS.DATE_NULL_VALUE))
			learningDesign.setCreateDateTime(learningDesignDTO.getCreateDateTime());
		if(!learningDesignDTO.getLessonStartDateTime().equals(WDDXTAGS.DATE_NULL_VALUE))
			learningDesign.setLessonStartDateTime(learningDesignDTO.getLessonStartDateTime());
		if(!learningDesignDTO.getLastModifiedDateTime().equals(WDDXTAGS.DATE_NULL_VALUE))
			learningDesign.setLastModifiedDateTime(learningDesignDTO.getLastModifiedDateTime());
		if(!learningDesignDTO.getLessonID().equals(WDDXTAGS.NUMERIC_NULL_VALUE_LONG))
			learningDesign.setLessonID(learningDesignDTO.getLessonID());
		if(!learningDesignDTO.getLessonOrgID().equals(WDDXTAGS.NUMERIC_NULL_VALUE_LONG))
			learningDesign.setLessonOrgID(learningDesignDTO.getLessonOrgID());
		if(!learningDesignDTO.getDuration().equals(WDDXTAGS.NUMERIC_NULL_VALUE_LONG))
			learningDesign.setDuration(learningDesignDTO.getDuration());
		
		return learningDesign;
	}
}
