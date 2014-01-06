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

/* $Id$ */

package org.lamsfoundation.lams.learning.web.action;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.servlet.AbstractExportPortfolioServlet;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * Export portfolio page for Grouping. If the grouping has been done then the groups are shown, otherwise a "not done"
 * message is given. The screen is the same for teachers and learners.
 * 
 * @web:servlet name="groupingExportPortfolio"
 * @web:servlet-mapping url-pattern="/groupingExportPortfolio"
 * 
 */
public class GroupingExportPortfolioServlet extends AbstractExportPortfolioServlet {

    private static final long serialVersionUID = 677903773190753547L;

    // ---------------------------------------------------------------------
    // Class level constants - Session Attributes
    // ---------------------------------------------------------------------
    public static final String GROUPS = "groups";
    private final String FILENAME = "groups.html";

    @Override
    public String doExport(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies) {
	Long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	Long activityId = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);

	String basePath = WebUtil.getBaseServerURL() + request.getContextPath();
	writeResponseToFile(basePath + "/grouping.do?method=exportPortfolio&lessonID=" + lessonId + "&activityID="
		+ activityId, directoryName, FILENAME, cookies);
	return FILENAME;
    }
}