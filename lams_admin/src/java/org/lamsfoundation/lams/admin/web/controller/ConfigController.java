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
package org.lamsfoundation.lams.admin.web.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.admin.web.form.ConfigForm;
import org.lamsfoundation.lams.config.ConfigurationItem;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.LanguageUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * ConfigAction
 *
 * @author Mitchell Seaton
 */
@Controller
public class ConfigController {
    
    @Autowired
    private Configuration configurationService;
    @Autowired
    @Qualifier("adminMessageService")
    private MessageService messageService;

    @RequestMapping(path = "/config")
    public String unspecified(@ModelAttribute ConfigForm configForm, HttpServletRequest request) throws Exception {

	request.setAttribute("config", configurationService.arrangeItems(Configuration.ITEMS_NON_LDAP));
	request.setAttribute("countryCodes", LanguageUtil.getCountryCodes(false));
	Map<String, String> smtpAuthTypes = new LinkedHashMap<String, String>();
	smtpAuthTypes.put("none", "None");
	smtpAuthTypes.put("starttls", "STARTTLS");
	smtpAuthTypes.put("ssl", "SSL");
	request.setAttribute("smtpAuthTypes", smtpAuthTypes);

	return "config/editconfig";
    }

    @RequestMapping(path = "/config/save", method = RequestMethod.POST)
    public String save(@ModelAttribute ConfigForm configForm, HttpServletRequest request) throws Exception {

	String[] keys = configForm.getKey();
	String[] values = configForm.getValue();

	String errorForward = "config/editconfig";

	for (int i = 0; i < keys.length; i++) {
	    ConfigurationItem item = configurationService.getConfigItemByKey(keys[i]);

	    if (item != null) {
		// return to ldap page if that's where we came from
		if (StringUtils.contains(item.getHeaderName(), "config.header.ldap")) {
		    errorForward = "ldap";
		}

		if (item.getRequired()) {
		    if (!(values[i] != null && values[i].length() > 0)) {
			request.setAttribute("error", getRequiredError(item.getDescriptionKey()));
			request.setAttribute("config", configurationService.arrangeItems(Configuration.ITEMS_NON_LDAP));
			return errorForward;
		    }
		}
		String format = item.getFormat();
		if (format != null && format.equals(ConfigurationItem.LONG_FORMAT)) {
		    try {
			Long.parseLong(values[i]);
		    } catch (NumberFormatException e) {
			request.setAttribute("error", getNumericError(item.getDescriptionKey()));
			request.setAttribute("config", configurationService.arrangeItems(Configuration.ITEMS_NON_LDAP));
			return errorForward;
		    }
		}
		Configuration.updateItem(keys[i], values[i]);
	    }
	}
	configurationService.persistUpdate();

	Configuration.refreshCache();

	return "redirect:/sysadminstart.do";
    }

    private String getRequiredError(String arg) {
	String[] args = new String[1];
	args[0] = messageService.getMessage(arg);
	return messageService.getMessage("error.required", args);
    }

    private String getNumericError(String arg) {
	String[] args = new String[1];
	args[0] = messageService.getMessage(arg);
	return messageService.getMessage("error.numeric", args);
    }

}
