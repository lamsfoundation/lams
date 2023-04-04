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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Spliterator;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
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
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.UserOrganisation;
import org.lamsfoundation.lams.usermanagement.UserOrganisationRole;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.HashUtil;
import org.lamsfoundation.lams.util.WebUtil;
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
	    return Arrays.asList(MANAGER.getRole(), LEARNER.getRole(), STAFF.getRole());
	}
    };

    private static final long serialVersionUID = -5348322697437185394L;

    private static final Logger logger = Logger.getLogger(SPEnrolmentServlet.class);

    private static final String FILE_INPUT_DEFAULT_NAME = "LAMS-OUTPUT.csv";
    private static final String FILE_INPUT_PARAM = "file-input";
    private static final int THREADS_DEFAULT_VALUE = 4;
    private static final String THREADS_PARAM = "threads";

    private static final String DELIMITER = "\\|";
    private static final String INTEGRATED_SERVER_NAME = "saml";

    private static ILessonService lessonService = null;
    private static IUserManagementService userManagementService = null;
    private static ILogEventService logEventService = null;
    private static IIntegrationService integrationService = null;

    private int threadCount = THREADS_DEFAULT_VALUE;
    private ExecutorService executor = null;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
	Integer threadCount = WebUtil.readIntParam(request, THREADS_PARAM, true);
	if (threadCount != null && threadCount > 0) {
	    this.threadCount = threadCount;
	}
	executor = Executors.newFixedThreadPool(this.threadCount);

	// check if path to input file is provided as GET parameter
	// if not, use default one of <tempDir>/LAMS-OUTPUT.csv
	String fileInputParam = request.getParameter(FILE_INPUT_PARAM);
	Path fileInput = StringUtils.isBlank(fileInputParam)
		? Paths.get(Configuration.get(ConfigurationKeys.LAMS_TEMP_DIR), FILE_INPUT_DEFAULT_NAME)
		: Paths.get(fileInputParam);
	if (!Files.isReadable(fileInput)) {
	    throw new IOException("File not readable: " + fileInput.toAbsolutePath().toString());
	}

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
		List<List<String>> allLines = Files.readAllLines(fileInput).parallelStream().unordered()
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
			}).collect(Collectors.toList());

		if (allLines.isEmpty()) {
		    throw new IOException("File is empty");
		}

		// map of user login -> user ID
		Map<String, Integer> userIDs = new ConcurrentHashMap<>();

		// find sysadmin as he/she will be the creator of organisations
		Organisation rootOrganisation = userManagementService.getRootOrganisation();
		Integer creatorId = rootOrganisation.getCreatedBy().getUserId();

		// map of user login -> email, first name, user ID used as last name (only for learners)
		// for learner email is login, for staff it is a different ID in email format
		ConcurrentMap<String, String[]> allParsedUserMapping = allLines.parallelStream().unordered()
			.collect(Collectors.toConcurrentMap(
				elem -> (elem.get(6).equals(Mode.STAFF.getRole())
					|| elem.get(6).equals(Mode.MANAGER.getRole()) ? elem.get(3) : elem.get(5))
						.strip().toLowerCase(),
				elem -> new String[] { elem.get(5).strip().toLowerCase(), elem.get(4),
					elem.get(6).equals(Mode.STAFF.getRole())
						|| elem.get(6).equals(Mode.MANAGER.getRole()) ? "." : elem.get(3) },
				(elem1, elem2) -> elem1));
		logger.info("Found " + allParsedUserMapping.size() + " users in the file");

		Integer extServerSid = extServer.getSid();
		// load all users from DB which are present in the output file
		// map of user login -> user
		ConcurrentMap<String, User> allExistingParsedUsers = userManagementService
			.findByPropertyValues(User.class, "login", allParsedUserMapping.keySet()).parallelStream()
			.collect(Collectors.toConcurrentMap(u -> u.getLogin().toLowerCase(), u -> u));

		logger.info(allExistingParsedUsers.size() + " users already exist");

		// load all ext users from DB which are present in the output file
		// map of user login -> extUser
		ConcurrentMap<String, ExtUserUseridMap> allExistingParsedExtUsers = userManagementService
			.findByPropertyValues(ExtUserUseridMap.class, "extUsername", allExistingParsedUsers.keySet())
			.parallelStream().filter(e -> e.getExtServer().getSid().equals(extServerSid))
			.collect(Collectors.toConcurrentMap(e -> e.getExtUsername().toLowerCase(), e -> e));

		// create users and ext users
		Set<User> allParsedUsers = createUsers(extServer, creatorId, allParsedUserMapping, userIDs,
			allExistingParsedUsers, allExistingParsedExtUsers);

		// map of course code (ID) -> course name
		// for all organisations present in the output file
		ConcurrentMap<String, String> allParsedCourseMapping = allLines.parallelStream().unordered()
			.collect(Collectors.toConcurrentMap(elem -> elem.get(0).strip(), elem -> elem.get(1).strip(),
				(elem1, elem2) -> elem1));

		logger.info("Found " + allParsedCourseMapping.size() + " courses in the file");

		// load all organisations from DB which are present in the output file, by code
		// map of code -> organisation
		ConcurrentMap<String, Organisation> allExistingParsedCourses = userManagementService
			.findByPropertyValues(Organisation.class, "code", allParsedCourseMapping.keySet())
			.parallelStream().collect(Collectors.toConcurrentMap(Organisation::getCode, o -> o));

		logger.info(allExistingParsedCourses.size() + " courses already exist");

		// prepare codes of all suborganisations disregarding which organisation is their parent
		Set<String> allParsedSubcourseMapping = allLines.stream()
			.collect(Collectors.mapping(elem -> elem.get(2), Collectors.toSet()));

		logger.info("Found " + allParsedSubcourseMapping.size() + " subcourses in the file");

		// load all suborganisations from DB which are present in the output file
		List<Organisation> allExistingParsedSubcourses = userManagementService
			.findByPropertyValues(Organisation.class, "code", allParsedSubcourseMapping);

		logger.info(allParsedSubcourseMapping.size() + " subcourses already exist");

		// map of course ID -> subcourse code -> subcourse
		ConcurrentMap<Integer, ConcurrentMap<String, Organisation>> allExistingParsedCoursesAndSubcourses = allExistingParsedSubcourses
			.parallelStream().filter(o -> o.getParentOrganisation() != null)
			.collect(Collectors.groupingByConcurrent(o -> o.getParentOrganisation().getOrganisationId(),
				Collectors.toConcurrentMap(Organisation::getCode, o -> o)));

		// load all ext courses and subcourses from DB which are present in the output file
		ConcurrentMap<Integer, ExtCourseClassMap> allExistingParsedExtCourses = userManagementService
			.findByPropertyValues(ExtCourseClassMap.class, "classid", Stream
				// merge IDs of organisations and suborganisations
				.concat(allExistingParsedCourses.values().stream(),
					allExistingParsedSubcourses.stream())
				.map(Organisation::getOrganisationId).collect(Collectors.toSet()))
			.parallelStream().filter(e -> e.getExtServer().getSid().equals(extServerSid))
			.collect(Collectors.toConcurrentMap(e -> e.getOrganisation().getOrganisationId(), e -> e));

		Set<User> allExistingUsersFromParsedCourses = Stream
			.concat(allExistingParsedCourses.values().stream(), allExistingParsedSubcourses.stream())
			.flatMap(o -> o.getUserOrganisations().stream())
			.collect(Collectors.mapping(UserOrganisation::getUser, Collectors.toSet()));

		// map lines into corresponding roles
		ConcurrentMap<String, List<List<String>>> linesByMode = allLines.stream()
			.collect(Collectors.groupingByConcurrent(row -> row.get(6)));

		for (String role : Mode.getAllRoles()) {
		    logger.info("Processing \"" + role + "\" role");

		    List<List<String>> lines = linesByMode.get(role);
		    // it is easier to detect whether we process managers or staff or learners just once
		    // than for each user - they do not come together anyway
		    final Mode mode = role.equals(Mode.STAFF.getRole()) ? Mode.STAFF
			    : (role.equals(Mode.MANAGER.getRole()) ? Mode.MANAGER : Mode.LEARNER);

		    if (lines == null) {
			// always process manager role, even if there are no managers
			if (mode == Mode.MANAGER) {
			    lines = List.of();
			} else {
			    continue;
			}
		    }

		    // map of user login -> course ID -> role IDs
		    // for all organisations which are present in the output file
		    Map<String, Map<Integer, Set<Integer>>> allExistingRoles = Stream
			    .concat(allExistingParsedCourses.values().stream(), allExistingParsedSubcourses.stream())
			    .flatMap(o -> o.getUserOrganisations().stream()).collect(
				    Collectors.groupingBy(uo -> uo.getUser().getLogin().toLowerCase(),
					    Collectors.toMap(
						    userOrganisation -> userOrganisation.getOrganisation()
							    .getOrganisationId(),
						    userOrganisation -> userOrganisation.getUserOrganisationRoles()
							    .stream().map(userOrganisationRole -> userOrganisationRole
								    .getRole().getRoleId())
							    .collect(Collectors.toSet()))));

		    // When setting group managers, just process courses, not subcourses and lessons
		    if (mode == Mode.MANAGER) {
			// map of course code -> user logins
			Map<String, List<String>> mappings = lines.stream()
				.collect(Collectors.groupingByConcurrent(elem -> elem.get(0), ConcurrentHashMap::new,
					Collectors.mapping(elem -> elem.get(3).toLowerCase(), Collectors.toList())));

			AtomicInteger mappingsProcessed = new AtomicInteger();
			logger.info("Processing manager courses and assigments");

			Collection<Spliterator<Entry<String, String>>> spliterators = splitCollection(
				allParsedCourseMapping.entrySet(), null);
			List<Future<?>> futures = new ArrayList<>(spliterators.size());

			for (Spliterator<Entry<String, String>> spliterator : spliterators) {
			    logger.info("Managers processing split count: " + spliterator.estimateSize());

			    futures.add(executor.submit(new Callable<Integer>() {
				@Override
				public Integer call() throws Exception {
				    try {
					HibernateSessionManager.openSession();
					boolean elementsRemaining = true;
					do {
					    elementsRemaining = spliterator.tryAdvance(courseEntry -> {
						logger.info("Processing course: " + courseEntry.getValue());

						try {
						    Organisation course = getCourse(courseEntry.getKey(),
							    courseEntry.getValue(), extServer, creatorId,
							    rootOrganisation, allExistingParsedCourses,
							    allExistingParsedExtCourses);
						    assignManagers(course, creatorId, mappings, allParsedUsers, userIDs,
							    allExistingRoles, allExistingParsedUsers,
							    mappingsProcessed);

						    logger.info("Processed " + mappingsProcessed.get() + " entries");
						} catch (UserInfoValidationException | InterruptedException
							| ExecutionException e) {
						    logger.error("Error while processing managers", e);
						}
					    });
					} while (elementsRemaining);
					logger.info("Processed " + mappingsProcessed.get() + " entries and finished");
				    } finally {
					HibernateSessionManager.closeSession();
				    }
				    return null;
				}
			    }));
			}
			for (Future<?> future : futures) {
			    future.get();
			}
			logger.info("Processing manager role finished");

			// END OF GROUP MANAGER PROCESSING
			continue;
		    }

		    // START OF LEARNER / STAFF PROCESSING

		    // map of course code -> subcourse code -> user logins
		    ConcurrentMap<String, ConcurrentMap<String, List<String>>> mappings = lines.stream()
			    .collect(Collectors.groupingByConcurrent(elem -> elem.get(0), ConcurrentHashMap::new,
				    Collectors.groupingByConcurrent(elem -> elem.get(2), ConcurrentHashMap::new,
					    Collectors.mapping(elem -> (mode == Mode.STAFF ? elem.get(3) : elem.get(5))
						    .toLowerCase(), Collectors.toList()))));

		    // go through each course
		    AtomicInteger mappingsProcessed = new AtomicInteger();
		    logger.info("Processing courses and assigments");

		    // Compare 2 courses on how many users to process each has and whether is already exists
		    // It helps with balancing thread load.
		    Comparator<Entry<String, String>> parsedCourseSizeComparator = (c1, c2) -> {
			boolean courseExists1 = allExistingParsedCourses.containsKey(c1.getKey());
			boolean courseExists2 = allExistingParsedCourses.containsKey(c2.getKey());
			if (courseExists1) {
			    if (!courseExists2) {
				return -1;
			    }
			} else if (courseExists2) {
			    return 1;
			}

			Map<String, List<String>> subcourseMappings1 = mappings.get(c1.getKey());
			Map<String, List<String>> subcourseMappings2 = mappings.get(c2.getKey());
			if (subcourseMappings1 == null) {
			    if (subcourseMappings2 == null) {
				return c1.equals(c2) ? 0 : 1;
			    } else {
				return -1;
			    }
			} else if (subcourseMappings2 == null) {
			    return 1;
			}

			int courseSize1 = subcourseMappings1.values().stream()
				.collect(Collectors.summingInt(List::size));
			int courseSize2 = subcourseMappings2.values().stream()
				.collect(Collectors.summingInt(List::size));
			int courseSizeDifference = courseSize1 - courseSize2;
			// never return 0 as course will considered a duplicate
			return courseSizeDifference == 0 && !c1.equals(c2) ? 1 : courseSizeDifference;
		    };

		    Collection<Spliterator<Entry<String, String>>> spliterators = splitCollection(
			    allParsedCourseMapping.entrySet(), parsedCourseSizeComparator);
		    List<Future<?>> futures = new ArrayList<>(spliterators.size());

		    for (Spliterator<Entry<String, String>> spliterator : spliterators) {
			logger.info(mode + " processing split count: " + spliterator.estimateSize());

			futures.add(executor.submit(new Callable<Integer>() {
			    @Override
			    public Integer call() throws Exception {
				try {
				    HibernateSessionManager.openSession();
				    boolean elementsRemaining = true;
				    do {
					elementsRemaining = spliterator.tryAdvance(courseEntry -> {
					    logger.info("Processing course: " + courseEntry.getValue());
					    String courseCode = courseEntry.getKey();
					    try {
						// create or get existing course
						Organisation course = getCourse(courseCode, courseEntry.getValue(),
							extServer, creatorId, rootOrganisation,
							allExistingParsedCourses, allExistingParsedExtCourses);
						// assign learners and staff to subcourses of this course
						assignLearnersOrStaff(course, mappings, extServer, creatorId,
							allParsedUsers, userIDs, allExistingRoles,
							allExistingParsedCoursesAndSubcourses,
							allExistingParsedExtCourses, allExistingParsedUsers,
							mode == Mode.STAFF, mappingsProcessed);

						logger.info("Processed " + mappingsProcessed.get() + " entries");
					    } catch (UserInfoValidationException | InterruptedException
						    | ExecutionException e) {
						logger.error("Error while processing managers", e);
					    }
					});
				    } while (elementsRemaining);
				    logger.info("Processed " + mappingsProcessed.get() + " entries and finished");
				} finally {
				    HibernateSessionManager.closeSession();
				}
				return null;
			    }
			}));
		    }
		    for (Future<?> future : futures) {
			future.get();
		    }

		    logger.info("Processing " + role + " role finished");
		}

		logger.info("Disabling users");
		// users who are part of courses but are not in the file anymore are eligible for disabling
		allExistingUsersFromParsedCourses.removeAll(allParsedUsers);
		for (User user : allExistingUsersFromParsedCourses) {
		    boolean hasAnyRoles = userManagementService.hasUserAnyRoles(user.getUserId());
		    if (!hasAnyRoles) {
			// he is only a learner or this is staff mode, so disable
			userManagementService.disableUser(user.getUserId());

			String message = "User \"" + user.getLogin().toLowerCase() + "\" disabled";
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
	    Map<String, Integer> userIDs, Map<String, User> allExistingUsers,
	    Map<String, ExtUserUseridMap> allExistingExtUsers)
	    throws UserInfoValidationException, UserInfoFetchException, InterruptedException, ExecutionException {
	Set<User> allUsersParsed = ConcurrentHashMap.newKeySet();
	logger.info("Creating users");

	Collection<Spliterator<Entry<String, String[]>>> spliterators = splitCollection(users.entrySet(), null);
	List<Future<?>> futures = new ArrayList<>(spliterators.size());

	for (Spliterator<Entry<String, String[]>> spliterator : spliterators) {
	    logger.info("Users processing split count: " + spliterator.estimateSize());

	    futures.add(executor.submit(new Callable<Integer>() {
		@Override
		public Integer call() throws Exception {
		    try {
			HibernateSessionManager.openSession();
			boolean elementsRemaining = true;
			do {
			    elementsRemaining = spliterator.tryAdvance(userEntry -> {

				// email servers as login
				String login = userEntry.getKey().toLowerCase();
				User user = allExistingUsers.get(login);
				if (user == null) {
				    String salt = HashUtil.salt();
				    String password = HashUtil.sha256(RandomPasswordGenerator.nextPassword(10), salt);

				    String email = userEntry.getValue()[0];
				    String firstName = userEntry.getValue()[1];
				    String lastName = userEntry.getValue()[2];
				    ExtUserUseridMap userMap = null;
				    try {
					userMap = integrationService.getImplicitExtUserUseridMap(extServer, login,
						password, salt, firstName, lastName, email);
				    } catch (UserInfoValidationException e) {
					logger.error("Error while getting/creating external user: " + login, e);
					return;
				    }
				    user = userMap.getUser();

				    allExistingUsers.put(login, user);
				    allExistingExtUsers.put(login, userMap);

				    String message = "User created with login \"" + login + "\" and ID "
					    + user.getUserId();
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

					String message = "External user created for existing user with login \"" + login
						+ "\" and ID " + user.getUserId();
					logger.info(message);
					logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, creatorId, null, null,
						null, "SPEnrolment: " + message);
				    }
				    if (user.getDisabledFlag()) {
					user = userManagementService.getUserById(user.getUserId());
					// re-enable user who was disabled before
					user.setDisabledFlag(false);
					userManagementService.save(user);
				    }
				}

				// fill data for later usage
				userIDs.put(login, user.getUserId());
				// add user to a collection of all users in the parsed file
				allUsersParsed.add(user);
			    });
			} while (elementsRemaining);
		    } finally {
			HibernateSessionManager.closeSession();
		    }
		    return null;
		}
	    }));
	}
	for (Future<?> future : futures) {
	    future.get();
	}
	return allUsersParsed;
    }

    private Organisation getCourse(String courseCode, String courseName, ExtServer extServer, Integer creatorId,
	    Organisation rootOrganisation, Map<String, Organisation> allExistingParsedCourses,
	    Map<Integer, ExtCourseClassMap> allExistingParsedExtCourses) throws UserInfoValidationException {
	Organisation course = allExistingParsedCourses.get(courseCode);
	// create course
	if (course == null) {
	    String name = courseName;
	    ExtCourseClassMap extOrgMap = integrationService.createExtCourseClassMap(extServer, creatorId, name, name,
		    rootOrganisation.getOrganisationId().toString(), false);
	    course = extOrgMap.getOrganisation();
	    course.setCode(courseCode);
	    userManagementService.save(course);

	    allExistingParsedCourses.put(courseCode, course);
	    allExistingParsedExtCourses.put(extOrgMap.getOrganisation().getOrganisationId(), extOrgMap);

	    String message = "Course created with code \"" + courseCode + "\" and name \"" + name + "\" and ID "
		    + course.getOrganisationId();
	    logger.info(message);
	    logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, creatorId, null, null, null,
		    "SPEnrolment: " + message);
	} else {
	    course = userManagementService.getOrganisationById(course.getOrganisationId());
	    String name = course.getName();

	    ExtCourseClassMap extOrgMap = allExistingParsedExtCourses.get(course.getOrganisationId());
	    if (extOrgMap == null) {
		extOrgMap = new ExtCourseClassMap();
		extOrgMap.setCourseid(name);
		extOrgMap.setExtServer(extServer);
		extOrgMap.setOrganisation(course);
		userManagementService.save(extOrgMap);

		allExistingParsedExtCourses.put(course.getOrganisationId(), extOrgMap);

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
    private void assignLearnersOrStaff(Organisation course, Map<String, ConcurrentMap<String, List<String>>> mappings,
	    ExtServer extServer, Integer creatorId, Set<User> allUsersParsed, Map<String, Integer> userIDs,
	    Map<String, Map<Integer, Set<Integer>>> allExistingRoles,
	    Map<Integer, ConcurrentMap<String, Organisation>> allExistingParsedCoursesAndSubcourses,
	    Map<Integer, ExtCourseClassMap> allExistingParsedExtCourses, Map<String, User> allExistingParsedUsers,
	    boolean isStaffMode, AtomicInteger mappingsProcessed)
	    throws UserInfoValidationException, InterruptedException, ExecutionException {
	String courseCode = course.getCode();
	Integer courseId = course.getOrganisationId();
	ConcurrentMap<String, Organisation> existingSubcourses = allExistingParsedCoursesAndSubcourses.get(courseId);
	Map<String, Organisation> nonProcessedSubcourses = existingSubcourses == null ? new ConcurrentHashMap<>()
		: new ConcurrentHashMap<>(existingSubcourses);
	// go through each subcourse
	Map<String, List<String>> subcourseMappings = mappings.get(courseCode);
	if (subcourseMappings != null) {
	    for (Entry<String, List<String>> subcourseEntry : subcourseMappings.entrySet()) {
		String subcourseCode = subcourseEntry.getKey();
		nonProcessedSubcourses.remove(subcourseCode);

		logger.info("Processing subcourse with code: " + subcourseCode);

		Organisation subcourse = existingSubcourses == null ? null : existingSubcourses.get(subcourseCode);
		// create subcourse
		if (subcourse == null) {
		    ExtCourseClassMap extSubOrgMap;

		    extSubOrgMap = integrationService.createExtCourseClassMap(extServer, creatorId, subcourseCode,
			    subcourseCode, course.getOrganisationId().toString(), false);

		    subcourse = extSubOrgMap.getOrganisation();
		    subcourse.setCode(subcourseCode);
		    userManagementService.save(subcourse);

		    if (existingSubcourses == null) {
			existingSubcourses = new ConcurrentHashMap<>();
			allExistingParsedCoursesAndSubcourses.put(courseId, existingSubcourses);
		    }
		    existingSubcourses.put(subcourse.getCode(), subcourse);
		    allExistingParsedExtCourses.put(extSubOrgMap.getOrganisation().getOrganisationId(), extSubOrgMap);

		    String message = "Subcourse created with code and name \"" + subcourseCode + "\" and ID "
			    + subcourse.getOrganisationId();
		    logger.info(message);
		    logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, creatorId, null, null, null,
			    "SPEnrolment: " + message);
		} else {
		    String name = subcourse.getName();

		    ExtCourseClassMap extOrgMap = allExistingParsedExtCourses.get(subcourse.getOrganisationId());
		    if (extOrgMap == null) {
			extOrgMap = new ExtCourseClassMap();
			extOrgMap.setCourseid(name);
			extOrgMap.setExtServer(extServer);
			extOrgMap.setOrganisation(subcourse);
			userManagementService.save(extOrgMap);

			String message = "External subcourse created for existing subcourse with code \""
				+ subcourseCode + "\" and name \"" + name + "\" and ID "
				+ subcourse.getOrganisationId();
			logger.info(message);
			logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, creatorId, null, null, null,
				"SPEnrolment: " + message);
		    }
		}

		Integer subcourseId = subcourse.getOrganisationId();

		// get existing learners/staff members for given subcourse
		Collection<User> subcourseMonitorsOrLearners = userManagementService
			.getUsersFromOrganisationByRole(subcourseId, isStaffMode ? Role.MONITOR : Role.LEARNER, true);
		Set<User> subcourseUsers = ConcurrentHashMap.newKeySet();
		subcourseUsers.addAll(subcourseMonitorsOrLearners);
		if (isStaffMode) {
		    // make sure that staff has both monitor and author roles in subcourse,
		    // even if they are course managers in the parent organisations
		    // and they already have a monitor role in subcourse
		    Collection<User> authors = userManagementService.getUsersFromOrganisationByRole(subcourseId,
			    Role.AUTHOR, true);
		    subcourseUsers.retainAll(authors);
		}

		for (String login : subcourseEntry.getValue()) {
		    login = login.toLowerCase();

		    logger.info("Processing \"" + login + "\"");

		    mappingsProcessed.incrementAndGet();

		    // check if the user is already a learner/staff member in the subcourse
		    // if so, there is nothing to do
		    boolean userAlreadyAssigned = false;
		    Integer userId = userIDs.get(login);
		    Iterator<User> subcourseUserIterator = subcourseUsers.iterator();
		    while (subcourseUserIterator.hasNext()) {
			User user = subcourseUserIterator.next();
			if (userId.equals(user.getUserId())) {
			    // IMPORTANT: if we found a matching existing learner/staff member, we get remove him from this collection
			    // so after this loop he does not get removed from subcourses
			    subcourseUserIterator.remove();
			    subcourseMonitorsOrLearners.remove(user);
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
		    User user = allExistingParsedUsers.get(login);
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

		// user is a learner/staff member, but he should not; remove him role from subcourse, course and lessons
		for (User user : subcourseMonitorsOrLearners) {

		    boolean removedFromSubcourse = removeFromCourse(subcourse, user,
			    isStaffMode ? Mode.STAFF : Mode.LEARNER, allExistingRoles);
		    if (removedFromSubcourse) {
			String message = (isStaffMode ? "Teacher" : "Learner") + " \"" + user.getLogin()
				+ "\" removed from subcourse " + subcourseId + " and its lessons";
			logger.info(message);
			logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, creatorId, null, null, null,
				"SPEnrolment: " + message);
		    }
		}
	    }

	    for (Organisation subcourse : nonProcessedSubcourses.values()) {
		// get all learners/staff members for given subcourse and remove them
		Collection<User> subcourseMonitorsOrLearners = userManagementService.getUsersFromOrganisationByRole(
			subcourse.getOrganisationId(), isStaffMode ? Role.MONITOR : Role.LEARNER, true);
		for (User user : subcourseMonitorsOrLearners) {

		    boolean removedFromSubcourse = removeFromCourse(subcourse, user,
			    isStaffMode ? Mode.STAFF : Mode.LEARNER, allExistingRoles);
		    if (removedFromSubcourse) {
			String message = (isStaffMode ? "Teacher" : "Learner") + " \"" + user.getLogin().toLowerCase()
				+ "\" removed from subcourse " + subcourse.getOrganisationId() + " and its lessons";
			logger.info(message);
			logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, creatorId, null, null, null,
				"SPEnrolment: " + message);
		    }
		}
	    }
	}
    }

    private boolean removeFromCourse(Organisation course, User user, Mode mode,
	    Map<String, Map<Integer, Set<Integer>>> allExistingRoles) {
	// no existing roles and the user should be removed - nothing to do
	Map<Integer, Set<Integer>> existingCoursesRoles = allExistingRoles.get(user.getLogin().toLowerCase());
	Set<Integer> existingCourseRoles = existingCoursesRoles == null ? null
		: existingCoursesRoles.get(course.getOrganisationId());
	if (existingCourseRoles == null || existingCourseRoles.isEmpty()) {
	    return false;
	}

	Organisation parentCourse = course.getOrganisationType().getOrganisationTypeId()
		.equals(OrganisationType.CLASS_TYPE) ? course.getParentOrganisation() : null;

	if (mode == Mode.MANAGER) {
	    existingCourseRoles.remove(Role.ROLE_GROUP_MANAGER);
	} else if (mode == Mode.STAFF) {
	    // managers are always monitors in subcourses
	    if (parentCourse != null
		    && userManagementService.hasRoleInOrganisation(user, Role.ROLE_GROUP_MANAGER, parentCourse)) {
		return false;
	    }

	    existingCourseRoles.remove(Role.ROLE_MONITOR);
	    existingCourseRoles.remove(Role.ROLE_AUTHOR);
	} else {
	    existingCourseRoles.remove(Role.ROLE_LEARNER);
	}

	userManagementService.setRolesForUserOrganisation(user, course,
		existingCourseRoles.stream().map(String::valueOf).collect(Collectors.toList()), false);

	// add learners and staff to lessons
	if (mode != Mode.MANAGER) {
	    for (Lesson lesson : lessonService.getLessonsByGroup(course.getOrganisationId())) {
		if (mode == Mode.STAFF) {
		    lessonService.removeStaffMember(lesson.getLessonId(), user.getUserId());
		} else {
		    lessonService.removeLearner(lesson.getLessonId(), user.getUserId());
		}
	    }

	    // if learner or staff is not in any subcourse, remove him also from parent course
	    if (course.getOrganisationType().getOrganisationTypeId().equals(OrganisationType.CLASS_TYPE)) {
		for (Organisation subcourse : parentCourse.getChildOrganisations()) {
		    List<UserOrganisationRole> rolesInSubcourse = userManagementService
			    .getUserOrganisationRoles(subcourse.getOrganisationId(), user.getLogin().toLowerCase());
		    if (!rolesInSubcourse.isEmpty()) {
			return true;
		    }
		}
		removeFromCourse(parentCourse, user, mode, allExistingRoles);
	    }
	}

	return true;
    }

    @SuppressWarnings("unchecked")
    private void assignManagers(Organisation course, Integer creatorId, Map<String, List<String>> mappings,
	    Set<User> allUsersParsed, Map<String, Integer> userIDs,
	    Map<String, Map<Integer, Set<Integer>>> allExistingRoles, Map<String, User> allExistingParsedUsers,
	    AtomicInteger mappingsProcessed)
	    throws UserInfoValidationException, InterruptedException, ExecutionException {
	String courseCode = course.getCode();
	Integer courseId = course.getOrganisationId();

	// get existing managers for given course
	Collection<User> courseUsers = userManagementService.getUsersFromOrganisationByRole(courseId,
		Role.GROUP_MANAGER, true);

	// go through each user
	List<String> courseMappings = mappings.get(courseCode);
	if (courseMappings != null) {
	    for (String login : courseMappings) {
		login = login.toLowerCase();

		logger.info("Processing manager \"" + login + "\"");
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

		// always set roles for course managers as subcourses could have been added

		Map<Integer, Set<Integer>> existingCoursesRoles = allExistingRoles.get(login);
		Set<Integer> existingCourseRoles = existingCoursesRoles == null ? null
			: allExistingRoles.get(login).get(courseId);
		if (existingCourseRoles == null) {
		    existingCourseRoles = ConcurrentHashMap.newKeySet();
		}
		existingCourseRoles.add(Role.ROLE_GROUP_MANAGER);

		User user = allExistingParsedUsers.get(login);

		userManagementService.setRolesForUserOrganisation(user, course,
			existingCourseRoles.stream().map(String::valueOf).collect(Collectors.toList()), true);

		if (!userAlreadyAssigned) {
		    String message = "Manager \"" + login + "\" added to course";
		    logger.info(message);
		    logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, creatorId, null, null, null,
			    "SPEnrolment: " + message);
		}
	    }
	}

	// user is a group manager, but he should not; remove him role from course
	for (User user : courseUsers) {
	    boolean removedFromCourse = removeFromCourse(course, user, Mode.MANAGER, allExistingRoles);
	    if (removedFromCourse) {
		String message = "Manager \"" + user.getLogin().toLowerCase() + "\" removed from course " + courseId;
		logger.info(message);
		logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, creatorId, null, null, null,
			"SPEnrolment: " + message);
	    }
	}
    }

    /**
     * Splits collection into as many spliterators as there are threads, with balanced threads' load
     */
    private <T> Collection<Spliterator<T>> splitCollection(Collection<T> sourceCollection, Comparator<T> comparator) {
	if (sourceCollection.size() < 10) {
	    return Set.of(sourceCollection.spliterator());
	}

	if (comparator == null) {
	    List<T> shuffledCollection = new ArrayList<>(sourceCollection);
	    Collections.shuffle(shuffledCollection);
	    sourceCollection = shuffledCollection;
	} else {
	    Set<T> treeSet = new TreeSet<>(comparator);
	    treeSet.addAll(sourceCollection);
	    sourceCollection = treeSet;
	}

	List<List<T>> collectionList = new ArrayList<>(threadCount);
	for (int threadIndex = 0; threadIndex < threadCount; threadIndex++) {
	    collectionList.add(new ArrayList<>(sourceCollection.size() / threadCount + 1));
	}

	int listIndex = 0;
	for (T element : sourceCollection) {
	    collectionList.get(listIndex % threadCount).add(element);
	    listIndex++;
	}

	return collectionList.stream().collect(Collectors.mapping(List::spliterator, Collectors.toList()));
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