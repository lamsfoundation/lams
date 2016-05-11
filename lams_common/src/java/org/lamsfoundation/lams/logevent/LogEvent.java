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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */


package org.lamsfoundation.lams.logevent;

import java.io.Serializable;
import java.util.Date;

import org.lamsfoundation.lams.usermanagement.User;

/**
 * Base class for all activities. If you add another subclass, you must update
 * ActivityDAO.getActivityByActivityId() and add a ACTIVITY_TYPE constant.
 *
 * @hibernate.class table="lams_log_event"
 */
public class LogEvent implements Serializable {

    private static final long serialVersionUID = 5275008411348257866L;

    /***************************************************************************
     * static final variables indicating the type of events
     **************************************************************************/
    public static final int TYPE_TEACHER_LEARNING_DESIGN_CREATE = 1;
    public static final int TYPE_TEACHER_LESSON_CREATE = 2;
    public static final int TYPE_TEACHER_LESSON_START = 3;
    public static final int TYPE_TEACHER_LESSON_CHANGE_STATE = 4;
    public static final int TYPE_LEARNER_ACTIVITY_START = 5;
    public static final int TYPE_LEARNER_ACTIVITY_FINISH = 6;
    /** *************************************************************** */

    /** identifier field */
    private Long id;

    /**  */
    private Integer logEventTypeId;

    /** persistent field */
    //TODO perhaps make this field possible to be null
    private User user;

    /** Date this activity was created */
    private Date occurredDateTime;

    /**  */
    private Long learningDesignId;

    /**  */
    private Long lessonId;

    /**  */
    private Long activityId;

    /*
     * For the occurredDateTime fields, if the value is null, then it will default to the current time.
     */

    /** default constructor */
    public LogEvent() {
	occurredDateTime = new Date(); // default value is set to when the object is created
    }

    // ---------------------------------------------------------------------
    // Getters and Setters
    // ---------------------------------------------------------------------
    /**
     * @hibernate.id generator-class="native" type="java.lang.Long" column="id"
     */
    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    /**
     * @hibernate.property column="log_event_type_id" length="5"
     */
    public Integer getLogEventTypeId() {
	return logEventTypeId;
    }

    public void setLogEventTypeId(Integer logEventTypeId) {
	this.logEventTypeId = logEventTypeId;
    }

    /**
     * @hibernate.many-to-one not-null="true"
     * @hibernate.column name="user_id"
     */
    public User getUser() {
	return this.user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    /**
     * @hibernate.property column="occurred_date_time" length="19" not-null="true"
     */
    public Date getOccurredDateTime() {
	return occurredDateTime;
    }

    /* If occurredDateTime is null, then it will default to the current date/time */
    public void setOccurredDateTime(Date occurredDateTime) {
	this.occurredDateTime = occurredDateTime != null ? occurredDateTime : new Date();
    }

    /**
     * @hibernate.property column="learning_design_id"
     */
    public Long getLearningDesignId() {
	return learningDesignId;
    }

    public void setLearningDesignId(Long learningDesignId) {
	this.learningDesignId = learningDesignId;
    }

    /**
     * @hibernate.property column="lesson_id"
     */
    public Long getLessonId() {
	return lessonId;
    }

    public void setLessonId(Long lessonId) {
	this.lessonId = lessonId;
    }

    /**
     * @hibernate.property column="activity_id"
     */
    public Long getActivityId() {
	return activityId;
    }

    public void setActivityId(Long activityId) {
	this.activityId = activityId;
    }
}
