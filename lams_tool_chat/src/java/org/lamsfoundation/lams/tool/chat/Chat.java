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

/* $Id$ */ 
package org.lamsfoundation.lams.tool.chat;

import java.util.Date;
import java.util.Set;

import org.hibernate.criterion.Example;

/**  
 *        @hibernate.class
 *         table="tl_lachat11_chat"
 */

public class Chat implements java.io.Serializable {


    // Fields    

     /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
     private Long uid;
     private Date createDate;
     private Date updateDate;
     private Long createBy;
     private String title;
     private String instructions;
     private Boolean runOffline;
     private Boolean lockOnFinished;
     private String onlineInstructions;
     private String offlineInstructions;
     private Boolean contentInUse;
     private Boolean defineLater;
     private Long toolContentId;
     private Set chatAttachments;
     private Set chatSessions;


    // Constructors

    /** default constructor */
    public Chat() {
    }

    
    /** full constructor */
    public Chat(Date createDate, Date updateDate, Long createBy, String title, String instructions, Boolean runOffline, Boolean lockOnFinished, String onlineInstructions, String offlineInstructions, Boolean contentInUse, Boolean defineLater, Long toolContentId, Set chatAttachments, Set chatSessions) {
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.createBy = createBy;
        this.title = title;
        this.instructions = instructions;
        this.runOffline = runOffline;
        this.lockOnFinished = lockOnFinished;
        this.onlineInstructions = onlineInstructions;
        this.offlineInstructions = offlineInstructions;
        this.contentInUse = contentInUse;
        this.defineLater = defineLater;
        this.toolContentId = toolContentId;
        this.chatAttachments = chatAttachments;
        this.chatSessions = chatSessions;
    }

   
    // Property accessors
    /**       
     *            @hibernate.id
     *             generator-class="native"
     *             type="java.lang.Long"
     *             column="uid"
     *         
     */

    public Long getUid() {
        return this.uid;
    }
    
    public void setUid(Long uid) {
        this.uid = uid;
    }
    /**       
     *            @hibernate.property
     *             column="create_date"
     *         
     */

    public Date getCreateDate() {
        return this.createDate;
    }
    
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    /**       
     *            @hibernate.property
     *             column="update_date"
     *         
     */

    public Date getUpdateDate() {
        return this.updateDate;
    }
    
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    /**       
     *            @hibernate.property
     *             column="create_by"
     *             length="20"
     *         
     */

    public Long getCreateBy() {
        return this.createBy;
    }
    
    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }
    /**       
     *            @hibernate.property
     *             column="title"
     *             length="255"
     *         
     */

    public String getTitle() {
        return this.title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    /**       
     *            @hibernate.property
     *             column="instructions"
     *             length="65535"
     *         
     */

    public String getInstructions() {
        return this.instructions;
    }
    
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
    /**       
     *            @hibernate.property
     *             column="run_offline"
     *             length="1"
     *         
     */

    public Boolean getRunOffline() {
        return this.runOffline;
    }
    
    public void setRunOffline(Boolean runOffline) {
        this.runOffline = runOffline;
    }
    /**       
     *            @hibernate.property
     *             column="lock_on_finished"
     *             length="1"
     *         
     */

    public Boolean getLockOnFinished() {
        return this.lockOnFinished;
    }
    
    public void setLockOnFinished(Boolean lockOnFinished) {
        this.lockOnFinished = lockOnFinished;
    }
    /**       
     *            @hibernate.property
     *             column="online_instructions"
     *             length="65535"
     *         
     */

    public String getOnlineInstructions() {
        return this.onlineInstructions;
    }
    
    public void setOnlineInstructions(String onlineInstructions) {
        this.onlineInstructions = onlineInstructions;
    }
    /**       
     *            @hibernate.property
     *             column="offline_instructions"
     *             length="65535"
     *         
     */

    public String getOfflineInstructions() {
        return this.offlineInstructions;
    }
    
    public void setOfflineInstructions(String offlineInstructions) {
        this.offlineInstructions = offlineInstructions;
    }
    /**       
     *            @hibernate.property
     *             column="content_in_use"
     *             length="1"
     *         
     */

    public Boolean getContentInUse() {
        return this.contentInUse;
    }
    
    public void setContentInUse(Boolean contentInUse) {
        this.contentInUse = contentInUse;
    }
    /**       
     *            @hibernate.property
     *             column="define_later"
     *             length="1"
     *         
     */

    public Boolean getDefineLater() {
        return this.defineLater;
    }
    
    public void setDefineLater(Boolean defineLater) {
        this.defineLater = defineLater;
    }
    /**       
     *            @hibernate.property
     *             column="tool_content_id"
     *             length="20"
     *         
     */

    public Long getToolContentId() {
        return this.toolContentId;
    }
    
    public void setToolContentId(Long toolContentId) {
        this.toolContentId = toolContentId;
    }
    /**       
     *            @hibernate.set
     *             lazy="false"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="chat_uid"
     *            @hibernate.collection-one-to-many
     *             class="org.lamsfoundation.lams.tool.chat.ChatAttachment"
     *         
     */

    public Set getChatAttachments() {
        return this.chatAttachments;
    }
    
    public void setChatAttachments(Set chatAttachments) {
        this.chatAttachments = chatAttachments;
    }
    /**       
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="chat_uid"
     *            @hibernate.collection-one-to-many
     *             class="org.lamsfoundation.lams.tool.chat.ChatSession"
     *         
     */

    public Set getChatSessions() {
        return this.chatSessions;
    }
    
    public void setChatSessions(Set chatSessions) {
        this.chatSessions = chatSessions;
    }
   

    /**
     * toString
     * @return String
     */
     public String toString() {
	  StringBuffer buffer = new StringBuffer();

      buffer.append(getClass().getName()).append("@").append(Integer.toHexString(hashCode())).append(" [");
      buffer.append("title").append("='").append(getTitle()).append("' ");			
      buffer.append("instructions").append("='").append(getInstructions()).append("' ");			
      buffer.append("toolContentId").append("='").append(getToolContentId()).append("' ");			
      buffer.append("]");
      
      return buffer.toString();
     }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof Chat) ) return false;
		 Chat castOther = ( Chat ) other; 
         
		 return ( (this.getUid()==castOther.getUid()) || ( this.getUid()!=null && castOther.getUid()!=null && this.getUid().equals(castOther.getUid()) ) );
   }
   
   public int hashCode() {
         int result = 17;
         result = 37 * result + ( getUid() == null ? 0 : this.getUid().hashCode() );
         return result;
   }   





}