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

	IVersionedNode getFileNode(Long packageId, String relPathString) throws ResourceApplicationException ;

	Resource getResourceByContentId(Long contentId);

	Resource getDefaultContent(Long contentId) throws ResourceApplicationException;

	List getAuthoredItems(Long resourceUid);

	ResourceAttachment uploadInstructionFile(FormFile file, String type) throws UploadResourceFileException;

	void uploadResourceItemFile(ResourceItem item, FormFile file) throws UploadResourceFileException;

	//********** for user methods *************
	void createUser(ResourceUser resourceUser);
	ResourceUser getUserByID(Long long1);

	//********** Repository methods ***********************
	void deleteFromRepository(Long fileUuid, Long fileVersionId) throws ResourceApplicationException ;

	void saveOrUpdateResource(Resource Resource);

	void deleteResourceAttachment(Long attachmentUid);

	void deleteResourceItem(Long uid);
	
	/**
	 * Return all reource items within the given toolSessionID.
	 * @param sessionId
	 * @return
	 */
	List<ResourceItem> getResourceItemsBySessionId(Long sessionId);
	Resource getResourceBySessionId(Long sessionId);

	ResourceSession getResourceSessionBySessionId(Long sessionId);

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

