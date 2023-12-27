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



package org.lamsfoundation.lams.tool.wiki.dto;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.wiki.model.Wiki;
import org.lamsfoundation.lams.tool.wiki.model.WikiSession;

public class WikiDTO {

    private static Logger logger = Logger.getLogger(WikiDTO.class);

    public Long toolContentId;

    public String title;

    public String instructions;

    public boolean defineLater;

    public boolean contentInUse;

    public boolean lockOnFinish;

    private boolean allowLearnerCreatePages;

    private boolean allowLearnerInsertLinks;

    private boolean allowLearnerAttachImages;

    private boolean notifyUpdates;

    private Integer minimumEdits;

    private Integer maximumEdits;
   
    public Date submissionDeadline;

    

	public Set<WikiSessionDTO> sessionDTOs = new TreeSet<WikiSessionDTO>();

    public Long currentTab;

    /* Constructors */
    public WikiDTO() {
    }

    public WikiDTO(Wiki wiki) {
	toolContentId = wiki.getToolContentId();
	title = wiki.getTitle();
	instructions = wiki.getInstructions();
	contentInUse = wiki.isContentInUse();
	lockOnFinish = wiki.isLockOnFinished();
	allowLearnerCreatePages = wiki.isAllowLearnerCreatePages();
	allowLearnerInsertLinks = wiki.isAllowLearnerInsertLinks();
	allowLearnerAttachImages = wiki.isAllowLearnerAttachImages();
	notifyUpdates = wiki.isNotifyUpdates();
    submissionDeadline = wiki.getSubmissionDeadline();
	minimumEdits = wiki.getMinimumEdits();
	maximumEdits = wiki.getMaximumEdits();
	

	for (Iterator<WikiSession> iter = wiki.getWikiSessions().iterator(); iter.hasNext();) {
	    WikiSession session = iter.next();
	    WikiSessionDTO sessionDTO = new WikiSessionDTO(session);

	    sessionDTOs.add(sessionDTO);
	}
    }

    /* Getters / Setters */
    public Set<WikiSessionDTO> getSessionDTOs() {
	return sessionDTOs;
    }

    public void setSessionDTOs(Set<WikiSessionDTO> sessionDTOs) {
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

    public boolean isDefineLater() {
	return defineLater;
    }

    public void setDefineLater(boolean defineLater) {
	this.defineLater = defineLater;
    }

    public boolean isAllowLearnerCreatePages() {
	return allowLearnerCreatePages;
    }

    public void setAllowLearnerCreatePages(boolean allowLearnerCreatePages) {
	this.allowLearnerCreatePages = allowLearnerCreatePages;
    }

    public boolean isAllowLearnerInsertLinks() {
	return allowLearnerInsertLinks;
    }

    public void setAllowLearnerInsertLinks(boolean allowLearnerInsertLinks) {
	this.allowLearnerInsertLinks = allowLearnerInsertLinks;
    }

    public boolean isAllowLearnerAttachImages() {
	return allowLearnerAttachImages;
    }

    public void setAllowLearnerAttachImages(boolean allowLearnerAttachImages) {
	this.allowLearnerAttachImages = allowLearnerAttachImages;
    }

    public boolean isNotifyUpdates() {
	return notifyUpdates;
    }

    public void setNotifyUpdates(boolean notifyUpdates) {
	this.notifyUpdates = notifyUpdates;
    }

    public Integer getMinimumEdits() {
	return minimumEdits;
    }

    public void setMinimumEdits(Integer minimumEdits) {
	this.minimumEdits = minimumEdits;
    }

    public Integer getMaximumEdits() {
	return maximumEdits;
    }

    public void setMaximumEdits(Integer maximumEdits) {
	this.maximumEdits = maximumEdits;
    }

    public void setContentInUse(boolean contentInUse) {
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
    
    public Date getSubmissionDeadline() {
    	return submissionDeadline;
        }

    public void setSubmissionDeadline(Date submissionDeadline) {
    	this.submissionDeadline = submissionDeadline;
        }
}
