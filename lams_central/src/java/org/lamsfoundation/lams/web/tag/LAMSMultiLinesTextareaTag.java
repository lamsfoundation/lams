package org.lamsfoundation.lams.web.tag;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.DynamicAttributes;

import org.apache.commons.lang.StringUtils;

/**
 * @author steven
 */
public class LAMSMultiLinesTextareaTag extends BodyTagSupport implements DynamicAttributes {

    private static final long serialVersionUID = 7335341412469834337L;

    private final List<String> keys = new ArrayList<String>();
    private final List<Object> values = new ArrayList<Object>();

    private String id;
    private String name;
    private String onchange;
    private String disabled;

    @Override
    public int doStartTag() throws JspException {
	return super.doStartTag();
    }

    @Override
    public int doEndTag() throws JspException {
	String tagName = getName();
	String hiddenId = tagName + "__lamshidden";
	try {
	    //add Javascript event handler
	    String chbr = "filterData(this,document.getElementById('" + hiddenId + "'));";
	    String onChange = this.getOnchange();
	    if (onChange == null) {
		onChange = chbr;
	    } else if (onChange.indexOf(chbr) == -1) {
		if (onChange.endsWith(";")) {
		    onChange += chbr;
		} else {
		    onChange = onChange + ";" + chbr;
		}
	    }
	    this.setOnchange(onChange);

	    //reset some values to another in order to use them in hidden field.
	    String oldProperty = this.name;
	    BodyContent oldBodyContent = this.getBodyContent();

	    this.name += "__textarea";
	    if (StringUtils.isEmpty(this.getId())) {
		this.setId(tagName);
	    }
	    StringBuffer results = new StringBuffer(
		    renderTextareaElement(getDataNoBr(oldBodyContent == null ? null : oldBodyContent.getString())));

	    //construct hidden variable
	    results.append("<input type=\"hidden\"");
	    prepareAttribute(results, "name", oldProperty);
	    prepareAttribute(results, "id", hiddenId);
	    results.append("/>");

	    //onload script to reset hidden value, so it can works even onChange event does not happen when edit.
	    //the reason of why not directly assign value to hidden variable is
	    //hidden variable treat < and &lt; are same value when submit to form. The only way is use Javascript
	    //reset it value.
	    StringBuffer filterScript = new StringBuffer("<script language='javascript'>filterData(");
	    filterScript.append("document.getElementById('").append(this.getId())
		    .append("'),document.getElementById('");
	    filterScript.append(hiddenId);
	    filterScript.append("'));</script>");
	    results.append(filterScript);

	    pageContext.getOut().write(results.toString());
	} catch (Exception e) {
	    throw new JspException(e);
	}

	keys.clear();
	values.clear();
	// reset id and onChange as these are optional, and if they aren't included and there is more than one
	// instance of this tag on the page, then the same values are used for all tags.
	this.setId(null);
	this.setOnchange(null);

	return super.doEndTag();
    }

    /**
     * Generate an HTML &lt;textarea&gt; tag.
     *
     * @throws JspException
     * @throws UnsupportedEncodingException
     * @since Struts 1.1
     */
    protected String renderTextareaElement(String value) throws JspException, UnsupportedEncodingException {
	StringBuffer results = new StringBuffer("<textarea");

	prepareAttribute(results, "name", getName());
	prepareAttribute(results, "id", this.getId());
	prepareAttribute(results, "onchange", getOnchange());
	String disabled = getDisabled();
	if (StringUtils.isBlank(disabled) || Boolean.FALSE.toString().equalsIgnoreCase(disabled)) {
	    setDisabled(null);
	    keys.remove("disabled");
	} else {
	    prepareAttribute(results, "disabled", "disabled");
	}

	appendDynamicParam(results);

	results.append(">");

	results.append(value);

	results.append("</textarea>");
	return results.toString();
    }

    /**
     * Change input string &lt;BR&gt; tag into \n or \r\n (dependent on OS).
     *
     * @param data
     * @return
     * @throws JspException
     */
    private String getDataNoBr(String data) throws JspException {

	if (data != null) {
	    //comment: although window OS should be \r\n format, but in some case, it only include "\n" as well.
	    //so it is not safe to do replacement
//    		if(os.toLowerCase().indexOf("win") != -1)
//    			data = data.replaceAll("<BR>","\r\n");
//    		else
	    data = data.replaceAll("<BR>", "\n");
	}
	return data == null ? "" : data;
    }

    /**
     * @param sb
     *            StringBuffer
     * @throws UnsupportedEncodingException
     */
    private void appendDynamicParam(StringBuffer sb) throws UnsupportedEncodingException {
	for (int idx = 0; idx < keys.size(); idx++) {
	    sb.append(" ");
	    String key = keys.get(idx);
	    Object value = values.get(idx) == null ? "" : values.get(idx).toString();

	    prepareAttribute(sb, key, value);
	}

    }

    protected void prepareAttribute(StringBuffer handlers, String name, Object value) {
	if (value != null) {
	    handlers.append(" ");
	    handlers.append(name);
	    handlers.append("=\"");
	    handlers.append(value);
	    handlers.append("\"");
	}
    }

    @Override
    public void setDynamicAttribute(String url, String localName, Object value) throws JspException {
	keys.add(localName);
	values.add(value);
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getOnchange() {
	return onchange;
    }

    public void setOnchange(String onchange) {
	this.onchange = onchange;
    }

    @Override
    public String getId() {
	return id;
    }

    @Override
    public void setId(String id) {
	this.id = id;
    }

    public String getDisabled() {
	return disabled;
    }

    public void setDisabled(String disabled) {
	this.disabled = disabled;
    }
}