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
import java.util.SortedSet;

import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.contentrepository.IVersionedNode;
import org.lamsfoundation.lams.events.IEventNotificationService;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.tool.imageGallery.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.imageGallery.dto.Summary;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGallery;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGalleryAttachment;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGalleryItem;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGallerySession;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGalleryUser;

/**
 * @author Dapeng.Ni
 * 
 * Interface that defines the contract that all ShareImageGallery service provider must follow.
 */
public interface IImageGalleryService {

    /**
     * Get file <code>IVersiondNode</code> by given package id and path.
     * 
     * @param packageId
     * @param relPathString
     * @return
     * @throws ImageGalleryApplicationException
     */
    IVersionedNode getFileNode(Long packageId, String relPathString) throws ImageGalleryApplicationException;

    /**
     * Get <code>ImageGallery</code> by toolContentID.
     * 
     * @param contentId
     * @return
     */
    ImageGallery getImageGalleryByContentId(Long contentId);

    /**
     * Get a cloned copy of tool default tool content (ImageGallery) and assign the toolContentId of that copy as the
     * given <code>contentId</code>
     * 
     * @param contentId
     * @return
     * @throws ImageGalleryApplicationException
     */
    ImageGallery getDefaultContent(Long contentId) throws ImageGalleryApplicationException;

    /**
     * Get list of imageGallery items by given imageGalleryUid. These imageGallery items must be created by author.
     * 
     * @param imageGalleryUid
     * @return
     */
    List getAuthoredItems(Long imageGalleryUid);

    /**
     * Upload instruciton file into repository.
     * 
     * @param file
     * @param type
     * @return
     * @throws UploadImageGalleryFileException
     */
    ImageGalleryAttachment uploadInstructionFile(FormFile file, String type) throws UploadImageGalleryFileException;

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
    void createUser(ImageGalleryUser imageGalleryUser);

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
    ImageGalleryUser getUserByIDAndSession(Long long1, Long sessionId);

    // ********** Repository methods ***********************
    /**
     * Delete file from repository.
     */
    void deleteFromRepository(Long fileUuid, Long fileVersionId) throws ImageGalleryApplicationException;

    /**
     * Save or update imageGallery into database.
     * 
     * @param ImageGallery
     */
    void saveOrUpdateImageGallery(ImageGallery ImageGallery);

    /**
     * Delete reource attachment(i.e., offline/online instruction file) from database. This method does not delete the
     * file from repository.
     * 
     * @param attachmentUid
     */
    void deleteImageGalleryAttachment(Long attachmentUid);

    /**
     * Delete resoruce item from database.
     * 
     * @param uid
     */
    void deleteImageGalleryItem(Long uid);

    /**
     * Return all reource items within the given toolSessionID.
     * 
     * @param sessionId
     * @return
     */
    List<ImageGalleryItem> getImageGalleryItemsBySessionId(Long sessionId);

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
     * Save or update imageGallery session.
     * 
     * @param resSession
     */
    void saveOrUpdateImageGallerySession(ImageGallerySession resSession);

    void retrieveComplete(SortedSet<ImageGalleryItem> imageGalleryItemList, ImageGalleryUser user);

    void setItemComplete(Long imageGalleryItemUid, Long userId, Long sessionId);

    void setItemAccess(Long imageGalleryItemUid, Long userId, Long sessionId);

    /**
     * the reqired number minus the count of view of the given user.
     * 
     * @param userUid
     * @return
     */
    int checkMiniView(Long toolSessionId, Long userId);

    /**
     * If success return next activity's url, otherwise return null.
     * 
     * @param toolSessionId
     * @param userId
     * @return
     */
    String finishToolSession(Long toolSessionId, Long userId) throws ImageGalleryApplicationException;

    ImageGalleryItem getImageGalleryItemByUid(Long itemUid);

    /**
     * Return monitoring summary list. The return value is list of imageGallery summaries for each groups.
     * 
     * @param contentId
     * @return
     */
    List<List<Summary>> getSummary(Long contentId);

    List<ImageGalleryUser> getUserListBySessionItem(Long sessionId, Long itemUid);

    /**
     * Set a imageGallery item visible or not.
     * 
     * @param itemUid
     * @param visible
     *                true, item is visible. False, item is invisible.
     */
    void setItemVisible(Long itemUid, boolean visible);

    /**
     * Get imageGallery item <code>Summary</code> list according to sessionId and skipHide flag.
     * 
     * @param sessionId
     * @param skipHide
     *                true, don't get imageGallery item if its <code>isHide</code> flag is true. Otherwise, get all
     *                imageGallery item
     * @return
     */
    public List<Summary> exportBySessionId(Long sessionId, boolean skipHide);

    public List<List<Summary>> exportByContentId(Long contentId);

    /**
     * Create refection entry into notebook tool.
     * 
     * @param sessionId
     * @param notebook_tool
     * @param tool_signature
     * @param userId
     * @param entryText
     */
    public Long createNotebookEntry(Long sessionId, Integer notebookToolType, String toolSignature, Integer userId,
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
    public NotebookEntry getEntry(Long sessionId, Integer idType, String signature, Integer userID);

    /**
     * @param notebookEntry
     */
    public void updateEntry(NotebookEntry notebookEntry);

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

    public IEventNotificationService getEventNotificationService();

    /**
     * Gets a message from imageGallery bundle. Same as <code><fmt:message></code> in JSP pages.
     * 
     * @param key
     *                key of the message
     * @param args
     *                arguments for the message
     * @return message content
     */
    String getLocalisedMessage(String key, Object[] args);
}
