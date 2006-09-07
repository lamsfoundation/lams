package org.lamsfoundation.lams.web.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.lang.StringUtils;
/**
 * JSP tag. It converts text from \n or \r\n to &lt;BR&gt; before rendering.
 *  @jsp.tag name="out"
 * 		body-content="empty"
 * 		display-name="converts text from \n or \r\n to &lt;BR&gt; before rendering"
 * 		description="converts text from \n or \r\n to &lt;BR&gt; before rendering"
 * @jsp.
 * @author steven
 *
 */
public class MultiLinesOutputTag extends SimpleTagSupport{
	private static String os = (String) System.getProperties().get("os.name");
	
	private String value;
	
	@Override
	public void doTag() throws JspException, IOException {
		if(StringUtils.isEmpty(value))
			getJspContext().getOut().write(value);
		
		//change back
		if(os.toLowerCase().indexOf("win") != -1)
			value = value.replaceAll("\r\n","<BR>");
		else
			value = value.replaceAll("\n","<BR>");
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

}
