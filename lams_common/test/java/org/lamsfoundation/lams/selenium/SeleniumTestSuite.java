/**************************************************************** 
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org) 
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA 
 * 
 * http://www.gnu.org/licenses/gpl.txt 
 * **************************************************************** 
 */  
 
/* $Id$ */  
package org.lamsfoundation.lams.selenium;

import junit.framework.TestResult;
import junit.framework.TestSuite;

/**
 * Supports Selenium testing process. Allows creation of test suites with fixed test order. 
 * It also tears down selenium server after all test cases were executed.
 * Subclassed from <code>junit.framework.TestSuite</code>. 
 * 
 * @author Andrey Balan
 * @see junit.framework.TestSuite
 */
public class SeleniumTestSuite extends TestSuite {

    public SeleniumTestSuite() {
	super();
    }

    public SeleniumTestSuite(String name) {
	super(name);
    }
    
    /**
     * @param testClass class with test cases
     * @param testSequence ordered sequence of tests. It represents the order in which they will be executed
     */
    public SeleniumTestSuite(Class testClass, String[] testSequence) {
	this(testClass.getName());
	
	for (String testCaseName : testSequence) {
	    addTest(createTest(testClass, testCaseName));
	}
    }

    @Override
    public void run(TestResult result) {
	try {
	    super.run(result);
	} finally {
	    //tear down Selenium after all tests run
	    AbstractSeleniumTestCase.tearDownSelenium();
	}	
    }

}
