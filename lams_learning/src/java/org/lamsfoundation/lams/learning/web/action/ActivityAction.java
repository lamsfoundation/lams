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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $$Id$$ */	
package org.lamsfoundation.lams.learning.web.action;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.learning.progress.ProgressException;
import org.lamsfoundation.lams.learning.service.ICoreLearnerService;
import org.lamsfoundation.lams.learning.service.LearnerServiceException;
import org.lamsfoundation.lams.learning.service.LearnerServiceProxy;
import org.lamsfoundation.lams.learning.web.form.ActivityForm;
import org.lamsfoundation.lams.learning.web.util.ActivityMapping;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsAction;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/** 
 * Base class for all activity action classes. Each subclass should call 
 * super.execute() to set up the progress data in the ActivityForm.
 */
public abstract class ActivityAction extends LamsAction {
	
	public static final String ACTIVITY_REQUEST_ATTRIBUTE = "activity";
	public static final String LEARNER_PROGRESS_REQUEST_ATTRIBUTE = "learnerprogress";

	private ICoreLearnerService learnerService = null;
	
	protected ICoreLearnerService getLearnerService() {
		if (learnerService == null) 
			learnerService = LearnerServiceProxy.getLearnerService(getServlet().getServletContext());
		return learnerService;
	}
	
	/** Setup the progress string and the lesson id in the actionForm */
	public ActionForward setupProgressString(ActionForm actionForm, HttpServletRequest request)  {
		
		LearnerProgress learnerProgress = LearningWebUtil.getLearnerProgress(request,getLearnerService());		

		ActivityForm activityForm = (ActivityForm) actionForm; 
		
		// Calculate the progress summary. On join this method gets called twice, and we
		// only want to calculate once
		String progressSummary = activityForm.getProgressSummary();
		if ( progressSummary == null ) {
			progressSummary = getProgressSummary(learnerProgress);
			activityForm.setProgressSummary(progressSummary);
		} 
		
		Lesson currentLesson = learnerProgress.getLesson();
		if(currentLesson != null){
			activityForm.setLessonID(currentLesson.getLessonId());
			
			LearningDesign currentDesign = currentLesson.getLearningDesign();
			if(currentDesign != null)
				activityForm.setVersion(currentDesign.getDesignVersion());
		}
		
		
		if(log.isDebugEnabled())
		    log.debug("Entering activity: progress summary is "+activityForm.getProgressSummary());
		
		return null;
	}
	       	
	/**
	 * Get the ActionMappings.
	 */
	protected ActivityMapping getActivityMapping() {
        WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServlet().getServletContext());
        return (ActivityMapping)wac.getBean("activityMapping");
	}
	
	private String getProgressSummary(LearnerProgress learnerProgress) {
		StringBuffer progressSummary = new StringBuffer(100);
		if ( learnerProgress == null  ) {
			progressSummary.append("attempted=&completed=&current=");
			progressSummary.append("&lessonID=");
			Lesson currentLesson = learnerProgress.getLesson();
			if(currentLesson != null){
				progressSummary.append(currentLesson.getLessonId());
			}
		} else {
			progressSummary.append("attempted=");
			boolean first = true;
			for (Object obj : learnerProgress.getAttemptedActivities()) {
				Activity activity = (Activity ) obj;
				if ( ! first ) {
					progressSummary.append("_");
				} else {
					first = false;
				}
				progressSummary.append(activity.getActivityId());
			}
			
			progressSummary.append("&completed=");
			first = true;
			for ( Object obj : learnerProgress.getCompletedActivities() ) {
				Activity activity = (Activity ) obj;
				if ( ! first ) {
					progressSummary.append("_");
				} else {
					first = false;
				}
				progressSummary.append(activity.getActivityId());
			}

			progressSummary.append("&current=");
			Activity currentActivity = learnerProgress.getCurrentActivity();
			if ( currentActivity != null ) {
				progressSummary.append(currentActivity.getActivityId());
			}
			
		}
		return progressSummary.toString();
	}

	
}