/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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

package org.lamsfoundation.lams.tool.gmap.web.servlets;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.gmap.dto.GmapDTO;
import org.lamsfoundation.lams.tool.gmap.dto.GmapSessionDTO;
import org.lamsfoundation.lams.tool.gmap.dto.GmapUserDTO;
import org.lamsfoundation.lams.tool.gmap.model.Gmap;
import org.lamsfoundation.lams.tool.gmap.model.GmapConfigItem;
import org.lamsfoundation.lams.tool.gmap.model.GmapSession;
import org.lamsfoundation.lams.tool.gmap.model.GmapUser;
import org.lamsfoundation.lams.tool.gmap.service.IGmapService;
import org.lamsfoundation.lams.tool.gmap.service.GmapServiceProxy;
import org.lamsfoundation.lams.tool.gmap.util.GmapException;
import org.lamsfoundation.lams.tool.gmap.util.GmapConstants;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.servlet.AbstractExportPortfolioServlet;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.learning.export.web.action.CustomToolImageBundler;

public class ExportServlet extends AbstractExportPortfolioServlet {

    private static final long serialVersionUID = -2829707715037631881L;

    private static Logger logger = Logger.getLogger(ExportServlet.class);

    // list of images that need to be copied for export
    static String[] GMAP_IMAGES = { "blue_Marker.png", "paleblue_Marker.png", "red_Marker.png", "yellow_Marker.png",
	    "tree_closed.gif", "tree_open.gif" };

    private final String FILENAME = "gmap_main.html";

    private IGmapService gmapService;

    protected String doExport(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies) {

	if (gmapService == null) {
	    gmapService = GmapServiceProxy.getGmapService(getServletContext());
	}

	try {
	    if (StringUtils.equals(mode, ToolAccessMode.LEARNER.toString())) {
		request.getSession().setAttribute(AttributeNames.ATTR_MODE, ToolAccessMode.LEARNER);
		doLearnerExport(request, response, directoryName, cookies);
	    } else if (StringUtils.equals(mode, ToolAccessMode.TEACHER.toString())) {
		request.getSession().setAttribute(AttributeNames.ATTR_MODE, ToolAccessMode.TEACHER);
		doTeacherExport(request, response, directoryName, cookies);
	    }
	} catch (GmapException e) {
	    logger.error("Cannot perform export for gmap tool.");
	}

	// get the gmap API key from the config table and add it to the session
	GmapConfigItem gmapKey = gmapService.getConfigItem(GmapConfigItem.KEY_GMAP_KEY);
	if (gmapKey != null && gmapKey.getConfigValue() != null) {
	    request.getSession().setAttribute(GmapConstants.ATTR_GMAP_KEY, gmapKey.getConfigValue());
	}

	String basePath = WebUtil.getBaseServerURL()
		+ request.getContextPath();

	// Attempting to export required images
	try {
	    CustomToolImageBundler imageBundler = new CustomToolImageBundler();
	    imageBundler.bundle(request, cookies, directoryName, getImagesUrlDir(), GMAP_IMAGES);
	} catch (Exception e) {
	    logger.error("Could not export gmap images, some images may be missing in export portfolio", e);
	}

	writeResponseToFile(basePath + "/pages/export/exportPortfolio.jsp", directoryName, FILENAME, cookies);

	return FILENAME;
    }

    protected String doOfflineExport(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies) {
	if (toolContentID == null && toolSessionID == null) {
	    logger.error("Tool content Id or and session Id are null. Unable to activity title");
	} else {
	    if (gmapService == null) {
		gmapService = GmapServiceProxy.getGmapService(getServletContext());
	    }

	    Gmap content = null;
	    if (toolContentID != null) {
		content = gmapService.getGmapByContentId(toolContentID);
	    } else {
		GmapSession session = gmapService.getSessionBySessionId(toolSessionID);
		if (session != null)
		    content = session.getGmap();
	    }
	    if (content != null) {
		activityTitle = content.getTitle();
	    }
	}
	return super.doOfflineExport(request, response, directoryName, cookies);
    }

    private void doLearnerExport(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies) throws GmapException {

	logger.debug("doExportLearner: toolContentID:" + toolSessionID);

	// check if toolContentID available
	if (toolSessionID == null) {
	    String error = "Tool Session ID is missing. Unable to continue";
	    logger.error(error);
	    throw new GmapException(error);
	}

	GmapSession gmapSession = gmapService.getSessionBySessionId(toolSessionID);

	Gmap gmap = gmapSession.getGmap();

	UserDTO lamsUserDTO = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);

	GmapUser gmapUser = gmapService.getUserByUserIdAndSessionId(new Long(lamsUserDTO.getUserID()), toolSessionID);
	GmapUserDTO gmapUserDTO = new GmapUserDTO(gmapUser);

	// construct dto's
	GmapDTO gmapDTO = new GmapDTO(gmap);
	gmapDTO.setGmapMarkers(gmapService.getGmapMarkersBySessionId(toolSessionID));

	GmapSessionDTO sessionDTO = new GmapSessionDTO(gmapSession);
	// if reflectOnActivity is enabled add userDTO.
	if (gmap.isReflectOnActivity()) {

	    // get the entry.
	    NotebookEntry entry = gmapService.getEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		    GmapConstants.TOOL_SIGNATURE, gmapUser.getUserId().intValue());

	    if (entry != null) {
		gmapUserDTO.finishedReflection = true;
		gmapUserDTO.notebookEntry = entry.getEntry();
	    } else {
		gmapUserDTO.finishedReflection = false;
	    }
	    sessionDTO.getUserDTOs().add(gmapUserDTO);
	}

	request.getSession().setAttribute("gmapUserDTO", gmapUserDTO);
	request.getSession().setAttribute("sessionDTO", sessionDTO);
	gmapDTO.getSessionDTOs().add(sessionDTO);
	request.getSession().setAttribute("gmapDTO", gmapDTO);
    }

    private void doTeacherExport(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies) throws GmapException {

	logger.debug("doExportTeacher: toolContentID:" + toolContentID);

	// check if toolContentID available
	if (toolContentID == null) {
	    String error = "Tool Content ID is missing. Unable to continue";
	    logger.error(error);
	    throw new GmapException(error);
	}

	Gmap gmap = gmapService.getGmapByContentId(toolContentID);

	GmapDTO gmapDTO = new GmapDTO(gmap);

	for (GmapSessionDTO gmapSessionDTO : gmapDTO.getSessionDTOs()) {
	    gmapSessionDTO.setMarkerDTOs(gmapService.getGmapMarkersBySessionId(gmapSessionDTO.getSessionID()));

	    // if reflectOnActivity is enabled add all userDTO.
	    if (gmap.isReflectOnActivity()) {

		for (GmapUserDTO gmapUserDTO : gmapSessionDTO.getUserDTOs()) {
		    // get the entry.
		    NotebookEntry entry = gmapService.getEntry(gmapSessionDTO.getSessionID(),
			    CoreNotebookConstants.NOTEBOOK_TOOL, GmapConstants.TOOL_SIGNATURE, gmapUserDTO.getUserId()
				    .intValue());
		    if (entry != null) {
			gmapUserDTO.finishedReflection = true;
			gmapUserDTO.notebookEntry = entry.getEntry();
		    } else {
			gmapUserDTO.finishedReflection = false;
		    }
		    gmapSessionDTO.getUserDTOs().add(gmapUserDTO);
		}
	    }
	}

	request.getSession().setAttribute("gmapDTO", gmapDTO);
    }

    private String getImagesUrlDir() {
	String gmapUrlPath = Configuration.get(ConfigurationKeys.SERVER_URL);
	if (gmapUrlPath == null) {
	    logger
		    .error("Unable to get path to the LAMS Gmap URL from the configuration table. Gmap images export failed");
	    return "";
	} else {
	    gmapUrlPath = gmapUrlPath + "/tool/" + GmapConstants.TOOL_SIGNATURE + "/images/";
	    return gmapUrlPath;
	}
    }

}
