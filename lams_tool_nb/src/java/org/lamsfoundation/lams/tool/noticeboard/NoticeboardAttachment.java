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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $$Id$$ */
package org.lamsfoundation.lams.tool.noticeboard;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.contentrepository.service.IRepositoryService;


/**
 * <p>This class represents a file that is uploaded to the noticeboard tool.
 * It is used in the authoring environment, when an author
 * uploads an online/offline instructions file. The file is actually stored
 * in the content repository, however, the file details will be stored in the
 * tl_lanb11_attachment table. The file uploaded, will be of two types: online or offline.
 * </p>
 * @hibernate.class table="tl_lanb11_attachment"
 * @author mtruong
 */
public class NoticeboardAttachment implements Serializable, Cloneable {
    
	private static final long serialVersionUID = -3471513404550541296L;
	private static Logger log = Logger.getLogger(NoticeboardAttachment.class);

	/** identifier field */
    private Long attachmentId;
    
    /** persistent field. Cannot be null */
    private NoticeboardContent nbContent;
    
    /** persistent field. Cannot be null */
    private String filename;
    
    /** unique persistent field. Cannot be null */
    private Long uuid;
    
    /** nullable persistent field */
    private Long versionId;
    
    /** persistent field. Cannot be null. It can either take values "ONLINE" or "OFFLINE" */
    private boolean onlineFile;
    
    /** The two different types of files/attachment that can be uploaded */
    public final static String TYPE_ONLINE = "ONLINE";
    public final static String TYPE_OFFLINE = "OFFLINE";

    /**default constructor */
    public NoticeboardAttachment() {}
    
    /** minimal constructor */
    public NoticeboardAttachment(NoticeboardContent nbContent,
            					 String filename,
            					 boolean isOnline)
    {
        this.nbContent = nbContent;
        this.filename = filename;
        this.onlineFile = isOnline;
    }
    
    /**full constructor */
    public NoticeboardAttachment(NoticeboardContent nbContent,
								 String filename,
								 Long uuid,
								 Long versionId,
								 boolean isOnline)
    {
        this.nbContent = nbContent;
        this.filename = filename;
        this.uuid = uuid;
        this.versionId = versionId;
        this.onlineFile = isOnline;
    }
    
    /** Clone this attachment, including the unique id. This leaves two records pointing to the same item 
     * in the content repository.  */
    public Object clone(){
		Object obj = null;
		try {
			obj = super.clone();
		} catch (CloneNotSupportedException e) {
			log.error("Clone " + NoticeboardAttachment.class + " not supported");
		}
		return obj;
	}    

    /**
     * @hibernate.id 
     * 		generator-class="native" 
     * 		type="java.lang.Long" 
     * 		column="attachment_id"
     * 
     * @return Returns the attachmentId.
     */
    public Long getAttachmentId() {
        return attachmentId;
    }
    
    /**
     * @param attachmentId The attachmentId to set.
     */
    public void setAttachmentId(Long attachmentId) {
        this.attachmentId = attachmentId;
    }
    
    /**
     * @hibernate.property 
     * 		column="filename" 
     * 		length="255" 
     * 		not-null="true"
     * 
     * @return Returns the filename.
     */
    public String getFilename() {
        return filename;
    }
    /**
     * @param filename The filename to set.
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }
   
    /**
     * @hibernate.property 
     * 		column="online_file" 
     * 		length="1" 
     * 		not-null="true"
     * 
     * @return Returns the isOnline.
     */
    public boolean isOnlineFile() {
        return onlineFile;
    }
    /**
     * @param isOnline The isOnline to set.
     */
    public void setOnlineFile(boolean isOnline) {
        this.onlineFile = isOnline;
    }
   
    /**
     * @hibernate.many-to-one 
     * 		not-null="true" 
     * 
     * @hibernate.column 
     * 		name="nb_content_uid"
     * 
     * @return Returns the nbContent.
     */
    public NoticeboardContent getNbContent() {
        return nbContent;
    }
    /**
     * @param nbContent The nbContent to set.
     */
    public void setNbContent(NoticeboardContent nbContent) {
        this.nbContent = nbContent;
    }
   
    /**
     * @hibernate.property 
     * 		column="uuid" 
     * 		not-null="true" 
     * 		length="20"
     * 
     * @return Returns the uuid.
     */
    public Long getUuid() {
        return uuid;
    }
    /**
     * @param uuid The uuid to set.
     */
    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }
   
    /**
     * @hibernate.property 
     * 		column="version_id" 
     * 		length="20"
     * 
     * @return Returns the versionId.
     */
    public Long getVersionId() { //nullable
        return versionId;
    }
    /**
     * @param versionId The versionId to set.
     */
    public void setVersionId(Long versionId) {
        this.versionId = versionId;
    }
    
    public String getFileProperty()
    {
        if (isOnlineFile())
        {
            return TYPE_ONLINE;
        }
        else
            return TYPE_OFFLINE;
    }
    
    public void setFileProperty(String fileProperty) {
    	if(StringUtils.equals(IToolContentHandler.TYPE_ONLINE,fileProperty))
			this.onlineFile = true;
		else
			this.onlineFile = false;
    }
    
    public String returnKeyName()
    {
        return (getFilename() + "-" + getFileProperty());
    }
    
    /** Are two NoticeboardAttachments equal? Checks attachmentId, filename,
     * uuid, version id and online/offline status. Does not check the related
     * content object.
     */
    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof NoticeboardAttachment) ) return false;
        NoticeboardAttachment castOther = (NoticeboardAttachment) other;
        return new EqualsBuilder()
            .append(this.getAttachmentId(), castOther.getAttachmentId())
            .append(this.getFilename(), castOther.getFilename())
            .append(this.getUuid(), castOther.getUuid())
            .append(this.getVersionId(), castOther.getVersionId())
            .append(this.isOnlineFile(), castOther.isOnlineFile())
            .isEquals();
    }

    /** Generate the hashcode for the class. Based on the attachment id only. */
    public int hashCode() {
        return new HashCodeBuilder()
            .append(getAttachmentId())
            .toHashCode();
    }
}
