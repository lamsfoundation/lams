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

package org.lamsfoundation.lams.tool.dlfrum.web.servlets;

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
import org.lamsfoundation.lams.tool.dlfrum.dto.DotLRNForumDTO;
import org.lamsfoundation.lams.tool.dlfrum.model.DotLRNForum;
import org.lamsfoundation.lams.tool.dlfrum.model.DotLRNForumConfigItem;
import org.lamsfoundation.lams.tool.dlfrum.model.DotLRNForumSession;
import org.lamsfoundation.lams.tool.dlfrum.service.DotLRNForumServiceProxy;
import org.lamsfoundation.lams.tool.dlfrum.service.IDotLRNForumService;
import org.lamsfoundation.lams.tool.dlfrum.util.DotLRNForumException;
import org.lamsfoundation.lams.tool.dlfrum.util.WebUtility;
import org.lamsfoundation.lams.web.servlet.AbstractExportPortfolioServlet;
import org.lamsfoundation.lams.web.util.AttributeNames;

public class ExportServlet extends AbstractExportPortfolioServlet {

	private static final long serialVersionUID = -2829707715037631881L;

	private static Logger logger = Logger.getLogger(ExportServlet.class);

	private final String FILENAME = "dotLRNForum_main.html";

	private IDotLRNForumService dotLRNForumService;

	protected String doExport(HttpServletRequest request,
			HttpServletResponse response, String directoryName, Cookie[] cookies) {

		if (dotLRNForumService == null) {
			dotLRNForumService = DotLRNForumServiceProxy.getDotLRNForumService(getServletContext());
		}

		try {
			if (StringUtils.equals(mode, ToolAccessMode.LEARNER.toString())) {
				request.getSession().setAttribute(AttributeNames.ATTR_MODE,
						ToolAccessMode.LEARNER);
				doLearnerExport(request, response, directoryName, cookies);
			} else if (StringUtils.equals(mode, ToolAccessMode.TEACHER
					.toString())) {
				request.getSession().setAttribute(AttributeNames.ATTR_MODE,
						ToolAccessMode.TEACHER);
				doTeacherExport(request, response, directoryName, cookies);
				String basePath = request.getScheme() + "://" + request.getServerName()
						+ ":" + request.getServerPort() + request.getContextPath();
				writeResponseToFile(basePath + "/pages/export/exportPortfolio.jsp",
						directoryName, FILENAME, cookies);
		
				
			}
		} catch (DotLRNForumException e) {
			logger.error("Cannot perform export for dotLRNForum tool.");
		}
		return FILENAME;
	}

	protected String doOfflineExport(HttpServletRequest request, HttpServletResponse response, String directoryName, Cookie[] cookies) {
        if (toolContentID == null && toolSessionID == null) {
            logger.error("Tool content Id or and session Id are null. Unable to activity title");
        } else {
    		if (dotLRNForumService == null) {
    			dotLRNForumService = DotLRNForumServiceProxy.getDotLRNForumService(getServletContext());
    		}

        	DotLRNForum content = null;
            if ( toolContentID != null ) {
            	content = dotLRNForumService.getDotLRNForumByContentId(toolContentID);
            } else {
            	DotLRNForumSession session=dotLRNForumService.getSessionBySessionId(toolSessionID);
            	if ( session != null )
            		content = session.getDotLRNForum();
            }
            if ( content != null ) {
            	//activityTitle = content.getTitle();
            }
        }
        return super.doOfflineExport(request, response, directoryName, cookies);
	}
	
	private void doLearnerExport(HttpServletRequest request,
			HttpServletResponse response, String directoryName, Cookie[] cookies)
			throws DotLRNForumException {

		logger.debug("doExportLearner: toolContentID:" + toolSessionID);

		// check if toolSessionID available
		if (toolSessionID == null) {
			String error = "Tool Session ID is missing. Unable to continue";
			logger.error(error);
			throw new DotLRNForumException(error);
		}

		DotLRNForumSession dotLRNForumSession = dotLRNForumService.getSessionBySessionId(toolSessionID);

		DotLRNForum dotLRNForum = dotLRNForumSession.getDotLRNForum();

		try{
			exportFileFromExternalServer(request, response, 
				dotLRNForumSession.getExtSessionId(), dotLRNForum.getExtCourseId(), 
				directoryName + "/" + FILENAME);
		}
		catch (Exception e)
		{
			logger.error("Could not fetch export file from external servlet", e);
			throw new DotLRNForumException("Could not fetch export file from external servlet", e);
		}
	}

	private void doTeacherExport(HttpServletRequest request,
			HttpServletResponse response, String directoryName, Cookie[] cookies)
			throws DotLRNForumException {

		logger.debug("doExportTeacher: toolContentID:" + toolContentID);

		// check if toolContentID available
		if (toolContentID == null) {
			String error = "Tool Content ID is missing. Unable to continue";
			logger.error(error);
			throw new DotLRNForumException(error);
		}

		DotLRNForum dotLRNForum = dotLRNForumService.getDotLRNForumByContentId(toolContentID);
		DotLRNForumDTO dotLRNForumDTO = new DotLRNForumDTO(dotLRNForum);
		request.getSession().setAttribute("dotLRNForumDTO", dotLRNForumDTO);
		
		Set <DotLRNForumSession> sessions = dotLRNForum.getDotLRNForumSessions();
		for (DotLRNForumSession session : sessions)
		{
			try {
				String fullPath = directoryName + "/"+ session.getSessionName();
				exportFileFromExternalServer(request, response, session.getExtSessionId(), dotLRNForum.getExtCourseId(),fullPath);
			} catch (Exception e) {
				logger.error("Could not fetch export file from external servlet", e);
				throw new DotLRNForumException("Could not fetch export file from external servlet", e);
			}
		}
		
		request.getSession().setAttribute("sessions", sessions);
	}
	
	private void exportFileFromExternalServer(HttpServletRequest request,
			HttpServletResponse response, Long extToolSessionId, String extCourseId, String fullPath) throws Exception
	{
		String exportPortFolioServletUrl = dotLRNForumService.getConfigItem(DotLRNForumConfigItem.KEY_EXTERNAL_TOOL_SERVER).getConfigValue();
		
		String extUsername="authUser"; // setting user to arbitrary values since they are only used to construct the hash

		HashMap<String, String> params = dotLRNForumService.getRequiredExtServletParams(extUsername, extCourseId);
		params.put("method", IDotLRNForumService.EXT_SERVER_METHOD_EXPORT_PORTFOLIO);
		params.put("extToolContentID", extToolSessionId.toString());

		InputStream in = WebUtility.getResponseInputStreamFromExternalServer(exportPortFolioServletUrl, params);
		OutputStream out = new BufferedOutputStream(new FileOutputStream(fullPath));
		byte[] buffer = new byte[1024];
		int numRead;
		long numWritten = 0;
		logger.debug("Getting file...");
		while ((numRead = in.read(buffer)) != -1) 
		{
			out.write(buffer, 0, numRead);
			logger.debug(new String(buffer));
			numWritten += numRead;
		}
		logger.debug("Path to dotLRNForum export portfolio content: " + fullPath);
		out.flush();
		out.close();
	}
	

}
