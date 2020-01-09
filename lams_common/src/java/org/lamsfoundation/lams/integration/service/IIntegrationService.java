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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.imsglobal.lti.launch.LtiSigningException;
import org.lamsfoundation.lams.integration.ExtCourseClassMap;
import org.lamsfoundation.lams.integration.ExtServerLessonMap;
import org.lamsfoundation.lams.integration.ExtServer;
import org.lamsfoundation.lams.integration.ExtServerToolAdapterMap;
import org.lamsfoundation.lams.integration.ExtUserUseridMap;
import org.lamsfoundation.lams.integration.UserInfoFetchException;
import org.lamsfoundation.lams.integration.UserInfoValidationException;
import org.lamsfoundation.lams.integration.dto.ExtGroupDTO;
import org.lamsfoundation.lams.integration.util.GroupInfoFetchException;
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

    ExtCourseClassMap getExtCourseClassMap(ExtServer extServer, ExtUserUseridMap userMap, String extCourseId,
	    String prettyCourseName, String method, Boolean prefix) throws UserInfoValidationException;

    ExtCourseClassMap getExtCourseClassMap(ExtServer extServer, ExtUserUseridMap userMap, String extCourseId,
	    String prettyCourseName, String method) throws UserInfoValidationException;

    ExtCourseClassMap getExtCourseClassMap(ExtServer extServer, ExtUserUseridMap userMap, String extCourseId,
	    String extCourseName, String parentOrgId, String method, Boolean prefix)
	    throws UserInfoValidationException;

    ExtUserUseridMap getExtUserUseridMap(ExtServer extServer, String extUsername, boolean prefix)
	    throws UserInfoFetchException, UserInfoValidationException;

    /**
     * compatibility method to support existing integrations. It does callback call to integrated server to get user
     * details.
     *
     * @param extServer
     * @param extUsername
     * @return
     * @throws UserInfoFetchException
     * @throws UserInfoValidationException
     */
    ExtUserUseridMap getExtUserUseridMap(ExtServer extServer, String extUsername)
	    throws UserInfoFetchException, UserInfoValidationException;

    /**
     * Returns ExtUserUseridMap from DB, and null if it doesn't exist
     *
     * @param extServer
     * @param userId
     * @return
     */
    // ExtUserUseridMap getExtUserUseridMapByUserId(ExtServer extServer, Integer userId);

    List<ExtUserUseridMap> getExtUserUseridMapByExtServer(ExtServer extServer);

    /**
     * Returns ExtUserUseridMap from DB, and null if it doesn't exist
     *
     * @param extServer
     * @param extUsername
     * @return
     */
    ExtUserUseridMap getExistingExtUserUseridMap(ExtServer extServer, String extUsername) throws UserInfoFetchException;

    /**
     * Returns integration server by its automatically-created sid.
     * 
     * @param serverId
     * @return
     */
    ExtServer getExtServer(Integer sid);

    /**
     * Returns integration server by its human-entered serverId.
     * 
     * @param serverId
     * @return
     */
    ExtServer getExtServer(String serverId);

    /**
     * Returns ExtServerLessonMap for the LTI Tool Consumer identified by serverId.
     * 
     * @param serverId
     * @param resourceLinkId
     *            resource_link_id parameter from LTI request
     * @return
     */
    ExtServerLessonMap getLtiConsumerLesson(String serverId, String resourceLinkId);

    ExtServerLessonMap getExtServerLessonMap(Long lessonId);

    /**
     * Returns ExtCourseClassMap. If ExtCourseClassMap doesn't existent - returns null and does not create a new
     * ExtCourseClassMap.
     *
     * @param sid
     * @param extCourseId
     * @return ExtCourseClassMap if it exists, null otherwise
     */
    ExtCourseClassMap getExtCourseClassMap(Integer sid, String extCourseId);

    /**
     * @param extServer
     * @param extUsername
     * @param firstName
     * @param lastName
     * @param locale
     * @param country
     * @param email
     * @param prefix
     * @param isUpdateUserDetails
     *            whether user details should be updated with provided parameters
     * @return
     * @throws UserInfoValidationException
     */
    ExtUserUseridMap getImplicitExtUserUseridMap(ExtServer extServer, String extUsername, String firstName,
	    String lastName, String locale, String country, String email, boolean prefix, boolean isUpdateUserDetails)
	    throws UserInfoValidationException;

    /**
     * Checks whether user was created via integrations.
     * 
     * @param userId
     * @return
     */
    boolean isIntegrationUser(Integer userId);

    ExtUserUseridMap getImplicitExtUserUseridMap(ExtServer extServer, String extUsername, String password, String salt,
	    String firstName, String lastName, String email) throws UserInfoValidationException;

    /**
     * @return all available integrated servers (that is excluding tool consumers)
     */
    List<ExtServer> getAllExtServers();

    /**
     * @return all available tool consumers
     */
    List<ExtServer> getAllToolConsumers();

    void saveExtServer(ExtServer map);

    String hash(ExtServer extServer, String extUsername, String timestamp);

    List<ExtServerToolAdapterMap> getMappedServers(String toolSig);

    ExtServerToolAdapterMap getMappedServer(String serverId, String toolSig);

    void saveExtServerToolAdapterMap(ExtServerToolAdapterMap map);

    void deleteExtServerToolAdapterMap(ExtServerToolAdapterMap map);

    /**
     * Creates new ExtServerLessonMap object. Method is suitable for creating lessons via integration servers.
     * 
     * @param lessonId
     * @param extServer
     */
    ExtServerLessonMap createExtServerLessonMap(Long lessonId, ExtServer extServer);

    /**
     * Creates new ExtServerLessonMap object. Method is suitable for creating lessons via LTI tool consumers as long as
     * they provide resourceLinkId as a parameter and not lessonId.
     * 
     * @param lessonId
     * @param resourceLinkId
     *            resource_link_id parameter sent by LTI tool consumer
     * @param extServer
     */
    ExtServerLessonMap createExtServerLessonMap(Long lessonId, String resourceLinkId, ExtServer extServer);

    /**
     * Checks whether the lesson was created from extServer (not an LTI consumer one) and returns lessonFinishCallbackUrl if it's not blank.
     *
     * @param user
     * @param lesson
     * @return
     * @throws UnsupportedEncodingException
     */
    String getLessonFinishCallbackUrl(User user, Lesson lesson) throws UnsupportedEncodingException;
    
    /**
     * Check whether specified lesson was created using LTI consumer, and if so - push user mark to that server
     * 
     * @param user
     * @param lesson
     */
    void pushMarkToLtiConsumer(User user, Lesson lesson, Double mark);

    /**
     * Checks whether lesson was created using integrations and whether according integrated server has ExtGroupsUrl
     * property set up.
     *
     * @param lessonId
     * @return true in case lesson was created using integrations and according integrated server has ExtGroupsUrl
     *         property set up, false otherwise
     */
    boolean isIntegratedServerGroupFetchingAvailable(Long lessonId);

    List<ExtGroupDTO> getExtGroups(Long lessonId, String[] extGroupIds) throws GroupInfoFetchException, UserInfoValidationException, IOException;

    ExtCourseClassMap getExtCourseClassMap(Integer sid, Long lessonId);
    
    /**
     * Try to get users from ext server using membership service.
     * 
     * @param lessonId if supplied, user will be added to the according lesson; and only to the course otherwise
     */
    void addUsersUsingMembershipService(ExtServer extServer, Long lessonId, String extCourseId, String resourceLinkId)
	    throws IOException, UserInfoFetchException, UserInfoValidationException;
    
    /**
     * Adds an external user to the course with specified courseId.
     */
    ExtUserUseridMap addExtUserToCourse(ExtServer extServer, String method, String username, String firstName, String lastName,
	    String email, String extCourseId, String countryIsoCode, String langIsoCode)
	    throws UserInfoFetchException, UserInfoValidationException;

    /**
     * Add an external user to the course with specified courseId and then adds it the the lesson with specified
     * lessonId. (This method makes internal call to addExtUserToCourse()).
     */
    ExtUserUseridMap addExtUserToCourseAndLesson(ExtServer extServer, String method, Long lessonId, String username, String firstName,
	    String lastName, String email, String extCourseId, String countryIsoCode, String langIsoCode)
	    throws UserInfoFetchException, UserInfoValidationException;

    /**
     * Creates an external org and normal org. It does not set roles for the creator.
     */
    ExtCourseClassMap createExtCourseClassMap(ExtServer extServer, Integer userId, String extCourseId,
	    String extCourseName, String parentOrgId, Boolean prefix) throws UserInfoValidationException;
}