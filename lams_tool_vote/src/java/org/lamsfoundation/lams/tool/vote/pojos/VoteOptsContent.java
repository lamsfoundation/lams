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
 * <p>Persistent  object/bean that defines the content for the voting tool.
 * Provides accessors and mutators to get/set attributes
 * It maps to database table: tl_vote11_options_content
 * </p>
 * 
 * @author Ozgur Demirtas
 */ 
public class VoteOptsContent implements Serializable, Comparable {
	static Logger logger = Logger.getLogger(VoteOptsContent.class.getName());
	
    /** identifier field */
    private Long uid;
    
    private Long voteQueOptionId;
    

    /** nullable persistent field */
    private String voteQueOptionText;

    /** non persistent field */
    private Long voteQueContentId;
	
    /** persistent field */
    private org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent voteQueContent;
    
    /** persistent field */
    private Set voteUsrAttempts;

    
    public VoteOptsContent(Long voteQueOptionId, String voteQueOptionText, 
            org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent voteQueContent, Set voteUsrAttempts) {
    	this.voteQueOptionId=voteQueOptionId;
        this.voteQueOptionText = voteQueOptionText;
        this.voteQueContent = voteQueContent;
        this.voteUsrAttempts=voteUsrAttempts;
    }
    
    
    public VoteOptsContent(String voteQueOptionText, org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent voteQueContent, Set voteUsrAttempts) {
        this.voteQueOptionText = voteQueOptionText;
        this.voteQueContent = voteQueContent;
        this.voteUsrAttempts=voteUsrAttempts;
    }
    
    public static VoteOptsContent newInstance(VoteOptsContent voteOptsContent,
											VoteQueContent newVoteQueContent)
											
	{
    	VoteOptsContent newVoteOptsContent = new VoteOptsContent(
    	        											voteOptsContent.getVoteQueOptionText(),
    	        											newVoteQueContent,
														  new TreeSet());
    	return newVoteOptsContent;
	}
    
    
    /** default constructor */
    public VoteOptsContent() {
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
	 * @return Returns the mcUsrAttempts.
	 */
	public Set getVoteUsrAttempts() {
		if (this.voteUsrAttempts == null)
			setVoteUsrAttempts(new HashSet());
	    return this.voteUsrAttempts;
	}
	/**
	 * @param mcUsrAttempts The mcUsrAttempts to set.
	 */
	public void setVoteUsrAttempts(Set voteUsrAttempts) {
		this.voteUsrAttempts = voteUsrAttempts;
	}
	
	
	public int compareTo(Object o)
    {
		VoteOptsContent optContent = (VoteOptsContent) o;
        //if the object does not exist yet, then just return any one of 0, -1, 1. Should not make a difference.
        if (voteQueOptionId == null)
        	return 1;
		else
			return (int) (voteQueOptionId.longValue() - optContent.voteQueOptionId.longValue());
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
     * @return Returns the voteQueOptionText.
     */
    public String getVoteQueOptionText() {
        return voteQueOptionText;
    }
    /**
     * @param voteQueOptionText The voteQueOptionText to set.
     */
    public void setVoteQueOptionText(String voteQueOptionText) {
        this.voteQueOptionText = voteQueOptionText;
    }
    /**
     * @return Returns the voteQueOptionId.
     */
    public Long getVoteQueOptionId() {
        return voteQueOptionId;
    }
    /**
     * @param voteQueOptionId The voteQueOptionId to set.
     */
    public void setVoteQueOptionId(Long voteQueOptionId) {
        this.voteQueOptionId = voteQueOptionId;
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
}
