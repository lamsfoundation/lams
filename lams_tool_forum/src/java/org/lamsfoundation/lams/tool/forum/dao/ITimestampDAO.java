package org.lamsfoundation.lams.tool.forum.dao;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.tool.forum.model.Timestamp;

public interface ITimestampDAO extends IBaseDAO {

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
