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
/* $Id$ */

package org.lamsfoundation.lams.lesson.service;

import java.util.Collection;
import java.util.List;

import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.dto.LessonDTO;
import org.lamsfoundation.lams.lesson.dto.LessonDetailsDTO;
import org.lamsfoundation.lams.usermanagement.User;

/**
 * Access the general lesson details.
 * 
 * A lesson has three different "lists" of learners.
 * <OL>
 * <LI>The learners who are in the learner group attached to the lesson. This is fixed 
 * when the lesson is started and is a list of all the learners who could ever participate in
 * to the lesson. This is available via lesson.getAllLearners()
 * <LI>The learners who have started the lesson. They may or may not be logged in currently,
 * or if they are logged in they may or may not be doing this lesson. This is available
 * via getActiveLessonLearners().
 * <LI>The learners who are currently logged in and doing this lesson. This is available
 * via getLoggedInLessonLearners(). Note - learners in this list may actually have left but
 * we don't know unless they use the actual logout/exit buttons.
 *
 */
public interface ILessonService {

	/**
	 *  Cache the user in the list of "currently logged in users"
	 */
	public abstract void cacheLessonUser(Lesson lesson, User learner);

	/**
	 *  Remove the user from the list of "currently logged in users"
	 */
	public abstract void removeLessonUserFromCache(Lesson lesson, User learner);

	/** Get all the learners who are currently using the lesson */
	public abstract Collection getLoggedInLessonLearners(Long lessonId);

	/** Get all the learners who have started the lesson. They may not be currently online.*/
	public abstract List getActiveLessonLearners(Long lessonId);

	/** 
	 * Get the count of all the learners who have started the lesson. They may not be currently online.
	 */
	public Integer getCountActiveLessonLearners(Long lessonId);
    
	/** Get the lesson details for the LAMS client. Suitable for the monitoring client.
	 * Contains a count of the total number of learners in the lesson and the number of active learners.
	 * This is a pretty intensive call as it counts all the learners in the 
	 * lessons' learner group, and determines the number of active learners.
	 * @param lessonId
	 * @return lesson details
	 */
	public abstract LessonDetailsDTO getLessonDetails(Long lessonId);

	/** Get the lesson details for the LAMS client. Suitable for the learner client.
	 * Contains a reduced number of fields compared to getLessonDetails.
	 * @param lessonId
	 * @return lesson details
	 */
	public abstract LessonDTO getLessonData(Long lessonId);

}