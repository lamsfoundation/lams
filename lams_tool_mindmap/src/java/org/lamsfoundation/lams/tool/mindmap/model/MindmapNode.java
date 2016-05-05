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
package org.lamsfoundation.lams.tool.mindmap.model;

import org.apache.log4j.Logger;

/**
 * @author Ruslan Kazakov
 *
 */
public class MindmapNode implements Cloneable {

    private static Logger log = Logger.getLogger(MindmapNode.class);

    private Long nodeId;
    private Long uniqueId;
    private MindmapNode parent;
    private String text;
    private String color;
    private MindmapSession session;
    private MindmapUser user;
    private Mindmap mindmap;

    /** default constructor */
    public MindmapNode() {
    }

    /** full constructor */
    public MindmapNode(Long nodeId, Long uniqueId, MindmapNode parent, String text, String color, MindmapUser user,
	    Mindmap mindmap) {
	this.nodeId = nodeId;
	this.uniqueId = uniqueId;
	this.parent = parent;
	this.text = text;
	this.color = color;
	this.user = user;
	this.mindmap = mindmap;
    }

    //  **********************************************************
    //		get/set methods
    //  **********************************************************

    /**
     *
     */
    public Long getNodeId() {
	return nodeId;
    }

    public void setNodeId(Long nodeId) {
	this.nodeId = nodeId;
    }

    /**
     * @return Returns the subject of the Message.
     *
     */
    public Long getUniqueId() {
	return uniqueId;
    }

    /**
     * @param subject
     *            The subject of the Message to be set.
     */
    public void setUniqueId(Long uniqueId) {
	this.uniqueId = uniqueId;
    }

    /**
     *
     * @return
     */
    public MindmapNode getParent() {
	return parent;
    }

    /**
     * @param parent
     *            The parent of this MindmapNode
     */
    public void setParent(MindmapNode parent) {
	this.parent = parent;
    }

    /**
     * @return Returns the subject of the Message.
     *
     */
    public String getText() {
	return text;
    }

    /**
     * @param subject
     *            The subject of the Message to be set.
     */
    public void setText(String text) {
	this.text = text;
    }

    /**
     * @return Returns the subject of the Message.
     *
     */
    public String getColor() {
	return color;
    }

    /**
     * @param subject
     *            The subject of the Message to be set.
     */
    public void setColor(String color) {
	this.color = color;
    }

    /**
     * @return Returns the sessionid of the session in Mindmap.
     *
     */
    public MindmapSession getSession() {
	return session;
    }

    /**
     * @param session
     *            the session to set
     */
    public void setSession(MindmapSession session) {
	this.session = session;
    }

    /**
     * @return Returns the userid of the user who created the Mindmap.
     *
     */
    public MindmapUser getUser() {
	return user;
    }

    /**
     * @param createdBy
     *            The userid of the user who created this Mindmap.
     */
    public void setUser(MindmapUser user) {
	this.user = user;
    }

    /**
     * @return Returns the userid of the user who created the Mindmap.
     *
     */
    public Mindmap getMindmap() {
	return mindmap;
    }

    public void setMindmap(Mindmap mindmap) {
	this.mindmap = mindmap;
    }
}