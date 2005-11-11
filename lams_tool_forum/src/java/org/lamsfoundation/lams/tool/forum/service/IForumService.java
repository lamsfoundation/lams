package org.lamsfoundation.lams.tool.forum.service;

import java.util.List;

import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.tool.forum.core.PersistenceException;
import org.lamsfoundation.lams.tool.forum.persistence.Attachment;
import org.lamsfoundation.lams.tool.forum.persistence.Forum;
import org.lamsfoundation.lams.tool.forum.persistence.ForumUser;
import org.lamsfoundation.lams.tool.forum.persistence.Message;
import org.lamsfoundation.lams.tool.forum.persistence.ForumToolSession;

/**
 * Created by IntelliJ IDEA.
 * User: conradb
 * Date: 8/06/2005
 * Time: 14:49:59
 * To change this template use File | Settings | File Templates.
 */
public interface IForumService {

    public Forum editForum(Forum forum) throws PersistenceException;
    public Forum getForum(Long forumId) throws PersistenceException;
    public Forum getForumByContentId(Long contentID) throws PersistenceException;
    
    public void deleteForum(Long forumId) throws PersistenceException;
    public void deleteForumAttachment(Long attachmentId) throws PersistenceException;
    public Forum createForum(Long contentId) throws PersistenceException;
    
    public Attachment uploadInstructionFile(Long contentId, FormFile file, String type) throws PersistenceException;

    public Message getMessage(Long messageUid) throws PersistenceException;
    
    public Message createRootTopic(Long forumId, Long sessionId, Message message) throws PersistenceException ;
    public Message updateTopic(Message message) throws PersistenceException;
    public Message replyTopic(Long parentId, Message message) throws PersistenceException;
    public void deleteTopic(Long messageId) throws PersistenceException;
	/**
	 * This method only upload the given file into system repository. It does not execute any database operation.
	 * 
	 * @param file
	 * @return Attachment 
	 * 		A new instance of attachment has uploaded file VersionID and UUID information.
	 * @throws PersistenceException
	 */
	public Attachment uploadAttachment(FormFile file) throws PersistenceException;
	public void deleteInstructionFile(Long contentID, Long uuID, Long versionID, String type) throws PersistenceException;
	public void deleteFromRepository(Long uuID, Long versionID) throws PersistenceException;
	
	/** 
	 * Get topics list by given root topic ID.  
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
	 * @param sessionId
	 * @return
	 * 		List of MessageDTO
	 */
	public List getRootTopics(Long sessionId);
	/**
	 * @return
	 * 		List of MessageDTO
	 */
	public List getAuthoredTopics(Long forumId);
	
	public ForumUser getUserByUserId(Long userId);
	public void createUser(ForumUser forumUser);
	public ForumToolSession getSessionBySessionId(Long sessionId);
	/**
	 * This method will look up root topic ID by any level topicID.
	 * @param topicId
	 * @return
	 */
	public Long getRootTopicId(Long topicId);
	public void updateSession(ForumToolSession session);
}
