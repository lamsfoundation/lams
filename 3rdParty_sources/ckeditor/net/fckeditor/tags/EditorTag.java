/*
 * FCKeditor - The text editor for Internet - http://www.fckeditor.net
 * Copyright (C) 2004-2010 Frederico Caldeira Knabben
 * 
 * == BEGIN LICENSE ==
 * 
 * Licensed under the terms of any of the following licenses at your
 * choice:
 * 
 *  - GNU General Public License Version 2 or later (the "GPL")
 *    http://www.gnu.org/licenses/gpl.html
 * 
 *  - GNU Lesser General Public License Version 2.1 or later (the "LGPL")
 *    http://www.gnu.org/licenses/lgpl.html
 * 
 *  - Mozilla Public License Version 1.1 or later (the "MPL")
 *    http://www.mozilla.org/MPL/MPL-1.1.html
 * 
 * == END LICENSE ==
 */
package net.fckeditor.tags;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import net.fckeditor.FCKeditor;
import net.fckeditor.tool.Utils;

/**
 * Tag creates and configures a {@link FCKeditor} instance. The setter methods
 * are the same as in the FCKeditor class itself.
 * 
 *
 */
public class EditorTag extends TagSupport {

	private static final long serialVersionUID = -173091731589866140L;

	private String instanceName;
	private String inputName;
	private String width;
	private String height;
	private String toolbarSet;
	private String value;
	private String basePath;

	/** The underlying FCKeditor instance */
	private transient FCKeditor fckEditor;

	/**
	 * @see FCKeditor#setInstanceName(String)
	 */
	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}
	
	/**
	 * @see FCKeditor#setInputName(String) 
	 */
	public void setInputName(String inputName) {
		this.inputName = inputName;
	}

	/**
	 * @see FCKeditor#setWidth(String)
	 */
	public void setWidth(String width) {
		this.width = width;
	}

	/**
	 * @see FCKeditor#setHeight(String)
	 */
	public void setHeight(String height) {
		this.height = height;
	}

	/**
	 * @see FCKeditor#setToolbarSet(String)
	 */
	public void setToolbarSet(String toolbarSet) {
		this.toolbarSet = toolbarSet;
	}

	/**
	 * @see FCKeditor#setValue(String)
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @see FCKeditor#setBasePath(String)
	 */
	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	/**
	 * @see FCKeditor#setConfig(String, String)
	 */
	void setConfig(String name, String value) {
		fckEditor.setConfig(name, value);
	}

	@Override
	public int doStartTag() throws JspException {

		try {
			fckEditor = new FCKeditor((HttpServletRequest) pageContext
					.getRequest(), instanceName, inputName);

			if (Utils.isNotEmpty(width))
				fckEditor.setWidth(width);
			if (Utils.isNotEmpty(height))
				fckEditor.setHeight(height);
			if (Utils.isNotEmpty(toolbarSet))
				fckEditor.setToolbarSet(toolbarSet);
			if (Utils.isNotEmpty(value))
				fckEditor.setValue(value);
			if (Utils.isNotEmpty(basePath))
				fckEditor.setBasePath(basePath);

		} catch (Exception e) {
			throw new JspException(e);
		}

		return EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() throws JspException {

		JspWriter out = pageContext.getOut();

		try {
			out.println(fckEditor);
		} catch (IOException e) {
			throw new JspException(
					"Tag response could not be written to the user!", e);
		}

		return EVAL_PAGE;
	}

}