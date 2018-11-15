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
import org.lamsfoundation.lams.logevent.LogEventType;

/**
 * Manages <code>LogEvent</code>s.
 *
 * @author Andrey Balan
 */
public interface ILogEventService {

    /** Constants used for sorting */
    static final int SORT_BY_DATE_ASC = 0;
    static final int SORT_BY_DATE_DESC = 1;
    static final int SORT_BY_USER_ASC = 2;
    static final int SORT_BY_USER_DESC = 3;
    static final int SORT_BY_TARGET_ASC = 4;
    static final int SORT_BY_TARGET_DESC = 5;
    

    /**
     * Records event of specified type in database.
     *
     * @param logEventTypeId
     * @param userId
     */
    void logEvent(Integer logEventTypeId, Integer userId, Integer targetUserId, Long lessonId, Long activityId,
	    String description, Date eventDate);

    /**
     * Records event of specified type in database.
     *
     * @param logEventTypeId
     * @param userId
     */
    void logEvent(Integer logEventTypeId, Integer userId, Integer targetUserId, Long lessonId, Long activityId,
	    String description);
    
    /**
     * Records event of specified type in database. Designed to be called from tools, i.e. pass toolContentId instead of
     * lessonId and activityId.
     * 
     * @param eventType
     * @param toolContentId
     * @param learnerUserId optional parameter
     * @param message
     */
    void logToolEvent(int eventType, Long toolContentId, Long learnerUserId, String message);

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

    /** Get the Log Event Types in a structured form */
    List<LogEventType> getEventTypes();

    /** Get the date of the oldest event log entry. Handy for letting a user select a date range */
    Date getOldestEventDate();

    /** Used for displaying paged lists of events */
    List<Object[]> getEventsForTablesorter(int page, int size, int sorting, String searchUser, String searchTarget,
	    String searchRemarks, Date startDate, Date endDate, String area, Integer typeId);

    int countEventsWithRestrictions(String searchUser, String searchTarget, String searchRemarks, Date startDate,
	    Date endDate, String area, Integer typeId);

    /* ***************************** Helper methods used by tools to keep the audit entries consistent *****************/
    void logChangeLearnerContent(Long learnerUserId, String learnerUserLogin, Long toolContentId, String originalText,
	    String newText);
    void logChangeLearnerArbitraryChange(Long learnerUserId, String learnerUserLogin, Long toolContentId, String message);

    void logMarkChange(Long learnerUserId, String learnerUserLogin, Long toolContentId, String originalMark,
	    String newMark);

    void logHideLearnerContent(Long learnerUserId, String learnerUserLogin, Long toolContentId, String hiddenItem);

    void logShowLearnerContent(Long learnerUserId, String learnerUserLogin, Long toolContentId, String hiddenItem);

    void logStartEditingActivityInMonitor(Long toolContentId);

    void logFinishEditingActivityInMonitor(Long toolContentId);

    void logCancelEditingActivityInMonitor(Long toolContentId);
    
}
