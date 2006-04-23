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
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * <p>Persistent  object/bean that defines the user attempt for the MCQ tool.
 * Provides accessors and mutators to get/set attributes
 * It maps to database table: tl_vote11_usr_attempt
 * </p>
 * 
 * @author Ozgur Demirtas
 */
public class VoteUsrAttempt implements Serializable {

    /** identifier field */
    private Long uid;

    /** persistent field */
    private Long attemptId;

    /** nullable persistent field */
    private Date attemptTime;

    /** nullable persistent field */
    private String timeZone;
    
    private String userEntry;
    
    private int nominationCount;
    
    private Long queUsrId;
    
    private boolean singleUserEntry;
    
    private Long voteQueContentId;

    /** persistent field */
    private org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent voteQueContent;

    /** persistent field */
    private org.lamsfoundation.lams.tool.vote.pojos.VoteQueUsr voteQueUsr;

    /** persistent field */
    private org.lamsfoundation.lams.tool.vote.pojos.VoteOptsContent voteOptionsContent;

    /** full constructor */
    public VoteUsrAttempt(Long attemptId, Date attemptTime, String timeZone, org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent voteQueContent, 
    		org.lamsfoundation.lams.tool.vote.pojos.VoteQueUsr voteQueUsr) {
        this.attemptId = attemptId;
        this.attemptTime = attemptTime;
        this.timeZone = timeZone;
        this.voteQueContent = voteQueContent;
        this.voteQueUsr = voteQueUsr;
    }

    public VoteUsrAttempt(Date attemptTime, String timeZone, org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent voteQueContent, 
    		org.lamsfoundation.lams.tool.vote.pojos.VoteQueUsr voteQueUsr) {
        this.attemptTime = attemptTime;
        this.timeZone = timeZone;
        this.voteQueContent = voteQueContent;
        this.voteQueUsr = voteQueUsr;
    }

    public VoteUsrAttempt(Date attemptTime, String timeZone, org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent voteQueContent, 
    		org.lamsfoundation.lams.tool.vote.pojos.VoteQueUsr voteQueUsr, String userEntry) {
        this.attemptTime = attemptTime;
        this.timeZone = timeZone;
        this.voteQueContent = voteQueContent;
        this.voteQueUsr = voteQueUsr;
        this.userEntry=userEntry;
    }

    public VoteUsrAttempt(Date attemptTime, String timeZone, org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent voteQueContent, 
    		org.lamsfoundation.lams.tool.vote.pojos.VoteQueUsr voteQueUsr, String userEntry, int nominationCount) {
        this.attemptTime = attemptTime;
        this.timeZone = timeZone;
        this.voteQueContent = voteQueContent;
        this.voteQueUsr = voteQueUsr;
        this.userEntry=userEntry;
        this.nominationCount=nominationCount;
    }

    public VoteUsrAttempt(Date attemptTime, String timeZone, org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent voteQueContent, 
    		org.lamsfoundation.lams.tool.vote.pojos.VoteQueUsr voteQueUsr, String userEntry, int nominationCount, boolean singleUserEntry) {
        this.attemptTime = attemptTime;
        this.timeZone = timeZone;
        this.voteQueContent = voteQueContent;
        this.voteQueUsr = voteQueUsr;
        this.userEntry=userEntry;
        this.nominationCount=nominationCount;
        this.singleUserEntry=singleUserEntry;
    }

    
    

    public VoteUsrAttempt(String userEntry, Date attemptTime, String timeZone, org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent voteQueContent, 
    		org.lamsfoundation.lams.tool.vote.pojos.VoteQueUsr voteQueUsr) {
        this.userEntry=userEntry;
        this.attemptTime = attemptTime;
        this.timeZone = timeZone;
        this.voteQueContent = voteQueContent;
        this.voteQueUsr = voteQueUsr;
    }

    
    /** default constructor */
    public VoteUsrAttempt() {
    }

    /** minimal constructor */
    public VoteUsrAttempt(Long attemptId, org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent voteQueContent, 
            org.lamsfoundation.lams.tool.vote.pojos.VoteQueUsr voteQueUsr) {
        this.attemptId = attemptId;
        this.voteQueContent = voteQueContent;
        this.voteQueUsr = voteQueUsr;
    }

    public Long getUid() {
        return this.uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getAttemptId() {
        return this.attemptId;
    }

    public void setAttemptId(Long attemptId) {
        this.attemptId = attemptId;
    }

    public Date getAttemptTime() {
        return this.attemptTime;
    }

    public void setAttemptTime(Date attemptTime) {
        this.attemptTime = attemptTime;
    }

    public String getTimeZone() {
        return this.timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    
    public String toString() {
        return new ToStringBuilder(this)
            .append("uid", getUid())
            .toString();
    }

	/**
	 * @return Returns the queUsrId.
	 */
	public Long getQueUsrId() {
		return queUsrId;
	}
	/**
	 * @param queUsrId The queUsrId to set.
	 */
	public void setQueUsrId(Long queUsrId) {
		this.queUsrId = queUsrId;
	}
    /**
     * @return Returns the voteQueContentId.
     */
    public Long getVoteQueContentId() {
        return voteQueContentId;
    }
    /**
     * @param voteQueContentId The voteQueContentId to set.
     */
    public void setVoteQueContentId(Long voteQueContentId) {
        this.voteQueContentId = voteQueContentId;
    }
    /**
     * @return Returns the voteQueContent.
     */
    public org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent getVoteQueContent() {
        return voteQueContent;
    }
    /**
     * @param voteQueContent The voteQueContent to set.
     */
    public void setVoteQueContent(
            org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent voteQueContent) {
        this.voteQueContent = voteQueContent;
    }
    /**
     * @return Returns the voteQueUsr.
     */
    public org.lamsfoundation.lams.tool.vote.pojos.VoteQueUsr getVoteQueUsr() {
        return voteQueUsr;
    }
    /**
     * @param voteQueUsr The voteQueUsr to set.
     */
    public void setVoteQueUsr(
            org.lamsfoundation.lams.tool.vote.pojos.VoteQueUsr voteQueUsr) {
        this.voteQueUsr = voteQueUsr;
    }
    /**
     * @return Returns the voteOptionsContent.
     */
    public org.lamsfoundation.lams.tool.vote.pojos.VoteOptsContent getVoteOptionsContent() {
        return voteOptionsContent;
    }
    /**
     * @param voteOptionsContent The voteOptionsContent to set.
     */
    public void setVoteOptionsContent(
            org.lamsfoundation.lams.tool.vote.pojos.VoteOptsContent voteOptionsContent) {
        this.voteOptionsContent = voteOptionsContent;
    }
    /**
     * @return Returns the userEntry.
     */
    public String getUserEntry() {
        return userEntry;
    }
    /**
     * @param userEntry The userEntry to set.
     */
    public void setUserEntry(String userEntry) {
        this.userEntry = userEntry;
    }

    /**
     * @return Returns the nominationCount.
     */
    public int getNominationCount() {
        return nominationCount;
    }
    /**
     * @param nominationCount The nominationCount to set.
     */
    public void setNominationCount(int nominationCount) {
        this.nominationCount = nominationCount;
    }
    
    /**
     * @return Returns the singleUserEntry.
     */
    public boolean isSingleUserEntry() {
        return singleUserEntry;
    }
    /**
     * @param singleUserEntry The singleUserEntry to set.
     */
    public void setSingleUserEntry(boolean singleUserEntry) {
        this.singleUserEntry = singleUserEntry;
    }
    
}
