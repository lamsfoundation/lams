/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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
	
	private boolean generateLocalLink = false; 
	
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

		if ( serverURL != null ) {
			List themeList = CSSThemeUtil.getAllUserThemes();
			
			Iterator i = themeList.iterator();
			
			while (i.hasNext())
			{
				String theme = (String)i.next();
				if ( theme != null) {
					if (generateLocalLink)
						customStylesheetLink = generateLocalLink(theme,serverURL);
					else	
						customStylesheetLink = generateLink(theme,serverURL);
				}
		
				try {
		        	JspWriter writer = pageContext.getOut();
		    	   	if ( customStylesheetLink != null ) {
		    	   		writer.print(customStylesheetLink);
		    	   	}
			   		//writer.print(generateLink("default",serverURL));
				} catch ( IOException e ) {
					log.error("CssTag unable to write out CSS details due to IOException.", e);
					// don't throw a JSPException as we want the system to still function.
				}
			}
		} else {
	   		log.warn("CSSTag unable to write out CSS entries as the server url is missing from the configuration file.");
		}
    	return SKIP_BODY;
	}

	private String generateLocalLink(String stylesheetName, String serverURL) {
		return "<link href=\"../" + stylesheetName + ".css\" rel=\"stylesheet\" type=\"text/css\">";
	}
	
	private String generateLink(String stylesheetName, String serverURL)
	{
		if ( serverURL.endsWith("/") ) {
			return "<link href=\""+serverURL+"css/"+stylesheetName+".css\" rel=\"stylesheet\" type=\"text/css\">";
		} else {
			return "<link href=\""+serverURL+"/css/"+stylesheetName+".css\" rel=\"stylesheet\" type=\"text/css\">";
		}
	}

	public int doEndTag() {
		return EVAL_PAGE;
	}
	
	/**
	 * @jsp.attribute required="false" rtexprvalue="true" description="Should the css link be a local file link? Useful for export portfolio. Valid values true or false."
	 * 
	 * @return Returns the property.
	 */
	public String getLocalLink()
	{
		return generateLocalLink ? Boolean.TRUE.toString() : Boolean.FALSE.toString();
	}

	public void setLocalLink(String localLink)
	{
		if (Boolean.parseBoolean(localLink))
			this.generateLocalLink = true;
		else
			this.generateLocalLink = false;
	}
	
}
