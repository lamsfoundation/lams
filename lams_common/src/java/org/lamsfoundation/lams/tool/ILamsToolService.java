/*
 * LamsToolService.java
 *
 * Created on 11 January 2005, 13:49
 */

package org.lamsfoundation.lams.tool;

import java.util.List;
/**
 * Interface defines the services LAMS provides for Tools
 * @author chris
 */
public interface ILamsToolService
{
    /**
     * Returns a list of all learners who can use a specific set of tool content.
     * Note that none/some/all of these users may not reach the associated activity
     * so they may not end up using the content.
     * The purpose of this method is to provide a way for tools to do logic based on 
     * completions against potential completions.
     * @param toolContentID a long value that identifies the tool content (in the Tool and in LAMS).
     * @return a List of all the Learners who are scheduled to use the content.
     * @exception in case of any problems.
     */
    public List getAllPotentialLearners(long toolContentID) throws LamsToolServiceException;
    
    
}
