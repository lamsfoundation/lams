package org.lamsfoundation.lams.tool.forum.dao;

import java.util.List;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.tool.forum.model.ForumToolSession;

public interface IForumToolSessionDAO extends IBaseDAO {

    ForumToolSession getBySessionId(Long sessionId);

    void saveOrUpdate(ForumToolSession session);

    List getByContentId(Long contentID);

    void delete(Long sessionId);

    void delete(ForumToolSession session);

}
