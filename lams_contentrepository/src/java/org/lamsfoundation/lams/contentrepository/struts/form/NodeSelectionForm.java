//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.2/xslt/JavaClass.xsl

package org.lamsfoundation.lams.contentrepository.struts.form;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/** 
 * Didn't want to create this class, but I keep getting "Cannot retrieve definition for form bean null"
 * exception so it was just easier to write it and ignore it...
 * XDoclet definition:
 * @struts:form name="nodeSelectionForm"
 */
public class NodeSelectionForm extends ActionForm {

	// --------------------------------------------------------- Instance Variables

	private Map nodeMap;

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

		return null;
	}

	/** 
	 * Method reset
	 * @param mapping
	 * @param request
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
	}

	/**
	 * @return Returns the nodeMap.
	 */
	public Map getNodeMap() {
		return nodeMap;
	}
	/**
	 * @param nodeMap The nodeMap to set.
	 */
	public void setNodeMap(Map nodeMap) {
		this.nodeMap = nodeMap;
	}
}