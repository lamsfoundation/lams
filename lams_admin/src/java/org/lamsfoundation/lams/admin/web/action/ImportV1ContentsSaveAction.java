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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */
package org.lamsfoundation.lams.admin.web.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.admin.AdminConstants;
import org.lamsfoundation.lams.admin.service.AdminServiceProxy;
import org.lamsfoundation.lams.admin.web.dto.V1OrgRightDTO;
import org.lamsfoundation.lams.admin.web.dto.V1OrganisationDTO;
import org.lamsfoundation.lams.admin.web.dto.V1UserDTO;
import org.lamsfoundation.lams.admin.web.form.ImportV1ContentsForm;
import org.lamsfoundation.lams.themes.CSSThemeVisualElement;
import org.lamsfoundation.lams.usermanagement.AuthenticationMethod;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationState;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.SupportedLocale;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.HashUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * @author jliew
 * 
 * @struts:action path="/importv1contentssave"
 *              name="ImportV1ContentsForm"
 *              input=".importv1contents"
 *              scope="session"
 * 				validate="false"
 * 
 * @struts:action-forward name="importv1result" path=".importv1result"
 * @struts:action-forward name="sysadmin" path="/sysadminstart.do"
 */
public class ImportV1ContentsSaveAction extends Action {
	
	private static Logger log = Logger.getLogger(ImportV1ContentsSaveAction.class);
	private static IUserManagementService service;
	
	public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		
		if (isCancelled(request)) {
			removeSessionVars(request);
			return mapping.findForward("sysadmin");
		}
		
		service = AdminServiceProxy.getService(getServlet().getServletContext());
		// import data
		List<V1UserDTO> users = (List)request.getSession().getAttribute("users");
		List<V1OrganisationDTO> orgs = (List)request.getSession().getAttribute("orgs");
		removeSessionVars(request);
		
		// import options
		ImportV1ContentsForm importV1ContentsForm = (ImportV1ContentsForm)form;
		String[] sids;
		List<String> orgSids;
		if ((sids=importV1ContentsForm.getOrgSids())==null) {
			orgSids = new ArrayList<String>();
		} else {
			orgSids = Arrays.asList(sids);
		}
		List<String> sessSids;
		if ((sids=importV1ContentsForm.getSessSids())==null) {
			sessSids = new ArrayList<String>();
		} else {
			sessSids = Arrays.asList(sids);
		}
		boolean onlyMembers = importV1ContentsForm.getOnlyMembers();
		
		// default import options
		String defaultLocale = Configuration.get(ConfigurationKeys.SERVER_LANGUAGE);
		SupportedLocale locale = service.getSupportedLocale(defaultLocale.substring(0,2),defaultLocale.substring(3));
		final OrganisationType courseType = (OrganisationType)service.findById(
				OrganisationType.class, OrganisationType.COURSE_TYPE);
		final OrganisationState activeState = (OrganisationState)service.findById(
				OrganisationState.class, OrganisationState.ACTIVE);
		Integer userID = ((UserDTO)SessionManager.getSession().getAttribute(AttributeNames.USER)).getUserID();
		
		// import users
		List<V1UserDTO> alreadyExists = new ArrayList<V1UserDTO>();  // users whose logins already exist
		if (onlyMembers) {
			List<V1UserDTO> createdUsers = new ArrayList<V1UserDTO>();
			for (V1UserDTO user : users) {
				if (!isUserMember(user, orgSids, sessSids)) {
					continue;
				}
				boolean created = createUser(user, locale);
				if (!created) {
					alreadyExists.add(user);
				} else {
					createdUsers.add(user);
				}
			}
			users = createdUsers;
		} else {
			for (V1UserDTO user : users) {
				boolean created = createUser(user, locale);
				if (!created) {
					alreadyExists.add(user);
				}
			}
		}
		
		// import orgs
		HashMap<String, Integer> orgIdsMap = new HashMap<String, Integer>();  // map v1 orgids to v2 orgids
		for (V1OrganisationDTO org : orgs) {
			if (StringUtils.equals(org.getAccountOrganisation(), "1")) {
				if (!orgSids.contains(org.getSid())) {
					continue;
				}
			} else if (StringUtils.equals(org.getAccountOrganisation(), "0")) {
				if (!sessSids.contains(org.getSid())) {
					continue;
				}
			}
			Organisation v2org = createOrg(org, locale, courseType, activeState, userID);
			orgIdsMap.put(org.getSid(), v2org.getOrganisationId());
		}
		
		// import user roles
		for (V1UserDTO v1user : users) {
			User user = service.getUserByLogin(v1user.getLogin());
			// special case for 'core' role, equivalent to global role of sysadmin
			if (v1user.getRoleIds().contains(V1UserDTO.ROLE_CORE)) {
				ArrayList<String> roles = new ArrayList<String>();
				roles.add(Role.ROLE_SYSADMIN.toString());
				service.setRolesForUserOrganisation(user, service.getRootOrganisation(), roles);
			}
			for (V1OrgRightDTO v1orgRight : v1user.getOrgRights()) {
				// the org was not selected to be imported
				if (orgIdsMap.get(v1orgRight.getOrgSid())==null) {
					continue;
				}
				List<String> roles = getV2RoleIds(v1user.getRoleIds(), v1orgRight);
				Organisation org = (Organisation)service.findById(Organisation.class, orgIdsMap.get(v1orgRight.getOrgSid()));
				service.setRolesForUserOrganisation(user, org, roles);
			}
		}
		
		MessageService messageService = AdminServiceProxy
			.getMessageService(getServlet().getServletContext());
		String[] args = new String[2];
		args[0] = String.valueOf(users.size());
		args[1] = String.valueOf(orgIdsMap.size());
		request.setAttribute("msgNumCreated", messageService
			.getMessage("msg.importv1.created", args));
		request.setAttribute("alreadyExists", alreadyExists);
				
		return mapping.findForward("importv1result");
	}
	
	// converts v1 roles/org rights to v2 roles.
	private List<String> getV2RoleIds(List<String> roleIds, V1OrgRightDTO orgRight) {
		List<String> newRoleIds = new ArrayList<String>();
		for (String roleId : roleIds) {
			if (StringUtils.equals(roleId, V1UserDTO.ROLE_ADMINISTRATOR)) {
				if (StringUtils.equals(orgRight.getUserRight(), V1OrgRightDTO.ADMIN)) {
					newRoleIds.add(Role.ROLE_GROUP_ADMIN.toString());
				}
			} else if (StringUtils.equals(roleId, V1UserDTO.ROLE_AUTHOR)) {
				newRoleIds.add(Role.ROLE_AUTHOR.toString());
			} else if (StringUtils.equals(roleId, V1UserDTO.ROLE_STAFF)) {
				if (!StringUtils.equals(orgRight.getUserRight(), V1OrgRightDTO.MEMBERSHIP)) {
					newRoleIds.add(Role.ROLE_MONITOR.toString());
				}
			} else if (StringUtils.equals(roleId, V1UserDTO.ROLE_LEARNER)) {
				newRoleIds.add(Role.ROLE_LEARNER.toString());
			}
		}
		return newRoleIds;
	}
	
	private void removeSessionVars(HttpServletRequest request) {
		request.getSession().removeAttribute("users");
		request.getSession().removeAttribute("orgs");
		request.getSession().removeAttribute("ImportV1ContentsForm");
	}
	
	// make 'create user' audit log entry
	private void createUserAuditLog(User user) {
		String[] args = new String[2];
		args[0] = user.getLogin()+"("+user.getUserId()+")";
		args[1] = user.getFullName();
		String message = AdminServiceProxy.getMessageService(getServlet().getServletContext())
			.getMessage("audit.user.create", args);
		AdminServiceProxy.getAuditService(getServlet().getServletContext())
			.log(AdminConstants.MODULE_NAME, message);
	}
	
	private boolean createUser(V1UserDTO user, SupportedLocale locale) throws Exception {
		// test username already exists
		if (service.getUserByLogin(user.getLogin())!=null) {
			return false;
		}
		
		User newUser = new User();
		BeanUtils.copyProperties(newUser, user);
		
		// password must be sha1'ed, not md5'ed as in v1; so we use the login as the password
		newUser.setPassword(HashUtil.sha1(user.getLogin()));
		newUser.setChangePassword(true);
		
		String flashName = Configuration.get(ConfigurationKeys.DEFAULT_FLASH_THEME);
		List list = service.findByProperty(CSSThemeVisualElement.class, "name", flashName);
		if (list!=null) {
			CSSThemeVisualElement flashTheme = (CSSThemeVisualElement)list.get(0);
			newUser.setFlashTheme(flashTheme);
		}
		String htmlName = Configuration.get(ConfigurationKeys.DEFAULT_HTML_THEME);
		list = service.findByProperty(CSSThemeVisualElement.class, "name", htmlName);
		if (list!=null) {
			CSSThemeVisualElement htmlTheme = (CSSThemeVisualElement)list.get(0);
			newUser.setHtmlTheme(htmlTheme);
		}
		newUser.setDisabledFlag(false);
		newUser.setCreateDate(new Date());
		newUser.setAuthenticationMethod((AuthenticationMethod)service.findByProperty(AuthenticationMethod.class,
				"authenticationMethodName","LAMS-Database").get(0));
		newUser.setUserId(null);
		newUser.setLocale(locale);
		service.save(newUser);
		
		createUserAuditLog(newUser);
		return true;
	}

	// is the user a member of one of the orgs to be imported
	private boolean isUserMember(V1UserDTO user, List<String> orgSids, List<String> sessSids) {
		List<V1OrgRightDTO> orgRights = user.getOrgRights();
		if (orgRights != null) {
			for (V1OrgRightDTO orgRight : orgRights) {
				if (orgSids.contains(orgRight.getOrgSid()) || sessSids.contains(orgRight.getOrgSid())) {
					return true;
				}
			}
		}
		return false;
	}
	
	private Organisation createOrg(V1OrganisationDTO org,
			SupportedLocale locale,
			OrganisationType courseType,
			OrganisationState activeState,
			Integer userID) throws Exception {
		// create the org
		Organisation newOrg = new Organisation();
		BeanUtils.copyProperties(newOrg, org);
		newOrg.setParentOrganisation(service.getRootOrganisation());
		newOrg.setOrganisationType(courseType);
		newOrg.setOrganisationState(activeState);
		newOrg.setLocale(locale);
		newOrg = service.saveOrganisation(newOrg, userID);
		createOrgAuditLog(newOrg);
		return newOrg;
	}
	
	// make 'create org' audit log entry
	private void createOrgAuditLog(Organisation org) {
		String[] args = new String[2];
		args[0] = org.getName()+"("+org.getOrganisationId()+")";
		args[1] = org.getOrganisationType().getName();
		String message = AdminServiceProxy.getMessageService(getServlet().getServletContext())
			.getMessage("audit.organisation.create", args);
		AdminServiceProxy.getAuditService(getServlet().getServletContext())
			.log(AdminConstants.MODULE_NAME, message);
	}
}

