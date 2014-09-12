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
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.lamsfoundation.lams.tool.mc.dao.IMcSessionDAO;
import org.lamsfoundation.lams.tool.mc.pojos.McSession;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;

/**
 * @author ozgurd
 *         <p>
 *         Hibernate implementation for database access to Mc sessions for the mc tool.
 *         </p>
 */

public class McSessionDAO extends HibernateDaoSupport implements IMcSessionDAO {

    private static final String LOAD_MCSESSION_BY_USER = "select ms from McSession ms left join fetch "
	    + "ms.mcQueUsers user where user.queUsrId=:userId";

    private static final String LOAD_MCSESSION_BY_MCSESSIONID = "from McSession mcs where mcs.mcSessionId=?";

    public McSession getMcSessionById(Long mcSessionId) {

	List list = getSessionFactory().getCurrentSession().createQuery(LOAD_MCSESSION_BY_MCSESSIONID).setLong(0, mcSessionId.longValue()).list();

	if (list != null && list.size() > 0) {
	    McSession mcs = (McSession) list.get(0);
	    return mcs;
	}
	return null;
    }

    public void saveMcSession(McSession mcSession) {
	this.getHibernateTemplate().save(mcSession);
    }

    public void updateMcSession(McSession mcSession) {
	getSessionFactory().getCurrentSession().setFlushMode(FlushMode.AUTO);
	this.getHibernateTemplate().update(mcSession);
    }

    public void removeMcSession(McSession mcSession) {
	getSessionFactory().getCurrentSession().setFlushMode(FlushMode.AUTO);
	this.getHibernateTemplate().delete(mcSession);
    }

    public McSession getMcSessionByUser(final Long userId) {
	return (McSession) getHibernateTemplate().execute(new HibernateCallback() {

	    public Object doInHibernate(Session session) throws HibernateException {
		return session.createQuery(LOAD_MCSESSION_BY_USER).setLong("userId", userId.longValue()).uniqueResult();
	    }
	});
    }

}
