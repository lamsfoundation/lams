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

package org.lamsfoundation.lams.webservice;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.events.IEventNotificationService;
import org.lamsfoundation.lams.integration.ExtCourseClassMap;
import org.lamsfoundation.lams.integration.ExtServer;
import org.lamsfoundation.lams.integration.ExtUserUseridMap;
import org.lamsfoundation.lams.integration.UserInfoFetchException;
import org.lamsfoundation.lams.integration.security.Authenticator;
import org.lamsfoundation.lams.integration.security.RandomPasswordGenerator;
import org.lamsfoundation.lams.integration.service.IntegrationService;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.GroupUser;
import org.lamsfoundation.lams.learningdesign.dao.IGroupUserDAO;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.LessonClass;
import org.lamsfoundation.lams.lesson.dao.ILearnerProgressDAO;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationState;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.UserOrganisation;
import org.lamsfoundation.lams.usermanagement.dto.OrganisationDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.CentralConstants;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.HashUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 * @author Andrey Balan
 */
public class RegisterServlet extends HttpServlet {
    private static Logger logger = Logger.getLogger(RegisterServlet.class);

    @Autowired
    private IntegrationService integrationService;
    @Autowired
    private ILearnerProgressDAO learnerProgressDAO;
    @Autowired
    private ILessonService lessonService;
    @Autowired
    private ILearnerService learnerService;
    @Autowired
    private IGroupUserDAO groupUserDAO;
    @Autowired
    private IUserManagementService userManagementService;
    @Autowired
    private IEventNotificationService eventNotificationService;
    @Autowired
    private MessageService centralMessageService;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	String method = request.getParameter(CentralConstants.PARAM_METHOD);
	if (method.equals("addUserToGroupLessons")) {
	    addUserToGroupLessons(request, response);

	} else if (method.equals("removeUserFromGroup")) {
	    removeUserFromGroup(request, response);

	} else if (method.equals("resetUserTimeLimit")) {
	    resetUserTimeLimit(request, response);
	}
    }

    /*
     * Request Spring to lookup the applicationContext tied to the current ServletContext and inject service beans
     * available in that applicationContext.
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
	super.init(config);
	SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
    }

    /**
     * Add user to group lessons.
     *
     * External server call must follow the next format: http://<
     * <yourlamsserver>>/lams/central/Register.do?method=addUserToGroupLessons &serverId=%serverId%&datetime=%datetime%
     * &hashValue=%hashValue%&courseId=%courseId%&username=%username%&firstName=
     * %firstName%&lastName=%lastName%&email=%email&isJoinLesson=%isJoinLesson%
     * &isEmailParticipant=%isEmailParticipant%&isEmailCoordinator=%isEmailCoordinator%
     *
     * Here are the parameters explanation:
     *
     * @param method
     *            =addUserToGroupLessons - selfexplanatory.
     * @param serverId
     *            - this is a string of characters we'll provide to you
     * @param datetime
     *            - a character string with the date and time you make this request (format example 2011100608:15:10)
     * @param hashValue
     *            - The hash value is generated using the SHA1 algorithm on the following (all in lower case) [ datetime
     *            + username + method + serverId + serverKey ]
     * @param courseId
     *            - courseId is essentially a unique identifier for your Functional Speciality (note this is not the
     *            name of the Functional Speciality but its unique id)
     * @param username
     *            - this is your WV Central ID
     * @param usePrefix
     *            - if set to 1 - force the addition of the server integration prefix to usernames
     * @param firstName
     *            - well, first name
     * @param lastName
     *            - last name
     * @param email
     *            - email
     * @param isJoinLesson
     *            - if set to 1 -then join user to lesson
     * @param isEmailParticipant
     *            - if set to 1 -then LAMS will email the user his/her login details
     * @param isEmailCoordinator
     *            - if an email confirmation to and admin is required, just put the email address for the admin person
     *            here.
     */
    public void addUserToGroupLessons(HttpServletRequest request, HttpServletResponse response) throws IOException {
	try {

	    // Check if Server registration is available
	    boolean serverToServerEnable = Configuration.getAsBoolean(ConfigurationKeys.ENABLE_SERVER_REGISTRATION);
	    if (!serverToServerEnable) {
		String msg = "Server to server registration is not enabled";
		logger.error(msg);
		response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, msg);
		return;
	    }
	    String method = request.getParameter(CentralConstants.PARAM_METHOD);
	    String serverId = request.getParameter(CentralConstants.PARAM_SERVER_ID);
	    String datetime = request.getParameter(CentralConstants.PARAM_DATE_TIME);
	    String hashValue = request.getParameter(CentralConstants.PARAM_HASH_VALUE);
	    String groupName = request.getParameter(CentralConstants.PARAM_COURSE_ID);
	    String lessonId = request.getParameter(CentralConstants.ATTR_LESSON_ID);
	    String username = request.getParameter(CentralConstants.PARAM_USERNAME);
	    String usePrefix = request.getParameter("usePrefix");
	    String firstName = request.getParameter("firstName");
	    String lastName = request.getParameter("lastName");
	    String email = request.getParameter("email");
	    String isJoinLesson = request.getParameter("isJoinLesson");
	    String isEmailParticipant = request.getParameter("isEmailParticipant");
	    String isEmailCoordinator = request.getParameter("isEmailCoordinator");

	    if (serverId == null || datetime == null || hashValue == null || username == null) {
		String msg = "Parameters missing";
		logger.error(msg);
		response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parameters missing");
	    }

	    // authenticate external server
	    ExtServer extServer = integrationService.getExtServer(serverId);
	    Authenticator.authenticate(extServer, datetime, username, method, hashValue);

	    // create new password
	    String password = RandomPasswordGenerator.nextPassword(8);
	    String salt = HashUtil.salt();
	    String passwordHash = HashUtil.sha256(password, salt);

	    // check whether we need to use a prefix for users
	    if ("1".equals(usePrefix)) {
		username = extServer.getPrefix() + "_" + username.trim();
		logger.debug("Adding prefix to username:" + username);
	    }

	    // get user from the DB if exists, create it otherwise
	    ExtUserUseridMap userMap = integrationService.getImplicitExtUserUseridMap(extServer, username, passwordHash,
		    salt, firstName, lastName, email);
	    User user = userMap.getUser();

	    HashSet<Lesson> lessonsToJoin = new HashSet<Lesson>();
	    HashSet<Organisation> organisationsToJoin = new HashSet<Organisation>();

	    if (StringUtils.isNotBlank(groupName)) {
		// gets organisation from DB if exists, throws exception otherwise
		Organisation org = getOrganisationByName(groupName);
		organisationsToJoin.add(org);
		lessonsToJoin.addAll(org.getLessons());
	    }

	    if (StringUtils.isNotBlank(lessonId)) {
		Long lessonIdLong = Long.parseLong(lessonId);
		Lesson lesson = lessonService.getLesson(lessonIdLong);
		organisationsToJoin.add(lesson.getOrganisation());
		lessonsToJoin.add(lesson);
	    }

	    // add to all required organisations
	    List<String> learnerRole = new ArrayList<String>();
	    learnerRole.add(Role.ROLE_LEARNER.toString());
	    for (Organisation organisationToJoin : organisationsToJoin) {
		userManagementService.setRolesForUserOrganisation(user, organisationToJoin.getOrganisationId(),
			learnerRole);
	    }

	    // add to all required lessons (checks for duplicates)
	    for (Lesson lesson : lessonsToJoin) {
		boolean isAdded = lessonService.addLearner(lesson.getLessonId(), user.getUserId());
		if (isAdded) {
		    logger.debug(
			    "Added user:" + user.getLogin() + " to lesson:" + lesson.getLessonName() + " as a learner");
		}
	    }

	    // join user to all lessons in the group
	    if ("1".equals(isJoinLesson)) {
		for (Lesson lesson : lessonsToJoin) {

		    LearnerProgress learnerProgress = learnerProgressDAO.getLearnerProgressByLearner(user.getUserId(),
			    lesson.getLessonId());
		    if (learnerProgress == null) {
			logger.debug(
				"The learner:" + user.getLogin() + " is joining the lesson:" + lesson.getLessonId());
			learnerService.joinLesson(user.getUserId(), lesson.getLessonId());
		    } else {// don't join to lessons which user is a part of already but make sure time limit is reset
			resetUserTimeLimit(lesson, user);
		    }
		}
	    }

	    // send email to participant
	    if ("1".equals(isEmailParticipant)) {
		boolean isHtmlFormat = false;

		eventNotificationService.sendMessage(null, user.getUserId(),
			IEventNotificationService.DELIVERY_METHOD_MAIL,
			centralMessageService.getMessage("register.user.email.subject"),
			centralMessageService.getMessage("register.user.email.body", new Object[] { username, password }),
			isHtmlFormat);
	    }

	    // send email to coordinator
	    if (StringUtils.isNotBlank(isEmailCoordinator)) {
		boolean isHtmlFormat = false;

		List<User> coordinators = userManagementService.getAllUsersWithEmail(isEmailCoordinator);
		if ((coordinators == null) || (coordinators.size() == 0)) {
		    throw new RuntimeException("There are no coordinators with email: " + isEmailCoordinator);
		}
		User coordinator = coordinators.get(0);

		String registeredUserName = firstName + " " + lastName + " (" + username + ")";
		eventNotificationService.sendMessage(null, coordinator.getUserId(),
			IEventNotificationService.DELIVERY_METHOD_MAIL,
			centralMessageService.getMessage("notify.coordinator.register.user.email.subject"),
			centralMessageService.getMessage("notify.coordinator.register.user.email.body",
				new Object[] { registeredUserName }),
			isHtmlFormat);
	    }

	    writeAJAXOKResponse(response);

	} catch (Exception e) {
	    logger.error(e.getMessage());
	    writeAJAXResponse(response, "ERROR: " + e.getMessage());
	}
    }

    /**
     * Remove user from group lessons.
     *
     * External server call must follow the next format: http://<
     * <yourlamsserver>>/lams/central/Register.do?method=removeUserFromGroup &serverId=%serverId%&datetime=%datetime%
     * &hashValue=%hashValue%&courseId=%courseId%&username=%username%&isRemoveFromAllCourses=%isRemoveFromAllCourses%
     *
     * Here are the parameters explanation:
     *
     * @param method
     *            =removeUserFromGroup - selfexplanatory.
     * @param serverId
     *            - this is a string of characters we'll provide to you
     * @param datetime
     *            - a character string with the date and time you make this request (format example 2011100608:15:10)
     * @param hashValue
     *            - The hash value is generated using the SHA1 algorithm on the following (all in lower case) [ datetime
     *            + username + method + serverId + serverKey ]
     * @param courseId
     *            - courseId is essentially a unique identifier for your Functional Speciality (note this is not the
     *            name of the Functional Speciality but its unique id)
     * @param username
     *            - this is your WV Central ID
     * @param usePrefix
     *            - if set to 1 - force the addition of the server integration prefix to usernames
     * @param isRemoveFromAllCourses
     *            - if set to 1 -then ignores courseId parameter and removes from all courses
     */
    public void removeUserFromGroup(HttpServletRequest request, HttpServletResponse response) throws IOException {
	try {
	    // Check if Server registration is available
	    boolean serverToServerEnable = Configuration.getAsBoolean(ConfigurationKeys.ENABLE_SERVER_REGISTRATION);
	    if (!serverToServerEnable) {
		String msg = "Server to server registration is not enabled";
		logger.error(msg);
		response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, msg);
		return;
	    }
	    String method = request.getParameter(CentralConstants.PARAM_METHOD);
	    String serverId = request.getParameter(CentralConstants.PARAM_SERVER_ID);
	    String datetime = request.getParameter(CentralConstants.PARAM_DATE_TIME);
	    String hashValue = request.getParameter(CentralConstants.PARAM_HASH_VALUE);
	    String groupName = request.getParameter(CentralConstants.PARAM_COURSE_ID);
	    String username = request.getParameter(CentralConstants.PARAM_USERNAME);
	    String isRemoveFromAllCourses = request.getParameter("isRemoveFromAllCourses");
	    String usePrefix = request.getParameter("usePrefix");

	    if (serverId == null || datetime == null || hashValue == null || username == null) {
		String msg = "Parameters missing";
		logger.error(msg);
		response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parameters missing");
	    }

	    // authenticate external server
	    ExtServer extServer = integrationService.getExtServer(serverId);
	    Authenticator.authenticate(extServer, datetime, username, method, hashValue);

	    // check whether we need to use a prefix for users
	    if ("1".equals(usePrefix)) {
		username = extServer.getPrefix() + "_" + username.trim();
		logger.debug("Adding prefix to username:" + username);
	    }

	    // get user from the DB if exists, throws exception otherwise
	    ExtUserUseridMap userMap = getExtUserUseridMap(extServer, username);
	    User user = userMap.getUser();

	    ArrayList<Lesson> lessons = new ArrayList<Lesson>();
	    if ("1".equals(isRemoveFromAllCourses)) {
		Set<UserOrganisation> userOrganisations = user.getUserOrganisations();

		// create list of organisationDtos in order to be able to delete userOrganisations afterwards
		List<OrganisationDTO> organisationDtos = new ArrayList<OrganisationDTO>();
		for (UserOrganisation userOrganisation : userOrganisations) {
		    Organisation organisation = userOrganisation.getOrganisation();
		    OrganisationDTO organisationDto = organisation.getOrganisationDTO();
		    organisationDtos.add(organisationDto);
		    lessons.addAll(organisation.getLessons());
		}

		for (OrganisationDTO organisationDto : organisationDtos) {
		    removeGroupMembership(user, organisationDto.getOrganisationID());
		}
	    } else {
		Organisation organisation = getOrganisationByName(groupName);
		removeGroupMembership(user, organisation.getOrganisationId());

		lessons.addAll(organisation.getLessons());
	    }

	    // remove user from lessons
	    removeUserFromLessons(user, lessons);

	    writeAJAXOKResponse(response);

	} catch (Exception e) {
	    writeAJAXResponse(response, "ERROR: " + e.getMessage());
	}
    }

    /**
     * Resets user's time limit for all lessons in a group with scheduledToCloseForIndividuals setting on. In case
     * lesson's time limit is not individual it does nothing.
     *
     * External server call must follow the next format: http://<
     * <yourlamsserver>>/lams/central/Register.do?method=resetUserTimeLimit &serverId=%serverId%&datetime=%datetime%
     * &hashValue=%hashValue%&courseId=%courseId%&username=%username%
     *
     * Here are the parameters explanation:
     *
     * @param method
     *            =resetUserTimeLimit - selfexplanatory.
     * @param serverId
     *            - this is a string of characters we'll provide to you
     * @param datetime
     *            - a character string with the date and time you make this request (format example 2011100608:15:10)
     * @param hashValue
     *            - The hash value is generated using the SHA1 algorithm on the following (all in lower case) [ datetime
     *            + username + method + serverId + serverKey ]
     * @param courseId
     *            - courseId is essentially a unique identifier for your Functional Speciality (note this is not the
     *            name of the Functional Speciality but its unique id)
     * @param username
     *            - this is your WV Central ID
     * @param usePrefix
     *            - if set to 1 - force the addition of the server integration prefix to usernames
     */
    public void resetUserTimeLimit(HttpServletRequest request, HttpServletResponse response) throws IOException {
	try {
	    // Check if Server registration is available
	    boolean serverToServerEnable = Configuration.getAsBoolean(ConfigurationKeys.ENABLE_SERVER_REGISTRATION);
	    if (!serverToServerEnable) {
		String msg = "Server to server registration is not enabled";
		logger.error(msg);
		response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, msg);
		return;
	    }

	    String method = request.getParameter(CentralConstants.PARAM_METHOD);
	    String serverId = request.getParameter(CentralConstants.PARAM_SERVER_ID);
	    String datetime = request.getParameter(CentralConstants.PARAM_DATE_TIME);
	    String hashValue = request.getParameter(CentralConstants.PARAM_HASH_VALUE);
	    String courseId = request.getParameter(CentralConstants.PARAM_COURSE_ID);
	    String username = request.getParameter(CentralConstants.PARAM_USERNAME);
	    String usePrefix = request.getParameter("usePrefix");

	    if (serverId == null || datetime == null || hashValue == null || username == null || courseId == null) {
		String msg = "Parameters missing";
		logger.error(msg);
		response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parameters missing");
	    }

	    // authenticate external server
	    ExtServer extServer = integrationService.getExtServer(serverId);
	    Authenticator.authenticate(extServer, datetime, username, method, hashValue);

	    // check whether we need to use a prefix for users
	    if ("1".equals(usePrefix)) {
		username = extServer.getPrefix() + "_" + username.trim();
		logger.debug("Adding prefix to username:" + username);
	    }

	    // get user from the DB if exists, throws exception otherwise
	    ExtUserUseridMap userMap = getExtUserUseridMap(extServer, username);
	    User user = userMap.getUser();

	    // get organisation from DB if exists, throws exception otherwise
	    Organisation org = getOrganisationByName(courseId);

	    // reset time limit
	    if (org.getLessons() != null) {
		for (Lesson lesson : (Set<Lesson>) org.getLessons()) {
		    resetUserTimeLimit(lesson, user);
		}
	    }

	    writeAJAXOKResponse(response);

	} catch (Exception e) {
	    logger.error(e.getMessage());
	    writeAJAXResponse(response, "ERROR: " + e.getMessage());
	}
    }

    /**
     * resetUserTimeLimit
     *
     * @param lesson
     * @param user
     */
    private void resetUserTimeLimit(Lesson lesson, User user) {
	// check if lesson is set to be finished for individual users then reset finish date
	if (lesson.isScheduledToCloseForIndividuals()) {
	    GroupUser groupUser = groupUserDAO.getGroupUser(lesson, user.getUserId());
	    if (groupUser != null) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, lesson.getScheduledNumberDaysToLessonFinish());
		Date endDate = calendar.getTime();
		groupUser.setScheduledLessonEndDate(endDate);
		logger.debug("Reset time limit for user:" + user.getLogin() + "in lesson:" + lesson.getLessonId()
			+ " to " + endDate);
	    }
	}
    }

    private ExtUserUseridMap getExtUserUseridMap(ExtServer extServer, String extUsername)
	    throws UserInfoFetchException {
	Map<String, Object> properties = new HashMap<String, Object>();
	properties.put("extServer.sid", extServer.getSid());
	properties.put("extUsername", extUsername);
	List list = userManagementService.findByProperties(ExtUserUseridMap.class, properties);
	if (list == null || list.size() == 0) {
	    throw new RuntimeException("There is no user with username: " + extUsername
		    + " assossiated with external server " + extServer.getSid());
	} else {
	    return (ExtUserUseridMap) list.get(0);
	}
    }

    // newer method which accepts course name, a parent org id, a flag for whether user should get
    // 'teacher' roles, and a flag for whether to use a prefix in the org's name
    private Organisation getOrganisationByName(String name) {
	Map<String, Object> properties = new HashMap<String, Object>();
	properties.put("name", name);
	properties.put("organisationState.organisationStateId", OrganisationState.ACTIVE);
	List<Organisation> list = userManagementService.findByProperties(Organisation.class, properties);
	if (list == null || list.size() == 0) {
	    throw new RuntimeException("There is no active course with courseId: " + name);
	} else {
	    Organisation organisation = list.get(0);
	    return organisation;
	}
    }

    // newer method which accepts course name, a parent org id, a flag for whether user should get
    // 'teacher' roles, and a flag for whether to use a prefix in the org's name
    private ExtCourseClassMap getExtCourseClassMap(ExtServer extServer, ExtUserUseridMap userMap,
	    String extCourseId, Boolean isTeacher) {
	Map<String, Object> properties = new HashMap<String, Object>();
	properties.put("courseid", extCourseId);
	properties.put("extServer.sid", extServer.getSid());
	List list = userManagementService.findByProperties(ExtCourseClassMap.class, properties);
	if (list == null || list.size() == 0) {
	    throw new RuntimeException("There is no course with courseId: " + extCourseId);
	} else {
	    ExtCourseClassMap map = (ExtCourseClassMap) list.get(0);
	    return map;
	}
    }

    private void removeGroupMembership(User user, Integer organisationId) {
	UserOrganisation userOrganisation = userManagementService.getUserOrganisation(user.getUserId(), organisationId);

	Set<UserOrganisation> userOrganisations = user.getUserOrganisations();
	userOrganisations.remove(userOrganisation);
	userManagementService.saveUser(user);

	// userOrganisation and UserOrganisationRoles will be deleted by Hibernate automatically.
    }

    private void removeUserFromLessons(User user, Collection<Lesson> lessons) {
	for (Lesson lesson : lessons) {
	    LessonClass grouping = lesson.getLessonClass();
	    Group group = grouping.getLearnersGroup();

	    boolean isSuccefullyRemoved = group.getUsers().remove(user);
	    if (isSuccefullyRemoved) {
		logger.debug("Removed user:" + user.getLogin() + " from lesson:" + lesson.getLessonName());
	    }
	}
    }

    protected void writeAJAXResponse(HttpServletResponse response, String output) throws IOException {
	response.setContentType("text/html;charset=utf-8");
	PrintWriter out = response.getWriter();

	if (output.length() > 0) {
	    out.println(output);
	}

	out.flush();
	out.close();
    }

    protected void writeAJAXOKResponse(HttpServletResponse response) throws IOException {
	writeAJAXResponse(response, "OK");
    }

}
