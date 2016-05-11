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


import java.util.ArrayList;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.struts.util.MessageResources;
import org.apache.struts.taglib.TagUtils;


/**
 * Define a scripting variable based on the value(s) of the specified
 * cookie received with this request.
 *
 * @version $Rev: 54929 $ $Date$
 */

public class CookieTag extends TagSupport {


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
     * Return an array of Cookies if <code>multiple</code> is non-null.
     */
    protected String multiple = null;

    public String getMultiple() {
        return (this.multiple);
    }

    public void setMultiple(String multiple) {
        this.multiple = multiple;
    }


    /**
     * The name of the cookie whose value is to be exposed.
     */
    protected String name = null;

    public String getName() {
        return (this.name);
    }

    public void setName(String name) {
        this.name = name;
    }


    /**
     * The default value to return if no cookie of the specified name is found.
     */
    protected String value = null;

    public String getValue() {
        return (this.value);
    }

    public void setValue(String value) {
        this.value = value;
    }


    // --------------------------------------------------------- Public Methods


    /**
     * Retrieve the required property and expose it as a scripting variable.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {

        // Retrieve the required cookie value(s)
        ArrayList values = new ArrayList();
        Cookie cookies[] =
            ((HttpServletRequest) pageContext.getRequest()).getCookies();
        if (cookies == null)
            cookies = new Cookie[0];

        for (int i = 0; i < cookies.length; i++) {
            if (name.equals(cookies[i].getName()))
                values.add(cookies[i]);
        }
        if ((values.size() < 1) && (value != null))
            values.add(new Cookie(name, value));
        if (values.size() < 1) {
            JspException e = new JspException
                (messages.getMessage("cookie.get", name));
            TagUtils.getInstance().saveException(pageContext, e);
            throw e;
        }

        // Expose an appropriate variable containing these results
        if (multiple == null) {
            Cookie cookie = (Cookie) values.get(0);
            pageContext.setAttribute(id, cookie);
        } else {
            cookies = new Cookie[values.size()];
            pageContext.setAttribute(id, values.toArray(cookies));
        }
        return (SKIP_BODY);

    }


    /**
     * Release all allocated resources.
     */
    public void release() {

        super.release();
        id = null;
        multiple = null;
        name = null;
        value = null;

    }


}
