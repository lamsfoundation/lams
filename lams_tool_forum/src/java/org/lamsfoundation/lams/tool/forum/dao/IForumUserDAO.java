package org.lamsfoundation.lams.tool.forum.dao;

import java.util.List;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.tool.forum.model.ForumUser;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;

public interface IForumUserDAO extends IBaseDAO {

    List getBySessionId(Long sessionID);

    void save(ForumUser forumUser);

    ForumUser getByUserIdAndSessionId(Long userId, Long sessionId);

    ForumUser getByUserId(Long userId);

    ForumUser getByUid(Long userUid);

    int getCountUsersBySession(Long sessionId, String searchString);

    List<Object[]> getUsersForTablesorter(final Long sessionId, int page, int size, int sorting, String searchString,
	    IUserManagementService userManagementService);

}
