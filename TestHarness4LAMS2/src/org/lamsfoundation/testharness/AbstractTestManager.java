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

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;

/**
 * @version
 *
 * <p>
 * <a href="AbstractTestManager.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public abstract class AbstractTestManager {

    private static final Logger log = Logger.getLogger(AbstractTestManager.class);
    
    protected List<TestSuite> testSuites = new LinkedList<TestSuite>();
    
    protected static final Integer MAX_USERNAME_LENGTH = 20;
    
    protected CountDownLatch allDoneSignal;
    
    /**
     * This is where test manager initializes the test suites.
     * Must set TestReport's reportFileName and reportFileTemplate here.
     */
    protected abstract void init();

    protected final void kickOff(){
    	init();
    	if(!TestReporter.initialized()){
    		throw new TestHarnessException("TestReport class is not initialized! It should be a bug in the AbstractTestManager implementation");
    	}
        log.info(composeStartInfo());
        for (int i = 0; i < testSuites.size(); i++) {
            new Thread(testSuites.get(i),"TestSuite-"+testSuites.get(i).getSuiteIndex()).start();
        }
        if(testSuites.size()>0){
        	allDoneSignal = new CountDownLatch(testSuites.size());
        	try{
        		allDoneSignal.await();
        	}catch(InterruptedException e){
        		log.fatal(e.getMessage(), e);
        		//what to do?
        	}
        }
        log.info(composeEndInfo());
    }

    private String composeStartInfo() {
        switch (testSuites.size()) {
        case 0:
            return "There is no test suite found on the list";
        case 1:
            return "Kicking off the test suite...";
        default:
            return "Kicking off the " + testSuites.size() + " test suites...";
        }
    }

    private String composeEndInfo() {
        switch (testSuites.size()) {
        case 0:
            return "So we can play the game now";
        case 1:
        	if(countAborted()==0)
        		return "The only test suite is finished";
        	else
        		return "The only test suite aborted";
        case 2:
        	switch(countAborted()){
        	case 0:
        		return "Both the test suites are finished";
        	case 1:
        		return "One test suite is finished, and the other aborted";
        	case 2:
        		return "Both the test suites aborted";
        	default:
        		return "Impossible! "+countAborted()+" out of "+testSuites.size()+" testSuites aborted!";	
        	}
        default:
        	if(countAborted() == 0)
        		return "All the test suites are finished";
        	else if(countAborted() == testSuites.size())
        		return "All the test suites aborted";
        	else{
        		int abortedCounter = countAborted();
        		int finishedCounter = testSuites.size()-countAborted(); 
        		return finishedCounter+" test suite"+(finishedCounter>1? "s are finished and " : " is finished and ")
        			+ abortedCounter + "test suite"+(abortedCounter>1? "s aborted":" aborted");
        	}
        }
    }

 	protected final void addTestSuite(TestSuite suite) {
        testSuites.add(suite);
    }

 	protected final int countAborted(){
 		int amount = 0;
 		for(TestSuite suite : testSuites){
 			if (!suite.isFinished())
 				amount++;
 		}
 		return amount;
 	}
}
