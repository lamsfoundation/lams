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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.Grouper;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.learningdesign.dao.IActivityDAO;
import org.lamsfoundation.lams.learningdesign.dao.IGroupingDAO;
import org.lamsfoundation.lams.learningdesign.exception.GroupingException;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.dao.ILessonDAO;
import org.lamsfoundation.lams.lesson.dto.LessonDTO;
import org.lamsfoundation.lams.lesson.dto.LessonDetailsDTO;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;


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
	private static Logger log = Logger.getLogger(LessonService.class);
	 
	private ILessonDAO lessonDAO;
	private IGroupingDAO groupingDAO;
	private MessageService messageService;
    private HashMap<Long,HashMap> lessonMaps = new HashMap<Long,HashMap>(); // contains maps of lesson users

    /* ******* Spring injection methods ***************************************/
	public void setLessonDAO(ILessonDAO lessonDAO) {
		this.lessonDAO = lessonDAO;
	}
    
	public void setGroupingDAO(IGroupingDAO groupingDAO) {
		this.groupingDAO = groupingDAO;
	}


	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}


	/* *********** Service methods ***********************************************/
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

	/**
     * If the supplied learner is not already in a group, then perform grouping for 
     * the learners who have started the lesson, based on the grouping activity.
     * The grouper decides who goes in which group.
     * 
     *  Can only be run on a Random Grouping
     * 
     * @param lessonId lesson id (mandatory)
     * @param groupingActivity the activity that has create grouping. (mandatory)
     * @param learner the learner to be check before grouping. (mandatory)
     */
    public void performGrouping(Long lessonId, GroupingActivity groupingActivity, User learner) throws LessonServiceException
    {
		Grouping grouping = groupingActivity.getCreateGrouping();
		if ( grouping != null && grouping.isRandomGrouping() ) {
			// get the real objects, not the CGLIB version
			grouping = groupingDAO.getGroupingById(grouping.getGroupingId());
        	Grouper grouper = grouping.getGrouper();
        	
        	if ( grouper != null ) {
        		grouper.setCommonMessageService(messageService);
        		try {
	        		if ( grouping.getGroups().size() == 0 ) {
	        			// no grouping done yet - do everyone already in the lesson.
	        			List usersInLesson = getActiveLessonLearners(lessonId);
	        			grouper.doGrouping(grouping, (String)null, usersInLesson);
	        		} else if ( ! grouping.doesLearnerExist(learner) )  {
	        			// done the others, just do the one user
	        			grouper.doGrouping(grouping, null, learner);
	        		}
        		} catch ( GroupingException e ) {
        			throw new LessonServiceException(e);
        		}
        		groupingDAO.update(grouping);
        	}
        	
		} else {
			String error = "The method performGrouping supports only grouping methods where the grouper decides the groups (currently only RandomGrouping). Called with a groupingActivity with the wrong grouper "+groupingActivity.getActivityId();
			log.error(error);
			throw new LessonServiceException(error);
		}
    }

	/**
     * Perform the grouping, setting the given list of learners as one group. Not used initially.
     * @param groupingActivity the activity that has create grouping. (mandatory)
     * @param learners to form one group
     */
  	public void performGrouping(GroupingActivity groupingActivity, String groupName, List learners) throws LessonServiceException {
    	
        Grouping grouping = groupingActivity.getCreateGrouping();
		if ( grouping != null && grouping.isChosenGrouping() ) {
        	Grouper grouper = grouping.getGrouper();
        	if ( grouper != null ) {
        		grouper.setCommonMessageService(messageService);
        		try {
        			grouper.doGrouping(grouping, groupName, learners);
        		} catch ( GroupingException e ) {
        			throw new LessonServiceException(e);
        		}
        		groupingDAO.update(grouping);
        	}
		} else {
			String error = "The method performChosenGrouping supports only grouping methods where the supplied list should be used as a single group (currently only ChosenGrouping). Called with a groupingActivity with the wrong grouper "+groupingActivity.getActivityId();
			log.error(error);
			throw new LessonServiceException(error);
		}
  	}
    
	/**
     * Perform the grouping, setting the given list of learners as one group. Currently used for chosen grouping.
     * @param groupingActivity the activity that has create grouping. (mandatory)
     * @param learners to form one group
     */
   public void performGrouping(GroupingActivity groupingActivity, Long groupId, List learners) throws LessonServiceException {
       Grouping grouping = groupingActivity.getCreateGrouping();
		if ( grouping != null && grouping.isChosenGrouping() ) {
	       	Grouper grouper = grouping.getGrouper();
	       	if ( grouper != null ) {
	       		grouper.setCommonMessageService(messageService);
	       		try {
	       			grouper.doGrouping(grouping, groupId, learners);
	       		} catch ( GroupingException e ) {
	       			throw new LessonServiceException(e);
	       		}
	       		groupingDAO.update(grouping);
	       	}
		} else {
			String error = "The method performChosenGrouping supports only grouping methods where the supplied list should be used as a single group (currently only ChosenGrouping). Called with a groupingActivity with the wrong grouper "+groupingActivity.getActivityId();
			log.error(error);
			throw new LessonServiceException(error);
		}
   }

	/**
     * Remove learners from the given group. 
     * @param groupingActivity the activity that has create grouping. (mandatory)
     * @param groupID if not null only remove user from this group, if null remove learner from any group.
     * @param learners the learners to be removed (mandatory)
     */
    public void removeLearnersFromGroup(GroupingActivity groupingActivity, Long groupID, List<User> learners) throws LessonServiceException
    {
		Grouping grouping = groupingActivity.getCreateGrouping();
		if ( grouping != null ) {
			// get the real objects, not the CGLIB version
			grouping = groupingDAO.getGroupingById(grouping.getGroupingId());
        	Grouper grouper = grouping.getGrouper();
        	if ( grouper != null ) {
        		try {
        			grouper.removeLearnersFromGroup(grouping, groupID, learners);
        		} catch ( GroupingException e ) {
        			throw new LessonServiceException(e);
        		}
        	}
    		groupingDAO.update(grouping);
		}
    }

    /** Create an empty group for the given grouping. If the group name is not supplied
     * or the group name already exists then nothing happens.
     * 
     * @param groupingActivity the activity that has create grouping. (mandatory)
     * @param groupName (mandatory)
     */
    public void createGroup(GroupingActivity groupingActivity, String name) throws LessonServiceException 
    {
		Grouping grouping = groupingActivity.getCreateGrouping();
		if ( grouping != null ) {
			// get the real objects, not the CGLIB version
			grouping = groupingDAO.getGroupingById(grouping.getGroupingId());
        	Grouper grouper = grouping.getGrouper();
        	if ( grouper != null ) {
        		try {
        			grouper.createGroup(grouping, name);
        		} catch ( GroupingException e ) {
        			throw new LessonServiceException(e);
        		}
        	}
    		groupingDAO.update(grouping);
		}
	}

    /** 
     * Remove a group for the given grouping. If the group is already used (e.g. a tool session exists)
     * then it throws a GroupingException.
     *  
     * @param groupingActivity the activity that has create grouping. (mandatory)
     * @param groupID (mandatory)
     */
    public void removeGroup(GroupingActivity groupingActivity, Long groupID) throws LessonServiceException {
		Grouping grouping = groupingActivity.getCreateGrouping();
		if ( grouping != null ) {
			// get the real objects, not the CGLIB version
			grouping = groupingDAO.getGroupingById(grouping.getGroupingId());
        	Grouper grouper = grouping.getGrouper();
        	if ( grouper != null ) {
        		try {
        			grouper.removeGroup(grouping, groupID);
        		} catch ( GroupingException e ) {
        			throw new LessonServiceException(e);
        		}
        	}
    		groupingDAO.update(grouping);
		}
	}
    
    
}
