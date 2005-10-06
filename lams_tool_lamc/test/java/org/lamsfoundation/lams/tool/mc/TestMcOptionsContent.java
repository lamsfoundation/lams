/* ********************************************************************************
 *  Copyright Notice
 *  =================
 * This file contains propriety information of LAMS Foundation. 
 * Copying or reproduction with prior written permission is prohibited.
 * Copyright (c) 2004 
 * Created on 2004-12-23
 ******************************************************************************** */

package org.lamsfoundation.lams.tool.mc;

/*
 * 
 * @author ozgurd
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class TestMcOptionsContent extends McDataAccessTestCase
{
	protected void setUp() throws Exception
    {
        super.setUp();
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    public TestMcOptionsContent(String name)
    {
        super(name);
    }
    
    
    public void testCreateMcOptionsContent()
    {
    	McQueContent mcQueContent = mcQueContentDAO.findMcQueContentById(TEST_QUE_ID1);
    	McOptsContent mcOptionsContent= new McOptsContent(new Long(777), true, "red", mcQueContent);
    	mcOptionsContentDAO.saveMcOptionsContent(mcOptionsContent);
    	
    	McOptsContent mcOptionsContent2= new McOptsContent(new Long(888), false, "blue", mcQueContent);
    	mcOptionsContentDAO.saveMcOptionsContent(mcOptionsContent2);
    	
    	McOptsContent mcOptionsContent3= new McOptsContent(new Long(999), false, "yellow", mcQueContent);
    	mcOptionsContentDAO.saveMcOptionsContent(mcOptionsContent3);
    } 
    
    	    
}



