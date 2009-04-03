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

package org.lamsfoundation.lams.tool.mindmap.web.actions;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.authoring.web.AuthoringConstants;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.mindmap.model.Mindmap;
import org.lamsfoundation.lams.tool.mindmap.model.MindmapAttachment;
import org.lamsfoundation.lams.tool.mindmap.model.MindmapNode;
import org.lamsfoundation.lams.tool.mindmap.model.MindmapUser;
import org.lamsfoundation.lams.tool.mindmap.service.IMindmapService;
import org.lamsfoundation.lams.tool.mindmap.service.MindmapServiceProxy;
import org.lamsfoundation.lams.tool.mindmap.util.MindmapConstants;
import org.lamsfoundation.lams.tool.mindmap.util.xmlmodel.NodeConceptModel;
import org.lamsfoundation.lams.tool.mindmap.util.xmlmodel.NodeModel;
import org.lamsfoundation.lams.tool.mindmap.web.forms.AuthoringForm;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.FileValidatorUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

import com.thoughtworks.xstream.XStream;

/**
 * @author Ruslan Kazakov
 * @version 1.0.1
 * 
 * @struts.action path="/authoring" name="authoringForm" parameter="dispatch" scope="request" validate="false"
 * @struts.action-forward name="success" path="tiles:/authoring/main"
 * @struts.action-forward name="message_page" path="tiles:/generic/message"
 */
public class AuthoringAction extends LamsDispatchAction {

    private static Logger logger = Logger.getLogger(AuthoringAction.class);

    public IMindmapService mindmapService;

    // Authoring SessionMap key names
    private static final String KEY_TOOL_CONTENT_ID = "toolContentID";
    private static final String KEY_CONTENT_FOLDER_ID = "contentFolderID";
    private static final String KEY_MODE = "mode";
    private static final String KEY_ONLINE_FILES = "onlineFiles";
    private static final String KEY_OFFLINE_FILES = "offlineFiles";
    private static final String KEY_UNSAVED_ONLINE_FILES = "unsavedOnlineFiles";
    private static final String KEY_UNSAVED_OFFLINE_FILES = "unsavedOfflineFiles";
    private static final String KEY_DELETED_FILES = "deletedFiles";

    /**
     * Default method when no dispatch parameter is specified. It is expected that the parameter
     * <code>toolContentID</code> will be passed in. This will be used to retrieve content for this tool.
     */
    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// Extract toolContentID from parameters.
	Long toolContentID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);

	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, "mode", true);

	// set up mindmapService
	if (mindmapService == null) {
	    mindmapService = MindmapServiceProxy.getMindmapService(this.getServlet().getServletContext());
	}

	// retrieving Mindmap with given toolContentID
	Mindmap mindmap = mindmapService.getMindmapByContentId(toolContentID);
	if (mindmap == null) {
	    mindmap = mindmapService.copyDefaultContent(toolContentID);
	    mindmap.setCreateDate(new Date());
	    mindmapService.saveOrUpdateMindmap(mindmap);
	    // TODO NOTE: this causes DB orphans when LD not saved.

	    // creating default nodes for current mindmap
	    String rootNodeName = mindmapService.getMindmapMessageService().getMessage("node.root.defaultName");
	    String childNodeName1 = mindmapService.getMindmapMessageService().getMessage("node.child1.defaultName");
	    String childNodeName2 = mindmapService.getMindmapMessageService().getMessage("node.child2.defaultName");
	    
	    MindmapNode rootMindmapNode = mindmapService.saveMindmapNode(null, null, 1l, rootNodeName, "ffffff", null, mindmap);
	    mindmapService.saveOrUpdateMindmapNode(rootMindmapNode);
	    mindmapService.saveMindmapNode(null, rootMindmapNode, 2l, childNodeName1, "ffffff", null, mindmap);
	    mindmapService.saveMindmapNode(null, rootMindmapNode, 3l, childNodeName2, "ffffff", null, mindmap);
	}

	if (mode != null && mode.isTeacher()) {
	    // Set the defineLater flag so that learners cannot use content
	    // while we
	    // are editing. This flag is released when updateContent is called.
	    mindmap.setDefineLater(true);
	    mindmapService.saveOrUpdateMindmap(mindmap);
	}

	/* MINDMAP Code */

	String mindmapContentPath = Configuration.get(ConfigurationKeys.SERVER_URL)
		+ "tool/lamind10/authoring.do?dispatch=setMindmapContent%26mindmapId=" + mindmap.getUid();
	request.setAttribute("mindmapContentPath", mindmapContentPath);
	
	String localizationPath = Configuration.get(ConfigurationKeys.SERVER_URL) + 
		"tool/lamind10/authoring.do?dispatch=setLocale";
	request.setAttribute("localizationPath", localizationPath);
	
	String currentMindmapUser = mindmapService.getMindmapMessageService().getMessage("node.instructor.label");
	request.setAttribute("currentMindmapUser", currentMindmapUser);

	String mindmapType = "images/mindmap_singleuser.swf";
	request.setAttribute("mindmapType", mindmapType);

	/* MINDMAP Code */
	
	// Set up the authForm.
	AuthoringForm authForm = (AuthoringForm) form;
	updateAuthForm(authForm, mindmap);

	// Set up sessionMap
	SessionMap<String, Object> map = createSessionMap(mindmap, getAccessMode(request), contentFolderID,
		toolContentID);
	authForm.setSessionMapID(map.getSessionID());

	// add the sessionMap to HTTPSession.
	request.getSession().setAttribute(map.getSessionID(), map);
	request.setAttribute(MindmapConstants.ATTR_SESSION_MAP, map);

	return mapping.findForward("success");
    }

    /**
     * Returns the serialized XML of the Mindmap Nodes from Database
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return null
     */
    public ActionForward setMindmapContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Long mindmapId = WebUtil.readLongParam(request, "mindmapId", false);
	
	List mindmapNodeList = mindmapService.getAuthorRootNodeByMindmapId(mindmapId);

	if (mindmapNodeList != null && mindmapNodeList.size() > 0) {
	    MindmapNode rootMindmapNode = (MindmapNode) mindmapNodeList.get(0);

	    String rootMindmapUser = mindmapService.getMindmapMessageService().getMessage("node.instructor.label");

	    NodeModel rootNodeModel = new NodeModel(new NodeConceptModel(rootMindmapNode.getUniqueId(), rootMindmapNode
		    .getText(), rootMindmapNode.getColor(), rootMindmapUser, 1));
	    NodeModel currentNodeModel = mindmapService.getMindmapXMLFromDatabase(rootMindmapNode.getNodeId(),
		    mindmapId, rootNodeModel, null);

	    XStream xstream = new XStream();
	    xstream.alias("branch", NodeModel.class);
	    String mindmapContent = xstream.toXML(currentNodeModel);
	    
	    try {
		response.setContentType("text/xml");
		//response.setCharacterEncoding("utf-8");
		response.getWriter().write(mindmapContent);
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}

	return null;
    }
    
    /**
     * Returns the serialized XML of the Mindmap Nodes from Database
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return null
     */
    public ActionForward setLocale(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	
	try {
	    response.setContentType("text/xml");
	    //response.setCharacterEncoding("utf-8");
	    response.getWriter().write(mindmapService.getLanguageXML());
	} catch (IOException e) {
	    e.printStackTrace();
	}

	return null;
    }

    /**
     * Saves Mindmap Nodes to Database
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return null
     */
    public ActionForward updateContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	// TODO need error checking.

	// get authForm and session map.
	AuthoringForm authForm = (AuthoringForm) form;
	SessionMap<String, Object> map = getSessionMap(request, authForm);

	// get mindmap content.
	Mindmap mindmap = mindmapService.getMindmapByContentId((Long) map.get(AuthoringAction.KEY_TOOL_CONTENT_ID));

	// update mindmap content using form inputs.
	ToolAccessMode mode = (ToolAccessMode) map.get(AuthoringAction.KEY_MODE);
	updateMindmap(mindmap, authForm, mode);

	// remove attachments marked for deletion.
	Set<MindmapAttachment> attachments = mindmap.getMindmapAttachments();
	if (attachments == null) {
	    attachments = new HashSet<MindmapAttachment>();
	}

	for (MindmapAttachment att : getAttList(AuthoringAction.KEY_DELETED_FILES, map)) {
	    // remove from db, leave in repository
	    attachments.remove(att);
	}

	// add unsaved attachments
	attachments.addAll(getAttList(AuthoringAction.KEY_UNSAVED_ONLINE_FILES, map));
	attachments.addAll(getAttList(AuthoringAction.KEY_UNSAVED_OFFLINE_FILES, map));

	// set attachments in case it didn't exist
	mindmap.setMindmapAttachments(attachments);
	
	// set the update date
	mindmap.setUpdateDate(new Date());

	// releasing defineLater flag so that learner can start using the tool.
	mindmap.setDefineLater(false);

	mindmapService.saveOrUpdateMindmap(mindmap);

	request.setAttribute(AuthoringConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);

	// add the sessionMapID to form
	authForm.setSessionMapID(map.getSessionID());

	request.setAttribute(MindmapConstants.ATTR_SESSION_MAP, map);

	/* MINDMAP Code */

	// getting xml data from SWF
	String mindmapContent = authForm.getMindmapContent();
	MindmapUser mindmapUser = mindmapService.getUserByUID(mindmap.getCreateBy());

	// Saving Mindmap data to XML
	XStream xstream = new XStream();
	xstream.alias("branch", NodeModel.class);
	NodeModel rootNodeModel = (NodeModel) xstream.fromXML(mindmapContent);
	NodeConceptModel nodeConceptModel = rootNodeModel.getConcept();
	List<NodeModel> branches = rootNodeModel.getBranch();

	// saving root Node into database
	MindmapNode rootMindmapNode = (MindmapNode) mindmapService.getAuthorRootNodeByMindmapId(mindmap.getUid())
		.get(0);
	rootMindmapNode = mindmapService.saveMindmapNode(rootMindmapNode, null, nodeConceptModel.getId(),
		nodeConceptModel.getText(), nodeConceptModel.getColor(), mindmapUser, mindmap);

	// string to accumulate deleted nodes for query
	String nodesToDeleteCondition = " where uniqueId <> " + rootMindmapNode.getUniqueId();

	// saving child Nodes into database
	if (branches != null) {
	    mindmapService.setNodesToDeleteCondition("");
	    mindmapService.getChildMindmapNodes(branches, rootMindmapNode, mindmapUser, mindmap);
	}

	nodesToDeleteCondition += mindmapService.getNodesToDeleteCondition() + " and mindmap_id = " + mindmap.getUid();
	mindmapService.deleteNodes(nodesToDeleteCondition);

	/* MINDMAP Code */

	return mapping.findForward("success");
    }

    public ActionForward uploadOnline(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return uploadFile(mapping, (AuthoringForm) form, IToolContentHandler.TYPE_ONLINE, request);
    }

    public ActionForward uploadOffline(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return uploadFile(mapping, (AuthoringForm) form, IToolContentHandler.TYPE_OFFLINE, request);
    }

    public ActionForward deleteOnline(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return deleteFile(mapping, (AuthoringForm) form, IToolContentHandler.TYPE_ONLINE, request);
    }

    public ActionForward deleteOffline(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return deleteFile(mapping, (AuthoringForm) form, IToolContentHandler.TYPE_OFFLINE, request);
    }

    public ActionForward removeUnsavedOnline(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return removeUnsaved(mapping, (AuthoringForm) form, IToolContentHandler.TYPE_ONLINE, request);
    }

    public ActionForward removeUnsavedOffline(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return removeUnsaved(mapping, (AuthoringForm) form, IToolContentHandler.TYPE_OFFLINE, request);
    }

    /** Private Methods */

    private ActionForward uploadFile(ActionMapping mapping, AuthoringForm authForm, String type,
	    HttpServletRequest request) {
	SessionMap<String, Object> map = getSessionMap(request, authForm);

	FormFile file;
	List<MindmapAttachment> unsavedFiles;
	List<MindmapAttachment> savedFiles;
	if (StringUtils.equals(IToolContentHandler.TYPE_OFFLINE, type)) {
	    file = authForm.getOfflineFile();
	    unsavedFiles = getAttList(AuthoringAction.KEY_UNSAVED_OFFLINE_FILES, map);

	    savedFiles = getAttList(AuthoringAction.KEY_OFFLINE_FILES, map);
	} else {
	    file = authForm.getOnlineFile();
	    unsavedFiles = getAttList(AuthoringAction.KEY_UNSAVED_ONLINE_FILES, map);

	    savedFiles = getAttList(AuthoringAction.KEY_ONLINE_FILES, map);
	}

	// validate file max size
	ActionMessages errors = new ActionMessages();
	FileValidatorUtil.validateFileSize(file, true, errors);
	if (!errors.isEmpty()) {
	    request.setAttribute(MindmapConstants.ATTR_SESSION_MAP, map);
	    this.saveErrors(request, errors);
	    return mapping.findForward("success");
	}

	if (file.getFileName().length() != 0) {

	    // upload file to repository
	    MindmapAttachment newAtt = mindmapService.uploadFileToContent((Long) map
		    .get(AuthoringAction.KEY_TOOL_CONTENT_ID), file, type);

	    // Add attachment to unsavedFiles
	    // check to see if file with same name exists
	    MindmapAttachment currAtt;
	    Iterator iter = savedFiles.iterator();
	    while (iter.hasNext()) {
		currAtt = (MindmapAttachment) iter.next();
		if (StringUtils.equals(currAtt.getFileName(), newAtt.getFileName())
			&& StringUtils.equals(currAtt.getFileType(), newAtt.getFileType())) {
		    // move from this this list to deleted list.
		    getAttList(AuthoringAction.KEY_DELETED_FILES, map).add(currAtt);
		    iter.remove();
		    break;
		}
	    }
	    unsavedFiles.add(newAtt);

	    request.setAttribute(MindmapConstants.ATTR_SESSION_MAP, map);
	    request.setAttribute("unsavedChanges", new Boolean(true));
	}
	return mapping.findForward("success");
    }

    private ActionForward deleteFile(ActionMapping mapping, AuthoringForm authForm, String type,
	    HttpServletRequest request) {
	SessionMap<String, Object> map = getSessionMap(request, authForm);

	List fileList;
	if (StringUtils.equals(IToolContentHandler.TYPE_OFFLINE, type)) {
	    fileList = getAttList(AuthoringAction.KEY_OFFLINE_FILES, map);
	} else {
	    fileList = getAttList(AuthoringAction.KEY_ONLINE_FILES, map);
	}

	Iterator iter = fileList.iterator();

	while (iter.hasNext()) {
	    MindmapAttachment att = (MindmapAttachment) iter.next();

	    if (att.getFileUuid().equals(authForm.getDeleteFileUuid())) {
		// move to delete file list, deleted at next updateContent
		getAttList(AuthoringAction.KEY_DELETED_FILES, map).add(att);

		// remove from this list
		iter.remove();
		break;
	    }
	}

	request.setAttribute(MindmapConstants.ATTR_SESSION_MAP, map);
	request.setAttribute("unsavedChanges", new Boolean(true));

	return mapping.findForward("success");
    }

    private ActionForward removeUnsaved(ActionMapping mapping, AuthoringForm authForm, String type,
	    HttpServletRequest request) {
	SessionMap<String, Object> map = getSessionMap(request, authForm);

	List unsavedFiles;

	if (StringUtils.equals(IToolContentHandler.TYPE_OFFLINE, type)) {
	    unsavedFiles = getAttList(AuthoringAction.KEY_UNSAVED_OFFLINE_FILES, map);
	} else {
	    unsavedFiles = getAttList(AuthoringAction.KEY_UNSAVED_ONLINE_FILES, map);
	}

	Iterator iter = unsavedFiles.iterator();
	while (iter.hasNext()) {
	    MindmapAttachment att = (MindmapAttachment) iter.next();

	    if (att.getFileUuid().equals(authForm.getDeleteFileUuid())) {
		// delete from repository and list
		mindmapService.deleteFromRepository(att.getFileUuid(), att.getFileVersionId());
		iter.remove();
		break;
	    }
	}

	request.setAttribute(MindmapConstants.ATTR_SESSION_MAP, map);
	request.setAttribute("unsavedChanges", new Boolean(true));

	return mapping.findForward("success");
    }

    /**
     * Updates Mindmap content using AuthoringForm inputs.
     * 
     * @param mindmap
     * @param authForm
     * @param mode
     */
    private void updateMindmap(Mindmap mindmap, AuthoringForm authForm, ToolAccessMode mode) {
	mindmap.setTitle(authForm.getTitle());
	mindmap.setInstructions(authForm.getInstructions());
	if (mode.isAuthor()) { // Teacher cannot modify following
	    mindmap.setOfflineInstructions(authForm.getOfflineInstruction());
	    mindmap.setOnlineInstructions(authForm.getOnlineInstruction());
	    mindmap.setLockOnFinished(authForm.isLockOnFinished());
	    mindmap.setMultiUserMode(authForm.isMultiUserMode());
	    // reflection
	    mindmap.setReflectOnActivity(authForm.isReflectOnActivity());
	    mindmap.setReflectInstructions(authForm.getReflectInstructions());
	}
    }

    /**
     * Updates AuthoringForm using Mindmap content.
     * 
     * @param authForm
     * @param mindmap
     */
    private void updateAuthForm(AuthoringForm authForm, Mindmap mindmap) {
	authForm.setTitle(mindmap.getTitle());
	authForm.setInstructions(mindmap.getInstructions());
	authForm.setOnlineInstruction(mindmap.getOnlineInstructions());
	authForm.setOfflineInstruction(mindmap.getOfflineInstructions());
	authForm.setLockOnFinished(mindmap.isLockOnFinished());
	authForm.setMultiUserMode(mindmap.isMultiUserMode());
	// reflection
	authForm.setReflectOnActivity(mindmap.isReflectOnActivity());
	authForm.setReflectInstructions(mindmap.getReflectInstructions());
    }

    /**
     * Updates SessionMap using Mindmap content.
     * 
     * @param mindmap
     * @param mode
     * @return map
     */
    private SessionMap<String, Object> createSessionMap(Mindmap mindmap, ToolAccessMode mode, String contentFolderID,
	    Long toolContentID) {

	SessionMap<String, Object> map = new SessionMap<String, Object>();

	map.put(AuthoringAction.KEY_MODE, mode);
	map.put(AuthoringAction.KEY_CONTENT_FOLDER_ID, contentFolderID);
	map.put(AuthoringAction.KEY_TOOL_CONTENT_ID, toolContentID);
	map.put(AuthoringAction.KEY_ONLINE_FILES, new LinkedList<MindmapAttachment>());
	map.put(AuthoringAction.KEY_OFFLINE_FILES, new LinkedList<MindmapAttachment>());
	map.put(AuthoringAction.KEY_UNSAVED_ONLINE_FILES, new LinkedList<MindmapAttachment>());
	map.put(AuthoringAction.KEY_UNSAVED_OFFLINE_FILES, new LinkedList<MindmapAttachment>());
	map.put(AuthoringAction.KEY_DELETED_FILES, new LinkedList<MindmapAttachment>());

	Iterator iter = mindmap.getMindmapAttachments().iterator();
	while (iter.hasNext()) {
	    MindmapAttachment attachment = (MindmapAttachment) iter.next();
	    String type = attachment.getFileType();
	    if (type.equals(IToolContentHandler.TYPE_OFFLINE)) {
		getAttList(AuthoringAction.KEY_OFFLINE_FILES, map).add(attachment);
	    }
	    if (type.equals(IToolContentHandler.TYPE_ONLINE)) {
		getAttList(AuthoringAction.KEY_ONLINE_FILES, map).add(attachment);
	    }
	}

	return map;
    }

    /**
     * Gets ToolAccessMode from HttpRequest parameters. Default value is AUTHOR mode.
     * 
     * @param request
     */
    private ToolAccessMode getAccessMode(HttpServletRequest request) {
	ToolAccessMode mode;
	String modeStr = request.getParameter(AttributeNames.ATTR_MODE);
	if (StringUtils.equalsIgnoreCase(modeStr, ToolAccessMode.TEACHER.toString())) {
	    mode = ToolAccessMode.TEACHER;
	} else {
	    mode = ToolAccessMode.AUTHOR;
	}
	return mode;
    }

    /**
     * Retrieves a List of attachments from the map using the key.
     * 
     * @param key
     * @param map
     */
    private List<MindmapAttachment> getAttList(String key, SessionMap<String, Object> map) {
	List<MindmapAttachment> list = (List<MindmapAttachment>) map.get(key);
	return list;
    }

    /**
     * Retrieve the SessionMap from the HttpSession.
     * 
     * @param request
     * @param authForm
     */
    private SessionMap<String, Object> getSessionMap(HttpServletRequest request, AuthoringForm authForm) {
	return (SessionMap<String, Object>) request.getSession().getAttribute(authForm.getSessionMapID());
    }
}
