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


package org.lamsfoundation.lams.learning.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.learning.service.ICoreLearnerService;
import org.lamsfoundation.lams.learning.web.util.ActivityMapping;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.web.action.LamsAction;

/**
 * Action class to forward the user to a Tool.
 *
 * @author daveg
 *
 *         XDoclet definition:
 *
 *
 *
 */
public class DisplayToolActivityAction extends ActivityAction {

    /**
     * Gets a tool activity from the request (attribute) and uses a redirect to forward the user to the tool.
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	//ActivityForm form = (ActivityForm)actionForm;
	ActivityMapping actionMappings = LearningWebUtil.getActivityMapping(this.getServlet().getServletContext());

	ICoreLearnerService learnerService = getLearnerService();
	LearnerProgress learnerProgress = LearningWebUtil.getLearnerProgress(request, learnerService);
	Activity activity = LearningWebUtil.getActivityFromRequest(request, learnerService);

	if (!(activity instanceof ToolActivity)) {
	    LamsAction.log.error(LamsAction.className + ": activity not ToolActivity");
	    return mapping.findForward(ActivityMapping.ERROR);
	}

	ToolActivity toolActivity = (ToolActivity) activity;

	String url = actionMappings.getLearnerToolURL(learnerProgress.getLesson(), toolActivity,
		learnerProgress.getUser());
	try {
	    response.sendRedirect(url);
	} catch (java.io.IOException e) {
	    return mapping.findForward(ActivityMapping.ERROR);
	}
	return null;
    }
}