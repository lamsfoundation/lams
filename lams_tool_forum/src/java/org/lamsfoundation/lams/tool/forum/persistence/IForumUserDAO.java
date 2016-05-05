package org.lamsfoundation.lams.tool.forum.persistence;

import java.util.List;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;

public interface IForumUserDAO extends IBaseDAO {

    List getBySessionId(Long sessionID);

    void save(ForumUser forumUser);

    ForumUser getByUserIdAndSessionId(Long userId, Long sessionId);

    ForumUser getByUserId(Long userId);

    ForumUser getByUid(Long userUid);

    void delete(ForumUser user);

    int getCountUsersBySession(Long sessionId, String searchString);

    List<Object[]> getUsersForTablesorter(final Long sessionId, int page, int size, int sorting, String searchString,
	    boolean getNotebookEntries, ICoreNotebookService coreNotebookService);

}
