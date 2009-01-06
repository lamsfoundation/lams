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

package org.lamsfoundation.lams.tool.mdscrm.service;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

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
import org.lamsfoundation.lams.tool.mdscrm.dao.IMdlScormConfigItemDAO;
import org.lamsfoundation.lams.tool.mdscrm.dao.IMdlScormDAO;
import org.lamsfoundation.lams.tool.mdscrm.dao.IMdlScormSessionDAO;
import org.lamsfoundation.lams.tool.mdscrm.dao.IMdlScormUserDAO;
import org.lamsfoundation.lams.tool.mdscrm.model.MdlScorm;
import org.lamsfoundation.lams.tool.mdscrm.model.MdlScormConfigItem;
import org.lamsfoundation.lams.tool.mdscrm.model.MdlScormSession;
import org.lamsfoundation.lams.tool.mdscrm.model.MdlScormUser;
import org.lamsfoundation.lams.tool.mdscrm.util.MdlScormConstants;
import org.lamsfoundation.lams.tool.mdscrm.util.MdlScormException;
import org.lamsfoundation.lams.tool.mdscrm.util.MdlScormToolContentHandler;
import org.lamsfoundation.lams.tool.mdscrm.util.WebUtility;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.SessionDataExistsException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.HashUtil;
import org.lamsfoundation.lams.util.audit.IAuditService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * An implementation of the IMdlScormService interface.
 * 
 * As a requirement, all LAMS tool's service bean must implement
 * ToolContentManager and ToolSessionManager.
 */

public class MdlScormService implements ToolSessionManager, ToolAdapterContentManager, IMdlScormService,
	ToolContentImport102Manager {

    static Logger logger = Logger.getLogger(MdlScormService.class.getName());

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

    private IMdlScormDAO mdlScormDAO = null;

    private IMdlScormSessionDAO mdlScormSessionDAO = null;

    private IMdlScormUserDAO mdlScormUserDAO = null;

    private IMdlScormConfigItemDAO mdlScormConfigItemDAO = null;

    private ILearnerService learnerService;

    private ILamsToolService toolService;

    private IToolContentHandler mdlScormToolContentHandler = null;

    private IRepositoryService repositoryService = null;

    private IAuditService auditService = null;

    private IExportToolContentService exportContentService;

    private ICoreNotebookService coreNotebookService;

    private IIntegrationService integrationService;

    private MdlScormOutputFactory mdlScormOutputFactory;

    public MdlScormService() {
	super();
	// TODO Auto-generated constructor stub
    }

    /* ************ Methods from ToolSessionManager ************* */
    public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException {
	if (logger.isDebugEnabled()) {
	    logger.debug("entering method createToolSession:" + " toolSessionId = " + toolSessionId
		    + " toolSessionName = " + toolSessionName + " toolContentId = " + toolContentId);
	}

	MdlScormSession session = new MdlScormSession();
	session.setSessionId(toolSessionId);
	session.setSessionName(toolSessionName);

	// learner starts
	MdlScorm mdlScorm = mdlScormDAO.getByContentId(toolContentId);
	session.setMdlScorm(mdlScorm);

	try {
	    // Get the required params, then call the eternal server
	    HashMap<String, String> params = getRequiredExtServletParams(mdlScorm);
	    params.put(EXT_SERVER_PARAM_EXT_TOOL_CONTENT_ID, mdlScorm.getExtToolContentId().toString());
	    session.setExtSessionId(copyExternalToolContent(params));
	} catch (Exception e) {
	    logger.error("Failed to call external server to copy tool content" + e);
	    throw new ToolException("Failed to call external server to copy tool content" + e);
	}

	mdlScormSessionDAO.saveOrUpdate(session);
    }

    /**
     * Calls the external server to copy the content and return a new id
     * 
     * @param extToolContentId
     * @return
     */
    public Long copyExternalToolContent(HashMap<String, String> params) throws ToolException, Exception {

	String cloneServletUrl = mdlScormConfigItemDAO.getConfigItemByKey(MdlScormConfigItem.KEY_EXTERNAL_TOOL_SERVLET)
		.getConfigValue();

	// add the method to the params
	params.put(EXT_SERVER_PARAM_METHOD, EXT_SERVER_METHOD_CLONE);

	// Make the request
	InputStream is = WebUtility.getResponseInputStreamFromExternalServer(cloneServletUrl, params);
	BufferedReader isReader = new BufferedReader(new InputStreamReader(is));
	String str = isReader.readLine();
	if (str == null) {
	    throw new UserInfoFetchException("Fail to clone scorm in .LRN:"
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

    public HashMap<String, String> getRequiredExtServletParams(MdlScorm mdlscorm) {
	HashMap<String, String> params = new HashMap<String, String>();
	params.put(EXT_SERVER_PARAM_USER, mdlscorm.getExtUsername());
	params.put(EXT_SERVER_PARAM_COURSE, mdlscorm.getExtCourseId());
	params.put(EXT_SERVER_PARAM_SECTION, mdlscorm.getExtSection());

	String timestamp = Long.toString(new Date().getTime());
	params.put(EXT_SERVER_PARAM_TIMESTAMP, timestamp);

	ExtServerOrgMap serverMap = this.getExtServerOrgMap();
	String hash = hash(serverMap, mdlscorm.getExtUsername(), timestamp);
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

    public int getExternalToolOutputInt(String outputName, MdlScorm mdlScorm, Long userId, String extToolContentId,
	    Long toolSessionId) {
	MdlScormUser user = this.getUserByUserIdAndSessionId(userId, toolSessionId);
	ExtServerOrgMap extServerMap = getExtServerOrgMap();

	String extUserName = user.getLoginName().substring(extServerMap.getPrefix().length() + 1);

	try {
	    String outputServletUrl = mdlScormConfigItemDAO.getConfigItemByKey(
		    MdlScormConfigItem.KEY_EXTERNAL_TOOL_SERVLET).getConfigValue();

	    // setting the mdlScorm username so the params are set up correctly
	    mdlScorm.setExtUsername(extUserName);
	    HashMap<String, String> params = getRequiredExtServletParams(mdlScorm);
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
	mdlScormOutputFactory = getMdlScormOutputFactory();
	MdlScormSession session = this.getSessionBySessionId(toolSessionId);
	if (session == null) {
	    return null;
	}
	return mdlScormOutputFactory.getToolOutput(names, this, toolSessionId, learnerId, session.getMdlScorm(),
		session.getExtSessionId());
    }

    /**
     * Get the tool output for the given tool output name.
     * 
     * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.lang.String,
     *      java.lang.Long, java.lang.Long)
     */
    public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
	mdlScormOutputFactory = getMdlScormOutputFactory();
	MdlScormSession session = this.getSessionBySessionId(toolSessionId);
	if (session == null) {
	    return null;
	}

	return mdlScormOutputFactory.getToolOutput(name, this, toolSessionId, learnerId, session.getMdlScorm(), session
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
	mdlScormOutputFactory = getMdlScormOutputFactory();
	MdlScorm mdscrm = getMdlScormByContentId(toolContentId);
	if (mdscrm == null) {
	    mdscrm = getDefaultContent();
	}
	return mdlScormOutputFactory.getToolOutputDefinitions(mdscrm);
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
	mdlScormSessionDAO.deleteBySessionID(toolSessionId);
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

	MdlScorm fromContent = null;
	if (fromContentId != null) {
	    fromContent = mdlScormDAO.getByContentId(fromContentId);
	}
	if (fromContent == null) {
	    // create the fromContent using the default tool content
	    fromContent = getDefaultContent();
	}
	MdlScorm toContent = MdlScorm.newInstance(fromContent, toContentId, mdlScormToolContentHandler);

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

	mdlScormDAO.saveOrUpdate(toContent);
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
	    throw new ToolException("mdlScorm tool cusomCSV not in required (user,course,courseURL) form: "
		    + EXPECTED_CSV_FORM);
	}

	MdlScorm fromContent = null;
	if (fromContentId != null) {
	    fromContent = mdlScormDAO.getByContentId(fromContentId);
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
	MdlScorm toContent = MdlScorm.newInstance(fromContent, toContentId, mdlScormToolContentHandler);
	toContent.setByCustomCSVHashMap(mapCSV);

	// calling the external tool to copy it's content.
	try {
	    params.put(EXT_SERVER_PARAM_EXT_TOOL_CONTENT_ID, fromContent.getExtToolContentId().toString());
	    toContent.setExtToolContentId(copyExternalToolContent(params));
	} catch (Exception e) {
	    throw new ToolException("Failed to call external server to copy tool content" + e);
	}

	mdlScormDAO.saveOrUpdate(toContent);
    }

    public void setAsDefineLater(Long toolContentId, boolean value) throws DataMissingException, ToolException {
	MdlScorm mdlScorm = mdlScormDAO.getByContentId(toolContentId);
	if (mdlScorm == null) {
	    throw new ToolException("Could not find tool with toolContentID: " + toolContentId);
	}
	mdlScorm.setDefineLater(value);
	mdlScormDAO.saveOrUpdate(mdlScorm);
    }

    public void setAsRunOffline(Long toolContentId, boolean value) throws DataMissingException, ToolException {
	MdlScorm mdlScorm = mdlScormDAO.getByContentId(toolContentId);
	if (mdlScorm == null) {
	    throw new ToolException("Could not find tool with toolContentID: " + toolContentId);
	}
	mdlScorm.setRunOffline(value);
	mdlScormDAO.saveOrUpdate(mdlScorm);
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

	MdlScorm mdlScorm = mdlScormDAO.getByContentId(toolContentId);
	if (mdlScorm == null) {
	    mdlScorm = getDefaultContent();
	}
	if (mdlScorm == null)
	    throw new DataMissingException("Unable to find default content for the mdlScorm tool");

	// If no external content was found, export empty mdlScorm
	// Otherwise, call the external servlet to get the export file
	if (mdlScorm.getExtToolContentId() == null) {
	    mdlScorm.setExtToolContentId(null);
	    mdlScorm.setToolContentHandler(null);
	    mdlScorm.setMdlScormSessions(null);

	    try {
		exportContentService.exportToolContent(toolContentId, mdlScorm, mdlScormToolContentHandler, rootPath);
	    } catch (ExportToolContentException e) {
		throw new ToolException(e);
	    }
	} else {

	    URLConnection conn = null;
	    try {
		// Create the directory to store the export file
		String toolPath = FileUtil.getFullPath(rootPath, toolContentId.toString());
		FileUtil.createDirectory(toolPath);

		String exportServletUrl = mdlScormConfigItemDAO.getConfigItemByKey(
			MdlScormConfigItem.KEY_EXTERNAL_TOOL_SERVLET).getConfigValue();

		// setting these to arbitrary values since they are only used to construct the hash

		mdlScorm.setExtCourseId("extCourse");
		mdlScorm.setExtSection("0");
		mdlScorm.setExtUsername("authUser");
		HashMap<String, String> params = this.getRequiredExtServletParams(mdlScorm);
		params.put(EXT_SERVER_PARAM_METHOD, EXT_SERVER_METHOD_EXPORT);
		params.put(EXT_SERVER_PARAM_EXT_TOOL_CONTENT_ID, mdlScorm.getExtToolContentId().toString());

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
		logger.debug("Path to mdlScorm export content: " + toolPath + "/ext_tool.txt");

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
	IToolVO tool = toolService.getToolBySignature(MdlScormConstants.TOOL_SIGNATURE);
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
	// otherwise, simply import an empty mdlScorm
	if (extExportFile.exists()) {

	    try {

		String importServletUrl = mdlScormConfigItemDAO.getConfigItemByKey(
			MdlScormConfigItem.KEY_EXTERNAL_TOOL_SERVLET).getConfigValue();

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

		// Save the resulting mdl scorm 
		MdlScorm mdlScorm = new MdlScorm();
		mdlScorm.setToolContentId(toolContentId);
		mdlScorm.setCreateDate(new Date());
		mdlScorm.setExtToolContentId(extContentId);
		saveOrUpdateMdlScorm(mdlScorm);

	    } catch (Exception e) {
		logger.error("Problem passing mdlScorm export file to external server", e);
		throw new ToolException(e);
	    }
	} else // use the normal LAMS method of importing activities from an xml file
	{

	    try {
		Object toolPOJO = exportContentService.importToolContent(toolContentPath, mdlScormToolContentHandler,
			fromVersion, toVersion);
		if (!(toolPOJO instanceof MdlScorm))
		    throw new ImportToolContentException("Import MdlScorm tool content failed. Deserialized object is "
			    + toolPOJO);
		MdlScorm mdlScorm = (MdlScorm) toolPOJO;
		// reset it to new toolContentId
		mdlScorm.setToolContentId(toolContentId);
		mdlScormDAO.saveOrUpdate(mdlScorm);
	    } catch (Exception e) {
		throw new ToolException(e);
	    }
	}
    }

    /* ********** IMdlScormService Methods ********************************* */

    public Long getDefaultContentIdBySignature(String toolSignature) {
	Long toolContentId = null;
	toolContentId = new Long(toolService.getToolDefaultContentIdBySignature(toolSignature));
	if (toolContentId == null) {
	    String error = "Could not retrieve default content id for this tool";
	    logger.error(error);
	    throw new MdlScormException(error);
	}
	return toolContentId;
    }

    public MdlScorm getDefaultContent() {
	/*
	Long defaultContentID = getDefaultContentIdBySignature(MdlScormConstants.TOOL_SIGNATURE);
	MdlScorm defaultContent = getMdlScormByContentId(defaultContentID);
	if (defaultContent == null) {
		String error = "Could not retrieve default content record for this tool";
		logger.error(error);
		throw new MdlScormException(error);
	}
	return defaultContent;
	*/
	MdlScorm defaultContent = new MdlScorm();
	return defaultContent;
    }

    public MdlScorm copyDefaultContent(Long newContentID) {

	if (newContentID == null) {
	    String error = "Cannot copy the MdlScorm tools default content: + " + "newContentID is null";
	    logger.error(error);
	    throw new MdlScormException(error);
	}

	MdlScorm defaultContent = getDefaultContent();
	// create new mdlScorm using the newContentID
	MdlScorm newContent = new MdlScorm();
	newContent = MdlScorm.newInstance(defaultContent, newContentID, mdlScormToolContentHandler);
	mdlScormDAO.saveOrUpdate(newContent);
	return newContent;
    }

    public MdlScorm getMdlScormByContentId(Long toolContentID) {
	MdlScorm mdlScorm = (MdlScorm) mdlScormDAO.getByContentId(toolContentID);
	if (mdlScorm == null) {
	    logger.debug("Could not find the content with toolContentID:" + toolContentID);
	}
	return mdlScorm;
    }

    public MdlScormConfigItem getConfigItem(String key) {
	return mdlScormConfigItemDAO.getConfigItemByKey(key);
    }

    public void saveOrUpdateMdlScormConfigItem(MdlScormConfigItem item) {
	mdlScormConfigItemDAO.saveOrUpdate(item);
    }

    public MdlScormSession getSessionBySessionId(Long toolSessionId) {
	MdlScormSession mdlScormSession = mdlScormSessionDAO.getBySessionId(toolSessionId);
	if (mdlScormSession == null) {
	    logger.debug("Could not find the mdlScorm session with toolSessionID:" + toolSessionId);
	}
	return mdlScormSession;
    }

    public MdlScormUser getUserByUserIdAndSessionId(Long userId, Long toolSessionId) {
	return mdlScormUserDAO.getByUserIdAndSessionId(userId, toolSessionId);
    }

    public MdlScormUser getUserByLoginNameAndSessionId(String loginName, Long toolSessionId) {
	return mdlScormUserDAO.getByLoginNameAndSessionId(loginName, toolSessionId);
    }

    public MdlScormUser getUserByUID(Long uid) {
	return mdlScormUserDAO.getByUID(uid);
    }

    public void deleteFromRepository(Long uuid, Long versionID) throws MdlScormException {
	ITicket ticket = getRepositoryLoginTicket();
	try {
	    repositoryService.deleteVersion(ticket, uuid, versionID);
	} catch (Exception e) {
	    throw new MdlScormException("Exception occured while deleting files from" + " the repository "
		    + e.getMessage());
	}
    }

    public void saveOrUpdateMdlScorm(MdlScorm mdlScorm) {
	mdlScormDAO.saveOrUpdate(mdlScorm);
    }

    public void saveOrUpdateMdlScormSession(MdlScormSession mdlScormSession) {
	mdlScormSessionDAO.saveOrUpdate(mdlScormSession);
    }

    public void saveOrUpdateMdlScormUser(MdlScormUser mdlScormUser) {
	mdlScormUserDAO.saveOrUpdate(mdlScormUser);
    }

    public MdlScormUser createMdlScormUser(UserDTO user, MdlScormSession mdlScormSession) {
	MdlScormUser mdlScormUser = new MdlScormUser(user, mdlScormSession);
	saveOrUpdateMdlScormUser(mdlScormUser);
	return mdlScormUser;
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
    private ITicket getRepositoryLoginTicket() throws MdlScormException {
	repositoryService = RepositoryProxy.getRepositoryService();
	ICredentials credentials = new SimpleCredentials(MdlScormToolContentHandler.repositoryUser,
		MdlScormToolContentHandler.repositoryId);
	try {
	    ITicket ticket = repositoryService.login(credentials, MdlScormToolContentHandler.repositoryWorkspaceName);
	    return ticket;
	} catch (AccessDeniedException ae) {
	    throw new MdlScormException("Access Denied to repository." + ae.getMessage());
	} catch (WorkspaceNotFoundException we) {
	    throw new MdlScormException("Workspace not found." + we.getMessage());
	} catch (LoginException e) {
	    throw new MdlScormException("Login failed." + e.getMessage());
	}
    }

    /* ===============Methods implemented from ToolContentImport102Manager =============== */

    /**
     * Import the data for a 1.0.2 MdlScorm
     */
    public void import102ToolContent(Long toolContentId, UserDTO user, Hashtable importValues) {
	Date now = new Date();
	MdlScorm mdlScorm = new MdlScorm();
	mdlScorm.setCreateDate(now);
	mdlScorm.setDefineLater(Boolean.FALSE);
	mdlScorm.setRunOffline(Boolean.FALSE);
	mdlScorm.setToolContentId(toolContentId);
	mdlScorm.setUpdateDate(now);

	mdlScormDAO.saveOrUpdate(mdlScorm);
    }

    /**
     * Set the description, throws away the title value as this is not supported
     * in 2.0
     */
    public void setReflectiveData(Long toolContentId, String title, String description) throws ToolException,
	    DataMissingException {

	logger
		.warn("Setting the reflective field on a mdlScorm. This doesn't make sense as the mdlScorm is for reflection and we don't reflect on reflection!");
	MdlScorm mdlScorm = getMdlScormByContentId(toolContentId);
	if (mdlScorm == null) {
	    throw new DataMissingException("Unable to set reflective data titled " + title
		    + " on activity toolContentId " + toolContentId + " as the tool content does not exist.");
	}

	//mdlScorm.setInstructions(description);
    }

    //=========================================================================================
    /* ********** Used by Spring to "inject" the linked objects ************* */

    public IMdlScormDAO getMdlScormDAO() {
	return mdlScormDAO;
    }

    public void setMdlScormDAO(IMdlScormDAO mdlScormDAO) {
	this.mdlScormDAO = mdlScormDAO;
    }

    public IMdlScormConfigItemDAO getMdlScormConfigItemDAO() {
	return mdlScormConfigItemDAO;
    }

    public void setMdlScormConfigItemDAO(IMdlScormConfigItemDAO mdlScormConfigItemDAO) {
	this.mdlScormConfigItemDAO = mdlScormConfigItemDAO;
    }

    public IToolContentHandler getMdlScormToolContentHandler() {
	return mdlScormToolContentHandler;
    }

    public void setMdlScormToolContentHandler(IToolContentHandler mdlScormToolContentHandler) {
	this.mdlScormToolContentHandler = mdlScormToolContentHandler;
    }

    public IMdlScormSessionDAO getMdlScormSessionDAO() {
	return mdlScormSessionDAO;
    }

    public void setMdlScormSessionDAO(IMdlScormSessionDAO sessionDAO) {
	this.mdlScormSessionDAO = sessionDAO;
    }

    public ILamsToolService getToolService() {
	return toolService;
    }

    public void setToolService(ILamsToolService toolService) {
	this.toolService = toolService;
    }

    public IMdlScormUserDAO getMdlScormUserDAO() {
	return mdlScormUserDAO;
    }

    public void setMdlScormUserDAO(IMdlScormUserDAO userDAO) {
	this.mdlScormUserDAO = userDAO;
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

    public MdlScormOutputFactory getMdlScormOutputFactory() {
	if (mdlScormOutputFactory == null) {
	    mdlScormOutputFactory = new MdlScormOutputFactory();
	}
	return mdlScormOutputFactory;
    }

    public void setMdlScormOutputFactory(MdlScormOutputFactory mdlScormOutputFactory) {
	this.mdlScormOutputFactory = mdlScormOutputFactory;
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
		    "/org/lamsfoundation/lams/tool/mdscrm/mdlScormApplicationContext.xml",
		    "/org/lamsfoundation/lams/commonContext.xml" };

	    ApplicationContext context = new ClassPathXmlApplicationContext(contexts);

	    if (context == null)
		throw new MdlScormException(
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
