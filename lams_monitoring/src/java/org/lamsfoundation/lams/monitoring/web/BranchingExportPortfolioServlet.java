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



package org.lamsfoundation.lams.monitoring.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.servlet.AbstractExportPortfolioServlet;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * Export portfolio page for the branching. Only the teacher gets portfolio pages for gates - learners
 * do not see a page in their portfolio. Uses the normal branching "view" page, which lists which
 * learner is in which branch.
 *
 * @web:servlet name="branchingExportPortfolio"
 * @web:servlet-mapping url-pattern="/branchingExportPortfolio"
 * 
 */
public class BranchingExportPortfolioServlet extends AbstractExportPortfolioServlet {

    //private static final long serialVersionUID = -5262996309778140432L;

    //---------------------------------------------------------------------
    // Class level constants - Session Attributes
    //---------------------------------------------------------------------
    private final String FILENAME = "branching.html";

    @Override
    public String doExport(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies) {
	Long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	Long gateId = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);

	// should really be a different URL for each type of branching, but really it goes to the same method not matter what
	// so just use the simplest one!
	String basePath = WebUtil.getBaseServerURL() + request.getContextPath();
	writeResponseToFile(
		basePath + "/chosenBranching.do?method=exportPortfolio&lessonID=" + lessonId + "&activityID=" + gateId,
		directoryName, FILENAME, cookies);
	return FILENAME;
    }

}
