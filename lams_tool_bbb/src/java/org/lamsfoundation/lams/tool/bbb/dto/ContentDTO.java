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


package org.lamsfoundation.lams.tool.bbb.dto;

import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.bbb.model.Bbb;
import org.lamsfoundation.lams.tool.bbb.model.BbbSession;

public class ContentDTO {

    private static final Logger logger = Logger.getLogger(ContentDTO.class);

    Long toolContentId;

    String title;

    String instructions;

    boolean reflectOnActivity;

    String reflectInstructions;

    boolean defineLater;

    boolean contentInUse;
    
    boolean lockOnFinish;

    Set<SessionDTO> sessionDTOs = new TreeSet<SessionDTO>();

    Long currentTab;

    boolean isGroupedActivity; // set manually in MonitoringAction


    /* Constructors */
    public ContentDTO() {
    }

    public ContentDTO(Bbb bbb) {
	this.toolContentId = bbb.getToolContentId();
	this.title = bbb.getTitle();
	this.instructions = bbb.getInstructions();
	this.contentInUse = bbb.isContentInUse();
	this.reflectInstructions = bbb.getReflectInstructions();
	this.reflectOnActivity = bbb.isReflectOnActivity();
	this.lockOnFinish = bbb.isLockOnFinished();

	for (BbbSession bbbSession : bbb.getBbbSessions()) {
	    sessionDTOs.add(new SessionDTO(bbbSession));
	}
	
	isGroupedActivity = false;
    }

    /* Getters / Setters */
    public Set<SessionDTO> getSessionDTOs() {
	return sessionDTOs;
    }

    public void setSessionDTOs(Set<SessionDTO> sessionDTOs) {
	this.sessionDTOs = sessionDTOs;
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

    public Boolean getContentInUse() {
	return contentInUse;
    }

    public void setContentInUse(Boolean contentInUse) {
	this.contentInUse = contentInUse;
    }

    public boolean isLockOnFinish() {
	return lockOnFinish;
    }

    public void setLockOnFinish(boolean lockOnFinish) {
	this.lockOnFinish = lockOnFinish;
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
