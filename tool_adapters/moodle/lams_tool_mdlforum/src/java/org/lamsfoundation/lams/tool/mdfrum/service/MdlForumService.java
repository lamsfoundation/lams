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

package org.lamsfoundation.lams.tool.mdfrum.service;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
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
import org.lamsfoundation.lams.integration.ExtServerToolAdapterMap;
import org.lamsfoundation.lams.integration.UserInfoFetchException;
import org.lamsfoundation.lams.integration.service.IIntegrationService;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.ToolAdapterContentManager;
import org.lamsfoundation.lams.tool.ToolContentImport102Manager;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.SessionDataExistsException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.mdfrum.dao.IMdlForumDAO;
import org.lamsfoundation.lams.tool.mdfrum.dao.IMdlForumSessionDAO;
import org.lamsfoundation.lams.tool.mdfrum.dao.IMdlForumUserDAO;
import org.lamsfoundation.lams.tool.mdfrum.model.MdlForum;
import org.lamsfoundation.lams.tool.mdfrum.model.MdlForumSession;
import org.lamsfoundation.lams.tool.mdfrum.model.MdlForumUser;
import org.lamsfoundation.lams.tool.mdfrum.util.MdlForumConstants;
import org.lamsfoundation.lams.tool.mdfrum.util.MdlForumException;
import org.lamsfoundation.lams.tool.mdfrum.util.MdlForumToolContentHandler;
import org.lamsfoundation.lams.tool.mdfrum.util.WebUtility;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.HashUtil;
import org.lamsfoundation.lams.util.audit.IAuditService;

/**
 * An implementation of the IMdlForumService interface.
 * 
 * As a requirement, all LAMS tool's service bean must implement
 * ToolContentManager and ToolSessionManager.
 */

public class MdlForumService implements ToolSessionManager, ToolAdapterContentManager, IMdlForumService,
	ToolContentImport102Manager {

    static Logger logger = Logger.getLogger(MdlForumService.class.getName());

    public static final String CUSTOM_CSV_MAP_PARAM_USER = "user";
    public static final String CUSTOM_CSV_MAP_PARAM_COURSE = "course";
    public static final String CUSTOM_CSV_MAP_PARAM_SECTION = "section";
    public static final String CUSTOM_CSV_MAP_PARAM_EXT_LMS_ID = "extlmsid";

    public static final String EXT_SERVER_PARAM_USER = "un";
    public static final String EXT_SERVER_PARAM_COURSE = "cs";
    public static final String EXT_SERVER_PARAM_SECTION = "section";
    public static final String EXT_SERVER_PARAM_TIMESTAMP = "ts";
    public static final String EXT_SERVER_PARAM_HASH = "hs";
    public static final String EXT_SERVER_PARAM_METHOD = "method";
    public static final String EXT_SERVER_PARAM_EXT_TOOL_CONTENT_ID = "extToolContentID";
    public static final String EXT_SERVER_PARAM_UPLOAD_FILE = "upload_file";
    public static final String EXT_SERVER_PARAM_OUTPUT_NAME = "oname";

    private static final int EXPECTED_CSV_SIZE = 4;
    private static final String EXPECTED_CSV_FORM = "user,course";

    private IMdlForumDAO mdlForumDAO = null;

    private IMdlForumSessionDAO mdlForumSessionDAO = null;

    private IMdlForumUserDAO mdlForumUserDAO = null;

    private ILearnerService learnerService;

    private ILamsToolService toolService;

    private IToolContentHandler mdlForumToolContentHandler = null;

    private IRepositoryService repositoryService = null;

    private IAuditService auditService = null;

    private IExportToolContentService exportContentService;

    private ICoreNotebookService coreNotebookService;

    private IIntegrationService integrationService;

    private MdlForumOutputFactory mdlForumOutputFactory;

    public MdlForumService() {
	super();
	// TODO Auto-generated constructor stub
    }

    /* ************ Methods from ToolSessionManager ************* */
    public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException {
	if (logger.isDebugEnabled()) {
	    logger.debug("entering method createToolSession:" + " toolSessionId = " + toolSessionId
		    + " toolSessionName = " + toolSessionName + " toolContentId = " + toolContentId);
	}

	MdlForumSession session = new MdlForumSession();
	session.setSessionId(toolSessionId);
	session.setSessionName(toolSessionName);

	// learner starts
	MdlForum mdlForum = mdlForumDAO.getByContentId(toolContentId);
	session.setMdlForum(mdlForum);

	try {
	    // Get the required params, then call the eternal server
	    HashMap<String, String> params = getRequiredExtServletParams(mdlForum);
	    params.put(EXT_SERVER_PARAM_EXT_TOOL_CONTENT_ID, mdlForum.getExtToolContentId().toString());
	    session.setExtSessionId(copyExternalToolContent(params));
	} catch (Exception e) {
	    logger.error("Failed to call external server to copy tool content" + e);
	    throw new ToolException("Failed to call external server to copy tool content" + e);
	}

	mdlForumSessionDAO.saveOrUpdate(session);
    }

    /**
     * Calls the external server to copy the content and return a new id
     * 
     * @param extToolContentId
     * @return
     */
    public Long copyExternalToolContent(HashMap<String, String> params) throws ToolException, Exception {

	String cloneServletUrl = getExtToolAdapterServletUrl(params.get(CUSTOM_CSV_MAP_PARAM_EXT_LMS_ID));

	// add the method to the params
	params.put(EXT_SERVER_PARAM_METHOD, EXT_SERVER_METHOD_CLONE);

	// Make the request
	InputStream is = WebUtility.getResponseInputStreamFromExternalServer(cloneServletUrl, params);
	BufferedReader isReader = new BufferedReader(new InputStreamReader(is));
	String str = isReader.readLine();
	if (str == null) {
	    throw new UserInfoFetchException("Fail to clone forum in .LRN:"
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
	params.put(CUSTOM_CSV_MAP_PARAM_EXT_LMS_ID, paramsCSV.get(CUSTOM_CSV_MAP_PARAM_EXT_LMS_ID));

	String timestamp = Long.toString(new Date().getTime());
	params.put(EXT_SERVER_PARAM_TIMESTAMP, timestamp);

	ExtServerOrgMap serverMap = getExtServerOrgMap(paramsCSV.get(CUSTOM_CSV_MAP_PARAM_EXT_LMS_ID));

	this.getExtServerOrgMap(paramsCSV.get(CUSTOM_CSV_MAP_PARAM_EXT_LMS_ID));
	String hash = hash(serverMap, paramsCSV.get(CUSTOM_CSV_MAP_PARAM_USER), timestamp);
	params.put(EXT_SERVER_PARAM_HASH, hash);

	return params;
    }

    public HashMap<String, String> getRequiredExtServletParams(MdlForum mdlforum) {
	HashMap<String, String> params = new HashMap<String, String>();
	params.put(EXT_SERVER_PARAM_USER, mdlforum.getExtUsername());
	params.put(EXT_SERVER_PARAM_COURSE, mdlforum.getExtCourseId());
	params.put(EXT_SERVER_PARAM_SECTION, mdlforum.getExtSection());
	params.put(CUSTOM_CSV_MAP_PARAM_EXT_LMS_ID, mdlforum.getExtLmsId());

	String timestamp = Long.toString(new Date().getTime());
	params.put(EXT_SERVER_PARAM_TIMESTAMP, timestamp);

	ExtServerOrgMap serverMap = getExtServerOrgMap(mdlforum.getExtLmsId());
	String hash = hash(serverMap, mdlforum.getExtUsername(), timestamp);
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
	    map.put(CUSTOM_CSV_MAP_PARAM_EXT_LMS_ID, split[3]);
	    return map;
	} else {
	    return null;
	}
    }

    public int getExternalToolOutputInt(String outputName, MdlForum mdlForum, Long userId, String extToolContentId,
	    Long toolSessionId) {
	MdlForumUser user = this.getUserByUserIdAndSessionId(userId, toolSessionId);
	ExtServerOrgMap extServerMap = getExtServerOrgMap(mdlForum.getExtLmsId());

	String extUserName = user.getLoginName().substring(extServerMap.getPrefix().length() + 1);

	try {
	    String outputServletUrl = this.getExtToolAdapterServletUrl(mdlForum.getExtLmsId());

	    // setting the mdlForum username so the params are set up correctly
	    mdlForum.setExtUsername(extUserName);
	    HashMap<String, String> params = getRequiredExtServletParams(mdlForum);
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

    public String getExtServerUrl(String extLmsId) {
	ExtServerOrgMap serverMap = integrationService.getExtServerOrgMap(extLmsId);
	String ret = null;
	if (serverMap != null) {
	    ret = serverMap.getServerUrl();
	}
	return ret;
    }

    public String getExtToolAdapterServletUrl(String extLmsId) {
	String ret = getExtServerUrl(extLmsId);
	if (ret != null) {
	    ret += MdlForumConstants.RELATIVE_SERVLET_URL;
	}
	return ret;
    }

    public ExtServerOrgMap getExtServerOrgMap(String extLmsId) {
	return integrationService.getExtServerOrgMap(extLmsId);
    }

    /**
     * Get the tool output for the given tool output names.
     * 
     * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.util.List<String>,
     *      java.lang.Long, java.lang.Long)
     */
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, Long toolSessionId, Long learnerId) {
	mdlForumOutputFactory = getMdlForumOutputFactory();
	MdlForumSession session = this.getSessionBySessionId(toolSessionId);
	if (session == null) {
	    return null;
	}
	return mdlForumOutputFactory.getToolOutput(names, this, toolSessionId, learnerId, session.getMdlForum(),
		session.getExtSessionId());
    }

    /**
     * Get the tool output for the given tool output name.
     * 
     * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.lang.String,
     *      java.lang.Long, java.lang.Long)
     */
    public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
	mdlForumOutputFactory = getMdlForumOutputFactory();
	MdlForumSession session = this.getSessionBySessionId(toolSessionId);
	if (session == null) {
	    return null;
	}

	return mdlForumOutputFactory.getToolOutput(name, this, toolSessionId, learnerId, session.getMdlForum(), session
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
	mdlForumOutputFactory = getMdlForumOutputFactory();
	MdlForum mdfrum = getMdlForumByContentId(toolContentId);
	if (mdfrum == null) {
	    mdfrum = getDefaultContent();
	}
	return mdlForumOutputFactory.getToolOutputDefinitions(mdfrum);
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
	mdlForumSessionDAO.deleteBySessionID(toolSessionId);
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

	MdlForum fromContent = null;
	if (fromContentId != null) {
	    fromContent = mdlForumDAO.getByContentId(fromContentId);
	}
	if (fromContent == null) {
	    // create the fromContent using the default tool content
	    fromContent = getDefaultContent();
	}
	MdlForum toContent = MdlForum.newInstance(fromContent, toContentId, mdlForumToolContentHandler);

	mdlForumDAO.saveOrUpdate(toContent);
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
	    throw new ToolException("mdlForum tool cusomCSV not in required (user,course,courseURL) form: "
		    + EXPECTED_CSV_FORM);
	}

	MdlForum fromContent = null;
	if (fromContentId != null) {
	    fromContent = mdlForumDAO.getByContentId(fromContentId);
	}

	// Set the default params for the external cloning request
	HashMap<String, String> params = getRequiredExtServletParams(customCSV);

	if ((fromContent == null) || fromContent.getExtToolContentId() == null) {
	    // create the fromContent using the default tool content
	    fromContent = getDefaultContent(params.get(CUSTOM_CSV_MAP_PARAM_EXT_LMS_ID));
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
	MdlForum toContent = MdlForum.newInstance(fromContent, toContentId, mdlForumToolContentHandler);
	toContent.setByCustomCSVHashMap(mapCSV);

	// calling the external tool to copy it's content.
	try {
	    params.put(EXT_SERVER_PARAM_EXT_TOOL_CONTENT_ID, fromContent.getExtToolContentId().toString());
	    toContent.setExtToolContentId(copyExternalToolContent(params));
	} catch (Exception e) {
	    throw new ToolException("Failed to call external server to copy tool content" + e);
	}

	mdlForumDAO.saveOrUpdate(toContent);
    }

    public void setAsDefineLater(Long toolContentId, boolean value) throws DataMissingException, ToolException {
	MdlForum mdlForum = mdlForumDAO.getByContentId(toolContentId);
	if (mdlForum == null) {
	    throw new ToolException("Could not find tool with toolContentID: " + toolContentId);
	}
	mdlForum.setDefineLater(value);
	mdlForumDAO.saveOrUpdate(mdlForum);
    }

    public void setAsRunOffline(Long toolContentId, boolean value) throws DataMissingException, ToolException {
	MdlForum mdlForum = mdlForumDAO.getByContentId(toolContentId);
	if (mdlForum == null) {
	    throw new ToolException("Could not find tool with toolContentID: " + toolContentId);
	}
	mdlForum.setRunOffline(value);
	mdlForumDAO.saveOrUpdate(mdlForum);
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

	MdlForum mdlForum = mdlForumDAO.getByContentId(toolContentId);
	if (mdlForum == null) {
	    mdlForum = getDefaultContent();
	}
	if (mdlForum == null)
	    throw new DataMissingException("Unable to find default content for the mdlForum tool");

	// If no external content was found, export empty mdlForum
	// Otherwise, call the external servlet to get the export file
	if (mdlForum.getExtToolContentId() == null) {
	    mdlForum.setExtToolContentId(null);
	    mdlForum.setToolContentHandler(null);
	    mdlForum.setMdlForumSessions(null);

	    try {
		exportContentService.exportToolContent(toolContentId, mdlForum, mdlForumToolContentHandler, rootPath);
	    } catch (ExportToolContentException e) {
		throw new ToolException(e);
	    }
	} else {

	    try {
		// Create the directory to store the export file
		String toolPath = FileUtil.getFullPath(rootPath, toolContentId.toString());
		FileUtil.createDirectory(toolPath);

		String exportServletUrl = getExtToolAdapterServletUrl(mdlForum.getExtLmsId());

		if (exportServletUrl != null) {
		    // setting these to arbitrary values since they are only used to construct the hash
		    mdlForum.setExtCourseId("extCourse");
		    mdlForum.setExtSection("0");
		    mdlForum.setExtUsername("authUser");
		    HashMap<String, String> params = this.getRequiredExtServletParams(mdlForum);
		    params.put(EXT_SERVER_PARAM_METHOD, EXT_SERVER_METHOD_EXPORT);
		    params.put(EXT_SERVER_PARAM_EXT_TOOL_CONTENT_ID, mdlForum.getExtToolContentId().toString());

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
		    logger.debug("Path to mdlForum export content: " + toolPath + "/ext_tool.txt");

		    out.flush();
		    out.close();
		    in.close();
		} else {
		    exportContentService.exportToolContent(toolContentId, mdlForum, mdlForumToolContentHandler,
			    rootPath);
		}

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
	// otherwise, simply import an empty mdlForum
	if (extExportFile.exists()) {

	    try {

		if (customCSV == null) {
		    logger.error("Could not retrieve customCSV required for importing tool adapter tool. CustomCSV: "
			    + customCSV);
		    throw new ToolException(
			    "Could not retrieve customCSV required for importing tool adapter tool. CustomCSV: "
				    + customCSV);
		}

		HashMap<String, String> customCSVMap = decodeCustomCSV(customCSV);
		String importServletUrl = getExtToolAdapterServletUrl(customCSVMap.get(CUSTOM_CSV_MAP_PARAM_EXT_LMS_ID));

		HashMap<String, String> params = getRequiredExtServletParams(customCSV);
		params.put(EXT_SERVER_PARAM_METHOD, EXT_SERVER_METHOD_IMPORT);

		// Do the external multipart post to upload the file to external LMS (returns an extToolContentId)
		InputStream is = WebUtility.performMultipartPost(extExportFile, EXT_SERVER_PARAM_UPLOAD_FILE,
			importServletUrl, params);
		DataInputStream inStream = new DataInputStream(is);
		String str = inStream.readLine();
		Long extContentId = Long.parseLong(str);
		inStream.close();

		// Save the resulting mdl forum 
		MdlForum mdlForum = new MdlForum();
		mdlForum.setToolContentId(toolContentId);
		mdlForum.setCreateDate(new Date());
		mdlForum.setExtToolContentId(extContentId);
		saveOrUpdateMdlForum(mdlForum);

	    } catch (Exception e) {
		logger.error("Problem passing mdlForum export file to external server", e);
		throw new ToolException(e);
	    }
	} else // use the normal LAMS method of importing activities from an xml file
	{

	    try {
		Object toolPOJO = exportContentService.importToolContent(toolContentPath, mdlForumToolContentHandler,
			fromVersion, toVersion);
		if (!(toolPOJO instanceof MdlForum))
		    throw new ImportToolContentException("Import MdlForum tool content failed. Deserialized object is "
			    + toolPOJO);
		MdlForum mdlForum = (MdlForum) toolPOJO;
		// reset it to new toolContentId
		mdlForum.setToolContentId(toolContentId);
		mdlForumDAO.saveOrUpdate(mdlForum);
	    } catch (Exception e) {
		throw new ToolException(e);
	    }
	}
    }

    /* ********** IMdlForumService Methods ********************************* */

    public Long getDefaultContentIdBySignature(String toolSignature) {
	Long toolContentId = null;
	toolContentId = new Long(toolService.getToolDefaultContentIdBySignature(toolSignature));
	if (toolContentId == null) {
	    String error = "Could not retrieve default content id for this tool";
	    logger.error(error);
	    throw new MdlForumException(error);
	}
	return toolContentId;
    }

    public MdlForum getDefaultContent() {
	MdlForum defaultContent = new MdlForum();
	return defaultContent;
    }

    public MdlForum getDefaultContent(String extLmsId) {
	MdlForum defaultContent = new MdlForum();
	defaultContent.setExtLmsId(extLmsId);
	return defaultContent;
    }

    public MdlForum copyDefaultContent(Long newContentID) {

	if (newContentID == null) {
	    String error = "Cannot copy the MdlForum tools default content: + " + "newContentID is null";
	    logger.error(error);
	    throw new MdlForumException(error);
	}

	MdlForum defaultContent = getDefaultContent();
	// create new mdlForum using the newContentID
	MdlForum newContent = new MdlForum();
	newContent = MdlForum.newInstance(defaultContent, newContentID, mdlForumToolContentHandler);
	mdlForumDAO.saveOrUpdate(newContent);
	return newContent;
    }

    @SuppressWarnings("unchecked")
    public List<ExtServerOrgMap> getExtServerList() {
	if (integrationService.getAllExtServerOrgMaps() != null) {
	    return (List<ExtServerOrgMap>) integrationService.getAllExtServerOrgMaps();
	} else {
	    return null;
	}

    }

    public List<ExtServerToolAdapterMap> getMappedServers() {
	return integrationService.getMappedServers(MdlForumConstants.TOOL_SIGNATURE);
    }

    public void saveServerMappings(String[] mappedServers) {
	Tool tool = toolService.getPersistToolBySignature(MdlForumConstants.TOOL_SIGNATURE);

	Set<ExtServerToolAdapterMap> mappedAdapterServers = new HashSet<ExtServerToolAdapterMap>();

	List<ExtServerToolAdapterMap> alreadyMapped = getMappedServers();

	if (tool != null) {
	    if (mappedServers != null) {

		for (int i = 0; i < mappedServers.length; i++) {

		    ExtServerOrgMap serverMap = integrationService.getExtServerOrgMap(mappedServers[i]);

		    if (serverMap != null) {

			ExtServerToolAdapterMap serverToolMap = integrationService.getMappedServer(serverMap
				.getServerid(), MdlForumConstants.TOOL_SIGNATURE);

			if (serverToolMap == null) {
			    serverToolMap = new ExtServerToolAdapterMap(tool, serverMap);
			    integrationService.saveExtServerToolAdapterMap(serverToolMap);
			}
			mappedAdapterServers.add(serverToolMap);
		    }
		}
	    }
	}

	for (ExtServerToolAdapterMap map : alreadyMapped) {
	    if (!mappedAdapterServers.contains(map)) {
		integrationService.deleteExtServerToolAdapterMap(map);
	    }
	}

	//tool.setMappedServers(mappedAdapterServers);
	//toolService.saveOrUpdateTool(tool);
    }

    public MdlForum getMdlForumByContentId(Long toolContentID) {
	MdlForum mdlForum = (MdlForum) mdlForumDAO.getByContentId(toolContentID);
	if (mdlForum == null) {
	    logger.debug("Could not find the content with toolContentID:" + toolContentID);
	}
	return mdlForum;
    }

    public MdlForumSession getSessionBySessionId(Long toolSessionId) {
	MdlForumSession mdlForumSession = mdlForumSessionDAO.getBySessionId(toolSessionId);
	if (mdlForumSession == null) {
	    logger.debug("Could not find the mdlForum session with toolSessionID:" + toolSessionId);
	}
	return mdlForumSession;
    }

    public MdlForumUser getUserByUserIdAndSessionId(Long userId, Long toolSessionId) {
	return mdlForumUserDAO.getByUserIdAndSessionId(userId, toolSessionId);
    }

    public MdlForumUser getUserByLoginNameAndSessionId(String loginName, Long toolSessionId) {
	return mdlForumUserDAO.getByLoginNameAndSessionId(loginName, toolSessionId);
    }

    public MdlForumUser getUserByUID(Long uid) {
	return mdlForumUserDAO.getByUID(uid);
    }

    public void deleteFromRepository(Long uuid, Long versionID) throws MdlForumException {
	ITicket ticket = getRepositoryLoginTicket();
	try {
	    repositoryService.deleteVersion(ticket, uuid, versionID);
	} catch (Exception e) {
	    throw new MdlForumException("Exception occured while deleting files from" + " the repository "
		    + e.getMessage());
	}
    }

    public void saveOrUpdateMdlForum(MdlForum mdlForum) {
	mdlForumDAO.saveOrUpdate(mdlForum);
    }

    public void saveOrUpdateMdlForumSession(MdlForumSession mdlForumSession) {
	mdlForumSessionDAO.saveOrUpdate(mdlForumSession);
    }

    public void saveOrUpdateMdlForumUser(MdlForumUser mdlForumUser) {
	mdlForumUserDAO.saveOrUpdate(mdlForumUser);
    }

    public MdlForumUser createMdlForumUser(UserDTO user, MdlForumSession mdlForumSession) {
	MdlForumUser mdlForumUser = new MdlForumUser(user, mdlForumSession);
	saveOrUpdateMdlForumUser(mdlForumUser);
	return mdlForumUser;
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
    private ITicket getRepositoryLoginTicket() throws MdlForumException {
	repositoryService = RepositoryProxy.getRepositoryService();
	ICredentials credentials = new SimpleCredentials(MdlForumToolContentHandler.repositoryUser,
		MdlForumToolContentHandler.repositoryId);
	try {
	    ITicket ticket = repositoryService.login(credentials, MdlForumToolContentHandler.repositoryWorkspaceName);
	    return ticket;
	} catch (AccessDeniedException ae) {
	    throw new MdlForumException("Access Denied to repository." + ae.getMessage());
	} catch (WorkspaceNotFoundException we) {
	    throw new MdlForumException("Workspace not found." + we.getMessage());
	} catch (LoginException e) {
	    throw new MdlForumException("Login failed." + e.getMessage());
	}
    }

    /* ===============Methods implemented from ToolContentImport102Manager =============== */

    /**
     * Import the data for a 1.0.2 MdlForum
     */
    public void import102ToolContent(Long toolContentId, UserDTO user, Hashtable importValues) {
	Date now = new Date();
	MdlForum mdlForum = new MdlForum();
	mdlForum.setCreateDate(now);
	mdlForum.setDefineLater(Boolean.FALSE);
	mdlForum.setRunOffline(Boolean.FALSE);
	mdlForum.setToolContentId(toolContentId);
	mdlForum.setUpdateDate(now);

	mdlForumDAO.saveOrUpdate(mdlForum);
    }

    /**
     * Set the description, throws away the title value as this is not supported
     * in 2.0
     */
    public void setReflectiveData(Long toolContentId, String title, String description) throws ToolException,
	    DataMissingException {

	logger
		.warn("Setting the reflective field on a mdlForum. This doesn't make sense as the mdlForum is for reflection and we don't reflect on reflection!");
	MdlForum mdlForum = getMdlForumByContentId(toolContentId);
	if (mdlForum == null) {
	    throw new DataMissingException("Unable to set reflective data titled " + title
		    + " on activity toolContentId " + toolContentId + " as the tool content does not exist.");
	}

	//mdlForum.setInstructions(description);
    }

    //=========================================================================================
    /* ********** Used by Spring to "inject" the linked objects ************* */

    public IMdlForumDAO getMdlForumDAO() {
	return mdlForumDAO;
    }

    public void setMdlForumDAO(IMdlForumDAO mdlForumDAO) {
	this.mdlForumDAO = mdlForumDAO;
    }

    public IToolContentHandler getMdlForumToolContentHandler() {
	return mdlForumToolContentHandler;
    }

    public void setMdlForumToolContentHandler(IToolContentHandler mdlForumToolContentHandler) {
	this.mdlForumToolContentHandler = mdlForumToolContentHandler;
    }

    public IMdlForumSessionDAO getMdlForumSessionDAO() {
	return mdlForumSessionDAO;
    }

    public void setMdlForumSessionDAO(IMdlForumSessionDAO sessionDAO) {
	this.mdlForumSessionDAO = sessionDAO;
    }

    public ILamsToolService getToolService() {
	return toolService;
    }

    public void setToolService(ILamsToolService toolService) {
	this.toolService = toolService;
    }

    public IMdlForumUserDAO getMdlForumUserDAO() {
	return mdlForumUserDAO;
    }

    public void setMdlForumUserDAO(IMdlForumUserDAO userDAO) {
	this.mdlForumUserDAO = userDAO;
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

    public MdlForumOutputFactory getMdlForumOutputFactory() {
	if (mdlForumOutputFactory == null) {
	    mdlForumOutputFactory = new MdlForumOutputFactory();
	}
	return mdlForumOutputFactory;
    }

    public void setMdlForumOutputFactory(MdlForumOutputFactory mdlForumOutputFactory) {
	this.mdlForumOutputFactory = mdlForumOutputFactory;
    }

    /**
     * TODO: Use spring injection instead of hacking a context
     * 
     * @return
     */
    public IIntegrationService integrationService() {
	return integrationService;
    }

    public void setIntegrationService(IIntegrationService integrationService) {
	this.integrationService = integrationService;
    }
}
