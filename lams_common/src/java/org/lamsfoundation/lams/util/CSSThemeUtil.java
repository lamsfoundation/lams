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

package org.lamsfoundation.lams.util;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.lamsfoundation.lams.themes.dto.ThemeDTO;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

public class CSSThemeUtil {

    public static String DEFAULT_HTML_THEME = "defaultHTML";

    /**
     * Will return a list of stylesheets for the current user. If the user does not have a specific stylesheet, then the
     * default stylesheet will be included in this list. The different skins replace the default stylesheet, they do 
     * not build on top of the stylesheet. This is a change from earlier version of LAMS.
     *
     * @return
     */
    public static List<String> getAllUserThemes() {
	List<String> themeList = new ArrayList<String>();

	HttpSession ss = SessionManager.getSession();
	if (ss != null) {
	    UserDTO user = null;
	    try {
		user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    } catch (IllegalStateException e) {
		// session was invalidated, normal situation so do not show any error
	    }
	    if (user != null) {
		ThemeDTO theme = user.getTheme();

		if (theme != null) {
		    String themeName = theme.getName();
		    if (themeName != null) {
			themeList.add(theme.getName());
		    }
		}
	    }
	}

	// if we haven't got a user theme, we are probably on the login page
	// so we'd better include the default server theme (if it isn't the LAMS default theme
	if (themeList.size() == 0) {
	    String serverDefaultTheme = Configuration.get(ConfigurationKeys.DEFAULT_THEME);
	    if (serverDefaultTheme != null) {
		themeList.add(serverDefaultTheme);
	    }
	}

	// Still no theme? Default to default. 
	if (themeList.size() == 0) {
	    themeList.add(CSSThemeUtil.DEFAULT_HTML_THEME);
	}
	
	return themeList;
    }

    public static ThemeDTO getUserTheme() {
	ThemeDTO theme = null;

	HttpSession ss = SessionManager.getSession();
	if (ss != null) {
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    if (user != null) {
		theme = user.getTheme();
	    }
	}

	return theme;

    }

    // Is this theme the LAMS basic theme, which must always be included on a web page?
    // This is NOT the server default theme - which may be a custom theme.
    public static boolean isLAMSDefaultTheme(String themeName) {
	return themeName.equals(CSSThemeUtil.DEFAULT_HTML_THEME);
    }
}
