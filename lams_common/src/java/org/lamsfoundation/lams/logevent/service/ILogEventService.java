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


package org.lamsfoundation.lams.logevent.service;

import java.util.Date;
import java.util.List;

import org.lamsfoundation.lams.logevent.LogEvent;

/**
 * Manages <code>LogEvent</code>s.
 *
 * @author Andrey Balan
 */
public interface ILogEventService {

    /**
     * Records event of specified type in database.
     *
     * @param logEventTypeId
     * @param userId
     */
    void logEvent(Integer logEventTypeId, Integer userId, Long learningDesignId, Long lessonId, Long activityId);

    /**
     * Returns event by the given id.
     *
     * @return
     */
    LogEvent getLogEventById(Long logEventId);

    /**
     * Returns all events initiated by user
     *
     * @param userId
     * @return
     */
    List<LogEvent> getLogEventByUser(Integer userId);

    /**
     * Returns all events occurred between specified dates
     *
     * @param startDate
     *            the first date
     * @param finishDate
     *            the second date
     * @return
     */
    List<LogEvent> getEventsOccurredBetween(Date startDate, Date finishDate);

}
