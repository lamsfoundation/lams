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

package org.lamsfoundation.lams.tool.scratchie.web.servlet;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.scratchie.ScratchieConstants;
import org.lamsfoundation.lams.tool.scratchie.dto.GroupSummary;
import org.lamsfoundation.lams.tool.scratchie.model.Scratchie;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieItem;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieSession;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieUser;
import org.lamsfoundation.lams.tool.scratchie.service.IScratchieService;
import org.lamsfoundation.lams.tool.scratchie.service.ScratchieApplicationException;
import org.lamsfoundation.lams.tool.scratchie.service.ScratchieServiceProxy;
import org.lamsfoundation.lams.tool.scratchie.util.ScratchieBundler;
import org.lamsfoundation.lams.tool.scratchie.util.ScratchieItemComparator;
import org.lamsfoundation.lams.tool.scratchie.util.ScratchieToolContentHandler;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.servlet.AbstractExportPortfolioServlet;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 * Export portfolio servlet to export all scratchie into offline HTML package.
 * 
 * @author Andrey Balan
 */
public class ExportServlet extends AbstractExportPortfolioServlet {
    private static final long serialVersionUID = -4529093489007108143L;

    private static Logger logger = Logger.getLogger(ExportServlet.class);

    private final String FILENAME = "scratchie_main.html";

    private ScratchieToolContentHandler handler;

    private IScratchieService service;

    @Override
    public void init() throws ServletException {
	service = ScratchieServiceProxy.getScratchieService(getServletContext());
	super.init();
    }

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
	} catch (ScratchieApplicationException e) {
	    logger.error("Cannot perform export for scratchie tool.");
	}

	// Attempting to export required js and image files
	try {
	    ScratchieBundler imageBundler = new ScratchieBundler();
	    imageBundler.bundle(request, cookies, directoryName);
	} catch (Exception e) {
	    logger.error(
		    "Could not export spreadsheet javascript files, some files may be missing in export portfolio", e);
	}

	String basePath = WebUtil.getBaseServerURL()
		+ request.getContextPath();
	writeResponseToFile(basePath + "/pages/export/exportportfolio.jsp?sessionMapID=" + sessionMap.getSessionID(),
		directoryName, FILENAME, cookies);

	return FILENAME;
    }

    protected String doOfflineExport(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies) {
	if (toolContentID == null && toolSessionID == null) {
	    logger.error("Tool content Id or and session Id are null. Unable to activity title");
	} else {

	    Scratchie content = null;
	    if (toolContentID != null) {
		content = service.getScratchieByContentId(toolContentID);
	    } else {
		ScratchieSession session = service.getScratchieSessionBySessionId(toolSessionID);
		if (session != null)
		    content = session.getScratchie();
	    }
	    if (content != null) {
		activityTitle = content.getTitle();
	    }
	}
	return super.doOfflineExport(request, response, directoryName, cookies);
    }

    public void learner(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies, HashMap sessionMap) throws ScratchieApplicationException {

	if (userID == null || toolSessionID == null) {
	    String error = "Tool session Id or user Id is null. Unable to continue";
	    logger.error(error);
	    throw new ScratchieApplicationException(error);
	}

	ScratchieUser learner = service.getUserByIDAndSession(userID, toolSessionID);
	if (learner == null) {
	    String error = "The user with user id " + userID + " does not exist.";
	    logger.error(error);
	    throw new ScratchieApplicationException(error);
	}

	Scratchie content = service.getScratchieBySessionId(toolSessionID);
	if (content == null) {
	    String error = "The content for this activity has not been defined yet.";
	    logger.error(error);
	    throw new ScratchieApplicationException(error);
	}

	// set complete flag for display purpose
	Set<ScratchieItem> items = service.getItemsWithIndicatedScratches(toolSessionID);
	sessionMap.put(ScratchieConstants.ATTR_ITEM_LIST, items);

	// Add flag to indicate whether to render user notebook entries
	sessionMap.put(ScratchieConstants.ATTR_REFLECTION_ON, content.isReflectOnActivity());
	sessionMap.put(ScratchieConstants.ATTR_SCRATCHIE, content);
    }

    public void teacher(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies, HashMap sessionMap) throws ScratchieApplicationException {

	// check if toolContentId exists in db or not
	if (toolContentID == null) {
	    String error = "Tool Content Id is missing. Unable to continue";
	    logger.error(error);
	    throw new ScratchieApplicationException(error);
	}

	Scratchie content = service.getScratchieByContentId(toolContentID);

	if (content == null) {
	    String error = "Data is missing from the database. Unable to Continue";
	    logger.error(error);
	    throw new ScratchieApplicationException(error);
	}

	List<GroupSummary> summaryList = service.getMonitoringSummary(content.getContentId(), false);

	content.toDTO();

	// cache into sessionMap
	boolean isGroupedActivity = service.isGroupedActivity(toolContentID);
	sessionMap.put(ScratchieConstants.ATTR_IS_GROUPED_ACTIVITY, isGroupedActivity);
	sessionMap.put(ScratchieConstants.ATTR_SUMMARY_LIST, summaryList);
	sessionMap.put(ScratchieConstants.PAGE_EDITABLE, content.isContentInUse());
	sessionMap.put(ScratchieConstants.ATTR_SCRATCHIE, content);
	sessionMap.put(ScratchieConstants.ATTR_TOOL_CONTENT_ID, toolContentID);
    }
}
