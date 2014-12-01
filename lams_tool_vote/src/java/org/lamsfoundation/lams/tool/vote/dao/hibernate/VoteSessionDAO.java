/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
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

package org.lamsfoundation.lams.tool.vote.dao.hibernate;

import java.util.List;

import org.hibernate.FlushMode;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.vote.dao.IVoteSessionDAO;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueUsr;
import org.lamsfoundation.lams.tool.vote.pojos.VoteSession;
import org.springframework.stereotype.Repository;

/**
 * @author ozgurd
 *         <p>
 *         Hibernate implementation for database access to Vote sessions for the voting tool.
 *         </p>
 */
@Repository
public class VoteSessionDAO extends LAMSBaseDAO implements IVoteSessionDAO {

    private static final String FIND_VOTE_SESSION_CONTENT = "from " + VoteSession.class.getName()
	    + " as votes where vote_session_id=?";

    private static final String LOAD_VOTESESSION_BY_USER = "select votes from VoteSession votes left join fetch "
	    + "votes.voteQueUsers user where user.queUsrId=:userId";

    private static final String GET_SESSIONS_FROM_CONTENT = "select votes.voteSessionId from VoteSession votes where votes.voteContent=:voteContent";

    private static final String COUNT_SESSION_COMPLETE = "from voteSession in class VoteSession where voteSession.sessionStatus='COMPLETE'";

    public VoteSession getVoteSessionByUID(Long sessionUid) {
	return (VoteSession) this.getSession().get(VoteSession.class, sessionUid);
    }

    public VoteSession getSessionBySessionId(Long voteSessionId) {
	String query = "from VoteSession votes where votes.voteSessionId=?";

	List list = getSessionFactory().getCurrentSession().createQuery(query).setLong(0, voteSessionId.longValue()).list();

	if (list != null && list.size() > 0) {
	    VoteSession vote = (VoteSession) list.get(0);
	    return vote;
	}
	return null;
    }

    public int countSessionComplete() {
	List list = getSessionFactory().getCurrentSession().createQuery(COUNT_SESSION_COMPLETE).list();

	if (list != null && list.size() > 0) {
	    return list.size();
	} else
	    return 0;
    }

    public void saveVoteSession(VoteSession voteSession) {
	this.getSession().save(voteSession);
    }

    public void updateVoteSession(VoteSession voteSession) {
	getSessionFactory().getCurrentSession().setFlushMode(FlushMode.AUTO);
	this.getSession().update(voteSession);
    }

    public void removeVoteSessionByUID(Long uid) {
	VoteSession votes = (VoteSession) getSession().get(VoteSession.class, uid);
	this.getSession().delete(votes);
    }

    public void removeVoteSessionById(Long voteSessionId) {

	if (voteSessionId != null) {
	    List list = getSessionFactory().getCurrentSession().createQuery(FIND_VOTE_SESSION_CONTENT).setLong(0, voteSessionId.longValue())
		    .list();

	    if (list != null && list.size() > 0) {
		VoteSession vote = (VoteSession) list.get(0);
		getSessionFactory().getCurrentSession().setFlushMode(FlushMode.AUTO);
		getSession().delete(vote);
		getSession().flush();
	    }
	}

    }

    public void removeVoteSession(VoteSession voteSession) {
	getSessionFactory().getCurrentSession().setFlushMode(FlushMode.AUTO);
	this.getSession().delete(voteSession);
    }

	public VoteSession getVoteSessionByUser(final Long userId) {
		return (VoteSession) getSession().createQuery(LOAD_VOTESESSION_BY_USER).setLong("userId", userId.longValue())
				.uniqueResult();
	}

    public void removeVoteUsers(VoteSession voteSession) {
    	deleteAll(voteSession.getVoteQueUsers());
    }

    public void addVoteUsers(Long voteSessionId, VoteQueUsr user) {
	VoteSession session = getSessionBySessionId(voteSessionId);
	user.setVoteSession(session);
	session.getVoteQueUsers().add(user);
	this.getSession().saveOrUpdate(user);
	this.getSession().saveOrUpdate(session);

    }

    @SuppressWarnings("unchecked")
    public List<Long> getSessionsFromContent(VoteContent voteContent) {
	return (List<Long>) (doFindByNamedParam(GET_SESSIONS_FROM_CONTENT, new String[]{"voteContent"}, new Object[]{voteContent}));
    }
}
