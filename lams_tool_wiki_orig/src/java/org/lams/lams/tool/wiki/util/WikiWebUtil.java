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
package org.lams.lams.tool.wiki.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.lams.lams.tool.wiki.WikiApplicationException;
import org.lams.lams.tool.wiki.WikiAttachment;
import org.lams.lams.tool.wiki.WikiConstants;
import org.lams.lams.tool.wiki.WikiContent;
import org.lams.lams.tool.wiki.service.IWikiService;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 * This Web Utility class contains helper methods used in the Action Servlets
 * 
 * @author mtruong
 *
 */
public class WikiWebUtil {

    private WikiWebUtil() {}
    
     	public static Long convertToLong(String id)
	 	{
	 	    try
	 	    {
	 	        return new Long(id);
	 	    }
	 	    catch (NumberFormatException e)
	 	    {
	 	        return null;
	 	    }
	 		
	 	}
	 	
	     
	     /**
	     * <p>This method is used in authoring and monitoring to display the list of files that have been uploaded.
	     * Contents of the collections are WikiAttachments. The current files are included in the attachmentList, 
	     * files that the user has nominated to delete are in the deletedAttachementList.</p>
	     * 
	     * <p>If the input collections are null, then the session variables are not modified. This
	     * is particularly useful for the deleted files.</p>
	     * 
	     * @param request the HttpServletRequest which is used to obtain the HttpSession
	     * @param attachmentList
	     * @param deletedAttachmentList
	     */
	    public static SessionMap addUploadsToSession(SessionMap sessionMap, HttpServletRequest request, List attachmentList, List deletedAttachmentList)
	    {
	    	SessionMap map = sessionMap != null ? sessionMap : new SessionMap();
			map.put(WikiConstants.ATTACHMENT_LIST, attachmentList != null ? attachmentList : new ArrayList());
			map.put(WikiConstants.DELETED_ATTACHMENT_LIST, deletedAttachmentList != null ? deletedAttachmentList : new ArrayList());
			request.getSession().setAttribute(map.getSessionID(), map);
			return map;
	    }
	    
		/** Setup the map containing the files that have been uploaded for this particular tool content id.
		 * If WikiContent content does not exist, set wiki=null and an empty list will be created.
		 *
		 * @param wikiService
		 * @param wiki
		 * @return the attachmentList
		 */
	    public static List setupAttachmentList(IWikiService wikiService, WikiContent wiki) {

			List attachmentList = new ArrayList();

			if ( wikiService!=null && wiki!=null ) {
				List attachmentIdList = wikiService.getAttachmentIdsFromContent(wiki);
				for (int i=0; i<attachmentIdList.size(); i++)
				{
				    WikiAttachment file = wikiService.retrieveAttachment((Long)attachmentIdList.get(i));
				    attachmentList.add(file);
				}
			} 
			return attachmentList;
		}

		/** 
		 * Setup an empty deleted attachment map
		 * @return the new attachmentMap
		 */
	    public static List setupDeletedAttachmentList() {
			return new ArrayList();
		}


}
