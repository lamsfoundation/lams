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
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.mindmap.MindmapConstants;
import org.lamsfoundation.lams.tool.mindmap.dto.IdeaJSON;
import org.lamsfoundation.lams.tool.mindmap.dto.MindmapDTO;
import org.lamsfoundation.lams.tool.mindmap.dto.NotifyResponseJSON;
import org.lamsfoundation.lams.tool.mindmap.dto.RootJSON;
import org.lamsfoundation.lams.tool.mindmap.model.Mindmap;
import org.lamsfoundation.lams.tool.mindmap.model.MindmapNode;
import org.lamsfoundation.lams.tool.mindmap.model.MindmapRequest;
import org.lamsfoundation.lams.tool.mindmap.model.MindmapSession;
import org.lamsfoundation.lams.tool.mindmap.model.MindmapUser;
import org.lamsfoundation.lams.tool.mindmap.service.IMindmapService;
import org.lamsfoundation.lams.tool.mindmap.util.MindmapException;
import org.lamsfoundation.lams.tool.mindmap.util.xmlmodel.NodeConceptModel;
import org.lamsfoundation.lams.tool.mindmap.util.xmlmodel.NodeModel;
import org.lamsfoundation.lams.tool.mindmap.web.forms.LearningForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.JsonUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Ruslan Kazakov
 */
@Controller
@RequestMapping("/learning")
public class LearningController {
    private static Logger log = Logger.getLogger(LearningController.class);

    private static final boolean MODE_OPTIONAL = false;

    private static final String REQUEST_JSON_TYPE = "type"; // Expected to be int: 0 - delete; 1 - create node; 2 - change color; 3 - change text
    private static final String REQUEST_JSON_REQUEST_ID = "requestId"; // Expected to be long
    private static final String REQUEST_JSON_PARENT_NODE_ID = "parentId"; // Expected to be long

    @Autowired
    private IMindmapService mindmapService;

    @Autowired
    @Qualifier("mindmapMessageService")
    private MessageService messageService;

    /**
     * Default action on page load. Clones Mindmap Nodes for each Learner in single-user mode. Uses shared (runtime
     * created in CopyToolContent method) Mindmap Nodes in multi-user mode.
     */
    @RequestMapping("/learning")
    public String unspecified(@ModelAttribute LearningForm learningForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	// 'toolSessionID' and 'mode' parameters are expected to be present.
	// TODO need to catch exceptions and handle errors.
	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE,
		LearningController.MODE_OPTIONAL);

	Long toolSessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

	// Retrieve the session and content.
	MindmapSession mindmapSession = mindmapService.getSessionBySessionId(toolSessionID);
	if (mindmapSession == null) {
	    throw new MindmapException("Cannot retrieve session with toolSessionID: " + toolSessionID);
	}

	request.setAttribute(AttributeNames.ATTR_IS_LAST_ACTIVITY, mindmapService.isLastActivity(toolSessionID));

	HttpSession ss = SessionManager.getSession();
	UserDTO userDto = (UserDTO) ss.getAttribute(AttributeNames.USER);

	MindmapUser mindmapUser;
	if (mode.equals(ToolAccessMode.TEACHER)) {
	    mindmapUser = mindmapService.getUserByUserIdAndSessionId(userDto.getUserID().longValue(), toolSessionID);
	} else {
	    mindmapUser = getCurrentUser(toolSessionID, true);
	}

	Mindmap mindmap = mindmapSession.getMindmap();
	if (mindmap.isGalleryWalkEnabled() && mode != null && mode.isAuthor()) {
	    String[] galleryWalkParameterValuesArray = request.getParameterValues("galleryWalk");
	    if (galleryWalkParameterValuesArray != null) {
		Collection<String> galleryWalkParameterValues = Set.of(galleryWalkParameterValuesArray);

		if (!mindmap.isGalleryWalkStarted() && galleryWalkParameterValues.contains("forceStart")) {
		    mindmap.setGalleryWalkStarted(true);
		    mindmapService.saveOrUpdateMindmap(mindmap);
		}

		if (!mindmap.isGalleryWalkFinished() && galleryWalkParameterValues.contains("forceFinish")) {
		    mindmap.setGalleryWalkFinished(true);
		    mindmapService.saveOrUpdateMindmap(mindmap);
		}
	    }
	}

	MindmapDTO mindmapDTO = new MindmapDTO(mindmap);
	request.setAttribute("mindmapDTO", mindmapDTO);
	request.setAttribute("reflectOnActivity", mindmap.isReflectOnActivity());

	LearningController.storeMindmapCanvasParameters(mindmap, toolSessionID, mindmapUser, mode.toString(),
		!(mode.equals(ToolAccessMode.TEACHER)
			|| (mindmap.isLockOnFinished() && mindmapUser.isFinishedActivity())),
		request);

	if (mindmap.isGalleryWalkStarted()) {

	    mindmapService.fillGalleryWalkRatings(mindmapDTO, mindmapUser.getUserId());
	    return "pages/learning/galleryWalk";
	}

	// check defineLater
	if (mindmap.isDefineLater()) {
	    return "pages/learning/defineLater";
	}

	// set mode, toolSessionID and MindmapDTO
	learningForm.setToolSessionID(toolSessionID);

	// Set the content in use flag.
	if (!mindmap.isContentInUse()) {
	    mindmap.setContentInUse(true);
	    mindmapService.saveOrUpdateMindmap(mindmap);
	}
	// check if there is submission deadline
	Date submissionDeadline = mindmap.getSubmissionDeadline();
	if (submissionDeadline != null) {
	    // store submission deadline to sessionMap
	    request.setAttribute(MindmapConstants.ATTR_SUBMISSION_DEADLINE, submissionDeadline);
	    TimeZone learnerTimeZone = userDto.getTimeZone();
	    Date tzSubmissionDeadline = DateUtil.convertToTimeZoneFromDefault(learnerTimeZone, submissionDeadline);
	    Date currentLearnerDate = DateUtil.convertToTimeZoneFromDefault(learnerTimeZone, new Date());

	    // calculate whether submission deadline has passed, and if so forward to "submissionDeadline"
	    if (currentLearnerDate.after(tzSubmissionDeadline)) {
		return "pages/learning/submissionDeadline";
	    }
	}

	// setting userId for reflection
	request.setAttribute("userIdParam", mindmapUser.getUid());

	// if not multi-user mode
	if (!mindmap.isMultiUserMode()) {
	    // clonning Mindmap Nodes for every new user
	    List rootNodeList = mindmapService.getRootNodeByMindmapIdAndUserId(mindmap.getUid(), mindmapUser.getUid());

	    if ((rootNodeList == null) || (rootNodeList.size() == 0)) {
		MindmapNode fromMindmapNode = (MindmapNode) mindmapService
			.getAuthorRootNodeByMindmapId(mindmap.getUid()).get(0);
		cloneMindmapNodesForRuntime(fromMindmapNode, null, mindmap, mindmap, mindmapUser, mindmapSession);
	    }
	} else {
	    // clonning Mindmap Nodes for every new session
	    List rootNodeList = mindmapService.getAuthorRootNodeBySessionId(toolSessionID);

	    if ((rootNodeList == null) || (rootNodeList.size() == 0)) {
		MindmapNode fromMindmapNode = (MindmapNode) mindmapService
			.getAuthorRootNodeByMindmapId(mindmap.getUid()).get(0);
		cloneMindmapNodesForRuntime(fromMindmapNode, null, mindmap, mindmap, null, mindmapSession);
	    }

	    // Using Learner in Monitor mode (for Teachers to participate in multimode)
	    boolean isMonitor = WebUtil.readBooleanParam(request, "monitor", false);
	    request.setAttribute("isMonitor", isMonitor);
	}

	return "pages/learning/mindmap";
    }

    /**
     * Clones Mindmap Nodes for each Learner (used in single-user mode only).
     */
    @SuppressWarnings("rawtypes")
    public void cloneMindmapNodesForRuntime(MindmapNode fromMindmapNode, MindmapNode toMindmapNode, Mindmap fromContent,
	    Mindmap toContent, MindmapUser user, MindmapSession session) {
	toMindmapNode = mindmapService.saveMindmapNode(null, toMindmapNode, fromMindmapNode.getUniqueId(),
		fromMindmapNode.getText(), fromMindmapNode.getColor(), user, toContent, session);

	List childMindmapNodes = mindmapService.getMindmapNodeByParentId(fromMindmapNode.getNodeId(),
		fromContent.getUid());

	if ((childMindmapNodes != null) && (childMindmapNodes.size() > 0)) {
	    for (Iterator iterator = childMindmapNodes.iterator(); iterator.hasNext();) {
		MindmapNode childMindmapNode = (MindmapNode) iterator.next();
		cloneMindmapNodesForRuntime(childMindmapNode, toMindmapNode, fromContent, toContent, user, session);
	    }
	}
    }

    /**
     * Gets the Notify Requests (Actions) from Flash and returns proper Notify Responses
     */
    @RequestMapping("/notifyServerActionJSON")
    @ResponseBody
    public String notifyServerActionJSON(HttpServletRequest request, HttpServletResponse response)
	    throws JsonProcessingException, IOException {
	Long userId = WebUtil.readLongParam(request, "userId", false);
	Long mindmapId = WebUtil.readLongParam(request, "mindmapId", false);
	Long toolSessionId = WebUtil.readLongParam(request, "sessionId", false);
	String requestAction = WebUtil.readStrParam(request, "actionJSON", false);

	MindmapSession mindmapSession = mindmapService.getSessionBySessionId(toolSessionId);

	ObjectNode notifyRequest = JsonUtil.readObject(requestAction);
	int requestType = JsonUtil.optInt(notifyRequest, REQUEST_JSON_TYPE);

	MindmapRequest mindmapRequest = null;
	if (notifyRequest.has(REQUEST_JSON_REQUEST_ID)) {
	    Long lastActionId = WebUtil.readLongParam(request, "lastActionId", false);
	    mindmapRequest = mindmapService.getRequestByUniqueId(
		    JsonUtil.optLong(notifyRequest, REQUEST_JSON_REQUEST_ID), userId, mindmapId, lastActionId);
	}

	NotifyResponseJSON notifyResponse = null;

	MindmapUser currentUser = mindmapService.getUserByUID(userId);
	if (currentUser == null
		|| (mindmapSession.getMindmap().isLockOnFinished() && currentUser.isFinishedActivity())) {
	    notifyResponse = new NotifyResponseJSON(0, null, null);
	}

	// if request wasn't created before, create it
	else if (mindmapRequest == null) {
	    // getting node to which changes will be applied
	    MindmapNode mindmapNode = null;
	    if (notifyRequest.has(IdeaJSON.MAPJS_JSON_ID_KEY) && requestType != 1) {
		mindmapNode = mindmapService.getMindmapNodeByUniqueIdSessionId(
			JsonUtil.optLong(notifyRequest, IdeaJSON.MAPJS_JSON_ID_KEY), mindmapId, toolSessionId);
		if (mindmapNode == null) {
		    LearningController.log.error("notifyServerAction(): Error finding node!");
		    return null;
		}
	    }

	    // delete node
	    if (requestType == 0) {
		// if node is created not by author or by other user... cannot delete
		if (mindmapNode.getUser() == mindmapService.getUserByUID(userId)) {

		    List childNodes = mindmapService.getMindmapNodeByParentIdMindmapIdSessionId(mindmapNode.getNodeId(),
			    mindmapId, toolSessionId);

		    if ((childNodes == null) || (childNodes.size() == 0)) // check if node has any children
		    {
			mindmapService.deleteNodeByUniqueMindmapUser(mindmapNode.getUniqueId(), mindmapId, userId,
				mindmapSession.getUid());
			mindmapRequest = saveMindmapRequestJSON(mindmapRequest, requestType, null,
				mindmapNode.getUniqueId(), userId, mindmapId, null, toolSessionId);
			notifyResponse = new NotifyResponseJSON(1, mindmapRequest.getGlobalId(), null);
		    } else {
			notifyResponse = new NotifyResponseJSON(0, null, null);
		    }
		} else {
		    notifyResponse = new NotifyResponseJSON(0, null, null);
		}
	    }
	    // create node
	    else if (requestType == 1) {
		// no checking... users can create nodes everywhere.

		// node unique ID - keep the biggest out of (next database value, client node). this ensures
		// that deletions don't go back and fill holes with nodes that may conflict with something on a client.
		Long uniqueId = mindmapService.getNodeLastUniqueIdByMindmapUidSessionId(mindmapId, toolSessionId) + 1;
		Long childIdFromRequest = JsonUtil.optLong(notifyRequest, IdeaJSON.MAPJS_JSON_ID_KEY);
		if (childIdFromRequest.longValue() > uniqueId.longValue()) {
		    uniqueId = childIdFromRequest;
		}

		Long parentNodeId = JsonUtil.optLong(notifyRequest, REQUEST_JSON_PARENT_NODE_ID);
		MindmapNode parentNode = mindmapService.getMindmapNodeByUniqueIdSessionId(parentNodeId, mindmapId,
			toolSessionId);
		if (parentNode == null) {
		    LearningController.log.error("notifyServerAction(): Unable to find parent node: " + parentNodeId
			    + " toolSessionId " + toolSessionId);
		}

		mindmapService.saveMindmapNode(null, parentNode, uniqueId,
			JsonUtil.optString(notifyRequest, IdeaJSON.MAPJS_JSON_TITLE_KEY),
			JsonUtil.optString(notifyRequest, IdeaJSON.MAPJS_JSON_BACKGROUND_COLOR_KEY),
			mindmapService.getUserByUID(userId), mindmapService.getMindmapByUid(mindmapId), mindmapSession);

		mindmapRequest = saveMindmapRequestJSON(mindmapRequest, requestType, null, parentNodeId, userId,
			mindmapId, uniqueId, toolSessionId);
		notifyResponse = new NotifyResponseJSON(1, mindmapRequest.getGlobalId(), uniqueId);
	    }
	    // change color
	    else if (requestType == 2) {
		if (mindmapNode.getUser() == mindmapService.getUserByUID(userId)) {
		    mindmapNode.setColor(JsonUtil.optString(notifyRequest, IdeaJSON.MAPJS_JSON_BACKGROUND_COLOR_KEY));
		    mindmapNode.setUser(mindmapService.getUserByUID(userId));
		    mindmapService.saveOrUpdateMindmapNode(mindmapNode);
		    mindmapRequest = saveMindmapRequestJSON(mindmapRequest, requestType, null,
			    mindmapNode.getUniqueId(), userId, mindmapId, null, toolSessionId);
		    notifyResponse = new NotifyResponseJSON(1, mindmapRequest.getGlobalId(), null);
		} else {
		    notifyResponse = new NotifyResponseJSON(0, null, null);
		}
	    }
	    // change text
	    else if (requestType == 3) {
		if (mindmapNode.getUser() == mindmapService.getUserByUID(userId)) {
		    mindmapNode.setText(JsonUtil.optString(notifyRequest, IdeaJSON.MAPJS_JSON_TITLE_KEY));
		    mindmapNode.setUser(mindmapService.getUserByUID(userId));
		    mindmapService.saveOrUpdateMindmapNode(mindmapNode);
		    mindmapRequest = saveMindmapRequestJSON(mindmapRequest, requestType, null,
			    mindmapNode.getUniqueId(), userId, mindmapId, null, toolSessionId);
		    notifyResponse = new NotifyResponseJSON(1, mindmapRequest.getGlobalId(), null);
		} else {
		    notifyResponse = new NotifyResponseJSON(0, null, null);
		}
	    }
	} else {
	    if (requestType == 1) {
		notifyResponse = new NotifyResponseJSON(1, mindmapRequest.getGlobalId(),
			mindmapRequest.getNodeChildId());
	    } else {
		notifyResponse = new NotifyResponseJSON(1, mindmapRequest.getGlobalId(), null);
	    }
	}
	response.setContentType("application/json;charset=UTF-8");
	return notifyResponse.toString();
    }

    /**
     * Saves Notify Requests to database
     */
    private MindmapRequest saveMindmapRequestJSON(MindmapRequest mindmapRequest, int requestType, Long requestId,
	    Long nodeId, Long userId, Long mindmapId, Long nodeChildId, Long sessionId) {
	mindmapRequest = new MindmapRequest();
	mindmapRequest.setType(requestType);
	mindmapRequest.setUniqueId(requestId);
	// incrementing lastRequestId
	mindmapRequest.setGlobalId(mindmapService.getLastGlobalIdByMindmapId(mindmapId, sessionId) + 1);
	mindmapRequest.setUser(mindmapService.getUserByUID(userId));
	mindmapRequest.setMindmap(mindmapService.getMindmapByUid(mindmapId));
	mindmapRequest.setNodeId(nodeId);
	mindmapRequest.setNodeChildId(nodeChildId); // nodeChildId
	mindmapService.saveOrUpdateMindmapRequest(mindmapRequest);
	return mindmapRequest;
    }

    /**
     * Returns the serialized JSON of the Mindmap Nodes from Database
     */
    @RequestMapping("/setMindmapContentJSON")
    @ResponseBody
    public String setMindmapContentJSON(HttpServletRequest request, HttpServletResponse response) throws IOException {

	Long mindmapId = WebUtil.readLongParam(request, "mindmapId", false);
	Long toolSessionId = WebUtil.readLongParam(request, "sessionId", true);
	Mindmap mindmap = mindmapService.getMindmapByUid(mindmapId);

	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE,
		LearningController.MODE_OPTIONAL);
	boolean monitoring = mode.equals(ToolAccessMode.TEACHER);

	// In monitoring we do not need the user when accessing multi mode mindmap
	Long userId = WebUtil.readLongParam(request, "userId", true);
	if (userId == null && !(monitoring && mindmap.isMultiUserMode())) {
	    log.error(
		    "Unable to display mindmap as no user id is supplied. Can only skip user id if this is monitoring and multiuser mode.");
	    return null;
	}

	MindmapUser mindmapUser = null;
	if (userId != null) {
	    mindmapUser = mindmapService.getUserByUID(userId);
	}

	List mindmapNodeList = null;
	if (mindmap.isMultiUserMode()) {
	    mindmapNodeList = mindmapService.getAuthorRootNodeByMindmapSession(mindmapId, toolSessionId);
	} else {
	    mindmapNodeList = mindmapService.getRootNodeByMindmapIdAndUserId(mindmapId, userId);
	}

	if ((mindmapNodeList != null) && (mindmapNodeList.size() > 0)) {
	    MindmapNode rootMindmapNode = (MindmapNode) mindmapNodeList.get(0);

	    String mindmapUserName = null;
	    if (rootMindmapNode.getUser() == null) {
		mindmapUserName = mindmapService.getMindmapMessageService().getMessage("node.instructor.label");
	    } else {
		mindmapUserName = rootMindmapNode.getUser().getFirstName() + " "
			+ rootMindmapNode.getUser().getLastName();
	    }

	    NodeModel rootNodeModel = new NodeModel(new NodeConceptModel(rootMindmapNode.getUniqueId(),
		    rootMindmapNode.getText(), rootMindmapNode.getColor(), mindmapUserName, 0));

	    NodeModel currentNodeModel = mindmapService.getMindmapXMLFromDatabase(rootMindmapNode.getNodeId(),
		    mindmapId, rootNodeModel, mindmapUser, monitoring, false,
		    mindmap.isLockOnFinished() && mindmapUser.isFinishedActivity());

	    ObjectNode jsonObject = JsonNodeFactory.instance.objectNode();
	    jsonObject.set("mindmap", new RootJSON(currentNodeModel, mindmap.isMultiUserMode()));
	    // adding lastActionId
	    if (mindmap.isMultiUserMode()) {
		Long lastActionId = mindmapService.getLastGlobalIdByMindmapId(mindmap.getUid(), toolSessionId);
		jsonObject.put("lastActionId", lastActionId);
	    }
	    response.setContentType("application/json;charset=UTF-8");
	    return jsonObject.toString();
	}
	return null;
    }

    @RequestMapping("/saveLastMindmapChanges")
    @ResponseBody
    public String saveLastMindmapChanges(HttpServletRequest request, HttpServletResponse response) throws IOException {

	Long userId = WebUtil.readLongParam(request, "userId", false);
	Long toolContentId = WebUtil.readLongParam(request, "mindmapId", false);
	Long toolSessionId = WebUtil.readLongParam(request, "toolSessionID", true);
	MindmapUser mindmapUser = mindmapService.getUserByUID(userId);
	Mindmap mindmap = mindmapService.getMindmapByUid(toolContentId);
	MindmapSession mindmapSession = mindmapService.getSessionBySessionId(toolSessionId);

	NotifyResponseJSON responseJSON = null;
	// Saving Mindmap Nodes
	if (!mindmap.isMultiUserMode()) {
	    // getting JSON from Mindmup
	    String mindmapContent = WebUtil.readStrParam(request, "content", false);
	    Long rootNodeId = saveMapJsJSON(mindmap, mindmapUser, mindmapContent, mindmapSession);
	    responseJSON = new NotifyResponseJSON(rootNodeId != null ? 1 : 0, null, rootNodeId);
	} else {
	    responseJSON = new NotifyResponseJSON(0, null, 0L);
	}
	response.setContentType("application/json;charset=UTF-8");
	return responseJSON.toString();
    }

    private Long saveMapJsJSON(Mindmap mindmap, MindmapUser mindmapUser, String mindmapContent,
	    MindmapSession mindmapSession) throws IOException {

	NodeModel rootNodeModel = RootJSON.toNodeModel(mindmapContent);
	if (rootNodeModel == null) {
	    String error = new StringBuilder("Unable to save mindmap for session:")
		    .append(mindmapSession.getSessionName()).append("(").append(mindmapSession.getSessionId())
		    .append(") user:").append(mindmapUser.getLoginName()).append("(" + mindmapUser.getUserId())
		    .append("). No root node. JSON: ").append(mindmapContent).toString();
	    throw new IOException(error);
	}

	NodeConceptModel nodeConceptModel = rootNodeModel.getConcept();
	List<NodeModel> branches = rootNodeModel.getBranch();

	// saving root Node into database
	MindmapNode rootMindmapNode = (MindmapNode) mindmapService
		.getRootNodeByMindmapIdAndUserId(mindmap.getUid(), mindmapUser.getUid()).get(0);
	rootMindmapNode = mindmapService.saveMindmapNode(rootMindmapNode, null, nodeConceptModel.getId(),
		nodeConceptModel.getText(), nodeConceptModel.getColor(), mindmapUser, mindmap, mindmapSession);

	// string to accumulate deleted nodes for query
	String nodesToDeleteCondition = " where uniqueId <> " + rootMindmapNode.getUniqueId();

	// saving child Nodes into database
	if (branches != null) {
	    mindmapService.setNodesToDeleteCondition("");
	    mindmapService.getChildMindmapNodes(branches, rootMindmapNode, mindmapUser, mindmap, mindmapSession);
	}

	nodesToDeleteCondition += mindmapService.getNodesToDeleteCondition() + " and mindmap_id = " + mindmap.getUid()
		+ " and user_id = " + mindmapUser.getUid();
	mindmapService.deleteNodes(nodesToDeleteCondition);

	return rootMindmapNode.getNodeId();
    }

    /**
     * Returns current learner
     */
    private MindmapUser getCurrentUser(Long toolSessionId, boolean create) {
	UserDTO user = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);

	// attempt to retrieve user using userId and toolSessionId
	MindmapUser mindmapUser = mindmapService.getUserByUserIdAndSessionId(new Long(user.getUserID().intValue()),
		toolSessionId);

	if (mindmapUser == null && create) {
	    MindmapSession mindmapSession = mindmapService.getSessionBySessionId(toolSessionId);
	    mindmapUser = mindmapService.createMindmapUser(user, mindmapSession);
	}

	return mindmapUser;
    }

    /**
     * Saving Mindmap nodes and proceed to reflection.
     */
    @RequestMapping("/reflect")
    public String reflect(@ModelAttribute LearningForm learningForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {

	Long userId = WebUtil.readLongParam(request, "userUid", false);
	Long toolContentId = WebUtil.readLongParam(request, "toolContentId", false);

	MindmapUser mindmapUser = mindmapService.getUserByUID(userId);
	Mindmap mindmap = mindmapService.getMindmapByContentId(toolContentId);
	MindmapSession mindmapSession = mindmapUser.getMindmapSession();

	request.setAttribute("reflectTitle", mindmap.getTitle());
	request.setAttribute("reflectInstructions", mindmap.getReflectInstructions());

	if (mindmap.isLockOnFinished() && mindmapUser.isFinishedActivity()) {
	    request.setAttribute("contentEditable", false);
	} else {
	    request.setAttribute("contentEditable", true);
	}

	// Saving Mindmap Nodes
	if (!mindmap.isMultiUserMode() && !StringUtils.isBlank(learningForm.getMindmapContent())) {
	    saveMapJsJSON(mindmap, mindmapUser, learningForm.getMindmapContent(), mindmapSession);
	}

	// Reflection
	NotebookEntry entry = mindmapService.getEntry(mindmapUser.getEntryUID());
	if (entry != null) {
	    request.setAttribute("reflectEntry", entry.getEntry());
	}

	request.setAttribute(AttributeNames.ATTR_IS_LAST_ACTIVITY,
		mindmapService.isLastActivity(mindmapSession.getSessionId()));
	return "pages/learning/reflect";
    }

    /**
     * Finish Mindmap Activity and save reflection if appropriate.
     */
    @RequestMapping("/finishActivity")
    public String finishActivity(@ModelAttribute LearningForm learningForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {

	Long toolSessionID = WebUtil.readLongParam(request, "toolSessionID");
	MindmapUser mindmapUser = getCurrentUser(toolSessionID, true);

	// Retrieve the session and content
	MindmapSession mindmapSession = mindmapService.getSessionBySessionId(toolSessionID);
	if (mindmapSession == null) {
	    throw new MindmapException("Cannot retrieve session with toolSessionID" + toolSessionID);
	}
	Mindmap mindmap = mindmapSession.getMindmap();

	// if locked do not update anything - do not want to risk having accidently changed the mindmap
	boolean contentLocked = mindmap.isLockOnFinished() && mindmapUser.isFinishedActivity();

	if (mindmapUser != null) {
	    if (!contentLocked) {

		mindmapUser.setFinishedActivity(true);
		mindmapService.saveOrUpdateMindmapUser(mindmapUser);

		// save the reflection entry and call the notebook.
		if (mindmap.isReflectOnActivity()) {
		    // check for existing notebook entry
		    NotebookEntry entry = mindmapService.getEntry(mindmapUser.getEntryUID());
		    if (entry == null) {
			// create new entry
			Long entryUID = mindmapService.createNotebookEntry(toolSessionID,
				CoreNotebookConstants.NOTEBOOK_TOOL, MindmapConstants.TOOL_SIGNATURE,
				mindmapUser.getUserId().intValue(), learningForm.getEntryText());
			mindmapUser.setEntryUID(entryUID);
			mindmapService.saveOrUpdateMindmapUser(mindmapUser);
		    } else {
			// update existing entry
			entry.setEntry(learningForm.getEntryText());
			entry.setLastModified(new Date());
			mindmapService.updateEntry(entry);
		    }
		} else {
		    if (!mindmap.isMultiUserMode() && !StringUtils.isBlank(learningForm.getMindmapContent())) {
			saveMapJsJSON(mindmap, mindmapUser, learningForm.getMindmapContent(), mindmapSession);
		    }
		}
	    }

	} else {
	    LearningController.log.error(
		    "finishActivity(): couldn't find MindmapUser is null " + " and toolSessionID: " + toolSessionID);
	}

	String nextActivityUrl;
	try {
	    nextActivityUrl = ((ToolSessionManager) mindmapService).leaveToolSession(toolSessionID,
		    mindmapUser.getUserId());
	    response.sendRedirect(nextActivityUrl);
	} catch (DataMissingException e) {
	    throw new MindmapException(e);
	} catch (ToolException e) {
	    throw new MindmapException(e);
	} catch (IOException e) {
	    throw new MindmapException(e);
	}

	return null;
    }

    @RequestMapping("/getGalleryWalkMindmap")
    public String getGalleryWalkMindmap(@RequestParam Long toolSessionID, HttpServletRequest request) {
	MindmapSession session = mindmapService.getSessionBySessionId(toolSessionID);
	Mindmap mindmap = session.getMindmap();
	MindmapUser user = getCurrentUser(toolSessionID, false);
	boolean contentEditable = mindmap.isGalleryWalkEditEnabled() && user != null
		&& user.getMindmapSession().getSessionId().equals(toolSessionID);
	LearningController.storeMindmapCanvasParameters(mindmap, toolSessionID, user,
		user == null ? ToolAccessMode.TEACHER.toString() : ToolAccessMode.LEARNER.toString(), contentEditable,
		request);
	return "pages/learning/singleMindmap";
    }

    @RequestMapping("/getPrintMindmap")
    public String getPrintMindmap(@RequestParam Long toolSessionID, HttpServletRequest request) {
	MindmapSession session = mindmapService.getSessionBySessionId(toolSessionID);
	Mindmap mindmap = session.getMindmap();
	LearningController.storeMindmapCanvasParameters(mindmap, toolSessionID, null, ToolAccessMode.TEACHER.toString(),
		false, request);
	request.setAttribute("printMode", true);
	request.setAttribute("sessionName", session.getSessionName());
	return "pages/learning/singleMindmap";
    }

    private static void storeMindmapCanvasParameters(Mindmap mindmap, Long toolSessionID, MindmapUser user, String mode,
	    boolean contentEditable, HttpServletRequest request) {
	request.setAttribute("mindmapId", mindmap.getUid());
	request.setAttribute("multiMode", mindmap.isMultiUserMode());
	request.setAttribute("toolContentID", mindmap.getToolContentId());
	request.setAttribute("sessionId", toolSessionID);
	request.setAttribute("toolSessionID", toolSessionID);
	request.setAttribute("userUid", user == null ? null : user.getUid());
	request.setAttribute("contentEditable", contentEditable);
	request.setAttribute("mode", mode);
	request.setAttribute("currentMindmapUser", user == null ? "" : user.getFirstName() + " " + user.getLastName());
	request.setAttribute("finishedActivity", user == null ? false : user.isFinishedActivity());
    }
}