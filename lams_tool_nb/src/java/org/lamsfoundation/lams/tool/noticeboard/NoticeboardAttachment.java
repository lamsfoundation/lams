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

/*
 * Created on Jul 28, 2005
 */
package org.lamsfoundation.lams.tool.noticeboard;

import java.io.Serializable;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;


/**
 * <p>This class represents a file that is uploaded to the noticeboard tool.
 * It is used in the authoring environment, when an author
 * uploads an online/offline instructions file. The file is actually stored
 * in the content repository, however, the file details will be stored in the
 * tl_lanb11_attachment table. The file uploaded, will be of two types: online or offline.
 * </p>
 * 
 * @author mtruong
 */
public class NoticeboardAttachment implements Serializable {
    
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
    
    
    
    /**
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
    
    public String returnFileType()
    {
        if (isOnlineFile())
        {
            return TYPE_ONLINE;
        }
        else
            return TYPE_OFFLINE;
    }
    
    public String returnKeyName()
    {
        return (getFilename() + "-" + returnFileType());
    }
}
