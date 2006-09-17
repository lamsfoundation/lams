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
package org.lamsfoundation.lams.web;

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
import org.lamsfoundation.lams.usermanagement.SupportedLocale;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @version
 *
 * <p>
 * <a href="ProfileAction.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 *
 * Created at 14:21:53 on 28/06/2006
 */

/**
 * @struts:action path="/profile"
 *                name="UserForm"
 * 	              scope="request"
 *                parameter="method"
 * 				  validate="false"
 * 
 * @struts:action-forward name="view" path=".profile"
 * @struts:action-forward name="edit" path=".editprofile"
 */
public class ProfileAction extends LamsDispatchAction {
	
	private static Logger log = Logger.getLogger(ProfileAction.class);
	private static IUserManagementService service;
	private static List<SupportedLocale> locales;

	public ActionForward view(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		
		User requestor = (User)getService().getUserByLogin(request.getRemoteUser());
		String fullName = requestor.getTitle()+" "+requestor.getFirstName()+" "+requestor.getLastName();
		
		request.setAttribute("fullName", fullName);
		request.setAttribute("email", requestor.getEmail());
		request.setAttribute("portraitUuid", (requestor.getPortraitUuid()==null ? 0 : requestor.getPortraitUuid()));
		return mapping.findForward("view");
	}
	
	public ActionForward edit(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		
		User requestor = (User)getService().getUserByLogin(request.getRemoteUser());
		log.debug("editing profile of userId: "+requestor.getUserId());
		DynaActionForm userForm = (DynaActionForm)form;
		BeanUtils.copyProperties(userForm, requestor);
		SupportedLocale locale = requestor.getLocale();
		userForm.set("localeId",locale.getLocaleId());
		request.setAttribute("locales", locales);
		return mapping.findForward("edit");
	}
	
	private IUserManagementService getService(){
		if(service==null){
			WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
			service = (IUserManagementService) ctx.getBean("userManagementServiceTarget");
			locales = getService().findAll(SupportedLocale.class);
			Collections.sort(locales);
		}
		return service;
	}
}
