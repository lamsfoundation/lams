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



package org.lamsfoundation.lams.tool.imageGallery.web.servlet;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import org.lamsfoundation.lams.tool.imageGallery.ImageGalleryConstants;
import org.lamsfoundation.lams.tool.imageGallery.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.imageGallery.dto.UserImageContributionDTO;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGallery;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGalleryItem;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGalleryUser;
import org.lamsfoundation.lams.tool.imageGallery.service.IImageGalleryService;
import org.lamsfoundation.lams.tool.imageGallery.service.ImageGalleryException;
import org.lamsfoundation.lams.tool.imageGallery.service.ImageGalleryServiceProxy;
import org.lamsfoundation.lams.tool.imageGallery.util.ImageGalleryBundler;
import org.lamsfoundation.lams.tool.imageGallery.util.ImageGalleryToolContentHandler;
import org.lamsfoundation.lams.tool.imageGallery.util.ReflectDTOComparator;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.servlet.AbstractExportPortfolioServlet;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Export portfolio servlet to export all images into offline HTML package.
 *
 * @author Andrey Balan
 *
 * @version $Revision$
 */
public class ExportServlet extends AbstractExportPortfolioServlet {
    private static final long serialVersionUID = -4529093489007108143L;

    private static Logger logger = Logger.getLogger(ExportServlet.class);

    private final String FILENAME = "imageGallery_main.html";

    private ImageGalleryToolContentHandler handler;

    private IImageGalleryService service;

    @Override
    public void init() throws ServletException {
	service = ImageGalleryServiceProxy.getImageGalleryService(getServletContext());
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
	} catch (ImageGalleryException e) {
	    logger.error("Cannot perform export for imageGallery tool.");
	}

	String basePath = WebUtil.getBaseServerURL() + request.getContextPath();

	// Attempting to export required images
	try {
	    ImageGalleryBundler imageBundler = new ImageGalleryBundler();
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
	    Cookie[] cookies, HashMap sessionMap) throws ImageGalleryException {

	if (userID == null || toolSessionID == null) {
	    String error = "Tool session Id or user Id is null. Unable to continue";
	    logger.error(error);
	    throw new ImageGalleryException(error);
	}

	ImageGalleryUser learner = service.getUserByIDAndSession(userID, toolSessionID);

	if (learner == null) {
	    String error = "The user with user id " + userID + " does not exist.";
	    logger.error(error);
	    throw new ImageGalleryException(error);
	}

	ImageGallery content = service.getImageGalleryBySessionId(toolSessionID);

	if (content == null) {
	    String error = "The content for this activity has not been defined yet.";
	    logger.error(error);
	    throw new ImageGalleryException(error);
	}

	List<List<List<UserImageContributionDTO>>> exportImageList = service.exportBySessionId(toolSessionID, learner,
		true);
	saveFileToLocal(exportImageList, directoryName);

	// Add flag to indicate whether to render user notebook entries
	sessionMap.put(ImageGalleryConstants.ATTR_REFLECTION_ON, content.isReflectOnActivity());

	// Create reflectList if reflection is enabled.
	if (content.isReflectOnActivity()) {
	    // Create reflectList, need to follow same structure used in teacher
	    // see service.getReflectList();
	    Map<Long, Set<ReflectDTO>> map = new HashMap<Long, Set<ReflectDTO>>();
	    Set<ReflectDTO> reflectDTOSet = new TreeSet<ReflectDTO>(new ReflectDTOComparator());
	    reflectDTOSet.add(getReflectionEntry(learner));
	    map.put(toolSessionID, reflectDTOSet);

	    // Add reflectList to sessionMap
	    sessionMap.put(ImageGalleryConstants.ATTR_REFLECT_LIST, map);
	}

	// put it into HTTPSession
	sessionMap.put(ImageGalleryConstants.ATTR_IMAGE_GALLERY, content);
	sessionMap.put(ImageGalleryConstants.ATTR_TITLE, content.getTitle());
	sessionMap.put(ImageGalleryConstants.ATTR_INSTRUCTIONS, content.getInstructions());
	sessionMap.put(ImageGalleryConstants.ATTR_EXPORT_IMAGE_LIST, exportImageList);
    }

    public void teacher(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies, HashMap sessionMap) throws ImageGalleryException {

	// check if toolContentId exists in db or not
	if (toolContentID == null) {
	    String error = "Tool Content Id is missing. Unable to continue";
	    logger.error(error);
	    throw new ImageGalleryException(error);
	}

	ImageGallery content = service.getImageGalleryByContentId(toolContentID);

	if (content == null) {
	    String error = "Data is missing from the database. Unable to Continue";
	    logger.error(error);
	    throw new ImageGalleryException(error);
	}
	List<List<List<UserImageContributionDTO>>> exportImageList = service.exportByContentId(toolContentID);
	saveFileToLocal(exportImageList, directoryName);

	// Add flag to indicate whether to render user notebook entries
	sessionMap.put(ImageGalleryConstants.ATTR_REFLECTION_ON, content.isReflectOnActivity());

	// Create reflectList if reflection is enabled.
	if (content.isReflectOnActivity()) {
	    Map<Long, Set<ReflectDTO>> reflectList = service.getReflectList(content.getContentId(), true);
	    // Add reflectList to sessionMap
	    sessionMap.put(ImageGalleryConstants.ATTR_REFLECT_LIST, reflectList);
	}

	// put it into HTTPSession
	sessionMap.put(ImageGalleryConstants.ATTR_IMAGE_GALLERY, content);
	sessionMap.put(ImageGalleryConstants.ATTR_TITLE, content.getTitle());
	sessionMap.put(ImageGalleryConstants.ATTR_INSTRUCTIONS, content.getInstructions());
	sessionMap.put(ImageGalleryConstants.ATTR_EXPORT_IMAGE_LIST, exportImageList);
    }

    /**
     * Create download links for every attachment.
     *
     * @param taskSummaries
     * @param directoryName
     * @throws IOException
     */
    private void saveFileToLocal(List<List<List<UserImageContributionDTO>>> sessionList, String directoryName) {
	handler = getToolContentHandler();

	for (List<List<UserImageContributionDTO>> imageList : sessionList) {
	    for (List<UserImageContributionDTO> userContributionList : imageList) {
		for (UserImageContributionDTO userContribution : userContributionList) {
		    try {
			ImageGalleryItem image = userContribution.getImage();
			int idx = 1;
			String userName = image.getCreateBy().getLoginName();
			String localDir;
			while (true) {
			    localDir = FileUtil.getFullPath(directoryName, userName + "/" + idx);
			    File local = new File(localDir);
			    if (!local.exists()) {
				local.mkdirs();
				break;
			    }
			    idx++;
			}
			image.setAttachmentLocalUrl(userName + "/" + idx + "/" + image.getMediumFileUuid() + '.'
				+ FileUtil.getFileExtension(image.getFileName()));
			handler.saveFile(image.getMediumFileUuid(),
				FileUtil.getFullPath(directoryName, image.getAttachmentLocalUrl()));
		    } catch (Exception e) {
			logger.error("Export forum topic attachment failed: " + e.toString());
		    }
		    break;
		}
	    }
	    break;
	}

    }

    private ImageGalleryToolContentHandler getToolContentHandler() {
	if (handler == null) {
	    WebApplicationContext wac = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(this.getServletContext());
	    handler = (ImageGalleryToolContentHandler) wac.getBean(ImageGalleryConstants.TOOL_CONTENT_HANDLER_NAME);
	}
	return handler;
    }

    private ReflectDTO getReflectionEntry(ImageGalleryUser imageGalleryUser) {
	ReflectDTO reflectDTO = new ReflectDTO(imageGalleryUser);
	NotebookEntry notebookEntry = service.getEntry(imageGalleryUser.getSession().getSessionId(),
		CoreNotebookConstants.NOTEBOOK_TOOL, ImageGalleryConstants.TOOL_SIGNATURE,
		imageGalleryUser.getUserId().intValue());

	// check notebookEntry is not null
	if (notebookEntry != null) {
	    reflectDTO.setReflect(notebookEntry.getEntry());
	    logger.debug("Could not find notebookEntry for ImageGalleryUser: " + imageGalleryUser.getUid());
	}
	return reflectDTO;
    }
}
