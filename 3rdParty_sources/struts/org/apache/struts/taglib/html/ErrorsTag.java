/*
 *
 *
 * Copyright 1999-2005 The Apache Software Foundation.
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

import java.util.Iterator;
import java.util.Locale;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.taglib.TagUtils;
import org.apache.struts.util.MessageResources;

/**
 * Custom tag that renders error messages if an appropriate request attribute
 * has been created.  The tag looks for a request attribute with a reserved
 * key, and assumes that it is either a String, a String array, containing
 * message keys to be looked up in the module's MessageResources, or
 * an object of type <code>org.apache.struts.action.ActionErrors</code>.
 * <p>
 * The following optional message keys will be utilized if corresponding
 * messages exist for them in the application resources:
 * <ul>
 * <li><b>errors.header</b> - If present, the corresponding message will be
 *     rendered prior to the individual list of error messages.</li>
 * <li><b>errors.footer</b> - If present, the corresponding message will be
 *     rendered following the individual list of error messages.</li>
 * <li><b>errors.prefix</b> - If present, the corresponding message will be
 *     rendered before each individual error message.</li>
 * <li><b>errors.suffix</b> - If present, the corresponding message will be
 *     rendered after each individual error message.</li>
 * </ul>
 *
 * @version $Rev: 164530 $ $Date$
 */
public class ErrorsTag extends TagSupport {

    // ----------------------------------------------------------- Properties

    /**
     * The servlet context attribute key for our resources.
     */
    protected String bundle = null;

    public String getBundle() {
        return (this.bundle);
    }

    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

    /**
     * The default locale on our server.
     * @deprecated Use Locale.getDefault() directly.
     */
    protected static Locale defaultLocale = Locale.getDefault();

    /**
     * The line ending string.
     * @deprecated No longer used.
     */
    protected static String lineEnd = System.getProperty("line.separator");

    /**
     * The session attribute key for our locale.
     */
    protected String locale = Globals.LOCALE_KEY;

    public String getLocale() {
        return (this.locale);
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    /**
     * The message resources for this package.
     */
    protected static MessageResources messages =
        MessageResources.getMessageResources(Constants.Package + ".LocalStrings");

    /**
     * The request attribute key for our error messages (if any).
     */
    protected String name = Globals.ERROR_KEY;

    public String getName() {
        return (this.name);
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * The name of the property for which error messages should be returned,
     * or <code>null</code> to return all errors.
     */
    protected String property = null;

    public String getProperty() {
        return (this.property);
    }

    public void setProperty(String property) {
        this.property = property;
    }

    /**
     * The message resource key for errors header.
     */
    protected String header = null;

    public String getHeader() {
        return header == null ? "errors.header" : header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    /**
     * The message resource key for errors footer.
     */
    protected String footer = null;

    public String getFooter() {
        return footer == null ? "errors.footer" : footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    /**
     * The message resource key for errors prefix.
     */
    protected String prefix= null;

    public String getPrefix() {
        return prefix == null ? "errors.prefix" : prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix= prefix;
    }

    /**
     * The message resource key for errors suffix.
     */
    protected String suffix= null;

    public String getSuffix() {
        return suffix == null ? "errors.suffix" : suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix= suffix;
    }

    // ------------------------------------------------------- Public Methods

    /**
     * Render the specified error messages if there are any.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {

        // Were any error messages specified?
        ActionMessages errors = null;
        try {
            errors = TagUtils.getInstance().getActionMessages(pageContext, name);
        } catch (JspException e) {
            TagUtils.getInstance().saveException(pageContext, e);
            throw e;
        }

        if ((errors == null) || errors.isEmpty()) {
            return (EVAL_BODY_INCLUDE);
        }

        boolean headerPresent =
            TagUtils.getInstance().present(pageContext, bundle, locale, getHeader());

        boolean footerPresent =
            TagUtils.getInstance().present(pageContext, bundle, locale, getFooter());

        boolean prefixPresent =
            TagUtils.getInstance().present(pageContext, bundle, locale, getPrefix());

        boolean suffixPresent =
            TagUtils.getInstance().present(pageContext, bundle, locale, getSuffix());

        // Render the error messages appropriately
        StringBuffer results = new StringBuffer();
        boolean headerDone = false;
        String message = null;
        Iterator reports = (property == null) ? errors.get() : errors.get(property);

        while (reports.hasNext()) {
            ActionMessage report = (ActionMessage) reports.next();
            if (!headerDone) {
                if (headerPresent) {
                    message =
                        TagUtils.getInstance().message(
                            pageContext,
                            bundle,
                            locale,
                            getHeader());
                            
                    results.append(message);
                }
                headerDone = true;
            }
            
            if (prefixPresent) {
                message =
                    TagUtils.getInstance().message(
                        pageContext,
                        bundle,
                        locale,
                        getPrefix());
                results.append(message);
            }
            
            if (report.isResource()) {
                message =
                    TagUtils.getInstance().message(
                        pageContext,
                        bundle,
                        locale,
                        report.getKey(),
                        report.getValues());
            } else {
                message = report.getKey();
            }
                    
            if (message != null) {
                results.append(message);
            }
            
            if (suffixPresent) {
                message =
                    TagUtils.getInstance().message(
                        pageContext,
                        bundle,
                        locale,
                        getSuffix());
                results.append(message);
            }
        }
        
        if (headerDone && footerPresent) {
            message =
                TagUtils.getInstance().message(pageContext, bundle, locale, getFooter());
            results.append(message);
        }

        TagUtils.getInstance().write(pageContext, results.toString());

        return (EVAL_BODY_INCLUDE);

    }

    /**
     * Release any acquired resources.
     */
    public void release() {
        super.release();
        bundle = Globals.MESSAGES_KEY;
        locale = Globals.LOCALE_KEY;
        name = Globals.ERROR_KEY;
        property = null;
        header = null;
        footer = null;
        prefix = null;
        suffix = null;
    }

}
