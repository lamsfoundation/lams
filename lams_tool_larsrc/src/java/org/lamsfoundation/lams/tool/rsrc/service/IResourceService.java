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
package org.lamsfoundation.lams.tool.rsrc.service;

import java.util.List;
import java.util.SortedSet;

import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.contentrepository.IVersionedNode;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.tool.rsrc.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.rsrc.dto.ResourceItemDTO;
import org.lamsfoundation.lams.tool.rsrc.dto.SessionDTO;
import org.lamsfoundation.lams.tool.rsrc.dto.VisitLogDTO;
import org.lamsfoundation.lams.tool.rsrc.model.Resource;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceItem;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceSession;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceUser;

/**
 * @author Dapeng.Ni
 *
 *         Interface that defines the contract that all ShareResource service provider must follow.
 */
public interface IResourceService {

    /**
     * Get file <code>IVersiondNode</code> by given package id and path.
     *
     * @param packageId
     * @param relPathString
     * @return
     * @throws ResourceApplicationException
     */
    IVersionedNode getFileNode(Long packageId, String relPathString) throws ResourceApplicationException;

    /**
     * Get <code>Resource</code> by toolContentID.
     *
     * @param contentId
     * @return
     */
    Resource getResourceByContentId(Long contentId);

    /**
     * Get a cloned copy of tool default tool content (Resource) and assign the toolContentId of that copy as the given
     * <code>contentId</code>
     *
     * @param contentId
     * @return
     * @throws ResourceApplicationException
     */
    Resource getDefaultContent(Long contentId) throws ResourceApplicationException;

    /**
     * Get list of resource items by given resourceUid. These resource items must be created by author.
     *
     * @param resourceUid
     * @return
     */
    List getAuthoredItems(Long resourceUid);

    /**
     * Upload resource item file to repository. i.e., single file, websize zip file, or learning object zip file.
     *
     * @param item
     * @param file
     * @throws UploadResourceFileException
     */
    void uploadResourceItemFile(ResourceItem item, FormFile file) throws UploadResourceFileException;

    // ********** for user methods *************
    /**
     * Create a new user in database.
     */
    void createUser(ResourceUser resourceUser);

    /**
     * Get user by given userID and toolContentID.
     *
     * @param long1
     * @return
     */
    ResourceUser getUserByIDAndContent(Long userID, Long contentId);

    /**
     * Get user by sessionID and UserID
     *
     * @param long1
     * @param sessionId
     * @return
     */
    ResourceUser getUserByIDAndSession(Long long1, Long sessionId);

    // ********** Repository methods ***********************
    /**
     * Delete file from repository.
     */
    void deleteFromRepository(Long fileUuid, Long fileVersionId) throws ResourceApplicationException;

    /**
     * Save or update resource into database.
     *
     * @param Resource
     */
    void saveOrUpdateResource(Resource Resource);

    /**
     * Delete resoruce item from database.
     *
     * @param uid
     */
    void deleteResourceItem(Long uid);

    /**
     * Return all reource items within the given toolSessionID.
     *
     * @param sessionId
     * @return
     */
    List<ResourceItem> getResourceItemsBySessionId(Long sessionId);

    /**
     * Get resource which is relative with the special toolSession.
     *
     * @param sessionId
     * @return
     */
    Resource getResourceBySessionId(Long sessionId);

    /**
     * Get resource toolSession by toolSessionId
     *
     * @param sessionId
     * @return
     */
    ResourceSession getResourceSessionBySessionId(Long sessionId);

    /**
     * Save or update resource session.
     *
     * @param resSession
     */
    void saveOrUpdateResourceSession(ResourceSession resSession);

    void retrieveComplete(SortedSet<ResourceItem> resourceItemList, ResourceUser user);

    void setItemComplete(Long resourceItemUid, Long userId, Long sessionId);

    void setItemAccess(Long resourceItemUid, Long userId, Long sessionId);

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
    String finishToolSession(Long toolSessionId, Long userId) throws ResourceApplicationException;

    ResourceItem getResourceItemByUid(Long itemUid);

    /**
     * Return monitoring summary list. The return value is list of resource summaries for each groups.
     *
     * @param contentId
     * @return
     */
    List<SessionDTO> getSummary(Long contentId);

    List<ResourceUser> getUserListBySessionItem(Long sessionId, Long itemUid);

    List<VisitLogDTO> getPagedVisitLogsBySessionAndItem(Long sessionId, Long itemUid, int page, int size, String sortBy,
	    String sortOrder, String searchString);

    int getCountVisitLogsBySessionAndItem(Long sessionId, Long itemUid, String searchString);

    /**
     * Set a resource item visible or not.
     *
     * @param itemUid
     * @param visible
     *            true, item is visible. False, item is invisible.
     */
    void setItemVisible(Long itemUid, boolean visible);

    /**
     * Get resource item <code>ResourceItemDTO</code> list according to sessionId and skipHide flag.
     *
     * @param sessionId
     * @param skipHide
     *            true, don't get resource item if its <code>isHide</code> flag is true. Otherwise, get all
     *            resource item
     * @return
     */
    List<ResourceItemDTO> exportBySessionId(Long sessionId, boolean skipHide);

    List<List<ResourceItemDTO>> exportByContentId(Long contentId);

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
     * Get Reflect DTO list.
     *
     * @param contentId
     * @return
     */
    List<ReflectDTO> getReflectList(Long contentId);

    /**
     * Get user by UID
     *
     * @param uid
     * @return
     */
    ResourceUser getUser(Long uid);

    void notifyTeachersOnAssigmentSumbit(Long sessionId, ResourceUser resourceUser);

    /**
     * Returns whether activity is grouped and therefore it is expected more than one tool session.
     *
     * @param toolContentID
     * @return
     */
    boolean isGroupedActivity(long toolContentID);
}
