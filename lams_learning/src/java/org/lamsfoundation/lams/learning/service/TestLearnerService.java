/*
 * Created on 18/01/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.learning.service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.lamsfoundation.lams.learning.progress.ProgressException;
import org.lamsfoundation.lams.learning.web.bean.ActivityURL;
import org.lamsfoundation.lams.learning.web.util.Utils;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ComplexActivity;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.OptionsActivity;
import org.lamsfoundation.lams.learningdesign.ParallelActivity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.dao.ILessonDAO;
import org.lamsfoundation.lams.usermanagement.User;

/**
 * @author kevin
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestLearnerService implements ILearnerService {
	
	private HttpServletRequest request;
	private static String NAME = "lams.learning.testlearnerservice";
	
	private ILessonDAO lessonDAO;
	
	public void setLessonDAO(ILessonDAO lessonDAO) {
		this.lessonDAO = lessonDAO;
	}
	

	public TestLearnerService() {
	}
	
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	private LearnerProgress getProgress() {
		HttpSession session = request.getSession(true);
		LearnerProgress progress = (LearnerProgress)session.getAttribute(NAME);
		if (progress == null) {
			// create new progress
			progress = createProgress();
			// store progress
			session.setAttribute(NAME, progress);
		}
		return progress;
	}

	private void setProgress(LearnerProgress progress) {
		HttpSession session = request.getSession(true);
		session.setAttribute(NAME, progress);
	}
	
	public void clearProgress() {
		setProgress(null);
	}
	
	
	public List getActiveLessons(User learner) {
		return null;
	}

	public Lesson getLesson(Long lessonID) {
		return null;
	}

	public LearnerProgress startLesson(User learner, Lesson lesson) {
		return null;
	}

	public LearnerProgress getProgress(User learner, Lesson lesson) {
		return getProgress();
	}

	public LearnerProgress calculateProgress(Activity completedActivity, User learner, Lesson lesson) {
		LearnerProgress progress = getProgress(learner, lesson);
		long completedActivityId = completedActivity.getActivityId().longValue();
		setComplete(completedActivityId, progress);
		
		LearnerProgress newProgress = copyProgress(progress);
		Activity currentActivity = calculateCurrentActivity(newProgress);
		newProgress.setCurrentActivity(currentActivity);
		Activity nextActivity = calculateNextActivity(completedActivityId, newProgress);
		newProgress.setNextActivity(nextActivity);
		
		setProgress(newProgress);
		
		return newProgress;
	}

	public String completeToolActivity(long toolSessionId) {
    	// get learner, lesson and activity using toolSessionId
    	User learner = null;
    	Lesson lesson = null;
    	//Activity activity = null;
		LearnerProgress progress = getProgress();
    	Activity activity = getActivity(toolSessionId, progress);
    	
    	String url = null;
    	LearnerProgress nextLearnerProgress = calculateProgress(activity, learner, lesson);
    	//Activity nextActivity = nextLearnerProgress.getNextActivity();
    	//ActivityURL activityURL = Utils.generateActivityURL(nextActivity, nextLearnerProgress);
    	ActivityURL activityURL = Utils.generateNextActivityURL(progress, nextLearnerProgress);
    	url = activityURL.getUrl();
    	
    	return url;
	}

	
	private void setComplete(long activityId, LearnerProgress progress) {
		Activity activity = getActivity(activityId, progress);
		progress.getAttemptedActivities().remove(activity);
		progress.getCompletedActivities().add(activity);

		if ((activityId == 4) &&
				progress.getProgressState(getActivity(5, progress)) == LearnerProgress.ACTIVITY_COMPLETED) {
			setComplete(3, progress);
		}
		else if ((activityId == 5) &&
				progress.getProgressState(getActivity(4, progress)) == LearnerProgress.ACTIVITY_COMPLETED) {
			setComplete(3, progress);
		}
		else if ((activityId == 7) || (activityId == 8) || (activityId == 9)) {
			if ((progress.getProgressState(getActivity(7, progress)) == LearnerProgress.ACTIVITY_COMPLETED) &&
					(progress.getProgressState(getActivity(8, progress)) == LearnerProgress.ACTIVITY_COMPLETED) &&
					(progress.getProgressState(getActivity(9, progress)) == LearnerProgress.ACTIVITY_COMPLETED)) {
				setComplete(6, progress);
			}
		}
		else if ((activityId == 10) &&
				progress.getProgressState(getActivity(10, progress)) == LearnerProgress.ACTIVITY_COMPLETED) {
			progress.setLessonComplete(true);
		}
	}
	
	private Activity getActivity(long activityId, LearnerProgress progress) {
		Lesson lesson = progress.getLesson();
		if (lesson == null) return null;
		LearningDesign learningDesign = lesson.getLearningDesign();
		if (learningDesign == null) return null;
		Set activities = learningDesign.getActivities();
		if (activities == null) return null;
		Iterator i = activities.iterator();
		while (i.hasNext()) {
			Activity activity = (Activity)i.next();
			if (activity.getActivityId().longValue() == activityId) {
				return activity;
			}
		}
		return null;
	}
	
	
	private Activity calculateCurrentActivity(LearnerProgress progress) {
		Activity activity = null;
		if (progress.getProgressState(getActivity(1, progress)) != LearnerProgress.ACTIVITY_COMPLETED) activity = getActivity(1, progress);
		else if (progress.getProgressState(getActivity(2, progress)) != LearnerProgress.ACTIVITY_COMPLETED) activity = getActivity(2, progress);
		else if (progress.getProgressState(getActivity(3, progress)) != LearnerProgress.ACTIVITY_COMPLETED) activity = getActivity(3, progress);
		else if (progress.getProgressState(getActivity(6, progress)) != LearnerProgress.ACTIVITY_COMPLETED) activity = getActivity(6, progress);
		else if (progress.getProgressState(getActivity(10, progress)) != LearnerProgress.ACTIVITY_COMPLETED) activity = getActivity(10, progress);
		
		return activity;
	}
	
	private Activity calculateNextActivity(long completedId, LearnerProgress progress) {
		// nextActivity is the next activity to be attempted after completedId,
		// usually this will be whatever is now the current
		Activity nextActivity = progress.getCurrentActivity();
		
		if ((completedId == 4) || (completedId == 5)) {
			nextActivity = null;
		}
		else if ((completedId == 7) ||
				(completedId == 8) ||
				(completedId == 9)) {
			nextActivity = null;
		}
		
		if (completedId == 10) {
			nextActivity = null;
		}
		
		return nextActivity;
	}

	
	private LearnerProgress createProgress() {
		
		LearnerProgress progress = new LearnerProgress();
		Set attempted = new HashSet();
		Set complete = new HashSet();

		Lesson lesson = new Lesson();
		//Lesson lesson = lessonDAO.getLesson(new Long(1));
		progress.setLesson(lesson);
		
		LearningDesign learningDesign = new LearningDesign();
		lesson.setLearningDesign(learningDesign);
		Set activities = new HashSet();
		learningDesign.setActivities(activities);
		
		Activity activity = new ToolActivity();
		activity.setActivityId(new Long(1));
		activities.add(activity);
		activity = new ToolActivity();
		activity.setActivityId(new Long(2));
		activities.add(activity);
		activity = new ParallelActivity();
		activity.setActivityId(new Long(3));
		Set subActivities = new HashSet();
		((ComplexActivity)activity).setActivities(subActivities);
		activities.add(activity);
		
		activity = new ToolActivity();
		activity.setActivityId(new Long(4));
		subActivities.add(activity);
		activities.add(activity);
		activity = new ToolActivity();
		activity.setActivityId(new Long(5));
		subActivities.add(activity);
		activities.add(activity);

		activity = new OptionsActivity();
		activity.setActivityId(new Long(6));
		activity.setTitle("foo");
		activity.setDescription("activities for foo");
		((OptionsActivity)activity).setMinNumberOfOptions(new Integer(2));
		((OptionsActivity)activity).setMaxNumberOfOptions(new Integer(3));
		subActivities = new HashSet();
		((ComplexActivity)activity).setActivities(subActivities);
		activities.add(activity);
		
		activity = new ToolActivity();
		activity.setActivityId(new Long(7));
		subActivities.add(activity);
		activities.add(activity);
		activity = new ToolActivity();
		activity.setActivityId(new Long(8));
		subActivities.add(activity);
		activities.add(activity);
		activity = new ToolActivity();
		activity.setActivityId(new Long(9));
		subActivities.add(activity);
		activities.add(activity);

		activity = new ToolActivity();
		activity.setActivityId(new Long(10));
		activities.add(activity);
		
		progress.setAttemptedActivities(attempted);
		progress.setCompletedActivities(complete);
		
		progress.setCurrentActivity(getActivity(1, progress));
		
		return progress;
	}
	
	private LearnerProgress createNextProgress(long completedId, LearnerProgress oldProgress) {
		LearnerProgress progress = new LearnerProgress();
		progress.setLesson(oldProgress.getLesson());
		progress.setLessonComplete(oldProgress.isLessonComplete());
		progress.setAttemptedActivities(oldProgress.getAttemptedActivities());
		progress.setCompletedActivities(oldProgress.getCompletedActivities());
		progress.setCurrentActivity(oldProgress.getCurrentActivity());
		
		// nextActivity is the next activity to be attempted after completedId,
		// usually this will be whatever is now the current
		Activity nextActivity = oldProgress.getCurrentActivity();
		
		if ((completedId == 4) || (completedId == 5)) {
			nextActivity = null;
		}
		else if ((completedId == 7) ||
				(completedId == 8) ||
				(completedId == 9)) {
			nextActivity = null;
		}
		
		if (completedId == 10) {
			nextActivity = null;
		}
		
		// this is the completed activity specific progress
		progress.setNextActivity(nextActivity);
		
		return progress;
	}
	
	private LearnerProgress copyProgress(LearnerProgress progress) {
		LearnerProgress newProgress = new LearnerProgress();
		newProgress.setLesson(progress.getLesson());
		newProgress.setLessonComplete(progress.isLessonComplete());
		newProgress.setAttemptedActivities(progress.getAttemptedActivities());
		newProgress.setCompletedActivities(progress.getCompletedActivities());
		newProgress.setCurrentActivity(progress.getCurrentActivity());
		return newProgress;
	}
	
}
