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

import java.util.Iterator;
import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.vote.dao.IVoteUserDAO;
import org.lamsfoundation.lams.tool.vote.model.VoteQueUsr;
import org.lamsfoundation.lams.tool.vote.model.VoteSession;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Hibernate implementation for database access to Voting users (learners) for the voting tool.
 * </p>
 *
 * @author Ozgur Demirtas
 */
@Repository
public class VoteUserDAO extends LAMSBaseDAO implements IVoteUserDAO {

    private static final String LOAD_USER_FOR_SESSION = "from voteQueUsr in class VoteQueUsr where  voteQueUsr.voteSession.uid= :voteSessionId";

    @SuppressWarnings("unchecked")
    @Override
    public VoteQueUsr getUserByUserId(Long userId) {
	String query = "from VoteQueUsr user where user.queUsrId=?";

	List<VoteQueUsr> list = getSessionFactory().getCurrentSession().createQuery(query)
		.setParameter("voteSessionId", userId.longValue()).list();

	if (list != null && list.size() > 0) {
	    VoteQueUsr voteu = list.get(0);
	    return voteu;
	}
	return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public int getCompletedVoteUserBySessionUid(final Long voteSessionUid) {
	List<VoteQueUsr> list = getSessionFactory().getCurrentSession().createQuery(LOAD_USER_FOR_SESSION)
		.setParameter("voteSessionId", voteSessionUid.longValue()).list();

	int completedSessionUserCount = 0;
	if (list != null && list.size() > 0) {
	    Iterator<VoteQueUsr> listIterator = list.iterator();
	    while (listIterator.hasNext()) {
		VoteQueUsr user = listIterator.next();
		if (user.getVoteSession().getSessionStatus().equals("COMPLETED")) {
		    ++completedSessionUserCount;
		}
	    }
	}

	return completedSessionUserCount;
    }

    @SuppressWarnings("unchecked")
    @Override
    public VoteQueUsr getVoteUserBySession(final Long queUsrId, final Long voteSessionId) {

	String strGetUser = "from voteQueUsr in class VoteQueUsr where voteQueUsr.queUsrId=:queUsrId and voteQueUsr.voteSession.uid=:voteSessionId";
	List<VoteQueUsr> list = getSessionFactory().getCurrentSession().createQuery(strGetUser)
		.setParameter("queUsrId", queUsrId).setParameter("voteSessionId", voteSessionId)
		.list();

	if (list != null && list.size() > 0) {
	    VoteQueUsr usr = list.get(0);
	    return usr;
	}
	return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public VoteQueUsr getVoteQueUsrById(long voteQueUsrId) {
	String query = "from VoteQueUsr user where user.queUsrId=:queUsrId";

	List<VoteQueUsr> list = getSessionFactory().getCurrentSession().createQuery(query)
		.setParameter("queUsrId", voteQueUsrId).list();

	if (list != null && list.size() > 0) {
	    VoteQueUsr qu = list.get(0);
	    return qu;
	}
	return null;
    }

    @Override
    public void saveVoteUser(VoteQueUsr voteUser) {
	this.getSession().save(voteUser);
    }

    @Override
    public void updateVoteUser(VoteQueUsr voteUser) {
	this.getSession().update(voteUser);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<VoteQueUsr> getUserBySessionOnly(final VoteSession voteSession) {
	List<VoteQueUsr> list = getSessionFactory().getCurrentSession().createQuery(LOAD_USER_FOR_SESSION)
		.setParameter("voteSessionId", voteSession.getUid().longValue()).list();
	return list;
    }

    @Override
    public void removeVoteUser(VoteQueUsr voteUser) {
	this.getSession().delete(voteUser);
    }

    @Override
    public int getTotalNumberOfUsers() {
	String query = "from obj in class VoteQueUsr";
	return this.doFind(query).size();
    }
}