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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
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