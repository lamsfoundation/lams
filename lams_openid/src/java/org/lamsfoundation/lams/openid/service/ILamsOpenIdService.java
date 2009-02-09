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

import java.sql.SQLException;

import javax.naming.NamingException;
import javax.security.auth.login.FailedLoginException;

import org.lamsfoundation.lams.integration.ExtCourseClassMap;
import org.lamsfoundation.lams.integration.ExtServerOrgMap;
import org.lamsfoundation.lams.integration.ExtUserUseridMap;
import org.lamsfoundation.lams.integration.UserInfoFetchException;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.User;

public interface ILamsOpenIdService {

    String getUserPassword(String username) throws FailedLoginException, NamingException, SQLException;

    ExtCourseClassMap addUserToGroup(String username, String serverId, String datetime, String hash, String courseId,
	    String courseName, String countryIsoCode, String langIsoCode, Boolean isTeacher)
	    throws java.rmi.RemoteException;

    boolean addUserToSubgroup(String username, String serverId, String datetime, String hash, String courseId,
	    String courseName, String countryIsoCode, String langIsoCode, String subgroupId, String subgroupName,
	    Boolean isTeacher) throws java.rmi.RemoteException;

    boolean addUserToGroupLessons(String username, String serverId, String datetime, String hash, String courseId,
	    String courseName, String countryIsoCode, String langIsoCode, Boolean asStaff, ExtCourseClassMap orgMap)
	    throws java.rmi.RemoteException;

    boolean addUserToSubgroupLessons(String username, String serverId, String datetime, String hash, String courseId,
	    String courseName, String countryIsoCode, String langIsoCode, String subgroupId, String subgroupName,
	    Boolean asStaff) throws java.rmi.RemoteException;

    void addUserToLessons(User user, Organisation org, Boolean asStaff);

    ExtServerOrgMap getExtServerOrgMap(String serverId);

    ExtUserUseridMap getExtUserUseridMap(ExtServerOrgMap serverMap, String extUsername) throws UserInfoFetchException;

    ExtUserUseridMap getImplicitExtUserUseridMap(ExtServerOrgMap serverMap, String extUsername, String firstName,
	    String lastName, String langIsoCode, String countryIsoCode, String email) throws UserInfoFetchException;
    
    User getUserByLogin(String login);
    
    ExtUserUseridMap getExistingExtUserUseridMap(ExtServerOrgMap map, String loggedInAs) throws UserInfoFetchException;
} 
