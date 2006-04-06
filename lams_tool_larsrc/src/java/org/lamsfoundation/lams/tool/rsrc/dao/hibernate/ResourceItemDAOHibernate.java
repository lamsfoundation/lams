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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.tool.rsrc.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.tool.rsrc.dao.ResourceItemDAO;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceItem;

public class ResourceItemDAOHibernate extends BaseDAOHibernate implements ResourceItemDAO{
	
	private static final String FIND_AUTHORING_ITEMS = "from " + ResourceItem.class.getName() + " where resource_uid = ? order by create_date asc";
	
	public List getAuthoringItems(Long resourceUid) {
		
		return this.getHibernateTemplate().find(FIND_AUTHORING_ITEMS,resourceUid); 
	}

}
