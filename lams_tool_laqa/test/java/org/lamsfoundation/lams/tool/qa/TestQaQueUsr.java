/***************************************************************************
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
 * ***********************************************************************/

/* $$Id$$ */
package org.lamsfoundation.lams.tool.qa;

import java.util.TreeSet;


/**
 * This test is designed to test all service provided by SurveyQueUsr domain 
 * class
 * @author ozgurd
 * 
 */

/**
 * Test case for TestQaQueUsr
 */

public class TestQaQueUsr extends QaDataAccessTestCase
{
        protected void setUp() throws Exception
    {
        super.setUp();
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();

    }

    
    public TestQaQueUsr(String name)
    {
        super(name);
    }
    
    
    public void testCreateNewUser()
    {
    	QaQueContent qaQueContent = qaQueContentDAO.getQaQueById(new Long(1).longValue() );
		QaSession qaSession = qaSessionDAO.getQaSessionById(new Long(102).longValue());
    	
        QaQueUsr qaQueUsr= new QaQueUsr(new Long(TEST_NEW_USER_ID),
    									"john",
										"John Baker",
										qaQueContent, 
										qaSession, 
										new TreeSet());
    	qaQueUsrDAO.createUsr(qaQueUsr);
    }
	
    
}
