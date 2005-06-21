/* ********************************************************************************
 *  Copyright Notice
 *  =================
 * This file contains propriety information of LAMS Foundation. 
 * Copying or reproduction with prior written permission is prohibited.
 * Copyright (c) 2004 
 * Created on 2004-12-23
 ******************************************************************************** */

package org.lamsfoundation.lams.tool.qa;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;


/*
 * 
 * @author ozgurd
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * Test case for TestQaSession
 */

public class TestQaSession extends QaDataAccessTestCase
{
	
	protected final long DEVELOPMENT_TOOL_SESSION_ID= 1160776126064221551L;
	protected void setUp() throws Exception
    {
        super.setUp();
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    public TestQaSession(String name)
    {
        super(name);
    }
    
    protected long getTestQaContentID()
    {
        return this.TEST_EXISTING_CONTENT_ID;
    }
    
    /*
    public void testGetExistingSession()
    {
    	QaContent qa = qaContentDAO.getQaById(getTestQaContentID());
    	System.out.println(this.getClass().getName() + " gets: " + qa);
    }
    */
    
    /*
    public void testCreateNewSession()
    {
    	QaContent qa = qaContentDAO.getQaById(getTestQaContentID());

        qaSession = new QaSession(new Long(TEST_NEW_SESSION_ID),
                                   new Date(System.currentTimeMillis()),
                                   new Date(System.currentTimeMillis()+ ONE_DAY),
                                   this.NOT_ATTEMPTED, 
                                   qa,
                                   new TreeSet());

    
        qaSessionDAO.CreateQaSession(qaSession);
        System.out.println(this.getClass().getName() + " New session created: " + qaSession);
     
        qaSessionDAO.deleteQaSession(qaSession);
        System.out.println(this.getClass().getName() + " New session deleted: ");
    }
    */
    
    public void testDevToolSession()
    {
    	QaSession qaSession=qaSessionDAO.getQaSessionById(this.DEVELOPMENT_TOOL_SESSION_ID);
    	//System.out.println(this.getClass().getName() + " Retrieved session : " + qaSession);
    	
    	QaContent qaContent=qaSession.getQaContent();
    	//System.out.println(this.getClass().getName() + " \nSession has content id : " + qaContent);
    	
    	Map mapQuestions= new TreeMap(new QaComparator());
    	
    	//get iterator of questions collection
    	Iterator contentIterator=qaContent.getQaQueContents().iterator();
    	while (contentIterator.hasNext())
    	{
    		QaQueContent qaQueContent=(QaQueContent)contentIterator.next();
    		System.out.println(this.getClass().getName() + "\n question : " + qaQueContent.getDisplayOrder() + " " + qaQueContent.getQuestion());
    		String displayOrder= new Integer(qaQueContent.getDisplayOrder()).toString();
    		mapQuestions.put(displayOrder,qaQueContent.getQuestion());
    	}
    	System.out.println(this.getClass().getName() + "\n mapQuestions is ready" + mapQuestions);
    	
    	
    	Iterator itMap = mapQuestions.entrySet().iterator();
        int diplayOrder=0;
        while (itMap.hasNext()) 
	    {
	        Map.Entry pairs = (Map.Entry)itMap.next();
	        System.out.println(this.getClass().getName() + "\n pairs.key: " + pairs.getKey());
	    }
    	
    	
    	
    	
    	
    	
    	
    	
    }
    

}