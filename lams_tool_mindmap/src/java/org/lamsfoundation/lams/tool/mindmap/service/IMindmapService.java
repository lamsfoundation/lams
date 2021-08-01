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

package org.lamsfoundation.lams.tool.mindmap.service;

import java.io.IOException;
import java.util.List;

import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.tool.mindmap.model.Mindmap;
import org.lamsfoundation.lams.tool.mindmap.model.MindmapNode;
import org.lamsfoundation.lams.tool.mindmap.model.MindmapRequest;
import org.lamsfoundation.lams.tool.mindmap.model.MindmapSession;
import org.lamsfoundation.lams.tool.mindmap.model.MindmapUser;
import org.lamsfoundation.lams.tool.mindmap.util.xmlmodel.NodeModel;
import org.lamsfoundation.lams.tool.service.ICommonToolService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.MessageService;

import com.thoughtworks.xstream.XStream;

/**
 * Defines the services available to the web layer from the Mindmap Service
 */
public interface IMindmapService extends ICommonToolService {

    Mindmap copyDefaultContent(Long newContentID);

    Mindmap getDefaultContent();

    Long getDefaultContentIdBySignature(String toolSignature);

    Mindmap getMindmapByContentId(Long toolContentID);

    Mindmap getMindmapByUid(Long Uid);

    void saveOrUpdateMindmap(Mindmap mindmap);

    MindmapSession getSessionBySessionId(Long toolSessionId);

    void saveOrUpdateMindmapSession(MindmapSession mindmapSession);

    MindmapUser getUserByUserIdAndSessionId(Long userId, Long toolSessionId);

    MindmapUser getUserByLoginAndSessionId(String login, long toolSessionId);

    MindmapUser getUserByUID(Long uid);

    void saveOrUpdateMindmapUser(MindmapUser mindmapUser);

    MindmapUser createMindmapUser(UserDTO user, MindmapSession mindmapSession);

    Long createNotebookEntry(Long id, Integer idType, String signature, Integer userID, String entry);

    NotebookEntry getEntry(Long uid);

    void updateEntry(Long uid, String entry);

    void updateEntry(NotebookEntry notebookEntry);

    void setMindmapMessageService(MessageService mindmapMessageService);

    MessageService getMindmapMessageService();

    void deleteNodeByUniqueMindmapUser(Long uniqueId, Long mindmapId, Long userId, Long sessionId);

    void deleteNodes(String nodesToDeleteCondition);

    String getNodesToDeleteCondition();

    void setNodesToDeleteCondition(String nodesToDeleteCondition);

    void saveOrUpdateMindmapNode(MindmapNode mindmapNode);

    List getAuthorRootNodeByMindmapId(Long mindmapId);

    List getAuthorRootNodeBySessionId(Long sessionId);

    List getAuthorRootNodeByMindmapSession(Long mindmapId, Long toolSessionId);

    List getRootNodeByMindmapIdAndUserId(Long mindmapId, Long userId);

    List getRootNodeByMindmapIdAndSessionId(Long mindmapId, Long sessionId);

    List getMindmapNodeByParentId(Long parentId, Long mindmapId);

    List getMindmapNodeByParentIdMindmapIdSessionId(Long parentId, Long mindmapId, Long sessionId);

    List getMindmapNodeByUniqueId(Long uniqueId, Long mindmapId);

    MindmapNode getMindmapNodeByUniqueIdSessionId(Long uniqueId, Long mindmapId, Long sessionId);

    List getMindmapNodeByUniqueIdMindmapIdUserId(Long uniqueId, Long mindmapId, Long userId);

    MindmapNode saveMindmapNode(MindmapNode currentMindmapNode, MindmapNode parentMindmapNode, Long uniqueId,
	    String text, String color, MindmapUser mindmapUser, Mindmap mindmap, MindmapSession session);

    NodeModel getMindmapXMLFromDatabase(Long rootNodeId, Long mindmapId, NodeModel rootNodeModel,
	    MindmapUser mindmapUser, boolean isMonitor, boolean isAuthor, boolean isUserLocked);

    void getChildMindmapNodes(List<NodeModel> branches, MindmapNode rootMindmapNode, MindmapUser mindmapUser,
	    Mindmap mindmap, MindmapSession mindmapSession);

    void saveOrUpdateMindmapRequest(MindmapRequest mindmapRequest);

    Long getNodeLastUniqueIdByMindmapUidSessionId(Long mindmapUid, Long sessionId);

    List<MindmapRequest> getLastRequestsAfterGlobalId(Long globalId, Long mindmapId, Long sessionId);

    MindmapRequest getRequestByUniqueId(Long uniqueId, Long userId, Long mindmapId, Long globalId);

    Long getLastGlobalIdByMindmapId(Long mindmapId, Long sessionId);

    // Outputs
    int getNumNodes(Long learnerId, Long toolSessionId);

    XStream getXStream();

    void createGalleryWalkRatingCriterion(long toolContentId);

    void startGalleryWalk(long toolContentId) throws IOException;

    void finishGalleryWalk(long toolContentId) throws IOException;

    void enableGalleryWalkLearnerEdit(long toolContentId) throws IOException;
}