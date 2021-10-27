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

package org.lamsfoundation.lams.tool.dokumaran.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.dokumaran.dao.DokumaranDAO;
import org.lamsfoundation.lams.tool.dokumaran.model.Dokumaran;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Andrey Balan
 */
@Repository
public class DokumaranDAOHibernate extends LAMSBaseDAO implements DokumaranDAO {
    private static final String GET_RESOURCE_BY_CONTENTID = "from " + Dokumaran.class.getName()
	    + " as r where r.contentId=?";

    @Override
    public Dokumaran getByContentId(Long contentId) {
	List<?> list = doFindCacheable(GET_RESOURCE_BY_CONTENTID, contentId);
	if (list.size() > 0) {
	    return (Dokumaran) list.get(0);
	} else {
	    return null;
	}
    }

    @Override
    public Dokumaran getByUid(Long dokumaranUid) {
	return (Dokumaran) getObject(Dokumaran.class, dokumaranUid);
    }

    @Override
    public void delete(Dokumaran dokumaran) {
	getSession().delete(dokumaran);
    }

}
