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
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.integration.ExtCourseClassMap;
import org.lamsfoundation.lams.integration.ExtServer;
import org.lamsfoundation.lams.integration.ExtUserUseridMap;
import org.lamsfoundation.lams.integration.UserInfoValidationException;
import org.lamsfoundation.lams.integration.security.Authenticator;
import org.lamsfoundation.lams.integration.service.IIntegrationService;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationState;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.SupportedLocale;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.UserOrganisation;
import org.lamsfoundation.lams.usermanagement.UserOrganisationRole;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.LanguageUtil;
import org.lamsfoundation.lams.util.ValidationUtil;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * <p>
 * The RegisterService exists to provide a SOAP interface to 3rd parties wishing to manipulate user accounts on a LAMS
 * server. You can create user accounts, add users to groups, and add users to the groups' lessons.
 * </p>
 *
 * <p>
 * It is disabled in LAMS by default. To enable, uncomment the RegisterService block in the file
 * JBOSS_HOME/server/default/deploy/lams.ear/lams-central.war/WEB-INF/server-config.wsdd.
 * </p>
 *
 * <p>
 * To authenticate your request to LAMS, each method accepts a datetime, server id, and hash parameter.
 * </p>
 *
 * <ul>
 * <li>server id: On the LAMS server you must setup your 3rd party server id and secret key by logging in as a sysadmin
 * and going to 'Maintain integrated servers'. All other fields are irrelevant for the RegisterService.
 * <li>datetime: Current date and time. e.g. in PHP,
 * <ul>
 * <li>$datetime = date("F d,Y g:i a");
 * </ul>
 * <li>hash: SHA1 hash of the datetime, server id, and secret key, in that order. e.g. in PHP,
 * <ul>
 * <li>$rawstring = trim($datetime).trim($CFG->lamstwo_serverid).trim($CFG->lamstwo_serverkey);
 * <li>$hashvalue = sha1(strtolower($rawstring));
 * </ul>
 * </ul>
 *
 * @author jliew
 *
 */
public class RegisterServiceSoapBindingImpl implements Register {

    Logger log = Logger.getLogger(RegisterServiceSoapBindingImpl.class);

    private static MessageContext context = MessageContext.getCurrentContext();

    private static IUserManagementService service = (IUserManagementService) WebApplicationContextUtils
	    .getRequiredWebApplicationContext(
		    ((HttpServlet) context.getProperty(HTTPConstants.MC_HTTP_SERVLET)).getServletContext())
	    .getBean("userManagementService");

    private static ILessonService lessonService = (ILessonService) WebApplicationContextUtils
	    .getRequiredWebApplicationContext(
		    ((HttpServlet) context.getProperty(HTTPConstants.MC_HTTP_SERVLET)).getServletContext())
	    .getBean("lessonService");

    private static IIntegrationService integrationService = (IIntegrationService) WebApplicationContextUtils
	    .getRequiredWebApplicationContext(
		    ((HttpServlet) context.getProperty(HTTPConstants.MC_HTTP_SERVLET)).getServletContext())
	    .getBean("integrationService");

    /**
     * Creates a user account in LAMS with the given username.
     */
    @Override
    public boolean createUser(String username, String password, String firstName, String lastName, String email,
	    String serverId, String datetime, String hash) throws java.rmi.RemoteException {
	try {
	    ExtServer extServer = integrationService.getExtServer(serverId);
	    Authenticator.authenticate(extServer, datetime, hash);
	    if (service.getUserByLogin(username) != null) {
		return false;
	    }
	    ExtUserUseridMap userMap = integrationService.getImplicitExtUserUseridMap(extServer, username, password,
		    firstName, lastName, email);
	    return true;
	} catch (Exception e) {
	    log.debug(e.getMessage(), e);
	    throw new java.rmi.RemoteException(e.getMessage());
	}
    }

    /**
     * Create a new group with the given name.
     */
    @Override
    public int createOrganisation(String name, String code, String description, String owner, String serverId,
	    String datetime, String hash) throws java.rmi.RemoteException {
	try {
	    // validate organisation name
	    if (StringUtils.isNotBlank(name) && !ValidationUtil.isOrgNameValid(name)) {
		throw new UserInfoValidationException("Can't create organisation due to validation error: "
			+ "organisation name cannot contain any of these characters < > ^ * @ % $. External serverId:"
			+ serverId + ", orgName:" + name);
	    }

	    Organisation org = new Organisation();
	    org.setName(name);
	    org.setCode(code);
	    org.setDescription(description);
	    org.setParentOrganisation(service.getRootOrganisation());
	    org.setOrganisationType(
		    (OrganisationType) service.findById(OrganisationType.class, OrganisationType.COURSE_TYPE));
	    org.setOrganisationState(
		    (OrganisationState) service.findById(OrganisationState.class, OrganisationState.ACTIVE));
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

    /**
     * Add the given username to the given group or subgroup. User and group/subgroup must exist. If asStaff is true,
     * user is given Learner, Author, and Monitor roles; otherwise only Learner.
     */
    @Override
    public boolean addUserToOrganisation(String login, Integer organisationId, Boolean asStaff, String serverId,
	    String datetime, String hash) throws java.rmi.RemoteException {
	try {
	    // authenticate external server
	    ExtServer extServer = integrationService.getExtServer(serverId);
	    Authenticator.authenticate(extServer, datetime, hash);

	    // get user and organisation
	    User user = service.getUserByLogin(login);
	    Organisation org = (Organisation) service.findById(Organisation.class, organisationId);

	    if (user == null || org == null) {
		return false;
	    }

	    // check user is not already in org
	    UserOrganisation uo = service.getUserOrganisation(user.getUserId(), org.getOrganisationId());

	    if (uo == null) {
		// create UserOrganisation

		uo = new UserOrganisation(user, org);
		service.save(uo);
		Integer[] roles;
		if (asStaff) {
		    roles = new Integer[] { Role.ROLE_AUTHOR, Role.ROLE_MONITOR, Role.ROLE_LEARNER };
		} else {
		    roles = new Integer[] { Role.ROLE_LEARNER };
		}
		for (Integer roleId : roles) {
		    UserOrganisationRole uor = new UserOrganisationRole(uo,
			    (Role) service.findById(Role.class, roleId));
		    service.save(uor);
		    uo.addUserOrganisationRole(uor);
		}
		user.addUserOrganisation(uo);
		service.save(user);

		return true;

	    } else {
		// do nothing
		return false;
	    }

	} catch (Exception e) {
	    log.debug(e.getMessage(), e);
	    throw new java.rmi.RemoteException(e.getMessage());
	}

    }

    /**
     * <p>
     * Adds the given username to the LAMS group with the given courseId. If courseId doesn't exist in the
     * lams_ext_course_class_map table (i.e. new external 3rd party course), a new group is created. If it does exist,
     * the group name is updated.
     * </p>
     *
     * <p>
     * If the username doesn't exist, this method will fail unless there is a working user callback url configured for
     * this 3rd party server id. However the usual way of creating users with the RegisterService is to use the
     * createUser method.
     * </p>
     */
    @Override
    public boolean addUserToGroup(String username, String serverId, String datetime, String hash, String courseId,
	    String courseName, String countryIsoCode, String langIsoCode, Boolean isTeacher)
	    throws java.rmi.RemoteException {
	try {
	    // authenticate external server
	    ExtServer extServer = integrationService.getExtServer(serverId);
	    Authenticator.authenticate(extServer, datetime, hash);

	    // get user map, creating if necessary
	    ExtUserUseridMap userMap = integrationService.getExtUserUseridMap(extServer, username);

	    // add user to org, creating org if necessary
	    ExtCourseClassMap orgMap = integrationService.getExtCourseClassMap(extServer, userMap, courseId, courseName,
		    countryIsoCode, langIsoCode, service.getRootOrganisation().getOrganisationId().toString(),
		    isTeacher, false);
	    return true;
	} catch (Exception e) {
	    log.debug(e.getMessage(), e);
	    throw new java.rmi.RemoteException(e.getMessage());
	}
    }

    /**
     * Same as addUserToGroup, except adds user to the given aubgroup (and creates it if it doesn't exist).
     */
    @Override
    public boolean addUserToSubgroup(String username, String serverId, String datetime, String hash, String courseId,
	    String courseName, String countryIsoCode, String langIsoCode, String subgroupId, String subgroupName,
	    Boolean isTeacher) throws java.rmi.RemoteException {
	try {
	    // authenticate external server
	    ExtServer extServer = integrationService.getExtServer(serverId);
	    Authenticator.authenticate(extServer, datetime, hash);

	    // get group to use for this request
	    ExtUserUseridMap userMap = integrationService.getExtUserUseridMap(extServer, username);
	    ExtCourseClassMap orgMap = integrationService.getExtCourseClassMap(extServer, userMap, courseId, courseName,
		    countryIsoCode, langIsoCode, service.getRootOrganisation().getOrganisationId().toString(),
		    isTeacher, false);
	    Organisation group = orgMap.getOrganisation();

	    // add user to subgroup, creating subgroup if necessary
	    ExtCourseClassMap subOrgMap = integrationService.getExtCourseClassMap(extServer, userMap, subgroupId,
		    subgroupName, countryIsoCode, langIsoCode, group.getOrganisationId().toString(), isTeacher, false);
	    return true;
	} catch (Exception e) {
	    log.debug(e.getMessage(), e);
	    throw new java.rmi.RemoteException(e.getMessage());
	}
    }

    /**
     * <p>
     * Adds given username as learner or monitor (as specified by asStaff parameter) to all lessons in
     * given LAMS group.
     * </p>
     *
     * <p>
     * The LAMS group is identified by the external courseId parameter. If the courseId doesn't exist in the
     * lams_ext_course_class_map table, then a new LAMS group is created. If it does exist, the group name is
     * updated.
     * </p>
     */
    @Override
    public boolean addUserToGroupLessons(String username, String serverId, String datetime, String hash,
	    String courseId, String courseName, String countryIsoCode, String langIsoCode, Boolean asStaff)
	    throws java.rmi.RemoteException {
	try {
	    // authenticate external server
	    ExtServer extServer = integrationService.getExtServer(serverId);
	    Authenticator.authenticate(extServer, datetime, hash);

	    // get group to use for this request
	    ExtUserUseridMap userMap = integrationService.getExtUserUseridMap(extServer, username);
	    ExtCourseClassMap orgMap = integrationService.getExtCourseClassMap(extServer, userMap, courseId, courseName,
		    countryIsoCode, langIsoCode, service.getRootOrganisation().getOrganisationId().toString(), asStaff,
		    false);
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

    /**
     * Same as addUserToLessons, except adds user to lessons in given subgroup.
     */
    @Override
    public boolean addUserToSubgroupLessons(String username, String serverId, String datetime, String hash,
	    String courseId, String courseName, String countryIsoCode, String langIsoCode, String subgroupId,
	    String subgroupName, Boolean asStaff) throws java.rmi.RemoteException {
	try {
	    // authenticate external server
	    ExtServer extServer = integrationService.getExtServer(serverId);
	    Authenticator.authenticate(extServer, datetime, hash);

	    // get group to use for this request
	    ExtUserUseridMap userMap = integrationService.getExtUserUseridMap(extServer, username);
	    ExtCourseClassMap orgMap = integrationService.getExtCourseClassMap(extServer, userMap, courseId, courseName,
		    countryIsoCode, langIsoCode, service.getRootOrganisation().getOrganisationId().toString(), asStaff,
		    false);
	    Organisation group = orgMap.getOrganisation();

	    // get subgroup to add user to
	    ExtCourseClassMap subOrgMap = integrationService.getExtCourseClassMap(extServer, userMap, subgroupId,
		    subgroupName, countryIsoCode, langIsoCode, group.getOrganisationId().toString(), asStaff, false);
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
		if (asStaff) {
		    lessonService.addStaffMember(lesson.getLessonId(), user.getUserId());
		}
		if (log.isDebugEnabled()) {
		    log.debug("Added " + user.getLogin() + " to " + lesson.getLessonName()
			    + (asStaff ? " as staff, and" : " as learner"));
		}
	    }
	}
    }

}
