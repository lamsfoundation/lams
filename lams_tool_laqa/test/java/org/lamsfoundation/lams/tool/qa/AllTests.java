/*
 * Created on 1/04/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool.qa;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author ozgurd
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AllTests {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(AllTests.suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite("QaTestSuite");
		//$JUnit-BEGIN$
		suite.addTestSuite(org.lamsfoundation.lams.tool.qa.TestQaContent.class);
		
		//$JUnit-END$
		return suite;
	}
}