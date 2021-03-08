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

import java.util.Set;
import java.util.TreeSet;

import org.lamsfoundation.lams.tool.zoom.model.Zoom;
import org.lamsfoundation.lams.tool.zoom.model.ZoomSession;

public class ContentDTO {

    Long toolContentId;

    String title;

    String instructions;

    boolean reflectOnActivity;

    String reflectInstructions;

    boolean startInMonitor;

    Integer duration;

    Long currentTab;

    boolean contentInUse;

    boolean isGroupedActivity; // set manually in MonitoringAction

    Set<SessionDTO> sessionDTOs = new TreeSet<>();

    /* Constructors */
    public ContentDTO() {
    }

    public ContentDTO(Zoom zoom) {
	this.toolContentId = zoom.getToolContentId();
	this.title = zoom.getTitle();
	this.instructions = zoom.getInstructions();
	this.contentInUse = zoom.isContentInUse();
	this.reflectInstructions = zoom.getReflectInstructions();
	this.reflectOnActivity = zoom.isReflectOnActivity();
	this.startInMonitor = zoom.isStartInMonitor();
	this.duration = zoom.getDuration();
	for (ZoomSession session : zoom.getZoomSessions()) {
	    sessionDTOs.add(new SessionDTO(session));
	}

	isGroupedActivity = false;
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

    public boolean isStartInMonitor() {
	return startInMonitor;
    }

    public void setStartInMonitor(boolean startInMonitor) {
	this.startInMonitor = startInMonitor;
    }

    public Integer getDuration() {
	return duration;
    }

    public void setDuration(Integer duration) {
	this.duration = duration;
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

    public Boolean getContentInUse() {
	return contentInUse;
    }

    public void setContentInUse(Boolean contentInUse) {
	this.contentInUse = contentInUse;
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

    public Set<SessionDTO> getSessionDTOs() {
	return sessionDTOs;
    }

    public void setSessionDTOs(Set<SessionDTO> sessionDTOs) {
	this.sessionDTOs = sessionDTOs;
    }
}