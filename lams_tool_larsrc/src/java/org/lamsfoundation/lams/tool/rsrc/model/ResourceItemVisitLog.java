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

package org.lamsfoundation.lams.tool.rsrc.model;

import java.util.Date;

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
 * Resource item visit log
 *
 * @author Dapeng Ni
 */
@Entity
@Table(name = "tl_larsrc11_item_log")
public class ResourceItemVisitLog {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uid")
    private ResourceUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resource_item_uid")
    private ResourceItem resourceItem;

    @Column
    private boolean complete;

    @Column(name = "complete_date")
    private Date completeDate;

    @Column(name = "access_date")
    private Date accessDate;

    @Column(name = "session_id")
    private Long sessionId;

    public Date getAccessDate() {
	return accessDate;
    }

    public void setAccessDate(Date accessDate) {
	this.accessDate = accessDate;
    }

    public Date getCompleteDate() {
	return completeDate;
    }

    public void setCompleteDate(Date completeDate) {
	this.completeDate = completeDate;
    }

    public ResourceItem getResourceItem() {
	return resourceItem;
    }

    public void setResourceItem(ResourceItem item) {
	this.resourceItem = item;
    }

    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public ResourceUser getUser() {
	return user;
    }

    public void setUser(ResourceUser user) {
	this.user = user;
    }

    public boolean isComplete() {
	return complete;
    }

    public void setComplete(boolean complete) {
	this.complete = complete;
    }

    public Long getSessionId() {
	return sessionId;
    }

    public void setSessionId(Long sessionId) {
	this.sessionId = sessionId;
    }

}
