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

package org.lamsfoundation.lams.tool.mindmap.service;

import java.util.List;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.tool.mindmap.model.Mindmap;
import org.lamsfoundation.lams.tool.mindmap.model.MindmapAttachment;
import org.lamsfoundation.lams.tool.mindmap.model.MindmapNode;
import org.lamsfoundation.lams.tool.mindmap.model.MindmapRequest;
import org.lamsfoundation.lams.tool.mindmap.model.MindmapSession;
import org.lamsfoundation.lams.tool.mindmap.model.MindmapUser;
import org.lamsfoundation.lams.tool.mindmap.util.MindmapException;
import org.lamsfoundation.lams.tool.mindmap.util.xmlmodel.NodeModel;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.MessageService;

/**
 * Defines the services available to the web layer from the Mindmap Service
 */
public interface IMindmapService {
    /**
     * Makes a copy of the default content and assigns it a newContentID
     * @param newContentID
     * @return 
     */
    public Mindmap copyDefaultContent(Long newContentID);

    /**
     * Returns an instance of the Mindmap tools default content.
     * @return 
     */
    public Mindmap getDefaultContent();

    /**
     * @param toolSignature
     * @return
     */
    public Long getDefaultContentIdBySignature(String toolSignature);

    /**
     * @param toolContentID
     * @return
     */
    public Mindmap getMindmapByContentId(Long toolContentID);

    /**
     * @param Uid
     * @return 
     */
    public Mindmap getMindmapByUid(Long Uid);
    
    /**
     * @param toolContentId
     * @param file
     * @param type
     * @return 
     */
    public MindmapAttachment uploadFileToContent(Long toolContentId, FormFile file, String type);

    /**
     * @param uuid
     * @param versionID
     */
    public void deleteFromRepository(Long uuid, Long versionID) throws MindmapException;

    /**
     * @param contentID
     * @param uuid
     * @param versionID
     * @param type
     */
    public void deleteInstructionFile(Long contentID, Long uuid, Long versionID, String type);

    /**
     * @param mindmap
     */
    public void saveOrUpdateMindmap(Mindmap mindmap);

    /**
     * @param toolSessionId
     * @return 
     */
    public MindmapSession getSessionBySessionId(Long toolSessionId);

    /**
     * @param mindmapSession
     */
    public void saveOrUpdateMindmapSession(MindmapSession mindmapSession);

    /**
     * 
     * @param userId
     * @param toolSessionId
     * @return 
     */
    public MindmapUser getUserByUserIdAndSessionId(Long userId, Long toolSessionId);

    /**
     * 
     * @param uid
     * @return
     */
    public MindmapUser getUserByUID(Long uid);

    /**
     * 
     * @param mindmapUser
     */
    public void saveOrUpdateMindmapUser(MindmapUser mindmapUser);

    /**
     * 
     * @param user
     * @param mindmapSession
     * @return
     */
    public MindmapUser createMindmapUser(UserDTO user, MindmapSession mindmapSession);

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

    /**
     * 
     * @param uid
     * @return
     */
    NotebookEntry getEntry(Long uid);

    /**
     * 
     * @param uid
     * @param title
     * @param entry
     */
    void updateEntry(Long uid, String entry);
    
    public void updateEntry(NotebookEntry notebookEntry);
    
    boolean isGroupedActivity(long toolContentID);
    
    public void setMindmapMessageService(MessageService mindmapMessageService);

    public MessageService getMindmapMessageService();
    
    /** Outputs */
    public int getNumNodes(Long learnerId, Long toolSessionId);
    
    public String getLanguageXML();
    
    public void deleteNodeByUniqueMindmapUser(Long uniqueId, Long mindmapId, Long userId);
    
    public void deleteNodes(String nodesToDeleteCondition);
    
    public String getNodesToDeleteCondition();
    
    public void setNodesToDeleteCondition(String nodesToDeleteCondition);
    
    public void saveOrUpdateMindmapNode(MindmapNode mindmapNode);
    
    public List getAuthorRootNodeByMindmapId(Long mindmapId);
    
    public List getAuthorRootNodeByMindmapIdMulti(Long mindmapId);
    
    public List getRootNodeByMindmapIdAndUserId(Long mindmapId, Long userId);
    
    public List getUserRootNodeByUserId(Long userId);
    
    public List getMindmapNodeByParentId(Long parentId, Long mindmapId);
    
    public List getMindmapNodeByUniqueId(Long uniqueId, Long mindmapId);
    
    public List getMindmapNodeByUniqueIdMindmapIdUserId(Long uniqueId, Long mindmapId, Long userId);
    
    public MindmapNode saveMindmapNode(MindmapNode currentMindmapNode, MindmapNode parentMindmapNode, Long uniqueId, 
	    String text, String color, MindmapUser mindmapUser, Mindmap mindmap);
    
    public NodeModel getMindmapXMLFromDatabase(Long rootNodeId, Long mindmapId, NodeModel rootNodeModel, MindmapUser mindmapUser);
    
    public void getChildMindmapNodes(List<NodeModel> branches, MindmapNode rootMindmapNode, MindmapUser mindmapUser, 
	    Mindmap mindmap);
    
    public void saveOrUpdateMindmapRequest(MindmapRequest mindmapRequest);
    
    public Long getLastUniqueIdByMindmapIdUserId(Long mindmapId, Long userId);
    
    public Long getLastUniqueIdByMindmapId(Long mindmapId);
    
    public List getLastRequestsAfterGlobalId(Long globalId, Long mindmapId, Long userId);
    
    public MindmapRequest getRequestByUniqueId(Long uniqueId, Long userId, Long mindmapId, Long globalId);
    
    public Long getLastGlobalIdByMindmapId(Long mindmapId);
}
