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


package org.lamsfoundation.lams.tool.pixlr.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.pixlr.dao.IPixlrDAO;
import org.lamsfoundation.lams.tool.pixlr.model.Pixlr;
import org.springframework.stereotype.Repository;

/**
 * DAO for accessing the Pixlr objects - Hibernate specific code.
 */
@Repository
public class PixlrDAO extends LAMSBaseDAO implements IPixlrDAO {

    private static final String FIND_FORUM_BY_CONTENTID = "from Pixlr pixlr where pixlr.toolContentId=?";

    @Override
    @SuppressWarnings("unchecked")
    public Pixlr getByContentId(Long toolContentId) {
	List list = doFind(PixlrDAO.FIND_FORUM_BY_CONTENTID, toolContentId);
	if (list != null && list.size() > 0) {
	    return (Pixlr) list.get(0);
	} else {
	    return null;
	}
    }

    @Override
    public void saveOrUpdate(Pixlr pixlr) {
	getSession().saveOrUpdate(pixlr);
	getSession().flush();
    }

    @Override
    public void releaseFromCache(Object o) {
	getSessionFactory().getCurrentSession().evict(o);

    }
}
