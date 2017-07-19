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

package org.lamsfoundation.lams.web.tag;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.util.CSSThemeUtil;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.web.filter.LocaleFilter;

/**
 * Output the required css based in the user's details. Will output one or more lines. If the user has a theme then
 * exports the default and then the user's style sheet. If the user doesn't have a theme then it just exports the
 * default. This way, if the user's entry points to a stylesheet that doesn't exist, the default one is always
 * available.
 *
 *
 * on the user preferences."
 *
 * @author Fiona Malikoff
 */
public class CssTag extends TagSupport {
    private static final long serialVersionUID = -3143529984657965761L;

    private static final Logger log = Logger.getLogger(CssTag.class);

    private static final String RTL_DIR = "rtl"; // right-to-left direction

    private String suffix = null;
    private String webapp = null;
    
    public CssTag() {
	super();
    }

    @Override
    public int doStartTag() throws JspException {

	String serverURL = Configuration.get(ConfigurationKeys.SERVER_URL);
	serverURL = serverURL == null ? null : serverURL.trim();

	if (serverURL != null) {
	    try {
		HttpSession session = ((HttpServletRequest) this.pageContext.getRequest()).getSession();
		String pageDirection = (String) session.getAttribute(LocaleFilter.DIRECTION); // RTL or LTR (default)
		boolean rtl = CssTag.RTL_DIR.equalsIgnoreCase(pageDirection);

		List<String> themeList = CSSThemeUtil.getAllUserThemes();
		String customStylesheetLink = null;
		for (String theme : themeList) {
		    if (theme != null) {
			theme = appendStyle(theme, rtl);
			customStylesheetLink = generateLink(theme, serverURL);
		    }

		    if (customStylesheetLink != null) {
			JspWriter writer = pageContext.getOut();
			writer.println(customStylesheetLink);
		    }
		}

	    } catch (IOException e) {
		CssTag.log.error("CssTag unable to write out CSS details due to IOException.", e);
		// don't throw a JSPException as we want the system to still function.
	    }
	} else {
	    CssTag.log.warn(
		    "CSSTag unable to write out CSS entries as the server url is missing from the configuration file.");
	}

	return Tag.SKIP_BODY;
    }

    private String appendStyle(String stylesheetName, boolean rtl) {
	if (stylesheetName != null) {
	    StringBuilder bldr = new StringBuilder(stylesheetName);
	    if ( rtl ) {
		bldr.append("_").append(CssTag.RTL_DIR);
	    }
	    bldr.append("_");
	    bldr.append(suffix != null ? suffix : "learner");
	    return bldr.toString();
	}
	return null;
    }

    private String generateLink(String stylesheetName, String serverURL) {
	StringBuilder bldr = new StringBuilder("<link href=\"").append(serverURL);
	if (!serverURL.endsWith("/")) {
	    bldr.append("/");
	}
	if ( webapp!= null ) {
	    bldr.append(webapp).append("/");
	}
	bldr.append("css/").append(stylesheetName).append(".css\" rel=\"stylesheet\" type=\"text/css\">");
	return bldr.toString();
    }

    @Override
    public int doEndTag() {
	return Tag.EVAL_PAGE;
    }

    public String getSuffix() {
	return suffix;
    }

    /** Set the end of the stylesheet name to call a secondary stylesheet. Do not define to get the normal blah_learner.html stylesheet */
    public void setSuffix(String suffix) {
	this.suffix = suffix;
    }

    public String getWebapp() {
	return webapp;
    }

    public void setWebapp(String webapp) {
	this.webapp = webapp;
    }
}