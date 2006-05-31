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
package org.lamsfoundation.lams.lesson.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.dao.ILessonDAO;
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
/* TODO improve caching of current learners.
 * The caching is based on the LearnerDataManager cache written by Jacky Fang
 * but it has been moved out of the servlet context to this singleton. 
 */
public class LessonService implements ILessonService
{
    private ILessonDAO lessonDAO;
    private HashMap<Long,HashMap> lessonMaps = new HashMap<Long,HashMap>(); // contains maps of lesson users

    /* (non-Javadoc)
	 * @see org.lamsfoundation.lams.lesson.service.ILessonService#cacheLessonUser(org.lamsfoundation.lams.lesson.Lesson, org.lamsfoundation.lams.usermanagement.User)
	 */
    public void cacheLessonUser(Lesson lesson, User learner)
    {
		synchronized (lesson)
		{
			//retrieve the map for this lesson
			HashMap<Integer,User> lessonUsersMap = lessonMaps.get(lesson.getLessonId());
			//create new if never created before
			if (lessonUsersMap == null) {
			    lessonUsersMap = new HashMap<Integer,User>();
			    lessonMaps.put(lesson.getLessonId(),lessonUsersMap);
			}
			if (!lessonUsersMap.containsKey(learner.getUserId())) {
				    lessonUsersMap.put(learner.getUserId(), learner);
			}
    	}
    }
    
    /* (non-Javadoc)
	 * @see org.lamsfoundation.lams.lesson.service.ILessonService#removeLessonUserFromCache(org.lamsfoundation.lams.lesson.Lesson, org.lamsfoundation.lams.usermanagement.User)
	 */
    public void removeLessonUserFromCache(Lesson lesson, User learner)
    {
		synchronized (lesson)
		{
			HashMap<Integer,User> lessonUsersMap = lessonMaps.get(lesson.getLessonId());
			if (lessonUsersMap != null) {
				lessonUsersMap.remove(learner.getUserId());
			}
			if (lessonUsersMap.size()==0) {
				// no one active in lesson? get rid of lesson from cache 
				lessonMaps.remove(lesson.getLessonId());
			}
		}
    }

    /* (non-Javadoc)
	 * @see org.lamsfoundation.lams.lesson.service.ILessonService#getLoggedInLessonLearners(java.lang.Long)
	 */
    public Collection getLoggedInLessonLearners(Long lessonId)
    {
		HashMap<Integer,User> lessonUsersMap = lessonMaps.get(lessonId);
		if ( lessonUsersMap != null )
			return lessonUsersMap.values();
		return new ArrayList();
    }

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.lesson.service.ILessonService#getActiveLessonLearners(java.lang.Long)
	 */
	public List getActiveLessonLearners(Long lessonId)
    {
        return lessonDAO.getActiveLearnerByLesson(lessonId);
    }

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.lesson.service.ILessonService#getCountActiveLessonLearners(java.lang.Long)
	 */
	public Integer getCountActiveLessonLearners(Long lessonId)
    {
        return lessonDAO.getCountActiveLearnerByLesson(lessonId);
    }

	
	public void setLessonDAO(ILessonDAO lessonDAO) {
		this.lessonDAO = lessonDAO;
	}
    
	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.lesson.service.ILessonService#getLessonDetails(java.lang.Long)
	 */
	public LessonDetailsDTO getLessonDetails(Long lessonId) {
		Lesson lesson = lessonDAO.getLesson(lessonId);
		LessonDetailsDTO dto = null;
		if ( lesson != null ) {
			dto = lesson.getLessonDetails();
			Integer active = getCountActiveLessonLearners(lessonId);
			dto.setNumberStartedLearners(active!=null?active:new Integer(0));
		}
		return dto;
	}
	
	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.lesson.service.ILessonService#getLessonData(java.lang.Long)
	 */
	public LessonDTO getLessonData(Long lessonId) {
		Lesson lesson = lessonDAO.getLesson(lessonId);
		LessonDTO dto = null;
		if ( lesson != null ) {
			dto = lesson.getLessonData();
		}
		return dto;
	}

}
