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


package org.lamsfoundation.lams.gradebook.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;
import org.lamsfoundation.lams.gradebook.util.GBGridView;
import org.lamsfoundation.lams.gradebook.util.GradebookUtil;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.CompetenceMapping;
import org.lamsfoundation.lams.learningdesign.ToolActivity;

public class GBActivityGridRowDTO extends GradebookGridRowDTO {

    public static final String VIEW_USER = "userView";
    public static final String VIEW_ACTIVITY = "activityView";

    private String competences;

    // Properties for user view
    private String activityUrl;
    private Date startDate;

    // Properties for activity view
    private String monitorUrl;
    private Long groupId;

    public Long getGroupId() {
	return groupId;
    }

    public void setGroupId(Long groupId) {
	this.groupId = groupId;
    }

    public GBActivityGridRowDTO(Activity activity, String groupName, Long groupId) {

	if (groupName != null && groupId != null) {
	    // Need to make the id unique, so appending the group id for this row
	    this.id = activity.getActivityId().toString() + "_" + groupId.toString();

	    this.groupId = groupId;
	    // If grouped acitivty, append group name
	    this.rowName = StringEscapeUtils.escapeHtml(activity.getTitle()) + " (" + groupName + ")";
	} else {
	    this.id = activity.getActivityId().toString();

	    this.rowName = StringEscapeUtils.escapeHtml(activity.getTitle());
	}

	String competenceMappingsStr = "";
	if ( activity.isToolActivity() ) {
	    ToolActivity toolActivity = (ToolActivity) activity;
        	//Constructs the competences for this activity.
        	Set<CompetenceMapping> competenceMappings = toolActivity.getCompetenceMappings();
        	
        	if (competenceMappings != null) {
        	    for (CompetenceMapping mapping : competenceMappings) {
        		competenceMappingsStr += mapping.getCompetence().getTitle() + ", ";
        	    }
        
        	    // trim the last comma off
        	    if (competenceMappingsStr.length() > 0) {
        		competenceMappingsStr = competenceMappingsStr.substring(0, competenceMappingsStr.lastIndexOf(","));
        	    }
        	}
        
	} 
	this.competences = competenceMappingsStr;

    }

    @Override
    public ArrayList<String> toStringArray(GBGridView view) {
	ArrayList<String> ret = new ArrayList<String>();
	ret.add(id.toString());

	if (view == GBGridView.MON_USER) {
	    ret.add(marksAvailable != null ? marksAvailable.toString() : "");

	    if (activityUrl != null && activityUrl.length() != 0) {
		ret.add("<a href='javascript:launchPopup(\"" + activityUrl + "\",\"" + rowName + "\",796,570)'>"
			+ rowName + "</a>");
	    } else {
		ret.add(rowName);
	    }
	    ret.add(status);
	    ret.add(timeTaken != null ? convertTimeToString(timeTaken) : CELL_EMPTY);
	    ret.add(feedback);
	    ret.add(mark != null ? GradebookUtil.niceFormatting(mark) : CELL_EMPTY);

	} else if (view == GBGridView.MON_ACTIVITY) {

	    ret.add(groupId != null ? groupId.toString() : "");

	    if (monitorUrl != null && monitorUrl.length() != 0) {
		ret.add("<a href='javascript:launchPopup(\"" + monitorUrl + "\",\"" + rowName + "\",796,570)'>"
			+ rowName + "</a>");
	    } else {
		ret.add(rowName);
	    }

	    ret.add((medianTimeTaken != null) ? convertTimeToString(medianTimeTaken) : CELL_EMPTY);
	    ret.add(competences);
	    ret.add(averageMark != null ? GradebookUtil.niceFormatting(averageMark) : CELL_EMPTY);

	} else if (view == GBGridView.LRN_ACTIVITY) {
	    ret.add(rowName);
	    ret.add(status);
	    ret.add(feedback);
	    ret.add((medianTimeTaken != null) ? convertTimeToString(medianTimeTaken) : CELL_EMPTY);
	    ret.add((timeTaken != null) ? convertTimeToString(timeTaken) : CELL_EMPTY);
	    ret.add(averageMark != null ? GradebookUtil.niceFormatting(averageMark) : CELL_EMPTY);
	    ret.add(mark != null ? GradebookUtil.niceFormatting(mark) : CELL_EMPTY);

	}

	return ret;
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
