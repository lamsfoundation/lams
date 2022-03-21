package org.lamsfoundation.lams.webservice.xml;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletConfig;
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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.exception.RepositoryCheckedException;
import org.lamsfoundation.lams.integration.ExtServer;
import org.lamsfoundation.lams.integration.ExtUserUseridMap;
import org.lamsfoundation.lams.integration.UserInfoFetchException;
import org.lamsfoundation.lams.integration.UserInfoValidationException;
import org.lamsfoundation.lams.integration.security.AuthenticationException;
import org.lamsfoundation.lams.integration.security.Authenticator;
import org.lamsfoundation.lams.integration.service.IntegrationService;
import org.lamsfoundation.lams.integration.util.IntegrationConstants;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;
import org.lamsfoundation.lams.usermanagement.exception.UserAccessDeniedException;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.CentralConstants;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.workspace.dto.FolderContentDTO;
import org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService;
import org.lamsfoundation.lams.workspace.web.WorkspaceController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class LearningDesignRepositoryServlet extends HttpServlet {
    private static final long serialVersionUID = -4962711640290319063L;
    private static Logger log = Logger.getLogger(LearningDesignRepositoryServlet.class);
    private static final String PARAM_LEARING_DESIGN_ID = "learningDesignID";

    @Autowired
    private IntegrationService integrationService;
    @Autowired
    private IWorkspaceManagementService workspaceManagementService;
    @Autowired
    private MessageService centralMessageService;
    @Autowired
    private IExportToolContentService exportToolContentService;
    @Autowired
    private IUserManagementService userManagementService;

    /**
     * Constructor of the object.
     */
    public LearningDesignRepositoryServlet() {
	super();
    }

    /**
     * Destruction of the servlet. <br>
     */
    @Override
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
	    children = new LinkedList<>();
	}

	void addChild(ContentTreeNode node) {
	    children.add(node);
	}

	/**
	 * the format should be something like this: [ ['My Workspace', null, ['Mary Morgan Folder', null, ['3 activity
	 * sequence','1024'] ], ['Organisations', null, ['Developers Playpen', null, ['Lesson Sequence Folder', null,
	 * ['',null] ] ], ['MATH111', null, ['Lesson Sequence Folder', null, ['',null] ] ] ] ] ]
	 */
	@Override
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

    private ContentTreeNode buildContentTree(Integer userId, Integer mode)
	    throws IOException, UserAccessDeniedException, RepositoryCheckedException {
	log.debug("User Id - " + userId);
	FolderContentDTO rootFolder = new FolderContentDTO(
		centralMessageService.getMessage("label.workspace.root_folder"),
		centralMessageService.getMessage("folder"), null, null, FolderContentDTO.FOLDER,
		WorkspaceController.BOOTSTRAP_FOLDER_ID.longValue(), WorkspaceFolder.READ_ACCESS, null);
	ContentTreeNode root = new ContentTreeNode(rootFolder);
	FolderContentDTO userFolder = workspaceManagementService.getUserWorkspaceFolder(userId);
	root.addChild(buildContentTreeNode(userFolder, userId, mode));

	FolderContentDTO dummyOrgFolder = new FolderContentDTO(centralMessageService.getMessage("organisations"),
		centralMessageService.getMessage("folder"), null, null, FolderContentDTO.FOLDER,
		new Long(WorkspaceController.ORG_FOLDER_ID.longValue()), WorkspaceFolder.READ_ACCESS, null);
	ContentTreeNode dummyOrgNode = new ContentTreeNode(dummyOrgFolder);
	// tried using service.getAccessibleOrganisationWorkspaceFolders(userId)
	// api,
	// but it doesn't work, the userOrganisations set of the user
	// got from workspaceManagementService with the userId supplied is
	// empty, which
	// is not true.
	Vector orgFolders = workspaceManagementService.getAccessibleOrganisationWorkspaceFolders(userId);
	for (int i = 0; i < orgFolders.size(); i++) {
	    FolderContentDTO orgFolder = (FolderContentDTO) orgFolders.get(i);
	    dummyOrgNode.addChild(buildContentTreeNode(orgFolder, userId, mode));
	}
	root.addChild(dummyOrgNode);

	FolderContentDTO publicFolder = workspaceManagementService.getPublicWorkspaceFolder(userId);
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
	    WorkspaceFolder wsfolder = workspaceManagementService.getWorkspaceFolder(folder.getResourceID().intValue());
	    Vector items = workspaceManagementService.getFolderContentsExcludeHome(userId, wsfolder, mode);
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
     *            the request send by the client to the server
     * @param response
     *            the response send by the server to the client
     * @throws ServletException
     *             if an error occurred
     * @throws IOException
     *             if an error occurred
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	try {
	    boolean isAppAdmin = false;
	    String remoteUser = request.getRemoteUser();
	    if (StringUtils.isNotBlank(remoteUser)) {
		User user = userManagementService.getUserByLogin(remoteUser);
		if (user != null) {
		    isAppAdmin = userManagementService.isUserInRole(user.getUserId(),
			    userManagementService.getRootOrganisation().getOrganisationId(), Role.APPADMIN);
		}
	    }
	    // get parameters
	    String serverId = request.getParameter(CentralConstants.PARAM_SERVER_ID);
	    String datetime = request.getParameter(CentralConstants.PARAM_DATE_TIME);
	    String hashValue = request.getParameter(CentralConstants.PARAM_HASH_VALUE);
	    String username = request.getParameter(CentralConstants.PARAM_USERNAME);
	    String courseId = request.getParameter(CentralConstants.PARAM_COURSE_ID);
	    String courseName = request.getParameter(CentralConstants.PARAM_COURSE_NAME);
	    String country = request.getParameter(CentralConstants.PARAM_COUNTRY);
	    String locale = request.getParameter(CentralConstants.PARAM_LANG);
	    Integer mode = WebUtil.readIntParam(request, CentralConstants.PARAM_MODE, true);
	    String usePrefix = request.getParameter(CentralConstants.PARAM_USE_PREFIX);
	    String method = request.getParameter(CentralConstants.PARAM_METHOD);
	    String firstName = request.getParameter(IntegrationConstants.PARAM_FIRST_NAME);
	    String lastName = request.getParameter(IntegrationConstants.PARAM_LAST_NAME);
	    String email = request.getParameter(IntegrationConstants.PARAM_EMAIL);

	    final boolean isUpdateUserDetails = false;
	    ExtServer extServer = null;

	    if (!isAppAdmin) {
		if ((serverId == null) || (datetime == null) || (hashValue == null) || (username == null)
			|| (courseId == null) || (country == null) || (locale == null)) {
		    String msg = "Parameters missing";
		    log.error(msg);
		    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parameters missing");
		}

		// LDEV-2196 preserve character encoding if necessary
		if (request.getCharacterEncoding() == null) {
		    log.debug(
			    "request.getCharacterEncoding is empty, parsing username and courseName as 8859_1 to UTF-8...");
		    username = new String(username.getBytes("8859_1"), "UTF-8");
		    if (courseName != null) {
			courseName = new String(courseName.getBytes("8859_1"), "UTF-8");
		    }
		}

		// get Server map
		extServer = integrationService.getExtServer(serverId);

		// authenticate
		Authenticator.authenticate(extServer, datetime, username, hashValue);
	    }

	    // get user map, user is created if this is their first use

	    if ((method != null) && method.equals("exportLD")) {
		// do export
		exportLD(request, response, isAppAdmin);

	    } else if ((method != null)
		    && (method.equals("getLearningDesignsJSON") || method.equals("getPagedHomeLearningDesignsJSON"))) {

		Integer userId = getUserId(username, courseId, courseName, locale, country, usePrefix,
			isUpdateUserDetails, firstName, lastName, email, extServer);

		boolean allowInvalidDesigns = WebUtil.readBooleanParam(request, "allowInvalidDesigns", false);

		String folderContentsJSON = null;
		if (method.equals("getLearningDesignsJSON")) {
		    Integer folderID = WebUtil.readIntParam(request, "folderID", true);
		    String designType = request.getParameter("type");
		    folderContentsJSON = workspaceManagementService.getFolderContentsJSON(folderID, userId,
			    allowInvalidDesigns, designType);
		} else {
		    Integer page = WebUtil.readIntParam(request, "page", true);
		    Integer size = WebUtil.readIntParam(request, "size", true);
		    String sortName = request.getParameter("sortName");
		    String sortDate = request.getParameter("sortDate");
		    String search = request.getParameter("search");
		    folderContentsJSON = workspaceManagementService.getPagedLearningDesignsJSON(userId,
			    allowInvalidDesigns, search, page, size,
			    sortName == null ? null : (sortName.equals("0") ? "DESC" : "ASC"),
			    sortDate == null ? null : (sortDate.equals("0") ? "DESC" : "ASC"));
		}

		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(folderContentsJSON);

	    } else if ((method != null) && method.equals("deleteLearningDesignJSON")) {

		Integer userId = getUserId(username, courseId, courseName, locale, country, usePrefix,
			isUpdateUserDetails, firstName, lastName, email, extServer);

		Long learningDesignId = WebUtil.readLongParam(request, PARAM_LEARING_DESIGN_ID);
		log.debug("User " + userId + " " + username + " deleting learning design " + learningDesignId);
		// if OK then just return HTTP 200, otherwise an exception will result in error code
		workspaceManagementService.deleteResource(learningDesignId, FolderContentDTO.DESIGN, userId);

		//TODO remove the next else-paragraph as soon as all integrations will start using new method. (After this also stop checking for (method != null && method.equals("getLearningDesignsJSONFormat")))
	    } else {

		if (mode == null) {
		    String msg = "Parameter missing: mode";
		    log.error(msg);
		    response.sendError(HttpServletResponse.SC_BAD_REQUEST, msg);
		}

		ExtUserUseridMap userMap = null;
		boolean prefix = usePrefix == null ? true : Boolean.parseBoolean(usePrefix);
		if ((firstName == null) && (lastName == null)) {
		    userMap = integrationService.getExtUserUseridMap(extServer, username, prefix);
		} else {
		    userMap = integrationService.getImplicitExtUserUseridMap(extServer, username, firstName, lastName,
			    locale, country, email, prefix, isUpdateUserDetails);
		}

		// create group for external course if necessary
		integrationService.getExtCourseClassMap(extServer, userMap, courseId, courseName,
			IntegrationConstants.METHOD_AUTHOR);
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
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "mode is not an integer");
	} catch (AuthenticationException e) {
	    log.error("can not authenticate", e);
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "can not authenticate");
	} catch (UserInfoFetchException e) {
	    log.error("can not retrieve user information", e);
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "can not retrieve user information");
	} catch (UserAccessDeniedException e) {
	    log.error("user access denied", e);
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "user access denied");
	} catch (RepositoryCheckedException e) {
	    log.error("repository checked", e);
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "repository checked");
	} catch (Exception e) {
	    log.error("Problem with LearningDesignRepositoryServlet request", e);
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST,
		    "Problem with LearningDesignRepositoryServlet request");
	}

    }

    private Integer getUserId(String username, String courseId, String courseName, String locale, String country,
	    String usePrefix, final boolean isUpdateUserDetails, String firstName, String lastName, String email,
	    ExtServer extServer) throws UserInfoFetchException, UserInfoValidationException {
	ExtUserUseridMap userMap = null;
	boolean prefix = usePrefix == null ? true : Boolean.parseBoolean(usePrefix);
	if ((firstName == null) && (lastName == null)) {
	    userMap = integrationService.getExtUserUseridMap(extServer, username, prefix);
	} else {
	    userMap = integrationService.getImplicitExtUserUseridMap(extServer, username, firstName, lastName, locale,
		    country, email, prefix, isUpdateUserDetails);
	}

	// create group for external course if necessary
	integrationService.getExtCourseClassMap(extServer, userMap, courseId, courseName,
		IntegrationConstants.METHOD_AUTHOR);
	Integer userId = userMap.getUser().getUserId();
	return userId;
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	doGet(request, response);
    }

    public void exportLD(HttpServletRequest request, HttpServletResponse response, boolean isAppAdmin) {
	Long learningDesignId = WebUtil.readLongParam(request, PARAM_LEARING_DESIGN_ID);
	boolean getPathOnly = isAppAdmin && WebUtil.readBooleanParam(request, "getPathOnly", false);

	List<String> toolsErrorMsgs = new ArrayList<>();

	try {
	    String zipFilename = exportToolContentService.exportLearningDesign(learningDesignId, toolsErrorMsgs);

	    if (getPathOnly) {
		// send only path, not the actual zip file content
		response.setContentType("text/plain");
		response.getWriter().write(zipFilename);
		return;
	    }

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
		    if (in != null) {
			in.close(); // very important
		    }
		    if (out != null) {
			out.close();
		    }
		} catch (Exception e) {
		    log.error("Error Closing file. File already written out - no exception being thrown.", e);
		}
	    }
	} catch (Exception e1) {
	    log.error("Unable to export tool content to external integrated server: " + e1.toString());
	}
    }

    /*
     * Request Spring to lookup the applicationContext tied to the current ServletContext and inject service beans
     * available in that applicationContext.
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
	super.init(config);
	SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
    }
}
