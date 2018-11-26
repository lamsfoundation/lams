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

package org.lamsfoundation.lams.rating.model;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.lesson.Lesson;

@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("4")
public class LessonRatingCriteria extends RatingCriteria implements Cloneable, Serializable {

    /** Holds value of property lessonId. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @Override
    public Object clone() {
	LessonRatingCriteria newCriteria = (LessonRatingCriteria) super.clone();
	newCriteria.setLesson(null);
	return newCriteria;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("ratingCriteriaId", getRatingCriteriaId()).toString();
    }

    /**
     * @see org.lamsfoundation.lams.util.Nullable#isNull()
     */
    @Override
    public boolean isNull() {
	return false;
    }

    /**
     * Getter for property lesson.
     *
     * @return Value of property lesson.
     */
    public Lesson getLesson() {
	return this.lesson;
    }

    /**
     * Setter for property lesson.
     *
     * @param lessonId
     *            New value of property lesson.
     */
    public void setLesson(Lesson lesson) {
	this.lesson = lesson;
    }
}
