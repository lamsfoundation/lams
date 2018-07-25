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

import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;
import org.lamsfoundation.testharness.admin.AdminTest;
import org.lamsfoundation.testharness.author.AuthorTest;
import org.lamsfoundation.testharness.learner.LearnerTest;
import org.lamsfoundation.testharness.monitor.MonitorTest;

/**
 * @author Fei Yang, Marcin Cieslak
 */
public class TestSuite implements Runnable {

    private static final Logger log = Logger.getLogger(TestSuite.class);

    private TestManager manager;
    private int suiteIndex;
    private String targetServer;
    private String contextRoot;
    private String cookieDomain;

    private AdminTest adminTest;
    private AuthorTest authorTest;
    private MonitorTest monitorTest;
    private LearnerTest learnerTest;
    private boolean finished = false;

    public TestSuite(TestManager manager, int suiteIndex, String targetServer, String contextRoot, AdminTest adminTest,
	    AuthorTest authorTest, MonitorTest monitorTest, LearnerTest learnerTest) {
	this.manager = manager;
	this.suiteIndex = suiteIndex;
	this.targetServer = targetServer == null ? "localhost" : targetServer;
	this.contextRoot = (contextRoot == null) || contextRoot.equals("/") ? "" : contextRoot;

	int beginIndex = this.targetServer.lastIndexOf("/") + 1;
	int endIndex = this.targetServer.lastIndexOf(":");
	this.cookieDomain = endIndex < 0 ? this.targetServer.substring(beginIndex)
		: this.targetServer.substring(beginIndex, endIndex);

	this.adminTest = adminTest;
	adminTest.setTestSuite(this);
	this.authorTest = authorTest;
	authorTest.setTestSuite(this);
	this.monitorTest = monitorTest;
	monitorTest.setTestSuite(this);
	this.learnerTest = learnerTest;
	learnerTest.setTestSuite(this);
    }

    /**
     * @return Returns the adminTest.
     */
    public final AdminTest getAdminTest() {
	return adminTest;
    }

    /**
     * @return Returns the authorTest.
     */
    public final AuthorTest getAuthorTest() {
	return authorTest;
    }

    public final String getContextRoot() {
	return contextRoot;
    }

    public String getCookieDomain() {
	return cookieDomain;
    }

    /**
     * @return Returns the learnerTest.
     */
    public final LearnerTest getLearnerTest() {
	return learnerTest;
    }

    public final TestManager getManager() {
	return manager;
    }

    /**
     * @return Returns the monitorTest.
     */
    public final MonitorTest getMonitorTest() {
	return monitorTest;
    }

    public final int getSuiteIndex() {
	return suiteIndex;
    }

    /**
     * @return Returns the targetServer.
     */
    public final String getTargetServer() {
	return targetServer;
    }

    public final boolean isFinished() {
	return finished;
    }

    /**
     * The order is important, not to be changed
     */
    @Override
    public void run() {
	try {
	    TestSuite.log.info("Starting test suite " + suiteIndex);
	    if (adminTest != null) {
		adminTest.start();
	    }
	    if (authorTest != null) {
		authorTest.start();
	    }
	    if (monitorTest != null) {
		monitorTest.start();
	    }
	    if ((learnerTest != null) && (monitorTest != null)) {
		learnerTest.start();
	    }
	    if (monitorTest != null) {
		CountDownLatch stopSignal = new CountDownLatch(1);
		monitorTest.notifyMonitorToStop(stopSignal);
		stopSignal.await();
	    }
	    finished = true;
	    TestSuite.log.info("Finished test suite " + suiteIndex);
	} catch (Exception e) {// All the exceptions which happened during test stop propagation here
	    TestSuite.log.error("Test suite " + suiteIndex + " aborted", e);
	    // Exception is not propagated so that other testsuite will not be affected
	} finally {
	    manager.allDoneSignal.countDown();
	}
    }

    public final void setContextRoot(String contextRoot) {
	this.contextRoot = contextRoot;
    }

    @Override
    public String toString() {
	return "suiteIndex:" + suiteIndex + " targetServer:" + targetServer;
    }
}