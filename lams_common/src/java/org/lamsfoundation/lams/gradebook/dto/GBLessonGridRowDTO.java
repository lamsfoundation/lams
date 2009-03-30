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

public class GBLessonGridRowDTO extends GradeBookGridRowDTO {

    public static final String VIEW_MONITOR = "monitorView";
    public static final String VIEW_LEARNER = "learnerView";

    Long lessonId;
    String lessonName;
    String lessonDescription;
    Double mark;
    String subGroup;
    
    // Only for monitor view
    String gradeBookMonitorURL;
    
    // Only for learner view
    String gradeBookLearnerURL;

    
    public GBLessonGridRowDTO() {
    }

    @Override
    public String getRowId() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public ArrayList<String> toStringArray(String view) {
	ArrayList<String> ret = new ArrayList<String>();
	
	ret.add(lessonId.toString());
	
	if (view.equals(VIEW_MONITOR)) {
	    if (gradeBookMonitorURL != null && gradeBookMonitorURL.length() != 0) {
		ret.add("<a href='javascript:launchPopup(\"" + gradeBookMonitorURL + "\",\"" + lessonName + "\")'>" + lessonName
			+ "</a>");
	    } else {
		ret.add(lessonName);
	    }
	    ret.add(subGroup);
	    ret.add(lessonDescription);
	    if (mark != null) {
		ret.add(mark.toString());
	    } else {
		ret.add("-");
	    }
	} else if (view.equals(VIEW_LEARNER)) {
	    if (gradeBookLearnerURL != null && gradeBookLearnerURL.length() != 0) {
		ret.add("<a href='javascript:launchPopup(\"" + gradeBookLearnerURL + "\",\"" + lessonName + "\")'>" + lessonName
			+ "</a>");
	    } else {
		ret.add(lessonName);
	    }
	    ret.add(lessonDescription);
	    if (mark != null) {
		ret.add(mark.toString());
	    } else {
		ret.add("-");
	    }
	}
	return ret;
    }

    public Long getLessonId() {
	return lessonId;
    }

    public void setLessonId(Long lessonId) {
	this.lessonId = lessonId;
    }

    public String getLessonName() {
	return lessonName;
    }

    public void setLessonName(String lessonName) {
	this.lessonName = lessonName;
    }

    public String getLessonDescription() {
	return lessonDescription;
    }

    public void setLessonDescription(String lessonDescription) {
	this.lessonDescription = lessonDescription;
    }

    public Double getMark() {
        return mark;
    }

    public void setMark(Double mark) {
        this.mark = mark;
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
}
