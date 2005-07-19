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

package org.lamsfoundation.lams.tool.noticeboard.util;

import javax.servlet.http.HttpServletRequest;

import org.lamsfoundation.lams.tool.noticeboard.NbApplicationException;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardConstants;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;

/**
 * @author mtruong
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class NbMonitoringUtil {

    public static void cleanSession(HttpServletRequest request)
    {
        request.getSession().removeAttribute(NoticeboardConstants.TOOL_CONTENT_ID_INMONITORMODE);
        request.getSession().removeAttribute(NoticeboardConstants.TITLE);
        request.getSession().removeAttribute(NoticeboardConstants.CONTENT);
        request.getSession().removeAttribute(NoticeboardConstants.OFFLINE_INSTRUCTIONS);
        request.getSession().removeAttribute(NoticeboardConstants.ONLINE_INSTRUCTIONS);
        request.getSession().removeAttribute(NoticeboardConstants.CONTENT_IN_USE);
    }
    
    /**
     * <p> This method checks the two tool content flags, defineLater and contentInUse
     * to determine whether the tool content is modifiable or not. Returns true is content is
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
    
   	public static void copyValuesIntoSession(HttpServletRequest request, NoticeboardContent content)
    {
        request.getSession().setAttribute(NoticeboardConstants.TITLE, content.getTitle());
        request.getSession().setAttribute(NoticeboardConstants.CONTENT, content.getContent());
        request.getSession().setAttribute(NoticeboardConstants.ONLINE_INSTRUCTIONS, content.getOnlineInstructions());
        request.getSession().setAttribute(NoticeboardConstants.OFFLINE_INSTRUCTIONS, content.getOfflineInstructions());
    }
}
