/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */
package org.lamsfoundation.lams.tool.rsrc.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.tool.rsrc.dao.ResourceDAO;
import org.lamsfoundation.lams.tool.rsrc.model.Resource;

/**
 * 
 * @author Steve.Ni
 * 
 * @version $Revision$
 */
public class ResourceDAOHibernate extends BaseDAOHibernate implements ResourceDAO{
	private static final String GET_RESOURCE_BY_CONTENTID = "from "+Resource.class.getName()+" as r where r.contentId=?";
	
	public Resource getByContentId(Long contentId) {
		List list = getHibernateTemplate().find(GET_RESOURCE_BY_CONTENTID,contentId);
		if(list.size() > 0)
			return (Resource) list.get(0);
		else
			return null;
	}

	public Resource getByUid(Long resourceUid) {
		return (Resource) getObject(Resource.class,resourceUid);
	}

}
