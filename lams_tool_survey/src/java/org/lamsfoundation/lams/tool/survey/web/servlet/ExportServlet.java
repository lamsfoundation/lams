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

package org.lamsfoundation.lams.tool.survey.web.servlet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.survey.SurveyConstants;
import org.lamsfoundation.lams.tool.survey.dto.Summary;
import org.lamsfoundation.lams.tool.survey.model.Survey;
import org.lamsfoundation.lams.tool.survey.model.SurveyUser;
import org.lamsfoundation.lams.tool.survey.service.ISurveyService;
import org.lamsfoundation.lams.tool.survey.service.SurveyApplicationException;
import org.lamsfoundation.lams.tool.survey.service.SurveyServiceProxy;
import org.lamsfoundation.lams.web.servlet.AbstractExportPortfolioServlet;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 * Export portfolio servlet to export all shared survey into offline HTML
 * package.
 * 
 * @author Steve.Ni
 * 
 * @version $Revision$
 */
public class ExportServlet extends AbstractExportPortfolioServlet {
	private static final long serialVersionUID = -4529093489007108143L;

	private static Logger logger = Logger.getLogger(ExportServlet.class);

	private final String FILENAME = "shared_surveys_main.html";

	public String doExport(HttpServletRequest request, HttpServletResponse response, String directoryName, Cookie[] cookies) {

//		initial sessionMap
		SessionMap sessionMap = new SessionMap();
		request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
		
		try {
			if (StringUtils.equals(mode, ToolAccessMode.LEARNER.toString())) {
				sessionMap.put(AttributeNames.ATTR_MODE,ToolAccessMode.LEARNER);
				learner(request, response, directoryName, cookies,sessionMap);
			} else if (StringUtils.equals(mode, ToolAccessMode.TEACHER.toString())) {
				sessionMap.put(AttributeNames.ATTR_MODE,ToolAccessMode.TEACHER);
				teacher(request, response, directoryName, cookies,sessionMap);
			}
		} catch (SurveyApplicationException e) {
			logger.error("Cannot perform export for share survey tool.");
		}

		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath();
		writeResponseToFile(basePath + "/pages/export/exportportfolio.jsp?sessionMapID="+sessionMap.getSessionID()
				, directoryName, FILENAME, cookies);

		return FILENAME;
	}

	public void learner(HttpServletRequest request, HttpServletResponse response, String directoryName, Cookie[] cookies, HashMap sessionMap)
			throws SurveyApplicationException {

		ISurveyService service = SurveyServiceProxy.getSurveyService(getServletContext());

		if (userID == null || toolSessionID == null) {
			String error = "Tool session Id or user Id is null. Unable to continue";
			logger.error(error);
			throw new SurveyApplicationException(error);
		}

		SurveyUser learner = service.getUserByIDAndSession(userID,toolSessionID);

		if (learner == null) {
			String error = "The user with user id " + userID + " does not exist.";
			logger.error(error);
			throw new SurveyApplicationException(error);
		}

		Survey content = service.getSurveyBySessionId(toolSessionID);

		if (content == null) {
			String error = "The content for this activity has not been defined yet.";
			logger.error(error);
			throw new SurveyApplicationException(error);
		}
		
		
		List<Summary> group = service.exportBySessionId(toolSessionID,true);
		
		List<List> groupList = new ArrayList<List>();
		if(group.size() > 0)
			groupList.add(group);
		sessionMap.put(SurveyConstants.ATTR_TITLE, content.getTitle());
		sessionMap.put(SurveyConstants.ATTR_SUMMARY_LIST, groupList);
	}

	public void teacher(HttpServletRequest request, HttpServletResponse response, String directoryName, Cookie[] cookies, HashMap sessionMap)
			throws SurveyApplicationException {
		ISurveyService service = SurveyServiceProxy.getSurveyService(getServletContext());

		// check if toolContentId exists in db or not
		if (toolContentID == null) {
			String error = "Tool Content Id is missing. Unable to continue";
			logger.error(error);
			throw new SurveyApplicationException(error);
		}

		Survey content = service.getSurveyByContentId(toolContentID);

		if (content == null) {
			String error = "Data is missing from the database. Unable to Continue";
			logger.error(error);
			throw new SurveyApplicationException(error);
		}
		List<List<Summary>> groupList = service.exportByContentId(toolContentID);
		
		// put it into HTTPSession
		sessionMap.put(SurveyConstants.ATTR_TITLE, content.getTitle());
		sessionMap.put(SurveyConstants.ATTR_SUMMARY_LIST, groupList);
	}

}
