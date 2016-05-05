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
package org.lamsfoundation.testharness;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;
import org.lamsfoundation.testharness.admin.AdminTest;
import org.lamsfoundation.testharness.admin.MockAdmin;
import org.lamsfoundation.testharness.author.AuthorTest;
import org.lamsfoundation.testharness.author.MockAuthor;
import org.lamsfoundation.testharness.learner.LearnerTest;
import org.lamsfoundation.testharness.learner.MockLearner;
import org.lamsfoundation.testharness.monitor.MockMonitor;
import org.lamsfoundation.testharness.monitor.MonitorTest;

/**
 * @author Fei Yang, Marcin Cieslak
 */
public class TestManager {
    private static final Logger log = Logger.getLogger(TestManager.class);

    List<TestSuite> testSuites = new LinkedList<TestSuite>();
    CountDownLatch allDoneSignal;
    private String testPropertyFileName;

    private static final int MAX_USERNAME_LENGTH = 20;
    // property keys of the master property file
    private static final String REPORT_FILE_NAME = "ReportFileName";
    private static final String REPORT_FILE_TEMPLATE = "ReportFileTemplate";
    private static final String NUMBER_OF_TEST_SUITES = "NumberOfTestSuites";
    private static final String TARGET_SERVER = "TargetServer";
    private static final String CONTEXT_ROOT = "ContextRoot";
    private static final String ADMIN_PROPERTY_FILE = "AdminTestPropertyFile";
    private static final String AUTHOR_PROPERTY_FILE = "AuthorTestPropertyFile";
    private static final String MONITOR_PROPERTY_FILE = "MonitorTestPropertyFile";
    private static final String LEARNER_PROPERTY_FILE = "LearnerTestPropertyFile";
    private static final String STORED_USERS_FILE = "UsersFile";

    // common property keys of all the single tests(admin test, author test, monitor test, learner test)
    private static final String MIN_DELAY = "MinDelay";
    private static final String MAX_DELAY = "MaxDelay";

    // property keys of admin test
    private static final String CREATE_COURSE_URL = "CreateCourseURL";
    private static final String CREATE_USER_URL = "CreateUserURL";
    private static final String ADD_ROLES_URL = "AddRolesURL";
    private static final String COURSE_NAME = "CourseName";
    private static final String COURSE_ID = "CourseId";
    private static final String SYSADMIN_USERNAME = "SysadminUsername";
    private static final String SYSADMIN_PASSWORD = "SysadminPassword";

    // property keys of author test
    private static final String LEARNING_DESIGN_UPLOAD_URL = "LearningDesignUploadURL";
    private static final String LEARNING_DESIGN_FILE = "LearningDesignFile";
    private static final String LEARNING_DESIGN_ID = "LearningDesignId";
    private static final String BASE_AUTHOR_NAME = "BaseAuthorName";

    // property keys of monitor test
    private static final String INIT_LESSON_URL = "InitLessonURL";
    private static final String CREATE_LESSON_CLASS_URL = "CreateLessonClassURL";
    private static final String START_LESSON_URL = "StartLessonURL";
    private static final String LESSON_ID = "LessonId";
    private static final String LESSON_NAME = "LessonName";
    private static final String BASE_MONITOR_NAME = "BaseMonitorName";
    private static final String GET_ALL_PROGRESS_URL = "GetAllLearnersProgressURL";

    // property keys of learner test
    private static final String NUMBER_LEARNERS = "NumberOfLearners";
    private static final String LEARNER_OFFSET = "LearnerOffset";
    private static final String BASE_LEARNER_NAME = "BaseLearnerName";
    private static final String JOIN_LESSON_URL = "JoinLessonURL";
    private static final String LESSON_ENTRY_URL = "LessonEntryURL";
    private static final String FILES_TO_UPLOAD = "FilesToUpload";

    public TestManager(String name) {
	this.testPropertyFileName = name;
    }

    private static String buildPropertyKey(String basePropertyKey, int suiteIndex) {
	return basePropertyKey + "." + suiteIndex;
    }

    private static String extractTestName(String propertyFileName) {
	return propertyFileName.substring(0, propertyFileName.indexOf(".properties"));
    }

    private static Integer getIntegerProperty(String propertyFileName, Properties properties, String key,
	    boolean nullable) {
	String value = TestManager.getStringProperty(propertyFileName, properties, key, nullable);
	try {
	    if (value == null) {
		return null;
	    } else {
		Integer number = new Integer(value);
		if (number <= 0) {
		    throw new TestHarnessException(
			    key + " is not specified as a positive number in file: " + propertyFileName);
		}
		return number;
	    }
	} catch (NumberFormatException e) {
	    throw new TestHarnessException(key + " is not specified as a number in file: " + propertyFileName, e);
	}
    }

    private static String getStringProperty(String properyFileName, Properties properties, String key,
	    boolean optional) {
	String value = properties.getProperty(key);
	if (value != null) {
	    value = value.trim();
	}
	if (!optional) {
	    if ((value == null) || (value.length() == 0)) {
		throw new TestHarnessException(key + " is not specified in file: " + properyFileName);
	    }
	}
	return (value != null) && (value.length() == 0) ? null : value;
    }

    private static Properties loadProperties(String propertyFileName) {
	InputStream propFile = null;
	try {
	    propFile = new FileInputStream(propertyFileName);
	    Properties props = new Properties();
	    props.load(propFile);
	    return props;
	} catch (FileNotFoundException e) {
	    throw new TestHarnessException("File \"" + propertyFileName + "\" was not found", e);
	} catch (IOException e) {
	    throw new TestHarnessException("IO error occured during loading file " + propertyFileName, e);
	} finally {
	    if (propFile != null) {
		try {
		    propFile.close();
		} catch (IOException e) {
		    throw new TestHarnessException("Error while closing properties file handler", e);
		}
	    }
	}
    }

    int countAborted() {
	int amount = 0;
	for (TestSuite suite : testSuites) {
	    if (!suite.isFinished()) {
		amount++;
	    }
	}
	return amount;
    }

    void start() {
	init();

	if (!TestReporter.initialized()) {
	    throw new TestHarnessException("TestReport class is not initialized");
	}
	TestManager.log.info("Running " + testSuites.size() + " test suites");

	if (testSuites.size() > 0) {
	    allDoneSignal = new CountDownLatch(testSuites.size());
	    for (TestSuite testSuite : testSuites) {
		new Thread(testSuite, "TestSuite-" + testSuite.getSuiteIndex()).start();
	    }
	    try {
		allDoneSignal.await();
	    } catch (InterruptedException e) {
		TestManager.log.error(e);
	    }
	}

	int abortedCount = countAborted();
	int finishedCount = testSuites.size() - abortedCount;
	TestManager.log.info(finishedCount + " test suites finished and " + abortedCount + " aborted");
    }

    private AdminTest createAdminTest(String adminTestPropertyFileName, String storedUsersFileName) {
	String testName = TestManager.extractTestName(adminTestPropertyFileName);
	TestManager.log.info("Creating admin test " + testName);

	Properties adminTestProperties = TestManager.loadProperties(adminTestPropertyFileName);
	Integer courseId = TestManager.getIntegerProperty(adminTestPropertyFileName, adminTestProperties,
		TestManager.COURSE_ID, true);
	Integer minDelay = TestManager.getIntegerProperty(adminTestPropertyFileName, adminTestProperties,
		TestManager.MIN_DELAY, true);
	Integer maxDelay = TestManager.getIntegerProperty(adminTestPropertyFileName, adminTestProperties,
		TestManager.MAX_DELAY, true);
	String createCourseURL = TestManager.getStringProperty(adminTestPropertyFileName, adminTestProperties,
		TestManager.CREATE_COURSE_URL, courseId != null);
	String createUserURL = TestManager.getStringProperty(adminTestPropertyFileName, adminTestProperties,
		TestManager.CREATE_USER_URL, false);
	String addRolesURL = TestManager.getStringProperty(adminTestPropertyFileName, adminTestProperties,
		TestManager.ADD_ROLES_URL, false);
	String courseName = TestManager.getStringProperty(adminTestPropertyFileName, adminTestProperties,
		TestManager.COURSE_NAME, true);
	String sysadminUsername = TestManager.getStringProperty(adminTestPropertyFileName, adminTestProperties,
		TestManager.SYSADMIN_USERNAME, false);
	String sysadminPassword = TestManager.getStringProperty(adminTestPropertyFileName, adminTestProperties,
		TestManager.SYSADMIN_PASSWORD, false);

	AdminTest test = new AdminTest(testName, minDelay, maxDelay, createCourseURL, createUserURL, addRolesURL,
		courseId == null ? null : courseId.toString(), courseName, storedUsersFileName);

	test.setUsers(new MockAdmin[] { new MockAdmin(test, sysadminUsername, sysadminPassword) });

	TestManager.log.info("Finished creating admin test " + testName);
	return test;
    }

    private AuthorTest createAuthorTest(String authorTestPropertyFileName, Map<String, String> storedUsers) {
	String testName = TestManager.extractTestName(authorTestPropertyFileName);
	TestManager.log.info("Creating author test " + testName);

	Properties authorTestProperties = TestManager.loadProperties(authorTestPropertyFileName);
	Integer ldId = TestManager.getIntegerProperty(authorTestPropertyFileName, authorTestProperties,
		TestManager.LEARNING_DESIGN_ID, true);
	Integer minDelay = TestManager.getIntegerProperty(authorTestPropertyFileName, authorTestProperties,
		TestManager.MIN_DELAY, true);
	Integer maxDelay = TestManager.getIntegerProperty(authorTestPropertyFileName, authorTestProperties,
		TestManager.MAX_DELAY, true);
	String learningDesignUploadURL = TestManager.getStringProperty(authorTestPropertyFileName, authorTestProperties,
		TestManager.LEARNING_DESIGN_UPLOAD_URL, ldId != null);
	String learningDesignFile = TestManager.getStringProperty(authorTestPropertyFileName, authorTestProperties,
		TestManager.LEARNING_DESIGN_FILE, ldId != null);
	String baseAuthorName = TestManager.getStringProperty(authorTestPropertyFileName, authorTestProperties,
		TestManager.BASE_AUTHOR_NAME, true);

	AuthorTest test = new AuthorTest(testName, minDelay, maxDelay, learningDesignUploadURL, learningDesignFile,
		ldId == null ? null : ldId.toString());

	baseAuthorName = baseAuthorName == null ? MockAuthor.DEFAULT_NAME : baseAuthorName;
	String username = TestUtil.buildName(testName, baseAuthorName, TestManager.MAX_USERNAME_LENGTH);
	String userId = storedUsers.get(username);
	test.setUsers(new MockAuthor[] { new MockAuthor(test, username, username, userId) });

	TestManager.log.info("Finished creating author test " + testName);
	return test;
    }

    private LearnerTest createLearnerTest(String learnerTestPropertyFileName, Map<String, String> storedUsers) {
	String testName = TestManager.extractTestName(learnerTestPropertyFileName);
	TestManager.log.info("Creating learner test:" + testName);

	Properties learnerTestProperties = TestManager.loadProperties(learnerTestPropertyFileName);
	Integer minDelay = TestManager.getIntegerProperty(learnerTestPropertyFileName, learnerTestProperties,
		TestManager.MIN_DELAY, true);
	Integer maxDelay = TestManager.getIntegerProperty(learnerTestPropertyFileName, learnerTestProperties,
		TestManager.MAX_DELAY, true);
	Integer numberOfLearners = TestManager.getIntegerProperty(learnerTestPropertyFileName, learnerTestProperties,
		TestManager.NUMBER_LEARNERS, true);
	numberOfLearners = numberOfLearners == null ? 1 : numberOfLearners;
	Integer learnerOffset = TestManager.getIntegerProperty(learnerTestPropertyFileName, learnerTestProperties,
		TestManager.LEARNER_OFFSET, true);
	learnerOffset = learnerOffset == null ? 1 : learnerOffset;
	String baseLearnerName = TestManager.getStringProperty(learnerTestPropertyFileName, learnerTestProperties,
		TestManager.BASE_LEARNER_NAME, true);
	String joinLessonURL = TestManager.getStringProperty(learnerTestPropertyFileName, learnerTestProperties,
		TestManager.JOIN_LESSON_URL, false);
	String lessonEntryURL = TestManager.getStringProperty(learnerTestPropertyFileName, learnerTestProperties,
		TestManager.LESSON_ENTRY_URL, false);
	String filesToUpload = TestManager.getStringProperty(learnerTestPropertyFileName, learnerTestProperties,
		TestManager.FILES_TO_UPLOAD, false);

	LearnerTest test = new LearnerTest(testName, minDelay, maxDelay, joinLessonURL, lessonEntryURL,
		filesToUpload == null ? null : filesToUpload.split(";"));

	MockLearner[] learners = new MockLearner[numberOfLearners];
	for (int i = 0; i < numberOfLearners; i++) {
	    baseLearnerName = baseLearnerName == null ? MockLearner.DEFAULT_NAME : baseLearnerName;
	    String username = TestUtil.buildName(testName, baseLearnerName + (learnerOffset + i),
		    TestManager.MAX_USERNAME_LENGTH);

	    String userId = storedUsers.get(username);
	    learners[i] = new MockLearner(test, username, username, userId);
	}
	test.setUsers(learners);

	TestManager.log.info("Finished creating learner test " + testName);
	return test;
    }

    private MonitorTest createMonitorTest(String monitorTestPropertyFileName, Map<String, String> storedUsers) {
	String testName = TestManager.extractTestName(monitorTestPropertyFileName);
	TestManager.log.info("Creating monitor test " + testName);

	Properties monitorTestProperties = TestManager.loadProperties(monitorTestPropertyFileName);
	Integer lsId = TestManager.getIntegerProperty(monitorTestPropertyFileName, monitorTestProperties,
		TestManager.LESSON_ID, true);
	Integer minDelay = TestManager.getIntegerProperty(monitorTestPropertyFileName, monitorTestProperties,
		TestManager.MIN_DELAY, true);
	Integer maxDelay = TestManager.getIntegerProperty(monitorTestPropertyFileName, monitorTestProperties,
		TestManager.MAX_DELAY, true);
	String initLessonURL = TestManager.getStringProperty(monitorTestPropertyFileName, monitorTestProperties,
		TestManager.INIT_LESSON_URL, lsId != null);
	String createLessonClassURL = TestManager.getStringProperty(monitorTestPropertyFileName, monitorTestProperties,
		TestManager.CREATE_LESSON_CLASS_URL, lsId != null);
	String startLessonURL = TestManager.getStringProperty(monitorTestPropertyFileName, monitorTestProperties,
		TestManager.START_LESSON_URL, lsId != null);
	String getAllLearnersProgressURL = TestManager.getStringProperty(monitorTestPropertyFileName,
		monitorTestProperties, TestManager.GET_ALL_PROGRESS_URL, lsId != null);
	String lsName = TestManager.getStringProperty(monitorTestPropertyFileName, monitorTestProperties,
		TestManager.LESSON_NAME, true);
	String baseMonitorName = TestManager.getStringProperty(monitorTestPropertyFileName, monitorTestProperties,
		TestManager.BASE_MONITOR_NAME, true);

	MonitorTest test = new MonitorTest(testName, minDelay, maxDelay, initLessonURL, createLessonClassURL,
		startLessonURL, getAllLearnersProgressURL, lsName, lsId == null ? null : lsId.toString());

	baseMonitorName = baseMonitorName == null ? MockMonitor.DEFAULT_NAME : baseMonitorName;
	String username = TestUtil.buildName(testName, baseMonitorName, TestManager.MAX_USERNAME_LENGTH);
	String userId = storedUsers.get(username);
	test.setUsers(new MockMonitor[] { new MockMonitor(test, username, username, userId) });

	TestManager.log.info("Finished creating monitor test " + testName);
	return test;
    }

    private TestSuite createTestSuite(Properties testProperties, int suiteIndex) {
	TestManager.log.info("Creating test suite " + suiteIndex);

	String targetServer = TestManager.getStringProperty(testPropertyFileName, testProperties,
		TestManager.buildPropertyKey(TestManager.TARGET_SERVER, suiteIndex), true);
	String contextRoot = TestManager.getStringProperty(testPropertyFileName, testProperties,
		TestManager.buildPropertyKey(TestManager.CONTEXT_ROOT, suiteIndex), true);
	String storedUsersFileName = TestManager.getStringProperty(testPropertyFileName, testProperties,
		TestManager.buildPropertyKey(TestManager.STORED_USERS_FILE, suiteIndex), true);
	Map<String, String> storedUsers = TestManager.getStoredUsers(storedUsersFileName);

	String adminTestPropertyFileName = TestManager.getStringProperty(testPropertyFileName, testProperties,
		TestManager.buildPropertyKey(TestManager.ADMIN_PROPERTY_FILE, suiteIndex), true);
	AdminTest adminTest = adminTestPropertyFileName == null ? null
		: createAdminTest(adminTestPropertyFileName, storedUsersFileName);
	String authorTestPropertyFileName = TestManager.getStringProperty(testPropertyFileName, testProperties,
		TestManager.buildPropertyKey(TestManager.AUTHOR_PROPERTY_FILE, suiteIndex), true);
	AuthorTest authorTest = authorTestPropertyFileName == null ? null
		: createAuthorTest(authorTestPropertyFileName, storedUsers);
	String monitorTestPropertyFileName = TestManager.getStringProperty(testPropertyFileName, testProperties,
		TestManager.buildPropertyKey(TestManager.MONITOR_PROPERTY_FILE, suiteIndex), true);
	MonitorTest monitorTest = monitorTestPropertyFileName == null ? null
		: createMonitorTest(monitorTestPropertyFileName, storedUsers);
	String learnerTestPropertyFileName = TestManager.getStringProperty(testPropertyFileName, testProperties,
		TestManager.buildPropertyKey(TestManager.LEARNER_PROPERTY_FILE, suiteIndex), true);
	LearnerTest learnerTest = learnerTestPropertyFileName == null ? null
		: createLearnerTest(learnerTestPropertyFileName, storedUsers);

	TestSuite suite = new TestSuite(this, suiteIndex, targetServer, contextRoot, adminTest, authorTest, monitorTest,
		learnerTest);

	TestManager.log.info("Finished creating test suite " + suite.toString());
	return suite;
    }

    private void init() {
	TestManager.log.info("Initializing");

	Properties testProperties = TestManager.loadProperties(testPropertyFileName);
	TestReporter.setFileName(TestManager.getStringProperty(testPropertyFileName, testProperties,
		TestManager.REPORT_FILE_NAME, false));
	String fileTemplate = TestManager.getStringProperty(testPropertyFileName, testProperties,
		TestManager.REPORT_FILE_TEMPLATE, false);
	TestReporter.setFileTemplate(fileTemplate);

	int numberOfTestSuites = TestManager.getIntegerProperty(testPropertyFileName, testProperties,
		TestManager.NUMBER_OF_TEST_SUITES, false);
	for (int suiteIndex = 1; suiteIndex < (numberOfTestSuites + 1); suiteIndex++) {
	    testSuites.add(createTestSuite(testProperties, suiteIndex));
	}

	TestManager.log.info("Finished initialization");
    }

    private static Map<String, String> getStoredUsers(String storedUsersFileName) {
	Map<String, String> result = new TreeMap<String, String>();

	if (storedUsersFileName != null) {
	    BufferedReader reader = null;
	    try {
		File file = new File(storedUsersFileName);
		if (file.canRead()) {
		    reader = new BufferedReader(new FileReader(storedUsersFileName));
		    String line = null;
		    do {
			line = reader.readLine();
			if (line != null) {
			    String[] userData = line.split(",");
			    if ((userData[0] != null) && (userData[1] != null)) {
				result.put(userData[0], userData[1]);
			    }
			}
		    } while (line != null);
		}
	    } catch (Exception e) {
		TestManager.log.warn("Error while trying to read users file: " + storedUsersFileName, e);
	    } finally {
		if (reader != null) {
		    try {
			reader.close();
		    } catch (IOException e) {
			TestManager.log.warn("Error while trying to read users file: " + storedUsersFileName, e);
		    }
		}
	    }
	}

	return result;
    }

    public static void storeUsers(String storedUsersFileName, Collection<MockUser> storedUsers) {
	if (storedUsersFileName != null) {
	    BufferedWriter writer = null;

	    try {
		File file = new File(storedUsersFileName);
		file.delete();

		writer = new BufferedWriter(new FileWriter(file));
		for (MockUser user : storedUsers) {
		    if (user.getUserId() != null) {
			writer.append(user.getUsername() + "," + user.getUserId());
			writer.newLine();
		    }
		}
	    } catch (Exception e) {
		TestManager.log.warn("Error while trying to write users file: " + storedUsersFileName, e);
	    } finally {
		if (writer != null) {
		    try {
			writer.close();
		    } catch (IOException e) {
			TestManager.log.warn("Error while trying to write users file: " + storedUsersFileName, e);
		    }
		}
	    }
	}
    }
}