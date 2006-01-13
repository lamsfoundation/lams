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
 * @author chris, Jacky
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
    
	/**
	 * Get the tool session by learner and activity. Will attempted to get an appropriate grouped
	 * tool session (the most common case as this covers a normal group or a whole of class group) 
	 * and then attempts to get a non-grouped base tool session. The non-grouped tool session
	 * is meant to be unique against the user and activity. 
	 * @returns toolSession may be of subclass NonGroupedToolSession or GroupedToolSession
	 */
	public ToolSession getToolSessionByLearner(final User learner,final Activity activity);

	public void updateToolSession(ToolSession toolSession);
}
