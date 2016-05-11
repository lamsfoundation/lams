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

import org.apache.struts.config.ModuleConfig;
import org.apache.struts.taglib.TagUtils;
import org.apache.struts.util.MessageResources;


/**
 * Define a scripting variable that exposes the requested Struts
 * internal configuraton object.
 *
 * @version $Rev: 54929 $ $Date$
 */

public class StrutsTag extends TagSupport {


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
     * The name of the <code>ActionFormBean</code> object to be exposed.
     */
    protected String formBean = null;

    public String getFormBean() {
        return (this.formBean);
    }

    public void setFormBean(String formBean) {
        this.formBean = formBean;
    }


    /**
     * The name of the <code>ActionForward</code> object to be exposed.
     */
    protected String forward = null;

    public String getForward() {
        return (this.forward);
    }

    public void setForward(String forward) {
        this.forward = forward;
    }


    /**
     * The name of the <code>ActionMapping</code> object to be exposed.
     */
    protected String mapping = null;

    public String getMapping() {
        return (this.mapping);
    }

    public void setMapping(String mapping) {
        this.mapping = mapping;
    }


    // --------------------------------------------------------- Public Methods


    /**
     * Retrieve the required configuration object and expose it as a
     * scripting variable.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {

        // Validate the selector arguments
        int n = 0;
        if (formBean != null)
            n++;
        if (forward != null)
            n++;
        if (mapping != null)
            n++;
        if (n != 1) {
            JspException e = new JspException
                (messages.getMessage("struts.selector"));
            TagUtils.getInstance().saveException(pageContext, e);
            throw e;
        }

        // Retrieve our module configuration information
        ModuleConfig config = TagUtils.getInstance().getModuleConfig(pageContext);

        // Retrieve the requested object to be exposed
        Object object = null;
        String selector = null;
        if (formBean != null) {
            selector = formBean;
            object = config.findFormBeanConfig(formBean);
        } else if (forward != null) {
            selector = forward;
            object = config.findForwardConfig(forward);
        } else if (mapping != null) {
            selector = mapping;
            object = config.findActionConfig(mapping);
        }
        if (object == null) {
            JspException e = new JspException
                (messages.getMessage("struts.missing", selector));
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
        formBean = null;
        forward = null;
        mapping = null;

    }


}
