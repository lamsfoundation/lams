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

package org.lamsfoundation.lams.tool.vote.pojos;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * <p>Persistent  object/bean that defines the content for the Voting tool.
 * Provides accessors and mutators to get/set attributes
 * It maps to database table: tl_vote11_session
 * </p>
 * 
 * @author Ozgur Demirtas
 */
public class VoteSession implements Serializable {

	public final static String INCOMPLETE = "INCOMPLETE";
    
    public static final String COMPLETED = "COMPLETED";
    
    /** identifier field */
    private Long uid;

    /** persistent field */
    private Long voteSessionId;

    /** nullable persistent field */
    private Date sessionStartDate;

    /** nullable persistent field */
    private Date sessionEndDate;

    /** nullable persistent field */
    private String sessionStatus;
    
    private String session_name;

    /** nullable persistent field */
    private Long voteContentId;

    /** nullable persistent field */
    private org.lamsfoundation.lams.tool.vote.pojos.VoteContent voteContent;

    /** persistent field */
    private Set voteQueUsers;

    /** full constructor */
    public VoteSession(Long voteSessionId, Date sessionStartDate, Date sessionEndDate, String sessionStatus, org.lamsfoundation.lams.tool.vote.pojos.VoteContent voteContent, Set voteQueUsers) {
        this.voteSessionId = voteSessionId;
        this.sessionStartDate = sessionStartDate;
        this.sessionEndDate = sessionEndDate;
        this.sessionStatus = sessionStatus;
        this.voteContent = voteContent;
        this.voteQueUsers = voteQueUsers;
    }
    
    public VoteSession(Long voteSessionId, Date sessionStartDate, String sessionStatus, String session_name, org.lamsfoundation.lams.tool.vote.pojos.VoteContent voteContent, Set voteQueUsers) {
        this.voteSessionId = voteSessionId;
        this.sessionStartDate = sessionStartDate;
        this.sessionStatus = sessionStatus;
        this.session_name = session_name;
        this.voteContent = voteContent;
        this.voteQueUsers = voteQueUsers;
    }
    
    public VoteSession(Long voteSessionId, Date sessionStartDate, String sessionStatus, org.lamsfoundation.lams.tool.vote.pojos.VoteContent voteContent, Set voteQueUsers) {
        this.voteSessionId = voteSessionId;
        this.sessionStartDate = sessionStartDate;
        this.sessionStatus = sessionStatus;
        this.voteContent = voteContent;
        this.voteQueUsers = voteQueUsers;
    }
    
    /** default constructor */
    public VoteSession() {
    }

    /** minimal constructor */
    public VoteSession(Long voteSessionId, Set voteQueUsers) {
        this.voteSessionId = voteSessionId;
        this.voteQueUsers = voteQueUsers;
    }

    public Long getUid() {
        return this.uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
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
	 * @return Returns the session_name.
	 */
	public String getSession_name() {
		return session_name;
	}
	/**
	 * @param session_name The session_name to set.
	 */
	public void setSession_name(String session_name) {
		this.session_name = session_name;
	}
    /**
     * @return Returns the voteSessionId.
     */
    public Long getVoteSessionId() {
        return voteSessionId;
    }
    /**
     * @param voteSessionId The voteSessionId to set.
     */
    public void setVoteSessionId(Long voteSessionId) {
        this.voteSessionId = voteSessionId;
    }
    /**
     * @return Returns the voteContentId.
     */
    public Long getVoteContentId() {
        return voteContentId;
    }
    /**
     * @param voteContentId The voteContentId to set.
     */
    public void setVoteContentId(Long voteContentId) {
        this.voteContentId = voteContentId;
    }
    /**
     * @return Returns the voteContent.
     */
    public org.lamsfoundation.lams.tool.vote.pojos.VoteContent getVoteContent() {
        return voteContent;
    }
    /**
     * @param voteContent The voteContent to set.
     */
    public void setVoteContent(
            org.lamsfoundation.lams.tool.vote.pojos.VoteContent voteContent) {
        this.voteContent = voteContent;
    }
    /**
     * @return Returns the voteQueUsers.
     */
    public Set getVoteQueUsers() {
        return voteQueUsers;
    }
    /**
     * @param voteQueUsers The voteQueUsers to set.
     */
    public void setVoteQueUsers(Set voteQueUsers) {
        this.voteQueUsers = voteQueUsers;
    }
}
