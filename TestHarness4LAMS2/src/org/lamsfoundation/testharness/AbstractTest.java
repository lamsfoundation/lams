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

import org.apache.log4j.Logger;
import org.lamsfoundation.testharness.Call.CallType;


/**
 * @version
 *
 * <p>
 * <a href="AbstractTest.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public abstract class AbstractTest
{

	private static final Logger log = Logger.getLogger(AbstractTest.class);
	
    protected TestSuite testSuite;
    protected final String testName;
    protected final CallType callType;
    protected String rmiRegistryName;
    protected String webServiceAddress;
    protected final Integer minDelay;
    protected final Integer maxDelay;
    protected MockUser[] users;
    protected boolean finished = false;

    protected AbstractTest(String name, CallType type, String rmiRegistryName, String address, Integer minDelay, Integer maxDelay) {
        this.testName = name;
        this.callType = type;
        this.rmiRegistryName = rmiRegistryName;
        this.webServiceAddress = address;
        this.minDelay = minDelay==null? 0 : minDelay;
        this.maxDelay = maxDelay==null? 0 : maxDelay;
    }

    public final void start(){
    	try{
    		log.info("Starting "+testName+"...");
			switch (callType) {
				case WEB: 
					startWEB();
					break;
				case WS:
					startWS();
					break;
				case RMI:
					startRMI();
					break;
				default:
					break;
			}
			finished = true;
			log.info(testName+" is finished");
    	}catch(RuntimeException e){
    		log.info(testName+" aborted");
    		//Since latter tests depend on precedent tests in a test suite, 
    		//propagate the exception to let the test suite stop executing the latter tests
    		throw e;
    	}
    }

    protected abstract void startWEB();

    protected abstract void startWS();

    protected abstract void startRMI();

    /**
     * @return Returns the testSuite.
     */
    public final TestSuite getTestSuite() {
        return testSuite;
    }

    /**
     * @param testSuite The testSuite to set.
     */
    public final void setTestSuite(TestSuite testSuite) {
        this.testSuite = testSuite;
    }

	public final MockUser[] getUsers() {
		return users;
	}

	public final void setUsers(MockUser[] users) {
		this.users = users;
	}

	public final CallType getCallType() {
		return callType;
	}

	public final String getRmiRegistryName() {
		return rmiRegistryName;
	}

	public final String getTestName() {
		return testName;
	}

	public final String getWebServiceAddress() {
		return webServiceAddress;
	}

	public final Integer getMaxDelay() {
		return maxDelay;
	}

	public final Integer getMinDelay() {
		return minDelay;
	}

	public final boolean isFinished() {
		return finished;
	}

}
