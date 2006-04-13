/***************************************************************************
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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.vote.pojos;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * <p>Persistent  object/bean that defines the user for the Voting tool.
 * Provides accessors and mutators to get/set attributes
 * It maps to database table: tl_vote11_que_usr
 * </p>
 * 
 * @author Ozgur Demirtas
 */
public class VoteQueUsr implements Serializable {

    /** identifier field */
    private Long uid;

    /** persistent field */
    private Long queUsrId;

    /** nullable persistent field */
    private String username;

    /** nullable persistent field */
    private String fullname;

    private Long voteSessionId;
    
    /** nullable persistent field */
    private org.lamsfoundation.lams.tool.vote.pojos.VoteSession voteSession;

    /** persistent field */
    private Set voteUsrAttempts;

    /** full constructor */
    public VoteQueUsr(Long queUsrId, String username, String fullname,  org.lamsfoundation.lams.tool.vote.pojos.VoteSession voteSession, Set voteUsrAttempts) {
        this.queUsrId = queUsrId;
        this.username = username;
        this.fullname = fullname;
        this.voteSession = voteSession;
        this.voteUsrAttempts = voteUsrAttempts;
    }

    /** default constructor */
    public VoteQueUsr() {
    }

    /** minimal constructor */
    public VoteQueUsr(Long queUsrId, Set voteUsrAttempts) {
        this.queUsrId = queUsrId;
        this.voteUsrAttempts = voteUsrAttempts;
    }

    public Long getUid() {
        return this.uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getQueUsrId() {
        return this.queUsrId;
    }

    public void setQueUsrId(Long queUsrId) {
        this.queUsrId = queUsrId;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return this.fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Set getVoteUsrAttempts() {
    	if (this.voteUsrAttempts == null)
        	setVoteUsrAttempts(new HashSet());
        return this.voteUsrAttempts;
    }
    
    
    public void setMcUsrAttempts(Set voteUsrAttempts) {
        this.voteUsrAttempts = voteUsrAttempts;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("uid", getUid())
            .toString();
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
     * @return Returns the voteSession.
     */
    public org.lamsfoundation.lams.tool.vote.pojos.VoteSession getVoteSession() {
        return voteSession;
    }
    /**
     * @param voteSession The voteSession to set.
     */
    public void setVoteSession(
            org.lamsfoundation.lams.tool.vote.pojos.VoteSession voteSession) {
        this.voteSession = voteSession;
    }
    /**
     * @param voteUsrAttempts The voteUsrAttempts to set.
     */
    public void setVoteUsrAttempts(Set voteUsrAttempts) {
        this.voteUsrAttempts = voteUsrAttempts;
    }
}
