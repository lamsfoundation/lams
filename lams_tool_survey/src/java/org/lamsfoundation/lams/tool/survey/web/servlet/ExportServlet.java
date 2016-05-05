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

package org.lamsfoundation.lams.tool.survey.web.servlet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.survey.SurveyConstants;
import org.lamsfoundation.lams.tool.survey.dto.AnswerDTO;
import org.lamsfoundation.lams.tool.survey.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.survey.model.Survey;
import org.lamsfoundation.lams.tool.survey.model.SurveyQuestion;
import org.lamsfoundation.lams.tool.survey.model.SurveySession;
import org.lamsfoundation.lams.tool.survey.model.SurveyUser;
import org.lamsfoundation.lams.tool.survey.service.ISurveyService;
import org.lamsfoundation.lams.tool.survey.service.SurveyApplicationException;
import org.lamsfoundation.lams.tool.survey.service.SurveyServiceProxy;
import org.lamsfoundation.lams.tool.survey.util.ReflectDTOComparator;
import org.lamsfoundation.lams.util.WebUtil;
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

    private final String FILENAME = "survey_main.html";

    private ISurveyService service;

    @Override
    public void init() throws ServletException {
	service = SurveyServiceProxy.getSurveyService(getServletContext());
	super.init();
    }

    @Override
    public String doExport(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies) {

//		initial sessionMap
	SessionMap sessionMap = new SessionMap();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);

	try {
	    if (StringUtils.equals(mode, ToolAccessMode.LEARNER.toString())) {
		sessionMap.put(AttributeNames.ATTR_MODE, ToolAccessMode.LEARNER);
		learner(request, response, directoryName, cookies, sessionMap);
	    } else if (StringUtils.equals(mode, ToolAccessMode.TEACHER.toString())) {
		sessionMap.put(AttributeNames.ATTR_MODE, ToolAccessMode.TEACHER);
		teacher(request, response, directoryName, cookies, sessionMap);
	    }
	} catch (SurveyApplicationException e) {
	    logger.error("Cannot perform export for survey tool.");
	}

	String basePath = WebUtil.getBaseServerURL() + request.getContextPath();
	writeResponseToFile(basePath + "/pages/export/exportportfolio.jsp?sessionMapID=" + sessionMap.getSessionID(),
		directoryName, FILENAME, cookies);

	return FILENAME;
    }

    public void learner(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies, HashMap sessionMap) throws SurveyApplicationException {

	if (userID == null || toolSessionID == null) {
	    String error = "Tool session Id or user Id is null. Unable to continue";
	    logger.error(error);
	    throw new SurveyApplicationException(error);
	}

	SurveyUser learner = service.getUserByIDAndSession(userID, toolSessionID);

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

	// Add flag to indicate whether to render user notebook entries
	sessionMap.put(SurveyConstants.ATTR_REFLECTION_ON, content.isReflectOnActivity());

	// Create reflectList if reflection is enabled.
	if (content.isReflectOnActivity()) {
	    // Create reflectList, need to follow same structure used in teacher
	    // see surveyService.getReflectList();
	    Map<Long, Set<ReflectDTO>> map = new HashMap<Long, Set<ReflectDTO>>();
	    Set<ReflectDTO> reflectDTOSet = new TreeSet<ReflectDTO>(new ReflectDTOComparator());
	    reflectDTOSet.add(getReflectionEntry(learner));
	    map.put(toolSessionID, reflectDTOSet);

	    // Add reflectList to sessionMap
	    sessionMap.put(SurveyConstants.ATTR_REFLECT_LIST, map);
	}

	SortedMap<SurveySession, SortedMap<SurveyQuestion, List<AnswerDTO>>> groupList = service
		.exportLearnerPortfolio(learner);
	sessionMap.put(SurveyConstants.ATTR_TITLE, content.getTitle());
	sessionMap.put(SurveyConstants.ATTR_SUMMARY_LIST, groupList);
    }

    public void teacher(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies, HashMap sessionMap) throws SurveyApplicationException {

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
	SortedMap<SurveySession, SortedMap<SurveyQuestion, List<AnswerDTO>>> groupList = service
		.exportClassPortfolio(toolContentID);

	// Add flag to indicate whether to render user notebook entries
	sessionMap.put(SurveyConstants.ATTR_REFLECTION_ON, content.isReflectOnActivity());

	// Create reflectList if reflection is enabled.
	if (content.isReflectOnActivity()) {
	    Map<Long, Set<ReflectDTO>> reflectList = service.getReflectList(content.getContentId(), true);
	    // Add reflectList to sessionMap
	    sessionMap.put(SurveyConstants.ATTR_REFLECT_LIST, reflectList);
	}

	// put it into HTTPSession
	sessionMap.put(SurveyConstants.ATTR_TITLE, content.getTitle());
	sessionMap.put(SurveyConstants.ATTR_SUMMARY_LIST, groupList);
    }

    private ReflectDTO getReflectionEntry(SurveyUser surveyUser) {
	ReflectDTO reflectDTO = new ReflectDTO(surveyUser);
	NotebookEntry notebookEntry = service.getEntry(surveyUser.getSession().getSessionId(),
		CoreNotebookConstants.NOTEBOOK_TOOL, SurveyConstants.TOOL_SIGNATURE, surveyUser.getUserId().intValue());

	// check notebookEntry is not null
	if (notebookEntry != null) {
	    reflectDTO.setReflect(notebookEntry.getEntry());
	    logger.debug("Could not find notebookEntry for SurveyUser: " + surveyUser.getUid());
	}
	return reflectDTO;
    }

}
