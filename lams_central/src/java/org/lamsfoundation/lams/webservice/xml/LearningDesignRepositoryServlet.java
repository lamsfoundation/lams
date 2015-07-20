package org.lamsfoundation.lams.webservice.xml;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.integration.ExtServerOrgMap;
import org.lamsfoundation.lams.integration.ExtUserUseridMap;
import org.lamsfoundation.lams.integration.UserInfoFetchException;
import org.lamsfoundation.lams.integration.UserInfoValidationException;
import org.lamsfoundation.lams.integration.security.AuthenticationException;
import org.lamsfoundation.lams.integration.security.Authenticator;
import org.lamsfoundation.lams.integration.service.IntegrationService;
import org.lamsfoundation.lams.integration.util.LoginRequestDispatcher;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;
import org.lamsfoundation.lams.usermanagement.exception.UserAccessDeniedException;
import org.lamsfoundation.lams.util.CentralConstants;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.wddx.FlashMessage;
import org.lamsfoundation.lams.util.wddx.WDDXProcessor;
import org.lamsfoundation.lams.workspace.dto.FolderContentDTO;
import org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService;
import org.lamsfoundation.lams.workspace.web.WorkspaceAction;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class LearningDesignRepositoryServlet extends HttpServlet {

    private static final long serialVersionUID = -4962711640290319063L;

    private static Logger log = Logger.getLogger(LearningDesignRepositoryServlet.class);

    private static IntegrationService integrationService = null;

    private static IWorkspaceManagementService service = null;

    private static MessageService msgService = null;

    private static IExportToolContentService exportToolContentService = null;

    private static final String PARAM_LEARING_DESIGN_ID = "learningDesignID";
    private static final String PARAM_EXPORT_FORMAT = "format";
    private static final String IMS_XSLT_NAME = "learning-design-ims.xslt";
    private static final String IMS_XSLT_PATH = "/toolcontent";

    /**
     * Constructor of the object.
     */
    public LearningDesignRepositoryServlet() {
	super();
    }

    /**
     * Destruction of the servlet. <br>
     */
    public void destroy() {
	super.destroy(); // Just puts "destroy" string in log
	// Put your code here
    }

    // TODO, this is common with the webservices class, we should move it from
    // here.
    private static class ContentTreeNode {
	FolderContentDTO content;

	List<ContentTreeNode> children;

	ContentTreeNode(FolderContentDTO content) {
	    this.content = content;
	    children = new LinkedList<ContentTreeNode>();
	}

	List<ContentTreeNode> getChildren() {
	    return children;
	}

	void setChildren(List<ContentTreeNode> children) {
	    this.children = children;
	}

	FolderContentDTO getContent() {
	    return content;
	}

	void setContent(FolderContentDTO content) {
	    this.content = content;
	}

	void addChild(FolderContentDTO content) {
	    children.add(new ContentTreeNode(content));
	}

	void addChild(ContentTreeNode node) {
	    children.add(node);
	}

	/**
	 * the format should be something like this: [ ['My Workspace', null,
	 * ['Mary Morgan Folder', null, ['3 activity sequence','1024'] ],
	 * ['Organisations', null, ['Developers Playpen', null, ['Lesson
	 * Sequence Folder', null, ['',null] ] ], ['MATH111', null, ['Lesson
	 * Sequence Folder', null, ['',null] ] ] ] ] ]
	 */
	public String toString() {
	    // return '[' + convert() + ']';

	    Document document = getDocument();

	    try {
		DOMSource domSource = new DOMSource(document);
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.transform(domSource, result);
		return writer.toString();
	    } catch (TransformerException ex) {
		ex.printStackTrace();
		return null;
	    }
	}

	String convert() {
	    StringBuilder sb = new StringBuilder();
	    if (content.getResourceType().equals(FolderContentDTO.FOLDER)) {
		sb.append("['");
		sb.append(content.getName()).append("',").append("null").append(',');
		if (children.size() == 0) {
		    sb.append("['',null]");
		} else {
		    sb.append(children.get(0).convert());
		    for (int i = 1; i < children.size(); i++) {
			sb.append(',').append(children.get(i).convert());
		    }
		}
		sb.append(']');
	    } else if (content.getResourceType().equals(FolderContentDTO.DESIGN)) {
		sb.append('[');
		sb.append('\'').append(content.getName()).append('\'').append(',').append('\'').append(
			content.getResourceID()).append('\'');
		sb.append(']');
	    }
	    return sb.toString();
	}

	public Document getDocument() {
	    try {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.newDocument();

		Element element = createXML(document);

		document.appendChild(element); // TODO null values ??

		return document;

	    } catch (Exception e) {
		e.printStackTrace();
	    }

	    return null;
	}

	public Element createXML(Document document) {
	    Element elem = null;
	    if (content.getResourceType().equals(FolderContentDTO.FOLDER)) {
		elem = document.createElement(CentralConstants.ELEM_FOLDER);
		elem.setAttribute(CentralConstants.ATTR_NAME, content.getName());

		if (children.size() > 0) {
		    for (int i = 0; i < children.size(); i++) {
			elem.appendChild(children.get(i).createXML(document));
		    }
		}

	    } else if (content.getResourceType().equals(FolderContentDTO.DESIGN)) {
		elem = document.createElement(CentralConstants.ELEM_LEARNING_DESIGN);
		elem.setAttribute(CentralConstants.ATTR_NAME, content.getName());
		elem.setAttribute(CentralConstants.ATTR_RESOURCE_ID, content.getResourceID().toString());
	    } else {
		// TODO ERROR shouldnt get here !!
	    }

	    return elem;
	}
    }

    private ContentTreeNode buildContentTree(Integer userId, Integer mode) throws IOException,
	    UserAccessDeniedException, RepositoryCheckedException {
	log.debug("User Id - " + userId);
	FolderContentDTO rootFolder = new FolderContentDTO(msgService.getMessage("label.workspace.root_folder"),
		msgService.getMessage("folder"), null, null, FolderContentDTO.FOLDER,
		WorkspaceAction.BOOTSTRAP_FOLDER_ID.longValue(), WorkspaceFolder.READ_ACCESS, null);
	ContentTreeNode root = new ContentTreeNode(rootFolder);
	FolderContentDTO userFolder = service.getUserWorkspaceFolder(userId);
	root.addChild(buildContentTreeNode(userFolder, userId, mode));
	
	FolderContentDTO dummyOrgFolder = new FolderContentDTO(msgService.getMessage("organisations"), msgService
		.getMessage("folder"), null, null, FolderContentDTO.FOLDER, new Long(WorkspaceAction.ORG_FOLDER_ID
		.longValue()), WorkspaceFolder.READ_ACCESS, null);
	ContentTreeNode dummyOrgNode = new ContentTreeNode(dummyOrgFolder);
	// tried using service.getAccessibleOrganisationWorkspaceFolders(userId)
	// api,
	// but it doesn't work, the userOrganisations set of the user
	// got from workspaceManagementService with the userId supplied is
	// empty, which
	// is not true.
	Vector orgFolders = service.getAccessibleOrganisationWorkspaceFolders(userId);
	for (int i = 0; i < orgFolders.size(); i++) {
	    FolderContentDTO orgFolder = (FolderContentDTO) orgFolders.get(i);
	    dummyOrgNode.addChild(buildContentTreeNode(orgFolder, userId, mode));
	}
	root.addChild(dummyOrgNode);
	
	FolderContentDTO publicFolder = service.getPublicWorkspaceFolder(userId);
	if (publicFolder != null) {
	    root.addChild(buildContentTreeNode(publicFolder, userId, mode));
	}
	return root;
    }

    private ContentTreeNode buildContentTreeNode(FolderContentDTO folder, Integer userId, Integer mode)
	    throws UserAccessDeniedException, RepositoryCheckedException {
	log.debug("build content tree node for folder - " + folder.getName());
	ContentTreeNode node = new ContentTreeNode(folder);
	if (folder.getResourceType().equals(FolderContentDTO.FOLDER)) {
	    log.debug(folder.getName() + " is a folder");
	    WorkspaceFolder wsfolder = service.getWorkspaceFolder(folder.getResourceID().intValue());
	    Vector items = service.getFolderContentsExcludeHome(userId, wsfolder, mode);
	    for (int i = 0; i < items.size(); i++) {
		FolderContentDTO content = (FolderContentDTO) items.get(i);
		node.addChild(buildContentTreeNode(content, userId, mode));
	    }
	}
	return node;
    }

    /**
     * The doGet method of the servlet. <br>
     * 
     * This method is called when a form has its tag value method equals to get.
     * 
     * @param request
     *                the request send by the client to the server
     * @param response
     *                the response send by the server to the client
     * @throws ServletException
     *                 if an error occurred
     * @throws IOException
     *                 if an error occurred
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	try {
	    // get parameters
	    String serverId = request.getParameter(CentralConstants.PARAM_SERVER_ID);
	    String datetime = request.getParameter(CentralConstants.PARAM_DATE_TIME);
	    String hashValue = request.getParameter(CentralConstants.PARAM_HASH_VALUE);
	    String username = request.getParameter(CentralConstants.PARAM_USERNAME);
	    String courseId = request.getParameter(CentralConstants.PARAM_COURSE_ID);
	    String courseName = request.getParameter(CentralConstants.PARAM_COURSE_NAME);
	    String country = request.getParameter(CentralConstants.PARAM_COUNTRY);
	    String lang = request.getParameter(CentralConstants.PARAM_LANG);
	    Integer mode = WebUtil.readIntParam(request, CentralConstants.PARAM_MODE, true);
	    String method = request.getParameter(CentralConstants.PARAM_METHOD);
	    String usePrefix = request.getParameter(CentralConstants.PARAM_USE_PREFIX);
	    final boolean isUpdateUserDetails = false;
	    
	    String firstName = request.getParameter(LoginRequestDispatcher.PARAM_FIRST_NAME);
	    String lastName = request.getParameter(LoginRequestDispatcher.PARAM_LAST_NAME);
	    String email = request.getParameter(LoginRequestDispatcher.PARAM_EMAIL);
	    
	    if (serverId == null || datetime == null || hashValue == null || username == null || courseId == null
		    || country == null || lang == null ) {
		String msg = "Parameters missing";
		log.error(msg);
		response.sendError(response.SC_BAD_REQUEST, "Parameters missing");
	    }
	    
	    // LDEV-2196 preserve character encoding if necessary
	    if (request.getCharacterEncoding() == null) {
	    	log.debug("request.getCharacterEncoding is empty, parsing username and courseName as 8859_1 to UTF-8...");
	    	username = new String(username.getBytes("8859_1"), "UTF-8");
	    	if (courseName != null) {
	    		courseName = new String(courseName.getBytes("8859_1"), "UTF-8");
	    	}
	    }

	    // get Server map
	    ExtServerOrgMap serverMap = integrationService.getExtServerOrgMap(serverId);

	    // authenticate
	    Authenticator.authenticate(serverMap, datetime, username, hashValue);

	    // get user map, user is created if this is their first use

	    if (method != null && method.equals("exportLD")) {
		// do export
		exportLD(request, response);
		
	    } else if (method != null && ( method.equals("getLearningDesignsJSON") || method.equals("getPagedHomeLearningDesignsJSON")) ) {

    		Integer userId = getUserId(username, courseId, courseName, country, lang, usePrefix,
			isUpdateUserDetails, firstName, lastName, email, serverMap);
        
       		boolean allowInvalidDesigns = WebUtil.readBooleanParam(request, "allowInvalidDesigns", false);
       		 
       		String folderContentsJSON = null;
       		if ( method.equals("getLearningDesignsJSON") ) {
       		    Integer folderID = WebUtil.readIntParam(request, "folderID", true);
       		    folderContentsJSON = service.getFolderContentsJSON(folderID, userId, allowInvalidDesigns);
       		} else {
       		    Integer folderID = WebUtil.readIntParam(request, "folderID", true);
       		    Integer page = WebUtil.readIntParam(request, "page", true);
       		    Integer size = WebUtil.readIntParam(request, "size", true);
       		    String sortName = request.getParameter("sortName");
       		    String sortDate = request.getParameter("sortDate");
       		    String search = request.getParameter("search");
       		    folderContentsJSON = service.getPagedLearningDesignsJSON(userId, allowInvalidDesigns, search, page, size, 
       			sortName == null ? null : ( sortName.equals("0") ? "DESC" : "ASC" ),
       			sortDate == null ? null : ( sortDate.equals("0") ? "DESC" : "ASC" ));
       		}

       		log.debug("LearningDesignRepositoryServlet returning "+folderContentsJSON);
       		
        	response.setContentType("application/json;charset=UTF-8");
        	response.getWriter().write(folderContentsJSON);

	    } else if (method != null && method.equals("deleteLearningDesignJSON") ) {
		
		Integer userId = getUserId(username, courseId, courseName, country, lang, usePrefix,
			isUpdateUserDetails, firstName, lastName, email, serverMap);
		
		Long learningDesignId = WebUtil.readLongParam(request, PARAM_LEARING_DESIGN_ID);
		log.debug("User "+userId+" "+username+" deleting learning design "+learningDesignId);
		String wddxResponse = service.deleteResource(learningDesignId, FolderContentDTO.DESIGN, userId);
		Hashtable table = (Hashtable) WDDXProcessor.deserialize(wddxResponse);

		log.debug("Delete response "+wddxResponse);

		Double messageTypeDouble = (Double) table.get("messageType");
		int messageType = messageTypeDouble != null ? messageTypeDouble.intValue() : 0;
		if ( messageType == FlashMessage.OBJECT_MESSAGE ) {
		    JSONObject jsonObject = new JSONObject(table);
		    response.setContentType("application/json;charset=utf-8");
		    response.getWriter().print(jsonObject.toString());

		} else {
		    log.error("Unable to delete learning design "+learningDesignId+" for user "+userId+" error "+table.get("messageValue"));
		    response.sendError(response.SC_INTERNAL_SERVER_ERROR, table.get("messageValue").toString());
		}

	    //TODO remove the next else-paragraph as soon as all integrations will start using new method. (After this also stop checking for (method != null && method.equals("getLearningDesignsJSONFormat")))
	    } else {

		if ( mode == null) {
		    String msg = "Parameter missing: mode";
		    log.error(msg);
		    response.sendError(response.SC_BAD_REQUEST, msg);
		}

		ExtUserUseridMap userMap = null;
		boolean prefix = usePrefix == null ? true : Boolean.parseBoolean(usePrefix);
		if (firstName == null && lastName == null) {
		    userMap = integrationService.getExtUserUseridMap(serverMap, username, prefix);
		} else {
		    userMap = integrationService.getImplicitExtUserUseridMap(serverMap, username, firstName, lastName,
			    lang, country, email, prefix, isUpdateUserDetails);
		}

		// create group for external course if necessary
		integrationService.getExtCourseClassMap(serverMap, userMap, courseId, country, lang, courseName, LoginRequestDispatcher.METHOD_AUTHOR);
		Integer userId = userMap.getUser().getUserId();

		String contentTree = buildContentTree(userId, mode).toString();

		// generate response
		response.setContentType("text/xml");
		response.setCharacterEncoding("UTF8");
		PrintWriter out = response.getWriter();

		out.print(contentTree);
	    }

	} catch (NumberFormatException nfe) {
	    log.error("mode is not an integer", nfe);
	    response.sendError(response.SC_BAD_REQUEST, "mode is not an integer");
	} catch (AuthenticationException e) {
	    log.error("can not authenticate", e);
	    response.sendError(response.SC_BAD_REQUEST, "can not authenticate");
	} catch (UserInfoFetchException e) {
	    log.error("can not retrieve user information", e);
	    response.sendError(response.SC_BAD_REQUEST, "can not retrieve user information");
	} catch (UserAccessDeniedException e) {
	    log.error("user access denied", e);
	    response.sendError(response.SC_BAD_REQUEST, "user access denied");
	} catch (RepositoryCheckedException e) {
	    log.error("repository checked", e);
	    response.sendError(response.SC_BAD_REQUEST, "repository checked");
	} catch (Exception e) {
	    log.error("Problem with LearningDesignRepositoryServlet request", e);
	    response.sendError(response.SC_BAD_REQUEST, "Problem with LearningDesignRepositoryServlet request");
	}

    }

    private Integer getUserId(String username, String courseId, String courseName, String country, String lang,
	    String usePrefix, final boolean isUpdateUserDetails, String firstName, String lastName, String email,
	    ExtServerOrgMap serverMap) throws UserInfoFetchException, UserInfoValidationException {
	ExtUserUseridMap userMap = null;
	boolean prefix = usePrefix == null ? true : Boolean.parseBoolean(usePrefix);
	if (firstName == null && lastName == null) {
	    userMap = integrationService.getExtUserUseridMap(serverMap, username, prefix);
	} else {
	    userMap = integrationService.getImplicitExtUserUseridMap(serverMap, username, firstName, lastName,
		    lang, country, email, prefix, isUpdateUserDetails);
	}
      
	// create group for external course if necessary
	integrationService.getExtCourseClassMap(serverMap, userMap, courseId, country, lang, courseName, LoginRequestDispatcher.METHOD_AUTHOR);
	Integer userId = userMap.getUser().getUserId();
	return userId;
    }
        
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doGet(request, response);
    }

    public void exportLD(HttpServletRequest request, HttpServletResponse response) {
	Long learningDesignId = WebUtil.readLongParam(request, PARAM_LEARING_DESIGN_ID);
	int format = WebUtil.readIntParam(request, PARAM_EXPORT_FORMAT);
	List<String> toolsErrorMsgs = new ArrayList<String>();

	try {
	    File xslt = new File(getServletContext().getRealPath(IMS_XSLT_PATH) + File.separator + IMS_XSLT_NAME);
	    String zipFilename = exportToolContentService.exportLearningDesign(learningDesignId, toolsErrorMsgs,
		    format, xslt);

	    // get only filename
	    String zipfile = FileUtil.getFileName(zipFilename);

	    // replace spaces (" ") with underscores ("_")
	    zipfile = zipfile.replaceAll(" ", "_");

	    // Different browsers handle stream downloads differently LDEV-1243
	    String filename = FileUtil.encodeFilenameForDownload(request, zipfile);
	    log.debug("Final filename to export: " + filename);

	    response.setContentType("application/x-download");
	    //			response.setContentType("application/zip");
	    response.setHeader("Content-Disposition", "attachment;filename=" + filename);
	    InputStream in = null;
	    OutputStream out = null;
	    try {
		in = new BufferedInputStream(new FileInputStream(zipFilename));
		out = response.getOutputStream();
		int count = 0;

		int ch;
		while ((ch = in.read()) != -1) {
		    out.write((char) ch);
		    count++;
		}
		log.debug("Wrote out " + count + " bytes");
		response.setContentLength(count);
		out.flush();
	    } catch (Exception e) {
		log.error("Exception occured writing out file:" + e.getMessage());
		throw new ExportToolContentException(e);
	    } finally {
		try {
		    if (in != null)
			in.close(); // very important
		    if (out != null)
			out.close();
		} catch (Exception e) {
		    log.error("Error Closing file. File already written out - no exception being thrown.", e);
		}
	    }
	} catch (Exception e1) {
	    log.error("Unable to export tool content to external integrated server: " + e1.toString());
	}
    }

    /**
     * Initialization of the servlet. <br>
     * 
     * @throws ServletException
     *                 if an error occure
     */
    public void init() throws ServletException {
	integrationService = (IntegrationService) WebApplicationContextUtils.getRequiredWebApplicationContext(
		getServletContext()).getBean("integrationService");

	service = (IWorkspaceManagementService) WebApplicationContextUtils.getRequiredWebApplicationContext(
		getServletContext()).getBean("workspaceManagementService");

	msgService = service.getMessageService();

	exportToolContentService = (IExportToolContentService) WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServletContext()).getBean("exportToolContentService");
    }
}
