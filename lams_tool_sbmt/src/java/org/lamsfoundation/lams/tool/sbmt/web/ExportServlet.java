/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */
package org.lamsfoundation.lams.tool.sbmt.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.FileException;
import org.lamsfoundation.lams.contentrepository.IVersionedNode;
import org.lamsfoundation.lams.contentrepository.NodeType;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.sbmt.Learner;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesContent;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesSession;
import org.lamsfoundation.lams.tool.sbmt.dto.FileDetailsDTO;
import org.lamsfoundation.lams.tool.sbmt.exception.SubmitFilesException;
import org.lamsfoundation.lams.tool.sbmt.service.ISubmitFilesService;
import org.lamsfoundation.lams.tool.sbmt.service.SubmitFilesServiceProxy;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.util.LastNameAlphabeticComparator;
import org.lamsfoundation.lams.web.servlet.AbstractExportPortfolioServlet;

public class ExportServlet extends AbstractExportPortfolioServlet {
	private static final long serialVersionUID = -4529093489007108143L;

	private static Logger logger = Logger.getLogger(ExportServlet.class);

	private final String FILENAME = "sbmt_main.html";

	public String doExport(HttpServletRequest request,
			HttpServletResponse response, String directoryName, Cookie[] cookies) {
		
		ISubmitFilesService sbmtService = SubmitFilesServiceProxy
				.getSubmitFilesService(getServletContext());

		Map map = null;
		if (StringUtils.equals(mode, ToolAccessMode.LEARNER.toString())) {
			map = learner(request, response, directoryName, cookies, sbmtService);
		} else if (StringUtils.equals(mode, ToolAccessMode.TEACHER.toString())) {
			map = teacher(request, response, directoryName, cookies, sbmtService);
		}

		String basePath = request.getScheme() + "://" + request.getServerName()
				+ ":" + request.getServerPort() + request.getContextPath();

		// Writing learner submitted files to disk. A folder is generated for
		// each
		// learner, within this folder a sub folder is generated for each
		// submitted file. The FileDetailsDTO object is updated to record the
		// filepath of the exported file.
		
		List attachmentList = new LinkedList();
		
		Iterator userIter = map.keySet().iterator();
		while (userIter.hasNext()) {
			UserDTO user = (UserDTO) userIter.next();
			List fileList = (List) map.get(user);

			Iterator fileListIter = fileList.iterator();

			int submissionCount = 1;

			while (fileListIter.hasNext()) {

				FileDetailsDTO submittedFile = (FileDetailsDTO) fileListIter
						.next();

				logger.debug("doExport: starting to write file:"
						+ submittedFile.getFilePath());

				IVersionedNode node = sbmtService.downloadFile(submittedFile
						.getUuID(), submittedFile.getVersionID());
				
				// checking to see if our node type is a file.
				if (node.isNodeType(NodeType.FILENODE)) {
					try {
						InputStream is = node.getFile();
						BufferedInputStream bis = new BufferedInputStream(is);
						
						// The path name where this file is to be stored
						File outFileDir = new File(directoryName
								+ File.separator + user.getLogin()
								+ File.separator + submissionCount);

						// Creating the directory structure
						outFileDir.mkdirs();

						File outFile = new File(outFileDir, submittedFile
								.getFilePath());

						// writing the data to file
						FileOutputStream fos = new FileOutputStream(outFile);
						BufferedOutputStream bos = new BufferedOutputStream(fos);
						
						for (int c = bis.read(); c != -1; c = bis.read()) {
							bos.write(c);
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
				
				String exportedURL = user.getLogin() + "/" + submissionCount
						+ "/" + submittedFile.getFilePath();
				
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

				logger.debug("doExport: finished file:"
						+ submittedFile.getFilePath());
			}
		}

		// Generating the attachments.txt file. This contains a list of all
		// files exported.
		// TODO
		
		// writing out the attachmentList
		request.getSession().setAttribute("attachmentList", attachmentList);
		writeResponseToFile(basePath + "/export/exportAttachmentList.jsp",
				directoryName, "attachment_list.txt", cookies);
		
		// generate the submit main page
		writeResponseToFile(basePath + "/export/exportportfolio.jsp",
				directoryName, FILENAME, cookies);

		return FILENAME;

	}

	public Map learner(HttpServletRequest request,
			HttpServletResponse response, String directoryName, Cookie[] cookies, ISubmitFilesService sbmtService) {
		
		if (userID == null || toolSessionID == null) {
			String error = "Tool session Id or user Id is null. Unable to continue";
			logger.error(error);
			throw new SubmitFilesException(error);
		}

		Learner learner = sbmtService.getLearner(toolSessionID, userID);

		if (learner == null) {
			String error = "The user with user id "
					+ userID
					+ " does not exist in this session or session may not exist.";
			logger.error(error);
			throw new SubmitFilesException(error);
		}

		SubmitFilesContent content = sbmtService
				.getSubmitFilesContent(toolContentID);

		if (content == null) {
			String error = "The content for this activity has not been defined yet.";
			logger.error(error);
			throw new SubmitFilesException(error);
		}

		List fileList = sbmtService.getFilesUploadedByUser(userID,
				toolSessionID);
		Map userFilesMap = new HashMap();
		userFilesMap.put(sbmtService.getUserDetails(learner.getUserID()),
				fileList);
		request.getSession().setAttribute("report", userFilesMap);
		return userFilesMap;
	}

	public Map teacher(HttpServletRequest request,
			HttpServletResponse response, String directoryName, Cookie[] cookies, ISubmitFilesService sbmtService) {
		
		// check if toolContentId exists in db or not
		if (toolContentID == null) {
			String error = "Tool Content Id is missing. Unable to continue";
			logger.error(error);
			throw new SubmitFilesException(error);
		}

		SubmitFilesContent content = sbmtService
				.getSubmitFilesContent(toolContentID);

		if (content == null) {
			String error = "Data is missing from the database. Unable to Continue";
			logger.error(error);
			throw new SubmitFilesException(error);
		}
		// return FileDetailsDTO list according to the given sessionID
		Set sessionList = content.getToolSession();
		Iterator iter = sessionList.iterator();
		SortedMap userFilesMap = new TreeMap(new LastNameAlphabeticComparator());
		while (iter.hasNext()) {
			SubmitFilesSession session = (SubmitFilesSession) iter.next();
			userFilesMap.putAll(sbmtService.getFilesUploadedBySession(session
					.getSessionID()));
		}
		request.getSession().setAttribute("report", userFilesMap);

		return userFilesMap;
	}

}
