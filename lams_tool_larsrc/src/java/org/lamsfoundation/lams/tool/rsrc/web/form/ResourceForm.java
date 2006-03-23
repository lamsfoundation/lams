package org.lamsfoundation.lams.tool.rsrc.web.form;

import org.apache.log4j.Logger;
import org.apache.struts.validator.ValidatorForm;
import org.lamsfoundation.lams.tool.rsrc.model.Resource;

/**
 *
 * 	Message Form.
 *	@struts.form name="resourceForm"
 *
 * User: Dapeng.Ni
 */
public class ResourceForm extends ValidatorForm {
	private static final long serialVersionUID = -6054354910960460120L;
	private static Logger logger = Logger.getLogger(ResourceForm.class.getName());
	
	
	public void setResource(Resource resource) {
		//TODO
	}


}
