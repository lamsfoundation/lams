/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
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
 * ***********************************************************************/

package org.lamsfoundation.lams.learning.web.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;

import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.usermanagement.User;


/**
 * The utility class to managed learner cache who has joined the lesson. This
 * is to avoid unecessary database call for getting learners who are doing
 * the currently running lesson.
 * 
 * The current implementation is a simple solution that is using servlet context
 * as the caching container. TODO This meant to be replaced by other cache
 * solution such as JBoss transactional cache.
 * 
 * @author Jacky Fang
 * @since  2005-3-30
 * @version 1.1
 * 
 */
public class LessonLearnerDataManager
{
    public static final String ATTR_LESSON_USER_CACHE = "lesson-";
    
    
    /**
     * Cache the user who are running the lesson into the hashmap.
     * @param context the context of the servlet container.
     * @param lesson the running lesson.
     * @param learner the learner who is participating the lesson.
     */
    public static void cacheLessonUser(ServletContext context,
                                       Lesson lesson,
                                       User learner)
    {
		synchronized (context)
		{
			//retrieve the map from context
			HashMap lessonUsersMap =
				(HashMap) context.getAttribute(ATTR_LESSON_USER_CACHE+lesson.getLessonId().toString());
			//create new if never created before
			if (lessonUsersMap == null)
			    lessonUsersMap = new HashMap();
			//set into cache if never cached before
			if (!lessonUsersMap.containsKey(learner.getUserId()))
			{
			    lessonUsersMap.put(learner.getUserId(), learner);
				context.setAttribute(ATTR_LESSON_USER_CACHE+lesson.getLessonId().toString(), lessonUsersMap);
			}
		}
    }
    
    /**
     * Remove the user cache from the hashmap if the learner exit the lesson.
     * @param context the context of the servlet container.
     * @param lesson the running lesson.
     * @param learner the learner who is exitting the lesson.
     */
    public static void removeLessonUserFromCache(ServletContext context,
                                                 Lesson lesson,
                                                 User learner)
    {
		synchronized (context)
		{
			//retrieve the map from context
			HashMap lessonUsersMap =
				(HashMap) context.getAttribute(ATTR_LESSON_USER_CACHE+lesson.getLessonId().toString());
			//don't do anything if the cache is not there.
			if (lessonUsersMap == null)
			    return;
			//remove it if it is there.
			if (lessonUsersMap.containsKey(learner.getUserId()))
			{
			    lessonUsersMap.remove(learner.getUserId());
				context.setAttribute(ATTR_LESSON_USER_CACHE+lesson.getLessonId().toString(), lessonUsersMap);
			}
		}
    }
    
    /**
     * Return all the learners who are doing the requested lesson at the moment.
     * @param context the context of the servlet container. 
     * @param lessonId the requested lesson.
     * @param learnerService the learner service object.
     */
    public static List getAllLessonLearners(ServletContext context,
                                            long lessonId,
                                            ILearnerService learnerService)
    {
		//retrieve the map from context
		HashMap lessonUsersMap =
			(HashMap) context.getAttribute(ATTR_LESSON_USER_CACHE+lessonId);
		
		if(lessonUsersMap==null)
		    lessonUsersMap = restoreMapFromDatabase(learnerService,lessonId,
		                                            context);
		
		ArrayList lessonUsers = new ArrayList();
		
		for(Iterator i = lessonUsersMap.entrySet().iterator();i.hasNext();)
		{
		    Map.Entry entry = (Map.Entry)i.next();
		    lessonUsers.add(entry.getValue());
		}
		
		return lessonUsers;
    }

    /**
     * Restore hashmap by querying active learners from database. 
     * @param learnerService the learner service object.
     * @param lessonId the requested lesson.
     * @param context the context of the servlet container. 
     * @return the restored hashmap cache. 
     */
    private static HashMap restoreMapFromDatabase(ILearnerService learnerService,
                                                  long lessonId,
                                                  ServletContext context)
    {
        HashMap learnersMap = new HashMap();
        List learners = learnerService.getActiveLearnersByLesson(lessonId);
        
        for(Iterator i = learners.iterator();i.hasNext();)
        {
            User learner = (User)i.next();
            learnersMap.put(learner.getUserId(), learner);
        }
        
		context.setAttribute(ATTR_LESSON_USER_CACHE+lessonId, learnersMap);

        return learnersMap;
    }
    
}
