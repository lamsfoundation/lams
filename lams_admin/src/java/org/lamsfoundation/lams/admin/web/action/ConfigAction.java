/****************************************************************
 * Copyright (C) 2006 LAMS Foundation (http://lamsfoundation.org)
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
package org.lamsfoundation.lams.admin.web.action;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.lamsfoundation.lams.admin.service.AdminServiceProxy;
import org.lamsfoundation.lams.config.ConfigurationItem;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.LanguageUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;

/**
 * ConfigAction
 *
 * @author Mitchell Seaton
 */
/**
 * struts doclets
 *
 *
 *
 *
 *
 */
public class ConfigAction extends LamsDispatchAction {

    private static Configuration configurationService;
    private static MessageService messageService;

    private Configuration getConfiguration() {
	if (configurationService == null) {
	    configurationService = AdminServiceProxy.getConfiguration(getServlet().getServletContext());
	}
	return configurationService;
    }

    private MessageService getMessageService() {
	if (messageService == null) {
	    messageService = AdminServiceProxy.getMessageService(getServlet().getServletContext());
	}
	return messageService;
    }

    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	request.setAttribute("config", getConfiguration().arrangeItems(Configuration.ITEMS_NON_LDAP));
	request.setAttribute("countryCodes", LanguageUtil.getCountryCodes(false));
	Map<String, String> smtpAuthTypes = new LinkedHashMap<String, String>();
	smtpAuthTypes.put("none", "None");
	smtpAuthTypes.put("starttls", "STARTTLS");
	smtpAuthTypes.put("ssl", "SSL");
	request.setAttribute("smtpAuthTypes", smtpAuthTypes);

	return mapping.findForward("config");
    }

    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	if (isCancelled(request)) {
	    return mapping.findForward("sysadmin");
	}

	DynaActionForm configForm = (DynaActionForm) form;
	String[] keys = (String[]) configForm.get("key");
	String[] values = (String[]) configForm.get("value");

	String errorForward = "config";

	for (int i = 0; i < keys.length; i++) {
	    ConfigurationItem item = getConfiguration().getConfigItemByKey(keys[i]);

	    if (item != null) {
		// return to ldap page if that's where we came from
		if (StringUtils.contains(item.getHeaderName(), "config.header.ldap")) {
		    errorForward = "ldap";
		}

		if (item.getRequired()) {
		    if (!(values[i] != null && values[i].length() > 0)) {
			request.setAttribute("error", getRequiredError(item.getDescriptionKey()));
			request.setAttribute("config", getConfiguration().arrangeItems(Configuration.ITEMS_NON_LDAP));
			return mapping.findForward(errorForward);
		    }
		}
		String format = item.getFormat();
		if (format != null && format.equals(ConfigurationItem.LONG_FORMAT)) {
		    try {
			Long.parseLong(values[i]);
		    } catch (NumberFormatException e) {
			request.setAttribute("error", getNumericError(item.getDescriptionKey()));
			request.setAttribute("config", getConfiguration().arrangeItems(Configuration.ITEMS_NON_LDAP));
			return mapping.findForward(errorForward);
		    }
		}
		Configuration.updateItem(keys[i], values[i]);
	    }
	}
	getConfiguration().persistUpdate();

	Configuration.refreshCache();

	return mapping.findForward("sysadmin");
    }

    private String getRequiredError(String arg) {
	String[] args = new String[1];
	args[0] = getMessageService().getMessage(arg);
	return getMessageService().getMessage("error.required", args);
    }

    private String getNumericError(String arg) {
	String[] args = new String[1];
	args[0] = getMessageService().getMessage(arg);
	return getMessageService().getMessage("error.numeric", args);
    }

}
