/* ********************************************************************************
 *  Copyright Notice
 *  =================
 * This file contains propriety information of LAMS Foundation. 
 * Copying or reproduction with prior written permission is prohibited.
 * Copyright (c) 2004 
 * Created on 2004-12-20
 ******************************************************************************** */

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
