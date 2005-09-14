package org.lamsfoundation.lams.web.development;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

/**
 * @author fmalikoff
 * 
 * @struts:form name="WDDXPostActionForm"
 * 		include-pk="true"
 * 		include-all="true"
 */
public class WDDXPostActionForm extends ActionForm {

	public static final String formName = "WDDXPostActionForm"; // must match name in @struts:action section above

	private static Logger log = Logger.getLogger(WDDXPostActionForm.class);

	/**
	 * @return Returns the action.
	 */
	public String getUrlAction() {
		return urlAction;
	}
	/**
	 * @param action The action to set.
	 */
	public void setUrlAction(String action) {
		this.urlAction = action;
	}
	/**
	 * @return Returns the wddxFile.
	 */
	public FormFile getWddxFile() {
		return wddxFile;
	}
	/**
	 * @param wddxFile The wddxFile to set.
	 */
	public void setWddxFile(FormFile wddxFile) {
		this.wddxFile = wddxFile;
	}
	private String urlAction;
	private FormFile wddxFile;

	public WDDXPostActionForm() {
	}

	/**
	 * Reset all properties to their default values.
	 *
	 * @param mapping The mapping used to select this instance
	 * @param request The servlet request we are processing
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
	}


}