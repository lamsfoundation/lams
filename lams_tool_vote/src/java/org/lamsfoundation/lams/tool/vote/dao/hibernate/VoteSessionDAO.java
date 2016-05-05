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
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.lamsfoundation.lams.tool.vote.dao.IVoteSessionDAO;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueUsr;
import org.lamsfoundation.lams.tool.vote.pojos.VoteSession;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author ozgurd
 *         <p>
 *         Hibernate implementation for database access to Vote sessions for the voting tool.
 *         </p>
 */

public class VoteSessionDAO extends HibernateDaoSupport implements IVoteSessionDAO {

    private static final String FIND_VOTE_SESSION_CONTENT = "from " + VoteSession.class.getName()
	    + " as votes where vote_session_id=?";

    private static final String LOAD_VOTESESSION_BY_USER = "select votes from VoteSession votes left join fetch "
	    + "votes.voteQueUsers user where user.queUsrId=:userId";

    private static final String GET_SESSIONS_FROM_CONTENT = "select votes.voteSessionId from VoteSession votes where votes.voteContent=:voteContent";

    private static final String COUNT_SESSION_COMPLETE = "from voteSession in class VoteSession where voteSession.sessionStatus='COMPLETE'";

    @Override
    public VoteSession getVoteSessionByUID(Long sessionUid) {
	return (VoteSession) this.getHibernateTemplate().get(VoteSession.class, sessionUid);
    }

    @Override
    public VoteSession getSessionBySessionId(Long voteSessionId) {
	String query = "from VoteSession votes where votes.voteSessionId=?";

	List list = getSession().createQuery(query).setLong(0, voteSessionId.longValue()).list();

	if (list != null && list.size() > 0) {
	    VoteSession vote = (VoteSession) list.get(0);
	    return vote;
	}
	return null;
    }

    @Override
    public int countSessionComplete() {
	List list = getSession().createQuery(COUNT_SESSION_COMPLETE).list();

	if (list != null && list.size() > 0) {
	    return list.size();
	} else {
	    return 0;
	}
    }

    @Override
    public void saveVoteSession(VoteSession voteSession) {
	this.getHibernateTemplate().save(voteSession);
    }

    @Override
    public void updateVoteSession(VoteSession voteSession) {
	this.getSession().setFlushMode(FlushMode.AUTO);
	this.getHibernateTemplate().update(voteSession);
    }

    @Override
    public void removeVoteSessionByUID(Long uid) {
	VoteSession votes = (VoteSession) getHibernateTemplate().get(VoteSession.class, uid);
	this.getHibernateTemplate().delete(votes);
    }

    @Override
    public void removeVoteSessionById(Long voteSessionId) {

	HibernateTemplate templ = this.getHibernateTemplate();
	if (voteSessionId != null) {
	    List list = getSession().createQuery(FIND_VOTE_SESSION_CONTENT).setLong(0, voteSessionId.longValue())
		    .list();

	    if (list != null && list.size() > 0) {
		VoteSession vote = (VoteSession) list.get(0);
		this.getSession().setFlushMode(FlushMode.AUTO);
		templ.delete(vote);
		templ.flush();
	    }
	}

    }

    @Override
    public void removeVoteSession(VoteSession voteSession) {
	this.getSession().setFlushMode(FlushMode.AUTO);
	this.getHibernateTemplate().delete(voteSession);
    }

    @Override
    public VoteSession getVoteSessionByUser(final Long userId) {
	return (VoteSession) getHibernateTemplate().execute(new HibernateCallback() {

	    @Override
	    public Object doInHibernate(Session session) throws HibernateException {
		return session.createQuery(LOAD_VOTESESSION_BY_USER).setLong("userId", userId.longValue())
			.uniqueResult();
	    }
	});
    }

    @Override
    public void removeVoteUsers(VoteSession voteSession) {
	this.getHibernateTemplate().deleteAll(voteSession.getVoteQueUsers());
    }

    @Override
    public void addVoteUsers(Long voteSessionId, VoteQueUsr user) {
	VoteSession session = getSessionBySessionId(voteSessionId);
	user.setVoteSession(session);
	session.getVoteQueUsers().add(user);
	this.getHibernateTemplate().saveOrUpdate(user);
	this.getHibernateTemplate().saveOrUpdate(session);

    }

    @Override
    public List<Long> getSessionsFromContent(VoteContent voteContent) {
	return (getHibernateTemplate().findByNamedParam(GET_SESSIONS_FROM_CONTENT, "voteContent", voteContent));
    }
}
