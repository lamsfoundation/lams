/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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
/* $$Id$$ */

package org.lamsfoundation.lams.tool.gmap.dao.hibernate;

import java.util.List;

import org.hibernate.FlushMode;
import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.tool.gmap.dao.IGmapDAO;
import org.lamsfoundation.lams.tool.gmap.model.Gmap;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * DAO for accessing the Gmap objects - Hibernate specific code.
 */
public class GmapDAO extends BaseDAO implements IGmapDAO {

	private static final String FIND_FORUM_BY_CONTENTID = "from Gmap gmap where gmap.toolContentId=?";

	public Gmap getByContentId(Long toolContentId) {
		List list = getHibernateTemplate().find(FIND_FORUM_BY_CONTENTID,
				toolContentId);
		if (list != null && list.size() > 0)
			return (Gmap) list.get(0);
		else
			return null;
	}

	public void saveOrUpdate(Gmap gmap) {
		this.getHibernateTemplate().saveOrUpdate(gmap);
		this.getHibernateTemplate().flush();
	}
}
