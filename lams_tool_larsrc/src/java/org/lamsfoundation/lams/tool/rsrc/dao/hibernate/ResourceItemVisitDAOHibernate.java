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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lamsfoundation.lams.tool.rsrc.dao.ResourceItemVisitDAO;
import org.lamsfoundation.lams.tool.rsrc.dto.Summary;
import org.lamsfoundation.lams.tool.rsrc.model.Resource;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceItem;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceItemVisitLog;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceSession;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceUser;

public class ResourceItemVisitDAOHibernate extends BaseDAOHibernate implements ResourceItemVisitDAO{
	
	private static final String FIND_BY_ITEM_AND_USER = "from " + ResourceItemVisitLog.class.getName()
			+ " as r where r.user.userId = ? and r.resourceItem.uid=?";

	private static final String FIND_BY_ITEM_BYSESSION = "from " + ResourceItemVisitLog.class.getName()
			+ " as r where r.sessionId = ? and r.resourceItem.uid=?";
	
	private static final String FIND_VIEW_COUNT_BY_USER = "select count(*) from " + ResourceItemVisitLog.class.getName() 
			+ " as r where  r.sessionId=? and  r.user.userId =?";

	private static final String FIND_SUMMARY = "select v.resourceItem.uid, count(v.resourceItem) from  "
		+ ResourceItemVisitLog.class.getName() + " as v , "
		+ ResourceSession.class.getName() + " as s, "
		+ Resource.class.getName() + "  as r "
		+" where v.sessionId = s.sessionId "
		+" and s.resource.uid = r.uid "
		+" and r.contentId =? "
		+" group by v.sessionId, v.resourceItem.uid ";
	
	public ResourceItemVisitLog getResourceItemLog(Long itemUid,Long userId){
		List list = getHibernateTemplate().find(FIND_BY_ITEM_AND_USER,new Object[]{userId,itemUid});
		if(list == null || list.size() ==0)
			return null;
		return (ResourceItemVisitLog) list.get(0);
	}

	public int getUserViewLogCount(Long toolSessionId ,Long userUid) {
		List list = getHibernateTemplate().find(FIND_VIEW_COUNT_BY_USER,new Object[]{toolSessionId, userUid});
		if(list == null || list.size() ==0)
			return 0;
		return ((Integer) list.get(0)).intValue();
	}

	public Map<Long,Integer> getSummary(Long contentId) {
		
		List<Object[]> result =  getHibernateTemplate().find(FIND_SUMMARY,contentId);
		Map<Long,Integer>  summaryList = new HashMap<Long,Integer> (result.size());
		for(Object[] list : result){
			summaryList.put((Long)list[0],(Integer)list[1]);
		}
		return summaryList;
		
	}

	public List<ResourceItemVisitLog> getResourceItemLogBySession(Long sessionId, Long itemUid) {
		
		return getHibernateTemplate().find(FIND_BY_ITEM_BYSESSION,new Object[]{sessionId,itemUid});
	}

}
