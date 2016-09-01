package org.lamsfoundation.lams.web.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.springframework.web.util.HtmlUtils;

/**
 * JSP tag. It converts text from \n or \r\n to &lt;BR&gt; before rendering.
 *
 *
 *
 * Converts text from \n or \r\n to &lt;BR&gt; before rendering
 *
 * @author steven
 */
public class MultiLinesOutputTag extends SimpleTagSupport {

    private String value;
    private boolean escapeHtml;

    @Override
    public void doTag() throws JspException, IOException {
	if (escapeHtml) {
	    value = HtmlUtils.htmlEscape(value);
	}
	value = value.replaceAll("\n", "<br>");
	getJspContext().getOut().write(value.toString());
    }

    /**
     *
     * @return
     */
    public String getValue() {
	return value;
    }

    public void setValue(String value) {
	this.value = value;
    }

    /**
     *
     */
    public boolean getEscapeHtml() {
	return this.escapeHtml;
    }

    public void setEscapeHtml(boolean escapeHtml) {
	this.escapeHtml = escapeHtml;
    }
}
