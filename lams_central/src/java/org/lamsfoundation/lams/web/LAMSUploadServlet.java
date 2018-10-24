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
 * This servlet accepts just file uploads, eventually with a parameter specifying file type
 *
 * This servlet has been modified for LAMS to support the lams_www/secure/[design folder] format. The design folder is a
 * folder with a unique numeric name. Whenever a new design is created (using "New" in the client), a new design folder
 * name is assigned. The [design folder] is passed in as the CurrentFolder. This servlet supports the image and link
 * windows, not the browse window.
 *
 *
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
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	if (log.isDebugEnabled()) {
	    log.debug("Upload started");
	}

	String currentFolderStr = request.getParameter("CurrentFolder");

	String newName = null;
	String fileUrl = null;
	String returnMessage = null;

	if (currentFolderStr.equals("/-1/")) {
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
		boolean maxFilesizeExceededMessage = FileValidatorUtil.validateFileSize(uplFile.getSize(), true);
		if (!maxFilesizeExceededMessage) {
		    fileName = centralMessageService.getMessage("errors.maxfilesize",
			    new Object[] { Configuration.getAsInt(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE) });

		    // validate file extension
		} else if (!FileUtil.isExtensionAllowed(fileType, fileName)) {
		    returnMessage = "Invalid file type";

		} else {
		    File uploadDir = UploadFileUtil.getUploadDir(currentFolderStr, fileType);
		    fileName = UploadFileUtil.getUploadFileName(uploadDir, fileName);
		    newName = fileName;
		    File destinationFile = new File(uploadDir, fileName);

		    String currentWebPath = UploadFileUtil.getUploadWebPath(currentFolderStr, fileType);
		    fileUrl = currentWebPath + '/' + fileName;

		    FileCopyUtils.copy(uplFile.getInputStream(), new FileOutputStream(destinationFile));
		    if (log.isDebugEnabled()) {
			log.debug("Uploaded file to " + destinationFile.getAbsolutePath());
		    }
		}
	    } catch (Exception e) {
		log.error(e);
		returnMessage = "Error while uploading file: " + e.getMessage();
	    }
	}

	if (returnMessage == null) {
	    returnMessage = "File successfully uploaded: " + newName;
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
	} else if (log.isDebugEnabled()) {
	    log.debug("No CKEditor method found to run after completion, but upload finished with message: "
		    + returnMessage);
	}

	if (log.isDebugEnabled()) {
	    log.debug("Upload finished");
	}
    }
}