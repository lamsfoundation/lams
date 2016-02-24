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
 * @jsp.tag name="css" body-content="empty" display-name="User's chosen stylesheet" description="Output stylesheet based
 *          on the user preferences."
 * 
 * @author Fiona Malikoff
 */
public class CssTag extends TagSupport {
    private static final long serialVersionUID = -3143529984657965761L;

    private static final Logger log = Logger.getLogger(CssTag.class);
    private String style = null;

    private static final String LEARNER_STYLE = "learner"; // expandable
    private static final String RTL_DIR = "rtl"; // right-to-left direction

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
	String ssName = stylesheetName;
	if (ssName != null) {
	    if ((getStyle() == null) || CssTag.LEARNER_STYLE.equalsIgnoreCase(getStyle())) {
		ssName = rtl ? ssName + "_" + CssTag.RTL_DIR + "_" + "learner" : ssName + "_" + "learner";
	    } else {
		ssName = rtl ? ssName + "_" + CssTag.RTL_DIR : ssName;
	    }
	}
	return ssName;
    }

    private String generateLink(String stylesheetName, String serverURL) {
	if (serverURL.endsWith("/")) {
	    return "<link href=\"" + serverURL + "css/" + stylesheetName
		    + ".css\" rel=\"stylesheet\" type=\"text/css\">";
	} else {
	    return "<link href=\"" + serverURL + "/css/" + stylesheetName
		    + ".css\" rel=\"stylesheet\" type=\"text/css\">";
	}
    }

    @Override
    public int doEndTag() {
	return Tag.EVAL_PAGE;
    }

    /**
     * @jsp.attribute required="false" rtexprvalue="true" description="Learner pages use learner, fancy pages such as
     *                index page use core"
     * 
     *                Sets whether to use blah.css (style="core") or blah_learner.css (style=learner). If this parameter
     *                is left blank, you get blah_learner.css e.g. default_learner.css
     * @return Returns style.
     */
    public String getStyle() {
	return style;
    }

    public void setStyle(String style) {
	this.style = style;
    }
}