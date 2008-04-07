/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0 
 * as published by the Free Software Foundation.
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
package org.lams.lams.tool.wiki.service;

import static org.lams.lams.tool.wiki.util.WikiConstants.OLD_FORUM_STYLE;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;
import java.util.Map.Entry;

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
import org.lams.lams.tool.wiki.dto.MessageDTO;
import org.lams.lams.tool.wiki.persistence.Attachment;
import org.lams.lams.tool.wiki.persistence.WikiAttachmentDao;
import org.lams.lams.tool.wiki.persistence.Wiki;
import org.lams.lams.tool.wiki.persistence.WikiDao;
import org.lams.lams.tool.wiki.persistence.WikiException;
import org.lams.lams.tool.wiki.persistence.WikiReport;
import org.lams.lams.tool.wiki.persistence.WikiReportDAO;
import org.lams.lams.tool.wiki.persistence.WikiToolSession;
import org.lams.lams.tool.wiki.persistence.WikiToolSessionDao;
import org.lams.lams.tool.wiki.persistence.WikiUser;
import org.lams.lams.tool.wiki.persistence.WikiUserDao;
import org.lams.lams.tool.wiki.persistence.Message;
import org.lams.lams.tool.wiki.persistence.WikiMessageDao;
import org.lams.lams.tool.wiki.persistence.MessageSeq;
import org.lams.lams.tool.wiki.persistence.WikiMessageSeqDao;
import org.lams.lams.tool.wiki.persistence.PersistenceException;
import org.lams.lams.tool.wiki.util.DateComparator;
import org.lams.lams.tool.wiki.util.WikiConstants;
import org.lams.lams.tool.wiki.util.WikiToolContentHandler;
import org.lams.lams.tool.wiki.util.TopicComparator;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.audit.IAuditService;
import org.lamsfoundation.lams.util.wddx.WDDXProcessor;
import org.lamsfoundation.lams.util.wddx.WDDXProcessorConversionException;

/**
 * 
 * @author Steve.Ni
 * 
 * @version $Revision$
 */
public class WikiService implements IWikiService,ToolContentManager,ToolSessionManager,ToolContentImport102Manager {
	private static final Logger log = Logger.getLogger(WikiService.class);
	//DAO variables
	private WikiDao wikiDao;
	private WikiAttachmentDao wikiAttachmentDao;
	private WikiMessageDao wikiMessageDao;
	private WikiMessageSeqDao wikiMessageSeqDao;
	private WikiUserDao wikiUserDao;
	private WikiToolSessionDao wikiToolSessionDao;
	private WikiReportDAO wikiReportDAO;
	//system level handler and service 
	private ILamsToolService toolService;
	private WikiToolContentHandler wikiToolContentHandler;
	private IRepositoryService repositoryService;
	private ILearnerService learnerService;
	private IAuditService auditService;
    private MessageService messageService;
    private IExportToolContentService exportContentService;
    private IUserManagementService userManagementService;
	private ICoreNotebookService coreNotebookService;
	private WikiOutputFactory wikiOutputFactory;
	
	//---------------------------------------------------------------------
    // Inversion of Control Methods - Method injection
    //---------------------------------------------------------------------
	public void setAuditService(IAuditService auditService) {
		this.auditService = auditService;
	}

	public IAuditService getAuditService( ) {
		return auditService;
	}

	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}
	
	public WikiOutputFactory getWikiOutputFactory() {
		return wikiOutputFactory;
	}

	public void setWikiOutputFactory(
			WikiOutputFactory wikiOutputFactory) {
		this.wikiOutputFactory = wikiOutputFactory;
	}

	public Wiki updateWiki(Wiki wiki) throws PersistenceException {
        wikiDao.saveOrUpdate(wiki);
        return wiki;
    }

    public Wiki getWiki(Long wikiUid) throws PersistenceException {
        return (Wiki) wikiDao.getById(wikiUid);
    }

	public Wiki getWikiByContentId(Long contentID)  throws PersistenceException {    	
		return (Wiki) wikiDao.getByContentId(contentID);
	}

    public void deleteWikiAttachment(Long attachmentId) throws PersistenceException {
        Attachment attachment = (Attachment) wikiAttachmentDao.getById(attachmentId);
        wikiAttachmentDao.delete(attachment);
    }

    public Message createRootTopic(Long wikiId, Long sessionId, Message message) throws PersistenceException {
    	return createRootTopic(wikiId, getSessionBySessionId(sessionId), message);
    }

    public Message createRootTopic(Long wikiId, WikiToolSession session, Message message) throws PersistenceException {
    	//get Wiki and WikiToolSesion
    	if(message.getWiki() == null){
    		Wiki wiki = wikiDao.getById(wikiId);
    		message.setWiki(wiki);
    	}
    	//if topic created by author, session will be null.
    	if(session != null){
    		message.setToolSession(session);
    	}
    	
    	if(message.getUid() == null){
	    	//update message sequence
	    	MessageSeq msgSeq = new MessageSeq();
	    	msgSeq.setMessage(message);
	    	msgSeq.setMessageLevel((short) 0);
	    	//set itself as root
	    	msgSeq.setRootMessage(message);
	    	wikiMessageSeqDao.save(msgSeq);
    	}
        
        // if this message had any cloned objects, they also need to be changed.
        // this will only happen if an authored topic is changed via monitoring
        if ( message.getSessionClones().size() > 0 ) {
        	Iterator iter = message.getSessionClones().iterator();
        	while ( iter.hasNext() ) {
        		Message clone = (Message) iter.next();
        		message.updateClone(clone);
        	}
        }

    	//create message in database
        wikiMessageDao.saveOrUpdate(message);

        return message;
    }

     public Message updateTopic(Message message) throws PersistenceException {
    	 
    	 //update message
    	wikiMessageDao.saveOrUpdate(message);
    	
    	//udate root message's lastReply date if this message
    	//if this message is root message, then actually, it will update itself lastReplayDate
    	MessageSeq msgSeq = wikiMessageSeqDao.getByTopicId(message.getUid());
    	Message root = msgSeq.getRootMessage();
    	//update reply date
        wikiMessageDao.saveOrUpdate(root);
        
        return message;
    }

 	public void updateReport(WikiReport report) {
 		wikiReportDAO.saveObject(report);
 	}
    public Message updateMessageHideFlag(Long messageId, boolean hideFlag) {
    	
    	Message message = getMessage(messageId);
    	if ( message !=null ) {
    		Long userId = 0L;
    		String loginName = "Default";
    		if(message.getCreatedBy() == null){
    			userId = message.getCreatedBy().getUserId();
    			loginName = message.getCreatedBy().getLoginName();
    		}
			if ( hideFlag ) {
				auditService.logHideEntry(WikiConstants.TOOL_SIGNATURE, userId, 
						loginName, message.toString());
			} else {
				auditService.logShowEntry(WikiConstants.TOOL_SIGNATURE,userId, 
						loginName, message.toString());
			}

	    	message.setHideFlag(hideFlag);
	    	
	    	// update message
	    	wikiMessageDao.update(message);
    	}
    	return message;
     }

    public Message getMessage(Long messageUid) throws PersistenceException {
        return (Message) wikiMessageDao.getById(messageUid);
    }

    public void deleteTopic(Long topicUid) throws PersistenceException {
    	List children = wikiMessageDao.getChildrenTopics(topicUid);
    	//cascade delete children topic by recursive
    	if(children != null){
    		Iterator iter = children.iterator();
    		while(iter.hasNext()){
    			Message msg = (Message) iter.next();
    			this.deleteTopic(msg.getUid());
    		}
    	}
    	wikiMessageSeqDao.deleteByTopicId(topicUid);
        wikiMessageDao.delete(topicUid);
     }

    public Message replyTopic(Long parentId,Long sessionId, Message replyMessage) throws PersistenceException {
    	//set parent
        Message parent = this.getMessage(parentId);
        replyMessage.setParent(parent);
        replyMessage.setWiki(parent.getWiki());
        //parent sessionID maybe empty if created by author role. So given sessionId is exactly value.
        WikiToolSession session = getSessionBySessionId(sessionId);
        replyMessage.setToolSession(session);
        wikiMessageDao.saveOrUpdate(replyMessage);
        
        //get root topic and create record in MessageSeq table
        MessageSeq parentSeq = wikiMessageSeqDao.getByTopicId(parent.getUid());
        if(parentSeq == null){
        	log.error("Message Sequence table is broken becuase topic " + parent +" can not get Sequence Record");
        }
        Message root = parentSeq.getRootMessage();
        MessageSeq msgSeq = new MessageSeq();
        msgSeq.setMessage(replyMessage);
        msgSeq.setMessageLevel((short) (parentSeq.getMessageLevel() + 1));
        msgSeq.setRootMessage(root);
        wikiMessageSeqDao.save(msgSeq);
        
        //update last reply date for root message
        root.setLastReplyDate(new Date());
        //update reply message number for root 
        root.setReplyNumber(root.getReplyNumber()+1);
        wikiMessageDao.saveOrUpdate(root);
        
        return replyMessage;
    }

	public Attachment uploadInstructionFile(FormFile uploadFile, String fileType) throws PersistenceException{
		if(uploadFile == null || StringUtils.isEmpty(uploadFile.getFileName()))
			throw new WikiException("Could not find upload file: " + uploadFile);
		
		//upload file to repository
		NodeKey nodeKey = processFile(uploadFile,fileType);
		
		//create new attachement
		Attachment file = new Attachment();
		file.setFileType(fileType);
		file.setFileUuid(nodeKey.getUuid());
		file.setFileVersionId(nodeKey.getVersion());
		file.setFileName(uploadFile.getFileName());
		
		return file;

	}
 
	/**
	 * This method deletes the content with the given <code>uuid</code> and
	 * <code>versionID</code> from the content repository
	 * 
	 * @param uuid
	 *            The <code>uuid</code> of the node to be deleted
	 * @param versionID
	 *            The <code>version_id</code> of the node to be deleted.
	 * @throws SubmitFilesException
	 */
	public void deleteFromRepository(Long uuid, Long versionID)
			throws WikiException {
		ITicket ticket = getRepositoryLoginTicket();
		try {
			repositoryService.deleteVersion(ticket, uuid,versionID);
		} catch (Exception e) {
			throw new WikiException(
					"Exception occured while deleting files from"
							+ " the repository " + e.getMessage());
		}
	}


	public Attachment uploadAttachment(FormFile uploadFile) throws PersistenceException {
		if(uploadFile == null || StringUtils.isEmpty(uploadFile.getFileName()))
			throw new WikiException("Could not find upload file: " + uploadFile);
		
		NodeKey nodeKey = processFile(uploadFile,IToolContentHandler.TYPE_ONLINE);
		Attachment file = new Attachment();
		file.setFileType(IToolContentHandler.TYPE_ONLINE);
		file.setFileUuid(nodeKey.getUuid());
		file.setFileVersionId(nodeKey.getVersion());
		file.setFileName(uploadFile.getFileName());
		
		return file;
	}
	public List getTopicThread(Long rootTopicId){
		
		List unsortedThread =  wikiMessageSeqDao.getTopicThread(rootTopicId);
		Iterator iter = unsortedThread.iterator();
		MessageSeq msgSeq;
		SortedMap<MessageSeq,Message> map = new TreeMap<MessageSeq,Message>(new TopicComparator());
		while(iter.hasNext()){
			msgSeq = (MessageSeq) iter.next();
			map.put(msgSeq,msgSeq.getMessage());
		}
		return 	getSortedMessageDTO(map);
	}


	public List getRootTopics(Long sessionId){
		List topicsBySession =  wikiMessageDao.getRootTopics(sessionId);
		WikiToolSession session = getSessionBySessionId(sessionId);
		if(session == null || session.getWiki() == null){
			log.error("Failed on getting session by given sessionID:" + sessionId);
			throw new WikiException("Failed on getting session by given sessionID:" + sessionId);
		}
		
		//sorted by last post date
		Message msg;
		SortedMap<Date,Message> map = new TreeMap<Date,Message>(new DateComparator());
		Iterator iter = topicsBySession.iterator();
		while(iter.hasNext()){
			msg = (Message) iter.next();
			if(OLD_FORUM_STYLE)
				map.put(msg.getCreated(),msg);
			else
				map.put(msg.getLastReplyDate(),msg);
		}
		return 	MessageDTO.getMessageDTO(new ArrayList<Message>(map.values()));
		
	}

	public int getTopicsNum(Long userID, Long sessionId) {
		return wikiMessageDao.getTopicsNum(userID,sessionId);
	}

	public WikiUser getUserByID(Long userId) {
		return  wikiUserDao.getByUserId(userId);
	}

	public WikiUser getUserByUserAndSession(Long userId,Long sessionId) {
		return  wikiUserDao.getByUserIdAndSessionId(userId,sessionId);
	}

	public void createUser(WikiUser wikiUser) {
		wikiUserDao.save(wikiUser);
	}
	public WikiToolSession getSessionBySessionId(Long sessionId) {
		return wikiToolSessionDao.getBySessionId(sessionId);
	}

	public Long getRootTopicId(Long topicId) {
		MessageSeq seq = wikiMessageSeqDao.getByTopicId(topicId);
		if(seq == null ||seq.getRootMessage() == null){
			log.error("A sequence information can not be found for topic ID:" + topicId);
			return null;
		}
		return seq.getRootMessage().getUid();
	}

	public List getAuthoredTopics(Long wikiUid) {
		List list = wikiMessageDao.getTopicsFromAuthor(wikiUid);
		
		TreeMap<Date,Message> map = new TreeMap<Date,Message>(new DateComparator());
		// get all the topics skipping ones with a tool session (we may be editing in monitor) and sort by create date
		Iterator iter = list.iterator();
		while(iter.hasNext()){
			Message topic = (Message) iter.next();
			if ( topic.getToolSession() == null ) 
				map.put(topic.getCreated(),topic);
		}
		return MessageDTO.getMessageDTO(new ArrayList<Message>(map.values()));
	}


    public Long getToolDefaultContentIdBySignature(String toolSignature)
    {
        Long contentId = null;
    	contentId=new Long(toolService.getToolDefaultContentIdBySignature(toolSignature));    
    	if (contentId == null)
    	{
    	    String error="Could not retrieve default content id for this tool";
    	    log.error(error);
    	    throw new WikiException(error);
    	}
	    return contentId;
    }

	public List getSessionsByContentId(Long contentID) {
		return wikiToolSessionDao.getByContentId(contentID);
	}

	public List getUsersBySessionId(Long sessionID) {
		return wikiUserDao.getBySessionId(sessionID);
	}
    
	public List getMessagesByUserUid(Long userId,Long sessionId) {
		List list = wikiMessageDao.getByUserAndSession(userId,sessionId);
		
		return MessageDTO.getMessageDTO(list);
	}

	public WikiUser getUser(Long userUid) {
		return wikiUserDao.getByUid(userUid);
	}

	public void releaseMarksForSession(Long sessionID) {
		//udate release mark date for each message.
		List list = wikiMessageDao.getBySession(sessionID);
		Iterator iter = list.iterator();
		while(iter.hasNext()){
			Message msg = (Message) iter.next();
			WikiReport report = msg.getReport();
			if(report != null)
				report.setDateMarksReleased(new Date());
			wikiMessageDao.saveOrUpdate(msg);
		}
		//update session to set MarkRelease flag.
		WikiToolSession session = wikiToolSessionDao.getBySessionId(sessionID);
		session.setMarkReleased(true);
		wikiToolSessionDao.saveOrUpdate(session);
		
	}

	public void finishUserSession(WikiUser currentUser) {
		currentUser.setSessionFinished(true);
		wikiUserDao.save(currentUser);
	}
	
    //***************************************************************************************************************
    // Private methods
    //***************************************************************************************************************
	/**
	 * @param map
	 * @return
	 */
	private List getSortedMessageDTO(SortedMap<MessageSeq,Message> map) {
		Iterator iter;
		MessageSeq msgSeq;
		List<MessageDTO> msgDtoList = new ArrayList<MessageDTO>();
		iter =map.entrySet().iterator();
		while(iter.hasNext()){
			Map.Entry entry = (Entry) iter.next();
			msgSeq = (MessageSeq) entry.getKey();
			MessageDTO dto = MessageDTO.getMessageDTO((Message) entry.getValue());
			dto.setLevel(msgSeq.getMessageLevel());
			msgDtoList.add(dto);
		}
		return msgDtoList;
	}
	   /**
     * Process an uploaded file.
     * 
     * @param wikiForm
     * @throws FileNotFoundException
     * @throws IOException
     * @throws RepositoryCheckedException
     * @throws InvalidParameterException
     */
    private NodeKey processFile(FormFile file, String fileType){
    	NodeKey node = null;
        if (file!= null && !StringUtils.isEmpty(file.getFileName())) {
            String fileName = file.getFileName();
            try {
				node = getWikiToolContentHandler().uploadFile(file.getInputStream(), fileName, 
				        file.getContentType(), fileType);
			} catch (InvalidParameterException e) {
				throw new WikiException("FileNotFoundException occured while trying to upload File" + e.getMessage());
			} catch (FileNotFoundException e) {
				throw new WikiException("FileNotFoundException occured while trying to upload File" + e.getMessage());
			} catch (RepositoryCheckedException e) {
				throw new WikiException("FileNotFoundException occured while trying to upload File" + e.getMessage());
			} catch (IOException e) {
				throw new WikiException("FileNotFoundException occured while trying to upload File" + e.getMessage());
			}
          }
        return node;
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
	private ITicket getRepositoryLoginTicket() throws WikiException {
		ICredentials credentials = new SimpleCredentials(
				wikiToolContentHandler.getRepositoryUser(),
				wikiToolContentHandler.getRepositoryId());
		try {
			ITicket ticket = repositoryService.login(credentials,
					wikiToolContentHandler.getRepositoryWorkspaceName());
			return ticket;
		} catch (AccessDeniedException ae) {
			throw new WikiException("Access Denied to repository."
					+ ae.getMessage());
		} catch (WorkspaceNotFoundException we) {
			throw new WikiException("Workspace not found."
					+ we.getMessage());
		} catch (LoginException e) {
			throw new WikiException("Login failed." + e.getMessage());
		}
	}
    private Wiki getDefaultWiki(){
    	Long defaultWikiId = getToolDefaultContentIdBySignature(WikiConstants.TOOL_SIGNATURE);
    	Wiki defaultWiki = getWikiByContentId(defaultWikiId);
    	if(defaultWiki == null)
    	{
    	    String error="Could not retrieve default content record for this tool";
    	    log.error(error);
    	    throw new WikiException(error);
    	}
    	
    	return defaultWiki;

    }

	public Long createNotebookEntry(Long sessionId, Integer notebookToolType, String toolSignature, Integer userId, String entryText) {
		return coreNotebookService.createNotebookEntry(sessionId, notebookToolType, toolSignature, userId, "", entryText);
	}
	public NotebookEntry getEntry(Long sessionId, Integer idType, String signature, Integer userID){
		List<NotebookEntry> list = coreNotebookService.getEntry(sessionId, idType, signature, userID);
		if (list == null || list.isEmpty()) {
			return null;
		} else {
			return list.get(0);
		}
	}
	
	/**
	 * @param notebookEntry
	 */
	public void updateEntry(NotebookEntry notebookEntry) {
		coreNotebookService.updateEntry(notebookEntry);
	}
    //***************************************************************************************************************
    // ToolContentManager and ToolSessionManager methods
    //***************************************************************************************************************
	public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException {
		if (toContentId == null)
			throw new ToolException(
					"Failed to create the WikiFiles tool seession");

		Wiki fromContent = null;
		if ( fromContentId != null ) {
			fromContent = wikiDao.getByContentId(fromContentId);
		}
		if ( fromContent == null ) {
			fromContent = getDefaultWiki();
		}
		
		Wiki toContent = Wiki.newInstance(fromContent,toContentId,wikiToolContentHandler);
		wikiDao.saveOrUpdate(toContent);
		
		//save topics in this wiki, only save the author created topic!!! and reset its reply number to zero.
		Set topics = toContent.getMessages();
		if(topics != null){
			Iterator iter = topics.iterator();
			while(iter.hasNext()){
				Message msg = (Message) iter.next();
				//set this message wiki Uid as toContent
				if(!msg.getIsAuthored())
					continue;
				msg.setReplyNumber(0);
				msg.setCreated(new Date());
				msg.setUpdated(new Date());
				msg.setLastReplyDate(new Date());
				msg.setHideFlag(false);
				msg.setWiki(toContent);
				createRootTopic(toContent.getUid(),(WikiToolSession) null,msg);
			}
		}

	}

	public void setAsDefineLater(Long toolContentId, boolean value) throws DataMissingException, ToolException {
		Wiki wiki = wikiDao.getByContentId(toolContentId);
		if(wiki == null){
			throw new ToolException("No found tool content by given content ID:" + toolContentId);
		}
		wiki.setDefineLater(value);
		wiki.setContentInUse(false);
	}

	public void setAsRunOffline(Long toolContentId, boolean value) throws DataMissingException, ToolException {
		Wiki wiki = wikiDao.getByContentId(toolContentId);
		if(wiki == null){
			throw new ToolException("No found tool content by given content ID:" + toolContentId);
		}
		wiki.setRunOffline(value);
		
	}

	public void removeToolContent(Long toolContentId, boolean removeSessionData) throws SessionDataExistsException, ToolException {
		Wiki wiki = wikiDao.getByContentId(toolContentId);
		if(removeSessionData){
			List list = wikiToolSessionDao.getByContentId(toolContentId);
			Iterator iter = list.iterator();
			while(iter.hasNext()){
				WikiToolSession session = (WikiToolSession) iter.next();
				wikiToolSessionDao.delete(session);
			}
		}
		wikiDao.delete(wiki);
	}
	
    
	/**
     * Export the XML fragment for the tool's content, along with any files needed
     * for the content.
     * @throws DataMissingException if no tool content matches the toolSessionId 
     * @throws ToolException if any other error occurs
     */

	public void exportToolContent(Long toolContentId, String rootPath) throws DataMissingException, ToolException {
		Wiki toolContentObj = wikiDao.getByContentId(toolContentId);
 		if(toolContentObj == null)
 			toolContentObj = getDefaultWiki();
 		if(toolContentObj == null)
 			throw new DataMissingException("Unable to find default content for the wiki tool");
 		
 		//set ResourceToolContentHandler as null to avoid copy file node in repository again.
 		toolContentObj = Wiki.newInstance(toolContentObj,toolContentId,null);
 		toolContentObj.setToolContentHandler(null);
 		toolContentObj.setCreatedBy(null);
 		Set<Message> items = toolContentObj.getMessages();
 		Set<Message> authorItems = new HashSet<Message>();
		for(Message item:items){
			if(item.getIsAuthored()){
				authorItems.add(item);
				item.setCreatedBy(null);
				item.setModifiedBy(null);
				item.setToolSession(null);
				item.setWiki(null);
				item.setToolContentHandler(null);
				item.setReport(null);
				item.setReplyNumber(0);
				item.setParent(null);
				item.setSessionClones(null);
			}
		}
		toolContentObj.setMessages(authorItems);
		try {
			exportContentService.registerFileClassForExport(Attachment.class.getName(),"fileUuid","fileVersionId");
			exportContentService.exportToolContent( toolContentId, toolContentObj,wikiToolContentHandler, rootPath);
		} catch (ExportToolContentException e) {
			throw new ToolException(e);
		}	
	}

    /**
     * Import the XML fragment for the tool's content, along with any files needed
     * for the content.
     * @throws ToolException if any other error occurs
     */
	public void importToolContent(Long toolContentId, Integer newUserUid, String toolContentPath ,String fromVersion,String toVersion)
			throws ToolException {

		
		try {
			exportContentService.registerFileClassForImport(Attachment.class.getName()
					,"fileUuid","fileVersionId","fileName","fileType",null,null);
			
			Object toolPOJO =  exportContentService.importToolContent(toolContentPath,wikiToolContentHandler,fromVersion,toVersion);
			if(!(toolPOJO instanceof Wiki))
				throw new ImportToolContentException("Import Wiki tool content failed. Deserialized object is " + toolPOJO);
			Wiki toolContentObj = (Wiki) toolPOJO;
			
//			reset it to new toolContentId
			toolContentObj.setContentId(toolContentId);
			WikiUser user = wikiUserDao.getByUserId(new Long(newUserUid.longValue()));
			if(user == null){
				user = new WikiUser();
				UserDTO sysUser = ((User)userManagementService.findById(User.class,newUserUid)).getUserDTO();
				user.setFirstName(sysUser.getFirstName());
				user.setLastName(sysUser.getLastName());
				user.setLoginName(sysUser.getLogin());
				user.setUserId(new Long(newUserUid.longValue()));
				this.createUser(user);
			}
			toolContentObj.setCreatedBy(user);
			//save wiki first
			wikiDao.saveOrUpdate(toolContentObj);
			
			//save all authoring message one by one.
			//reset all resourceItem createBy user
			Set<Message> items = toolContentObj.getMessages();
			for(Message item:items){
				item.setCreatedBy(user);
				item.setIsAuthored(true);
				item.setWiki(toolContentObj);
				item.setSessionClones(new HashSet());
				createRootTopic(toolContentObj.getUid(),(WikiToolSession) null,item);
			}
		} catch (ImportToolContentException e) {
			throw new ToolException(e);
		}
	}

	  /** Get the definitions for possible output for an activity, based on the toolContentId. These may be definitions that are always
     * available for the tool (e.g. number of marks for Multiple Choice) or a custom definition created for a particular activity
     * such as the answer to the third question contains the word Koala and hence the need for the toolContentId
     * @return SortedMap of ToolOutputDefinitions with the key being the name of each definition
     */
	public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Long toolContentId) throws ToolException {
		Wiki wiki = getWiki(toolContentId);
		if ( wiki == null ) {
			wiki = getDefaultWiki();
		}
		return getWikiOutputFactory().getToolOutputDefinitions(wiki);
	}
 

	/** @see org.lamsfoundation.lams.tool.ToolSessionManager#createToolSession(java.lang.Long, java.lang.String, java.lang.Long) */
	public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException {
		WikiToolSession session = new WikiToolSession();
		session.setSessionId(toolSessionId);
		session.setSessionName(toolSessionName);
		Wiki wiki = wikiDao.getByContentId(toolContentId);
		session.setWiki(wiki);
		
//		also clone author created topic from this wiki tool content!!!
//		this can avoid topic record information conflict when multiple sessions are against same tool content
//		for example, the reply number maybe various for different sessions.
		log.debug("Clone tool content [" + wiki.getContentId() +"] topics for session [" + session.getSessionId() + "]");		
		Set<Message> contentTopics = wiki.getMessages();
		if(contentTopics != null && contentTopics.size() > 0){
			for(Message msg : contentTopics){
				if(msg.getIsAuthored() && msg.getToolSession() == null){
					Message newMsg = Message.newInstance(msg, wikiToolContentHandler);
					msg.getSessionClones().add(newMsg);
					createRootTopic(wiki.getContentId(), session, newMsg);
				}
			}
		}
		session.setStatus(WikiConstants.STATUS_CONTENT_COPYED);

		wikiToolSessionDao.saveOrUpdate(session);
		log.debug("tool session ["+session.getSessionId()+"] created.");
	}

	public String leaveToolSession(Long toolSessionId, Long learnerId) throws DataMissingException, ToolException {
		if(toolSessionId == null){
			log.error("Fail to leave tool Session based on null tool session id.");
			throw new ToolException("Fail to remove tool Session based on null tool session id.");
		}
		if(learnerId == null){
			log.error("Fail to leave tool Session based on null learner.");
			throw new ToolException("Fail to remove tool Session based on null learner.");
		}
		
		WikiToolSession session = wikiToolSessionDao.getBySessionId(toolSessionId);
		if(session != null){
			wikiToolSessionDao.saveOrUpdate(session);
		}else{
			log.error("Fail to leave tool Session.Could not find submit file " +
					"session by given session id: "+toolSessionId);
			throw new DataMissingException("Fail to leave tool Session." +
					"Could not find submit file session by given session id: "+toolSessionId);
		}
		return learnerService.completeToolSession(toolSessionId,learnerId);
	}
	public ToolSessionExportOutputData exportToolSession(Long toolSessionId) throws DataMissingException, ToolException {
		return null;
	}

	public ToolSessionExportOutputData exportToolSession(List toolSessionIds) throws DataMissingException, ToolException {
		return null;
	}

	
	public void removeToolSession(Long toolSessionId) throws DataMissingException, ToolException {
		wikiToolSessionDao.delete(toolSessionId);
	}
    
	/** 
	 * Get the tool output for the given tool output names.
	 * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.util.List<String>, java.lang.Long, java.lang.Long)
	 */
	public SortedMap<String, ToolOutput> getToolOutput(List<String> names,
			Long toolSessionId, Long learnerId) {
		
		return wikiOutputFactory.getToolOutput(names, this, toolSessionId, learnerId);

	}

	/** 
	 * Get the tool output for the given tool output name.
	 * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.lang.String, java.lang.Long, java.lang.Long)
	 */
	public ToolOutput getToolOutput(String name, Long toolSessionId,
			Long learnerId) {
		return wikiOutputFactory.getToolOutput(name, this, toolSessionId, learnerId);
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.sbmt.service.ISubmitFilesService#getDefaultContent(java.lang.Long)
     */
	public Wiki getDefaultContent(Long contentID) {
    	if (contentID == null)
    	{
    	    String error="Could not retrieve default content id for Wiki tool";
    	    log.error(error);
    	    throw new WikiException(error);
    	}
    	
    	Wiki defaultContent = getDefaultWiki();
    	//get default content by given ID.
    	Wiki content = new Wiki();
    	content = Wiki.newInstance(defaultContent,contentID,wikiToolContentHandler);
    	
		Set topics = content.getMessages();
		if(topics != null){
			Iterator iter = topics.iterator();
			while(iter.hasNext()){
				Message msg = (Message) iter.next();
				//clear message wiki so that they can be saved when persistent happens
				msg.setWiki(null);
			}
		}

		return content;
	}
	public List<MessageDTO> getAllTopicsFromSession(Long sessionID) {
		return MessageDTO.getMessageDTO(wikiMessageDao.getBySession(sessionID));
	}
	/* ===============Methods implemented from ToolContentImport102Manager =============== */
	

    /**
     * Import the data for a 1.0.2 Wiki
     */
    public void import102ToolContent(Long toolContentId, UserDTO user, Hashtable importValues)
    {
    	Date now = new Date();
    	Wiki toolContentObj = new Wiki();

    	try {
	
	    	toolContentObj.setTitle((String)importValues.get(ToolContentImport102Manager.CONTENT_TITLE));

	    	toolContentObj.setAllowAnonym(Boolean.FALSE);
	    	toolContentObj.setAllowEdit(Boolean.TRUE); // this is the default value
	    	toolContentObj.setAllowNewTopic(Boolean.TRUE);
	    	//toolContentObj.setAllowRichEditor(Boolean.FALSE);
	    	toolContentObj.setAllowUpload(Boolean.TRUE); // this is the default value
	    	toolContentObj.setContentId(toolContentId);
	    	toolContentObj.setContentInUse(Boolean.FALSE);
	    	toolContentObj.setCreated(now);
	    	toolContentObj.setDefineLater(Boolean.FALSE);		    	
	    	toolContentObj.setInstructions(WebUtil.convertNewlines((String)importValues.get(ToolContentImport102Manager.CONTENT_BODY)));
	    	toolContentObj.setLimitedChar(5000); // this is the default value
   	    	toolContentObj.setReflectOnActivity(Boolean.FALSE);
	    	toolContentObj.setReflectInstructions(null);

	    	// lockOnFinsh = ! isReusable
	    	Boolean bool = WDDXProcessor.convertToBoolean(importValues, ToolContentImport102Manager.CONTENT_REUSABLE);
	    	toolContentObj.setLockWhenFinished(bool != null ? ! bool.booleanValue() : false);
	    	toolContentObj.setMaximumReply(0);
	    	toolContentObj.setMinimumReply(0);
	    	toolContentObj.setOfflineInstructions(null);
	    	toolContentObj.setOnlineInstructions(null);
	    	toolContentObj.setRunOffline(Boolean.FALSE);
	    	toolContentObj.setUpdated(now);

	    	// unused entries from 1.0.2
            // isNewTopicAllowed  - not actually used in 1.0.2
	    	// durationInDays - no equivalent in 2.0
	    	// isPostingModerated - no equivalent in 2.0
	    	// isPostingNotified - no equivalent in 2.0
            // contentShowUser - no equivalent in 2.0
            // isHTML - no equivalent in 2.0
            // terminationType=moderator - no equivalent in 2.0

	    	WikiUser wikiUser = new WikiUser();
			wikiUser.setUserId(new Long(user.getUserID().longValue()));
			wikiUser.setFirstName(user.getFirstName());
			wikiUser.setLastName(user.getLastName());
			wikiUser.setLoginName(user.getLogin());
			createUser(wikiUser);
	    	toolContentObj.setCreatedBy(wikiUser);
	
	    	// leave as empty, no need to set them to anything.
	    	//toolContentObj.setAttachments(attachments);
	    	wikiDao.saveOrUpdate(toolContentObj);
	
	    	// topics in the XML file are ordered using the "number" field, not in their order in the vector.
	    	TreeMap<Integer, Map> messageMaps = new TreeMap<Integer, Map>();
	    	Vector topics = (Vector) importValues.get(ToolContentImport102Manager.CONTENT_MB_TOPICS);
			Date msgDate = null;
	    	if ( topics != null ) {
	    		Iterator iter = topics.iterator();
	    		while ( iter.hasNext() ) {
	    			Hashtable messageMap = (Hashtable) iter.next();
	    	    	Integer order = WDDXProcessor.convertToInteger(messageMap, ToolContentImport102Manager.CONTENT_MB_TOPIC_NUMBER);
	    			messageMaps.put(order, messageMap);
	    		}
	    		
	        	iter = messageMaps.values().iterator();
	        	while ( iter.hasNext() ) {
	        			
	    			Map messageMap = (Map) iter.next();
	
	    			Message message = new Message();
	    			message.setIsAuthored(true);
	    			
	    			// topics are ordered by date, so I need to try to assign each entry a different date. Won't work if this is too quick.
	    			if ( msgDate != null ) {
	    				try {
	    					Thread.sleep(1000);
	    				} catch (Exception e) {}
	    			}
	    			msgDate = new Date();
	
	    			message.setCreated(msgDate);
	    			message.setCreatedBy(wikiUser);
	    			message.setUpdated(msgDate);
	    			message.setLastReplyDate(msgDate);
	    			message.setSubject((String)messageMap.get(ToolContentImport102Manager.CONTENT_TITLE));
	    			message.setBody(WebUtil.convertNewlines((String)messageMap.get(ToolContentImport102Manager.CONTENT_MB_TOPIC_MESSAGE)));
	    			// ignore the old subject field - it wasn't updated by the old interface.
	    			message.setHideFlag(Boolean.FALSE);
	    			message.setIsAnonymous(Boolean.FALSE);
	    			
	    			createRootTopic(toolContentObj.getUid(),(WikiToolSession) null,message);
	    			
	        	}
	    	}
	    	
		} catch (WDDXProcessorConversionException e) {
			log.error("Unable to content for activity "+toolContentObj.getTitle()+"properly due to a WDDXProcessorConversionException.",e);
			throw new ToolException("Invalid import data format for activity "+toolContentObj.getTitle()+"- WDDX caused an exception. Some data from the design will have been lost. See log for more details.");
		}



    }

   /** Set the description, throws away the title value as this is not supported in 2.0 */
   public void setReflectiveData(Long toolContentId, String title, String description) 
    		throws ToolException, DataMissingException {
   
	   Wiki toolContentObj = getWikiByContentId(toolContentId);
    	if ( toolContentObj == null ) {
    		throw new DataMissingException("Unable to set reflective data titled "+title
	       			+" on activity toolContentId "+toolContentId
	       			+" as the tool content does not exist.");
    	}

    	toolContentObj.setReflectOnActivity(Boolean.TRUE);
    	toolContentObj.setReflectInstructions(description);
    }

    //***************************************************************************************************************
    // Get / Set methods
    //***************************************************************************************************************
	public ILamsToolService getToolService() {
		return toolService;
	}

	public void setToolService(ILamsToolService toolService) {
		this.toolService = toolService;
	}
	
	public WikiAttachmentDao getWikiAttachmentDao() {
		return wikiAttachmentDao;
	}

	public void setWikiAttachmentDao(WikiAttachmentDao wikiAttachmentDao) {
		this.wikiAttachmentDao = wikiAttachmentDao;
	}

	public WikiDao getWikiDao() {
		return wikiDao;
	}

	public void setWikiDao(WikiDao wikiDao) {
		this.wikiDao = wikiDao;
	}

	public WikiMessageDao getWikiMessageDao() {
		return wikiMessageDao;
	}

	public void setWikiMessageDao(WikiMessageDao wikiMessageDao) {
		this.wikiMessageDao = wikiMessageDao;
	}


	public WikiMessageSeqDao getWikiMessageSeqDao() {
		return wikiMessageSeqDao;
	}

	public void setWikiMessageSeqDao(WikiMessageSeqDao wikiMessageSeqDao) {
		this.wikiMessageSeqDao = wikiMessageSeqDao;
	}

	public WikiToolSessionDao getWikiToolSessionDao() {
		return wikiToolSessionDao;
	}

	public void setWikiToolSessionDao(WikiToolSessionDao wikiToolSessionDao) {
		this.wikiToolSessionDao = wikiToolSessionDao;
	}

	public WikiUserDao getWikiUserDao() {
		return wikiUserDao;
	}

	public void setWikiUserDao(WikiUserDao wikiUserDao) {
		this.wikiUserDao = wikiUserDao;
	}

	public IRepositoryService getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(IRepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}
	public WikiToolContentHandler getWikiToolContentHandler() {
		return wikiToolContentHandler;
	}

	public void setWikiToolContentHandler(WikiToolContentHandler toolContentHandler) {
		this.wikiToolContentHandler = toolContentHandler;
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
	public IUserManagementService getUserManagementService() {
		return userManagementService;
	}


	public void setUserManagementService(IUserManagementService userManagementService) {
		this.userManagementService = userManagementService;
	}

	public WikiReportDAO getWikiReportDAO() {
		return wikiReportDAO;
	}

	public void setWikiReportDAO(WikiReportDAO wikiReportDAO) {
		this.wikiReportDAO = wikiReportDAO;
	}

	public ICoreNotebookService getCoreNotebookService() {
		return coreNotebookService;
	}

	public void setCoreNotebookService(ICoreNotebookService coreNotebookService) {
		this.coreNotebookService = coreNotebookService;
	}


}
