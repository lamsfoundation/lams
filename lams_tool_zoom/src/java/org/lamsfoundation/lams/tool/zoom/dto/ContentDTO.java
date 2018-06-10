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

package org.lamsfoundation.lams.tool.zoom.dto;

import org.lamsfoundation.lams.tool.zoom.model.Zoom;

public class ContentDTO {

    Long toolContentId;

    String title;

    String instructions;

    boolean reflectOnActivity;

    String reflectInstructions;

    Long currentTab;

    boolean isGroupedActivity; // set manually in MonitoringAction

    /* Constructors */
    public ContentDTO() {
    }

    public ContentDTO(Zoom zoom) {
	this.toolContentId = zoom.getToolContentId();
	this.title = zoom.getTitle();
	this.instructions = zoom.getInstructions();
	this.reflectInstructions = zoom.getReflectInstructions();
	this.reflectOnActivity = zoom.isReflectOnActivity();
    }

    public String getInstructions() {
	return instructions;
    }

    public void setInstructions(String instructions) {
	this.instructions = instructions;
    }

    public boolean isReflectOnActivity() {
	return reflectOnActivity;
    }

    public void setReflectOnActivity(boolean reflectOnActivity) {
	this.reflectOnActivity = reflectOnActivity;
    }

    public String getReflectInstructions() {
	return reflectInstructions;
    }

    public void setReflectInstructions(String reflectInstructions) {
	this.reflectInstructions = reflectInstructions;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public Long getToolContentId() {
	return toolContentId;
    }

    public void setToolContentId(Long toolContentID) {
	this.toolContentId = toolContentID;
    }

    public Long getCurrentTab() {
	return currentTab;
    }

    public void setCurrentTab(Long currentTab) {
	this.currentTab = currentTab;
    }

    public boolean isGroupedActivity() {
	return isGroupedActivity;
    }

    public void setGroupedActivity(boolean isGroupedActivity) {
	this.isGroupedActivity = isGroupedActivity;
    }
}