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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */


package org.lamsfoundation.lams.planner.dao;

import java.util.List;
import java.util.Set;

import org.lamsfoundation.lams.planner.PedagogicalPlannerSequenceNode;

public interface PedagogicalPlannerDAO {
    PedagogicalPlannerSequenceNode getByUid(Long uid);

    PedagogicalPlannerSequenceNode getRootNode();

    void saveOrUpdateNode(PedagogicalPlannerSequenceNode node);

    void removeNode(PedagogicalPlannerSequenceNode node);

    List<String[]> getTitlePath(Long nodeUid);

    Integer getNextOrderId(Long parentUid);

    PedagogicalPlannerSequenceNode getNeighbourNode(PedagogicalPlannerSequenceNode node, Integer orderDelta);

    Boolean isEditor(Integer userId, Long nodeUid, Integer roleId);

    List getNodeUsers(Long nodeUid, Integer roleId);

    Set getInheritedNodeUsers(Long nodeUid, Integer roleId);

    void saveNodeRole(Integer userId, Long nodeUid, Integer roleId);

    void removeNodeRole(Integer userId, Long nodeUid, Integer roleId);
}