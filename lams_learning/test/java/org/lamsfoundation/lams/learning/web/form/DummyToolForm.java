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
 * @struts:form name="dummyToolForm"
 */
public class DummyToolForm extends ActionForm {
	
	private Long toolSessionId;

	public Long getToolSessionId() {
		return toolSessionId;
	}
	public void setToolSessionId(Long toolSessionId) {
		this.toolSessionId = toolSessionId;
	}
	
}
