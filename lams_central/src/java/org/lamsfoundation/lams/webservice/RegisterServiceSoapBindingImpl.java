/**
 * RegisterServiceSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.lamsfoundation.lams.webservice;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServlet;

import org.apache.axis.MessageContext;
import org.apache.axis.transport.http.HTTPConstants;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.integration.ExtServerOrgMap;
import org.lamsfoundation.lams.integration.security.Authenticator;
import org.lamsfoundation.lams.integration.service.IIntegrationService;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.usermanagement.AuthenticationMethod;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.UserOrganisation;
import org.lamsfoundation.lams.usermanagement.UserOrganisationRole;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.LangUtil;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class RegisterServiceSoapBindingImpl implements Register {

	Logger log = Logger.getLogger(RegisterServiceSoapBindingImpl.class);

	private static MessageContext context = MessageContext.getCurrentContext();

	private static IUserManagementService service = (IUserManagementService) WebApplicationContextUtils
			.getRequiredWebApplicationContext(
					((HttpServlet) context.getProperty(HTTPConstants.MC_HTTP_SERVLET))
							.getServletContext()).getBean("userManagementService");

	private static ILessonService lessonService = (ILessonService) WebApplicationContextUtils
			.getRequiredWebApplicationContext(
					((HttpServlet) context.getProperty(HTTPConstants.MC_HTTP_SERVLET))
							.getServletContext()).getBean("lessonService");

	private static IIntegrationService integrationService = (IIntegrationService) WebApplicationContextUtils
	.getRequiredWebApplicationContext(
			((HttpServlet) context.getProperty(HTTPConstants.MC_HTTP_SERVLET))
					.getServletContext()).getBean("integrationService");
	
	public boolean createUser(
			String username,
			String password,
			String firstName,
			String lastName,
			String email,
			String serverId,
			String datetime,
			String hash) throws java.rmi.RemoteException {
		try {
			ExtServerOrgMap extServer = integrationService.getExtServerOrgMap(serverId);
			Authenticator.authenticate(extServer, datetime,	hash);
			if (service.getUserByLogin(username) != null)
				return false;
			User user = new User();
			user.setLogin(username);
			user.setPassword(password);
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setEmail(email);
			user.setAuthenticationMethod((AuthenticationMethod) service.findById(
					AuthenticationMethod.class, AuthenticationMethod.DB));
			user.setCreateDate(new Date());
			user.setDisabledFlag(false);
			user.setLocale(LangUtil.getDefaultLocale());
			user.setFlashTheme(service.getDefaultFlashTheme());
			user.setHtmlTheme(service.getDefaultHtmlTheme());
			service.save(user);
			return true;
		} catch (Exception e) {
			log.debug(e.getMessage(), e);
			throw new java.rmi.RemoteException(e.getMessage());
		}
	}

	public boolean addUserToGroup(
			String username, 
			String serverId, 
			String datetime, 
			String hash,
			Boolean isTeacher) throws java.rmi.RemoteException {
		try {
			ExtServerOrgMap extServer = integrationService.getExtServerOrgMap(serverId);
			Authenticator.authenticate(extServer, datetime,	hash);
			User user = service.getUserByLogin(username);
			Organisation org = extServer.getOrganisation();
			addMemberships(user, org, isTeacher);
			return true;
		} catch (Exception e) {
			log.debug(e.getMessage(), e);
			throw new java.rmi.RemoteException(e.getMessage());
		}
	}
	
	public boolean addUserToSubgroup(
			String username,
			String orgId,
			String serverId, 
			String datetime, 
			String hash,
			Boolean isTeacher) throws java.rmi.RemoteException {
		try {
			ExtServerOrgMap extServer = integrationService.getExtServerOrgMap(serverId);
			Authenticator.authenticate(extServer, datetime,	hash);
			User user = service.getUserByLogin(username);
			Organisation group = extServer.getOrganisation();
			Organisation subgroup = (Organisation)service.findById(Organisation.class, new Integer(orgId));
			Set children = group.getChildOrganisations();
			Iterator iter = children.iterator();
			while (iter.hasNext()) {
				Organisation child = (Organisation)iter.next();
				if (child.getOrganisationId().equals(subgroup.getOrganisationId())) {
					addMemberships(user, subgroup, isTeacher);
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			log.debug(e.getMessage(), e);
			throw new java.rmi.RemoteException(e.getMessage());
		}
	}
	
	public boolean addUserToGroupLessons(
			String username, 
			String serverId, 
			String datetime, 
			String hash) throws java.rmi.RemoteException {
		try {
			ExtServerOrgMap extServer = integrationService.getExtServerOrgMap(serverId);
			Authenticator.authenticate(extServer, datetime,	hash);
			User user = service.getUserByLogin(username);
			Organisation org = extServer.getOrganisation();
			addUserToLessons(user, org);
			return true;
		} catch (Exception e) {
			log.debug(e.getMessage(), e);
			throw new java.rmi.RemoteException(e.getMessage());
		}
	}
	
	public boolean addUserToSubgroupLessons(
			String username,
			String orgId,
			String serverId, 
			String datetime, 
			String hash) throws java.rmi.RemoteException {
		try {
			ExtServerOrgMap extServer = integrationService.getExtServerOrgMap(serverId);
			Authenticator.authenticate(extServer, datetime,	hash);
			User user = service.getUserByLogin(username);
			Organisation group = extServer.getOrganisation();
			
			Organisation subgroup = (Organisation)service.findById(Organisation.class, new Integer(orgId));
			Set children = group.getChildOrganisations();
			Iterator iter = children.iterator();
			while (iter.hasNext()) {
				Organisation child = (Organisation)iter.next();
				if (child.getOrganisationId().equals(subgroup.getOrganisationId())) {
					addUserToLessons(user, subgroup);
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			log.debug(e.getMessage(), e);
			throw new java.rmi.RemoteException(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	private void addMemberships(User user, Organisation org, Boolean isTeacher) {
		log.debug("adding memberships for user " + user.getUserId() + " in " + org.getName());
		UserOrganisation uo = new UserOrganisation(user, org);
		service.save(uo);
		Integer[] roles;
		if (isTeacher) {
			roles = new Integer[] { Role.ROLE_AUTHOR, Role.ROLE_MONITOR, Role.ROLE_LEARNER };
		} else {
			roles = new Integer[] { Role.ROLE_LEARNER };
		}
		for (Integer roleId : roles) {
			UserOrganisationRole uor = new UserOrganisationRole(uo, (Role) service.findById(
					Role.class, roleId));
			service.save(uor);
			uo.addUserOrganisationRole(uor);
		}
		user.addUserOrganisation(uo);
		service.save(user);
	}

	private void addUserToLessons(User user, Organisation org) {
		if (org.getLessons() != null) {
			Iterator iter2 = org.getLessons().iterator();
			while (iter2.hasNext()) {
				Lesson lesson = (Lesson) iter2.next();
				lessonService.addLearner(lesson.getLessonId(), user.getUserId());
				lessonService.addStaffMember(lesson.getLessonId(), user.getUserId());
				log.debug("Added " + user.getLogin() + " to " + lesson.getLessonName());
			}
		}
	}

}
