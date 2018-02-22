/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */


package org.lamsfoundation.lams.logevent.dao.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.type.StringType;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.logevent.LogEvent;
import org.lamsfoundation.lams.logevent.LogEventType;
import org.lamsfoundation.lams.logevent.dao.ILogEventDAO;
import org.lamsfoundation.lams.logevent.service.ILogEventService;
import org.springframework.stereotype.Repository;

/**
 * Hibernate implementation of <code>ILogEventDAO</code>.
 *
 * @author Andrey Balan
 * @see org.lamsfoundation.lams.timezone.dao.ILogEventDAO
 */
@Repository
public class LogEventDAO extends LAMSBaseDAO implements ILogEventDAO {

    private static final String GET_LOG_EVENT_BY_ID = "from " + LogEvent.class.getName() + "where id = ?";

    private static final String GET_LOG_EVENT_BY_USER = "from " + LogEvent.class.getName()
	    + " where user_id = ? order by occurred_date_time asc";

    private static final String GET_LOG_EVENTS_OCCURED_BETWEEN_DATES = "from " + LogEvent.class.getName()
	    + " where occurred_date_time > ? and occurred_date_time <= ? order by occurred_date_time asc";

    private static final String GET_OLDEST_LOG_EVENT_DATE = "select min(occurredDateTime) from " + LogEvent.class.getName();

    private static final String GET_LOG_EVENT_TYPES = "from " + LogEventType.class.getName();

    @Override
    public void save(LogEvent logEvent) {
	super.insert(logEvent);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public LogEvent getById(Long logEventId) {
	List list = doFind(GET_LOG_EVENT_BY_ID, logEventId);
	if (list.size() > 0) {
	    return (LogEvent) list.get(0);
	} else {
	    return null;
	}
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<LogEvent> getByUser(Integer userId) {
	return (List<LogEvent>) doFind(GET_LOG_EVENT_BY_USER, userId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<LogEvent> getEventsOccurredBetween(Date startDate, Date finishDate) {
	return (List<LogEvent>) doFind(GET_LOG_EVENTS_OCCURED_BETWEEN_DATES, startDate, finishDate);
    }

    /** Get the generic event types */
    @SuppressWarnings("unchecked")
    public List<LogEventType> getEventTypes() {
	return (List<LogEventType>) doFind(GET_LOG_EVENT_TYPES);
    }
    
    /** Get the date of the oldest log event (and not the time) */
    @SuppressWarnings("rawtypes")
    public Date getOldestEventDate() {
	List list = doFind(GET_OLDEST_LOG_EVENT_DATE);
	if ( list.size() > 0 )
	    return (Date) list.get(0);
	return null;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    /**
     * Will return List<[LogEvent, String], [LogEvent, String], ... , [LogEvent, String]>
     * where the String is the lesson name. Lesson name may be null.
     */
    public List<Object[]> getEventsForTablesorter(int page, int size, int sorting, String searchString, Date startDate,
	    Date endDate, String area, Integer typeId) {
	String sortingOrder;
	switch (sorting) {
	    case ILogEventService.SORT_BY_DATE_ASC:
		sortingOrder = "occurred_date_time ASC";
		break;
	    case ILogEventService.SORT_BY_DATE_DESC:
		sortingOrder = "occurred_date_time DESC";
		break;
	    default:
		sortingOrder = "occurred_date_time ASC";
	}

	// Basic select for the user records
	StringBuilder queryText = new StringBuilder();

	queryText.append("SELECT le.*, lesson.name lessonName, activity.title activityName FROM lams_log_event le ")
		.append(" LEFT JOIN lams_lesson lesson ON le.lesson_id = lesson.lesson_id")
		.append(" LEFT JOIN lams_learning_activity activity ON le.activity_id = activity.activity_id");

	addWhereClause(startDate, endDate, area, typeId, queryText);
//	// If filtering by name add a name based where clause (LDEV-3779: must come before the Notebook JOIN statement)
//	buildNameSearch(queryText, searchString);
//
	// Now specify the sort based on the switch statement above.
	queryText.append(" ORDER BY " + sortingOrder);

	SQLQuery query = getSession().createSQLQuery(queryText.toString());
	query.addEntity("event", LogEvent.class);
	query.addScalar("lessonName", StringType.INSTANCE);
	query.addScalar("activityName", StringType.INSTANCE);
	addParameters(startDate, endDate, area, typeId, query);
	query.setFirstResult(page * size).setMaxResults(size);
	return query.list();

    }

    private void addWhereClause(Date startDate, Date endDate, String area, Integer typeId, StringBuilder queryText) {
	boolean needAnd = false;
	if ((startDate != null && endDate != null) || area != null || typeId != null) {
	    queryText.append(" WHERE ");
	    if (startDate != null && endDate != null) {
		queryText.append("( occurred_date_time BETWEEN :startDate AND DATE_ADD(:endDate,INTERVAL 1 DAY)) ");
		needAnd = true;
	    }
	    if (typeId != null) {
		if (needAnd)
		    queryText.append(" AND ");
		queryText.append(" log_event_type_id = :typeId ");
	    } else if (area != null) {
		if (needAnd)
		    queryText.append(" AND ");
		queryText.append(
			" log_event_type_id in (SELECT log_event_type_id FROM lams_log_event_type WHERE area = :area) ");
	    }
	}
    }

    private void addParameters(Date startDate, Date endDate, String area, Integer typeId, SQLQuery query) {
	if (startDate != null && endDate != null) {
	    query.setDate("startDate", startDate);
	    query.setDate("endDate", endDate);
	}
	if (typeId != null) {
	    query.setInteger("typeId", typeId);
	} else if (area != null) {
	    query.setString("area", area);
	}
    }

//    private void buildNameSearch(StringBuilder queryText, String searchString) {
//	if (!StringUtils.isBlank(searchString)) {
//	    String[] tokens = searchString.trim().split("\\s+");
//	    for (String token : tokens) {
//		String escToken = StringEscapeUtils.escapeSql(token);
//		queryText.append(" AND (user.first_name LIKE '%").append(escToken)
//			.append("%' OR user.last_name LIKE '%").append(escToken).append("%' OR user.login_name LIKE '%")
//			.append(escToken).append("%')");
//	    }
//	}
//    }

    @Override
    @SuppressWarnings("rawtypes")
    public int countEventsWithRestrictions(String searchString, Date startDate, Date endDate, String area,
	    Integer typeId) {

	// Basic select for the user records
	StringBuilder queryText = new StringBuilder();

	queryText.append("SELECT count(*) FROM lams_log_event e ");
	addWhereClause(startDate, endDate, area, typeId, queryText);

	SQLQuery query = getSession().createSQLQuery(queryText.toString());
	addParameters(startDate, endDate, area, typeId, query);

	List list = query.list();
	if (list == null || list.size() == 0) {
	    return 0;
	}
	return ((Number) list.get(0)).intValue();
    }

}
