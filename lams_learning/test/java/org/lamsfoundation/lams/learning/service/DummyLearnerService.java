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

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.lamsfoundation.lams.learning.web.bean.ActivityURL;
import org.lamsfoundation.lams.learning.web.util.ActionMappings;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ActivityOrderComparator;
import org.lamsfoundation.lams.learningdesign.ComplexActivity;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.OptionsActivity;
import org.lamsfoundation.lams.learningdesign.ParallelActivity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.LessonCompleteActivity;
import org.lamsfoundation.lams.lesson.ParallelWaitActivity;
import org.lamsfoundation.lams.lesson.dao.ILessonDAO;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.usermanagement.User;

/**
 * @author daveg
 *
 */
public class DummyLearnerService implements ILearnerService {
	
	private HttpServletRequest request;
	private static String NAME = "lams.learning.testlearnerservice";
	
	private ActionMappings actionMappings;
	
	private ILessonDAO lessonDAO;
	
	public void setLessonDAO(ILessonDAO lessonDAO) {
		this.lessonDAO = lessonDAO;
	}
	

	public DummyLearnerService() {
	}
	
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	public void setActionMappings(ActionMappings actionMappings) {
		this.actionMappings = actionMappings;
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

	public LearnerProgress joinLesson(User learner, Lesson lesson) {
		return null;
	}

	public LearnerProgress getProgress(User learner, Lesson lesson) {
		return getProgress();
	}
    

    public LearnerProgress chooseActivity(User learner, Lesson lesson, Activity activity) {
		LearnerProgress progress = getProgress();
    	progress.setProgressState(activity, LearnerProgress.ACTIVITY_ATTEMPTED);
    	return progress;
    }

	public LearnerProgress calculateProgress(Activity completedActivity, User learner, Lesson lesson) {
		LearnerProgress progress = getProgress(learner, lesson);
		long completedActivityId = completedActivity.getActivityId().longValue();
		setComplete(completedActivityId, progress);
		
		Activity nextActivity = null;
		Activity previousActivity = null;
		Activity currentActivity = progress.getCurrentActivity();
		
		if (completedActivityId == 1) {
		    nextActivity = getActivity(2, progress);
		    previousActivity = completedActivity;
		    currentActivity = nextActivity;
		}
		else if (completedActivityId == 2) {
		    nextActivity = getActivity(3, progress);
		    previousActivity = completedActivity;
		    currentActivity = nextActivity;
		}
		else if ((completedActivityId == 4) || (completedActivityId == 5)) {
		    Activity activity3 = getActivity(3, progress);
		    if (progress.getProgressState(activity3) == LearnerProgress.ACTIVITY_COMPLETED) {
			    nextActivity = getActivity(6, progress);
			    previousActivity = activity3;
			    currentActivity = nextActivity;
		    }
		    else {
		        nextActivity = new ParallelWaitActivity();
			    previousActivity = completedActivity;
		    }
		}
		else if (completedActivityId == 6) {
		    nextActivity = getActivity(10, progress);
		    previousActivity = currentActivity;
		    currentActivity = nextActivity;
		}
		else if ((completedActivityId == 7) || (completedActivityId == 8) || (completedActivityId == 9)) {
		    Activity activity6 = getActivity(6, progress);
		    if (progress.getProgressState(activity6) == LearnerProgress.ACTIVITY_COMPLETED) {
			    nextActivity = getActivity(10, progress);
			    previousActivity = activity6;
			    currentActivity = nextActivity;
		    }
		    else {
		        nextActivity = activity6;
			    previousActivity = completedActivity;
		    }
		}
		else if (completedActivityId == 10) {
		    nextActivity = new LessonCompleteActivity();
		    previousActivity = currentActivity;
		    currentActivity = nextActivity;
		}

		progress.setCurrentActivity(currentActivity);
		progress.setNextActivity(nextActivity);
		progress.setPreviousActivity(previousActivity);

		setProgress(progress);
		
		return progress;
	}

	public String completeToolActivity(long toolSessionId) {
    	// get learner, lesson and activity using toolSessionId
    	User learner = null;
    	Lesson lesson = null;
    	//Activity activity = null;
		LearnerProgress progress = getProgress();
    	Activity activity = getActivity(toolSessionId, progress);
    	
    	progress = calculateProgress(activity, learner, lesson);
    	String url = actionMappings.getProgressURL(progress);
    	
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
		
		Tool tool = new Tool();
		tool.setLearnerUrl("http://127.0.0.1:8080/lams_learning/test/DummyTool.do");
		
		ToolActivity toolActivity = new ToolActivity();
		toolActivity.setTool(tool);
		toolActivity.setActivityId(new Long(1));
		activities.add(toolActivity);
		toolActivity = new ToolActivity();
		toolActivity.setActivityId(new Long(2));
		toolActivity.setTool(tool);
		activities.add(toolActivity);
		Activity parallelActivity = new ParallelActivity();
		parallelActivity.setActivityId(new Long(3));
		Set subActivities = new HashSet();
		((ComplexActivity)parallelActivity).setActivities(subActivities);
		activities.add(parallelActivity);
		
		toolActivity = new ToolActivity();
		toolActivity.setActivityId(new Long(4));
		toolActivity.setTool(tool);
		subActivities.add(toolActivity);
		activities.add(toolActivity);
		toolActivity = new ToolActivity();
		toolActivity.setActivityId(new Long(5));
		toolActivity.setTool(tool);
		subActivities.add(toolActivity);
		activities.add(toolActivity);

		OptionsActivity optionsActivity = new OptionsActivity();
		optionsActivity.setActivityId(new Long(6));
		optionsActivity.setTitle("foo");
		optionsActivity.setDescription("activities for foo");
		((OptionsActivity)optionsActivity).setMinNumberOfOptions(new Integer(2));
		((OptionsActivity)optionsActivity).setMaxNumberOfOptions(new Integer(3));
		//subActivities = new HashSet();
		subActivities = new TreeSet(new ActivityOrderComparator());
		((ComplexActivity)optionsActivity).setActivities(subActivities);
		activities.add(optionsActivity);
		
		toolActivity = new ToolActivity();
		toolActivity.setActivityId(new Long(7));
		toolActivity.setTool(tool);
		toolActivity.setTitle("activity 7");
		subActivities.add(toolActivity);
		activities.add(toolActivity);
		toolActivity = new ToolActivity();
		toolActivity.setActivityId(new Long(8));
		toolActivity.setTool(tool);
		toolActivity.setTitle("activity 8");
		subActivities.add(toolActivity);
		activities.add(toolActivity);
		toolActivity = new ToolActivity();
		toolActivity.setActivityId(new Long(9));
		toolActivity.setTool(tool);
		toolActivity.setTitle("activity 9");
		subActivities.add(toolActivity);
		activities.add(toolActivity);

		toolActivity = new ToolActivity();
		toolActivity.setActivityId(new Long(10));
		toolActivity.setTool(tool);
		activities.add(toolActivity);
		
		progress.setAttemptedActivities(attempted);
		progress.setCompletedActivities(complete);
		
		progress.setCurrentActivity(getActivity(1, progress));
		
		return progress;
	}	
	
}
