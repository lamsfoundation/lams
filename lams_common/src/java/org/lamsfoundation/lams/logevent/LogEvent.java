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
 *
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
    public static final int TYPE_LEARNER_LESSON_COMPLETE = 7;
    public static final int TYPE_LEARNER_LESSON_MARK_SUBMIT = 8;
    public static final int TYPE_ACTIVITY_EDIT = 9; // Audit Service
    public static final int TYPE_FORCE_COMPLETE = 10; // Audit Service
    public static final int TYPE_USER_ORG_ADMIN = 11;
    public static final int TYPE_LOGIN_AS  = 12;
    public static final int TYPE_PASSWORD_CHANGE  = 13;
    public static final int TYPE_ROLE_FAILURE = 14;
    public static final int TYPE_ACCOUNT_LOCKED = 15;
    public static final int TYPE_NOTIFICATION  = 16;
    public static final int TYPE_MARK_UPDATED = 17;  // Audit Service
    public static final int TYPE_MARK_RELEASED = 18; 
    public static final int TYPE_LEARNER_CONTENT_UPDATED = 19; // Audit Service
    public static final int TYPE_LEARNER_CONTENT_SHOW_HIDE = 20; // Audit Service
    public static final int TYPE_UNKNOWN = 21; // catch all for conversion
    public static final int TYPE_LIVE_EDIT = 22; // Start or end Live Edit of a lesson

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
    private User targetUser;

    /**  */
    private Long lessonId;

    /**  */
    private Long activityId;
    
    private String description;

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
     *
     */
    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    /**
     *
     */
    public Integer getLogEventTypeId() {
	return logEventTypeId;
    }

    public void setLogEventTypeId(Integer logEventTypeId) {
	this.logEventTypeId = logEventTypeId;
    }

    /**
     *
     *
     */
    public User getUser() {
	return this.user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    /**
     *
     */
    public Date getOccurredDateTime() {
	return occurredDateTime;
    }

    /* If occurredDateTime is null, then it will default to the current date/time */
    public void setOccurredDateTime(Date occurredDateTime) {
	this.occurredDateTime = occurredDateTime != null ? occurredDateTime : new Date();
    }

    /**
     *
     */
    public User getTargetUser() {
	return targetUser;
    }

    public void setTargetUser(User targetUser) {
	this.targetUser = targetUser;
    }

    /**
     *
     */
    public Long getLessonId() {
	return lessonId;
    }

    public void setLessonId(Long lessonId) {
	this.lessonId = lessonId;
    }

    /**
     *
     */
    public Long getActivityId() {
	return activityId;
    }

    public void setActivityId(Long activityId) {
	this.activityId = activityId;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }
}
