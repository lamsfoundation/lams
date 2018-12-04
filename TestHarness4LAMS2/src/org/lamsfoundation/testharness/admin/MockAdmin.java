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
package org.lamsfoundation.testharness.admin;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.lamsfoundation.testharness.AbstractTest;
import org.lamsfoundation.testharness.Call;
import org.lamsfoundation.testharness.MockUser;
import org.lamsfoundation.testharness.TestHarnessException;
import org.lamsfoundation.testharness.TestManager;
import org.lamsfoundation.testharness.author.AuthorTest;
import org.lamsfoundation.testharness.learner.LearnerTest;
import org.lamsfoundation.testharness.monitor.MonitorTest;
import org.xml.sax.SAXException;

import com.meterware.httpunit.TableCell;
import com.meterware.httpunit.WebLink;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;

/**
 * <p>
 * <a href="MockAdmin.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class MockAdmin extends MockUser {
    private static final Logger log = Logger.getLogger(MockAdmin.class);
    
    public static final String AUTHOR_ROLE = "3";
    public static final String MONITOR_ROLE = "4";
    public static final String LEARNER_ROLE = "5";

    private static final String COURSE_FORM_FLAG = "organisationForm";
    private static final String COURSE_NAME = "name";
    private static final String COURSE_ID_PATTERN = "%orgId%";
    private static final String USER_FORM_FLAG = "userForm";
    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";
    private static final String PASSWORD2 = "password2";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String EMAIL = "email";
    private static final String COMMON_LAST_NAME = "Testharness";
    private static final String ROLES = "roles";
    private static final String USER_ID_START_FLAG = "userId=";
    private static final char USER_ID_END_FLAG = '&';

    private static final String LOGIN_TAKEN_ERROR = "Login is already taken.";
    private static final Pattern NEW_USER_ID_PATTERN = Pattern.compile("name=\"userId\" type=\"hidden\" value=\"(\\d+)\"");
    private static final Pattern EXISTING_USER_ID_PATTERN = Pattern.compile(", ID: (\\d+)");

    public MockAdmin(AbstractTest test, String username, String password) {
	super(test, username, password, null, null);
    }

    public String createCourse(String createCourseURL, String courseName) {
	try {
	    delay();
	    WebResponse resp = (WebResponse) new Call(wc, test, username + " creating course  " + courseName,
		    createCourseURL).execute();
	    if (!MockUser.checkPageContains(resp, MockAdmin.COURSE_FORM_FLAG)) {
		log.debug(resp.getText());
		throw new TestHarnessException(
			username + " did not get course creation page with the url:" + createCourseURL);
	    }
	    Map<String, Object> params = new HashMap<String, Object>();
	    params.put(MockAdmin.COURSE_NAME, courseName);
	    // fill the form and submit it and return the course id
	    new Call(wc, test, username + " submit course creation form", fillForm(resp, 0, params)).execute();

	    resp = (WebResponse) new Call(wc, test, username + " checking course ID by name " + courseName,
		    "/admin/organisation/getOrganisationIdByName.do?name=" + courseName).execute();
	    String idAsString = resp.getText();

	    if (idAsString == null) {
		log.debug(resp.getText());
		throw new TestHarnessException("Failed to get the course id for " + courseName);
	    }
	    log.info(username + " created course " + courseName + " and the id is " + idAsString);
	    return idAsString;
	} catch (IOException e) {
	    throw new RuntimeException(e);
	} catch (SAXException e) {
	    throw new RuntimeException(e);
	}
    }

    public void createUsers(String createUserURL, String addRolesURL, String courseId, boolean courseIdSet,
	    String storedUsersFileName) {
	try {
	    String url = createUserURL.replace(MockAdmin.COURSE_ID_PATTERN, courseId.toString());
	    List<MockUser> mockUsers = new ArrayList<MockUser>();
	    AuthorTest authorTest = test.getTestSuite().getAuthorTest();
	    Collections.addAll(mockUsers, authorTest.getUsers());
	    MonitorTest monitorTest = test.getTestSuite().getMonitorTest();
	    Collections.addAll(mockUsers, monitorTest.getUsers());
	    LearnerTest learnerTest = test.getTestSuite().getLearnerTest();
	    Collections.addAll(mockUsers, learnerTest.getUsers());

	    for (MockUser mockUser : mockUsers) {
		WebResponse resp = null;
		String name = mockUser.getUsername();
		boolean userExists = mockUser.getUserId() != null;
		if (!userExists) {
		    // create the user
		    log.info(username + " creating user " + name);
		    resp = (WebResponse) new Call(wc, test, username + " creating user " + name, url).execute();
		    if (!MockUser.checkPageContains(resp, MockAdmin.USER_FORM_FLAG)) {
			log.debug(resp.getText());
			throw new TestHarnessException(
				username + " did not get user creation page with the url " + url);
		    }
		    Map<String, Object> params = new HashMap<String, Object>();
		    params.put(MockAdmin.LOGIN, name);
		    params.put(MockAdmin.PASSWORD, name);
		    params.put(MockAdmin.PASSWORD2, name);
		    params.put(MockAdmin.FIRST_NAME, name);
		    params.put(MockAdmin.LAST_NAME, MockAdmin.COMMON_LAST_NAME);
		    params.put(MockAdmin.EMAIL,
			    name + "@" + MockAdmin.COMMON_LAST_NAME + "." + MockAdmin.COMMON_LAST_NAME.toLowerCase());
		    resp = (WebResponse) new Call(wc, test, username + " submit user creation form",
			    fillForm(resp, 0, params)).execute();
		    // add the roles
		    String respText = resp.getText();

		    if (respText.contains(MockAdmin.LOGIN_TAKEN_ERROR)) {
			Matcher m = MockAdmin.EXISTING_USER_ID_PATTERN.matcher(respText);
			if (m.find()) {
			    String userId = m.group(1);
			    mockUser.setUserId(userId);
			    log.debug("User " + name + " already exists with ID " + userId);
			} else {
			    throw new TestHarnessException(
				    "User " + name + " already exists, but could not retrieve his ID");
			}
		    } else {
			Matcher m = MockAdmin.NEW_USER_ID_PATTERN.matcher(respText);
			if (m.find()) {
			    String userId = m.group(1);
			    if (userId.equals("0")) {
				log.error(respText);
				throw new TestHarnessException(
					"Error while creating user " + name + ". Server returned user ID 0");
			    }
			    mockUser.setUserId(userId);
			    log.debug("User " + name + " created with ID " + userId);
			} else {
			    throw new TestHarnessException(
				    "User " + name + " was just created, but could not retrieve his ID");
			}
		    }
		} else {
		    log.debug("User " + name + " already exists, skipping creation");
		}

		if (!userExists || !courseIdSet) {
		    StringBuilder bodyBuilder = new StringBuilder();
		    bodyBuilder.append("orgId=").append(courseId).append("&userId=").append(mockUser.getUserId())
			    .append("&roles=").append(mockUser.getRole());
		    InputStream is = new ByteArrayInputStream(bodyBuilder.toString().getBytes("UTF-8"));

		    log.info(username + " adding roles to user " + name);
		    resp = (WebResponse) new Call(wc, test, username + " submit user roles form", addRolesURL, is)
			    .execute();
		    WebTable[] tables = resp.getTables();
		    if ((tables == null) || (tables.length < 2)) {
			log.debug(resp.getText());
			throw new TestHarnessException(
				username + " failed to get an user table after submitting user role form");
		    }

		    WebTable table = tables[1];
		    String idAsString = null;
		    for (int j = table.getRowCount() - 1; j >= 0; j--) {
			log.debug("1:" + table.getCellAsText(j, 0));
			log.debug("4:" + table.getCellAsText(j, 4));
			if (table.getCellAsText(j, 0).indexOf(name) != -1) {
			    TableCell cell = table.getTableCell(j, 4);
			    WebLink link = cell.getLinks()[0];
			    String cellText = link.getAttribute("href");
			    int startIndex = cellText.indexOf(MockAdmin.USER_ID_START_FLAG);
			    int endIndex = cellText.indexOf(MockAdmin.USER_ID_END_FLAG, startIndex);
			    idAsString = cellText.substring(startIndex + MockAdmin.USER_ID_START_FLAG.length(),
				    endIndex);
			    break;
			}
		    }

		    if (idAsString == null) {
			log.debug(resp.getText());
			throw new TestHarnessException("Failed to set roles for user " + name);
		    }

		    delay();
		} else {
		    log.debug("User " + name + " already has roles assigned to course " + courseId);
		}
	    }

	    new Call(wc, test, username + " logs out", "/lams/home/logout.do").execute();

	    TestManager.storeUsers(storedUsersFileName, mockUsers);
	} catch (IOException e) {
	    throw new RuntimeException(e);
	} catch (SAXException e) {
	    throw new RuntimeException(e);
	}
    }
}