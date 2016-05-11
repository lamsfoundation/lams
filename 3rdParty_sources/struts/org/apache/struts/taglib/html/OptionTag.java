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

import java.util.Locale;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.struts.Globals;
import org.apache.struts.taglib.TagUtils;
import org.apache.struts.util.MessageResources;

/**
 * Tag for select options.  The body of this tag is presented to the user
 * in the option list, while the value attribute is the value returned to
 * the server if this option is selected.
 *
 * @version $Rev: 54929 $ $Date$
 */
public class OptionTag extends BodyTagSupport {

    // ----------------------------------------------------- Instance Variables

    /**
     * The default locale for our server.
     * @deprecated Use Locale.getDefault() directly.
     */
    protected static final Locale defaultLocale = Locale.getDefault();

    /**
     * The message resources for this package.
     */
    protected static MessageResources messages =
        MessageResources.getMessageResources(Constants.Package + ".LocalStrings");

    /**
     * The message text to be displayed to the user for this tag (if any)
     */
    protected String text = null;

    // ------------------------------------------------------------- Properties

    /**
     * The name of the servlet context attribute containing our message
     * resources.
     */
    protected String bundle = Globals.MESSAGES_KEY;

    public String getBundle() {
        return (this.bundle);
    }

    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

    /**
     * Is this option disabled?
     */
    protected boolean disabled = false;

    public boolean getDisabled() {
        return (this.disabled);
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    /**
     * The key used to look up the text displayed to the user for this
     * option, if any.
     */
    protected String key = null;

    public String getKey() {
        return (this.key);
    }

    public void setKey(String key) {
        this.key = key;
    }

    /**
     * The name of the attribute containing the Locale to be used for
     * looking up internationalized messages.
     */
    protected String locale = Globals.LOCALE_KEY;

    public String getLocale() {
        return (this.locale);
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    /**
     * The style associated with this tag.
     */
    private String style = null;

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    /**
     * The named style class associated with this tag.
     */
    private String styleClass = null;

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    /**
     * The identifier associated with this tag.
     */
    protected String styleId = null;

    /**
     * Return the style identifier for this tag.
     */
    public String getStyleId() {

        return (this.styleId);

    }

    /**
     * Set the style identifier for this tag.
     *
     * @param styleId The new style identifier
     */
    public void setStyleId(String styleId) {

        this.styleId = styleId;

    }

    /**
     * The server value for this option, also used to match against the
     * current property value to determine whether this option should be
     * marked as selected.
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
     * Process the start of this tag.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {

        // Initialize the placeholder for our body content
        this.text = null;

        // Do nothing until doEndTag() is called
        return (EVAL_BODY_TAG);

    }

    /**
     * Process the body text of this tag (if any).
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doAfterBody() throws JspException {

        if (bodyContent != null) {
            String text = bodyContent.getString();
            if (text != null) {
                text = text.trim();
                if (text.length() > 0) {
                    this.text = text;
                }
            }
        }
        return (SKIP_BODY);

    }

    /**
     * Process the end of this tag.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doEndTag() throws JspException {

        TagUtils.getInstance().write(pageContext, this.renderOptionElement());

        return (EVAL_PAGE);
    }

    /**
     * Generate an HTML %lt;option&gt; element.
     * @throws JspException
     * @since Struts 1.1
     */
    protected String renderOptionElement() throws JspException {
        StringBuffer results = new StringBuffer("<option value=\"");
        
        results.append(this.value);
        results.append("\"");
        if (disabled) {
            results.append(" disabled=\"disabled\"");
        }
        if (this.selectTag().isMatched(this.value)) {
            results.append(" selected=\"selected\"");
        }
        if (style != null) {
            results.append(" style=\"");
            results.append(style);
            results.append("\"");
        }
        if (styleId != null) {
            results.append(" id=\"");
            results.append(styleId);
            results.append("\"");
        }
        if (styleClass != null) {
            results.append(" class=\"");
            results.append(styleClass);
            results.append("\"");
        }
        results.append(">");

        results.append(text());
        
        results.append("</option>");
        return results.toString();
    }

    /**
     * Acquire the select tag we are associated with.
     * @throws JspException
     */
    private SelectTag selectTag() throws JspException {
        SelectTag selectTag =
            (SelectTag) pageContext.getAttribute(Constants.SELECT_KEY);
            
        if (selectTag == null) {
            JspException e =
                new JspException(messages.getMessage("optionTag.select"));
                
            TagUtils.getInstance().saveException(pageContext, e);
            throw e;
        }
        
        return selectTag;
    }

    /**
     * Release any acquired resources.
     */
    public void release() {
        super.release();
        bundle = Globals.MESSAGES_KEY;
        disabled = false;
        key = null;
        locale = Globals.LOCALE_KEY;
        style = null;
        styleClass = null;
        text = null;
        value = null;
    }

    // ------------------------------------------------------ Protected Methods

    /**
     * Return the text to be displayed to the user for this option (if any).
     *
     * @exception JspException if an error occurs
     */
    protected String text() throws JspException {
        String optionText = this.text;

        if ((optionText == null) && (this.key != null)) {
            optionText =
                TagUtils.getInstance().message(pageContext, bundle, locale, key);
        }

        // no body text and no key to lookup so display the value
        if (optionText == null) {
            optionText = this.value;    
        }

        return optionText;
    }

}
