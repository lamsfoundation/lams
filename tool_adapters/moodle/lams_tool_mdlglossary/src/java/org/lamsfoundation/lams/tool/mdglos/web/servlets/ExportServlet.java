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

package org.lamsfoundation.lams.tool.mdglos.web.servlets;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.mdglos.dto.MdlGlossaryDTO;
import org.lamsfoundation.lams.tool.mdglos.model.MdlGlossary;
import org.lamsfoundation.lams.tool.mdglos.model.MdlGlossarySession;
import org.lamsfoundation.lams.tool.mdglos.service.IMdlGlossaryService;
import org.lamsfoundation.lams.tool.mdglos.service.MdlGlossaryServiceProxy;
import org.lamsfoundation.lams.tool.mdglos.util.MdlGlossaryException;
import org.lamsfoundation.lams.tool.mdglos.util.WebUtility;
import org.lamsfoundation.lams.web.servlet.AbstractExportPortfolioServlet;
import org.lamsfoundation.lams.web.util.AttributeNames;

public class ExportServlet extends AbstractExportPortfolioServlet {

    private static final long serialVersionUID = -2829707715037631881L;

    private static Logger logger = Logger.getLogger(ExportServlet.class);

    private final String FILENAME = "mdlGlossary_main.html";

    private IMdlGlossaryService mdlGlossaryService;

    protected String doExport(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies) {

	if (mdlGlossaryService == null) {
	    mdlGlossaryService = MdlGlossaryServiceProxy.getMdlGlossaryService(getServletContext());
	}

	try {
	    if (StringUtils.equals(mode, ToolAccessMode.LEARNER.toString())) {
		request.getSession().setAttribute(AttributeNames.ATTR_MODE, ToolAccessMode.LEARNER);
		doLearnerExport(request, response, directoryName, cookies);
	    } else if (StringUtils.equals(mode, ToolAccessMode.TEACHER.toString())) {
		request.getSession().setAttribute(AttributeNames.ATTR_MODE, ToolAccessMode.TEACHER);
		doTeacherExport(request, response, directoryName, cookies);
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath();
		writeResponseToFile(basePath + "/pages/export/exportPortfolio.jsp", directoryName, FILENAME, cookies);

	    }
	} catch (MdlGlossaryException e) {
	    logger.error("Cannot perform export for mdlGlossary tool.");
	}
	return FILENAME;
    }

    protected String doOfflineExport(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies) {
	if (toolContentID == null && toolSessionID == null) {
	    logger.error("Tool content Id or and session Id are null. Unable to activity title");
	} else {
	    if (mdlGlossaryService == null) {
		mdlGlossaryService = MdlGlossaryServiceProxy.getMdlGlossaryService(getServletContext());
	    }

	    MdlGlossary content = null;
	    if (toolContentID != null) {
		content = mdlGlossaryService.getMdlGlossaryByContentId(toolContentID);
	    } else {
		MdlGlossarySession session = mdlGlossaryService.getSessionBySessionId(toolSessionID);
		if (session != null)
		    content = session.getMdlGlossary();
	    }
	    if (content != null) {
		//activityTitle = content.getTitle();
	    }
	}
	return super.doOfflineExport(request, response, directoryName, cookies);
    }

    private void doLearnerExport(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies) throws MdlGlossaryException {

	logger.debug("doExportLearner: toolContentID:" + toolSessionID);

	// check if toolSessionID available
	if (toolSessionID == null) {
	    String error = "Tool Session ID is missing. Unable to continue";
	    logger.error(error);
	    throw new MdlGlossaryException(error);
	}

	MdlGlossarySession mdlGlossarySession = mdlGlossaryService.getSessionBySessionId(toolSessionID);

	MdlGlossary mdlGlossary = mdlGlossarySession.getMdlGlossary();

	try {
	    exportFileFromExternalServer(request, response, mdlGlossarySession.getExtSessionId(), mdlGlossary, directoryName
		    + "/" + FILENAME);
	} catch (Exception e) {
	    logger.error("Could not fetch export file from external servlet", e);
	    throw new MdlGlossaryException("Could not fetch export file from external servlet", e);
	}
    }

    private void doTeacherExport(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies) throws MdlGlossaryException {

	logger.debug("doExportTeacher: toolContentID:" + toolContentID);

	// check if toolContentID available
	if (toolContentID == null) {
	    String error = "Tool Content ID is missing. Unable to continue";
	    logger.error(error);
	    throw new MdlGlossaryException(error);
	}

	MdlGlossary mdlGlossary = mdlGlossaryService.getMdlGlossaryByContentId(toolContentID);
	MdlGlossaryDTO mdlGlossaryDTO = new MdlGlossaryDTO(mdlGlossary);
	request.getSession().setAttribute("mdlGlossaryDTO", mdlGlossaryDTO);

	Set<MdlGlossarySession> sessions = mdlGlossary.getMdlGlossarySessions();
	for (MdlGlossarySession session : sessions) {
	    try {
		String fullPath = directoryName + "/" + session.getSessionName();
		exportFileFromExternalServer(request, response, session.getExtSessionId(), mdlGlossary, fullPath);
	    } catch (Exception e) {
		logger.error("Could not fetch export file from external servlet", e);
		throw new MdlGlossaryException("Could not fetch export file from external servlet", e);
	    }
	}

	request.getSession().setAttribute("sessions", sessions);
    }

    private void exportFileFromExternalServer(HttpServletRequest request, HttpServletResponse response,
	    Long extToolSessionId, MdlGlossary mdlGlossary, String fullPath) throws Exception {
	String exportPortFolioServletUrl = mdlGlossaryService.getExtServerUrl(mdlGlossary.getExtLmsId());

	String extUsername = "user"; // setting user to arbitrary values since they are only used to construct the hash

	HashMap<String, String> params = mdlGlossaryService.getRequiredExtServletParams(mdlGlossary);
	params.put("method", IMdlGlossaryService.EXT_SERVER_METHOD_EXPORT_PORTFOLIO);
	params.put("extToolContentID", extToolSessionId.toString());

	InputStream in = WebUtility.getResponseInputStreamFromExternalServer(exportPortFolioServletUrl, params);
	OutputStream out = new BufferedOutputStream(new FileOutputStream(fullPath));
	byte[] buffer = new byte[1024];
	int numRead;
	long numWritten = 0;
	logger.debug("Getting file...");
	while ((numRead = in.read(buffer)) != -1) {
	    out.write(buffer, 0, numRead);
	    logger.debug(new String(buffer));
	    numWritten += numRead;
	}
	logger.debug("Path to mdlGlossary export portfolio content: " + fullPath);
	out.flush();
	out.close();
    }

}
