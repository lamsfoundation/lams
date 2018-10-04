/*
Copyright (c) 2003-2011, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

package org.lamsfoundation.lams.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.CommonConstants;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.FileValidatorUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Servlet to upload and browse files.<br>
 *
 * This servlet accepts 4 commands used to retrieve and create files and folders from a server directory. The allowed
 * commands are:
 * <ul>
 * <li>GetFolders: Retrive the list of directory under the current folder
 * <li>GetFoldersAndFiles: Retrive the list of files and directory under the current folder
 * <li>CreateFolder: Create a new directory under the current folder
 * <li>FileUpload: Send a new file to the server (must be sent with a POST)
 * </ul>
 *
 * This servlet has been modified for LAMS to support the lams_www/secure/[design folder] format. The design folder is a
 * folder with a unique numeric name. Whenever a new design is created (using "New" in the client), a new design folder
 * name is assigned. The [design folder] is passed in as DesignFolder and the CurrentFolder is the folder within the
 * DesignFolder (but not the type folder). This support the multi-level folders that are available in the File browser
 * but is different to the LAMSUploadServlet.
 *
 * @author Simone Chiaretta (simo@users.sourceforge.net)
 * @author Mitchell Seaton
 */
public class LAMSConnectorServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(LAMSConnectorServlet.class);

    private static String baseDir;
    private static boolean debug = false;

    private String realBaseDir;
    private String lamsContextPath;

    @Autowired
    @Qualifier("centralMessageService")
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
     * Initialize the servlet.<br>
     * Retrieve from the servlet configuration the "baseDir" which is the root of the file repository:<br>
     * If not specified the value of "/UserFiles/" will be used.
     */
    @Override
    public void init() throws ServletException {
	LAMSConnectorServlet.baseDir = getInitParameter("baseDir");
	debug = (new Boolean(getInitParameter("debug"))).booleanValue() && log.isDebugEnabled();

	if (LAMSConnectorServlet.baseDir == null) {
	    LAMSConnectorServlet.baseDir = "secure";
	}

	getConfigKeyValues();

	File baseFile = new File(realBaseDir);
	if (!baseFile.exists()) {
	    baseFile.mkdir();
	}
    }

    /**
     * Manage the Get requests (GetFolders, GetFoldersAndFiles, CreateFolder).<br>
     *
     * The servlet accepts commands sent in the following format:<br>
     * connector?Command=CommandName&Type=ResourceType&CurrentFolder=FolderPath<br>
     * <br>
     * It execute the command and then return the results to the client in XML format.
     *
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	if (LAMSConnectorServlet.debug) {
	    log.debug("Browse started");
	}

	// get realBaseDir and lamsContextPath at request time from config values in memory
	getConfigKeyValues();

	String commandStr = request.getParameter("Command");
	String typeStr = request.getParameter("Type");
	String currentFolderStr = request.getParameter("CurrentFolder");
	String designFolder = request.getParameter("DesignFolder");

	// create content directory if non-existant
	String currentDirPath = realBaseDir + designFolder + typeStr + "/" + currentFolderStr;
	String validCurrentDirPath = currentDirPath.replace('/', File.separatorChar);

	String currentWebPath = lamsContextPath + CommonConstants.LAMS_WWW_FOLDER + FileUtil.LAMS_WWW_SECURE_DIR
		+ designFolder + typeStr + "/" + currentFolderStr;

	File currentDir = new File(validCurrentDirPath);
	if (!currentDir.exists()) {
	    currentDir.mkdirs();
	}

	Document document = null;
	try {
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    document = builder.newDocument();
	} catch (ParserConfigurationException e) {
	    log.error(e);
	}

	Node root = CreateCommonXml(document, commandStr, typeStr, currentFolderStr, currentWebPath);

	if (LAMSConnectorServlet.debug) {
	    log.debug("Command = " + commandStr);
	}

	if (commandStr.equals("GetFolders")) {
	    getFolders(currentDir, root, document);
	} else if (commandStr.equals("GetFoldersAndFiles")) {
	    getFolders(currentDir, root, document);
	    getFiles(currentDir, root, document);
	} else if (commandStr.equals("CreateFolder")) {
	    String newFolderStr = request.getParameter("NewFolderName");
	    File newFolder = new File(currentDir, newFolderStr);
	    String retValue = "110";

	    if (newFolder.exists()) {
		retValue = "101";
	    } else {
		try {
		    boolean dirCreated = newFolder.mkdir();
		    if (dirCreated) {
			retValue = "0";
		    } else {
			retValue = "102";
		    }
		} catch (SecurityException sex) {
		    retValue = "103";
		}

	    }
	    setCreateFolderResponse(retValue, root, document);
	} else if (commandStr.equals("DeleteFile")) {
	    String fileName = request.getParameter("fileName");
	    File fileToDelete = new File(currentDir, fileName);

	    if (fileToDelete.isFile()) {
		fileToDelete.delete();
	    }

	} else if (commandStr.equals("DeleteFolder")) {
	    String folderName = request.getParameter("folderName");
	    File folderToDelete = new File(currentDir, folderName);

	    if (folderToDelete.isDirectory()) {
		folderToDelete.delete();
	    }
	}

	document.getDocumentElement().normalize();

	try {
	    TransformerFactory tFactory = TransformerFactory.newInstance();
	    Transformer transformer = tFactory.newTransformer();

	    DOMSource source = new DOMSource(document);

	    response.setContentType("text/xml; charset=UTF-8");
	    response.setHeader("Cache-Control", "no-cache");
	    PrintWriter out = response.getWriter();

	    StreamResult result = new StreamResult(out);
	    transformer.transform(source, result);

	    out.flush();
	    out.close();

	    if (LAMSConnectorServlet.debug) {
		StreamResult dbgResult = new StreamResult(System.out);
		transformer.transform(source, dbgResult);
		log.debug("Browse finished");
	    }
	} catch (Exception e) {
	    log.error(e);
	}
    }

    /**
     * Manage the Post requests (FileUpload).<br>
     *
     * The servlet accepts commands sent in the following format:<br>
     * connector?Command=FileUpload&Type=ResourceType&CurrentFolder=FolderPath<br>
     * <br>
     * It store the file (renaming it in case a file with the same name exists) and then return an HTML file with a
     * javascript command in it.
     *
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	if (LAMSConnectorServlet.debug) {
	    log.debug("Upload started");
	}

	String commandStr = request.getParameter("Command");
	String fileType = request.getParameter("Type");
	String currentFolderStr = request.getParameter("CurrentFolder");
	String designFolder = request.getParameter("DesignFolder");

	String currentDirPath = realBaseDir + designFolder + fileType + "/" + currentFolderStr;
	String validCurrentDirPath = currentDirPath.replace("/", File.separator);

	if (LAMSConnectorServlet.debug) {
	    log.debug(currentDirPath);
	}

	StringBuilder retVal = new StringBuilder();
	String newName = null;

	try {
	    if (commandStr.equals("PaintUpload")) {
		newName = createNewPaint(validCurrentDirPath, request, retVal);
	    } else if (!commandStr.equals("FileUpload") || currentFolderStr.equals("/-1/")) {
		throw new Exception("Illegal command.");
	    } else {
		newName = createNewFile(validCurrentDirPath, request, retVal, fileType);
	    }
	} catch (Exception e) {
	    log.error(e);
	    retVal = new StringBuilder("203");
	}

	response.setContentType("text/html; charset=UTF-8");
	response.setHeader("Cache-Control", "no-cache");
	PrintWriter out = response.getWriter();

	if (!commandStr.equals("PaintUpload")) {
	    out.println("<script type=\"text/javascript\">");
	    out.println("window.parent.frames['frmUpload'].OnUploadCompleted(" + retVal + ",'" + newName + "');");
	    out.println("</script>");

	} else {
	    // send back URL to new Paint file
	    String currentWebPath = lamsContextPath + CommonConstants.LAMS_WWW_FOLDER + FileUtil.LAMS_WWW_SECURE_DIR
		    + designFolder + fileType + "/" + currentFolderStr;
	    out.println(currentWebPath + newName);
	}

	out.flush();
	out.close();

	if (LAMSConnectorServlet.debug) {
	    log.debug("Upload finished");
	}
    }

    private String createNewFile(String validCurrentDirPath, HttpServletRequest request, StringBuilder retVal,
	    String fileType) throws FileUploadException, IOException, Exception {
	if (LAMSConnectorServlet.debug) {
	    log.debug("File save started");
	}

	DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
	ServletFileUpload fileUpload = new ServletFileUpload(fileItemFactory);
	List<FileItem> items = fileUpload.parseRequest(request);

	Map<String, Object> fields = new HashMap<>();
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
	String fileNameLong = uplFile.getName();
	fileNameLong = fileNameLong.replace('\\', '/');
	String[] pathParts = fileNameLong.split("/");
	String fileName = pathParts[pathParts.length - 1];

	// validate file size
	boolean maxFilesizeExceededMessage = FileValidatorUtil.validateFileSize(uplFile.getSize(), true);
	if (!maxFilesizeExceededMessage) {
	    //assign fileName an error message to be shown on a client side
	    fileName = centralMessageService.getMessage("errors.maxfilesize",
		    new Object[] { Configuration.getAsInt(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE) });
	    retVal.append("1");

	    // validate file extension
	} else if (!FileUtil.isExtensionAllowed(fileType, fileName)) {
	    if (LAMSConnectorServlet.debug) {
		log.debug("File extension is prohibited for upload " + fileName);
	    }

	    //will generate client-side alert message 'Invalid file type'
	    retVal.append("204");

	} else {
	    File pathToSave = new File(validCurrentDirPath, fileName);

	    int counter = 1;
	    while (pathToSave.exists()) {
		String ext = getExtension(fileName);
		String nameWithoutExt = LAMSConnectorServlet.getNameWithoutExtension(fileName);
		fileName = nameWithoutExt + "_" + counter + "." + ext;
		pathToSave = new File(validCurrentDirPath, fileName);
		counter++;
	    }

	    FileCopyUtils.copy(uplFile.getInputStream(), new FileOutputStream(pathToSave));

	    if (counter > 1) {
		retVal.append("201");
	    } else {
		retVal.append("0");
	    }

	    if (LAMSConnectorServlet.debug) {
		log.debug("File save finished");
	    }
	}

	return fileName;
    }

    private String createNewPaint(String validCurrentDirPath, HttpServletRequest request, StringBuilder retVal)
	    throws IOException {
	if (LAMSConnectorServlet.debug) {
	    log.debug("Paint save started");
	}

	String extension = "png";
	String nameWithoutExt = getUserID() + "_" + String.valueOf(new Date().getTime());
	String fileName = nameWithoutExt + "." + extension;

	File dir = new File(validCurrentDirPath);
	File pathToSave = new File(validCurrentDirPath, fileName);

	int counter = 1;
	while (pathToSave.exists()) {
	    fileName = nameWithoutExt + "_" + counter + "." + extension;
	    pathToSave = new File(validCurrentDirPath, fileName);
	    counter++;
	}

	if (!dir.exists()) {
	    dir.mkdirs();
	}

	InputStream is = request.getInputStream();
	FileOutputStream fileos = new FileOutputStream(pathToSave);

	byte[] bs = IOUtils.toByteArray(is);

	fileos.write(bs);

	fileos.flush();
	fileos.close();

	if (counter > 1) {
	    retVal.append("201");
	} else {
	    retVal.append("0");
	}

	if (LAMSConnectorServlet.debug) {
	    log.debug("Paint save finished");
	}

	return fileName;
    }

    private void setCreateFolderResponse(String retValue, Node root, Document doc) {
	Element myEl = doc.createElement("Error");
	myEl.setAttribute("number", retValue);
	root.appendChild(myEl);
    }

    private void getFolders(File dir, Node root, Document doc) {
	Element folders = doc.createElement("Folders");
	root.appendChild(folders);
	File[] fileList = dir.listFiles();
	for (int i = 0; i < fileList.length; ++i) {
	    if (fileList[i].isDirectory()) {
		Element myEl = doc.createElement("Folder");
		myEl.setAttribute("name", fileList[i].getName());
		folders.appendChild(myEl);
	    }
	}
    }

    private void getFiles(File dir, Node root, Document doc) {
	Element files = doc.createElement("Files");
	root.appendChild(files);
	File[] fileList = dir.listFiles();
	for (int i = 0; i < fileList.length; ++i) {
	    if (fileList[i].isFile()) {
		Element myEl = doc.createElement("File");
		myEl.setAttribute("name", fileList[i].getName());
		myEl.setAttribute("size", "" + fileList[i].length() / 1024);
		files.appendChild(myEl);
	    }
	}
    }

    private Node CreateCommonXml(Document doc, String commandStr, String typeStr, String currentPath,
	    String currentUrl) {

	Element root = doc.createElement("Connector");
	doc.appendChild(root);
	root.setAttribute("command", commandStr);
	root.setAttribute("resourceType", typeStr);

	Element myEl = doc.createElement("CurrentFolder");
	myEl.setAttribute("path", currentPath);
	myEl.setAttribute("url", currentUrl);
	root.appendChild(myEl);

	return root;

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
	return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private void getConfigKeyValues() {
	realBaseDir = Configuration.get(ConfigurationKeys.LAMS_EAR_DIR) + File.separator + FileUtil.LAMS_WWW_DIR
		+ File.separator + LAMSConnectorServlet.baseDir;
	lamsContextPath = "/" + Configuration.get(ConfigurationKeys.SERVER_URL_CONTEXT_PATH) + "/";
    }

    private String getUserID() {
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	return user != null ? user.getUserID().toString() : "";
    }
}