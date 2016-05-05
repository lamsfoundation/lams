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

import org.apache.log4j.Logger;

/**
 * @author Fei Yang, Marcin Cieslak
 */
public abstract class AbstractTest {
    private static final Logger log = Logger.getLogger(AbstractTest.class);

    protected final String testName;
    protected final Integer minDelay;
    protected final Integer maxDelay;

    protected TestSuite testSuite;
    protected MockUser[] users;
    protected boolean finished = false;

    protected AbstractTest(String name, Integer minDelay, Integer maxDelay) {
	this.testName = name;
	this.minDelay = minDelay == null ? 0 : minDelay;
	this.maxDelay = maxDelay == null ? 0 : maxDelay;
    }

    public final Integer getMaxDelay() {
	return maxDelay;
    }

    public final Integer getMinDelay() {
	return minDelay;
    }

    public final String getTestName() {
	return testName;
    }

    public final TestSuite getTestSuite() {
	return testSuite;
    }

    public final MockUser[] getUsers() {
	return users;
    }

    public final boolean isFinished() {
	return finished;
    }

    public final void setTestSuite(TestSuite testSuite) {
	this.testSuite = testSuite;
    }

    public final void setUsers(MockUser[] users) {
	this.users = users;
    }

    protected final void start() {
	try {
	    AbstractTest.log.info("Starting " + testName);
	    startTest();
	    finished = true;
	    AbstractTest.log.info(testName + " is finished");
	} catch (RuntimeException e) {
	    AbstractTest.log.info(testName + " aborted");
	    // Since latter tests depend on precedent tests in a test suite,
	    // propagate the exception to let the test suite stop executing the latter tests
	    throw e;
	}
    }

    protected abstract void startTest();
}