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
/* $$Id$$ */
package org.lamsfoundation.lams.tool.mc.dao.hibernate;

import java.util.List;

import org.hibernate.FlushMode;
import org.lamsfoundation.lams.tool.mc.dao.IMcUserDAO;
import org.lamsfoundation.lams.tool.mc.pojos.McQueUsr;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author Ozgur Demirtas
 *         <p>
 *         Hibernate implementation for database access to Mc users (learners) for the mc tool.
 *         </p>
 */
public class McUserDAO extends HibernateDaoSupport implements IMcUserDAO {

    private static final String CALC_MARK_STATS_FOR_SESSION = "select max(mu.lastAttemptTotalMark), min(mu.lastAttemptTotalMark), avg(mu.lastAttemptTotalMark)"
	    + " from McQueUsr mu where mu.mcSessionId = :mcSessionUid";

    private static final String GET_USER_BY_USER_ID_SESSION = "from mcQueUsr in class McQueUsr where mcQueUsr.queUsrId=:queUsrId and mcQueUsr.mcSessionId=:mcSessionUid";

    public McQueUsr getMcUserByUID(Long uid) {
	return (McQueUsr) this.getHibernateTemplate().get(McQueUsr.class, uid);
    }

    public McQueUsr getMcUserBySession(final Long queUsrId, final Long mcSessionUid) {

	List list = getSession().createQuery(GET_USER_BY_USER_ID_SESSION).setLong("queUsrId", queUsrId.longValue())
		.setLong("mcSessionUid", mcSessionUid.longValue()).list();

	if (list != null && list.size() > 0) {
	    McQueUsr usr = (McQueUsr) list.get(0);
	    return usr;
	}
	return null;
    }

    public void saveMcUser(McQueUsr mcUser) {
	this.getHibernateTemplate().save(mcUser);
    }

    public void updateMcUser(McQueUsr mcUser) {
	this.getHibernateTemplate().update(mcUser);
    }

    public void removeMcUser(McQueUsr mcUser) {
	this.getSession().setFlushMode(FlushMode.AUTO);
	this.getHibernateTemplate().delete(mcUser);
    }

    /** Get the max, min and average mark (in that order) for a session */
    public Integer[] getMarkStatisticsForSession(Long sessionUid) {
	Object[] stats = (Object[]) getSession().createQuery(CALC_MARK_STATS_FOR_SESSION)
		.setLong("mcSessionUid", sessionUid.longValue()).uniqueResult();

	if (stats != null) {
	    if (stats[2] instanceof Float) {
		return new Integer[] { (Integer) stats[0], (Integer) stats[1],
			new Integer(((Float) stats[2]).intValue()) };
	    } else if (stats[2] instanceof Double) {
		return new Integer[] { (Integer) stats[0], (Integer) stats[1],
			new Integer(((Double) stats[2]).intValue()) };
	    }
	}

	return null;

    }

}
