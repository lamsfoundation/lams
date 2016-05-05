package org.lamsfoundation.lams.tool.forum.persistence;

import org.lamsfoundation.lams.dao.IBaseDAO;

public interface ITimestampDAO extends IBaseDAO {

    void delete(Timestamp timestamp);

    /**
     * Save timestamp.
     * 
     * @param timestamp
     * @return
     */
    void saveOrUpdate(Timestamp timestamp);

    /**
     * Get timestamp.
     * 
     * @param messageId
     * @param forumUserId
     * @return
     */
    Timestamp getTimestamp(Long messageId, Long forumUserId);

}
