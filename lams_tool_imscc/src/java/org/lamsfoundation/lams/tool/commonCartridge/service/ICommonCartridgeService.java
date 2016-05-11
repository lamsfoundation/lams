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

package org.lamsfoundation.lams.tool.commonCartridge.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.contentrepository.IVersionedNode;
import org.lamsfoundation.lams.events.IEventNotificationService;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.tool.commonCartridge.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.commonCartridge.dto.Summary;
import org.lamsfoundation.lams.tool.commonCartridge.model.CommonCartridge;
import org.lamsfoundation.lams.tool.commonCartridge.model.CommonCartridgeConfigItem;
import org.lamsfoundation.lams.tool.commonCartridge.model.CommonCartridgeItem;
import org.lamsfoundation.lams.tool.commonCartridge.model.CommonCartridgeSession;
import org.lamsfoundation.lams.tool.commonCartridge.model.CommonCartridgeUser;
import org.lamsfoundation.lams.usermanagement.User;

/**
 * @author Andrey Balan
 *
 *         Interface that defines the contract that all ShareCommonCartridge service provider must follow.
 */
public interface ICommonCartridgeService {

    /**
     * Get file <code>IVersiondNode</code> by given package id and path.
     *
     * @param packageId
     * @param relPathString
     * @return
     * @throws CommonCartridgeApplicationException
     */
    IVersionedNode getFileNode(Long packageId, String relPathString) throws CommonCartridgeApplicationException;

    /**
     * Get <code>CommonCartridge</code> by toolContentID.
     *
     * @param contentId
     * @return
     */
    CommonCartridge getCommonCartridgeByContentId(Long contentId);

    /**
     * Get a cloned copy of tool default tool content (CommonCartridge) and assign the toolContentId of that copy as the
     * given
     * <code>contentId</code>
     *
     * @param contentId
     * @return
     * @throws CommonCartridgeApplicationException
     */
    CommonCartridge getDefaultContent(Long contentId) throws CommonCartridgeApplicationException;

    /**
     * Get list of commonCartridge items by given commonCartridgeUid. These commonCartridge items must be created by
     * author.
     *
     * @param commonCartridgeUid
     * @return
     */
    List getAuthoredItems(Long commonCartridgeUid);

    /**
     * Upload commonCartridge item file to repository. i.e., single file, websize zip file, or learning object zip file.
     *
     * @param item
     * @param file
     * @return
     * @throws UploadCommonCartridgeFileException
     */
    List<CommonCartridgeItem> uploadCommonCartridgeFile(CommonCartridgeItem item, FormFile file)
	    throws UploadCommonCartridgeFileException;

    // ********** for user methods *************
    /**
     * Create a new user in database.
     */
    void createUser(CommonCartridgeUser commonCartridgeUser);

    /**
     * Get user by given userID and toolContentID.
     *
     * @param long1
     * @return
     */
    CommonCartridgeUser getUserByIDAndContent(Long userID, Long contentId);

    /**
     * Get user by sessionID and UserID
     *
     * @param long1
     * @param sessionId
     * @return
     */
    CommonCartridgeUser getUserByIDAndSession(Long long1, Long sessionId);

    // ********** Repository methods ***********************
    /**
     * Delete file from repository.
     */
    void deleteFromRepository(Long fileUuid, Long fileVersionId) throws CommonCartridgeApplicationException;

    /**
     * Save or update commonCartridge into database.
     *
     * @param CommonCartridge
     */
    void saveOrUpdateCommonCartridge(CommonCartridge CommonCartridge);

    /**
     * Delete resoruce item from database.
     *
     * @param uid
     */
    void deleteCommonCartridgeItem(Long uid);

    /**
     * Return all reource items within the given toolSessionID.
     *
     * @param sessionId
     * @return
     */
    List<CommonCartridgeItem> getCommonCartridgeItemsBySessionId(Long sessionId);

    /**
     * Get commonCartridge which is relative with the special toolSession.
     *
     * @param sessionId
     * @return
     */
    CommonCartridge getCommonCartridgeBySessionId(Long sessionId);

    /**
     * Get commonCartridge toolSession by toolSessionId
     *
     * @param sessionId
     * @return
     */
    CommonCartridgeSession getCommonCartridgeSessionBySessionId(Long sessionId);

    /**
     * Save or update commonCartridge session.
     *
     * @param resSession
     */
    void saveOrUpdateCommonCartridgeSession(CommonCartridgeSession resSession);

    void retrieveComplete(SortedSet<CommonCartridgeItem> commonCartridgeItemList, CommonCartridgeUser user);

    void setItemComplete(Long commonCartridgeItemUid, Long userId, Long sessionId);

    void setItemAccess(Long commonCartridgeItemUid, Long userId, Long sessionId);

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
    String finishToolSession(Long toolSessionId, Long userId) throws CommonCartridgeApplicationException;

    CommonCartridgeItem getCommonCartridgeItemByUid(Long itemUid);

    /**
     * Return monitoring summary list. The return value is list of commonCartridge summaries for each groups.
     *
     * @param contentId
     * @return
     */
    List<List<Summary>> getSummary(Long contentId);

    List<CommonCartridgeUser> getUserListBySessionItem(Long sessionId, Long itemUid);

    /**
     * Set a commonCartridge item visible or not.
     *
     * @param itemUid
     * @param visible
     *            true, item is visible. False, item is invisible.
     */
    void setItemVisible(Long itemUid, boolean visible);

    /**
     * Get commonCartridge item <code>Summary</code> list according to sessionId and skipHide flag.
     *
     * @param sessionId
     * @param skipHide
     *            true, don't get commonCartridge item if its <code>isHide</code> flag is true. Otherwise, get all
     *            commonCartridge item
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
    CommonCartridgeUser getUser(Long uid);

    public IEventNotificationService getEventNotificationService();

    /**
     * Get the CommonCartridge config item by key
     *
     * @param key
     * @return
     */
    public CommonCartridgeConfigItem getConfigItem(String key);

    /**
     * Save a CommonCartridge configItem
     *
     * @param item
     */
    public void saveOrUpdateConfigItem(CommonCartridgeConfigItem item);

    /**
     * Gets a message from commonCartridge bundle. Same as <code><fmt:message></code> in JSP pages.
     *
     * @param key
     *            key of the message
     * @param args
     *            arguments for the message
     * @return message content
     */
    String getLocalisedMessage(String key, Object[] args);

    /**
     * Finds out which lesson the given tool content belongs to and returns its monitoring users.
     *
     * @param sessionId
     *            tool session ID
     * @return list of teachers that monitor the lesson which contains the tool with given session ID
     */
    public List<User> getMonitorsByToolSessionId(Long sessionId);
}
