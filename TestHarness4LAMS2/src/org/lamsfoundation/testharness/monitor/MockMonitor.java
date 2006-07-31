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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
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
import org.lamsfoundation.testharness.Call;
import org.lamsfoundation.testharness.MockUser;
import org.lamsfoundation.testharness.TestHarnessException;
import org.lamsfoundation.testharness.TestUtil;

import com.allaire.wddx.WddxDeserializationException;
import com.meterware.httpunit.WebResponse;

/**
 * @version
 * 
 * <p>
 * <a href="MockMonitor.java.html"><i>View Source</i></a>
 * </p>
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

	private static final String LESSON_ID_KEY = "messageValue";

	private static final String LESSON_CREATED_FLAG = "<boolean value='true'/>";

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

	public String initLesson(String initLessonURL, String ldId, String userId, String name) {
		try {
			delay();
			String url = initLessonURL.replace(LDID_PATTERN, ldId).replace(USER_ID_PATTERN, userId).replace(LESSON_NAME_PATTERN, name);
			WebResponse resp = (WebResponse) new Call(wc, test, "Init Lesson", url).execute();
			Hashtable hashtable = (Hashtable) TestUtil.deserialize(resp.getText());
			String idAsString = new Integer(((Double) hashtable.get(LESSON_ID_KEY)).intValue()).toString();
			log.info(username + " initialized the lesson " + name + " and the id is " + idAsString);
			return idAsString;
		} catch (WddxDeserializationException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void createLessonClass(String createLessonClassURL, String userId) {
		try {
			delay();
			String url = createLessonClassURL.replace(USER_ID_PATTERN, userId);
			InputStream postBodyIS = buildPostBody();
			new Call(wc, test, "Create Lesson Class", url, postBodyIS, WDDX_CONTENT_TYPE).execute();
			log.info(username + " set the lesson class");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void startLesson(String startLessonURL, String lsId, String userId) {
		try {
			delay();
			String url = startLessonURL.replace(LESSON_ID_PATTERN, lsId).replace(USER_ID_PATTERN, userId);
			WebResponse resp = (WebResponse) new Call(wc, test, "Start Lesson", url).execute();
			if (!checkPageContains(resp, LESSON_CREATED_FLAG)) {
				log.debug(resp.getText());
				throw new TestHarnessException(username + " failed to create lesson with the url " + url);
			}
			log.info(username + " started the lesson " + lsId);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private String getLessonDetails(String getLessonDetailsURL, String lsId) throws WddxDeserializationException, IOException {
		String url = getLessonDetailsURL.replace(LESSON_ID_PATTERN, lsId);
		WebResponse resp = (WebResponse) new Call(wc, test, username + " get lesson", url).execute();
		Hashtable hashtable = (Hashtable) TestUtil.deserialize(resp.getText());
		hashtable = (Hashtable) hashtable.get(MESSAGE_VALUE_KEY);
		return new Integer(((Double) hashtable.get(LD_ID_KEY)).intValue()).toString();
	}

	private void getContributeActivities(String getContributeActivitiesURL, String lsId) throws WddxDeserializationException, IOException {
		String url = getContributeActivitiesURL.replace(LESSON_ID_PATTERN, lsId);
		WebResponse resp = (WebResponse) new Call(wc, test, username + " get contribute activities", url).execute();
		TestUtil.deserialize(resp.getText());
	}

	private void getLearningDesignDetails(String getLearningDesignDetailsURL, String ldId) throws WddxDeserializationException, IOException {
		String url = getLearningDesignDetailsURL.replace(LDID_PATTERN, ldId);
		WebResponse resp = (WebResponse) new Call(wc, test, username + " get learning design details", url).execute();
		TestUtil.deserialize(resp.getText());
	}

	private void getAllLearnersProgress(String getAllLearnersProgressURL, String lsId) throws WddxDeserializationException, IOException {
		String url = getAllLearnersProgressURL.replace(LESSON_ID_PATTERN, lsId);
		WebResponse resp = (WebResponse) new Call(wc, test, username + " get all learners progress", url).execute();
		TestUtil.deserialize(resp.getText());
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
		staff.put("users", new Integer[] { new Integer(test.getTestSuite().getMonitorTest().getUsers()[0].getUserId()) });
		staff.put("groupName", courseName + "_staff");
		Hashtable<String, Object> lessonClassInfo = new Hashtable<String, Object>();
		lessonClassInfo.put("learners", learners);
		lessonClassInfo.put("staff", staff);
		lessonClassInfo.put("organisationID", new Integer(test.getTestSuite().getAdminTest().getCourseId()));
		lessonClassInfo.put("lessonID", new Integer(((MonitorTest) test).getLsId()));
		String lessonClassWDDX = TestUtil.serialize(lessonClassInfo);
		log.debug("Generated LessonClass WDDX packet:" + lessonClassWDDX);
		return new ByteArrayInputStream(lessonClassWDDX.getBytes("UTF-8"));
	}

	public void run() {
		monitor();
	}

	private void monitor() {
		try {
			log.info(username+" is monitoring...");
			MonitorTest monitorTest = (MonitorTest) test;
			String ldId = getLessonDetails(monitorTest.getGetLessonDetailsURL(), monitorTest.getLsId());
			getContributeActivities(monitorTest.getGetContributeActivitiesURL(), monitorTest.getLsId());
			getLearningDesignDetails(monitorTest.getGetLearningDesignDetailsURL(), ldId);
			while (stopSignal == null) {
				delay();
				log.info(username+" is refreshing all learners progress");
				getAllLearnersProgress(monitorTest.getGetAllLearnersProgressURL(), monitorTest.getLsId());
				log.info(username+" got the latest learners progress");
			}
			log.info(username+" stopped monitoring");
			stopSignal.countDown();
		} catch (Exception e) {
			log.info(username+" aborted on monitoring due to some exception");
			log.debug(e.getMessage(), e);
		}
	}

	public final void setStopFlag(CountDownLatch stopSignal) {
		this.stopSignal = stopSignal;
	}

}
