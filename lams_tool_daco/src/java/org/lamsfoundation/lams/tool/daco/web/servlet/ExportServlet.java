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



package org.lamsfoundation.lams.tool.daco.web.servlet;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.daco.DacoConstants;
import org.lamsfoundation.lams.tool.daco.dto.MonitoringSummarySessionDTO;
import org.lamsfoundation.lams.tool.daco.dto.MonitoringSummaryUserDTO;
import org.lamsfoundation.lams.tool.daco.dto.QuestionSummaryDTO;
import org.lamsfoundation.lams.tool.daco.model.Daco;
import org.lamsfoundation.lams.tool.daco.model.DacoAnswer;
import org.lamsfoundation.lams.tool.daco.model.DacoUser;
import org.lamsfoundation.lams.tool.daco.service.DacoApplicationException;
import org.lamsfoundation.lams.tool.daco.service.DacoServiceProxy;
import org.lamsfoundation.lams.tool.daco.service.IDacoService;
import org.lamsfoundation.lams.tool.daco.util.DacoToolContentHandler;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.servlet.AbstractExportPortfolioServlet;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Export portfolio servlet to export all Data Collection daco into offline HTML package.
 *
 * @author Marcin Cieslak
 *
 */
public class ExportServlet extends AbstractExportPortfolioServlet {

    private static Logger logger = Logger.getLogger(ExportServlet.class);

    private final String FILENAME = "daco_main.html";
    private DacoToolContentHandler handler;

    @Override
    public String doExport(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies) {
	handler = getToolContentHandler();
	SessionMap sessionMap = new SessionMap();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	String basePath = WebUtil.getBaseServerURL() + request.getContextPath();
	try {
	    if (StringUtils.equals(mode, ToolAccessMode.LEARNER.toString())) {
		learnerExport(sessionMap, directoryName);
	    } else if (StringUtils.equals(mode, ToolAccessMode.TEACHER.toString())) {
		File learnerDirectory = new File(new File(directoryName), "learners");
		learnerDirectory.mkdir();
		new File(learnerDirectory, "files").mkdir();
		teacherExport(sessionMap, basePath, learnerDirectory.getAbsolutePath(), cookies);
	    }
	} catch (DacoApplicationException e) {
	    ExportServlet.logger.error("Cannot perform export for daco tool.");
	}

	writeResponseToFile(basePath + "/includes/css/daco.css", directoryName, "daco.css", cookies);

	writeResponseToFile(basePath + "/pages/export/exportportfolio.jsp?sessionMapID=" + sessionMap.getSessionID(),
		directoryName, FILENAME, cookies);

	return FILENAME;
    }

    private void teacherExport(SessionMap sessionMap, String basePath, String learnerDirectory, Cookie[] cookies)
	    throws DacoApplicationException {
	if (toolContentID == null) {
	    String error = "Tool Content Id is missing. Unable to continue";
	    ExportServlet.logger.error(error);
	    throw new DacoApplicationException(error);
	}
	IDacoService service = DacoServiceProxy.getDacoService(getServletContext());
	Daco daco = service.getDacoByContentId(toolContentID);
	if (daco == null) {
	    String error = "Data is missing from the database. Unable to Continue";
	    ExportServlet.logger.error(error);
	    throw new DacoApplicationException(error);
	}
	List<MonitoringSummarySessionDTO> monitoringSummary = service.getExportPortfolioSummary(daco.getContentId(),
		DacoConstants.MONITORING_SUMMARY_MATCH_ALL);
	sessionMap.put(DacoConstants.ATTR_MONITORING_SUMMARY, monitoringSummary);
	sessionMap.put(AttributeNames.ATTR_MODE, ToolAccessMode.TEACHER);
	sessionMap.put(DacoConstants.ATTR_LEARNING_VIEW, DacoConstants.LEARNING_VIEW_VERTICAL);
	sessionMap.put(DacoConstants.ATTR_DACO, daco);

	boolean anyRecordsAvailable = false;
	for (MonitoringSummarySessionDTO session : monitoringSummary) {
	    for (MonitoringSummaryUserDTO user : session.getUsers()) {
		if (user.getRecordCount() > 0) {
		    anyRecordsAvailable = true;

		    List<QuestionSummaryDTO> summaries = service.getQuestionSummaries(user.getUid());
		    sessionMap.put(DacoConstants.ATTR_QUESTION_SUMMARIES, summaries);

		    Integer totalRecordCount = service.getGroupRecordCount(session);
		    sessionMap.put(DacoConstants.ATTR_TOTAL_RECORD_COUNT, totalRecordCount);

		    sessionMap.put(DacoConstants.ATTR_USER, user);

		    for (List<DacoAnswer> record : user.getRecords()) {
			for (DacoAnswer answer : record) {
			    if (answer.getFileUuid() != null) {
				String fileLocalPath = FileUtil.getFullPath("files",
					answer.getFileUuid() + "-" + answer.getFileName());
				try {
				    handler.saveFile(answer.getFileUuid(),
					    FileUtil.getFullPath(learnerDirectory, fileLocalPath));
				} catch (Exception e) {
				    ExportServlet.logger.error("File export failed: " + e.toString());
				}
			    }
			}
		    }

		    writeResponseToFile(
			    basePath + "/pages/export/listRecordsTemplate.jsp?sessionMapID="
				    + sessionMap.getSessionID(),
			    learnerDirectory, user.getUid() + "-records.html", cookies);
		    writeResponseToFile(
			    basePath + "/pages/monitoring/notebook.jsp?sessionMapID=" + sessionMap.getSessionID()
				    + "&includeMode=exportportfolio",
			    learnerDirectory, user.getUid() + "-reflection.html", cookies);

		}
	    }
	}
	if (anyRecordsAvailable) {
	    writeResponseToFile(basePath + "/pages/export/listRecords.jsp?sessionMapID=" + sessionMap.getSessionID()
		    + "&includeMode=exportportfolio", learnerDirectory, "allRecords.html", cookies);
	}
    }

    private void learnerExport(HashMap sessionMap, String directory) throws DacoApplicationException {
	IDacoService service = DacoServiceProxy.getDacoService(getServletContext());

	if (userID == null || toolSessionID == null) {
	    String error = "Tool session Id or user Id is null. Unable to continue";
	    ExportServlet.logger.error(error);
	    throw new DacoApplicationException(error);
	}
	DacoUser user = service.getUserByUserIdAndSessionId(userID, toolSessionID);

	if (user == null) {
	    String error = "The user with user id " + userID + " does not exist.";
	    ExportServlet.logger.error(error);
	    throw new DacoApplicationException(error);
	}

	Daco daco = user.getDaco();

	if (daco == null) {
	    String error = "The content for this activity has not been defined yet.";
	    ExportServlet.logger.error(error);
	    throw new DacoApplicationException(error);
	}

	List<QuestionSummaryDTO> summaries = service.getQuestionSummaries(user.getUid());
	sessionMap.put(DacoConstants.ATTR_QUESTION_SUMMARIES, summaries);

	Integer totalRecordCount = service.getGroupRecordCount(toolSessionID);
	sessionMap.put(DacoConstants.ATTR_TOTAL_RECORD_COUNT, totalRecordCount);

	sessionMap.put(DacoConstants.ATTR_DACO, daco);
	List<List<DacoAnswer>> records = service.getDacoAnswersByUser(user);
	new File(directory, "files").mkdir();

	for (List<DacoAnswer> record : records) {
	    for (DacoAnswer answer : record) {
		if (answer.getFileUuid() != null) {
		    String fileLocalPath = FileUtil.getFullPath("files",
			    answer.getFileUuid() + "-" + answer.getFileName());
		    try {
			handler.saveFile(answer.getFileUuid(), FileUtil.getFullPath(directory, fileLocalPath));
		    } catch (Exception e) {
			e.printStackTrace();
			ExportServlet.logger.error("File export failed: " + e.toString());
		    }
		}
	    }
	}

	String entryText = null;
	if (user != null) {
	    NotebookEntry notebookEntry = service.getEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		    DacoConstants.TOOL_SIGNATURE, user.getUserId().intValue());
	    if (notebookEntry != null) {
		entryText = notebookEntry.getEntry();
	    }
	}
	sessionMap.put(DacoConstants.ATTR_REFLECTION_ENTRY, entryText);
	sessionMap.put(DacoConstants.ATTR_RECORD_LIST, records);
	sessionMap.put(AttributeNames.ATTR_MODE, ToolAccessMode.LEARNER);
	sessionMap.put(DacoConstants.ATTR_LEARNING_VIEW, DacoConstants.LEARNING_VIEW_VERTICAL);
    }

    private DacoToolContentHandler getToolContentHandler() {
	if (handler == null) {
	    WebApplicationContext wac = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(this.getServletContext());
	    handler = (DacoToolContentHandler) wac.getBean(DacoConstants.TOOL_CONTENT_HANDLER_NAME);
	}
	return handler;
    }
}