//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.2/xslt/JavaClass.xsl

package org.lamsfoundation.lams.contentrepository.struts.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/** 
 * MyEclipse Struts
 * Creation date: 01-13-2005
 * 
 * XDoclet definition:
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
	 * @param mapping
	 * @param request
	 * @return ActionErrors
	 */
	public ActionErrors validate(
		ActionMapping mapping,
		HttpServletRequest request) {

		ActionErrors errors = super.validate(mapping,request);
		if ( errors == null ) errors = new ActionErrors();
		
		if ( getToolName() == null || getToolName().length() < 1 ) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.mandatory","Tool Name"));
		}
		
		if ( getWorkspaceName() == null || getWorkspaceName().length() < 1 ) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.mandatory","Tool Name"));
		}

		if ( getIndentificationString() == null || getIndentificationString().length() < 1 ) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.mandatory","Tool Name"));
		}

		if ( errors.size() == 0 ) return null;
		return errors;
	}

	/** 
	 * Method reset
	 * @param mapping
	 * @param request
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		setToolName("");
		setWorkspaceName("");
		setIndentificationString("");
	}

	/** 
	 * Returns the toolName.
	 * @return String
	 */
	public String getToolName() {
		return toolName;
	}

	/** 
	 * Set the toolName.
	 * @param toolName The toolName to set
	 */
	public void setToolName(String toolName) {
		this.toolName = toolName;
	}

	/** 
	 * Returns the workspaceName.
	 * @return String
	 */
	public String getWorkspaceName() {
		return workspaceName;
	}

	/** 
	 * Set the workspaceName.
	 * @param workspaceName The workspaceName to set
	 */
	public void setWorkspaceName(String workspaceName) {
		this.workspaceName = workspaceName;
	}

	/** 
	 * Returns the indentificationString.
	 * @return String
	 */
	public String getIndentificationString() {
		return indentificationString;
	}

	/** 
	 * Set the indentificationString.
	 * @param indentificationString The indentificationString to set
	 */
	public void setIndentificationString(String indentificationString) {
		this.indentificationString = indentificationString;
	}

}