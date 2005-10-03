/* ********************************************************************************
 *  Copyright Notice
 *  =================
 * This file contains propriety information of LAMS Foundation. 
 * Copying or reproduction with prior written permission is prohibited.
 * Copyright (c) 2004 
 * Created on 2004-12-20
 ******************************************************************************** */

package org.lamsfoundation.lams.tool.qa;

import java.util.Date;


/**
 * This test is designed to test all service provided by SurveyQueUsr domain 
 * class
 * @author ozgurd
 * 
 */

/**
 * Test case for TestQaUsrResp
 */

public class TestQaUsrResp extends QaDataAccessTestCase
{
    protected void setUp() throws Exception
    {
        super.setUp();
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();

    }

    
    public TestQaUsrResp(String name)
    {
        super(name);
    }
    
    
    public void testCreateNewResponse()
    {
    	QaQueContent qaQueContent = qaQueContentDAO.getQaQueById(TEST_EXISTING_QUE_CONTENT_ID);
    	
		QaUsrResp qaUsrResp= new QaUsrResp("I am from Mars",false,
											new Date(System.currentTimeMillis()),
											"",
											qaQueContent,
											getExistingUser("randomstriker","Michael Random"));
		qaUsrRespDAO.createUserResponse(qaUsrResp);
    }
    
}
