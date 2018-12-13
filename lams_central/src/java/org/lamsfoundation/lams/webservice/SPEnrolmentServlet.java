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
import java.util.Date;
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
import org.lamsfoundation.lams.integration.security.RandomPasswordGenerator;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.logevent.LogEvent;
import org.lamsfoundation.lams.logevent.service.ILogEventService;
import org.lamsfoundation.lams.themes.Theme;
import org.lamsfoundation.lams.usermanagement.AuthenticationMethod;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationState;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.SupportedLocale;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.HashUtil;
import org.lamsfoundation.lams.util.LanguageUtil;
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

    private static ILessonService lessonService = null;
    private static IUserManagementService userManagementService = null;
    private static ILogEventService logEventService;

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
		// start interacting with DB
		HibernateSessionManager.openSession();

		// split each line into list of trimmed pieces
		List<List<String>> lines = Files.readAllLines(fileInput).parallelStream().unordered()
			.collect(Collectors.mapping(line -> Arrays.stream(line.split(DELIMITER))
				.map(elem -> elem.trim()).collect(Collectors.toList()), Collectors.toList()));

		// map of course code (ID) -> course name
		ConcurrentMap<String, String> courses = lines.parallelStream().unordered().collect(
			Collectors.toConcurrentMap(elem -> elem.get(0), elem -> elem.get(1), (elem1, elem2) -> elem1));

		// map of user login -> user first name and email
		ConcurrentMap<String, String[]> users = lines.parallelStream().unordered()
			.collect(Collectors.toConcurrentMap(elem -> elem.get(3),
				elem -> new String[] { elem.get(4), elem.get(5) }, (elem1, elem2) -> elem1));

		// map of course code -> subcourse code -> user logins
		Map<String, Map<String, List<String>>> mappings = lines.stream()
			.collect(Collectors.groupingBy(elem -> elem.get(0), LinkedHashMap::new,
				Collectors.groupingBy(elem -> elem.get(2), LinkedHashMap::new,
					Collectors.mapping(elem -> elem.get(3), Collectors.toList()))));

		// map of user login -> course ID -> role IDs
		Map<String, Map<Integer, Set<Integer>>> existingRoles = new HashMap<>();
		// map of user login -> user ID
		Map<String, Integer> userIDs = new HashMap<>();

		// prepare missing, default inromation
		String defaultCountry = LanguageUtil.getDefaultCountry();
		SupportedLocale defaultLocale = LanguageUtil.getDefaultLocale();
		Theme defaultTheme = userManagementService.getDefaultTheme();
		AuthenticationMethod defaultAuthenticationMethod = (AuthenticationMethod) userManagementService
			.findById(AuthenticationMethod.class, AuthenticationMethod.DB);
		Date createDate = new Date();

		// find sysadmin as he/she will be the creator of organisations
		Organisation rootOrganisation = userManagementService.getRootOrganisation();
		Integer creatorId = rootOrganisation.getCreatedBy().getUserId();

		// create users
		for (Entry<String, String[]> userEntry : users.entrySet()) {
		    String login = userEntry.getKey();
		    User user = userManagementService.getUserByLogin(login);
		    if (user == null) {
			String salt = HashUtil.salt();
			String password = HashUtil.sha256(RandomPasswordGenerator.nextPassword(10), salt);

			user = new User();
			user.setAuthenticationMethod(defaultAuthenticationMethod);
			user.setLogin(login);
			user.setSalt(salt);
			user.setPassword(password);
			user.setFirstName(userEntry.getValue()[0]);
			user.setLastName(".");
			user.setEmail(userEntry.getValue()[1]);
			user.setCountry(defaultCountry);
			user.setLocale(defaultLocale);
			user.setCreateDate(createDate);
			user.setDisabledFlag(false);
			user.setTheme(defaultTheme);
			userManagementService.saveUser(user);

			String message = "User created with login \"" + login + "\" and ID " + user.getUserId();
			logger.info(message);
			logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, creatorId, null, null, null,
				"SPEnrolment: " + message);
		    }

		    // fill data for later usage
		    existingRoles.put(login, userManagementService.getRolesForUser(user.getUserId()));
		    userIDs.put(login, user.getUserId());
		}

		// organisation defaults
		OrganisationState defaultOrganisationState = (OrganisationState) userManagementService
			.findById(OrganisationState.class, OrganisationState.ACTIVE);
		OrganisationType courseOrganisationType = (OrganisationType) userManagementService
			.findById(OrganisationType.class, OrganisationType.COURSE_TYPE);
		OrganisationType subcourseOrganisationType = (OrganisationType) userManagementService
			.findById(OrganisationType.class, OrganisationType.CLASS_TYPE);

		// go through each course
		for (Entry<String, String> courseEntry : courses.entrySet()) {
		    Organisation course = null;
		    String code = courseEntry.getKey();
		    List<Organisation> organisations = userManagementService.findByProperty(Organisation.class, "code",
			    code);
		    // create course
		    if (organisations.isEmpty()) {
			String name = courseEntry.getValue();
			course = new Organisation();
			course.setCode(code);
			course.setName(name);
			course.setOrganisationState(defaultOrganisationState);
			course.setOrganisationType(courseOrganisationType);
			userManagementService.saveOrganisation(course, creatorId);

			String message = "Course created with code \"" + code + "\" and name \"" + name + "\" and ID "
				+ course.getOrganisationId();
			logger.info(message);
			logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, creatorId, null, null, null,
				"SPEnrolment: " + message);
		    } else {
			course = organisations.get(0);
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
			    subcourse = new Organisation();
			    subcourse.setCode(subcourseCode);
			    subcourse.setName(subcourseCode);
			    subcourse.setOrganisationState(defaultOrganisationState);
			    subcourse.setOrganisationType(subcourseOrganisationType);
			    subcourse.setParentOrganisation(course);
			    userManagementService.saveOrganisation(subcourse, creatorId);

			    String message = "Subcourse created with code and name \"" + code + "\" and ID "
				    + subcourse.getOrganisationId();
			    logger.info(message);
			    logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, creatorId, null, null, null,
				    "SPEnrolment: " + message);
			} else {
			    subcourse = organisations.get(0);
			}

			Integer subcourseId = subcourse.getOrganisationId();

			// get existing learners for given subcourse
			Collection<User> subcourseLearners = userManagementService
				.getUsersFromOrganisationByRole(subcourseId, Role.LEARNER, true);

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
			    Set<Integer> existingSubcourseRoles = existingRoles.get(user.getLogin()).get(subcourseId);
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
    }
}