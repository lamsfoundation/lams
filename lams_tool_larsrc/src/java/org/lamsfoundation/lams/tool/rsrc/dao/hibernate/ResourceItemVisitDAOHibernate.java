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
import org.lamsfoundation.lams.tool.rsrc.model.ResourceItemVisitLog;

public class ResourceItemVisitDAOHibernate extends BaseDAOHibernate implements ResourceItemVisitDAO{
	
	private static final String FIND_BY_ITEM_AND_USER = "from " + ResourceItemVisitLog.class.getName()
			+ " as r where r.user.userId = ? and r.resourceItem.uid=?";

	private static final String FIND_BY_ITEM_BYSESSION = "from " + ResourceItemVisitLog.class.getName()
			+ " as r where r.session_id = ? and r.resourceItem.uid=?";
	
	private static final String FIND_VIEW_COUNT_BY_USER = "select count(*) from " + ResourceItemVisitLog.class.getName() 
			+ " as r where  r.sessionId=? and  r.user.userId =?";

	private static final String FIND_SUMMARY = "select v.session_id, s.session_name,i.uid,i.item_type ,i.create_by_author" +
			", i.is_hide, i.title, u.login_name, count(v.resource_item_uid) "
		+" from tl_larsrc11_resource_item_visit_log as v , "
		+" tl_larsrc11_resource_item as i, "
		+" tl_larsrc11_user as u, "
		+" tl_larsrc11_session as s, "
		+"  tl_larsrc11_resource as r "
		+" where i.uid = v.resource_item_uid and i.create_by = u.uid and v.session_id = s.session_id "
		+" and s.resource_uid = r.uid "
		+" and r.content_id =? "
		+" group by v.session_id, .v.resource_item_uid ";
	
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
		
		List result =  getHibernateTemplate().find(FIND_SUMMARY,contentId);
		List<Summary> summaryList = new ArrayList(result);
		int idx=0;
		for(Object obj : result){
			List list = (List) obj;
			Summary sum = new Summary();
			sum.setSessionId((Long) list.get(idx++));
			sum.setSessionName((String) list.get(idx++));
			sum.setItemUid((Long) list.get(idx++));
			sum.setItemType((Short) list.get(idx++));
			sum.setItemCreateByAuthor((Boolean) list.get(idx++));
			sum.setItemHide((Boolean) list.get(idx++));
			sum.setItemTitle((String) list.get(idx++));
			sum.setUsername((String) list.get(idx++));
			sum.setViewNumber((Integer) list.get(idx++));
		}
		return summaryList;
		
	}

	public List<ResourceItemVisitLog> getResourceItemLogBySession(Long sessionId, Long itemUid) {
		
		return getHibernateTemplate().find(FIND_BY_ITEM_BYSESSION,new Object[]{sessionId,itemUid});
	}

}
