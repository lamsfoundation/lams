/*
 * LearnerService.java
 *
 * Created on 11 January 2005, 15:42
 */
package org.lamsfoundation.lams.learning.service;

import java.util.List;
import java.util.Set;

import org.lamsfoundation.lams.learning.progress.ProgressEngine;
import org.lamsfoundation.lams.learning.progress.ProgressException;
import org.lamsfoundation.lams.learning.web.bean.ActivityURL;
import org.lamsfoundation.lams.learning.web.util.Utils;
import org.lamsfoundation.lams.learningdesign.Activity;

import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.dao.ILessonDAO;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.dao.ILearnerProgressDAO;
import org.lamsfoundation.lams.usermanagement.User;
/**
 * This class is a facade over the Learning middle tier.
 * @author chris
 */
public class LearnerService implements ILearnerService
{
    private ILearnerProgressDAO learnerProgressDAO = null;
    private ILessonDAO lessonDAO = null;
    
    /** Creates a new instance of LearnerService */
    public LearnerService()
    {
    }

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
     * Joins a User to a a new lesson as a learner
     * @param learner the Learner
     * @param lessionID identifies the Lesson to start
     * @throws LearnerServiceException in case of problems.
     */
    public LearnerProgress startLesson(User learner, Lesson lesson) throws ProgressException
    {
        //return ProgressEngine.startLesson(learner, lesson);
    	LearnerProgress learnerProgress = new ProgressEngine().getStartPoint(learner, lesson);
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
        return lessonDAO.getLearnerProgress(learner, lesson);
    }
    

    public LearnerProgress chooseActivity(User learner, Lesson lesson, Activity activity) {
    	LearnerProgress progress = lessonDAO.getLearnerProgress(learner, lesson);
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
    	return new ProgressEngine().calculateProgress(learner, lesson, completedActivity);
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
	    	ActivityURL activityURL = Utils.getActivityURL(nextActivity, nextLearnerProgress);
	    	url = activityURL.getUrl();
    	}
    	catch (ProgressException e) {
    		// log e
    		throw new LearnerServiceException(e.getMessage());
    	}
    	
    	return url;
    }
    
	public ILessonDAO getLessonDAO() {
		return lessonDAO;
	}
	
	public void setLessonDAO(ILessonDAO lessonDAO) {
		this.lessonDAO = lessonDAO;
	}
}
