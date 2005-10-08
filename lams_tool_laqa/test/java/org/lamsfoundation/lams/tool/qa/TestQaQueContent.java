/* ********************************************************************************
 *  Copyright Notice
 *  =================
 * This file contains propriety information of LAMS Foundation. 
 * Copying or reproduction with prior written permission is prohibited.
 * Copyright (c) 2004 
 * Created on 2004-12-23
 ******************************************************************************** */

package org.lamsfoundation.lams.tool.qa;
/*
 * 
 * @author ozgurd
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * Test case for QaQueContent
 */

public class TestQaQueContent extends QaDataAccessTestCase
{
	protected void setUp() throws Exception
    {
        super.setUp();
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    public TestQaQueContent(String name)
    {
        super(name);
    }
    
/*    
    public void testCreateQaQueContent()
    {
    	QaContent qaContent = qaContentDAO.getQaById(DEFAULT_CONTENT_ID);	
    	System.out.println(this.getClass().getName() + "qa content: " + qaContent);
    	
    	QaQueContent qaQueContent=  new QaQueContent("ozgur's new question test", 
    													1, 
														qaContent,
														null,
														null);
    	   	
    	System.out.println(this.getClass().getName() + " qaQueContent: " + qaQueContent);
    	qaQueContentDAO.createQueContent(qaQueContent);
    	System.out.println(this.getClass().getName() + " qaQueContent created: ");
    }
 */
 
   /* 
    public void testCreateQaQueContentMap()
    {
    	Map mapQuestionContent= new TreeMap();
    	mapQuestionContent.put(new Long(1).toString(), "questionEntry -1");
    	mapQuestionContent.put(new Long(2).toString(), "questionEntry -2");
    	
    	QaContent qaContent = qaContentDAO.getQaById(DEFAULT_CONTENT_ID);	
    	System.out.println(this.getClass().getName() + "qa content: " + qaContent);
    	
    	
    	Iterator itMap = mapQuestionContent.entrySet().iterator();
	    while (itMap.hasNext()) 
	    {
	        Map.Entry pairs = (Map.Entry)itMap.next();
	        
	        QaQueContent queContent=  new QaQueContent(pairs.getValue().toString(), 
	        											new Long(pairs.getKey().toString()).intValue(),
	        											qaContent,
														null,
														null);
		
	        queContent.setQaContent(qaContent);
	        qaContent.getQaQueContents().add(queContent);
	        qaContentDAO.createQa(qaContent);
	        System.out.println(this.getClass().getName() + " queContent: " + queContent);
	    }
    }
  */  
    public void testCreateDefaultQaQueContent()
    {
    	QaQueContent defaultQaQueContent = qaQueContentDAO.getQaQueById(TEST_EXISTING_QUE_CONTENT_ID);	
    	System.out.println(this.getClass().getName() + " Default qa que content: " + defaultQaQueContent);
    }
    
    
    
    
    
}