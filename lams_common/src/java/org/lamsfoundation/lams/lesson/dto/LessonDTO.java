/***************************************************************************
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
 * ***********************************************************************/

package org.lamsfoundation.lams.lesson.dto;

import java.util.Date;

import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.usermanagement.Organisation;

/**
 * <p>
 * This is a cut down version of Lesson domain object. This data transfer object is design for the data interaction
 * between UI and java. As the UI and java communication is expensive, DTO pattern works fine here to save all
 * unecessary data transfer.
 * </p>
 *
 * <p>
 * As DTO is only used for data transfer purpose, we should make it immutable to avoid any unecessary error state.
 * </p>
 *
 * @author Jacky Fang 2005-3-3
 * @version 1.1
 *
 */
public class LessonDTO {
    //---------------------------------------------------------------------
    // attributes
    //---------------------------------------------------------------------
    private Long lessonID;
    private String lessonName;
    private String lessonDescription;
    private Integer lessonStateID;
    private Integer organisationID;
    private Date createDateTime;
    private String createDateTimeString;
    private Date startDateTime;
    private Long learningDesignID;
    private Boolean displayMonitor;

    //---------------------------------------------------------------------
    // Construtors
    //---------------------------------------------------------------------
    /**
     * Full constructor
     */
    public LessonDTO(Lesson lesson) {
	this.lessonID = lesson.getLessonId();
	this.lessonName = lesson.getLessonName();
	this.lessonDescription = lesson.getLessonDescription();
	this.lessonStateID = lesson.getLessonStateId();
	LearningDesign design = lesson.getLearningDesign();
	this.learningDesignID = design != null ? design.getLearningDesignId() : null;
	Organisation org = lesson.getOrganisation();
	this.organisationID = org != null ? org.getOrganisationId() : null;
	this.createDateTime = lesson.getCreateDateTime();
	this.createDateTimeString = lesson.getCreateDateTime().toString();
	this.startDateTime = lesson.getStartDateTime();
    }

    //---------------------------------------------------------------------
    // Getters
    //---------------------------------------------------------------------
    /**
     * Returns the lesson id.
     *
     * @return Returns the lessonID.
     */
    public Long getLessonID() {
	return lessonID;
    }

    /**
     * Returns the name of the lesson..
     *
     * @return Returns the title.
     */
    public String getLessonName() {
	return lessonName;
    }

    /**
     * Returns the lesson description.
     *
     * @return Returns the description.
     */
    public String getLessonDescription() {
	return lessonDescription;
    }

    /**
     * Returns the lesson state. Plese refer to the lesson object for lesson state.
     *
     * @return Returns the lessonStateID.
     */
    public Integer getLessonStateID() {
	return lessonStateID;
    }

    public Date getCreateDateTime() {
	return createDateTime;
    }

    public Date getStartDateTime() {
	return startDateTime;
    }

    public String getCreateDateTimeString() {
	return createDateTimeString;
    }

    public Integer getOrganisationID() {
	return organisationID;
    }

    public Long getLearningDesignID() {
	return learningDesignID;
    }

    public Boolean getDisplayMonitor() {
	return displayMonitor;
    }

    public void setDisplayMonitor(Boolean displayMonitor) {
	this.displayMonitor = displayMonitor;
    }

    /**
     * Returns the String representation of lesson data transfer object.
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	StringBuffer sb = new StringBuffer(getClass().getName() + ": ");
	sb.append("lessonID='" + getLessonID() + "'; ");
	sb.append("lessonName='" + getLessonName() + "';");
	sb.append("lessonDescription='" + getLessonDescription() + "'; ");
	sb.append("lessonStateID='" + getLessonStateID() + "'; ");
	sb.append("learningDesignID='" + getLearningDesignID() + "'; ");
	sb.append("organisationID='" + getOrganisationID() + "'; ");
	sb.append("createDateTime='" + getCreateDateTime() + "'; ");
	sb.append("createDateTimeString='" + getCreateDateTimeString() + "'; ");
	sb.append("startDateTime='" + getStartDateTime() + "'; ");
	sb.append("displayMonitor='" + getDisplayMonitor() + "'; ");
	return sb.toString();
    }
}
