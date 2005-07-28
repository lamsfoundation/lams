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
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool.noticeboard;

import java.io.Serializable;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;


/**
 * @author mtruong
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class NoticeboardAttachment implements Serializable {
    
    private Long attachmentId;
    private NoticeboardContent nbContent;
    private String filename;
    private Long uuid;
    private Long versionId;
    private boolean isOnline;

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
    public boolean isOnline() {
        return isOnline;
    }
    /**
     * @param isOnline The isOnline to set.
     */
    public void setOnline(boolean isOnline) {
        this.isOnline = isOnline;
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
    public Long getVersionId() {
        return versionId;
    }
    /**
     * @param versionId The versionId to set.
     */
    public void setVersionId(Long versionId) {
        this.versionId = versionId;
    }
}
