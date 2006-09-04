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
package org.lamsfoundation.lams.admin.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.lamsfoundation.lams.admin.AdminConstants;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationState;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.SupportedLocale;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.audit.IAuditService;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @version
 *
 * <p>
 * <a href="OrgSaveAction.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 *
 * Created at 16:42:53 on 2006-6-7
 */

/**
 * struts doclets
 * 
 * @struts:action path="/orgsave"
 *                name="OrganisationForm"
 *                input=".organisation"
 *                scope="request"
 *                validate="false"
 *
 * @struts:action-forward name="organisation"
 *                        path=".organisation"                
 * @struts:action-forward name="orglist"
 *                        path="/orgmanage.do"
 */

public class OrgSaveAction extends Action {
	
	private static Logger log = Logger.getLogger(OrgSaveAction.class);

	private static IUserManagementService service;

	public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception{
		
		DynaActionForm orgForm = (DynaActionForm)form;

		if(isCancelled(request)){
			request.setAttribute("org",orgForm.get("parentId"));
			return mapping.findForward("orglist");
		}
		ActionMessages errors = new ActionMessages();
		if((orgForm.get("name")==null)||(((String)orgForm.getString("name").trim()).length()==0)){
			errors.add("name",new ActionMessage("error.name.required"));
		}
		if(errors.isEmpty()){
			Integer orgId = (Integer)orgForm.get("orgId");
			Organisation org;

			HttpSession ss = SessionManager.getSession();
			UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
			SupportedLocale locale = (SupportedLocale)getService().findById(SupportedLocale.class,(Byte)orgForm.get("localeId"));
			OrganisationState state = (OrganisationState)getService().findById(OrganisationState.class,(Integer)orgForm.get("stateId"));

			if(orgId!=0){
				org = (Organisation)getService().findById(Organisation.class,orgId);
				writeAuditLog(org, orgForm, state, locale);
				BeanUtils.copyProperties(org,orgForm);
				org.setLocale(locale);
			}else{
				org = new Organisation();
				BeanUtils.copyProperties(org,orgForm);
				org.setLocale(locale);
				org.setParentOrganisation((Organisation)getService().findById(Organisation.class,(Integer)orgForm.get("parentId")));
				org.setOrganisationType((OrganisationType)getService().findById(OrganisationType.class,(Integer)orgForm.get("typeId")));
				writeAuditLog(org, orgForm, org.getOrganisationState(), org.getLocale());
			}
			
			log.debug("orgId:"+org.getOrganisationId()+" locale:"+org.getLocale()+" create date:"+org.getCreateDate());
			org.setOrganisationState(state);
			org = getService().saveOrganisation(org, user.getUserID());
			
			request.setAttribute("org",orgForm.get("parentId"));
			return mapping.findForward("orglist");
		}else{
			saveErrors(request,errors);
			return mapping.findForward("organisation");
		}
	}
	
	private void writeAuditLog(Organisation org, DynaActionForm orgForm, OrganisationState newState, SupportedLocale newLocale) {
		
		WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
		IAuditService auditService = (IAuditService) ctx.getBean("auditService");
		
		// audit log entries for organisation attribute changes	
		if((Integer)orgForm.get("orgId")!=0) {
			if(!org.getOrganisationState().getOrganisationStateId().equals((Integer)orgForm.get("stateId"))) {
				auditService.log(AdminConstants.MODULE_NAME, "Changed state for organisation: "+org.getName()
						+"("+org.getOrganisationId()+") from: "+org.getOrganisationState().getDescription()
						+" to: "+newState.getDescription());
			}
			if(!org.getName().equals((String)orgForm.get("name"))) {
				auditService.log(AdminConstants.MODULE_NAME, "Changed name for organisation: "+org.getName()
						+"("+org.getOrganisationId()+") from: "+org.getName()+" to: "+orgForm.get("name"));
			}
			if(!org.getCode().equals((String)orgForm.get("code"))) {
				auditService.log(AdminConstants.MODULE_NAME, "Changed code for organisation: "+org.getName()
						+"("+org.getOrganisationId()+") from: "+org.getCode()+" to: "+orgForm.get("code"));
			}
			if(!org.getDescription().equals((String)orgForm.getString("description"))) {
				auditService.log(AdminConstants.MODULE_NAME, "Changed description for organisation: "+org.getName()
						+"("+org.getOrganisationId()+") from: "+org.getDescription()+" to: "+orgForm.getString("description"));
			}
			if(!org.getCourseAdminCanAddNewUsers().equals((Boolean)orgForm.get("courseAdminCanAddNewUsers"))) {
				auditService.log(AdminConstants.MODULE_NAME, "Changed courseAdminCanAddNewUsers for organisation: "+org.getName()
						+"("+org.getOrganisationId()+") from: "+org.getCourseAdminCanAddNewUsers()+" to: "+orgForm.get("courseAdminCanAddNewUsers"));
			}
			if(!org.getCourseAdminCanBrowseAllUsers().equals((Boolean)orgForm.get("courseAdminCanBrowseAllUsers"))) {
				auditService.log(AdminConstants.MODULE_NAME, "Changed courseAdminCanBrowseAllUsers for organisation: "+org.getName()
						+"("+org.getOrganisationId()+") from: "+org.getCourseAdminCanBrowseAllUsers()+" to: "+orgForm.get("courseAdminCanBrowseAllUsers"));
			}
			if(!org.getCourseAdminCanChangeStatusOfCourse().equals((Boolean)orgForm.get("courseAdminCanChangeStatusOfCourse"))) {
				auditService.log(AdminConstants.MODULE_NAME, "Changed courseAdminCanChangeStatusOfCourse for organisation: "+org.getName()
						+"("+org.getOrganisationId()+") from: "+org.getCourseAdminCanChangeStatusOfCourse()+" to: "+orgForm.get("courseAdminCanChangeStatusOfCourse"));
			}
			/* this field not set yet 
			if(!org.getCourseAdminCanCreateGuestAccounts().equals((Boolean)orgForm.get("courseAdminCanCreateGuestAccounts"))) {
				auditService.log(AdminConstants.MODULE_NAME, "Changed courseAdminCanCreateGuestAccounts for organisation: "+org.getName()
						+"("+org.getOrganisationId()+") from: "+org.getCourseAdminCanCreateGuestAccounts()+" to: "+orgForm.get("courseAdminCanCreateGuestAccounts"));
			}*/
			if(!org.getLocale().getLocaleId().equals((Byte)orgForm.get("localeId"))) {
				auditService.log(AdminConstants.MODULE_NAME, "Changed locale for organisation: "+org.getName()
						+"("+org.getOrganisationId()+") from: "+org.getLocale().getDescription()+" to: "+newLocale.getDescription());
			}
		} else {
			auditService.log(AdminConstants.MODULE_NAME, "Created organisation: "+org.getName()
					+"("+org.getOrganisationId()+") of type: "+org.getOrganisationType().getName());
		}
	}
	
	private IUserManagementService getService(){
		if(service==null){
			WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
			service = (IUserManagementService) ctx.getBean("userManagementServiceTarget");
		}
		return service;
	}
	
}
