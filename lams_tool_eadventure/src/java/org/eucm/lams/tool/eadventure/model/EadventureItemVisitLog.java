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

/* $Id$ */
package org.eucm.lams.tool.eadventure.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Eadventure
 * 
 * @author Dapeng Ni
 *
 * @hibernate.class table="tl_eueadv10_item_log"
 *
 */
public class EadventureItemVisitLog {

    private Long uid;
    private EadventureUser user;
    private Eadventure eadventure;
    private boolean complete;
    private Date accessDate;
    private Long sessionId;
    private Set eadventureVars;

    public EadventureItemVisitLog() {
	eadventureVars = new HashSet();
    }

    /**
     * @hibernate.property column="access_date"
     * @return
     */
    public Date getAccessDate() {
	return accessDate;
    }

    public void setAccessDate(Date accessDate) {
	this.accessDate = accessDate;
    }

    /**
     * @hibernate.many-to-one column="eadventure_item_uid"
     *                        cascade="none"
     * @return
     */
    public Eadventure getEadventure() {
	return eadventure;
    }

    public void setEadventure(Eadventure item) {
	this.eadventure = item;
    }

    /**
     * @hibernate.id generator-class="native" type="java.lang.Long" column="uid"
     * @return Returns the log Uid.
     */
    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     * @hibernate.many-to-one column="user_uid"
     *                        cascade="none"
     * @return
     */
    public EadventureUser getUser() {
	return user;
    }

    public void setUser(EadventureUser user) {
	this.user = user;
    }

    /**
     * @hibernate.property column="complete"
     * @return
     */
    public boolean isComplete() {
	return complete;
    }

    public void setComplete(boolean complete) {
	this.complete = complete;
    }

    /**
     * @hibernate.property column="session_id"
     * @return
     */
    public Long getSessionId() {
	return sessionId;
    }

    public void setSessionId(Long sessionId) {
	this.sessionId = sessionId;
    }

    /**
     * 
     * 
     * @hibernate.set lazy="true"
     *                inverse="false"
     *                cascade="all"
     * 
     * @hibernate.collection-key column="visit_log_uid"
     * @hibernate.collection-one-to-many class="org.eucm.lams.tool.eadventure.model.EadventureVars"
     * 
     * @return
     */
    public Set getEadventureVars() {
	return eadventureVars;
    }

    public void setEadventureVars(Set eadventureVars) {
	this.eadventureVars = eadventureVars;
    }

}
