package org.lamsfoundation.lams.tool.forum.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.commons.beanutils.PropertyUtils;
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
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.SessionDataExistsException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.forum.core.PersistenceException;
import org.lamsfoundation.lams.tool.forum.dto.MessageDTO;
import org.lamsfoundation.lams.tool.forum.persistence.Attachment;
import org.lamsfoundation.lams.tool.forum.persistence.AttachmentDao;
import org.lamsfoundation.lams.tool.forum.persistence.Forum;
import org.lamsfoundation.lams.tool.forum.persistence.ForumDao;
import org.lamsfoundation.lams.tool.forum.persistence.ForumException;
import org.lamsfoundation.lams.tool.forum.persistence.ForumToolSession;
import org.lamsfoundation.lams.tool.forum.persistence.ForumToolSessionDao;
import org.lamsfoundation.lams.tool.forum.persistence.ForumUser;
import org.lamsfoundation.lams.tool.forum.persistence.ForumUserDao;
import org.lamsfoundation.lams.tool.forum.persistence.Message;
import org.lamsfoundation.lams.tool.forum.persistence.MessageDao;
import org.lamsfoundation.lams.tool.forum.persistence.MessageSeq;
import org.lamsfoundation.lams.tool.forum.persistence.MessageSeqDao;
import org.lamsfoundation.lams.tool.forum.util.ForumConstants;
import org.lamsfoundation.lams.tool.forum.util.ForumToolContentHandler;
import org.lamsfoundation.lams.tool.forum.util.LastReplayDateComparator;
import org.lamsfoundation.lams.tool.forum.util.TopicComparator;
import org.lamsfoundation.lams.usermanagement.User;


/**
 * 
 * @author Steve.Ni
 * 
 * @version $Revision$
 */
public class ForumService implements IForumService,ToolContentManager,ToolSessionManager {
	private static final Logger log = Logger.getLogger(ForumService.class);
	//DAO variables
	private ForumDao forumDao;
	private AttachmentDao attachmentDao;
	private MessageDao messageDao;
	private MessageSeqDao messageSeqDao;
	private ForumUserDao forumUserDao;
	private ForumToolSessionDao toolSessionDao;
	
	//system level handler and service 
	private ForumToolContentHandler toolContentHandler;
	private IRepositoryService repositoryService;
	

    public Forum editForum(Forum forum) throws PersistenceException {
        forumDao.saveOrUpdate(forum);
        return forum;
    }

    public Forum getForum(Long forumId) throws PersistenceException {
        return (Forum) forumDao.getById(forumId);
    }

	public Forum getForumByContentId(Long contentID)  throws PersistenceException {
		Forum forum = (Forum) forumDao.getByContentId(contentID);
		if(forum == null){
			log.error("Could not find the content by given ID:"+contentID);
		}
		return forum; 
	}
    public void deleteForum(Long forumId) throws PersistenceException {
        Forum forum = this.getForum(forumId);
        forumDao.delete(forum);
    }

    public List getTopics(Long forumId) throws PersistenceException {
        return messageDao.allAuthoredMessage(forumId);
    }

    public void deleteForumAttachment(Long attachmentId) throws PersistenceException {
        Attachment attachment = (Attachment) attachmentDao.getById(attachmentId);
        attachmentDao.delete(attachment);

    }

    public Message createRootTopic(Long forumId, Long sessionId, Message message) throws PersistenceException {
    	//get Forum and ForumToolSesion
    	Forum forum = getForumByContentId(forumId);
    	message.setForum(forum);
    	//if topic created by author, sessionId will be null.
    	if(sessionId != null){
    		ForumToolSession session = getSessionBySessionId(sessionId);
    		message.setToolSession(session);
    	}
    	//create message in database
        messageDao.save(message);
        
        //update message sequence
        MessageSeq msgSeq = new MessageSeq();
        msgSeq.setMessage(message);
        msgSeq.setMessageLevel((short) 0);
        //set itself as root
        msgSeq.setRootMessage(message);
        messageSeqDao.save(msgSeq);
        
        return message;
    }

     public Message updateTopic(Message message) throws PersistenceException {
    	 //update message
    	messageDao.saveOrUpdate(message);
    	
    	//udate root message's lastReply date if this message
    	//if this message is root message, then actually, it will update itself lastReplayDate
    	MessageSeq msgSeq = messageSeqDao.getByTopicId(message.getUid());
    	Message root = msgSeq.getRootMessage();
        //update last reply date for root message
        root.setLastReplyDate(new Date());
        messageDao.saveOrUpdate(root);
        
        return message;
    }

    public Message getMessage(Long messageId) throws PersistenceException {
        return (Message) messageDao.getById(messageId);
    }

    public void deleteTopic(Long topicUid) throws PersistenceException {
        messageDao.deleteById(topicUid);
        messageSeqDao.deleteByTopicId(topicUid);
     }

    public Message replyTopic(Long parentId, Message replyMessage) throws PersistenceException {
    	//set parent
        Message parent = this.getMessage(parentId);
        replyMessage.setParent(parent);
        messageDao.saveOrUpdate(replyMessage);
        
        //get root topic and create record in MessageSeq table
        MessageSeq parentSeq = messageSeqDao.getByTopicId(parent.getUid());
        if(parentSeq == null){
        	log.error("Message Sequence table is broken becuase topic " + parent +" can not get Sequence Record");
        }
        Message root = parentSeq.getRootMessage();
        MessageSeq msgSeq = new MessageSeq();
        msgSeq.setMessage(replyMessage);
        msgSeq.setMessageLevel((short) (parentSeq.getMessageLevel() + 1));
        msgSeq.setRootMessage(root);
        messageSeqDao.save(msgSeq);
        
        //update last reply date for root message
        root.setLastReplyDate(new Date());
        messageDao.saveOrUpdate(root);
        
        return replyMessage;
    }

	public AttachmentDao getAttachmentDao() {
		return attachmentDao;
	}

	public void setAttachmentDao(AttachmentDao attachmentDao) {
		this.attachmentDao = attachmentDao;
	}

	public ForumDao getForumDao() {
		return forumDao;
	}

	public void setForumDao(ForumDao forumDao) {
		this.forumDao = forumDao;
	}

	public MessageDao getMessageDao() {
		return messageDao;
	}

	public void setMessageDao(MessageDao messageDao) {
		this.messageDao = messageDao;
	}

	public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException {
	}

	public void setAsDefineLater(Long toolContentId) throws DataMissingException, ToolException {
	}

	public void setAsRunOffline(Long toolContentId) throws DataMissingException, ToolException {
	}

	public void removeToolContent(Long toolContentId, boolean removeSessionData) throws SessionDataExistsException, ToolException {
	}

	public void createToolSession(Long toolSessionId, Long toolContentId) throws ToolException {
	}

	public String leaveToolSession(Long toolSessionId, User learner) throws DataMissingException, ToolException {
		return null;
	}

	public ToolSessionExportOutputData exportToolSession(Long toolSessionId) throws DataMissingException, ToolException {
		return null;
	}

	public ToolSessionExportOutputData exportToolSession(List toolSessionIds) throws DataMissingException, ToolException {
		return null;
	}

	public void removeToolSession(Long toolSessionId) throws DataMissingException, ToolException {
	}

	public Attachment uploadInstructionFile(Long contentId, FormFile uploadFile, String fileType) throws PersistenceException{
		Attachment refile = null;
		if(uploadFile == null || StringUtils.isEmpty(uploadFile.getFileName()))
			throw new ForumException("Could not find upload file: " + uploadFile);
		
        Forum content = getForumByContentId(contentId);
        if ( content == null || !contentId.equals(content.getContentId())) {
        	content  = new Forum();
        	content.setContentId(contentId);
        	//user firstly upload file without any other input, even the not-null 
        	//field "title". Set title as default title.
        	content.setTitle(ForumConstants.DEFAULT_TITLE);
        }
		NodeKey nodeKey = processFile(uploadFile,fileType);
		
		Set fileSet = content.getAttachments();
		if(fileSet == null){
			fileSet = new HashSet();
			content.setAttachments(fileSet);
		}
		Attachment file = new Attachment();
		file.setFileType(fileType);
		file.setFileUuid(nodeKey.getUuid());
		file.setFileVersionId(nodeKey.getVersion());
		file.setFileName(uploadFile.getFileName());
		fileSet.add(file);
		forumDao.saveOrUpdate(content);
		
		refile = new Attachment();
		try {
			PropertyUtils.copyProperties(refile,file);
		} catch (Exception e) {
			throw new ForumException("Could not get return InstructionFile instance" +e.getMessage());
		}
		return refile;

	}
    /**
     * Process an uploaded file.
     * 
     * @param forumForm
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
				node = getToolContentHandler().uploadFile(file.getInputStream(), fileName, 
				        file.getContentType(), fileType);
			} catch (InvalidParameterException e) {
				throw new ForumException("FileNotFoundException occured while trying to upload File" + e.getMessage());
			} catch (FileNotFoundException e) {
				throw new ForumException("FileNotFoundException occured while trying to upload File" + e.getMessage());
			} catch (RepositoryCheckedException e) {
				throw new ForumException("FileNotFoundException occured while trying to upload File" + e.getMessage());
			} catch (IOException e) {
				throw new ForumException("FileNotFoundException occured while trying to upload File" + e.getMessage());
			}
          }
        return node;
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
			throws ForumException {
		ITicket ticket = getRepositoryLoginTicket();
		try {
			repositoryService.deleteVersion(ticket, uuid,versionID);
		} catch (Exception e) {
			throw new ForumException(
					"Exception occured while deleting files from"
							+ " the repository " + e.getMessage());
		}
	}

	public void deleteInstructionFile(Long contentID, Long uuid, Long versionID, String type){
		forumDao.deleteInstrcutionFile(contentID, uuid, versionID, type);
	}

	public ForumToolContentHandler getToolContentHandler() {
		return toolContentHandler;
	}

	public void setToolContentHandler(ForumToolContentHandler toolContentHandler) {
		this.toolContentHandler = toolContentHandler;
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
	private ITicket getRepositoryLoginTicket() throws ForumException {
		ICredentials credentials = new SimpleCredentials(
				toolContentHandler.getRepositoryUser(),
				toolContentHandler.getRepositoryId());
		try {
			ITicket ticket = repositoryService.login(credentials,
					toolContentHandler.getRepositoryWorkspaceName());
			return ticket;
		} catch (AccessDeniedException ae) {
			throw new ForumException("Access Denied to repository."
					+ ae.getMessage());
		} catch (WorkspaceNotFoundException we) {
			throw new ForumException("Workspace not found."
					+ we.getMessage());
		} catch (LoginException e) {
			throw new ForumException("Login failed." + e.getMessage());
		}
	}

	public IRepositoryService getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(IRepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	public Attachment uploadAttachment(FormFile uploadFile) throws PersistenceException {
		if(uploadFile == null || StringUtils.isEmpty(uploadFile.getFileName()))
			throw new ForumException("Could not find upload file: " + uploadFile);
		
		NodeKey nodeKey = processFile(uploadFile,IToolContentHandler.TYPE_ONLINE);
		Attachment file = new Attachment();
		file.setFileType(IToolContentHandler.TYPE_ONLINE);
		file.setFileUuid(nodeKey.getUuid());
		file.setFileVersionId(nodeKey.getVersion());
		file.setFileName(uploadFile.getFileName());
		
		return file;
	}
	public List getTopicThread(Long rootTopicId){
		
		List unsortedThread =  messageSeqDao.getTopicThread(rootTopicId);
		Iterator iter = unsortedThread.iterator();
		MessageSeq msgSeq;
		SortedMap map = new TreeMap(new TopicComparator());
		while(iter.hasNext()){
			msgSeq = (MessageSeq) iter.next();
			map.put(msgSeq,msgSeq.getMessage());
		}
		return 	getSortedMessageDTO(map);
	}


	public List getRootTopics(Long sessionId){
		List topicsBySession =  messageDao.getRootTopics(sessionId);
		ForumToolSession session = getSessionBySessionId(sessionId);
		if(session == null || session.getForum() == null){
			log.error("Failed on getting session by given sessionID:" + sessionId);
			throw new ForumException("Failed on getting session by given sessionID:" + sessionId);
		}
		List topicsFromAuthor = messageDao.getTopicsFromAuthor(session.getForum().getUid());
		
		//sorted by last post date
		Message msg;
		SortedMap map = new TreeMap(new LastReplayDateComparator());
		Iterator iter = topicsBySession.iterator();
		while(iter.hasNext()){
			msg = (Message) iter.next();
			map.put(msg.getLastReplyDate(),msg);
		}
		iter = topicsFromAuthor.iterator();
		while(iter.hasNext()){
			msg = (Message) iter.next();
			map.put(msg.getLastReplyDate(),msg);
		}
		return 	new ArrayList(map.values());
		
	}
	public Forum createForum(Long contentId) throws PersistenceException {
		return null;
	}

	public ForumUser getUserByUserId(Long userId) {
		List list =  forumUserDao.getUserByUserId(userId);
		if(list == null || list.isEmpty())
			return null;
		
		return (ForumUser) list.get(0);
	}

	public void createUser(ForumUser forumUser) {
		forumUserDao.save(forumUser);
	}

	public ForumUserDao getForumUserDao() {
		return forumUserDao;
	}

	public void setForumUserDao(ForumUserDao forumUserDao) {
		this.forumUserDao = forumUserDao;
	}

	public ForumToolSession getSessionBySessionId(Long sessionId) {
		return toolSessionDao.getBySessionId(sessionId);
	}
	
	/**
	 * @param map
	 * @return
	 */
	private List getSortedMessageDTO(SortedMap map) {
		Iterator iter;
		MessageSeq msgSeq;
		List msgDtoList = new ArrayList();
		iter =map.keySet().iterator();
		while(iter.hasNext()){
			Map.Entry entry = (Entry) iter.next();
			msgSeq = (MessageSeq) entry.getKey();
			MessageDTO dto = MessageDTO.getMessageDTO((Message) entry.getValue());
			dto.setLevel(msgSeq.getMessageLevel());
			msgDtoList.add(dto);
		}
		return msgDtoList;
	}
}
