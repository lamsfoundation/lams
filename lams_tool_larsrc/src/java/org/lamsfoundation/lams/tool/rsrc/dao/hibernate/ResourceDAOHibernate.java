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

package org.lamsfoundation.lams.tool.rsrc.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.rsrc.dao.ResourceDAO;
import org.lamsfoundation.lams.tool.rsrc.model.Resource;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Steve.Ni
 */
@Repository
public class ResourceDAOHibernate extends LAMSBaseDAO implements ResourceDAO {
    private static final String GET_RESOURCE_BY_CONTENTID = "from " + Resource.class.getName()
	    + " as r where r.contentId=?";

    @Override
    public Resource getByContentId(Long contentId) {
	List<Resource> list = doFindCacheable(GET_RESOURCE_BY_CONTENTID, contentId);
	if (list.size() > 0) {
	    return list.get(0);
	} else {
	    return null;
	}
    }

    @Override
    public Resource getByUid(Long resourceUid) {
	return (Resource) getObject(Resource.class, resourceUid);
    }

    @Override
    public void delete(Resource resource) {
	getSession().delete(resource);
    }

}
