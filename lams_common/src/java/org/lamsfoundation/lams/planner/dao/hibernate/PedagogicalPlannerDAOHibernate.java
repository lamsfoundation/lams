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
package org.lamsfoundation.lams.planner.dao.hibernate;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.lamsfoundation.lams.planner.PedagogicalPlannerSequenceNode;
import org.lamsfoundation.lams.planner.dao.PedagogicalPlannerDAO;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * 
 * @author Marcin Cieslak
 * 
 * @version $Revision$
 */
public class PedagogicalPlannerDAOHibernate extends HibernateDaoSupport implements PedagogicalPlannerDAO {
    private static final String FIND_ROOT_NODE = "FROM " + PedagogicalPlannerSequenceNode.class.getName()
	    + " AS n WHERE n.parent=NULL";
    private static final String FIND_PARENT_TITLE = "SELECT n.parent.uid, n.title FROM "
	    + PedagogicalPlannerSequenceNode.class.getName() + " AS n WHERE n.uid=?";

    private static final String FIND_PARENT_DIR = "SELECT n.parent.uid, n.subdir FROM "
	    + PedagogicalPlannerSequenceNode.class.getName() + " AS n WHERE n.uid=?";

    private static final String FIND_MAX_SUBDIR = "SELECT MAX(n.subdir) FROM "
	    + PedagogicalPlannerSequenceNode.class.getName() + " AS n WHERE n.parent.uid=?";

    public PedagogicalPlannerSequenceNode getByUid(Long uid) {
	return (PedagogicalPlannerSequenceNode) getHibernateTemplate().get(PedagogicalPlannerSequenceNode.class, uid);
    }

    public String getFilePath(PedagogicalPlannerSequenceNode node) {
	if (node.getParent() == null) {
	    return null;
	}
	Long currentUid = node.getUid();
	LinkedList<Integer> filePathParts = new LinkedList<Integer>();
	List<Object[]> result;
	Object[] row;
	while (currentUid != null) {
	    result = getHibernateTemplate().find(PedagogicalPlannerDAOHibernate.FIND_PARENT_DIR, currentUid);
	    if (result.size() > 0) {
		row = result.get(0);
		Integer subdir = (Integer) row[1];
		filePathParts.addFirst(subdir);
		currentUid = (Long) row[0];
	    } else {
		return null;
	    }
	}
	StringBuilder filePath = new StringBuilder();
	for (Integer dir : filePathParts) {
	    filePath.append(dir.toString() + File.separator);
	}
	filePath.append(node.getFileName());
	return filePath.toString();
    }

    public PedagogicalPlannerSequenceNode getRootNode() {
	List<PedagogicalPlannerSequenceNode> subnodeList = getHibernateTemplate().find(
		PedagogicalPlannerDAOHibernate.FIND_ROOT_NODE);
	PedagogicalPlannerSequenceNode rootNode = new PedagogicalPlannerSequenceNode();
	rootNode.setLocked(true);
	Set<PedagogicalPlannerSequenceNode> subnodeSet = new LinkedHashSet<PedagogicalPlannerSequenceNode>(subnodeList);
	rootNode.setSubnodes(subnodeSet);
	return rootNode;
    }

    public List<String[]> getTitlePath(PedagogicalPlannerSequenceNode node) {
	if (node.getParent() == null) {
	    return null;
	}
	Long currentUid = node.getUid();
	LinkedList<String[]> titlePath = new LinkedList<String[]>();
	List<Object[]> result;
	Object[] row;
	while (currentUid != null) {
	    result = getHibernateTemplate().find(PedagogicalPlannerDAOHibernate.FIND_PARENT_TITLE, currentUid);
	    if (result.size() > 0) {
		row = result.get(0);
		if (!currentUid.equals(node.getUid())) {
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

    public void removeNode(PedagogicalPlannerSequenceNode node) {
	getHibernateTemplate().delete(node);
	getHibernateTemplate().flush();
    }

    public void saveOrUpdateNode(PedagogicalPlannerSequenceNode node) {
	getHibernateTemplate().saveOrUpdate(node);
    }

    public Integer getNextSubdir(Long parentUid) {
	Integer maxSubdir = (Integer) getHibernateTemplate().find(PedagogicalPlannerDAOHibernate.FIND_MAX_SUBDIR,
		parentUid).get(0);
	if (maxSubdir == null) {
	    maxSubdir = 0;
	}
	return maxSubdir + 1;
    }
}