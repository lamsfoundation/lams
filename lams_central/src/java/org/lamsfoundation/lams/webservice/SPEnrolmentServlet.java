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

    private static ILessonService lessonService = null;
    private static IUserManagementService userManagementService = null;
    private static ILogEventService logEventService = null;
    private static IIntegrationService integrationService = null;

    @SuppressWarnings("unchecked")
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
			.filter(row -> row.size() == 6).collect(Collectors.toList());

		// map of course code (ID) -> course name
		ConcurrentMap<String, String> courses = lines.parallelStream().unordered().collect(
			Collectors.toConcurrentMap(elem -> elem.get(0), elem -> elem.get(1), (elem1, elem2) -> elem1));

		// map of user login (email) -> first name
		ConcurrentMap<String, String> users = lines.parallelStream().unordered().collect(
			Collectors.toConcurrentMap(elem -> elem.get(5), elem -> elem.get(4), (elem1, elem2) -> elem1));

		// map of course code -> subcourse code -> user logins
		Map<String, Map<String, List<String>>> mappings = lines.stream()
			.collect(Collectors.groupingBy(elem -> elem.get(0), LinkedHashMap::new,
				Collectors.groupingBy(elem -> elem.get(2), LinkedHashMap::new,
					Collectors.mapping(elem -> elem.get(5), Collectors.toList()))));

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

		Set<User> allUsersInSubcourses = new HashSet<>();
		Set<User> allUsersParsed = new HashSet<>();
		// create users
		for (Entry<String, String> userEntry : users.entrySet()) {
		    // email servers as login
		    String login = userEntry.getKey();
		    User user = userManagementService.getUserByLogin(login);
		    if (user == null) {
			String salt = HashUtil.salt();
			String password = HashUtil.sha256(RandomPasswordGenerator.nextPassword(10), salt);

			ExtUserUseridMap userMap = integrationService.getImplicitExtUserUseridMap(extServer, login,
				password, salt, userEntry.getValue(), ".", login);
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

			    String message = "External user created for existing user with login \"" + login
				    + "\" and ID " + user.getUserId();
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

		// go through each course
		for (Entry<String, String> courseEntry : courses.entrySet()) {
		    Organisation course = null;
		    String code = courseEntry.getKey();
		    List<Organisation> organisations = userManagementService.findByProperty(Organisation.class, "code",
			    code);
		    // create course
		    if (organisations.isEmpty()) {
			String name = courseEntry.getValue();
			ExtCourseClassMap extOrgMap = integrationService.createExtCourseClassMap(extServer, creatorId,
				name, name, rootOrganisation.getOrganisationId().toString(), false);
			course = extOrgMap.getOrganisation();
			course.setCode(code);
			userManagementService.save(course);

			String message = "Course created with code \"" + code + "\" and name \"" + name + "\" and ID "
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

			    String message = "External course created for existing course with code \"" + code
				    + "\" and name \"" + name + "\" and ID " + course.getOrganisationId();
			    logger.info(message);
			    logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, creatorId, null, null, null,
				    "SPEnrolment: " + message);
			}
		    }

		    // go through each subcourse
		    for (Entry<String, List<String>> subcourseEntry : mappings.get(code).entrySet()) {
			Organisation subcourse = null;
			String subcourseCode = subcourseEntry.getKey();

			Map<String, Object> queryParams = new HashMap<>();
			queryParams.put("code", subcourseCode);
			queryParams.put("parentOrganisation.organisationId", course.getOrganisationId());
			organisations = userManagementService.findByProperties(Organisation.class, queryParams);

			// create subcourse
			if (organisations.isEmpty()) {

			    ExtCourseClassMap extSubOrgMap = integrationService.createExtCourseClassMap(extServer,
				    creatorId, subcourseCode, subcourseCode, course.getOrganisationId().toString(),
				    false);
			    subcourse = extSubOrgMap.getOrganisation();
			    subcourse.setCode(subcourseCode);
			    userManagementService.save(subcourse);

			    String message = "Subcourse created with code and name \"" + code + "\" and ID "
				    + subcourse.getOrganisationId();
			    logger.info(message);
			    logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, creatorId, null, null, null,
				    "SPEnrolment: " + message);
			} else {
			    subcourse = organisations.get(0);
			    String name = subcourse.getName();

			    ExtCourseClassMap extOrgMap = integrationService.getExtCourseClassMap(extServer.getSid(),
				    name);
			    if (extOrgMap == null) {
				extOrgMap = new ExtCourseClassMap();
				extOrgMap.setCourseid(name);
				extOrgMap.setExtServer(extServer);
				extOrgMap.setOrganisation(subcourse);
				userManagementService.save(extOrgMap);

				String message = "External subcourse created for existing subcourse with code \"" + code
					+ "\" and name \"" + name + "\" and ID " + course.getOrganisationId();
				logger.info(message);
				logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, creatorId, null, null, null,
					"SPEnrolment: " + message);
			    }
			}

			Integer subcourseId = subcourse.getOrganisationId();

			// get existing learners for given subcourse
			Collection<User> subcourseLearners = userManagementService
				.getUsersFromOrganisationByRole(subcourseId, Role.LEARNER, true);
			// add users to set containing all users in any subcourse which is processed
			allUsersInSubcourses.addAll(subcourseLearners);

			// go through each user
			for (String login : subcourseEntry.getValue()) {

			    // check if the user is already a learner in the subcourse
			    // if so, there is nothing to do
			    boolean userAlreadyLearner = false;
			    Integer userId = userIDs.get(login);
			    Iterator<User> subcourseLearnerIterator = subcourseLearners.iterator();
			    while (subcourseLearnerIterator.hasNext()) {
				if (userId.equals(subcourseLearnerIterator.next().getUserId())) {
				    subcourseLearnerIterator.remove();
				    userAlreadyLearner = true;
				    break;
				}
			    }
			    if (userAlreadyLearner) {
				continue;
			    }

			    // the user is not a learner yet, so assign him the role and add him to lessons
			    Set<Integer> existingSubcourseRoles = existingRoles.get(login).get(subcourseId);
			    if (existingSubcourseRoles == null) {
				existingSubcourseRoles = new HashSet<>();
			    }
			    existingSubcourseRoles.add(Role.ROLE_LEARNER);
			    userManagementService.setRolesForUserOrganisation(userId, subcourseId,
				    existingSubcourseRoles);

			    for (Lesson lesson : lessonService.getLessonsByGroup(subcourseId)) {
				lessonService.addLearner(lesson.getLessonId(), userId);
			    }

			    String message = "Learner \"" + login + "\" added to subcourse " + subcourseId
				    + " and its lessons";
			    logger.info(message);
			    logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, creatorId, null, null, null,
				    "SPEnrolment: " + message);
			}

			// user is a learner, but he should not; remove learner role from subcourse and lessons
			for (User user : subcourseLearners) {
			    Map<Integer, Set<Integer>> existingSubcoursesRoles = existingRoles.get(user.getLogin());
			    Set<Integer> existingSubcourseRoles = existingSubcoursesRoles == null ? null
				    : existingSubcoursesRoles.get(subcourseId);
			    if (existingSubcourseRoles != null) {
				existingSubcourseRoles.remove(Role.ROLE_LEARNER);
				userManagementService.setRolesForUserOrganisation(user.getUserId(), subcourseId,
					existingSubcourseRoles);

				for (Lesson lesson : lessonService.getLessonsByGroup(subcourseId)) {
				    lessonService.removeLearner(lesson.getLessonId(), user.getUserId());
				}

				String message = "Learner \"" + user.getLogin() + "\" removed from subcourse "
					+ subcourseId + " and its lessons";
				logger.info(message);
				logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, creatorId, null, null, null,
					"SPEnrolment: " + message);
			    }
			}
		    }
		}

		// users who are part of courses but are not in the file anymore are eligible for disabling
		allUsersInSubcourses.removeAll(allUsersParsed);
		for (User user : allUsersInSubcourses) {
		    // make a flat set of roles from all subcourses
		    Set<Integer> roles = userManagementService.getRolesForUser(user.getUserId()).values().stream()
			    .collect(HashSet::new, Set::addAll, Set::addAll);
		    // check if the user is staff in any course
		    roles.remove(Role.ROLE_LEARNER);
		    if (roles.isEmpty()) {
			// he is only a learner, so disable
			userManagementService.disableUser(user.getUserId());

			String message = "Learner \"" + user.getLogin() + "\" disabled";
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