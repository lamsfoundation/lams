//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.2/xslt/JavaClass.xsl

package org.lamsfoundation.lams.learning.web.form;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/** 
 * MyEclipse Struts
 * Creation date: 01-12-2005
 * 
 * XDoclet definition:
 * @struts:form name="activityForm"
 */
public class ActivityForm extends ActionForm {

	/** Unique identifier specifying the session for this activity, maps back to
	 * LearnerProgress (or Learner) and Activity. Note that the activity may already
	 * be complete.
	 */
	private Long activityId;
		
	/** List of ActivityURL, will only contain one if a simple activity */
	private List activityURLs;
	

	/** 
	 * Method validate
	 * @param mapping
	 * @param request
	 * @return ActionErrors
	 */
	public ActionErrors validate(
		ActionMapping mapping,
		HttpServletRequest request) {

		throw new UnsupportedOperationException("Generated method 'validate(...)' not implemented.");
	}

	/** 
	 * Method reset
	 * @param mapping
	 * @param request
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		activityURLs = null;
	}

	public List getActivityURLs() {
		return activityURLs;
	}
	public void setActivityURLs(List activityURLs) {
		this.activityURLs = activityURLs;
	}
	public Long getActivityId() {
		return activityId;
	}
	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}
}