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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.rsrc.dao.ResourceItemVisitDAO;
import org.lamsfoundation.lams.tool.rsrc.dto.VisitLogDTO;
import org.lamsfoundation.lams.tool.rsrc.model.Resource;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceItemVisitLog;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceSession;
import org.springframework.stereotype.Repository;

@Repository
public class ResourceItemVisitDAOHibernate extends LAMSBaseDAO implements ResourceItemVisitDAO {

    private static final String FIND_BY_ITEM_AND_USER = "from " + ResourceItemVisitLog.class.getName()
	    + " as r where r.user.userId = ? and r.resourceItem.uid=?";

    private static final String FIND_BY_ITEM_BYSESSION = "from " + ResourceItemVisitLog.class.getName()
	    + " as r where r.sessionId = ? and r.resourceItem.uid=?";

    private static final String FIND_VIEW_COUNT_BY_USER = "select count(*) from " + ResourceItemVisitLog.class.getName()
	    + " as r where  r.sessionId=? and  r.user.userId =?";

    private static final String FIND_SUMMARY = "select v.resourceItem.uid, count(v.resourceItem) from  "
	    + ResourceItemVisitLog.class.getName() + " as v , " + ResourceSession.class.getName() + " as s, "
	    + Resource.class.getName() + "  as r " + " where v.sessionId = :sessionId and v.sessionId = s.sessionId "
	    + " and s.resource.uid = r.uid " + " and r.contentId =:contentId " + " group by v.sessionId, v.resourceItem.uid ";

    private static final String SQL_QUERY_DATES_BY_USER_SESSION = "SELECT MIN(access_date) start_date, MAX(access_date) end_date "
	    + " FROM tl_larsrc11_item_log WHERE user_uid = :userUid  && session_id = :sessionId";

    @Override
    public ResourceItemVisitLog getResourceItemLog(Long itemUid, Long userId) {
	List list = doFind(FIND_BY_ITEM_AND_USER, new Object[] { userId, itemUid });
	if (list == null || list.size() == 0) {
	    return null;
	}
	return (ResourceItemVisitLog) list.get(0);
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
    public Map<Long, Integer> getSummary(Long contentId, Long sessionId) {

	// Note: Hibernate 3.1 query.uniqueResult() returns Integer, Hibernate 3.2 query.uniqueResult() returns Long
    	List<Object[]> result = getSession().createQuery(FIND_SUMMARY)
    		.setLong("sessionId", sessionId)
    		.setLong("contentId", contentId)
    		.list();
	
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
    public List<ResourceItemVisitLog> getResourceItemLogBySession(Long sessionId, Long itemUid) {

	return (List<ResourceItemVisitLog>) doFind(FIND_BY_ITEM_BYSESSION, new Object[] { sessionId, itemUid });
    }

    @Override
    public List<VisitLogDTO> getPagedVisitLogsBySessionAndItem(Long sessionId, Long itemUid, int page, int size,
	    String sortBy, String sortOrder, String searchString) {
	String LOAD_USERS_ORDERED_BY_NAME = "SELECT visit.user.userId, CONCAT(visit.user.lastName, ' ', visit.user.firstName), visit.completeDate, visit.accessDate"
		+ " FROM " + ResourceItemVisitLog.class.getName() + " visit" + " WHERE visit.sessionId = :sessionId "
		+ " AND visit.resourceItem.uid = :itemUid "
		+ " AND (CONCAT(visit.user.lastName, ' ', visit.user.firstName) LIKE CONCAT('%', :searchString, '%')) "
		+ " ORDER BY " + " CASE " + " WHEN :sortBy='userName' THEN CONCAT(user.lastName, ' ', user.firstName) "
		+ " WHEN :sortBy='startTime' THEN visit.accessDate "
		+ " WHEN :sortBy='completeTime' THEN visit.completeDate "
		+ " WHEN :sortBy='timeTaken' THEN TIMEDIFF(visit.completeDate,visit.accessDate) " + " END " + sortOrder;

	Query query = getSession().createQuery(LOAD_USERS_ORDERED_BY_NAME);
	query.setLong("sessionId", sessionId);
	query.setLong("itemUid", itemUid);
	// support for custom search from a toolbar
	searchString = searchString == null ? "" : searchString;
	query.setString("searchString", searchString);
	query.setString("sortBy", sortBy);
	query.setFirstResult(page * size);
	query.setMaxResults(size);
	List<Object[]> list = query.list();

	ArrayList<VisitLogDTO> visitLogDto = new ArrayList<VisitLogDTO>();
	if (list != null && list.size() > 0) {
	    for (Object[] element : list) {

		Long userId = ((Number) element[0]).longValue();
		String userFullName = (String) element[1];
		Date completeDate = element[2] == null ? null : new Date(((Timestamp) element[2]).getTime());
		Date accessDate = element[3] == null ? null : new Date(((Timestamp) element[3]).getTime());
		Date timeTaken = (element[2] == null || element[3] == null) ? null
			: new Date(completeDate.getTime() - accessDate.getTime());

		VisitLogDTO userDto = new VisitLogDTO();
		userDto.setUserId(userId);
		userDto.setUserFullName(userFullName);
		userDto.setCompleteDate(completeDate);
		userDto.setAccessDate(accessDate);
		userDto.setTimeTaken(timeTaken);
		;
		visitLogDto.add(userDto);
	    }
	}

	return visitLogDto;
    }

    @Override
    public int getCountVisitLogsBySessionAndItem(Long sessionId, Long itemUid, String searchString) {
	String COUNT_USERS_BY_SESSION_AND_ITEM = "SELECT COUNT(*) FROM " + ResourceItemVisitLog.class.getName()
		+ " visit WHERE visit.sessionId = :sessionId AND visit.resourceItem.uid = :itemUid"
		+ " AND (CONCAT(visit.user.lastName, ' ', visit.user.firstName) LIKE CONCAT('%', :searchString, '%')) ";

	Query query = getSession().createQuery(COUNT_USERS_BY_SESSION_AND_ITEM);
	query.setLong("sessionId", sessionId);
	query.setLong("itemUid", itemUid);
	// support for custom search from a toolbar
	searchString = searchString == null ? "" : searchString;
	query.setString("searchString", searchString);
	List list = query.list();

	if ((list == null) || (list.size() == 0)) {
	    return 0;
	} else {
	    return ((Number) list.get(0)).intValue();
	}
    }

    @Override
    public Object[] getDateRangeOfAccesses(Long userUid, Long toolSessionId) {
	SQLQuery query = (SQLQuery) getSession().createSQLQuery(SQL_QUERY_DATES_BY_USER_SESSION.toString())
		.setLong("userUid", userUid).setLong("sessionId", toolSessionId);
	Object[] values = (Object[]) query.list().get(0);
	return values;
    }
}
