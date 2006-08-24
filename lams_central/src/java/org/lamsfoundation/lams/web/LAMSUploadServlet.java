/*
 * FCKeditor - The text editor for internet
 * Copyright (C) 2003-2005 Frederico Caldeira Knabben
 * 
 * Licensed under the terms of the GNU Lesser General Public License:
 * 		http://www.opensource.org/licenses/lgpl-license.php
 * 
 * For further information visit:
 * 		http://www.fckeditor.net/
 * 
 * File Name: ConnectorServlet.java
 * 	Java Connector for Resource Manager class.
 * 
 * Version:  2.3
 * Modified: 2005-08-11 16:29:00
 * 
 * File Authors:
 * 		Simone Chiaretta (simo@users.sourceforge.net)
 */ 
 
package org.lamsfoundation.lams.web;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;


import org.apache.commons.fileupload.*;
import org.lamsfoundation.lams.authoring.web.AuthoringConstants;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;


import javax.xml.parsers.*;
import org.w3c.dom.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource; 
import javax.xml.transform.stream.StreamResult; 

/**
 * Servlet to upload files.<br>
 *
 * This servlet accepts just file uploads, eventually with a parameter specifying file type
 *
 * @author Simone Chiaretta (simo@users.sourceforge.net)
 * @author Mitchell Seaton
 * 
 * @web:servlet name="SimpleUploader" load-on-startup = "1"
 * @web.servlet-init-param name = "baseDir"
 *                         value = "secure"
 * @web.servlet-init-param name = "debug"
 *                         value = "true"
 * @web.servlet-init-param name = "enabled"
 *                         value = "true"
 * @web.servlet-init-param name = "AllowedExtensionsFile"
 *                         value = ""
 * @web.servlet-init-param name = "DeniedExtensionsFile"
 *                         value = "php|php3|php5|phtml|asp|aspx|ascx|jsp|cfm|cfc|pl|bat|exe|dll|reg|cg" 
 * @web.servlet-init-param name = "AllowedExtensionsImage"
 *                         value = "jpg|gif|jpeg|png|bmp" 
 * @web.servlet-init-param name = "DeniedExtensionsImage"
 *                         value = ""  
 * @web.servlet-init-param name = "AllowedExtensionsFlash"
 *                         value = "swf|fla"    
 * @web.servlet-init-param name = "DeniedExtensionsFlash"
 *                         value = "" 
 * @web:servlet-mapping url-pattern="/fckeditor/editor/filemanager/upload/simpleuploader"
 * 
 */

public class LAMSUploadServlet extends HttpServlet {
	
	private static String baseDir;
	private String realBaseDir;
	private static boolean debug=false;
	private static boolean enabled=false;
	private static Hashtable allowedExtensions;
	private static Hashtable deniedExtensions;
	
	/**
	 * Initialize the servlet.<br>
	 * Retrieve from the servlet configuration the "baseDir" which is the root of the file repository:<br>
	 * If not specified the value of "/UserFiles/" will be used.<br>
	 * Also it retrieve all allowed and denied extensions to be handled.
	 *
	 */
	 public void init() throws ServletException {
	 	
	 	debug=(new Boolean(getInitParameter("debug"))).booleanValue();
	 	
	 	if(debug) System.out.println("\r\n---- SimpleUploaderServlet initialization started ----");
	 	
		baseDir=getInitParameter("baseDir");
		enabled=(new Boolean(getInitParameter("enabled"))).booleanValue();
		
		if(baseDir==null)
			baseDir="secure";
		
		realBaseDir = Configuration.get(ConfigurationKeys.LAMS_EAR_DIR) + File.separator + AuthoringConstants.LAMS_WWW_DIR + File.separator + baseDir;
		
		File baseFile=new File(realBaseDir);
		if(!baseFile.exists()){
			baseFile.mkdir();
		}
		
		allowedExtensions = new Hashtable(3);
		deniedExtensions = new Hashtable(3);
				
		allowedExtensions.put("File",stringToArrayList(getInitParameter("AllowedExtensionsFile")));
		deniedExtensions.put("File",stringToArrayList(getInitParameter("DeniedExtensionsFile")));

		allowedExtensions.put("Image",stringToArrayList(getInitParameter("AllowedExtensionsImage")));
		deniedExtensions.put("Image",stringToArrayList(getInitParameter("DeniedExtensionsImage")));
		
		allowedExtensions.put("Flash",stringToArrayList(getInitParameter("AllowedExtensionsFlash")));
		deniedExtensions.put("Flash",stringToArrayList(getInitParameter("DeniedExtensionsFlash")));
		
		if(debug) System.out.println("---- SimpleUploaderServlet initialization completed ----\r\n");
		
	}
	

	/**
	 * Manage the Upload requests.<br>
	 *
	 * The servlet accepts commands sent in the following format:<br>
	 * simpleUploader?Type=ResourceType<br><br>
	 * It store the file (renaming it in case a file with the same name exists) and then return an HTML file
	 * with a javascript command in it.
	 *
	 */	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		if (debug) System.out.println("--- BEGIN DOPOST ---");

		response.setContentType("text/html; charset=UTF-8");
		response.setHeader("Cache-Control","no-cache");
		PrintWriter out = response.getWriter();
		

		String typeStr=request.getParameter("Type");
		String currentFolderStr=request.getParameter("CurrentFolder");
		
		//String currentPath=baseDir+typeStr;
		//String currentDirPath=getServletContext().getRealPath(currentPath);
		//currentPath=request.getContextPath()+currentPath;
		
		//	create content directory if non-existant
		String currentDirPath=realBaseDir + currentFolderStr;
		String validCurrentDirPath = currentDirPath.replace('/', File.separatorChar);
		
		String currentWebPath= Configuration.get(ConfigurationKeys.SERVER_URL) + AuthoringConstants.LAMS_WWW_FOLDER + AuthoringConstants.LAMS_WWW_SECURE_DIR + currentFolderStr + typeStr;
		
		File currentContentDir=new File(validCurrentDirPath);
		if(!currentContentDir.exists()){
			currentContentDir.mkdir();
		}
		
		// create content type directory if non-existant
		validCurrentDirPath += typeStr;
		
		File currentDir=new File(validCurrentDirPath);
		if(!currentDir.exists()){
			currentDir.mkdir();
		}
		
		if (debug) System.out.println(currentDirPath);
		
		String retVal="0";
		String newName="";
		String fileUrl="";
		String errorMessage="";
		
		if(enabled) {		
			DiskFileUpload upload = new DiskFileUpload();
			try {
				List items = upload.parseRequest(request);
				
				Map fields=new HashMap();
				
				Iterator iter = items.iterator();
				while (iter.hasNext()) {
				    FileItem item = (FileItem) iter.next();
				    if (item.isFormField())
				    	fields.put(item.getFieldName(),item.getString());
				    else
				    	fields.put(item.getFieldName(),item);
				}
				FileItem uplFile=(FileItem)fields.get("NewFile");
				String fileNameLong=uplFile.getName();
				fileNameLong=fileNameLong.replace('\\','/');
				String[] pathParts=fileNameLong.split("/");
				String fileName=pathParts[pathParts.length-1];
				
				String nameWithoutExt=getNameWithoutExtension(fileName);
				String ext=getExtension(fileName);
				File pathToSave=new File(validCurrentDirPath,fileName);
				fileUrl=currentWebPath+'/'+fileName;
				if(extIsAllowed(typeStr,ext)) {
					int counter=1;
					while(pathToSave.exists()){
						newName=nameWithoutExt+"("+counter+")"+"."+ext;
						fileUrl=currentWebPath+'/'+newName;
						retVal="201";
						pathToSave=new File(currentDirPath,newName);
						counter++;
						}
					uplFile.write(pathToSave);
				}
				else {
					retVal="202";
					errorMessage="";
					if (debug) System.out.println("Invalid file type: " + ext);	
				}
			}catch (Exception ex) {
				if (debug) ex.printStackTrace();
				retVal="203";
			}
		}
		else {
			retVal="1";
			errorMessage="This file uploader is disabled. Please check the WEB-INF/web.xml file";
		}
		
		
		out.println("<script type=\"text/javascript\">");
		out.println("window.parent.OnUploadCompleted("+retVal+",'"+fileUrl+"','"+newName+"','"+errorMessage+"');");
		out.println("</script>");
		out.flush();
		out.close();
	
		if (debug) System.out.println("--- END DOPOST ---");	
		
	}


	/*
	 * This method was fixed after Kris Barnhoorn (kurioskronic) submitted SF bug #991489
	 */
  	private static String getNameWithoutExtension(String fileName) {
    		return fileName.substring(0, fileName.lastIndexOf("."));
    	}
    	
	/*
	 * This method was fixed after Kris Barnhoorn (kurioskronic) submitted SF bug #991489
	 */
	private String getExtension(String fileName) {
		return fileName.substring(fileName.lastIndexOf(".")+1);
	}



	/**
	 * Helper function to convert the configuration string to an ArrayList.
	 */
	 
	 private ArrayList stringToArrayList(String str) {
	 
	 if(debug) System.out.println(str);
	 String[] strArr=str.split("\\|");
	 	 
	 ArrayList tmp=new ArrayList();
	 if(str.length()>0) {
		 for(int i=0;i<strArr.length;++i) {
		 		if(debug) System.out.println(i +" - "+strArr[i]);
		 		tmp.add(strArr[i].toLowerCase());
			}
		}
		return tmp;
	 }


	/**
	 * Helper function to verify if a file extension is allowed or not allowed.
	 */
	 
	 private boolean extIsAllowed(String fileType, String ext) {
	 		
	 		ext=ext.toLowerCase();
	 		
	 		ArrayList allowList=(ArrayList)allowedExtensions.get(fileType);
	 		ArrayList denyList=(ArrayList)deniedExtensions.get(fileType);
	 		
	 		if(allowList.size()==0)
	 			if(denyList.contains(ext))
	 				return false;
	 			else
	 				return true;

	 		if(denyList.size()==0)
	 			if(allowList.contains(ext))
	 				return true;
	 			else
	 				return false;
	 
	 		return false;
	 }

}

