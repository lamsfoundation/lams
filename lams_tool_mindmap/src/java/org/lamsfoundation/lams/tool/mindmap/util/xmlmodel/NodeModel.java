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

package org.lamsfoundation.lams.tool.mindmap.util.xmlmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * XML Model Class for Mindmap Node
 *
 * @author Ruslan Kazakov
 */
public class NodeModel {
    private NodeConceptModel concept; // Concept data
    private List<NodeModel> nodes = new ArrayList<NodeModel>(); // list of self-linked nodes

    /** Constructor */
    public NodeModel(NodeConceptModel nodeConceptModel) {
	this.setConcept(nodeConceptModel);
    }

    public NodeModel() {

    }

    /**
     * Sets Concept for Mindmap Node
     *
     * @param concept
     */
    public void setConcept(NodeConceptModel concept) {
	this.concept = concept;
    }

    /**
     * Returns Mindmap Concept
     *
     * @return concept
     */
    public NodeConceptModel getConcept() {
	return concept;
    }

    /**
     * Returns all children of the Node
     *
     * @return nodes
     */
    public List<NodeModel> getBranch() {
	return nodes;
    }

    /**
     * Adds Node as a Child
     *
     * @param nodeModel
     */
    public void addNode(NodeModel nodeModel) {
	nodes.add(nodeModel);
    }
}
