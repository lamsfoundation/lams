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
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.util.CSSThemeUtil;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.web.filter.LocaleFilter;

/**
 * Output the required css based in the user's details. Will output one or more
 * lines. If the user has a theme then exports the default and then the user's
 * style sheet. If the user doesn't have a theme then it just exports the
 * default. This way, if the user's entry points to a stylesheet that doesn't
 * exist, the default one is always available.
 *
 * @jsp.tag name="css" body-content="empty" display-name="User's chosen
 *          stylesheet" description="Output stylesheet based on the user
 *          preferences."
 *
 * @author Fiona Malikoff
 */
public class CssTag extends TagSupport {

    private static final Logger log = Logger.getLogger(CssTag.class);
    private static final String IE_STYLESHEET_NAME = "ie-styles";
    private static final String IE_STYLESHEET_NAME_RTL = "ie-styles_rtl";
    private static final String IE7_STYLESHEET_NAME = "ie7-styles";
    private static final String IE7_STYLESHEET_NAME_RTL = "ie7-styles_rtl";
    private String localLinkPath = null;
    private String style = null;

    private static final String LEARNER_STYLE = "learner"; // expandable
    private static final String TABBED_STYLE = "tabbed"; // fixed width
    private static final String RTL_DIR = "rtl"; // right-to-left direction
    private static final String MAIN_STYLE = "main"; // expandable

    /**
     *
     */
    public CssTag() {
	super();
    }

    @Override
    public int doStartTag() throws JspException {
	HttpSession session = ((HttpServletRequest) this.pageContext.getRequest()).getSession();

	String customStylesheetLink = null;

	boolean rtl = false;
	String pageDirection = (String) session.getAttribute(LocaleFilter.DIRECTION); // RTL or LTR (default)

	String serverURL = Configuration.get(ConfigurationKeys.SERVER_URL);
	serverURL = (serverURL != null ? serverURL.trim() : null);

	try {

	    JspWriter writer = pageContext.getOut();
	    if (serverURL != null) {
		if (pageDirection != null) {
		    if (pageDirection.toLowerCase().equals(RTL_DIR)) {
			rtl = true;
		    }
		}

		List<String> themeList = CSSThemeUtil.getAllUserThemes();

		for (String theme : themeList) {
		    if (theme != null) {
			theme = appendStyle(theme, rtl);
			if (localLinkPath != null) {
			    customStylesheetLink = generateLocalLink(theme);
			} else {
			    customStylesheetLink = generateLink(theme, serverURL);
			}
		    }

		    if (customStylesheetLink != null) {
			writer.println(customStylesheetLink);
		    }
		}

	    } else {
		log.warn(
			"CSSTag unable to write out CSS entries as the server url is missing from the configuration file.");
	    }

	    // Special IE stylesheet for all those IE related formatting issues
	    String ieStylesheetName = (!rtl) ? IE_STYLESHEET_NAME : IE_STYLESHEET_NAME_RTL;
	    String ie7StylesheetName = (!rtl) ? IE7_STYLESHEET_NAME : IE7_STYLESHEET_NAME_RTL;
	    String ieLink = localLinkPath != null ? generateLocalURL(ieStylesheetName)
		    : generateURL(ieStylesheetName, serverURL);

	    writer.println("<!--[if IE]>");
	    writer.println("<style type=\"text/css\">");
	    writer.println("@import url (" + ieLink + ");");
	    writer.println("</style>");
	    writer.println("<![endif]-->");

	    String ie7Link = localLinkPath != null ? generateLocalURL(ie7StylesheetName)
		    : generateURL(ie7StylesheetName, serverURL);
	    writer.println("<!--[if IE 7]>");
	    writer.println("<style type=\"text/css\">");
	    writer.println("@import url (" + ie7Link + ");");
	    writer.println("</style>");
	    writer.println("<![endif]-->");

	} catch (IOException e) {
	    log.error("CssTag unable to write out CSS details due to IOException.", e);
	    // don't throw a JSPException as we want the system to still function.
	}
	return SKIP_BODY;
    }

    private String generateLocalLink(String stylesheetName) {
	if (localLinkPath.endsWith("/")) {
	    return "<link href=\"" + localLinkPath + "css/" + stylesheetName
		    + ".css\" rel=\"stylesheet\" type=\"text/css\">";
	} else {
	    return "<link href=\"" + localLinkPath + "/css/" + stylesheetName
		    + ".css\" rel=\"stylesheet\" type=\"text/css\">";
	}
    }

    private String appendStyle(String stylesheetName, boolean rtl) {
	String ssName = stylesheetName;
	if (ssName != null && (getStyle() == null || getStyle().equals(LEARNER_STYLE))) {
	    ssName = (!rtl) ? ssName + "_" + "learner" : ssName + "_" + RTL_DIR + "_" + "learner";
	} else {
	    ssName = (!rtl) ? ssName : ssName + "_" + RTL_DIR;
	}
	return ssName;
    }

    private String generateLocalURL(String stylesheetName) {
	if (localLinkPath.endsWith("/")) {
	    return "\"" + localLinkPath + "css/" + stylesheetName + ".css\"";
	} else {
	    return "\"" + localLinkPath + "/css/" + stylesheetName + ".css\"";
	}
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

    private String generateURL(String stylesheetName, String serverURL) {
	if (serverURL.endsWith("/")) {
	    return "\"" + serverURL + "css/" + stylesheetName + ".css\"";
	} else {
	    return "\"" + serverURL + "/css/" + stylesheetName + ".css\"";
	}
    }

    @Override
    public int doEndTag() {
	return EVAL_PAGE;
    }

    /**
     * @jsp.attribute required="false" rtexprvalue="true" description="Should
     *                the css link be a local file link? If so, what is the path
     *                back to the 'root'. For tools' export portfolio pages,
     *                this should be set to \"../\""
     *
     * @return Returns the property.
     */
    public String getLocalLinkPath() {
	return localLinkPath;
    }

    public void setLocalLinkPath(String localLinkPath) {
	this.localLinkPath = localLinkPath;
    }

    /**
     * @jsp.attribute required="false" rtexprvalue="true" description="Learner
     *                pages use learner, fancy pages such as index page use
     *                core"
     *
     *                Sets whether to use blah.css (style="core") or
     *                blah_learner.css (style=learner). If this parameter is
     *                left blank, you get blah_learner.css e.g.
     *                default_learner.css
     * @return Returns style.
     */
    public String getStyle() {
	return style;
    }

    public void setStyle(String style) {
	this.style = style;
    }

}
