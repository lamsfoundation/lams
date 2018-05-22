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
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.authoring.web.AuthoringConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.mindmap.dto.RootJSON;
import org.lamsfoundation.lams.tool.mindmap.model.Mindmap;
import org.lamsfoundation.lams.tool.mindmap.model.MindmapNode;
import org.lamsfoundation.lams.tool.mindmap.model.MindmapUser;
import org.lamsfoundation.lams.tool.mindmap.service.IMindmapService;
import org.lamsfoundation.lams.tool.mindmap.service.MindmapServiceProxy;
import org.lamsfoundation.lams.tool.mindmap.util.MindmapConstants;
import org.lamsfoundation.lams.tool.mindmap.util.xmlmodel.NodeConceptModel;
import org.lamsfoundation.lams.tool.mindmap.util.xmlmodel.NodeModel;
import org.lamsfoundation.lams.tool.mindmap.web.forms.AuthoringForm;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 * @author Ruslan Kazakov
 * @version 1.0.1
 *
 *
 *
 *
 */
public class AuthoringAction extends LamsDispatchAction {

    private static Logger logger = Logger.getLogger(AuthoringAction.class);

    public IMindmapService mindmapService;

    // Authoring SessionMap key names
    private static final String KEY_TOOL_CONTENT_ID = "toolContentID";
    private static final String KEY_CONTENT_FOLDER_ID = "contentFolderID";
    private static final String KEY_MODE = "mode";

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
	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);

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

	    MindmapNode rootMindmapNode = mindmapService.saveMindmapNode(null, null, 1l, rootNodeName, "#ffffff", null,
		    mindmap, null);
	    mindmapService.saveOrUpdateMindmapNode(rootMindmapNode);
	    mindmapService.saveMindmapNode(null, rootMindmapNode, 2l, childNodeName1, "#ffffff", null, mindmap, null);
	    mindmapService.saveMindmapNode(null, rootMindmapNode, 3l, childNodeName2, "#ffffff", null, mindmap, null);
	}

	if (mode.isTeacher()) {
	    // Set the defineLater flag so that learners cannot use content
	    // while we are editing. This flag is released when updateContent is called.
	    mindmap.setDefineLater(true);
	    mindmapService.saveOrUpdateMindmap(mindmap);
	    
	    //audit log the teacher has started editing activity in monitor
	    mindmapService.auditLogStartEditingActivityInMonitor(toolContentID);
	}

	/* Mindmap Attributes */
	request.setAttribute("mindmapId", mindmap.getUid());
	
	// Set up the authForm.
	AuthoringForm authForm = (AuthoringForm) form;
	updateAuthForm(authForm, mindmap);

	// Set up sessionMap
	SessionMap<String, Object> map = createSessionMap(mindmap, mode, contentFolderID,
		toolContentID);
	authForm.setSessionMapID(map.getSessionID());

	// add the sessionMap to HTTPSession.
	request.getSession().setAttribute(map.getSessionID(), map);
	request.setAttribute(MindmapConstants.ATTR_SESSION_MAP, map);

	return mapping.findForward("success");
    }

    /**
     * Returns the serialized XML of the Mindmap Nodes from Database
     */
    public ActionForward setMindmapContentJSON(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws JSONException, IOException {

	// set up mindmapService
	if (mindmapService == null) {
	    mindmapService = MindmapServiceProxy.getMindmapService(this.getServlet().getServletContext());
	}

	Long mindmapId = WebUtil.readLongParam(request, "mindmapId", false);
	List mindmapNodeList = mindmapService.getAuthorRootNodeByMindmapId(mindmapId);

	if (mindmapNodeList != null && mindmapNodeList.size() > 0) {
	    MindmapNode rootMindmapNode = (MindmapNode) mindmapNodeList.get(0);

	    String rootMindmapUser = mindmapService.getMindmapMessageService().getMessage("node.instructor.label");

	    NodeModel rootNodeModel = new NodeModel(new NodeConceptModel(rootMindmapNode.getUniqueId(),
		    rootMindmapNode.getText(), rootMindmapNode.getColor(), rootMindmapUser, 1));
	    NodeModel currentNodeModel = mindmapService.getMindmapXMLFromDatabase(rootMindmapNode.getNodeId(),
		    mindmapId, rootNodeModel, null, false, true, false);

	    JSONObject jsonObject = new JSONObject();
	    jsonObject.put("mindmap", new RootJSON(currentNodeModel));

	    response.setContentType("application/x-json;charset=utf-8");
	    response.getWriter().print(jsonObject.toString());
	    return null;
	}

	return null;
    }

    /**
     * Saves Mindmap Nodes to Database
     */
    public ActionForward updateContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws JSONException {
	// get authForm and session map.
	AuthoringForm authForm = (AuthoringForm) form;
	SessionMap<String, Object> map = getSessionMap(request, authForm);

	// get mindmap content.
	Mindmap mindmap = mindmapService.getMindmapByContentId((Long) map.get(AuthoringAction.KEY_TOOL_CONTENT_ID));

	// update mindmap content using form inputs
	updateMindmap(mindmap, authForm);

	// set the update date
	mindmap.setUpdateDate(new Date());

	// releasing defineLater flag so that learner can start using the tool.
	mindmap.setDefineLater(false);

	mindmapService.saveOrUpdateMindmap(mindmap);

	request.setAttribute(AuthoringConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);

	// add the sessionMapID to form
	authForm.setSessionMapID(map.getSessionID());

	request.setAttribute(MindmapConstants.ATTR_SESSION_MAP, map);

	/* Saving Minmdap Nodes */
	MindmapUser mindmapUser = mindmapService.getUserByUID(mindmap.getCreateBy());
	String mindmapContent = authForm.getMindmapContent();

	NodeModel rootNodeModel = RootJSON.toNodeModel(mindmapContent);
	if ( rootNodeModel == null ) {
	    String error = new StringBuilder("Unable to save mindmap for authoring. User:")
		    .append(mindmapUser.getLoginName())
		    .append("("+mindmapUser.getUserId())
		    .append("). No root node. JSON: ")
		    .append(mindmapContent)
		    .toString();
	    throw new JSONException(error);
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

	return mapping.findForward("success");
    }

    /**
     * Updates Mindmap content using AuthoringForm inputs.
     *
     * @param mindmap
     * @param authForm
     * @param mode
     */
    private void updateMindmap(Mindmap mindmap, AuthoringForm authForm) {
	mindmap.setTitle(authForm.getTitle());
	mindmap.setInstructions(authForm.getInstructions());
	mindmap.setLockOnFinished(authForm.isLockOnFinished());
	mindmap.setMultiUserMode(authForm.isMultiUserMode());
	// reflection
	mindmap.setReflectOnActivity(authForm.isReflectOnActivity());
	mindmap.setReflectInstructions(authForm.getReflectInstructions());
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

	return map;
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
