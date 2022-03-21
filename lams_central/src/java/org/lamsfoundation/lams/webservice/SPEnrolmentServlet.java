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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import org.lamsfoundation.lams.usermanagement.UserOrganisation;
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
    // provisioning behaves differently when processing different flavours of files
    private static enum Mode {
	LEARNER("learner"), STAFF("staff"), MANAGER("manager");

	private String role;

	private Mode(String role) {
	    this.role = role;
	}

	private String getRole() {
	    return role;
	}

	private static List<String> getAllRoles() {
	    return Arrays.asList(LEARNER.getRole(), STAFF.getRole(), MANAGER.getRole());
	}
    };

    private static final long serialVersionUID = -5348322697437185394L;

    private static final Logger logger = Logger.getLogger(SPEnrolmentServlet.class);

    private static final String FILE_INPUT_DEFAULT_NAME = "LAMS-OUTPUT.csv";
    private static final String FILE_INPUT_PARAM = "file-input";
    private static final String DELIMITER = "\\|";
    private static final String INTEGRATED_SERVER_NAME = "saml";

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

		ExtServer extServer = integrationService.getExtServer(INTEGRATED_SERVER_NAME);
		if (extServer == null) {
		    throw new ServletException("Integrated server not found: " + INTEGRATED_SERVER_NAME);
		}

		// split each line into list of trimmed pieces
		Map<String, List<List<String>>> linesByMode = Files.readAllLines(fileInput).parallelStream().unordered()
			.map(line -> Arrays.stream(line.split(DELIMITER)).map(elem -> elem.trim())
				.collect(Collectors.toList()))
			// filter out malformed rows
			.filter(row -> {
			    // there must be exactly 7 rows
			    if (row.size() == 7) {
				return true;
			    }
			    // check if role is one of predefined strings
			    String role = row.get(6);
			    if (Mode.getAllRoles().contains(role)) {
				return true;
			    }
			    // throw an exception when a row is malformed rather than silently discard it
			    throw new RuntimeException("Malformed row: " + String.join(DELIMITER, row));
			}).collect(Collectors.groupingByConcurrent(row -> row.get(6)));

		if (linesByMode.isEmpty()) {
		    throw new ServletException("File is empty");
		}

		for (String role : Mode.getAllRoles()) {
		    List<List<String>> lines = linesByMode.get(role);
		    if (lines == null) {
			continue;
		    }
		    
		    logger.info("Processing \"" + role + "\" role");

		    // it is easier to detect whether we process managers or staff or learners just once
		    // than for each user - they do not come together anyway
		    final Mode mode = role.equals(Mode.STAFF.getRole()) ? Mode.STAFF
			    : (role.equals(Mode.MANAGER.getRole()) ? Mode.MANAGER : Mode.LEARNER);

		    // map of course code (ID) -> course name
		    ConcurrentMap<String, String> courses = lines.parallelStream().unordered().collect(Collectors
			    .toConcurrentMap(elem -> elem.get(0), elem -> elem.get(1), (elem1, elem2) -> elem1));

		    // map of user login -> email, first name
		    // for learner email is login, for staff it is a different ID in email format
		    ConcurrentMap<String, String[]> users = lines.parallelStream().unordered()
			    .collect(Collectors.toConcurrentMap(
				    elem -> mode == Mode.STAFF || mode == Mode.MANAGER ? elem.get(3) : elem.get(5),
				    elem -> new String[] { elem.get(5), elem.get(4) }, (elem1, elem2) -> elem1));

		    // map of user login -> user ID
		    Map<String, Integer> userIDs = new HashMap<>();

		    // find appadmin as he/she will be the creator of organisations
		    Organisation rootOrganisation = userManagementService.getRootOrganisation();
		    Integer creatorId = rootOrganisation.getCreatedBy().getUserId();

		    Integer extServerSid = extServer.getSid();
		    // load all users from DB which are present in the output file
		    // map of user login -> user
		    Map<String, User> allExistingUsers = userManagementService
			    .findByPropertyValues(User.class, "login", users.keySet()).parallelStream()
			    .collect(Collectors.toConcurrentMap(User::getLogin, u -> u));
		    // load all ext users from DB which are present in the output file
		    // map of user login -> extUser
		    Map<String, ExtUserUseridMap> allExistingExtUsers = userManagementService
			    .findByPropertyValues(ExtUserUseridMap.class, "extUsername", allExistingUsers.keySet())
			    .parallelStream().filter(e -> e.getExtServer().getSid().equals(extServerSid))
			    .collect(Collectors.toConcurrentMap(ExtUserUseridMap::getExtUsername, e -> e));
		    // CREATE USERS and ext users
		    Set<User> allUsersParsed = createUsers(extServer, creatorId, users, userIDs, allExistingUsers,
			    allExistingExtUsers);

		    // map of user login -> course ID -> role IDs
		    // for all users which are present in the output file
		    Map<String, Map<Integer, Set<Integer>>> allExistingRoles = userManagementService
			    .findByPropertyValues(UserOrganisation.class, "user.userId",
				    allExistingUsers.values().parallelStream()
					    .collect(Collectors.mapping(User::getUserId, Collectors.toSet())))
			    .stream()
			    .collect(Collectors.groupingBy(uo -> uo.getUser().getLogin(), Collectors.toMap(
				    userOrganisation -> userOrganisation.getOrganisation().getOrganisationId(),
				    userOrganisation -> userOrganisation.getUserOrganisationRoles().stream()
					    .map(userOrganisationRole -> userOrganisationRole.getRole().getRoleId())
					    .collect(Collectors.toSet()))));
		    // load all organisations from DB which are present in the output file, by code
		    // map of code -> organisation
		    Map<String, Organisation> allExistingOrganisations = userManagementService
			    .findByPropertyValues(Organisation.class, "code", courses.keySet()).parallelStream()
			    .collect(Collectors.toConcurrentMap(Organisation::getCode, o -> o));

		    // When setting group managers, just process courses, not subcourses and lessons
		    if (mode == Mode.MANAGER) {
			// map of course code -> user logins
			Map<String, List<String>> mappings = lines.stream()
				.collect(Collectors.groupingBy(elem -> elem.get(0), LinkedHashMap::new,
					Collectors.mapping(elem -> elem.get(3), Collectors.toList())));

			AtomicInteger mappingsProcessed = new AtomicInteger();
			logger.info("Processing courses and assigments");
			for (String courseCode : courses.keySet()) {
			    Organisation course = allExistingOrganisations.get(courseCode);

			    assignManagers(course, creatorId, mappings, allUsersParsed, userIDs, allExistingRoles,
				    allExistingUsers, mappingsProcessed);

			    logger.info("Processed " + mappingsProcessed.get() + " entries");
			}
			logger.info("Processing \"" + role + "\" role finished");

			// END OF GROUP MANAGER PROCESSING!
			return;
		    }

		    // START OF LEARNER / STAFF PROCESSING

		    // map of course code -> subcourse code -> user logins
		    Map<String, Map<String, List<String>>> mappings = lines.stream()
			    .collect(Collectors.groupingBy(elem -> elem.get(0), LinkedHashMap::new,
				    Collectors.groupingBy(elem -> elem.get(2), LinkedHashMap::new,
					    Collectors.mapping(elem -> mode == Mode.STAFF ? elem.get(3) : elem.get(5),
						    Collectors.toList()))));

		    // prepare codes of all suborganisations disregarding which organisation is their parent
		    Set<String> allExistingSubOrganisationCodes = mappings.values().stream()
			    .flatMap(e -> e.keySet().stream()).collect(Collectors.toSet());

		    // load all suborganisations from DB which are present in the output file
		    List<Organisation> allExistingSubcourseObjects = userManagementService
			    .findByPropertyValues(Organisation.class, "code", allExistingSubOrganisationCodes);

		    // map of course code -> subcourse code -> subcourse
		    Map<Integer, ConcurrentMap<String, Organisation>> allExistingSubcourses = allExistingSubcourseObjects
			    .parallelStream().filter(o -> o.getParentOrganisation() != null)
			    .collect(Collectors.groupingByConcurrent(o -> o.getParentOrganisation().getOrganisationId(),
				    Collectors.toConcurrentMap(Organisation::getCode, o -> o)));

		    // load all ext courses and subcourses from DB which are present in the output file
		    Map<Integer, ExtCourseClassMap> allExistingExtCourses = userManagementService
			    .findByPropertyValues(ExtCourseClassMap.class, "classid", Stream
				    // merge IDs of organisations and suborganisations
				    .concat(allExistingOrganisations.values().stream()
					    .map(Organisation::getOrganisationId),
					    allExistingSubcourseObjects.stream().map(Organisation::getOrganisationId))
				    .collect(Collectors.toSet()))
			    .parallelStream().filter(e -> e.getExtServer().getSid().equals(extServerSid))
			    .collect(Collectors.toConcurrentMap(e -> e.getOrganisation().getOrganisationId(), e -> e));

		    Set<User> allUsersinCourses = new HashSet<>();
		    // go through each course
		    AtomicInteger mappingsProcessed = new AtomicInteger();
		    logger.info("Processing courses and assigments");
		    for (Entry<String, String> courseEntry : courses.entrySet()) {
			String courseCode = courseEntry.getKey();
			// create or get existing course
			Organisation course = getCourse(courseCode, courseEntry.getValue(), extServer, creatorId,
				rootOrganisation, allExistingOrganisations, allExistingExtCourses);
			// collect learners from all subcourses of this course
			Set<User> allUsersInSubcourses = assignLearnersOrStaff(course, mappings, extServer, creatorId,
				allUsersParsed, userIDs, allExistingRoles, allExistingSubcourses, allExistingExtCourses,
				allExistingUsers, mode == Mode.STAFF, mappingsProcessed);
			allUsersinCourses.addAll(allUsersInSubcourses);

			logger.info("Processed " + mappingsProcessed.get() + " entries");
		    }

		    logger.info("Disabling users");
		    // users who are part of courses but are not in the file anymore are eligible for disabling
		    allUsersinCourses.removeAll(allUsersParsed);
		    for (User user : allUsersinCourses) {
			// make a flat set of roles from all subcourses
			Set<Integer> roles = userManagementService.getRolesForUser(user.getUserId()).values().stream()
				.collect(HashSet::new, Set::addAll, Set::addAll);
			if (mode == Mode.STAFF) {
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
		    logger.info("Processing \"" + role + "\" role finished");
		}
		logger.info("SP enrolments provisioning completed successfully");
	    } catch (Exception e) {
		logger.error("Error while provisioning SP enrolments", e);
	    } finally {
		try {
		    HibernateSessionManager.closeSession();
		} catch (Exception e) {
		    logger.error("Error while closing Hibernate session", e);
		}
	    }
	}).start();

	response.getWriter().print("Provisioning is running. Check logs.");
    }

    private Set<User> createUsers(ExtServer extServer, Integer creatorId, Map<String, String[]> users,
	    Map<String, Integer> userIDs, Map<String, User> allExistingUsers,
	    Map<String, ExtUserUseridMap> allExistingExtUsers)
	    throws UserInfoValidationException, UserInfoFetchException {
	Set<User> allUsersParsed = new HashSet<>();
	logger.info("Creating users");
	for (Entry<String, String[]> userEntry : users.entrySet()) {
	    // email servers as login
	    String login = userEntry.getKey();
	    User user = allExistingUsers.get(login);
	    if (user == null) {
		String salt = HashUtil.salt();
		String password = HashUtil.sha256(RandomPasswordGenerator.nextPassword(10), salt);

		String email = userEntry.getValue()[0];
		String firstName = userEntry.getValue()[1];
		ExtUserUseridMap userMap = integrationService.getImplicitExtUserUseridMap(extServer, login, password,
			salt, firstName, ".", email);
		user = userMap.getUser();

		allExistingUsers.put(login, user);
		allExistingExtUsers.put(login, userMap);

		String message = "User created with login \"" + login + "\" and ID " + user.getUserId();
		logger.info(message);
		logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, creatorId, null, null, null,
			"SPEnrolment: " + message);
	    } else {
		ExtUserUseridMap userMap = allExistingExtUsers.get(login);
		if (userMap == null) {
		    userMap = new ExtUserUseridMap();
		    userMap.setExtServer(extServer);
		    userMap.setUser(user);
		    userMap.setExtUsername(login);
		    userManagementService.save(userMap);

		    allExistingExtUsers.put(login, userMap);

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
	    userIDs.put(login, user.getUserId());
	    // add user to a collection of all users in the parsed file
	    allUsersParsed.add(user);
	}

	return allUsersParsed;
    }

    private Organisation getCourse(String courseCode, String courseName, ExtServer extServer, Integer creatorId,
	    Organisation rootOrganisation, Map<String, Organisation> allExistingOrganisations,
	    Map<Integer, ExtCourseClassMap> allExistingExtCourses) throws UserInfoValidationException {
	Organisation course = allExistingOrganisations.get(courseCode);
	// create course
	if (course == null) {
	    String name = courseName;
	    ExtCourseClassMap extOrgMap = integrationService.createExtCourseClassMap(extServer, creatorId, name, name,
		    rootOrganisation.getOrganisationId().toString(), false);
	    course = extOrgMap.getOrganisation();
	    course.setCode(courseCode);
	    userManagementService.save(course);

	    allExistingOrganisations.put(courseCode, course);
	    allExistingExtCourses.put(extOrgMap.getOrganisation().getOrganisationId(), extOrgMap);

	    String message = "Course created with code \"" + courseCode + "\" and name \"" + name + "\" and ID "
		    + course.getOrganisationId();
	    logger.info(message);
	    logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, creatorId, null, null, null,
		    "SPEnrolment: " + message);
	} else {
	    String name = course.getName();

	    ExtCourseClassMap extOrgMap = allExistingExtCourses.get(course.getOrganisationId());
	    if (extOrgMap == null) {
		extOrgMap = new ExtCourseClassMap();
		extOrgMap.setCourseid(name);
		extOrgMap.setExtServer(extServer);
		extOrgMap.setOrganisation(course);
		userManagementService.save(extOrgMap);

		allExistingExtCourses.put(course.getOrganisationId(), extOrgMap);

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
    private Set<User> assignLearnersOrStaff(Organisation course, Map<String, Map<String, List<String>>> mappings,
	    ExtServer extServer, Integer creatorId, Set<User> allUsersParsed, Map<String, Integer> userIDs,
	    Map<String, Map<Integer, Set<Integer>>> allExistingRoles,
	    Map<Integer, ConcurrentMap<String, Organisation>> allExistingSubcourses,
	    Map<Integer, ExtCourseClassMap> allExistingExtCourses, Map<String, User> allExistingUsers,
	    boolean isStaffMode, AtomicInteger mappingsProcessed) throws UserInfoValidationException {
	Set<User> allUsersInSubcourses = new HashSet<>();
	String courseCode = course.getCode();
	Integer courseId = course.getOrganisationId();
	// go through each subcourse
	for (Entry<String, List<String>> subcourseEntry : mappings.get(courseCode).entrySet()) {
	    String subcourseCode = subcourseEntry.getKey();
	    ConcurrentMap<String, Organisation> existingSubcourses = allExistingSubcourses.get(courseId);
	    Organisation subcourse = existingSubcourses == null ? null : existingSubcourses.get(subcourseCode);

	    // create subcourse
	    if (subcourse == null) {
		ExtCourseClassMap extSubOrgMap = integrationService.createExtCourseClassMap(extServer, creatorId,
			subcourseCode, subcourseCode, course.getOrganisationId().toString(), false);
		subcourse = extSubOrgMap.getOrganisation();
		subcourse.setCode(subcourseCode);
		userManagementService.save(subcourse);

		if (existingSubcourses == null) {
		    existingSubcourses = new ConcurrentHashMap<>();
		    allExistingSubcourses.put(courseId, existingSubcourses);
		}
		existingSubcourses.put(subcourse.getCode(), subcourse);
		allExistingExtCourses.put(extSubOrgMap.getOrganisation().getOrganisationId(), extSubOrgMap);

		String message = "Subcourse created with code and name \"" + courseCode + "\" and ID "
			+ subcourse.getOrganisationId();
		logger.info(message);
		logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, creatorId, null, null, null,
			"SPEnrolment: " + message);
	    } else {
		String name = subcourse.getName();

		ExtCourseClassMap extOrgMap = allExistingExtCourses.get(subcourse.getOrganisationId());
		if (extOrgMap == null) {
		    extOrgMap = new ExtCourseClassMap();
		    extOrgMap.setCourseid(name);
		    extOrgMap.setExtServer(extServer);
		    extOrgMap.setOrganisation(subcourse);
		    userManagementService.save(extOrgMap);

		    String message = "External subcourse created for existing subcourse with code \"" + subcourseCode
			    + "\" and name \"" + name + "\" and ID " + subcourse.getOrganisationId();
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
		mappingsProcessed.incrementAndGet();

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
		Map<Integer, Set<Integer>> existingSubcoursesRoles = allExistingRoles.get(login);
		Set<Integer> existingSubcourseRoles = existingSubcoursesRoles == null ? null
			: allExistingRoles.get(login).get(subcourseId);
		if (existingSubcourseRoles == null) {
		    existingSubcourseRoles = new HashSet<>();
		}
		if (isStaffMode) {
		    existingSubcourseRoles.add(Role.ROLE_AUTHOR);
		    existingSubcourseRoles.add(Role.ROLE_MONITOR);
		} else {
		    existingSubcourseRoles.add(Role.ROLE_LEARNER);
		}
		User user = allExistingUsers.get(login);
		userManagementService.setRolesForUserOrganisation(user, subcourse,
			existingSubcourseRoles.stream().map(String::valueOf).collect(Collectors.toList()), false);

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
		Map<Integer, Set<Integer>> existingSubcoursesRoles = allExistingRoles.get(user.getLogin());
		Set<Integer> existingSubcourseRoles = existingSubcoursesRoles == null ? null
			: existingSubcoursesRoles.get(subcourseId);
		if (existingSubcourseRoles != null) {
		    if (isStaffMode) {
			existingSubcourseRoles.remove(Role.ROLE_AUTHOR);
			existingSubcourseRoles.remove(Role.ROLE_MONITOR);
		    } else {
			existingSubcourseRoles.remove(Role.ROLE_LEARNER);
		    }
		    userManagementService.setRolesForUserOrganisation(user, subcourse,
			    existingSubcourseRoles.stream().map(String::valueOf).collect(Collectors.toList()), false);

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

    @SuppressWarnings("unchecked")
    private void assignManagers(Organisation course, Integer creatorId, Map<String, List<String>> mappings,
	    Set<User> allUsersParsed, Map<String, Integer> userIDs,
	    Map<String, Map<Integer, Set<Integer>>> allExistingRoles, Map<String, User> allExistingUsers,
	    AtomicInteger mappingsProcessed) throws UserInfoValidationException {
	String courseCode = course.getCode();
	Integer courseId = course.getOrganisationId();

	// get existing managers for given course
	Collection<User> courseUsers = userManagementService.getUsersFromOrganisationByRole(courseId,
		Role.GROUP_MANAGER, true);

	// go through each user
	for (String login : mappings.get(courseCode)) {
	    mappingsProcessed.incrementAndGet();

	    // check if the user is already a manager is the course
	    // if so, there is nothing to do
	    boolean userAlreadyAssigned = false;
	    Integer userId = userIDs.get(login);
	    Iterator<User> courseUserIterator = courseUsers.iterator();
	    while (courseUserIterator.hasNext()) {
		if (userId.equals(courseUserIterator.next().getUserId())) {
		    courseUserIterator.remove();
		    userAlreadyAssigned = true;
		    break;
		}
	    }
	    if (userAlreadyAssigned) {
		continue;
	    }

	    // the user is not a manager yet, so assign him the role
	    Map<Integer, Set<Integer>> existingCoursesRoles = allExistingRoles.get(login);
	    Set<Integer> existingCourseRoles = existingCoursesRoles == null ? null
		    : allExistingRoles.get(login).get(courseId);
	    if (existingCourseRoles == null) {
		existingCourseRoles = new HashSet<>();
	    }
	    existingCourseRoles.add(Role.ROLE_GROUP_MANAGER);

	    User user = allExistingUsers.get(login);
	    userManagementService.setRolesForUserOrganisation(user, course,
		    existingCourseRoles.stream().map(String::valueOf).collect(Collectors.toList()), false);

	    String message = "Group manager \"" + login + "\" added to course";
	    logger.info(message);
	    logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, creatorId, null, null, null,
		    "SPEnrolment: " + message);
	}

	// user is a group manager, but he should not; remove him role from course
	for (User user : courseUsers) {
	    Map<Integer, Set<Integer>> existingCoursesRoles = allExistingRoles.get(user.getLogin());
	    Set<Integer> existingSubcourseRoles = existingCoursesRoles == null ? null
		    : existingCoursesRoles.get(courseId);
	    if (existingSubcourseRoles != null) {
		existingSubcourseRoles.remove(Role.ROLE_GROUP_MANAGER);
		userManagementService.setRolesForUserOrganisation(user.getUserId(), courseId, existingSubcourseRoles);

		String message = "Group manager \"" + user.getLogin() + "\" removed from course " + courseId;
		logger.info(message);
		logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, creatorId, null, null, null,
			"SPEnrolment: " + message);
	    }
	}
    }

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