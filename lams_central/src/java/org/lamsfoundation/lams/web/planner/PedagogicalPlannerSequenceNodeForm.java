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

package org.lamsfoundation.lams.web.planner;

import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.planner.PedagogicalPlannerSequenceNode;

public class PedagogicalPlannerSequenceNodeForm {
    public final static String NODE_TYPE_SUBNODES = "subnodes";
    public final static String NODE_TYPE_TEMPLATE = "template";

    private Long uid;
    private Long parentUid;
    private String contentFolderId;
    private String title;
    private String briefDescription;
    private String fullDescription;
    private FormFile file;
    private Boolean removeTemplate;
    private String nodeType = PedagogicalPlannerSequenceNodeForm.NODE_TYPE_SUBNODES;

    private Boolean permitEditorViewTemplate;
    private Boolean permitEditorModifyTemplate;
    private Boolean permitEditorReplaceTemplate;
    private Boolean permitEditorRemoveNode;

    private Boolean permitTeacherViewTemplate;
    private Boolean permitTeacherEditCopy;
    private Boolean permitTeacherPreview;
    private Boolean permitTeacherViewCopyInFullAuthor;
    private Boolean permitTeacherExportCopy;
    private Boolean permitTeacherSaveCopy;

    /**
     * Set checkbox values according to compacted form of node permissions; set defaults if NULL
     */
    public void setPermissions(Integer nodePermissions) {
	//
	permitEditorViewTemplate = nodePermissions == null
		|| (nodePermissions & PedagogicalPlannerSequenceNode.PERMISSION_EDITOR_VIEW) != 0;
	permitEditorModifyTemplate = nodePermissions == null
		|| (nodePermissions & PedagogicalPlannerSequenceNode.PERMISSION_EDITOR_MODIFY) != 0;
	permitEditorReplaceTemplate = nodePermissions != null
		&& (nodePermissions & PedagogicalPlannerSequenceNode.PERMISSION_EDITOR_REPLACE) != 0;
	permitEditorRemoveNode = nodePermissions != null
		&& (nodePermissions & PedagogicalPlannerSequenceNode.PERMISSION_EDITOR_REMOVE) != 0;

	permitTeacherViewTemplate = nodePermissions == null
		|| (nodePermissions & PedagogicalPlannerSequenceNode.PERMISSION_TEACHER_VIEW) != 0;
	permitTeacherEditCopy = nodePermissions == null
		|| (nodePermissions & PedagogicalPlannerSequenceNode.PERMISSION_TEACHER_COPY) != 0;
	permitTeacherPreview = nodePermissions == null
		|| (nodePermissions & PedagogicalPlannerSequenceNode.PERMISSION_TEACHER_PREVIEW) != 0;
	permitTeacherViewCopyInFullAuthor = nodePermissions == null
		|| (nodePermissions & PedagogicalPlannerSequenceNode.PERMISSION_TEACHER_VIEW_IN_FULL_AUTHOR) != 0;
	permitTeacherExportCopy = nodePermissions == null
		|| (nodePermissions & PedagogicalPlannerSequenceNode.PERMISSION_TEACHER_EXPORT) != 0;
	permitTeacherSaveCopy = nodePermissions == null
		|| (nodePermissions & PedagogicalPlannerSequenceNode.PERMISSION_TEACHER_SAVE) != 0;
    }

    /**
     * Get compacted node permissions according to checkbox settings.
     */
    public int getPermissions() {
	int permissions = 0;
	permissions += Boolean.TRUE.equals(permitEditorViewTemplate)
		? PedagogicalPlannerSequenceNode.PERMISSION_EDITOR_VIEW
		: 0;
	permissions += Boolean.TRUE.equals(permitEditorModifyTemplate)
		? PedagogicalPlannerSequenceNode.PERMISSION_EDITOR_MODIFY
		: 0;
	permissions += Boolean.TRUE.equals(permitEditorReplaceTemplate)
		? PedagogicalPlannerSequenceNode.PERMISSION_EDITOR_REPLACE
		: 0;
	permissions += Boolean.TRUE.equals(permitEditorRemoveNode)
		? PedagogicalPlannerSequenceNode.PERMISSION_EDITOR_REMOVE
		: 0;
	permissions += Boolean.TRUE.equals(permitTeacherViewTemplate)
		? PedagogicalPlannerSequenceNode.PERMISSION_TEACHER_VIEW
		: 0;
	permissions += Boolean.TRUE.equals(permitTeacherEditCopy)
		? PedagogicalPlannerSequenceNode.PERMISSION_TEACHER_COPY
		: 0;
	permissions += Boolean.TRUE.equals(permitTeacherPreview)
		? PedagogicalPlannerSequenceNode.PERMISSION_TEACHER_PREVIEW
		: 0;
	permissions += Boolean.TRUE.equals(permitTeacherViewCopyInFullAuthor)
		? PedagogicalPlannerSequenceNode.PERMISSION_TEACHER_VIEW_IN_FULL_AUTHOR
		: 0;
	permissions += Boolean.TRUE.equals(permitTeacherExportCopy)
		? PedagogicalPlannerSequenceNode.PERMISSION_TEACHER_EXPORT
		: 0;
	permissions += Boolean.TRUE.equals(permitTeacherSaveCopy)
		? PedagogicalPlannerSequenceNode.PERMISSION_TEACHER_SAVE
		: 0;
	return permissions;
    }

    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getBriefDescription() {
	return briefDescription;
    }

    public void setBriefDescription(String briefDescription) {
	this.briefDescription = briefDescription;
    }

    public String getFullDescription() {
	return fullDescription;
    }

    public void setFullDescription(String fullDescription) {
	this.fullDescription = fullDescription;
    }

    public Long getParentUid() {
	return parentUid;
    }

    public void setParentUid(Long parentUid) {
	this.parentUid = parentUid;
    }

    public FormFile getFile() {
	return file;
    }

    public void setFile(FormFile file) {
	this.file = file;
    }

    public Boolean getRemoveTemplate() {
	return removeTemplate;
    }

    public void setRemoveTemplate(Boolean removeFile) {
	this.removeTemplate = removeFile;
    }

    public String getNodeType() {
	return nodeType;
    }

    public void setNodeType(String nodeType) {
	this.nodeType = nodeType;
    }

    public String getContentFolderId() {
	return contentFolderId;
    }

    public void setContentFolderId(String contentFolderId) {
	this.contentFolderId = contentFolderId;
    }

    public Boolean getPermitEditorViewTemplate() {
	return permitEditorViewTemplate;
    }

    public void setPermitEditorViewTemplate(Boolean permitEditorViewTemplate) {
	this.permitEditorViewTemplate = permitEditorViewTemplate;
    }

    public Boolean getPermitEditorModifyTemplate() {
	return permitEditorModifyTemplate;
    }

    public void setPermitEditorModifyTemplate(Boolean permitEditorEditTemplate) {
	this.permitEditorModifyTemplate = permitEditorEditTemplate;
    }

    public Boolean getPermitEditorReplaceTemplate() {
	return permitEditorReplaceTemplate;
    }

    public void setPermitEditorReplaceTemplate(Boolean permitEditorReplaceTemplate) {
	this.permitEditorReplaceTemplate = permitEditorReplaceTemplate;
    }

    public Boolean getPermitEditorRemoveNode() {
	return permitEditorRemoveNode;
    }

    public void setPermitEditorRemoveNode(Boolean permitEditorRemoveTemplate) {
	this.permitEditorRemoveNode = permitEditorRemoveTemplate;
    }

    public Boolean getPermitTeacherViewTemplate() {
	return permitTeacherViewTemplate;
    }

    public void setPermitTeacherViewTemplate(Boolean permitTeacherViewTemplate) {
	this.permitTeacherViewTemplate = permitTeacherViewTemplate;
    }

    public Boolean getPermitTeacherEditCopy() {
	return permitTeacherEditCopy;
    }

    public void setPermitTeacherEditCopy(Boolean permitTeacherEditCopy) {
	this.permitTeacherEditCopy = permitTeacherEditCopy;
    }

    public Boolean getPermitTeacherPreview() {
	return permitTeacherPreview;
    }

    public void setPermitTeacherPreview(Boolean permitTeacherPreview) {
	this.permitTeacherPreview = permitTeacherPreview;
    }

    public Boolean getPermitTeacherViewCopyInFullAuthor() {
	return permitTeacherViewCopyInFullAuthor;
    }

    public void setPermitTeacherViewCopyInFullAuthor(Boolean permitTeacherViewCopyInFullAuthor) {
	this.permitTeacherViewCopyInFullAuthor = permitTeacherViewCopyInFullAuthor;
    }

    public Boolean getPermitTeacherExportCopy() {
	return permitTeacherExportCopy;
    }

    public void setPermitTeacherExportCopy(Boolean permitTeacherExportCopy) {
	this.permitTeacherExportCopy = permitTeacherExportCopy;
    }

    public Boolean getPermitTeacherSaveCopy() {
	return permitTeacherSaveCopy;
    }

    public void setPermitTeacherSaveCopy(Boolean permitTeacherSaveCopy) {
	this.permitTeacherSaveCopy = permitTeacherSaveCopy;
    }
}