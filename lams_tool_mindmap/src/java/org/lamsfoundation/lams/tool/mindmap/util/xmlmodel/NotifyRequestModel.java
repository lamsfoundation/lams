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

/**
 * XML Model Class for Notify Request (Actions sent by Flash) in Mindmap.
 *
 * @author Ruslan Kazakov
 */
public class NotifyRequestModel {
    private Long ID; // Request ID
    private Long nodeID; // Node ID
    private int type; // 0 - delete; 1 - create node; 2 - change color; 3 - change text
    private String text; // Text (if type is 3)
    private String color; // Color (if type is 2)
    private NodeConceptModel concept; // (if type is 1)

    /** Full Constructor */
    public NotifyRequestModel(Long ID, Long nodeID, int type, String text, String color, NodeConceptModel concept) {
	this.setID(ID);
	this.setNodeID(nodeID);
	this.setType(type);
	if (text != null) {
	    this.text = text;
	}
	if (color != null) {
	    this.color = color;
	}
	this.setConcept(concept);
    }

    /**
     * Sets the id of the Request
     * 
     * @param ID
     */
    public void setID(Long ID) {
	this.ID = ID;
    }

    /**
     * Returns the if of the Request
     * 
     * @return the ID
     */
    public Long getID() {
	return ID;
    }

    /**
     * Sets the type of Request
     * 
     * @param type
     */
    public void setType(int type) {
	this.type = type;
    }

    /**
     * Returns the type of the Request
     * 
     * @return type
     */
    public int getType() {
	return type;
    }

    /**
     * Sets the id of the Node
     * 
     * @param nodeID
     */
    public void setNodeID(Long nodeID) {
	this.nodeID = nodeID;
    }

    /**
     * Returns the id of the Node
     * 
     * @return nodeID
     */
    public Long getNodeID() {
	return nodeID;
    }

    /**
     * Sets the text of the Node
     * 
     * @param text
     *            the text to set
     */
    public void setText(String text) {
	this.text = text;
    }

    /**
     * Returns text of the Node
     * 
     * @return text
     */
    public String getText() {
	return text;
    }

    /**
     * Sets Color of the Node
     * 
     * @param color
     */
    public void setColor(String color) {
	this.color = color;
    }

    /**
     * Returns Color of the Node
     * 
     * @return color
     */
    public String getColor() {
	return color;
    }

    /**
     * Sets Node Concept
     * 
     * @param concept
     */
    public void setConcept(NodeConceptModel concept) {
	this.concept = concept;
    }

    /**
     * Returns the Node Concept
     * 
     * @return the nodeConceptModel
     */
    public NodeConceptModel getConcept() {
	return concept;
    }
}
