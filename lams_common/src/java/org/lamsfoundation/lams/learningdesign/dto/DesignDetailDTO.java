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

import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.util.wddx.WDDXTAGS;

/**
 * @author Manpreet Minhas
 */
public class DesignDetailDTO extends BaseDTO {
	
	private Long learningDesignID;
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
	public DesignDetailDTO(Long learningDesignID, Integer learningDesignUIID,
			String description, String title, Long firstActivityID,
			Boolean validDesign, Boolean readOnly,
			Date dateReadOnly, Integer userID, String helpText,
			Integer copyTypeID, String version, Long parentLearningDesignID,
			Integer workspaceFolderID) {		
		this.learningDesignID = learningDesignID;
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
		this.learningDesignID = learningDesign.getLearningDesignId();
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
	 * @return Returns the learningDesignID.
	 */
	public Long getLearningDesignID() {
		return learningDesignID!=null?learningDesignID:WDDXTAGS.NUMERIC_NULL_VALUE_LONG;
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
