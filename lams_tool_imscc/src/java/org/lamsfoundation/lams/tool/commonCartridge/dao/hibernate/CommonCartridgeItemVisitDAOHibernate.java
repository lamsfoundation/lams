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

package org.lamsfoundation.lams.tool.commonCartridge.dao.hibernate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.query.NativeQuery;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.commonCartridge.dao.CommonCartridgeItemVisitDAO;
import org.lamsfoundation.lams.tool.commonCartridge.model.CommonCartridge;
import org.lamsfoundation.lams.tool.commonCartridge.model.CommonCartridgeItemVisitLog;
import org.lamsfoundation.lams.tool.commonCartridge.model.CommonCartridgeSession;
import org.springframework.stereotype.Repository;

@Repository
public class CommonCartridgeItemVisitDAOHibernate extends LAMSBaseDAO implements CommonCartridgeItemVisitDAO {

    private static final String FIND_BY_ITEM_AND_USER = "from " + CommonCartridgeItemVisitLog.class.getName()
	    + " as r where r.user.userId = ? and r.commonCartridgeItem.uid=?";

    private static final String FIND_BY_ITEM_BYSESSION = "from " + CommonCartridgeItemVisitLog.class.getName()
	    + " as r where r.sessionId = ? and r.commonCartridgeItem.uid=?";

    private static final String FIND_VIEW_COUNT_BY_USER = "select count(*) from "
	    + CommonCartridgeItemVisitLog.class.getName() + " as r where  r.sessionId=? and  r.user.userId =?";

    private static final String FIND_SUMMARY = "select v.commonCartridgeItem.uid, count(v.commonCartridgeItem) from  "
	    + CommonCartridgeItemVisitLog.class.getName() + " as v , " + CommonCartridgeSession.class.getName()
	    + " as s, " + CommonCartridge.class.getName() + "  as r " + " where v.sessionId = s.sessionId "
	    + " and s.commonCartridge.uid = r.uid " + " and r.contentId =? "
	    + " group by v.sessionId, v.commonCartridgeItem.uid ";

    private static final String SQL_QUERY_DATES_BY_USER_SESSION = "SELECT MIN(access_date) start_date, MAX(access_date) end_date "
	    + " FROM tl_laimsc11_item_log WHERE user_uid = :userUid";

    @Override
    public CommonCartridgeItemVisitLog getCommonCartridgeItemLog(Long itemUid, Long userId) {
	List list = doFind(FIND_BY_ITEM_AND_USER, new Object[] { userId, itemUid });
	if (list == null || list.size() == 0) {
	    return null;
	}
	return (CommonCartridgeItemVisitLog) list.get(0);
    }

    @Override
    public int getUserViewLogCount(Long toolSessionId, Long userUid) {
	List list = doFind(FIND_VIEW_COUNT_BY_USER, new Object[] { toolSessionId, userUid });
	if (list == null || list.size() == 0) {
	    return 0;
	}
	return ((Number) list.get(0)).intValue();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<Long, Integer> getSummary(Long contentId) {

	// Note: Hibernate 3.1 query.uniqueResult() returns Integer, Hibernate 3.2 query.uniqueResult() returns Long
	List<Object[]> result = (List<Object[]>) doFind(FIND_SUMMARY, contentId);
	Map<Long, Integer> summaryList = new HashMap<Long, Integer>(result.size());
	for (Object[] list : result) {
	    if (list[1] != null) {
		summaryList.put((Long) list[0], new Integer(((Number) list[1]).intValue()));
	    }
	}
	
	return summaryList;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<CommonCartridgeItemVisitLog> getCommonCartridgeItemLogBySession(Long sessionId, Long itemUid) {
	return (List<CommonCartridgeItemVisitLog>) doFind(FIND_BY_ITEM_BYSESSION, new Object[] { sessionId, itemUid });
    }

    @Override
    public Object[] getDateRangeOfAccesses(Long userUid) {
	@SuppressWarnings("unchecked")
	NativeQuery<Object[]> query = getSession().createNativeQuery(SQL_QUERY_DATES_BY_USER_SESSION.toString())
		.setParameter("userUid", userUid);
	Object[] values = query.list().get(0);
	return values;
    }
}
