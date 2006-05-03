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
package org.lamsfoundation.lams.learning.web.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learning.service.LearnerServiceException;
import org.lamsfoundation.lams.learning.service.LearnerServiceProxy;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.learning.web.util.LessonLearnerDataManager;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.GateActivity;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;


/**
 * <p>The action servlet that deals with gate activity. This class allows the 
 * learner to knock gate when they reach the gate. The knocking process will
 * be triggered by the lams progress engine in the first place. The learner
 * can also trigger the knocking process by clicking on the button on the 
 * waiting page.</p>
 * 
 * <p>Learner will progress to the next activity if the gate is open. Otherwise,
 * the learner should see the waiting page.</p>
 * 
 * @author Jacky Fang
 * @since  2005-4-7
 * @version 1.1
 * 
 * ----------------XDoclet Tags--------------------
 * 
 * @struts:action path="/gate" 
 *                parameter="method" 
 *                validate="false"
 * @struts.action-exception key="error.system.learner" scope="request"
 *                          type="org.lamsfoundation.lams.learning.service.LearnerServiceException"
 *                          path=".systemError"
 * 							handler="org.lamsfoundation.lams.learning.util.CustomStrutsExceptionHandler"
 * 
 * @struts:action-forward name="waiting" path=".gateWaiting"
 * ----------------XDoclet Tags--------------------
 */
public class GateAction extends LamsDispatchAction
{

    //---------------------------------------------------------------------
    // Instance variables
    //---------------------------------------------------------------------
	private static Logger log = Logger.getLogger(GateAction.class);

    //---------------------------------------------------------------------
    // Class level constants - Struts forward
    //---------------------------------------------------------------------
    private static final String WAITING = "waiting";
	
    //---------------------------------------------------------------------
    // Struts Dispatch Method
    //---------------------------------------------------------------------    
	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward knockGate(ActionMapping mapping,
                                   ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response) throws IOException,
                                                                          ServletException
    {
        LearnerProgress learnerProgress = LearningWebUtil.getLearnerProgressByID(request,
                                                                                 getServlet().getServletContext());
        //validate pre-condition.
        validateLearnerProgress(learnerProgress);
        
        //initialize service object
        ILearnerService learnerService = LearnerServiceProxy.getLearnerService(getServlet().getServletContext());
        //get all learners in the lesson
        List currentLessonLearners = LessonLearnerDataManager.getAllLessonLearners(getServlet().getServletContext(),
                                                                                   learnerProgress.getLesson().getLessonId().longValue(),
                                                                                   learnerService);
        //knock the gate
        boolean gateOpen = learnerService.knockGate(learnerProgress.getNextActivity().getActivityId(),
                                                    learnerProgress.getUser(),
                                                    currentLessonLearners);
        //if the gate is open, let the learner go to the next activity ( updating the cached learner progress on the way )
        if(gateOpen)
        {
            String nextActivityUrl = learnerService.completeActivity(learnerProgress.getUser(),
                                                                     learnerProgress.getNextActivity().getActivityId(),
                                                                     learnerProgress.getLesson());
            // get the update
            LearningWebUtil.setLearnerProgress(learnerService.getProgressById(learnerProgress.getLearnerProgressId()));
            response.sendRedirect(nextActivityUrl);
            return null;
        }
        //if the gate is closed, ask the learner to wait ( updating the cached learner progress on the way )
        else {
            LearningWebUtil.setLearnerProgress(learnerService.getProgressById(learnerProgress.getLearnerProgressId()));
            return mapping.findForward(WAITING);
        }
    }
	
    //---------------------------------------------------------------------
    // Helper method
    //---------------------------------------------------------------------
    /**
     * @param learnerProgress
     */
    private void validateLearnerProgress(LearnerProgress learnerProgress)
    {
        if(learnerProgress ==null)
            throw new LearnerServiceException("Can't perform grouping without knowing" +
            		" the learner progress.");
        
        if(!isNextActivityValid(learnerProgress))
            throw new LearnerServiceException("Error in progress engine. Getting "
                                              +learnerProgress.getNextActivity().toString()
                                              +" where it should be gate activity");
    }

    /**
     * @param learnerProgress
     * @return
     */
    private boolean isNextActivityValid(LearnerProgress learnerProgress)
    {
        return learnerProgress.getNextActivity()!=null&&(learnerProgress.getNextActivity().isGateActivity());
    }	
}
