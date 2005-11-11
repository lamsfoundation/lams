/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
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
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.mc;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Ozgur Demirtas */
public class McSession implements Serializable {

	public final static String INCOMPLETE = "INCOMPLETE";
    
    public static final String COMPLETED = "COMPLETED";
    
    /** identifier field */
    private Long uid;

    /** persistent field */
    private Long mcSessionId;

    /** nullable persistent field */
    private Date sessionStartDate;

    /** nullable persistent field */
    private Date sessionEndDate;

    /** nullable persistent field */
    private String sessionStatus;

    /** nullable persistent field */
    private Long mcContentId;

    /** nullable persistent field */
    private org.lamsfoundation.lams.tool.mc.McContent mcContent;

    /** persistent field */
    private Set mcQueUsers;

    /** full constructor */
    public McSession(Long mcSessionId, Date sessionStartDate, Date sessionEndDate, String sessionStatus, org.lamsfoundation.lams.tool.mc.McContent mcContent, Set mcQueUsers) {
        this.mcSessionId = mcSessionId;
        this.sessionStartDate = sessionStartDate;
        this.sessionEndDate = sessionEndDate;
        this.sessionStatus = sessionStatus;
        this.mcContent = mcContent;
        this.mcQueUsers = mcQueUsers;
    }
    
    public McSession(Long mcSessionId, Date sessionStartDate, String sessionStatus, org.lamsfoundation.lams.tool.mc.McContent mcContent, Set mcQueUsers) {
        this.mcSessionId = mcSessionId;
        this.sessionStartDate = sessionStartDate;
        this.sessionStatus = sessionStatus;
        this.mcContent = mcContent;
        this.mcQueUsers = mcQueUsers;
    }
    
    
    /** default constructor */
    public McSession() {
    }

    /** minimal constructor */
    public McSession(Long mcSessionId, Set mcQueUsers) {
        this.mcSessionId = mcSessionId;
        this.mcQueUsers = mcQueUsers;
    }

    public Long getUid() {
        return this.uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getMcSessionId() {
        return this.mcSessionId;
    }

    public void setMcSessionId(Long mcSessionId) {
        this.mcSessionId = mcSessionId;
    }

    
    public Long getMcContentId() {
        return this.mcContentId;
    }

    public void setMcContentId(Long mcContentId) {
        this.mcContentId = mcContentId;
    }

    public org.lamsfoundation.lams.tool.mc.McContent getMcContent() {
        return this.mcContent;
    }

    public void setMcContent(org.lamsfoundation.lams.tool.mc.McContent mcContent) {
        this.mcContent = mcContent;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("uid", getUid())
            .toString();
    }

	/**
	 * @return Returns the sessionEndDate.
	 */
	public Date getSessionEndDate() {
		return sessionEndDate;
	}
	/**
	 * @param sessionEndDate The sessionEndDate to set.
	 */
	public void setSessionEndDate(Date sessionEndDate) {
		this.sessionEndDate = sessionEndDate;
	}
	/**
	 * @return Returns the sessionStartDate.
	 */
	public Date getSessionStartDate() {
		return sessionStartDate;
	}
	/**
	 * @param sessionStartDate The sessionStartDate to set.
	 */
	public void setSessionStartDate(Date sessionStartDate) {
		this.sessionStartDate = sessionStartDate;
	}
	/**
	 * @return Returns the sessionStatus.
	 */
	public String getSessionStatus() {
		return sessionStatus;
	}
	/**
	 * @param sessionStatus The sessionStatus to set.
	 */
	public void setSessionStatus(String sessionStatus) {
		this.sessionStatus = sessionStatus;
	}
	/**
	 * @return Returns the mcQueUsers.
	 */
	
	public Set getMcQueUsers() {
		if (this.mcQueUsers == null)
			setMcQueUsers(new HashSet());
	    return this.mcQueUsers;
	}
	/**
	 * @param mcQueUsers The mcQueUsers to set.
	 */
	public void setMcQueUsers(Set mcQueUsers) {
		this.mcQueUsers = mcQueUsers;
	}
}
