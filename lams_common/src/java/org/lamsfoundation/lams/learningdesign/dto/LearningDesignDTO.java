/*
 * Created on Mar 15, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.learningdesign.dto;

import java.util.Date;

import org.lamsfoundation.lams.learningdesign.LearningDesign;
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
	
	/**
	 * @param learningDesignId
	 * @param learningDesignUIID
	 * @param description
	 * @param title
	 * @param firstActivityID
	 * @param maxID
	 * @param validDesign
	 * @param readOnly
	 * @param dateReadOnly
	 * @param userID
	 * @param helpText
	 * @param copyTypeID
	 * @param createDateTime
	 * @param version
	 * @param parentLearningDesignID
	 * @param workspaceFolderID
	 * @param duration
	 * @param licenseText
	 * @param licenseID
	 * @param lessonOrgID
	 * @param lessonOrgName
	 * @param lessonID
	 * @param lessonName
	 * @param lessonStartDateTime
	 * @param lastModifiedDateTime
	 */
	public LearningDesignDTO(Long learningDesignId, Integer learningDesignUIID,
			String description, String title, Long firstActivityID,
			Integer maxId, Boolean validDesign, Boolean readOnly,
			Date dateReadOnly, Integer userID, String helpText,
			Integer copyTypeID, Date createDateTime, String version,
			Long parentLearningDesignID, Integer workspaceFolderID,
			Long duration, String licenseText, Long licenseID,
			Long lessonOrgID, String lessonOrgName, Long lessonID,
			String lessonName, Date lessonStartDateTime,
			Date lastModifiedDateTime) {
		super();
		this.learningDesignId = learningDesignId;
		this.learningDesignUIID = learningDesignUIID;
		this.description = description;
		this.title = title;
		this.firstActivityID = firstActivityID;
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
	}
	public LearningDesignDTO(){	
		
	}
	public LearningDesignDTO(LearningDesign learningDesign){
		this.learningDesignId = learningDesign.getLearningDesignId();
		this.learningDesignUIID = learningDesign.getLearningDesignUIID();
		this.description = learningDesign.getDescription();
		this.title = learningDesign.getTitle();
		this.firstActivityID = learningDesign.getFirstActivity()!=null?
							   learningDesign.getFirstActivity().getActivityId():
							   WDDXTAGS.NUMERIC_NULL_VALUE_LONG;
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
}
