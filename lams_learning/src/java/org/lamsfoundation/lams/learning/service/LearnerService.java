/*
 * LearnerService.java
 *
 * Created on 11 January 2005, 15:42
 */
package org.lamsfoundation.lams.learning.service;

import java.util.Iterator;
import java.util.List;

import org.lamsfoundation.lams.learning.progress.ProgressEngine;
import org.lamsfoundation.lams.learning.progress.ProgressException;
import org.lamsfoundation.lams.learning.web.bean.ActivityURL;
import org.lamsfoundation.lams.learning.web.util.ActionMappings;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;

import org.lamsfoundation.lams.lesson.Lesson;

import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.dao.ILearnerProgressDAO;
import org.lamsfoundation.lams.lesson.dao.ILessonDAO;

import org.lamsfoundation.lams.tool.dao.IToolSessionDAO;
import org.lamsfoundation.lams.usermanagement.User;
/**
 * This class is a facade over the Learning middle tier.
 * @author chris
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
     * @throws LearnerServiceException in case of problems.
     */
    public LearnerProgress joinLesson(User learner, Lesson lesson) throws ProgressException
    {
        LearnerProgress learnerProgress = learnerProgressDAO.getLearnerProgressByLearner(learner,lesson);
    	
        if(learnerProgress!=null)
        {
            //create a new learner progress for new learner
            learnerProgress = new LearnerProgress(learner,lesson);
            
            progressEngine.getStartPoint(learner, lesson,learnerProgress);
            
            learnerProgressDAO.saveLearnerProgress(learnerProgress);
        }
        
        createToolSessionsIfNecessary(learnerProgress);
    	return learnerProgress;
    }
    

    /**
     * @param learnerProgress
     */
    private void createToolSessionsIfNecessary(LearnerProgress learnerProgress)
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
                createToolSessionFor(toolActivity);
        }
    }
    
    /**
     * @return
     */
    private boolean shouldCreateToolSession(LearnerProgress learnerProgress,
                                            ToolActivity toolActivity)
    {
        if(!toolActivity.getTool().getSupportsGrouping())
        {
            //ToolSession nonGroupedToolSession = toolSessionDAO.get
        }
        return false;
    }
    /**
     * @param toolActivity
     */
    private void createToolSessionFor(ToolActivity toolActivity)
    {
        // TODO Auto-generated method stub
        
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
    public LearnerProgress calculateProgress(Activity completedActivity, User learner, Lesson lesson) throws ProgressException
    {
    	return progressEngine.calculateProgress(learner, lesson, completedActivity);
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
	    	ActivityURL activityURL = actionMappings.getActivityURL(nextActivity, nextLearnerProgress);
	    	url = activityURL.getUrl();
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
	
}
