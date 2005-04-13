/*
 * Created on Apr 11, 2005
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
public class DesignDetailDTO extends BaseDTO {
	
	private Long learningDesignId;
	private Integer learningDesignUIID;
	private String description;
	private String title;	
	private Long firstActivityID;		
	private Boolean validDesign;
	private Boolean readOnly;	
	private Date dateReadOnly;
	private Integer userID;
	private String helpText;
	private Integer copyTypeID;	
	private String version;	
	private Long parentLearningDesignID;
	private Integer workspaceFolderID;
	
	public DesignDetailDTO(){
		
	}
	public DesignDetailDTO(Long learningDesignId, Integer learningDesignUIID,
			String description, String title, Long firstActivityID,
			Boolean validDesign, Boolean readOnly,
			Date dateReadOnly, Integer userID, String helpText,
			Integer copyTypeID, String version, Long parentLearningDesignID,
			Integer workspaceFolderID) {		
		this.learningDesignId = learningDesignId;
		this.learningDesignUIID = learningDesignUIID;
		this.description = description;
		this.title = title;
		this.firstActivityID = firstActivityID;		
		this.validDesign = validDesign;
		this.readOnly = readOnly;
		this.dateReadOnly = dateReadOnly;
		this.userID = userID;
		this.helpText = helpText;
		this.copyTypeID = copyTypeID;
		this.version = version;
		this.parentLearningDesignID = parentLearningDesignID;
		this.workspaceFolderID = workspaceFolderID;
	}
	public DesignDetailDTO(LearningDesign learningDesign){
		this.learningDesignId = learningDesign.getLearningDesignId();
		this.learningDesignUIID = learningDesign.getLearningDesignUIID();
		this.description = learningDesign.getDescription();
		this.title = learningDesign.getTitle();		
		this.firstActivityID = learningDesign.getFirstActivity()!=null?
							   learningDesign.getFirstActivity().getActivityId():
							   WDDXTAGS.NUMERIC_NULL_VALUE_LONG;
		this.validDesign = learningDesign.getValidDesign();
		this.readOnly = learningDesign.getReadOnly();
		this.dateReadOnly = learningDesign.getDateReadOnly();
		this.userID = learningDesign.getUser().getUserId();
		this.helpText = learningDesign.getHelpText();
		this.copyTypeID = learningDesign.getCopyTypeID();
		this.version = learningDesign.getVersion();
		this.parentLearningDesignID = learningDesign.getParentLearningDesign()!=null?
									  learningDesign.getParentLearningDesign().getLearningDesignId():
									  WDDXTAGS.NUMERIC_NULL_VALUE_LONG;
		this.workspaceFolderID = learningDesign.getWorkspaceFolder().getWorkspaceFolderId();
	}
	/**
	 * @return Returns the copyTypeID.
	 */
	public Integer getCopyTypeID() {
		return copyTypeID!=null?copyTypeID:WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
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
