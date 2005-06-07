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
package org.lamsfoundation.lams.lesson.dto;

import java.util.Date;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.util.wddx.WDDXTAGS;

/**
 * @author Manpreet Minhas
 */
public class LessonDetailsDTO {
	
	private Long lessonID;
	private Integer lessonStateID;
	private Date createDateTime;
	private Date startDateTime;
	private Long duration;	
	private Integer organisationID;
	private String organisationName;
	private String organisationDescription;	
	private Integer workspaceFolderID;	
	private Long licenseID;
	private String licenseText;	
	
	private Long learningDesignID;
	
	
	/** Full constructor */
	public LessonDetailsDTO(Long lessonID, Long duration, Date createDateTime,
			Date startDateTime, Integer organisationID,
			String organisationName, String organisationDescription,
			Integer workspaceFolderID, Long licenseID, String licenseText,
			Integer lessonStateID, Long learningDesignID) {		
		this.lessonID = lessonID;
		this.duration = duration;
		this.createDateTime = createDateTime;
		this.startDateTime = startDateTime;
		this.organisationID = organisationID;
		this.organisationName = organisationName;
		this.organisationDescription = organisationDescription;
		this.workspaceFolderID = workspaceFolderID;
		this.licenseID = licenseID;
		this.licenseText = licenseText;
		this.lessonStateID = lessonStateID;
		this.learningDesignID = learningDesignID;
	}
	public LessonDetailsDTO(Lesson lesson){
		this.lessonID = lesson.getLessonId();
		
		this.duration = lesson.getLearningDesign().getDuration();
		
		this.createDateTime = lesson.getCreateDateTime();
		
		this.startDateTime = lesson.getStartDateTime();
		
		this.organisationID = lesson.getOrganisation()!=null?
							  lesson.getOrganisation().getOrganisationId():
							  WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
		
        this.organisationName = lesson.getOrganisation()!=null?
								lesson.getOrganisation().getName():
								WDDXTAGS.STRING_NULL_VALUE;
		
		this.organisationDescription = lesson.getOrganisation()!=null?
									   lesson.getOrganisation().getDescription():
									   WDDXTAGS.STRING_NULL_VALUE;
		
		this.workspaceFolderID = lesson.getLearningDesign().getWorkspaceFolder()!=null?
								 lesson.getLearningDesign().getWorkspaceFolder().getWorkspaceFolderId():
								 WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
		
		this.licenseID = lesson.getLearningDesign().getLicense()!=null?
						 lesson.getLearningDesign().getLicense().getLicenseID():
					     WDDXTAGS.NUMERIC_NULL_VALUE_LONG;
		
		this.licenseText = lesson.getLearningDesign().getLicenseText()!=null?
						   lesson.getLearningDesign().getLicenseText():
					       WDDXTAGS.STRING_NULL_VALUE;
		
		this.lessonStateID = lesson.getLessonStateId();
		
		this.learningDesignID = lesson.getLearningDesign().getLearningDesignId();		
	}	
	/**
	 * @return Returns the createDateTime.
	 */
	public Date getCreateDateTime() {
		return createDateTime!=null?createDateTime:WDDXTAGS.DATE_NULL_VALUE;
	}
	/**
	 * @return Returns the duration.
	 */
	public Long getDuration() {
		return duration!=null?duration:WDDXTAGS.NUMERIC_NULL_VALUE_LONG;
	}
	/**
	 * @return Returns the learningDesignID.
	 */
	public Long getLearningDesignID() {
		return learningDesignID!=null?learningDesignID:WDDXTAGS.NUMERIC_NULL_VALUE_LONG;
	}
	/**
	 * @return Returns the lessonID.
	 */
	public Long getLessonID() {
		return lessonID!=null?lessonID:WDDXTAGS.NUMERIC_NULL_VALUE_LONG;
	}
	/**
	 * @return Returns the lessonStateID.
	 */
	public Integer getLessonStateID() {
		return lessonStateID!=null?lessonStateID:WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
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
	 * @return Returns the organisationDescription.
	 */
	public String getOrganisationDescription() {
		return organisationDescription!=null?organisationDescription:WDDXTAGS.STRING_NULL_VALUE;
	}
	/**
	 * @return Returns the organisationID.
	 */
	public Integer getOrganisationID() {
		return organisationID!=null?organisationID:WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
	}
	/**
	 * @return Returns the organisationName.
	 */
	public String getOrganisationName() {
		return organisationName!=null?organisationName:WDDXTAGS.STRING_NULL_VALUE;
	}
	/**
	 * @return Returns the startDateTime.
	 */
	public Date getStartDateTime() {
		return startDateTime!=null?startDateTime:WDDXTAGS.DATE_NULL_VALUE;
	}
	/**
	 * @return Returns the workspaceFolderID.
	 */
	public Integer getWorkspaceFolderID() {
		return workspaceFolderID!=null?workspaceFolderID:WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
	}
}
