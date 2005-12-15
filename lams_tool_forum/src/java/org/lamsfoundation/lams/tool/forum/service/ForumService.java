package org.lamsfoundation.lams.tool.forum.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
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
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.SessionDataExistsException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.forum.dto.MessageDTO;
import org.lamsfoundation.lams.tool.forum.persistence.Attachment;
import org.lamsfoundation.lams.tool.forum.persistence.AttachmentDao;
import org.lamsfoundation.lams.tool.forum.persistence.Forum;
import org.lamsfoundation.lams.tool.forum.persistence.ForumDao;
import org.lamsfoundation.lams.tool.forum.persistence.ForumException;
import org.lamsfoundation.lams.tool.forum.persistence.ForumReport;
import org.lamsfoundation.lams.tool.forum.persistence.ForumToolSession;
import org.lamsfoundation.lams.tool.forum.persistence.ForumToolSessionDao;
import org.lamsfoundation.lams.tool.forum.persistence.ForumUser;
import org.lamsfoundation.lams.tool.forum.persistence.ForumUserDao;
import org.lamsfoundation.lams.tool.forum.persistence.Message;
import org.lamsfoundation.lams.tool.forum.persistence.MessageDao;
import org.lamsfoundation.lams.tool.forum.persistence.MessageSeq;
import org.lamsfoundation.lams.tool.forum.persistence.MessageSeqDao;
import org.lamsfoundation.lams.tool.forum.persistence.PersistenceException;
import org.lamsfoundation.lams.tool.forum.util.DateComparator;
import org.lamsfoundation.lams.tool.forum.util.ForumConstants;
import org.lamsfoundation.lams.tool.forum.util.ForumToolContentHandler;
import org.lamsfoundation.lams.tool.forum.util.TopicComparator;
import org.lamsfoundation.lams.tool.service.ILamsToolService;

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
	private ForumToolSessionDao forumToolSessionDao;
	//system level handler and service 
	private ILamsToolService toolService;
	private ForumToolContentHandler forumToolContentHandler;
	private IRepositoryService repositoryService;
	private ILearnerService learnerService;

    public Forum updateForum(Forum forum) throws PersistenceException {
        forumDao.saveOrUpdate(forum);
        return forum;
    }

    public Forum getForum(Long forumUid) throws PersistenceException {
        return (Forum) forumDao.getById(forumUid);
    }

	public Forum getForumByContentId(Long contentID)  throws PersistenceException {
		Forum forum = (Forum) forumDao.getByContentId(contentID);
		if(forum == null){
			log.error("Could not find the content by given ID:"+contentID);
		}
		return forum; 
	}

    public void deleteForumAttachment(Long attachmentId) throws PersistenceException {
        Attachment attachment = (Attachment) attachmentDao.getById(attachmentId);
        attachmentDao.delete(attachment);
    }

    public Message createRootTopic(Long forumId, Long sessionId, Message message) throws PersistenceException {
    	//get Forum and ForumToolSesion
    	if(message.getForum() == null){
    		Forum forum = new Forum();
    		forum.setUid(forumId);
    		message.setForum(forum);
    	}
    	//if topic created by author, sessionId will be null.
    	if(sessionId != null){
    		ForumToolSession session = getSessionBySessionId(sessionId);
    		message.setToolSession(session);
    	}
    	
    	if(message.getUid() == null){
	    	//update message sequence
	    	MessageSeq msgSeq = new MessageSeq();
	    	msgSeq.setMessage(message);
	    	msgSeq.setMessageLevel((short) 0);
	    	//set itself as root
	    	msgSeq.setRootMessage(message);
	    	messageSeqDao.save(msgSeq);
    	}
    	//create message in database
        messageDao.saveOrUpdate(message);
        
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

    public Message getMessage(Long messageUid) throws PersistenceException {
        return (Message) messageDao.getById(messageUid);
    }

    public void deleteTopic(Long topicUid) throws PersistenceException {
    	List children = messageDao.getChildrenTopics(topicUid);
    	//cascade delete children topic by recursive
    	if(children != null){
    		Iterator iter = children.iterator();
    		while(iter.hasNext()){
    			Message msg = (Message) iter.next();
    			this.deleteTopic(msg.getUid());
    		}
    	}
    	messageSeqDao.deleteByTopicId(topicUid);
        messageDao.deleteById(topicUid);
     }

    public Message replyTopic(Long parentId,Long sessionId, Message replyMessage) throws PersistenceException {
    	//set parent
        Message parent = this.getMessage(parentId);
        replyMessage.setParent(parent);
        replyMessage.setForum(parent.getForum());
        //parent sessionID maybe empty if created by author role. So given sessionId is exactly value.
        ForumToolSession session = getSessionBySessionId(sessionId);
        replyMessage.setToolSession(session);
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
        //update reply message number for root 
        root.setReplyNumber(root.getReplyNumber()+1);
        messageDao.saveOrUpdate(root);
        
        return replyMessage;
    }

	public Attachment uploadInstructionFile(FormFile uploadFile, String fileType) throws PersistenceException{
		if(uploadFile == null || StringUtils.isEmpty(uploadFile.getFileName()))
			throw new ForumException("Could not find upload file: " + uploadFile);
		
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
		SortedMap map = new TreeMap(new DateComparator());
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
		return 	MessageDTO.getMessageDTO(new ArrayList(map.values()));
		
	}

	public ForumUser getUserByID(Long userId) {
		return  forumUserDao.getByUserId(userId);
	}

	public ForumUser getUserByUserAndSession(Long userId,Long sessionId) {
		return  forumUserDao.getByUserIdAndSessionId(userId,sessionId);
	}

	public void createUser(ForumUser forumUser) {
		forumUserDao.save(forumUser);
	}
	public ForumToolSession getSessionBySessionId(Long sessionId) {
		return forumToolSessionDao.getBySessionId(sessionId);
	}

	public Long getRootTopicId(Long topicId) {
		MessageSeq seq = messageSeqDao.getByTopicId(topicId);
		if(seq == null ||seq.getRootMessage() == null){
			log.error("A sequence information can not be found for topic ID:" + topicId);
			return null;
		}
		return seq.getRootMessage().getUid();
	}

	public List getAuthoredTopics(Long forumUid) {
		List list = messageDao.getTopicsFromAuthor(forumUid);
		
		TreeMap map = new TreeMap(new DateComparator());
		//sorted by create date
		Iterator iter = list.iterator();
		while(iter.hasNext()){
			Message topic = (Message) iter.next();
			map.put(topic.getCreated(),topic);
		}
		return MessageDTO.getMessageDTO(new ArrayList(map.values()));
	}

	public void updateSession(ForumToolSession session) {
		forumToolSessionDao.saveOrUpdate(session);
	}

    public Long getToolDefaultContentIdBySignature(String toolSignature)
    {
        Long contentId = null;
    	contentId=new Long(toolService.getToolDefaultContentIdBySignature(toolSignature));    
    	if (contentId == null)
    	{
    	    String error="Could not retrieve default content id for this tool";
    	    log.error(error);
    	    throw new ForumException(error);
    	}
	    return contentId;
    }

	public List getSessionsByContentId(Long contentID) {
		return forumToolSessionDao.getByContentId(contentID);
	}

	public List getUsersBySessionId(Long sessionID) {
		return forumUserDao.getBySessionId(sessionID);
	}
    
	public List getMessagesByUserUid(Long userId,Long sessionId) {
		List list = messageDao.getByUserAndSession(userId,sessionId);
		
		return MessageDTO.getMessageDTO(list);
	}

	public ForumUser getUser(Long userUid) {
		return forumUserDao.getByUid(userUid);
	}

	public void releaseMarksForSession(Long sessionID) {
		List list = messageDao.getBySession(sessionID);
		Iterator iter = list.iterator();
		while(iter.hasNext()){
			Message msg = (Message) iter.next();
			ForumReport report = msg.getReport();
			if(report != null)
				report.setDateMarksReleased(new Date());
			messageDao.saveOrUpdate(msg);
		}
	}

    //***************************************************************************************************************
    // Private methods
    //***************************************************************************************************************
	/**
	 * @param map
	 * @return
	 */
	private List getSortedMessageDTO(SortedMap map) {
		Iterator iter;
		MessageSeq msgSeq;
		List msgDtoList = new ArrayList();
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
				node = getForumToolContentHandler().uploadFile(file.getInputStream(), fileName, 
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
				forumToolContentHandler.getRepositoryUser(),
				forumToolContentHandler.getRepositoryId());
		try {
			ITicket ticket = repositoryService.login(credentials,
					forumToolContentHandler.getRepositoryWorkspaceName());
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
    private Forum getDefaultForum(){
    	Long defaultForumId = getToolDefaultContentIdBySignature(ForumConstants.TOOLSIGNNATURE);
    	Forum defaultForum = getForumByContentId(defaultForumId);
    	if(defaultForum == null)
    	{
    	    String error="Could not retrieve default content record for this tool";
    	    log.error(error);
    	    throw new ForumException(error);
    	}
    	
    	return defaultForum;

    }


    //***************************************************************************************************************
    // ToolContentManager and ToolSessionManager methods
    //***************************************************************************************************************
	public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException {
		if (fromContentId == null || toContentId == null)
			throw new ToolException(
					"Failed to create the SubmitFiles tool seession");

		Forum fromContent = forumDao.getByContentId(fromContentId);
		if ( fromContent == null ) {
			fromContent = getDefaultContent(fromContentId);
		}
		Forum toContent = Forum.newInstance(fromContent,toContentId,forumToolContentHandler);
		forumDao.saveOrUpdate(toContent);
		
		//save topics in this forum
		Set topics = toContent.getMessages();
		if(topics != null){
			Iterator iter = topics.iterator();
			while(iter.hasNext()){
				Message msg = (Message) iter.next();
				//set this message forum Uid as toContent
				msg.setForum(toContent);
				createRootTopic(toContent.getUid(),null,msg);
			}
		}

	}

	public void setAsDefineLater(Long toolContentId) throws DataMissingException, ToolException {
		Forum forum = forumDao.getByContentId(toolContentId);
		if(forum == null){
			throw new ToolException("No found tool content by given content ID:" + toolContentId);
		}
		forum.setDefineLater(true);
		forumDao.flush();
	}

	public void setAsRunOffline(Long toolContentId) throws DataMissingException, ToolException {
		Forum forum = forumDao.getByContentId(toolContentId);
		if(forum == null){
			throw new ToolException("No found tool content by given content ID:" + toolContentId);
		}
		forum.setRunOffline(true);
		forumDao.flush();
	}

	public void removeToolContent(Long toolContentId, boolean removeSessionData) throws SessionDataExistsException, ToolException {
		Forum forum = forumDao.getByContentId(toolContentId);
		if(removeSessionData){
			List list = forumToolSessionDao.getByContentId(toolContentId);
			Iterator iter = list.iterator();
			while(iter.hasNext()){
				ForumToolSession session = (ForumToolSession) iter.next();
				forumToolSessionDao.delete(session);
			}
		}
		forumDao.delete(forum);
	}

	public void createToolSession(Long toolSessionId, Long toolContentId) throws ToolException {
		ForumToolSession session = new ForumToolSession();
		session.setSessionId(toolSessionId);
		Forum forum = forumDao.getByContentId(toolContentId);
		session.setForum(forum);
		forumToolSessionDao.saveOrUpdate(session);
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
		
		ForumToolSession session = forumToolSessionDao.getBySessionId(toolSessionId);
		if(session != null){
			session.setStatus(ForumConstants.COMPLETED);
			forumToolSessionDao.saveOrUpdate(session);
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
		forumToolSessionDao.delete(toolSessionId);
	}
    
    /* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.sbmt.service.ISubmitFilesService#getDefaultContent(java.lang.Long)
     */
	public Forum getDefaultContent(Long contentID) {
    	if (contentID == null)
    	{
    	    String error="Could not retrieve default content id for Forum tool";
    	    log.error(error);
    	    throw new ForumException(error);
    	}
    	
    	Forum defaultContent = getDefaultForum();
    	//save default content by given ID.
    	Forum content = new Forum();
    	content = Forum.newInstance(defaultContent,contentID,forumToolContentHandler);
		//save topics in this forum
		Set topics = content.getMessages();
		if(topics != null){
			Iterator iter = topics.iterator();
			while(iter.hasNext()){
				Message msg = (Message) iter.next();
				//clear message forum so that they can be saved when persistent happens
				msg.setForum(null);
			}
		}

		return content;
	}
	public List getAllTopicsFromSession(Long sessionID) {
		return MessageDTO.getMessageDTO(messageDao.getBySession(sessionID));
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


	public MessageSeqDao getMessageSeqDao() {
		return messageSeqDao;
	}

	public void setMessageSeqDao(MessageSeqDao messageSeqDao) {
		this.messageSeqDao = messageSeqDao;
	}

	public ForumToolSessionDao getForumToolSessionDao() {
		return forumToolSessionDao;
	}

	public void setForumToolSessionDao(ForumToolSessionDao forumToolSessionDao) {
		this.forumToolSessionDao = forumToolSessionDao;
	}

	public ForumUserDao getForumUserDao() {
		return forumUserDao;
	}

	public void setForumUserDao(ForumUserDao forumUserDao) {
		this.forumUserDao = forumUserDao;
	}

	public IRepositoryService getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(IRepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}
	public ForumToolContentHandler getForumToolContentHandler() {
		return forumToolContentHandler;
	}

	public void setForumToolContentHandler(ForumToolContentHandler toolContentHandler) {
		this.forumToolContentHandler = toolContentHandler;
	}

	public ILearnerService getLearnerService() {
		return learnerService;
	}

	public void setLearnerService(ILearnerService learnerService) {
		this.learnerService = learnerService;
	}

}
