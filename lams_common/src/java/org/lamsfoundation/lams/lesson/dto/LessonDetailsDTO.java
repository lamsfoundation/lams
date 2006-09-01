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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.lesson.dto;

import java.util.Date;
import java.text.DateFormat;
import java.util.Set;

import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.util.wddx.WDDXTAGS;

/**
 * @author Manpreet Minhas
 */
public class LessonDetailsDTO {
	
	private Long lessonID;
	private String lessonName;
	private String lessonDescription;
	private Integer lessonStateID;
	private Date createDateTime;
	private Date startDateTime;
    private Date scheduleStartDate;
    private String scheduleStartDateStr;
    private Date scheduleEndDate;
 	private Long duration;	
	private Integer organisationID;
	private String organisationName;
	private String organisationDescription;	
	private Integer workspaceFolderID;	
	private String contentFolderID;
	private Long licenseID;
	private String licenseText;	
	private Long learningDesignID;
	private Integer numberPossibleLearners;
	private Integer numberStartedLearners;
	
	/** Create the DTO based on the lesson. Sets up all the fields except numberStartedLearners */
	public LessonDetailsDTO(Lesson lesson){
		this.lessonID = lesson.getLessonId();
		this.lessonName = lesson.getLessonName();
		this.lessonDescription = lesson.getLessonDescription();

		this.lessonStateID = lesson.getLessonStateId();
		this.createDateTime = lesson.getCreateDateTime();
		this.startDateTime = lesson.getStartDateTime();
		this.scheduleStartDate = lesson.getScheduleStartDate();
		this.scheduleStartDateStr = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL).format(this.scheduleStartDate);
		
		this.scheduleEndDate = lesson.getScheduleEndDate();
		
		this.duration = lesson.getLearningDesign().getDuration();
		
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
		
		this.contentFolderID = lesson.getLearningDesign().getContentFolderID()!=null?
							   lesson.getLearningDesign().getContentFolderID():
							   WDDXTAGS.STRING_NULL_VALUE;
							   
		this.licenseID = lesson.getLearningDesign().getLicense()!=null?
						 lesson.getLearningDesign().getLicense().getLicenseID():
					     WDDXTAGS.NUMERIC_NULL_VALUE_LONG;
		
		this.licenseText = lesson.getLearningDesign().getLicenseText()!=null?
						   lesson.getLearningDesign().getLicenseText():
					       WDDXTAGS.STRING_NULL_VALUE;
		
		this.learningDesignID = lesson.getLearningDesign().getLearningDesignId();		

		Set allLearners = lesson.getAllLearners();
		this.numberPossibleLearners = new Integer(allLearners !=null ? allLearners.size() : 0);
		this.numberStartedLearners = new Integer(0);
	}	
	public Date getScheduleEndDate() {
		return scheduleEndDate;
	}
	public Date getScheduleStartDate() {
		return scheduleStartDate;
	}
	public String getScheduleStartDateStr() {
		return scheduleStartDateStr;
	}
	public void setScheduleStartDateStr(String scheduleStartDateStr) {
		this.scheduleStartDateStr = scheduleStartDateStr;
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
	/**
	 * 
	 * @return Returns the contentFolderID.
	 */
	public String getContentFolderID() {
		return contentFolderID!=null?contentFolderID:WDDXTAGS.STRING_NULL_VALUE;
	}
	public Integer getNumberStartedLearners() {
		return numberStartedLearners;
	}
	public void setNumberStartedLearners(Integer numberStartedLearners) {
		this.numberStartedLearners = numberStartedLearners;
	}
	public String getLessonDescription() {
		return lessonDescription;
	}
	public String getLessonName() {
		return lessonName;
	}
	public Integer getNumberPossibleLearners() {
		return numberPossibleLearners;
	}
}
