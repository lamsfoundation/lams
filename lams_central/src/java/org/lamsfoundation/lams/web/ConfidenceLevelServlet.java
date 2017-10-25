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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */


package org.lamsfoundation.lams.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.confidencelevel.service.IConfidenceLevelService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Stores rating.
 *
 * @author Andrey Balan
 */
public class ConfidenceLevelServlet extends HttpServlet {

    private static Logger log = Logger.getLogger(ConfidenceLevelServlet.class);
    private static IConfidenceLevelService confidenceLevelService;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	getConfidenceLevelService();

	Long questionUid = WebUtil.readLongParam(request, "questionUid");
	Long toolSessionId = WebUtil.readLongParam(request, "toolSessionId");

	UserDTO user = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);
	Integer userId = user.getUserID();
	int confidenceLevel = WebUtil.readIntParam(request, "confidenceLevel");

	confidenceLevelService.saveConfidenceLevel(toolSessionId, userId, questionUid, confidenceLevel);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	doGet(request, response);
    }

    private IConfidenceLevelService getConfidenceLevelService() {
	if (confidenceLevelService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServletContext());
	    confidenceLevelService = (IConfidenceLevelService) ctx.getBean("confidenceLevelService");
	}
	return confidenceLevelService;
    }
}
