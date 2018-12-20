package org.lamsfoundation.lams.tool.service;

/**
 * Interface contains methods that shall be implemented by all tools.
 * 
 * @author Andrey Balan
 */
public interface ICommonToolService {

    /**
     * Returns whether activity is grouped and therefore it is expected more than one tool session.
     *
     * @param toolContentID
     * @return
     */
    boolean isGroupedActivity(long toolContentID);
    
    /**
     * Audit log the teacher has started editing activity in monitor.
     * 
     * @param toolContentID
     */
    void auditLogStartEditingActivityInMonitor(long toolContentID);
    
    /**
     * Checks whether specified activity is the last one in the learning design.
     */
    boolean isLastActivity(Long toolSessionId);

}
