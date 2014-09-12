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
import java.util.Set;

import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.lamsfoundation.lams.tool.vote.dao.IVoteContentDAO;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteSession;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;

/**
 * @author Ozgur Demirtas
 *         <p>
 *         Hibernate implementation for database access to VoteContent for the
 *         voting tool.
 *         </p>
 */

public class VoteContentDAO extends HibernateDaoSupport implements IVoteContentDAO {

    private static final String FIND_VOTE_CONTENT = "from " + VoteContent.class.getName()
	    + " as vote where content_id=?";

    private static final String LOAD_VOTE_BY_SESSION = "select vote from VoteContent vote left join fetch "
	    + "vote.voteSessions session where session.voteSessionId=:sessionId";

    public VoteContent getVoteContentByUID(Long uid) {
	return (VoteContent) this.getHibernateTemplate().get(VoteContent.class, uid);
    }

    public void saveOrUpdateVote(VoteContent vote) {
	getSessionFactory().getCurrentSession().setFlushMode(FlushMode.AUTO);
	this.getHibernateTemplate().saveOrUpdate(vote);
    }

    public VoteContent getVoteContentByContentId(Long voteContentId) {
	String query = "from VoteContent as vote where vote.voteContentId = ?";
	HibernateTemplate templ = this.getHibernateTemplate();
	List list = getSessionFactory().getCurrentSession().createQuery(query).setLong(0, voteContentId.longValue()).list();

	if (list != null && list.size() > 0) {
	    VoteContent vote = (VoteContent) list.get(0);
	    return vote;
	}
	return null;
    }

    public VoteContent getVoteContentBySession(final Long voteSessionId) {
	return (VoteContent) getHibernateTemplate().execute(new HibernateCallback() {

	    public Object doInHibernate(Session session) throws HibernateException {
		return session.createQuery(VoteContentDAO.LOAD_VOTE_BY_SESSION).setLong("sessionId",
			voteSessionId.longValue()).uniqueResult();
	    }
	});
    }

    public void saveVoteContent(VoteContent voteContent) {
	getSessionFactory().getCurrentSession().setFlushMode(FlushMode.AUTO);
	this.getHibernateTemplate().saveOrUpdate(voteContent);
    }

    public void updateVoteContent(VoteContent voteContent) {
	getSessionFactory().getCurrentSession().setFlushMode(FlushMode.AUTO);
	this.getHibernateTemplate().update(voteContent);
    }

    public void removeVoteById(Long voteContentId) {
	HibernateTemplate templ = this.getHibernateTemplate();
	if (voteContentId != null) {
	    List list = getSessionFactory().getCurrentSession().createQuery(VoteContentDAO.FIND_VOTE_CONTENT)
		    .setLong(0, voteContentId.longValue()).list();

	    if (list != null && list.size() > 0) {
		VoteContent vote = (VoteContent) list.get(0);
		getSessionFactory().getCurrentSession().setFlushMode(FlushMode.AUTO);
		templ.delete(vote);
		templ.flush();
	    }
	}
    }

    public void removeVoteSessions(VoteContent voteContent) {
	this.getHibernateTemplate().deleteAll(voteContent.getVoteSessions());
    }

    public void addVoteSession(Long voteContentId, VoteSession voteSession) {
	VoteContent content = getVoteContentByContentId(voteContentId);
	voteSession.setVoteContent(content);
	content.getVoteSessions().add(voteSession);
	this.getHibernateTemplate().saveOrUpdate(voteSession);
	this.getHibernateTemplate().saveOrUpdate(content);

    }

    public void removeQuestionsFromCache(VoteContent voteContent) {
	if (voteContent != null) {
	    for (VoteQueContent question : (Set<VoteQueContent>) voteContent.getVoteQueContents()) {
		getHibernateTemplate().evict(question);
	    }
	}
    }
    
    public void removeVoteContentFromCache(VoteContent voteContent) {
	if (voteContent != null) {
	    getHibernateTemplate().evict(voteContent);
	}
    }

    @Override
    public void delete(Object object) {
	getHibernateTemplate().delete(object);
    }
}
