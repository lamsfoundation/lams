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

/* $$Id$$ */
package org.lamsfoundation.lams.tool.mindmap.dao.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.tool.mindmap.dao.IMindmapNodeDAO;
import org.lamsfoundation.lams.tool.mindmap.model.MindmapNode;

/**
 * MindmapNodeDao
 * 
 * @author Ruslan Kazakov
 */
public class MindmapNodeDAO extends BaseDAO implements IMindmapNodeDAO {
    private static final String SQL_QUERY_FIND_ROOT_NODE_BY_MINDMAP_ID = "from " + MindmapNode.class.getName()
	    + " mn where mn.parent is null and mn.mindmap.uid = ? and mn.user is null ";

    private static final String SQL_QUERY_FIND_ROOT_NODE_BY_MINDMAP_ID_MULTI = "from " + MindmapNode.class.getName()
	    + " mn where mn.parent is null and mn.mindmap.uid = ? ";

    private static final String SQL_QUERY_FIND_ROOT_NODE_BY_MINDMAP_ID_USER_ID = "from " + MindmapNode.class.getName()
	    + " mn where mn.parent is null and mn.mindmap.uid = ? and mn.user.uid = ? ";

    private static final String SQL_QUERY_FIND_ROOT_NODE_BY_USER_ID = "from " + MindmapNode.class.getName()
	    + " mn where mn.parent is null and mn.user.uid = ? ";

    private static final String SQL_QUERY_FIND_NODE_BY_PARENT_ID_MINDMAP_ID = "from " + MindmapNode.class.getName()
	    + " mn where mn.parent.nodeId = ? and mn.mindmap.uid = ? ";

    private static final String SQL_QUERY_FIND_NODE_BY_UNIQUE_ID_MINDMAP_ID = "from " + MindmapNode.class.getName()
	    + " mn where mn.uniqueId = ? and mn.mindmap.uid = ? ";

    private static final String SQL_QUERY_FIND_NODE_BY_UNIQUE_MINDMAP_ID_AND_USER_ID = "from "
	    + MindmapNode.class.getName() + " mn where mn.uniqueId = ? and mn.mindmap.uid = ? and mn.user.uid = ? ";

    private static final String SQL_QUERY_FIND_UNIQUE_ID_BY_MINDMAP_ID_AND_USER_ID = " select mn.uniqueId from "
	    + MindmapNode.class.getName() + " mn where mn.mindmap.uid = ? and mn.user.uid = ? "
	    + " order by mn.uniqueId desc ";

    private static final String SQL_QUERY_FIND_UNIQUE_ID_BY_MINDMAP_ID = " select mn.uniqueId from "
	    + MindmapNode.class.getName() + " mn where mn.mindmap.uid = ? " + " order by mn.uniqueId desc ";

    private static final String SQL_QUERY_NODES_NUMBER_BY_USER_SESSION = " select count(*) from "
	    + MindmapNode.class.getName() + " mn where mn.user.uid = ? ";

    public void deleteNodeByUniqueMindmapUser(Long uniqueId, Long mindmapId, Long userId) {
	Session session = getSessionFactory().getCurrentSession();
	String hql = "delete from " + MindmapNode.class.getName() + " where unique_id = " + uniqueId
		+ " and mindmap_id = " + mindmapId + " and user_id = " + userId;
	Query query = session.createQuery(hql);
	query.executeUpdate();
    }

    public void deleteNodes(String deletedNodesQuery) {
	Session session = getSessionFactory().getCurrentSession();
	String hql = "delete from " + MindmapNode.class.getName() + deletedNodesQuery;
	Query query = session.createQuery(hql);
	query.executeUpdate();
    }

    public void saveOrUpdate(MindmapNode mindmapNode) {
	this.getHibernateTemplate().saveOrUpdate(mindmapNode);
    }

    public List getAuthorRootNodeByMindmapId(Long mindmapId) {
	return this.getHibernateTemplate().find(SQL_QUERY_FIND_ROOT_NODE_BY_MINDMAP_ID, mindmapId);
    }

    public List getAuthorRootNodeByMindmapIdMulti(Long mindmapId) {
	return this.getHibernateTemplate().find(SQL_QUERY_FIND_ROOT_NODE_BY_MINDMAP_ID_MULTI, mindmapId);
    }

    public List getRootNodeByMindmapIdAndUserId(Long mindmapId, Long userId) {
	return this.getHibernateTemplate().find(SQL_QUERY_FIND_ROOT_NODE_BY_MINDMAP_ID_USER_ID,
		new Object[] { mindmapId, userId });
    }

    public List getUserRootNodeByUserId(Long userId) {
	return this.getHibernateTemplate().find(SQL_QUERY_FIND_ROOT_NODE_BY_USER_ID, userId);
    }

    public List getMindmapNodeByParentId(Long parentId, Long mindmapId) {
	return this.getHibernateTemplate().find(SQL_QUERY_FIND_NODE_BY_PARENT_ID_MINDMAP_ID,
		new Object[] { parentId, mindmapId });
    }

    public List getMindmapNodeByUniqueId(Long uniqueId, Long mindmapId) {
	return this.getHibernateTemplate().find(SQL_QUERY_FIND_NODE_BY_UNIQUE_ID_MINDMAP_ID,
		new Object[] { uniqueId, mindmapId });
    }

    public List getMindmapNodeByUniqueIdMindmapIdUserId(Long uniqueId, Long mindmapId, Long userId) {
	return this.getHibernateTemplate().find(SQL_QUERY_FIND_NODE_BY_UNIQUE_MINDMAP_ID_AND_USER_ID,
		new Object[] { uniqueId, mindmapId, userId });
    }

    public Long getLastUniqueIdByMindmapIdUserId(Long mindmapId, Long userId) {
	List list = this.getHibernateTemplate().find(SQL_QUERY_FIND_UNIQUE_ID_BY_MINDMAP_ID_AND_USER_ID,
		new Object[] { mindmapId, userId });
	if (list != null && list.size() > 0)
	    return ((Number) list.get(0)).longValue();
	else
	    return null;
    }

    public Long getLastUniqueIdByMindmapId(Long mindmapId) {
	List list = this.getHibernateTemplate().find(SQL_QUERY_FIND_UNIQUE_ID_BY_MINDMAP_ID, mindmapId);
	if (list != null && list.size() > 0)
	    return ((Number) list.get(0)).longValue();
	else
	    return null;
    }
    
    /** Outputs */
    public int getNumNodesByUserAndSession(Long userId, Long sessionId) {
	List list = this.getHibernateTemplate().find(SQL_QUERY_NODES_NUMBER_BY_USER_SESSION, new Object[] { userId });
	if (list != null && list.size() > 0)
	    return ((Number) list.get(0)).intValue();
	else
	    return 0;
    }
}
