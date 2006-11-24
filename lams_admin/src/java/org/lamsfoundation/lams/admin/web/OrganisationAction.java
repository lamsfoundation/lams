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
/* $$Id$$ */
package org.lamsfoundation.lams.admin.web;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.lamsfoundation.lams.admin.service.AdminServiceProxy;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationState;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.SupportedLocale;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;

/**
 * @author Fei Yang
 *
 * @struts:action path="/organisation"
 *              name="OrganisationForm"
 *              scope="request"
 *              parameter="method"
 * 				validate="false"
 * 
 * @struts:action-forward name="organisation" path=".organisation"
 * @struts:action-forward name="orglist" path="/orgmanage.do"
 */
public class OrganisationAction extends LamsDispatchAction {

	private static Logger log = Logger.getLogger(OrganisationAction.class);

	private static IUserManagementService service;
	private static MessageService messageService;
	private static List<SupportedLocale> locales; 
	private static List status;
	
	public ActionForward edit(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception{
		service = AdminServiceProxy.getService(getServlet().getServletContext());
		initLocalesAndStatus();
		DynaActionForm orgForm = (DynaActionForm)form;
		Integer orgId = WebUtil.readIntParam(request,"orgId",true);

		if(orgId != null){//editing existing organisation
			Organisation org = (Organisation)service.findById(Organisation.class,orgId);
			BeanUtils.copyProperties(orgForm,org);
			log.debug("Struts Populated orgId:"+(Integer)orgForm.get("orgId"));
			orgForm.set("parentId",org.getParentOrganisation().getOrganisationId());
			orgForm.set("parentName",org.getParentOrganisation().getName());
			orgForm.set("typeId",org.getOrganisationType().getOrganisationTypeId());
			orgForm.set("stateId",org.getOrganisationState().getOrganisationStateId());
			SupportedLocale locale = org.getLocale();
			orgForm.set("localeId",locale != null ? locale.getLocaleId() : null);
		} else if(!request.isUserInRole(Role.SYSADMIN) && orgForm.get("typeId").equals(OrganisationType.COURSE_TYPE)) {
			// only sysadmin can create new group
			messageService = AdminServiceProxy.getMessageService(getServlet().getServletContext());
			request.setAttribute("errorName", "OrganisationAction");
			request.setAttribute("errorMessage", messageService.getMessage("error.authorisation"));
			return mapping.findForward("error");
		} else {
			// creating new organisation
			orgForm.set("orgId", null);
			Integer parentId = WebUtil.readIntParam(request,"parentId",true);
			if (parentId!=null) {
				Organisation parentOrg = (Organisation)service.findById(Organisation.class,parentId);
				orgForm.set("parentName", parentOrg.getName());
			}
		}
		request.getSession().setAttribute("locales",locales);
		request.getSession().setAttribute("status",status);
		return mapping.findForward("organisation");
	}

	/*public ActionForward remove(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response){
		Integer orgId = WebUtil.readIntParam(request,"orgId");
		getService().deleteById(Organisation.class,orgId);
		Integer parentId = WebUtil.readIntParam(request,"parentId");
		request.setAttribute("org",parentId);
		return mapping.findForward("orglist");
	}*/
	
	@SuppressWarnings("unchecked")
	private void initLocalesAndStatus(){
		if((locales==null)||(status==null) && service!=null){
			locales = service.findAll(SupportedLocale.class);
			status = service.findAll(OrganisationState.class);
			Collections.sort(locales);
		}
	}

} // end Action

