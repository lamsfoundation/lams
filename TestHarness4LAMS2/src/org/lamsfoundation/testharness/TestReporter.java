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

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.lamsfoundation.testharness.Call.CallRecord;
import org.lamsfoundation.testharness.learner.LearnerTest;

/**
 * @version
 *
 * <p>
 * <a href="TestReporter.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class TestReporter {
    
	private static final Logger log = Logger.getLogger(TestReporter.class);
    private static List<CallRecord> callRecords = new LinkedList<CallRecord>();
    private static String fileName;
    private static String fileTemplate;

    public static boolean initialized(){
    	return ((fileName!=null)&&(fileTemplate!=null));
    }
    
    public static List<CallRecord> getCallRecords() {
		return callRecords;
	}


	public static void setCallRecords(List<CallRecord> callRecords) {
		TestReporter.callRecords = callRecords;
	}


	public static String getFileName() {
		return fileName;
	}


	public static void setFileName(String fileName) {
		TestReporter.fileName = fileName;
	}


	public static String getFileTemplate() {
		return fileTemplate;
	}


	public static void setFileTemplate(String fileTemplate) {
		TestReporter.fileTemplate = fileTemplate;
	}


	public static synchronized void addCallRecord(CallRecord callRecord){
        callRecords.add(callRecord);
    }

	public static void report(AbstractTestManager manager) {
		final String NEW_LINE = "\n* ";
		final String NEW_LINE_INDENT = "\n*   ";
		StringBuilder report = new StringBuilder();
		report.append("See below\n\n**********************************Brief Report*******************************************");
		
		//disclaimer
		report.append(NEW_LINE);
		report.append(NEW_LINE).append("Disclaimer:");
		report.append(NEW_LINE_INDENT).append("This program is created in the hope that it will help estimate how many concurrent");
		report.append(NEW_LINE_INDENT).append("users a LAMS 2.x server can handle, but WITHOUT ANY GARANTEE  the server can support");
		report.append(NEW_LINE_INDENT).append("that number of users in service use.");
		report.append(NEW_LINE);
		report.append(NEW_LINE_INDENT).append("This program is more a load test tool than a functional test tool, ");
		report.append(NEW_LINE_INDENT).append("so it does NOT GARANTEE there is no functional bug in the target server.");
		report.append(NEW_LINE);
		
		report.append(NEW_LINE).append("Test Result Summary:");
		report.append(NEW_LINE_INDENT).append(manager.testSuites.size()).append(" test suite(s) launched. ");
		report.append(manager.testSuites.size()-manager.countAborted()).append(" test suite(s) finished, and ").append(manager.countAborted()).append(" test suite(s) aborted.");
		for (TestSuite testSuite : manager.testSuites){
			report.append(NEW_LINE_INDENT).append("Test Suite ").append(testSuite.getSuiteIndex()).append(testSuite.isFinished()? " finished" : " aborted").append(", in which");
			report.append(NEW_LINE_INDENT);
			AbstractTest[] tests = new AbstractTest[]{testSuite.getAdminTest(),testSuite.getAuthorTest(),testSuite.getMonitorTest(),testSuite.getLearnerTest()};
			boolean first = true;
			for(AbstractTest test : tests){
				if(test!=null){
					if(!first)
						report.append(", ");
					report.append(test.getTestName()).append(test.isFinished()? " finished" : " aborted");
					first = false;
				}
			}
			report.append(NEW_LINE_INDENT);
			LearnerTest learnerTest = testSuite.getLearnerTest();
			report.append("In ").append(learnerTest.getTestName()).append(", ");
			report.append(learnerTest.countLearners()).append(" learner(s) attended, "); 
			report.append(learnerTest.countLearners()-learnerTest.countAborted()).append(" finished and ");
			report.append(learnerTest.countAborted()).append(" aborted.");
			report.append(NEW_LINE);
		}
		report.append(NEW_LINE).append("Refer to the formal test report document for the details.");
		report.append(NEW_LINE).append("\n*****************************************************************************************\n");
		log.info(report.toString());
	}
	
	public static void generateReport(AbstractTestManager manager) {
		report(manager);
		log.info("Generating the formal test report document...");
		//TODO implement me
		log.info("Sorry, this feature is not ready yet. It should come soon.");
	}

}
