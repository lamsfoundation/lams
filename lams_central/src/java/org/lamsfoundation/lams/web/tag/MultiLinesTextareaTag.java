/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.web.tag;


import javax.servlet.jsp.JspException;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.taglib.html.TextareaTag;
/**
 * Customerized HTML textarea tag. This tag must used with /lams_web_root/includes/javascript/common.js. 
 * This Tag is 100% compatiable with STRUTS HTML:textarea tag. But this tag can save break line character into 
 * HTML &lt;BR&gt;. Any  specified string for characters that are sensitive to HTML interpreters will be replace 
 * as well. So the content saved into database thru this tag will 100% compatiable with FCKEditor content.
 * 
 * @author Steve.Ni
 * 
 * @version $Revision$
 */
public class MultiLinesTextareaTag extends TextareaTag {
	private static String os = (String) System.getProperties().get("os.name");
    /**
	 * 
	 */
	private static final long serialVersionUID = -2282473095816882899L;

	/**
     * Generate an HTML &lt;textarea&gt; tag. 
     * @override
     * @throws JspException
     */
    protected String renderTextareaElement() throws JspException {
    	if(this.property == null)
    		return super.renderTextareaElement();
    	String tagName = prepareName();
    	String hiddenId = tagName + "__lamshidden";
    	
    	//add Javascript event handler
    	String chbr ="filterData(this,document.getElementById('" + hiddenId + "'));"; 
    	String onChange = this.getOnchange();
    	if(onChange == null)
    		onChange = chbr;
    	else if(onChange.indexOf(chbr) == -1){
    		if(onChange.endsWith(";"))
    			onChange += chbr;
    		else
    			onChange = onChange + ";" + chbr;
    	}
    	this.setOnchange(onChange);
    	
    	//reset some values to another in order to use them in hidden field. 
    	String oldProperty= this.property;
    	String oldValue = this.value;
    	String oldId = this.getStyleId();
    	
    	this.value=getDataNoBr(this.value);
    	this.property +="__textarea";
    	if(StringUtils.isEmpty(this.getStyleId()))
    		this.setStyleId(tagName + "__lamstextarea");
    	StringBuffer results = new StringBuffer(super.renderTextareaElement());
    	this.property = oldProperty;
    	
    	
    	//construct hidden variable
    	results.append("<input type=\"hidden\"");
        prepareAttribute(results, "name", prepareName());
        prepareAttribute(results, "id", hiddenId);
        results.append("/>");
        
        //onload script to reset hidden value, so it can works even onChange event does not happen when edit. 
        //the reason of why not directly assign value to hidden variable is
        //hidden variable treat < and &lt; are same value when submit to form. The only way is use Javascript 
        //reset it value.
        StringBuffer filterScript = new StringBuffer("<script language='javascript'>filterData(");
        filterScript.append("document.getElementById('").append(this.getStyleId()).append("'),document.getElementById('");
        filterScript.append(hiddenId);
        filterScript.append("'));</script>");
        results.append(filterScript);
        
        //restore value
        this.value = oldValue;
        this.setStyleId(oldId);
        return results.toString();
    }
    /**
     * Change input string &lt;BR&gt; tag into \n or \r\n (dependent on OS).
     * @param data
     * @return
     * @throws JspException
     */
    private String getDataNoBr(String data) throws JspException{
        if (data == null) {
            data = this.lookupProperty(this.name, this.property);
        }
    	if(data != null){
    		//change back
    		if(os.toLowerCase().indexOf("win") != -1)
    			data = data.replaceAll("<BR>","\r\n");
    		else
    			data = data.replaceAll("<BR>","\n");
    	}
    	return data == null?"":data;
    }

    /**
     * Renders the value displayed in the &lt;textarea&gt; tag.
     * @override
     * @throws JspException
     */
    protected String renderData() throws JspException {
        String data = this.value;

        if (data == null) {
            data = this.lookupProperty(this.name, this.property);
        }
        
        return (data == null) ? "":data;
    }
}
