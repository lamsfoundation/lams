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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.lamsfoundation.testharness.AbstractTest;
import org.lamsfoundation.testharness.Call;
import org.lamsfoundation.testharness.MockUser;
import org.lamsfoundation.testharness.TestHarnessException;
import org.lamsfoundation.testharness.author.AuthorTest;
import org.lamsfoundation.testharness.learner.LearnerTest;
import org.lamsfoundation.testharness.monitor.MonitorTest;
import org.xml.sax.SAXException;

import com.meterware.httpunit.TableCell;
import com.meterware.httpunit.WebLink;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;

/**
 * @version
 * 
 *          <p>
 *          <a href="MockAdmin.java.html"><i>View Source</i></a>
 *          </p>
 * 
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class MockAdmin extends MockUser {

    private static class MockUserWithRoles {
	MockUser user;
	String role;

	public MockUserWithRoles(MockUser user, String roles) {
	    this.user = user;
	    this.role = roles;
	}
    }

    private static final Logger log = Logger.getLogger(MockAdmin.class);
    private static final String COURSE_FORM_FLAG = "OrganisationForm";
    private static final String COURSE_NAME = "name";
    private static final String COURSE_ID_PATTERN = "%orgId%";
    private static final String USER_FORM_FLAG = "UserForm";
    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";
    private static final String PASSWORD2 = "password2";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String EMAIL = "email";
    private static final String COMMON_LAST_NAME = "Testharness";
    private static final String ROLES = "roles";
    private static final String AUTHOR_ROLE = "3";
    private static final String MONITOR_ROLE = "4";
    private static final String LEARNER_ROLE = "5";
    private static final String USER_ID_START_FLAG = "userId=";
    private static final char USER_ID_END_FLAG = '&';

    private static final String LOGIN_TAKEN_ERROR = "Login is already taken.";

    public MockAdmin(AbstractTest test, String username, String password, String userId) {
	super(test, username, password, userId);
    }

    private static MockUserWithRoles[] getMockUsersWithRoles(AbstractTest[] tests) {
	List<MockUserWithRoles> users = new ArrayList<MockUserWithRoles>();
	for (AbstractTest test : tests) {
	    if (test != null) {
		MockUser[] mockUsers = test.getUsers();
		String role;
		if (test instanceof AuthorTest) {
		    role = MockAdmin.AUTHOR_ROLE;
		} else if (test instanceof MonitorTest) {
		    role = MockAdmin.MONITOR_ROLE;
		} else {
		    role = MockAdmin.LEARNER_ROLE;
		}
		for (MockUser mockUser : mockUsers) {
		    users.add(new MockUserWithRoles(mockUser, role));
		}
	    }
	}
	return users.toArray(new MockUserWithRoles[] {});
    }

    public String createCourse(String createCourseURL, String courseName) {
	try {
	    delay();
	    WebResponse resp = (WebResponse) new Call(wc, test, "Creating Course:" + courseName, createCourseURL)
		    .execute();
	    if (!MockUser.checkPageContains(resp, MockAdmin.COURSE_FORM_FLAG)) {
		MockAdmin.log.debug(resp.getText());
		throw new TestHarnessException(username + " did not get course creation page with the url:"
			+ createCourseURL);
	    }
	    Map<String, Object> params = new HashMap<String, Object>();
	    params.put(MockAdmin.COURSE_NAME, courseName);
	    // fill the form and submit it and return the course id
	    resp = (WebResponse) new Call(wc, test, "Submit Course Creation Form", fillForm(resp, 0, params)).execute();
	    WebTable[] tables = resp.getTables();
	    if ((tables == null) || (tables.length == 0)) {
		MockAdmin.log.debug(resp.getText());
		throw new TestHarnessException(username
			+ " failed to get an course table after submitting course creation form");
	    }
	    WebTable table = tables[0];
	    String idAsString = null;
	    for (int i = table.getRowCount() - 1; i >= 0; i--) {
		if (table.getCellAsText(i, 1).indexOf(courseName) != -1) {// found the organisation created just now
		    idAsString = table.getCellAsText(i, 0);
		    break;
		}
	    }
	    if (idAsString == null) {
		MockAdmin.log.debug(resp.getText());
		throw new TestHarnessException("Failed to get the course id for " + courseName);
	    }
	    MockAdmin.log.info(username + " created course " + courseName + " and the id is " + idAsString);
	    return idAsString;
	} catch (IOException e) {
	    throw new RuntimeException(e);
	} catch (SAXException e) {
	    throw new RuntimeException(e);
	}
    }

    public void createUsers(String createUserURL, String courseId) {
	try {
	    String url = createUserURL.replace(MockAdmin.COURSE_ID_PATTERN, courseId.toString());
	    AuthorTest authorTest = test.getTestSuite().getAuthorTest();
	    MonitorTest monitorTest = test.getTestSuite().getMonitorTest();
	    LearnerTest learnerTest = test.getTestSuite().getLearnerTest();
	    AbstractTest[] tests = new AbstractTest[] { authorTest, monitorTest, learnerTest };
	    MockUserWithRoles[] mockUsers = MockAdmin.getMockUsersWithRoles(tests);

	    for (MockUserWithRoles mockUser : mockUsers) {
		delay();

		// create the user
		String name = mockUser.user.getUsername();
		MockAdmin.log.info(username + " creating user " + name);
		WebResponse resp = (WebResponse) new Call(wc, test, username + " creating user " + name, url).execute();
		if (!MockUser.checkPageContains(resp, MockAdmin.USER_FORM_FLAG)) {
		    MockAdmin.log.debug(resp.getText());
		    throw new TestHarnessException(username + " did not get user creation page with the url " + url);
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(MockAdmin.LOGIN, name);
		params.put(MockAdmin.PASSWORD, name);
		params.put(MockAdmin.PASSWORD2, name);
		params.put(MockAdmin.FIRST_NAME, name);
		params.put(MockAdmin.LAST_NAME, MockAdmin.COMMON_LAST_NAME);
		params.put(MockAdmin.EMAIL,
			name + "@" + MockAdmin.COMMON_LAST_NAME + "." + MockAdmin.COMMON_LAST_NAME.toLowerCase());
		resp = (WebResponse) new Call(wc, test, username + " submit user creation form", fillForm(resp, 0,
			params)).execute();

		// add the roles
		if (resp.getText().indexOf(MockAdmin.LOGIN_TAKEN_ERROR) != -1) {
		    log.warn("Login " + name + " already taken.");
		    continue;
		}

		MockAdmin.log.info(username + " adding roles to user " + name);
		params = new HashMap<String, Object>();
		params.put(MockAdmin.ROLES, new String[] { mockUser.role });
		resp = (WebResponse) new Call(wc, test, username + " submit user rolesform", fillForm(resp, 0, params))
			.execute();
		WebTable[] tables = resp.getTables();
		if ((tables == null) || (tables.length < 2)) {
		    MockAdmin.log.debug(resp.getText());
		    throw new TestHarnessException(username
			    + " failed to get an user table after submitting user role form");
		}

		WebTable table = tables[1];
		String idAsString = null;
		for (int j = table.getRowCount() - 1; j >= 0; j--) {
		    MockAdmin.log.debug("1:" + table.getCellAsText(j, 0));
		    MockAdmin.log.debug("4:" + table.getCellAsText(j, 4));
		    if (table.getCellAsText(j, 0).indexOf(name) != -1) {
			TableCell cell = table.getTableCell(j, 4);
			WebLink link = cell.getLinks()[0];
			String cellText = link.getAttribute("href");
			int startIndex = cellText.indexOf(MockAdmin.USER_ID_START_FLAG);
			int endIndex = cellText.indexOf(MockAdmin.USER_ID_END_FLAG, startIndex);
			idAsString = cellText.substring(startIndex + MockAdmin.USER_ID_START_FLAG.length(), endIndex);
			break;
		    }
		}
		if (idAsString == null) {
		    MockAdmin.log.debug(resp.getText());
		    throw new TestHarnessException("Failed to get the user id for " + name);
		}
		MockAdmin.log.info(username + " created user " + name + " and the id is " + idAsString);
		mockUser.user.setUserId(idAsString);
	    }
	} catch (IOException e) {
	    throw new RuntimeException(e);
	} catch (SAXException e) {
	    throw new RuntimeException(e);
	}
    }
}