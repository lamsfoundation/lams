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

import org.apache.commons.lang.StringEscapeUtils;

import org.lamsfoundation.lams.gradebook.util.GBGridView;
import org.lamsfoundation.lams.gradebook.util.GradebookUtil;
import org.lamsfoundation.lams.usermanagement.User;

public class GBUserGridRowDTO extends GradebookGridRowDTO {
    
    // For activity view
    private Date startDate;
    private String output;
    private String activityUrl;
    
    // For excel export
    private String firstName;
    private String lastName;
    private String currentActivity;

    public GBUserGridRowDTO() {
    }
    
    public GBUserGridRowDTO(User user) {
	this.id = user.getUserId().toString();
	this.rowName = StringEscapeUtils.escapeHtml(user.getLastName() + " " + user.getFirstName());
	this.firstName = user.getFirstName();
	this.lastName = user.getLastName();
    }

    @Override
    public ArrayList<String> toStringArray(GBGridView view) {
	ArrayList<String> ret = new ArrayList<String>();

	ret.add(id.toString());
	
	if (view == GBGridView.MON_USER) {
	    
	    ret.add(rowName);
	    ret.add(status);
	    ret.add((timeTaken != null) ? convertTimeToString(timeTaken) : CELL_EMPTY);
	    ret.add(feedback);
	    ret.add((mark != null) ? GradebookUtil.niceFormatting(mark) : CELL_EMPTY);
	    	    
	} else if (view == GBGridView.MON_ACTIVITY){
	    
	    ret.add(marksAvailable != null ? marksAvailable.toString() : "");
	    if (activityUrl != null && activityUrl.length() != 0) {
		ret.add("<a href='javascript:launchPopup(\"" + activityUrl + "\",\"" + rowName + "\",796,570)'>" + rowName
			+ "</a>");
	    } else {
		ret.add(rowName);
	    }
	    ret.add(status);
	    ret.add(startDate != null ? convertDateToString(startDate, DEFAULT_DATE_FORMAT) : CELL_EMPTY);
	    ret.add((timeTaken != null) ? convertTimeToString(timeTaken) : CELL_EMPTY);
	    ret.add((output != null) ? output.toString() : CELL_EMPTY);
	    ret.add(feedback);
	    ret.add((mark != null) ? GradebookUtil.niceFormatting(mark) : CELL_EMPTY);
	    
	} else if (view == GBGridView.MON_COURSE){
	    ret.add(rowName);
	    ret.add(status);
	    ret.add((timeTaken != null) ? convertTimeToString(timeTaken) : CELL_EMPTY);
	    ret.add(feedback);
	    ret.add((mark != null) ? GradebookUtil.niceFormatting(mark) : CELL_EMPTY);
	}

	return ret;
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
    
    public String getCurrentActivity() {
	return currentActivity;
    }
    
    public void setCurrentActivity(String currentActivity) {
	this.currentActivity = currentActivity;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startTime) {
        this.startDate = startTime;
    }

}
