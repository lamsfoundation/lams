/*
 * LessonDAO.java
 *
 * Created on 13 January 2005, 10:32
 */

package org.lamsfoundation.lams.tool.dao.hibernate;

import org.springframework.orm.hibernate.support.HibernateDaoSupport;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.dao.IToolSessionDAO;

/**
 * Hibernate implementation of ILessonDAO
 * @author chris
 */
public class ToolSessionDAO extends HibernateDaoSupport implements IToolSessionDAO
{

    /**
     * Retrieves the ToolSession
     * @param toolSessionId identifies the ToolSession to get
     * @return the ToolSession
     */
	public ToolSession getToolSession(Long toolSessionId)
    {
        return (ToolSession)getHibernateTemplate().get(ToolSession.class, toolSessionId);
    }
	
    public void saveToolSession(ToolSession toolSession)
    {
        getHibernateTemplate().save(toolSession);
    }
    
}
