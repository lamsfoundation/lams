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
package org.lams.lams.tool.wiki.dao;

import java.util.List;

import org.lams.lams.tool.wiki.WikiAttachment;
import org.lams.lams.tool.wiki.WikiContent;
/**
 * @author mtruong
 *
 * <p>Handles database access for uploading, updates and removal of files.</p>
 */
public interface IWikiAttachmentDAO {

    /**
     * Return the persistent instance of WikiAttachment with the given
     * identifier <code>attachmentId</code>
     * 
     * @param attachmentId The unique identifier for attachment. If null, a null object will be returned.
     * @return an instance of WikiAttachment
     */
    public WikiAttachment retrieveAttachment(Long attachmentId);
    
    /**
     * Return the persistent instance of WikiAttachment with the given
     * unique identifier <code>uuid</code>. 
     * 
     * @param uuid The uuid of the file, which is also the uuid of the file which is stored in the content repository.
     * @return an instance of WikiAttachment
     */
    public WikiAttachment retrieveAttachmentByUuid(Long uuid);
    
    /**
     * Return the persistent instance of WikiAttachment with the given
     * <code>filename</code>. If there is more than one file with the same filename
     * then only the first file in the list will be returned.
     * 
     * @param filename The name of the file
     * @return an instance of WikiAttachment
     */
    public WikiAttachment retrieveAttachmentByFilename(String filename);
    
    /**
     * Returns a list of attachment ids which are associated with this
     * instance of WikiContent. 
     * <p>For example, if the given instance <code>wikiContent</code> has a tool content id
     * of 3, and consider an extract of the tl_lawiki10_attachment table:</p>
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
     * @param wikiContent
     * @return
     */
    public List getAttachmentIdsFromContent(WikiContent wikiContent);
    
    /**
	 * <p>Persist the given persistent instance of WikiAttachment.</p>
	 * 
	 * @param attachment The instance of WikiAttachment to persist.
	 */
    public void saveAttachment(WikiAttachment attachment);
    
    /**
	 * <p>Delete the given persistent instance of WikiAttachment.</p>
	 * 
	 * @param attachment The instance of WikiAttachment to delete.
	 */
    public void removeAttachment(WikiAttachment attachment);
    
    /**
     * <p>Delete the given instance of WikiAttachment with the
     * given <code>uuid</code>
     * 
     * @param uuid The unique id of the file/attachment.
     */
    public void removeAttachment(Long uuid);
    
}
