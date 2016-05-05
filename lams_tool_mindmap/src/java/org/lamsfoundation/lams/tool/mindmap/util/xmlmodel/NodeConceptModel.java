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
 * XML Model Class for Mindmap Node Concept. Class includes data:
 * Node ID, Text, Color, Creator(author of the node), and Edit(permitions to edit).
 *
 * @author Ruslan Kazakov
 */
public class NodeConceptModel {
    private Long id; // Node ID
    private String text; // Node Text
    private String color; // Node color in Hex Format (i.e. ffffff)
    private String creator; // Author of the Node
    private int edit; // Permitions to edit node. Can be either 1 (editable) or 0 (not editable)

    /** Default Constructor */
    public NodeConceptModel() {
    }

    /** Constructor without edit tag (for single-user mode) */
    public NodeConceptModel(Long id, String text, String color, String creator) {
	this.setId(id);
	this.setText(text);
	this.setColor(color);
	this.setCreator(creator);
    }

    /** Full Constructor */
    public NodeConceptModel(Long id, String text, String color, String creator, int edit) {
	this.setId(id);
	this.setText(text);
	this.setColor(color);
	this.setCreator(creator);
	this.setEdit(edit);
    }

    /**
     * Sets the id of the Node
     * 
     * @param id
     */
    public void setId(Long id) {
	this.id = id;
    }

    /**
     * Returns the id of the Node
     * 
     * @return id
     */
    public Long getId() {
	return id;
    }

    /**
     * Sets the text of the Node
     * 
     * @param text
     */
    public void setText(String text) {
	this.text = text;
    }

    /**
     * Returns the text of the Node
     * 
     * @return text
     */
    public String getText() {
	return text;
    }

    /**
     * Sets the color of the Node
     * 
     * @param color
     */
    public void setColor(String color) {
	this.color = color;
    }

    /**
     * Returns the color of the Node
     * 
     * @return color
     */
    public String getColor() {
	return color;
    }

    /**
     * Sets the author of the Node
     * 
     * @param creator
     */
    public void setCreator(String creator) {
	this.creator = creator;
    }

    /**
     * Returns the author of the Node
     * 
     * @return creator
     */
    public String getCreator() {
	return creator;
    }

    /**
     * Sets the state for the Node
     * 
     * @param edit
     */
    public void setEdit(int edit) {
	this.edit = edit;
    }

    /**
     * Returns the state of the Node
     * 
     * @return edit
     */
    public int isEdit() {
	return edit;
    }
}
