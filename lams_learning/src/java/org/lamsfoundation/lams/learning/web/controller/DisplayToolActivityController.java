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

package org.lamsfoundation.lams.learning.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learning.service.ILearnerFullService;
import org.lamsfoundation.lams.learning.web.util.ActivityMapping;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Action class to forward the user to a Tool.
 *
 * @author daveg
 */
@Controller
public class DisplayToolActivityController {
    private static Logger log = Logger.getLogger(DisplayToolActivityController.class);
    
    @Autowired
    private ILearnerFullService learnerService;
    @Autowired
    private ActivityMapping activityMapping;

    /**
     * Gets a tool activity from the request (attribute) and uses a redirect to forward the user to the tool.
     */
    @RequestMapping("/DisplayToolActivity")
    public String execute(HttpServletRequest request, HttpServletResponse response) {
	LearnerProgress learnerProgress = LearningWebUtil.getLearnerProgress(request, learnerService);
	Activity activity = LearningWebUtil.getActivityFromRequest(request, learnerService);

	if (!(activity instanceof ToolActivity)) {
	    log.error("activity not ToolActivity");
	    return "error";
	}

	ToolActivity toolActivity = (ToolActivity) activity;

	String url = activityMapping.getLearnerToolURL(learnerProgress.getLesson(), toolActivity,
		learnerProgress.getUser());
	try {
	    response.sendRedirect(url);
	} catch (java.io.IOException e) {
	    return "error";
	}
	return null;
    }
}