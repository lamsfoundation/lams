/*
 *
 *
 * Copyright 2001-2004 The Apache Software Foundation.
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
 * Convenience base class for the various input tags for text fields.
 *
 * @version $Rev: 54929 $ $Date$
 */

public abstract class BaseFieldTag extends BaseInputTag {

    // ----------------------------------------------------- Instance Variables

    /**
     * Comma-delimited list of content types that a server processing this form
     * will handle correctly.  This property is defined only for the
     * <code>file</code> tag, but is implemented here because it affects the
     * rendered HTML of the corresponding &lt;input&gt; tag.
     */
    protected String accept = null;

    public String getAccept() {
        return (this.accept);
    }

    public void setAccept(String accept) {
        this.accept = accept;
    }

    /**
     * The "redisplay contents" flag (used only on <code>password</code>).
     */
    protected boolean redisplay = true;

    public boolean getRedisplay() {
        return (this.redisplay);
    }

    public void setRedisplay(boolean redisplay) {
        this.redisplay = redisplay;
    }

    /**
     * The type of input field represented by this tag (text, password, or
     * hidden).
     */
    protected String type = null;

    // --------------------------------------------------------- Public Methods

    /**
     * Generate the required input tag.
     * <p>
     * Support for indexed property since Struts 1.1
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {
        
        TagUtils.getInstance().write(this.pageContext, this.renderInputElement());

        return (EVAL_BODY_TAG);

    }

    /**
     * Renders a fully formed &lt;input&gt; element.
     * @throws JspException
     * @since Struts 1.2
     */
    protected String renderInputElement() throws JspException {
        StringBuffer results = new StringBuffer("<input");

        prepareAttribute(results, "type", this.type);
        prepareAttribute(results, "name", prepareName());
        prepareAttribute(results, "accesskey", getAccesskey());
        prepareAttribute(results, "accept", getAccept());
        prepareAttribute(results, "maxlength", getMaxlength());
        prepareAttribute(results, "size", getCols());
        prepareAttribute(results, "tabindex", getTabindex());
        prepareValue(results);
        results.append(this.prepareEventHandlers());
        results.append(this.prepareStyles());
        prepareOtherAttributes(results);
        results.append(this.getElementClose());
        return results.toString();
    }

    /**
     * Render the value element
     * @param results The StringBuffer that output will be appended to.
     */
    protected void prepareValue(StringBuffer results) throws JspException {

        results.append(" value=\"");
        if (value != null) {
            results.append(this.formatValue(value));

        } else if (redisplay || !"password".equals(type)) {
            Object value =
                TagUtils.getInstance().lookup(pageContext, name, property, null);

            results.append(this.formatValue(value));
        }

        results.append('"');

    }
    
    /**
     * Return the given value as a formatted <code>String</code>.  This 
     * implementation escapes potentially harmful HTML characters.
     *
     * @param value The value to be formatted. <code>null</code> values will
     * be returned as the empty String "".
     * 
     * @throws JspException if a JSP exception has occurred
     * 
     * @since Struts 1.2
     */
    protected String formatValue(Object value) throws JspException {
        if (value == null) {
            return "";
        }

        return TagUtils.getInstance().filter(value.toString());
    }

    /**
     * Release any acquired resources.
     */
    public void release() {

        super.release();
        accept = null;
        name = Constants.BEAN_KEY;
        redisplay = true;

    }

}
