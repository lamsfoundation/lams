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

import java.util.Iterator;
import java.util.List;

import org.lamsfoundation.lams.learning.progress.ProgressEngine;
import org.lamsfoundation.lams.learning.progress.ProgressException;

import org.lamsfoundation.lams.learning.web.util.ActionMappings;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;

import org.lamsfoundation.lams.lesson.Lesson;

import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.dao.ILearnerProgressDAO;
import org.lamsfoundation.lams.lesson.dao.ILessonDAO;

import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.dao.IToolSessionDAO;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
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
    private ILearnerProgressDAO learnerProgressDAO;
    private ILessonDAO lessonDAO;
    private ProgressEngine progressEngine;
    private IToolSessionDAO toolSessionDAO;
    private ILamsToolService lamsToolService;
    private ActionMappings actionMappings;
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
    public void setLamsToolService(ILamsToolService lamsToolService)
    {
        this.lamsToolService = lamsToolService;
    }
    //---------------------------------------------------------------------
    // Service Methods
    //---------------------------------------------------------------------
    /**
     * Returns a list of all the active Lessons a User is a Learner in.
     * @param User the learner
     * @return List of Lessons
     * @throws LearnerServiceException in case of problems.
     */
    public List getActiveLessons(User learner)
    {
        return lessonDAO.getActiveLessonsForLearner(learner);
    }
    
    public Lesson getLesson(Long lessonId)
    {
        return lessonDAO.getLesson(lessonId);
    }
    
 
    /**
     * Joins a User to a new lesson as a learner
     * @param learner the Learner
     * @param lessionID identifies the Lesson to start
     * @throws LamsToolServiceException
     * @throws LearnerServiceException in case of problems.
     */
    public LearnerProgress joinLesson(User learner, Lesson lesson) throws ProgressException, LamsToolServiceException
    {
        LearnerProgress learnerProgress = learnerProgressDAO.getLearnerProgressByLearner(learner,lesson);
    	
        if(learnerProgress==null)
        {
            //create a new learner progress for new learner
            learnerProgress = new LearnerProgress(learner,lesson);
            
            progressEngine.setUpStartPoint(learner, lesson,learnerProgress);
            
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
    

    public LearnerProgress chooseActivity(User learner, Lesson lesson, Activity activity) {
    	LearnerProgress progress = learnerProgressDAO.getLearnerProgressByLearner(learner, lesson);
    	progress.setProgressState(activity, LearnerProgress.ACTIVITY_ATTEMPTED);
    	learnerProgressDAO.saveLearnerProgress(progress);
    	return progress;
    }
    
    
    /**
     * Calculates learner progress and returns the data required to be displayed to the learner (including URL(s)).
     * @param completedActivityID identifies the activity just completed
     * @param learner the Learner
     * @param lesson the Lesson in progress.
     * @return the bean containing the display data for the Learner
     * @throws LearnerServiceException in case of problems.
     */
    public LearnerProgress calculateProgress(Activity completedActivity, 
                                             User learner, 
                                             Lesson lesson) throws ProgressException
    {
        LearnerProgress learnerProgress = learnerProgressDAO.getLearnerProgressByLearner(learner,lesson);

        learnerProgress = progressEngine.calculateProgress(learner, lesson, completedActivity,learnerProgress);
        
        learnerProgressDAO.updateLearnerProgress(learnerProgress);
        
        return learnerProgress;
    }
    

    public String completeToolActivity(long toolSessionId) {
    	// get learner, lesson and activity using toolSessionId
    	User learner = null;
    	Lesson lesson = null;
    	Activity activity = null;
    	
    	String url = null;
    	try {
	    	LearnerProgress nextLearnerProgress = calculateProgress(activity, learner, lesson);
	    	Activity nextActivity = nextLearnerProgress.getNextActivity();
	    	url = actionMappings.getActivityURL(nextActivity, nextLearnerProgress);
    	}
    	catch (ProgressException e) {
    		// log e
    		throw new LearnerServiceException(e.getMessage());
    	}
    	
    	return url;
    }
    
    public void setActionMappings(ActionMappings actionMappings) {
		this.actionMappings = actionMappings;
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
    private void createToolSessionsIfNecessary(LearnerProgress learnerProgress) throws LamsToolServiceException
    {
        if(learnerProgress.getNextActivity()==null)
            throw new LearnerServiceException("Error occurs in [" +
            		"createToolSessionsIfNecessary], Can't initialize tool " +
            		"sessions without knowing the activity.");
        
        Activity nextActivity = learnerProgress.getNextActivity();
        
        for(Iterator i = nextActivity.getAllToolActivitiesFrom(nextActivity).iterator();i.hasNext();)
        {
            ToolActivity toolActivity = (ToolActivity)i.next();
            if(shouldCreateToolSession(learnerProgress,toolActivity))
                createToolSessionFor(toolActivity, learnerProgress.getUser(),learnerProgress.getLesson());
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
        ToolSession toolSession = lamsToolService.createToolSession(learner,toolActivity,lesson);
        
        toolActivity.getToolSessions().add(toolSession);
        
        lamsToolService.notifyToolsToCreateSession(toolSession.getToolSessionId(), toolActivity);
    }
}
