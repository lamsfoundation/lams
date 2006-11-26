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
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.themes.CSSThemeVisualElement;
import org.lamsfoundation.lams.usermanagement.AuthenticationMethod;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationState;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
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

	private static final String DEMO_ORG_NAME = "Demo Course";

	private static MessageContext context = MessageContext.getCurrentContext();

	private static IUserManagementService service = (IUserManagementService) WebApplicationContextUtils
			.getRequiredWebApplicationContext(
					((HttpServlet) context.getProperty(HTTPConstants.MC_HTTP_SERVLET))
							.getServletContext()).getBean("userManagementService");

	private static ILessonService lessonService = (ILessonService) WebApplicationContextUtils
			.getRequiredWebApplicationContext(
					((HttpServlet) context.getProperty(HTTPConstants.MC_HTTP_SERVLET))
							.getServletContext()).getBean("lessonService");

	public boolean createUser(String username, String password, String firstName, String lastName,
			String email) throws java.rmi.RemoteException {
		try {
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
			Organisation org = getDemoOrg(user);
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
				Role.ROLE_COURSE_MANAGER, Role.ROLE_LEARNER };
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

	private Organisation getDemoOrg(User user) {
		Organisation org = null;
		List list = service.findByProperty(Organisation.class, "name", DEMO_ORG_NAME);
		if (list != null && list.size() > 0) {
			org = (Organisation) list.get(0);
		}
		if (org == null) {
			org = new Organisation();
			org.setName(DEMO_ORG_NAME);
			org.setParentOrganisation(service.getRootOrganisation());
			org.setOrganisationType((OrganisationType) service.findById(OrganisationType.class,
					OrganisationType.COURSE_TYPE));
			org.setOrganisationState((OrganisationState) service.findById(OrganisationState.class,
					OrganisationState.ACTIVE));
			org.setLocale(getLocale());
			service.saveOrganisation(org, user.getUserId());
		}
		log.debug(" Got org " + org.getOrganisationId());
		return org;
	}

}
