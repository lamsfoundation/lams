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
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;


/**
 * <p>Persistent  object/bean that defines the question content for the Voting tool.
 * Provides accessors and mutators to get/set attributes
 * It maps to database table: tl_vote11_que_content
 * </p>
 * 
 * @author Ozgur Demirtas
 */
public class VoteQueContent implements Serializable, Comparable {
	static Logger logger = Logger.getLogger(VoteQueContent.class.getName());

    /** identifier field */
    private Long uid;

    /** persistent field */
    private Long voteQueContentId;

    /** nullable persistent field */
    private String question;
    
    private int displayOrder;

    
    /** non persistent field */
    private Long voteContentId;
    
    /** persistent field */
    private org.lamsfoundation.lams.tool.vote.pojos.VoteContent voteContent;
    
    /** persistent field */
    private Set voteUsrAttempts;

    /** persistent field */
    private Set voteOptionsContents;

    /** full constructor */
    public VoteQueContent(Long voteQueContentId, String question,  VoteContent voteContent, Set voteUsrAttempts, Set voteOptionsContents) {
        this.voteQueContentId = voteQueContentId;
        this.question = question;
        this.voteContent=voteContent;
        this.voteUsrAttempts = voteUsrAttempts;
        this.voteOptionsContents = voteOptionsContents;
    }
    
    public VoteQueContent(String question,  VoteContent voteContent, Set voteUsrAttempts, Set voteOptionsContents) {
        this.question = question;
        this.voteContent=voteContent;
        this.voteUsrAttempts = voteUsrAttempts;
        this.voteOptionsContents = voteOptionsContents;
    }

    public VoteQueContent(String question,  int displayOrder, VoteContent voteContent, Set voteUsrAttempts, Set voteOptionsContents) {
        this.question = question;
        this.displayOrder=displayOrder;
        this.voteContent=voteContent;
        this.voteUsrAttempts = voteUsrAttempts;
        this.voteOptionsContents = voteOptionsContents;
    }

    
    public VoteQueContent(Long voteQueContentId, String question, Set voteUsrAttempts, Set voteOptionsContents) {
        this.voteQueContentId = voteQueContentId;
        this.question = question;
        this.voteUsrAttempts = voteUsrAttempts;
        this.voteOptionsContents = voteOptionsContents;
    }
    
    public VoteQueContent(String question, Set voteUsrAttempts, Set voteOptionsContents) {
        this.question = question;
        this.voteUsrAttempts = voteUsrAttempts;
        this.voteOptionsContents = voteOptionsContents;
    }
    
    

    /** default constructor */
    public VoteQueContent() {
    }

    /** minimal constructor */
    public VoteQueContent(Long voteQueContentId, org.lamsfoundation.lams.tool.vote.pojos.VoteContent voteContent, Set voteUsrAttempts, Set voteOptionsContents) {
        this.voteQueContentId = voteQueContentId;
        this.voteContent = voteContent;
        this.voteUsrAttempts = voteUsrAttempts;
        this.voteOptionsContents = voteOptionsContents;
    }
    
    
    /**
     *  gets called by copyToolContent
     * 
     * Copy constructor
     * @param queContent the original qa question content
     * @return the new qa question content object
     */
    public static VoteQueContent newInstance(VoteQueContent queContent, int displayOrder,
    										VoteContent newMcContent)
    										
    {
    	VoteQueContent newQueContent = new VoteQueContent(queContent.getQuestion(),
    	        										displayOrder, 
													  newMcContent,
                                                      new TreeSet(),
                                                      new TreeSet());
    	
    	return newQueContent;
    }
    
    
    public Long getUid() {
        return this.uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getQuestion() {
        return this.question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public org.lamsfoundation.lams.tool.vote.pojos.VoteContent getMcContent() {
        return this.voteContent;
    }

    public void setMcContent(org.lamsfoundation.lams.tool.vote.pojos.VoteContent voteContent) {
        this.voteContent = voteContent;
    }

    public Set getVoteUsrAttempts() {
    	if (this.voteUsrAttempts == null)
        	setVoteUsrAttempts(new HashSet());
        return this.voteUsrAttempts;
    }

    
    public void setMcUsrAttempts(Set voteUsrAttempts) {
        this.voteUsrAttempts = voteUsrAttempts;
    }

    
    public Set getVoteOptionsContents() {
    	if (this.voteOptionsContents == null)
        	setVoteOptionsContents(new HashSet());
        return this.voteOptionsContents;
    }

    public void setMcOptionsContents(Set mcOptionsContents) {
        this.voteOptionsContents = voteOptionsContents;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("uid", getUid())
            .toString();
    }
	

	public int compareTo(Object o)
    {
        VoteQueContent queContent = (VoteQueContent) o;
        //if the object does not exist yet, then just return any one of 0, -1, 1. Should not make a difference.
        if (voteQueContentId == null)
        	return 1;
		else
			return (int) (voteQueContentId.longValue() - queContent.voteQueContentId.longValue());
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
     * @return Returns the logger.
     */
    public static Logger getLogger() {
        return logger;
    }
    /**
     * @param logger The logger to set.
     */
    public static void setLogger(Logger logger) {
        VoteQueContent.logger = logger;
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
     * @param voteUsrAttempts The voteUsrAttempts to set.
     */
    public void setVoteUsrAttempts(Set voteUsrAttempts) {
        this.voteUsrAttempts = voteUsrAttempts;
    }
    /**
     * @param voteOptionsContents The voteOptionsContents to set.
     */
    public void setVoteOptionsContents(Set voteOptionsContents) {
        this.voteOptionsContents = voteOptionsContents;
    }
    /**
     * @return Returns the displayOrder.
     */
    public int getDisplayOrder() {
        return displayOrder;
    }
    /**
     * @param displayOrder The displayOrder to set.
     */
    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }
}
