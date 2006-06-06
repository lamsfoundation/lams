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
import java.util.Iterator;
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
import org.lamsfoundation.lams.usermanagement.UserOrganisation;
import org.lamsfoundation.lams.usermanagement.UserOrganisationRole;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.HttpSessionManager;
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
 *                name="OrgManageForm"
 *                scope="request"
 *                validate="false"
 *                
 * @struts:action-forward name="orglist"
 *                        path=".orglist"
 */
public class OrgManageAction extends Action {
	
	private static final Logger log = Logger.getLogger(OrgManageAction.class);
	private static WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(HttpSessionManager.getInstance().getServletContext());
	private static IUserManagementService service = (IUserManagementService)ctx.getBean("userManagementServiceTarget");
	
	public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception{
		
		Integer orgId = WebUtil.readIntParam(request,"org");
		String username = request.getRemoteUser();
		List userOrganisations = service.getUserOrganisationsForUser(service.getUserByLogin(username));
		OrgManageActionForm orgManageForm = new OrgManageActionForm();
		List<OrgManageActionForm.OrgManageBean> orgManageBeans = new ArrayList<OrgManageActionForm.OrgManageBean>();
		if(userOrganisations!=null){
			for(int i=0; i<userOrganisations.size(); i++){
				OrgManageActionForm.OrgManageBean orgManageBean = new OrgManageActionForm.OrgManageBean();
				orgManageBean.setEditable(false);
				UserOrganisation userOrganisation = (UserOrganisation)userOrganisations.get(i);
				Organisation organisation = userOrganisation.getOrganisation();
				if(organisation.getParentOrganisation().getOrganisationId().equals(orgId)){
					BeanUtils.copyProperties(orgManageBean,organisation);
					orgManageBean.setStatus(organisation.getOrganisationState().getDescription());
					Iterator iter = userOrganisation.getUserOrganisationRoles().iterator();
					while(iter.hasNext()){
						UserOrganisationRole userOrganisationRole = (UserOrganisationRole)iter.next();
						if(userOrganisationRole.getRole().isSysAdmin()||userOrganisationRole.getRole().isCourseManager()||userOrganisationRole.getRole().isCourseAdmin()){
							orgManageBean.setEditable(true);
							break;
						}
					}
					orgManageBeans.add(orgManageBean);
				}
			}
			UserOrganisation userOrganisation = (UserOrganisation)userOrganisations.get(0);
			Organisation organisation = userOrganisation.getOrganisation();
			orgManageForm.setType(organisation.getOrganisationType().getOrganisationTypeId());
			orgManageForm.setParentOrganisation(organisation.getParentOrganisation());
		}
		orgManageForm.setOrgManageBeans(orgManageBeans);
		request.setAttribute("OrgManageForm",orgManageForm);
		return mapping.findForward("orglist");
	}

}
