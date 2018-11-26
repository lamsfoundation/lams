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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Ruslan Kazakov
 *
 *
 *
 */
@Entity
@Table(name = "tl_lamind10_request")
public class MindmapRequest implements Cloneable {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(name = "unique_id")
    private Long uniqueId;

    @Column(name = "global_id")
    private Long globalId;

    @Column(name = "request_type")
    private int type;

    @Column(name = "node_id")
    private Long nodeId;

    @Column(name = "node_child_id")
    private Long nodeChildId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private MindmapUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mindmap_id")
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

    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public Long getUniqueId() {
	return uniqueId;
    }

    public void setUniqueId(Long uniqueId) {
	this.uniqueId = uniqueId;
    }

    public Long getGlobalId() {
	return globalId;
    }

    public void setGlobalId(Long globalId) {
	this.globalId = globalId;
    }

    public int getType() {
	return type;
    }

    public void setType(int type) {
	this.type = type;
    }

    public Long getNodeId() {
	return nodeId;
    }

    public void setNodeId(Long nodeId) {
	this.nodeId = nodeId;
    }

    public Long getNodeChildId() {
	return nodeChildId;
    }

    public void setNodeChildId(Long nodeChildId) {
	this.nodeChildId = nodeChildId;
    }

    public MindmapUser getUser() {
	return user;
    }

    public void setUser(MindmapUser user) {
	this.user = user;
    }

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