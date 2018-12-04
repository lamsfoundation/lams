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
package org.lamsfoundation.testharness.monitor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;
import org.lamsfoundation.testharness.Call;
import org.lamsfoundation.testharness.MockUser;
import org.lamsfoundation.testharness.TestHarnessException;
import org.lamsfoundation.testharness.admin.MockAdmin;

import com.meterware.httpunit.WebResponse;

/**
 * <p>
 * <a href="MockMonitor.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class MockMonitor extends MockUser implements Runnable {
    private static final Logger log = Logger.getLogger(MockMonitor.class);

    public static final String DEFAULT_NAME = "Monitor";

    private static final String LDID_PATTERN = "%ldId%";

    private static final String USER_ID_PATTERN = "%uid%";

    private static final String LESSON_NAME_PATTERN = "%name%";

    private static final String LESSON_ID_PATTERN = "%lsId%";

    private static final String ORGANISATION_ID_PATTERN = "%orgId%";

    private static final String LESSON_CREATED_FLAG = "true";

    private CountDownLatch stopSignal = null;

    /**
     * MockMonitor Constructor
     *
     * @param
     */
    public MockMonitor(MonitorTest test, String username, String password, String userId) {
	super(test, username, password, MockAdmin.MONITOR_ROLE, userId);
    }

    public void createLessonClass(String createLessonClassURL, String userId) {
	try {
	    String url = createLessonClassURL.replace(MockMonitor.USER_ID_PATTERN, userId);
	    StringBuilder bodyBuilder = new StringBuilder();
	    bodyBuilder.append("lessonID=").append(((MonitorTest) test).getLsId()).append("&organisationID=")
		    .append(test.getTestSuite().getAdminTest().getCourseId()).append("&userID=").append(userId)
		    .append("&learners=");

	    MockUser[] mockLearners = test.getTestSuite().getLearnerTest().getUsers();

	    for (MockUser mockLearner : mockLearners) {
		bodyBuilder.append(mockLearner.getUserId()).append(",");
	    }
	    bodyBuilder.append("&monitors=").append(test.getTestSuite().getMonitorTest().getUsers()[0].getUserId());

	    InputStream is = new ByteArrayInputStream(bodyBuilder.toString().getBytes("UTF-8"));
	    new Call(wc, test, username + " creates lesson class", url, is).execute();
	    log.info(username + " set the lesson class");
	} catch (IOException e) {
	    throw new RuntimeException(e);
	}
    }

    public String initLesson(String initLessonURL, String ldId, String organisationID, String userId, String name) {
	try {
	    if (userId == null) {
		throw new TestHarnessException(
			"User id is missing. If you have set UserCreated (admin properties) to true, then you must set UserId in the monitor properties.");
	    }

	    String url = initLessonURL.replace(MockMonitor.LDID_PATTERN, ldId)
		    .replace(MockMonitor.ORGANISATION_ID_PATTERN, organisationID)
		    .replace(MockMonitor.USER_ID_PATTERN, userId).replace(MockMonitor.LESSON_NAME_PATTERN, name);
	    log.debug("initLessonURL:  " + url);
	    WebResponse resp = (WebResponse) new Call(wc, test, username + " inits lesson", url).execute();

	    String idAsString = resp.getText().trim();
	    log.info(username + " initialized the lesson " + name + " and the id is " + idAsString);
	    return idAsString;
	} catch (IOException e) {
	    throw new RuntimeException(e);
	}
    }

    @Override
    public void run() {
	try {
	    log.info(username + " is monitoring");
	    MonitorTest monitorTest = (MonitorTest) test;

	    while (stopSignal == null) {
		delay();
		log.debug(username + " is refreshing all learners progress");
		getAllLearnersProgress(monitorTest.getGetAllLearnersProgressURL(), monitorTest.getLsId());
	    }
	    new Call(wc, test, username + " logs out", "/lams/home/logout.do").execute();
	    log.info(username + " stopped monitoring");
	    stopSignal.countDown();
	} catch (Exception e) {
	    log.error(username + " aborted on monitoring", e);
	}
    }

    public final void setStopFlag(CountDownLatch stopSignal) {
	this.stopSignal = stopSignal;
    }

    public void startLesson(String startLessonURL, String lsId, String userId) {
	try {
	    String url = startLessonURL.replace(MockMonitor.LESSON_ID_PATTERN, lsId)
		    .replace(MockMonitor.USER_ID_PATTERN, userId);
	    WebResponse resp = (WebResponse) new Call(wc, test, username + " starts Lesson", url).execute();
	    if (!MockUser.checkPageContains(resp, MockMonitor.LESSON_CREATED_FLAG)) {
		log.debug(resp.getText());
		throw new TestHarnessException(username + " failed to create lesson with the url " + url);
	    }
	    log.info(username + " started the lesson " + lsId);
	} catch (IOException e) {
	    throw new RuntimeException(e);
	}
    }

    private void getAllLearnersProgress(String getAllLearnersProgressURL, String lsId) throws IOException {
	// it gets some redundant information, but reflects what Monitor would be doing in his interface
	String url = getAllLearnersProgressURL.replace(MockMonitor.LESSON_ID_PATTERN, lsId);
	WebResponse resp = (WebResponse) new Call(wc, test, username + " get all learners progress", url).execute();
	log.debug("Learner progress: " + resp.getText());
    }
}