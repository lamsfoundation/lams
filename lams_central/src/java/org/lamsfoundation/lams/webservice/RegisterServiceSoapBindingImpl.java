/**
 * RegisterServiceSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.lamsfoundation.lams.webservice;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServlet;

import org.apache.axis.MessageContext;
import org.apache.axis.transport.http.HTTPConstants;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.integration.ExtServerOrgMap;
import org.lamsfoundation.lams.integration.security.Authenticator;
import org.lamsfoundation.lams.integration.service.IIntegrationService;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.themes.CSSThemeVisualElement;
import org.lamsfoundation.lams.usermanagement.AuthenticationMethod;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.SupportedLocale;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.UserOrganisation;
import org.lamsfoundation.lams.usermanagement.UserOrganisationRole;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
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
	
	public boolean createUser(String username, String password, String firstName, String lastName,
			String email, String serverId, String datetime, String hash) throws java.rmi.RemoteException {
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
			user.setLocale(getLocale());
			String flashName = Configuration.get(ConfigurationKeys.DEFAULT_FLASH_THEME);
			List list = service.findByProperty(CSSThemeVisualElement.class, "name", flashName);
			if (list != null && list.size() > 0) {
				CSSThemeVisualElement flashTheme = (CSSThemeVisualElement) list.get(0);
				user.setFlashTheme(flashTheme);
			}
			String htmlName = Configuration.get(ConfigurationKeys.DEFAULT_HTML_THEME);
			list = service.findByProperty(CSSThemeVisualElement.class, "name", htmlName);
			if (list != null && list.size() > 0) {
				CSSThemeVisualElement htmlTheme = (CSSThemeVisualElement) list.get(0);
				user.setHtmlTheme(htmlTheme);
			}
			service.save(user);
			Organisation org = extServer.getOrganisation();
			addMemberships(user, org);
			addUserToLessons(user, org);
			return true;
		} catch (Exception e) {
			log.debug(e.getMessage(), e);
			throw new java.rmi.RemoteException(e.getMessage());
		}
	}

	private SupportedLocale getLocale() {
		String defaultLocale = Configuration.get(ConfigurationKeys.SERVER_LANGUAGE);
		return service
				.getSupportedLocale(defaultLocale.substring(0, 2), defaultLocale.substring(3));
	}

	@SuppressWarnings("unchecked")
	private void addMemberships(User user, Organisation org) {
		log.debug("adding memberships for user " + user.getUserId() + " in " + org.getName());
		UserOrganisation uo = new UserOrganisation(user, org);
		service.save(uo);
		Integer[] roles = new Integer[] { Role.ROLE_AUTHOR, Role.ROLE_MONITOR,
				Role.ROLE_GROUP_MANAGER, Role.ROLE_LEARNER };
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
