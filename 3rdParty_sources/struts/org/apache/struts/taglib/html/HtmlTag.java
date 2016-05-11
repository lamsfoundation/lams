/*
 *
 *
 * Copyright 1999-2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.struts.taglib.html;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.Globals;
import org.apache.struts.taglib.TagUtils;
import org.apache.struts.util.MessageResources;

/**
 * Renders an HTML <html> element with appropriate language attributes if
 * there is a current Locale available in the user's session.
 *
 * @version $Rev: 54929 $ $Date$
 */
public class HtmlTag extends TagSupport {
  

    // ------------------------------------------------------------- Properties


    /**
     * The message resources for this package.
     */
    protected static MessageResources messages =
     MessageResources.getMessageResources(Constants.Package + ".LocalStrings");


    /**
     * Should we set the current Locale for this user if needed?
     * @deprecated This will be removed after Struts 1.2.
     */
    protected boolean locale = false;

    /**
     * @deprecated This will be removed after Struts 1.2.
     */
    public boolean getLocale() {
        return (locale);
    }

    /**
     * @deprecated This will be removed after Struts 1.2.
     */
    public void setLocale(boolean locale) {
        this.locale = locale;
    }

    /**
     * Are we rendering an xhtml page?
     */
    protected boolean xhtml = false;
    
    /**
     * Are we rendering a lang attribute?
     * @since Struts 1.2
     */
    protected boolean lang = false;

    public boolean getXhtml() {
        return this.xhtml;
    }

    public void setXhtml(boolean xhtml) {
        this.xhtml = xhtml;
    }
    
    /**
     * Returns true if the tag should render a lang attribute.
     * @since Struts 1.2
     */
    public boolean getLang() {
        return this.lang;
    }

    /**
     * Sets whether the tag should render a lang attribute.
     * @since Struts 1.2
     */
    public void setLang(boolean lang) {
        this.lang = lang;
    }

    /**
     * Process the start of this tag.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {

        TagUtils.getInstance().write(this.pageContext, this.renderHtmlStartElement());

        return EVAL_BODY_INCLUDE;
    }

    /**
     * Renders an &lt;html&gt; element with appropriate language attributes.
     * @since Struts 1.2
     */
    protected String renderHtmlStartElement() {
        StringBuffer sb = new StringBuffer("<html");

        String language = null;
        String country = "";
                
        if (this.locale) {
            // provided for 1.1 backward compatibility, remove after 1.2
            language = this.getCurrentLocale().getLanguage();
        } else {
            Locale currentLocale =
                TagUtils.getInstance().getUserLocale(pageContext, Globals.LOCALE_KEY);

            language = currentLocale.getLanguage();
            country = currentLocale.getCountry();
        }

        boolean validLanguage = ((language != null) && (language.length() > 0));
        boolean validCountry = country.length() > 0;

        if (this.xhtml) {
            this.pageContext.setAttribute(
                Globals.XHTML_KEY,
                "true",
                PageContext.PAGE_SCOPE);
                
            sb.append(" xmlns=\"http://www.w3.org/1999/xhtml\"");
        }

        if ((this.lang || this.locale || this.xhtml) && validLanguage) {
            sb.append(" lang=\"");
            sb.append(language);
            if (validCountry) {
                sb.append("-");
                sb.append(country);
            }
            sb.append("\"");
        }

        if (this.xhtml && validLanguage) {
            sb.append(" xml:lang=\"");
            sb.append(language);
            if (validCountry) {
                sb.append("-");
                sb.append(country);
            }
            sb.append("\"");
        }

        sb.append(">");

        return sb.toString();
    }


    /**
     * Process the end of this tag.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doEndTag() throws JspException {

        TagUtils.getInstance().write(pageContext, "</html>");

        // Evaluate the remainder of this page
        return (EVAL_PAGE);

    }

    /**
     * Release any acquired resources.
     */
    public void release() {
        this.locale = false;
        this.xhtml = false;
        this.lang=false;
    }


    // ------------------------------------------------------ Protected Methods


    /**
     * Return the current Locale for this request.  If there is no locale in the session and
     * the locale attribute is set to "true", this method will create a Locale based on the 
     * client's Accept-Language header or the server's default locale and store it in the 
     * session.  This will always return a Locale and never null.
     * @since Struts 1.1
     * @deprecated This will be removed after Struts 1.2.
     */
    protected Locale getCurrentLocale() {

        Locale userLocale = TagUtils.getInstance().getUserLocale(pageContext, Globals.LOCALE_KEY);

        // Store a new current Locale, if requested
        if (this.locale) {
            HttpSession session = ((HttpServletRequest) this.pageContext.getRequest()).getSession();
            session.setAttribute(Globals.LOCALE_KEY, userLocale);
        }

        return userLocale;
    }



}
