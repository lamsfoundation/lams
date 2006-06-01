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

import javax.servlet.http.HttpServletRequest;

import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learning.service.LearnerServiceProxy;
import org.lamsfoundation.lams.learning.web.util.ActivityMapping;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.web.action.LamsAction;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/** 
 * MyEclipse Struts
 * Creation date: 01-12-2005
 * 
 */
public class ActivityAction extends LamsAction {
	
	public static final String ACTIVITY_REQUEST_ATTRIBUTE = "activity";
	public static final String LEARNER_PROGRESS_REQUEST_ATTRIBUTE = "learnerprogress";
	
	/**
	 * Get the learner service.
	 */
	protected ILearnerService getLearnerService() {
		ILearnerService learnerService = LearnerServiceProxy.getLearnerService(this.getServlet().getServletContext());
		return learnerService;
	}
	
	/**
	 * Get the ActionMappings.
	 */
	protected ActivityMapping getActivityMapping() {
        WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServlet().getServletContext());
        return (ActivityMapping)wac.getBean("activityMapping");
	}
	
	/** 
	 * Get the current learner progress. The request attributes are checked
	 * first, if not in request then a new LearnerProgress is loaded using
	 * the LearnerService. The LearnerProgress is also stored in the
	 * session so that the Flash requests don't have to reload it.
	 */
	protected LearnerProgress getLearnerProgress(HttpServletRequest request) {
		
	    return LearningWebUtil.getLearnerProgressByID(request,this.getServlet().getServletContext());
	}
	
	/**
	 * Sets the LearnerProgress in session so that the Flash requests don't
	 * have to reload it.
	 */
	protected void setLearnerProgress(HttpServletRequest request, LearnerProgress learnerProgress) {
		request.setAttribute(ActivityAction.LEARNER_PROGRESS_REQUEST_ATTRIBUTE, learnerProgress);
	}
	
}