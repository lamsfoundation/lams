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

public class GBActivityGridRowDTO extends GradeBookGridRowDTO {

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

    // Properties for activity view
    String monitorUrl;

    public GBActivityGridRowDTO() {
    }

    @Override
    public ArrayList<String> toStringArray(String view) {
	ArrayList<String> ret = new ArrayList<String>();
	ret.add(id.toString());

	if (view.equals(VIEW_USER)) {
	    if (activityUrl != null && activityUrl.length() != 0) {
		ret.add("<a href='javascript:launchPopup(\"" + activityUrl + "\",\"" + rowName + "\",796,570)'>"
			+ rowName + "</a>");
	    } else {
		ret.add(rowName);
	    }
	    ret.add(status);
	    ret.add(output);
	    ret.add(competences);
	    
	    if (timeTaken != null) {
		ret.add(convertTimeToString(timeTaken));
	    } else {
		ret.add("-");
	    }
	    
	    ret.add(feedback);
	    
	    if (mark != null) {
		ret.add(mark.toString());
	    } else {
		ret.add("-");
	    }

	} else if (view.equals(VIEW_ACTIVITY)) {
	    if (monitorUrl != null && monitorUrl.length() != 0) {
		ret.add("<a href='javascript:launchPopup(\"" + monitorUrl + "\",\"" + rowName + "\",796,570)'>"
			+ rowName + "</a>");
	    } else {
		ret.add(rowName);
	    }

	    ret.add(competences);

	    if (mark != null) {
		ret.add(mark.toString());
	    } else {
		ret.add("-");
	    }
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
}
