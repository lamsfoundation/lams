/*
 * ILessonDAO.java
 *
 * Created on 13 January 2005, 10:32
 */

package org.lamsfoundation.lams.tool.dao;

import org.lamsfoundation.lams.tool.ToolSession;

/**
 * Inteface defines Lesson DAO Methods
 * @author chris
 */
public interface IToolSessionDAO
{
    
    /**
     * Retrieves the ToolSession
     * @param toolSessionId identifies the ToolSession to get
     * @return the ToolSession
     */
    public ToolSession getToolSession(Long toolSessionId);
    
    public void saveOrUpdateToolSession(ToolSession toolSession);

}
