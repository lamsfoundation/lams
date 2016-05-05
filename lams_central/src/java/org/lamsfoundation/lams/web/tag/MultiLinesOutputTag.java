package org.lamsfoundation.lams.web.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * JSP tag. It converts text from \n or \r\n to &lt;BR&gt; before rendering.
 *
 * @jsp.tag name="out" body-content="empty"
 *          display-name="Converts text from \n or \r\n to &lt;BR&gt; before rendering"
 *          description="Converts text from \n or \r\n to &lt;BR&gt; before rendering"
 *
 * @author steven
 */
public class MultiLinesOutputTag extends SimpleTagSupport {

    private String value;
    private boolean escapeHtml;

    @Override
    public void doTag() throws JspException, IOException {
	if (escapeHtml) {
	    value = StringEscapeUtils.escapeHtml(value);
	}
	value = value.replaceAll("\n", "<br>");
	getJspContext().getOut().write(value.toString());
    }

    /**
     * @jsp.attribute required="true" rtexprvalue="true"
     * @return
     */
    public String getValue() {
	return value;
    }

    public void setValue(String value) {
	this.value = value;
    }

    /**
     * @jsp.attribute required="false" rtexprvalue="true" description="escape html characters"
     */
    public boolean getEscapeHtml() {
	return this.escapeHtml;
    }

    public void setEscapeHtml(boolean escapeHtml) {
	this.escapeHtml = escapeHtml;
    }
}
