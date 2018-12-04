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

import java.util.concurrent.CountDownLatch;

import org.lamsfoundation.testharness.AbstractTest;
import org.lamsfoundation.testharness.TestUtil;

/**
 * <p>
 * <a href="MonitorTest.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class MonitorTest extends AbstractTest {

    public static final String DEFAULT_LESSON_NAME = "Lesson";

    private String initLessonURL;
    private String createLessonClassURL;
    private String startLessonURL;
    private String getAllLearnersProgressURL;
    private String lessonName;
    private String lsId;
    private Thread monitorThread;

    /**
     * MonitorTest Construtor
     */
    public MonitorTest(String testName, Integer minDelay, Integer maxDelay, String initLessonURL,
	    String createLessonClassURL, String startLessonURL, String getAllLearnersProgressURL, String lessonName,
	    String lsId) {
	super(testName, minDelay, maxDelay);
	this.initLessonURL = initLessonURL;
	this.createLessonClassURL = createLessonClassURL;
	this.startLessonURL = startLessonURL;
	this.getAllLearnersProgressURL = getAllLearnersProgressURL;
	this.lessonName = lessonName == null ? TestUtil.buildName(testName, MonitorTest.DEFAULT_LESSON_NAME)
		: TestUtil.buildName(testName, lessonName);
	this.lsId = lsId;
    }

    @Override
    protected void startTest() {
	MockMonitor monitor = (MockMonitor) users[0];
	if (lsId == null) {
	    monitor.login();
	    String organisationID = getTestSuite().getAdminTest().getCourseId();
	    String ldId = getTestSuite().getAuthorTest().getLdId();
	    setLsId(monitor.initLesson(initLessonURL, ldId, organisationID, monitor.getUserId(), lessonName));
	    monitor.createLessonClass(createLessonClassURL, monitor.getUserId());
	    monitor.startLesson(startLessonURL, lsId, monitor.getUserId());
	}
	// monitor learners progress
	monitorThread = new Thread(monitor, monitor.getUsername());
	monitorThread.start();
    }

    public void notifyMonitorToStop(CountDownLatch stopSignal) {
	if ((monitorThread != null) && monitorThread.isAlive()) {
	    ((MockMonitor) users[0]).setStopFlag(stopSignal);
	} else {
	    stopSignal.countDown();
	}
    }

    public final String getCreateLessonClassURL() {
	return createLessonClassURL;
    }

    public final void setCreateLessonClassURL(String createLessonClassURL) {
	this.createLessonClassURL = createLessonClassURL;
    }

    public final String getInitLessonURL() {
	return initLessonURL;
    }

    public final void setInitLessonURL(String initLessonURL) {
	this.initLessonURL = initLessonURL;
    }

    public final String getStartLessonURL() {
	return startLessonURL;
    }

    public final void setStartLessonURL(String startLessonURL) {
	this.startLessonURL = startLessonURL;
    }

    public final String getLsId() {
	return lsId;
    }

    public final void setLsId(String lsId) {
	this.lsId = lsId;
    }

    public final String getGetAllLearnersProgressURL() {
	return getAllLearnersProgressURL;
    }
}