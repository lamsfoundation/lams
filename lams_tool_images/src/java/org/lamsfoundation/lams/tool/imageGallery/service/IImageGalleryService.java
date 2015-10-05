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
/* $$Id$$ */
package org.lamsfoundation.lams.tool.imageGallery.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.rating.ToolRatingManager;
import org.lamsfoundation.lams.rating.dto.ItemRatingDTO;
import org.lamsfoundation.lams.tool.imageGallery.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.imageGallery.dto.Summary;
import org.lamsfoundation.lams.tool.imageGallery.dto.UserImageContributionDTO;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGallery;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGalleryConfigItem;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGalleryItem;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGallerySession;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGalleryUser;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageVote;

/**
 * Interface that defines the contract that all ShareImageGallery service provider must follow.
 * 
 * @author Andrey Balan        
 */
public interface IImageGalleryService extends ToolRatingManager {

    /**
     * Get a cloned copy of tool default tool content (ImageGallery) and assign the toolContentId of that copy as the
     * given <code>contentId</code>
     * 
     * @param contentId
     * @return
     * @throws ImageGalleryException
     */
    ImageGallery getDefaultContent(Long contentId) throws ImageGalleryException;

    /**
     * Get list of imageGallery items by given imageGalleryUid. These imageGallery items must be created by author.
     * 
     * @param imageGalleryUid
     * @return
     */
    List getAuthoredItems(Long imageGalleryUid);

    /**
     * Upload imageGallery item file to repository. i.e., single file, websize zip file, or learning object zip file.
     * 
     * @param item
     * @param file
     * @throws UploadImageGalleryFileException
     */
    void uploadImageGalleryItemFile(ImageGalleryItem item, FormFile file) throws UploadImageGalleryFileException;

    // ********** for user methods *************
    /**
     * Create a new user in database.
     */
    void saveUser(ImageGalleryUser imageGalleryUser);

    /**
     * Get user by given userID and toolContentID.
     * 
     * @param long1
     * @return
     */
    ImageGalleryUser getUserByIDAndContent(Long userID, Long contentId);

    /**
     * Get user by sessionID and UserID
     * 
     * @param long1
     * @param sessionId
     * @return
     */
    ImageGalleryUser getUserByIDAndSession(Long userId, Long sessionId);

    /**
     * Save or update imageGallery into database.
     * 
     * @param ImageGallery
     */
    void saveOrUpdateImageGallery(ImageGallery ImageGallery);

    /**
     * Delete resoruce item from database.
     * 
     * @param uid
     */
    void deleteImageGalleryItem(Long uid);

    /**
     * Returns set of images from authoring + the tasks added by members of that particular group.
     * 
     * @param imageGallery
     * @param sessionId
     *            sessionId which defines Group
     * @return
     */
    Set<ImageGalleryItem> getImagesForGroup(ImageGallery imageGallery, Long sessionId);
    
    ItemRatingDTO getRatingCriteriaDtos(Long contentId, Long imageUid, Long userId);

    /**
     * Save/update current ImageGalleryItem.
     * 
     * @param item
     *            current ImageGalleryItem
     * @return
     */
    void saveOrUpdateImageGalleryItem(ImageGalleryItem item);

    /**
     * Delete image with the given uid from imageGallery.
     * 
     * @param toolSessionId
     * @param imageUid
     */
    void deleteImage(Long toolSessionId, Long imageUid);

    /**
     * Save/update ImageVote.
     * 
     * @param vote
     *            ImageVote
     * @return
     */
    void saveOrUpdateImageVote(ImageVote vote);

    /**
     * Get imageGallery which is relative with the special toolSession.
     * 
     * @param sessionId
     * @return
     */
    ImageGallery getImageGalleryBySessionId(Long sessionId);

    /**
     * Get imageGallery toolSession by toolSessionId
     * 
     * @param sessionId
     * @return
     */
    ImageGallerySession getImageGallerySessionBySessionId(Long sessionId);

    /**
     * Get <code>ImageGallery</code> by toolContentID.
     * 
     * @param contentId
     * @return
     */
    ImageGallery getImageGalleryByContentId(Long contentId);

    /**
     * Returns imageVote by the given imageUid and userId
     * 
     * @param imageUid
     * @param userId
     * @return
     */
    ImageVote getImageVoteByImageAndUser(Long imageUid, Long userId);

    /**
     * Return number of imageVotes made by user.
     * 
     * @param userId
     * @return
     */
    int getNumberVotesByUserId(Long userId);

    /**
     * Save or update imageGallery session.
     * 
     * @param resSession
     */
    void saveOrUpdateImageGallerySession(ImageGallerySession resSession);

    void setItemAccess(Long imageGalleryItemUid, Long userId, Long sessionId);

    /**
     * If success return next activity's url, otherwise return null.
     * 
     * @param toolSessionId
     * @param userId
     * @return
     */
    String finishToolSession(Long toolSessionId, Long userId) throws ImageGalleryException;

    ImageGalleryItem getImageGalleryItemByUid(Long itemUid);

    /**
     * Return monitoring summary list. The return value is list of imageGallery summaries for each groups.
     * 
     * @param contentId
     * @return
     */
    List<List<Summary>> getSummary(Long contentId);

    /**
     * Return monitoring image summary. The return value is list of UserImageContributionDTOs for each groups.
     * 
     * @param contentId
     * @param imageUid
     * @return
     */
    List<List<UserImageContributionDTO>> getImageSummary(Long contentId, Long imageUid);

    List<ImageGalleryUser> getUserListBySessionId(Long sessionId);

    /**
     * Set a imageGallery item visible or not.
     * 
     * @param itemUid
     * @param visible
     *            true, item is visible. False, item is invisible.
     */
    void setItemVisible(Long itemUid, boolean visible);

    /**
     * Get imageGallery item <code>Summary</code> list according to sessionId and skipHide flag.
     * 
     * @param sessionId
     * @param skipHide
     *            true, don't get imageGallery item if its <code>isHide</code> flag is true. Otherwise, get all
     *            imageGallery item
     * @return
     */
    List<List<List<UserImageContributionDTO>>> exportBySessionId(Long sessionId, ImageGalleryUser user, boolean skipHide);

    List<List<List<UserImageContributionDTO>>> exportByContentId(Long contentId);

    /**
     * Create refection entry into notebook tool.
     * 
     * @param sessionId
     * @param notebook_tool
     * @param tool_signature
     * @param userId
     * @param entryText
     */
    Long createNotebookEntry(Long sessionId, Integer notebookToolType, String toolSignature, Integer userId,
	    String entryText);

    /**
     * Get reflection entry from notebook tool.
     * 
     * @param sessionId
     * @param idType
     * @param signature
     * @param userID
     * @return
     */
    NotebookEntry getEntry(Long sessionId, Integer idType, String signature, Integer userID);

    /**
     * @param notebookEntry
     */
    void updateEntry(NotebookEntry notebookEntry);

    /**
     * Get Reflect DTO list grouped by sessionID.
     * 
     * @param contentId
     * @return
     */
    Map<Long, Set<ReflectDTO>> getReflectList(Long contentId, boolean setEntry);

    /**
     * Get user by UID
     * 
     * @param uid
     * @return
     */
    ImageGalleryUser getUser(Long uid);

    /**
     * Gets a message from imageGallery bundle. Same as <code><fmt:message></code> in JSP pages.
     * 
     * @param key
     *            key of the message
     * @param args
     *            arguments for the message
     * @return message content
     */
    String getLocalisedMessage(String key, Object[] args);

    /**
     * Get the ImageGallery config item by key
     * 
     * @param key
     * @return
     */
    ImageGalleryConfigItem getConfigItem(String key);

    /**
     * Save a ImageGallery configItem
     * 
     * @param item
     */
    void saveOrUpdateImageGalleryConfigItem(ImageGalleryConfigItem item);

    void notifyTeachersOnImageSumbit(Long sessionId, ImageGalleryUser imageGalleryUser);

    /**
     * Returns whether activity is grouped and therefore it is expected more than one tool session.
     * 
     * @param toolContentID
     * @return
     */
    boolean isGroupedActivity(long toolContentID);
}
