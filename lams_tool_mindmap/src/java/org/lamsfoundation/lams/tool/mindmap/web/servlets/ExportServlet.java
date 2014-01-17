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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.mindmap.dto.MindmapDTO;
import org.lamsfoundation.lams.tool.mindmap.dto.MindmapSessionDTO;
import org.lamsfoundation.lams.tool.mindmap.dto.MindmapUserDTO;
import org.lamsfoundation.lams.tool.mindmap.dto.NotebookEntryDTO;
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
    private final String ERROR = "error.html";
    private final String FILENAME = "mindmap_main.html";
    private IMindmapService mindmapService;

    protected String doExport(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies) {
	String basePath = 
		WebUtil.getBaseServerURL() + request.getContextPath();

	if (mindmapService == null) {
	    mindmapService = MindmapServiceProxy.getMindmapService(getServletContext());
	}

	try {
	    // exporting Flash and JavaScript files
	    writeResponseToFile(basePath + "/images/mindmap_locked.swf", directoryName, "mindmap.swf", cookies);
	    writeResponseToFile(Configuration.get(ConfigurationKeys.SERVER_URL) + "includes/javascript/swfobject.js",
		    directoryName, "swfobject.js", cookies);
	    writeResponseToFile(basePath + "/includes/javascript/mindmap.resize.js", directoryName, "resize.js",
		    cookies);
	    writeResponseToFile(Configuration.get(ConfigurationKeys.SERVER_URL) + "includes/javascript/jquery.js",
		    directoryName, "jquery.js", cookies);
	    
	    try {
		File localeFile = new File(directoryName + "/locale.xml");
		FileOutputStream fop = new FileOutputStream(localeFile);
		fop.write(mindmapService.getLanguageXML().getBytes());
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	    
	    if (StringUtils.equals(mode, ToolAccessMode.LEARNER.toString())) {
		request.getSession().setAttribute(AttributeNames.ATTR_MODE, ToolAccessMode.LEARNER);
		doLearnerExport(request, response, directoryName, cookies);
		writeResponseToFile(basePath + "/pages/export/exportPortfolioSinglemode.jsp", directoryName, FILENAME, cookies);
		
		return FILENAME;
	    }
	    else if (StringUtils.equals(mode, ToolAccessMode.TEACHER.toString())) {
		request.getSession().setAttribute(AttributeNames.ATTR_MODE, ToolAccessMode.TEACHER);
		
		Mindmap mindmap = mindmapService.getMindmapByContentId(toolContentID);
		MindmapDTO mindmapDTO = new MindmapDTO(mindmap);
		mindmapDTO.setTitle(mindmap.getTitle());
		mindmapDTO.setInstructions(mindmap.getInstructions());
		
		if (mindmap.isMultiUserMode()) {
		    Set<MindmapSessionDTO> sessionDTOs = mindmapDTO.getSessionDTOs();
		    for (Iterator iterator = sessionDTOs.iterator(); iterator.hasNext();) {
			MindmapSessionDTO mindmapSessionDTO = (MindmapSessionDTO) iterator.next();
			
			String filename = mindmapSessionDTO.getSessionName() + "_" + mindmapSessionDTO.getSessionID() + ".html";
			
			doTeacherMultiModeExport(request, response, directoryName, cookies, mindmap, mindmapSessionDTO);
			writeResponseToFile(basePath + "/pages/export/exportPortfolioMultimode.jsp", directoryName, filename, cookies);
		    }
		    request.getSession().setAttribute("mindmapDTO", mindmapDTO);
		    
		    writeResponseToFile(basePath + "/pages/export/exportPortfolioMultimodeLinks.jsp", directoryName, FILENAME, cookies);
		
		    return FILENAME;
		}
		else {
		    Set<MindmapSessionDTO> sessionDTOs = mindmapDTO.getSessionDTOs();
		    for (Iterator iterator = sessionDTOs.iterator(); iterator.hasNext();) {
			MindmapSessionDTO mindmapSessionDTO = (MindmapSessionDTO) iterator.next();	    
			    
			Set<MindmapUserDTO> userDTOs = mindmapSessionDTO.getUserDTOs();
			for (Iterator userIterator = userDTOs.iterator(); userIterator.hasNext();) {
			    MindmapUserDTO mindmapUserDTO = (MindmapUserDTO) userIterator.next();
			    MindmapUser mindmapUser = mindmapService.getUserByUID(mindmapUserDTO.getUid());
			    String filename = mindmapUser.getFirstName() + "_" + mindmapUser.getLastName() + 
			    	"_" + mindmapUser.getUid() + ".html";
			    
			    doTeacherSingleModeExport(request, response, directoryName, cookies, mindmap, mindmapUser);
			    writeResponseToFile(basePath + "/pages/export/exportPortfolioSinglemode.jsp", directoryName, filename, cookies);
			}
		    }
		    
		    request.getSession().setAttribute("mindmapDTO", mindmapDTO);
		    
		    writeResponseToFile(basePath + "/pages/export/exportPortfolioSinglemodeLinks.jsp", directoryName, FILENAME, cookies);
		    
		    return FILENAME;
		}
	    }
	} catch (MindmapException e) {
	    logger.error("Cannot perform export for Mindmap tool!");
	}
	
	return ERROR;
    }
    
    private void exportMindmapNodes(List mindmapNodeList, Mindmap mindmap, MindmapUser mindmapUser, String path)
    {
	if (mindmapNodeList != null && mindmapNodeList.size() > 0) {
	    MindmapNode rootMindmapNode = (MindmapNode) mindmapNodeList.get(0);

	    String mindmapUserName = null;
	    if (rootMindmapNode.getUser() == null)
		mindmapUserName = mindmapService.getMindmapMessageService().getMessage("node.instructor.label");
	    else
		mindmapUserName = rootMindmapNode.getUser().getFirstName() + " " + rootMindmapNode.getUser().getLastName();

	    NodeModel rootNodeModel = new NodeModel(new NodeConceptModel(rootMindmapNode.getUniqueId(), 
		    rootMindmapNode.getText(), rootMindmapNode.getColor(), mindmapUserName));

	    NodeModel currentNodeModel = mindmapService.getMindmapXMLFromDatabase(rootMindmapNode.getNodeId(), 
		    mindmap.getUid(), rootNodeModel, mindmapUser);

	    XStream xstream = new XStream();
	    xstream.alias("branch", NodeModel.class);
	    String mindmapContent = xstream.toXML(currentNodeModel);

	    try {
		File mindmapFile = new File(path);
		FileOutputStream fop = new FileOutputStream(mindmapFile);
		fop.write(mindmapContent.getBytes());
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
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
	
	if (mindmap.isReflectOnActivity()) {
	    NotebookEntry notebookEntry = mindmapService.getEntry(mindmapUser.getEntryUID());
	    NotebookEntryDTO notebookEntryDTO = new NotebookEntryDTO(notebookEntry);
	    userDTO.setEntryDTO(notebookEntryDTO);
	}
	
	sessionDTO.getUserDTOs().add(userDTO);
	mindmapDTO.getSessionDTOs().add(sessionDTO);

	// adding Mindmap files to archive
	
	request.getSession().setAttribute("mindmapContentPath", "mindmap.xml");
	request.getSession().setAttribute("localizationPath", "locale.xml");
	request.getSession().setAttribute("mindmapUserDTO", userDTO);
	
	String currentMindmapUser = mindmapUser.getFirstName() + " " + mindmapUser.getLastName();
	request.getSession().setAttribute("currentMindmapUser", currentMindmapUser);

	List mindmapNodeList = null;
	if (mindmap.isMultiUserMode()) // is multi-user
	    mindmapNodeList = mindmapService.getAuthorRootNodeByMindmapSession(mindmap.getUid(), toolSessionID);
	else
	    mindmapNodeList = mindmapService.getRootNodeByMindmapIdAndUserId(mindmap.getUid(), mindmapUser.getUid());

	exportMindmapNodes(mindmapNodeList, mindmap, mindmapUser, directoryName + "/mindmap.xml");

	request.getSession().setAttribute("mindmapDTO", mindmapDTO);
    }

    private void doTeacherSingleModeExport(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies, Mindmap mindmap, MindmapUser mindmapUser) throws MindmapException {
	
	logger.debug("doTeacherSingleModeExport: toolContentID: " + toolContentID);
	
	String filename = mindmapUser.getFirstName() + "_" + mindmapUser.getLastName() + 
		"_" + mindmapUser.getUid();
	
	if (mindmap.isReflectOnActivity()) {
	    MindmapUserDTO userDTO = new MindmapUserDTO(mindmapUser);
	    NotebookEntry notebookEntry = mindmapService.getEntry(mindmapUser.getEntryUID());
	    NotebookEntryDTO notebookEntryDTO = new NotebookEntryDTO(notebookEntry);
	    userDTO.setEntryDTO(notebookEntryDTO);
	    request.getSession().setAttribute("mindmapUserDTO", userDTO);
	}
	
	request.getSession().setAttribute("mindmapContentPath", filename + ".xml");
	request.getSession().setAttribute("localizationPath", "locale.xml");
	
	String currentMindmapUser = mindmapUser.getFirstName() + " " + mindmapUser.getLastName();
	request.getSession().setAttribute("currentMindmapUser", currentMindmapUser);
	
	List mindmapNodeList = mindmapService.getRootNodeByMindmapIdAndUserId(mindmap.getUid(), mindmapUser.getUid());
	
	exportMindmapNodes(mindmapNodeList, mindmap, mindmapUser, directoryName + "/" + filename + ".xml");
    }
    
    private void doTeacherMultiModeExport(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies, Mindmap mindmap, MindmapSessionDTO mindmapSessionDTO) 
    		throws MindmapException {

	logger.debug("doExportTeacher: toolContentID: " + toolContentID);
	
	String filename = mindmapSessionDTO.getSessionName() + "_" + mindmapSessionDTO.getSessionID();
	
	if (mindmap.isReflectOnActivity()) {
	    Set<MindmapUserDTO> userDTOs = mindmapSessionDTO.getUserDTOs();
	    for (Iterator userIterator = userDTOs.iterator(); userIterator.hasNext();) {
		MindmapUserDTO mindmapUserDTO = (MindmapUserDTO) userIterator.next();
		MindmapUser mindmapUser = mindmapService.getUserByUID(mindmapUserDTO.getUid());
		NotebookEntry notebookEntry = mindmapService.getEntry(mindmapUser.getEntryUID());
		NotebookEntryDTO notebookEntryDTO = new NotebookEntryDTO(notebookEntry);
		mindmapUserDTO.setEntryDTO(notebookEntryDTO);
	    }
	    request.getSession().setAttribute("userDTOs", userDTOs);
	}
	
	request.getSession().setAttribute("mindmapContentPath", filename + ".xml");
	request.getSession().setAttribute("localizationPath", "locale.xml");
	    
	List mindmapNodeList = mindmapService.getAuthorRootNodeByMindmapSession(mindmap.getUid(), mindmapSessionDTO.getSessionID());
	    
	exportMindmapNodes(mindmapNodeList, mindmap, null, directoryName + "/" + filename + ".xml");
	
    }
    
}
