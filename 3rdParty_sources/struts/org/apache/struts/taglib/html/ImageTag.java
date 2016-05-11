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


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.apache.struts.Globals;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.taglib.TagUtils;


/**
 * Tag for input fields of type "image".
 *
 * @version $Rev: 164927 $ $Date$
 */

public class ImageTag extends SubmitTag {


    // ------------------------------------------------------------- Properties


    /**
     * The alignment for this image.
     */
    protected String align = null;

    /**
     * @deprecated Align attribute is deprecated in HTML 4.x.
     */
    public String getAlign() {
        return (this.align);
    }

    /**
     * @deprecated Align attribute is deprecated in HTML 4.x.
     */
    public void setAlign(String align) {
        this.align = align;
    }


    /**
     * The border size around the image.
     */
    protected String border = null;

    public String getBorder() {
        return (this.border);
    }

    public void setBorder(String border) {
        this.border = border;
    }


    /**
     * The module-relative URI of the image.
     */
    protected String page = null;

    public String getPage() {
        return (this.page);
    }

    public void setPage(String page) {
        this.page = page;
    }


    /**
     * The message resources key of the module-relative URI of the image.
     */
    protected String pageKey = null;

    public String getPageKey() {
        return (this.pageKey);
    }

    public void setPageKey(String pageKey) {
        this.pageKey = pageKey;
    }

    /**
     * The URL of this image.
     */
    protected String src = null;

    public String getSrc() {
        return (this.src);
    }

    public void setSrc(String src) {
        this.src = src;
    }


    /**
     * The message resources key for the URL of this image.
     */
    protected String srcKey = null;

    public String getSrcKey() {
        return (this.srcKey);
    }

    public void setSrcKey(String srcKey) {
        this.srcKey = srcKey;
    }


    // --------------------------------------------------------- Constructor

    public ImageTag() {
        super();
        property = ""; 
    }

    // --------------------------------------------------------- Protected Methods

    /**
     * Render the opening element.
     *
     * @return The opening part of the element.
     */
    protected String getElementOpen() {
        return "<input type=\"image\"";
    }

    /**
     * Render the button attributes
     * @param results The StringBuffer that output will be appended to.
     */
    protected void prepareButtonAttributes(StringBuffer results)
                      throws JspException {
        String tmp = src();
        if (tmp != null) {
            HttpServletResponse response =
                (HttpServletResponse) pageContext.getResponse();
            prepareAttribute(results, "src", response.encodeURL(tmp));
        }
        prepareAttribute(results, "align", getAlign());
        prepareAttribute(results, "border", getBorder());
        prepareAttribute(results, "value", getValue());
        prepareAttribute(results, "accesskey", getAccesskey());
        prepareAttribute(results, "tabindex", getTabindex());
    }


    /**
     * Release any acquired resources.
     */
    public void release() {

        super.release();
        page = null;
        pageKey = null;
        property = "";
        src = null;
        srcKey = null;

    }


    // ------------------------------------------------------ Protected Methods


    /**
     * Return the base source URL that will be rendered in the <code>src</code>
     * property for this generated element, or <code>null</code> if there is
     * no such URL.
     *
     * @exception JspException if an error occurs
     */
    protected String src() throws JspException {

        // Deal with a direct context-relative page that has been specified
        if (this.page != null) {
            if ((this.src != null) || (this.srcKey != null) ||
                (this.pageKey != null)) {
                JspException e = new JspException
                    (messages.getMessage("imgTag.src"));
                TagUtils.getInstance().saveException(pageContext, e);
                throw e;
            }
            
            ModuleConfig config = (ModuleConfig)
                pageContext.getRequest().getAttribute(Globals.MODULE_KEY);
            
            HttpServletRequest request =
                (HttpServletRequest) pageContext.getRequest();
            
            String pageValue = this.page;
            if (config != null) {
                pageValue = TagUtils.getInstance().pageURL(request,
                                                           this.page,
                                                           config);
            }
            return (request.getContextPath() + pageValue);
        }

        // Deal with an indirect context-relative page that has been specified
        if (this.pageKey != null) {
            if ((this.src != null) || (this.srcKey != null)) {
                JspException e = new JspException
                    (messages.getMessage("imgTag.src"));
                TagUtils.getInstance().saveException(pageContext, e);
                throw e;
            }
            
            ModuleConfig config = (ModuleConfig)
                pageContext.getRequest().getAttribute(Globals.MODULE_KEY);
            
            HttpServletRequest request =
                (HttpServletRequest) pageContext.getRequest();
            
            String pageValue = TagUtils.getInstance().message(
                                          pageContext,
                                          getBundle(),
                                          getLocale(),
                                          this.pageKey);
            if (config != null) {
                pageValue = TagUtils.getInstance().pageURL(request,
                                                           pageValue,
                                                           config);
            }
            return (request.getContextPath() + pageValue);
        }

        // Deal with an absolute source that has been specified
        if (this.src != null) {
            if (this.srcKey != null) {
                JspException e = new JspException
                    (messages.getMessage("imgTag.src"));
                TagUtils.getInstance().saveException(pageContext, e);
                throw e;
            }
            return (this.src);
        }

        // Deal with an indirect source that has been specified
        if (this.srcKey == null) {
            JspException e = new JspException
                (messages.getMessage("imgTag.src"));
            TagUtils.getInstance().saveException(pageContext, e);
            throw e;
        }
        
        return TagUtils.getInstance().message(
            pageContext,
            getBundle(),
            getLocale(),
            this.srcKey);

    }


}
