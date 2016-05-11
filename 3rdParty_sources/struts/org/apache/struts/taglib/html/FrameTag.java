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

import javax.servlet.jsp.JspException;

import org.apache.struts.taglib.TagUtils;

/**
 * Generate an HTML <code>&lt;frame&gt;</code> tag with similar capabilities
 * as those the <code>&lt;html:link&gt;</code> tag provides for hyperlink
 * elements.  The <code>src</code> element is rendered using the same technique
 * that {@link LinkTag} uses to render the <code>href</code> attribute of a
 * hyperlink.  Additionall, the HTML 4.0
 * frame tag attributes <code>noresize</code>, <code>scrolling</code>,
 * <code>marginheight</code>, <code>marginwidth</code>,
 * <code>frameborder</code>, and <code>longdesc</code> are supported.
 * The frame
 * <code>name</code> attribute is rendered based on the <code>frameName</code>
 * property.
 *
 * Note that the value of <code>longdesc</code> is intended to be a URI, but
 * currently no rewriting is supported.  The attribute is set directly from
 * the property value.
 *
 * @version $Rev: 54929 $ $Date$
 * @since Struts 1.1
 */
public class FrameTag extends LinkTag {


    // ------------------------------------------------------------- Properties


    /**
     * The frameborder attribute that should be rendered (1, 0).
     */
    protected String frameborder = null;

    public String getFrameborder() {
        return (this.frameborder);
    }

    public void setFrameborder(String frameborder) {
        this.frameborder = frameborder;
    }


    /**
     * The <code>name</code> attribute that should be rendered for this frame.
     */
    protected String frameName = null;

    public String getFrameName() {
        return (this.frameName);
    }

    public void setFrameName(String frameName) {
        this.frameName = frameName;
    }


    /**
     * URI of a long description of this frame (complements title).
     */
    protected String longdesc = null;

    public String getLongdesc() {
        return (this.longdesc);
    }

    public void setLongdesc(String longdesc) {
        this.longdesc = longdesc;
    }


    /**
     * The margin height in pixels, or zero for no setting.
     */
    protected Integer marginheight = null;

    public Integer getMarginheight() {
        return (this.marginheight);
    }

    public void setMarginheight(Integer marginheight) {
        this.marginheight = marginheight;
    }


    /**
     * The margin width in pixels, or null for no setting.
     */
    protected Integer marginwidth = null;

    public Integer getMarginwidth() {
        return (this.marginwidth);
    }

    public void setMarginwidth(Integer marginwidth) {
        this.marginwidth = marginwidth;
    }


    /**
     * Should users be disallowed to resize the frame?
     */
    protected boolean noresize = false;

    public boolean getNoresize() {
        return (this.noresize);
    }

    public void setNoresize(boolean noresize) {
        this.noresize = noresize;
    }


    /**
     * What type of scrolling should be supported (yes, no, auto)?
     */
    protected String scrolling = null;

    public String getScrolling() {
        return (this.scrolling);
    }

    public void setScrolling(String scrolling) {
        this.scrolling = scrolling;
    }


    // --------------------------------------------------------- Public Methods


    /**
     * Render the appropriately encoded URI.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {

    // Print this element to our output writer
        StringBuffer results = new StringBuffer("<frame");

        prepareAttribute(results, "src", calculateURL());
        prepareAttribute(results, "name", getFrameName());

        if (noresize) {
            results.append(" noresize=\"noresize\"");
        }
        prepareAttribute(results, "scrolling", getScrolling());
        prepareAttribute(results, "marginheight", getMarginheight());
        prepareAttribute(results, "marginwidth", getMarginwidth());
        prepareAttribute(results, "frameborder", getFrameborder());
        prepareAttribute(results, "longdesc", getLongdesc());
        results.append(prepareStyles());
        prepareOtherAttributes(results);
        results.append(getElementClose());
        TagUtils.getInstance().write(pageContext,results.toString());

        return (SKIP_BODY);

    }


    /**
     * Ignore the end of this tag.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doEndTag() throws JspException {

        return (EVAL_PAGE);

    }


    /**
     * Release any acquired resources.
     */
    public void release() {

        super.release();
        frameborder = null;
        frameName = null;
        longdesc = null;
        marginheight = null;
        marginwidth = null;
        noresize = false;
        scrolling = null;

    }


}
