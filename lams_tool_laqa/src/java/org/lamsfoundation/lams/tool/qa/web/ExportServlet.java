/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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

package org.lamsfoundation.lams.tool.qa.web;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaQueUsr;
import org.lamsfoundation.lams.tool.qa.QaSession;
import org.lamsfoundation.lams.tool.qa.dto.GeneralLearnerFlowDTO;
import org.lamsfoundation.lams.tool.qa.dto.ReflectionDTO;
import org.lamsfoundation.lams.tool.qa.service.IQaService;
import org.lamsfoundation.lams.tool.qa.service.QaServiceProxy;
import org.lamsfoundation.lams.tool.qa.util.QaApplicationException;
import org.lamsfoundation.lams.tool.qa.util.QaBundler;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.servlet.AbstractExportPortfolioServlet;

/**
 * <p>
 * Enables exporting portfolio for teacher and learner modes.
 * </p>
 *
 * @author Ozgur Demirtas
 */
public class ExportServlet extends AbstractExportPortfolioServlet implements QaAppConstants {
    static Logger logger = Logger.getLogger(ExportServlet.class.getName());
    private static final long serialVersionUID = -1779093489007108143L;
    private final String FILENAME = "qa_main.html";

    @Override
    public String doExport(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies) {
	String basePath = WebUtil.getBaseServerURL() + request.getContextPath();

	if (StringUtils.equals(mode, ToolAccessMode.LEARNER.toString())) {
	    learner(request, response, directoryName, cookies);
	} else if (StringUtils.equals(mode, ToolAccessMode.TEACHER.toString())) {
	    teacher(request, response, directoryName, cookies);
	}

	// Attempting to export required images
	try {
	    QaBundler qaBundler = new QaBundler();
	    qaBundler.bundle(request, cookies, directoryName);

	} catch (Exception e) {
	    logger.error("Could not export Q&A javascript files, some files may be missing in export portfolio", e);
	}

	writeResponseToFile(basePath + "/export/exportportfolio.jsp", directoryName, FILENAME, cookies);

	return FILENAME;
    }

    public void learner(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies) {
	IQaService qaService = QaServiceProxy.getQaService(getServletContext());

	if (userID == null || toolSessionID == null) {
	    String error = "Tool session Id or user Id is null. Unable to continue";
	    logger.error(error);
	    throw new QaApplicationException(error);
	}

	QaSession qaSession = qaService.getSessionById(toolSessionID.longValue());

	// If the learner hasn't answered yet, then they won't exist in the session.
	// Yet we might be asked for their page, as the activity has been commenced.
	// So need to do a "blank" page in that case
	QaQueUsr learner = qaService.getUserByIdAndSession(userID, qaSession.getQaSessionId());
	QaContent content = qaSession.getQaContent();

	if (content == null) {
	    String error = "The content for this activity has not been defined yet.";
	    logger.error(error);
	    throw new QaApplicationException(error);
	}

	// if learner is null, don't want to show other people's answers
	if (learner != null) {
	    boolean isNoToolSessions = qaService.isStudentActivityOccurredGlobal(content);
	    request.getSession().setAttribute(QaAppConstants.IS_TOOL_SESSION_AVAILABLE, isNoToolSessions);

	    GeneralLearnerFlowDTO generalLearnerFlowDTO = LearningUtil.buildGeneralLearnerFlowDTO(qaService, content);
	    List listMonitoredAnswersContainerDTO = qaService.exportLearner(content, content.isUsernameVisible(), true,
		    toolSessionID.toString(), userID.toString());
	    generalLearnerFlowDTO.setListMonitoredAnswersContainerDTO(listMonitoredAnswersContainerDTO);
	    generalLearnerFlowDTO.setRequestLearningReportProgress(new Boolean(true).toString());
	    generalLearnerFlowDTO.setUserUid(userID.toString());
	    request.getSession().setAttribute(GENERAL_LEARNER_FLOW_DTO, generalLearnerFlowDTO);

	    if (content.isReflect()) {
		List<ReflectionDTO> reflectionDTOs = qaService.getReflectList(content, userID.toString());
		request.getSession().setAttribute(QaAppConstants.REFLECTIONS_CONTAINER_DTO, reflectionDTOs);
	    }
	}

	request.getSession().setAttribute(PORTFOLIO_EXPORT_MODE, "learner");
    }

    public void teacher(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies) {
	IQaService qaService = QaServiceProxy.getQaService(getServletContext());

	if (toolContentID == null) {
	    String error = "Tool Content Id is missing. Unable to continue";
	    logger.error(error);
	    throw new QaApplicationException(error);
	}

	QaContent content = qaService.getQaContent(toolContentID.longValue());

	if (content == null) {
	    String error = "Data is missing from the database. Unable to Continue";
	    logger.error(error);
	    throw new QaApplicationException(error);
	}

	boolean isToolSessionAvailable = qaService.isStudentActivityOccurredGlobal(content);
	request.getSession().setAttribute(QaAppConstants.IS_TOOL_SESSION_AVAILABLE, isToolSessionAvailable);

	GeneralLearnerFlowDTO generalLearnerFlowDTO = LearningUtil.buildGeneralLearnerFlowDTO(qaService, content);
	request.getSession().setAttribute(GENERAL_LEARNER_FLOW_DTO, generalLearnerFlowDTO);

	request.getSession().setAttribute(PORTFOLIO_EXPORT_MODE, "teacher");

	if (content.isReflect()) {
	    List<ReflectionDTO> reflectionDTOs = qaService.getReflectList(content, null);
	    request.getSession().setAttribute(QaAppConstants.REFLECTIONS_CONTAINER_DTO, reflectionDTOs);
	}

	// generateGroupsSessionData
	List listAllGroupsDTO = qaService.exportTeacher(content);
	request.setAttribute(LIST_ALL_GROUPS_DTO, listAllGroupsDTO);
	request.getSession().setAttribute(LIST_ALL_GROUPS_DTO, listAllGroupsDTO);
    }
}
