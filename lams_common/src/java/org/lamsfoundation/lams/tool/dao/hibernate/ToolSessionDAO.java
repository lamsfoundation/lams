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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.tool.dao.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.tool.GroupedToolSession;
import org.lamsfoundation.lams.tool.NonGroupedToolSession;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.dao.IToolSessionDAO;
import org.lamsfoundation.lams.usermanagement.User;
import org.springframework.stereotype.Repository;

/**
 * Hibernate implementation of ILessonDAO
 *
 * @author chris, Jacky Fang
 */
@Repository
public class ToolSessionDAO extends LAMSBaseDAO implements IToolSessionDAO {

    private static final String LOAD_NONGROUPED_TOOL_SESSION_BY_LEARNER = "from NonGroupedToolSession s where s.user = :learner and s.toolActivity = :activity";
    private static final String LOAD_GROUPED_TOOL_SESSION_BY_GROUP2 = "select s from GroupedToolSession as s inner join s.sessionGroup as sg inner join sg.users as u "
	    + " where :learner = u and s.toolActivity = :activity";
    private static final String LOAD_TOOL_SESSION_BY_ACTIVITY = "from ToolSession s where s.toolActivity = :activity";
    private static final String LOAD_TOOL_SESSION_BY_LESSON = "from ToolSession s where s.lesson = :lesson";

    /**
     * Retrieves the ToolSession
     *
     * @param toolSessionId
     *            identifies the ToolSession to get
     * @return the ToolSession
     */
    @Override
    public ToolSession getToolSession(Long toolSessionId) {
	ToolSession session = (ToolSession) getSession().get(ToolSession.class, toolSessionId);
	return session;
    }

    /**
     * Get the tool session by learner and activity. Will attempted to get an appropriate grouped tool session (the most
     * common case as this covers a normal group or a whole of class group) and then attempts to get a non-grouped base
     * tool session. The non-grouped tool session is meant to be unique against the user and activity.
     *
     * @see org.lamsfoundation.lams.tool.dao.IToolSessionDAO#getToolSessionByLearner(org.lamsfoundation.lams.usermanagement.User,
     *      org.lamsfoundation.lams.learningdesign.Activity)
     * @returns toolSession may be of subclass NonGroupedToolSession or GroupedToolSession
     */
    @Override
    public ToolSession getToolSessionByLearner(final User learner, final Activity activity) {
	Query query = getSessionFactory().getCurrentSession()
		.createQuery(ToolSessionDAO.LOAD_GROUPED_TOOL_SESSION_BY_GROUP2);
	query.setParameter("learner", learner);
	query.setParameter("activity", activity);
	GroupedToolSession groupedToolSession = (GroupedToolSession) query.uniqueResult();
	if (groupedToolSession != null) {
	    return groupedToolSession;
	}

	query = getSessionFactory().getCurrentSession()
		.createQuery(ToolSessionDAO.LOAD_NONGROUPED_TOOL_SESSION_BY_LEARNER);
	query.setParameter("learner", learner);
	query.setParameter("activity", activity);
	NonGroupedToolSession nonGroupedSession = (NonGroupedToolSession) query.uniqueResult();
	return nonGroupedSession;

    }

    /**
     * Get the tool session by activity. A class-grouped activity should have only one tool session, per activity but a
     * proper grouped activity or an individial activity may have more than one tool sesssion.
     *
     * @see org.lamsfoundation.lams.tool.dao.IToolSessionDAO#getToolSessionByActivity(org.lamsfoundation.lams.learningdesign.Activity)
     * @returns List of toolSessions, may be of subclass NonGroupedToolSession or GroupedToolSession
     */
    @Override
    public List getToolSessionByActivity(final Activity activity) {
	Query query = getSessionFactory().getCurrentSession().createQuery(ToolSessionDAO.LOAD_TOOL_SESSION_BY_ACTIVITY);
	query.setParameter("activity", activity);
	return query.list();
    }

    @Override
    public void saveToolSession(ToolSession toolSession) {
	getSession().save(toolSession);
    }

    /**
     * @see org.lamsfoundation.lams.tool.dao.IToolSessionDAO#removeToolSession(org.lamsfoundation.lams.tool.ToolSession)
     */
    @Override
    public void removeToolSession(ToolSession toolSession) {
	getSession().delete(toolSession);
    }

    /**
     * @see org.lamsfoundation.lams.tool.dao.IToolSessionDAO#getToolSessionsByLesson(org.lamsfoundation.lams.lesson.Lesson)
     */
    @Override
    public List getToolSessionsByLesson(final Lesson lesson) {

	Query query = getSessionFactory().getCurrentSession().createQuery(ToolSessionDAO.LOAD_TOOL_SESSION_BY_LESSON);
	query.setParameter("lesson", lesson);
	return query.list();

    }

    /**
     * @see org.lamsfoundation.lams.tool.dao.IToolSessionDAO#updateToolSession(org.lamsfoundation.lams.tool.ToolSession)
     */
    @Override
    public void updateToolSession(ToolSession toolSession) {
	getSession().update(toolSession);
    }
}