/* ********************************************************************************
 *  Copyright Notice
 *  =================
 * This file contains propriety information of LAMS Foundation. 
 * Copying or reproduction with prior written permission is prohibited.
 * Copyright (c) 2005 
 * Created on 9/02/2005
 ******************************************************************************** */

package org.lamsfoundation.lams.tool.dao;

import java.util.List;

import org.lamsfoundation.lams.tool.BasicToolVO;
import org.lamsfoundation.lams.tool.ToolDataAccessTestCase;


/**
 * 
 * @author Jacky Fang 9/02/2005
 * updated: Ozgur Demirtas 24/06/2005
 * 
 */
public class TestToolDAO extends ToolDataAccessTestCase
{

    /*
     * @see ToolDataAccessTestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
    }

    /*
     * @see ToolDataAccessTestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    /**
     * Constructor for TestToolDAO.
     * @param arg0
     */
    public TestToolDAO(String arg0)
    {
        super(arg0);
    }

    public void testGetToolBySignature()
    {
    	BasicToolVO testTool = toolDao.getToolBySignature("voting_signature");
        assertEquals(new Long(9),testTool.getToolId());
    }

    public void testGetToolByID()
    {
        testTool = toolDao.getToolByID(super.TEST_TOOL_ID);
        assertEquals("verify signature","survey_signature",testTool.getToolSignature());
        assertEquals("verify service name","surveyService",testTool.getServiceName());
        assertEquals("verify display name","Survey",testTool.getToolDisplayName());
        assertEquals("verify default content id",6,testTool.getDefaultToolContentId());
        assertTrue("verify support define later",testTool.getSupportsDefineLater());
    }

    public void testGetAllTools(){
    	List list = toolDao.getAllTools();
    	assertNotNull(list);    	
    }

    
}
