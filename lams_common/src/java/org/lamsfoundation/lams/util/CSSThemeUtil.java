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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.util;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.themes.CSSThemeVisualElement;
import org.lamsfoundation.lams.themes.dto.CSSThemeBriefDTO;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

public class CSSThemeUtil {
	
	private static Logger log = Logger.getLogger(CSSThemeUtil.class);
	
	/**
	 * Will return a list of stylesheets for the current user.
	 * If the user does not have a specific stylesheet, then 
	 * the default stylesheet will be included in this list.
	 * The default stylesheet will always be included in this list.
	 * @return
	 */
	public static List getAllUserThemes()
	{
		CSSThemeBriefDTO theme = null;

		List themeList = new ArrayList();
		
	   	HttpSession ss = SessionManager.getSession();
	   	if ( ss != null ) {
	   		UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	   		if ( user != null ) {
	   			theme = user.getHtmlTheme();
	   			
	   			if (theme != null & theme.getName() != null)
	   				themeList.add(theme.getName());
	   		}
	   		
	   	}
	 
	   	themeList.add("default");
	   	
	   	return themeList;
	}
	
	public static CSSThemeBriefDTO getUserTheme()
	{
		CSSThemeBriefDTO theme = null;
		
	   	HttpSession ss = SessionManager.getSession();
	   	if ( ss != null ) {
	   		UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	   		if ( user != null ) {
	   			theme = user.getHtmlTheme();
	   		} 
	   	}
	   	
	   	return theme;
	   	
	}
}
