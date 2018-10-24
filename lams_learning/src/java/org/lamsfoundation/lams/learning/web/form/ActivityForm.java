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

package org.lamsfoundation.lams.learning.web.form;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.lamsfoundation.lams.learningdesign.dto.ActivityURL;

/**
 * @author daveg
 *
 *         XDoclet definition:
 *
 */
public class ActivityForm {

    /**
     * Unique identifier specifying the session for this activity, maps back to LearnerProgress (or Learner) and
     * Activity. Note that the activity may already be complete.
     */
    private Long activityID;

    /** List of ActivityURL, will only contain one if a simple activity */
    private List activityURLs;

    /** Lesson to which this activity belongs. Id comes from the learner progress */
    private Long lessonID;

    /** Version no for the design. Increments after every major modification */
    private Integer version;

    /**
     * Method reset
     *
     * @param mapping
     * @param request
     */

    public void reset(HttpServletRequest request) {
	activityURLs = null;
    }

    public List getActivityURLs() {
	return activityURLs;
    }

    public void setActivityURLs(List activityURLs) {
	this.activityURLs = activityURLs;
    }

    public Long getActivityID() {
	return activityID;
    }

    public void setActivityID(Long activityID) {
	this.activityID = activityID;
    }

    public void addActivityURL(ActivityURL activityUrl) {
	if (this.activityURLs == null) {
	    this.activityURLs = new ArrayList();
	}
	this.activityURLs.add(activityUrl);
    }

    public Long getLessonID() {
	return lessonID;
    }

    public void setLessonID(Long lessonID) {
	this.lessonID = lessonID;
    }

    public Integer getVersion() {
	return version;
    }

    public void setVersion(Integer version) {
	this.version = version;
    }
}