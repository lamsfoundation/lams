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

package org.lamsfoundation.lams.lesson.dto;

import java.util.Date;

import org.lamsfoundation.lams.lesson.Lesson;

/**
 * @author Manpreet Minhas
 */
public class LessonDetailsDTO {

    private Long lessonID;
    private String lessonName;
    private String lessonIntro;;
    private String learningDesignDescription;
    private Integer lessonStateID;
    private Date createDateTime;
    private String createDateTimeStr;
    private Date startDateTime;
    private String startDateTimeStr;
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
    private Boolean lockedForEdit;
    private Integer lockedForEditUserId;
    private String lockedForEditUsername;
    private Boolean learnerPresenceAvailable;
    private Boolean learnerImAvailable;
    private Boolean liveEditEnabled;
    private Boolean isPreview;
    private String encodedLessonID;
    private Boolean enabledLessonNotifications;
    private Boolean enableLessonIntro;
    private Boolean gradebookOnComplete;

    /**
     * Create the DTO based on the lesson. Sets up all the fields except numberStartedLearners
     */
    public LessonDetailsDTO(Lesson lesson) {
	this.lessonID = lesson.getLessonId();
	this.lessonName = lesson.getLessonName();
	this.lessonIntro = lesson.getLessonDescription();
	this.learningDesignDescription = lesson.getLearningDesign().getDescription();
	this.lessonStateID = lesson.getLessonStateId();
	this.createDateTime = lesson.getCreateDateTime();
	this.createDateTimeStr = null;
	this.startDateTime = lesson.getStartDateTime();
	this.startDateTimeStr = null;
	this.scheduleStartDate = lesson.getScheduleStartDate();
	// if(this.scheduleStartDate != null) {
	// this.scheduleStartDateStr =
	// DateFormat.getDateTimeInstance(DateFormat.FULL,
	// DateFormat.FULL).format(this.scheduleStartDate);

	// } else {
	this.scheduleStartDateStr = null;
	// }
	this.scheduleEndDate = lesson.getScheduleEndDate();

	this.duration = lesson.getLearningDesign().getDuration();

	this.organisationID = lesson.getOrganisation() != null ? lesson.getOrganisation().getOrganisationId() : null;

	this.organisationName = lesson.getOrganisation() != null ? lesson.getOrganisation().getName() : null;

	this.organisationDescription = lesson.getOrganisation() != null ? lesson.getOrganisation().getDescription()
		: null;

	this.workspaceFolderID = lesson.getLearningDesign().getWorkspaceFolder() != null
		? lesson.getLearningDesign().getWorkspaceFolder().getWorkspaceFolderId()
		: null;

	this.contentFolderID = lesson.getLearningDesign().getContentFolderID() != null
		? lesson.getLearningDesign().getContentFolderID()
		: null;

	this.licenseID = lesson.getLearningDesign().getLicense() != null
		? lesson.getLearningDesign().getLicense().getLicenseID()
		: null;

	this.licenseText = lesson.getLearningDesign().getLicenseText();

	this.learningDesignID = lesson.getLearningDesign().getLearningDesignId();

	this.numberPossibleLearners = lesson.getAllLearners().size();
	this.numberStartedLearners = 0;

	this.learnerPresenceAvailable = lesson.getLearnerPresenceAvailable();
	this.learnerImAvailable = lesson.getLearnerImAvailable();

	this.liveEditEnabled = lesson.getLiveEditEnabled();

	this.lockedForEdit = lesson.getLockedForEdit();
	if (this.lockedForEdit && lesson.getLearningDesign().getEditOverrideUser() != null) {
	    this.lockedForEditUserId = lesson.getLearningDesign().getEditOverrideUser().getUserId();
	    this.lockedForEditUsername = lesson.getLearningDesign().getEditOverrideUser().getFullName();
	}

	this.isPreview = lesson.isPreviewLesson();
	this.enabledLessonNotifications = lesson.getEnableLessonNotifications();
	this.enableLessonIntro = lesson.isEnableLessonIntro();
	this.gradebookOnComplete = lesson.getGradebookOnComplete();
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
	return createDateTime;
    }

    public String getCreateDateTimeStr() {
	return createDateTimeStr;
    }

    public void setCreateDateTimeStr(String createDateTimeStr) {
	this.createDateTimeStr = createDateTimeStr;
    }

    /**
     * @return Returns the duration.
     */
    public Long getDuration() {
	return duration;
    }

    /**
     * @return Returns the learningDesignID.
     */
    public Long getLearningDesignID() {
	return learningDesignID;
    }

    /**
     * @return Returns the lessonID.
     */
    public Long getLessonID() {
	return lessonID;
    }

    /**
     * @return Returns the lessonStateID.
     */
    public Integer getLessonStateID() {
	return lessonStateID;
    }

    /**
     * @return Returns the licenseID.
     */
    public Long getLicenseID() {
	return licenseID;
    }

    /**
     * @return Returns the licenseText.
     */
    public String getLicenseText() {
	return licenseText;
    }

    /**
     * @return Returns the organisationDescription.
     */
    public String getOrganisationDescription() {
	return organisationDescription;
    }

    /**
     * @return Returns the organisationID.
     */
    public Integer getOrganisationID() {
	return organisationID;
    }

    /**
     * @return Returns the organisationName.
     */
    public String getOrganisationName() {
	return organisationName;
    }

    /**
     * @return Returns the startDateTime.
     */
    public Date getStartDateTime() {
	return startDateTime;
    }

    public String getStartDateTimeStr() {
	return startDateTimeStr;
    }

    public void setStartDateTimeStr(String startDateTimeStr) {
	this.startDateTimeStr = startDateTimeStr;
    }

    /**
     * @return Returns the workspaceFolderID.
     */
    public Integer getWorkspaceFolderID() {
	return workspaceFolderID;
    }

    /**
     *
     * @return Returns the contentFolderID.
     */
    public String getContentFolderID() {
	return contentFolderID;
    }

    public Integer getNumberStartedLearners() {
	return numberStartedLearners;
    }

    public void setNumberStartedLearners(Integer numberStartedLearners) {
	this.numberStartedLearners = numberStartedLearners;
    }

    public String getLearningDesignDescription() {
	return learningDesignDescription;
    }

    public String getLessonIntro() {
	return lessonIntro;
    }

    public String getLessonName() {
	return lessonName;
    }

    public Integer getNumberPossibleLearners() {
	return numberPossibleLearners;
    }

    public Boolean getLearnerPresenceAvailable() {
	return learnerPresenceAvailable;
    }

    public Boolean getLearnerImAvailable() {
	return learnerImAvailable;
    }

    public Boolean getLockedForEdit() {
	return lockedForEdit;
    }

    public Boolean getLiveEditEnabled() {
	return liveEditEnabled;
    }

    public Boolean getIsPreview() {
	return isPreview;
    }

    public Boolean getEnabledLessonNotifications() {
	return enabledLessonNotifications;

    }

    public void setEnabledLessonNotifications(Boolean enabledLessonNotifications) {
	this.enabledLessonNotifications = enabledLessonNotifications;
    }

    public void setIsPreview(Boolean isPreview) {
	this.isPreview = isPreview;
    }

    public void setLessonID(Long lessonID) {
	this.lessonID = lessonID;
    }

    public void setLessonName(String lessonName) {
	this.lessonName = lessonName;
    }

    public void setLearningDesignDescription(String lessonDescription) {
	this.learningDesignDescription = lessonDescription;
    }

    public void setLessonStateID(Integer lessonStateID) {
	this.lessonStateID = lessonStateID;
    }

    public void setCreateDateTime(Date createDateTime) {
	this.createDateTime = createDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
	this.startDateTime = startDateTime;
    }

    public void setScheduleStartDate(Date scheduleStartDate) {
	this.scheduleStartDate = scheduleStartDate;
    }

    public void setScheduleEndDate(Date scheduleEndDate) {
	this.scheduleEndDate = scheduleEndDate;
    }

    public void setDuration(Long duration) {
	this.duration = duration;
    }

    public void setOrganisationID(Integer organisationID) {
	this.organisationID = organisationID;
    }

    public void setOrganisationName(String organisationName) {
	this.organisationName = organisationName;
    }

    public void setOrganisationDescription(String organisationDescription) {
	this.organisationDescription = organisationDescription;
    }

    public void setWorkspaceFolderID(Integer workspaceFolderID) {
	this.workspaceFolderID = workspaceFolderID;
    }

    public void setContentFolderID(String contentFolderID) {
	this.contentFolderID = contentFolderID;
    }

    public void setLicenseID(Long licenseID) {
	this.licenseID = licenseID;
    }

    public void setLicenseText(String licenseText) {
	this.licenseText = licenseText;
    }

    public void setLearningDesignID(Long learningDesignID) {
	this.learningDesignID = learningDesignID;
    }

    public void setNumberPossibleLearners(Integer numberPossibleLearners) {
	this.numberPossibleLearners = numberPossibleLearners;
    }

    public void setLearnerPresenceAvailable(Boolean learnerPresenceAvailable) {
	this.learnerPresenceAvailable = learnerPresenceAvailable;
    }

    public void setLearnerImAvailable(Boolean learnerImAvailable) {
	this.learnerImAvailable = learnerImAvailable;
    }

    public void setLockedForEdit(Boolean lockedForEdit) {
	this.lockedForEdit = lockedForEdit;
    }

    public void setLiveEditEnabled(Boolean liveEditEnabled) {
	this.liveEditEnabled = liveEditEnabled;
    }

    public String getEncodedLessonID() {
	return encodedLessonID;
    }

    public void setEncodedLessonID(String encodedLessonID) {
	this.encodedLessonID = encodedLessonID;
    }

    public Boolean getEnableLessonIntro() {
	return enableLessonIntro;
    }

    public void setEnableLessonIntro(Boolean enableLessonIntro) {
	this.enableLessonIntro = enableLessonIntro;
    }

    public Boolean getGradebookOnComplete() {
	return gradebookOnComplete;
    }

    public void setGradebookOnComplete(Boolean gradebookOnComplete) {
	this.gradebookOnComplete = gradebookOnComplete;
    }

    public Integer getLockedForEditUserId() {
	return lockedForEditUserId;
    }

    public void setLockedForEditUserId(Integer lockedForEditUserId) {
	this.lockedForEditUserId = lockedForEditUserId;
    }

    public String getLockedForEditUsername() {
	return lockedForEditUsername;
    }

    public void setLockedForEditUsername(String lockedForEditUsername) {
	this.lockedForEditUsername = lockedForEditUsername;
    }
}
