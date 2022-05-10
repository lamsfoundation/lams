/****************************************************************
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
 * ****************************************************************
 */

package org.lamsfoundation.lams.tool.leaderselection.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.leaderselection.dao.ILeaderselectionDAO;
import org.lamsfoundation.lams.tool.leaderselection.model.Leaderselection;
import org.springframework.stereotype.Repository;

/**
 * DAO for accessing the Leaderselection objects - Hibernate specific code.
 */
@Repository
public class LeaderselectionDAO extends LAMSBaseDAO implements ILeaderselectionDAO {

    private static final String FIND_FORUM_BY_CONTENTID = "from Leaderselection leaderselection where leaderselection.toolContentId=?";

    @Override
    public Leaderselection getByContentId(Long toolContentId) {
	List list = doFindCacheable(LeaderselectionDAO.FIND_FORUM_BY_CONTENTID, toolContentId);
	if (list != null && list.size() > 0) {
	    return (Leaderselection) list.get(0);
	} else {
	    return null;
	}
    }

    @Override
    public void saveOrUpdate(Leaderselection leaderselection) {
	getSession().saveOrUpdate(leaderselection);
	getSession().flush();
    }

    @Override
    public void releaseFromCache(Object o) {
	getSessionFactory().getCurrentSession().evict(o);

    }
}
