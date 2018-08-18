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


package org.lamsfoundation.lams.tool.mindmap.model;

/**
 * @author Ruslan Kazakov
 *
 *
 *
 */
public class MindmapRequest implements Cloneable {

    private Long uid;
    private Long uniqueId;
    private Long globalId;
    private int type;
    private Long nodeId;
    private Long nodeChildId;
    private MindmapUser user;
    private Mindmap mindmap;

    /** default constructor */
    public MindmapRequest() {
    }

    /** full constructor */
    public MindmapRequest(Long uid, Long uniqueId, int type, Long nodeId, MindmapUser user, Mindmap mindmap) {
	this.uid = uid;
	this.uniqueId = uniqueId;
	this.type = type;
	this.nodeId = nodeId;
	this.user = user;
	this.mindmap = mindmap;
    }

    //  **********************************************************
    //		get/set methods
    //  **********************************************************

    /**
     *
     */
    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
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
     * @return Returns the subject of the Message.
     *
     */
    public Long getGlobalId() {
	return globalId;
    }

    /**
     * @param globalId
     *            the globalId to set
     */
    public void setGlobalId(Long globalId) {
	this.globalId = globalId;
    }

    /**
     * @return Returns the subject of the Message.
     *
     */
    public int getType() {
	return type;
    }

    /**
     * @param subject
     *            The subject of the Message to be set.
     */
    public void setType(int type) {
	this.type = type;
    }

    /**
     * @return Returns the userid of the user who created the Mindmap.
     *
     */
    public Long getNodeId() {
	return nodeId;
    }

    /**
     * @param createdBy
     *            The userid of the user who created this Mindmap.
     */
    public void setNodeId(Long nodeId) {
	this.nodeId = nodeId;
    }

    /**
     * @return Returns the userid of the user who created the Mindmap.
     *
     */
    public Long getNodeChildId() {
	return nodeChildId;
    }

    public void setNodeChildId(Long nodeChildId) {
	this.nodeChildId = nodeChildId;
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
    
    
    @Override
    public String toString() {
	return "MindmapRequest [uid=" + uid + ", uniqueId=" + uniqueId + ", globalId=" + globalId + ", type=" + type
		+ ", nodeId=" + nodeId + ", nodeChildId=" + nodeChildId + ", user=" + user + ", mindmap=" + mindmap
		+ "]";
    }
}