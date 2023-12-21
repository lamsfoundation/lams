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

package org.lamsfoundation.lams.tool.mindmap.service;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.thoughtworks.xstream.security.AnyTypePermission;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.confidencelevel.ConfidenceLevelDTO;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.logevent.service.ILogEventService;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
import org.lamsfoundation.lams.rating.dto.ItemRatingDTO;
import org.lamsfoundation.lams.rating.model.RatingCriteria;
import org.lamsfoundation.lams.rating.model.ToolActivityRatingCriteria;
import org.lamsfoundation.lams.rating.service.IRatingService;
import org.lamsfoundation.lams.rest.RestTags;
import org.lamsfoundation.lams.rest.ToolRestManager;
import org.lamsfoundation.lams.tool.ToolCompletionStatus;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.SessionDataExistsException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.mindmap.MindmapConstants;
import org.lamsfoundation.lams.tool.mindmap.dao.IMindmapDAO;
import org.lamsfoundation.lams.tool.mindmap.dao.IMindmapNodeDAO;
import org.lamsfoundation.lams.tool.mindmap.dao.IMindmapRequestDAO;
import org.lamsfoundation.lams.tool.mindmap.dao.IMindmapSessionDAO;
import org.lamsfoundation.lams.tool.mindmap.dao.IMindmapUserDAO;
import org.lamsfoundation.lams.tool.mindmap.dto.MindmapDTO;
import org.lamsfoundation.lams.tool.mindmap.dto.MindmapSessionDTO;
import org.lamsfoundation.lams.tool.mindmap.model.Mindmap;
import org.lamsfoundation.lams.tool.mindmap.model.MindmapNode;
import org.lamsfoundation.lams.tool.mindmap.model.MindmapRequest;
import org.lamsfoundation.lams.tool.mindmap.model.MindmapSession;
import org.lamsfoundation.lams.tool.mindmap.model.MindmapUser;
import org.lamsfoundation.lams.tool.mindmap.util.MindmapException;
import org.lamsfoundation.lams.tool.mindmap.util.xmlmodel.NodeConceptModel;
import org.lamsfoundation.lams.tool.mindmap.util.xmlmodel.NodeModel;
import org.lamsfoundation.lams.tool.mindmap.util.xmlmodel.NotifyRequestModel;
import org.lamsfoundation.lams.tool.mindmap.util.xmlmodel.NotifyResponseModel;
import org.lamsfoundation.lams.tool.mindmap.util.xmlmodel.PollResponseModel;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.JsonUtil;
import org.lamsfoundation.lams.util.MessageService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * An implementation of the IMindmapService interface. As a requirement, all LAMS tool's service bean must implement
 * ToolContentManager and ToolSessionManager.
 */
public class MindmapService implements ToolSessionManager, ToolContentManager, IMindmapService, ToolRestManager {

    private static final int NODE_TEXT_LENGTH = 100; // node_text column in the database is varchar(100)
    private static Logger logger = Logger.getLogger(MindmapService.class.getName());

    private final XStream xstream = new XStream(new StaxDriver());

    private IMindmapDAO mindmapDAO = null;
    private IMindmapSessionDAO mindmapSessionDAO = null;
    private IMindmapUserDAO mindmapUserDAO = null;
    private IMindmapNodeDAO mindmapNodeDAO = null;
    private IMindmapRequestDAO mindmapRequestDAO = null;
    private ILamsToolService toolService;
    private IToolContentHandler mindmapToolContentHandler = null;
    private ILogEventService logEventService = null;
    private IExportToolContentService exportContentService;
    private ICoreNotebookService coreNotebookService;
    private ILearnerService learnerService;
    private IRatingService ratingService;
    private MindmapOutputFactory mindmapOutputFactory;
    private String nodesToDeleteCondition = null; // string to accumulate nodes to delete
    private MessageService mindmapMessageService;

    public MindmapService() {
	super();

	xstream.addPermission(AnyTypePermission.ANY);
	xstream.alias("branch", NodeModel.class);
	xstream.alias("response", NotifyResponseModel.class);
	xstream.alias("action", NotifyRequestModel.class);
	xstream.alias("pollResponse", PollResponseModel.class);
    }

    /* Methods from ToolSessionManager */
    @Override
    public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException {
	if (MindmapService.logger.isDebugEnabled()) {
	    MindmapService.logger.debug(
		    "entering method createToolSession:" + " toolSessionId = " + toolSessionId + " toolSessionName = "
			    + toolSessionName + " toolContentId = " + toolContentId);
	}

	MindmapSession session = new MindmapSession();
	session.setSessionId(toolSessionId);
	session.setSessionName(toolSessionName);
	// learner starts
	// TODO need to also set other fields.
	Mindmap mindmap = mindmapDAO.getByContentId(toolContentId);
	session.setMindmap(mindmap);
	mindmapSessionDAO.saveOrUpdate(session);
    }

    @Override
    public String leaveToolSession(Long toolSessionId, Long learnerId) throws DataMissingException, ToolException {
	return toolService.completeToolSession(toolSessionId, learnerId);
    }

    @Override
    public ToolSessionExportOutputData exportToolSession(Long toolSessionId)
	    throws DataMissingException, ToolException {
	return null;
    }

    @Override
    public ToolSessionExportOutputData exportToolSession(List toolSessionIds)
	    throws DataMissingException, ToolException {
	return null;
    }

    @Override
    public void removeToolSession(Long toolSessionId) throws DataMissingException, ToolException {
	mindmapSessionDAO.deleteBySessionID(toolSessionId);
	// TODO check if cascade worked
    }

    @Override
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, Long toolSessionId, Long learnerId) {
	return getMindmapOutputFactory().getToolOutput(names, this, toolSessionId, learnerId);
    }

    @Override
    public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
	return getMindmapOutputFactory().getToolOutput(name, this, toolSessionId, learnerId);
    }

    @Override
    public List<ToolOutput> getToolOutputs(String name, Long toolContentId) {
	return new ArrayList<>();
    }

    @Override
    public List<ConfidenceLevelDTO> getConfidenceLevels(Long toolSessionId) {
	return null;
    }

    @Override
    public boolean isUserGroupLeader(Long userId, Long toolSessionId) {
	return false;
    }

    @Override
    public void forceCompleteUser(Long toolSessionId, User user) {
	//no actions required
    }

    /**
     * Get number of nodes created by particular user
     *
     * @param learnerId
     * @param toolSessionId
     * @return
     */
    @Override
    public int getNumNodes(Long learnerId, Long toolSessionId) {
	MindmapUser mindmapUser = getUserByUserIdAndSessionId(learnerId, toolSessionId);
	// MindmapSession mindmapSession = getSessionBySessionId(toolSessionId);

	return mindmapNodeDAO.getNumNodesByUserAndSession(mindmapUser.getUid(), toolSessionId);
    }

    /*
     * Methods from ToolContentManager Called on adding lesson.
     */
    @Override
    public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException {

	if (MindmapService.logger.isDebugEnabled()) {
	    MindmapService.logger.debug(
		    "entering method copyToolContent:" + " fromContentId=" + fromContentId + " toContentId="
			    + toContentId);
	}

	if (toContentId == null) {
	    String error = "Failed to copy tool content: toContentID is null";
	    throw new ToolException(error);
	}

	Mindmap fromContent = null;
	if (fromContentId != null) {
	    fromContent = mindmapDAO.getByContentId(fromContentId);
	}
	if (fromContent == null) {
	    // create the fromContent using the default tool content
	    fromContent = getDefaultContent();
	}

	Mindmap toContent = Mindmap.newInstance(fromContent, toContentId);
	mindmapDAO.saveOrUpdate(toContent);

	if (toContent.isGalleryWalkEnabled() && !toContent.isGalleryWalkReadOnly()) {
	    createGalleryWalkRatingCriterion(toContentId);
	}

	/* Copying Mindmap Nodes */

	// creating default nodes for current mindmap
	String rootNodeName = getMindmapMessageService().getMessage("node.root.defaultName");
	String childNodeName1 = getMindmapMessageService().getMessage("node.child1.defaultName");
	String childNodeName2 = getMindmapMessageService().getMessage("node.child2.defaultName");

	List rootNode = getAuthorRootNodeByMindmapId(fromContent.getUid());

	MindmapNode rootMindmapNode = null;
	if ((rootNode == null) || (rootNode.size() == 0)) {
	    // Create default content
	    rootMindmapNode = saveMindmapNode(null, null, 1l, rootNodeName, "ffffff", null, fromContent, null);
	    saveOrUpdateMindmapNode(rootMindmapNode);
	    saveMindmapNode(null, rootMindmapNode, 2l, childNodeName1, "ffffff", null, fromContent, null);
	    saveMindmapNode(null, rootMindmapNode, 3l, childNodeName2, "ffffff", null, fromContent, null);
	} else {
	    rootMindmapNode = (MindmapNode) getAuthorRootNodeByMindmapId(fromContent.getUid()).get(0);
	}

	// MindmapNode fromMindmapNode = (MindmapNode) getAuthorRootNodeByMindmapId(fromContent.getUid()).get(0);
	cloneMindmapNodesForRuntime(rootMindmapNode, null, fromContent, toContent);

    }

    /**
     * Makes a runtime copy of Mindmap Nodes for single-user mode. Makes a shared copy of Mindmap Nodes for multi-user
     * mode.
     *
     * @param fromMindmapNode
     * @param toMindmapNode
     * @param fromContent
     * @param toContent
     * @return
     */
    public void cloneMindmapNodesForRuntime(MindmapNode fromMindmapNode, MindmapNode toMindmapNode, Mindmap fromContent,
	    Mindmap toContent) {
	toMindmapNode = saveMindmapNode(null, toMindmapNode, fromMindmapNode.getUniqueId(), fromMindmapNode.getText(),
		fromMindmapNode.getColor(), fromMindmapNode.getUser(), toContent, null);

	List childMindmapNodes = getMindmapNodeByParentId(fromMindmapNode.getNodeId(), fromContent.getUid());

	if ((childMindmapNodes != null) && (childMindmapNodes.size() > 0)) {
	    for (Iterator iterator = childMindmapNodes.iterator(); iterator.hasNext(); ) {
		MindmapNode childMindmapNode = (MindmapNode) iterator.next();
		cloneMindmapNodesForRuntime(childMindmapNode, toMindmapNode, fromContent, toContent);
	    }
	}
    }

    /**
     * Saves Mindmap Node to database.
     *
     * @param currentMindmapNode
     * @param parentMindmapNode
     * @param uniqueId
     * @param text
     * @param color
     * @param mindmapUser
     * @param mindmap
     * @return null
     */
    @Override
    public MindmapNode saveMindmapNode(MindmapNode currentMindmapNode, MindmapNode parentMindmapNode, Long uniqueId,
	    String text, String color, MindmapUser mindmapUser, Mindmap mindmap, MindmapSession session) {
	if (currentMindmapNode == null) {
	    currentMindmapNode = new MindmapNode();
	}
	currentMindmapNode.setParent(parentMindmapNode);
	currentMindmapNode.setUniqueId(uniqueId);
	currentMindmapNode.setText(text);
	currentMindmapNode.setColor(color);
	currentMindmapNode.setSession(session);
	currentMindmapNode.setUser(mindmapUser);
	currentMindmapNode.setMindmap(mindmap);
	saveOrUpdateMindmapNode(currentMindmapNode);

	return currentMindmapNode;
    }

    /**
     * Makes a runtime copy of Mindmap Nodes for single-user mode.
     *
     * @param rootNodeId
     * @param mindmapId
     * @param rootNodeModel
     * @param mindmapUser
     * @return rootNodeModel
     */
    @Override
    public NodeModel getMindmapXMLFromDatabase(Long rootNodeId, Long mindmapId, NodeModel rootNodeModel,
	    MindmapUser mindmapUser, boolean isMonitor, boolean isAuthor, boolean isUserLocked) {
	List mindmapNodes = getMindmapNodeByParentId(rootNodeId, mindmapId);

	if ((mindmapNodes != null) && (mindmapNodes.size() > 0)) {
	    for (Iterator iterator = mindmapNodes.iterator(); iterator.hasNext(); ) {
		MindmapNode mindmapNode = (MindmapNode) iterator.next();

		String mindmapUserName = null;
		if (mindmapNode.getUser() == null) {
		    mindmapUserName = getMindmapMessageService().getMessage("node.instructor.label");
		} else {
		    mindmapUserName = mindmapNode.getUser().getFirstName() + " " + mindmapNode.getUser().getLastName();
		}

		int edit;
		if (isAuthor) {
		    edit = 1;
		} else if (isMonitor || isUserLocked || mindmapUser == null) {
		    edit = 0;
		} else {
		    edit = mindmapUser.equals(mindmapNode.getUser()) ? 1 : 0;
		}

		NodeModel nodeModel = new NodeModel(
			new NodeConceptModel(mindmapNode.getUniqueId(), mindmapNode.getText(), mindmapNode.getColor(),
				mindmapUserName, edit));

		rootNodeModel.addNode(nodeModel);
		getMindmapXMLFromDatabase(mindmapNode.getNodeId(), mindmapId, nodeModel, mindmapUser, isMonitor,
			isAuthor, isUserLocked);
	    }
	}

	return rootNodeModel;
    }

    @Override
    public void getChildMindmapNodes(List<NodeModel> branches, MindmapNode rootMindmapNode, MindmapUser mindmapUser,
	    Mindmap mindmap, MindmapSession mindmapSession) {
	for (Iterator<NodeModel> iterator = branches.iterator(); iterator.hasNext(); ) {
	    NodeModel nodeModel = iterator.next();
	    NodeConceptModel nodeConceptModel = nodeModel.getConcept();
	    // saving branch
	    List curMindmapNodeList = null;
	    if (mindmap.getUid() != null) {
		if (mindmapUser == null) {
		    curMindmapNodeList = getMindmapNodeByUniqueId(nodeConceptModel.getId(), mindmap.getUid());
		} else {
		    curMindmapNodeList = getMindmapNodeByUniqueIdMindmapIdUserId(nodeConceptModel.getId(),
			    mindmap.getUid(), mindmapUser.getUid());
		}
	    }

	    MindmapNode currentMindmapNode = null;
	    if ((curMindmapNodeList != null) && (curMindmapNodeList.size() > 0)) {
		currentMindmapNode = (MindmapNode) curMindmapNodeList.get(0);
	    }
	    nodesToDeleteCondition += " and uniqueId <> " + nodeConceptModel.getId();
	    currentMindmapNode = saveMindmapNode(currentMindmapNode, rootMindmapNode, nodeConceptModel.getId(),
		    nodeConceptModel.getText(), nodeConceptModel.getColor(), mindmapUser, mindmap, mindmapSession);
	    // if there are child nodes, redo for every child
	    if (nodeModel.getBranch() != null) {
		getChildMindmapNodes(nodeModel.getBranch(), currentMindmapNode, mindmapUser, mindmap, mindmapSession);
	    }
	}
    }

    @Override
    public void resetDefineLater(Long toolContentId) throws DataMissingException, ToolException {
	Mindmap mindmap = mindmapDAO.getByContentId(toolContentId);
	if (mindmap == null) {
	    throw new ToolException("Could not find tool with toolContentID: " + toolContentId);
	}
	mindmap.setDefineLater(false);
	mindmapDAO.saveOrUpdate(mindmap);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void removeToolContent(Long toolContentId) throws SessionDataExistsException, ToolException {
	Mindmap mindmap = mindmapDAO.getByContentId(toolContentId);
	if (mindmap == null) {
	    MindmapService.logger.warn("Can not remove the tool content as it does not exist, ID: " + toolContentId);
	    return;
	}

	for (MindmapSession session : mindmap.getMindmapSessions()) {
	    List<NotebookEntry> entries = coreNotebookService.getEntry(session.getSessionId(),
		    CoreNotebookConstants.NOTEBOOK_TOOL, MindmapConstants.TOOL_SIGNATURE);
	    for (NotebookEntry entry : entries) {
		coreNotebookService.deleteEntry(entry);
	    }
	}

	mindmapDAO.delete(mindmap);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void removeLearnerContent(Long toolContentId, Integer userId, boolean resetActivityCompletionOnly)
	    throws ToolException {
	if (logger.isDebugEnabled()) {
	    if (resetActivityCompletionOnly) {
		logger.debug(
			"Resetting Mindmap completion for user ID " + userId + " and toolContentId " + toolContentId);
	    } else {
		logger.debug("Removing Mindmap content for user ID " + userId + " and toolContentId " + toolContentId);
	    }
	}

	Mindmap mindmap = mindmapDAO.getByContentId(toolContentId);
	if (mindmap == null) {
	    MindmapService.logger.warn(
		    "Did not find activity with toolContentId: " + toolContentId + " to remove learner content");
	    return;
	}

	if (!resetActivityCompletionOnly) {
	    List<MindmapNode> nodesToDelete = new LinkedList<>();
	    for (MindmapSession session : mindmap.getMindmapSessions()) {
		List<MindmapNode> nodes = mindmapNodeDAO.getMindmapNodesBySessionIdAndUserId(session.getSessionId(),
			userId.longValue());

		for (MindmapNode node : nodes) {
		    List<MindmapNode> descendants = new LinkedList<>();
		    if ((node.getUser() != null) && node.getUser().getUserId().equals(userId.longValue())
			    && !nodesToDelete.contains(node) && userOwnsChildrenNodes(node, userId.longValue(),
			    descendants)) {
			// reverse so leafs are first and nodes closer to root are last
			descendants.add(node);
			nodesToDelete.addAll(descendants);
		    }
		}
	    }

	    for (MindmapNode node : nodesToDelete) {
		mindmapNodeDAO.delete(node);
	    }
	}
	for (MindmapSession session : mindmap.getMindmapSessions()) {
	    MindmapUser user = mindmapUserDAO.getByUserIdAndSessionId(userId.longValue(), session.getSessionId());
	    if (user != null) {
		if (!resetActivityCompletionOnly) {
		    if (user.getEntryUID() != null) {
			NotebookEntry entry = coreNotebookService.getEntry(user.getEntryUID());
			mindmapDAO.delete(entry);
		    }

		    user.setEntryUID(null);
		}
		user.setFinishedActivity(false);
		mindmapDAO.update(user);
	    }
	}
    }

    @SuppressWarnings("unchecked")
    private boolean userOwnsChildrenNodes(MindmapNode node, Long userId, List<MindmapNode> descendants) {
	List<MindmapNode> children = mindmapNodeDAO.getMindmapNodeByParentIdMindmapIdSessionId(node.getNodeId(),
		node.getMindmap().getUid(), node.getSession().getSessionId());
	for (MindmapNode child : children) {
	    boolean userOwnsChild =
		    (child.getUser() != null) && child.getUser().getUserId().equals(userId) && userOwnsChildrenNodes(
			    child, userId, descendants);
	    if (!userOwnsChild) {
		return false;
	    }
	    descendants.add(child);
	}
	return true;
    }

    /**
     * Export the XML fragment for the tool's content, along with any files needed for the content.
     *
     * @throws DataMissingException
     * 	if no tool content matches the toolSessionId
     * @throws ToolException
     * 	if any other error occurs
     */
    @Override
    public void exportToolContent(Long toolContentId, String rootPath) throws DataMissingException, ToolException {
	Mindmap mindmap = mindmapDAO.getByContentId(toolContentId);
	if (mindmap == null) {
	    mindmap = getDefaultContent();
	}
	if (mindmap == null) {
	    throw new DataMissingException("Unable to find default content for the mindmap tool");
	}

	// generating Mindmap XML to export
	String mindmapContent = null;
	List mindmapNodeList = getAuthorRootNodeByMindmapId(mindmap.getUid());
	if ((mindmapNodeList != null) && (mindmapNodeList.size() > 0)) {
	    MindmapNode rootMindmapNode = (MindmapNode) mindmapNodeList.get(0);

	    String rootMindmapUser = getMindmapMessageService().getMessage("node.instructor.label");

	    NodeModel rootNodeModel = new NodeModel(
		    new NodeConceptModel(rootMindmapNode.getUniqueId(), rootMindmapNode.getText(),
			    rootMindmapNode.getColor(), rootMindmapUser, 1));
	    NodeModel currentNodeModel = getMindmapXMLFromDatabase(rootMindmapNode.getNodeId(), mindmap.getUid(),
		    rootNodeModel, null, false, false, false);

	    mindmapContent = xstream.toXML(currentNodeModel);
	}

	mindmap = Mindmap.newInstance(mindmap, toolContentId);

	mindmap.setMindmapExportContent(mindmapContent);
	mindmap.setMindmapSessions(null);

	try {
	    exportContentService.exportToolContent(toolContentId, mindmap, mindmapToolContentHandler, rootPath);
	} catch (ExportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    /**
     * Import the XML fragment for the tool's content, along with any files needed for the content.
     *
     * @throws ToolException
     * 	if any other error occurs
     */
    @Override
    public void importToolContent(Long toolContentId, Integer newUserUid, String toolContentPath, String fromVersion,
	    String toVersion) throws ToolException {
	try {
	    // register version filter class
	    exportContentService.registerImportVersionFilterClass(MindmapImportContentVersionFilter.class);

	    Object toolPOJO = exportContentService.importToolContent(toolContentPath, mindmapToolContentHandler,
		    fromVersion, toVersion);
	    if (!(toolPOJO instanceof Mindmap)) {
		throw new ImportToolContentException(
			"Import Mindmap tool content failed. Deserialized object is " + toolPOJO);
	    }

	    Mindmap mindmap = (Mindmap) toolPOJO;

	    String mindmapContent = mindmap.getMindmapExportContent();
	    if (mindmapContent != null) {
		MindmapUser mindmapUser = null;

		NodeModel rootNodeModel = (NodeModel) xstream.fromXML(mindmapContent);
		NodeConceptModel nodeConceptModel = rootNodeModel.getConcept();
		List<NodeModel> branches = rootNodeModel.getBranch();

		MindmapNode rootMindmapNode = null;
		rootMindmapNode = saveMindmapNode(rootMindmapNode, null, nodeConceptModel.getId(),
			nodeConceptModel.getText(), nodeConceptModel.getColor(), mindmapUser, mindmap, null);

		// saving child Nodes into database
		if (branches != null) {
		    getChildMindmapNodes(branches, rootMindmapNode, mindmapUser, mindmap, null);
		}
	    }

	    // reset it to new toolContentId
	    mindmap.setToolContentId(toolContentId);
	    // mindmap.setCreateBy(new Long(newUserUid.longValue()));

	    mindmapDAO.saveOrUpdate(mindmap);

	} catch (ImportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    /**
     * Get the definitions for possible output for an activity, based on the toolContentId. These may be definitions
     * that are always available for the tool (e.g. number of marks for Multiple Choice) or a custom definition created
     * for a particular activity such as the answer to the third question contains the word Koala and hence the need for
     * the toolContentId
     *
     * @return SortedMap of ToolOutputDefinitions with the key being the name of each definition
     */
    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Long toolContentId, int definitionType)
	    throws ToolException {
	Mindmap mindmap = getMindmapDAO().getByContentId(toolContentId);
	if (mindmap == null) {
	    mindmap = getDefaultContent();
	}
	return getMindmapOutputFactory().getToolOutputDefinitions(mindmap, definitionType);
    }

    @Override
    public String getToolContentTitle(Long toolContentId) {
	return getMindmapByContentId(toolContentId).getTitle();
    }

    @Override
    public boolean isContentEdited(Long toolContentId) {
	return getMindmapByContentId(toolContentId).isDefineLater();
    }

    @Override
    public boolean isReadOnly(Long toolContentId) {
	Mindmap mindmap = mindmapDAO.getByContentId(toolContentId);

	for (MindmapSession session : mindmap.getMindmapSessions()) {
	    for (MindmapUser user : session.getMindmapUsers()) {
		if (!mindmapNodeDAO.getMindmapNodesBySessionIdAndUserId(session.getSessionId(), user.getUserId())
			.isEmpty()) {
		    return true;
		}
	    }

	}
	return false;
    }

    /* IMindmapService Methods */

    @Override
    public Long createNotebookEntry(Long id, Integer idType, String signature, Integer userID, String entry) {
	return coreNotebookService.createNotebookEntry(id, idType, signature, userID, "", entry);
    }

    @Override
    public NotebookEntry getEntry(Long uid) {
	return coreNotebookService.getEntry(uid);
    }

    @Override
    public void updateEntry(Long uid, String entry) {
	coreNotebookService.updateEntry(uid, "", entry);
    }

    @Override
    public void updateEntry(NotebookEntry notebookEntry) {
	coreNotebookService.updateEntry(notebookEntry);
    }

    @Override
    public Long getDefaultContentIdBySignature(String toolSignature) {
	Long toolContentId = null;
	toolContentId = new Long(toolService.getToolDefaultContentIdBySignature(toolSignature));
	if (toolContentId == null) {
	    String error = "Could not retrieve default content id for this tool";
	    MindmapService.logger.error(error);
	    throw new MindmapException(error);
	}
	return toolContentId;
    }

    @Override
    public Mindmap getDefaultContent() {
	Long defaultContentID = getDefaultContentIdBySignature(MindmapConstants.TOOL_SIGNATURE);
	Mindmap defaultContent = getMindmapByContentId(defaultContentID);
	if (defaultContent == null) {
	    String error = "Could not retrieve default content record for this tool";
	    MindmapService.logger.error(error);
	    throw new MindmapException(error);
	}

	return defaultContent;
    }

    @Override
    public Mindmap copyDefaultContent(Long newContentID) {

	if (newContentID == null) {
	    String error = "Cannot copy the Mindmap tools default content: + " + "newContentID is null";
	    MindmapService.logger.error(error);
	    throw new MindmapException(error);
	}

	Mindmap defaultContent = getDefaultContent();
	// create new mindmap using the newContentID
	Mindmap newContent = new Mindmap();
	newContent = Mindmap.newInstance(defaultContent, newContentID);
	mindmapDAO.saveOrUpdate(newContent);
	return newContent;
    }

    @Override
    public Mindmap getMindmapByContentId(Long toolContentID) {
	Mindmap mindmap = mindmapDAO.getByContentId(toolContentID);
	if (mindmap == null) {
	    MindmapService.logger.debug("Could not find the content with toolContentID:" + toolContentID);
	}
	return mindmap;
    }

    @Override
    public Mindmap getMindmapByUid(Long Uid) {
	Mindmap mindmap = mindmapDAO.getMindmapByUid(Uid);
	if (mindmap == null) {
	    MindmapService.logger.debug("Could not find the content with Uid:" + Uid);
	}
	return mindmap;
    }

    @Override
    public MindmapSession getSessionBySessionId(Long toolSessionId) {
	MindmapSession mindmapSession = mindmapSessionDAO.getBySessionId(toolSessionId);
	if (mindmapSession == null) {
	    MindmapService.logger.debug("Could not find the mindmap session with toolSessionID:" + toolSessionId);
	}
	return mindmapSession;
    }

    @Override
    public MindmapUser getUserByUserIdAndSessionId(Long userId, Long toolSessionId) {
	return mindmapUserDAO.getByUserIdAndSessionId(userId, toolSessionId);
    }

    @SuppressWarnings("unchecked")
    @Override
    public MindmapUser getUserByLoginAndSessionId(String login, long toolSessionId) {
	List<User> user = mindmapUserDAO.findByProperty(User.class, "login", login);
	return user.isEmpty()
		? null
		: mindmapUserDAO.getByUserIdAndSessionId(user.get(0).getUserId().longValue(), toolSessionId);
    }

    public MindmapUser getUserByLoginNameAndSessionId(String loginName, Long toolSessionId) {
	return mindmapUserDAO.getByLoginNameAndSessionId(loginName, toolSessionId);
    }

    @Override
    public MindmapUser getUserByUID(Long uid) {
	return mindmapUserDAO.getByUID(uid);
    }

    @Override
    public void saveOrUpdateMindmap(Mindmap mindmap) {
	mindmapDAO.saveOrUpdate(mindmap);
    }

    @Override
    public void saveOrUpdateMindmapRequest(MindmapRequest mindmapRequest) {
	mindmapRequestDAO.saveOrUpdate(mindmapRequest);
    }

    @Override
    public void saveOrUpdateMindmapSession(MindmapSession mindmapSession) {
	mindmapSessionDAO.saveOrUpdate(mindmapSession);
    }

    @Override
    public void saveOrUpdateMindmapUser(MindmapUser mindmapUser) {
	mindmapUserDAO.saveOrUpdate(mindmapUser);
    }

    @Override
    public MindmapUser createMindmapUser(UserDTO user, MindmapSession mindmapSession) {
	MindmapUser mindmapUser = new MindmapUser(user, mindmapSession);
	saveOrUpdateMindmapUser(mindmapUser);
	return mindmapUser;
    }

    public ILogEventService getLogEventService() {
	return logEventService;
    }

    public void setLogEventService(ILogEventService logEventService) {
	this.logEventService = logEventService;
    }

// =========================================================================================
    /* Used by Spring to "inject" the linked objects */

    public IMindmapDAO getMindmapDAO() {
	return mindmapDAO;
    }

    public void setMindmapDAO(IMindmapDAO mindmapDAO) {
	this.mindmapDAO = mindmapDAO;
    }

    public IToolContentHandler getMindmapToolContentHandler() {
	return mindmapToolContentHandler;
    }

    public void setMindmapToolContentHandler(IToolContentHandler mindmapToolContentHandler) {
	this.mindmapToolContentHandler = mindmapToolContentHandler;
    }

    public IMindmapSessionDAO getMindmapSessionDAO() {
	return mindmapSessionDAO;
    }

    public void setMindmapSessionDAO(IMindmapSessionDAO sessionDAO) {
	mindmapSessionDAO = sessionDAO;
    }

    public ILamsToolService getToolService() {
	return toolService;
    }

    public void setToolService(ILamsToolService toolService) {
	this.toolService = toolService;
    }

    public void setRatingService(IRatingService ratingService) {
	this.ratingService = ratingService;
    }

    public void setLearnerService(ILearnerService learnerService) {
	this.learnerService = learnerService;
    }

    public IMindmapUserDAO getMindmapUserDAO() {
	return mindmapUserDAO;
    }

    public void setMindmapUserDAO(IMindmapUserDAO userDAO) {
	mindmapUserDAO = userDAO;
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

    public MindmapOutputFactory getMindmapOutputFactory() {
	return mindmapOutputFactory;
    }

    public void setMindmapOutputFactory(MindmapOutputFactory mindmapOutputFactory) {
	this.mindmapOutputFactory = mindmapOutputFactory;
    }

    @Override
    public boolean isGroupedActivity(long toolContentID) {
	return toolService.isGroupedActivity(toolContentID);
    }

    @Override
    public void auditLogStartEditingActivityInMonitor(long toolContentID) {
	toolService.auditLogStartEditingActivityInMonitor(toolContentID);
    }

    @Override
    public boolean isLastActivity(Long toolSessionId) {
	return toolService.isLastActivity(toolSessionId);
    }

    public void setMindmapNodeDAO(IMindmapNodeDAO mindmapNodeDAO) {
	this.mindmapNodeDAO = mindmapNodeDAO;
    }

    public IMindmapNodeDAO getMindmapNodeDAO() {
	return mindmapNodeDAO;
    }

    @Override
    public void saveOrUpdateMindmapNode(MindmapNode mindmapNode) {
	if (mindmapNode.getText() != null && mindmapNode.getText().length() > NODE_TEXT_LENGTH) {
	    mindmapNode.setText(mindmapNode.getText().substring(0, NODE_TEXT_LENGTH));
	}
	mindmapNodeDAO.saveOrUpdate(mindmapNode);
    }

    @Override
    public List getAuthorRootNodeByMindmapId(Long mindmapId) {
	return mindmapNodeDAO.getAuthorRootNodeByMindmapId(mindmapId);
    }

    @Override
    public List getAuthorRootNodeBySessionId(Long sessionId) {
	return mindmapNodeDAO.getAuthorRootNodeBySessionId(sessionId);
    }

    @Override
    public List getAuthorRootNodeByMindmapSession(Long mindmapId, Long toolSessionId) {
	return mindmapNodeDAO.getAuthorRootNodeByMindmapSession(mindmapId, toolSessionId);
    }

    @Override
    public List getRootNodeByMindmapIdAndUserId(Long mindmapId, Long userId) {
	return mindmapNodeDAO.getRootNodeByMindmapIdAndUserId(mindmapId, userId);
    }

    @Override
    public List getRootNodeByMindmapIdAndSessionId(Long mindmapId, Long sessionId) {
	return mindmapNodeDAO.getRootNodeByMindmapIdAndUserId(mindmapId, sessionId);
    }

    @Override
    public List getMindmapNodeByParentId(Long parentId, Long mindmapId) {
	return mindmapNodeDAO.getMindmapNodeByParentId(parentId, mindmapId);
    }

    @Override
    public List getMindmapNodeByParentIdMindmapIdSessionId(Long parentId, Long mindmapId, Long sessionId) {
	return mindmapNodeDAO.getMindmapNodeByParentIdMindmapIdSessionId(parentId, mindmapId, sessionId);
    }

    @Override
    public List getMindmapNodeByUniqueId(Long uniqueId, Long mindmapId) {
	return mindmapNodeDAO.getMindmapNodeByUniqueId(uniqueId, mindmapId);
    }

    @Override
    public MindmapNode getMindmapNodeByUniqueIdSessionId(Long uniqueId, Long mindmapId, Long sessionId) {
	return mindmapNodeDAO.getMindmapNodeByUniqueIdSessionId(uniqueId, mindmapId, sessionId);
    }

    @Override
    public List getMindmapNodeByUniqueIdMindmapIdUserId(Long uniqueId, Long mindmapId, Long userId) {
	return mindmapNodeDAO.getMindmapNodeByUniqueIdMindmapIdUserId(uniqueId, mindmapId, userId);
    }

    @Override
    public void deleteNodeByUniqueMindmapUser(Long uniqueId, Long mindmapId, Long userId, Long sessionId) {
	mindmapNodeDAO.deleteNodeByUniqueMindmapUser(uniqueId, mindmapId, userId, sessionId);
    }

    @Override
    public void deleteNodes(String nodesToDeleteCondition) {
	mindmapNodeDAO.deleteNodes(nodesToDeleteCondition);
    }

    @Override
    public String getNodesToDeleteCondition() {
	return nodesToDeleteCondition;
    }

    @Override
    public void setNodesToDeleteCondition(String nodesToDeleteCondition) {
	this.nodesToDeleteCondition = nodesToDeleteCondition;
    }

    public void setMindmapRequestDAO(IMindmapRequestDAO mindmapRequestDAO) {
	this.mindmapRequestDAO = mindmapRequestDAO;
    }

    public IMindmapRequestDAO getMindmapRequestDAO() {
	return mindmapRequestDAO;
    }

    @Override
    public List<MindmapRequest> getLastRequestsAfterGlobalId(Long globalId, Long mindmapId, Long sessionId) {
	return mindmapRequestDAO.getLastRequestsAfterGlobalId(globalId, mindmapId, sessionId);
    }

    @Override
    public MindmapRequest getRequestByUniqueId(Long uniqueId, Long userId, Long mindmapId, Long globalId) {
	return mindmapRequestDAO.getRequestByUniqueId(uniqueId, userId, mindmapId, globalId);
    }

    @Override
    public Long getLastGlobalIdByMindmapId(Long mindmapId, Long sessionId) {
	return mindmapRequestDAO.getLastGlobalIdByMindmapId(mindmapId, sessionId);
    }

    @Override
    public Long getNodeLastUniqueIdByMindmapUidSessionId(Long mindmapUid, Long sessionId) {
	return mindmapNodeDAO.getNodeLastUniqueIdByMindmapUidSessionId(mindmapUid, sessionId);
    }

    @Override
    public void setMindmapMessageService(MessageService mindmapMessageService) {
	this.mindmapMessageService = mindmapMessageService;
    }

    @Override
    public MessageService getMindmapMessageService() {
	return mindmapMessageService;
    }

    @Override
    public Class[] getSupportedToolOutputDefinitionClasses(int definitionType) {
	return getMindmapOutputFactory().getSupportedDefinitionClasses(definitionType);
    }

    @Override
    public ToolCompletionStatus getCompletionStatus(Long learnerId, Long toolSessionId) {
	MindmapUser learner = getUserByUserIdAndSessionId(learnerId, toolSessionId);
	if (learner == null) {
	    return new ToolCompletionStatus(ToolCompletionStatus.ACTIVITY_NOT_ATTEMPTED, null, null);
	}

	return new ToolCompletionStatus(learner.isFinishedActivity()
		? ToolCompletionStatus.ACTIVITY_COMPLETED
		: ToolCompletionStatus.ACTIVITY_ATTEMPTED, null, null);
    }
// ****************** REST methods *************************

    /**
     * Used by the Rest calls to create content. Creates default nodes as seen when first opening authoring. Mandatory
     * fields in toolContentJSON: title, instructions Optional fields: multiUserMode (default false), lockWhenFinished
     * (default false), reflectOnActivity (default false), reflectInstructions
     */
    @Override
    public void createRestToolContent(Integer userID, Long toolContentID, ObjectNode toolContentJSON) {

	Mindmap content = new Mindmap();
	Date updateDate = new Date();
	content.setToolContentId(toolContentID);
	content.setCreateDate(updateDate);
	content.setUpdateDate(updateDate);
	content.setTitle(JsonUtil.optString(toolContentJSON, RestTags.TITLE));
	content.setInstructions(JsonUtil.optString(toolContentJSON, RestTags.INSTRUCTIONS));
	content.setContentInUse(false);
	content.setDefineLater(false);
	content.setReflectInstructions(JsonUtil.optString(toolContentJSON, RestTags.REFLECT_INSTRUCTIONS));
	content.setReflectOnActivity(JsonUtil.optBoolean(toolContentJSON, RestTags.REFLECT_ON_ACTIVITY, Boolean.FALSE));
	content.setLockOnFinished(JsonUtil.optBoolean(toolContentJSON, RestTags.LOCK_WHEN_FINISHED, Boolean.FALSE));
	content.setMultiUserMode(JsonUtil.optBoolean(toolContentJSON, "multiUserMode", Boolean.FALSE));

	// createdBy and submissionDeadline are null in the database using the standard authoring module
	// submissionDeadline is set in monitoring
	// content.setSubmissionDeadline((Date) JsonUtil.opt(toolContentJSON, RestTags.SUBMISSION_DEADLINE, null));
	// content.setCreatedBy(user);
	// content.setMindmapExportContent(exportContent) only set by export, not by authoring
	saveOrUpdateMindmap(content);

	// creating default nodes for current mindmap
	String rootNodeName = getMindmapMessageService().getMessage("node.root.defaultName");
	String childNodeName1 = getMindmapMessageService().getMessage("node.child1.defaultName");
	String childNodeName2 = getMindmapMessageService().getMessage("node.child2.defaultName");

	MindmapNode rootMindmapNode = saveMindmapNode(null, null, 1l, rootNodeName, "ffffff", null, content, null);
	saveOrUpdateMindmapNode(rootMindmapNode);
	saveMindmapNode(null, rootMindmapNode, 2l, childNodeName1, "ffffff", null, content, null);
	saveMindmapNode(null, rootMindmapNode, 3l, childNodeName2, "ffffff", null, content, null);
    }

    @Override
    public XStream getXStream() {
	return xstream;
    }

    public void fillGalleryWalkRatings(MindmapDTO mindmapDTO, Long ratingUserId) {
	Map<Long, ItemRatingDTO> itemRatingDtoMap = null;
	if (!mindmapDTO.isGalleryWalkReadOnly()) {
	    // it should have been created on lesson create,
	    // but in case Live Edit added Gallery Walk, we need to add it now, but just once
	    try {
		createGalleryWalkRatingCriterion(mindmapDTO.getToolContentId());
	    } catch (Exception e) {
		logger.warn("Ignoring error while processing Mindmap Gallery Walk criteria for tool content ID "
			+ mindmapDTO.getToolContentId());
	    }
	}

	// Item IDs are WhiteboardSession session IDs, i.e. a single Whiteboard
	Set<Long> itemIds = mindmapDTO.getSessionDTOs().stream()
		.collect(Collectors.mapping(MindmapSessionDTO::getSessionID, Collectors.toSet()));

	List<ItemRatingDTO> itemRatingDtos = ratingService.getRatingCriteriaDtos(mindmapDTO.getToolContentId(), null,
		itemIds, true, ratingUserId);
	// Mapping of Item ID -> DTO
	itemRatingDtoMap = itemRatingDtos.stream()
		.collect(Collectors.toMap(ItemRatingDTO::getItemId, Function.identity()));

	for (MindmapSessionDTO sessionDTO : mindmapDTO.getSessionDTOs()) {
	    sessionDTO.setItemRatingDto(itemRatingDtoMap.get(sessionDTO.getSessionID()));
	}
    }

    @Override
    public void createGalleryWalkRatingCriterion(long toolContentId) {
	List<RatingCriteria> criteria = ratingService.getCriteriasByToolContentId(toolContentId);

	if (criteria.size() >= 2) {
	    criteria = ratingService.getCriteriasByToolContentId(toolContentId);
	    // Mindmap currently supports only one place for ratings.
	    // It is rating other groups' boards on results page.
	    // Criterion gets automatically created and there must be only one.
	    try {
		for (int criterionIndex = 1; criterionIndex < criteria.size(); criterionIndex++) {
		    RatingCriteria criterion = criteria.get(criterionIndex);
		    Long criterionId = criterion.getRatingCriteriaId();
		    mindmapDAO.delete(criterion);
		    logger.warn("Removed a duplicate criterion ID " + criterionId + " for Mindmap tool content ID "
			    + toolContentId);
		}
	    } catch (Exception e) {
		logger.warn("Ignoring error while deleting a duplicate criterion for Mindmap tool content ID "
			+ toolContentId + ": " + e.getMessage());
	    }
	    return;
	}

	if (criteria.isEmpty()) {
	    ToolActivityRatingCriteria criterion = (ToolActivityRatingCriteria) RatingCriteria.getRatingCriteriaInstance(
		    RatingCriteria.TOOL_ACTIVITY_CRITERIA_TYPE);
	    criterion.setTitle(mindmapMessageService.getMessage("label.pad.rating.title"));
	    criterion.setOrderId(1);
	    criterion.setRatingStyle(RatingCriteria.RATING_STYLE_STAR);
	    criterion.setCommentsEnabled(true);
	    criterion.setToolContentId(toolContentId);

	    mindmapDAO.insert(criterion);
	    criteria.add(criterion);
	}
    }

    @Override
    public void startGalleryWalk(long toolContentId) {
	Mindmap mindmap = getMindmapByContentId(toolContentId);
	if (!mindmap.isGalleryWalkEnabled()) {
	    throw new IllegalArgumentException(
		    "Can not start Gallery Walk as it is not enabled for Mindmap with tool content ID "
			    + toolContentId);
	}
	if (mindmap.isGalleryWalkFinished()) {
	    throw new IllegalArgumentException(
		    "Can not start Gallery Walk as it is already finished for Mindmap with tool content ID "
			    + toolContentId);
	}
	mindmap.setGalleryWalkStarted(true);
	mindmapDAO.update(mindmap);

	sendGalleryWalkRefreshRequest(mindmap);
    }

    @Override
    public void skipGalleryWalk(long toolContentId) {
	Mindmap mindmap = getMindmapByContentId(toolContentId);
	if (!mindmap.isGalleryWalkEnabled()) {
	    throw new IllegalArgumentException(
		    "Can not skip Gallery Walk as it is not enabled for Mindmap with tool content ID " + toolContentId);
	}
	if (mindmap.isGalleryWalkStarted()) {
	    throw new IllegalArgumentException(
		    "Can not skip Gallery Walk as it is already started for Mindmap with tool content ID "
			    + toolContentId);
	}
	if (mindmap.isGalleryWalkFinished()) {
	    throw new IllegalArgumentException(
		    "Can not skip Gallery Walk as it is already finished for Mindmap with tool content ID "
			    + toolContentId);
	}
	mindmap.setGalleryWalkEnabled(false);
	mindmapDAO.update(mindmap);

	sendGalleryWalkRefreshRequest(mindmap);
    }

    @Override
    public void finishGalleryWalk(long toolContentId) {
	Mindmap mindmap = getMindmapByContentId(toolContentId);
	if (!mindmap.isGalleryWalkEnabled()) {
	    throw new IllegalArgumentException(
		    "Can not finish Gallery Walk as it is not enabled for Mindmap with tool content ID "
			    + toolContentId);
	}
	mindmap.setGalleryWalkFinished(true);
	mindmapDAO.update(mindmap);

	sendGalleryWalkRefreshRequest(mindmap);
    }

    @Override
    public void enableGalleryWalkLearnerEdit(long toolContentId) throws IOException {
	Mindmap mindmap = getMindmapByContentId(toolContentId);
	if (!mindmap.isGalleryWalkEnabled()) {
	    throw new IllegalArgumentException(
		    "Can not allow learners to reedit activity as Gallery Walk is not enabled for Mindmap with tool content ID "
			    + toolContentId);
	}
	mindmap.setGalleryWalkEditEnabled(true);
	mindmapDAO.update(mindmap);

	sendGalleryWalkRefreshRequest(mindmap);
    }

    private void sendGalleryWalkRefreshRequest(Mindmap mindmap) {
	ObjectNode jsonCommand = JsonNodeFactory.instance.objectNode();
	jsonCommand.put("hookTrigger", "mindmap-refresh-" + mindmap.getToolContentId());
	// get all learners in this mindmap
	Set<Integer> userIds = mindmap.getMindmapSessions().stream()
		.flatMap(session -> session.getMindmapUsers().stream())
		.collect(Collectors.mapping(user -> user.getUserId().intValue(), Collectors.toSet()));

	learnerService.createCommandForLearners(mindmap.getToolContentId(), userIds, jsonCommand.toString());
    }
}