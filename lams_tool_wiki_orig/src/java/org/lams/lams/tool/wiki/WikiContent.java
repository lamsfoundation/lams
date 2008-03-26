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
package org.lams.lams.tool.wiki;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.lamsfoundation.lams.contentrepository.ItemNotFoundException;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;


/**
 * <p>Persistent wiki object/bean that defines the content for the wiki tool.
 * Provides accessors and mutators to get/set wiki attributes</p>
 * @hibernate.class table="tl_lawiki10_content"
 * @author mtruong
 */
public class WikiContent implements Serializable {
	
	/** identifier field */
    private Long uid;
    
    /** non-nullable persistent field */
	private Long wikiContentId;
	
	/** nullable persistent field */
	private String title;
	
	/** nullable persistent field */
	private String content;
	
	/** nullable persistent field */
	private String onlineInstructions;
	
	/** nullable persistent field */
	private String offlineInstructions;
	
	/** nullable persistent field */
	private boolean defineLater;
	
	/** nullable persistent field */
	private boolean forceOffline;

	private Boolean reflectOnActivity;
	
	private String reflectInstructions;
	
	/** nullable persistent field */
	private boolean contentInUse;
	
	/** nullable persistent field */
	private Long creatorUserId;
	
	/** nullable persistent field */
	private Date dateCreated;
	
	/** nullable persistent field */
	private Date dateUpdated;
	
	/** persistent field */
	private Set wikiSessions = new HashSet();
	
	private Set wikiAttachments = new HashSet();
	
	/** default constructor */
	public WikiContent()
	{
	}
	
	/** full constructor */
	public WikiContent(Long wikiContentId,
							  String title,
							  String content,
							  String onlineInstructions,
							  String offlineInstructions,
							  boolean defineLater,
							  boolean forceOffline,
							  boolean reflectOnActivity,
							  String reflectInstructions,
							  boolean contentInUse,
							  Long creatorUserId,
							  Date dateCreated,
							  Date dateUpdated)
	{
		this.wikiContentId = wikiContentId;
		this.title = title;
		this.content = content;
		this.onlineInstructions = onlineInstructions;
		this.offlineInstructions = offlineInstructions;
		this.defineLater = defineLater;
		this.forceOffline = forceOffline;
		this.reflectOnActivity = reflectOnActivity;
		this.reflectInstructions = reflectInstructions;
		this.contentInUse = contentInUse;
		this.creatorUserId = creatorUserId;
		this.dateCreated = dateCreated;
		this.dateUpdated = dateUpdated;
	}
	
	/**
	 * Minimal Constructor used to initialise values for the WikiContent object
	 * @return
	 */
	
	public WikiContent(Long wikiContentId,
	        				  String title,
	        				  String content,
	        				  String onlineInstructions,
	        				  String offlineInstructions,
	        				  Date dateCreated)
	{
	    this.wikiContentId = wikiContentId;
		this.title = title;
		this.content = content;
		this.onlineInstructions = onlineInstructions;
		this.offlineInstructions = offlineInstructions;
		this.defineLater = false;
		this.forceOffline = false;
		this.reflectOnActivity = false;
		this.contentInUse = false;
		this.creatorUserId = null;
		this.dateCreated = dateCreated;
		this.dateUpdated = null;
	}
	
	
    
	/**
	 *		 @hibernate.property
     *       column="content"
     *       length="65535"
	 */
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	/**
	 * 
	 *		@hibernate.property
     *		column="creator_user_id"
     *		length="20"
     */	
	public Long getCreatorUserId() {
		return creatorUserId;
	}
	
	public void setCreatorUserId(Long creatorUserId) {
		this.creatorUserId = creatorUserId;
	}
	
	/**
	 * 
	 *		@hibernate.property
     *		column="date_created"
     *		length="19"
     */
	public Date getDateCreated() {
		return dateCreated;
	}
	
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	/**
	 * 
	 *		@hibernate.property
     *		column="date_updated"
     *		length="19"
     */
	public Date getDateUpdated() {
		return dateUpdated;
	}
	
	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}
	
	/** 
	 *		@hibernate.property
     *		column="define_later"
     *		length="1"
     */
	public boolean isDefineLater() {
		return defineLater;
	}
	
	public void setDefineLater(boolean defineLater) {
		this.defineLater = defineLater;
	}
	
	/** 
	 *		@hibernate.property
     *		column="force_offline"
     *		length="1"
     */
	public boolean isForceOffline() {
		return forceOffline;
	}
	
	public void setForceOffline(boolean forceOffline) {
		this.forceOffline = forceOffline;
	}
	
	/** 
	 *		@hibernate.property
     *		column="reflect_on_activity"
     *		length="1"
     */
	public boolean getReflectOnActivity() {
		return reflectOnActivity;
	}
	
	public void setReflectOnActivity(boolean reflectOnActivity) {
		this.reflectOnActivity = reflectOnActivity;
	}
	
	/**
	 *		 @hibernate.property
     *       column="reflect_instructions"
     *       length="65535"
	 */
	public String getReflectInstructions() {
		return reflectInstructions;
	}

	public void setReflectInstructions(String reflectInstructions) {
		this.reflectInstructions = reflectInstructions;
	}
	
	 /** 
	 *		@hibernate.property
     *		column="content_in_use"
     *		length="1"
     */
    
    public boolean isContentInUse() {
        return contentInUse;
    }
    /**
     * @param contentInUse The contentInUse to set.
     */
    public void setContentInUse(boolean contentInUse) {
        this.contentInUse = contentInUse;
    }
	
	/** 
	 *		@hibernate.property
     *      column="wiki_content_id"
     *      length="20"
     *      not-null="true"
     */
	
	public Long getWikiContentId() {
		return wikiContentId;
	}
	
	public void setWikiContentId(Long wikiContentId) {
		this.wikiContentId = wikiContentId;
	}
	/**
	 * 		@hibernate.set
     *      lazy="true"
     *      inverse="true"
     *      cascade="all-delete-orphan"
     *     	@hibernate.collection-key
     *      column="wiki_content_uid"
     *     	@hibernate.collection-one-to-many
     *      class="org.lams.lams.tool.wiki.WikiSession"
     */
	public Set getWikiSessions() {
		if (this.wikiSessions == null)
		{
			setWikiSessions(new HashSet());
		}
		return wikiSessions;
	}
	
	public void setWikiSessions(Set wikiSessions) {
		this.wikiSessions = wikiSessions;
	}
	
	/**
     *  	@hibernate.set
     *      lazy="true"
     *      inverse="true"
     *      cascade="all-delete-orphan"
     *      @hibernate.collection-key
     *      column="wiki_content_uid"
     * 		@hibernate.collection-one-to-many
     *      class="org.lams.lams.tool.wiki.WikiAttachment"
     */
    public Set getWikiAttachments() {
        return wikiAttachments;
    }
    /**
     * @param wikiAttachments The wikiAttachments to set.
     */
    public void setWikiAttachments(Set wikiAttachments) {
        this.wikiAttachments = wikiAttachments;
    }
	
	
	
	/**
	 * 
	 *		@hibernate.property
     *		column="offline_instructions"
     *		length="65535"
     */	
	public String getOfflineInstructions() {
		return offlineInstructions;
	}
	
	public void setOfflineInstructions(String offlineInstructions) {
		this.offlineInstructions = offlineInstructions;
	}
	
	/**
	 * 
	 *		@hibernate.property
     *		column="online_instructions"
     *		length="65535"
     */	
	public String getOnlineInstructions() {
		return onlineInstructions;
	}
	
	public void setOnlineInstructions(String onlineInstructions) {
		this.onlineInstructions = onlineInstructions;
	}
	
	/**
	 * 		@hibernate.property
     *      column="title"
     *      length="65535"
	 */
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	 /**
      * 	@hibernate.id
      *     generator-class="native"
      *     type="java.lang.Long"
      *     column="uid"
      *     unsaved-value="0"
      */
    public Long getUid() {
        return uid;
    }
    
    public void setUid(Long uid) {
        this.uid = uid;
    }
	

   
    
    
    
    
	/** 
	 * Creates a new WikiContent object from the supplied object.
	 * Assigns it the toContendId. Also copies all the items in the attachment set
	 * to the new object's attachment set. So while the two contents have different
	 * attachment records, they point to the same entries in the database.
	 * 
	 * @param wiki			WikiContent object containing the content to copy from
	 * @param toContentId 	The new Id of the new wiki object
	 * @return newContent	The new wiki content object
	 * @throws RepositoryCheckedException 
	 * @throws ItemNotFoundException 
	 */
	public static WikiContent newInstance(WikiContent wiki, Long toContentId, IToolContentHandler toolContentHandler) throws ItemNotFoundException, RepositoryCheckedException
	{
		WikiContent newContent = new WikiContent(toContentId,
														wiki.getTitle(),
														wiki.getContent(),
														wiki.getOnlineInstructions(),
														wiki.getOfflineInstructions(),
														wiki.isDefineLater(),
														wiki.isForceOffline(),
														wiki.getReflectOnActivity(),
														wiki.getReflectInstructions(),
														wiki.isContentInUse(),
														wiki.getCreatorUserId(),
														wiki.getDateCreated(),
														wiki.getDateUpdated());
		
		if ( wiki.getWikiAttachments() != null && wiki.getWikiAttachments().size() > 0 ) {
			HashSet newAttachmentSet = new HashSet();
			Iterator iter = wiki.getWikiAttachments().iterator();
			while (iter.hasNext()) {
				WikiAttachment element = (WikiAttachment) iter.next();
				WikiAttachment newAttachment = new WikiAttachment(newContent, element.getFilename(), element.isOnlineFile());
				//keep old value do not duplicate file
				newAttachment.setUuid(element.getUuid());
				newAttachment.setVersionId(element.getVersionId());
				
				newAttachmentSet.add(newAttachment);
			} 
			newContent.setWikiAttachments(newAttachmentSet);
		}
		
		return newContent;
	}
   
	
 
}
