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
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationState;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.SupportedLocale;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
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

			if(orgId!=0){
				org = (Organisation)getService().findById(Organisation.class,orgId);
				BeanUtils.copyProperties(org,orgForm);
				org.setLocale(locale);
			}else{
				org = new Organisation();
				BeanUtils.copyProperties(org,orgForm);
				org.setLocale(locale);
				org.setParentOrganisation((Organisation)getService().findById(Organisation.class,(Integer)orgForm.get("parentId")));
				org.setOrganisationType((OrganisationType)getService().findById(OrganisationType.class,(Integer)orgForm.get("typeId")));
			}
			
			log.debug("orgId:"+org.getOrganisationId()+" locale:"+org.getLocale()+" create date:"+org.getCreateDate());
			org.setOrganisationState((OrganisationState)getService().findById(OrganisationState.class,(Integer)orgForm.get("stateId")));
			org = getService().saveOrganisation(org, user.getUserID());
			
			request.setAttribute("org",orgForm.get("parentId"));
			return mapping.findForward("orglist");
		}else{
			saveErrors(request,errors);
			return mapping.findForward("organisation");
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
