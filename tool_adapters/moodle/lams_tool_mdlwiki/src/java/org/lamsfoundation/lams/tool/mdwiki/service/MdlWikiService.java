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

package org.lamsfoundation.lams.tool.mdwiki.service;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.SortedMap;

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
import org.lamsfoundation.lams.tool.mdwiki.dao.IMdlWikiConfigItemDAO;
import org.lamsfoundation.lams.tool.mdwiki.dao.IMdlWikiDAO;
import org.lamsfoundation.lams.tool.mdwiki.dao.IMdlWikiSessionDAO;
import org.lamsfoundation.lams.tool.mdwiki.dao.IMdlWikiUserDAO;
import org.lamsfoundation.lams.tool.mdwiki.model.MdlWiki;
import org.lamsfoundation.lams.tool.mdwiki.model.MdlWikiConfigItem;
import org.lamsfoundation.lams.tool.mdwiki.model.MdlWikiSession;
import org.lamsfoundation.lams.tool.mdwiki.model.MdlWikiUser;
import org.lamsfoundation.lams.tool.mdwiki.util.MdlWikiConstants;
import org.lamsfoundation.lams.tool.mdwiki.util.MdlWikiException;
import org.lamsfoundation.lams.tool.mdwiki.util.MdlWikiToolContentHandler;
import org.lamsfoundation.lams.tool.mdwiki.util.WebUtility;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.HashUtil;
import org.lamsfoundation.lams.util.audit.IAuditService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * An implementation of the IMdlWikiService interface.
 * 
 * As a requirement, all LAMS tool's service bean must implement
 * ToolContentManager and ToolSessionManager.
 */

public class MdlWikiService implements ToolSessionManager, ToolAdapterContentManager, IMdlWikiService,
	ToolContentImport102Manager {

    static Logger logger = Logger.getLogger(MdlWikiService.class.getName());

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

    private IMdlWikiDAO mdlWikiDAO = null;

    private IMdlWikiSessionDAO mdlWikiSessionDAO = null;

    private IMdlWikiUserDAO mdlWikiUserDAO = null;

    private IMdlWikiConfigItemDAO mdlWikiConfigItemDAO = null;

    private ILearnerService learnerService;

    private ILamsToolService toolService;

    private IToolContentHandler mdlWikiToolContentHandler = null;

    private IRepositoryService repositoryService = null;

    private IAuditService auditService = null;

    private IExportToolContentService exportContentService;

    private ICoreNotebookService coreNotebookService;

    private IIntegrationService integrationService;

    private MdlWikiOutputFactory mdlWikiOutputFactory;

    public MdlWikiService() {
	super();
	// TODO Auto-generated constructor stub
    }

    /* ************ Methods from ToolSessionManager ************* */
    public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException {
	if (logger.isDebugEnabled()) {
	    logger.debug("entering method createToolSession:" + " toolSessionId = " + toolSessionId
		    + " toolSessionName = " + toolSessionName + " toolContentId = " + toolContentId);
	}

	MdlWikiSession session = new MdlWikiSession();
	session.setSessionId(toolSessionId);
	session.setSessionName(toolSessionName);

	// learner starts
	MdlWiki mdlWiki = mdlWikiDAO.getByContentId(toolContentId);
	session.setMdlWiki(mdlWiki);

	try {
	    // Get the required params, then call the eternal server
	    HashMap<String, String> params = getRequiredExtServletParams(mdlWiki);
	    params.put(EXT_SERVER_PARAM_EXT_TOOL_CONTENT_ID, mdlWiki.getExtToolContentId().toString());
	    session.setExtSessionId(copyExternalToolContent(params));
	} catch (Exception e) {
	    logger.error("Failed to call external server to copy tool content" + e);
	    throw new ToolException("Failed to call external server to copy tool content" + e);
	}

	mdlWikiSessionDAO.saveOrUpdate(session);
    }

    /**
     * Calls the external server to copy the content and return a new id
     * 
     * @param extToolContentId
     * @return
     */
    public Long copyExternalToolContent(HashMap<String, String> params) throws ToolException, Exception {

	String cloneServletUrl = mdlWikiConfigItemDAO.getConfigItemByKey(MdlWikiConfigItem.KEY_EXTERNAL_TOOL_SERVLET)
		.getConfigValue();

	// add the method to the params
	params.put(EXT_SERVER_PARAM_METHOD, EXT_SERVER_METHOD_CLONE);

	// Make the request
	InputStream is = WebUtility.getResponseInputStreamFromExternalServer(cloneServletUrl, params);
	BufferedReader isReader = new BufferedReader(new InputStreamReader(is));
	String str = isReader.readLine();
	if (str == null) {
	    throw new UserInfoFetchException("Fail to clone wiki in .LRN:" + " - No data returned from external server");
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

    public HashMap<String, String> getRequiredExtServletParams(MdlWiki mdlwiki) {
	HashMap<String, String> params = new HashMap<String, String>();
	params.put(EXT_SERVER_PARAM_USER, mdlwiki.getExtUsername());
	params.put(EXT_SERVER_PARAM_COURSE, mdlwiki.getExtCourseId());
	params.put(EXT_SERVER_PARAM_SECTION, mdlwiki.getExtSection());

	String timestamp = Long.toString(new Date().getTime());
	params.put(EXT_SERVER_PARAM_TIMESTAMP, timestamp);

	ExtServerOrgMap serverMap = this.getExtServerOrgMap();
	String hash = hash(serverMap, mdlwiki.getExtUsername(), timestamp);
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

    public int getExternalToolOutputInt(String outputName, MdlWiki mdlWiki, Long userId, String extToolContentId,
	    Long toolSessionId) {
	MdlWikiUser user = this.getUserByUserIdAndSessionId(userId, toolSessionId);
	ExtServerOrgMap extServerMap = getExtServerOrgMap();

	String extUserName = user.getLoginName().substring(extServerMap.getPrefix().length() + 1);

	try {
	    String outputServletUrl = mdlWikiConfigItemDAO.getConfigItemByKey(
		    MdlWikiConfigItem.KEY_EXTERNAL_TOOL_SERVLET).getConfigValue();

	    // setting the mdlWiki username so the params are set up correctly
	    mdlWiki.setExtUsername(extUserName);
	    HashMap<String, String> params = getRequiredExtServletParams(mdlWiki);
	    params.put(EXT_SERVER_PARAM_EXT_TOOL_CONTENT_ID, extToolContentId);
	    params.put(EXT_SERVER_PARAM_METHOD, EXT_SERVER_METHOD_OUTPUT);
	    params.put(EXT_SERVER_PARAM_OUTPUT_NAME, URLEncoder.encode(outputName, "UTF8"));

	    InputStream is = WebUtility.getResponseInputStreamFromExternalServer(outputServletUrl, params);
	    BufferedReader isReader = new BufferedReader(new InputStreamReader(is));
	    int ret = Integer.parseInt(isReader.readLine());
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
	mdlWikiOutputFactory = getMdlWikiOutputFactory();
	MdlWikiSession session = this.getSessionBySessionId(toolSessionId);
	if (session == null) {
	    return null;
	}
	return mdlWikiOutputFactory.getToolOutput(names, this, toolSessionId, learnerId, session.getMdlWiki(), session
		.getExtSessionId());
    }

    /**
     * Get the tool output for the given tool output name.
     * 
     * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.lang.String,
     *      java.lang.Long, java.lang.Long)
     */
    public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
	mdlWikiOutputFactory = getMdlWikiOutputFactory();
	MdlWikiSession session = this.getSessionBySessionId(toolSessionId);
	if (session == null) {
	    return null;
	}

	return mdlWikiOutputFactory.getToolOutput(name, this, toolSessionId, learnerId, session.getMdlWiki(), session
		.getExtSessionId());
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
	mdlWikiOutputFactory = getMdlWikiOutputFactory();
	MdlWiki mdwiki = getMdlWikiByContentId(toolContentId);
	if (mdwiki == null) {
	    mdwiki = getDefaultContent();
	}
	return mdlWikiOutputFactory.getToolOutputDefinitions(mdwiki);
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
	mdlWikiSessionDAO.deleteBySessionID(toolSessionId);
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

	MdlWiki fromContent = null;
	if (fromContentId != null) {
	    fromContent = mdlWikiDAO.getByContentId(fromContentId);
	}
	if (fromContent == null) {
	    // create the fromContent using the default tool content
	    fromContent = getDefaultContent();
	}
	MdlWiki toContent = MdlWiki.newInstance(fromContent, toContentId, mdlWikiToolContentHandler);

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

	mdlWikiDAO.saveOrUpdate(toContent);
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
	    throw new ToolException("mdlWiki tool cusomCSV not in required (user,course,courseURL) form: "
		    + EXPECTED_CSV_FORM);
	}

	MdlWiki fromContent = null;
	if (fromContentId != null) {
	    fromContent = mdlWikiDAO.getByContentId(fromContentId);
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
	MdlWiki toContent = MdlWiki.newInstance(fromContent, toContentId, mdlWikiToolContentHandler);
	toContent.setByCustomCSVHashMap(mapCSV);

	// calling the external tool to copy it's content.
	try {
	    params.put(EXT_SERVER_PARAM_EXT_TOOL_CONTENT_ID, fromContent.getExtToolContentId().toString());
	    toContent.setExtToolContentId(copyExternalToolContent(params));
	} catch (Exception e) {
	    throw new ToolException("Failed to call external server to copy tool content" + e);
	}

	mdlWikiDAO.saveOrUpdate(toContent);
    }

    public void setAsDefineLater(Long toolContentId, boolean value) throws DataMissingException, ToolException {
	MdlWiki mdlWiki = mdlWikiDAO.getByContentId(toolContentId);
	if (mdlWiki == null) {
	    throw new ToolException("Could not find tool with toolContentID: " + toolContentId);
	}
	mdlWiki.setDefineLater(value);
	mdlWikiDAO.saveOrUpdate(mdlWiki);
    }

    public void setAsRunOffline(Long toolContentId, boolean value) throws DataMissingException, ToolException {
	MdlWiki mdlWiki = mdlWikiDAO.getByContentId(toolContentId);
	if (mdlWiki == null) {
	    throw new ToolException("Could not find tool with toolContentID: " + toolContentId);
	}
	mdlWiki.setRunOffline(value);
	mdlWikiDAO.saveOrUpdate(mdlWiki);
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

	MdlWiki mdlWiki = mdlWikiDAO.getByContentId(toolContentId);
	if (mdlWiki == null) {
	    mdlWiki = getDefaultContent();
	}
	if (mdlWiki == null)
	    throw new DataMissingException("Unable to find default content for the mdlWiki tool");

	// If no external content was found, export empty mdlWiki
	// Otherwise, call the external servlet to get the export file
	if (mdlWiki.getExtToolContentId() == null) {
	    mdlWiki.setExtToolContentId(null);
	    mdlWiki.setToolContentHandler(null);
	    mdlWiki.setMdlWikiSessions(null);

	    try {
		exportContentService.exportToolContent(toolContentId, mdlWiki, mdlWikiToolContentHandler, rootPath);
	    } catch (ExportToolContentException e) {
		throw new ToolException(e);
	    }
	} else {

	    URLConnection conn = null;
	    try {
		// Create the directory to store the export file
		String toolPath = FileUtil.getFullPath(rootPath, toolContentId.toString());
		FileUtil.createDirectory(toolPath);

		String exportServletUrl = mdlWikiConfigItemDAO.getConfigItemByKey(
			MdlWikiConfigItem.KEY_EXTERNAL_TOOL_SERVLET).getConfigValue();

		// setting these to arbitrary values since they are only used to construct the hash

		mdlWiki.setExtCourseId("extCourse");
		mdlWiki.setExtSection("0");
		mdlWiki.setExtUsername("authUser");
		HashMap<String, String> params = this.getRequiredExtServletParams(mdlWiki);
		params.put(EXT_SERVER_PARAM_METHOD, EXT_SERVER_METHOD_EXPORT);
		params.put(EXT_SERVER_PARAM_EXT_TOOL_CONTENT_ID, mdlWiki.getExtToolContentId().toString());

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
		logger.debug("Path to mdlWiki export content: " + toolPath + "/ext_tool.txt");

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
	IToolVO tool = toolService.getToolBySignature(MdlWikiConstants.TOOL_SIGNATURE);
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
	// otherwise, simply import an empty mdlWiki
	if (extExportFile.exists()) {

	    try {

		String importServletUrl = mdlWikiConfigItemDAO.getConfigItemByKey(
			MdlWikiConfigItem.KEY_EXTERNAL_TOOL_SERVLET).getConfigValue();

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

		// Save the resulting mdl wiki 
		MdlWiki mdlWiki = new MdlWiki();
		mdlWiki.setToolContentId(toolContentId);
		mdlWiki.setCreateDate(new Date());
		mdlWiki.setExtToolContentId(extContentId);
		saveOrUpdateMdlWiki(mdlWiki);

	    } catch (Exception e) {
		logger.error("Problem passing mdlWiki export file to external server", e);
		throw new ToolException(e);
	    }
	} else // use the normal LAMS method of importing activities from an xml file
	{

	    try {
		Object toolPOJO = exportContentService.importToolContent(toolContentPath, mdlWikiToolContentHandler,
			fromVersion, toVersion);
		if (!(toolPOJO instanceof MdlWiki))
		    throw new ImportToolContentException("Import MdlWiki tool content failed. Deserialized object is "
			    + toolPOJO);
		MdlWiki mdlWiki = (MdlWiki) toolPOJO;
		// reset it to new toolContentId
		mdlWiki.setToolContentId(toolContentId);
		mdlWikiDAO.saveOrUpdate(mdlWiki);
	    } catch (Exception e) {
		throw new ToolException(e);
	    }
	}
    }

    /* ********** IMdlWikiService Methods ********************************* */

    public Long getDefaultContentIdBySignature(String toolSignature) {
	Long toolContentId = null;
	toolContentId = new Long(toolService.getToolDefaultContentIdBySignature(toolSignature));
	if (toolContentId == null) {
	    String error = "Could not retrieve default content id for this tool";
	    logger.error(error);
	    throw new MdlWikiException(error);
	}
	return toolContentId;
    }

    public MdlWiki getDefaultContent() {
	/*
	Long defaultContentID = getDefaultContentIdBySignature(MdlWikiConstants.TOOL_SIGNATURE);
	MdlWiki defaultContent = getMdlWikiByContentId(defaultContentID);
	if (defaultContent == null) {
		String error = "Could not retrieve default content record for this tool";
		logger.error(error);
		throw new MdlWikiException(error);
	}
	return defaultContent;
	*/
	MdlWiki defaultContent = new MdlWiki();
	return defaultContent;
    }

    public MdlWiki copyDefaultContent(Long newContentID) {

	if (newContentID == null) {
	    String error = "Cannot copy the MdlWiki tools default content: + " + "newContentID is null";
	    logger.error(error);
	    throw new MdlWikiException(error);
	}

	MdlWiki defaultContent = getDefaultContent();
	// create new mdlWiki using the newContentID
	MdlWiki newContent = new MdlWiki();
	newContent = MdlWiki.newInstance(defaultContent, newContentID, mdlWikiToolContentHandler);
	mdlWikiDAO.saveOrUpdate(newContent);
	return newContent;
    }

    public MdlWiki getMdlWikiByContentId(Long toolContentID) {
	MdlWiki mdlWiki = (MdlWiki) mdlWikiDAO.getByContentId(toolContentID);
	if (mdlWiki == null) {
	    logger.debug("Could not find the content with toolContentID:" + toolContentID);
	}
	return mdlWiki;
    }

    public MdlWikiConfigItem getConfigItem(String key) {
	return mdlWikiConfigItemDAO.getConfigItemByKey(key);
    }

    public void saveOrUpdateMdlWikiConfigItem(MdlWikiConfigItem item) {
	mdlWikiConfigItemDAO.saveOrUpdate(item);
    }

    public MdlWikiSession getSessionBySessionId(Long toolSessionId) {
	MdlWikiSession mdlWikiSession = mdlWikiSessionDAO.getBySessionId(toolSessionId);
	if (mdlWikiSession == null) {
	    logger.debug("Could not find the mdlWiki session with toolSessionID:" + toolSessionId);
	}
	return mdlWikiSession;
    }

    public MdlWikiUser getUserByUserIdAndSessionId(Long userId, Long toolSessionId) {
	return mdlWikiUserDAO.getByUserIdAndSessionId(userId, toolSessionId);
    }

    public MdlWikiUser getUserByLoginNameAndSessionId(String loginName, Long toolSessionId) {
	return mdlWikiUserDAO.getByLoginNameAndSessionId(loginName, toolSessionId);
    }

    public MdlWikiUser getUserByUID(Long uid) {
	return mdlWikiUserDAO.getByUID(uid);
    }

    public void deleteFromRepository(Long uuid, Long versionID) throws MdlWikiException {
	ITicket ticket = getRepositoryLoginTicket();
	try {
	    repositoryService.deleteVersion(ticket, uuid, versionID);
	} catch (Exception e) {
	    throw new MdlWikiException("Exception occured while deleting files from" + " the repository "
		    + e.getMessage());
	}
    }

    public void saveOrUpdateMdlWiki(MdlWiki mdlWiki) {
	mdlWikiDAO.saveOrUpdate(mdlWiki);
    }

    public void saveOrUpdateMdlWikiSession(MdlWikiSession mdlWikiSession) {
	mdlWikiSessionDAO.saveOrUpdate(mdlWikiSession);
    }

    public void saveOrUpdateMdlWikiUser(MdlWikiUser mdlWikiUser) {
	mdlWikiUserDAO.saveOrUpdate(mdlWikiUser);
    }

    public MdlWikiUser createMdlWikiUser(UserDTO user, MdlWikiSession mdlWikiSession) {
	MdlWikiUser mdlWikiUser = new MdlWikiUser(user, mdlWikiSession);
	saveOrUpdateMdlWikiUser(mdlWikiUser);
	return mdlWikiUser;
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
    private ITicket getRepositoryLoginTicket() throws MdlWikiException {
	repositoryService = RepositoryProxy.getRepositoryService();
	ICredentials credentials = new SimpleCredentials(MdlWikiToolContentHandler.repositoryUser,
		MdlWikiToolContentHandler.repositoryId);
	try {
	    ITicket ticket = repositoryService.login(credentials, MdlWikiToolContentHandler.repositoryWorkspaceName);
	    return ticket;
	} catch (AccessDeniedException ae) {
	    throw new MdlWikiException("Access Denied to repository." + ae.getMessage());
	} catch (WorkspaceNotFoundException we) {
	    throw new MdlWikiException("Workspace not found." + we.getMessage());
	} catch (LoginException e) {
	    throw new MdlWikiException("Login failed." + e.getMessage());
	}
    }

    /* ===============Methods implemented from ToolContentImport102Manager =============== */

    /**
     * Import the data for a 1.0.2 MdlWiki
     */
    public void import102ToolContent(Long toolContentId, UserDTO user, Hashtable importValues) {
	Date now = new Date();
	MdlWiki mdlWiki = new MdlWiki();
	mdlWiki.setCreateDate(now);
	mdlWiki.setDefineLater(Boolean.FALSE);
	mdlWiki.setRunOffline(Boolean.FALSE);
	mdlWiki.setToolContentId(toolContentId);
	mdlWiki.setUpdateDate(now);

	mdlWikiDAO.saveOrUpdate(mdlWiki);
    }

    /**
     * Set the description, throws away the title value as this is not supported
     * in 2.0
     */
    public void setReflectiveData(Long toolContentId, String title, String description) throws ToolException,
	    DataMissingException {

	logger
		.warn("Setting the reflective field on a mdlWiki. This doesn't make sense as the mdlWiki is for reflection and we don't reflect on reflection!");
	MdlWiki mdlWiki = getMdlWikiByContentId(toolContentId);
	if (mdlWiki == null) {
	    throw new DataMissingException("Unable to set reflective data titled " + title
		    + " on activity toolContentId " + toolContentId + " as the tool content does not exist.");
	}

	//mdlWiki.setInstructions(description);
    }

    //=========================================================================================
    /* ********** Used by Spring to "inject" the linked objects ************* */

    public IMdlWikiDAO getMdlWikiDAO() {
	return mdlWikiDAO;
    }

    public void setMdlWikiDAO(IMdlWikiDAO mdlWikiDAO) {
	this.mdlWikiDAO = mdlWikiDAO;
    }

    public IMdlWikiConfigItemDAO getMdlWikiConfigItemDAO() {
	return mdlWikiConfigItemDAO;
    }

    public void setMdlWikiConfigItemDAO(IMdlWikiConfigItemDAO mdlWikiConfigItemDAO) {
	this.mdlWikiConfigItemDAO = mdlWikiConfigItemDAO;
    }

    public IToolContentHandler getMdlWikiToolContentHandler() {
	return mdlWikiToolContentHandler;
    }

    public void setMdlWikiToolContentHandler(IToolContentHandler mdlWikiToolContentHandler) {
	this.mdlWikiToolContentHandler = mdlWikiToolContentHandler;
    }

    public IMdlWikiSessionDAO getMdlWikiSessionDAO() {
	return mdlWikiSessionDAO;
    }

    public void setMdlWikiSessionDAO(IMdlWikiSessionDAO sessionDAO) {
	this.mdlWikiSessionDAO = sessionDAO;
    }

    public ILamsToolService getToolService() {
	return toolService;
    }

    public void setToolService(ILamsToolService toolService) {
	this.toolService = toolService;
    }

    public IMdlWikiUserDAO getMdlWikiUserDAO() {
	return mdlWikiUserDAO;
    }

    public void setMdlWikiUserDAO(IMdlWikiUserDAO userDAO) {
	this.mdlWikiUserDAO = userDAO;
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

    public MdlWikiOutputFactory getMdlWikiOutputFactory() {
	if (mdlWikiOutputFactory == null) {
	    mdlWikiOutputFactory = new MdlWikiOutputFactory();
	}
	return mdlWikiOutputFactory;
    }

    public void setMdlWikiOutputFactory(MdlWikiOutputFactory mdlWikiOutputFactory) {
	this.mdlWikiOutputFactory = mdlWikiOutputFactory;
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
		    "/org/lamsfoundation/lams/tool/mdwiki/mdlWikiApplicationContext.xml",
		    "/org/lamsfoundation/lams/commonContext.xml" };

	    ApplicationContext context = new ClassPathXmlApplicationContext(contexts);

	    if (context == null)
		throw new MdlWikiException(
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
