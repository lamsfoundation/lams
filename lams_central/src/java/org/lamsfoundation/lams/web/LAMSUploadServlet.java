/*
Copyright (c) 2003-2011, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

package org.lamsfoundation.lams.web;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.util.UploadFileUtil;

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
 * 
 * @web:servlet name="SimpleUploader" load-on-startup = "1"
 * @web.servlet-init-param name = "AllowedExtensionsFile" value = ""
 * @web.servlet-init-param name = "DeniedExtensionsFile" value =
 *                         "php|php3|php5|phtml|asp|aspx|ascx|jsp|cfm|cfc|pl|bat|exe|dll|reg|cg"
 * @web.servlet-init-param name = "AllowedExtensionsImage" value = "jpg|gif|jpeg|png|bmp"
 * @web.servlet-init-param name = "DeniedExtensionsImage" value = ""
 * @web.servlet-init-param name = "AllowedExtensionsFlash" value = "swf|fla"
 * @web.servlet-init-param name = "DeniedExtensionsFlash" value = ""
 * @web:servlet-mapping url-pattern="/ckeditor/filemanager/upload/simpleuploader"
 * 
 */

public class LAMSUploadServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(LAMSUploadServlet.class);

    private static Hashtable<String, ArrayList<String>> allowedExtensions;
    private static Hashtable<String, ArrayList<String>> deniedExtensions;

    /**
     * Initialize the servlet.<br>
     * Retrieve from the servlet configuration the "baseDir" which is the root of the file repository:<br>
     * If not specified the value of "/UserFiles/" will be used.<br>
     * Also it retrieve all allowed and denied extensions to be handled.
     * 
     */
    @Override
    public void init() throws ServletException {
	if (LAMSUploadServlet.log.isDebugEnabled()) {
	    LAMSUploadServlet.log.debug("Initialization started");
	}

	LAMSUploadServlet.allowedExtensions = new Hashtable<String, ArrayList<String>>(3);
	LAMSUploadServlet.deniedExtensions = new Hashtable<String, ArrayList<String>>(3);

	LAMSUploadServlet.allowedExtensions.put("File", stringToArrayList(getInitParameter("AllowedExtensionsFile")));
	LAMSUploadServlet.deniedExtensions.put("File", stringToArrayList(getInitParameter("DeniedExtensionsFile")));

	LAMSUploadServlet.allowedExtensions.put("Image", stringToArrayList(getInitParameter("AllowedExtensionsImage")));
	LAMSUploadServlet.deniedExtensions.put("Image", stringToArrayList(getInitParameter("DeniedExtensionsImage")));

	LAMSUploadServlet.allowedExtensions.put("Flash", stringToArrayList(getInitParameter("AllowedExtensionsFlash")));
	LAMSUploadServlet.deniedExtensions.put("Flash", stringToArrayList(getInitParameter("DeniedExtensionsFlash")));

	if (LAMSUploadServlet.log.isDebugEnabled()) {
	    LAMSUploadServlet.log.debug("Initialization completed");
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
    @SuppressWarnings("unchecked")
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	if (LAMSUploadServlet.log.isDebugEnabled()) {
	    LAMSUploadServlet.log.debug("Upload started");
	}

	String currentFolderStr = request.getParameter("CurrentFolder");

	String newName = null;
	String fileUrl = null;
	String returnMessage = null;

	if (currentFolderStr.equals("/-1/")) {
	    returnMessage = "Security error. You probably don't have enough permissions to upload. Please check your server.";
	} else {
	    // get realBaseDir and lamsContextPath at request time from config values in memory
	    String typeStr = request.getParameter("Type");

	    DiskFileUpload upload = new DiskFileUpload();
	    try {
		List<FileItem> items = upload.parseRequest(request);
		Map<String, Object> fields = new HashMap<String, Object>();

		Iterator<FileItem> iter = items.iterator();
		while (iter.hasNext()) {
		    FileItem item = iter.next();
		    if (item.isFormField()) {
			fields.put(item.getFieldName(), item.getString());
		    } else {
			fields.put(item.getFieldName(), item);
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
		String ext = UploadFileUtil.getFileExtension(fileName);

		if (extIsAllowed(typeStr, ext)) {
		    File uploadDir = UploadFileUtil.getUploadDir(currentFolderStr, typeStr);
		    fileName = UploadFileUtil.getUploadFileName(uploadDir, fileName);
		    File destinationFile = new File(uploadDir, fileName);

		    String currentWebPath = UploadFileUtil.getUploadWebPath(currentFolderStr, typeStr);
		    fileUrl = currentWebPath + '/' + fileName;

		    uplFile.write(destinationFile);
		    if (LAMSUploadServlet.log.isDebugEnabled()) {
			LAMSUploadServlet.log.debug("Uploaded file to " + destinationFile.getAbsolutePath());
		    }
		} else {
		    returnMessage = "Invalid file type";
		}
	    } catch (Exception e) {
		LAMSUploadServlet.log.error(e);
		returnMessage = "Error while uploading file";
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
	} else if (LAMSUploadServlet.log.isDebugEnabled()) {
	    LAMSUploadServlet.log
		    .debug("No CKEditor method found to run after completion, but upload finished with message: "
			    + returnMessage);
	}

	if (LAMSUploadServlet.log.isDebugEnabled()) {
	    LAMSUploadServlet.log.debug("Upload finished");
	}
    }

    /**
     * Helper function to convert the configuration string to an ArrayList.
     */

    private ArrayList<String> stringToArrayList(String str) {
	if (LAMSUploadServlet.log.isDebugEnabled()) {
	    LAMSUploadServlet.log.debug(str);
	}
	String[] strArr = str.split("\\|");

	ArrayList<String> tmp = new ArrayList<String>();
	if (str.length() > 0) {
	    for (int i = 0; i < strArr.length; ++i) {
		if (LAMSUploadServlet.log.isDebugEnabled()) {
		    LAMSUploadServlet.log.debug(i + " - " + strArr[i]);
		}
		tmp.add(strArr[i].toLowerCase());
	    }
	}
	return tmp;
    }

    /**
     * Helper function to verify if a file extension is allowed or not allowed.
     */

    private boolean extIsAllowed(String fileType, String ext) {
	String extLower = ext.toLowerCase();

	ArrayList<String> allowList = LAMSUploadServlet.allowedExtensions.get(fileType);
	ArrayList<String> denyList = LAMSUploadServlet.deniedExtensions.get(fileType);

	if (allowList.size() == 0) {
	    return !denyList.contains(extLower);
	}

	if (denyList.size() == 0) {
	    return allowList.contains(extLower);
	}

	return false;
    }
}