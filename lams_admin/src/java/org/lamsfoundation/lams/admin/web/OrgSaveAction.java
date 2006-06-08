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

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.web.util.HttpSessionManager;
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
	
	private static WebApplicationContext ctx = WebApplicationContextUtils
	.getWebApplicationContext(HttpSessionManager.getInstance()
			.getServletContext());

	private static IUserManagementService service = (IUserManagementService) ctx
	.getBean("userManagementServiceTarget");

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
			Organisation org = new Organisation();
			BeanUtils.copyProperties(org,orgForm);
			log.debug("orgId in orgForm:"+(Integer)orgForm.get("orgId"));
			if((Integer)orgForm.get("orgId")!=0){
				org.setCreateDate(service.getOrganisationById((Integer)orgForm.get("orgId")).getCreateDate());
			}else{
				org.setCreateDate(new Date());
			}
			org.setParentOrganisation(service.getOrganisationById((Integer)orgForm.get("parentId")));
			org.setOrganisationState(service.getOrganisationStateById((Integer)orgForm.get("stateId")));
			org.setOrganisationType(service.getOrganisationTypeById((Integer)orgForm.get("typeId")));
			service.saveOrganisation(org,service.getUserByLogin(request.getRemoteUser()).getUserId());
			request.setAttribute("org",orgForm.get("parentId"));
			return mapping.findForward("orglist");
		}else{
			saveErrors(request,errors);
			return mapping.findForward("organisation");
		}
	}
}
