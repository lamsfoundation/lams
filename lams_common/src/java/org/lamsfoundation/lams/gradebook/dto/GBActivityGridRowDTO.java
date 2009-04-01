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

    long activityId;
    String activityTitle;
    String competences;
    //String toolString;

    // Properties for user view
    String status;
    String output;
    String activityUrl;
    //double timeTaken;
    Double mark;
    String feedback;
    String timeTaken = "-";

    // Properties for activity view
    Double average;
    String monitorUrl;

    public GBActivityGridRowDTO() {
    }

    @Override
    public ArrayList<String> toStringArray(String view) {
	ArrayList<String> ret = new ArrayList<String>();
	ret.add("" + activityId);

	if (view.equals(VIEW_USER)) {
	    if (activityUrl != null && activityUrl.length() != 0) {
		ret.add("<a href='javascript:launchPopup(\"" + activityUrl + "\",\"" + activityTitle + "\",796,570)'>"
			+ activityTitle + "</a>");
	    } else {
		ret.add(activityTitle);
	    }
	    ret.add(status);
	    ret.add(output);
	    ret.add(competences);
	    
	    ret.add(timeTaken);
	    
	    ret.add(feedback);
	    
	    if (mark != null) {
		ret.add(mark.toString());
	    } else {
		ret.add("-");
	    }

	} else if (view.equals(VIEW_ACTIVITY)) {
	    if (monitorUrl != null && monitorUrl.length() != 0) {
		ret.add("<a href='javascript:launchPopup(\"" + monitorUrl + "\",\"" + activityTitle + "\",796,570)'>"
			+ activityTitle + "</a>");
	    } else {
		ret.add(activityTitle);
	    }

	    ret.add(competences);

	    if (average != null) {
		ret.add(average.toString());
	    } else {
		ret.add("-");
	    }
	}

	return ret;
    }

    @Override
    public String getRowId() {
	return "" + activityId;
    }

    public int getId() {
	return new Long(activityId).intValue();
    }

    public long getActivityId() {
	return activityId;
    }

    public void setActivityId(long activityId) {
	this.activityId = activityId;
    }

    public String getActivityTitle() {
	return activityTitle;
    }

    public void setActivityTitle(String activityTitle) {
	this.activityTitle = activityTitle;
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

    public Double getMark() {
	return mark;
    }

    public void setMark(Double mark) {
	this.mark = mark;
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

    public Double getAverage() {
	return average;
    }

    public void setAverage(Double average) {
	this.average = average;
    }

    public String getMonitorUrl() {
	return monitorUrl;
    }

    public void setMonitorUrl(String monitorUrl) {
	this.monitorUrl = monitorUrl;
    }



    public void setTimeTakenInMillis(long timeTakenInMillis) {
	if (timeTakenInMillis > 1000) {
	    long totalTimeInSeconds = timeTakenInMillis / 1000;

	    long seconds = (totalTimeInSeconds >= 60 ? totalTimeInSeconds % 60 : totalTimeInSeconds); 
	    long minutes = (totalTimeInSeconds = (totalTimeInSeconds / 60)) >= 60 ? totalTimeInSeconds % 60 : totalTimeInSeconds; 
	    long hours = (totalTimeInSeconds = (totalTimeInSeconds / 60)) >= 24 ? totalTimeInSeconds % 24 : totalTimeInSeconds; 
	    long days = (totalTimeInSeconds = (totalTimeInSeconds / 24)); 

	    StringBuilder sb = new StringBuilder();
	    
	    if (days != 0 ) { sb.append("" + days + "d, "); }
	    if (hours != 0 ) { sb.append("" + hours + "h, "); }
	    if (minutes != 0 ) { sb.append("" + minutes + "m, "); }
	    if (seconds != 0 ) { sb.append("" + seconds + "s"); }
	    
	    this.timeTaken = sb.toString();
	}
    }

    public String getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(String timeTaken) {
        this.timeTaken = timeTaken;
    }
    
    
}
