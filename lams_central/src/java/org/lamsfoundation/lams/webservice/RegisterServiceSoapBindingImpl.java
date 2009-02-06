/**
 * RegisterServiceSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.lamsfoundation.lams.webservice;

import java.util.Iterator;

import javax.servlet.http.HttpServlet;

import org.apache.axis.MessageContext;
import org.apache.axis.transport.http.HTTPConstants;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.integration.ExtCourseClassMap;
import org.lamsfoundation.lams.integration.ExtServerOrgMap;
import org.lamsfoundation.lams.integration.ExtUserUseridMap;
import org.lamsfoundation.lams.integration.security.Authenticator;
import org.lamsfoundation.lams.integration.service.IIntegrationService;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationState;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.SupportedLocale;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.LanguageUtil;
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
			ExtUserUseridMap userMap = integrationService.getImplicitExtUserUseridMap(extServer, 
					username, password, firstName, lastName, email);
			return true;
		} catch (Exception e) {
			log.debug(e.getMessage(), e);
			throw new java.rmi.RemoteException(e.getMessage());
		}
	}
	
	public int createGroup(
			String name,
			String code,
			String description,
			String owner,
			String serverId,
			String datetime,
			String hash) throws java.rmi.RemoteException {
		try {
			Organisation org = new Organisation();
			org.setName(name);
			org.setParentOrganisation(service.getRootOrganisation());
			org.setOrganisationType((OrganisationType)service.findById(OrganisationType.class,OrganisationType.COURSE_TYPE));
			org.setOrganisationState((OrganisationState)service.findById(OrganisationState.class,OrganisationState.ACTIVE));
			SupportedLocale locale = LanguageUtil.getDefaultLocale();
			org.setLocale(locale);
			User user = service.getUserByLogin(owner);
			service.saveOrganisation(org, user.getUserId());
			return org.getOrganisationId();
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
			String courseId,
			String courseName,
			String countryIsoCode,
			String langIsoCode,
			Boolean isTeacher) throws java.rmi.RemoteException {
		try {
			// authenticate external server
			ExtServerOrgMap serverMap = integrationService.getExtServerOrgMap(serverId);
			Authenticator.authenticate(serverMap, datetime,	hash);
			
			// get user map, creating if necessary
			ExtUserUseridMap userMap = integrationService.getExtUserUseridMap(serverMap, username);
			
			// add user to org, creating org if necessary
			ExtCourseClassMap orgMap = integrationService.getExtCourseClassMap(serverMap, userMap, 
					courseId, courseName, countryIsoCode, langIsoCode, 
					service.getRootOrganisation().getOrganisationId().toString(), isTeacher, false);
			return true;
		} catch (Exception e) {
			log.debug(e.getMessage(), e);
			throw new java.rmi.RemoteException(e.getMessage());
		}
	}
	
	public boolean addUserToSubgroup(
			String username,
			String serverId, 
			String datetime, 
			String hash,
			String courseId,
			String courseName,
			String countryIsoCode,
			String langIsoCode,
			String subgroupId,
			String subgroupName,
			Boolean isTeacher) throws java.rmi.RemoteException {
		try {
			// authenticate external server
			ExtServerOrgMap serverMap = integrationService.getExtServerOrgMap(serverId);
			Authenticator.authenticate(serverMap, datetime,	hash);
			
			// get group to use for this request 
			ExtUserUseridMap userMap = integrationService.getExtUserUseridMap(serverMap, username);
			ExtCourseClassMap orgMap = integrationService.getExtCourseClassMap(serverMap, userMap, 
					courseId, courseName, countryIsoCode, langIsoCode, 
					service.getRootOrganisation().getOrganisationId().toString(), isTeacher, false);
			Organisation group = orgMap.getOrganisation();
			
			// add user to subgroup, creating subgroup if necessary
			ExtCourseClassMap subOrgMap = integrationService.getExtCourseClassMap(serverMap, userMap, 
					subgroupId, subgroupName, countryIsoCode, langIsoCode, 
					group.getOrganisationId().toString(), isTeacher, false);
			return true;
		} catch (Exception e) {
			log.debug(e.getMessage(), e);
			throw new java.rmi.RemoteException(e.getMessage());
		}
	}
	
	public boolean addUserToGroupLessons(
			String username, 
			String serverId, 
			String datetime, 
			String hash,
			String courseId,
			String courseName,
			String countryIsoCode,
			String langIsoCode,
			Boolean asStaff) throws java.rmi.RemoteException {
		try {
			// authenticate external server
			ExtServerOrgMap serverMap = integrationService.getExtServerOrgMap(serverId);
			Authenticator.authenticate(serverMap, datetime,	hash);
			
			// get group to use for this request
			ExtUserUseridMap userMap = integrationService.getExtUserUseridMap(serverMap, username);
			ExtCourseClassMap orgMap = integrationService.getExtCourseClassMap(serverMap, userMap,
					courseId, courseName, countryIsoCode, langIsoCode, 
					service.getRootOrganisation().getOrganisationId().toString(), asStaff, false);
			Organisation org = orgMap.getOrganisation();
			
			// add user to lessons
			User user = service.getUserByLogin(username);
			addUserToLessons(user, org, asStaff);
			return true;
		} catch (Exception e) {
			log.debug(e.getMessage(), e);
			throw new java.rmi.RemoteException(e.getMessage());
		}
	}
	
	public boolean addUserToSubgroupLessons(
			String username,
			String serverId, 
			String datetime, 
			String hash,
			String courseId,
			String courseName,
			String countryIsoCode,
			String langIsoCode,
			String subgroupId,
			String subgroupName,
			Boolean asStaff) throws java.rmi.RemoteException {
		try {
			// authenticate external server
			ExtServerOrgMap serverMap = integrationService.getExtServerOrgMap(serverId);
			Authenticator.authenticate(serverMap, datetime,	hash);
			
			// get group to use for this request 
			ExtUserUseridMap userMap = integrationService.getExtUserUseridMap(serverMap, username);
			ExtCourseClassMap orgMap = integrationService.getExtCourseClassMap(serverMap, userMap, 
					courseId, courseName, countryIsoCode, langIsoCode, 
					service.getRootOrganisation().getOrganisationId().toString(), asStaff, false);
			Organisation group = orgMap.getOrganisation();
			
			// get subgroup to add user to
			ExtCourseClassMap subOrgMap = integrationService.getExtCourseClassMap(serverMap, userMap, 
					subgroupId, subgroupName, countryIsoCode, langIsoCode, 
					group.getOrganisationId().toString(), asStaff, false);
			Organisation subgroup = subOrgMap.getOrganisation();
			
			// add user to subgroup lessons
			if (subgroup != null) {
				User user = service.getUserByLogin(username);
				addUserToLessons(user, subgroup, asStaff);
				return true;
			}
			return false;
		} catch (Exception e) {
			log.debug(e.getMessage(), e);
			throw new java.rmi.RemoteException(e.getMessage());
		}
	}

	private void addUserToLessons(User user, Organisation org, Boolean asStaff) {
		if (org.getLessons() != null) {
			Iterator iter2 = org.getLessons().iterator();
			while (iter2.hasNext()) {
				Lesson lesson = (Lesson) iter2.next();
				lessonService.addLearner(lesson.getLessonId(), user.getUserId());
				if (asStaff) lessonService.addStaffMember(lesson.getLessonId(), user.getUserId());
				if (log.isDebugEnabled()) {
					log.debug("Added " + user.getLogin() + " to " + lesson.getLessonName() + 
							(asStaff ? " as staff, and" : " as learner") );
				}
			}
		}
	}

}
