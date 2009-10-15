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

package org.lamsfoundation.lams.tool.mdlesn.service;

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
import org.lamsfoundation.lams.tool.mdlesn.dao.IMdlLessonDAO;
import org.lamsfoundation.lams.tool.mdlesn.dao.IMdlLessonSessionDAO;
import org.lamsfoundation.lams.tool.mdlesn.dao.IMdlLessonUserDAO;
import org.lamsfoundation.lams.tool.mdlesn.model.MdlLesson;
import org.lamsfoundation.lams.tool.mdlesn.model.MdlLessonSession;
import org.lamsfoundation.lams.tool.mdlesn.model.MdlLessonUser;
import org.lamsfoundation.lams.tool.mdlesn.util.MdlLessonConstants;
import org.lamsfoundation.lams.tool.mdlesn.util.MdlLessonException;
import org.lamsfoundation.lams.tool.mdlesn.util.MdlLessonToolContentHandler;
import org.lamsfoundation.lams.tool.mdlesn.util.WebUtility;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.HashUtil;
import org.lamsfoundation.lams.util.audit.IAuditService;

/**
 * An implementation of the IMdlLessonService interface.
 * 
 * As a requirement, all LAMS tool's service bean must implement
 * ToolContentManager and ToolSessionManager.
 */

public class MdlLessonService implements ToolSessionManager, ToolAdapterContentManager, IMdlLessonService,
	ToolContentImport102Manager {

    static Logger logger = Logger.getLogger(MdlLessonService.class.getName());

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

    private IMdlLessonDAO mdlLessonDAO = null;

    private IMdlLessonSessionDAO mdlLessonSessionDAO = null;

    private IMdlLessonUserDAO mdlLessonUserDAO = null;

    private ILearnerService learnerService;

    private ILamsToolService toolService;

    private IToolContentHandler mdlLessonToolContentHandler = null;

    private IRepositoryService repositoryService = null;

    private IAuditService auditService = null;

    private IExportToolContentService exportContentService;

    private ICoreNotebookService coreNotebookService;

    private IIntegrationService integrationService;

    private MdlLessonOutputFactory mdlLessonOutputFactory;

    public MdlLessonService() {
	super();
	// TODO Auto-generated constructor stub
    }

    /* ************ Methods from ToolSessionManager ************* */
    public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException {
	if (logger.isDebugEnabled()) {
	    logger.debug("entering method createToolSession:" + " toolSessionId = " + toolSessionId
		    + " toolSessionName = " + toolSessionName + " toolContentId = " + toolContentId);
	}

	MdlLessonSession session = new MdlLessonSession();
	session.setSessionId(toolSessionId);
	session.setSessionName(toolSessionName);

	// learner starts
	MdlLesson mdlLesson = mdlLessonDAO.getByContentId(toolContentId);
	session.setMdlLesson(mdlLesson);

	try {
	    // Get the required params, then call the eternal server
	    HashMap<String, String> params = getRequiredExtServletParams(mdlLesson);
	    params.put(EXT_SERVER_PARAM_EXT_TOOL_CONTENT_ID, mdlLesson.getExtToolContentId().toString());
	    session.setExtSessionId(copyExternalToolContent(params));
	} catch (Exception e) {
	    logger.error("Failed to call external server to copy tool content" + e);
	    throw new ToolException("Failed to call external server to copy tool content" + e);
	}

	mdlLessonSessionDAO.saveOrUpdate(session);
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
	    throw new UserInfoFetchException("Fail to clone lesson in .LRN:"
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
	String hash = hash(serverMap, paramsCSV.get(CUSTOM_CSV_MAP_PARAM_USER), timestamp);
	params.put(EXT_SERVER_PARAM_HASH, hash);

	return params;
    }

    public HashMap<String, String> getRequiredExtServletParams(MdlLesson mdllesson) {
	HashMap<String, String> params = new HashMap<String, String>();
	params.put(EXT_SERVER_PARAM_USER, mdllesson.getExtUsername());
	params.put(EXT_SERVER_PARAM_COURSE, mdllesson.getExtCourseId());
	params.put(EXT_SERVER_PARAM_SECTION, mdllesson.getExtSection());
	params.put(CUSTOM_CSV_MAP_PARAM_EXT_LMS_ID, mdllesson.getExtLmsId());

	String timestamp = Long.toString(new Date().getTime());
	params.put(EXT_SERVER_PARAM_TIMESTAMP, timestamp);

	ExtServerOrgMap serverMap = getExtServerOrgMap(mdllesson.getExtLmsId());
	String hash = hash(serverMap, mdllesson.getExtUsername(), timestamp);
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

    public int getExternalToolOutputInt(String outputName, MdlLesson mdlLesson, Long userId, String extToolContentId,
	    Long toolSessionId) {
	MdlLessonUser user = this.getUserByUserIdAndSessionId(userId, toolSessionId);
	ExtServerOrgMap extServerMap = getExtServerOrgMap(mdlLesson.getExtLmsId());

	String extUserName = user.getLoginName().substring(extServerMap.getPrefix().length() + 1);

	try {
	    String outputServletUrl = getExtToolAdapterServletUrl(mdlLesson.getExtLmsId());
	    ;

	    // setting the mdlLesson username so the params are set up correctly
	    mdlLesson.setExtUsername(extUserName);
	    HashMap<String, String> params = getRequiredExtServletParams(mdlLesson);
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
	mdlLessonOutputFactory = getMdlLessonOutputFactory();
	MdlLessonSession session = this.getSessionBySessionId(toolSessionId);
	if (session == null) {
	    return null;
	}
	return mdlLessonOutputFactory.getToolOutput(names, this, toolSessionId, learnerId, session.getMdlLesson(),
		session.getExtSessionId());
    }

    /**
     * Get the tool output for the given tool output name.
     * 
     * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.lang.String,
     *      java.lang.Long, java.lang.Long)
     */
    public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
	mdlLessonOutputFactory = getMdlLessonOutputFactory();
	MdlLessonSession session = this.getSessionBySessionId(toolSessionId);
	if (session == null) {
	    return null;
	}

	return mdlLessonOutputFactory.getToolOutput(name, this, toolSessionId, learnerId, session.getMdlLesson(),
		session.getExtSessionId());
    }

    /**
     * Get the definitions for possible output for an activity, based on the
     * toolContentId. These may be definitions that are always available for the
     * tool (e.g. number of marks for Multiple Lesson) or a custom definition
     * created for a particular activity such as the answer to the third
     * question contains the word Koala and hence the need for the toolContentId
     * 
     * @return SortedMap of ToolOutputDefinitions with the key being the name of
     *         each definition
     */
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Long toolContentId, int definitionType) throws ToolException {
	mdlLessonOutputFactory = getMdlLessonOutputFactory();
	MdlLesson mdlesn = getMdlLessonByContentId(toolContentId);
	if (mdlesn == null) {
	    mdlesn = getDefaultContent();
	}
	return mdlLessonOutputFactory.getToolOutputDefinitions(mdlesn, definitionType);
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
	    ret += MdlLessonConstants.RELATIVE_SERVLET_URL;
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
	mdlLessonSessionDAO.deleteBySessionID(toolSessionId);
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

	MdlLesson fromContent = null;
	if (fromContentId != null) {
	    fromContent = mdlLessonDAO.getByContentId(fromContentId);
	}
	if (fromContent == null) {
	    // create the fromContent using the default tool content
	    fromContent = getDefaultContent();
	}
	MdlLesson toContent = MdlLesson.newInstance(fromContent, toContentId, mdlLessonToolContentHandler);
	mdlLessonDAO.saveOrUpdate(toContent);
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
	    throw new ToolException("mdlLesson tool cusomCSV not in required (user,course,courseURL) form: "
		    + EXPECTED_CSV_FORM);
	}

	MdlLesson fromContent = null;
	if (fromContentId != null) {
	    fromContent = mdlLessonDAO.getByContentId(fromContentId);
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
	MdlLesson toContent = MdlLesson.newInstance(fromContent, toContentId, mdlLessonToolContentHandler);
	toContent.setByCustomCSVHashMap(mapCSV);

	// calling the external tool to copy it's content.
	try {
	    params.put(EXT_SERVER_PARAM_EXT_TOOL_CONTENT_ID, fromContent.getExtToolContentId().toString());
	    toContent.setExtToolContentId(copyExternalToolContent(params));
	} catch (Exception e) {
	    throw new ToolException("Failed to call external server to copy tool content" + e);
	}

	mdlLessonDAO.saveOrUpdate(toContent);
    }

    public void setAsDefineLater(Long toolContentId, boolean value) throws DataMissingException, ToolException {
	MdlLesson mdlLesson = mdlLessonDAO.getByContentId(toolContentId);
	if (mdlLesson == null) {
	    throw new ToolException("Could not find tool with toolContentID: " + toolContentId);
	}
	mdlLesson.setDefineLater(value);
	mdlLessonDAO.saveOrUpdate(mdlLesson);
    }

    public void setAsRunOffline(Long toolContentId, boolean value) throws DataMissingException, ToolException {
	MdlLesson mdlLesson = mdlLessonDAO.getByContentId(toolContentId);
	if (mdlLesson == null) {
	    throw new ToolException("Could not find tool with toolContentID: " + toolContentId);
	}
	mdlLesson.setRunOffline(value);
	mdlLessonDAO.saveOrUpdate(mdlLesson);
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

	MdlLesson mdlLesson = mdlLessonDAO.getByContentId(toolContentId);
	if (mdlLesson == null) {
	    mdlLesson = getDefaultContent();
	}
	if (mdlLesson == null)
	    throw new DataMissingException("Unable to find default content for the mdlLesson tool");

	// If no external content was found, export empty mdlLesson
	// Otherwise, call the external servlet to get the export file
	if (mdlLesson.getExtToolContentId() == null) {
	    mdlLesson.setExtToolContentId(null);
	    mdlLesson.setToolContentHandler(null);
	    mdlLesson.setMdlLessonSessions(null);

	    try {
		exportContentService.exportToolContent(toolContentId, mdlLesson, mdlLessonToolContentHandler, rootPath);
	    } catch (ExportToolContentException e) {
		throw new ToolException(e);
	    }
	} else {

	    URLConnection conn = null;
	    try {
		// Create the directory to store the export file
		String toolPath = FileUtil.getFullPath(rootPath, toolContentId.toString());
		FileUtil.createDirectory(toolPath);

		String exportServletUrl = getExtToolAdapterServletUrl(mdlLesson.getExtLmsId());

		if (exportServletUrl != null) {
		    // setting these to arbitrary values since they are only used to construct the hash
		    mdlLesson.setExtCourseId("extCourse");
		    mdlLesson.setExtSection("0");
		    mdlLesson.setExtUsername("authUser");
		    HashMap<String, String> params = this.getRequiredExtServletParams(mdlLesson);
		    params.put(EXT_SERVER_PARAM_METHOD, EXT_SERVER_METHOD_EXPORT);
		    params.put(EXT_SERVER_PARAM_EXT_TOOL_CONTENT_ID, mdlLesson.getExtToolContentId().toString());

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
		    logger.debug("Path to mdlLesson export content: " + toolPath + "/ext_tool.txt");

		    out.flush();
		    out.close();
		    in.close();
		} else {
		    exportContentService.exportToolContent(toolContentId, mdlLesson, mdlLessonToolContentHandler,
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
	// otherwise, simply import an empty mdlLesson
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

		// Save the resulting mdl lesson 
		MdlLesson mdlLesson = new MdlLesson();
		mdlLesson.setToolContentId(toolContentId);
		mdlLesson.setCreateDate(new Date());
		mdlLesson.setExtToolContentId(extContentId);
		saveOrUpdateMdlLesson(mdlLesson);

	    } catch (Exception e) {
		logger.error("Problem passing mdlLesson export file to external server", e);
		throw new ToolException(e);
	    }
	} else // use the normal LAMS method of importing activities from an xml file
	{

	    try {
		Object toolPOJO = exportContentService.importToolContent(toolContentPath, mdlLessonToolContentHandler,
			fromVersion, toVersion);
		if (!(toolPOJO instanceof MdlLesson))
		    throw new ImportToolContentException(
			    "Import MdlLesson tool content failed. Deserialized object is " + toolPOJO);
		MdlLesson mdlLesson = (MdlLesson) toolPOJO;
		// reset it to new toolContentId
		mdlLesson.setToolContentId(toolContentId);
		mdlLessonDAO.saveOrUpdate(mdlLesson);
	    } catch (Exception e) {
		throw new ToolException(e);
	    }
	}
    }

    /* ********** IMdlLessonService Methods ********************************* */

    public Long getDefaultContentIdBySignature(String toolSignature) {
	Long toolContentId = null;
	toolContentId = new Long(toolService.getToolDefaultContentIdBySignature(toolSignature));
	if (toolContentId == null) {
	    String error = "Could not retrieve default content id for this tool";
	    logger.error(error);
	    throw new MdlLessonException(error);
	}
	return toolContentId;
    }

    public MdlLesson getDefaultContent() {
	MdlLesson defaultContent = new MdlLesson();
	return defaultContent;
    }

    public MdlLesson getDefaultContent(String extLmsId) {
	MdlLesson defaultContent = new MdlLesson();
	defaultContent.setExtLmsId(extLmsId);
	return defaultContent;
    }

    public MdlLesson copyDefaultContent(Long newContentID) {

	if (newContentID == null) {
	    String error = "Cannot copy the MdlLesson tools default content: + " + "newContentID is null";
	    logger.error(error);
	    throw new MdlLessonException(error);
	}

	MdlLesson defaultContent = getDefaultContent();
	// create new mdlLesson using the newContentID
	MdlLesson newContent = new MdlLesson();
	newContent = MdlLesson.newInstance(defaultContent, newContentID, mdlLessonToolContentHandler);
	mdlLessonDAO.saveOrUpdate(newContent);
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
	return integrationService.getMappedServers(MdlLessonConstants.TOOL_SIGNATURE);
    }

    public void saveServerMappings(String[] mappedServers) {
	Tool tool = toolService.getPersistToolBySignature(MdlLessonConstants.TOOL_SIGNATURE);

	Set<ExtServerToolAdapterMap> mappedAdapterServers = new HashSet<ExtServerToolAdapterMap>();

	List<ExtServerToolAdapterMap> alreadyMapped = getMappedServers();

	if (tool != null) {
	    if (mappedServers != null) {

		for (int i = 0; i < mappedServers.length; i++) {

		    ExtServerOrgMap serverMap = integrationService.getExtServerOrgMap(mappedServers[i]);

		    if (serverMap != null) {

			ExtServerToolAdapterMap serverToolMap = integrationService.getMappedServer(serverMap
				.getServerid(), MdlLessonConstants.TOOL_SIGNATURE);

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

    public MdlLesson getMdlLessonByContentId(Long toolContentID) {
	MdlLesson mdlLesson = (MdlLesson) mdlLessonDAO.getByContentId(toolContentID);
	if (mdlLesson == null) {
	    logger.debug("Could not find the content with toolContentID:" + toolContentID);
	}
	return mdlLesson;
    }

    public MdlLessonSession getSessionBySessionId(Long toolSessionId) {
	MdlLessonSession mdlLessonSession = mdlLessonSessionDAO.getBySessionId(toolSessionId);
	if (mdlLessonSession == null) {
	    logger.debug("Could not find the mdlLesson session with toolSessionID:" + toolSessionId);
	}
	return mdlLessonSession;
    }

    public MdlLessonUser getUserByUserIdAndSessionId(Long userId, Long toolSessionId) {
	return mdlLessonUserDAO.getByUserIdAndSessionId(userId, toolSessionId);
    }

    public MdlLessonUser getUserByLoginNameAndSessionId(String loginName, Long toolSessionId) {
	return mdlLessonUserDAO.getByLoginNameAndSessionId(loginName, toolSessionId);
    }

    public MdlLessonUser getUserByUID(Long uid) {
	return mdlLessonUserDAO.getByUID(uid);
    }

    public void deleteFromRepository(Long uuid, Long versionID) throws MdlLessonException {
	ITicket ticket = getRepositoryLoginTicket();
	try {
	    repositoryService.deleteVersion(ticket, uuid, versionID);
	} catch (Exception e) {
	    throw new MdlLessonException("Exception occured while deleting files from" + " the repository "
		    + e.getMessage());
	}
    }

    public void saveOrUpdateMdlLesson(MdlLesson mdlLesson) {
	mdlLessonDAO.saveOrUpdate(mdlLesson);
    }

    public void saveOrUpdateMdlLessonSession(MdlLessonSession mdlLessonSession) {
	mdlLessonSessionDAO.saveOrUpdate(mdlLessonSession);
    }

    public void saveOrUpdateMdlLessonUser(MdlLessonUser mdlLessonUser) {
	mdlLessonUserDAO.saveOrUpdate(mdlLessonUser);
    }

    public MdlLessonUser createMdlLessonUser(UserDTO user, MdlLessonSession mdlLessonSession) {
	MdlLessonUser mdlLessonUser = new MdlLessonUser(user, mdlLessonSession);
	saveOrUpdateMdlLessonUser(mdlLessonUser);
	return mdlLessonUser;
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
    private ITicket getRepositoryLoginTicket() throws MdlLessonException {
	repositoryService = RepositoryProxy.getRepositoryService();
	ICredentials credentials = new SimpleCredentials(MdlLessonToolContentHandler.repositoryUser,
		MdlLessonToolContentHandler.repositoryId);
	try {
	    ITicket ticket = repositoryService.login(credentials, MdlLessonToolContentHandler.repositoryWorkspaceName);
	    return ticket;
	} catch (AccessDeniedException ae) {
	    throw new MdlLessonException("Access Denied to repository." + ae.getMessage());
	} catch (WorkspaceNotFoundException we) {
	    throw new MdlLessonException("Workspace not found." + we.getMessage());
	} catch (LoginException e) {
	    throw new MdlLessonException("Login failed." + e.getMessage());
	}
    }

    /* ===============Methods implemented from ToolContentImport102Manager =============== */

    /**
     * Import the data for a 1.0.2 MdlLesson
     */
    public void import102ToolContent(Long toolContentId, UserDTO user, Hashtable importValues) {
	Date now = new Date();
	MdlLesson mdlLesson = new MdlLesson();
	mdlLesson.setCreateDate(now);
	mdlLesson.setDefineLater(Boolean.FALSE);
	mdlLesson.setRunOffline(Boolean.FALSE);
	mdlLesson.setToolContentId(toolContentId);
	mdlLesson.setUpdateDate(now);

	mdlLessonDAO.saveOrUpdate(mdlLesson);
    }

    /**
     * Set the description, throws away the title value as this is not supported
     * in 2.0
     */
    public void setReflectiveData(Long toolContentId, String title, String description) throws ToolException,
	    DataMissingException {

	logger
		.warn("Setting the reflective field on a mdlLesson. This doesn't make sense as the mdlLesson is for reflection and we don't reflect on reflection!");
	MdlLesson mdlLesson = getMdlLessonByContentId(toolContentId);
	if (mdlLesson == null) {
	    throw new DataMissingException("Unable to set reflective data titled " + title
		    + " on activity toolContentId " + toolContentId + " as the tool content does not exist.");
	}

	//mdlLesson.setInstructions(description);
    }

    //=========================================================================================
    /* ********** Used by Spring to "inject" the linked objects ************* */

    public IMdlLessonDAO getMdlLessonDAO() {
	return mdlLessonDAO;
    }

    public void setMdlLessonDAO(IMdlLessonDAO mdlLessonDAO) {
	this.mdlLessonDAO = mdlLessonDAO;
    }

    public IToolContentHandler getMdlLessonToolContentHandler() {
	return mdlLessonToolContentHandler;
    }

    public void setMdlLessonToolContentHandler(IToolContentHandler mdlLessonToolContentHandler) {
	this.mdlLessonToolContentHandler = mdlLessonToolContentHandler;
    }

    public IMdlLessonSessionDAO getMdlLessonSessionDAO() {
	return mdlLessonSessionDAO;
    }

    public void setMdlLessonSessionDAO(IMdlLessonSessionDAO sessionDAO) {
	this.mdlLessonSessionDAO = sessionDAO;
    }

    public ILamsToolService getToolService() {
	return toolService;
    }

    public void setToolService(ILamsToolService toolService) {
	this.toolService = toolService;
    }

    public IMdlLessonUserDAO getMdlLessonUserDAO() {
	return mdlLessonUserDAO;
    }

    public void setMdlLessonUserDAO(IMdlLessonUserDAO userDAO) {
	this.mdlLessonUserDAO = userDAO;
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

    public MdlLessonOutputFactory getMdlLessonOutputFactory() {
	if (mdlLessonOutputFactory == null) {
	    mdlLessonOutputFactory = new MdlLessonOutputFactory();
	}
	return mdlLessonOutputFactory;
    }

    public void setMdlLessonOutputFactory(MdlLessonOutputFactory mdlLessonOutputFactory) {
	this.mdlLessonOutputFactory = mdlLessonOutputFactory;
    }

    /**
     * TODO: Use spring injection instead of hacking a context
     * 
     * @return
     */
    public IIntegrationService getIntegrationService() {
	return integrationService;
    }

    public void setIntegrationService(IIntegrationService integrationService) {
	this.integrationService = integrationService;
    }
    
    public Class[] getSupportedToolOutputDefinitionClasses(int definitionType) {
	return null;
    }
}
