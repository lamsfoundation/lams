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

/* $Id$ */
package org.lamsfoundation.lams.web.planner;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

public class PedagogicalPlannerSequenceNodeForm extends ActionForm {
    public final static String NODE_TYPE_SUBNODES = "subnodes";
    public final static String NODE_TYPE_TEMPLATE = "template";

    private Long uid;
    private Long parentUid;
    private String contentFolderId;
    private String title;
    private String briefDescription;
    private String fullDescription;
    private FormFile file;
    private Boolean removeFile;
    private String nodeType = PedagogicalPlannerSequenceNodeForm.NODE_TYPE_SUBNODES;
    private Long fileUuid;

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

    public Boolean getRemoveFile() {
	return removeFile;
    }

    public void setRemoveFile(Boolean removeFile) {
	this.removeFile = removeFile;
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

    public Long getFileUuid() {
	return fileUuid;
    }

    public void setFileUuid(Long fileUuid) {
	this.fileUuid = fileUuid;
    }
}