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

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Output the required css based in the user's details. Will output one or more
 * lines. If the user has a theme then exports the default and then the user's
 * style sheet. If the user doesn't have a theme then it just exports
 * the default. This way, if the user's entry points to a stylesheet that
 * doesn't exist, the default one is always available.  
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
        	ILamsToolService toolService = (ILamsToolService) getContext().getBean(AuthoringConstants.TOOL_SERVICE_BEAN_NAME);
			IToolVO tool = toolService.getToolBySignature(toolSig);
			
			helpURL = tool.getHelpUrl();
			
			Locale locale = (Locale) session.getAttribute(LocaleFilter.PREFERRED_LOCALE_KEY);
		    if ( locale != null ) {
		    	language = locale.getLanguage();
		    	country = locale.getCountry();
		    }
			
		    fullURL = helpURL + module + "#" + toolSig + module + "-" + language + country;
		    
			writer.println("<a href='" + fullURL + "' target='_blank'>?</a>");

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
