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

import org.hibernate.FlushMode;
import org.lamsfoundation.lams.tool.vote.dao.IVoteUserDAO;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueUsr;
import org.lamsfoundation.lams.tool.vote.pojos.VoteSession;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * <p>
 * Hibernate implementation for database access to Voting users (learners) for the voting tool.
 * </p>
 * 
 * @author Ozgur Demirtas
 */
public class VoteUserDAO extends HibernateDaoSupport implements IVoteUserDAO {

    private static final String LOAD_USER_FOR_SESSION = "from voteQueUsr in class VoteQueUsr where  voteQueUsr.voteSessionId= :voteSessionId";

    public VoteQueUsr getUserByUserId(Long userId) {
	String query = "from VoteQueUsr user where user.queUsrId=?";

	List list = getSession().createQuery(query).setLong(0, userId.longValue()).list();

	if (list != null && list.size() > 0) {
	    VoteQueUsr voteu = (VoteQueUsr) list.get(0);
	    return voteu;
	}
	return null;
    }

    public int getCompletedVoteUserBySessionUid(final Long voteSessionUid) {
	List list = getSession().createQuery(LOAD_USER_FOR_SESSION)
		.setLong("voteSessionId", voteSessionUid.longValue()).list();

	int completedSessionUserCount = 0;
	if (list != null && list.size() > 0) {
	    Iterator listIterator = list.iterator();
	    while (listIterator.hasNext()) {
		VoteQueUsr user = (VoteQueUsr) listIterator.next();
		if (user.getVoteSession().getSessionStatus().equals("COMPLETED")) {
		    ++completedSessionUserCount;
		}
	    }
	}

	return completedSessionUserCount;
    }

    public VoteQueUsr getVoteUserBySession(final Long queUsrId, final Long voteSessionId) {

	String strGetUser = "from voteQueUsr in class VoteQueUsr where voteQueUsr.queUsrId=:queUsrId and voteQueUsr.voteSessionId=:voteSessionId";
	List list = getSession().createQuery(strGetUser).setLong("queUsrId", queUsrId.longValue())
		.setLong("voteSessionId", voteSessionId.longValue()).list();

	if (list != null && list.size() > 0) {
	    VoteQueUsr usr = (VoteQueUsr) list.get(0);
	    return usr;
	}
	return null;
    }

    public VoteQueUsr getVoteQueUsrById(long voteQueUsrId) {
	String query = "from VoteQueUsr user where user.queUsrId=?";

	List list = getSession().createQuery(query).setLong(0, voteQueUsrId).list();

	if (list != null && list.size() > 0) {
	    VoteQueUsr qu = (VoteQueUsr) list.get(0);
	    return qu;
	}
	return null;
    }

    public void saveVoteUser(VoteQueUsr voteUser) {
	this.getHibernateTemplate().save(voteUser);
    }

    public void updateVoteUser(VoteQueUsr voteUser) {
	this.getHibernateTemplate().update(voteUser);
    }

    public List<VoteQueUsr> getUserBySessionOnly(final VoteSession voteSession) {
	List<VoteQueUsr> list = getSession().createQuery(LOAD_USER_FOR_SESSION)
		.setLong("voteSessionId", voteSession.getUid().longValue()).list();
	return list;
    }

    public void removeVoteUser(VoteQueUsr voteUser) {
	this.getSession().setFlushMode(FlushMode.AUTO);
	this.getHibernateTemplate().delete(voteUser);
    }

    public int getTotalNumberOfUsers() {
	String query = "from obj in class VoteQueUsr";
	return this.getHibernateTemplate().find(query).size();
    }

}
