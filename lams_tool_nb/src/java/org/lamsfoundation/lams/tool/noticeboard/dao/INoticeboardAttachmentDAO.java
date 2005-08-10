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
 * Created on Jul 29, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool.noticeboard.dao;

import java.util.List;

import org.lamsfoundation.lams.tool.noticeboard.NoticeboardAttachment;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;
/**
 * @author mtruong
 *
 * <p>Handles database access for uploading, updates and removal of files.</p>
 */
public interface INoticeboardAttachmentDAO {

    /**
     * Return the persistent instance of NoticeboardAttachment with the given
     * identifier <code>attachmentId</code>
     * 
     * @param attachmentId The unique identifier for attachment. If null, a null object will be returned.
     * @return an instance of NoticeboardAttachment
     */
    public NoticeboardAttachment retrieveAttachment(Long attachmentId);
    
    /**
     * Return the persistent instance of NoticeboardAttachment with the given
     * unique identifier <code>uuid</code>. 
     * 
     * @param uuid The uuid of the file, which is also the uuid of the file which is stored in the content repository.
     * @return an instance of NoticeboardAttachment
     */
    public NoticeboardAttachment retrieveAttachmentByUuid(Long uuid);
    
    /**
     * Return the persistent instance of NoticeboardAttachment with the given
     * <code>filename</code>. If there is more than one file with the same filename
     * then only the first file in the list will be returned.
     * 
     * @param filename The name of the file
     * @return an instance of NoticeboardAttachment
     */
    public NoticeboardAttachment retrieveAttachmentByFilename(String filename);
    
    /**
     * Returns a list of attachment ids which are associated with this
     * instance of NoticeboardContent. 
     * <p>For example, if the given instance <code>nbContent</code> has a tool content id
     * of 3, and consider an extract of the tl_lanb11_attachment table:</p>
     * <pre> 
     * 		 ----------------------------
     * 		 attachmentId | toolContentId
     * 		 ----------------------------
     * 			1		  | 	1
     * 			2		  | 	3
     * 			3		  | 	3
     * 			4 		  | 	1
     * 		 ----------------------------
     * </pre>
     * Then a call to <code>getAttachmentIdsFromContent</code> will return a list containing the values
     * 2 and 3. 
     * @param nbContent
     * @return
     */
    public List getAttachmentIdsFromContent(NoticeboardContent nbContent);
    
    /**
	 * <p>Persist the given persistent instance of NoticeboardAttachment.</p>
	 * 
	 * @param attachment The instance of NoticeboardAttachment to persist.
	 */
    public void saveAttachment(NoticeboardAttachment attachment);
    
    /**
	 * <p>Delete the given persistent instance of NoticeboardAttachment.</p>
	 * 
	 * @param attachment The instance of NoticeboardAttachment to delete.
	 */
    public void removeAttachment(NoticeboardAttachment attachment);
    
    /**
     * <p>Delete the given instance of NoticeboardAttachment with the
     * given <code>uuid</code>
     * 
     * @param uuid The unique id of the file/attachment.
     */
    public void removeAttachment(Long uuid);
    
}
