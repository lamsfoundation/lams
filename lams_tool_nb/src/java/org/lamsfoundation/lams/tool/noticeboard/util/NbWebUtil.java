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
 * Created on Jul 20, 2005
 *
 */
package org.lamsfoundation.lams.tool.noticeboard.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.lamsfoundation.lams.tool.noticeboard.NbApplicationException;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardConstants;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;

/**
 * This Web Utility class contains helper methods used in the Action Servlets
 * 
 * @author mtruong
 *
 */
public class NbWebUtil {

    private NbWebUtil() {}
    
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
	 	
	 	public static void cleanAuthoringSession(HttpServletRequest request)
	 	{
	 	    request.getSession().removeAttribute(NoticeboardConstants.TOOL_CONTENT_ID);
	 	    request.getSession().removeAttribute(NoticeboardConstants.ATTACHMENT_LIST);
	 	}
	 	
	 	public static void cleanLearnerSession(HttpServletRequest request)
	 	{
	 	       request.getSession().removeAttribute(NoticeboardConstants.READ_ONLY_MODE);
	 	      //  request.getSession().removeAttribute(NoticeboardConstants.IS_TOOL_COMPLETED);
	 	}
	 	
	 	 public static void cleanMonitoringSession(HttpServletRequest request)
	     {
	         //request.getSession().removeAttribute(NoticeboardConstants.TOOL_CONTENT_ID_INMONITORMODE);
	         request.getSession().removeAttribute(NoticeboardConstants.TITLE);
	         request.getSession().removeAttribute(NoticeboardConstants.CONTENT);
	         request.getSession().removeAttribute(NoticeboardConstants.OFFLINE_INSTRUCTIONS);
	         request.getSession().removeAttribute(NoticeboardConstants.ONLINE_INSTRUCTIONS);
	         request.getSession().removeAttribute(NoticeboardConstants.ATTACHMENT_LIST);
	      
	     }
	     
	     /**
	      * <p> This method checks the two tool content flags, defineLater and contentInUse
	      * to determine whether the tool content is modifiable or not. Returns true if the content is
	      * modifiable and false otherwise 
	      * <br>Tool content is modifiable if:
	      * <li>defineLater is set to true</li>
	      * <li>defineLater is set to false and contentInUse is set to false</li>
	      * <br>Tool content is not modifiable if:
	      * <li>contentInUse is set to true</li></p>
	      * @param content The instance of NoticeboardContent to check
	      * @return true if content is modifiable and false otherwise
	      * @throws NbApplicationException
	      */
	     public static boolean isContentEditable(NoticeboardContent content) throws NbApplicationException
	     {
	         if ( (content.isDefineLater() == true) && (content.isContentInUse()==true) )
	         {
	             throw new NbApplicationException("An exception has occurred: There is a bug in this tool, conflicting flags are set");
	                     //return false;
	         }
	         else if ( (content.isDefineLater() == true) && (content.isContentInUse() == false))
	             return true;
	         else if ( (content.isDefineLater() == false) && (content.isContentInUse() == false))
	             return true;
	         else //  (content.isContentInUse()==true && content.isDefineLater() == false)
	             return false;
	     }
	    
	    /**
	     * Used in the monitoring environment. 
	     * Copies the values of the pojo/bean properties title, content, onlineInstructions and offlineInstructions
	     * into the session scope variables title, content, onlineInstructions and offlineInstructions respectively.
	     * @param request the HttpServletRequest which is used to obtain the HttpSession, in which is used to store the session variables for the 4 properties.
	     * @param content the bean in which to copy the 4 properties from
	     */ 
	    public static void copyValuesIntoSession(HttpServletRequest request, NoticeboardContent content)
	    {
	         request.getSession().setAttribute(NoticeboardConstants.TITLE, content.getTitle());
	         request.getSession().setAttribute(NoticeboardConstants.CONTENT, content.getContent());
	         request.getSession().setAttribute(NoticeboardConstants.ONLINE_INSTRUCTIONS, content.getOnlineInstructions());
	         request.getSession().setAttribute(NoticeboardConstants.OFFLINE_INSTRUCTIONS, content.getOfflineInstructions());
	    }
	    
	    /**
	     * <p>This method takes in a Map as a parameter and converts it
	     * into a Collections view of the values of this map. A new 
	     * ArrayList is formed from this Collection of values and stored in
	     * the session scope variable <code>attachmentList</code> (represented by NoticeboardConstants.ATTACHMENT_LIST) </p>
	     * 
	     * <p>This method is used in authoring and monitoring to display the list of files that have been uploaded.
	     * They key for this map is the filename-fileType where fileType is either "online" or "offline"
	     * and the value that maps to this key, is an NoticeboardAttachment object.</p>
	     * 
	     * @param request the HttpServletRequest which is used to obtain the HttpSession
	     * @param map
	     */
	    public static void addUploadsToSession(HttpServletRequest request, Map map)
	    {
	        Collection entries = map.values();
	        List attachmentList = new ArrayList(entries);
	        request.getSession().setAttribute(NoticeboardConstants.ATTACHMENT_LIST, attachmentList);
	    }
	    
	    
}
