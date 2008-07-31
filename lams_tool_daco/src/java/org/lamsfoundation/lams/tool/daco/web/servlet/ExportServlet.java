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

package org.lamsfoundation.lams.tool.daco.web.servlet;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.daco.DacoConstants;
import org.lamsfoundation.lams.tool.daco.dto.Summary;
import org.lamsfoundation.lams.tool.daco.model.Daco;
import org.lamsfoundation.lams.tool.daco.model.DacoSession;
import org.lamsfoundation.lams.tool.daco.model.DacoUser;
import org.lamsfoundation.lams.tool.daco.service.DacoApplicationException;
import org.lamsfoundation.lams.tool.daco.service.DacoServiceProxy;
import org.lamsfoundation.lams.tool.daco.service.IDacoService;
import org.lamsfoundation.lams.tool.daco.util.DacoToolContentHandler;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.web.servlet.AbstractExportPortfolioServlet;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Export portfolio servlet to export all shared daco into offline HTML package.
 * 
 * @author Steve.Ni
 * 
 * @version $Revision$
 */
public class ExportServlet extends AbstractExportPortfolioServlet {
	private static final long serialVersionUID = -4529093489007108143L;

	private static Logger logger = Logger.getLogger(ExportServlet.class);

	private final String FILENAME = "shared_daco_main.html";

	private DacoToolContentHandler handler;

	@Override
	public String doExport(HttpServletRequest request, HttpServletResponse response, String directoryName, Cookie[] cookies) {

		// initial sessionMap
		SessionMap sessionMap = new SessionMap();
		request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);

		try {
			if (StringUtils.equals(mode, ToolAccessMode.LEARNER.toString())) {
				sessionMap.put(AttributeNames.ATTR_MODE, ToolAccessMode.LEARNER);
				learner(request, response, directoryName, cookies, sessionMap);
			}
			else if (StringUtils.equals(mode, ToolAccessMode.TEACHER.toString())) {
				sessionMap.put(AttributeNames.ATTR_MODE, ToolAccessMode.TEACHER);
				teacher(request, response, directoryName, cookies, sessionMap);
			}
		}
		catch (DacoApplicationException e) {
			ExportServlet.logger.error("Cannot perform export for daco tool.");
		}

		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath();
		writeResponseToFile(basePath + "/pages/export/exportportfolio.jsp?sessionMapID=" + sessionMap.getSessionID(),
				directoryName, FILENAME, cookies);

		return FILENAME;
	}

	@Override
	protected String doOfflineExport(HttpServletRequest request, HttpServletResponse response, String directoryName,
			Cookie[] cookies) {
		if (toolContentID == null && toolSessionID == null) {
			ExportServlet.logger.error("Tool content Id or and session Id are null. Unable to activity title");
		}
		else {
			IDacoService service = DacoServiceProxy.getDacoService(getServletContext());
			Daco content = null;
			if (toolContentID != null) {
				content = service.getDacoByContentId(toolContentID);
			}
			else {
				DacoSession session = service.getDacoSessionBySessionId(toolSessionID);
				if (session != null) {
					content = session.getDaco();
				}
			}
			if (content != null) {
				activityTitle = content.getTitle();
			}
		}
		return super.doOfflineExport(request, response, directoryName, cookies);
	}

	public void learner(HttpServletRequest request, HttpServletResponse response, String directoryName, Cookie[] cookies,
			HashMap sessionMap) throws DacoApplicationException {

		IDacoService service = DacoServiceProxy.getDacoService(getServletContext());

		if (userID == null || toolSessionID == null) {
			String error = "Tool session Id or user Id is null. Unable to continue";
			ExportServlet.logger.error(error);
			throw new DacoApplicationException(error);
		}

		DacoUser learner = service.getUserByIDAndSession(userID, toolSessionID);

		if (learner == null) {
			String error = "The user with user id " + userID + " does not exist.";
			ExportServlet.logger.error(error);
			throw new DacoApplicationException(error);
		}

		Daco content = service.getDacoBySessionId(toolSessionID);

		if (content == null) {
			String error = "The content for this activity has not been defined yet.";
			ExportServlet.logger.error(error);
			throw new DacoApplicationException(error);
		}

		List<Summary> group = service.exportBySessionId(toolSessionID, true);
		saveFileToLocal(group, directoryName);

		List<List> groupList = new ArrayList<List>();
		if (group.size() > 0) {
			groupList.add(group);
		}
		sessionMap.put(DacoConstants.ATTR_TITLE, content.getTitle());
		//sessionMap.put(DacoConstants.ATTR_SUMMARY_LIST, groupList);
	}

	public void teacher(HttpServletRequest request, HttpServletResponse response, String directoryName, Cookie[] cookies,
			HashMap sessionMap) throws DacoApplicationException {
		IDacoService service = DacoServiceProxy.getDacoService(getServletContext());

		// check if toolContentId exists in db or not
		if (toolContentID == null) {
			String error = "Tool Content Id is missing. Unable to continue";
			ExportServlet.logger.error(error);
			throw new DacoApplicationException(error);
		}

		Daco content = service.getDacoByContentId(toolContentID);

		if (content == null) {
			String error = "Data is missing from the database. Unable to Continue";
			ExportServlet.logger.error(error);
			throw new DacoApplicationException(error);
		}
		List<List<Summary>> groupList = service.exportByContentId(toolContentID);
		if (groupList != null) {
			for (List<Summary> list : groupList) {
				saveFileToLocal(list, directoryName);
			}
		}
		// put it into HTTPSession
		sessionMap.put(DacoConstants.ATTR_TITLE, content.getTitle());
		//sessionMap.put(DacoConstants.ATTR_SUMMARY_LIST, groupList);
	}

	private void saveFileToLocal(List<Summary> list, String directoryName) {
		handler = getToolContentHandler();
		for (Summary summary : list) {
			// for learning object, it just display "No offlice pakcage avaliable" information.
			if (summary.getQuestionType() == DacoConstants.QUESTION_TYPE_TEXTFIELD) {
				continue;
			}
			try {
				int index = 1;
				String userName = summary.getUsername();
				String localDir;
				while (true) {
					localDir = FileUtil.getFullPath(directoryName, userName + "/" + index);
					File local = new File(localDir);
					if (!local.exists()) {
						local.mkdirs();
						break;
					}
					index++;
				}
				// REMOVED!!
			}
			catch (Exception e) {
				ExportServlet.logger.error("Export forum topic attachment failed: " + e.toString());
			}
		}

	}

	private DacoToolContentHandler getToolContentHandler() {
		if (handler == null) {
			WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext());
			handler = (DacoToolContentHandler) wac.getBean(DacoConstants.TOOL_CONTENT_HANDLER_NAME);
		}
		return handler;
	}
}
