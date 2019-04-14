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


package org.lamsfoundation.lams.contentrepository.dao.hibernate;

import java.util.List;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.CrNode;
import org.lamsfoundation.lams.contentrepository.CrNodeVersion;
import org.lamsfoundation.lams.contentrepository.dao.INodeDAO;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.springframework.stereotype.Repository;

/**
 * Data access routines for Nodes
 *
 * @author Fiona Malikoff
 */
@Repository
public class NodeDAO extends LAMSBaseDAO implements INodeDAO {
    protected Logger log = Logger.getLogger(NodeDAO.class);

    @Override
    public void saveOrUpdate(Object object) {
	insertOrUpdate(object);
    }

    /**
     * Get all child nodes for a node/version.
     *
     * The child/parent relationship set is lazy loaded to make things more efficient. But as the repository doesn't
     * keep the session open, the lazy loading can't occur! So load manually.
     *
     * @param parentNodeVersion
     * @return Set of CrNodes that are child nodes of this node/version
     */
    @Override
    public List findChildNodes(CrNodeVersion parentNodeVersion) {

	if (log.isDebugEnabled()) {
	    log.debug("Getting all child nodes for " + parentNodeVersion);
	}

	String queryString = "from CrNode as n where n.parentNodeVersion = ?";
	List nodes = doFind(queryString, parentNodeVersion);

	if (nodes.size() == 0) {
	    log.debug("No nodes found");
	    return null;
	} else {
	    if (log.isDebugEnabled()) {
		log.debug("Returning " + nodes.size() + " nodes.");
	    }
	    return nodes;
	}
    }

    /**
     * Get all child nodes for a node/version.
     *
     * The child/parent relationship set is lazy loaded to make things more efficient. But as the repository doesn't
     * keep the session open, the lazy loading can't occur! So load manually.
     *
     * @param parentNodeVersion
     * @param relPath
     *            relative path of the file, based on the main page from the parent node.
     * @return Set of CrNodes that are child nodes of this node/version
     */
    @Override
    public CrNode findChildNode(CrNodeVersion parentNodeVersion, String relPath) {

	if (log.isDebugEnabled()) {
	    log.debug("Getting child node from " + parentNodeVersion + " path " + relPath);
	}

	String queryString = "from CrNode as n where n.parentNodeVersion = ? and n.path = ? ";
	List nodes = doFind(queryString, new Object[] { parentNodeVersion, relPath });

	if (nodes.size() == 0) {
	    log.debug("No nodes found");
	    return null;
	} else {
	    if (nodes.size() == 0) {
		log.error(nodes.size() + " matches found for " + parentNodeVersion + " path " + relPath);
	    }
	    return (CrNode) nodes.get(0);
	}
    }
}