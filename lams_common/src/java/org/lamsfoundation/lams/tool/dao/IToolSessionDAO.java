/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General License version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General License for more details.
 *
 * You should have received a copy of the GNU General License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.tool.dao;

import java.util.List;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.usermanagement.User;

/**
 * Inteface defines Lesson DAO Methods
 *
 * @author chris, Jacky
 */
public interface IToolSessionDAO {

    /**
     * Retrieves the ToolSession
     *
     * @param toolSessionId
     *            identifies the ToolSession to get
     * @return the ToolSession
     */
    ToolSession getToolSession(Long toolSessionId);

    void saveToolSession(ToolSession toolSession);

    void removeToolSession(ToolSession toolSession);

    /**
     * Get the tool session by learner and activity. Will attempted to get an appropriate grouped tool session (the most
     * common case as this covers a normal group or a whole of class group) and then attempts to get a non-grouped base
     * tool session. The non-grouped tool session is meant to be unique against the user and activity.
     *
     * @returns toolSession may be of subclass NonGroupedToolSession or GroupedToolSession
     */
    ToolSession getToolSessionByLearner(final User learner, final Activity activity);

    /**
     * Get all the tools for a lesson. Does not order the tool sessions with respect to their activities - to do that
     * you need to get the activities first and get the tool session from the activity.
     *
     * @returns list of tool sessions.
     */
    List getToolSessionsByLesson(final Lesson lesson);

    /**
     * Get the tool session by activity. A class-grouped activity should have only one tool session, per activity but a
     * proper grouped activity or an individial activity may have more than one tool sesssion.
     *
     * @see org.lamsfoundation.lams.tool.dao.IToolSessionDAO#getToolSessionByActivity(org.lamsfoundation.lams.learningdesign.Activity)
     * @returns List of toolSessions, may be of subclass NonGroupedToolSession or GroupedToolSession
     */
    List getToolSessionByActivity(final Activity activity);

    void updateToolSession(ToolSession toolSession);
}