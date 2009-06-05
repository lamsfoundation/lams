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

package org.lamsfoundation.lams.tool.mdquiz.service;

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
import org.lamsfoundation.lams.tool.mdquiz.dao.IMdlQuizDAO;
import org.lamsfoundation.lams.tool.mdquiz.dao.IMdlQuizSessionDAO;
import org.lamsfoundation.lams.tool.mdquiz.dao.IMdlQuizUserDAO;
import org.lamsfoundation.lams.tool.mdquiz.model.MdlQuiz;
import org.lamsfoundation.lams.tool.mdquiz.model.MdlQuizSession;
import org.lamsfoundation.lams.tool.mdquiz.model.MdlQuizUser;
import org.lamsfoundation.lams.tool.mdquiz.util.MdlQuizConstants;
import org.lamsfoundation.lams.tool.mdquiz.util.MdlQuizException;
import org.lamsfoundation.lams.tool.mdquiz.util.MdlQuizToolContentHandler;
import org.lamsfoundation.lams.tool.mdquiz.util.WebUtility;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.HashUtil;
import org.lamsfoundation.lams.util.audit.IAuditService;

/**
 * An implementation of the IMdlQuizService interface.
 * 
 * As a requirement, all LAMS tool's service bean must implement
 * ToolContentManager and ToolSessionManager.
 */

public class MdlQuizService implements ToolSessionManager, ToolAdapterContentManager, IMdlQuizService,
	ToolContentImport102Manager {

    static Logger logger = Logger.getLogger(MdlQuizService.class.getName());

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

    private IMdlQuizDAO mdlQuizDAO = null;

    private IMdlQuizSessionDAO mdlQuizSessionDAO = null;

    private IMdlQuizUserDAO mdlQuizUserDAO = null;

    private ILearnerService learnerService;

    private ILamsToolService toolService;

    private IToolContentHandler mdlQuizToolContentHandler = null;

    private IRepositoryService repositoryService = null;

    private IAuditService auditService = null;

    private IExportToolContentService exportContentService;

    private ICoreNotebookService coreNotebookService;

    private IIntegrationService integrationService;

    private MdlQuizOutputFactory mdlQuizOutputFactory;

    public MdlQuizService() {
	super();
	// TODO Auto-generated constructor stub
    }

    /* ************ Methods from ToolSessionManager ************* */
    public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException {
	if (logger.isDebugEnabled()) {
	    logger.debug("entering method createToolSession:" + " toolSessionId = " + toolSessionId
		    + " toolSessionName = " + toolSessionName + " toolContentId = " + toolContentId);
	}

	MdlQuizSession session = new MdlQuizSession();
	session.setSessionId(toolSessionId);
	session.setSessionName(toolSessionName);

	// learner starts
	MdlQuiz mdlQuiz = mdlQuizDAO.getByContentId(toolContentId);
	session.setMdlQuiz(mdlQuiz);

	try {
	    // Get the required params, then call the eternal server
	    HashMap<String, String> params = getRequiredExtServletParams(mdlQuiz);
	    params.put(EXT_SERVER_PARAM_EXT_TOOL_CONTENT_ID, mdlQuiz.getExtToolContentId().toString());
	    session.setExtSessionId(copyExternalToolContent(params));
	} catch (Exception e) {
	    logger.error("Failed to call external server to copy tool content" + e);
	    throw new ToolException("Failed to call external server to copy tool content" + e);
	}

	mdlQuizSessionDAO.saveOrUpdate(session);
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
	    throw new UserInfoFetchException("Fail to clone quiz in .LRN:" + " - No data returned from external server");
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
	String hash = hash(serverMap, paramsCSV.get(CUSTOM_CSV_MAP_PARAM_USER), timestamp);
	params.put(EXT_SERVER_PARAM_HASH, hash);

	return params;
    }

    public HashMap<String, String> getRequiredExtServletParams(MdlQuiz mdlquiz) {
	HashMap<String, String> params = new HashMap<String, String>();
	params.put(EXT_SERVER_PARAM_USER, mdlquiz.getExtUsername());
	params.put(EXT_SERVER_PARAM_COURSE, mdlquiz.getExtCourseId());
	params.put(EXT_SERVER_PARAM_SECTION, mdlquiz.getExtSection());
	params.put(CUSTOM_CSV_MAP_PARAM_EXT_LMS_ID, mdlquiz.getExtLmsId());

	String timestamp = Long.toString(new Date().getTime());
	params.put(EXT_SERVER_PARAM_TIMESTAMP, timestamp);

	ExtServerOrgMap serverMap = getExtServerOrgMap(mdlquiz.getExtLmsId());
	String hash = hash(serverMap, mdlquiz.getExtUsername(), timestamp);
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

    public int getExternalToolOutputInt(String outputName, MdlQuiz mdlQuiz, Long userId, String extToolContentId,
	    Long toolSessionId) {
	MdlQuizUser user = this.getUserByUserIdAndSessionId(userId, toolSessionId);
	ExtServerOrgMap extServerMap = getExtServerOrgMap(mdlQuiz.getExtLmsId());

	String extUserName = user.getLoginName().substring(extServerMap.getPrefix().length() + 1);

	try {
	    String outputServletUrl = getExtToolAdapterServletUrl(mdlQuiz.getExtLmsId());

	    // setting the mdlQuiz username so the params are set up correctly
	    mdlQuiz.setExtUsername(extUserName);
	    HashMap<String, String> params = getRequiredExtServletParams(mdlQuiz);
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
	mdlQuizOutputFactory = getMdlQuizOutputFactory();
	MdlQuizSession session = this.getSessionBySessionId(toolSessionId);
	if (session == null) {
	    return null;
	}
	return mdlQuizOutputFactory.getToolOutput(names, this, toolSessionId, learnerId, session.getMdlQuiz(), session
		.getExtSessionId());
    }

    /**
     * Get the tool output for the given tool output name.
     * 
     * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.lang.String,
     *      java.lang.Long, java.lang.Long)
     */
    public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
	mdlQuizOutputFactory = getMdlQuizOutputFactory();
	MdlQuizSession session = this.getSessionBySessionId(toolSessionId);
	if (session == null) {
	    return null;
	}

	return mdlQuizOutputFactory.getToolOutput(name, this, toolSessionId, learnerId, session.getMdlQuiz(), session
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
	mdlQuizOutputFactory = getMdlQuizOutputFactory();
	MdlQuiz mdquiz = getMdlQuizByContentId(toolContentId);
	if (mdquiz == null) {
	    mdquiz = getDefaultContent();
	}
	return mdlQuizOutputFactory.getToolOutputDefinitions(mdquiz);
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
	    ret += MdlQuizConstants.RELATIVE_SERVLET_URL;
	}
	return ret;
    }

    public ExtServerOrgMap getExtServerOrgMap(String extLmsId) {
	return integrationService.getExtServerOrgMap(extLmsId);
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
	mdlQuizSessionDAO.deleteBySessionID(toolSessionId);
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

	MdlQuiz fromContent = null;
	if (fromContentId != null) {
	    fromContent = mdlQuizDAO.getByContentId(fromContentId);
	}
	if (fromContent == null) {
	    // create the fromContent using the default tool content
	    fromContent = getDefaultContent();
	}
	MdlQuiz toContent = MdlQuiz.newInstance(fromContent, toContentId, mdlQuizToolContentHandler);
	mdlQuizDAO.saveOrUpdate(toContent);
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
	    throw new ToolException("mdlQuiz tool cusomCSV not in required (user,course,courseURL) form: "
		    + EXPECTED_CSV_FORM);
	}

	MdlQuiz fromContent = null;
	if (fromContentId != null) {
	    fromContent = mdlQuizDAO.getByContentId(fromContentId);
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
	MdlQuiz toContent = MdlQuiz.newInstance(fromContent, toContentId, mdlQuizToolContentHandler);
	toContent.setByCustomCSVHashMap(mapCSV);

	// calling the external tool to copy it's content.
	try {
	    params.put(EXT_SERVER_PARAM_EXT_TOOL_CONTENT_ID, fromContent.getExtToolContentId().toString());
	    toContent.setExtToolContentId(copyExternalToolContent(params));
	} catch (Exception e) {
	    throw new ToolException("Failed to call external server to copy tool content" + e);
	}

	mdlQuizDAO.saveOrUpdate(toContent);
    }

    public void setAsDefineLater(Long toolContentId, boolean value) throws DataMissingException, ToolException {
	MdlQuiz mdlQuiz = mdlQuizDAO.getByContentId(toolContentId);
	if (mdlQuiz == null) {
	    throw new ToolException("Could not find tool with toolContentID: " + toolContentId);
	}
	mdlQuiz.setDefineLater(value);
	mdlQuizDAO.saveOrUpdate(mdlQuiz);
    }

    public void setAsRunOffline(Long toolContentId, boolean value) throws DataMissingException, ToolException {
	MdlQuiz mdlQuiz = mdlQuizDAO.getByContentId(toolContentId);
	if (mdlQuiz == null) {
	    throw new ToolException("Could not find tool with toolContentID: " + toolContentId);
	}
	mdlQuiz.setRunOffline(value);
	mdlQuizDAO.saveOrUpdate(mdlQuiz);
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

	MdlQuiz mdlQuiz = mdlQuizDAO.getByContentId(toolContentId);
	if (mdlQuiz == null) {
	    mdlQuiz = getDefaultContent();
	}
	if (mdlQuiz == null)
	    throw new DataMissingException("Unable to find default content for the mdlQuiz tool");

	// If no external content was found, export empty mdlQuiz
	// Otherwise, call the external servlet to get the export file
	if (mdlQuiz.getExtToolContentId() == null) {
	    mdlQuiz.setExtToolContentId(null);
	    mdlQuiz.setToolContentHandler(null);
	    mdlQuiz.setMdlQuizSessions(null);

	    try {
		exportContentService.exportToolContent(toolContentId, mdlQuiz, mdlQuizToolContentHandler, rootPath);
	    } catch (ExportToolContentException e) {
		throw new ToolException(e);
	    }
	} else {

	    URLConnection conn = null;
	    try {
		// Create the directory to store the export file
		String toolPath = FileUtil.getFullPath(rootPath, toolContentId.toString());
		FileUtil.createDirectory(toolPath);

		String exportServletUrl = getExtToolAdapterServletUrl(mdlQuiz.getExtLmsId());

		if (exportServletUrl != null) {
		    // setting these to arbitrary values since they are only used to construct the hash
		    mdlQuiz.setExtCourseId("extCourse");
		    mdlQuiz.setExtSection("0");
		    mdlQuiz.setExtUsername("authUser");
		    HashMap<String, String> params = this.getRequiredExtServletParams(mdlQuiz);
		    params.put(EXT_SERVER_PARAM_METHOD, EXT_SERVER_METHOD_EXPORT);
		    params.put(EXT_SERVER_PARAM_EXT_TOOL_CONTENT_ID, mdlQuiz.getExtToolContentId().toString());

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
		    logger.debug("Path to mdlQuiz export content: " + toolPath + "/ext_tool.txt");

		    out.flush();
		    out.close();
		    in.close();
		} else {
		    exportContentService.exportToolContent(toolContentId, mdlQuiz, mdlQuizToolContentHandler, rootPath);
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
	// otherwise, simply import an empty mdlQuiz
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

		// Save the resulting mdl quiz 
		MdlQuiz mdlQuiz = new MdlQuiz();
		mdlQuiz.setToolContentId(toolContentId);
		mdlQuiz.setCreateDate(new Date());
		mdlQuiz.setExtToolContentId(extContentId);
		saveOrUpdateMdlQuiz(mdlQuiz);

	    } catch (Exception e) {
		logger.error("Problem passing mdlQuiz export file to external server", e);
		throw new ToolException(e);
	    }
	} else // use the normal LAMS method of importing activities from an xml file
	{

	    try {
		Object toolPOJO = exportContentService.importToolContent(toolContentPath, mdlQuizToolContentHandler,
			fromVersion, toVersion);
		if (!(toolPOJO instanceof MdlQuiz))
		    throw new ImportToolContentException("Import MdlQuiz tool content failed. Deserialized object is "
			    + toolPOJO);
		MdlQuiz mdlQuiz = (MdlQuiz) toolPOJO;
		// reset it to new toolContentId
		mdlQuiz.setToolContentId(toolContentId);
		mdlQuizDAO.saveOrUpdate(mdlQuiz);
	    } catch (Exception e) {
		throw new ToolException(e);
	    }
	}
    }

    /* ********** IMdlQuizService Methods ********************************* */

    public Long getDefaultContentIdBySignature(String toolSignature) {
	Long toolContentId = null;
	toolContentId = new Long(toolService.getToolDefaultContentIdBySignature(toolSignature));
	if (toolContentId == null) {
	    String error = "Could not retrieve default content id for this tool";
	    logger.error(error);
	    throw new MdlQuizException(error);
	}
	return toolContentId;
    }

    public MdlQuiz getDefaultContent() {
	MdlQuiz defaultContent = new MdlQuiz();
	return defaultContent;
    }

    public MdlQuiz getDefaultContent(String extLmsId) {
	MdlQuiz defaultContent = new MdlQuiz();
	defaultContent.setExtLmsId(extLmsId);
	return defaultContent;
    }

    public MdlQuiz copyDefaultContent(Long newContentID) {

	if (newContentID == null) {
	    String error = "Cannot copy the MdlQuiz tools default content: + " + "newContentID is null";
	    logger.error(error);
	    throw new MdlQuizException(error);
	}

	MdlQuiz defaultContent = getDefaultContent();
	// create new mdlQuiz using the newContentID
	MdlQuiz newContent = new MdlQuiz();
	newContent = MdlQuiz.newInstance(defaultContent, newContentID, mdlQuizToolContentHandler);
	mdlQuizDAO.saveOrUpdate(newContent);
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
	return integrationService.getMappedServers(MdlQuizConstants.TOOL_SIGNATURE);
    }

    public void saveServerMappings(String[] mappedServers) {
	Tool tool = toolService.getPersistToolBySignature(MdlQuizConstants.TOOL_SIGNATURE);

	Set<ExtServerToolAdapterMap> mappedAdapterServers = new HashSet<ExtServerToolAdapterMap>();

	List<ExtServerToolAdapterMap> alreadyMapped = getMappedServers();

	if (tool != null) {
	    if (mappedServers != null) {

		for (int i = 0; i < mappedServers.length; i++) {

		    ExtServerOrgMap serverMap = integrationService.getExtServerOrgMap(mappedServers[i]);

		    if (serverMap != null) {

			ExtServerToolAdapterMap serverToolMap = integrationService.getMappedServer(serverMap
				.getServerid(), MdlQuizConstants.TOOL_SIGNATURE);

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

    }

    public MdlQuiz getMdlQuizByContentId(Long toolContentID) {
	MdlQuiz mdlQuiz = (MdlQuiz) mdlQuizDAO.getByContentId(toolContentID);
	if (mdlQuiz == null) {
	    logger.debug("Could not find the content with toolContentID:" + toolContentID);
	}
	return mdlQuiz;
    }

    public MdlQuizSession getSessionBySessionId(Long toolSessionId) {
	MdlQuizSession mdlQuizSession = mdlQuizSessionDAO.getBySessionId(toolSessionId);
	if (mdlQuizSession == null) {
	    logger.debug("Could not find the mdlQuiz session with toolSessionID:" + toolSessionId);
	}
	return mdlQuizSession;
    }

    public MdlQuizUser getUserByUserIdAndSessionId(Long userId, Long toolSessionId) {
	return mdlQuizUserDAO.getByUserIdAndSessionId(userId, toolSessionId);
    }

    public MdlQuizUser getUserByLoginNameAndSessionId(String loginName, Long toolSessionId) {
	return mdlQuizUserDAO.getByLoginNameAndSessionId(loginName, toolSessionId);
    }

    public MdlQuizUser getUserByUID(Long uid) {
	return mdlQuizUserDAO.getByUID(uid);
    }

    public void deleteFromRepository(Long uuid, Long versionID) throws MdlQuizException {
	ITicket ticket = getRepositoryLoginTicket();
	try {
	    repositoryService.deleteVersion(ticket, uuid, versionID);
	} catch (Exception e) {
	    throw new MdlQuizException("Exception occured while deleting files from" + " the repository "
		    + e.getMessage());
	}
    }

    public void saveOrUpdateMdlQuiz(MdlQuiz mdlQuiz) {
	mdlQuizDAO.saveOrUpdate(mdlQuiz);
    }

    public void saveOrUpdateMdlQuizSession(MdlQuizSession mdlQuizSession) {
	mdlQuizSessionDAO.saveOrUpdate(mdlQuizSession);
    }

    public void saveOrUpdateMdlQuizUser(MdlQuizUser mdlQuizUser) {
	mdlQuizUserDAO.saveOrUpdate(mdlQuizUser);
    }

    public MdlQuizUser createMdlQuizUser(UserDTO user, MdlQuizSession mdlQuizSession) {
	MdlQuizUser mdlQuizUser = new MdlQuizUser(user, mdlQuizSession);
	saveOrUpdateMdlQuizUser(mdlQuizUser);
	return mdlQuizUser;
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
    private ITicket getRepositoryLoginTicket() throws MdlQuizException {
	repositoryService = RepositoryProxy.getRepositoryService();
	ICredentials credentials = new SimpleCredentials(MdlQuizToolContentHandler.repositoryUser,
		MdlQuizToolContentHandler.repositoryId);
	try {
	    ITicket ticket = repositoryService.login(credentials, MdlQuizToolContentHandler.repositoryWorkspaceName);
	    return ticket;
	} catch (AccessDeniedException ae) {
	    throw new MdlQuizException("Access Denied to repository." + ae.getMessage());
	} catch (WorkspaceNotFoundException we) {
	    throw new MdlQuizException("Workspace not found." + we.getMessage());
	} catch (LoginException e) {
	    throw new MdlQuizException("Login failed." + e.getMessage());
	}
    }

    /* ===============Methods implemented from ToolContentImport102Manager =============== */

    /**
     * Import the data for a 1.0.2 MdlQuiz
     */
    public void import102ToolContent(Long toolContentId, UserDTO user, Hashtable importValues) {
	Date now = new Date();
	MdlQuiz mdlQuiz = new MdlQuiz();
	mdlQuiz.setCreateDate(now);
	mdlQuiz.setDefineLater(Boolean.FALSE);
	mdlQuiz.setRunOffline(Boolean.FALSE);
	mdlQuiz.setToolContentId(toolContentId);
	mdlQuiz.setUpdateDate(now);

	mdlQuizDAO.saveOrUpdate(mdlQuiz);
    }

    /**
     * Set the description, throws away the title value as this is not supported
     * in 2.0
     */
    public void setReflectiveData(Long toolContentId, String title, String description) throws ToolException,
	    DataMissingException {

	logger
		.warn("Setting the reflective field on a mdlQuiz. This doesn't make sense as the mdlQuiz is for reflection and we don't reflect on reflection!");
	MdlQuiz mdlQuiz = getMdlQuizByContentId(toolContentId);
	if (mdlQuiz == null) {
	    throw new DataMissingException("Unable to set reflective data titled " + title
		    + " on activity toolContentId " + toolContentId + " as the tool content does not exist.");
	}

	//mdlQuiz.setInstructions(description);
    }

    //=========================================================================================
    /* ********** Used by Spring to "inject" the linked objects ************* */

    public IMdlQuizDAO getMdlQuizDAO() {
	return mdlQuizDAO;
    }

    public void setMdlQuizDAO(IMdlQuizDAO mdlQuizDAO) {
	this.mdlQuizDAO = mdlQuizDAO;
    }

    public IToolContentHandler getMdlQuizToolContentHandler() {
	return mdlQuizToolContentHandler;
    }

    public void setMdlQuizToolContentHandler(IToolContentHandler mdlQuizToolContentHandler) {
	this.mdlQuizToolContentHandler = mdlQuizToolContentHandler;
    }

    public IMdlQuizSessionDAO getMdlQuizSessionDAO() {
	return mdlQuizSessionDAO;
    }

    public void setMdlQuizSessionDAO(IMdlQuizSessionDAO sessionDAO) {
	this.mdlQuizSessionDAO = sessionDAO;
    }

    public ILamsToolService getToolService() {
	return toolService;
    }

    public void setToolService(ILamsToolService toolService) {
	this.toolService = toolService;
    }

    public IMdlQuizUserDAO getMdlQuizUserDAO() {
	return mdlQuizUserDAO;
    }

    public void setMdlQuizUserDAO(IMdlQuizUserDAO userDAO) {
	this.mdlQuizUserDAO = userDAO;
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

    public MdlQuizOutputFactory getMdlQuizOutputFactory() {
	if (mdlQuizOutputFactory == null) {
	    mdlQuizOutputFactory = new MdlQuizOutputFactory();
	}
	return mdlQuizOutputFactory;
    }

    public void setMdlQuizOutputFactory(MdlQuizOutputFactory mdlQuizOutputFactory) {
	this.mdlQuizOutputFactory = mdlQuizOutputFactory;
    }
    
    public IIntegrationService getIntegrationService() {
	return integrationService;
    }

    public void setIntegrationService(IIntegrationService integrationService) {
	this.integrationService = integrationService;
    }
}
