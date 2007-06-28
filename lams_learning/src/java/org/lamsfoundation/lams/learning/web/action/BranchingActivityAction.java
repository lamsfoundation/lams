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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.lamsfoundation.lams.learning.service.ICoreLearnerService;
import org.lamsfoundation.lams.learning.service.LearnerServiceProxy;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.BranchingActivity;
import org.lamsfoundation.lams.learningdesign.SequenceActivity;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.learning.web.util.ActivityMapping;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;

/**
 * Action class to display an OptionsActivity.
 * 
 * @author daveg
 *
 * XDoclet definition:
 * 
 * @struts:action path="/branching" 
 * 				  name="BranchingForm"
 * 				  parameter="method" 
 *                validate="false"
 * 
 * @struts:action-forward name="displayBranchingWait" path=".branchingActivityWait"
 * 
 */
public class BranchingActivityAction extends LamsDispatchAction {
	
	public static final String CHOSEN_TYPE = "chosen";
	public static final String GROUP_BASED_TYPE = "group";
	public static final String TOOL_BASED_TYPE = "tool";

	private ICoreLearnerService learnerService = null;
	
	protected ICoreLearnerService getLearnerService() {
		if (learnerService == null) 
			learnerService = LearnerServiceProxy.getLearnerService(getServlet().getServletContext());
		return learnerService;
	}

	/**
	 * Gets an options activity from the request (attribute) and forwards to
	 * the display JSP.
	 */
	public ActionForward performBranching(
			ActionMapping mapping,
			ActionForm actionForm,
			HttpServletRequest request,
			HttpServletResponse response) {

		ActivityMapping actionMappings = LearningWebUtil.getActivityMapping(this.getServlet().getServletContext());	
		getLearnerService(); // initialise the learner service, if necessary

		LearnerProgress learnerProgress = LearningWebUtil.getLearnerProgress(request, learnerService);
		Activity activity = LearningWebUtil.getActivityFromRequest(request, learnerService);
		Integer learnerId = LearningWebUtil.getUserId();
		
		ActionForward forward = null;
		
		if (activity == null) {
			learnerProgress = learnerService.joinLesson(learnerId, learnerProgress.getLesson().getLessonId());
			forward = actionMappings.getActivityForward(activity, learnerProgress, true);
  		
		} else if (!(activity instanceof BranchingActivity)) {
		    log.error(className+": activity not BranchingActivity "+activity.getActivityId());
			forward = mapping.findForward(ActivityMapping.ERROR);
		
		} else {

			BranchingActivity branchingActivity = (BranchingActivity)activity;
			SequenceActivity branch = learnerService.determineBranch(learnerProgress.getLesson(), 
				branchingActivity, learnerId);

			if ( branch == null ) {
				// goto waiting page
		        DynaActionForm groupForm = (DynaActionForm)actionForm;
	        	groupForm.set("activityID", activity.getActivityId());
	        	groupForm.set("progressID", learnerProgress.getLearnerProgressId());
		        groupForm.set("previewLesson",learnerProgress.getLesson().isPreviewLesson()?Boolean.TRUE:Boolean.FALSE);
	        	groupForm.set("showFinishButton", Boolean.TRUE);
		        groupForm.set("title", activity.getTitle());
		        
		        if ( branchingActivity.isChosenBranchingActivity() ) {
		        	groupForm.set("type", CHOSEN_TYPE);
		        } else if ( branchingActivity.isGroupBranchingActivity() ) {
		        	groupForm.set("type", GROUP_BASED_TYPE);
		        } else if ( branchingActivity.isToolBranchingActivity() ) {
		        	groupForm.set("type", TOOL_BASED_TYPE);
		        }

				forward = mapping.findForward("displayBranchingWait");
			} else {
				// forward to the sequence activity.
				if ( log.isDebugEnabled() ) {
					log.debug("Branching: selecting the branch "+branch+" for user "+learnerId);
				}
				
				// Set the branch as the current part of the sequence and display it
				learnerProgress = learnerService.chooseActivity(learnerId, learnerProgress.getLesson().getLessonId(), branch);
				forward = actionMappings.getActivityForward(branch, learnerProgress, true);
			}
		}

		LearningWebUtil.putLearnerProgressInRequest(request,learnerProgress);
		return forward;
	}
	
}
