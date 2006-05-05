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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.tool.rsrc.service;

import java.util.List;

import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.contentrepository.IVersionedNode;
import org.lamsfoundation.lams.tool.rsrc.dto.Summary;
import org.lamsfoundation.lams.tool.rsrc.model.Resource;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceAttachment;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceItem;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceSession;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceUser;

/**
 * @author Dapeng.Ni
 * 
 * Interface that defines the contract that all ShareResource service provider must follow.
 */
public interface IResourceService 
{

	/**
	 * Get file <code>IVersiondNode</code> by given package id and path.
	 * @param packageId
	 * @param relPathString
	 * @return
	 * @throws ResourceApplicationException
	 */
	IVersionedNode getFileNode(Long packageId, String relPathString) throws ResourceApplicationException ;
	
	/**
	 * Get <code>Resource</code> by toolContentID.
	 * @param contentId
	 * @return
	 */
	Resource getResourceByContentId(Long contentId);
	/**
	 * Get a cloned copy of  tool default tool content (Resource) and assign the toolContentId of that copy as the 
	 * given <code>contentId</code> 
	 * @param contentId
	 * @return
	 * @throws ResourceApplicationException
	 */
	Resource getDefaultContent(Long contentId) throws ResourceApplicationException;
	
	/**
	 * Get list of resource items by given resourceUid. These resource items must be created by author.
	 * @param resourceUid
	 * @return
	 */
	List getAuthoredItems(Long resourceUid);
	/**
	 * Upload instruciton file into repository.
	 * @param file
	 * @param type
	 * @return
	 * @throws UploadResourceFileException
	 */
	ResourceAttachment uploadInstructionFile(FormFile file, String type) throws UploadResourceFileException;
	
	/**
	 * Upload resource item file to repository. i.e., single file, websize zip file, or learning object zip file.
	 * @param item
	 * @param file
	 * @throws UploadResourceFileException
	 */
	void uploadResourceItemFile(ResourceItem item, FormFile file) throws UploadResourceFileException;

	//********** for user methods *************
	/**
	 * Create a new user in database.
	 */
	void createUser(ResourceUser resourceUser);
	/**
	 * Get user by given userID.
	 * @param long1
	 * @return
	 */
	ResourceUser getUserByID(Long userID);

	//********** Repository methods ***********************
	/**
	 * Delete file from repository.
	 */
	void deleteFromRepository(Long fileUuid, Long fileVersionId) throws ResourceApplicationException ;

	/**
	 * Save or update resource into database.
	 * @param Resource
	 */
	void saveOrUpdateResource(Resource Resource);
	/**
	 * Delete reource attachment(i.e., offline/online instruction file) from database. This method does not
	 * delete the file from repository.
	 * 
	 * @param attachmentUid
	 */
	void deleteResourceAttachment(Long attachmentUid);
	/**
	 * Delete resoruce item from database.
	 * @param uid
	 */
	void deleteResourceItem(Long uid);
	
	/**
	 * Return all reource items within the given toolSessionID.
	 * @param sessionId
	 * @return
	 */
	List<ResourceItem> getResourceItemsBySessionId(Long sessionId);
	/**
	 * Get resource which is relative with the special toolSession.
	 * @param sessionId
	 * @return
	 */
	Resource getResourceBySessionId(Long sessionId);
	/**
	 * Get resource toolSession by toolSessionId
	 * @param sessionId
	 * @return
	 */
	ResourceSession getResourceSessionBySessionId(Long sessionId);

	/**
	 * Save or update resource session.
	 * @param resSession
	 */
	void saveOrUpdateResourceSession(ResourceSession resSession);
	
	void retrieveComplete(List<ResourceItem> resourceItemList, ResourceUser user);
	void setItemComplete(Long resourceItemUid, Long userId , Long sessionId);
	void setItemAccess(Long resourceItemUid, Long userId, Long sessionId);
	/**
	 * the reqired number minus the count of view of the given user.
	 * @param userUid
	 * @return
	 */
	int checkMiniView(Long toolSessionId, Long userId);
	/**
	 * If success return next activity's url, otherwise return null.
	 * @param toolSessionId
	 * @param userId
	 * @return
	 */
	String finishToolSession(Long toolSessionId, Long userId)  throws ResourceApplicationException;

	ResourceItem getResourceItemByUid(Long itemUid);

	List<List> getSummary(Long contentId);

	List<ResourceUser> getUserListBySessionItem(Long sessionId, Long itemUid);

	/**
	 * Set a resource item visible or not.
	 * @param itemUid
	 * @param visible true, item is visible. False, item is invisible.
	 */
	void setItemVisible(Long itemUid, boolean visible);

	/**
	 * Get resource item <code>Summary</code> list according to sessionId and skipHide flag.
	 *  
	 * @param sessionId
	 * @param skipHide true, don't get resource item if its <code>isHide</code> flag is true.
	 * 				  Otherwise, get all resource item  
	 * @return
	 */
	public List<Summary> exportBySessionId(Long sessionId, boolean skipHide);
	public List<List> exportByContentId(Long contentId); 
}

