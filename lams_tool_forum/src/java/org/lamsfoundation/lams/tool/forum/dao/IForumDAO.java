package org.lamsfoundation.lams.tool.forum.dao;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.tool.forum.model.Forum;
import org.lamsfoundation.lams.tool.forum.model.ForumCondition;

public interface IForumDAO extends IBaseDAO {

    void saveOrUpdate(Forum forum);

    Forum getById(Long forumId);

    /**
     * NOTE: before call this method, must be sure delete all messages in this forum. Example code like this:
     * 
     * <pre>
     * <code>
     * messageDao.deleteForumMessage(forum.getUuid());
     * </code>
     * </pre>
     * 
     * @param forum
     */
    void delete(Forum forum);

    Forum getByContentId(Long contentID);

    void deleteCondition(ForumCondition condition);
}
