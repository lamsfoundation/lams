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
package org.lamsfoundation.lams.admin.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.HttpSessionManager;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

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

	private static WebApplicationContext ctx = WebApplicationContextUtils
			.getWebApplicationContext(HttpSessionManager.getInstance()
					.getServletContext());

	private static IUserManagementService service = (IUserManagementService) ctx
			.getBean("userManagementServiceTarget");

	private static List countries = service.getAllCountries();
	
	private static List languages = service.getAllLanguages();
	
	private static List status = service.getAllOrgnisationStates();

	public ActionForward edit(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception{
		Integer orgId = WebUtil.readIntParam(request,"orgId",true);
		if(orgId != null){//editing existing organisation
			Organisation org = service.getOrganisationById(orgId);
			DynaActionForm orgForm = (DynaActionForm)form;
			BeanUtils.copyProperties(orgForm,org);
			log.debug("Struts Pupulated orgId:"+(Integer)orgForm.get("orgId"));
			orgForm.set("parentId",org.getParentOrganisation().getOrganisationId());
			orgForm.set("parentName",org.getParentOrganisation().getName());
			orgForm.set("typeId",org.getOrganisationType().getOrganisationTypeId());
			orgForm.set("stateId",org.getOrganisationState().getOrganisationStateId());
		}
		request.getSession().setAttribute("countries",countries);
		request.getSession().setAttribute("languages",languages);
		request.getSession().setAttribute("status",status);
		return mapping.findForward("organisation");
	}

	public ActionForward remove(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response){
		Integer orgId = WebUtil.readIntParam(request,"orgId");
		service.deleteOrganisationById(orgId);
		Integer parentId = WebUtil.readIntParam(request,"parentId");
		request.setAttribute("org",parentId);
		return mapping.findForward("orglist");
	}

} // end Action

