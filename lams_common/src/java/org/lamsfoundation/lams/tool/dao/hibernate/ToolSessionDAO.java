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

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.tool.GroupedToolSession;
import org.lamsfoundation.lams.tool.NonGroupedToolSession;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.dao.IToolSessionDAO;
import org.lamsfoundation.lams.usermanagement.User;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Hibernate implementation of ILessonDAO
 * 
 * @author chris, Jacky Fang
 */
public class ToolSessionDAO extends HibernateDaoSupport implements IToolSessionDAO {

    protected static final String LOAD_NONGROUPED_TOOL_SESSION_BY_LEARNER = "from NonGroupedToolSession s where s.user = :learner and s.toolActivity = :activity";
    protected static final String LOAD_GROUPED_TOOL_SESSION_BY_GROUP = "from GroupedToolSession s where s.sessionGroup = :inputgroup and s.toolActivity = :activity";
    protected static final String LOAD_GROUPED_TOOL_SESSION_BY_GROUP2 = "select s from GroupedToolSession as s inner join s.sessionGroup as sg inner join sg.users as u "
	    + " where :learner = u and s.toolActivity = :activity";
    protected static final String LOAD_TOOL_SESSION_BY_ACTIVITY = "from ToolSession s where s.toolActivity = :activity";
    protected static final String LOAD_TOOL_SESSION_BY_LESSON = "from ToolSession s where s.lesson = :lesson";
    private final static String COUNT_GROUPED_LEARNERS_SQL = "select count(*) from lams_user_group ug, lams_tool_session s "
	    + " where ug.group_id = s.group_id and s.tool_session_id = :toolSessionId";

    /**
     * Retrieves the ToolSession
     * 
     * @param toolSessionId
     *            identifies the ToolSession to get
     * @return the ToolSession
     */
    @Override
    public ToolSession getToolSession(Long toolSessionId) {
	ToolSession session = (ToolSession) getHibernateTemplate().get(ToolSession.class, toolSessionId);
	return session;
    }

    /**
     * Get the tool session by learner and activity. Will attempted to get an appropriate grouped
     * tool session (the most common case as this covers a normal group or a whole of class group)
     * and then attempts to get a non-grouped base tool session. The non-grouped tool session
     * is meant to be unique against the user and activity.
     * 
     * @see org.lamsfoundation.lams.tool.dao.IToolSessionDAO#getToolSessionByLearner(org.lamsfoundation.lams.usermanagement.User,
     *      org.lamsfoundation.lams.learningdesign.Activity)
     * @returns toolSession may be of subclass NonGroupedToolSession or GroupedToolSession
     */
    @Override
    public ToolSession getToolSessionByLearner(final User learner, final Activity activity) {
	Query query = this.getSession().createQuery(LOAD_GROUPED_TOOL_SESSION_BY_GROUP2);
	query.setParameter("learner", learner);
	query.setParameter("activity", activity);
	GroupedToolSession groupedToolSession = (GroupedToolSession) query.uniqueResult();
	if (groupedToolSession != null) {
	    return groupedToolSession;
	}

	query = this.getSession().createQuery(LOAD_NONGROUPED_TOOL_SESSION_BY_LEARNER);
	query.setParameter("learner", learner);
	query.setParameter("activity", activity);
	NonGroupedToolSession nonGroupedSession = (NonGroupedToolSession) query.uniqueResult();
	return nonGroupedSession;

    }

    /**
     * Get the tool session by activity. A class-grouped activity should have only one tool session,
     * per activity but a proper grouped activity or an individial activity may have more
     * than one tool sesssion.
     * 
     * @see org.lamsfoundation.lams.tool.dao.IToolSessionDAO#getToolSessionByActivity(org.lamsfoundation.lams.learningdesign.Activity)
     * @returns List of toolSessions, may be of subclass NonGroupedToolSession or GroupedToolSession
     */
    @Override
    public List getToolSessionByActivity(final Activity activity) {
	Query query = this.getSession().createQuery(LOAD_TOOL_SESSION_BY_ACTIVITY);
	query.setParameter("activity", activity);
	return query.list();
    }

    @Override
    public void saveToolSession(ToolSession toolSession) {
	getHibernateTemplate().save(toolSession);
    }

    /**
     * @see org.lamsfoundation.lams.tool.dao.IToolSessionDAO#removeToolSession(org.lamsfoundation.lams.tool.ToolSession)
     */
    @Override
    public void removeToolSession(ToolSession toolSession) {
	getHibernateTemplate().delete(toolSession);
    }

    /**
     * @see org.lamsfoundation.lams.tool.dao.IToolSessionDAO#getToolSessionsByLesson(org.lamsfoundation.lams.lesson.Lesson)
     */
    @Override
    public List getToolSessionsByLesson(final Lesson lesson) {

	Query query = this.getSession().createQuery(LOAD_TOOL_SESSION_BY_LESSON);
	query.setParameter("lesson", lesson);
	return query.list();

    }

    /**
     * @see org.lamsfoundation.lams.tool.dao.IToolSessionDAO#updateToolSession(org.lamsfoundation.lams.tool.ToolSession)
     */
    @Override
    public void updateToolSession(ToolSession toolSession) {
	getHibernateTemplate().update(toolSession);
    }

    /**
     * Get a count of all the possible users for an activity connected to a tool session, where
     * it is a GroupedToolSession ie discriminator-value="1". Don't call on any other type of
     * tool session.
     */
    @Override
    public Integer getCountUsersGrouped(final long toolSessionId) {
	return (Integer) getHibernateTemplate().execute(new HibernateCallback() {
	    @Override
	    public Object doInHibernate(Session session) throws HibernateException {
		Query query = session.createSQLQuery(ToolSessionDAO.COUNT_GROUPED_LEARNERS_SQL);
		query.setLong("toolSessionId", toolSessionId);
		Object value = query.uniqueResult();
		return new Integer(((Number) value).intValue());
	    }
	});
    }

}
