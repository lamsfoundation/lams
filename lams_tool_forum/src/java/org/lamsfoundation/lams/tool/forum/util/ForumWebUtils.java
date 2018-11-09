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


package org.lamsfoundation.lams.tool.forum.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.forum.model.Forum;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * Contains helper methods used by the Action Servlets
 *
 * @author Anthony Sukkar
 *
 */
public class ForumWebUtils {

    public static boolean isForumEditable(Forum forum) {
	if ((forum.isDefineLater() == true) && (forum.isContentInUse() == true)) {
	    throw new ForumException(
		    "An exception has occurred: There is a bug in this tool, conflicting flags are set");
	    //return false;
	} else if ((forum.isDefineLater() == true) && (forum.isContentInUse() == false)) {
	    return true;
	} else if ((forum.isDefineLater() == false) && (forum.isContentInUse() == false)) {
	    return true;
	} else {
	    return false;
	}
    }

}
