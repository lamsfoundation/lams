/*
 * Created on Jan 11, 2005
 */
package org.lamsfoundation.lams.contentrepository;

import org.lamsfoundation.lams.contentrepository.dao.file.TestFileDAO;

import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * Runs all the currently implemented tests for the Content Repository.
 * Before running, load the test data in the database, clear
 * the repository and check that the files listed in BaseTestCase
 * exist.
 * 
 * @author Fiona Malikoff
 */
public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
			"Test for org.lamsfoundation.lams.contentrepository");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestSimpleTicket.class);
		suite.addTestSuite(TestSimpleVersionDetail.class);
		suite.addTestSuite(TestSimpleVersionedNode.class);
		suite.addTestSuite(TestSimpleRepository.class);
		suite.addTestSuite(TestFileDAO.class);
		//$JUnit-END$
		return suite;
	}
}