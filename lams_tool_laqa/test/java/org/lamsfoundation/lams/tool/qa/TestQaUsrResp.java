/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
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
    	QaQueContent qaQueContent = qaQueContentDAO.getQaQueById(new Long(1).longValue());
    	
    	QaQueUsr qaQueUsr=qaQueUsrDAO.getQaQueUsrById(new Long(700).longValue()); 
    	
		QaUsrResp qaUsrResp= new QaUsrResp("I am from Sydney.",false,
											new Date(System.currentTimeMillis()),
											"",
											qaQueContent,
											qaQueUsr);
		qaUsrRespDAO.createUserResponse(qaUsrResp);
    }
    
}
