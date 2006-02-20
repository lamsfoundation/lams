/*
 * LessonDAO.java
 *
 * Created on 13 January 2005, 10:32
 */

package org.lamsfoundation.lams.tool.dao.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.tool.GroupedToolSession;
import org.lamsfoundation.lams.tool.NonGroupedToolSession;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.dao.IToolSessionDAO;
import org.lamsfoundation.lams.usermanagement.User;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Hibernate implementation of ILessonDAO
 * @author chris, Jacky Fang
 */
public class ToolSessionDAO extends HibernateDaoSupport implements IToolSessionDAO
{

    protected static final String LOAD_NONGROUPED_TOOL_SESSION_BY_LEARNER = 
        "from NonGroupedToolSession s where s.user = :learner and s.toolActivity = :activity";
    protected static final String LOAD_GROUPED_TOOL_SESSION_BY_GROUP = 
        "from GroupedToolSession s where s.sessionGroup = :inputgroup and s.toolActivity = :activity";
    protected static final String LOAD_GROUPED_TOOL_SESSION_BY_GROUP2 = 
        "select s from GroupedToolSession as s inner join s.sessionGroup as sg inner join sg.users as u "
    	+" where :learner = u and s.toolActivity = :activity";
    protected static final String LOAD_TOOL_SESSION_BY_LESSON =  
        "from ToolSession s where s.lesson = :lesson";

    /**
     * Retrieves the ToolSession
     * @param toolSessionId identifies the ToolSession to get
     * @return the ToolSession
     */
	public ToolSession getToolSession(Long toolSessionId)
    {
        ToolSession session = (ToolSession) getHibernateTemplate().get(ToolSession.class, toolSessionId);
        return session;
    }

	/**
	 * Get the tool session by learner and activity. Will attempted to get an appropriate grouped
	 * tool session (the most common case as this covers a normal group or a whole of class group) 
	 * and then attempts to get a non-grouped base tool session. The non-grouped tool session
	 * is meant to be unique against the user and activity. 
	 * @see org.lamsfoundation.lams.tool.dao.IToolSessionDAO#getToolSessionByLearner(org.lamsfoundation.lams.usermanagement.User, org.lamsfoundation.lams.learningdesign.Activity)
	 * @returns toolSession may be of subclass NonGroupedToolSession or GroupedToolSession
	 */
	public ToolSession getToolSessionByLearner(final User learner,final Activity activity)
	{
		Query query = this.getSession().createQuery(LOAD_GROUPED_TOOL_SESSION_BY_GROUP2);
		query.setParameter("learner",learner);
		query.setParameter("activity",activity);
		GroupedToolSession groupedToolSession = (GroupedToolSession) query.uniqueResult();
		if ( groupedToolSession != null ) 
			return groupedToolSession;

		query = this.getSession().createQuery(LOAD_NONGROUPED_TOOL_SESSION_BY_LEARNER);
		query.setParameter("learner",learner);
		query.setParameter("activity",activity);
		NonGroupedToolSession nonGroupedSession = (NonGroupedToolSession) query.uniqueResult();
		return nonGroupedSession;
		
	}

    public void saveToolSession(ToolSession toolSession)
    {
        getHibernateTemplate().save(toolSession);
    }
    /**
     * @see org.lamsfoundation.lams.tool.dao.IToolSessionDAO#removeToolSession(org.lamsfoundation.lams.tool.ToolSession)
     */
    public void removeToolSession(ToolSession toolSession)
    {
        getHibernateTemplate().delete(toolSession);
    }

    /**
     * @see org.lamsfoundation.lams.tool.dao.IToolSessionDAO#getToolSessionsByLesson(org.lamsfoundation.lams.lesson.Lesson)
     */
	public List getToolSessionsByLesson(final Lesson lesson) {

		Query query = this.getSession().createQuery(LOAD_TOOL_SESSION_BY_LESSON);
		query.setParameter("lesson",lesson);
		return query.list();

	}

    /**
     * @see org.lamsfoundation.lams.tool.dao.IToolSessionDAO#updateToolSession(org.lamsfoundation.lams.tool.ToolSession)
     */
    public void updateToolSession(ToolSession toolSession)
    {
        getHibernateTemplate().update(toolSession);
    }


    
}
