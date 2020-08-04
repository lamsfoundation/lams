/*
Copyright (c) 2003-2011, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

package org.lamsfoundation.lams.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.FileValidatorUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.UploadFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 * Servlet to upload files.<br>
 *
 * This servlet accepts just file uploads, optionally with a parameter specifying file type
 *
 * For CKEditor uploads:
 * This servlet has been modified for LAMS to support the lams_www/secure/[design folder] format. The design folder is a
 * folder with a unique numeric name. Whenever a new design is created (using "New" in the client), a new design folder
 * name is assigned. The [design folder] is passed in as the CurrentFolder. This servlet supports the image and link
 * windows, not the browse window.
 *
 * Currently this servlet can not be rewritten to Spring Controller.
 * Requests to controllers pass few additional Spring layers.
 * For files uploaded with Ajax Spring looks for Spring Security CSRF token.
 * It is not present as we do not use Spring Security. It results in HTTP 405 Method not allowed.
 * There are ways to turn off CSRF check, but not with current Spring version.
 * We can try to rewrite this servlet once we upgrade Spring and switch to programmatic configuration.
 *
 * @author Marcin Cieslak
 * @author Simone Chiaretta (simo@users.sourceforge.net)
 * @author Mitchell Seaton
 */
public class LAMSUploadServlet extends HttpServlet {

    private static final long serialVersionUID = 7839808388592495717L;
    private static final Logger log = Logger.getLogger(LAMSUploadServlet.class);

    @Autowired
    private MessageService centralMessageService;

    /*
     * Request Spring to lookup the applicationContext tied to the current ServletContext and inject service beans
     * available in that applicationContext.
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
	super.init(config);
	SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
	if (!request.getServletPath().endsWith("tmpFileUploadDelete")) {
	    return;
	}

	String tmpFileUploadId = request.getParameter("tmpFileUploadId");
	String fileName = request.getParameter("name");

	String uploadSubDir = FileUtil.prefix + tmpFileUploadId;
	File uploadDir = new File(Configuration.get(ConfigurationKeys.LAMS_TEMP_DIR), uploadSubDir);
	File file = new File(uploadDir, fileName);
	if (file.exists() && FileUtils.deleteQuietly(file)) {
	    if (log.isDebugEnabled()) {
		log.debug("Deleted temporarily uploaded file: " + file.getAbsolutePath());
	    }
	} else {
	    log.warn("Could not find or delete temporarily uploaded file: " + file.getAbsolutePath());
	}

    }

    /**
     * Manage the Upload requests.<br>
     *
     * The servlet accepts commands sent in the following format:<br>
     * simpleUploader?Type=ResourceType<br>
     * <br>
     * It store the file (renaming it in case a file with the same name exists) and then return an HTML file with a
     * javascript command in it.
     *
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
	if (log.isDebugEnabled()) {
	    log.debug("Upload started");
	}

	String currentFolder = request.getParameter("CurrentFolder");
	if (request.getServletPath().endsWith("tmpFileUpload")) {
	    processTemporaryFileUpload(request, response);
	} else if (StringUtils.isNotBlank(currentFolder)) {
	    processCKEditorUpload(request, response, currentFolder);
	} else {
	    log.warn("Unrecognised file upload type");
	}

    }

    /**
     * Processes files uploaded using CKEditor
     */
    private void processCKEditorUpload(HttpServletRequest request, HttpServletResponse response, String currentFolder)
	    throws IOException {
	String newName = null;
	String fileUrl = null;
	String returnMessage = null;

	if (currentFolder.equals("/-1/")) {
	    returnMessage = "Security error. You probably don't have enough permissions to upload. Please check your server.";
	} else {
	    // get realBaseDir and lamsContextPath at request time from config values in memory
	    String fileType = request.getParameter("Type");

	    DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
	    ServletFileUpload fileUpload = new ServletFileUpload(fileItemFactory);
	    try {
		List<FileItem> fileItems = fileUpload.parseRequest(request);
		Map<String, Object> fields = new HashMap<>();

		Iterator<FileItem> iter = fileItems.iterator();
		while (iter.hasNext()) {
		    FileItem fileItem = iter.next();
		    if (fileItem.isFormField()) {
			fields.put(fileItem.getFieldName(), fileItem.getString());
		    } else {
			fields.put(fileItem.getFieldName(), fileItem);
		    }
		}
		FileItem uplFile = (FileItem) fields.get("NewFile");
		if (uplFile == null) {
		    // form field name used by CKEditor 3.x
		    uplFile = (FileItem) fields.get("upload");
		}

		String fileNameLong = uplFile.getName();
		fileNameLong = fileNameLong.replace('\\', '/');
		String[] pathParts = fileNameLong.split("/");
		String fileName = pathParts[pathParts.length - 1];

		// validate file size
		boolean fileSizeValidated = FileValidatorUtil.validateFileSize(uplFile.getSize(), true);
		if (!fileSizeValidated) {
		    returnMessage = centralMessageService.getMessage("errors.maxfilesize",
			    new Object[] { Configuration.getAsInt(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE) });

		    // validate file extension
		} else if (!FileUtil.isExtensionAllowed(fileType, fileName)) {
		    returnMessage = "Invalid file type";
		} else {
		    try {
			boolean isVirusFree = FileUtil.isVirusFree(uplFile.getInputStream());
			if (!isVirusFree) {
			    returnMessage = "File contains a virus: " + fileName;
			}
		    } catch (IOException e) {
			returnMessage = "Could not scan file: " + fileName;
		    }

		    if (returnMessage == null) {
			File uploadDir = UploadFileUtil.getUploadDir(currentFolder, fileType);
			fileName = UploadFileUtil.getUploadFileName(uploadDir, fileName);
			newName = fileName;
			File destinationFile = new File(uploadDir, fileName);

			String currentWebPath = UploadFileUtil.getUploadWebPath(currentFolder, fileType);
			fileUrl = currentWebPath + '/' + fileName;

			FileCopyUtils.copy(uplFile.getInputStream(), new FileOutputStream(destinationFile));
			if (log.isDebugEnabled()) {
			    log.debug("Uploaded file to " + destinationFile.getAbsolutePath());
			}
		    }
		}
	    } catch (Exception e) {
		log.error(e);
		returnMessage = e.getMessage();
	    }
	}

	if (returnMessage != null) {
	    log.warn("Error while uploading file \"" + newName + "\": " + returnMessage);
	}

	String ckeditorSetUrlFuncNum = request.getParameter("CKEditorFuncNum");
	if (ckeditorSetUrlFuncNum != null) {
	    response.setContentType("text/html; charset=UTF-8");
	    response.setHeader("Cache-Control", "no-cache");
	    PrintWriter out = response.getWriter();

	    out.println("<script type=\"text/javascript\">");
	    out.println("this.parent.CKEDITOR.tools.callFunction(" + ckeditorSetUrlFuncNum + ",'" + fileUrl + "','"
		    + returnMessage + "');");
	    out.println("</script>");
	    out.flush();
	    out.close();
	}
    }

    /**
     * Processes files using Ajax. Puts them into temporary dir so a subsequent form submit can pick them up.
     *
     * @throws IOException
     */
    private void processTemporaryFileUpload(HttpServletRequest request, HttpServletResponse response)
	    throws IOException {
	String newName = null;
	String returnMessage = null;

	DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
	ServletFileUpload fileUpload = new ServletFileUpload(fileItemFactory);
	try {
	    List<FileItem> fileItems = fileUpload.parseRequest(request);
	    Map<String, Object> fields = new HashMap<>();

	    Iterator<FileItem> iter = fileItems.iterator();
	    while (iter.hasNext()) {
		FileItem fileItem = iter.next();
		if (fileItem.isFormField()) {
		    fields.put(fileItem.getFieldName(), fileItem.getString());
		} else {
		    fields.put(fileItem.getFieldName(), fileItem);
		}
	    }
	    FileItem uplFile = (FileItem) fields.get("file");
	    if (uplFile == null) {
		returnMessage = "Can not find a field named \"file\"";
	    }

	    if (returnMessage == null) {
		String fileName = uplFile.getName();

		// validate file size
		boolean fileSizeValidated = FileValidatorUtil.validateFileSize(uplFile.getSize(), true);
		if (!fileSizeValidated) {
		    returnMessage = centralMessageService.getMessage("errors.maxfilesize",
			    new Object[] { Configuration.getAsInt(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE) });
		} else {
		    try {
			boolean isVirusFree = FileUtil.isVirusFree(uplFile.getInputStream());
			if (!isVirusFree) {
			    returnMessage = "File contains a virus: " + fileName;
			}
		    } catch (IOException e) {
			returnMessage = "Could not scan file: " + fileName;
		    }
		}

		if (returnMessage == null) {
		    String uploadSubDir = FileUtil.prefix + fields.get("tmpFileUploadId");
		    File uploadDir = new File(Configuration.get(ConfigurationKeys.LAMS_TEMP_DIR), uploadSubDir);
		    uploadDir.mkdir();
		    fileName = UploadFileUtil.getUploadFileName(uploadDir, fileName);
		    newName = fileName;
		    File destinationFile = new File(uploadDir, fileName);

		    FileCopyUtils.copy(uplFile.getInputStream(), new FileOutputStream(destinationFile));
		    if (log.isDebugEnabled()) {
			log.debug("Uploaded file to " + destinationFile.getAbsolutePath());
		    }
		}
	    }
	} catch (Exception e) {
	    returnMessage = e.getMessage();
	}

	if (returnMessage == null) {
	    response.setContentType("application/json;charset=UTF-8");
	    response.getWriter().write("{\"name\" : \"" + newName + "\"}");
	} else {
	    log.warn("Error while uploading file \"" + newName + "\": " + returnMessage);
	    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	}
    }
}