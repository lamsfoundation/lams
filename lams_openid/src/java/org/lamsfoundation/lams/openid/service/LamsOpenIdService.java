/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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
package org.lamsfoundation.lams.openid.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.login.FailedLoginException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.integration.ExtCourseClassMap;
import org.lamsfoundation.lams.integration.ExtServerOrgMap;
import org.lamsfoundation.lams.integration.ExtUserUseridMap;
import org.lamsfoundation.lams.integration.UserInfoFetchException;
import org.lamsfoundation.lams.integration.service.IIntegrationService;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;

public class LamsOpenIdService implements ILamsOpenIdService {

    private static Logger log = Logger.getLogger(LamsOpenIdService.class);

    private IIntegrationService integrationService = null;

    private IUserManagementService userManagementService = null;

    private ILessonService lessonService = null;

    private static final String JNDI_DATASOURCE = "java:/jdbc/lams-ds";

    private static final String PASSWORD_QUERY = "select password from lams_user where login=?";

    // using JDBC connection to prevent the caching of passwords by hibernate
    public String getUserPassword(String username) throws FailedLoginException, NamingException, SQLException {
	InitialContext ctx = new InitialContext();

	DataSource ds = (DataSource) ctx.lookup(JNDI_DATASOURCE);
	Connection conn = null;
	String password = null;
	try {
	    conn = ds.getConnection();
	    PreparedStatement ps = conn.prepareStatement(PASSWORD_QUERY);
	    ps.setString(1, username);
	    ResultSet rs = ps.executeQuery();

	    // check if there is any result
	    if (rs.next() == false)
		throw new FailedLoginException("invalid username");

	    password = rs.getString(1);
	    rs.close();
	} finally {
	    if (conn != null && !conn.isClosed())
		conn.close();
	}
	return password;
    }

    public ExtCourseClassMap addUserToGroup(String username, String serverId, String datetime, String hash,
	    String courseId, String courseName, String countryIsoCode, String langIsoCode, Boolean isTeacher)
	    throws java.rmi.RemoteException {
	try {
	    // authenticate external server
	    ExtServerOrgMap serverMap = integrationService.getExtServerOrgMap(serverId);

	    // get user map, creating if necessary
	    ExtUserUseridMap userMap = integrationService.getExtUserUseridMap(serverMap, username);

	    User user = userManagementService.getUserByLogin(serverMap.getPrefix() + "_" + username);

	    log.debug("This is a login: " + user.getLogin());

	    userMap.setUser(user);

	    // add user to org, creating org if necessary
	    ExtCourseClassMap orgMap = integrationService.getExtCourseClassMap(serverMap, userMap, courseId,
		    courseName, countryIsoCode, langIsoCode, userManagementService.getRootOrganisation()
			    .getOrganisationId().toString(), isTeacher, false);
	    return orgMap;
	} catch (Exception e) {
	    log.debug(e.getMessage(), e);
	    throw new java.rmi.RemoteException(e.getMessage());
	}
    }

    public boolean addUserToSubgroup(String username, String serverId, String datetime, String hash, String courseId,
	    String courseName, String countryIsoCode, String langIsoCode, String subgroupId, String subgroupName,
	    Boolean isTeacher) throws java.rmi.RemoteException {
	try {
	    // authenticate external server
	    ExtServerOrgMap serverMap = integrationService.getExtServerOrgMap(serverId);
	    //Authenticator.authenticate(serverMap, datetime, hash);

	    // get group to use for this request 
	    ExtUserUseridMap userMap = integrationService.getExtUserUseridMap(serverMap, username);
	    Organisation rootOrg = userManagementService.getRootOrganisation();
	    ExtCourseClassMap orgMap = integrationService.getExtCourseClassMap(serverMap, userMap, courseId,
		    courseName, countryIsoCode, langIsoCode, rootOrg.getOrganisationId().toString(), isTeacher, false);
	    Organisation group = orgMap.getOrganisation();

	    // add user to subgroup, creating subgroup if necessary
	    ExtCourseClassMap subOrgMap = integrationService.getExtCourseClassMap(serverMap, userMap, subgroupId,
		    subgroupName, countryIsoCode, langIsoCode, group.getOrganisationId().toString(), isTeacher, false);
	    return true;
	} catch (Exception e) {
	    log.debug(e.getMessage(), e);
	    throw new java.rmi.RemoteException(e.getMessage());
	}
    }

    public boolean addUserToGroupLessons(String username, String serverId, String datetime, String hash,
	    String courseId, String courseName, String countryIsoCode, String langIsoCode, Boolean asStaff,
	    ExtCourseClassMap orgMap) throws java.rmi.RemoteException {
	try {
	    // authenticate external server
	    ExtServerOrgMap serverMap = integrationService.getExtServerOrgMap(serverId);
	    //Authenticator.authenticate(serverMap, datetime, hash);

	    // get group to use for this request
	    ExtUserUseridMap userMap = integrationService.getExtUserUseridMap(serverMap, username);
	    //ExtCourseClassMap orgMap = integrationService.getExtCourseClassMap(serverMap, userMap, courseId,
	    //	courseName, countryIsoCode, langIsoCode, userManagementService.getRootOrganisation()
	    //	    .getOrganisationId().toString(), asStaff, false);
	    Organisation org = orgMap.getOrganisation();

	    // add user to lessons
	    User user = userManagementService.getUserByLogin(serverMap.getPrefix() + "_" + username);
	    addUserToLessons(user, org, asStaff);
	    return true;
	} catch (Exception e) {
	    log.debug(e.getMessage(), e);
	    throw new java.rmi.RemoteException(e.getMessage());
	}
    }

    public boolean addUserToSubgroupLessons(String username, String serverId, String datetime, String hash,
	    String courseId, String courseName, String countryIsoCode, String langIsoCode, String subgroupId,
	    String subgroupName, Boolean asStaff) throws java.rmi.RemoteException {
	try {
	    // authenticate external server
	    ExtServerOrgMap serverMap = integrationService.getExtServerOrgMap(serverId);
	    //Authenticator.authenticate(serverMap, datetime, hash);

	    // get group to use for this request 
	    ExtUserUseridMap userMap = integrationService.getExtUserUseridMap(serverMap, username);
	    ExtCourseClassMap orgMap = integrationService.getExtCourseClassMap(serverMap, userMap, courseId,
		    courseName, countryIsoCode, langIsoCode, userManagementService.getRootOrganisation()
			    .getOrganisationId().toString(), asStaff, false);
	    Organisation group = orgMap.getOrganisation();

	    // get subgroup to add user to
	    ExtCourseClassMap subOrgMap = integrationService.getExtCourseClassMap(serverMap, userMap, subgroupId,
		    subgroupName, countryIsoCode, langIsoCode, group.getOrganisationId().toString(), asStaff, false);
	    Organisation subgroup = subOrgMap.getOrganisation();

	    // add user to subgroup lessons
	    if (subgroup != null) {
		User user = userManagementService.getUserByLogin(username);
		addUserToLessons(user, subgroup, asStaff);
		return true;
	    }
	    return false;
	} catch (Exception e) {
	    log.debug(e.getMessage(), e);
	    throw new java.rmi.RemoteException(e.getMessage());
	}
    }

    public void addUserToLessons(User user, Organisation org, Boolean asStaff) {
	if (org.getLessons() != null) {
	    Iterator iter2 = org.getLessons().iterator();
	    while (iter2.hasNext()) {
		Lesson lesson = (Lesson) iter2.next();
		lessonService.addLearner(lesson.getLessonId(), user.getUserId());
		if (asStaff)
		    lessonService.addStaffMember(lesson.getLessonId(), user.getUserId());
		if (log.isDebugEnabled()) {
		    log.debug("Added " + user.getLogin() + " to " + lesson.getLessonName()
			    + (asStaff ? " as staff, and" : " as learner"));
		}
	    }
	}
    }

    public ExtServerOrgMap getExtServerOrgMap(String serverId) {
	return integrationService.getExtServerOrgMap(serverId);
    }

    public ExtUserUseridMap getExtUserUseridMap(ExtServerOrgMap serverMap, String extUsername)
	    throws UserInfoFetchException {
	return integrationService.getExtUserUseridMap(serverMap, extUsername);
    }

    public ExtUserUseridMap getImplicitExtUserUseridMap(ExtServerOrgMap serverMap, String extUsername,
	    String firstName, String lastName, String langIsoCode, String countryIsoCode, String email)
	    throws UserInfoFetchException {
	return integrationService.getImplicitExtUserUseridMap(serverMap, extUsername, firstName, lastName, langIsoCode,
		countryIsoCode, email);
    }

    public ExtUserUseridMap getExistingExtUserUseridMap(ExtServerOrgMap map, String loggedInAs)
	    throws UserInfoFetchException {
	return integrationService.getExistingExtUserUseridMap(map, loggedInAs);
    }

    public User getUserByLogin(String login) {
	return userManagementService.getUserByLogin(login);
    }

    public IIntegrationService getIntegrationService() {
	return integrationService;
    }

    public void setIntegrationService(IIntegrationService integrationService) {
	this.integrationService = integrationService;
    }

    public IUserManagementService getUserManagementService() {
	return userManagementService;
    }

    public void setUserManagementService(IUserManagementService userManagementService) {
	this.userManagementService = userManagementService;
    }

    public ILessonService getLessonService() {
	return lessonService;
    }

    public void setLessonService(ILessonService lessonService) {
	this.lessonService = lessonService;
    }
}
