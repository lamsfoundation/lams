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

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.vote.dao.IVoteContentDAO;
import org.lamsfoundation.lams.tool.vote.model.VoteContent;
import org.lamsfoundation.lams.tool.vote.model.VoteQueContent;
import org.lamsfoundation.lams.tool.vote.model.VoteSession;
import org.springframework.stereotype.Repository;

/**
 * @author Ozgur Demirtas
 *         <p>
 *         Hibernate implementation for database access to VoteContent for the
 *         voting tool.
 *         </p>
 */
@Repository
public class VoteContentDAO extends LAMSBaseDAO implements IVoteContentDAO {

    private static final String FIND_VOTE_CONTENT = "from " + VoteContent.class.getName()
	    + " as vote where content_id=:contentId";

    private static final String LOAD_VOTE_BY_SESSION = "select vote from VoteContent vote left join fetch "
	    + "vote.voteSessions session where session.voteSessionId=:sessionId";

    @Override
    public VoteContent getVoteContentByUID(Long uid) {
	return this.getSession().get(VoteContent.class, uid);
    }

    @Override
    public void saveOrUpdateVote(VoteContent vote) {
	this.getSession().saveOrUpdate(vote);
    }

    @SuppressWarnings("unchecked")
    @Override
    public VoteContent getVoteContentByContentId(Long voteContentId) {
	String query = "from VoteContent as vote where vote.voteContentId = :voteContentId";
	List<VoteContent> list = getSessionFactory().getCurrentSession().createQuery(query)
		.setParameter("voteContentId", voteContentId).setCacheable(true).list();

	if (list != null && list.size() > 0) {
	    VoteContent vote = list.get(0);
	    return vote;
	}
	return null;
    }

    @Override
    public VoteContent getVoteContentBySession(final Long voteSessionId) {
	return (VoteContent) getSession().createQuery(VoteContentDAO.LOAD_VOTE_BY_SESSION)
		.setParameter("sessionId", voteSessionId).setCacheable(true).uniqueResult();
    }

    @Override
    public void saveVoteContent(VoteContent voteContent) {
	this.getSession().saveOrUpdate(voteContent);
    }

    @Override
    public void updateVoteContent(VoteContent voteContent) {
	this.getSession().update(voteContent);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void removeVoteById(Long voteContentId) {
	if (voteContentId != null) {
	    List<VoteContent> list = getSessionFactory().getCurrentSession()
		    .createQuery(VoteContentDAO.FIND_VOTE_CONTENT).setParameter("contentId", voteContentId).list();

	    if (list != null && list.size() > 0) {
		VoteContent vote = list.get(0);
		getSession().delete(vote);
		getSession().flush();
	    }
	}
    }

    @Override
    public void removeVoteSessions(VoteContent voteContent) {
	deleteAll(voteContent.getVoteSessions());
    }

    @Override
    public void addVoteSession(Long voteContentId, VoteSession voteSession) {
	VoteContent content = getVoteContentByContentId(voteContentId);
	voteSession.setVoteContent(content);
	content.getVoteSessions().add(voteSession);
	this.getSession().saveOrUpdate(voteSession);
	this.getSession().saveOrUpdate(content);

    }

    @Override
    public void removeQuestionsFromCache(VoteContent voteContent) {
	if (voteContent != null) {
	    for (VoteQueContent question : voteContent.getVoteQueContents()) {
		getSession().evict(question);
	    }
	}
    }

    @Override
    public void removeVoteContentFromCache(VoteContent voteContent) {
	if (voteContent != null) {
	    getSession().evict(voteContent);
	}
    }

    @Override
    public void delete(Object object) {
	getSession().delete(object);
    }
}
