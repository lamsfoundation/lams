/*
 *
 *
 * Copyright 2000-2004 The Apache Software Foundation.
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

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.util.MessageResources;
import org.apache.struts.taglib.TagUtils;

/**
 * Generate a URL-encoded redirect to the specified URI.
 *
 * @version $Rev: 54929 $ $Date$
 */
public class RedirectTag extends TagSupport {

    // ------------------------------------------------------------- Properties

    /**
     * The anchor to be added to the end of the generated hyperlink.
     */
    protected String anchor = null;

    public String getAnchor() {
        return (this.anchor);
    }

    public void setAnchor(String anchor) {
        this.anchor = anchor;
    }

    /**
     * The logical forward name from which to retrieve the redirect URI.
     */
    protected String forward = null;

    public String getForward() {
        return (this.forward);
    }

    public void setForward(String forward) {
        this.forward = forward;
    }

    /**
     * The redirect URI.
     */
    protected String href = null;

    public String getHref() {
        return (this.href);
    }

    public void setHref(String href) {
        this.href = href;
    }

    /**
     * The message resources for this package.
     */
    protected static MessageResources messages =
        MessageResources.getMessageResources(
            "org.apache.struts.taglib.logic.LocalStrings");

    /**
     * The JSP bean name for query parameters.
     */
    protected String name = null;

    public String getName() {
        return (this.name);
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * The module-relative page URL (beginning with a slash) to which
     * this redirect will be rendered.
     */
    protected String page = null;

    public String getPage() {
        return (this.page);
    }

    public void setPage(String page) {
        this.page = page;
    }

    /**
     * The module-relative action (beginning with a slash) which will be
     * called by this link
     */
    protected String action = null;

    public String getAction() {
        return (this.action);
    }

    public void setAction(String action) {
        this.action = action;
    }


	/**
	 * The module prefix (beginning with a slash) which will be
	 * used to find the action for this link.
	 */
	protected String module = null;

	public String getModule() {
		return (this.module);
	}

	public void setModule(String module) {
		this.module = module;
	}

    /**
     * The single-parameter request parameter name to generate.
     */
    protected String paramId = null;

    public String getParamId() {
        return (this.paramId);
    }

    public void setParamId(String paramId) {
        this.paramId = paramId;
    }

    /**
     * The single-parameter JSP bean name.
     */
    protected String paramName = null;

    public String getParamName() {
        return (this.paramName);
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    /**
     * The single-parameter JSP bean property.
     */
    protected String paramProperty = null;

    public String getParamProperty() {
        return (this.paramProperty);
    }

    public void setParamProperty(String paramProperty) {
        this.paramProperty = paramProperty;
    }

    /**
     * The single-parameter JSP bean scope.
     */
    protected String paramScope = null;

    public String getParamScope() {
        return (this.paramScope);
    }

    public void setParamScope(String paramScope) {
        this.paramScope = paramScope;
    }

    /**
     * The JSP bean property name for query parameters.
     */
    protected String property = null;

    public String getProperty() {
        return (this.property);
    }

    public void setProperty(String property) {
        this.property = property;
    }

    /**
     * The scope of the bean specified by the name property, if any.
     */
    protected String scope = null;

    public String getScope() {
        return (this.scope);
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    /**
     * Include our transaction control token?
     */
    protected boolean transaction = false;

    public boolean getTransaction() {
        return (this.transaction);
    }

    public void setTransaction(boolean transaction) {
        this.transaction = transaction;
    }

	/**
	 * Use character encoding from ServletResponse#getCharacterEncoding
	 * to get bytes of the url string for urlencoding?
	 */
	protected boolean useLocalEncoding = false;
    
	public boolean isUseLocalEncoding() {
	   return useLocalEncoding;
	}

	public void setUseLocalEncoding(boolean b) {
	   useLocalEncoding = b;
	}

    // --------------------------------------------------------- Public Methods

    /**
     * Defer generation until the end of this tag is encountered.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {

        return (SKIP_BODY);

    }

    /**
     * Render the redirect and skip the remainder of this page.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doEndTag() throws JspException {

        this.doRedirect(this.generateRedirectURL());

        return (SKIP_PAGE);

    }

    /**
     * Calculate the url to redirect to.
     * @throws JspException
     * @since Struts 1.2
     */
    protected String generateRedirectURL() throws JspException {
        Map params =
            TagUtils.getInstance().computeParameters(
                pageContext,
                paramId,
                paramName,
                paramProperty,
                paramScope,
                name,
                property,
                scope,
                transaction);

        String url = null;
        try {
            url =
                TagUtils.getInstance().computeURLWithCharEncoding(
                    pageContext,
                    forward,
                    href,
                    page,
                    action,
                    module,
                    params,
                    anchor,
                    true,
                    useLocalEncoding);

        } catch (MalformedURLException e) {
            TagUtils.getInstance().saveException(pageContext, e);
            throw new JspException(
                messages.getMessage("redirect.url", e.toString()));
        }

        return url;
    }

    /**
     * Redirect to the given url converting exceptions to JspException.
     * @param url The path to redirect to.
     * @throws JspException
     * @since Struts 1.2
     */
    protected void doRedirect(String url) throws JspException {
        HttpServletResponse response =
            (HttpServletResponse) pageContext.getResponse();

        try {
            response.sendRedirect(url);

        } catch (IOException e) {
            TagUtils.getInstance().saveException(pageContext, e);
            throw new JspException(e.getMessage());
        }
    }

    /**
     * Release any acquired resources.
     */
    public void release() {

        super.release();
        anchor = null;
        forward = null;
        href = null;
        name = null;
        page = null;
        action = null;
        paramId = null;
        paramName = null;
        paramProperty = null;
        paramScope = null;
        property = null;
        scope = null;

    }

}
