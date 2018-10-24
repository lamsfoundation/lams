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

package org.lamsfoundation.lams.web.tag;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.web.filter.LocaleFilter;

/**
 * Replaces the tradition HTML tag with one that sets the appropriate page direction,
 * based on the page_direction session attribute. It also sets the lang attribute.
 *
 * There is a single parameter xhtml, which is used to append the http://www.w3.org/1999/xhtml
 * string and to set the flag to make Struts tags xhtml compliant.
 *
 * It has been done as a class rather than a .tag file as so that the body of the
 * page can contain scriptlets. The code is based on the Struts HTML tag.
 *
 *
 *
 *
 * Render html tag with direction and language
 */
public class HtmlTag extends TagSupport {

    private static final Logger log = Logger.getLogger(HtmlTag.class);

    protected boolean xhtml = false;

    /**
     *
     * Is this an xhtml file?
     */
    public boolean getXhtml() {
	return this.xhtml;
    }

    public void setXhtml(boolean xhtml) {
	this.xhtml = xhtml;
    }

    /**
     * Render the HTML tag
     *
     * @exception JspException
     *                if a JSP exception has occurred
     */
    @Override
    public int doStartTag() throws JspException {

	StringBuffer sb = new StringBuffer("<html");

	String language = null;
	String country = null;

	HttpSession session = ((HttpServletRequest) this.pageContext.getRequest()).getSession();

	Locale locale = (Locale) session.getAttribute(LocaleFilter.PREFERRED_LOCALE_KEY);
	if (locale != null) {
	    language = locale.getLanguage();
	    country = locale.getCountry();
	}
	String direction = (String) session.getAttribute(LocaleFilter.DIRECTION);

	if (this.xhtml) {
	    // for struts compatibility

	    sb.append(" xmlns=\"http://www.w3.org/1999/xhtml\"");
	}

	if (language != null) {
	    sb.append(" lang=\"");
	    sb.append(language);
	    if (country != null) {
		sb.append("-");
		sb.append(country);
	    }
	    sb.append("\"");
	}

	if (this.xhtml && language != null) {
	    sb.append(" xml:lang=\"");
	    sb.append(language);
	    if (country != null) {
		sb.append("-");
		sb.append(country);
	    }
	    sb.append("\"");
	}

	if (direction != null) {
	    this.pageContext.setAttribute(LocaleFilter.DIRECTION, direction, PageContext.PAGE_SCOPE);

	    sb.append(" dir=\"");
	    sb.append(direction);
	    sb.append("\"");
	}

	sb.append(">");

	writeString(sb.toString());

	return EVAL_BODY_INCLUDE;

    }

    private void writeString(String output) {
	try {
	    JspWriter writer = pageContext.getOut();
	    writer.println(output);
	} catch (IOException e) {
	    log.error("HTML tag unable to write out HTML details due to IOException.", e);
	    // don't throw a JSPException as we want the system to still function, well, best
	    // the page can without the html tag!
	}
    }

    /**
     * Process the end of this tag.
     *
     * @exception JspException
     *                if a JSP exception has occurred
     */
    @Override
    public int doEndTag() throws JspException {

	writeString("</html>");

	// Evaluate the remainder of this page
	return (EVAL_PAGE);

    }

    /**
     * Release any acquired resources.
     */
    @Override
    public void release() {
	this.xhtml = false;
    }

}
