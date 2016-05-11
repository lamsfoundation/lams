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


package org.lamsfoundation.lams.gradebook;

import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.usermanagement.User;

/**
 * @author lfoxton
 *
 *         This class maps to one learner's entire mark for a lesson
 *
 * @hibernate.class table="lams_gradebook_user_lesson"
 */
public class GradebookUserLesson {
    private long uid;
    private Lesson lesson;
    private User learner;
    private Double mark;
    private String feedback;

    public GradebookUserLesson() {
    }

    public GradebookUserLesson(Lesson lesson, User learner) {
	this.lesson = lesson;
	this.learner = learner;
    }

    /**
     * @hibernate.id column="uid" generator-class="native" type="java.lang.Long"
     */
    public long getUid() {
	return uid;
    }

    public void setUid(long uid) {
	this.uid = uid;
    }

    /**
     * @hibernate.many-to-one not-null="true"
     * @hibernate.column name="lesson_id"
     */
    public Lesson getLesson() {
	return lesson;
    }

    public void setLesson(Lesson lesson) {
	this.lesson = lesson;
    }

    /**
     * @hibernate.many-to-one not-null="true"
     * @hibernate.column name="user_id"
     */
    public User getLearner() {
	return learner;
    }

    public void setLearner(User learner) {
	this.learner = learner;
    }

    /**
     * @hibernate.property column="mark"
     */
    public Double getMark() {
	return mark;
    }

    public void setMark(Double mark) {
	this.mark = mark;
    }

    /**
     * @hibernate.property column="feedback" length="65535"
     */
    public String getFeedback() {
	return feedback;
    }

    public void setFeedback(String feedback) {
	this.feedback = feedback;
    }
}
