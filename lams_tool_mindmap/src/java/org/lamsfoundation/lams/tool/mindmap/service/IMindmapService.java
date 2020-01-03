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

    public Mindmap copyDefaultContent(Long newContentID);

    public Mindmap getDefaultContent();

    public Long getDefaultContentIdBySignature(String toolSignature);

    public Mindmap getMindmapByContentId(Long toolContentID);

    public Mindmap getMindmapByUid(Long Uid);

    public void saveOrUpdateMindmap(Mindmap mindmap);

    public MindmapSession getSessionBySessionId(Long toolSessionId);

    public void saveOrUpdateMindmapSession(MindmapSession mindmapSession);

    public MindmapUser getUserByUserIdAndSessionId(Long userId, Long toolSessionId);

    public MindmapUser getUserByLoginAndSessionId(String login, long toolSessionId);

    public MindmapUser getUserByUID(Long uid);

    public void saveOrUpdateMindmapUser(MindmapUser mindmapUser);

    public MindmapUser createMindmapUser(UserDTO user, MindmapSession mindmapSession);

    Long createNotebookEntry(Long id, Integer idType, String signature, Integer userID, String entry);

    NotebookEntry getEntry(Long uid);

    void updateEntry(Long uid, String entry);

    public void updateEntry(NotebookEntry notebookEntry);

    public void setMindmapMessageService(MessageService mindmapMessageService);

    public MessageService getMindmapMessageService();

    public void deleteNodeByUniqueMindmapUser(Long uniqueId, Long mindmapId, Long userId, Long sessionId);

    public void deleteNodes(String nodesToDeleteCondition);

    public String getNodesToDeleteCondition();

    public void setNodesToDeleteCondition(String nodesToDeleteCondition);

    public void saveOrUpdateMindmapNode(MindmapNode mindmapNode);

    public List getAuthorRootNodeByMindmapId(Long mindmapId);

    public List getAuthorRootNodeBySessionId(Long sessionId);

    public List getAuthorRootNodeByMindmapSession(Long mindmapId, Long toolSessionId);

    public List getRootNodeByMindmapIdAndUserId(Long mindmapId, Long userId);

    public List getRootNodeByMindmapIdAndSessionId(Long mindmapId, Long sessionId);

    public List getMindmapNodeByParentId(Long parentId, Long mindmapId);

    public List getMindmapNodeByParentIdMindmapIdSessionId(Long parentId, Long mindmapId, Long sessionId);

    public List getMindmapNodeByUniqueId(Long uniqueId, Long mindmapId);

    public MindmapNode getMindmapNodeByUniqueIdSessionId(Long uniqueId, Long mindmapId, Long sessionId);

    public List getMindmapNodeByUniqueIdMindmapIdUserId(Long uniqueId, Long mindmapId, Long userId);

    public MindmapNode saveMindmapNode(MindmapNode currentMindmapNode, MindmapNode parentMindmapNode, Long uniqueId,
	    String text, String color, MindmapUser mindmapUser, Mindmap mindmap, MindmapSession session);

    public NodeModel getMindmapXMLFromDatabase(Long rootNodeId, Long mindmapId, NodeModel rootNodeModel,
	    MindmapUser mindmapUser, boolean isMonitor, boolean isAuthor, boolean isUserLocked);

    public void getChildMindmapNodes(List<NodeModel> branches, MindmapNode rootMindmapNode, MindmapUser mindmapUser,
	    Mindmap mindmap, MindmapSession mindmapSession);

    public void saveOrUpdateMindmapRequest(MindmapRequest mindmapRequest);

    public Long getNodeLastUniqueIdByMindmapUidSessionId(Long mindmapUid, Long sessionId);

    public List<MindmapRequest> getLastRequestsAfterGlobalId(Long globalId, Long mindmapId, Long sessionId);

    public MindmapRequest getRequestByUniqueId(Long uniqueId, Long userId, Long mindmapId, Long globalId);

    public Long getLastGlobalIdByMindmapId(Long mindmapId, Long sessionId);

    // Outputs
    public int getNumNodes(Long learnerId, Long toolSessionId);

    public XStream getXStream();
}