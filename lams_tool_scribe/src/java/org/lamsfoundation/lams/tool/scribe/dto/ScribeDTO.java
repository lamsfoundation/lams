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



package org.lamsfoundation.lams.tool.scribe.dto;

import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.scribe.model.Scribe;

public class ScribeDTO {

    private static Logger logger = Logger.getLogger(ScribeDTO.class);

    public Long toolContentID;

    public String title;

    public String instructions;

    public boolean defineLater;

    public boolean contentInUse;

    public Set<ScribeSessionDTO> sessionDTOs = new TreeSet<ScribeSessionDTO>();

    public Set<ScribeHeadingDTO> headingDTOs = new TreeSet<ScribeHeadingDTO>();

    public boolean autoSelectScribe;

    public boolean showAggregatedReports;

    public ScribeDTO(Scribe scribe) {
	toolContentID = scribe.getToolContentId();
	title = scribe.getTitle();
	instructions = scribe.getInstructions();
	contentInUse = scribe.isContentInUse();
	autoSelectScribe = scribe.isAutoSelectScribe();
	showAggregatedReports = scribe.isShowAggregatedReports();

    }

    public Set<ScribeSessionDTO> getSessionDTOs() {
	return sessionDTOs;
    }

    public void setSessionDTOs(Set<ScribeSessionDTO> sessionDTOs) {
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

    public Long getToolContentID() {
	return toolContentID;
    }

    public void setToolContentID(Long toolContentID) {
	this.toolContentID = toolContentID;
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

    public boolean isAutoSelectScribe() {
	return autoSelectScribe;
    }

    public void setAutoSelectScribe(boolean autoSelectScribe) {
	this.autoSelectScribe = autoSelectScribe;
    }

    public boolean isShowAggregatedReports() {
	return showAggregatedReports;
    }

    public void setShowAggregatedReports(boolean showAggregatedReports) {
	this.showAggregatedReports = showAggregatedReports;
    }
}
