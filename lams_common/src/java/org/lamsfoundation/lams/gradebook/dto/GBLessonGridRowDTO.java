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

import org.lamsfoundation.lams.gradebook.util.GBGridView;

public class GBLessonGridRowDTO extends GradeBookGridRowDTO {

    public static final String VIEW_MONITOR = "monitorView";
    public static final String VIEW_LEARNER = "learnerView";

    String lessonDescription;
    String subGroup;
    String startDate;
    
    // Only for monitor view
    String gradeBookMonitorURL;
    
    // Only for learner view
    String gradeBookLearnerURL;
    String finishDate;
    String status;
    
    public GBLessonGridRowDTO() {
    }

    @Override
    public ArrayList<String> toStringArray(GBGridView view) {
	ArrayList<String> ret = new ArrayList<String>();
	
	ret.add(id.toString());
	
	if (view == GBGridView.MON_COURSE) {
	    if (gradeBookMonitorURL != null && gradeBookMonitorURL.length() != 0) {
		ret.add("<a href='javascript:launchPopup(\"" + gradeBookMonitorURL + "\",\"" + rowName + "\",1220,600)'>" + rowName
			+ "</a>");
	    } else {
		ret.add(rowName);
	    }
	    ret.add(subGroup);
	    ret.add(lessonDescription);
	    ret.add(startDate);
	    ret.add((averageTimeTaken != null) ? convertTimeToString(averageTimeTaken) : CELL_EMPTY);
	    ret.add((averageMark != null) ? averageMark.toString() : CELL_EMPTY);
	    
	} else if (view == GBGridView.LRN_COURSE) {
	    if (gradeBookLearnerURL != null && gradeBookLearnerURL.length() != 0) {
		ret.add("<a href='javascript:launchPopup(\"" + gradeBookLearnerURL + "\",\"" + rowName + "\",796,570)'>" + rowName
			+ "</a>");
	    } else {
		ret.add(rowName);
	    }
	    ret.add(subGroup);
	    ret.add(lessonDescription);
	    ret.add(status);
	    ret.add(startDate);
	    ret.add((finishDate != null) ? finishDate : CELL_EMPTY);
	    ret.add((mark != null) ? mark.toString() : CELL_EMPTY);
	}
	return ret;
    }

    public String getLessonName() {
	return rowName;
    }

    public void setLessonName(String rowName) {
	this.rowName = rowName;
    }

    public String getLessonDescription() {
	return lessonDescription;
    }

    public void setLessonDescription(String lessonDescription) {
	this.lessonDescription = lessonDescription;
    }

    public String getGradeBookMonitorURL() {
        return gradeBookMonitorURL;
    }

    public void setGradeBookMonitorURL(String gradeBookMonitorURL) {
        this.gradeBookMonitorURL = gradeBookMonitorURL;
    }

    public String getGradeBookLearnerURL() {
        return gradeBookLearnerURL;
    }

    public void setGradeBookLearnerURL(String gradeBookLearnerURL) {
        this.gradeBookLearnerURL = gradeBookLearnerURL;
    }

    public String getSubGroup() {
        return subGroup;
    }

    public void setSubGroup(String subGroup) {
        this.subGroup = subGroup;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
