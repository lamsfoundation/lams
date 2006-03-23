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
