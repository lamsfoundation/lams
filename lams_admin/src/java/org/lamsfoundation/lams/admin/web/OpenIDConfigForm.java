package org.lamsfoundation.lams.admin.web;

import org.apache.struts.action.ActionForm;

/**
 * 
 * @author lfoxton
 * @struts.form name="openIDForm"
 */
public class OpenIDConfigForm extends ActionForm {

	private static final long serialVersionUID = 1453453453463790L;
	
	private Boolean openIDEnabled;
	private String portalURL;
	private String trustedIDPs;
	
	public OpenIDConfigForm() {}

	public Boolean getOpenIDEnabled() {
		return openIDEnabled;
	}

	public void setOpenIDEnabled(Boolean openIDEnabled) {
		this.openIDEnabled = openIDEnabled;
	}

	public String getPortalURL() {
		return portalURL;
	}

	public void setPortalURL(String portalURL) {
		this.portalURL = portalURL;
	}

	public String getTrustedIDPs() {
		return trustedIDPs;
	}

	public void setTrustedIDPs(String trustedIDPs) {
		this.trustedIDPs = trustedIDPs;
	}
}
