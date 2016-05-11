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



package org.lamsfoundation.lams.tool.sbmt.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.FileException;
import org.lamsfoundation.lams.contentrepository.IVersionedNode;
import org.lamsfoundation.lams.contentrepository.NodeType;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesContent;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesSession;
import org.lamsfoundation.lams.tool.sbmt.SubmitUser;
import org.lamsfoundation.lams.tool.sbmt.dto.FileDetailsDTO;
import org.lamsfoundation.lams.tool.sbmt.dto.SubmitUserDTO;
import org.lamsfoundation.lams.tool.sbmt.exception.SubmitFilesException;
import org.lamsfoundation.lams.tool.sbmt.service.ISubmitFilesService;
import org.lamsfoundation.lams.tool.sbmt.service.SubmitFilesServiceProxy;
import org.lamsfoundation.lams.tool.sbmt.util.SbmtConstants;
import org.lamsfoundation.lams.usermanagement.util.LastNameAlphabeticComparator;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.servlet.AbstractExportPortfolioServlet;
import org.lamsfoundation.lams.web.util.SessionMap;

public class ExportServlet extends AbstractExportPortfolioServlet {
    private static final long serialVersionUID = -4529093489007108143L;

    private static Logger logger = Logger.getLogger(ExportServlet.class);

    private final String FILENAME = "sbmt_main.html";

    private ISubmitFilesService sbmtService;

    @Override
    public void init() throws ServletException {
	sbmtService = SubmitFilesServiceProxy.getSubmitFilesService(getServletContext());
	super.init();
    }

    private class StringComparator implements Comparator<String> {
	@Override
	public int compare(String o1, String o2) {
	    if (o1 != null && o2 != null) {
		return o1.compareTo(o2);
	    } else if (o1 != null) {
		return 1;
	    } else {
		return -1;
	    }
	}
    }

    @Override
    public String doExport(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies) {

	// initial sessionMap
	SessionMap sessionMap = new SessionMap();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);

	Map map = null;
	if (StringUtils.equals(mode, ToolAccessMode.LEARNER.toString())) {
	    map = learner(request, response, directoryName, cookies, sessionMap);
	} else if (StringUtils.equals(mode, ToolAccessMode.TEACHER.toString())) {
	    map = teacher(request, response, directoryName, cookies, sessionMap);
	}

	String basePath = WebUtil.getBaseServerURL() + request.getContextPath();

	// Writing learner submitted files to disk. A folder is generated for
	// each
	// learner, within this folder a sub folder is generated for each
	// submitted file. The FileDetailsDTO object is updated to record the
	// filepath of the exported file.

	List attachmentList = new LinkedList();

	Iterator userIter = map.keySet().iterator();
	while (userIter.hasNext()) {
	    SubmitUserDTO user = (SubmitUserDTO) userIter.next();
	    List fileList = (List) map.get(user);

	    Iterator fileListIter = fileList.iterator();

	    int submissionCount = 1;

	    while (fileListIter.hasNext()) {

		FileDetailsDTO submittedFile = (FileDetailsDTO) fileListIter.next();

		IVersionedNode node = sbmtService.downloadFile(submittedFile.getUuID(), submittedFile.getVersionID());

		// checking to see if our node type is a file.
		if (node.isNodeType(NodeType.FILENODE)) {
		    try {
			InputStream is = node.getFile();

			// The path name where this file is to be stored
			File outFileDir = new File(
				directoryName + File.separator + user.getLogin() + File.separator + submissionCount);

			// Creating the directory structure
			outFileDir.mkdirs();

			logger.debug("doExport: starting to write file in zip compatible format:"
				+ node.getZipCompatibleFilename());

			File outFile = new File(outFileDir, node.getZipCompatibleFilename());

			// writing the data to file

			FileOutputStream fos = new FileOutputStream(outFile);

			byte[] out = new byte[8 * 1024];
			int len = -1;
			while ((len = is.read(out)) != -1) {
			    fos.write(out, 0, len);
			}

			logger.debug("doExport: finished writing to file");

			is.close();
			fos.close();

		    } catch (FileException fe) {
			String error = "Unable to retrieve file from repository";
			logger.error(error);
			throw new SubmitFilesException(error);
		    } catch (IOException ioe) {
			String error = "An IO error occured while writing file to disk";
			logger.error(error);
			throw new SubmitFilesException(error);
		    }
		} else {
		    String error = "Node in repository is not a FILENODE";
		    logger.error(error);
		    throw new SubmitFilesException(error);
		}

		logger.debug("doExport: encoding exportedURL");

		String exportedURL = user.getLogin() + "/" + submissionCount + "/" + node.getZipCompatibleFilename();

		attachmentList.add(exportedURL);

		// encoding the URL to application/x-www-form-urlencoded
		// see
		// http://www.w3.org/TR/html4/interact/forms.html#h-17.13.4.1
		try {
		    URLEncoder.encode(exportedURL, "UTF8");
		} catch (UnsupportedEncodingException e) {
		    exportedURL = "/";
		    String error = "Unable to URL encode the file path, files written to disk";
		    logger.error(error);
		    throw new SubmitFilesException(error);

		}

		exportedURL = exportedURL.replaceAll("\\+", "%20");
		submittedFile.setExportedURL(exportedURL);
		submissionCount++;

		logger.debug("doExport: finished file:" + submittedFile.getFilePath());
	    }
	}

	// Generating the attachments.txt file. This contains a list of all
	// files exported.
	// TODO

	// writing out the attachmentList
	sessionMap.put("attachmentList", attachmentList);
	writeResponseToFile(basePath + "/export/exportAttachmentList.jsp?sessionMapID=" + sessionMap.getSessionID(),
		directoryName, "attachment_list.txt", cookies);

	// generate the submit main page
	writeResponseToFile(basePath + "/export/exportportfolio.jsp?sessionMapID=" + sessionMap.getSessionID(),
		directoryName, FILENAME, cookies);

	return FILENAME;

    }

    public Map learner(HttpServletRequest request, HttpServletResponse response, String directoryName, Cookie[] cookies,
	    HashMap sessionMap) {

	if (userID == null || toolSessionID == null) {
	    String error = "Tool session Id or user Id is null. Unable to continue";
	    logger.error(error);
	    throw new SubmitFilesException(error);
	}

	SubmitUser learner = sbmtService.getSessionUser(toolSessionID, new Integer(userID.intValue()));

	if (learner == null) {
	    String error = "The user with user id " + userID
		    + " does not exist in this session or session may not exist.";
	    logger.error(error);
	    throw new SubmitFilesException(error);
	}
	SubmitUserDTO submitUserDTO = new SubmitUserDTO(learner);

	SubmitFilesSession session = sbmtService.getSessionById(toolSessionID);
	if (session == null) {
	    String error = "The session does not exist.";
	    logger.error(error);
	    throw new SubmitFilesException(error);
	}
	SubmitFilesContent content = session.getContent();
	if (content == null) {
	    String error = "The content for this activity has not been defined yet.";
	    logger.error(error);
	    throw new SubmitFilesException(error);
	}

	List fileList = sbmtService.getFilesUploadedByUser(new Integer(userID.intValue()), toolSessionID,
		request.getLocale());
	//if mark not release, then set these message as null.
	Iterator iter = fileList.iterator();
	while (iter.hasNext()) {
	    FileDetailsDTO filedto = (FileDetailsDTO) iter.next();
	    if (filedto.getDateMarksReleased() == null) {
		filedto.setComments(null);
		filedto.setMarks(null);
	    }
	}

	if (content.isReflectOnActivity()) {
	    // Get Reflection Entry 
	    submitUserDTO.setReflect(getReflectionEntry(learner.getSessionID(), learner.getUserID()));
	}

	Map userFilesMap = new HashMap();
	userFilesMap.put(submitUserDTO, fileList);

	// Add flag to indicate whether to render user notebook entries
	sessionMap.put(SbmtConstants.ATTR_REFLECTION_ON, content.isReflectOnActivity());

	//add session name to construct a new map
	Map report = new TreeMap(this.new StringComparator());
	report.put(sbmtService.getSessionById(toolSessionID).getSessionName(), userFilesMap);
	sessionMap.put("report", report);
	return userFilesMap;
    }

    public Map teacher(HttpServletRequest request, HttpServletResponse response, String directoryName, Cookie[] cookies,
	    HashMap sessionMap) {

	// check if toolContentId exists in db or not
	if (toolContentID == null) {
	    String error = "Tool Content Id is missing. Unable to continue";
	    logger.error(error);
	    throw new SubmitFilesException(error);
	}

	SubmitFilesContent content = sbmtService.getSubmitFilesContent(toolContentID);

	if (content == null) {
	    String error = "Data is missing from the database. Unable to Continue";
	    logger.error(error);
	    throw new SubmitFilesException(error);
	}
	// return FileDetailsDTO list according to the given sessionID
	Map report = new TreeMap(this.new StringComparator());
	Map allFileMap = new TreeMap(new LastNameAlphabeticComparator());

	//iterate all session in this content
	List<SubmitFilesSession> sessionList = sbmtService.getSessionsByContentID(toolContentID);
	for (SubmitFilesSession session : sessionList) {
	    SortedMap userFilesMap = new TreeMap(new LastNameAlphabeticComparator());
	    userFilesMap.putAll(sbmtService.getFilesUploadedBySession(session.getSessionID(), request.getLocale()));

	    if (content.isReflectOnActivity()) {
		// Iterate over all users in session and get reflection entry
		for (SubmitUserDTO submitUserDTO : (Set<SubmitUserDTO>) userFilesMap.keySet()) {
		    submitUserDTO.setReflect(getReflectionEntry(session.getSessionID(), submitUserDTO.getUserID()));
		}
	    }

	    allFileMap.putAll(userFilesMap);
	    report.put(session.getSessionName(), userFilesMap);
	}

	// Add flag to indicate whether to render user notebook entries
	sessionMap.put(SbmtConstants.ATTR_REFLECTION_ON, content.isReflectOnActivity());

//		add session name to construct a new map
	sessionMap.put("report", report);

	return allFileMap;
    }

    /**
     * Retrieves the reflection entry for the submitUser and stores it in a UserDTO
     * 
     * @param submitUser
     * @param notebookEntry
     */
    private String getReflectionEntry(Long toolSessionID, Integer userId) {

	NotebookEntry notebookEntry = sbmtService.getEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		SbmtConstants.TOOL_SIGNATURE, userId);

	if (notebookEntry != null) {
	    return notebookEntry.getEntry();
	}

	return null;
    }

}
