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

package org.lamsfoundation.lams.tool.assessment.web.servlet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.assessment.AssessmentConstants;
import org.lamsfoundation.lams.tool.assessment.dto.QuestionSummary;
import org.lamsfoundation.lams.tool.assessment.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.assessment.dto.SessionDTO;
import org.lamsfoundation.lams.tool.assessment.dto.UserSummary;
import org.lamsfoundation.lams.tool.assessment.model.Assessment;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentSession;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentUser;
import org.lamsfoundation.lams.tool.assessment.service.AssessmentApplicationException;
import org.lamsfoundation.lams.tool.assessment.service.AssessmentServiceProxy;
import org.lamsfoundation.lams.tool.assessment.service.IAssessmentService;
import org.lamsfoundation.lams.tool.assessment.util.AssessmentBundler;
import org.lamsfoundation.lams.tool.assessment.util.AssessmentToolContentHandler;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.servlet.AbstractExportPortfolioServlet;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 * Export portfolio servlet to export all assessment questions into offline HTML package.
 *
 * @author Steve.Ni
 */
public class ExportServlet extends AbstractExportPortfolioServlet {
    private static final long serialVersionUID = -4529093489007108143L;

    private static Logger logger = Logger.getLogger(ExportServlet.class);

    private final String FILENAME = "assessment_main.html";

    private AssessmentToolContentHandler handler;

    private IAssessmentService service;

    @Override
    public void init() throws ServletException {
	service = AssessmentServiceProxy.getAssessmentService(getServletContext());
	super.init();
    }

    @Override
    public String doExport(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies) {

	// initial sessionMap
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
	} catch (AssessmentApplicationException e) {
	    logger.error("Cannot perform export for assessment tool.");
	}

	String basePath = WebUtil.getBaseServerURL() + request.getContextPath();

	// Attempting to export required js and image files
	try {
	    AssessmentBundler imageBundler = new AssessmentBundler();
	    imageBundler.bundle(request, cookies, directoryName);
	} catch (Exception e) {
	    logger.error("Could not export spreadsheet javascript files, some files may be missing in export portfolio",
		    e);
	}

	writeResponseToFile(basePath + "/pages/export/exportportfolio.jsp?sessionMapID=" + sessionMap.getSessionID(),
		directoryName, FILENAME, cookies);

	return FILENAME;
    }

    public void learner(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies, HashMap sessionMap) throws AssessmentApplicationException {

	if (userID == null || toolSessionID == null) {
	    String error = "Tool session Id or user Id is null. Unable to continue";
	    logger.error(error);
	    throw new AssessmentApplicationException(error);
	}

	AssessmentUser learner = service.getUserByIDAndSession(userID, toolSessionID);

	if (learner == null) {
	    String error = "The user with user id " + userID + " does not exist.";
	    logger.error(error);
	    throw new AssessmentApplicationException(error);
	}

	Assessment content = service.getAssessmentBySessionId(toolSessionID);

	if (content == null) {
	    String error = "The content for this activity has not been defined yet.";
	    logger.error(error);
	    throw new AssessmentApplicationException(error);
	}

	AssessmentSession session = service.getAssessmentSessionBySessionId(toolSessionID);
	if (session == null) {
	    String error = "Failed get AssessmentSession by ID [" + toolSessionID + "]";
	    logger.error(error);
	    throw new AssessmentApplicationException(error);
	}

	UserSummary userSummary = service.getUserSummary(content.getContentId(), userID, toolSessionID);

	sessionMap.put(AssessmentConstants.ATTR_ASSESSMENT, content);
	sessionMap.put(AssessmentConstants.ATTR_USER_SUMMARY, userSummary);

	// Create reflectList if reflection is enabled.
	if (content.isReflectOnActivity()) {

	    // Add reflectList to sessionMap
	    sessionMap.put(AssessmentConstants.ATTR_REFLECTION_ENTRY, getReflectionEntry(learner));
	}

    }

    public void teacher(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies, HashMap sessionMap) throws AssessmentApplicationException {

	// check if toolContentId exists in db or not
	if (toolContentID == null) {
	    String error = "Tool Content Id is missing. Unable to continue";
	    logger.error(error);
	    throw new AssessmentApplicationException(error);
	}

	Assessment content = service.getAssessmentByContentId(toolContentID);

	if (content == null) {
	    String error = "Data is missing from the database. Unable to Continue";
	    logger.error(error);
	    throw new AssessmentApplicationException(error);
	}

	List<SessionDTO> summaryList = service.getSessionDataForExport(toolContentID);
	Map<Long, QuestionSummary> questionSummaries = service.getQuestionSummaryForExport(content);

	// cache into sessionMap
	sessionMap.put(AssessmentConstants.ATTR_ASSESSMENT, content);
	sessionMap.put(AssessmentConstants.ATTR_SUMMARY_LIST, summaryList);
	sessionMap.put(AssessmentConstants.ATTR_QUESTION_SUMMARY_LIST, questionSummaries.values());

	// Create reflectList if reflection is enabled.
	if (content.isReflectOnActivity()) {
	    List<ReflectDTO> reflectList = service.getReflectList(content.getContentId());
	    // Add reflectList to sessionMap
	    sessionMap.put(AssessmentConstants.ATTR_REFLECT_LIST, reflectList);
	}

    }

    private ReflectDTO getReflectionEntry(AssessmentUser user) {
	ReflectDTO reflectDTO = new ReflectDTO(user);
	NotebookEntry notebookEntry = service.getEntry(user.getSession().getSessionId(), user.getUserId().intValue());

	// check notebookEntry is not null
	if (notebookEntry != null) {
	    reflectDTO.setReflect(notebookEntry.getEntry());
	    logger.debug("Could not find notebookEntry for ResourceUser: " + user.getUid());
	}
	return reflectDTO;
    }

}
