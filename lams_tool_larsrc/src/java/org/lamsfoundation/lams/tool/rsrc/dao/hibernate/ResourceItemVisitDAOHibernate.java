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

import org.lamsfoundation.lams.tool.rsrc.dao.ResourceItemVisitDAO;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceItemVisitLog;

public class ResourceItemVisitDAOHibernate extends BaseDAOHibernate implements ResourceItemVisitDAO{
	
	private static final String FIND_BY_ITEM_AND_USER = "from " + ResourceItemVisitLog.class.getName() + " where user.uid = ? and resourceItem.uid=?";
	private static final String FIND_VIEW_COUNT_BY_USER = "select count(*) from " + ResourceItemVisitLog.class.getName() 
			+ " where user.uid = ?";

	public ResourceItemVisitLog getResourceItemLog(Long userUid,Long itemUid){
		List list = getHibernateTemplate().find(FIND_BY_ITEM_AND_USER,new Object[]{userUid,itemUid});
		if(list == null || list.size() ==0)
			return null;
		return (ResourceItemVisitLog) list.get(0);
	}

	public int getUserViewLogCount(Long userUid) {
		List list = getHibernateTemplate().find(FIND_VIEW_COUNT_BY_USER,new Object[]{userUid});
		if(list == null || list.size() ==0)
			return 0;
		return ((Integer) list.get(0)).intValue();
	}

}
