/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */
package org.lamsfoundation.lams.gradebook.dto;

import java.util.ArrayList;
import java.util.Date;

import org.lamsfoundation.lams.gradebook.util.GBGridView;

public class GBActivityGridRowDTO extends GradebookGridRowDTO {

    public static final String VIEW_USER = "userView";
    public static final String VIEW_ACTIVITY = "activityView";
    
    String competences;
    //String toolString;

    // Properties for user view
    String status;
    String output;
    String activityUrl;
    //double timeTaken;
    String feedback;
    Date startDate;

    // Properties for activity view
    String monitorUrl;
    Long groupId;

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public GBActivityGridRowDTO() {
    }

    @Override
    public ArrayList<String> toStringArray(GBGridView view) {
	ArrayList<String> ret = new ArrayList<String>();
	ret.add(id.toString());

	if (view == GBGridView.MON_USER) {
	    
	    if (activityUrl != null && activityUrl.length() != 0) {
		ret.add("<a href='javascript:launchPopup(\"" + activityUrl + "\",\"" + rowName + "\",796,570)'>"
			+ rowName + "</a>");
	    } else {
		ret.add(rowName);
	    }
	    ret.add(status);
	    ret.add((output != null) ? output.toString() : CELL_EMPTY);
	    ret.add(competences);
	    ret.add((timeTaken != null) ? convertTimeToString(timeTaken) : CELL_EMPTY);
	    ret.add(feedback);
	    ret.add((mark != null) ? mark.toString() : CELL_EMPTY);

	} else if (view == GBGridView.MON_ACTIVITY) {
	    
	    ret.add(groupId != null ? groupId.toString() : "");
	    if (monitorUrl != null && monitorUrl.length() != 0) {
		ret.add("<a href='javascript:launchPopup(\"" + monitorUrl + "\",\"" + rowName + "\",796,570)'>"
			+ rowName + "</a>");
	    } else {
		ret.add(rowName);
	    }
	    
	    ret.add((averageTimeTaken != null) ? convertTimeToString(averageTimeTaken) : CELL_EMPTY);
	    ret.add(competences);
	    ret.add((averageMark != null) ? averageMark.toString() : CELL_EMPTY);
	} else if (view == GBGridView.LRN_ACTIVITY) {
	    ret.add(rowName);
	    ret.add(status);
	    ret.add(feedback);
	    ret.add((averageTimeTaken != null) ? convertTimeToString(averageTimeTaken) : CELL_EMPTY);
	    ret.add((timeTaken != null) ? convertTimeToString(timeTaken) : CELL_EMPTY);
	    ret.add((averageMark != null) ? averageMark.toString() : CELL_EMPTY); 
	    ret.add((mark != null) ? mark.toString() : CELL_EMPTY); 
	}
	
	return ret;
    }

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
    }

    public String getOutput() {
	return output;
    }

    public void setOutput(String output) {
	this.output = output;
    }

    public String getCompetences() {
	return competences;
    }

    public void setCompetences(String competences) {
	this.competences = competences;
    }

    public String getActivityUrl() {
	return activityUrl;
    }

    public void setActivityUrl(String activityUrl) {
	this.activityUrl = activityUrl;
    }

    public String getFeedback() {
	return feedback;
    }

    public void setFeedback(String feedback) {
	this.feedback = feedback;
    }

    public String getMonitorUrl() {
	return monitorUrl;
    }

    public void setMonitorUrl(String monitorUrl) {
	this.monitorUrl = monitorUrl;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    } 
}
