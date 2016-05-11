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
 * Define a scripting variable based on the value(s) of the specified
 * parameter received with this request.
 *
 * @version $Rev: 54929 $ $Date$
 */
public class ParameterTag extends TagSupport {

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
        MessageResources.getMessageResources(
            "org.apache.struts.taglib.bean.LocalStrings");

    /**
     * Return an array of parameter values if <code>multiple</code> is
     * non-null.
     */
    protected String multiple = null;

    public String getMultiple() {
        return (this.multiple);
    }

    public void setMultiple(String multiple) {
        this.multiple = multiple;
    }

    /**
     * The name of the parameter whose value is to be exposed.
     */
    protected String name = null;

    public String getName() {
        return (this.name);
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * The default value to return if no parameter of the specified name is
     * found.
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

        // Deal with a single parameter value
        if (multiple == null) {
            String value = pageContext.getRequest().getParameter(name);
            if ((value == null) && (this.value != null)) {
                value = this.value;
            }

            if (value == null) {
                JspException e =
                    new JspException(messages.getMessage("parameter.get", name));
                TagUtils.getInstance().saveException(pageContext, e);
                throw e;
            }

            pageContext.setAttribute(id, value);
            return (SKIP_BODY);
        }

        // Deal with multiple parameter values
        String values[] = pageContext.getRequest().getParameterValues(name);
        if ((values == null) || (values.length == 0)) {
            if (this.value != null) {
                values = new String[] { this.value };
            }
        }

        if ((values == null) || (values.length == 0)) {
            JspException e =
                new JspException(messages.getMessage("parameter.get", name));
            TagUtils.getInstance().saveException(pageContext, e);
            throw e;
        }
        
        pageContext.setAttribute(id, values);
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
