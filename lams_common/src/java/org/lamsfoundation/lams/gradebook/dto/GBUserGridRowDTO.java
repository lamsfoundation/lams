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

public class GBUserGridRowDTO extends GradeBookGridRowDTO {
    
    public static final String VIEW_USER = "userView";
    public static final String VIEW_ACTIVITY = "activityView";
    public static final String VIEW_COURSE_MONITOR = "courseMonitorView";
    
    String login;
    String firstName;
    String lastName;
    String status;
    String feedback;

    // For user view, this represents total lesson mark in the grid
    // For activity view, this represents an activity mark
    Double mark;

    // For activity view
    String output;
    String activityUrl;

    public GBUserGridRowDTO() {
    }

    @Override
    public ArrayList<String> toStringArray(String view) {
	ArrayList<String> ret = new ArrayList<String>();

	String fullName = lastName + " " + firstName;
	ret.add(login);

	if (view.equals(VIEW_USER)) {
	    ret.add(fullName);
	    ret.add(status);
	    ret.add(feedback);
	} else if (view.equals(VIEW_ACTIVITY)) {
	    if (activityUrl != null && activityUrl.length() != 0) {
		ret.add("<a href='javascript:launchPopup(\"" + activityUrl + "\",\"" + fullName + "\",796,570)'>" + fullName
			+ "</a>");
	    } else {
		ret.add(fullName);
	    }

	    ret.add(status);
	    ret.add(output);
	    ret.add(feedback);
	} else if (view.equals(VIEW_COURSE_MONITOR)){
	    ret.add(fullName);
	    ret.add(status);
	    ret.add(feedback);
	    
	}

	if (mark != null) {
	    ret.add(mark.toString());
	} else {
	    ret.add("-");
	}

	return ret;
    }

    @Override
    public String getRowId() {
	return login;
    }

    public Double getMark() {
	return mark;
    }

    public void setMark(Double mark) {
	this.mark = mark;
    }

    public String getLogin() {
	return login;
    }

    public void setLogin(String login) {
	this.login = login;
    }

    public String getFirstName() {
	return firstName;
    }

    public void setFirstName(String firstName) {
	this.firstName = firstName;
    }

    public String getLastName() {
	return lastName;
    }

    public void setLastName(String lastName) {
	this.lastName = lastName;
    }

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
    }

    public String getFeedback() {
	return feedback;
    }

    public void setFeedback(String feedback) {
	this.feedback = feedback;
    }

    public String getOutput() {
	return output;
    }

    public void setOutput(String output) {
	this.output = output;
    }

    public String getActivityUrl() {
	return activityUrl;
    }

    public void setActivityUrl(String activityUrl) {
	this.activityUrl = activityUrl;
    }
}
