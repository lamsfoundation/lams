/*
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
USA

http://www.gnu.org/licenses/gpl.txt
*/


package org.lamsfoundation.lams.learning.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learning.progress.ProgressEngine;
import org.lamsfoundation.lams.learning.progress.ProgressException;
import org.lamsfoundation.lams.learning.web.util.ActivityMapping;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.dao.IActivityDAO;

import org.lamsfoundation.lams.lesson.Lesson;

import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.dao.ILearnerProgressDAO;
import org.lamsfoundation.lams.lesson.dao.ILessonDAO;
import org.lamsfoundation.lams.lesson.dto.LessonDTO;

import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.dao.IToolSessionDAO;
import org.lamsfoundation.lams.tool.service.ILamsCoreToolService;

import org.lamsfoundation.lams.tool.service.LamsToolServiceException;
import org.lamsfoundation.lams.usermanagement.User;
/**
 * This class is a facade over the Learning middle tier.
 * @author chris, Jacky Fang
 */
public class LearnerService implements ILearnerService
{
    //---------------------------------------------------------------------
    // Instance variables
    //---------------------------------------------------------------------
	private static Logger log = Logger.getLogger(LearnerService.class);
    
    private ILearnerProgressDAO learnerProgressDAO;
    private ILessonDAO lessonDAO;
    private IActivityDAO activityDAO;
    private ProgressEngine progressEngine;
    private IToolSessionDAO toolSessionDAO;
    private ILamsCoreToolService lamsCoreToolService;
    private ActivityMapping activityMapping;
    //---------------------------------------------------------------------
    // Inversion of Control Methods - Constructor injection
    //---------------------------------------------------------------------
    
    /** Creates a new instance of LearnerService */
    public LearnerService(ProgressEngine progressEngine)
    {
        this.progressEngine = progressEngine;
    }
    //---------------------------------------------------------------------
    // Inversion of Control Methods - Method injection
    //---------------------------------------------------------------------
    /**
     * @param toolSessionDAO The toolSessionDAO to set.
     */
    public void setToolSessionDAO(IToolSessionDAO toolSessionDAO)
    {
        this.toolSessionDAO = toolSessionDAO;
    }
    
    /**
     * @param lessonDAO The lessonDAO to set.
     */
	public void setLessonDAO(ILessonDAO lessonDAO) 
	{
		this.lessonDAO = lessonDAO;
	}

    /**
     * @param learnerProgressDAO The learnerProgressDAO to set.
     */
    public void setLearnerProgressDAO(ILearnerProgressDAO learnerProgressDAO)
    {
        this.learnerProgressDAO = learnerProgressDAO;
    }

    /**
     * @param lamsToolService The lamsToolService to set.
     */
    public void setLamsCoreToolService(ILamsCoreToolService lamsToolService)
    {
        this.lamsCoreToolService = lamsToolService;
    }
    
    public void setActivityMapping(ActivityMapping activityMapping) {
		this.activityMapping = activityMapping;
	}
    
    /**
     * @param activityDAO The activityDAO to set.
     */
    public void setActivityDAO(IActivityDAO activityDAO)
    {
        this.activityDAO = activityDAO;
    }
    //---------------------------------------------------------------------
    // Service Methods
    //---------------------------------------------------------------------
    /**
     * Delegate to lesson dao to load up the lessons.
     * @see org.lamsfoundation.lams.learning.service.ILearnerService#getActiveLessonsFor(org.lamsfoundation.lams.usermanagement.User)
     */
    public LessonDTO[] getActiveLessonsFor(User learner)
    {
        List activeLessons = this.lessonDAO.getActiveLessonsForLearner(learner);
        return getLessonDataFor(activeLessons);
    }
    
    public Lesson getLesson(Long lessonId)
    {
        return lessonDAO.getLesson(lessonId);
    }
    
 
    /**
     * <p>Joins a User to a lesson as a learner. It could either be a new lesson
     * or a lesson that has been started.</p>
     * 
     * <p>In terms of new lesson, a new learner progress would be initialized.
     * Tool session for the next activity will be initialized if necessary.</p>
     * 
     * <p>In terms of an started lesson, the learner progress will be returned
     * without calculation. Tool session will be initialized if necessary.
     * Note that we won't initialize tool session for current activity because
     * we assume tool session will always initialize before it becomes a 
     * current activity.</p
     * 
     * @param learner the Learner
     * @param lessionID identifies the Lesson to start
     * @throws LamsToolServiceException
     * @throws LearnerServiceException in case of problems.
     */
    public LearnerProgress joinLesson(User learner, Lesson lesson) 
    {
        LearnerProgress learnerProgress = learnerProgressDAO.getLearnerProgressByLearner(learner,lesson);
    	
        if(learnerProgress==null)
        {
            //create a new learner progress for new learner
            learnerProgress = new LearnerProgress(learner,lesson);
            
            try
            {
                progressEngine.setUpStartPoint(learner, lesson,learnerProgress);
            }
            catch (ProgressException e)
            {
                log.error("error occurred in 'setUpStartPoint':"+e.getMessage());
        		throw new LearnerServiceException(e.getMessage());
            }
            
            learnerProgressDAO.saveLearnerProgress(learnerProgress);
        }
        
        createToolSessionsIfNecessary(learnerProgress);
    	return learnerProgress;
    }
    


    /**
     * Returns the current progress data of the User.
     * @param learner the Learner
     * @param lesson the Lesson to get progress from.
     * @return LearnerProgess contains the learner's progress for the lesson.
     * @throws LearnerServiceException in case of problems.
     */
    public LearnerProgress getProgress(User learner, Lesson lesson)
    {
        return learnerProgressDAO.getLearnerProgressByLearner(learner, lesson);
    }
    
    /**
     * @see org.lamsfoundation.lams.learning.service.ILearnerService#getProgressById(java.lang.Long)
     */
    public LearnerProgress getProgressById(Long progressId)
    {
        return learnerProgressDAO.getLearnerProgress(progressId);
    }
    
    /**
     * @see org.lamsfoundation.lams.learning.service.ILearnerService#chooseActivity(org.lamsfoundation.lams.usermanagement.User, org.lamsfoundation.lams.lesson.Lesson, org.lamsfoundation.lams.learningdesign.Activity)
     */
    public LearnerProgress chooseActivity(User learner, Lesson lesson, Activity activity) 
    {
    	LearnerProgress progress = learnerProgressDAO.getLearnerProgressByLearner(learner, lesson);
    	progress.setProgressState(activity, LearnerProgress.ACTIVITY_ATTEMPTED);
    	learnerProgressDAO.saveLearnerProgress(progress);
    	return progress;
    }
    
    
    /**
     * Calculates learner progress and returns the data required to be displayed 
     * to the learner.
     * TODO need to initialize tool session for next activity if necessary.
     * @param completedActivityID identifies the activity just completed
     * @param learner the Learner
     * @param lesson the Lesson in progress.
     * @return the bean containing the display data for the Learner
     * @throws LamsToolServiceException
     * @throws LearnerServiceException in case of problems.
     */
    public LearnerProgress calculateProgress(Activity completedActivity, 
                                             User learner, 
                                             Lesson lesson) 
    {
        LearnerProgress learnerProgress = learnerProgressDAO.getLearnerProgressByLearner(learner,lesson);

        try
        {
            learnerProgress = progressEngine.calculateProgress(learner, lesson, completedActivity,learnerProgress);
            learnerProgressDAO.updateLearnerProgress(learnerProgress);
            
            //createToolSessionsIfNecessary(learnerProgress);
        }
        catch (ProgressException e)
        {
            throw new LearnerServiceException(e.getMessage());
        }
/*        catch (LamsToolServiceException e)
        {
            throw new LearnerServiceException(e.getMessage());
        }
*/

        return learnerProgress;
    }
    
    /**
     * 
     * @see org.lamsfoundation.lams.learning.service.ILearnerService#completeToolSession(long, User)
     */
    public String completeToolSession(Long toolSessionId, User learner) 
    {
        //update tool session state in lams
        ToolSession toolSession = lamsCoreToolService.getToolSessionById(toolSessionId);
    	
        toolSession.setToolSessionStateId(ToolSession.ENDED_STATE);
        
        lamsCoreToolService.updateToolSession(toolSession);
        //build up the url for next activity.
    	try 
    	{
	    	LearnerProgress nextLearnerProgress = calculateProgress(toolSession.getToolActivity(), learner, toolSession.getLesson());
	    	return activityMapping.getProgressURL(nextLearnerProgress);
    	}
        catch (UnsupportedEncodingException e)
        {
            log.error("error occurred in 'getProgressURL':"+e.getMessage());
    		throw new LearnerServiceException(e.getMessage());
        }
    }
    
    /**
     * Exit a lesson.
     * @see org.lamsfoundation.lams.learning.service.ILearnerService#exitLesson(org.lamsfoundation.lams.lesson.LearnerProgress)
     */
    public void exitLesson(LearnerProgress progress)
    {
       progress.setRestarting(true);
       learnerProgressDAO.updateLearnerProgress(progress);
    }
    
    /**
     * @see org.lamsfoundation.lams.learning.service.ILearnerService#getActivity(java.lang.Long)
     */
    public Activity getActivity(Long activityId)
    {
        return activityDAO.getActivityByActivityId(activityId);
    }
    
    //---------------------------------------------------------------------
    // Helper Methods
    //---------------------------------------------------------------------
    /**
     * This method navigate through all the tool activities included inside
     * the next activity. For each tool activity, we look up the database
     * to check up the existance of correspondent tool session. If the tool 
     * session doesn't exist, we create a new tool session instance.
     * 
     * @param learnerProgress the learner progress we are processing.
     * @throws LamsToolServiceException
     */
    private void createToolSessionsIfNecessary(LearnerProgress learnerProgress) 
    {
        if(learnerProgress.getNextActivity()==null)
            throw new LearnerServiceException("Error occurs in [" +
            		"createToolSessionsIfNecessary], Can't initialize tool " +
            		"sessions without knowing the activity.");
        
        Activity nextActivity = learnerProgress.getNextActivity();
        try
        {
            for(Iterator i = nextActivity.getAllToolActivitiesFrom(nextActivity).iterator();i.hasNext();)
            {
                ToolActivity toolActivity = (ToolActivity)i.next();
                if(shouldCreateToolSession(learnerProgress,toolActivity))
                    createToolSessionFor(toolActivity, learnerProgress.getUser(),learnerProgress.getLesson());
            }
        }
        catch (LamsToolServiceException e)
        {
            log.error("error occurred in 'createToolSessionFor':"+e.getMessage());
    		throw new LearnerServiceException(e.getMessage());
        }
    }
    
    /**
     * Check up the database to see whether there is an existing tool session
     * has been created already.
     * @return the check up result.
     */
    private boolean shouldCreateToolSession(LearnerProgress learnerProgress,
                                            ToolActivity toolActivity)
    {
        ToolSession targetSession = null;
        //TODO need to be changed according to the change grouping concept
        if(!toolActivity.getTool().getSupportsGrouping())
            targetSession = toolSessionDAO.getToolSessionByLearner(learnerProgress.getUser(),
                                                                   toolActivity);
        else
            targetSession = toolSessionDAO.getToolSessionByGroup(toolActivity.getGroupFor(learnerProgress.getUser()),
                                                                 toolActivity);
        return targetSession!=null?false:true;
    }
    
    /**
     * <p>Create a lams tool session for learner against a tool activity. This will
     * have concurrency issues interms of grouped tool session because it might 
     * be inserting some tool session that has already been inserted by other
     * member in the group. If the unique_check is broken, we need to query
     * the database to get the instance instead of inserting it. It should be
     * done in the Spring rollback strategy. </p>
     *
     * Once lams tool session is inserted, we need to notify the tool to its
     * own session. 
     * 
     * @param toolActivity
     * @param learner
     * @throws LamsToolServiceException
     */
    private void createToolSessionFor(ToolActivity toolActivity,User learner,Lesson lesson) throws LamsToolServiceException
    {
        ToolSession toolSession = lamsCoreToolService.createToolSession(learner,toolActivity,lesson);
        
        toolActivity.getToolSessions().add(toolSession);
        
        lamsCoreToolService.notifyToolsToCreateSession(toolSession.getToolSessionId(), toolActivity);
    }
    
    
    /**
     * Create an array of lesson dto based a list of lessons.
     * @param lessons the list of lessons.
     * @return the lesson dto array.
     */
    private LessonDTO[] getLessonDataFor(List lessons)
    {
        List lessonDTOList = new ArrayList();
        for(Iterator i=lessons.iterator();i.hasNext();)
        {
            Lesson currentLesson = (Lesson)i.next();
            lessonDTOList.add(currentLesson.getLessonData());
        }
        return (LessonDTO[])lessonDTOList.toArray(new LessonDTO[lessonDTOList.size()]);   
    }

}
