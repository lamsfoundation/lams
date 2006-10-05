/***************************************************************************
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
 * ***********************************************************************/
/* $$Id$$ */
package org.lamsfoundation.lams.web.tag;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.util.CSSThemeUtil;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;

/**
 * Output the required css based in the user's details. Will output one or more
 * lines. If the user has a theme then exports the default and then the user's
 * style sheet. If the user doesn't have a theme then it just exports
 * the default. This way, if the user's entry points to a stylesheet that
 * doesn't exist, the default one is always available.  
 * 
 * @jsp.tag name="css"
 * 			body-content="empty"
 * 			display-name="User's chosen stylesheet"
 * 			description="Output stylesheet based on the user preferences."
 * 	
 * @author Fiona Malikoff
 */
public class CssTag extends TagSupport {

	private static final Logger log = Logger.getLogger(CssTag.class);
	private static final String IE_STYLESHEET_NAME = "ie-styles";
	private String localLinkPath = null; 
	private String style = null; 
	
	private static final String LEARNER_STYLE = "learner"; // expandable
	private static final String TABBED_STYLE = "tabbed"; // fixed width
	/**
	 * 
	 */
	public CssTag() {
		super();
	}
	
	public int doStartTag() throws JspException {
		
		String customStylesheetLink = null;
		String serverURL = Configuration.get(ConfigurationKeys.SERVER_URL);
		serverURL = ( serverURL != null ? serverURL.trim() : null);

		try {

        	JspWriter writer = pageContext.getOut();
			if ( serverURL != null ) {
				List themeList = CSSThemeUtil.getAllUserThemes();
				
				Iterator i = themeList.iterator();
				
				while (i.hasNext())
				{
					String theme = (String)i.next();
					if ( theme != null) {
						theme = appendStyle(theme);
						if (localLinkPath != null)
							customStylesheetLink = generateLocalLink(theme);
						else	
							customStylesheetLink = generateLink(theme,serverURL);
					}
					
		    	   	if ( customStylesheetLink != null ) {
		    	   		writer.println(customStylesheetLink);
		    	   	}
				}
	
			} else {
		   		log.warn("CSSTag unable to write out CSS entries as the server url is missing from the configuration file.");
			}
			
			// Special IE stylesheet for all those IE related formatting issues
			String ieLink = localLinkPath != null ? generateLocalURL(IE_STYLESHEET_NAME) : generateURL(IE_STYLESHEET_NAME,serverURL);
			writer.println("<!--[if IE]>");
			writer.println("<style type=\"text/css\">");
			writer.println("@import url ("+ieLink+");");
			writer.println("</style>");
			writer.println("<![endif]-->");

		} catch ( IOException e ) {
			log.error("CssTag unable to write out CSS details due to IOException.", e);
			// don't throw a JSPException as we want the system to still function.
		}
    	return SKIP_BODY;
	}

	private String generateLocalLink(String stylesheetName) {
		if ( localLinkPath.endsWith("/") ) {
			return "<link href=\""+localLinkPath+"css/"+stylesheetName + ".css\" rel=\"stylesheet\" type=\"text/css\">";
		} else { 
			return "<link href=\""+localLinkPath+"/css/"+stylesheetName + ".css\" rel=\"stylesheet\" type=\"text/css\">";
		}
	}

	private String appendStyle(String stylesheetName) {
		String ssName = stylesheetName;
		if ( ssName != null && ( getStyle() == null || getStyle().equals(LEARNER_STYLE)) ) {
				ssName = ssName + "_" + "learner";
		}
		return ssName;
	}
	
	private String generateLocalURL(String stylesheetName) {
		if ( localLinkPath.endsWith("/") ) {
			return "\""+localLinkPath+"css/"+stylesheetName + ".css\"";
		} else { 
			return "\""+localLinkPath+"/css/"+stylesheetName + ".css\"";
		}
	}

	private String generateLink(String stylesheetName, String serverURL)
	{
		if ( serverURL.endsWith("/") ) {
			return "<link href=\""+serverURL+"css/"+stylesheetName+".css\" rel=\"stylesheet\" type=\"text/css\">";
		} else {
			return "<link href=\""+serverURL+"/css/"+stylesheetName+".css\" rel=\"stylesheet\" type=\"text/css\">";
		}
	}

	private String generateURL(String stylesheetName, String serverURL)
	{
		if ( serverURL.endsWith("/") ) {
			return "\""+serverURL+"css/"+stylesheetName+".css\"";
		} else {
			return "\""+serverURL+"/css/"+stylesheetName+".css\"";
		}
	}

	public int doEndTag() {
		return EVAL_PAGE;
	}
	
	/**
	 * @jsp.attribute required="false" rtexprvalue="true" 
	 * description="Should the css link be a local file link? If so, what is the path back to the 'root'. For tools' export portfolio pages, this should be set to \"../\""
	 * 
	 * @return Returns the property.
	 */
	public String getLocalLinkPath()
	{
		return localLinkPath;
	}

	public void setLocalLinkPath(String localLinkPath)
	{
		this.localLinkPath = localLinkPath;
	}

	/**
	 * @jsp.attribute required="false" rtexprvalue="true" 
	 * description="Learner pages use learner, fancy pages such as index page use core"
	 * 
	 * Sets whether to use blah.css (style="core") or blah_learner.css (style=learner). 
	 * If this parameter is left blank, you get blah_learner.css e.g. default_learner.css
	 * @return Returns style.
	 */
	public String getStyle()
	{
		return style;
	}

	public void setStyle(String style)
	{
		this.style = style;
	}



}
