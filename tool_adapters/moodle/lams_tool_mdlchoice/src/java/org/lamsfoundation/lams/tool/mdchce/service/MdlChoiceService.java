/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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
/* $$Id$$ */

package org.lamsfoundation.lams.tool.mdchce.service;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.SortedMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.AccessDeniedException;
import org.lamsfoundation.lams.contentrepository.ICredentials;
import org.lamsfoundation.lams.contentrepository.ITicket;
import org.lamsfoundation.lams.contentrepository.LoginException;
import org.lamsfoundation.lams.contentrepository.WorkspaceNotFoundException;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.contentrepository.service.IRepositoryService;
import org.lamsfoundation.lams.contentrepository.service.RepositoryProxy;
import org.lamsfoundation.lams.contentrepository.service.SimpleCredentials;
import org.lamsfoundation.lams.integration.ExtServerOrgMap;
import org.lamsfoundation.lams.integration.UserInfoFetchException;
import org.lamsfoundation.lams.integration.service.IIntegrationService;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
import org.lamsfoundation.lams.tool.IToolVO;
import org.lamsfoundation.lams.tool.ToolAdapterContentManager;
import org.lamsfoundation.lams.tool.ToolContentImport102Manager;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.SessionDataExistsException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.mdchce.dao.IMdlChoiceConfigItemDAO;
import org.lamsfoundation.lams.tool.mdchce.dao.IMdlChoiceDAO;
import org.lamsfoundation.lams.tool.mdchce.dao.IMdlChoiceSessionDAO;
import org.lamsfoundation.lams.tool.mdchce.dao.IMdlChoiceUserDAO;
import org.lamsfoundation.lams.tool.mdchce.dto.MdlChoiceOutputDTO;
import org.lamsfoundation.lams.tool.mdchce.model.MdlChoice;
import org.lamsfoundation.lams.tool.mdchce.model.MdlChoiceConfigItem;
import org.lamsfoundation.lams.tool.mdchce.model.MdlChoiceSession;
import org.lamsfoundation.lams.tool.mdchce.model.MdlChoiceUser;
import org.lamsfoundation.lams.tool.mdchce.util.MdlChoiceConstants;
import org.lamsfoundation.lams.tool.mdchce.util.MdlChoiceException;
import org.lamsfoundation.lams.tool.mdchce.util.MdlChoiceToolContentHandler;
import org.lamsfoundation.lams.tool.mdchce.util.WebUtility;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.HashUtil;
import org.lamsfoundation.lams.util.audit.IAuditService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * An implementation of the IMdlChoiceService interface.
 * 
 * As a requirement, all LAMS tool's service bean must implement
 * ToolContentManager and ToolSessionManager.
 */

public class MdlChoiceService implements ToolSessionManager, ToolAdapterContentManager, IMdlChoiceService,
	ToolContentImport102Manager {

    static Logger logger = Logger.getLogger(MdlChoiceService.class.getName());

    public static final String CUSTOM_CSV_MAP_PARAM_USER = "user";
    public static final String CUSTOM_CSV_MAP_PARAM_COURSE = "course";
    public static final String CUSTOM_CSV_MAP_PARAM_SECTION = "section";

    public static final String EXT_SERVER_PARAM_USER = "un";
    public static final String EXT_SERVER_PARAM_COURSE = "cs";
    public static final String EXT_SERVER_PARAM_SECTION = "section";
    public static final String EXT_SERVER_PARAM_TIMESTAMP = "ts";
    public static final String EXT_SERVER_PARAM_HASH = "hs";
    public static final String EXT_SERVER_PARAM_METHOD = "method";
    public static final String EXT_SERVER_PARAM_EXT_TOOL_CONTENT_ID = "extToolContentID";
    public static final String EXT_SERVER_PARAM_UPLOAD_FILE = "upload_file";
    public static final String EXT_SERVER_PARAM_OUTPUT_NAME = "oname";

    private static final int EXPECTED_CSV_SIZE = 3;
    private static final String EXPECTED_CSV_FORM = "user,course";

    private IMdlChoiceDAO mdlChoiceDAO = null;

    private IMdlChoiceSessionDAO mdlChoiceSessionDAO = null;

    private IMdlChoiceUserDAO mdlChoiceUserDAO = null;

    private IMdlChoiceConfigItemDAO mdlChoiceConfigItemDAO = null;

    private ILearnerService learnerService;

    private ILamsToolService toolService;

    private IToolContentHandler mdlChoiceToolContentHandler = null;

    private IRepositoryService repositoryService = null;

    private IAuditService auditService = null;

    private IExportToolContentService exportContentService;

    private ICoreNotebookService coreNotebookService;

    private IIntegrationService integrationService;

    private MdlChoiceOutputFactory mdlChoiceOutputFactory;

    public MdlChoiceService() {
	super();
	// TODO Auto-generated constructor stub
    }

    /* ************ Methods from ToolSessionManager ************* */
    public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException {
	if (logger.isDebugEnabled()) {
	    logger.debug("entering method createToolSession:" + " toolSessionId = " + toolSessionId
		    + " toolSessionName = " + toolSessionName + " toolContentId = " + toolContentId);
	}

	MdlChoiceSession session = new MdlChoiceSession();
	session.setSessionId(toolSessionId);
	session.setSessionName(toolSessionName);

	// learner starts
	MdlChoice mdlChoice = mdlChoiceDAO.getByContentId(toolContentId);
	session.setMdlChoice(mdlChoice);

	try {
	    // Get the required params, then call the eternal server
	    HashMap<String, String> params = getRequiredExtServletParams(mdlChoice);
	    params.put(EXT_SERVER_PARAM_EXT_TOOL_CONTENT_ID, mdlChoice.getExtToolContentId().toString());
	    session.setExtSessionId(copyExternalToolContent(params));
	} catch (Exception e) {
	    logger.error("Failed to call external server to copy tool content" + e);
	    throw new ToolException("Failed to call external server to copy tool content" + e);
	}

	mdlChoiceSessionDAO.saveOrUpdate(session);
    }

    /**
     * Calls the external server to copy the content and return a new id
     * 
     * @param extToolContentId
     * @return
     */
    public Long copyExternalToolContent(HashMap<String, String> params) throws ToolException, Exception {

	String cloneServletUrl = mdlChoiceConfigItemDAO.getConfigItemByKey(
		MdlChoiceConfigItem.KEY_EXTERNAL_TOOL_SERVLET).getConfigValue();

	// add the method to the params
	params.put(EXT_SERVER_PARAM_METHOD, EXT_SERVER_METHOD_CLONE);

	// Make the request
	InputStream is = WebUtility.getResponseInputStreamFromExternalServer(cloneServletUrl, params);
	BufferedReader isReader = new BufferedReader(new InputStreamReader(is));
	String str = isReader.readLine();
	if (str == null) {
	    throw new UserInfoFetchException("Fail to clone choice in .LRN:"
		    + " - No data returned from external server");
	}

	return Long.parseLong(str);
    }

    public HashMap<String, String> getRequiredExtServletParams(String customCSV) {
	HashMap<String, String> params = new HashMap<String, String>();
	HashMap<String, String> paramsCSV = decodeCustomCSV(customCSV);
	params.put(EXT_SERVER_PARAM_COURSE, paramsCSV.get(CUSTOM_CSV_MAP_PARAM_COURSE));
	params.put(EXT_SERVER_PARAM_USER, paramsCSV.get(CUSTOM_CSV_MAP_PARAM_USER));
	params.put(EXT_SERVER_PARAM_SECTION, paramsCSV.get(CUSTOM_CSV_MAP_PARAM_SECTION));

	String timestamp = Long.toString(new Date().getTime());
	params.put(EXT_SERVER_PARAM_TIMESTAMP, timestamp);

	ExtServerOrgMap serverMap = this.getExtServerOrgMap();
	String hash = hash(serverMap, paramsCSV.get(CUSTOM_CSV_MAP_PARAM_USER), timestamp);
	params.put(EXT_SERVER_PARAM_HASH, hash);

	return params;
    }

    public HashMap<String, String> getRequiredExtServletParams(MdlChoice mdlchoice) {
	HashMap<String, String> params = new HashMap<String, String>();
	params.put(EXT_SERVER_PARAM_USER, mdlchoice.getExtUsername());
	params.put(EXT_SERVER_PARAM_COURSE, mdlchoice.getExtCourseId());
	params.put(EXT_SERVER_PARAM_SECTION, mdlchoice.getExtSection());

	String timestamp = Long.toString(new Date().getTime());
	params.put(EXT_SERVER_PARAM_TIMESTAMP, timestamp);

	ExtServerOrgMap serverMap = this.getExtServerOrgMap();
	String hash = hash(serverMap, mdlchoice.getExtUsername(), timestamp);
	params.put(EXT_SERVER_PARAM_HASH, hash);

	return params;
    }

    public HashMap<String, String> decodeCustomCSV(String customCSV) {
	HashMap<String, String> map = new HashMap<String, String>();
	if (customCSV != null) {
	    String[] split = customCSV.split(",");
	    if (split.length != EXPECTED_CSV_SIZE) {
		return null;
	    }
	    map.put(CUSTOM_CSV_MAP_PARAM_USER, split[0]);
	    map.put(CUSTOM_CSV_MAP_PARAM_COURSE, split[1]);
	    map.put(CUSTOM_CSV_MAP_PARAM_SECTION, split[2]);
	    return map;
	} else {
	    return null;
	}
    }

    public boolean getExternalToolOutputBoolean(String outputName, MdlChoice mdlChoice, Long userId, String extToolContentId,
	    Long toolSessionId, String optionID) {
	MdlChoiceUser user = this.getUserByUserIdAndSessionId(userId, toolSessionId);
	ExtServerOrgMap extServerMap = getExtServerOrgMap();

	String extUserName = user.getLoginName().substring(extServerMap.getPrefix().length() + 1);

	try {
	    String outputServletUrl = mdlChoiceConfigItemDAO.getConfigItemByKey(
		    MdlChoiceConfigItem.KEY_EXTERNAL_TOOL_SERVLET).getConfigValue();

	    // setting the mdlChoice username so the params are set up correctly
	    mdlChoice.setExtUsername(extUserName);
	    HashMap<String, String> params = getRequiredExtServletParams(mdlChoice);
	    params.put(EXT_SERVER_PARAM_EXT_TOOL_CONTENT_ID, extToolContentId);
	    params.put(EXT_SERVER_PARAM_METHOD, EXT_SERVER_METHOD_OUTPUT);
	    params.put(EXT_SERVER_PARAM_OUTPUT_NAME, URLEncoder.encode(outputName, "UTF8"));
	    params.put("optionID", URLEncoder.encode(optionID, "UTF8"));

	    InputStream is = WebUtility.getResponseInputStreamFromExternalServer(outputServletUrl, params);
	    BufferedReader isReader = new BufferedReader(new InputStreamReader(is));
	    boolean ret = Boolean.parseBoolean(isReader.readLine());
	    return ret;
	} catch (Exception e) {
	    logger.debug("Failed getting external output", e);
	    throw new ToolException("Failed getting external output", e);
	}
    }

    /**
     * Get the tool output for the given tool output names.
     * 
     * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.util.List<String>,
     *      java.lang.Long, java.lang.Long)
     */
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, Long toolSessionId, Long learnerId) {
	mdlChoiceOutputFactory = getMdlChoiceOutputFactory();
	MdlChoiceSession session = this.getSessionBySessionId(toolSessionId);
	if (session == null) {
	    return null;
	}
	return mdlChoiceOutputFactory.getToolOutput(names, this, toolSessionId, learnerId, session.getMdlChoice(),
		session.getExtSessionId());
    }

    /**
     * Get the tool output for the given tool output name.
     * 
     * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.lang.String,
     *      java.lang.Long, java.lang.Long)
     */
    public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
	mdlChoiceOutputFactory = getMdlChoiceOutputFactory();
	MdlChoiceSession session = this.getSessionBySessionId(toolSessionId);
	if (session == null) {
	    return null;
	}

	return mdlChoiceOutputFactory.getToolOutput(name, this, toolSessionId, learnerId, session.getMdlChoice(),
		session.getExtSessionId());
    }

    /**
     * Get the definitions for possible output for an activity, based on the
     * toolContentId. These may be definitions that are always available for the
     * tool (e.g. number of marks for Multiple Choice) or a custom definition
     * created for a particular activity such as the answer to the third
     * question contains the word Koala and hence the need for the toolContentId
     * 
     * @return SortedMap of ToolOutputDefinitions with the key being the name of
     *         each definition
     */
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Long toolContentId) throws ToolException {
	mdlChoiceOutputFactory = getMdlChoiceOutputFactory();
	MdlChoice mdchce = getMdlChoiceByContentId(toolContentId);
	if (mdchce == null) {
	    mdchce = getDefaultContent();
	}
	
	List<MdlChoiceOutputDTO> choices = getPossibleChoices(mdchce);
	
	//HashMap<String, Object> map = new HashMap<String, Object>();
	//map.put("toolContent", mdchce);
	//map.put("choices", choices);
	
	return mdlChoiceOutputFactory.getToolOutputDefinitions(choices);
    }

    private List<MdlChoiceOutputDTO> getPossibleChoices(MdlChoice mdchce) {
	try {
	    if (mdchce.getExtToolContentId() == null)
	    {
		return new ArrayList<MdlChoiceOutputDTO>();
	    }
	    
	    String servletUrl = mdlChoiceConfigItemDAO
		    .getConfigItemByKey(MdlChoiceConfigItem.KEY_EXTERNAL_TOOL_SERVLET).getConfigValue();

	    // Get the required params, then call the eternal server
	    HashMap<String, String> params = this.getRequiredExtServletParams(mdchce);
	    params.put(EXT_SERVER_PARAM_METHOD, EXT_SERVER_METHOD_EXPORT_GET_CHOICES);
	    params.put(EXT_SERVER_PARAM_EXT_TOOL_CONTENT_ID, mdchce.getExtToolContentId().toString());

	    // Make the request
	    InputStream is = WebUtility.getResponseInputStreamFromExternalServer(servletUrl, params);
	    BufferedReader isReader = new BufferedReader(new InputStreamReader(is));
	    String str = isReader.readLine();
	    if (str == null) {
		throw new UserInfoFetchException("Fail to clone choice in .LRN:"
			+ " - No data returned from external server");
	    }
	    
	    return getChoicesFromString(str);

	} catch (Exception e) {
	    logger.error("Failed to call external server to copy tool content" + e);
	    throw new ToolException("Failed to call external server to copy tool content" + e);
	}

    }

    private List<MdlChoiceOutputDTO> getChoicesFromString(String string) {
	try {
	    List<MdlChoiceOutputDTO> choices = new ArrayList<MdlChoiceOutputDTO>();
	    
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    DocumentBuilder db = dbf.newDocumentBuilder();
	    Document document = db.parse(new InputSource(new StringReader(string)));
	    NodeList list = document.getElementsByTagName("choice");

	    for (int i = 0; i < list.getLength(); i++) {
		NamedNodeMap markerNode = ((Node) list.item(i)).getAttributes();
		String choice = markerNode.getNamedItem("option").getNodeValue();
		long id = Long.parseLong(markerNode.getNamedItem("optionID").getNodeValue());
		MdlChoiceOutputDTO dto = new MdlChoiceOutputDTO(id, choice);
		choices.add(dto);
	    }
	    
	    return choices;

	} catch (Exception e) {
	    logger.error("Problem parsing choices xml", e);
	    throw new ToolException("Problem parsing choices xml" + e);
	}
    }

    public String hash(ExtServerOrgMap serverMap, String extUsername, String timestamp) {
	String serverId = serverMap.getServerid();
	String serverKey = serverMap.getServerkey();
	String plaintext = timestamp.trim().toLowerCase() + extUsername.trim().toLowerCase()
		+ serverId.trim().toLowerCase() + serverKey.trim().toLowerCase();
	return HashUtil.sha1(plaintext);
    }

    public String leaveToolSession(Long toolSessionId, Long learnerId) throws DataMissingException, ToolException {
	return learnerService.completeToolSession(toolSessionId, learnerId);
    }

    public ToolSessionExportOutputData exportToolSession(Long toolSessionId) throws DataMissingException, ToolException {
	// TODO Auto-generated method stub
	return null;
    }

    public ToolSessionExportOutputData exportToolSession(List toolSessionIds) throws DataMissingException,
	    ToolException {
	// TODO Auto-generated method stub
	return null;
    }

    public void removeToolSession(Long toolSessionId) throws DataMissingException, ToolException {
	mdlChoiceSessionDAO.deleteBySessionID(toolSessionId);
	// TODO check if cascade worked
    }

    /* ************ Methods from ToolContentManager ************************* */

    public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException {

	if (logger.isDebugEnabled()) {
	    logger.debug("entering method copyToolContent:" + " fromContentId=" + fromContentId + " toContentId="
		    + toContentId);
	}

	if (toContentId == null) {
	    String error = "Failed to copy tool content: toContentID is null";
	    throw new ToolException(error);
	}

	MdlChoice fromContent = null;
	if (fromContentId != null) {
	    fromContent = mdlChoiceDAO.getByContentId(fromContentId);
	}
	if (fromContent == null) {
	    // create the fromContent using the default tool content
	    fromContent = getDefaultContent();
	}
	MdlChoice toContent = MdlChoice.newInstance(fromContent, toContentId, mdlChoiceToolContentHandler);

	// calling the external tool to copy it's content.
	/*
	try
	{
		
		toContent.setExtToolContentId(copyExternalToolContent(
				fromContent.getExtToolContentId(),
				fromContent.getExtUsername(),
				fromContent.getExtCourseId(),
				fromContent.getExtCourseUrl()
				));
		
	}
	catch(Exception e)
	{
		throw new ToolException("Failed to call external server to copy tool content" + e);
	}*/

	mdlChoiceDAO.saveOrUpdate(toContent);
    }

    /**
     * Special copyToolContent implemented from ToolAdapterContentManager
     * customCSV is passed here so we have a chance to get the external course,
     * user and course url. This function is only neccessary when we are
     * integrated with an external LMS, otherwise this service should not
     * implement ToolAdapterContentManager, but instead implement the regular
     * ToolContentManager and use the regular copyContent(Long fromContentId,
     * Long toContentId) function
     */
    public void copyToolContent(Long fromContentId, Long toContentId, String customCSV) throws ToolException {

	if (logger.isDebugEnabled()) {
	    logger.debug("entering method copyToolContent:" + " fromContentId=" + fromContentId + " toContentId="
		    + toContentId);
	}

	if (toContentId == null) {
	    String error = "Failed to copy tool content: toContentID is null";
	    throw new ToolException(error);
	}

	HashMap<String, String> mapCSV = decodeCustomCSV(customCSV);
	if (mapCSV == null) {
	    logger.error("Tool adapter tool cusomCSV not in required (user,course,courseURL) form: "
		    + EXPECTED_CSV_FORM);
	    throw new ToolException("mdlChoice tool cusomCSV not in required (user,course,courseURL) form: "
		    + EXPECTED_CSV_FORM);
	}

	MdlChoice fromContent = null;
	if (fromContentId != null) {
	    fromContent = mdlChoiceDAO.getByContentId(fromContentId);
	}

	// Set the default params for the external cloning request
	HashMap<String, String> params = getRequiredExtServletParams(customCSV);

	if ((fromContent == null) || fromContent.getExtToolContentId() == null) {
	    // create the fromContent using the default tool content
	    fromContent = getDefaultContent();
	    try {
		// notify the external server to create the default content
		params.put(EXT_SERVER_PARAM_EXT_TOOL_CONTENT_ID, "0");
		fromContent.setExtToolContentId(copyExternalToolContent(params));
		fromContent.setByCustomCSVHashMap(mapCSV);
	    } catch (Exception e) {
		throw new ToolException("Failed to call external server to create default tool content" + e);
	    }
	}

	// Create a new instance to copy the tool content to
	MdlChoice toContent = MdlChoice.newInstance(fromContent, toContentId, mdlChoiceToolContentHandler);
	toContent.setByCustomCSVHashMap(mapCSV);

	// calling the external tool to copy it's content.
	try {
	    params.put(EXT_SERVER_PARAM_EXT_TOOL_CONTENT_ID, fromContent.getExtToolContentId().toString());
	    toContent.setExtToolContentId(copyExternalToolContent(params));
	} catch (Exception e) {
	    throw new ToolException("Failed to call external server to copy tool content" + e);
	}

	mdlChoiceDAO.saveOrUpdate(toContent);
    }

    public void setAsDefineLater(Long toolContentId, boolean value) throws DataMissingException, ToolException {
	MdlChoice mdlChoice = mdlChoiceDAO.getByContentId(toolContentId);
	if (mdlChoice == null) {
	    throw new ToolException("Could not find tool with toolContentID: " + toolContentId);
	}
	mdlChoice.setDefineLater(value);
	mdlChoiceDAO.saveOrUpdate(mdlChoice);
    }

    public void setAsRunOffline(Long toolContentId, boolean value) throws DataMissingException, ToolException {
	MdlChoice mdlChoice = mdlChoiceDAO.getByContentId(toolContentId);
	if (mdlChoice == null) {
	    throw new ToolException("Could not find tool with toolContentID: " + toolContentId);
	}
	mdlChoice.setRunOffline(value);
	mdlChoiceDAO.saveOrUpdate(mdlChoice);
    }

    public void removeToolContent(Long toolContentId, boolean removeSessionData) throws SessionDataExistsException,
	    ToolException {
	// TODO Auto-generated method stub
    }

    /**
     * Export the XML fragment for the tool's content, along with any files
     * needed for the content.
     * 
     * @throws DataMissingException
     *                 if no tool content matches the toolSessionId
     * @throws ToolException
     *                 if any other error occurs
     */

    public void exportToolContent(Long toolContentId, String rootPath) throws DataMissingException, ToolException {

	MdlChoice mdlChoice = mdlChoiceDAO.getByContentId(toolContentId);
	if (mdlChoice == null) {
	    mdlChoice = getDefaultContent();
	}
	if (mdlChoice == null)
	    throw new DataMissingException("Unable to find default content for the mdlChoice tool");

	// If no external content was found, export empty mdlChoice
	// Otherwise, call the external servlet to get the export file
	if (mdlChoice.getExtToolContentId() == null) {
	    mdlChoice.setExtToolContentId(null);
	    mdlChoice.setToolContentHandler(null);
	    mdlChoice.setMdlChoiceSessions(null);

	    try {
		exportContentService.exportToolContent(toolContentId, mdlChoice, mdlChoiceToolContentHandler, rootPath);
	    } catch (ExportToolContentException e) {
		throw new ToolException(e);
	    }
	} else {

	    URLConnection conn = null;
	    try {
		// Create the directory to store the export file
		String toolPath = FileUtil.getFullPath(rootPath, toolContentId.toString());
		FileUtil.createDirectory(toolPath);

		String exportServletUrl = mdlChoiceConfigItemDAO.getConfigItemByKey(
			MdlChoiceConfigItem.KEY_EXTERNAL_TOOL_SERVLET).getConfigValue();

		// setting these to arbitrary values since they are only used to construct the hash

		mdlChoice.setExtCourseId("extCourse");
		mdlChoice.setExtSection("0");
		mdlChoice.setExtUsername("authUser");
		HashMap<String, String> params = this.getRequiredExtServletParams(mdlChoice);
		params.put(EXT_SERVER_PARAM_METHOD, EXT_SERVER_METHOD_EXPORT);
		params.put(EXT_SERVER_PARAM_EXT_TOOL_CONTENT_ID, mdlChoice.getExtToolContentId().toString());

		// Get the reponse stream from the external server (hopefully containing the export file
		InputStream in = WebUtility.getResponseInputStreamFromExternalServer(exportServletUrl, params);

		// Get the output stream to write the file for extport
		OutputStream out = new BufferedOutputStream(new FileOutputStream(toolPath + "/ext_tool.txt"));

		byte[] buffer = new byte[1024];
		int numRead;
		long numWritten = 0;
		while ((numRead = in.read(buffer)) != -1) {
		    out.write(buffer, 0, numRead);
		    numWritten += numRead;
		}
		logger.debug("Path to mdlChoice export content: " + toolPath + "/ext_tool.txt");

		out.flush();
		out.close();
		in.close();
	    } catch (Exception e) {
		logger.error("Problem exporting data from external .LRN servlet", e);
	    }
	}
    }

    /**
     * Default importToolContent, not implemented because this is a tooladapter
     * tool
     */
    public void importToolContent(Long toolContentId, Integer newUserUid, String toolContentPath, String fromVersion,
	    String toVersion) throws ToolException {

    }

    public ExtServerOrgMap getExtServerOrgMap() {
	if (integrationService == null)
	    integrationService = getIntegrationService();
	IToolVO tool = toolService.getToolBySignature(MdlChoiceConstants.TOOL_SIGNATURE);
	return integrationService.getExtServerOrgMap(tool.getExtLmsId());
    }

    /**
     * Import the XML fragment for the tool's content, along with any files
     * needed for the content.
     * 
     * @throws ToolException
     *                 if any other error occurs
     */
    public void importToolContent(Long toolContentId, Integer newUserUid, String toolContentPath, String fromVersion,
	    String toVersion, String customCSV) throws ToolException {

	File extExportFile = new File(toolContentPath + "/ext_tool.txt");

	// if specially exported tooladapter file is found, send it to the external server
	// otherwise, simply import an empty mdlChoice
	if (extExportFile.exists()) {

	    try {

		String importServletUrl = mdlChoiceConfigItemDAO.getConfigItemByKey(
			MdlChoiceConfigItem.KEY_EXTERNAL_TOOL_SERVLET).getConfigValue();

		if (customCSV == null) {
		    logger.error("Could not retrieve customCSV required for importing tool adapter tool. CustomCSV: "
			    + customCSV);
		    throw new ToolException(
			    "Could not retrieve customCSV required for importing tool adapter tool. CustomCSV: "
				    + customCSV);
		}

		HashMap<String, String> params = getRequiredExtServletParams(customCSV);
		params.put(EXT_SERVER_PARAM_METHOD, EXT_SERVER_METHOD_IMPORT);

		// Do the external multipart post to upload the file to external LMS (returns an extToolContentId)
		InputStream is = WebUtility.performMultipartPost(extExportFile, EXT_SERVER_PARAM_UPLOAD_FILE,
			importServletUrl, params);
		DataInputStream inStream = new DataInputStream(is);
		String str = inStream.readLine();
		Long extContentId = Long.parseLong(str);
		inStream.close();

		// Save the resulting mdl choice 
		MdlChoice mdlChoice = new MdlChoice();
		mdlChoice.setToolContentId(toolContentId);
		mdlChoice.setCreateDate(new Date());
		mdlChoice.setExtToolContentId(extContentId);
		saveOrUpdateMdlChoice(mdlChoice);

	    } catch (Exception e) {
		logger.error("Problem passing mdlChoice export file to external server", e);
		throw new ToolException(e);
	    }
	} else // use the normal LAMS method of importing activities from an xml file
	{

	    try {
		Object toolPOJO = exportContentService.importToolContent(toolContentPath, mdlChoiceToolContentHandler,
			fromVersion, toVersion);
		if (!(toolPOJO instanceof MdlChoice))
		    throw new ImportToolContentException(
			    "Import MdlChoice tool content failed. Deserialized object is " + toolPOJO);
		MdlChoice mdlChoice = (MdlChoice) toolPOJO;
		// reset it to new toolContentId
		mdlChoice.setToolContentId(toolContentId);
		mdlChoiceDAO.saveOrUpdate(mdlChoice);
	    } catch (Exception e) {
		throw new ToolException(e);
	    }
	}
    }

    /* ********** IMdlChoiceService Methods ********************************* */

    public Long getDefaultContentIdBySignature(String toolSignature) {
	Long toolContentId = null;
	toolContentId = new Long(toolService.getToolDefaultContentIdBySignature(toolSignature));
	if (toolContentId == null) {
	    String error = "Could not retrieve default content id for this tool";
	    logger.error(error);
	    throw new MdlChoiceException(error);
	}
	return toolContentId;
    }

    public MdlChoice getDefaultContent() {
	/*
	Long defaultContentID = getDefaultContentIdBySignature(MdlChoiceConstants.TOOL_SIGNATURE);
	MdlChoice defaultContent = getMdlChoiceByContentId(defaultContentID);
	if (defaultContent == null) {
		String error = "Could not retrieve default content record for this tool";
		logger.error(error);
		throw new MdlChoiceException(error);
	}
	return defaultContent;
	*/
	MdlChoice defaultContent = new MdlChoice();
	return defaultContent;
    }

    public MdlChoice copyDefaultContent(Long newContentID) {

	if (newContentID == null) {
	    String error = "Cannot copy the MdlChoice tools default content: + " + "newContentID is null";
	    logger.error(error);
	    throw new MdlChoiceException(error);
	}

	MdlChoice defaultContent = getDefaultContent();
	// create new mdlChoice using the newContentID
	MdlChoice newContent = new MdlChoice();
	newContent = MdlChoice.newInstance(defaultContent, newContentID, mdlChoiceToolContentHandler);
	mdlChoiceDAO.saveOrUpdate(newContent);
	return newContent;
    }

    public MdlChoice getMdlChoiceByContentId(Long toolContentID) {
	MdlChoice mdlChoice = (MdlChoice) mdlChoiceDAO.getByContentId(toolContentID);
	if (mdlChoice == null) {
	    logger.debug("Could not find the content with toolContentID:" + toolContentID);
	}
	return mdlChoice;
    }

    public MdlChoiceConfigItem getConfigItem(String key) {
	return mdlChoiceConfigItemDAO.getConfigItemByKey(key);
    }

    public void saveOrUpdateMdlChoiceConfigItem(MdlChoiceConfigItem item) {
	mdlChoiceConfigItemDAO.saveOrUpdate(item);
    }

    public MdlChoiceSession getSessionBySessionId(Long toolSessionId) {
	MdlChoiceSession mdlChoiceSession = mdlChoiceSessionDAO.getBySessionId(toolSessionId);
	if (mdlChoiceSession == null) {
	    logger.debug("Could not find the mdlChoice session with toolSessionID:" + toolSessionId);
	}
	return mdlChoiceSession;
    }

    public MdlChoiceUser getUserByUserIdAndSessionId(Long userId, Long toolSessionId) {
	return mdlChoiceUserDAO.getByUserIdAndSessionId(userId, toolSessionId);
    }

    public MdlChoiceUser getUserByLoginNameAndSessionId(String loginName, Long toolSessionId) {
	return mdlChoiceUserDAO.getByLoginNameAndSessionId(loginName, toolSessionId);
    }

    public MdlChoiceUser getUserByUID(Long uid) {
	return mdlChoiceUserDAO.getByUID(uid);
    }

    public void deleteFromRepository(Long uuid, Long versionID) throws MdlChoiceException {
	ITicket ticket = getRepositoryLoginTicket();
	try {
	    repositoryService.deleteVersion(ticket, uuid, versionID);
	} catch (Exception e) {
	    throw new MdlChoiceException("Exception occured while deleting files from" + " the repository "
		    + e.getMessage());
	}
    }

    public void saveOrUpdateMdlChoice(MdlChoice mdlChoice) {
	mdlChoiceDAO.saveOrUpdate(mdlChoice);
    }

    public void saveOrUpdateMdlChoiceSession(MdlChoiceSession mdlChoiceSession) {
	mdlChoiceSessionDAO.saveOrUpdate(mdlChoiceSession);
    }

    public void saveOrUpdateMdlChoiceUser(MdlChoiceUser mdlChoiceUser) {
	mdlChoiceUserDAO.saveOrUpdate(mdlChoiceUser);
    }

    public MdlChoiceUser createMdlChoiceUser(UserDTO user, MdlChoiceSession mdlChoiceSession) {
	MdlChoiceUser mdlChoiceUser = new MdlChoiceUser(user, mdlChoiceSession);
	saveOrUpdateMdlChoiceUser(mdlChoiceUser);
	return mdlChoiceUser;
    }

    public IAuditService getAuditService() {
	return auditService;
    }

    public void setAuditService(IAuditService auditService) {
	this.auditService = auditService;
    }

    /**
     * This method verifies the credentials of the SubmitFiles Tool and gives it
     * the <code>Ticket</code> to login and access the Content Repository.
     * 
     * A valid ticket is needed in order to access the content from the
     * repository. This method would be called evertime the tool needs to
     * upload/download files from the content repository.
     * 
     * @return ITicket The ticket for repostory access
     * @throws SubmitFilesException
     */
    private ITicket getRepositoryLoginTicket() throws MdlChoiceException {
	repositoryService = RepositoryProxy.getRepositoryService();
	ICredentials credentials = new SimpleCredentials(MdlChoiceToolContentHandler.repositoryUser,
		MdlChoiceToolContentHandler.repositoryId);
	try {
	    ITicket ticket = repositoryService.login(credentials, MdlChoiceToolContentHandler.repositoryWorkspaceName);
	    return ticket;
	} catch (AccessDeniedException ae) {
	    throw new MdlChoiceException("Access Denied to repository." + ae.getMessage());
	} catch (WorkspaceNotFoundException we) {
	    throw new MdlChoiceException("Workspace not found." + we.getMessage());
	} catch (LoginException e) {
	    throw new MdlChoiceException("Login failed." + e.getMessage());
	}
    }

    /* ===============Methods implemented from ToolContentImport102Manager =============== */

    /**
     * Import the data for a 1.0.2 MdlChoice
     */
    public void import102ToolContent(Long toolContentId, UserDTO user, Hashtable importValues) {
	Date now = new Date();
	MdlChoice mdlChoice = new MdlChoice();
	mdlChoice.setCreateDate(now);
	mdlChoice.setDefineLater(Boolean.FALSE);
	mdlChoice.setRunOffline(Boolean.FALSE);
	mdlChoice.setToolContentId(toolContentId);
	mdlChoice.setUpdateDate(now);

	mdlChoiceDAO.saveOrUpdate(mdlChoice);
    }

    /**
     * Set the description, throws away the title value as this is not supported
     * in 2.0
     */
    public void setReflectiveData(Long toolContentId, String title, String description) throws ToolException,
	    DataMissingException {

	logger
		.warn("Setting the reflective field on a mdlChoice. This doesn't make sense as the mdlChoice is for reflection and we don't reflect on reflection!");
	MdlChoice mdlChoice = getMdlChoiceByContentId(toolContentId);
	if (mdlChoice == null) {
	    throw new DataMissingException("Unable to set reflective data titled " + title
		    + " on activity toolContentId " + toolContentId + " as the tool content does not exist.");
	}

	//mdlChoice.setInstructions(description);
    }

    //=========================================================================================
    /* ********** Used by Spring to "inject" the linked objects ************* */

    public IMdlChoiceDAO getMdlChoiceDAO() {
	return mdlChoiceDAO;
    }

    public void setMdlChoiceDAO(IMdlChoiceDAO mdlChoiceDAO) {
	this.mdlChoiceDAO = mdlChoiceDAO;
    }

    public IMdlChoiceConfigItemDAO getMdlChoiceConfigItemDAO() {
	return mdlChoiceConfigItemDAO;
    }

    public void setMdlChoiceConfigItemDAO(IMdlChoiceConfigItemDAO mdlChoiceConfigItemDAO) {
	this.mdlChoiceConfigItemDAO = mdlChoiceConfigItemDAO;
    }

    public IToolContentHandler getMdlChoiceToolContentHandler() {
	return mdlChoiceToolContentHandler;
    }

    public void setMdlChoiceToolContentHandler(IToolContentHandler mdlChoiceToolContentHandler) {
	this.mdlChoiceToolContentHandler = mdlChoiceToolContentHandler;
    }

    public IMdlChoiceSessionDAO getMdlChoiceSessionDAO() {
	return mdlChoiceSessionDAO;
    }

    public void setMdlChoiceSessionDAO(IMdlChoiceSessionDAO sessionDAO) {
	this.mdlChoiceSessionDAO = sessionDAO;
    }

    public ILamsToolService getToolService() {
	return toolService;
    }

    public void setToolService(ILamsToolService toolService) {
	this.toolService = toolService;
    }

    public IMdlChoiceUserDAO getMdlChoiceUserDAO() {
	return mdlChoiceUserDAO;
    }

    public void setMdlChoiceUserDAO(IMdlChoiceUserDAO userDAO) {
	this.mdlChoiceUserDAO = userDAO;
    }

    public ILearnerService getLearnerService() {
	return learnerService;
    }

    public void setLearnerService(ILearnerService learnerService) {
	this.learnerService = learnerService;
    }

    public IExportToolContentService getExportContentService() {
	return exportContentService;
    }

    public void setExportContentService(IExportToolContentService exportContentService) {
	this.exportContentService = exportContentService;
    }

    public ICoreNotebookService getCoreNotebookService() {
	return coreNotebookService;
    }

    public void setCoreNotebookService(ICoreNotebookService coreNotebookService) {
	this.coreNotebookService = coreNotebookService;
    }

    public MdlChoiceOutputFactory getMdlChoiceOutputFactory() {
	if (mdlChoiceOutputFactory == null) {
	    mdlChoiceOutputFactory = new MdlChoiceOutputFactory();
	}
	return mdlChoiceOutputFactory;
    }

    public void setMdlChoiceOutputFactory(MdlChoiceOutputFactory mdlChoiceOutputFactory) {
	this.mdlChoiceOutputFactory = mdlChoiceOutputFactory;
    }

    /**
     * TODO: Use spring injection instead of hacking a context
     * 
     * @return
     */
    public IIntegrationService getIntegrationService() {

	if (integrationService == null) {
	    String contexts[] = { "/org/lamsfoundation/lams/applicationContext.xml",
		    "/org/lamsfoundation/lams/lesson/lessonApplicationContext.xml",
		    "/org/lamsfoundation/lams/toolApplicationContext.xml",
		    "/org/lamsfoundation/lams/integrationContext.xml",
		    "/org/lamsfoundation/lams/learning/learningApplicationContext.xml",
		    "/org/lamsfoundation/lams/contentrepository/applicationContext.xml",
		    "/org/lamsfoundation/lams/tool/mdchce/mdlChoiceApplicationContext.xml",
		    "/org/lamsfoundation/lams/commonContext.xml" };

	    ApplicationContext context = new ClassPathXmlApplicationContext(contexts);

	    if (context == null)
		throw new MdlChoiceException(
			"Unable to access application context. Cannot create integration service object.");

	    IIntegrationService service = (IIntegrationService) context.getBean("integrationService");
	    return service;
	} else {
	    return integrationService;
	}
    }

    public void setIntegrationService(IIntegrationService integrationService) {
	this.integrationService = integrationService;
    }
}
