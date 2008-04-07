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
package org.lamsfoundation.lams.tool.wiki.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.wiki.persistence.Wiki;
import org.lamsfoundation.lams.tool.wiki.persistence.WikiException;
import org.lamsfoundation.lams.web.util.AttributeNames;


/**
 * Contains helper methods used by the Action Servlets
 * 
 * @author Anthony Sukkar
 *
 */
public class WikiWebUtils {

	public static boolean isWikiEditable(Wiki wiki) {
        if ( (wiki.isDefineLater() == true) && (wiki.isContentInUse()==true) )
        {
            throw new WikiException("An exception has occurred: There is a bug in this tool, conflicting flags are set");
                    //return false;
        }
        else if ( (wiki.isDefineLater() == true) && (wiki.isContentInUse() == false))
            return true;
        else if ( (wiki.isDefineLater() == false) && (wiki.isContentInUse() == false))
            return true;
        else //  (content.isContentInUse()==true && content.isDefineLater() == false)
            return false;
	}

	/**
	 * Get ToolAccessMode from HttpRequest parameters. Default value is AUTHOR mode.
	 * @param request
	 * @return
	 */
	public static ToolAccessMode getAccessMode(HttpServletRequest request) {
		ToolAccessMode mode = null;
		String modeStr = request.getParameter(AttributeNames.ATTR_MODE);
		if(StringUtils.equalsIgnoreCase(modeStr,ToolAccessMode.TEACHER.toString()))
			mode = ToolAccessMode.TEACHER;
		else if (StringUtils.equalsIgnoreCase(modeStr,ToolAccessMode.AUTHOR.toString()))
			mode = ToolAccessMode.AUTHOR;
		else if (StringUtils.equalsIgnoreCase(modeStr,ToolAccessMode.LEARNER.toString()))
			mode = ToolAccessMode.LEARNER;
		
		return mode;
	}
	
}
