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
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.tool.mindmap.dto.MindmapDTO;
import org.lamsfoundation.lams.tool.mindmap.dto.MindmapSessionDTO;
import org.lamsfoundation.lams.tool.mindmap.dto.MindmapUserDTO;
import org.lamsfoundation.lams.tool.mindmap.model.Mindmap;
import org.lamsfoundation.lams.tool.mindmap.model.MindmapNode;
import org.lamsfoundation.lams.tool.mindmap.model.MindmapUser;
import org.lamsfoundation.lams.tool.mindmap.service.IMindmapService;
import org.lamsfoundation.lams.tool.mindmap.service.MindmapServiceProxy;
import org.lamsfoundation.lams.tool.mindmap.util.xmlmodel.NodeConceptModel;
import org.lamsfoundation.lams.tool.mindmap.util.xmlmodel.NodeModel;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.AttributeNames;
import com.thoughtworks.xstream.XStream;

/**
 * @author Ruslan Kazakov
 * @version 1.0.1
 * 
 * @struts.action path="/monitoring" parameter="dispatch" scope="request" name="monitoringForm" validate="false"
 * @struts.action-forward name="success" path="tiles:/monitoring/main"
 * @struts.action-forward name="mindmap_display" path="tiles:/monitoring/mindmap_display"
 * @struts.action-forward name="reflect" path="tiles:/monitoring/reflect"
 */
public class MonitoringAction extends LamsDispatchAction {

    private static Logger log = Logger.getLogger(MonitoringAction.class);
    public IMindmapService mindmapService;

    /**
     * Default action on page load
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return null
     */
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	setupService();
	Long toolContentID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	Mindmap mindmap = mindmapService.getMindmapByContentId(toolContentID);
	
	if (mindmap == null) {
	    log.error("unspecified(): Mindmap is not found!");
	    return null;
	}

	boolean isGroupedActivity = mindmapService.isGroupedActivity(toolContentID);
	MindmapDTO mindmapDTO = new MindmapDTO(mindmap);

	Long currentTab = WebUtil.readLongParam(request, AttributeNames.PARAM_CURRENT_TAB, true);
	mindmapDTO.setCurrentTab(currentTab);

	Object[] users = ((MindmapSessionDTO) mindmapDTO.getSessionDTOs().toArray()[0]).getUserDTOs().toArray();
	if (users != null && users.length > 0)
	    request.setAttribute("mindmapUser", ((MindmapUserDTO) users[0]));
	
	request.setAttribute("mindmapDTO", mindmapDTO);
	request.setAttribute("contentFolderID", contentFolderID);
	request.setAttribute("isGroupedActivity", isGroupedActivity);

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
	Long userId = WebUtil.readLongParam(request, "userId", false);
	Mindmap mindmap = mindmapService.getMindmapByUid(mindmapId);

	List mindmapNodeList = null;
	if (mindmap.isMultiUserMode()) // is multi-user
	    mindmapNodeList = mindmapService.getAuthorRootNodeByMindmapIdMulti(mindmapId);
	else
	    mindmapNodeList = mindmapService.getRootNodeByMindmapIdAndUserId(mindmapId, userId);

	if (mindmapNodeList != null && mindmapNodeList.size() > 0) {
	    MindmapNode rootMindmapNode = (MindmapNode) mindmapNodeList.get(0);

	    String mindmapUserName = null;
	    if (rootMindmapNode.getUser() == null)
		mindmapUserName = mindmapService.getMindmapMessageService().getMessage("node.learner.label");
	    else
		mindmapUserName = rootMindmapNode.getUser().getFirstName() + " "
			+ rootMindmapNode.getUser().getLastName();

	    NodeModel rootNodeModel = new NodeModel(new NodeConceptModel(rootMindmapNode.getUniqueId(), rootMindmapNode
		    .getText(), rootMindmapNode.getColor(), mindmapUserName));
	    NodeModel currentNodeModel = mindmapService.getMindmapXMLFromDatabase(rootMindmapNode.getNodeId(),
		    mindmapId, rootNodeModel, null);
	    XStream xstream = new XStream();
	    xstream.alias("branch", NodeModel.class);
	    String mindmapContent = xstream.toXML(currentNodeModel);

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
	    // response.setCharacterEncoding("utf-8");
	    response.getWriter().write(mindmapService.getLanguageXML());
	} catch (IOException e) {
	    e.printStackTrace();
	}

	return null;
    }

    /**
     * Shows Mindmap Nodes for each learner
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return null
     */
    public ActionForward showMindmap(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	setupService();
	
	Long userId = new Long(WebUtil.readLongParam(request, "userUID"));
	Long toolContentID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	Mindmap mindmap = mindmapService.getMindmapByContentId(toolContentID);
	MindmapUser mindmapUser = mindmapService.getUserByUID(userId);

	MindmapUserDTO userDTO = new MindmapUserDTO(mindmapUser);
	request.setAttribute("userDTO", userDTO);

	// mindmapType Parameter
	String mindmapType = "images/mindmap_singleuser.swf";
	request.setAttribute("mindmapType", mindmapType);

	// Mindmap path Parameter
	String mindmapContentPath = Configuration.get(ConfigurationKeys.SERVER_URL)
		+ "tool/lamind10/monitoring.do?dispatch=setMindmapContent%26mindmapId=" + mindmap.getUid()
		+ "%26userId=" + userId;
	request.setAttribute("mindmapContentPath", mindmapContentPath);

	// Current user Parameter
	String currentMindmapUser = mindmapService.getMindmapMessageService().getMessage("node.instructor.label");
	request.setAttribute("currentMindmapUser", currentMindmapUser);

	String localizationPath = Configuration.get(ConfigurationKeys.SERVER_URL)
		+ "tool/lamind10/monitoring.do?dispatch=setLocale";
	request.setAttribute("localizationPath", localizationPath);

	request.setAttribute("toolContentID", toolContentID);
	request.setAttribute("contentFolderID", contentFolderID);
	request.setAttribute("isMultiUserMode", mindmap.isMultiUserMode());
	
	// Attributes for AJAX calls while saving Mindmap
	request.setAttribute("get", Configuration.get(ConfigurationKeys.SERVER_URL) + "tool/lamind10/monitoring.do");
	request.setAttribute("dispatch", "updateContent");
	request.setAttribute("mindmapId", mindmap.getUid());
	request.setAttribute("userId", mindmapUser.getUid());

	return mapping.findForward("mindmap_display");
    }
    
    /**
     * Shows Notebook reflection that Learner has done.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward reflect(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Long userId = WebUtil.readLongParam(request, "userUID", false);
	Long toolContentId = WebUtil.readLongParam(request, "toolContentID", false);
	MindmapUser mindmapUser = mindmapService.getUserByUID(userId);
	Mindmap mindmap = mindmapService.getMindmapByContentId(toolContentId);
	
	request.setAttribute("reflectTitle", mindmap.getTitle());
	request.setAttribute("mindmapUser", mindmapUser.getFirstName() + " " + mindmapUser.getLastName());
	
	// Reflection
	NotebookEntry entry = mindmapService.getEntry(mindmapUser.getEntryUID());
	if (entry != null) {
	    request.setAttribute("reflectEntry", entry.getEntry());
	}

	return mapping.findForward("reflect");
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
	
	Long userId = WebUtil.readLongParam(request, "userId", false);
	Long toolContentId = WebUtil.readLongParam(request, "mindmapId", false);
	String mindmapContent = WebUtil.readStrParam(request, "content", false);
	
	Mindmap mindmap = mindmapService.getMindmapByUid(toolContentId);
	MindmapUser mindmapUser = mindmapService.getUserByUID(userId);
	
	if (!mindmap.isMultiUserMode()) {
	    // Saving Mindmap data to XML
	    XStream xstream = new XStream();
	    xstream.alias("branch", NodeModel.class);
	    NodeModel rootNodeModel = (NodeModel) xstream.fromXML(mindmapContent);
	    NodeConceptModel nodeConceptModel = rootNodeModel.getConcept();
	    List<NodeModel> branches = rootNodeModel.getBranch();

	    // saving root Node into database
	    MindmapNode rootMindmapNode = (MindmapNode) mindmapService.getRootNodeByMindmapIdAndUserId(
		    mindmap.getUid(), userId).get(0);
	    rootMindmapNode = mindmapService.saveMindmapNode(rootMindmapNode, null, nodeConceptModel.getId(),
		    nodeConceptModel.getText(), nodeConceptModel.getColor(), mindmapUser, mindmap);

	    // string to accumulate deleted nodes for query
	    String nodesToDeleteCondition = " where uniqueId <> " + rootMindmapNode.getUniqueId();

	    // saving child Nodes into database
	    if (branches != null) {
		mindmapService.setNodesToDeleteCondition("");
		mindmapService.getChildMindmapNodes(branches, rootMindmapNode, mindmapUser, mindmap);
	    }

	    nodesToDeleteCondition += mindmapService.getNodesToDeleteCondition() + " and mindmap_id = "
		    + mindmap.getUid() + " and user_id = " + mindmapUser.getUid();
	    mindmapService.deleteNodes(nodesToDeleteCondition);
	}

	return null;
    }

    /**
     * Sets mindmapService
     */
    private void setupService() {
	if (mindmapService == null) {
	    mindmapService = MindmapServiceProxy.getMindmapService(this.getServlet().getServletContext());
	}
    }
}
