/****************************************************************
 * Copyright (C) 2006 LAMS Foundation (http://lamsfoundation.org)
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

package org.lamsfoundation.lams.integration.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.lamsfoundation.lams.integration.ExtCourseClassMap;
import org.lamsfoundation.lams.integration.ExtServerOrgMap;
import org.lamsfoundation.lams.integration.ExtServerToolAdapterMap;
import org.lamsfoundation.lams.integration.ExtUserUseridMap;
import org.lamsfoundation.lams.integration.UserInfoFetchException;
import org.lamsfoundation.lams.integration.UserInfoValidationException;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.usermanagement.User;

/**
 * <p>
 * <a href="IntegrationService.java.html"><i>View Source</i><a>
 * </p>
 * 
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public interface IIntegrationService {

    ExtCourseClassMap getExtCourseClassMap(ExtServerOrgMap serverMap, ExtUserUseridMap userMap, String extCourseId,
	    String countryIsoCode, String langIsoCode, String prettyCourseName, String method, Boolean prefix)
	    throws UserInfoValidationException;

    ExtCourseClassMap getExtCourseClassMap(ExtServerOrgMap serverMap, ExtUserUseridMap userMap, String extCourseId,
	    String countryIsoCode, String langIsoCode, String prettyCourseName, String method) throws UserInfoValidationException;

    ExtCourseClassMap getExtCourseClassMap(ExtServerOrgMap serverMap, ExtUserUseridMap userMap, String extCourseId,
	    String extCourseName, String countryIsoCode, String langIsoCode, String parentOrgId, Boolean isTeacher,
	    Boolean prefix) throws UserInfoValidationException;

    ExtUserUseridMap getExtUserUseridMap(ExtServerOrgMap serverMap, String extUsername, boolean prefix)
	    throws UserInfoFetchException, UserInfoValidationException;

    /**
     * compatibility method to support existing integrations. It does callback call to integrated server to get user details.
     * 
     * @param serverMap
     * @param extUsername
     * @return
     * @throws UserInfoFetchException
     * @throws UserInfoValidationException
     */
    ExtUserUseridMap getExtUserUseridMap(ExtServerOrgMap serverMap, String extUsername)
	    throws UserInfoFetchException, UserInfoValidationException;

    /**
     * Returns ExtUserUseridMap from DB, and null if it doesn't exist
     * 
     * @param serverMap
     * @param extUsername
     * @return
     */
    ExtUserUseridMap getExistingExtUserUseridMap(ExtServerOrgMap serverMap, String extUsername)
	    throws UserInfoFetchException;

    ExtServerOrgMap getExtServerOrgMap(Integer sid);

    ExtServerOrgMap getExtServerOrgMap(String serverId);

    /**
     * @param serverMap
     * @param extUsername
     * @param firstName
     * @param lastName
     * @param language
     * @param country
     * @param email
     * @param prefix
     * @param isUpdateUserDetails whether user details should be updated with provided parameters
     * @return
     * @throws UserInfoValidationException
     */
    ExtUserUseridMap getImplicitExtUserUseridMap(ExtServerOrgMap serverMap, String extUsername, String firstName,
	    String lastName, String language, String country, String email, boolean prefix, boolean isUpdateUserDetails)
	    throws UserInfoValidationException;

    ExtUserUseridMap getImplicitExtUserUseridMap(ExtServerOrgMap serverMap, String extUsername, String password,
	    String firstName, String lastName, String email) throws UserInfoValidationException;

    List getAllExtServerOrgMaps();

    void saveExtServerOrgMap(ExtServerOrgMap map);

    String hash(ExtServerOrgMap serverMap, String extUsername, String timestamp);

    List<ExtServerToolAdapterMap> getMappedServers(String toolSig);

    ExtServerToolAdapterMap getMappedServer(String serverId, String toolSig);

    void saveExtServerToolAdapterMap(ExtServerToolAdapterMap map);

    void deleteExtServerToolAdapterMap(ExtServerToolAdapterMap map);

    /**
     * Checks whether the lesson was created from extServer and returns lessonFinishCallbackUrl if it's not blank.
     * 
     * @param user
     * @param lesson
     * @return
     * @throws UnsupportedEncodingException
     */
    String getLessonFinishCallbackUrl(User user, Lesson lesson) throws UnsupportedEncodingException;
}
