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
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.util.CentralConstants;
import org.lamsfoundation.lams.util.HelpUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.web.filter.LocaleFilter;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Generates a help link to a contextualised tool help page.
 *
 *
 *
 * @author Fiona Malikoff
 */
public class HelpTag extends TagSupport {
    private static final long serialVersionUID = -4529242646662173012L;

    private static final Logger log = Logger.getLogger(HelpTag.class);
    private String module = null;
    private String page = null;
    private String toolSignature = null;
    private String style = null;

    /**
     *
     */
    public HelpTag() {
	super();
    }

    @Override
    public int doStartTag() throws JspException {
	try {

	    boolean div = false;
	    String helpWord = "";

	    JspWriter writer = pageContext.getOut();
	    if (StringUtils.equals(style, "no-tabs")) {
		writer.println("<div class='help-no-tabs'>");
		div = true;
	    }

	    if (!StringUtils.equals(style, "small")) {
		MessageService msgService = (MessageService) getContext()
			.getBean(CentralConstants.CENTRAL_MESSAGE_SERVICE_BEAN_NAME);
		helpWord = msgService.getMessage("label.help");
	    }

	    try {

		HttpSession session = ((HttpServletRequest) this.pageContext.getRequest()).getSession();
		Locale locale = (Locale) session.getAttribute(LocaleFilter.PREFERRED_LOCALE_KEY);
		String languageCode = locale != null ? locale.getLanguage() : "";

		if ((toolSignature != null) && (module != null)) {

		    // retrieve help URL for tool
		    ILamsToolService toolService = (ILamsToolService) getContext()
			    .getBean(CentralConstants.TOOL_SERVICE_BEAN_NAME);
		    Tool tool = toolService.getToolBySignature(toolSignature);

		    String fullURL = HelpUtil.constructToolURL(tool.getHelpUrl(), toolSignature, module, languageCode);

		    if (fullURL == null) {
			return Tag.SKIP_BODY;
		    }

		    writer.println("<button type=\"button\" class=\"nav-link no-decoration\" onclick=\"window.open('" + fullURL + "', 'help')\" id=\"help-tag\">"
			    + "<i class=\"fa fa-question-circle\" ></i> " + helpWord + "</button>");

		} else if (page != null) {

		    String fullURL = HelpUtil.constructPageURL(page, languageCode);

		    writer.println("<button type=\"button\" class=\"nav-link no-decoration\" onclick=\"window.open('" + fullURL + "', 'help')\" id=\"help-tag\">"
			    + "<i class=\"fa fa-question-circle\" ></i> " + helpWord + "</button>");

		} else {
		    HelpTag.log.error("HelpTag unable to write out due to unspecified values.");
		    writer.println("<i class=\"fa fa-times-circle\"></i>");
		}
	    } catch (NullPointerException npe) {
		HelpTag.log.error(
			"HelpTag unable to write out due to NullPointerException. Most likely a required paramater was unspecified or incorrect.",
			npe);
		// don't throw a JSPException as we want the system to still function.

	    }

	    if (div) {
		writer.println("</div>");
	    }

	} catch (IOException e) {
	    HelpTag.log.error("HelpTag unable to write out due to IOException.", e);
	    // don't throw a JSPException as we want the system to still function.
	}
	return Tag.SKIP_BODY;
    }

    @Override
    public int doEndTag() {
	return Tag.EVAL_PAGE;
    }

    /**
     * @return module
     *
     *
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
	WebApplicationContext ctx = WebApplicationContextUtils
		.getRequiredWebApplicationContext(pageContext.getServletContext());
	return ctx;
    }

    /**
     * @return
     *
     *
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

    /**
     * @return page
     *
     *
     */
    public String getPage() {
	return module;
    }

    /**
     *
     * @param page
     */
    public void setPage(String page) {
	this.page = page;
    }

    /**
     * @return style
     *
     *
     */
    public String getStyle() {
	return style;
    }

    /**
     *
     * @param style
     */
    public void setStyle(String style) {
	this.style = style;
    }

}
