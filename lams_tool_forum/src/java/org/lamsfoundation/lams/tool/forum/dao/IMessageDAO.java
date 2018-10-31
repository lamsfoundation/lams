package org.lamsfoundation.lams.tool.forum.dao;

import java.util.List;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.tool.forum.model.Message;

public interface IMessageDAO extends IBaseDAO {

    void saveOrUpdate(Message message);

    void update(Message message);

    Message getByIdForUpdate(Long messageId);

    Message getById(Long messageId);

    /**
     * Get all root (first level) topics in a special Session.
     *
     * @param sessionId
     * @return
     */
    List<Message> getRootTopics(Long sessionId);

    /**
     * Get all message posted by author role in a special forum.
     *
     * @param forumUid
     * @return
     */
    List<Message> getTopicsFromAuthor(Long forumUid);

    void delete(Long uid);

    /**
     * Get all children message from the given parent topic ID.
     *
     * @param parentId
     * @return
     */
    List<Message> getChildrenTopics(Long parentId);

    /**
     * Get all messages according to special user and session.
     *
     * @param userUid
     * @param sessionId
     * @return
     */
    List<Message> getByUserAndSession(Long userUid, Long sessionId);

    /**
     * Get all messages according to special session.
     *
     * @param sessionId
     * @return
     */
    List<Message> getBySession(Long sessionId);

    /**
     * Return how many post from this user and session. DOES NOT include posts from author.
     *
     * @param userID
     * @param sessionId
     * @return
     */
    int getTopicsNum(Long userID, Long sessionId);

    /** Get the create date of the first message left by this user in the session and the last
     * message left.
     * @param userUid
     * @return
     */
    Object[] getDateRangeOfMessages(Long userUid);
}
