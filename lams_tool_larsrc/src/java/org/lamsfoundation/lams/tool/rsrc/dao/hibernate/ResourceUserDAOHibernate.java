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

import org.lamsfoundation.lams.tool.rsrc.dao.ResourceUserDAO;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceUser;


public class ResourceUserDAOHibernate extends BaseDAOHibernate implements ResourceUserDAO{
	
	private static final String FIND_BY_USER_ID = "from " + ResourceUser.class.getName() + " as u where u.userId =?";
	public ResourceUser getUserByUserID(Class<ResourceUser> name, Long userUid) {
		List list = this.getHibernateTemplate().find(FIND_BY_USER_ID,userUid);
		if(list == null || list.size() == 0)
			return null;
		return (ResourceUser) list.get(0);
	}

}
