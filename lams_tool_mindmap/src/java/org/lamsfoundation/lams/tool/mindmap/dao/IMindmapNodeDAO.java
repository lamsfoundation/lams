/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
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


package org.lamsfoundation.lams.tool.mindmap.dao;

import java.util.List;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.tool.mindmap.model.MindmapNode;

/**
 * IMindmapNodeDao
 * 
 * @author Ruslan Kazakov
 */
public interface IMindmapNodeDAO extends IBaseDAO {

    public void deleteNodeByUniqueMindmapUser(Long uniqueId, Long mindmapId, Long userId, Long sessionId);

    public void deleteNodes(String deletedNodesQuery);

    public void saveOrUpdate(MindmapNode mindmapNode);

    public List getAuthorRootNodeByMindmapId(Long mindmapId);

    public List getAuthorRootNodeBySessionId(Long sessionId);

    public List getAuthorRootNodeByMindmapSession(Long mindmapId, Long toolSessionId);

    public List getRootNodeByMindmapIdAndUserId(Long mindmapId, Long userId);

    public List getMindmapNodeByParentId(Long parentId, Long mindmapId);

    public List getMindmapNodeByParentIdMindmapIdSessionId(Long parentId, Long mindmapId, Long sessionId);

    public List getMindmapNodeByUniqueId(Long uniqueId, Long mindmapId);

    public MindmapNode getMindmapNodeByUniqueIdSessionId(Long uniqueId, Long mindmapId, Long sessionId);

    public List getMindmapNodeByUniqueIdMindmapIdUserId(Long uniqueId, Long mindmapId, Long userId);

    public Long getNodeLastUniqueIdByMindmapUidSessionId(Long mindmapUid, Long sessionId);

    public int getNumNodesByUserAndSession(Long userId, Long sessionId);

    public List<MindmapNode> getMindmapNodesBySessionIdAndUserId(Long sessionId, Long userId);
}
