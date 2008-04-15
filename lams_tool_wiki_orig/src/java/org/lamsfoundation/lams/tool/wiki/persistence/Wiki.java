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

/* $$Id$$ */	
package org.lamsfoundation.lams.tool.wiki.persistence;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.ItemNotFoundException;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.tool.wiki.util.WikiToolContentHandler;

/**
 * Wiki
 * @author dcarlier, forum base code by conradb
 *
 * @hibernate.class  table="tl_lawiki10_wiki"
 *
 */
public class Wiki implements Cloneable{
	
	private static final Logger log = Logger.getLogger(Wiki.class);
	
	//key 
	private Long uid;
	//tool contentID
	private Long contentId;
	private String title;
	private boolean lockWhenFinished;
	private boolean runOffline;
	private boolean allowAnonym;
	private boolean allowEdit;
	private boolean allowNewWikiPage;
	private boolean allowAttachImage;
	private boolean allowInsertLink;
	private boolean allowUpload;
	private int maximumReply;
	private int minimumReply;
	
	private boolean allowRichEditor;
	private String instructions;
	private String onlineInstructions;
	private String offlineInstructions;
	private boolean defineLater;
	private boolean contentInUse;
	private Date created;
	private Date updated;
	private WikiUser createdBy;
	
	private Set messages;
	private Set attachments;
	private int limitedChar;
    private boolean limitedInput;
    
	private boolean reflectOnActivity;
	private String reflectInstructions;

    //********* Non Persist fields
	private WikiToolContentHandler toolContentHandler;
    
	/**
	 * Default contruction method. 
	 *
	 */
  	public Wiki(){
  		attachments = new HashSet();
  		messages = new HashSet();
  	}
//  **********************************************************
  	//		Function method for Wiki
//  **********************************************************
  	public Object clone(){
  		
  		Wiki wiki = null;
  		try{
  			wiki = (Wiki) super.clone();
  			wiki.setUid(null);
  			//clone message
  			if(messages != null){
				Iterator iter = messages.iterator();
				Set set = new HashSet();
				while(iter.hasNext()){
					set.add(Message.newInstance((Message)iter.next(),toolContentHandler));
				}
				wiki.messages = set;
  			}
  			//clone attachment
  			if(attachments != null){
  				Iterator iter = attachments.iterator();
  				Set set = new HashSet();
  				while(iter.hasNext()){
  					Attachment file = (Attachment)iter.next(); 
  					Attachment newFile = (Attachment) file.clone();
  					//clone old file without duplicate it in repository
 
					set.add(newFile);
  				}
  				wiki.attachments = set;
  			}
		} catch (CloneNotSupportedException e) {
			log.error("When clone " + Wiki.class + " failed");
		}
  		
  		return wiki;
  	}
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Wiki))
			return false;

		final Wiki genericEntity = (Wiki) o;

      	return new EqualsBuilder()
      	.append(this.uid,genericEntity.uid)
      	.append(this.title,genericEntity.title)
      	.append(this.instructions,genericEntity.instructions)
      	.append(this.onlineInstructions,genericEntity.onlineInstructions)
      	.append(this.offlineInstructions,genericEntity.offlineInstructions)
      	.append(this.created,genericEntity.created)
      	.append(this.updated,genericEntity.updated)
      	.append(this.createdBy,genericEntity.createdBy)
      	.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(uid).append(title)
		.append(instructions).append(onlineInstructions)
		.append(offlineInstructions).append(created)
		.append(updated).append(createdBy)
		.toHashCode();
	}
	
	//**********************************************************
	// get/set methods
	//**********************************************************
	/**
	 * Returns the object's creation date
	 *
	 * @return date
	 * @hibernate.property column="create_date"
	 */
	public Date getCreated() {
      return created;
	}

	/**
	 * Sets the object's creation date
	 *
	 * @param created
	 */
	public void setCreated(Date created) {
	    this.created = created;
	}

	/**
	 * Returns the object's date of last update
	 *
	 * @return date updated
	 * @hibernate.property column="update_date"
	 */
	public Date getUpdated() {
        return updated;
	}

	/**
	 * Sets the object's date of last update
	 *
	 * @param updated
	 */
	public void setUpdated(Date updated) {
        this.updated = updated;
	}

    /**
     * @return Returns the userid of the user who created the Wiki.
     *
     * @hibernate.many-to-one
     *      cascade="none"
     * 		column="create_by"
     *
     */
    public WikiUser getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy The userid of the user who created this Wiki.
     */
    public void setCreatedBy(WikiUser createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @hibernate.id column="uid" generator-class="native"
     */
	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	/**
	 * @return Returns the title.
	 *
	 * @hibernate.property
	 * 		column="title"
	 *
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return Returns the allowAnonym.
	 *
	 * @hibernate.property 
	 * 		column="allow_anonym"
	 *  
	 */
	public boolean getAllowAnonym() {
		return allowAnonym;
	}
	
	/**
	 * @param allowAnonym The allowAnonym to set.
	 *
	 */
	public void setAllowAnonym(boolean allowAnnomity) {
		this.allowAnonym = allowAnnomity;
	}

	/**
	 * @return Returns the runOffline.
	 *
	 * @hibernate.property 
	 * 		column="run_offline"
	 *
	 */
	public boolean getRunOffline() {
		return runOffline;
	}
    
	/**
	 * @param runOffline The forceOffLine to set.
	 *
	 *
	 */
	public void setRunOffline(boolean forceOffline) {
		this.runOffline = forceOffline;
	}

    /**
     * @return Returns the lockWhenFinish.
     *
     * @hibernate.property
     * 		column="lock_on_finished"
     *
     */
    public boolean getLockWhenFinished() {
        return lockWhenFinished;
    }

    /**
     * @param lockWhenFinished Set to true to lock the wiki for finished users.
     */
    public void setLockWhenFinished(boolean lockWhenFinished) {
        this.lockWhenFinished = lockWhenFinished;
    }

    /**
     * @return Returns the instructions set by the teacher.
     *
     * @hibernate.property
     * 		column="instructions"
     *      type="text"
     */
    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    /**
     * @return Returns the onlineInstructions set by the teacher.
     *
     * @hibernate.property
     * 		column="online_instructions"
     *      type="text"
     */
    public String getOnlineInstructions() {
        return onlineInstructions;
    }

    public void setOnlineInstructions(String onlineInstructions) {
        this.onlineInstructions = onlineInstructions;
    }

    /**
     * @return Returns the onlineInstructions set by the teacher.
     *
     * @hibernate.property
     * 		column="offline_instructions"
     *      type="text"
     */
    public String getOfflineInstructions() {
        return offlineInstructions;
    }

    public void setOfflineInstructions(String offlineInstructions) {
        this.offlineInstructions = offlineInstructions;
    }

	/**
     *
     * @hibernate.set   lazy="true"
     * 					cascade="all"
     * 					inverse="false"
     * 					order-by="create_date desc"
     * @hibernate.collection-key column="wiki_uid"
     * @hibernate.collection-one-to-many
     * 			class="org.lamsfoundation.lams.tool.wiki.persistence.Attachment"
     *
     * @return a set of Attachments to this Message.
     */
	public Set getAttachments() {
		return attachments;
	}

    /*
	 * @param attachments The attachments to set.
     */
    public void setAttachments(Set attachments) {
		this.attachments = attachments;
	}

	/**
	 * NOTE: The reason that relation don't use save-update to persist message is MessageSeq table need save 
	 * a record as well.   
	 * 
	 * @hibernate.set lazy="true"
	 *                inverse="true"
	 *                cascade="none"
	 *                order-by="create_date desc"
	 * @hibernate.collection-key column="wiki_uid"
	 * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.tool.wiki.persistence.Message"
	 * 
	 * @return
	 */
	public Set getMessages() {
		return messages;
	}
	public void setMessages(Set messages) {
		this.messages = messages;
	}

	/**
	 * Updates the modification data for this entity.
	 */
	public void updateModificationData() {
	
		long now = System.currentTimeMillis();
		if (created == null) {
			this.setCreated (new Date(now));
		}
		this.setUpdated(new Date(now));
	}

	/**
	 * @hibernate.property  column="content_in_use"
	 * @return
	 */
	public boolean isContentInUse() {
		return contentInUse;
	}

	public void setContentInUse(boolean contentInUse) {
		this.contentInUse = contentInUse;
	}
	/**
	 * @hibernate.property column="define_later"
	 * @return
	 */
	public boolean isDefineLater() {
		return defineLater;
	}

	public void setDefineLater(boolean defineLater) {
		this.defineLater = defineLater;
	}
	/**
	 * @hibernate.property column="content_id" unique="true" 
	 * @return
	 */
	public Long getContentId() {
		return contentId;
	}

	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}

	/**
	 * @hibernate.property column="allow_edit"
	 * @return
	 */
	public boolean isAllowEdit() {
		return allowEdit;
	}
	public void setAllowEdit(boolean allowEdit) {
		this.allowEdit = allowEdit;
	}
	
	public static Wiki newInstance(Wiki fromContent, Long contentId, WikiToolContentHandler wikiToolContentHandler){
		Wiki toContent = new Wiki();
		fromContent.toolContentHandler = wikiToolContentHandler;
		toContent = (Wiki) fromContent.clone();
		toContent.setContentId(contentId);
		return toContent;
	}
	/**
	 * @hibernate.property column="limited_of_chars"
	 * @return
	 */
	public int getLimitedChar() {
		return limitedChar;
	}
	public void setLimitedChar(int limitedChar) {
		this.limitedChar = limitedChar;
	}
	/**
	 * @hibernate.property column="limited_input_flag"
	 * @return
	 */
	public boolean isLimitedInput() {
		return limitedInput;
	}
	public void setLimitedInput(boolean limitedInput) {
		this.limitedInput = limitedInput;
	}
	public WikiToolContentHandler getToolContentHandler() {
		return toolContentHandler;
	}
	public void setToolContentHandler(WikiToolContentHandler toolContentHandler) {
		this.toolContentHandler = toolContentHandler;
	}
	/**
	 * @hibernate.property column="allow_new_wiki_page"
	 * @return
	 */	
	public boolean isAllowNewWikiPage() {
		return allowNewWikiPage;
	}
	public void setAllowNewWikiPage(boolean allowNewWikiPage) {
		this.allowNewWikiPage = allowNewWikiPage;
	}
	/**
	 * @hibernate.property column="allow_attach_image"
	 * @return
	 */	
	public boolean isAllowAttachImage() {
		return allowAttachImage;
	}
	public void setAllowAttachImage(boolean allowAttachImage) {
		this.allowAttachImage = allowAttachImage;
	}
	/**
	 * @hibernate.property column="allow_insert_link"
	 * @return
	 */	
	public boolean isAllowInsertLink() {
		return allowInsertLink;
	}
	public void setAllowInsertLink(boolean allowInsertLink) {
		this.allowInsertLink = allowInsertLink;
	}
	/**
	 * @hibernate.property column="allow_upload"
	 * @return
	 */
	public boolean isAllowUpload() {
		return allowUpload;
	}
	public void setAllowUpload(boolean allowUpload) {
		this.allowUpload = allowUpload;
	}
	/**
	 * @hibernate.property column="maximum_reply"
	 * @return
	 */	
	public int getMaximumReply() {
		return maximumReply;
	}
	public void setMaximumReply(int maximumReply) {
		this.maximumReply = maximumReply;
	}
	/**
	 * @hibernate.property column="minimum_reply"
	 * @return
	 */	
	public int getMinimumReply() {
		return minimumReply;
	}
	public void setMinimumReply(int minimumReply) {
		this.minimumReply = minimumReply;
	}
	/**
	 * @hibernate.property column="reflect_instructions"
	 * @return
	 */	
	public String getReflectInstructions() {
		return reflectInstructions;
	}
	public void setReflectInstructions(String reflectInstructions) {
		this.reflectInstructions = reflectInstructions;
	}
	/**
	 * @hibernate.property column="reflect_on_activity"
	 * @return
	 */		
	public boolean isReflectOnActivity() {
		return reflectOnActivity;
	}
	public void setReflectOnActivity(boolean reflectOnActivity) {
		this.reflectOnActivity = reflectOnActivity;
	}
}
