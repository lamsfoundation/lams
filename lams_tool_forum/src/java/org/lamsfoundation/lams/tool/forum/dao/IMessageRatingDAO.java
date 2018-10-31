package org.lamsfoundation.lams.tool.forum.dao;

import java.util.List;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.tool.forum.dto.AverageRatingDTO;
import org.lamsfoundation.lams.tool.forum.model.MessageRating;

public interface IMessageRatingDAO extends IBaseDAO {

    /**
     * Return responseRating by the given imageUid and userId.
     * 
     * @param messageId
     * @param userId
     * @return
     */
    MessageRating getRatingByMessageAndUser(Long messageId, Long userId);

    /**
     * Return list of responseRating by the the given imageUid.
     * 
     * @param messageId
     * @param userId
     * @return
     */
    List<MessageRating> getRatingsByMessage(Long messageId);

    /**
     * Returns rating statistics by particular message
     * 
     * @param messageId
     * @return
     */
    AverageRatingDTO getAverageRatingDTOByMessage(Long messageId);

    /**
     * Return total number of posts done by current user in this forum activity
     * 
     * @param userUid
     * @param forumUid
     * @return
     */
    int getNumOfRatingsByUserAndForum(Long userUid, Long forumUid);

    /**
     * Generic method to save an object - handles both update and insert.
     * 
     * @param o
     *            the object to save
     */
    void saveObject(Object o);

}
