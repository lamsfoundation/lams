/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */
package org.lamsfoundation.lams.tool.noticeboard;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import java.util.HashSet;


import org.apache.commons.lang.builder.EqualsBuilder;
/**
 * Persistent noticeboard object/bean that defines the content for the noticeboard tool.
 * Provides accessors and mutators to get/set noticeboard attributes
 * @author mtruong
 *
 */
public class NoticeboardContent implements Serializable {
	
	/** identifier field */
    private Long uid;
    
    /** non-nullable persistent field */
	private Long nbContentId;
	
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

	/** nullable persistent field */
	private boolean contentInUse;
	
	/** nullable persistent field */
	private Long creatorUserId;
	
	/** nullable persistent field */
	private Date dateCreated;
	
	/** nullable persistent field */
	private Date dateUpdated;
	
	/** persistent field */
	private Set nbSessions = new HashSet();
	
	/** default constructor */
	public NoticeboardContent()
	{
	}
	
	/** full constructor */
	public NoticeboardContent(Long nbContentId,
							  String title,
							  String content,
							  String onlineInstructions,
							  String offlineInstructions,
							  boolean defineLater,
							  boolean forceOffline,
							  boolean contentInUse,
							  Long creatorUserId,
							  Date dateCreated,
							  Date dateUpdated)
	{
		this.nbContentId = nbContentId;
		this.title = title;
		this.content = content;
		this.onlineInstructions = onlineInstructions;
		this.offlineInstructions = offlineInstructions;
		this.defineLater = defineLater;
		this.forceOffline = forceOffline;
		this.contentInUse = contentInUse;
		this.creatorUserId = creatorUserId;
		this.dateCreated = dateCreated;
		this.dateUpdated = dateUpdated;
	}
	
	/**
	 * Minimal Constructor used to initialise values for the NoticeboardContent object
	 * @return
	 */
	
	public NoticeboardContent(Long nbContentId,
	        				  String title,
	        				  String content,
	        				  String onlineInstructions,
	        				  String offlineInstructions,
	        				  Date dateCreated)
	{
	    this.nbContentId = nbContentId;
		this.title = title;
		this.content = content;
		this.onlineInstructions = onlineInstructions;
		this.offlineInstructions = offlineInstructions;
		this.defineLater = false;
		this.forceOffline = false;
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
     *      column="nb_content_id"
     *      length="20"
     *      not-null="true"
     */
	
	public Long getNbContentId() {
		return nbContentId;
	}
	
	public void setNbContentId(Long nbContentId) {
		this.nbContentId = nbContentId;
	}
	/**
	 * 		@hibernate.set
     *      lazy="true"
     *      inverse="true"
     *      cascade="all-delete-orphan"
     *     	@hibernate.collection-key
     *      column="nb_content_id"
     *     	@hibernate.collection-one-to-many
     *      class="org.lamsfoundation.lams.tool.noticeboard.NoticeboardSession"
     */
	public Set getNbSessions() {
		if (this.nbSessions == null)
		{
			setNbSessions(new HashSet());
		}
		return nbSessions;
	}
	
	public void setNbSessions(Set nbSessions) {
		this.nbSessions = nbSessions;
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
     *      length="255"
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
	 * 
	 * @param nb			NoticeboardContent object containing the content to copy from
	 * @param toContentId 	The new Id of the new noticeboard object
	 * @return newContent	The new noticeboard content object
	 */
	public static NoticeboardContent newInstance(NoticeboardContent nb, Long toContentId)
	{
		NoticeboardContent newContent = new NoticeboardContent(toContentId,
														nb.getTitle(),
														nb.getContent(),
														nb.getOnlineInstructions(),
														nb.getOfflineInstructions(),
														nb.isDefineLater(),
														nb.isForceOffline(),
														nb.isContentInUse(),
														nb.getCreatorUserId(),
														nb.getDateCreated(),
														nb.getDateUpdated());
		return newContent;
	}
   
	
 
}
