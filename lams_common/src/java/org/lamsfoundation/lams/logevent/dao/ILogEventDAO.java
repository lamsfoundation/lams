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


package org.lamsfoundation.lams.logevent.dao;

import java.util.Date;
import java.util.List;

import org.lamsfoundation.lams.logevent.LogEvent;
import org.lamsfoundation.lams.logevent.LogEventType;

/**
 * DAO interface for <code>LogEvent</code>.
 *
 * @author Andrey Balan
 * @see org.lamsfoundation.lams.logevent.LogEvent
 */
public interface ILogEventDAO {

    /**
     * Records occurred event in database.
     *
     * @param logEvent
     *            occurred event
     */
    void save(LogEvent logEvent);

    /**
     * Returns event by the given id.
     *
     * @return
     */
    LogEvent getById(Long logEventId);

    /**
     * Returns all events initiated by user
     *
     * @param userId
     * @return
     */
    List<LogEvent> getByUser(Integer userId);

    /**
     * Returns all events occurred between specified dates
     *
     * @param startDate
     * @param finishDate
     * @return
     */
    List<LogEvent> getEventsOccurredBetween(Date startDate, Date finishDate);

    /** Get the generic event types */
    List<LogEventType> getEventTypes();
    
    /** Get the date of the oldest log event */
    Date getOldestEventDate();

    /** Used for displaying paged lists of events */
    List<Object[]> getEventsForTablesorter(int page, int size, int sorting, String searchUser, String searchTarget,
	    String searchRemarks, Date startDate, Date endDate, String area, Integer typeId);

    int countEventsWithRestrictions(String searchUser, String searchTarget, String searchRemarks, Date startDate,
	    Date endDate, String area, Integer typeId);

}
