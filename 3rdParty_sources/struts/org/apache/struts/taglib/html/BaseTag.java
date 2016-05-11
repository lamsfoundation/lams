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

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.Globals;
import org.apache.struts.taglib.TagUtils;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

/**
 * Renders an HTML <base> element with an href 
 * attribute pointing to the absolute location of the enclosing JSP page. This 
 * tag is only valid when nested inside a head tag body. The presence 
 * of this tag allows the browser to resolve relative URL's to images,
 * CSS stylesheets  and other resources in a manner independent of the URL
 * used to call the ActionServlet.
 *
 * @version $Rev: 54929 $ $Date$
 */

public class BaseTag extends TagSupport {

    /**
     * The message resources for this package.
     */
    protected static MessageResources messages =
        MessageResources.getMessageResources(Constants.Package + ".LocalStrings");

    /**
     * The server name to use instead of request.getServerName().
     */
    protected String server = null;

    /**
     * The target window for this base reference.
     */
    protected String target = null;

    public String getTarget() {
        return (this.target);
    }

    public void setTarget(String target) {
        this.target = target;
    }

    /**
     * Process the start of this tag.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        String serverName = (this.server == null) ? request.getServerName() : this.server;
        
        String baseTag =
            renderBaseElement(
                request.getScheme(),
                serverName,
                request.getServerPort(),
                request.getRequestURI());

        JspWriter out = pageContext.getOut();
        try {
            out.write(baseTag);
        } catch (IOException e) {
            pageContext.setAttribute(Globals.EXCEPTION_KEY, e, PageContext.REQUEST_SCOPE);
            throw new JspException(messages.getMessage("common.io", e.toString()));
        }
        
        return EVAL_BODY_INCLUDE;
    }

    /**
     * Render a fully formed HTML &lt;base&gt; element and return it as a String.
     * @param scheme The scheme used in the url (ie. http or https).
     * @param serverName
     * @param port
     * @param uri  The portion of the url from the protocol name up to the query 
     * string.
     * @return String An HTML &lt;base&gt; element.
     * @since Struts 1.1
     */
    protected String renderBaseElement(
        String scheme,
        String serverName,
        int port,
        String uri) {
            
        StringBuffer tag = new StringBuffer("<base href=\"");
        tag.append(RequestUtils.createServerUriStringBuffer(scheme,serverName,port,uri).toString());

        tag.append("\"");
        
        if (this.target != null) {
            tag.append(" target=\"");
            tag.append(this.target);
            tag.append("\"");
        }
        
        if (TagUtils.getInstance().isXhtml(this.pageContext)) {
            tag.append(" />");
        } else {
            tag.append(">");
        }
        
        return tag.toString();
    }
    
    /**
     * Returns the server.
     * @return String
     */
    public String getServer() {
        return this.server;
    }

    /**
     * Sets the server.
     * @param server The server to set
     */
    public void setServer(String server) {
        this.server = server;
    }

}
