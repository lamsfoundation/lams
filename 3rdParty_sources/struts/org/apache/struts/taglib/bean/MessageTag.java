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

package org.apache.struts.taglib.bean;

import java.util.Locale;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.Globals;
import org.apache.struts.taglib.TagUtils;
import org.apache.struts.util.MessageResources;

/**
 * Custom tag that retrieves an internationalized messages string (with
 * optional parametric replacement) from the <code>ActionResources</code>
 * object stored as a context attribute by our associated
 * <code>ActionServlet</code> implementation.
 *
 * @version $Rev: 54929 $ $Date$
 */
public class MessageTag extends TagSupport {

    // ------------------------------------------------------------- Properties

    /**
     * The first optional argument.
     */
    protected String arg0 = null;

    public String getArg0() {
        return (this.arg0);
    }

    public void setArg0(String arg0) {
        this.arg0 = arg0;
    }

    /**
     * The second optional argument.
     */
    protected String arg1 = null;

    public String getArg1() {
        return (this.arg1);
    }

    public void setArg1(String arg1) {
        this.arg1 = arg1;
    }

    /**
     * The third optional argument.
     */
    protected String arg2 = null;

    public String getArg2() {
        return (this.arg2);
    }

    public void setArg2(String arg2) {
        this.arg2 = arg2;
    }

    /**
     * The fourth optional argument.
     */
    protected String arg3 = null;

    public String getArg3() {
        return (this.arg3);
    }

    public void setArg3(String arg3) {
        this.arg3 = arg3;
    }

    /**
     * The fifth optional argument.
     */
    protected String arg4 = null;

    public String getArg4() {
        return (this.arg4);
    }

    public void setArg4(String arg4) {
        this.arg4 = arg4;
    }

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
     * The default Locale for our server.
     * @deprecated This will be removed after Struts 1.2.
     */
    protected static final Locale defaultLocale = Locale.getDefault();

    /**
     * The message key of the message to be retrieved.
     */
    protected String key = null;

    public String getKey() {
        return (this.key);
    }

    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Name of the bean that contains the message key.
     */
    protected String name = null;

    public String getName() {
        return (this.name);
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Name of the property to be accessed on the specified bean.
     */
    protected String property = null;

    public String getProperty() {
        return (this.property);
    }

    public void setProperty(String property) {
        this.property = property;
    }

    /**
     * The scope to be searched to retrieve the specified bean.
     */
    protected String scope = null;

    public String getScope() {
        return (this.scope);
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    /**
     * The session scope key under which our Locale is stored.
     */
    protected String localeKey = Globals.LOCALE_KEY;

    public String getLocale() {
        return (this.localeKey);
    }

    public void setLocale(String localeKey) {
        this.localeKey = localeKey;
    }

    /**
     * The message resources for this package.
     */
    protected static MessageResources messages =
        MessageResources.getMessageResources(
            "org.apache.struts.taglib.bean.LocalStrings");

    // --------------------------------------------------------- Public Methods

    /**
     * Process the start tag.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {

        String key = this.key;
        if (key == null) {
            // Look up the requested property value
            Object value = TagUtils.getInstance().lookup(pageContext, name, property, scope);
            if (value != null && !(value instanceof String)) {
                JspException e =
                    new JspException(messages.getMessage("message.property", key));
               TagUtils.getInstance().saveException(pageContext, e);
                throw e;
            }
            key = (String) value;
        }

        // Construct the optional arguments array we will be using
        Object args[] = new Object[] { arg0, arg1, arg2, arg3, arg4 };

        // Retrieve the message string we are looking for
        String message =
            TagUtils.getInstance().message(
                pageContext,
                this.bundle,
                this.localeKey,
                key,
                args);
                
        if (message == null) {
            JspException e =
                new JspException(
                    messages.getMessage("message.message", "\"" + key + "\""));
            TagUtils.getInstance().saveException(pageContext, e);
            throw e;
        }

        TagUtils.getInstance().write(pageContext, message);

        return (SKIP_BODY);

    }

    /**
     * Release any acquired resources.
     */
    public void release() {

        super.release();
        arg0 = null;
        arg1 = null;
        arg2 = null;
        arg3 = null;
        arg4 = null;
        bundle = Globals.MESSAGES_KEY;
        key = null;
        name = null;
        property = null;
        scope = null;
        localeKey = Globals.LOCALE_KEY;

    }

}
