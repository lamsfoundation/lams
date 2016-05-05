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

/* $Id$ */

package org.lamsfoundation.lams.tool.wookie.dto;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.wookie.model.Wookie;
import org.lamsfoundation.lams.tool.wookie.model.WookieSession;

public class WookieDTO {

    private static Logger logger = Logger.getLogger(WookieDTO.class);

    public Long toolContentId;

    public String title;

    public String instructions;

    public boolean defineLater;

    public boolean contentInUse;

    public boolean reflectOnActivity;

    public boolean lockOnFinish;

    public Set<WookieSessionDTO> sessionDTOs = new TreeSet<WookieSessionDTO>();

    public Long currentTab;

    private String imageFileName;

    private String reflectInstructions;

    /* Constructors */
    public WookieDTO() {
    }

    public WookieDTO(Wookie wookie) {
	this.toolContentId = wookie.getToolContentId();
	this.title = wookie.getTitle();
	this.instructions = wookie.getInstructions();
	this.contentInUse = wookie.isContentInUse();
	this.reflectOnActivity = wookie.isReflectOnActivity();
	this.lockOnFinish = wookie.isLockOnFinished();
	this.reflectInstructions = wookie.getReflectInstructions();

	for (Iterator<WookieSession> iter = wookie.getWookieSessions().iterator(); iter.hasNext();) {
	    WookieSession session = iter.next();
	    WookieSessionDTO sessionDTO = new WookieSessionDTO(session);

	    sessionDTOs.add(sessionDTO);
	}
    }

    /* Getters / Setters */
    public Set<WookieSessionDTO> getSessionDTOs() {
	return sessionDTOs;
    }

    public void setSessionDTOs(Set<WookieSessionDTO> sessionDTOs) {
	this.sessionDTOs = sessionDTOs;
    }

    public String getInstructions() {
	return instructions;
    }

    public void setInstructions(String instructions) {
	this.instructions = instructions;
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

    public boolean isReflectOnActivity() {
	return reflectOnActivity;
    }

    public void setReflectOnActivity(boolean reflectOnActivity) {
	this.reflectOnActivity = reflectOnActivity;
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

    public String getImageFileName() {
	return imageFileName;
    }

    public void getImageFileName(String imageFileName) {
	this.imageFileName = imageFileName;
    }

    public void setImageFileName(String imageFileName) {
	this.imageFileName = imageFileName;
    }

    public String getReflectInstructions() {
	return reflectInstructions;
    }

    public void setReflectInstructions(String reflectInstructions) {
	this.reflectInstructions = reflectInstructions;
    }
}
