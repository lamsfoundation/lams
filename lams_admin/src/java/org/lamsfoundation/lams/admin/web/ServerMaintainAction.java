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
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.lamsfoundation.lams.integration.ExtServerOrgMap;
import org.lamsfoundation.lams.integration.service.IIntegrationService;
import org.lamsfoundation.lams.integration.service.IntegrationService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * <p>
 * <a href="ServerMaintainAction.java.html"><i>View Source</i><a>
 * </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
/**
 * struts doclets
 * 
 * @struts.action path="/servermaintain" parameter="method" name="ServerOrgMapForm" scope="request" validate="false"
 * @struts.action-forward name="servermaintain" path=".servermaintain"
 * @struts.action-forward name="serverlist" path="/serverlist.do"
 */
public class ServerMaintainAction extends LamsDispatchAction {

	private static IIntegrationService service;

	private static MessageService messageService;

	private MessageService getMessageService() {
		if (messageService == null) {
			WebApplicationContext ctx = WebApplicationContextUtils
					.getRequiredWebApplicationContext(getServlet().getServletContext());
			messageService = (MessageService) ctx.getBean("adminMessageService");

		}
		return messageService;
	}

	private IIntegrationService getService(){
		if(service == null){
			WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
			service = (IIntegrationService)ctx.getBean("integrationService");
		}
		return service;
	}

	@SuppressWarnings("unchecked")
	public ActionForward edit(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception{
		IUserManagementService userManagementService = ((IntegrationService)getService()).getService();
		Map<String,Object> properties = new HashMap<String,Object>();
		properties.put("organisationType.organisationTypeId", 1);
		properties.put("organisationState.organisationStateId", 1);
		List list = userManagementService.findByProperties(Organisation.class, properties);
		Organisation dummy = new Organisation();
		dummy.setOrganisationId(-1);
		dummy.setName(getMessageService().getMessage("sysadmin.organisation.select"));
		if(list == null){
			list = new ArrayList();
		}
		list.add(dummy);
		request.setAttribute("orgs", list);
		DynaActionForm serverOrgMapForm = (DynaActionForm)form;
		Integer sid = WebUtil.readIntParam(request,"sid",true);
		if(sid != null){
			ExtServerOrgMap map = getService().getExtServerOrgMap(sid);
			BeanUtils.copyProperties(serverOrgMapForm,map);
			serverOrgMapForm.set("orgId", map.getOrganisation().getOrganisationId());
		}
		return mapping.findForward("servermaintain");
	}

	public ActionForward disable(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception{
		Integer sid = WebUtil.readIntParam(request, "sid", false);
		ExtServerOrgMap map = getService().getExtServerOrgMap(sid);
		map.setDisabled(true);
		getService().saveExtServerOrgMap(map);
		return mapping.findForward("serverlist");
	}

	public ActionForward enable(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception{
		Integer sid = WebUtil.readIntParam(request, "sid", false);
		ExtServerOrgMap map = getService().getExtServerOrgMap(sid);
		map.setDisabled(false);
		getService().saveExtServerOrgMap(map);
		return mapping.findForward("serverlist");
	}

	public ActionForward delete(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception{
		Integer sid = WebUtil.readIntParam(request, "sid", false);
		((IntegrationService)getService()).getService().deleteById(ExtServerOrgMap.class,sid);
		return mapping.findForward("serverlist");
	}
	
}
