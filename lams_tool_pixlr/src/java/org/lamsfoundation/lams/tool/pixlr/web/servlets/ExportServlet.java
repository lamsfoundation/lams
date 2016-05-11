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



package org.lamsfoundation.lams.tool.pixlr.web.servlets;

import java.util.ArrayList;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learning.export.web.action.CustomToolImageBundler;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.pixlr.dto.PixlrDTO;
import org.lamsfoundation.lams.tool.pixlr.dto.PixlrSessionDTO;
import org.lamsfoundation.lams.tool.pixlr.dto.PixlrUserDTO;
import org.lamsfoundation.lams.tool.pixlr.model.Pixlr;
import org.lamsfoundation.lams.tool.pixlr.model.PixlrSession;
import org.lamsfoundation.lams.tool.pixlr.model.PixlrUser;
import org.lamsfoundation.lams.tool.pixlr.service.IPixlrService;
import org.lamsfoundation.lams.tool.pixlr.service.PixlrServiceProxy;
import org.lamsfoundation.lams.tool.pixlr.util.PixlrConstants;
import org.lamsfoundation.lams.tool.pixlr.util.PixlrException;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.servlet.AbstractExportPortfolioServlet;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

public class ExportServlet extends AbstractExportPortfolioServlet {

    private static final long serialVersionUID = -2829707715037631881L;

    private static Logger logger = Logger.getLogger(ExportServlet.class);

    private final String FILENAME = "pixlr_main.html";

    private IPixlrService pixlrService;

    @Override
    protected String doExport(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies) {

	if (pixlrService == null) {
	    pixlrService = PixlrServiceProxy.getPixlrService(getServletContext());
	}

	try {
	    if (StringUtils.equals(mode, ToolAccessMode.LEARNER.toString())) {
		request.getSession().setAttribute(AttributeNames.ATTR_MODE, ToolAccessMode.LEARNER);
		doLearnerExport(request, response, directoryName, cookies);
	    } else if (StringUtils.equals(mode, ToolAccessMode.TEACHER.toString())) {
		request.getSession().setAttribute(AttributeNames.ATTR_MODE, ToolAccessMode.TEACHER);
		doTeacherExport(request, response, directoryName, cookies);
	    }
	} catch (PixlrException e) {
	    logger.error("Cannot perform export for pixlr tool.");
	}

	String basePath = WebUtil.getBaseServerURL() + request.getContextPath();
	writeResponseToFile(basePath + "/pages/export/exportPortfolio.jsp", directoryName, FILENAME, cookies);

	return FILENAME;
    }

    private void doLearnerExport(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies) throws PixlrException {

	logger.debug("doExportLearner: toolContentID:" + toolSessionID);

	// check if toolContentID available
	if (toolSessionID == null) {
	    String error = "Tool Session ID is missing. Unable to continue";
	    logger.error(error);
	    throw new PixlrException(error);
	}

	PixlrSession pixlrSession = pixlrService.getSessionBySessionId(toolSessionID);

	Pixlr pixlr = pixlrSession.getPixlr();

	UserDTO lamsUserDTO = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);

	PixlrUser pixlrUser = pixlrService.getUserByUserIdAndSessionId(new Long(lamsUserDTO.getUserID()),
		toolSessionID);

	//NotebookEntry pixlrEntry = pixlrService.getEntry(pixlrUser.getEntryUID());

	// construct dto's
	PixlrDTO pixlrDTO = new PixlrDTO(pixlr);

	PixlrSessionDTO sessionDTO = new PixlrSessionDTO();
	sessionDTO.setSessionName(pixlrSession.getSessionName());
	sessionDTO.setSessionID(pixlrSession.getSessionId());

	// If the user hasn't put in their entry yet, pixlrEntry will be null;
	PixlrUserDTO userDTO = new PixlrUserDTO(pixlrUser);

	sessionDTO.getUserDTOs().add(userDTO);
	pixlrDTO.getSessionDTOs().add(sessionDTO);

	NotebookEntry notebookEntry = pixlrService.getEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		PixlrConstants.TOOL_SIGNATURE, userDTO.getUserId().intValue());

	if (notebookEntry != null) {
	    userDTO.setNotebookEntry(notebookEntry.getEntry());
	    userDTO.setFinishedReflection(true);
	}

	String imageFileArray[] = new String[2];
	imageFileArray[0] = pixlrDTO.getImageFileName();

	if (userDTO.getImageFileName() != null) {
	    imageFileArray[1] = userDTO.getImageFileName();
	}

	// bundling the images in export
	try {
	    CustomToolImageBundler imageBundler = new CustomToolImageBundler();
	    imageBundler.bundle(request, cookies, directoryName, PixlrConstants.LAMS_WWW_PIXLR_FOLDER_URL,
		    imageFileArray);
	} catch (Exception e) {
	    logger.error("Could not export gmap images, some images may be missing in export portfolio", e);
	}

	request.getSession().setAttribute("userDTO", userDTO);
	request.getSession().setAttribute("pixlrDTO", pixlrDTO);
    }

    private void doTeacherExport(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies) throws PixlrException {

	logger.debug("doExportTeacher: toolContentID:" + toolContentID);

	// check if toolContentID available
	if (toolContentID == null) {
	    String error = "Tool Content ID is missing. Unable to continue";
	    logger.error(error);
	    throw new PixlrException(error);
	}

	Pixlr pixlr = pixlrService.getPixlrByContentId(toolContentID);

	PixlrDTO pixlrDTO = new PixlrDTO(pixlr);

	// Creating a list of image files to copy for export
	ArrayList<String> imageFiles = new ArrayList<String>();
	imageFiles.add(pixlr.getImageFileName());

	for (PixlrSessionDTO sessionDTO : pixlrDTO.getSessionDTOs()) {
	    Long toolSessionID = sessionDTO.getSessionID();

	    for (PixlrUserDTO userDTO : sessionDTO.getUserDTOs()) {
		// get the notebook entry.
		NotebookEntry notebookEntry = pixlrService.getEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
			PixlrConstants.TOOL_SIGNATURE, userDTO.getUserId().intValue());
		if (notebookEntry != null) {
		    userDTO.notebookEntry = notebookEntry.getEntry();
		    userDTO.setFinishedReflection(true);
		    if (userDTO.getImageFileName() != null
			    && !userDTO.getImageFileName().equals(pixlrDTO.getImageFileName())) {
			imageFiles.add(userDTO.getImageFileName());
		    }
		}

	    }
	}

	String[] imageFileArray = new String[imageFiles.size()];
	int i = 0;
	for (String image : imageFiles) {
	    imageFileArray[i] = image;
	    i++;
	}

	// bundling the images in export
	try {
	    CustomToolImageBundler imageBundler = new CustomToolImageBundler();
	    imageBundler.bundle(request, cookies, directoryName, PixlrConstants.LAMS_WWW_PIXLR_FOLDER_URL,
		    imageFileArray);
	} catch (Exception e) {
	    logger.error("Could not export gmap images, some images may be missing in export portfolio", e);
	}

	request.getSession().setAttribute("pixlrDTO", pixlrDTO);
    }

}
