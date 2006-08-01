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
package org.lamsfoundation.testharness;

import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;
import org.lamsfoundation.testharness.admin.AdminTest;
import org.lamsfoundation.testharness.author.AuthorTest;
import org.lamsfoundation.testharness.learner.LearnerTest;
import org.lamsfoundation.testharness.monitor.MonitorTest;

/**
 * @version
 *
 * <p>
 * <a href="TestSuite.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class TestSuite implements Runnable {

	private static final Logger log = Logger.getLogger(TestSuite.class);
	
	private AbstractTestManager manager;
	private int suiteIndex;
	private String targetServer;
	private String contextRoot;
	private Integer rmiRegistryServicePort;
	private Integer httpPort;
	private AdminTest adminTest;
	private AuthorTest authorTest;
	private MonitorTest monitorTest;
	private LearnerTest learnerTest;
	private boolean finished = false;

	public TestSuite(AbstractTestManager manager, int suiteIndex, String targetServer, String contextRoot, Integer rmiRegistryServicePort, Integer httpPort, AdminTest adminTest, AuthorTest authorTest, MonitorTest monitorTest,
			LearnerTest learnerTest) {
		this.manager = manager;
		this.suiteIndex = suiteIndex;
		this.targetServer = targetServer == null ? "localhost" : targetServer;
		this.contextRoot = contextRoot == null || contextRoot.equals("/") ? "" : contextRoot;
		this.rmiRegistryServicePort = rmiRegistryServicePort == null ? 1099 : rmiRegistryServicePort;
		this.httpPort = httpPort == null ? 80 : httpPort;
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
	 * The order is important, not to be changed
	 */
	public void run() {
		try{
			log.info("Starting test suite " + suiteIndex + "...");
			if (adminTest != null)
				adminTest.start();
			if (authorTest != null)
				authorTest.start();
			if (monitorTest != null)
				monitorTest.start();
			if ((learnerTest != null)&&(monitorTest != null))
				learnerTest.start();
			if(monitorTest != null){
				CountDownLatch stopSignal = new CountDownLatch(1);
				monitorTest.notifyMonitorToStop(stopSignal);
				stopSignal.await();
			}
			finished = true;
			log.info("Finished test suite "+suiteIndex);
		} catch(Exception e) {//All the exceptions which happened during test stop propagation here
			log.debug(e.getMessage(),e);
			log.info("Test suite " + suiteIndex + " aborted");
			//Exception is not propagated so that other testsuite will not be affected
		}finally{
			manager.allDoneSignal.countDown();
		}
	}

	public final int getSuiteIndex() {
		return suiteIndex;
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

	/**
	 * @return Returns the httpPort.
	 */
	public final int getHttpPort() {
		return httpPort;
	}

	/**
	 * @return Returns the learnerTest.
	 */
	public final LearnerTest getLearnerTest() {
		return learnerTest;
	}

	/**
	 * @return Returns the rmiRegistryServicePort.
	 */
	public final int getRmiRegistryServicePort() {
		return rmiRegistryServicePort;
	}

	/**
	 * @return Returns the monitorTest.
	 */
	public final MonitorTest getMonitorTest() {
		return monitorTest;
	}

	/**
	 * @return Returns the targetServer.
	 */
	public final String getTargetServer() {
		return targetServer;
	}

	public final String getContextRoot() {
		return contextRoot;
	}

	public final void setContextRoot(String contextRoot) {
		this.contextRoot = contextRoot;
	}
	
	public String toString(){
		return "suiteIndex:"+suiteIndex+" targetServer:"+targetServer;
	}

	public final AbstractTestManager getManager() {
		return manager;
	}

	public final boolean isFinished() {
		return finished;
	}
}
