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

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.logevent.LogEvent;
import org.lamsfoundation.lams.logevent.dao.ILogEventDAO;
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

    @Override
    public void save(LogEvent logEvent) {
	super.insert(logEvent);
    }

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

}
