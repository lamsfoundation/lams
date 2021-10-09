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

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.mc.dao.IMcSessionDAO;
import org.lamsfoundation.lams.tool.mc.model.McSession;
import org.springframework.stereotype.Repository;

/**
 * @author ozgurd
 *         <p>
 *         Hibernate implementation for database access to Mc sessions for the mc tool.
 *         </p>
 */
@Repository
public class McSessionDAO extends LAMSBaseDAO implements IMcSessionDAO {

    private static final String LOAD_MCSESSION_BY_USER = "select ms from McSession ms left join fetch "
	    + "ms.mcQueUsers user where user.queUsrId=:userId";

    private static final String LOAD_MCSESSION_BY_MCSESSIONID = "from McSession mcs where mcs.mcSessionId=:mcSessionId";

    @Override
    public McSession getMcSessionById(Long mcSessionId) {

	List<?> list = getSessionFactory().getCurrentSession().createQuery(LOAD_MCSESSION_BY_MCSESSIONID)
		.setParameter("mcSessionId", mcSessionId).setCacheable(true).list();

	if (list != null && list.size() > 0) {
	    McSession mcs = (McSession) list.get(0);
	    return mcs;
	}
	return null;
    }

    @Override
    public void saveMcSession(McSession mcSession) {
	this.getSession().save(mcSession);
    }

    @Override
    public void updateMcSession(McSession mcSession) {
	this.getSession().update(mcSession);
    }

    @Override
    public void removeMcSession(McSession mcSession) {
	this.getSession().delete(mcSession);
    }

    @Override
    public McSession getMcSessionByUser(final Long userId) {
	return (McSession) getSession().createQuery(LOAD_MCSESSION_BY_USER).setParameter("userId", userId)
		.setCacheable(true).uniqueResult();
    }

}
