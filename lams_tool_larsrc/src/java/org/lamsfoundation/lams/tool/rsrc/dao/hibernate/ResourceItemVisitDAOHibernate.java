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

import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.rsrc.dao.ResourceItemVisitDAO;
import org.lamsfoundation.lams.tool.rsrc.dto.VisitLogDTO;
import org.lamsfoundation.lams.tool.rsrc.model.Resource;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceItemVisitLog;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceSession;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
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
	    + " and s.resource.uid = r.uid " + " and r.contentId =:contentId "
	    + " group by v.sessionId, v.resourceItem.uid ";

    private static final String SQL_QUERY_DATES_BY_USER_SESSION = "SELECT MIN(access_date) start_date, MAX(access_date) end_date "
	    + " FROM tl_larsrc11_item_log WHERE user_uid = :userUid  && session_id = :sessionId";

    @Override
    public ResourceItemVisitLog getResourceItemLog(Long itemUid, Long userId) {
	List<?> list = doFind(FIND_BY_ITEM_AND_USER, new Object[] { userId, itemUid });
	if (list == null || list.size() == 0) {
	    return null;
	}
	return (ResourceItemVisitLog) list.get(0);
    }

    @Override
    public int getUserViewLogCount(Long toolSessionId, Long userUid) {
	List<?> list = doFind(FIND_VIEW_COUNT_BY_USER, new Object[] { toolSessionId, userUid });
	if (list == null || list.size() == 0) {
	    return 0;
	}
	return ((Number) list.get(0)).intValue();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<Long, Integer> getSummary(Long contentId, Long sessionId) {

	// Note: Hibernate 3.1 query.uniqueResult() returns Integer, Hibernate 3.2 query.uniqueResult() returns Long
	List<Object[]> result = getSession().createQuery(FIND_SUMMARY).setParameter("sessionId", sessionId)
		.setParameter("contentId", contentId).list();

	Map<Long, Integer> summaryList = new HashMap<>(result.size());
	for (Object[] list : result) {
	    if (list[1] != null) {
		summaryList.put((Long) list[0], ((Number) list[1]).intValue());
	    }
	}
	return summaryList;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ResourceItemVisitLog> getResourceItemLogBySession(Long sessionId, Long itemUid) {
	return doFind(FIND_BY_ITEM_BYSESSION, new Object[] { sessionId, itemUid });
    }

    private static String LOAD_USERS_ORDERED_BY_NAME_SELECT = " SELECT user.user_id, CONCAT(user.first_name, ' ', user.last_name, ' (', user.login_name, ')'), "
	    + "visit.complete_date, visit.access_date ";
    private static String LOAD_USERS_ORDERED_BY_NAME_FROM = " FROM tl_larsrc11_item_log visit "
	    + " JOIN tl_larsrc11_user user ON visit.user_uid = user.uid "
	    + "	AND (CONCAT(user.last_name, ' ', user.first_name) LIKE CONCAT('%', :searchString, '%'))";
    private static String LOAD_USERS_ORDERED_BY_NAME_WHERE = " WHERE visit.session_id = :sessionId AND visit.resource_item_uid = :itemUid "
	    + " ORDER BY CASE WHEN :sortBy='userName' THEN CONCAT(user.last_name, ' ', user.first_name) "
	    + "	WHEN :sortBy='startTime' THEN visit.access_date "
	    + "	WHEN :sortBy='completeTime' THEN visit.complete_date "
	    + "	WHEN :sortBy='timeTaken' THEN TIMEDIFF(visit.complete_date,visit.access_date) END ";

    @SuppressWarnings("unchecked")
    @Override
    public List<VisitLogDTO> getPagedVisitLogsBySessionAndItem(Long sessionId, Long itemUid, int page, int size,
	    String sortBy, String sortOrder, String searchString, IUserManagementService userManagementService) {

	String[] portraitStrings = userManagementService.getPortraitSQL("user.user_id");

	StringBuilder bldr = new StringBuilder(LOAD_USERS_ORDERED_BY_NAME_SELECT).append(portraitStrings[0])
		.append(LOAD_USERS_ORDERED_BY_NAME_FROM).append(portraitStrings[1])
		.append(LOAD_USERS_ORDERED_BY_NAME_WHERE).append(sortOrder);

	Query<Object[]> query = getSession().createSQLQuery(bldr.toString()).setParameter("sessionId", sessionId)
		.setParameter("itemUid", itemUid);

	// support for custom search from a toolbar
	searchString = searchString == null ? "" : searchString;
	query.setParameter("searchString", searchString).setParameter("sortBy", sortBy).setFirstResult(page * size)
		.setMaxResults(size);
	List<Object[]> list = query.list();

	ArrayList<VisitLogDTO> visitLogDtos = new ArrayList<>();
	if (list != null && list.size() > 0) {
	    for (Object[] element : list) {

		Long userId = ((Number) element[0]).longValue();
		String userFullName = (String) element[1];
		Date completeDate = element[2] == null ? null : new Date(((Timestamp) element[2]).getTime());
		Date accessDate = element[3] == null ? null : new Date(((Timestamp) element[3]).getTime());
		Date timeTaken = (element[2] == null || element[3] == null) ? null
			: new Date(completeDate.getTime() - accessDate.getTime());
		String portraitId = (String) element[4];

		VisitLogDTO visitLogDto = new VisitLogDTO();
		visitLogDto.setUserId(userId);
		visitLogDto.setUserFullName(userFullName);
		visitLogDto.setCompleteDate(completeDate);
		visitLogDto.setAccessDate(accessDate);
		visitLogDto.setTimeTaken(timeTaken);
		visitLogDto.setPortraitId(portraitId);
		visitLogDtos.add(visitLogDto);
	    }
	}

	return visitLogDtos;
    }

    @Override
    public int getCountVisitLogsBySessionAndItem(Long sessionId, Long itemUid, String searchString) {
	String COUNT_USERS_BY_SESSION_AND_ITEM = "SELECT COUNT(*) FROM " + ResourceItemVisitLog.class.getName()
		+ " visit WHERE visit.sessionId = :sessionId AND visit.resourceItem.uid = :itemUid"
		+ " AND (CONCAT(visit.user.lastName, ' ', visit.user.firstName) LIKE CONCAT('%', :searchString, '%')) ";

	Query<Number> query = getSession().createQuery(COUNT_USERS_BY_SESSION_AND_ITEM, Number.class);
	query.setParameter("sessionId", sessionId);
	query.setParameter("itemUid", itemUid);
	// support for custom search from a toolbar
	searchString = searchString == null ? "" : searchString;
	query.setParameter("searchString", searchString);
	return query.uniqueResult().intValue();
    }

    @Override
    public Object[] getDateRangeOfAccesses(Long userUid, Long toolSessionId) {
	@SuppressWarnings("unchecked")
	NativeQuery<Object[]> query = getSession().createNativeQuery(SQL_QUERY_DATES_BY_USER_SESSION.toString())
		.setParameter("userUid", userUid).setParameter("sessionId", toolSessionId);
	Object[] values = query.list().get(0);
	return values;
    }
}
