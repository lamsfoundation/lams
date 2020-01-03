/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.mc.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.query.Query;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.mc.dao.IMcUsrAttemptDAO;
import org.lamsfoundation.lams.tool.mc.dto.ToolOutputDTO;
import org.lamsfoundation.lams.tool.mc.model.McQueUsr;
import org.lamsfoundation.lams.tool.mc.model.McUsrAttempt;
import org.lamsfoundation.lams.usermanagement.User;
import org.springframework.stereotype.Repository;

/**
 * @author Ozgur Demirtas
 *         <p>
 *         Hibernate implementation for database access to McUsrAttempt for the mc tool.
 *         </p>
 *         <p>
 *         Be very careful about the queUserId and the McQueUser.uid fields. McQueUser.queUsrId is the core's user id
 *         for this user and McQueUser.uid is the primary key for McQueUser. McUsrAttempt.queUsrId = McQueUser.uid, not
 *         McUsrAttempt.queUsrId = McQueUser.queUsrId as you would expect. A new McQueUser object is created for each
 *         new tool session, so if the McQueUser.uid is supplied, then this denotes one user in a particular tool
 *         session, but if McQueUser.queUsrId is supplied, then this is just the user, not the session and the session
 *         must also be checked.
 */
@Repository
public class McUsrAttemptDAO extends LAMSBaseDAO implements IMcUsrAttemptDAO {

    private static final String LOAD_PARTICULAR_QUESTION_ATTEMPT = "from attempt in class McUsrAttempt where attempt.mcQueUsr.uid=:queUsrUid"
	    + " and attempt.qbToolQuestion.uid=:mcQueContentId order by attempt.qbOption.uid";

    private static final String LOAD_FINAL_USER_QUESTION_ATTEMPTS_FOR_QUESTION_SESSION = "from attempt in class McUsrAttempt where "
	    + " attempt.mcQueUsr.mcSessionId=:sessionUid AND attempt.qbToolQuestion.uid=:mcQueContentId "
	    + " AND attempt.mcQueUsr.responseFinalised = true order by attempt.mcQueUsr.uid";

    private static final String LOAD_ALL_QUESTION_ATTEMPTS = "from attempt in class McUsrAttempt where attempt.mcQueUsr.uid=:queUsrUid"
	    + " AND attempt.mcQueUsr.responseFinalised = true order by attempt.qbToolQuestion.uid, attempt.qbOption.uid";

    private static final String FIND_ATTEMPTS_COUNT_BY_OPTION = "select count(*) from " + McUsrAttempt.class.getName()
	    + " as attempt where attempt.qbOption.uid=? AND attempt.qbToolQuestion.uid = ? AND attempt.mcQueUsr.responseFinalised = true";

    private static final String FIND_USER_TOTAL_MARK = "select COALESCE(SUM(attempt.mark),0) from "
	    + McUsrAttempt.class.getName()
	    + " as attempt where attempt.mcQueUsr.uid=:userUid AND attempt.mcQueUsr.responseFinalised = true";

    @Override
    public McUsrAttempt getUserAttemptByUid(Long uid) {
	return this.getSession().get(McUsrAttempt.class, uid);
    }

    @Override
    public void saveMcUsrAttempt(McUsrAttempt mcUsrAttempt) {
	this.getSession().saveOrUpdate(mcUsrAttempt);
    }

    @Override
    public List<McUsrAttempt> getFinalizedUserAttempts(final Long userUid) {
	return getSessionFactory().getCurrentSession().createQuery(LOAD_ALL_QUESTION_ATTEMPTS)
		.setParameter("queUsrUid", userUid).list();
    }

    @Override
    public List<Object[]> getFinalizedAttemptsBySessionId(final Long sessionId) {
	final String LOAD_QUESTION_ATTEMPTS_BY_SESSION_ID = "SELECT attempt, u.portraitUuid FROM "
		+ McUsrAttempt.class.getName() + " AS attempt, " + User.class.getName()
		+ " as u WHERE attempt.mcQueUsr.mcSession.mcSessionId=:sessionId"
		+ " AND attempt.mcQueUsr.responseFinalised = true AND u.userId=attempt.mcQueUsr.queUsrId";

	return getSessionFactory().getCurrentSession().createQuery(LOAD_QUESTION_ATTEMPTS_BY_SESSION_ID)
		.setParameter("sessionId", sessionId).list();
    }

    @Override
    public List<Object[]> getLeadersFinalizedAttemptsByContentId(final Long contentId) {
	final String LOAD_QUESTION_ATTEMPTS_BY_SESSION_ID = "SELECT attempt, u.portraitUuid FROM "
		+ McUsrAttempt.class.getName() + " AS attempt, " + User.class.getName()
		+ " as u WHERE attempt.mcQueUsr=attempt.mcQueUsr.mcSession.groupLeader "
		+ " AND attempt.mcQueContent.mcContent.mcContentId=:contentId "
		+ " AND attempt.mcQueUsr.responseFinalised = true AND u.userId=attempt.mcQueUsr.queUsrId";

	return getSessionFactory().getCurrentSession().createQuery(LOAD_QUESTION_ATTEMPTS_BY_SESSION_ID)
		.setParameter("contentId", contentId).list();
    }

    @Override
    public int getUserTotalMark(final Long userUid) {
	List list = getSessionFactory().getCurrentSession().createQuery(FIND_USER_TOTAL_MARK)
		.setParameter("userUid", userUid).list();

	if (list == null || list.size() == 0) {
	    return 0;
	}

	return ((Number) list.get(0)).intValue();
    }

    @Override
    @SuppressWarnings("unchecked")
    public McUsrAttempt getUserAttemptByQuestion(final Long userUid, final Long mcQueContentId) {
	List<McUsrAttempt> userAttemptList = getSessionFactory().getCurrentSession()
		.createQuery(LOAD_PARTICULAR_QUESTION_ATTEMPT).setParameter("queUsrUid", userUid)
		.setParameter("mcQueContentId", mcQueContentId).list();
	if (userAttemptList.size() > 1) {
	    throw new RuntimeException("There are more than 1 latest questionDescription attempt");
	}

	McUsrAttempt userAttempt = (userAttemptList.size() == 0) ? null : userAttemptList.get(0);
	return userAttempt;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<McUsrAttempt> getUserAttemptsByQuestionSession(final Long sessionUid, final Long mcQueContentId) {
	List<McUsrAttempt> userAttemptList = getSessionFactory().getCurrentSession()
		.createQuery(LOAD_FINAL_USER_QUESTION_ATTEMPTS_FOR_QUESTION_SESSION)
		.setParameter("sessionUid", sessionUid).setParameter("mcQueContentId", mcQueContentId).list();
	return userAttemptList;
    }

    @Override
    public void updateMcUsrAttempt(McUsrAttempt mcUsrAttempt) {
	this.getSession().update(mcUsrAttempt);
    }

    @Override
    public void removeAllUserAttempts(Long queUserUid) {
	List<McUsrAttempt> userAttempts = getSessionFactory().getCurrentSession()
		.createQuery(LOAD_ALL_QUESTION_ATTEMPTS).setParameter("queUsrUid", queUserUid).list();

	for (McUsrAttempt userAttempt : userAttempts) {
	    this.getSession().delete(userAttempt);
	}
    }

    @Override
    public void removeAttempt(McUsrAttempt userAttempt) {
	this.getSession().delete(userAttempt);
    }

    @Override
    public int getAttemptsCountPerOption(Long optionUid, Long mcQueContentId) {
	List list = doFind(FIND_ATTEMPTS_COUNT_BY_OPTION, new Object[] { optionUid, mcQueContentId });
	if (list == null || list.size() == 0) {
	    return 0;
	}
	return ((Number) list.get(0)).intValue();
    }

    @Override
    public boolean isMcContentAttempted(long toolContentId) {
	final String IS_USER_ATTEMPT_EXIST_BY_TOOL_CONTENT = "select COUNT(*) > 0 FROM " + McUsrAttempt.class.getName()
		+ " AS attempt WHERE attempt.qbToolQuestion.toolContentId=:toolContentId";

	Query<Boolean> q = getSession().createQuery(IS_USER_ATTEMPT_EXIST_BY_TOOL_CONTENT, Boolean.class);
	q.setParameter("toolContentId", toolContentId);
	return q.uniqueResult();
    }

    @Override
    public List<ToolOutputDTO> getLearnerMarksByContentId(Long toolContentId) {

	final String FIND_MARKS_FOR_CONTENT_ID = "SELECT user.queUsrId, user.lastAttemptTotalMark FROM "
		+ McQueUsr.class.getName()
		+ " user WHERE user.mcSession.mcContent.mcContentId = ? AND user.responseFinalised = true";

	List<Object[]> list = doFind(FIND_MARKS_FOR_CONTENT_ID, new Object[] { toolContentId });

	List<ToolOutputDTO> toolOutputDtos = new ArrayList<>();
	if (list != null && list.size() > 0) {
	    for (Object[] element : list) {

		Long userId = ((Number) element[0]).longValue();
		Integer grade = (Integer) element[1];

		ToolOutputDTO toolOutputDto = new ToolOutputDTO();
		toolOutputDto.setUserId(userId.intValue());
		toolOutputDto.setMark(grade);
		toolOutputDtos.add(toolOutputDto);
	    }

	}

	return toolOutputDtos;
    }

}