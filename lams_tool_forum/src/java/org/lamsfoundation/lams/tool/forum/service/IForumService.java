package org.lamsfoundation.lams.tool.forum.service;

import java.util.List;

import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.tool.forum.persistence.Attachment;
import org.lamsfoundation.lams.tool.forum.persistence.Forum;
import org.lamsfoundation.lams.tool.forum.persistence.ForumReport;
import org.lamsfoundation.lams.tool.forum.persistence.ForumToolSession;
import org.lamsfoundation.lams.tool.forum.persistence.ForumUser;
import org.lamsfoundation.lams.tool.forum.persistence.Message;
import org.lamsfoundation.lams.tool.forum.persistence.PersistenceException;

/**
 * User: conradb
 * Date: 8/06/2005
 * Time: 14:49:59
 */
public interface IForumService {
	//************************************************************************************
	// Forum Method
	//************************************************************************************
    /**
     * Create a Forum instance according to the default content. <BR> 
     * Note, this new insstance won't save into database until called persist method.
     * 
     * @param contentID
     * @return
     */
    public Forum getDefaultContent(Long contentID);
	/**
	 * Update forum by given <code>Forum</code>. If forum does not exist, the create a new forum.
	 * @param forum
	 * @return
	 * @throws PersistenceException
	 */
    public Forum updateForum(Forum forum) throws PersistenceException;
    /**
     * Upload instruction file
     * @param file
     * @param type
     * @return
     * @throws PersistenceException
     */
    public Attachment uploadInstructionFile(FormFile file, String type) throws PersistenceException;
    /**
     * Get forum by forum UID 
     * @param forumUid
     * @return
     * @throws PersistenceException
     */
    public Forum getForum(Long forumUid) throws PersistenceException;
    /**
     * Get forum by forum ID(not record UID)
     * @param contentID
     * @return
     * @throws PersistenceException
     */
    public Forum getForumByContentId(Long contentID) throws PersistenceException;

    /**
     * Delete authoring page instruction files.
     * @param attachmentId
     * @throws PersistenceException
     */
    public void deleteForumAttachment(Long attachmentId) throws PersistenceException;
	//************************************************************************************
	//Topic Method
	//************************************************************************************
    /**
     * Create a root topic.
     * @param forumId
     * @param sessionId
     * @param message
     * @return
     * @throws PersistenceException
     */
    public Message createRootTopic(Long forumId, Long sessionId, Message message) throws PersistenceException ;
    /**
     * Update a topic by give <code>Message</code> instance.
     * @param message
     * @return
     * @throws PersistenceException
     */
    public Message updateTopic(Message message) throws PersistenceException;
    /**
     * Reply a topic.
     * @param parentId
     * @param sessionId ToolSessionID
     * @param message
     * @return
     * @throws PersistenceException
     */
    public Message replyTopic(Long parentId,  Long sessionId, Message message) throws PersistenceException;

    /**
     * Delete the topic by given topic ID. The function will delete all children topics under this topic.
     * @param topicId
     * @throws PersistenceException
     */
    public void deleteTopic(Long topicId) throws PersistenceException;
	/**
	 * Upload message attachment file into repository.
	 * This method only upload the given file into system repository. It does not execute any database operation.
	 * 
	 * @param file
	 * @return Attachment 
	 * 		A new instance of attachment has uploaded file VersionID and UUID information.
	 * @throws PersistenceException
	 */
	public Attachment uploadAttachment(FormFile file) throws PersistenceException;

	/**
	 * Delete  file from repository.
	 * @param uuID
	 * @param versionID
	 * @throws PersistenceException
	 */
	public void deleteFromRepository(Long uuID, Long versionID) throws PersistenceException;
	
	//************************************************************************************
	//*********************Get topic methods **********************
	//************************************************************************************
	/** 
	 * Get topic and its children list by given root topic ID.  
	 * Note that the return type is DTO.
	 * 
	 * @param rootTopicId
	 * @return
	 * 		List of MessageDTO
	 */
	public List getTopicThread(Long rootTopicId);
	/**
	 * Get root topics by a given sessionID value. Simultanousely, it gets back topics, which author 
	 * posted in authoring page for this forum, which is related with the given sessionID value.
	 * 
	 * This method will used by  user to display initial topic page for a forum. 
	 * 
	 * Note that the return type is DTO.
	 * @param sessionId
	 * @return
	 * 		List of MessageDTO
	 */
	public List getRootTopics(Long sessionId);
	/**
	 * Get topics posted by author role. Note that the return type is DTO.
	 * @return
	 * 		List of MessageDTO
	 */
	public List getAuthoredTopics(Long forumId);
	/**
	 * This method will look up root topic ID by any level topicID.
	 * @param topicId
	 * @return
	 */
	public Long getRootTopicId(Long topicId);
    /**
     * Get message by given message UID
     * @param messageUid
     * @return 
     * 		Message 
     * @throws PersistenceException
     */
    public Message getMessage(Long messageUid) throws PersistenceException;
    /**
     * Get message list posted by given user.
     * Note that the return type is DTO.
     * @param userId
     * @return
     */
    public List getMessagesByUserUid(Long userId, Long sessionId);
	//************************************************************************************
	// Session Method
	//************************************************************************************
	/**
	 * Get Forum tool session by Session ID (not record UID).
	 * @param sessionId
	 * @return
	 */
	public ForumToolSession getSessionBySessionId(Long sessionId);
	/**
	 * Update Forum Tool Session record in database. 
	 * @param session
	 */
	public void updateSession(ForumToolSession session);
	/**
	 * Get session list according to content ID. 
	 * @param contentID
	 * @return List
	 */
	public List getSessionsByContentId(Long contentID);
	/**
	 * Get all message according to the given session ID.
	 * @param sessionID
	 * @return
	 */
	public List getAllTopicsFromSession(Long sessionID);
	//************************************************************************************
	// User  Method
	//************************************************************************************
    /**
     * Create a new user in database.
     * @param forumUser
     */
    public void createUser(ForumUser forumUser);
    /**
     * Get user by user ID (not record UID).
     * @param userId
     * @return
     */
    public ForumUser getUserByUserAndSession(Long userId,Long sessionId);
    /**
     * Get user list by given session ID.
     * @param sessionID
     * @return
     */
	public List getUsersBySessionId(Long sessionID);
	/**
	 * Get user by uid
	 * @param userUid
	 * @return
	 */
	public ForumUser getUser(Long userUid);
	/**
	 * Get user by user ID
	 * @param userId
	 * @return
	 */
	public ForumUser getUserByID(Long userId);
	
	//************************************************************************************
	// Report  Method
	//************************************************************************************
	
	//************************************************************************************
	// Miscellaneous Method
	//************************************************************************************	
	public void releaseMarksForSession(Long sessionID);
}
