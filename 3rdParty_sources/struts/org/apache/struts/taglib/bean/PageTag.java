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


import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.struts.util.MessageResources;
import org.apache.struts.taglib.TagUtils;


/**
 * Define a scripting variable that exposes the requested page context
 * item as a scripting variable and a page scope bean.
 *
 * @version $Rev: 54929 $ $Date$
 */

public class PageTag extends TagSupport {


    // ------------------------------------------------------------- Properties


    /**
     * The name of the scripting variable that will be exposed as a page
     * scope attribute.
     */
    protected String id = null;

    public String getId() {
        return (this.id);
    }

    public void setId(String id) {
        this.id = id;
    }


    /**
     * The message resources for this package.
     */
    protected static MessageResources messages =
        MessageResources.getMessageResources
        ("org.apache.struts.taglib.bean.LocalStrings");


    /**
     * The name of the page context property to be retrieved.
     */
    protected String property = null;

    public String getProperty() {
        return (this.property);
    }

    public void setProperty(String property) {
        this.property = property;
    }


    // --------------------------------------------------------- Public Methods


    /**
     * Retrieve the required configuration object and expose it as a
     * scripting variable.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {

        // Retrieve the requested object to be exposed
        Object object = null;
        if ("application".equalsIgnoreCase(property))
            object = pageContext.getServletContext();
        else if ("config".equalsIgnoreCase(property))
            object = pageContext.getServletConfig();
        else if ("request".equalsIgnoreCase(property))
            object = pageContext.getRequest();
        else if ("response".equalsIgnoreCase(property))
            object = pageContext.getResponse();
        else if ("session".equalsIgnoreCase(property))
            object = pageContext.getSession();
        else {
            JspException e = new JspException
                (messages.getMessage("page.selector", property));
            TagUtils.getInstance().saveException(pageContext, e);
            throw e;
        }

        // Expose this value as a scripting variable
        pageContext.setAttribute(id, object);
        return (SKIP_BODY);

    }


    /**
     * Release all allocated resources.
     */
    public void release() {

        super.release();
        id = null;
        property = null;

    }


}
