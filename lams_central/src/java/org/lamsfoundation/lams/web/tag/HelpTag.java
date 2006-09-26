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
import java.lang.NullPointerException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import org.lamsfoundation.lams.authoring.web.AuthoringConstants;
import org.lamsfoundation.lams.tool.service.*;
import org.lamsfoundation.lams.tool.IToolVO;
import org.lamsfoundation.lams.web.filter.LocaleFilter;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Generates a help link to a contextualised tool help page.
 * 
 * @jsp.tag name="help"
 * 			body-content="empty"
 * 			display-name="Help tag"
 * 			description="Help tag"
 * 	
 * @author Fiona Malikoff
 */
public class HelpTag extends TagSupport {

	private static final Logger log = Logger.getLogger(HelpTag.class);
	private String module = null;
	private String toolSignature = null;
	
	/**
	 * 
	 */
	public HelpTag() {
		super();
	}
	
	public int doStartTag() throws JspException {
		try {
			HttpSession session = ((HttpServletRequest) this.pageContext.getRequest()).getSession();

		    String language = null;
		    String country = null;
		    String helpURL = null;
		    String fullURL = null;
		    String toolSig = getToolSignature();
		    
        	JspWriter writer = pageContext.getOut();
        	
        	try {
	        	// retrieve help URL for tool
	        	ILamsToolService toolService = (ILamsToolService) getContext().getBean(AuthoringConstants.TOOL_SERVICE_BEAN_NAME);
				IToolVO tool = toolService.getToolBySignature(toolSig);
				
				helpURL = tool.getHelpUrl();
				
				if(helpURL == null)
					return SKIP_BODY;
				
				// construct link
				
				Locale locale = (Locale) session.getAttribute(LocaleFilter.PREFERRED_LOCALE_KEY);
			    if ( locale != null ) {
			    	language = locale.getLanguage();
			    	country = locale.getCountry();
			    }
				
			    fullURL = helpURL + module + "#" + toolSig + module + "-" + language + country;
				writer.println("<div class='right-buttons'>");
			    writer.println("<img src=\"" + Configuration.get(ConfigurationKeys.SERVER_URL) + "images/help.png\" border=\"0\" width=\"18\" height=\"18\" onclick=\"window.open('" + fullURL + "', 'help')\"/>");
			    writer.println("</div>");
			    
        	} catch (NullPointerException npe) {
    			log.error("HelpTag unable to write out due to NullPointerException. Most likely your Tool Signature was incorrect.", npe);
    			// don't throw a JSPException as we want the system to still function.
    
    			writer.println("<div class='right-buttons'>");
    			writer.println("<img src=\"" + Configuration.get(ConfigurationKeys.SERVER_URL) + "images/css/warning.gif\" border=\"0\" width=\"20\" height=\"20\"/>");
    			writer.println("</div>");
    			
    		}
        	
		} catch (IOException e) {
			log.error("HelpTag unable to write out due to IOException.", e);
			// don't throw a JSPException as we want the system to still function.
		}
    	return SKIP_BODY;
	}

	public int doEndTag() {
		return EVAL_PAGE;
	}
	/**
	 * @return module
	 * 
	 * @jsp.attribute required="true"
	 * 				  rtexprvalue="true"
	 * 				  description="Module Name"
	 */
	public String getModule() {
		return module;
	}
	
	/**
	 * 
	 * @param module
	 */
	public void setModule(String module) {
		this.module = module;
	}
	
	private WebApplicationContext getContext() {
		WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext( pageContext.getServletContext());
		return ctx;
	}
	
	/**
	 * @return
	 * 
	  * @jsp.attribute required="true"
	 * 				   rtexprvalue="true"
	 * 				   description="Tool Signature"
	 */
	public String getToolSignature() {
		return toolSignature;
	}
	
	/**
	 * 
	 * @param toolSignature
	 */
	public void setToolSignature(String toolSignature) {
		this.toolSignature = toolSignature;
	}

}
