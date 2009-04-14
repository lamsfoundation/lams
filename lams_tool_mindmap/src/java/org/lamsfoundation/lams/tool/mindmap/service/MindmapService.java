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

package org.lamsfoundation.lams.tool.mindmap.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.contentrepository.AccessDeniedException;
import org.lamsfoundation.lams.contentrepository.ICredentials;
import org.lamsfoundation.lams.contentrepository.ITicket;
import org.lamsfoundation.lams.contentrepository.InvalidParameterException;
import org.lamsfoundation.lams.contentrepository.LoginException;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.WorkspaceNotFoundException;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.contentrepository.service.IRepositoryService;
import org.lamsfoundation.lams.contentrepository.service.SimpleCredentials;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
import org.lamsfoundation.lams.tool.ToolContentImport102Manager;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.SessionDataExistsException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.mindmap.dao.IMindmapAttachmentDAO;
import org.lamsfoundation.lams.tool.mindmap.dao.IMindmapDAO;
import org.lamsfoundation.lams.tool.mindmap.dao.IMindmapNodeDAO;
import org.lamsfoundation.lams.tool.mindmap.dao.IMindmapRequestDAO;
import org.lamsfoundation.lams.tool.mindmap.dao.IMindmapSessionDAO;
import org.lamsfoundation.lams.tool.mindmap.dao.IMindmapUserDAO;
import org.lamsfoundation.lams.tool.mindmap.model.Mindmap;
import org.lamsfoundation.lams.tool.mindmap.model.MindmapAttachment;
import org.lamsfoundation.lams.tool.mindmap.model.MindmapNode;
import org.lamsfoundation.lams.tool.mindmap.model.MindmapRequest;
import org.lamsfoundation.lams.tool.mindmap.model.MindmapSession;
import org.lamsfoundation.lams.tool.mindmap.model.MindmapUser;
import org.lamsfoundation.lams.tool.mindmap.util.MindmapConstants;
import org.lamsfoundation.lams.tool.mindmap.util.MindmapException;
import org.lamsfoundation.lams.tool.mindmap.util.MindmapToolContentHandler;
import org.lamsfoundation.lams.tool.mindmap.util.xmlmodel.NodeConceptModel;
import org.lamsfoundation.lams.tool.mindmap.util.xmlmodel.NodeModel;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.audit.IAuditService;

/**
 * An implementation of the IMindmapService interface. As a requirement, all LAMS tool's service bean must implement
 * ToolContentManager and ToolSessionManager.
 */
public class MindmapService implements ToolSessionManager, ToolContentManager, IMindmapService,
	ToolContentImport102Manager {

    static Logger logger = Logger.getLogger(MindmapService.class.getName());

    private IMindmapDAO mindmapDAO = null;
    private IMindmapSessionDAO mindmapSessionDAO = null;
    private IMindmapUserDAO mindmapUserDAO = null;
    private IMindmapAttachmentDAO mindmapAttachmentDAO = null;
    private IMindmapNodeDAO mindmapNodeDAO = null;
    private IMindmapRequestDAO mindmapRequestDAO = null;
    private ILearnerService learnerService;
    private ILamsToolService toolService;
    private IToolContentHandler mindmapToolContentHandler = null;
    private IRepositoryService repositoryService = null;
    private IAuditService auditService = null;
    private IExportToolContentService exportContentService;
    private ICoreNotebookService coreNotebookService;
    private MindmapOutputFactory mindmapOutputFactory;
    private String nodesToDeleteCondition = null; // string to accumulate nodes to delete
    private MessageService mindmapMessageService;
    
    public MindmapService() {
	super();
    }

    /*  Methods from ToolSessionManager */
    public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException {
	if (MindmapService.logger.isDebugEnabled()) {
	    MindmapService.logger.debug("entering method createToolSession:" + " toolSessionId = " + toolSessionId
		    + " toolSessionName = " + toolSessionName + " toolContentId = " + toolContentId);
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
	mindmapSessionDAO.deleteBySessionID(toolSessionId);
	// TODO check if cascade worked
    }

    /**
     * Get the tool output for the given tool output names.
     * 
     * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.util.List<String>, java.lang.Long,
     *      java.lang.Long)
     */
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, Long toolSessionId, Long learnerId) {
	return getMindmapOutputFactory().getToolOutput(names, this, toolSessionId, learnerId);
    }

    /**
     * Get the tool output for the given tool output name.
     * 
     * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.lang.String, java.lang.Long,
     *      java.lang.Long)
     */
    public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
	return getMindmapOutputFactory().getToolOutput(name, this, toolSessionId, learnerId);
    }
    
    /**
     * Get number of nodes created by particular user
     * 
     * @param learnerId
     * @param toolSessionId
     * @return
     */
    public int getNumNodes(Long learnerId, Long toolSessionId) {
	//MindmapUser mindmapUser = getUserByUserIdAndSessionId(learnerId, toolSessionId);
	//MindmapSession mindmapSession = getSessionBySessionId(toolSessionId);

	return mindmapNodeDAO.getNumNodesByUserAndSession(learnerId, toolSessionId);
    }
    
    
    /*  Methods from ToolContentManager */

    public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException {

	if (MindmapService.logger.isDebugEnabled()) {
	    MindmapService.logger.debug("entering method copyToolContent:" + " fromContentId=" + fromContentId
		    + " toContentId=" + toContentId);
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
	Mindmap toContent = Mindmap.newInstance(fromContent, toContentId, mindmapToolContentHandler);
	mindmapDAO.saveOrUpdate(toContent);

	/* Copying Mindmap Nodes */
	
	// creating default nodes for current mindmap
	String rootNodeName = getMindmapMessageService().getMessage("node.root.defaultName");
	String childNodeName1 = getMindmapMessageService().getMessage("node.child1.defaultName");
	String childNodeName2 = getMindmapMessageService().getMessage("node.child2.defaultName");
	
	List rootNode = getAuthorRootNodeByMindmapId(fromContent.getUid());
	
	MindmapNode rootMindmapNode = null;
	if (rootNode == null || rootNode.size() == 0)
	{
	    // Create default content
	    rootMindmapNode = saveMindmapNode(null, null, 1l, rootNodeName, "ffffff", null, toContent);
	    saveOrUpdateMindmapNode(rootMindmapNode);
	    saveMindmapNode(null, rootMindmapNode, 2l, childNodeName1, "ffffff", null, toContent);
	    saveMindmapNode(null, rootMindmapNode, 3l, childNodeName2, "ffffff", null, toContent);
	}
	else { 
	    rootMindmapNode = (MindmapNode) getAuthorRootNodeByMindmapId(fromContent.getUid()).get(0);
	}
	
	//MindmapNode fromMindmapNode = (MindmapNode) getAuthorRootNodeByMindmapId(fromContent.getUid()).get(0);
	cloneMindmapNodesForRuntime(rootMindmapNode, null, fromContent, toContent);
    }
    
    /**
     * Makes a runtime copy of Mindmap Nodes for single-user mode.
     * Makes a shared copy of Mindmap Nodes for multi-user mode.
     * 
     * @param fromMindmapNode
     * @param toMindmapNode
     * @param fromContent
     * @param toContent
     * @return 
     */
    public void cloneMindmapNodesForRuntime(MindmapNode fromMindmapNode, MindmapNode toMindmapNode,
	    Mindmap fromContent, Mindmap toContent) {
	toMindmapNode = saveMindmapNode(null, toMindmapNode, fromMindmapNode.getUniqueId(), fromMindmapNode.getText(),
		fromMindmapNode.getColor(), fromMindmapNode.getUser(), toContent);

	List childMindmapNodes = getMindmapNodeByParentId(fromMindmapNode.getNodeId(), fromContent.getUid());

	if (childMindmapNodes != null && childMindmapNodes.size() > 0) {
	    for (Iterator iterator = childMindmapNodes.iterator(); iterator.hasNext();) {
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
    public MindmapNode saveMindmapNode(MindmapNode currentMindmapNode, MindmapNode parentMindmapNode, Long uniqueId,
	    String text, String color, MindmapUser mindmapUser, Mindmap mindmap) {
	if (currentMindmapNode == null) {
	    currentMindmapNode = new MindmapNode();
	}
	currentMindmapNode.setParent(parentMindmapNode);
	currentMindmapNode.setUniqueId(uniqueId);
	currentMindmapNode.setText(text);
	currentMindmapNode.setColor(color);
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
    public NodeModel getMindmapXMLFromDatabase(Long rootNodeId, Long mindmapId, NodeModel rootNodeModel,
	    MindmapUser mindmapUser) {
	List mindmapNodes = getMindmapNodeByParentId(rootNodeId, mindmapId);

	if (mindmapNodes != null && mindmapNodes.size() > 0) {
	    for (Iterator iterator = mindmapNodes.iterator(); iterator.hasNext();) {
		MindmapNode mindmapNode = (MindmapNode) iterator.next();

		String mindmapUserName = null;
		if (mindmapNode.getUser() == null)
		    mindmapUserName = getMindmapMessageService().getMessage("node.instructor.label");
		else
		    mindmapUserName = mindmapNode.getUser().getFirstName() + " " + mindmapNode.getUser().getLastName();

		NodeModel nodeModel = null;
		if (mindmapUser != null) {
		    int edit = 1;
		    if (mindmapNode.getUser() == mindmapUser)
			edit = 1;
		    else
			edit = 0;

		    nodeModel = new NodeModel(new NodeConceptModel(mindmapNode.getUniqueId(), mindmapNode.getText(),
			    mindmapNode.getColor(), mindmapUserName, edit));
		} else {
		    nodeModel = new NodeModel(new NodeConceptModel(mindmapNode.getUniqueId(), mindmapNode.getText(),
			    mindmapNode.getColor(), mindmapUserName, 1));
		}

		rootNodeModel.addNode(nodeModel);
		getMindmapXMLFromDatabase(mindmapNode.getNodeId(), mindmapId, nodeModel, mindmapUser);
	    }
	}

	return rootNodeModel;
    }

    public void getChildMindmapNodes(List<NodeModel> branches, MindmapNode rootMindmapNode, MindmapUser mindmapUser,
	    Mindmap mindmap) {
	for (Iterator<NodeModel> iterator = branches.iterator(); iterator.hasNext();) {
	    NodeModel nodeModel = (NodeModel) iterator.next();
	    NodeConceptModel nodeConceptModel = nodeModel.getConcept();
	    // saving branch
	    List curMindmapNodeList = null;
	    if (mindmapUser == null)
		curMindmapNodeList = getMindmapNodeByUniqueId(nodeConceptModel.getId(), mindmap.getUid());
	    else
		curMindmapNodeList = getMindmapNodeByUniqueIdMindmapIdUserId(nodeConceptModel.getId(),
			mindmap.getUid(), mindmapUser.getUid());

	    MindmapNode currentMindmapNode = null;
	    if (curMindmapNodeList != null && curMindmapNodeList.size() > 0) {
		currentMindmapNode = (MindmapNode) curMindmapNodeList.get(0);
	    }
	    this.nodesToDeleteCondition += " and uniqueId <> " + nodeConceptModel.getId();
	    currentMindmapNode = saveMindmapNode(currentMindmapNode, rootMindmapNode, nodeConceptModel.getId(),
		    nodeConceptModel.getText(), nodeConceptModel.getColor(), mindmapUser, mindmap);
	    // if there are child nodes, redo for every child
	    if (nodeModel.getBranch() != null)
		getChildMindmapNodes(nodeModel.getBranch(), currentMindmapNode, mindmapUser, mindmap);
	}
    }
    
    public String getLanguageXML() {
	ArrayList<String> languageCollection = new ArrayList<String>();		
	languageCollection.add(new String("local.title"));
	languageCollection.add(new String("local.delete_question"));
	languageCollection.add(new String("local.yes"));
	languageCollection.add(new String("local.no"));
	languageCollection.add(new String("local.node_creator"));
	
	String languageOutput = "<xml><language>";
	
	for(int i=0; i < languageCollection.size(); i++) {
	    languageOutput += "<entry key='" + languageCollection.get(i) + "'><name>" + 
	    	mindmapMessageService.getMessage(languageCollection.get(i)) + "</name></entry>";
	}
	
	languageOutput += "</language></xml>";
	
	return languageOutput;
    }

    public void setAsDefineLater(Long toolContentId, boolean value) throws DataMissingException, ToolException {
	Mindmap mindmap = mindmapDAO.getByContentId(toolContentId);
	if (mindmap == null) {
	    throw new ToolException("Could not find tool with toolContentID: " + toolContentId);
	}
	mindmap.setDefineLater(value);
	mindmapDAO.saveOrUpdate(mindmap);
    }

    public void setAsRunOffline(Long toolContentId, boolean value) throws DataMissingException, ToolException {
	Mindmap mindmap = mindmapDAO.getByContentId(toolContentId);
	if (mindmap == null) {
	    throw new ToolException("Could not find tool with toolContentID: " + toolContentId);
	}
	mindmap.setRunOffline(value);
	mindmapDAO.saveOrUpdate(mindmap);
    }

    public void removeToolContent(Long toolContentId, boolean removeSessionData) throws SessionDataExistsException,
	    ToolException {
	// TODO Auto-generated method stub
    }

    /**
     * Export the XML fragment for the tool's content, along with any files needed for the content.
     * 
     * @throws DataMissingException
     *             if no tool content matches the toolSessionId
     * @throws ToolException
     *             if any other error occurs
     */

    public void exportToolContent(Long toolContentId, String rootPath) throws DataMissingException, ToolException {
	Mindmap mindmap = mindmapDAO.getByContentId(toolContentId);
	if (mindmap == null) {
	    mindmap = getDefaultContent();
	}
	if (mindmap == null) {
	    throw new DataMissingException("Unable to find default content for the mindmap tool");
	}

	// set ResourceToolContentHandler as null to avoid copy file node in
	// repository again.
	mindmap = Mindmap.newInstance(mindmap, toolContentId, null);
	mindmap.setToolContentHandler(null);
	mindmap.setMindmapSessions(null);
	Set<MindmapAttachment> atts = mindmap.getMindmapAttachments();
	for (MindmapAttachment att : atts) {
	    att.setMindmap(null);
	}
	try {
	    exportContentService.registerFileClassForExport(MindmapAttachment.class.getName(), "fileUuid",
		    "fileVersionId");
	    exportContentService.exportToolContent(toolContentId, mindmap, mindmapToolContentHandler, rootPath);
	} catch (ExportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    /**
     * Import the XML fragment for the tool's content, along with any files needed for the content.
     * 
     * @throws ToolException
     *             if any other error occurs
     */
    public void importToolContent(Long toolContentId, Integer newUserUid, String toolContentPath, String fromVersion,
	    String toVersion) throws ToolException {
	try {
	    exportContentService.registerFileClassForImport(MindmapAttachment.class.getName(), "fileUuid",
		    "fileVersionId", "fileName", "fileType", null, null);

	    Object toolPOJO = exportContentService.importToolContent(toolContentPath, mindmapToolContentHandler,
		    fromVersion, toVersion);
	    if (!(toolPOJO instanceof Mindmap)) {
		throw new ImportToolContentException("Import Mindmap tool content failed. Deserialized object is "
			+ toolPOJO);
	    }
	    Mindmap mindmap = (Mindmap) toolPOJO;

	    // reset it to new toolContentId
	    mindmap.setToolContentId(toolContentId);
	    mindmap.setCreateBy(new Long(newUserUid.longValue()));

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
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Long toolContentId) throws ToolException {
	Mindmap mindmap = getMindmapDAO().getByContentId(toolContentId);
	if (mindmap == null) {
	    mindmap = getDefaultContent();
	}
	return getMindmapOutputFactory().getToolOutputDefinitions(mindmap);
    }

    /*  IMindmapService Methods */

    public Long createNotebookEntry(Long id, Integer idType, String signature, Integer userID, String entry) {
	return coreNotebookService.createNotebookEntry(id, idType, signature, userID, "", entry);
    }

    public NotebookEntry getEntry(Long uid) {
	return coreNotebookService.getEntry(uid);
    }

    public void updateEntry(Long uid, String entry) {
	coreNotebookService.updateEntry(uid, "", entry);
    }
    
    public void updateEntry(NotebookEntry notebookEntry) {
	coreNotebookService.updateEntry(notebookEntry);
    }
    
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

    public Mindmap copyDefaultContent(Long newContentID) {

	if (newContentID == null) {
	    String error = "Cannot copy the Mindmap tools default content: + " + "newContentID is null";
	    MindmapService.logger.error(error);
	    throw new MindmapException(error);
	}

	Mindmap defaultContent = getDefaultContent();
	// create new mindmap using the newContentID
	Mindmap newContent = new Mindmap();
	newContent = Mindmap.newInstance(defaultContent, newContentID, mindmapToolContentHandler);
	mindmapDAO.saveOrUpdate(newContent);
	return newContent;
    }

    public Mindmap getMindmapByContentId(Long toolContentID) {
	Mindmap mindmap = mindmapDAO.getByContentId(toolContentID);
	if (mindmap == null) {
	    MindmapService.logger.debug("Could not find the content with toolContentID:" + toolContentID);
	}
	return mindmap;
    }

    public Mindmap getMindmapByUid(Long Uid) {
	Mindmap mindmap = mindmapDAO.getMindmapByUid(Uid);
	if (mindmap == null) {
	    MindmapService.logger.debug("Could not find the content with Uid:" + Uid);
	}
	return mindmap;
    }

    public MindmapSession getSessionBySessionId(Long toolSessionId) {
	MindmapSession mindmapSession = mindmapSessionDAO.getBySessionId(toolSessionId);
	if (mindmapSession == null) {
	    MindmapService.logger.debug("Could not find the mindmap session with toolSessionID:" + toolSessionId);
	}
	return mindmapSession;
    }

    public MindmapUser getUserByUserIdAndSessionId(Long userId, Long toolSessionId) {
	return mindmapUserDAO.getByUserIdAndSessionId(userId, toolSessionId);
    }

    public MindmapUser getUserByLoginNameAndSessionId(String loginName, Long toolSessionId) {
	return mindmapUserDAO.getByLoginNameAndSessionId(loginName, toolSessionId);
    }

    public MindmapUser getUserByUID(Long uid) {
	return mindmapUserDAO.getByUID(uid);
    }

    public MindmapAttachment uploadFileToContent(Long toolContentId, FormFile file, String type) {
	if (file == null || StringUtils.isEmpty(file.getFileName())) {
	    throw new MindmapException("Could not find upload file: " + file);
	}

	NodeKey nodeKey = processFile(file, type);
	MindmapAttachment attachment = new MindmapAttachment(nodeKey.getVersion(), type, file.getFileName(), 
		nodeKey.getUuid(), new Date());
	return attachment;
    }

    public void deleteFromRepository(Long uuid, Long versionID) throws MindmapException {
	ITicket ticket = getRepositoryLoginTicket();
	try {
	    repositoryService.deleteVersion(ticket, uuid, versionID);
	} catch (Exception e) {
	    throw new MindmapException("Exception occured while deleting files from" + " the repository " + e.getMessage());
	}
    }

    public void deleteInstructionFile(Long contentID, Long uuid, Long versionID, String type) {
	mindmapDAO.deleteInstructionFile(contentID, uuid, versionID, type);

    }

    public void saveOrUpdateMindmap(Mindmap mindmap) {
	mindmapDAO.saveOrUpdate(mindmap);
    }

    public void saveOrUpdateMindmapRequest(MindmapRequest mindmapRequest) {
	mindmapRequestDAO.saveOrUpdate(mindmapRequest);
    }

    public void saveOrUpdateMindmapSession(MindmapSession mindmapSession) {
	mindmapSessionDAO.saveOrUpdate(mindmapSession);
    }

    public void saveOrUpdateMindmapUser(MindmapUser mindmapUser) {
	mindmapUserDAO.saveOrUpdate(mindmapUser);
    }

    public MindmapUser createMindmapUser(UserDTO user, MindmapSession mindmapSession) {
	MindmapUser mindmapUser = new MindmapUser(user, mindmapSession);
	saveOrUpdateMindmapUser(mindmapUser);
	return mindmapUser;
    }

    public IAuditService getAuditService() {
	return auditService;
    }

    public void setAuditService(IAuditService auditService) {
	this.auditService = auditService;
    }

    private NodeKey processFile(FormFile file, String type) {
	NodeKey node = null;
	if (file != null && !StringUtils.isEmpty(file.getFileName())) {
	    String fileName = file.getFileName();
	    try {
		node = getMindmapToolContentHandler().uploadFile(file.getInputStream(), fileName,
			file.getContentType(), type);
	    } catch (InvalidParameterException e) {
		throw new MindmapException("InvalidParameterException occured while trying to upload File"
			+ e.getMessage());
	    } catch (FileNotFoundException e) {
		throw new MindmapException("FileNotFoundException occured while trying to upload File" + e.getMessage());
	    } catch (RepositoryCheckedException e) {
		throw new MindmapException("RepositoryCheckedException occured while trying to upload File"
			+ e.getMessage());
	    } catch (IOException e) {
		throw new MindmapException("IOException occured while trying to upload File" + e.getMessage());
	    }
	}
	return node;
    }

    /**
     * This method verifies the credentials of the SubmitFiles Tool and gives it the <code>Ticket</code> to login and
     * access the Content Repository.
     * 
     * A valid ticket is needed in order to access the content from the repository. This method would be called evertime
     * the tool needs to upload/download files from the content repository.
     * 
     * @return ITicket The ticket for repostory access
     * @throws SubmitFilesException
     */
    private ITicket getRepositoryLoginTicket() throws MindmapException {
	ICredentials credentials = new SimpleCredentials(MindmapToolContentHandler.repositoryUser,
		MindmapToolContentHandler.repositoryId);
	try {
	    ITicket ticket = repositoryService.login(credentials, MindmapToolContentHandler.repositoryWorkspaceName);
	    return ticket;
	} catch (AccessDeniedException ae) {
	    throw new MindmapException("Access Denied to repository." + ae.getMessage());
	} catch (WorkspaceNotFoundException we) {
	    throw new MindmapException("Workspace not found." + we.getMessage());
	} catch (LoginException e) {
	    throw new MindmapException("Login failed." + e.getMessage());
	}
    }

    /* ===============Methods implemented from ToolContentImport102Manager =============== */

    /**
     * Import the data for a 1.0.2 Mindmap
     */
    public void import102ToolContent(Long toolContentId, UserDTO user, Hashtable importValues) {
	Date now = new Date();
	Mindmap mindmap = new Mindmap();
	mindmap.setContentInUse(Boolean.FALSE);
	mindmap.setCreateBy(new Long(user.getUserID().longValue()));
	mindmap.setCreateDate(now);
	mindmap.setDefineLater(Boolean.FALSE);
	mindmap.setInstructions(WebUtil.convertNewlines((String) importValues.get(ToolContentImport102Manager.CONTENT_BODY)));
	mindmap.setLockOnFinished(Boolean.TRUE);
	mindmap.setOfflineInstructions(null);
	mindmap.setOnlineInstructions(null);
	mindmap.setRunOffline(Boolean.FALSE);
	mindmap.setTitle((String) importValues.get(ToolContentImport102Manager.CONTENT_TITLE));
	mindmap.setToolContentId(toolContentId);
	mindmap.setUpdateDate(now);
	//mindmap.setAllowRichEditor(Boolean.FALSE);
	// leave as empty, no need to set them to anything.
	// setMindmapAttachments(Set mindmapAttachments);
	// setMindmapSessions(Set mindmapSessions);
	mindmapDAO.saveOrUpdate(mindmap);
    }

    /** Set the description, throws away the title value as this is not supported in 2.0 */
    public void setReflectiveData(Long toolContentId, String title, String description) throws ToolException,
	    DataMissingException {

	MindmapService.logger
		.warn("Setting the reflective field on a mindmap. This doesn't make sense as the mindmap is for reflection and we don't reflect on reflection!");
	Mindmap mindmap = getMindmapByContentId(toolContentId);
	if (mindmap == null) {
	    throw new DataMissingException("Unable to set reflective data titled " + title
		    + " on activity toolContentId " + toolContentId + " as the tool content does not exist.");
	}

	mindmap.setInstructions(description);
    }

    // =========================================================================================
    /*  Used by Spring to "inject" the linked objects */

    public IRepositoryService getRepositoryService() {
	return repositoryService;
    }

    public void setRepositoryService(IRepositoryService repositoryService) {
	this.repositoryService = repositoryService;
    }

    public IMindmapAttachmentDAO getMindmapAttachmentDAO() {
	return mindmapAttachmentDAO;
    }

    public void setMindmapAttachmentDAO(IMindmapAttachmentDAO attachmentDAO) {
	mindmapAttachmentDAO = attachmentDAO;
    }

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

    public IMindmapUserDAO getMindmapUserDAO() {
	return mindmapUserDAO;
    }

    public void setMindmapUserDAO(IMindmapUserDAO userDAO) {
	mindmapUserDAO = userDAO;
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

    public MindmapOutputFactory getMindmapOutputFactory() {
	return mindmapOutputFactory;
    }

    public void setMindmapOutputFactory(MindmapOutputFactory mindmapOutputFactory) {
	this.mindmapOutputFactory = mindmapOutputFactory;
    }

    public boolean isGroupedActivity(long toolContentID) {
	return toolService.isGroupedActivity(toolContentID);
    }

    public void setMindmapNodeDAO(IMindmapNodeDAO mindmapNodeDAO) {
	this.mindmapNodeDAO = mindmapNodeDAO;
    }

    public IMindmapNodeDAO getMindmapNodeDAO() {
	return mindmapNodeDAO;
    }

    public void saveOrUpdateMindmapNode(MindmapNode mindmapNode) {
	mindmapNodeDAO.saveOrUpdate(mindmapNode);
    }

    public List getAuthorRootNodeByMindmapId(Long mindmapId) {
	return mindmapNodeDAO.getAuthorRootNodeByMindmapId(mindmapId);
    }

    public List getAuthorRootNodeByMindmapIdMulti(Long mindmapId) {
	return mindmapNodeDAO.getAuthorRootNodeByMindmapIdMulti(mindmapId);
    }

    public List getRootNodeByMindmapIdAndUserId(Long mindmapId, Long userId) {
	return mindmapNodeDAO.getRootNodeByMindmapIdAndUserId(mindmapId, userId);
    }

    public List getUserRootNodeByUserId(Long userId) {
	return mindmapNodeDAO.getUserRootNodeByUserId(userId);
    }

    public List getMindmapNodeByParentId(Long parentId, Long mindmapId) {
	return mindmapNodeDAO.getMindmapNodeByParentId(parentId, mindmapId);
    }

    public List getMindmapNodeByUniqueId(Long uniqueId, Long mindmapId) {
	return mindmapNodeDAO.getMindmapNodeByUniqueId(uniqueId, mindmapId);
    }

    public List getMindmapNodeByUniqueIdMindmapIdUserId(Long uniqueId, Long mindmapId, Long userId) {
	return mindmapNodeDAO.getMindmapNodeByUniqueIdMindmapIdUserId(uniqueId, mindmapId, userId);
    }

    public void deleteNodeByUniqueMindmapUser(Long uniqueId, Long mindmapId, Long userId) {
	mindmapNodeDAO.deleteNodeByUniqueMindmapUser(uniqueId, mindmapId, userId);
    }

    public void deleteNodes(String nodesToDeleteCondition) {
	mindmapNodeDAO.deleteNodes(nodesToDeleteCondition);
    }

    public String getNodesToDeleteCondition() {
	return this.nodesToDeleteCondition;
    }

    public void setNodesToDeleteCondition(String nodesToDeleteCondition) {
	this.nodesToDeleteCondition = nodesToDeleteCondition;
    }

    public Long getLastUniqueIdByMindmapIdUserId(Long mindmapId, Long userId) {
	return this.mindmapNodeDAO.getLastUniqueIdByMindmapIdUserId(mindmapId, userId);
    }

    /**
     * @param mindmapRequestDAO
     *            the mindmapRequestDAO to set
     */
    public void setMindmapRequestDAO(IMindmapRequestDAO mindmapRequestDAO) {
	this.mindmapRequestDAO = mindmapRequestDAO;
    }

    /**
     * @return mindmapRequestDAO
     */
    public IMindmapRequestDAO getMindmapRequestDAO() {
	return mindmapRequestDAO;
    }

    public List getLastRequestsAfterGlobalId(Long globalId, Long mindmapId, Long userId) {
	return mindmapRequestDAO.getLastRequestsAfterGlobalId(globalId, mindmapId, userId);
    }

    public MindmapRequest getRequestByUniqueId(Long uniqueId, Long userId, Long mindmapId, Long globalId) {
	return mindmapRequestDAO.getRequestByUniqueId(uniqueId, userId, mindmapId, globalId);
    }
    
    /**
     * @param mindmapMessageService
     * @return mindmapRequestDAO.getLastGlobalIdByMindmapId(mindmapId)
     */
    public Long getLastGlobalIdByMindmapId(Long mindmapId) {
	return mindmapRequestDAO.getLastGlobalIdByMindmapId(mindmapId);
    }
    
    /**
     * @param mindmapMessageService
     * @return mindmapNodeDAO.getLastUniqueIdByMindmapId(mindmapId)
     */
    public Long getLastUniqueIdByMindmapId(Long mindmapId) {
	return mindmapNodeDAO.getLastUniqueIdByMindmapId(mindmapId);
    }

    /**
     * @param mindmapMessageService
     * @return
     */
    public void setMindmapMessageService(MessageService mindmapMessageService) {
	this.mindmapMessageService = mindmapMessageService;
    }

    /**
     * @return mindmapMessageService
     */
    public MessageService getMindmapMessageService() {
	return mindmapMessageService;
    }
}
