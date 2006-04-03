/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.lesson.dao;

import java.util.Date;
import java.util.List;

import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.usermanagement.User;

/**
 * Inteface defines Lesson DAO Methods
 * @author chris
 */
public interface ILessonDAO
{
    
    /**
     * Retrieves the Lesson
     * @param lessonId identifies the lesson to get
     * @return the lesson
     */
    public Lesson getLesson(Long lessonId);
    
    /** Get all the lessons in the database. This includes the disabled lessons. */
    public List getAllLessons();
    
    public Lesson getLessonWithJoinFetchedProgress(Long lessonId);
    /**
     * Gets all lessons that are active for a learner.
     * @param learner a User that identifies the learner.
     * @return a Set with all active lessons in it.
     */
    public List getActiveLessonsForLearner(User learner);
    
    /**
     * Saves or Updates a Lesson.
     * @param lesson the Lesson to save
     */
    public void saveLesson(Lesson lesson);
    
    /**
     * Deletes a Lesson <b>permanently</b>.
     * @param lesson the Lesson to remove.
     */
    public void deleteLesson(Lesson lesson);
    
    /**
     * Update a requested lesson.
     * @param createdLesson
     */
    public void updateLesson(Lesson lesson);
    
    /**
      * Returns the list of available Lessons created by
     * a given user. Does not return disabled lessons or preview lessons.
    * 
     * @param userID The user_id of the user
     * @return List The list of Lessons for the given user
     */
    public List getLessonsCreatedByUser(Integer userID);
    
    /**
     * Returns the all the learners that have started the requested lesson.
     * 
     * @param lessonId the id of the requested lesson.
     * @return the list of learners.
     */
    public List getActiveLearnerByLesson(final long lessonId);

    /**
     * Get all the preview lessons more with the creation date before the given date.
     * 
     * @param startDate UTC date 
     * @return the list of Lessons
     */
    public List getPreviewLessonsBeforeDate(final Date startDate);
}
