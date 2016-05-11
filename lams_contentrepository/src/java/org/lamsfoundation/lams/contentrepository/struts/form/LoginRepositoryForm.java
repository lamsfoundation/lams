/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */


package org.lamsfoundation.lams.contentrepository.struts.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 * MyEclipse Struts
 * Creation date: 01-13-2005
 *
 * XDoclet definition:
 * 
 * @struts:form name="loginRepositoryForm"
 */
public class LoginRepositoryForm extends ActionForm {

    // --------------------------------------------------------- Instance Variables

    /** toolName property */
    private String toolName;

    /** workspaceName property */
    private String workspaceName;

    /** indentificationString property */
    private String indentificationString;

    // --------------------------------------------------------- Methods

    /**
     * Method validate
     * 
     * @param mapping
     * @param request
     * @return ActionErrors
     */
    @Override
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

	ActionErrors errors = super.validate(mapping, request);
	if (errors == null) {
	    errors = new ActionErrors();
	}

	if (getToolName() == null || getToolName().length() < 1) {
	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.mandatory", "Tool Name"));
	}

	if (getWorkspaceName() == null || getWorkspaceName().length() < 1) {
	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.mandatory", "Tool Name"));
	}

	if (getIndentificationString() == null || getIndentificationString().length() < 1) {
	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.mandatory", "Tool Name"));
	}

	if (errors.size() == 0) {
	    return null;
	}
	return errors;
    }

    /**
     * Method reset
     * 
     * @param mapping
     * @param request
     */
    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
	setToolName("");
	setWorkspaceName("");
	setIndentificationString("");
    }

    /**
     * Returns the toolName.
     * 
     * @return String
     */
    public String getToolName() {
	return toolName;
    }

    /**
     * Set the toolName.
     * 
     * @param toolName
     *            The toolName to set
     */
    public void setToolName(String toolName) {
	this.toolName = toolName;
    }

    /**
     * Returns the workspaceName.
     * 
     * @return String
     */
    public String getWorkspaceName() {
	return workspaceName;
    }

    /**
     * Set the workspaceName.
     * 
     * @param workspaceName
     *            The workspaceName to set
     */
    public void setWorkspaceName(String workspaceName) {
	this.workspaceName = workspaceName;
    }

    /**
     * Returns the indentificationString.
     * 
     * @return String
     */
    public String getIndentificationString() {
	return indentificationString;
    }

    /**
     * Set the indentificationString.
     * 
     * @param indentificationString
     *            The indentificationString to set
     */
    public void setIndentificationString(String indentificationString) {
	this.indentificationString = indentificationString;
    }

}