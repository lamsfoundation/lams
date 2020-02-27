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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.integration.ExtCourseClassMap;
import org.lamsfoundation.lams.integration.ExtServer;
import org.lamsfoundation.lams.integration.ExtUserUseridMap;
import org.lamsfoundation.lams.integration.UserInfoFetchException;
import org.lamsfoundation.lams.integration.UserInfoValidationException;
import org.lamsfoundation.lams.integration.security.RandomPasswordGenerator;
import org.lamsfoundation.lams.integration.service.IIntegrationService;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.logevent.LogEvent;
import org.lamsfoundation.lams.logevent.service.ILogEventService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.HashUtil;
import org.lamsfoundation.lams.util.hibernate.HibernateSessionManager;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Consumes enrolments file produced by EnrolmentParser.
 * Accepts file-input parameter with a path to file to process.
 *
 * @author Marcin Cieslak
 */
public class SPEnrolmentServlet extends HttpServlet {
    private static final long serialVersionUID = -5348322697437185394L;

    private static final Logger logger = Logger.getLogger(SPEnrolmentServlet.class);

    private static final String FILE_INPUT_DEFAULT_NAME = "LAMS-OUTPUT.csv";
    private static final String FILE_INPUT_PARAM = "file-input";
    private static final String DELIMITER = "\\|";
    private static final String INTEGRATED_SERVER_NAME = "saml";
    private static final String ROLE_STAFF = "staff";

    private static ILessonService lessonService = null;
    private static IUserManagementService userManagementService = null;
    private static ILogEventService logEventService = null;
    private static IIntegrationService integrationService = null;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
	// check if path to input file is provided as GET parameter
	// if not, use default one of <tempDir>/LAMS-OUTPUT.csv
	String fileInputParam = request.getParameter(FILE_INPUT_PARAM);
	Path fileInput = StringUtils.isBlank(fileInputParam)
		? Paths.get(Configuration.get(ConfigurationKeys.LAMS_TEMP_DIR), FILE_INPUT_DEFAULT_NAME)
		: Paths.get(fileInputParam);

	// run processing in a separate thread as it can take a while and request would time out
	new Thread(() -> {
	    try {
		logger.info("SP enrolments provisioning starting");
		// start interacting with DB
		HibernateSessionManager.openSession();

		// split each line into list of trimmed pieces
		List<List<String>> lines = Files.readAllLines(fileInput).parallelStream().unordered()
			.map(line -> Arrays.stream(line.split(DELIMITER)).map(elem -> elem.trim())
				.collect(Collectors.toList()))
			// filter out malformed rows
			.filter(row -> row.size() == 7).collect(Collectors.toList());
		// it is easier to detect whether we process staff or learners just once
		// than for each user - they do not come together anyway
		boolean isStaffMode = !lines.isEmpty() && lines.get(0).get(6).equals(ROLE_STAFF);

		// map of course code (ID) -> course name
		ConcurrentMap<String, String> courses = lines.parallelStream().unordered().collect(
			Collectors.toConcurrentMap(elem -> elem.get(0), elem -> elem.get(1), (elem1, elem2) -> elem1));

		// map of user login -> email, first name
		// for learner email is login, for staff it is a different ID in email format
		ConcurrentMap<String, String[]> users = lines.parallelStream().unordered()
			.collect(Collectors.toConcurrentMap(
				elem -> elem.get(6).equals(ROLE_STAFF) ? elem.get(3) : elem.get(5),
				elem -> new String[] { elem.get(5), elem.get(4) }, (elem1, elem2) -> elem1));

		// map of course code -> subcourse code -> user logins
		Map<String, Map<String, List<String>>> mappings = lines.stream()
			.collect(Collectors.groupingBy(elem -> elem.get(0), LinkedHashMap::new,
				Collectors.groupingBy(elem -> elem.get(2), LinkedHashMap::new,
					Collectors.mapping(
						elem -> elem.get(6).equals(ROLE_STAFF) ? elem.get(3) : elem.get(5),
						Collectors.toList()))));

		// map of user login -> course ID -> role IDs
		Map<String, Map<Integer, Set<Integer>>> existingRoles = new HashMap<>();
		// map of user login -> user ID
		Map<String, Integer> userIDs = new HashMap<>();

		// find sysadmin as he/she will be the creator of organisations
		Organisation rootOrganisation = userManagementService.getRootOrganisation();
		Integer creatorId = rootOrganisation.getCreatedBy().getUserId();

		ExtServer extServer = integrationService.getExtServer(INTEGRATED_SERVER_NAME);
		if (extServer == null) {
		    throw new ServletException("Integrated server not found: " + INTEGRATED_SERVER_NAME);
		}

		// create users
		Set<User> allUsersParsed = createUsers(extServer, creatorId, users, userIDs, existingRoles);
		Set<User> allUsersinCourses = new HashSet<>();
		// go through each course
		for (Entry<String, String> courseEntry : courses.entrySet()) {
		    String courseCode = courseEntry.getKey();
		    // create or get existing course
		    Organisation course = getCourse(courseCode, courseEntry.getValue(), extServer, creatorId,
			    rootOrganisation);

		    // collect learners from all subcourses of this course
		    Set<User> allUsersInSubcourses = assignUsers(course, mappings, extServer, creatorId, allUsersParsed,
			    userIDs, existingRoles, isStaffMode);
		    allUsersinCourses.addAll(allUsersInSubcourses);
		}

		// users who are part of courses but are not in the file anymore are eligible for disabling
		allUsersinCourses.removeAll(allUsersParsed);
		for (User user : allUsersinCourses) {
		    // make a flat set of roles from all subcourses
		    Set<Integer> roles = userManagementService.getRolesForUser(user.getUserId()).values().stream()
			    .collect(HashSet::new, Set::addAll, Set::addAll);
		    if (isStaffMode) {
			// check if the user is learner in any course
			roles.remove(Role.ROLE_MONITOR);
			roles.remove(Role.ROLE_AUTHOR);
		    } else {
			// check if the user is staff in any course
			roles.remove(Role.ROLE_LEARNER);
		    }
		    if (roles.isEmpty()) {
			// he is only a learner or this is staff mode, so disable
			userManagementService.disableUser(user.getUserId());

			String message = "User \"" + user.getLogin() + "\" disabled";
			logger.info(message);
			logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, creatorId, null, null, null,
				"SPEnrolment: " + message);
		    }
		}

		logger.info("SP enrolments provisioning completed successfully");
	    } catch (Exception e) {
		logger.error("Error while provisioning SP enrolments", e);
	    } finally {
		HibernateSessionManager.closeSession();
	    }
	}).start();

	response.getWriter().print("Provisioning is running. Check logs.");
    }

    private Set<User> createUsers(ExtServer extServer, Integer creatorId, Map<String, String[]> users,
	    Map<String, Integer> userIDs, Map<String, Map<Integer, Set<Integer>>> existingRoles)
	    throws UserInfoValidationException, UserInfoFetchException {
	Set<User> allUsersParsed = new HashSet<>();
	for (Entry<String, String[]> userEntry : users.entrySet()) {
	    // email servers as login
	    String login = userEntry.getKey();
	    User user = userManagementService.getUserByLogin(login);
	    if (user == null) {
		String salt = HashUtil.salt();
		String password = HashUtil.sha256(RandomPasswordGenerator.nextPassword(10), salt);

		String email = userEntry.getValue()[0];
		String firstName = userEntry.getValue()[1];
		ExtUserUseridMap userMap = integrationService.getImplicitExtUserUseridMap(extServer, login, password,
			salt, firstName, ".", email);
		user = userMap.getUser();

		String message = "User created with login \"" + login + "\" and ID " + user.getUserId();
		logger.info(message);
		logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, creatorId, null, null, null,
			"SPEnrolment: " + message);
	    } else {
		ExtUserUseridMap userMap = integrationService.getExistingExtUserUseridMap(extServer, login);
		if (userMap == null) {
		    userMap = new ExtUserUseridMap();
		    userMap.setExtServer(extServer);
		    userMap.setUser(user);
		    userMap.setExtUsername(login);
		    userManagementService.save(userMap);

		    String message = "External user created for existing user with login \"" + login + "\" and ID "
			    + user.getUserId();
		    logger.info(message);
		    logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, creatorId, null, null, null,
			    "SPEnrolment: " + message);
		}
		if (user.getDisabledFlag()) {
		    // re-enable user who was disabled before
		    user.setDisabledFlag(false);
		    userManagementService.save(user);
		}
	    }

	    // fill data for later usage
	    existingRoles.put(login, userManagementService.getRolesForUser(user.getUserId()));
	    userIDs.put(login, user.getUserId());
	    // add user to a collection of all users in the parsed file
	    allUsersParsed.add(user);
	}

	return allUsersParsed;
    }

    @SuppressWarnings("unchecked")
    private Organisation getCourse(String courseCode, String courseName, ExtServer extServer, Integer creatorId,
	    Organisation rootOrganisation) throws UserInfoValidationException {
	Organisation course = null;
	List<Organisation> organisations = userManagementService.findByProperty(Organisation.class, "code", courseCode);
	// create course
	if (organisations.isEmpty()) {
	    String name = courseName;
	    ExtCourseClassMap extOrgMap = integrationService.createExtCourseClassMap(extServer, creatorId, name, name,
		    rootOrganisation.getOrganisationId().toString(), false);
	    course = extOrgMap.getOrganisation();
	    course.setCode(courseCode);
	    userManagementService.save(course);

	    String message = "Course created with code \"" + courseCode + "\" and name \"" + name + "\" and ID "
		    + course.getOrganisationId();
	    logger.info(message);
	    logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, creatorId, null, null, null,
		    "SPEnrolment: " + message);
	} else {
	    course = organisations.get(0);
	    String name = course.getName();

	    ExtCourseClassMap extOrgMap = integrationService.getExtCourseClassMap(extServer.getSid(), name);
	    if (extOrgMap == null) {
		extOrgMap = new ExtCourseClassMap();
		extOrgMap.setCourseid(name);
		extOrgMap.setExtServer(extServer);
		extOrgMap.setOrganisation(course);
		userManagementService.save(extOrgMap);

		String message = "External course created for existing course with code \"" + courseCode
			+ "\" and name \"" + name + "\" and ID " + course.getOrganisationId();
		logger.info(message);
		logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, creatorId, null, null, null,
			"SPEnrolment: " + message);
	    }
	}
	return course;
    }

    @SuppressWarnings("unchecked")
    private Set<User> assignUsers(Organisation course, Map<String, Map<String, List<String>>> mappings,
	    ExtServer extServer, Integer creatorId, Set<User> allUsersParsed, Map<String, Integer> userIDs,
	    Map<String, Map<Integer, Set<Integer>>> existingRoles, boolean isStaffMode)
	    throws UserInfoValidationException {
	Set<User> allUsersInSubcourses = new HashSet<>();
	String courseCode = course.getCode();
	// go through each subcourse
	for (Entry<String, List<String>> subcourseEntry : mappings.get(courseCode).entrySet()) {
	    Organisation subcourse = null;
	    String subcourseCode = subcourseEntry.getKey();

	    Map<String, Object> queryParams = new HashMap<>();
	    queryParams.put("code", subcourseCode);
	    queryParams.put("parentOrganisation.organisationId", course.getOrganisationId());
	    List<Organisation> organisations = userManagementService.findByProperties(Organisation.class, queryParams);

	    // create subcourse
	    if (organisations.isEmpty()) {

		ExtCourseClassMap extSubOrgMap = integrationService.createExtCourseClassMap(extServer, creatorId,
			subcourseCode, subcourseCode, course.getOrganisationId().toString(), false);
		subcourse = extSubOrgMap.getOrganisation();
		subcourse.setCode(subcourseCode);
		userManagementService.save(subcourse);

		String message = "Subcourse created with code and name \"" + courseCode + "\" and ID "
			+ subcourse.getOrganisationId();
		logger.info(message);
		logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, creatorId, null, null, null,
			"SPEnrolment: " + message);
	    } else {
		subcourse = organisations.get(0);
		String name = subcourse.getName();

		ExtCourseClassMap extOrgMap = integrationService.getExtCourseClassMap(extServer.getSid(), name);
		if (extOrgMap == null) {
		    extOrgMap = new ExtCourseClassMap();
		    extOrgMap.setCourseid(name);
		    extOrgMap.setExtServer(extServer);
		    extOrgMap.setOrganisation(subcourse);
		    userManagementService.save(extOrgMap);

		    String message = "External subcourse created for existing subcourse with code \"" + courseCode
			    + "\" and name \"" + name + "\" and ID " + course.getOrganisationId();
		    logger.info(message);
		    logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, creatorId, null, null, null,
			    "SPEnrolment: " + message);
		}
	    }

	    Integer subcourseId = subcourse.getOrganisationId();

	    // get existing learners/staff members for given subcourse
	    Collection<User> subcourseUsers = userManagementService.getUsersFromOrganisationByRole(subcourseId,
		    isStaffMode ? Role.MONITOR : Role.LEARNER, true);
	    // add users to set containing all users in any subcourse which is processed
	    allUsersInSubcourses.addAll(subcourseUsers);

	    // go through each user
	    for (String login : subcourseEntry.getValue()) {

		// check if the user is already a learner/staff member in the subcourse
		// if so, there is nothing to do
		boolean userAlreadyAssigned = false;
		Integer userId = userIDs.get(login);
		Iterator<User> subcourseUserIterator = subcourseUsers.iterator();
		while (subcourseUserIterator.hasNext()) {
		    if (userId.equals(subcourseUserIterator.next().getUserId())) {
			// IMPORTANT: if we found a matching existing learner/staff member, we get remove him from this collection
			// so after this loop he does not get removed from subcourses
			subcourseUserIterator.remove();
			userAlreadyAssigned = true;
			break;
		    }
		}
		if (userAlreadyAssigned) {
		    continue;
		}

		// the user is not a learner/staff member yet, so assign him the role and add him to lessons
		Set<Integer> existingSubcourseRoles = existingRoles.get(login).get(subcourseId);
		if (existingSubcourseRoles == null) {
		    existingSubcourseRoles = new HashSet<>();
		}
		if (isStaffMode) {
		    existingSubcourseRoles.add(Role.ROLE_AUTHOR);
		    existingSubcourseRoles.add(Role.ROLE_MONITOR);
		} else {
		    existingSubcourseRoles.add(Role.ROLE_LEARNER);
		}
		userManagementService.setRolesForUserOrganisation(userId, subcourseId, existingSubcourseRoles);

		for (Lesson lesson : lessonService.getLessonsByGroup(subcourseId)) {
		    if (isStaffMode) {
			lessonService.addStaffMember(lesson.getLessonId(), userId);
		    } else {
			lessonService.addLearner(lesson.getLessonId(), userId);
		    }
		}

		String message = (isStaffMode ? "Teacher" : "Learner") + " \"" + login + "\" added to subcourse "
			+ subcourseId + " and its lessons";
		logger.info(message);
		logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, creatorId, null, null, null,
			"SPEnrolment: " + message);
	    }

	    // user is a learner/staff member, but he should not; remove him role from subcourse and lessons
	    for (User user : subcourseUsers) {
		Map<Integer, Set<Integer>> existingSubcoursesRoles = existingRoles.get(user.getLogin());
		Set<Integer> existingSubcourseRoles = existingSubcoursesRoles == null ? null
			: existingSubcoursesRoles.get(subcourseId);
		if (existingSubcourseRoles != null) {
		    if (isStaffMode) {
			existingSubcourseRoles.remove(Role.ROLE_AUTHOR);
			existingSubcourseRoles.remove(Role.ROLE_MONITOR);
		    } else {
			existingSubcourseRoles.remove(Role.ROLE_LEARNER);
		    }
		    userManagementService.setRolesForUserOrganisation(user.getUserId(), subcourseId,
			    existingSubcourseRoles);

		    for (Lesson lesson : lessonService.getLessonsByGroup(subcourseId)) {
			if (isStaffMode) {
			    lessonService.removeStaffMember(lesson.getLessonId(), user.getUserId());
			} else {
			    lessonService.removeLearner(lesson.getLessonId(), user.getUserId());
			}

		    }

		    String message = (isStaffMode ? "Teacher" : "Learner") + " \"" + user.getLogin()
			    + "\" removed from subcourse " + subcourseId + " and its lessons";
		    logger.info(message);
		    logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, creatorId, null, null, null,
			    "SPEnrolment: " + message);
		}
	    }
	}

	return allUsersInSubcourses;
    }

//    @SuppressWarnings("unchecked")
//    private Set<User> assignTeachers(Organisation course, List<String> logins, ExtServer extServer, Integer creatorId,
//	    Set<User> allUsersParsed, Map<String, Integer> userIDs,
//	    Map<String, Map<Integer, Set<Integer>>> existingRoles) throws UserInfoValidationException {
//	// this is for detecting which users should be disabled
//	Set<User> allUsersInCourse = new HashSet<>();
//
//	Integer courseId = course.getOrganisationId();
//
//	// get existing teachers for given course
//	Collection<User> courseTeachers = userManagementService.getUsersFromOrganisationByRole(courseId, Role.MONITOR,
//		true);
//	// add users to set containing all users in any course which is processed
//	allUsersInCourse.addAll(courseTeachers);
//
//	// go through each user
//	for (String login : logins) {
//	    // check if user is alread a teacher in this course
//	    boolean userAlreadyStaff = false;
//	    Integer userId = userIDs.get(login);
//	    User user = null;
//	    Iterator<User> courseTeacherIterator = courseTeachers.iterator();
//	    while (courseTeacherIterator.hasNext()) {
//		User nextUser = courseTeacherIterator.next();
//		if (userId.equals(nextUser.getUserId())) {
//		    user = nextUser;
//		    // IMPORTANT: if we found a matching existing teacher, we get remove him from this collection
//		    // so after this loop he does not get removed from the course
//		    courseTeacherIterator.remove();
//		    userAlreadyStaff = true;
//		    break;
//		}
//	    }
//
//	    // if user is not a teacher, add him
//	    if (!userAlreadyStaff) {
//		// the user is not a teacher in course yet, so assign him roles
//		Set<Integer> existingCourseRoles = existingRoles.get(login).get(courseId);
//		if (existingCourseRoles == null) {
//		    existingCourseRoles = new HashSet<>();
//		}
//		// we modify existing role collection, as he can be also a course admin and/or learner
//		existingCourseRoles.add(Role.ROLE_AUTHOR);
//		existingCourseRoles.add(Role.ROLE_MONITOR);
//		userManagementService.setRolesForUserOrganisation(userId, courseId, existingCourseRoles);
//
//		String message = "Teacher \"" + login + "\" added to course " + courseId;
//		logger.info(message);
//		logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, creatorId, null, null, null,
//			"SPEnrolment: " + message);
//	    }
//
//	    // now go through each subcourse and make sure the user is also a teacher there and in its lessons
//	    for (Organisation subcourse : course.getChildOrganisations()) {
//		if (user == null) {
//		    user = (User) userManagementService.findById(User.class, userId);
//		}
//		Integer subcourseId = subcourse.getOrganisationId();
//		Set<Integer> existingSubcourseRoles = userManagementService.getUserOrganisationRoles(subcourseId, login)
//			.stream().collect(Collectors.mapping(r -> r.getRole().getRoleId(), Collectors.toSet()));
//		userAlreadyStaff = existingSubcourseRoles.contains(Role.ROLE_MONITOR);
//		if (!userAlreadyStaff) {
//		    // we modify existing role collection, as he can be also a learner
//		    existingSubcourseRoles.add(Role.ROLE_AUTHOR);
//		    existingSubcourseRoles.add(Role.ROLE_MONITOR);
//		    userManagementService.setRolesForUserOrganisation(userId, subcourseId, existingSubcourseRoles);
//
//		    for (Lesson lesson : lessonService.getLessonsByGroup(subcourseId)) {
//			lessonService.addStaffMember(lesson.getLessonId(), userId);
//		    }
//
//		    String message = "Teacher \"" + login + "\" added to subcourse " + subcourseId + " and its lessons";
//		    logger.info(message);
//		    logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, creatorId, null, null, null,
//			    "SPEnrolment: " + message);
//		}
//	    }
//	}
//
//	// user is a staff member, but he should not; remove all roles from course and lessons
//	for (User user : courseTeachers) {
//	    Map<Integer, Set<Integer>> existingCoursesRoles = existingRoles.get(user.getLogin());
//	    Set<Integer> existingCourseRoles = existingCoursesRoles == null ? null : existingCoursesRoles.get(courseId);
//	    if (existingCourseRoles != null) {
//		Integer userId = user.getUserId();
//
//		existingCourseRoles.remove(Role.ROLE_AUTHOR);
//		existingCourseRoles.remove(Role.ROLE_MONITOR);
//		userManagementService.setRolesForUserOrganisation(userId, courseId, existingCourseRoles);
//
//		for (Organisation subcourse : course.getChildOrganisations()) {
//		    boolean isLearner = userManagementService.hasRoleInOrganisation(user, Role.ROLE_MONITOR, subcourse);
//		    Integer subcourseId = subcourse.getOrganisationId();
//		    Set<Integer> subcourseRoles = new HashSet<>();
//		    if (isLearner) {
//			// there are no course admins in subcourses, so either user looses all roles or he stays just a learner
//			subcourseRoles.add(Role.ROLE_LEARNER);
//		    }
//		    userManagementService.setRolesForUserOrganisation(userId, subcourseId, subcourseRoles);
//
//		    for (Lesson lesson : lessonService.getLessonsByGroup(subcourseId)) {
//			lessonService.addStaffMember(lesson.getLessonId(), userId);
//		    }
//		}
//	    }
//
//	    String message = "Teacher \"" + user.getLogin() + "\" removed from course " + courseId
//		    + ", its subcourses and its lessons";
//	    logger.info(message);
//	    logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, creatorId, null, null, null,
//		    "SPEnrolment: " + message);
//	}
//
//	return allUsersInCourse;
//    }

    @Override
    public void init() throws ServletException {
	lessonService = (ILessonService) WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServletContext()).getBean("lessonService");

	userManagementService = (IUserManagementService) WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServletContext()).getBean("userManagementService");

	logEventService = (ILogEventService) WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServletContext()).getBean("logEventService");

	integrationService = (IIntegrationService) WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServletContext()).getBean("integrationService");
    }
}