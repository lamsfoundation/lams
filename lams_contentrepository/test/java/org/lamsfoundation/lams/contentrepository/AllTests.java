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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $$Id$$ */	
package org.lamsfoundation.lams.contentrepository;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.lamsfoundation.lams.contentrepository.dao.file.TestFileDAO;
import org.lamsfoundation.lams.contentrepository.service.TestSimpleTicket;
import org.lamsfoundation.lams.contentrepository.service.TestSimpleVersionedNode;
import org.lamsfoundation.lams.contentrepository.service.TestSimpleRepository;

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