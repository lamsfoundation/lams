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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.ItemNotFoundException;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;

/**
 * <p>Persistent  object/bean that defines the content for the Voting tool.
 * Provides accessors and mutators to get/set attributes
 * It maps to database table: tl_lavote11_content
 * </p>
 * 
 * @author Ozgur Demirtas
 */
public class VoteContent implements Serializable {
	static Logger logger = Logger.getLogger(VoteContent.class.getName());

    /** identifier field */
    private Long uid;

    /** persistent field */
    private Long voteContentId;
    
    /** persistent field, used for export portfolio */
    private String content;

    /** nullable persistent field */
    private String title;

    /** nullable persistent field */
    private String instructions;
    
       /** nullable persistent field */
    private boolean defineLater;

    /** nullable persistent field */
    private boolean runOffline;

    /** nullable persistent field */
    private Date creationDate;

    /** nullable persistent field */
    private Date updateDate;

   
    /** nullable persistent field */
    private long createdBy;
    
    private boolean voteChangable;
    
    private boolean allowText;
    
    private String maxNominationCount;

    /** nullable persistent field */
    private boolean lockOnFinish;

    /** nullable persistent field */
    private boolean contentInUse;

    /** nullable persistent field */
    private String offlineInstructions;

    /** nullable persistent field */
    private String onlineInstructions;

   
    /** persistent field */
    private Set voteQueContents;

    /** persistent field */
    private Set voteSessions;
    
    /** persistent field */
    private Set voteAttachments;

    /** full constructor */
    public VoteContent(Long voteContentId, String content, String title, String instructions, boolean defineLater, boolean runOffline, 
            Date creationDate, Date updateDate, boolean voteChangable, boolean allowText, String maxNominationCount, 
            long createdBy, boolean lockOnFinish, boolean contentInUse, String offlineInstructions, 
            String onlineInstructions, Set voteQueContents, Set voteSessions, 
			Set voteAttachments) {
        this.voteContentId = voteContentId;
        this.content=content;
        this.title = title;
        this.instructions = instructions;
        this.defineLater = defineLater;
        this.runOffline = runOffline;
        this.creationDate = creationDate;
        this.updateDate = updateDate;
        this.voteChangable=voteChangable;
        this.maxNominationCount=maxNominationCount;
        this.allowText=allowText;
        this.createdBy = createdBy;
        this.lockOnFinish = lockOnFinish;
        this.contentInUse = contentInUse;
        this.offlineInstructions = offlineInstructions;
        this.onlineInstructions = onlineInstructions;
        this.voteQueContents = voteQueContents;
        this.voteSessions = voteSessions;
        this.voteAttachments = voteAttachments;
    }

    /** default constructor */
    public VoteContent() {
    }

    /** minimal constructor */
    public VoteContent(Long voteContentId, Set voteQueContents, Set voteSessions) {
        this.voteContentId = voteContentId;
        this.voteQueContents = voteQueContents;
        this.voteSessions = voteSessions;
    }
    
    
    /**
     *  gets called as part of the copyToolContent
     *  
     * Copy Construtor to create a new mc content instance. Note that we
     * don't copy the mc session data here because the mc session 
     * will be created after we copied tool content.
     * @param mc the original mc content.
     * @param newContentId the new mc content id.
     * @return the new mc content object.
     */
    public static VoteContent newInstance(IToolContentHandler toolContentHandler, VoteContent vote,
            Long newContentId)
    throws ItemNotFoundException, RepositoryCheckedException
    {
        VoteContent newContent = new VoteContent(
    				 newContentId,
    				 vote.getContent(),
    				 vote.getTitle(),
    				 vote.getInstructions(),
    				 vote.isDefineLater(),
    				 vote.isRunOffline(),
    				 vote.getCreationDate(),
    				 vote.getUpdateDate(),
    				 vote.isVoteChangable(),
    				 vote.isAllowText(),
    				 vote.getMaxNominationCount(),
    				 vote.getCreatedBy(),				 
    				 vote.isLockOnFinish(),
    				 vote.isContentInUse(),
    				 vote.getOfflineInstructions(),
    				 vote.getOnlineInstructions(),
         			 new TreeSet(),
                     new TreeSet(),
                     new TreeSet()
					 );
    	newContent.setVoteQueContents(vote.deepCopyMcQueContent(newContent));
    	newContent.setVoteAttachments(vote.deepCopyMcAttachments(toolContentHandler, newContent));
    	
    	return newContent;
	}
    
    /**
     * gets called as part of the copyToolContent
     * 
     * @param newQaContent
     * @return Set
     */
    public Set deepCopyMcQueContent(VoteContent newMcContent)
    {
    	
    	Set newMcQueContent = new TreeSet();
        for (Iterator i = this.getVoteQueContents().iterator(); i.hasNext();)
        {
            VoteQueContent queContent = (VoteQueContent) i.next();
            if (queContent.getMcContent() != null)
            {
                int displayOrder=queContent.getDisplayOrder();
            	VoteQueContent mcQueContent=VoteQueContent.newInstance(queContent,displayOrder,
															newMcContent);
            	newMcQueContent.add(mcQueContent);
            }
        }
        return newMcQueContent;
    }
    
    /**
     * gets called as part of the copyToolContent
     * 
     * @param newMcContent
     * @return Set
     */
    public Set deepCopyMcAttachments(IToolContentHandler toolContentHandler,VoteContent newMcContent)
    throws ItemNotFoundException, RepositoryCheckedException
    {
    	Set newMcQueContent = new TreeSet();
        for (Iterator i = this.getVoteAttachments().iterator(); i.hasNext();)
        {
        	VoteUploadedFile mcUploadedFile = (VoteUploadedFile) i.next();
            if (mcUploadedFile.getVoteContent() != null)
            {
            	VoteUploadedFile newMcUploadedFile=VoteUploadedFile.newInstance(toolContentHandler, mcUploadedFile,
															newMcContent);
            	newMcQueContent.add(newMcUploadedFile);
            }
        }
        return newMcQueContent;
    }
    

    public Long getUid() {
        return this.uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }


    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInstructions() {
        return this.instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public boolean isDefineLater() {
        return this.defineLater;
    }

    public void setDefineLater(boolean defineLater) {
        this.defineLater = defineLater;
    }

    public boolean isRunOffline() {
        return this.runOffline;
    }

    public void setRunOffline(boolean runOffline) {
        this.runOffline = runOffline;
    }

    public Date getUpdateDate() {
        return this.updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public long getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(long createdBy) {
        this.createdBy = createdBy;
    }

    
    public boolean isContentInUse() {
        return this.contentInUse;
    }

    public void setContentInUse(boolean contentInUse) {
        this.contentInUse = contentInUse;
    }

    public String getOfflineInstructions() {
        return this.offlineInstructions;
    }

    public void setOfflineInstructions(String offlineInstructions) {
        this.offlineInstructions = offlineInstructions;
    }

    public String getOnlineInstructions() {
        return this.onlineInstructions;
    }

    public void setOnlineInstructions(String onlineInstructions) {
        this.onlineInstructions = onlineInstructions;
    }

    
    /**
     * @return Returns the voteQueContents.
     */
    public Set getVoteQueContents() {
    	if (this.voteQueContents == null)
        	setVoteQueContents(new HashSet());
        return this.voteQueContents;
        
    }
    /**
     * @param voteQueContents The voteQueContents to set.
     */
    public void setVoteQueContents(Set voteQueContents) {
        this.voteQueContents = voteQueContents;
    }
    
    
    /**
     * @return Returns the voteSessions.
     */
    public Set getVoteSessions() {
    	if (this.voteSessions == null)
    	    setVoteSessions(new HashSet());
        return this.voteSessions;        
    }
    
    /**
     * @param voteSessions The voteSessions to set.
     */
    public void setVoteSessions(Set voteSessions) {
        this.voteSessions = voteSessions;
    }    
    
    

    public String toString() {
        return new ToStringBuilder(this)
            .append("uid", getUid())
            .toString();
    }
    
	
    /**
     * @return Returns the voteAttachments.
     */
    public Set getVoteAttachments() {
        return voteAttachments;
    }
    /**
     * @param voteAttachments The voteAttachments to set.
     */
    public void setVoteAttachments(Set voteAttachments) {
        this.voteAttachments = voteAttachments;
    }	
	
	/**
	 * @return Returns the creationDate.
	 */
	public Date getCreationDate() {
		return creationDate;
	}
	/**
	 * @param creationDate The creationDate to set.
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	/**
	 * @return Returns the content.
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content The content to set.
	 */
	public void setContent(String content) {
		this.content = content;
	}
    /**
     * @return Returns the lockOnFinish.
     */
    public boolean isLockOnFinish() {
        return lockOnFinish;
    }
    /**
     * @param lockOnFinish The lockOnFinish to set.
     */
    public void setLockOnFinish(boolean lockOnFinish) {
        this.lockOnFinish = lockOnFinish;
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
     * @return Returns the voteChangable.
     */
    public boolean isVoteChangable() {
        return voteChangable;
    }
    /**
     * @param voteChangable The voteChangable to set.
     */
    public void setVoteChangable(boolean voteChangable) {
        this.voteChangable = voteChangable;
    }

    /**
     * @return Returns the allowText.
     */
    public boolean isAllowText() {
        return allowText;
    }
    /**
     * @param allowText The allowText to set.
     */
    public void setAllowText(boolean allowText) {
        this.allowText = allowText;
    }    
    /**
     * @return Returns the maxNominationCount.
     */
    public String getMaxNominationCount() {
        return maxNominationCount;
    }
    /**
     * @param maxNominationCount The maxNominationCount to set.
     */
    public void setMaxNominationCount(String maxNominationCount) {
        this.maxNominationCount = maxNominationCount;
    }
}
