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

package org.lamsfoundation.lams.tool.mindmap.web.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.mindmap.MindmapConstants;
import org.lamsfoundation.lams.tool.mindmap.dto.RootJSON;
import org.lamsfoundation.lams.tool.mindmap.model.Mindmap;
import org.lamsfoundation.lams.tool.mindmap.model.MindmapNode;
import org.lamsfoundation.lams.tool.mindmap.model.MindmapUser;
import org.lamsfoundation.lams.tool.mindmap.service.IMindmapService;
import org.lamsfoundation.lams.tool.mindmap.util.xmlmodel.NodeConceptModel;
import org.lamsfoundation.lams.tool.mindmap.util.xmlmodel.NodeModel;
import org.lamsfoundation.lams.tool.mindmap.web.forms.AuthoringForm;
import org.lamsfoundation.lams.util.CommonConstants;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Ruslan Kazakov
 */
@Controller
@RequestMapping("/authoring")
public class AuthoringController {
    private static Logger logger = Logger.getLogger(AuthoringController.class);

    // Authoring SessionMap key names
    private static final String KEY_TOOL_CONTENT_ID = "toolContentID";
    private static final String KEY_CONTENT_FOLDER_ID = "contentFolderID";
    private static final String KEY_MODE = "mode";

    @Autowired
    private IMindmapService mindmapService;

    @Autowired
    @Qualifier("mindmapMessageService")
    private MessageService messageService;

    /**
     * Default method when no dispatch parameter is specified. It is expected that the parameter
     * <code>toolContentID</code> will be passed in. This will be used to retrieve content for this tool.
     */
    @RequestMapping("/authoring")
    public String unspecified(@ModelAttribute AuthoringForm authoringForm, HttpServletRequest request,
	    HttpServletResponse response) {
	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);

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

	    MindmapNode rootMindmapNode = mindmapService.saveMindmapNode(null, null, 1l, rootNodeName, "#ffffff", null,
		    mindmap, null);
	    mindmapService.saveOrUpdateMindmapNode(rootMindmapNode);
	    mindmapService.saveMindmapNode(null, rootMindmapNode, 2l, childNodeName1, "#ffffff", null, mindmap, null);
	    mindmapService.saveMindmapNode(null, rootMindmapNode, 3l, childNodeName2, "#ffffff", null, mindmap, null);
	}

	return readDatabaseData(authoringForm, mindmap, request, mode);
    }

    /**
     * Set the defineLater flag so that learners cannot use content while we are editing. This flag is released when
     * updateContent is called.
     */
    @RequestMapping(path = "/definelater", method = RequestMethod.POST)
    public String definelater(@ModelAttribute AuthoringForm authoringForm, HttpServletRequest request) {
	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Mindmap mindmap = mindmapService.getMindmapByContentId(toolContentID);
	mindmap.setDefineLater(true);
	mindmapService.saveOrUpdateMindmap(mindmap);

	//audit log the teacher has started editing activity in monitor
	mindmapService.auditLogStartEditingActivityInMonitor(toolContentID);

	return readDatabaseData(authoringForm, mindmap, request, ToolAccessMode.TEACHER);
    }

    /**
     * Common method for "unspecified" and "defineLater"
     */
    private String readDatabaseData(AuthoringForm authoringForm, Mindmap mindmap, HttpServletRequest request,
	    ToolAccessMode mode) {
	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);

	/* Mindmap Attributes */
	request.setAttribute("mindmapId", mindmap.getUid());

	// Set up the authForm.
	updateAuthForm(authoringForm, mindmap);

	// Set up sessionMap
	SessionMap<String, Object> map = createSessionMap(mindmap, mode, contentFolderID, toolContentID);
	authoringForm.setSessionMapID(map.getSessionID());

	// add the sessionMap to HTTPSession.
	request.getSession().setAttribute(map.getSessionID(), map);
	request.setAttribute(MindmapConstants.ATTR_SESSION_MAP, map);

	return "pages/authoring/authoring";
    }

    /**
     * Returns the serialized XML of the Mindmap Nodes from Database
     */
    @RequestMapping("/setMindmapContentJSON")
    @ResponseBody
    public String setMindmapContentJSON(HttpServletRequest request, HttpServletResponse response) throws IOException {

	Long mindmapId = WebUtil.readLongParam(request, "mindmapId", false);
	List mindmapNodeList = mindmapService.getAuthorRootNodeByMindmapId(mindmapId);

	if (mindmapNodeList != null && mindmapNodeList.size() > 0) {
	    MindmapNode rootMindmapNode = (MindmapNode) mindmapNodeList.get(0);

	    String rootMindmapUser = messageService.getMessage("node.instructor.label");

	    NodeModel rootNodeModel = new NodeModel(new NodeConceptModel(rootMindmapNode.getUniqueId(),
		    rootMindmapNode.getText(), rootMindmapNode.getColor(), rootMindmapUser, 1));
	    NodeModel currentNodeModel = mindmapService.getMindmapXMLFromDatabase(rootMindmapNode.getNodeId(),
		    mindmapId, rootNodeModel, null, false, true, false);

	    ObjectNode jsonObject = JsonNodeFactory.instance.objectNode();
	    jsonObject.set("mindmap", new RootJSON(currentNodeModel, false));
	    response.setContentType("application/json;charset=UTF-8");
	    return jsonObject.toString();
	}

	return null;
    }

    /**
     * Saves Mindmap Nodes to Database
     */
    @RequestMapping(path = "/updateContent", method = RequestMethod.POST)
    public String updateContent(@ModelAttribute AuthoringForm authoringForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {

	// get session map.
	SessionMap<String, Object> map = getSessionMap(request, authoringForm);

	// get mindmap content.
	Mindmap mindmap = mindmapService.getMindmapByContentId((Long) map.get(AuthoringController.KEY_TOOL_CONTENT_ID));

	// update mindmap content using form inputs
	updateMindmap(mindmap, authoringForm);

	// set the update date
	mindmap.setUpdateDate(new Date());

	// releasing defineLater flag so that learner can start using the tool.
	mindmap.setDefineLater(false);

	mindmapService.saveOrUpdateMindmap(mindmap);

	request.setAttribute(CommonConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);

	// add the sessionMapID to form
	authoringForm.setSessionMapID(map.getSessionID());

	request.setAttribute(MindmapConstants.ATTR_SESSION_MAP, map);

	/* Saving Minmdap Nodes */
	MindmapUser mindmapUser = mindmapService.getUserByUID(mindmap.getCreateBy());
	String mindmapContent = authoringForm.getMindmapContent();

	NodeModel rootNodeModel = RootJSON.toNodeModel(mindmapContent);
	if (rootNodeModel == null) {
	    String error = new StringBuilder("Unable to save mindmap for authoring. User:")
		    .append(mindmapUser.getLoginName()).append("(" + mindmapUser.getUserId())
		    .append("). No root node. JSON: ").append(mindmapContent).toString();
	    throw new IOException(error);
	}

	NodeConceptModel nodeConceptModel = rootNodeModel.getConcept();
	List<NodeModel> branches = rootNodeModel.getBranch();

	// saving root Node into database
	MindmapNode rootMindmapNode = (MindmapNode) mindmapService.getAuthorRootNodeByMindmapId(mindmap.getUid())
		.get(0);
	rootMindmapNode = mindmapService.saveMindmapNode(rootMindmapNode, null, nodeConceptModel.getId(),
		nodeConceptModel.getText(), nodeConceptModel.getColor(), mindmapUser, mindmap, null);

	// string to accumulate deleted nodes for query
	String nodesToDeleteCondition = " where uniqueId <> " + rootMindmapNode.getUniqueId();

	// saving child Nodes into database
	if (branches != null) {
	    mindmapService.setNodesToDeleteCondition("");
	    mindmapService.getChildMindmapNodes(branches, rootMindmapNode, mindmapUser, mindmap, null);
	}

	nodesToDeleteCondition += mindmapService.getNodesToDeleteCondition() + " and mindmap_id = " + mindmap.getUid();
	mindmapService.deleteNodes(nodesToDeleteCondition);

	return "pages/authoring/authoring";
    }

    /**
     * Updates Mindmap content using AuthoringForm inputs.
     */
    private void updateMindmap(Mindmap mindmap, AuthoringForm authoringForm) {
	mindmap.setTitle(authoringForm.getTitle());
	mindmap.setInstructions(authoringForm.getInstructions());
	mindmap.setLockOnFinished(authoringForm.isLockOnFinished());
	mindmap.setMultiUserMode(authoringForm.isMultiUserMode());
	mindmap.setGalleryWalkEnabled(authoringForm.isGalleryWalkEnabled());
	mindmap.setGalleryWalkReadOnly(authoringForm.isGalleryWalkReadOnly());
	mindmap.setGalleryWalkInstructions(authoringForm.getGalleryWalkInstructions());
	// reflection
	mindmap.setReflectOnActivity(authoringForm.isReflectOnActivity());
	mindmap.setReflectInstructions(authoringForm.getReflectInstructions());
    }

    /**
     * Updates AuthoringForm using Mindmap content.
     */
    private void updateAuthForm(AuthoringForm authoringForm, Mindmap mindmap) {
	authoringForm.setTitle(mindmap.getTitle());
	authoringForm.setInstructions(mindmap.getInstructions());
	authoringForm.setLockOnFinished(mindmap.isLockOnFinished());
	authoringForm.setMultiUserMode(mindmap.isMultiUserMode());
	authoringForm.setGalleryWalkEnabled(mindmap.isGalleryWalkEnabled());
	authoringForm.setGalleryWalkReadOnly(mindmap.isGalleryWalkReadOnly());
	authoringForm.setGalleryWalkInstructions(mindmap.getGalleryWalkInstructions());
	// reflection
	authoringForm.setReflectOnActivity(mindmap.isReflectOnActivity());
	authoringForm.setReflectInstructions(mindmap.getReflectInstructions());
    }

    /**
     * Updates SessionMap using Mindmap content.
     */
    private SessionMap<String, Object> createSessionMap(Mindmap mindmap, ToolAccessMode mode, String contentFolderID,
	    Long toolContentID) {

	SessionMap<String, Object> map = new SessionMap<>();

	map.put(AuthoringController.KEY_MODE, mode);
	map.put(AuthoringController.KEY_CONTENT_FOLDER_ID, contentFolderID);
	map.put(AuthoringController.KEY_TOOL_CONTENT_ID, toolContentID);

	return map;
    }

    /**
     * Retrieve the SessionMap from the HttpSession.
     */
    private SessionMap<String, Object> getSessionMap(HttpServletRequest request, AuthoringForm authoringForm) {
	return (SessionMap<String, Object>) request.getSession().getAttribute(authoringForm.getSessionMapID());
    }
}
