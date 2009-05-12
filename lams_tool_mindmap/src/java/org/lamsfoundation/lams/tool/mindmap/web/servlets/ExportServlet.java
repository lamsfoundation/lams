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

package org.lamsfoundation.lams.tool.mindmap.web.servlets;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.mindmap.dto.MindmapDTO;
import org.lamsfoundation.lams.tool.mindmap.dto.MindmapSessionDTO;
import org.lamsfoundation.lams.tool.mindmap.dto.MindmapUserDTO;
import org.lamsfoundation.lams.tool.mindmap.model.Mindmap;
import org.lamsfoundation.lams.tool.mindmap.model.MindmapNode;
import org.lamsfoundation.lams.tool.mindmap.model.MindmapSession;
import org.lamsfoundation.lams.tool.mindmap.model.MindmapUser;
import org.lamsfoundation.lams.tool.mindmap.service.IMindmapService;
import org.lamsfoundation.lams.tool.mindmap.service.MindmapServiceProxy;
import org.lamsfoundation.lams.tool.mindmap.util.MindmapException;
import org.lamsfoundation.lams.tool.mindmap.util.xmlmodel.NodeConceptModel;
import org.lamsfoundation.lams.tool.mindmap.util.xmlmodel.NodeModel;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.servlet.AbstractExportPortfolioServlet;
import org.lamsfoundation.lams.web.util.AttributeNames;

import com.thoughtworks.xstream.XStream;

public class ExportServlet extends AbstractExportPortfolioServlet {

    private static final long serialVersionUID = -2829707715037631881L;
    private static Logger logger = Logger.getLogger(ExportServlet.class);
    private final String FILENAME = "mindmap_main.html";
    private IMindmapService mindmapService;

    protected String doExport(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies) {

	if (mindmapService == null) {
	    mindmapService = MindmapServiceProxy.getMindmapService(getServletContext());
	}

	try {
	    if (StringUtils.equals(mode, ToolAccessMode.LEARNER.toString())) {
		request.getSession().setAttribute(AttributeNames.ATTR_MODE, ToolAccessMode.LEARNER);
		doLearnerExport(request, response, directoryName, cookies);
	    } else if (StringUtils.equals(mode, ToolAccessMode.TEACHER.toString())) {
		request.getSession().setAttribute(AttributeNames.ATTR_MODE, ToolAccessMode.TEACHER);
		doTeacherExport(request, response, directoryName, cookies);
	    }
	} catch (MindmapException e) {
	    logger.error("Cannot perform export for Mindmap tool!");
	}

	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
		+ request.getContextPath();

	writeResponseToFile(basePath + "/pages/export/exportPortfolio.jsp", directoryName, FILENAME, cookies);

	return FILENAME;
    }

    protected String doOfflineExport(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies) {
	if (toolContentID == null && toolSessionID == null) {
	    logger.error("Tool content Id or and session Id are null. Unable to activity title");
	} else {
	    if (mindmapService == null) {
		mindmapService = MindmapServiceProxy.getMindmapService(getServletContext());
	    }

	    Mindmap content = null;
	    if (toolContentID != null) {
		content = mindmapService.getMindmapByContentId(toolContentID);
	    } else {
		MindmapSession session = mindmapService.getSessionBySessionId(toolSessionID);
		if (session != null)
		    content = session.getMindmap();
	    }
	    if (content != null) {
		activityTitle = content.getTitle();
	    }
	}
	return super.doOfflineExport(request, response, directoryName, cookies);
    }

    private void doLearnerExport(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies) throws MindmapException {

	logger.debug("doExportLearner: toolContentID:" + toolSessionID);

	// check if toolContentID available
	if (toolSessionID == null) {
	    String error = "Tool Session ID is missing. Unable to continue";
	    logger.error(error);
	    throw new MindmapException(error);
	}

	MindmapSession mindmapSession = mindmapService.getSessionBySessionId(toolSessionID);
	Mindmap mindmap = mindmapSession.getMindmap();
	Long userID = WebUtil.readLongParam(request, AttributeNames.PARAM_USER_ID);
	MindmapUser mindmapUser = mindmapService.getUserByUserIdAndSessionId(userID, toolSessionID);

	// construct dto's
	MindmapDTO mindmapDTO = new MindmapDTO();
	mindmapDTO.setTitle(mindmap.getTitle());
	mindmapDTO.setInstructions(mindmap.getInstructions());

	MindmapSessionDTO sessionDTO = new MindmapSessionDTO();
	sessionDTO.setSessionName(mindmapSession.getSessionName());
	sessionDTO.setSessionID(mindmapSession.getSessionId());

	MindmapUserDTO userDTO = new MindmapUserDTO(mindmapUser);

	sessionDTO.getUserDTOs().add(userDTO);
	mindmapDTO.getSessionDTOs().add(sessionDTO);

	// adding Mindmap files to archive
	request.getSession().setAttribute("mindmapContentPath", "mindmap.xml");

	String currentMindmapUser = mindmapUser.getFirstName() + " " + mindmapUser.getLastName();
	request.getSession().setAttribute("currentMindmapUser", currentMindmapUser);
	
	request.getSession().setAttribute("localizationPath", "locale.xml");

	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
		+ request.getContextPath();
	writeResponseToFile(basePath + "/images/mindmap_locked.swf", directoryName, "mindmap.swf", cookies);
	writeResponseToFile(basePath + "/includes/javascript/swfobject.js", directoryName, "swfobject.js", cookies);
	writeResponseToFile(basePath + "/includes/javascript/mindmap.resize.js", directoryName, "resize.js", cookies);
	writeResponseToFile(Configuration.get(ConfigurationKeys.SERVER_URL) 
		+ "includes/javascript/jquery-latest.pack.js", directoryName, "jquery.js", cookies);
	
	List mindmapNodeList = null;
	if (mindmap.isMultiUserMode()) // is multi-user
	    mindmapNodeList = mindmapService.getAuthorRootNodeByMindmapSession(mindmap.getUid(), toolSessionID);
	else
	    mindmapNodeList = mindmapService.getRootNodeByMindmapIdAndUserId(mindmap.getUid(), mindmapUser.getUid());

	if (mindmapNodeList != null && mindmapNodeList.size() > 0) {
	    MindmapNode rootMindmapNode = (MindmapNode) mindmapNodeList.get(0);

	    String mindmapUserName = null;
	    if (rootMindmapNode.getUser() == null)
		mindmapUserName = mindmapService.getMindmapMessageService().getMessage("node.instructor.label");
	    else
		mindmapUserName = rootMindmapNode.getUser().getFirstName() + " "
			+ rootMindmapNode.getUser().getLastName();

	    NodeModel rootNodeModel = new NodeModel(new NodeConceptModel(rootMindmapNode.getUniqueId(), rootMindmapNode
		    .getText(), rootMindmapNode.getColor(), mindmapUserName));

	    NodeModel currentNodeModel = mindmapService.getMindmapXMLFromDatabase(rootMindmapNode.getNodeId(),
		    mindmap.getUid(), rootNodeModel, mindmapUser);

	    XStream xstream = new XStream();
	    xstream.alias("branch", NodeModel.class);
	    String mindmapContent = xstream.toXML(currentNodeModel);

	    try {
		File mindmapFile = new File(directoryName + "/mindmap.xml");
		FileOutputStream fop = new FileOutputStream(mindmapFile);
		fop.write(mindmapContent.getBytes());
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}

	try {
	    File localeFile = new File(directoryName + "/locale.xml");
	    FileOutputStream fop = new FileOutputStream(localeFile);
	    fop.write(mindmapService.getLanguageXML().getBytes());
	} catch (IOException e) {
	    e.printStackTrace();
	}

	request.getSession().setAttribute("mindmapDTO", mindmapDTO);
    }

    private void doTeacherExport(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies) throws MindmapException {

	logger.debug("doExportTeacher: toolContentID:" + toolContentID);

	// check if toolContentID available
	if (toolContentID == null) {
	    String error = "Tool Content ID is missing. Unable to continue";
	    logger.error(error);
	    throw new MindmapException(error);
	}

	
	//Mindmap mindmap = mindmapService.getMindmapByContentId(toolContentID);
	//MindmapDTO mindmapDTO = new MindmapDTO(mindmap);
	
	
	MindmapSession mindmapSession = mindmapService.getSessionBySessionId(toolSessionID);
	//Mindmap mindmap = mindmapSession.getMindmap();
	Mindmap mindmap = mindmapService.getMindmapByContentId(toolContentID);
	//Long userID = WebUtil.readLongParam(request, AttributeNames.PARAM_USER_ID);
	MindmapUser mindmapUser = null; //mindmapService.getUserByUserIdAndSessionId(userID, toolSessionID);

	// construct dto's
	MindmapDTO mindmapDTO = new MindmapDTO(mindmap);
	mindmapDTO.setTitle(mindmap.getTitle());
	mindmapDTO.setInstructions(mindmap.getInstructions());

	//MindmapSessionDTO sessionDTO = new MindmapSessionDTO();
	//sessionDTO.setSessionName(mindmapSession.getSessionName());
	//sessionDTO.setSessionID(mindmapSession.getSessionId());

	//MindmapUserDTO userDTO = new MindmapUserDTO(mindmapUser);

	//sessionDTO.getUserDTOs().add(userDTO);
	//mindmapDTO.getSessionDTOs().add(sessionDTO);

	// adding Mindmap files to archive
	request.getSession().setAttribute("mindmapContentPath", "mindmap.xml");

	//String currentMindmapUser = mindmapUser.getFirstName() + " " + mindmapUser.getLastName();
	//request.getSession().setAttribute("currentMindmapUser", currentMindmapUser);
	
	request.getSession().setAttribute("localizationPath", "locale.xml");

	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
		+ request.getContextPath();
	writeResponseToFile(basePath + "/images/mindmap_locked.swf", directoryName, "mindmap.swf", cookies);
	writeResponseToFile(basePath + "/includes/javascript/swfobject.js", directoryName, "swfobject.js", cookies);
	writeResponseToFile(basePath + "/includes/javascript/mindmap.resize.js", directoryName, "resize.js", cookies);
	writeResponseToFile(Configuration.get(ConfigurationKeys.SERVER_URL) 
		+ "includes/javascript/jquery-latest.pack.js", directoryName, "jquery.js", cookies);
	
	List mindmapNodeList = null;
	if (mindmap.isMultiUserMode()) // is multi-user
	    mindmapNodeList = mindmapService.getAuthorRootNodeByMindmapSession(mindmap.getUid(), toolSessionID);
	else
	    mindmapNodeList = mindmapService.getRootNodeByMindmapIdAndUserId(mindmap.getUid(), mindmapUser.getUid());

	if (mindmapNodeList != null && mindmapNodeList.size() > 0) {
	    MindmapNode rootMindmapNode = (MindmapNode) mindmapNodeList.get(0);

	    String mindmapUserName = null;
	    if (rootMindmapNode.getUser() == null)
		mindmapUserName = mindmapService.getMindmapMessageService().getMessage("node.instructor.label");
	    else
		mindmapUserName = rootMindmapNode.getUser().getFirstName() + " "
			+ rootMindmapNode.getUser().getLastName();

	    NodeModel rootNodeModel = new NodeModel(new NodeConceptModel(rootMindmapNode.getUniqueId(), rootMindmapNode
		    .getText(), rootMindmapNode.getColor(), mindmapUserName));

	    NodeModel currentNodeModel = mindmapService.getMindmapXMLFromDatabase(rootMindmapNode.getNodeId(),
		    mindmap.getUid(), rootNodeModel, mindmapUser);

	    XStream xstream = new XStream();
	    xstream.alias("branch", NodeModel.class);
	    String mindmapContent = xstream.toXML(currentNodeModel);

	    try {
		File mindmapFile = new File(directoryName + "/mindmap.xml");
		FileOutputStream fop = new FileOutputStream(mindmapFile);
		fop.write(mindmapContent.getBytes());
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}

	try {
	    File localeFile = new File(directoryName + "/locale.xml");
	    FileOutputStream fop = new FileOutputStream(localeFile);
	    fop.write(mindmapService.getLanguageXML().getBytes());
	} catch (IOException e) {
	    e.printStackTrace();
	}

	request.getSession().setAttribute("mindmapDTO", mindmapDTO);
    }
}
