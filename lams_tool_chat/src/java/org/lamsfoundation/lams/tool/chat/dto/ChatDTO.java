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



package org.lamsfoundation.lams.tool.chat.dto;

import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.chat.model.Chat;

public class ChatDTO {

    private static Logger logger = Logger.getLogger(ChatDTO.class);

    public Long toolContentId;

    public String title;

    public String instructions;

    public boolean defineLater;

    public boolean contentInUse;

    public boolean lockOnFinish;

    public boolean filteringEnabled;

    public String filteredKeyWords;

    public Date submissionDeadline;

    public Set<ChatSessionDTO> sessionDTOs = new TreeSet<ChatSessionDTO>();;

    public Long currentTab;

    public ChatDTO(Chat chat) {
	toolContentId = chat.getToolContentId();
	title = chat.getTitle();
	instructions = chat.getInstructions();
	contentInUse = chat.isContentInUse();
	lockOnFinish = chat.isLockOnFinished();
	filteringEnabled = chat.isFilteringEnabled();
	filteredKeyWords = chat.getFilterKeywords();
	submissionDeadline = chat.getSubmissionDeadline();
    }

    public Set<ChatSessionDTO> getSessionDTOs() {
	return sessionDTOs;
    }

    public void setSessionDTOs(Set<ChatSessionDTO> sessionDTOs) {
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

    public boolean isContentInUse() {
	return contentInUse;
    }

    public void setContentInUse(boolean contentInUse) {
	this.contentInUse = contentInUse;
    }

    public boolean isDefineLater() {
	return defineLater;
    }

    public void setDefineLater(boolean defineLater) {
	this.defineLater = defineLater;
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

    public boolean isFilteringEnabled() {
	return filteringEnabled;
    }

    public void setFilteringEnabled(boolean filteringEnabled) {
	this.filteringEnabled = filteringEnabled;
    }

    public String getFilteredKeyWords() {
	return filteredKeyWords;
    }

    public void setFilteredKeyWords(String filteredKeyWords) {
	this.filteredKeyWords = filteredKeyWords;
    }

    public Date getSubmissionDeadline() {
	return submissionDeadline;
    }

    public void setSubmissionDeadline(Date submissionDeadline) {
	this.submissionDeadline = submissionDeadline;
    }
}