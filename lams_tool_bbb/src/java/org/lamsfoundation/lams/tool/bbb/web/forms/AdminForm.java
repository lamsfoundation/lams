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


package org.lamsfoundation.lams.tool.bbb.web.forms;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;
import org.lamsfoundation.lams.tool.bbb.util.Constants;

/**
 *
 * @author Ernie Ghiglione
 */
public class AdminForm extends ActionForm {

    private static final long serialVersionUID = 8367278543453322252L;

    private String serverURL;

    private String securitySalt;

    // Fields

    public String getServerURL() {
	return serverURL;
    }

    public String getSecuritySalt() {
	return securitySalt;
    }

    public void setServerURL(String serverURL) {
	this.serverURL = serverURL;
    }

    public void setSecuritySalt(String securitySalt) {
	this.securitySalt = securitySalt;
    }

    @Override
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

	ActionErrors errors = new ActionErrors();

	MessageResources resources = MessageResources.getMessageResources(Constants.APP_RESOURCES);

	if (StringUtils.isBlank(this.serverURL)) {
	    errors.add(ActionMessages.GLOBAL_MESSAGE,
		    new ActionMessage("error.mandatoryField", resources.getMessage("config.serverURL")));
	}

	if (StringUtils.isBlank(this.securitySalt)) {
	    errors.add(ActionMessages.GLOBAL_MESSAGE,
		    new ActionMessage("error.mandatoryField", resources.getMessage("config.securitySalt")));
	}

	return errors;
    }

}