/*
 * LessonDAO.java
 *
 * Created on 13 January 2005, 10:32
 */

package org.lamsfoundation.lams.tool.dao.hibernate;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.springframework.orm.hibernate.HibernateCallback;
import org.springframework.orm.hibernate.HibernateTemplate;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.dao.IToolSessionDAO;
import org.lamsfoundation.lams.usermanagement.User;

/**
 * Hibernate implementation of ILessonDAO
 * @author chris, Jacky Fang
 */
public class ToolSessionDAO extends HibernateDaoSupport implements IToolSessionDAO
{

    protected static final String LOAD_NONGROUPED_TOOL_SESSION_BY_LEARNER = 
        "from NonGroupedToolSession s where s.user = :learner and s.toolActivity = :activity";
    /**
     * Retrieves the ToolSession
     * @param toolSessionId identifies the ToolSession to get
     * @return the ToolSession
     */
	public ToolSession getToolSession(Long toolSessionId)
    {
        return (ToolSession)getHibernateTemplate().get(ToolSession.class, toolSessionId);
    }

	/**
	 * Get the tool session by learner and activity. Non-grouped base tool session
	 * meant to be unique against the user and activity.
	 * @see org.lamsfoundation.lams.tool.dao.IToolSessionDAO#getToolSessionByLearner(org.lamsfoundation.lams.usermanagement.User, org.lamsfoundation.lams.learningdesign.Activity)
	 */
	public ToolSession getToolSessionByLearner(final User learner,final Activity activity)
	{
        HibernateTemplate hibernateTemplate = new HibernateTemplate(this.getSessionFactory());

        return (ToolSession)hibernateTemplate.execute(
             new HibernateCallback() 
             {
                 public Object doInHibernate(Session session) throws HibernateException 
                 {
                     return session.createQuery(LOAD_NONGROUPED_TOOL_SESSION_BY_LEARNER)
                     			   .setEntity("learner",learner)
                     			   .setEntity("activity",activity)
                     			   .uniqueResult();
                 }
             }
       );   	    
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
    
}
