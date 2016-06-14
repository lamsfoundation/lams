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

import org.hibernate.FlushMode;
import org.lamsfoundation.lams.tool.mc.dao.IMcUsrAttemptDAO;
import org.lamsfoundation.lams.tool.mc.dto.ToolOutputDTO;
import org.lamsfoundation.lams.tool.mc.pojos.McQueUsr;
import org.lamsfoundation.lams.tool.mc.pojos.McUsrAttempt;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

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
public class McUsrAttemptDAO extends HibernateDaoSupport implements IMcUsrAttemptDAO {

    private static final String LOAD_PARTICULAR_QUESTION_ATTEMPT = "from attempt in class McUsrAttempt where attempt.mcQueUsr.uid=:queUsrUid"
	    + " and attempt.mcQueContentId=:mcQueContentId" + " order by attempt.mcOptionsContent.uid";

    private static final String LOAD_ALL_QUESTION_ATTEMPTS = "from attempt in class McUsrAttempt where attempt.mcQueUsr.uid=:queUsrUid"
	    + " AND attempt.mcQueUsr.responseFinalised = true order by attempt.mcQueContentId, attempt.mcOptionsContent.uid";

    private static final String FIND_ATTEMPTS_COUNT_BY_OPTION = "select count(*) from " + McUsrAttempt.class.getName()
	    + " as attempt where attempt.mcOptionsContent.uid=? AND attempt.mcQueUsr.responseFinalised = true";

    private static final String FIND_USER_TOTAL_MARK = "select COALESCE(SUM(attempt.mark),0) from "
	    + McUsrAttempt.class.getName()
	    + " as attempt where attempt.mcQueUsr.uid=:userUid AND attempt.mcQueUsr.responseFinalised = true";

    @Override
    public McUsrAttempt getUserAttemptByUid(Long uid) {
	return (McUsrAttempt) this.getHibernateTemplate().get(McUsrAttempt.class, uid);
    }

    @Override
    public void saveMcUsrAttempt(McUsrAttempt mcUsrAttempt) {
	this.getHibernateTemplate().saveOrUpdate(mcUsrAttempt);
    }

    @Override
    public List<McUsrAttempt> getFinalizedUserAttempts(final Long userUid) {
	return getSession().createQuery(LOAD_ALL_QUESTION_ATTEMPTS).setLong("queUsrUid", userUid.longValue()).list();
    }

    @Override
    public int getUserTotalMark(final Long userUid) {
	List list = getSession().createQuery(FIND_USER_TOTAL_MARK).setLong("userUid", userUid.longValue()).list();

	if (list == null || list.size() == 0) {
	    return 0;
	}

	return ((Number) list.get(0)).intValue();
    }

    @Override
    @SuppressWarnings("unchecked")
    public McUsrAttempt getUserAttemptByQuestion(final Long queUsrUid, final Long mcQueContentId) {
	List<McUsrAttempt> userAttemptList = getSession().createQuery(LOAD_PARTICULAR_QUESTION_ATTEMPT)
		.setLong("queUsrUid", queUsrUid.longValue()).setLong("mcQueContentId", mcQueContentId.longValue())
		.list();
	if (userAttemptList.size() > 1) {
	    throw new RuntimeException("There are more than 1 latest question attempt");
	}

	McUsrAttempt userAttempt = (userAttemptList.size() == 0) ? null : userAttemptList.get(0);
	return userAttempt;
    }

    @Override
    public void updateMcUsrAttempt(McUsrAttempt mcUsrAttempt) {
	this.getSession().setFlushMode(FlushMode.AUTO);
	this.getHibernateTemplate().update(mcUsrAttempt);
    }

    @Override
    public void removeAllUserAttempts(Long queUserUid) {
	this.getSession().setFlushMode(FlushMode.AUTO);

	List<McUsrAttempt> userAttempts = getSession().createQuery(LOAD_ALL_QUESTION_ATTEMPTS)
		.setLong("queUsrUid", queUserUid.longValue()).list();

	for (McUsrAttempt userAttempt : userAttempts) {
	    this.getHibernateTemplate().delete(userAttempt);
	}
    }

    @Override
    public void removeAttempt(McUsrAttempt userAttempt) {
	this.getHibernateTemplate().delete(userAttempt);
    }

    @Override
    public int getAttemptsCountPerOption(Long optionUid) {
	List list = getHibernateTemplate().find(FIND_ATTEMPTS_COUNT_BY_OPTION, new Object[] { optionUid });
	if (list == null || list.size() == 0) {
	    return 0;
	}
	return ((Number) list.get(0)).intValue();
    }
    
    @Override
    public List<ToolOutputDTO> getLearnerMarksByContentId(Long toolContentId) {
	
	final String FIND_MARKS_FOR_CONTENT_ID = "SELECT user.queUsrId, user.lastAttemptTotalMark FROM "
		+ McQueUsr.class.getName()
		+ " user WHERE user.mcSession.mcContent.mcContentId = ? AND user.responseFinalised = true";

	List<Object[]> list = getHibernateTemplate().find(FIND_MARKS_FOR_CONTENT_ID, new Object[] { toolContentId });
	
	List<ToolOutputDTO> toolOutputDtos = new ArrayList<ToolOutputDTO>();
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