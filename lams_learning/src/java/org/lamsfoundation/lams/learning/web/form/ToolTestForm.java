/*
 * Created on 27/01/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.learning.web.form;

import org.apache.struts.action.ActionForm;

/**
 * 
 * XDoclet definition:
 * @struts:form name="toolTestForm"
 */
public class ToolTestForm extends ActionForm {
	
	private Long activityId;

	public Long getActivityId() {
		return activityId;
	}
	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}
	
}
