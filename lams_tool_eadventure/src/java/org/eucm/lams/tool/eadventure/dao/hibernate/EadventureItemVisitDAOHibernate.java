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
/* $$Id$$ */
package org.eucm.lams.tool.eadventure.dao.hibernate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eucm.lams.tool.eadventure.dao.EadventureItemVisitDAO;
import org.eucm.lams.tool.eadventure.model.Eadventure;
import org.eucm.lams.tool.eadventure.model.EadventureItemVisitLog;
import org.eucm.lams.tool.eadventure.model.EadventureSession;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.springframework.stereotype.Repository;

@Repository
public class EadventureItemVisitDAOHibernate extends LAMSBaseDAO implements EadventureItemVisitDAO{
	
	private static final String FIND_BY_ITEM_AND_USER = "from " + EadventureItemVisitLog.class.getName()
			+ " as r where r.user.userId = ? and r.eadventure.uid = ?";

	private static final String FIND_BY_ITEM_BYSESSION = "from " + EadventureItemVisitLog.class.getName()
			+ " as r where r.sessionId = ? and r.eadventure.uid=?";
	
	private static final String FIND_VIEW_COUNT_BY_USER = "select count(*) from " + EadventureItemVisitLog.class.getName() 
			+ " as r where  r.sessionId=? and  r.user.userId =?";

	private static final String FIND_SUMMARY = "select v.eadventure.uid, count(v.eadventure) from  "
		+ EadventureItemVisitLog.class.getName() + " as v , "
		+ EadventureSession.class.getName() + " as s, "
		+ Eadventure.class.getName() + "  as r "
		+" where v.sessionId = s.sessionId "
		+" and s.eadventure.uid = r.uid "
		+" and r.contentId =? "
		+" group by v.sessionId, v.eadventure.uid ";
	
	/*private static final String FIND_SUMMARY = "select v.eadventure.uid, count(v.eadventureItem) from  "
		+ EadventureItemVisitLog.class.getName() + " as v , "
		+ EadventureSession.class.getName() + " as s, "
		+ Eadventure.class.getName() + "  as r "
		+" where v.sessionId = s.sessionId "
		+" and s.eadventure.uid = r.uid "
		+" and r.contentId =? "
		+" group by v.sessionId, v.eadventure.uid ";
	
	*/
	public EadventureItemVisitLog getEadventureItemLog(Long itemUid,Long userId){
		List list = doFind(FIND_BY_ITEM_AND_USER,new Object[]{userId,itemUid});
		if(list == null || list.size() ==0)
			return null;
		return (EadventureItemVisitLog) list.get(0);
	}

	public int getUserViewLogCount(Long toolSessionId ,Long userUid) {
		List list = doFind(FIND_VIEW_COUNT_BY_USER,new Object[]{toolSessionId, userUid});
		if(list == null || list.size() ==0)
			return 0;
		return ((Number) list.get(0)).intValue();
	}

	// returns for specified Eadventure UUID the number () of eadVisitLogs asociated to this eAdventure
	@SuppressWarnings("unchecked")
	public Map<Long,Integer> getSummary(Long contentId) {

		// Note: Hibernate 3.1 query.uniqueResult() returns Integer, Hibernate 3.2 query.uniqueResult() returns Long
		List<Object[]> result =  (List<Object[]>) doFind(FIND_SUMMARY,contentId);
		Map<Long,Integer>  summaryList = new HashMap<Long,Integer> (result.size());
		for(Object[] list : result){
			if ( list[1] != null ) {
				summaryList.put((Long)list[0],new Integer(((Number)list[1]).intValue()));
			} 
		}
		return summaryList;
		
	}

	public List<EadventureItemVisitLog> getEadventureItemLogBySession(Long sessionId, Long itemUid) {
		
		return (List<EadventureItemVisitLog>) doFind(FIND_BY_ITEM_BYSESSION,new Object[]{sessionId,itemUid});
	}

}
