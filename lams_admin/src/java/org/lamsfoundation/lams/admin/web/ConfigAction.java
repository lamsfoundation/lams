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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.WebUtil;
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

	private Configuration getConfiguration() {
		if (configurationService == null) {
			WebApplicationContext ctx = WebApplicationContextUtils
					.getRequiredWebApplicationContext(getServlet().getServletContext());
			configurationService = (Configuration) ctx.getBean("configurationService");

		}
		return configurationService;
	}

	public ActionForward unspecified(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception{
		
		request.setAttribute("config", Configuration.getAll());
		
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
		String[] keys = (String[])configForm.get("cKey");
		String[] values = (String[])configForm.get("cValue");
		
		for(int i=0; i<keys.length; i++) {
			Configuration.updateItem(keys[i], values[i]);
		}
		
		getConfiguration().persistUpdate();
		
		return mapping.findForward("sysadmin");
	}
}
