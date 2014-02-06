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
package org.lamsfoundation.lams.tool.vote;

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
public class ExportPortfolioDTO implements Comparable {
    protected String portfolioExportMode;
    protected String userExceptionNoToolSessions;
    protected String showResults;

    protected Map mapStandardNominationsHTMLedContent;
    protected Map mapStandardNominationsContent;
    protected Map mapStandardUserCount;
    protected Map mapStandardRatesContent;

    protected List listUserEntries;
    protected List listMonitoredAnswersContainerDto;

    protected boolean allowText;

    public int compareTo(Object o) {
	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = (VoteGeneralAuthoringDTO) o;

	if (voteGeneralAuthoringDTO == null)
	    return 1;
	else
	    return 0;
    }

    public String toString() {
	return new ToStringBuilder(this).append("portfolioExportMode: ", portfolioExportMode)
		.append("userExceptionNoToolSessions: ", userExceptionNoToolSessions)
		.append("mapStandardNominationsHTMLedContent: ", mapStandardNominationsHTMLedContent)
		.append("mapStandardUserCount: ", mapStandardUserCount)
		.append("mapStandardRatesContent: ", mapStandardRatesContent)
		.append("listUserEntries: ", listUserEntries)
		.append("listMonitoredAnswersContainerDto: ", listMonitoredAnswersContainerDto).toString();
    }

    /**
     * @return Returns the listMonitoredAnswersContainerDto.
     */
    public List getListMonitoredAnswersContainerDto() {
	return listMonitoredAnswersContainerDto;
    }

    /**
     * @param listMonitoredAnswersContainerDto
     *            The listMonitoredAnswersContainerDto to set.
     */
    public void setListMonitoredAnswersContainerDto(List listMonitoredAnswersContainerDto) {
	this.listMonitoredAnswersContainerDto = listMonitoredAnswersContainerDto;
    }

    /**
     * @return Returns the listUserEntries.
     */
    public List getListUserEntries() {
	return listUserEntries;
    }

    /**
     * @param listUserEntries
     *            The listUserEntries to set.
     */
    public void setListUserEntries(List listUserEntries) {
	this.listUserEntries = listUserEntries;
    }

    /**
     * @return Returns the mapStandardNominationsHTMLedContent.
     */
    public Map getMapStandardNominationsHTMLedContent() {
	return mapStandardNominationsHTMLedContent;
    }

    /**
     * @param mapStandardNominationsHTMLedContent
     *            The mapStandardNominationsHTMLedContent to set.
     */
    public void setMapStandardNominationsHTMLedContent(Map mapStandardNominationsHTMLedContent) {
	this.mapStandardNominationsHTMLedContent = mapStandardNominationsHTMLedContent;
    }

    /**
     * @return Returns the mapStandardRatesContent.
     */
    public Map getMapStandardRatesContent() {
	return mapStandardRatesContent;
    }

    /**
     * @param mapStandardRatesContent
     *            The mapStandardRatesContent to set.
     */
    public void setMapStandardRatesContent(Map mapStandardRatesContent) {
	this.mapStandardRatesContent = mapStandardRatesContent;
    }

    /**
     * @return Returns the mapStandardUserCount.
     */
    public Map getMapStandardUserCount() {
	return mapStandardUserCount;
    }

    /**
     * @param mapStandardUserCount
     *            The mapStandardUserCount to set.
     */
    public void setMapStandardUserCount(Map mapStandardUserCount) {
	this.mapStandardUserCount = mapStandardUserCount;
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
    public String getUserExceptionNoToolSessions() {
	return userExceptionNoToolSessions;
    }

    /**
     * @param userExceptionNoToolSessions
     *            The userExceptionNoToolSessions to set.
     */
    public void setUserExceptionNoToolSessions(String userExceptionNoToolSessions) {
	this.userExceptionNoToolSessions = userExceptionNoToolSessions;
    }

    public Map getMapStandardNominationsContent() {
	return mapStandardNominationsContent;
    }

    public void setMapStandardNominationsContent(Map mapStandardNominationsContent) {
	this.mapStandardNominationsContent = mapStandardNominationsContent;
    }

    public String getShowResults() {
	return showResults;
    }

    public void setShowResults(String showResults) {
	this.showResults = showResults;
    }

    public boolean isAllowText() {
	return allowText;
    }

    public void setAllowText(boolean allowText) {
	this.allowText = allowText;
    }
}
