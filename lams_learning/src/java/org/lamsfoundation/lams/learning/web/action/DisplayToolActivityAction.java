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
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.learning.web.util.ActivityMapping;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.lesson.LearnerProgress;

/** 
 * Action class to forward the user to a Tool.
 * 
 * @author daveg
 * 
 * XDoclet definition:
 * 
 * @struts:action path="/DisplayToolActivity" name="activityForm"
 *                validate="false" scope="request"
 * 
 */
public class DisplayToolActivityAction extends ActivityAction {

	/**
	 * Gets a tool activity from the request (attribute) and uses a redirect
	 * to forward the user to the tool.
	 */
	public ActionForward execute(ActionMapping mapping,
	                             ActionForm actionForm,
	                             HttpServletRequest request,
	                             HttpServletResponse response) 
	{
		//ActivityForm form = (ActivityForm)actionForm;
		ActivityMapping actionMappings = getActivityMapping();
		
		LearnerProgress learnerProgress = getLearnerProgress(request);
		Activity activity = LearningWebUtil.getActivityFromRequest(request, getLearnerService());
		if (!(activity instanceof ToolActivity)) 
		{
		    log.error(className+": activity not ToolActivity");
			return mapping.findForward(ActivityMapping.ERROR);
		}
		
		ToolActivity toolActivity = (ToolActivity)activity;

		String url = actionMappings.getLearnerToolURL(learnerProgress.getLesson(), toolActivity, learnerProgress.getUser());
		try 
		{
		    response.sendRedirect(url);
		}
		catch (java.io.IOException e) 
		{
		    return mapping.findForward(ActivityMapping.ERROR);
		}
		return null;
	}

}