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
import java.util.Hashtable;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.testharness.Call;
import org.lamsfoundation.testharness.MockUser;
import org.lamsfoundation.testharness.TestHarnessException;
import org.lamsfoundation.testharness.TestUtil;

import com.allaire.wddx.WddxDeserializationException;
import com.meterware.httpunit.WebResponse;

/**
 * @version
 * 
 *          <p>
 *          <a href="MockMonitor.java.html"><i>View Source</i></a>
 *          </p>
 * 
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class MockMonitor extends MockUser implements Runnable {

    private static final Logger log = Logger.getLogger(MockMonitor.class);

    public static final String DEFAULT_NAME = "Monitor";

    private static final String WDDX_CONTENT_TYPE = "text/xml; charset=utf-8";

    private static final String LDID_PATTERN = "%ldId%";

    private static final String USER_ID_PATTERN = "%uid%";

    private static final String LESSON_NAME_PATTERN = "%name%";

    private static final String LESSON_ID_PATTERN = "%lsId%";

    private static final String ORGANISATION_ID_PATTERN = "%orgId%";

    private static final String LESSON_ID_KEY = "messageValue";

    private static final String LESSON_CREATED_FLAG = "true";

    private static final String MESSAGE_VALUE_KEY = "messageValue";

    private static final String LD_ID_KEY = "learningDesignID";

    private CountDownLatch stopSignal = null;

    /**
     * MockMonitor Constructor
     * 
     * @param
     */
    public MockMonitor(MonitorTest test, String username, String password, String userId) {
	super(test, username, password, userId);
    }

    public void createLessonClass(String createLessonClassURL, String userId) {
	try {
	    String url = createLessonClassURL.replace(MockMonitor.USER_ID_PATTERN, userId);
	    InputStream postBodyIS = buildPostBody();
	    new Call(wc, test, "Create Lesson Class", url, postBodyIS, MockMonitor.WDDX_CONTENT_TYPE).execute();
	    MockMonitor.log.info(username + " set the lesson class");
	} catch (IOException e) {
	    throw new RuntimeException(e);
	}
    }

    @SuppressWarnings("rawtypes")
    public String initLesson(String initLessonURL, String ldId, String organisationID, String userId, String name) {
	try {
	    if (userId == null) {
		throw new TestHarnessException(
			"User id is missing. If you have set UserCreated (admin properties) to true, then you must set UserId in the monitor properties.");
	    }
	    MockMonitor.log.debug("initLessonURL " + initLessonURL + " ldId " + ldId + " orgId " + organisationID
		    + " userId " + userId + " name " + name);
	    String url = initLessonURL.replace(MockMonitor.LDID_PATTERN, ldId)
		    .replace(MockMonitor.ORGANISATION_ID_PATTERN, organisationID)
		    .replace(MockMonitor.USER_ID_PATTERN, userId).replace(MockMonitor.LESSON_NAME_PATTERN, name);
	    WebResponse resp = (WebResponse) new Call(wc, test, "Init Lesson", url).execute();
	    Hashtable hashtable = (Hashtable) TestUtil.deserialize(resp.getText());
	    String idAsString = new Integer(((Double) hashtable.get(MockMonitor.LESSON_ID_KEY)).intValue()).toString();
	    MockMonitor.log.info(username + " initialized the lesson " + name + " and the id is " + idAsString);
	    return idAsString;
	} catch (WddxDeserializationException e) {
	    throw new RuntimeException(e);
	} catch (IOException e) {
	    throw new RuntimeException(e);
	}
    }

    @Override
    public void run() {
	try {
	    MockMonitor.log.info(username + " is monitoring...");
	    MonitorTest monitorTest = (MonitorTest) test;
	    String ldId = getLessonDetails(monitorTest.getGetLessonDetailsURL(), monitorTest.getLsId());
	    getContributeActivities(monitorTest.getGetContributeActivitiesURL(), monitorTest.getLsId());
	    getLearningDesignDetails(monitorTest.getGetLearningDesignDetailsURL(), ldId);
	    while (stopSignal == null) {
		delay();
		MockMonitor.log.info(username + " is refreshing all learners progress");
		getAllLearnersProgress(monitorTest.getGetAllLearnersProgressURL(), monitorTest.getLsId());
		MockMonitor.log.info(username + " got the latest learners progress");
	    }
	    MockMonitor.log.info(username + " stopped monitoring");
	    stopSignal.countDown();
	} catch (Exception e) {
	    MockMonitor.log.error(username + " aborted on monitoring", e);
	}
    }

    public final void setStopFlag(CountDownLatch stopSignal) {
	this.stopSignal = stopSignal;
    }

    public void startLesson(String startLessonURL, String lsId, String userId) {
	try {
	    String url = startLessonURL.replace(MockMonitor.LESSON_ID_PATTERN, lsId).replace(
		    MockMonitor.USER_ID_PATTERN, userId);
	    WebResponse resp = (WebResponse) new Call(wc, test, "Start Lesson", url).execute();
	    if (!MockUser.checkPageContains(resp, MockMonitor.LESSON_CREATED_FLAG)) {
		MockMonitor.log.debug(resp.getText());
		throw new TestHarnessException(username + " failed to create lesson with the url " + url);
	    }
	    MockMonitor.log.info(username + " started the lesson " + lsId);
	} catch (IOException e) {
	    throw new RuntimeException(e);
	}
    }

    private InputStream buildPostBody() throws IOException {
	Hashtable<String, Object> learners = new Hashtable<String, Object>();
	MockUser[] mockLearners = test.getTestSuite().getLearnerTest().getUsers();
	String courseName = test.getTestSuite().getAdminTest().getCourseName();
	Integer[] users = new Integer[mockLearners.length];
	for (int i = 0; i < mockLearners.length; i++) {
	    users[i] = new Integer(mockLearners[i].getUserId());
	}
	learners.put("users", users);
	learners.put("groupName", courseName + "_learners");
	Hashtable<String, Object> staff = new Hashtable<String, Object>();
	staff.put("users",
		new Integer[] { new Integer(test.getTestSuite().getMonitorTest().getUsers()[0].getUserId()) });
	staff.put("groupName", courseName + "_staff");
	Hashtable<String, Object> lessonClassInfo = new Hashtable<String, Object>();
	lessonClassInfo.put("learners", learners);
	lessonClassInfo.put("staff", staff);
	lessonClassInfo.put("organisationID", new Integer(test.getTestSuite().getAdminTest().getCourseId()));
	lessonClassInfo.put("lessonID", new Integer(((MonitorTest) test).getLsId()));
	String lessonClassWDDX = TestUtil.serialize(lessonClassInfo);
	MockMonitor.log.debug("Generated LessonClass WDDX packet:" + lessonClassWDDX);
	return new ByteArrayInputStream(lessonClassWDDX.getBytes("UTF-8"));
    }

    private void getAllLearnersProgress(String getAllLearnersProgressURL, String lsId) throws IOException {
	String url = getAllLearnersProgressURL.replace(MockMonitor.LESSON_ID_PATTERN, lsId);
	WebResponse resp = (WebResponse) new Call(wc, test, username + " get all learners progress", url).execute();
	MockMonitor.log.debug("Learner progress: " + resp.getText());
    }

    private void getContributeActivities(String getContributeActivitiesURL, String lsId) throws IOException {
	String url = getContributeActivitiesURL.replace(MockMonitor.LESSON_ID_PATTERN, lsId);
	WebResponse resp = (WebResponse) new Call(wc, test, username + " get contribute activities", url).execute();
	MockMonitor.log.debug("Contribute activities: " + resp.getText());
    }

    private void getLearningDesignDetails(String getLearningDesignDetailsURL, String ldId)
	    throws WddxDeserializationException, IOException {
	String url = getLearningDesignDetailsURL.replace(MockMonitor.LDID_PATTERN, ldId);
	WebResponse resp = (WebResponse) new Call(wc, test, username + " get learning design details", url).execute();
	TestUtil.deserialize(resp.getText());
    }

    private String getLessonDetails(String getLessonDetailsURL, String lsId) throws JSONException, IOException {
	String url = getLessonDetailsURL.replace(MockMonitor.LESSON_ID_PATTERN, lsId);
	WebResponse resp = (WebResponse) new Call(wc, test, username + " get lesson", url).execute();
	MockMonitor.log.debug("Lesson details: " + resp.getText());
	JSONObject jsonObject = new JSONObject(resp.getText());
	return String.valueOf(jsonObject.getLong(MockMonitor.LD_ID_KEY));
    }
}