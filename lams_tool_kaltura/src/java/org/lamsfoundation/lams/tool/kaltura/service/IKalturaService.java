/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */


package org.lamsfoundation.lams.tool.kaltura.service;

import java.util.List;
import java.util.Set;

import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.tool.kaltura.dto.AverageRatingDTO;
import org.lamsfoundation.lams.tool.kaltura.dto.NotebookEntryDTO;
import org.lamsfoundation.lams.tool.kaltura.model.Kaltura;
import org.lamsfoundation.lams.tool.kaltura.model.KalturaItem;
import org.lamsfoundation.lams.tool.kaltura.model.KalturaSession;
import org.lamsfoundation.lams.tool.kaltura.model.KalturaUser;
import org.lamsfoundation.lams.tool.kaltura.util.KalturaException;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;

/**
 * Defines the services available to the web layer from the Kaltura Service
 */
public interface IKalturaService {
    /**
     * Makes a copy of the default content and assigns it a newContentID
     *
     * @params newContentID
     * @return
     */
    Kaltura copyDefaultContent(Long newContentID);

    /**
     * Returns an instance of the Kaltura tools default content.
     *
     * @return
     */
    Kaltura getDefaultContent();

    /**
     * @param toolSignature
     * @return
     */
    Long getDefaultContentIdBySignature(String toolSignature);

    /**
     * @param toolContentID
     * @return
     */
    Kaltura getKalturaByContentId(Long toolContentID);

    /**
     * @param kaltura
     */
    void saveOrUpdateKaltura(Kaltura kaltura);

    /**
     * @param toolSessionId
     * @return
     */
    KalturaSession getSessionBySessionId(Long toolSessionId);

    /**
     * @param kalturaSession
     */
    void saveOrUpdateKalturaSession(KalturaSession kalturaSession);

    /**
     *
     * @param userId
     * @param toolSessionId
     * @return
     */
    KalturaUser getUserByUserIdAndSessionId(Long userId, Long toolSessionId);

    /**
     *
     * @param uid
     * @return
     */
    KalturaUser getUserByUid(Long uid);

    KalturaUser getUserByUserIdAndContentId(Long userId, Long contentId);

    /**
     *
     * @param kalturaUser
     */
    void saveOrUpdateKalturaUser(KalturaUser kalturaUser);

    /**
     *
     * @param user
     * @param kalturaSession
     * @return
     */
    KalturaUser createKalturaUser(UserDTO user, KalturaSession kalturaSession);

    /**
     *
     * @param id
     * @param idType
     * @param signature
     * @param userID
     * @param title
     * @param entry
     * @return
     */
    Long createNotebookEntry(Long id, Integer idType, String signature, Integer userID, String entry);

    NotebookEntry getEntry(Long sessionId, Integer userId);

    void updateEntry(NotebookEntry notebookEntry);

    List<NotebookEntryDTO> getReflectList(Kaltura kaltura);

    String finishToolSession(Long toolSessionId, Long userId) throws KalturaException;

    boolean isGroupedActivity(long toolContentID);
    
    /**
     * Audit log the teacher has started editing activity in monitor.
     * 
     * @param toolContentID
     */
    void auditLogStartEditingActivityInMonitor(long toolContentID);

    void deleteKalturaItem(Long uid);

    KalturaItem getKalturaItem(Long itemUid);

    void saveKalturaItem(KalturaItem item);

    /**
     * Return all items submitted by the users of a group plus all authored items.
     *
     * @param toolContentId
     * @param toolSessionId
     * @param useId
     * @param isMonitoring
     *            if true show all items from the group regardless of hidden and isAllowSeeingOtherUsersRecordings
     *            parameters.
     * @return
     */
    Set<KalturaItem> getGroupItems(Long toolContentId, Long toolSessionId, Long useId, boolean isMonitoring);

    String getLocalisedMessage(String key, Object[] args);

    /**
     * Creates new KalturaItemVisitLog object with information about this item access.
     *
     * @param itemUid
     * @param userId
     * @param toolSessionId
     */
    void logItemWatched(Long itemUid, Long userId, Long toolSessionId);

    AverageRatingDTO rateMessage(Long itemUid, Long userId, Long toolSessionID, float rating);

    /**
     * Calculates AverageRatingDTO by the given itemUid.
     *
     * @param itemUid
     * @param sessionId
     * @return
     */
    AverageRatingDTO getAverageRatingDto(Long itemUid, Long sessionId);

    /**
     * Update item mark.
     *
     * @param itemUid
     * @param mark
     */
    void markItem(Long itemUid, Long mark);

    /**
     * Hide/show item.
     *
     * @param itemUid
     * @param isHiding
     *            true if item needs to be hidden, false - otherwise
     */
    void hideItem(Long itemUid, boolean isHiding);

    /**
     * Hide/show comment.
     *
     * @param commentUid
     * @param isHiding
     *            true if comment needs to be hidden, false - otherwise
     */
    void hideComment(Long commentUid, boolean isHiding);

    /**
     * Return Number of videos viewed by user.
     *
     * @param toolSessionId
     * @param userId
     * @return
     */
    int getNumberViewedVideos(Long toolSessionId, Long userId);

    /**
     * Return Number of videos uploaded/created by user.
     *
     * @param toolSessionId
     * @param userId
     * @return
     */
    int getNumberUploadedVideos(Long toolSessionId, Long userId);

}
