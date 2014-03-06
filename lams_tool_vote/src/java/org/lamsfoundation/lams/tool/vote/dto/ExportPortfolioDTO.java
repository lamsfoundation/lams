/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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
 * ***********************************************************************/
/* $$Id$$ */
package org.lamsfoundation.lams.tool.vote.dto;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <p>
 * DTO that holds export portfolio properties
 * </p>
 * 
 * @author Ozgur Demirtas
 */
public class ExportPortfolioDTO {
    protected String portfolioExportMode;
    protected boolean userExceptionNoToolSessions;
    protected LinkedList<SessionDTO> sessionDtos;

    /**
     * @return Returns the listMonitoredAnswersContainerDto.
     */
    public LinkedList<SessionDTO> getSessionDtos() {
	return sessionDtos;
    }

    /**
     * @param listMonitoredAnswersContainerDto
     *            The listMonitoredAnswersContainerDto to set.
     */
    public void setSessionDtos(LinkedList<SessionDTO> sessionDTOs) {
	this.sessionDtos = sessionDTOs;
    }

    /**
     * @return Returns the portfolioExportMode.
     */
    public String getPortfolioExportMode() {
	return portfolioExportMode;
    }

    /**
     * @param portfolioExportMode
     *            The portfolioExportMode to set.
     */
    public void setPortfolioExportMode(String portfolioExportMode) {
	this.portfolioExportMode = portfolioExportMode;
    }

    /**
     * @return Returns the userExceptionNoToolSessions.
     */
    public boolean getUserExceptionNoToolSessions() {
	return userExceptionNoToolSessions;
    }

    /**
     * @param userExceptionNoToolSessions
     *            The userExceptionNoToolSessions to set.
     */
    public void setUserExceptionNoToolSessions(boolean userExceptionNoToolSessions) {
	this.userExceptionNoToolSessions = userExceptionNoToolSessions;
    }
}
