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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.tool.rsrc.model;

import java.util.Date;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * Resource
 * @author Dapeng Ni
 *
 * @hibernate.class  table="tl_larsrc11_session"
 *
 */
public class ResourceSession{
	
	private static Logger log = Logger.getLogger(ResourceSession.class);
	
	private Long uid;
	private Long sessionId;
	private String sessionName;
	private Resource resource;
	private Date sessionStartDate;
	private Date sessionEndDate;
	//finish or not
	private int status;
	//resource Items
	private Set resourceItems;

  	
//  **********************************************************
  	//		Get/Set methods
//  **********************************************************
	/**
	 * @hibernate.id generator-class="identity" type="java.lang.Long" column="uid"
	 * @return Returns the learnerID.
	 */
	public Long getUid() {
		return uid;
	}
	public void setUid(Long uuid) {
		this.uid = uuid;
	}
	
	/**
	 * @hibernate.property column="session_end_date"
	 * @return
	 */
	public Date getSessionEndDate() {
		return sessionEndDate;
	}
	public void setSessionEndDate(Date sessionEndDate) {
		this.sessionEndDate = sessionEndDate;
	}
	/**
	 * @hibernate.property column="session_start_date"
	 * 
	 * @return
	 */
	public Date getSessionStartDate() {
		return sessionStartDate;
	}
	public void setSessionStartDate(Date sessionStartDate) {
		this.sessionStartDate = sessionStartDate;
	}
	/**
	 * @hibernate.property
	 * @return
	 */
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

	/**
 	 * @hibernate.many-to-one  column="resource_uid"
 	 * cascade="none"
	 * @return
	 */
	public Resource getResource() {
		return resource;
	}
	public void setResource(Resource resource) {
		this.resource = resource;
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
	 * @hibernate.property column="session_name" length="250"
	 * @return Returns the session name
	 */
	public String getSessionName() {
		return sessionName;
	}

	/**
	 * 
	 * @param sessionName The session name to set.
	 */
	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}

	/**
	 * 
	 * 
	 * @hibernate.set lazy="true"
	 *                inverse="false"
	 *                cascade="all"
	 *                order-by="create_date desc"
	 * @hibernate.collection-key column="session_uid"
	 * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.tool.rsrc.model.ResourceItem"
	 * 
	 * @return
	 */
	public Set getResourceItems() {
		return resourceItems;
	}
	public void setResourceItems(Set resourceItems) {
		this.resourceItems= resourceItems;
	}


}
