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



package org.lamsfoundation.lams.tool.leaderselection.dto;

import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.leaderselection.model.Leaderselection;
import org.lamsfoundation.lams.tool.leaderselection.model.LeaderselectionSession;

public class LeaderselectionDTO {

    private static Logger logger = Logger.getLogger(LeaderselectionDTO.class);

    private Long toolContentId;

    private String title;

    private String instructions;

    private boolean defineLater;

    private boolean contentInUse;

    private Set<LeaderselectionSessionDTO> sessionDTOs = new TreeSet<LeaderselectionSessionDTO>();

    private Long currentTab;

    /* Constructors */
    public LeaderselectionDTO() {
    }

    public LeaderselectionDTO(Leaderselection leaderselection) {
	toolContentId = leaderselection.getToolContentId();
	title = leaderselection.getTitle();
	instructions = leaderselection.getInstructions();
	contentInUse = leaderselection.isContentInUse();

	for (LeaderselectionSession session : (Set<LeaderselectionSession>) leaderselection
		.getLeaderselectionSessions()) {
	    LeaderselectionSessionDTO sessionDTO = new LeaderselectionSessionDTO(session);

	    sessionDTOs.add(sessionDTO);
	}
    }

    /* Getters / Setters */
    public Set<LeaderselectionSessionDTO> getSessionDTOs() {
	return sessionDTOs;
    }

    public void setSessionDTOs(Set<LeaderselectionSessionDTO> sessionDTOs) {
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

    public Long getCurrentTab() {
	return currentTab;
    }

    public void setCurrentTab(Long currentTab) {
	this.currentTab = currentTab;
    }
}
