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

package org.lamsfoundation.lams.tool.rsrc.service;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;

import org.lamsfoundation.lams.contentrepository.exception.InvalidParameterException;
import org.lamsfoundation.lams.contentrepository.exception.RepositoryCheckedException;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.rating.RatingException;
import org.lamsfoundation.lams.rating.dto.ItemRatingDTO;
import org.lamsfoundation.lams.rating.model.LearnerItemRatingCriteria;
import org.lamsfoundation.lams.tool.rsrc.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.rsrc.dto.SessionDTO;
import org.lamsfoundation.lams.tool.rsrc.dto.VisitLogDTO;
import org.lamsfoundation.lams.tool.rsrc.model.Resource;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceItem;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceSession;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceUser;
import org.lamsfoundation.lams.tool.service.ICommonToolService;

/**
 * Interface that defines the contract that all ShareResource service provider must follow.
 *
 * @author Dapeng.Ni
 */
public interface IResourceService extends ICommonToolService {

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
    List<ResourceItem> getAuthoredItems(Long resourceUid);

    /**
     * Upload resource item file to repository. i.e., single file, websize zip file, or learning object zip file.
     *
     */
    void uploadResourceItemFile(ResourceItem item, File file) throws UploadResourceFileException;

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
     *
     * @throws RepositoryCheckedException
     * @throws InvalidParameterException
     */
    void deleteFromRepository(Long fileUuid, Long fileVersionId)
	    throws InvalidParameterException, RepositoryCheckedException;

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
    void setItemVisible(Long itemUid, Long sessionId, Long contentId, boolean visible);

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

    void notifyTeachersOnFileUpload(Long toolContentId, Long toolSessionId, String sessionMapId, String userName,
	    Long itemUid, String fileName);

    void evict(Object object);

    /** Create an anonymous star rating criteria */
    LearnerItemRatingCriteria createRatingCriteria(Long toolContentId) throws RatingException;

    /** Delete an anonymous star rating criteria */
    int deleteRatingCriteria(Long toolContentId);

    /** Get the actual ratings and the criteria for display */
    List<ItemRatingDTO> getRatingCriteriaDtos(Long toolContentId, Long toolSessionId, Collection<Long> itemIds,
	    Long userId);
}
