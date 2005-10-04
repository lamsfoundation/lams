/* ********************************************************************************
 *  Copyright Notice
 *  =================
 * This file contains propriety information of LAMS Foundation. 
 * Copying or reproduction with prior written permission is prohibited.
 * Copyright (c) 2004 
 * Created on 2004-12-23
 ******************************************************************************** */

package org.lamsfoundation.lams.tool.mc;

import java.text.DateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.lamsfoundation.lams.tool.service.ILamsToolService;



/*
 * 
 * @author ozgurd
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */


public class TestMcContent extends McDataAccessTestCase
{
	public org.lamsfoundation.lams.tool.dao.IToolDAO toolDAO;
	public ILamsToolService lamsToolService;
	
	protected void setUp() throws Exception
    {
        super.setUp();
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    public TestMcContent(String name)
    {
        super(name);
    }
    
   /* 
    public void testCreateNewQaContent()
    {
    	//create new mc content
    	QaContent mc = new QaContent();
		mc.setQaContentId(new Long(TEST_NEW_CONTENT_ID));
		mc.setTitle("New - Put Title Here");
		mc.setInstructions("New - Put instructions here.");
		mc.setCreationDate(new Date(System.currentTimeMillis()));
		mc.setCreatedBy(0);
	    mc.setUsernameVisible(false);
	    mc.setDefineLater(false);
	    mc.setSynchInMonitor(false);
	    mc.setOnlineInstructions("New- online instructions");
	    mc.setOfflineInstructions("New- offline instructions");
	    mc.setReportTitle("New-Report title");
	    mc.setQaQueContents(new TreeSet());
	    mc.setQaSessions(new TreeSet());
	    
	    //create new mc que content
	    QaQueContent mcQueContent = new QaQueContent("What planet are you from",
	    											4, 
													mc, 
													new TreeSet(), 
													new TreeSet());
	    
	    mc.getQaQueContents().add(mcQueContent);
	    
	    //create the new content
	    mcContentDAO.createQa(mc);
	    
	    
        mcSession = new QaSession(new Long(TEST_NEW_SESSION_ID),
                new Date(System.currentTimeMillis()),
                new Date(System.currentTimeMillis()+ ONE_DAY),
                this.NOT_ATTEMPTED, 
				mc,
                new TreeSet());
        
        mcSession.setQaContent(mc);
        
        //create new mc que response
        mcQueContent.getQaUsrResps().add(getNewResponse("I am from Venus", mcQueContent));
        
	    //create a new session with the  new content
        mcSessionDAO.CreateQaSession(mcSession);
        System.out.println(this.getClass().getName() + " New session created: " + mc);
   }
   */
    
    
/*

    public void testCreateDefaultQaContent()
    {
    	QaContent defaultQaContent = mcContentDAO.getQaById(DEFAULT_CONTENT_ID);	
    	System.out.println(this.getClass().getName() + " Default mc content: " + defaultQaContent.getQaQueContents());
    	Iterator queIterator=defaultQaContent.getQaQueContents().iterator();
    	System.out.println(this.getClass().getName() + " \nqueIterator: " + queIterator);
    	while (queIterator.hasNext())
    	{
    		System.out.println(this.getClass().getName() + " \nin loop");
    		QaQueContent mcQueContent=(QaQueContent) queIterator.next();
    		System.out.println(this.getClass().getName() + " \nquestion: " + mcQueContent.getQuestion());
    	}
    }

*/   
    
    
  /*  
    public void testCreateExistingQaContent()
    {
    	QaContent defaultQaContent = mcContentDAO.getQaById(242453535L);
    	if (defaultQaContent == null)
		{
    		System.out.println(this.getClass().getName() + " Default mc content: " + defaultQaContent);
		}
    	else
    	{
    		System.out.println(this.getClass().getName() + " other content: ");
    	}
    }
  */
    
    
    /*
    public void testIdGeneration()
    {
    	System.out.println(this.getClass().getName() + " generated id: " + QaUtils.generateId());
    }
    */
    
    /*
    public void testRemoveNewQaContent()
    {
    	mcContentDAO.removeQa(new Long(TEST_NEW_CONTENT_ID));   
        System.out.println("New content removed: ");
    }
    */

    /*
    public void testRemoveQaContent()
    {
    //	mcQueContentDAO.removeQueContent(TEST_EXISTING_QUE_CONTENT_ID);
    //	System.out.println(this.getClass().getName() + " TEST_EXISTING_QUE_CONTENT_ID removed");
    	
    	mcContentDAO.removeQa(new Long(DEFAULT_CONTENT_ID));
    	System.out.println(this.getClass().getName() + " DEFAULT_CONTENT_ID removed");
    }
    */
 
    /*
    public void testRemoveQaContent()
    {
    	QaContent mcContent = mcContentDAO.loadQaById(TEST_NONEXISTING_CONTENT_ID);
    	System.out.println(this.getClass().getName() + "mcContent loaded : " + mcContent);
    }
    */
    
    public void testTimeZone()
    {
    	TimeZone timeZone=TimeZone.getDefault();
    	System.out.println("timeZone: " + timeZone.getDisplayName());
    }
    
    public void testDateTime()
    {
    	 Date now = new Date();
    	 System.out.println("10. " + DateFormat.getDateTimeInstance(
         DateFormat.LONG, DateFormat.LONG).format(now));	
    }
    
}