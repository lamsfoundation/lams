/*
 * ILessonDAO.java
 *
 * Created on 13 January 2005, 10:32
 */

package org.lamsfoundation.lams.tool.dao;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.usermanagement.User;

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
    
    public void saveToolSession(ToolSession toolSession);

    public void removeToolSession(ToolSession toolSession);
    
	public ToolSession getToolSessionByLearner(final User learner,final Activity activity);

}
