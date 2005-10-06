/* ********************************************************************************
 *  Copyright Notice
 *  =================
 * This file contains propriety information of LAMS Foundation. 
 * Copying or reproduction with prior written permission is prohibited.
 * Copyright (c) 2004 
 * Created on 2004-12-23
 ******************************************************************************** */

package org.lamsfoundation.lams.tool.mc;

import java.util.HashSet;
import java.util.Set;


/*
 * 
 * @author ozgurd
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class TestMcQueContent extends McDataAccessTestCase
{
	protected void setUp() throws Exception
    {
        super.setUp();
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    public TestMcQueContent(String name)
    {
        super(name);
    }
    
    
    public void testCreateMcQueContent()
    {
    	McContent mcContent = mcContentDAO.findMcContentById(new Long(2));
    	System.out.println(this.getClass().getName() + "retrieved mcContent: " + mcContent);
    	
    	McQueContent mcQueContent=  new McQueContent(TEST_QUE_ID1,
													 "How many elephants do you see in the picture?",
													 new Integer(20),
													 mcContent,
													 new HashSet(),
													 new HashSet()
    												);
    	
    	System.out.println(this.getClass().getName() + "retrieving mcQueContent: " + mcQueContent);
    	mcQueContentDAO.saveMcQueContent(mcQueContent);
    	
/*    	
    	mcQueContent.setMcContent(mcContent);
    	mcContent.getMcQueContents().add(mcQueContent);
    	mcQueContentDAO.saveMcQueContent(mcQueContent);
  */
    
    
    }
    
       
   /*
    public void testRetrieveMcQueContent()
    {
    	McQueContent mcQueContent= mcQueContentDAO.findMcQueContentById(new Long(20));
    	mcQueContent.setDisplayOrder(new Integer(88));
    	mcQueContentDAO.saveMcQueContent(mcQueContent);
    }
    */
    
    
}