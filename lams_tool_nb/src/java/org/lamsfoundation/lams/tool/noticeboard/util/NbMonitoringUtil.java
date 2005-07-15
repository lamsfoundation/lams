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
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardConstants;

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
}
