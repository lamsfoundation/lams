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

package org.lamsfoundation.lams.planner.dao.hibernate;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.planner.PedagogicalPlannerNodeRole;
import org.lamsfoundation.lams.planner.PedagogicalPlannerSequenceNode;
import org.lamsfoundation.lams.planner.dao.PedagogicalPlannerDAO;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Marcin Cieslak
 *
 * @version $Revision$
 */
@Repository
public class PedagogicalPlannerDAOHibernate extends LAMSBaseDAO implements PedagogicalPlannerDAO {
    private static final String FIND_ROOT_NODE = "FROM " + PedagogicalPlannerSequenceNode.class.getName()
	    + " AS n WHERE n.parent=NULL";
    private static final String FIND_PARENT_TITLE = "SELECT n.parent.uid, n.title FROM "
	    + PedagogicalPlannerSequenceNode.class.getName() + " AS n WHERE n.uid=?";
    private static final String FIND_MAX_ORDER_ID = "SELECT MAX(n.order) FROM "
	    + PedagogicalPlannerSequenceNode.class.getName() + " AS n WHERE n.parent.uid=?";
    private static final String FIND_NEIGHBOUR_NODE_ASC = "FROM " + PedagogicalPlannerSequenceNode.class.getName()
	    + " AS n WHERE ((? IS NULL AND n.parent=NULL) OR  n.parent.uid=?) AND n.order>=?";
    private static final String FIND_NEIGHBOUR_NODE_DESC = "FROM " + PedagogicalPlannerSequenceNode.class.getName()
	    + " AS n WHERE ((? IS NULL AND n.parent=NULL) OR  n.parent.uid=?) AND n.order<=? ORDER BY n.order DESC";
    private static final String GET_PLANNER_NODE_ROLE = "FROM " + PedagogicalPlannerNodeRole.class.getName()
	    + " WHERE user.userId=? AND node.uid=? AND role.roleId=?";
    // TODO include inherited users
    private static final String GET_PLANNER_NODE_ROLE_USERS = "SELECT p.user FROM "
	    + PedagogicalPlannerNodeRole.class.getName() + " AS p WHERE p.node.uid=? AND p.role.roleId=?";

    /*
     * private static final String FIND_RECENTLY_MODIFIED_LD = "SELECT ld FROM " + LearningDesign.class.getName() + " AS
     * ld, lams_planner_recent_learning_designs AS recent WHERE recent.user_id=? AND
     * ld.learningDesignId=recent.learning_design_id ORDER BY recent.last_modified_date DESC LIMIT ?";
     */

    @Override
    public PedagogicalPlannerSequenceNode getByUid(Long uid) {
	return (PedagogicalPlannerSequenceNode) getSession().get(PedagogicalPlannerSequenceNode.class, uid);
    }

    @Override
    @SuppressWarnings("unchecked")
    public PedagogicalPlannerSequenceNode getRootNode() {
	List<PedagogicalPlannerSequenceNode> subnodeList = (List<PedagogicalPlannerSequenceNode>) doFind(
		PedagogicalPlannerDAOHibernate.FIND_ROOT_NODE);
	PedagogicalPlannerSequenceNode rootNode = new PedagogicalPlannerSequenceNode();
	rootNode.setLocked(true);
	Set<PedagogicalPlannerSequenceNode> subnodeSet = new LinkedHashSet<PedagogicalPlannerSequenceNode>(subnodeList);
	rootNode.setSubnodes(subnodeSet);
	return rootNode;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String[]> getTitlePath(Long nodeUid) {
	Long currentUid = nodeUid;
	LinkedList<String[]> titlePath = new LinkedList<String[]>();
	List<Object[]> result;
	Object[] row;
	while (currentUid != null) {
	    result = (List<Object[]>) doFind(PedagogicalPlannerDAOHibernate.FIND_PARENT_TITLE, currentUid);
	    if (result.size() > 0) {
		row = result.get(0);
		if (!currentUid.equals(nodeUid)) {
		    String title = (String) row[1];
		    titlePath.addFirst(new String[] { currentUid.toString(), title });
		}
		currentUid = (Long) row[0];
	    } else {
		return null;
	    }
	}

	return titlePath;
    }

    @Override
    public void removeNode(PedagogicalPlannerSequenceNode node) {
	getSession().delete(node);
	getSession().flush();
    }

    @Override
    public void saveOrUpdateNode(PedagogicalPlannerSequenceNode node) {
	getSession().saveOrUpdate(node);
	getSession().flush();
    }

    @Override
    public Integer getNextOrderId(Long parentUid) {
	Integer maxOrderId = (Integer) doFind(PedagogicalPlannerDAOHibernate.FIND_MAX_ORDER_ID, parentUid).get(0);
	if (maxOrderId == null) {
	    maxOrderId = 0;
	}
	return maxOrderId + 1;
    }

    @Override
    public PedagogicalPlannerSequenceNode getNeighbourNode(PedagogicalPlannerSequenceNode node, Integer orderDelta) {
	Integer order = node.getOrder() + orderDelta;
	Long parentUid = node.getParent() == null ? null : node.getParent().getUid();
	String query = (orderDelta < 0) ? PedagogicalPlannerDAOHibernate.FIND_NEIGHBOUR_NODE_DESC
		: PedagogicalPlannerDAOHibernate.FIND_NEIGHBOUR_NODE_ASC;
	return (PedagogicalPlannerSequenceNode) doFind(query, new Object[] { parentUid, parentUid, order }).get(0);
    }

    private List getPlannerNodeRoles(Integer userId, Long nodeUid, Integer roleId) {
	return doFind(PedagogicalPlannerDAOHibernate.GET_PLANNER_NODE_ROLE, new Object[] { userId, nodeUid, roleId });
    }

    @Override
    public Boolean isEditor(Integer userId, Long nodeUid, Integer roleId) {
	List l = getPlannerNodeRoles(userId, nodeUid, roleId);
	if (l != null && l.size() > 0) {
	    return true;
	} else {
	    // check parent nodes for 'inherited' role
	    if (nodeUid != null) {
		PedagogicalPlannerSequenceNode node = getByUid(nodeUid);
		if (node != null) {
		    PedagogicalPlannerSequenceNode parent = node.getParent();
		    return isEditor(userId, (parent != null ? parent.getUid() : null), roleId);
		}
	    }
	}
	return false;
    }

    @Override
    public List getNodeUsers(Long nodeUid, Integer roleId) {
	return doFind(PedagogicalPlannerDAOHibernate.GET_PLANNER_NODE_ROLE_USERS, new Object[] { nodeUid, roleId });
    }

    @Override
    public Set getInheritedNodeUsers(Long nodeUid, Integer roleId) {
	HashSet users = new HashSet(); // use set to avoid duplicates

	PedagogicalPlannerSequenceNode node = getByUid(nodeUid);
	while (node.getParent() != null) {
	    PedagogicalPlannerSequenceNode parent = node.getParent();
	    users.addAll(getNodeUsers(parent.getUid(), roleId));
	    node = parent;
	}

	return users;
    }

    @Override
    public void saveNodeRole(Integer userId, Long nodeUid, Integer roleId) {
	PedagogicalPlannerSequenceNode node = getByUid(nodeUid);
	User user = (User) getSession().get(User.class, userId);
	Role role = (Role) getSession().get(Role.class, roleId);
	PedagogicalPlannerNodeRole nodeRole = new PedagogicalPlannerNodeRole(node, user, role);
	getSession().saveOrUpdate(nodeRole);
	getSession().flush();
    }

    @Override
    public void removeNodeRole(Integer userId, Long nodeUid, Integer roleId) {
	List l = getPlannerNodeRoles(userId, nodeUid, roleId);
	for (Object o : l) {
	    getSession().delete(o);
	}
	getSession().flush();
    }
}