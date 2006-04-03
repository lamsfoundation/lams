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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.lesson.dao;

import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.usermanagement.User;

/**
 * Inteface defines Lesson DAO Methods
 * @author chris
 */
public interface ILearnerProgressDAO
{
    
    /**
     * Retrieves the Lesson
     * @param lessonId identifies the lesson to get
     * @return the lesson
     */
    public LearnerProgress getLearnerProgress(Long learnerProgressId);
    
    /**
     * Retrieves the learner progress object for user in a lesson.
     * @param user the user who owns the learner progress data.
     * @return the user's progress data
     */
    public LearnerProgress getLearnerProgressByLearner(final User learner,
                                                      final Lesson lesson);
        
    /**
     * Saves or Updates learner progress data.
     * @param learnerProgress holds the learne progress data
     */
    public void saveLearnerProgress(LearnerProgress learnerProgress);
    
    /**
     * Deletes a LearnerProgress data <b>permanently</b>.
     * @param learnerProgress 
     */
    public void deleteLearnerProgress(LearnerProgress learnerProgress);
    
    /**
     * Update learner progress data.
     * @param learnerProgress
     */
    public void updateLearnerProgress(LearnerProgress learnerProgress);
}
