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
package org.lamsfoundation.lams.admin.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.lamsfoundation.lams.admin.service.AdminServiceProxy;
import org.lamsfoundation.lams.config.ConfigurationItem;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * ConfigAction
 *
 * @author Mitchell Seaton
 */
/**
 * struts doclets
 * 
 * @struts.action path="/config" parameter="method" name="ConfigForm" input=".config" scope="request" validate="false"
 * @struts.action-forward name="config" path=".config"
 * @struts.action-forward name="sysadmin" path="/sysadminstart.do"
 */
public class ConfigAction extends LamsDispatchAction {

	private static Configuration configurationService;
	private static MessageService messageService;

	private Configuration getConfiguration() {
		if (configurationService == null) {
			WebApplicationContext ctx = WebApplicationContextUtils
					.getRequiredWebApplicationContext(getServlet().getServletContext());
			configurationService = (Configuration) ctx.getBean("configurationService");

		}
		return configurationService;
	}
	
	private MessageService getMessageService() {
		if (messageService == null) {
			messageService = AdminServiceProxy.getMessageService(getServlet().getServletContext());
		}
		return messageService;
	}

	public ActionForward unspecified(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception{
		
		request.setAttribute("config", arrangeItems());
		
		return mapping.findForward("config");
	}
	
	public ActionForward save(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception{
		
		if (isCancelled(request)) {
			return mapping.findForward("sysadmin");
		}
		
		DynaActionForm configForm = (DynaActionForm) form;
		String[] keys = (String[])configForm.get("key");
		String[] values = (String[])configForm.get("value");
		
		for(int i=0; i<keys.length; i++) {
			ConfigurationItem item = getConfiguration().getConfigItemByKey(keys[i]);
			if (item.getRequired()) {
				if (!(values[i]!=null && values[i].length()>0)) {
					request.setAttribute("error", getRequiredError(item.getDescriptionKey()));
					request.setAttribute("config", arrangeItems());
					return mapping.findForward("config");
				}
			}
			String format = item.getFormat();
			if (format!=null && format.equals(ConfigurationItem.LONG_FORMAT)) {
				try {
					Long.parseLong(values[i]);
				} catch (NumberFormatException e) {
					request.setAttribute("error", getNumericError(item.getDescriptionKey()));
					request.setAttribute("config", arrangeItems());
					return mapping.findForward("config");
				}
			}
			Configuration.updateItem(keys[i], values[i]);
		}
		
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
	
	// get contents of lams_configuration and group them using header names as key
	private HashMap<String, ArrayList<ConfigurationItem>> arrangeItems() {
		List originalList = getConfiguration().getAllItems();
		HashMap<String, ArrayList<ConfigurationItem>> groupedList = new HashMap<String, ArrayList<ConfigurationItem>>();
		
		for (int i=0; i<originalList.size(); i++) {
			ConfigurationItem item = (ConfigurationItem)originalList.get(i);
			String header = item.getHeaderName();
			if (!groupedList.containsKey(header)) {
				groupedList.put(header, new ArrayList<ConfigurationItem>());
			}
			ArrayList<ConfigurationItem> currentList = groupedList.get(header);
			currentList.add(item);
			groupedList.put(header, currentList);
		}
		return groupedList;
	}
}
