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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @version
 *
 * <p>
 * <a href="OrgManageAction.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 *
 * Created at 20:29:13 on 2006-6-5
 */

/**
 * struts doclets
 * 
 * @struts:action path="/orgmanage"
 *                validate="false"
 *
 * @struts:action-forward name="orglist"
 *                        path=".orglist"
 */
public class OrgManageAction extends Action {
	
	private static final Logger log = Logger.getLogger(OrgManageAction.class);
	
	private static IUserManagementService service;
	
	private static MessageService messageService;

	
	public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception{
		
		Integer orgId = WebUtil.readIntParam(request,"org",true);
		if(orgId==null){
			orgId = (Integer)request.getAttribute("org");
		}
		if((orgId==null)||(orgId<=0)){
			request.setAttribute("errorName","OrgManageAction");
			request.setAttribute("errorMessage",getMessageService().getMessage("error.org.invalid"));
			return mapping.findForward("error");
		}
		OrgListDTO orgManageForm = new OrgListDTO();
		Organisation org = (Organisation)getService().findById(Organisation.class,orgId);
		log.debug("orgId:"+orgId);
		if(org==null){
			request.setAttribute("errorName","OrgManageAction");
			request.setAttribute("errorMessage",getMessageService().getMessage("error.org.invalid"));
			return mapping.findForward("error");
		}else if(org.getOrganisationType().getOrganisationTypeId().equals(OrganisationType.CLASS_TYPE)){
			request.setAttribute("errorName","OrgManageAction");
			request.setAttribute("errorMessage",getMessageService().getMessage("error.orgtype.invalid"));
			return mapping.findForward("error");
		}
		
		orgManageForm.setParentId(orgId);
		orgManageForm.setParentName(org.getName());
		orgManageForm.setType(org.getOrganisationType().getOrganisationTypeId());
		log.debug("orgType:"+orgManageForm.getType());
		List<OrgManageBean> orgManageBeans = new ArrayList<OrgManageBean>();
		//if(getService().isUserSysAdmin(username)){
		Integer userId = ((UserDTO)SessionManager.getSession().getAttribute(AttributeNames.USER)).getUserID();
		if(request.isUserInRole(Role.SYSADMIN) || request.isUserInRole(Role.COURSE_ADMIN) || request.isUserInRole(Role.COURSE_MANAGER)){
			Integer type;
			if(orgManageForm.getType().equals(OrganisationType.ROOT_TYPE)){
				type = OrganisationType.COURSE_TYPE;
			}else{
				type = OrganisationType.CLASS_TYPE;
			}
			List organisations = getService().findByProperty(Organisation.class,"organisationType.organisationTypeId",type);
			log.debug("user is an admin or manager");
			log.debug("Got "+organisations.size()+" organisations");
			log.debug("organisationType is "+type);
			for(int i=0; i<organisations.size(); i++){
				Organisation organisation = (Organisation)organisations.get(i);
				Organisation parentOrg = (type.equals(OrganisationType.CLASS_TYPE)) ? organisation.getParentOrganisation() : organisation;
				if (!request.isUserInRole(Role.SYSADMIN)) {
					if (!(getService().isUserInRole(userId, parentOrg.getOrganisationId(), Role.COURSE_ADMIN)
							|| getService().isUserInRole(userId, parentOrg.getOrganisationId(), Role.COURSE_MANAGER)))
						continue;
				}
				if(type.equals(OrganisationType.CLASS_TYPE)){
					if (parentOrg.getOrganisationId() != orgId)
						continue;
				}
				OrgManageBean orgManageBean = new OrgManageBean();
				BeanUtils.copyProperties(orgManageBean,organisation);
				orgManageBean.setStatus(organisation.getOrganisationState().getDescription());
				orgManageBean.setEditable(true);
				orgManageBeans.add(orgManageBean);
			}
		}
		orgManageForm.setOrgManageBeans(orgManageBeans);
		request.setAttribute("OrgManageForm",orgManageForm);
		return mapping.findForward("orglist");
	}

	private IUserManagementService getService(){
		if(service==null){
			WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
			service = (IUserManagementService) ctx.getBean("userManagementServiceTarget");
		}
		return service;
	}
	
	private MessageService getMessageService(){
		if(messageService==null){
			WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
			messageService = (MessageService)ctx.getBean("adminMessageService");
		}
		return messageService;
	}

}
