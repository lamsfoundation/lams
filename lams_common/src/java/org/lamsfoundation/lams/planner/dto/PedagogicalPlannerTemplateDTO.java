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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */


package org.lamsfoundation.lams.planner.dto;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.planner.PedagogicalPlannerSequenceNode;

public class PedagogicalPlannerTemplateDTO {

    private static enum PermissionToLetterMapping {
	PREVIEW("P"), VIEW_IN_FULL_AUTHOR("V"), EXPORT("E"), SAVE("S"), CLOSE_EDITOR("C");

	private String letter;

	private PermissionToLetterMapping(String letter) {
	    this.letter = letter;
	}

	public String getLetter() {
	    return letter;
	}
    }

    private String sequenceTitle;
    private Boolean sendInPortions;
    private Integer activitiesPerPortion;
    private Integer submitDelay;
    private List<PedagogicalPlannerActivityDTO> activities;
    private Long learningDesignID;
    private Integer activitySupportingPlannerCount = 0;
    private Long nodeUid;

    private Boolean isEditor = false;
    private Boolean permitPreview = true;
    private Boolean permitViewInFullAuthor = true;
    private Boolean permitExport = true;
    private Boolean permitSave = true;
    private Boolean permitExitEditor = true;

    public void setPermissions(Integer nodePermissions, boolean isEditor) {
	// set permissions the view is based on
	this.isEditor = isEditor;
	permitPreview = isEditor || (nodePermissions == null)
		|| ((nodePermissions & PedagogicalPlannerSequenceNode.PERMISSION_TEACHER_PREVIEW) != 0);
	permitViewInFullAuthor = isEditor || (nodePermissions == null)
		|| ((nodePermissions & PedagogicalPlannerSequenceNode.PERMISSION_TEACHER_VIEW_IN_FULL_AUTHOR) != 0);
	permitExport = isEditor || (nodePermissions == null)
		|| ((nodePermissions & PedagogicalPlannerSequenceNode.PERMISSION_TEACHER_EXPORT) != 0);
	permitSave = isEditor || (nodePermissions == null)
		|| ((nodePermissions & PedagogicalPlannerSequenceNode.PERMISSION_TEACHER_SAVE) != 0);
    }

    public void overridePermissions(String forbid) {
	if (!StringUtils.isBlank(forbid)) {
	    permitPreview &= !forbid.contains(PermissionToLetterMapping.PREVIEW.getLetter());
	    permitViewInFullAuthor &= !forbid.contains(PermissionToLetterMapping.VIEW_IN_FULL_AUTHOR.getLetter());
	    permitExport &= !forbid.contains(PermissionToLetterMapping.EXPORT.getLetter());
	    permitSave &= !forbid.contains(PermissionToLetterMapping.SAVE.getLetter());
	    permitExitEditor &= !forbid.contains(PermissionToLetterMapping.CLOSE_EDITOR.getLetter());
	}
    }

    public String getSequenceTitle() {
	return sequenceTitle;
    }

    public void setSequenceTitle(String sequenceTitle) {
	this.sequenceTitle = sequenceTitle;
    }

    public Boolean getSendInPortions() {
	return sendInPortions;
    }

    public void setSendInPortions(Boolean sendInPortions) {
	this.sendInPortions = sendInPortions;
    }

    public Integer getActivitiesPerPortion() {
	return activitiesPerPortion;
    }

    public void setActivitiesPerPortion(Integer activitiesInPortion) {
	activitiesPerPortion = activitiesInPortion;
    }

    public Integer getSubmitDelay() {
	return submitDelay;
    }

    public void setSubmitDelay(Integer submitDelay) {
	this.submitDelay = submitDelay;
    }

    public List<PedagogicalPlannerActivityDTO> getActivities() {
	return activities;
    }

    public void setActivities(List<PedagogicalPlannerActivityDTO> activities) {
	this.activities = activities;
    }

    public Long getLearningDesignID() {
	return learningDesignID;
    }

    public void setLearningDesignID(Long learningDesignID) {
	this.learningDesignID = learningDesignID;
    }

    public Integer getActivitySupportingPlannerCount() {
	return activitySupportingPlannerCount;
    }

    public void setActivitySupportingPlannerCount(Integer activitySupportingPlannerCount) {
	this.activitySupportingPlannerCount = activitySupportingPlannerCount;
    }

    public Long getNodeUid() {
	return nodeUid;
    }

    public void setNodeUid(Long nodeUid) {
	this.nodeUid = nodeUid;
    }

    public Boolean getIsEditor() {
	return isEditor;
    }

    public void setIsEditor(Boolean isEditor) {
	this.isEditor = isEditor;
    }

    public Boolean getPermitPreview() {
	return permitPreview;
    }

    public void setPermitPreview(Boolean permitTeacherPreview) {
	this.permitPreview = permitTeacherPreview;
    }

    public Boolean getPermitViewInFullAuthor() {
	return permitViewInFullAuthor;
    }

    public void setPermitViewInFullAuthor(Boolean permitTeacherViewCopyInFullAuthor) {
	this.permitViewInFullAuthor = permitTeacherViewCopyInFullAuthor;
    }

    public Boolean getPermitExport() {
	return permitExport;
    }

    public void setPermitExport(Boolean permitTeacherExportCopy) {
	this.permitExport = permitTeacherExportCopy;
    }

    public Boolean getPermitSave() {
	return permitSave;
    }

    public void setPermitSave(Boolean permitTeacherSaveCopy) {
	this.permitSave = permitTeacherSaveCopy;
    }

    public Boolean getPermitExitEditor() {
	return permitExitEditor;
    }

    public void setPermitExitEditor(Boolean permitExitEditor) {
	this.permitExitEditor = permitExitEditor;
    }
}