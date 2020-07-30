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

package org.lamsfoundation.lams.tool.dto;

public class ToolDTO {
    private Long toolId;
    private Long learningLibraryId;
    private String learningLibraryTitle;
    private Long defaultToolContentId;
    private String toolDisplayName;
    private String iconPath;
    private Boolean supportsOutputs;
    private Long[] childToolIds;
    private Boolean valid;

    public ToolDTO() {
    }

    public Long getToolId() {
	return toolId;
    }

    public void setToolId(Long toolID) {
	this.toolId = toolID;
    }

    public String getToolDisplayName() {
	return toolDisplayName;
    }

    public void setToolDisplayName(String displayName) {
	this.toolDisplayName = displayName;
    }

    public String getIconPath() {
	return iconPath;
    }

    public void setIconPath(String iconPath) {
	this.iconPath = iconPath;
    }

    public Boolean getSupportsOutputs() {
	return supportsOutputs;
    }

    public void setSupportsOutputs(Boolean supportsOutputs) {
	this.supportsOutputs = supportsOutputs;
    }

    public Long getLearningLibraryId() {
	return learningLibraryId;
    }

    public void setLearningLibraryId(Long learningLibraryId) {
	this.learningLibraryId = learningLibraryId;
    }

    public String getLearningLibraryTitle() {
	return learningLibraryTitle;
    }

    public void setLearningLibraryTitle(String learningLibraryTitle) {
	this.learningLibraryTitle = learningLibraryTitle;
    }

    public Long getDefaultToolContentId() {
	return defaultToolContentId;
    }

    public void setDefaultToolContentId(Long defaultToolContentId) {
	this.defaultToolContentId = defaultToolContentId;
    }

    public Long[] getChildToolIds() {
	return childToolIds;
    }

    public void setChildToolIds(Long[] childLearningLibraryIds) {
	this.childToolIds = childLearningLibraryIds;
    }

    public Boolean getValid() {
	return valid;
    }

    public void setValid(Boolean valid) {
	this.valid = valid;
    }
}