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


package org.apache.struts.taglib.logic;


import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.struts.util.MessageResources;


/**
 * Abstract base class for the various conditional evaluation tags.
 *
 * @version $Rev: 54929 $ $Date$
 */

public abstract class ConditionalTagBase extends TagSupport {


    // ------------------------------------------------------------- Properties


    /**
     * The name of the cookie to be used as a variable.
     */
    protected String cookie = null;

    public String getCookie() {
        return (this.cookie);
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }


    /**
     * The name of the HTTP request header to be used as a variable.
     */
    protected String header = null;

    public String getHeader() {
        return (this.header);
    }

    public void setHeader(String header) {
        this.header = header;
    }


    /**
     * The message resources for this package.
     */
    protected static MessageResources messages =
     MessageResources.getMessageResources
        ("org.apache.struts.taglib.logic.LocalStrings");


    /**
     * The name of the JSP bean to be used as a variable (if
     * <code>property</code> is not specified), or whose property is to be
     * accessed (if <code>property</code> is specified).
     */
    protected String name = null;

    public String getName() {
        return (this.name);
    }

    public void setName(String name) {
        this.name = name;
    }


    /**
     * The name of the HTTP request parameter to be used as a variable.
     */
    protected String parameter = null;

    public String getParameter() {
        return (this.parameter);
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }


    /**
     * The name of the bean property to be used as a variable.
     */
    protected String property = null;

    public String getProperty() {
        return (this.property);
    }

    public void setProperty(String property) {
        this.property = property;
    }


    /**
     * The name of the security role to be checked for.
     */
    protected String role = null;

    public String getRole() {
        return (this.role);
    }

    public void setRole(String role) {
        this.role = role;
    }


    /**
     * The scope to search for the bean named by the name property, or
     * "any scope" if null.
     */
    protected String scope = null;

    public String getScope() {
        return (this.scope);
    }

    public void setScope(String scope) {
        this.scope = scope;
    }


    /**
     * The user principal name to be checked for.
     */
    protected String user = null;

    public String getUser() {
        return (this.user);
    }

    public void setUser(String user) {
        this.user = user;
    }


    // --------------------------------------------------------- Public Methods


    /**
     * Perform the test required for this particular tag, and either evaluate
     * or skip the body of this tag.
     *
     * @exception JspException if a JSP exception occurs
     */
    public int doStartTag() throws JspException {

        if (condition())
            return (EVAL_BODY_INCLUDE);
        else
            return (SKIP_BODY);

    }


    /**
     * Evaluate the remainder of the current page normally.
     *
     * @exception JspException if a JSP exception occurs
     */
    public int doEndTag() throws JspException {

        return (EVAL_PAGE);

    }


    /**
     * Release all allocated resources.
     */
    public void release() {

        super.release();
        cookie = null;
        header = null;
        name = null;
        parameter = null;
        property = null;
        role = null;
        scope = null;
        user = null;

    }


    // ------------------------------------------------------ Protected Methods


    /**
     * Evaluate the condition that is being tested by this particular tag,
     * and return <code>true</code> if the nested body content of this tag
     * should be evaluated, or <code>false</code> if it should be skipped.
     * This method must be implemented by concrete subclasses.
     *
     * @exception JspException if a JSP exception occurs
     */
    protected abstract boolean condition() throws JspException;


}
