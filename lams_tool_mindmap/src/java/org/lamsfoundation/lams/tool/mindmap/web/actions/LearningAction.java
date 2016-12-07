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

package org.lamsfoundation.lams.tool.mindmap.web.actions;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.mindmap.dto.MindmapDTO;
import org.lamsfoundation.lams.tool.mindmap.model.Mindmap;
import org.lamsfoundation.lams.tool.mindmap.model.MindmapNode;
import org.lamsfoundation.lams.tool.mindmap.model.MindmapRequest;
import org.lamsfoundation.lams.tool.mindmap.model.MindmapSession;
import org.lamsfoundation.lams.tool.mindmap.model.MindmapUser;
import org.lamsfoundation.lams.tool.mindmap.service.IMindmapService;
import org.lamsfoundation.lams.tool.mindmap.service.MindmapServiceProxy;
import org.lamsfoundation.lams.tool.mindmap.util.MindmapConstants;
import org.lamsfoundation.lams.tool.mindmap.util.MindmapException;
import org.lamsfoundation.lams.tool.mindmap.util.xmlmodel.NodeConceptModel;
import org.lamsfoundation.lams.tool.mindmap.util.xmlmodel.NodeModel;
import org.lamsfoundation.lams.tool.mindmap.util.xmlmodel.NotifyRequestModel;
import org.lamsfoundation.lams.tool.mindmap.util.xmlmodel.NotifyResponseModel;
import org.lamsfoundation.lams.tool.mindmap.util.xmlmodel.PollResponseModel;
import org.lamsfoundation.lams.tool.mindmap.web.forms.LearningForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * @author Ruslan Kazakov
 * @version 1.0.1
 *
 *
 *
 *
 *
 *
 */
public class LearningAction extends LamsDispatchAction {

    private static Logger log = Logger.getLogger(LearningAction.class);
    private static final boolean MODE_OPTIONAL = false;
    private IMindmapService mindmapService;

    /**
     * Default action on page load. Clones Mindmap Nodes for each Learner in single-user mode. Uses shared (runtime
     * created in CopyToolContent method) Mindmap Nodes in multi-user mode.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return null
     */
    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	LearningForm learningForm = (LearningForm) form;

	// 'toolSessionID' and 'mode' parameters are expected to be present.
	// TODO need to catch exceptions and handle errors.
	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE,
		LearningAction.MODE_OPTIONAL);

	Long toolSessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

	// set up mindmapService
	if (mindmapService == null) {
	    mindmapService = MindmapServiceProxy.getMindmapService(this.getServlet().getServletContext());
	}

	// Retrieve the session and content.
	MindmapSession mindmapSession = mindmapService.getSessionBySessionId(toolSessionID);
	if (mindmapSession == null) {
	    throw new MindmapException("Cannot retrieve session with toolSessionID: " + toolSessionID);
	}

	Mindmap mindmap = mindmapSession.getMindmap();

	// check defineLater
	if (mindmap.isDefineLater()) {
	    return mapping.findForward("defineLater");
	}

	// set mode, toolSessionID and MindmapDTO
	request.setAttribute("mode", mode.toString());
	learningForm.setToolSessionID(toolSessionID);

	MindmapDTO mindmapDTO = new MindmapDTO();
	mindmapDTO.title = mindmap.getTitle();
	mindmapDTO.instructions = mindmap.getInstructions();
	mindmapDTO.lockOnFinish = mindmap.isLockOnFinished();
	mindmapDTO.multiUserMode = mindmap.isMultiUserMode();
	mindmapDTO.reflectInstructions = mindmap.getReflectInstructions();

	request.setAttribute("mindmapDTO", mindmapDTO);

	// Set the content in use flag.
	if (!mindmap.isContentInUse()) {
	    mindmap.setContentInUse(new Boolean(true));
	    mindmapService.saveOrUpdateMindmap(mindmap);
	}

	LearningWebUtil.putActivityPositionInRequestByToolSessionId(toolSessionID, request,
		getServlet().getServletContext());

	// check if there is submission deadline
	Date submissionDeadline = mindmap.getSubmissionDeadline();
	if (submissionDeadline != null) {
	    // store submission deadline to sessionMap
	    request.setAttribute(MindmapConstants.ATTR_SUBMISSION_DEADLINE, submissionDeadline);

	    HttpSession ss = SessionManager.getSession();
	    UserDTO learnerDto = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    TimeZone learnerTimeZone = learnerDto.getTimeZone();
	    Date tzSubmissionDeadline = DateUtil.convertToTimeZoneFromDefault(learnerTimeZone, submissionDeadline);
	    Date currentLearnerDate = DateUtil.convertToTimeZoneFromDefault(learnerTimeZone, new Date());

	    // calculate whether submission deadline has passed, and if so forward to "submissionDeadline"
	    if (currentLearnerDate.after(tzSubmissionDeadline)) {
		return mapping.findForward("submissionDeadline");
	    }
	}

	MindmapUser mindmapUser;
	if (mode.equals(ToolAccessMode.TEACHER)) {
	    Long userID = WebUtil.readLongParam(request, AttributeNames.PARAM_USER_ID, false);
	    mindmapUser = mindmapService.getUserByUserIdAndSessionId(userID, toolSessionID);
	} else {
	    mindmapUser = getCurrentUser(toolSessionID);
	}

	// set readOnly flag.
	if (mode.equals(ToolAccessMode.TEACHER) || (mindmap.isLockOnFinished() && mindmapUser.isFinishedActivity())) {
	    request.setAttribute("contentEditable", false);
	} else {
	    request.setAttribute("contentEditable", true);
	}
	request.setAttribute("finishedActivity", mindmapUser.isFinishedActivity());

	// mindmapContentPath Parameter
	String mindmapContentPath = Configuration.get(ConfigurationKeys.SERVER_URL)
		+ "tool/lamind10/learning.do?dispatch=setMindmapContent%26mindmapId=" + mindmap.getUid() + "%26userId="
		+ mindmapUser.getUid() + "%26sessionId=" + mindmapSession.getSessionId();
	request.setAttribute("mindmapContentPath", mindmapContentPath);

	// currentMindmapUser Parameter
	String currentMindmapUser = mindmapUser.getFirstName() + " " + mindmapUser.getLastName();
	request.setAttribute("currentMindmapUser", currentMindmapUser);

	// mindmapType Parameter
	String mindmapType = null;
	if (mindmap.isLockOnFinished() && mindmapUser.isFinishedActivity()) {
	    mindmapType = "images/mindmap_locked.swf";
	} else {
	    if (mindmap.isMultiUserMode() == true) {
		mindmapType = "images/mindmap_multiuser.swf";
	    } else {
		mindmapType = "images/mindmap_singleuser.swf";
	    }
	}
	request.setAttribute("mindmapType", mindmapType);

	// pollServer Parameter
	String pollServerParam = Configuration.get(ConfigurationKeys.SERVER_URL)
		+ "tool/lamind10/learning.do?dispatch=pollServerAction%26mindmapId=" + mindmap.getUid() + "%26userId="
		+ mindmapUser.getUid() + "%26sessionId=" + mindmapSession.getSessionId();
	request.setAttribute("pollServerParam", pollServerParam);

	// notifyServer Parameter
	String notifyServerParam = Configuration.get(ConfigurationKeys.SERVER_URL)
		+ "tool/lamind10/learning.do?dispatch=notifyServerAction%26mindmapId=" + mindmap.getUid() + "%26userId="
		+ mindmapUser.getUid() + "%26sessionId=" + mindmapSession.getSessionId();
	request.setAttribute("notifyServerParam", notifyServerParam);

	String localizationPath = Configuration.get(ConfigurationKeys.SERVER_URL)
		+ "tool/lamind10/learning.do?dispatch=setLocale";
	request.setAttribute("localizationPath", localizationPath);

	// setting userId for reflection
	request.setAttribute("userIdParam", mindmapUser.getUid());
	request.setAttribute("toolContentIdParam", mindmap.getUid());
	request.setAttribute("reflectOnActivity", mindmap.isReflectOnActivity());

	// AJAX calls for saving Mindmap every one minute
	request.setAttribute("get", Configuration.get(ConfigurationKeys.SERVER_URL) + "tool/lamind10/learning.do");
	request.setAttribute("dispatch", "saveLastMindmapChanges");
	request.setAttribute("mindmapId", mindmap.getUid());
	request.setAttribute("userId", mindmapUser.getUid());
	request.setAttribute("sessionId", toolSessionID);
	request.setAttribute("multiMode", mindmap.isMultiUserMode());

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

	return mapping.findForward("mindmap");
    }

    /**
     * Clones Mindmap Nodes for each Learner (used in single-user mode only).
     *
     * @param fromMindmapNode
     * @param toMindmapNode
     * @param fromContent
     * @param toContent
     * @param user
     */
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
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return null
     */
    public ActionForward notifyServerAction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Long userId = WebUtil.readLongParam(request, "userId", false);
	Long mindmapId = WebUtil.readLongParam(request, "mindmapId", false);
	Long toolSessionId = WebUtil.readLongParam(request, "sessionId", false);
	String requestAction = WebUtil.readStrParam(request, "actionXML", false);

	MindmapSession mindmapSession = mindmapService.getSessionBySessionId(toolSessionId);

	NotifyRequestModel notifyRequestModel = (NotifyRequestModel) mindmapService.getXStream().fromXML(requestAction);
	int requestType = notifyRequestModel.getType();

	// if request was previously created
	Long lastActionId = WebUtil.readLongParam(request, "lastActionId", false);

	MindmapRequest mindmapRequest = mindmapService.getRequestByUniqueId(notifyRequestModel.getID(), userId,
		mindmapId, lastActionId);

	String notifyResponse = null;

	// if request wasn't created before, create it
	if (mindmapRequest == null) {
	    // getting node to which changes will be applied
	    MindmapNode mindmapNode = null;
	    List mindmapNodeList = mindmapService.getMindmapNodeByUniqueIdSessionId(notifyRequestModel.getNodeID(),
		    mindmapId, toolSessionId);
	    if ((mindmapNodeList != null) && (mindmapNodeList.size() > 0)) {
		mindmapNode = (MindmapNode) mindmapNodeList.get(0);
	    } else {
		LearningAction.log.error("notifyServerAction(): Error finding node!");
		return null;
	    }

	    // delete node
	    if (requestType == 0) {
		// if node is created not by author or by other user... cannot delete
		if (mindmapNode.getUser() == mindmapService.getUserByUID(userId)) {

		    List nodes = mindmapService.getMindmapNodeByUniqueIdSessionId(notifyRequestModel.getNodeID(),
			    mindmapId, toolSessionId);

		    //if (nodes != null && nodes.size() > 0) // check if node exists
		    //{
		    MindmapNode curNode = (MindmapNode) nodes.get(0);
		    List childNodes = mindmapService.getMindmapNodeByParentIdMindmapIdSessionId(curNode.getNodeId(),
			    mindmapId, toolSessionId);

		    if ((childNodes == null) || (childNodes.size() == 0)) // check if node has any children
		    {
			mindmapService.deleteNodeByUniqueMindmapUser(notifyRequestModel.getNodeID(), mindmapId, userId,
				toolSessionId);
			mindmapRequest = saveMindmapRequest(mindmapRequest, requestType, notifyRequestModel, userId,
				mindmapId, null, toolSessionId);
			notifyResponse = generateNotifyResponse(1, mindmapRequest.getGlobalId(), null);
		    } else {
			notifyResponse = generateNotifyResponse(0, null, null);
		    }
		    //} else
		    //	notifyResponse = generateNotifyResponse(0, null, null);
		} else {
		    notifyResponse = generateNotifyResponse(0, null, null);
		}
	    }
	    // create node
	    else if (requestType == 1) {
		// no checking... users can create nodes everywhere
		NodeConceptModel nodeConceptModel = notifyRequestModel.getConcept();

		Long uniqueId = // node unique ID
			mindmapService.getNodeLastUniqueIdByMindmapUidSessionId(mindmapId, toolSessionId) + 1;

		mindmapService.saveMindmapNode(null, mindmapNode, uniqueId, nodeConceptModel.getText(),
			nodeConceptModel.getColor(), mindmapService.getUserByUID(userId),
			mindmapService.getMindmapByUid(mindmapId), mindmapSession);

		mindmapRequest = saveMindmapRequest(mindmapRequest, requestType, notifyRequestModel, userId, mindmapId,
			uniqueId, toolSessionId);
		notifyResponse = generateNotifyResponse(1, mindmapRequest.getGlobalId(), uniqueId);
	    }
	    // change color
	    else if (requestType == 2) {
		if (mindmapNode.getUser() == mindmapService.getUserByUID(userId)) {
		    mindmapNode.setColor(notifyRequestModel.getColor());
		    mindmapNode.setUser(mindmapService.getUserByUID(userId));
		    mindmapService.saveOrUpdateMindmapNode(mindmapNode);
		    mindmapRequest = saveMindmapRequest(mindmapRequest, requestType, notifyRequestModel, userId,
			    mindmapId, null, toolSessionId);
		    notifyResponse = generateNotifyResponse(1, mindmapRequest.getGlobalId(), null);
		} else {
		    notifyResponse = generateNotifyResponse(0, null, null);
		}
	    }
	    // change text
	    else if (requestType == 3) {
		if (mindmapNode.getUser() == mindmapService.getUserByUID(userId)) {
		    mindmapNode.setText(notifyRequestModel.getText());
		    mindmapNode.setUser(mindmapService.getUserByUID(userId));
		    mindmapService.saveOrUpdateMindmapNode(mindmapNode);
		    mindmapRequest = saveMindmapRequest(mindmapRequest, requestType, notifyRequestModel, userId,
			    mindmapId, null, toolSessionId);
		    notifyResponse = generateNotifyResponse(1, mindmapRequest.getGlobalId(), null);
		} else {
		    notifyResponse = generateNotifyResponse(0, null, null);
		}
	    }
	} else {
	    if (requestType == 1) {
		notifyResponse = generateNotifyResponse(1, mindmapRequest.getGlobalId(),
			mindmapRequest.getNodeChildId());
	    } else {
		notifyResponse = generateNotifyResponse(1, mindmapRequest.getGlobalId(), null);
	    }
	}

	try {
	    response.setContentType("text/xml");
	    response.setCharacterEncoding("utf-8");
	    response.getWriter().write(notifyResponse);
	} catch (IOException e) {
	    e.printStackTrace();
	}

	return null;
    }

    /**
     * Saves Notify Requests to database
     *
     * @param mindmapRequest
     * @param requestType
     * @param notifyRequestModel
     * @param userId
     * @param mindmapId
     * @param nodeChildId
     */
    private MindmapRequest saveMindmapRequest(MindmapRequest mindmapRequest, int requestType,
	    NotifyRequestModel notifyRequestModel, Long userId, Long mindmapId, Long nodeChildId, Long sessionId) {
	mindmapRequest = new MindmapRequest();
	mindmapRequest.setType(requestType);
	mindmapRequest.setUniqueId(notifyRequestModel.getID());
	// incrementing lastRequestId
	mindmapRequest.setGlobalId(mindmapService.getLastGlobalIdByMindmapId(mindmapId, sessionId) + 1);
	mindmapRequest.setUser(mindmapService.getUserByUID(userId));
	mindmapRequest.setMindmap(mindmapService.getMindmapByUid(mindmapId));
	mindmapRequest.setNodeId(notifyRequestModel.getNodeID());
	mindmapRequest.setNodeChildId(nodeChildId); // nodeChildId
	mindmapService.saveOrUpdateMindmapRequest(mindmapRequest);
	return mindmapRequest;
    }

    /**
     * Generated Notify Responses
     *
     * @param ok
     * @param id
     * @param data
     */
    private String generateNotifyResponse(int ok, Long id, Long data) {
	NotifyResponseModel nodeResponseModel = new NotifyResponseModel();
	nodeResponseModel.setOk(ok);
	nodeResponseModel.setId(id);
	if (data != null) {
	    nodeResponseModel.setData(data);
	}

	return mindmapService.getXStream().toXML(nodeResponseModel);
    }

    /**
     * Returns lists of Poll Requests (Actions) on Mindmap Nodes made by other learners
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return null
     */
    public ActionForward pollServerAction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Long mindmapId = WebUtil.readLongParam(request, "mindmapId", false);
	Long userId = WebUtil.readLongParam(request, "userId", false);
	Long toolSessionId = WebUtil.readLongParam(request, "sessionId", false);
	Long lastActionId = WebUtil.readLongParam(request, "lastActionID", false);

	PollResponseModel pollResponseModel = new PollResponseModel();

	List requestsList = mindmapService.getLastRequestsAfterGlobalId(lastActionId, mindmapId, userId, toolSessionId);
	for (Iterator iterator = requestsList.iterator(); iterator.hasNext();) {
	    MindmapRequest mindmapRequest = (MindmapRequest) iterator.next();
	    int requestType = mindmapRequest.getType();

	    NotifyRequestModel notifyRequestModel = null;
	    NodeConceptModel nodeConceptModel = null;

	    MindmapNode rootMindmapNode = null;
	    if ((requestType != 0) && (requestType != 1)) {
		//List nodesList = mindmapService.getMindmapNodeByUniqueId(mindmapRequest.getNodeId(), mindmapId);
		List nodesList = mindmapService.getMindmapNodeByUniqueIdSessionId(mindmapRequest.getNodeId(), mindmapId,
			toolSessionId);

		if ((nodesList != null) && (nodesList.size() > 0)) {
		    rootMindmapNode = (MindmapNode) nodesList.get(0);
		} else {
		    LearningAction.log.error("pollServerAction(): Error finding node while changing text or color!");
		}
	    }

	    MindmapNode mindmapNode = null;
	    if (requestType == 1) {
		//List nodesList = mindmapService.getMindmapNodeByUniqueId(mindmapRequest.getNodeChildId(), mindmapId);
		List nodesList = mindmapService.getMindmapNodeByUniqueIdSessionId(mindmapRequest.getNodeChildId(),
			mindmapId, toolSessionId);

		if ((nodesList != null) && (nodesList.size() > 0)) {
		    mindmapNode = (MindmapNode) nodesList.get(0);
		} else {
		    LearningAction.log.error("pollServerAction(): Error finding node while creating a node!");
		}
	    }

	    // delete node
	    if (requestType == 0) {
		notifyRequestModel = new NotifyRequestModel(mindmapRequest.getGlobalId(), mindmapRequest.getNodeId(),
			mindmapRequest.getType(), null, null, null);
	    }
	    // create node
	    else if (requestType == 1) {
		nodeConceptModel = new NodeConceptModel();
		nodeConceptModel.setId(mindmapNode.getUniqueId());
		nodeConceptModel.setText(mindmapNode.getText());
		nodeConceptModel.setColor(mindmapNode.getColor());

		MindmapUser mindmapUser = mindmapNode.getUser();
		if (mindmapUser != null) {
		    nodeConceptModel.setCreator(mindmapUser.getFirstName() + " " + mindmapUser.getLastName());
		} else {
		    nodeConceptModel.setCreator("Student");
		}

		notifyRequestModel = new NotifyRequestModel(mindmapRequest.getGlobalId(), mindmapRequest.getNodeId(),
			mindmapRequest.getType(), null, null, nodeConceptModel);
	    }
	    // change color
	    else if (requestType == 2) {
		notifyRequestModel = new NotifyRequestModel(mindmapRequest.getGlobalId(), mindmapRequest.getNodeId(),
			mindmapRequest.getType(), null, rootMindmapNode.getColor(), null);
	    }
	    // change text
	    else if (requestType == 3) {
		notifyRequestModel = new NotifyRequestModel(mindmapRequest.getGlobalId(), mindmapRequest.getNodeId(),
			mindmapRequest.getType(), rootMindmapNode.getText(), null, null);
	    }

	    pollResponseModel.addNotifyRequest(notifyRequestModel);
	}

	String pollResponse = mindmapService.getXStream().toXML(pollResponseModel);

	try {
	    response.setContentType("text/xml");
	    response.setCharacterEncoding("utf-8");
	    response.getWriter().write(pollResponse);
	} catch (IOException e) {
	    e.printStackTrace();
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
    public ActionForward setMindmapContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Long mindmapId = WebUtil.readLongParam(request, "mindmapId", false);
	Long userId = WebUtil.readLongParam(request, "userId", false);
	Long toolSessionId = WebUtil.readLongParam(request, "sessionId", true);
	Mindmap mindmap = mindmapService.getMindmapByUid(mindmapId);
	MindmapUser mindmapUser = mindmapService.getUserByUID(userId);

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

	    int edit = 1;
	    if (rootMindmapNode.getUser() == mindmapUser) {
		edit = 1;
	    } else {
		edit = 0;
	    }

	    NodeModel rootNodeModel = new NodeModel(new NodeConceptModel(rootMindmapNode.getUniqueId(),
		    rootMindmapNode.getText(), rootMindmapNode.getColor(), mindmapUserName, edit));

	    NodeModel currentNodeModel = mindmapService.getMindmapXMLFromDatabase(rootMindmapNode.getNodeId(),
		    mindmapId, rootNodeModel, mindmapUser);

	    String mindmapContent = mindmapService.getXStream().toXML(currentNodeModel);

	    // Saving lastActionID
	    Long lastActionId = mindmapService.getLastGlobalIdByMindmapId(mindmap.getUid(), toolSessionId);
	    //mindmap.setLastActionId(lastActionId);

	    // adding lastActionId
	    if (mindmap.isMultiUserMode()) {
		if (mindmap.isLockOnFinished() && !mindmapUser.isFinishedActivity()) {
		    mindmapContent = "<mindmap>\n" + mindmapContent + "\n<lastActionId>" + lastActionId
			    + "</lastActionId>\n</mindmap>";
		} else if (!mindmap.isLockOnFinished()) {
		    mindmapContent = "<mindmap>\n" + mindmapContent + "\n<lastActionId>" + lastActionId
			    + "</lastActionId>\n</mindmap>";
		}
	    }

	    try {
		response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");
		response.getWriter().write(mindmapContent);
	    } catch (IOException e) {
		e.printStackTrace();
	    }

	}
	return null;
    }

    public ActionForward saveLastMindmapChanges(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Long userId = WebUtil.readLongParam(request, "userId", false);
	Long toolContentId = WebUtil.readLongParam(request, "mindmapId", false);
	Long toolSessionId = WebUtil.readLongParam(request, "sessionId", true);
	MindmapUser mindmapUser = mindmapService.getUserByUID(userId);
	Mindmap mindmap = mindmapService.getMindmapByUid(toolContentId);
	MindmapSession mindmapSession = mindmapService.getSessionBySessionId(toolSessionId);

	// Saving Mindmap Nodes
	if (!mindmap.isMultiUserMode()) {
	    // getting xml data from SWF
	    String mindmapContent = WebUtil.readStrParam(request, "content", false);
	    // learningForm.getMindmapContent();

	    saveMindmapXML(mindmap, mindmapUser, mindmapContent, mindmapSession);
	}

	return null;
    }

    public void saveMindmapXML(Mindmap mindmap, MindmapUser mindmapUser, String mindmapContent,
	    MindmapSession mindmapSession) {
	// Saving Mindmap data to XML
	NodeModel rootNodeModel = (NodeModel) mindmapService.getXStream().fromXML(mindmapContent);
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
	    response.setCharacterEncoding("utf-8");
	    response.getWriter().write(mindmapService.getLanguageXML());
	} catch (IOException e) {
	    e.printStackTrace();
	}

	return null;
    }

    /**
     * Returns current learner
     *
     * @param toolSessionId
     * @return mindmapUser
     */
    private MindmapUser getCurrentUser(Long toolSessionId) {
	UserDTO user = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);

	// attempt to retrieve user using userId and toolSessionId
	MindmapUser mindmapUser = mindmapService.getUserByUserIdAndSessionId(new Long(user.getUserID().intValue()),
		toolSessionId);

	if (mindmapUser == null) {
	    MindmapSession mindmapSession = mindmapService.getSessionBySessionId(toolSessionId);
	    mindmapUser = mindmapService.createMindmapUser(user, mindmapSession);
	}

	return mindmapUser;
    }

    /**
     * Saving Mindmap nodes and proceed to reflection.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward reflect(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	LearningForm learningForm = (LearningForm) form;

	Long userId = WebUtil.readLongParam(request, "userId", false);
	Long toolContentId = WebUtil.readLongParam(request, "toolContentId", false);

	MindmapUser mindmapUser = mindmapService.getUserByUID(userId);
	Mindmap mindmap = mindmapService.getMindmapByUid(toolContentId);
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
	    saveMindmapXML(mindmap, mindmapUser, learningForm.getMindmapContent(), mindmapSession);
	}

	// Reflection
	NotebookEntry entry = mindmapService.getEntry(mindmapUser.getEntryUID());
	if (entry != null) {
	    request.setAttribute("reflectEntry", entry.getEntry());
	}

	LearningWebUtil.putActivityPositionInRequestByToolSessionId(mindmapSession.getSessionId(), request,
		getServlet().getServletContext());

	return mapping.findForward("reflect");
    }

    /**
     * Finish Mindmap Activity and save reflection if appropriate.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return null
     */
    public ActionForward finishActivity(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Long toolSessionID = WebUtil.readLongParam(request, "toolSessionID");
	MindmapUser mindmapUser = getCurrentUser(toolSessionID);

	if (mindmapUser != null) {
	    LearningForm learningForm = (LearningForm) form;

	    mindmapUser.setFinishedActivity(true);
	    mindmapService.saveOrUpdateMindmapUser(mindmapUser);

	    // Retrieve the session and content
	    MindmapSession mindmapSession = mindmapService.getSessionBySessionId(toolSessionID);
	    if (mindmapSession == null) {
		throw new MindmapException("Cannot retrieve session with toolSessionID" + toolSessionID);
	    }

	    Mindmap mindmap = mindmapSession.getMindmap();

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
		    saveMindmapXML(mindmap, mindmapUser, learningForm.getMindmapContent(), mindmapSession);
		}
	    }

	} else {
	    LearningAction.log.error(
		    "finishActivity(): couldn't find MindmapUser is null " + " and toolSessionID: " + toolSessionID);
	}

	ToolSessionManager sessionMgrService = MindmapServiceProxy
		.getMindmapSessionManager(getServlet().getServletContext());

	String nextActivityUrl;
	try {
	    nextActivityUrl = sessionMgrService.leaveToolSession(toolSessionID, mindmapUser.getUserId());
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

}
