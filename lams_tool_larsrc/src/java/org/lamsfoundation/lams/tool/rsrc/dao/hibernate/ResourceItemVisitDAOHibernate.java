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
import java.util.List;

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

	private static final String FIND_SUMMARY = "select v.sessionId, s.sessionName,i.uid,i.type ,i.createByAuthor" +
			", i.hide, i.title, u.loginName, count(v.resourceItem) from  "
		+ ResourceItemVisitLog.class.getName() + " as v , "
		+ ResourceItem.class.getName() + "  as i, "
		+ ResourceUser.class.getName() + "  as u, "
		+ ResourceSession.class.getName() + " as s, "
		+ Resource.class.getName() + "  as r "
		+" where i.uid = v.resourceItem.uid and i.createBy.uid = u.uid and v.sessionId = s.sessionId "
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

	public List<Summary> getSummary(Long contentId) {
		
		List<Object[]> result =  getHibernateTemplate().find(FIND_SUMMARY,contentId);
		List<Summary> summaryList = new ArrayList<Summary>(result.size());
		int idx=0;
		for(Object[] list : result){
			Summary sum = new Summary();
			sum.setSessionId((Long) list[idx++]);
			sum.setSessionName((String) list[idx++]);
			sum.setItemUid((Long) list[idx++]);
			sum.setItemType((Short) list[idx++]);
			sum.setItemCreateByAuthor((Boolean) list[idx++]);
			sum.setItemHide((Boolean) list[idx++]);
			sum.setItemTitle((String) list[idx++]);
			sum.setUsername((String) list[idx++]);
			sum.setViewNumber((Integer) list[idx++]);
			summaryList.add(sum);
		}
		return summaryList;
		
	}

	public List<ResourceItemVisitLog> getResourceItemLogBySession(Long sessionId, Long itemUid) {
		
		return getHibernateTemplate().find(FIND_BY_ITEM_BYSESSION,new Object[]{sessionId,itemUid});
	}

}
