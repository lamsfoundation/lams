/* ********************************************************************************
 *  Copyright Notice
 *  =================
 * This file contains propriety information of LAMS Foundation. 
 * Copying or reproduction with prior written permission is prohibited.
 * Copyright (c) 2005 
 * Created on 2005-1-10
 ******************************************************************************** */

package org.lamsfoundation.lams.util;

import javax.servlet.http.HttpSession;

import org.lamsfoundation.lams.tool.ToolAccessMode;

import servletunit.HttpServletRequestSimulator;
import servletunit.struts.MockStrutsTestCase;


/**
 * 
 * @author Jacky Fang 2005-1-10
 * 
 */
public class TestWebUtil extends MockStrutsTestCase 
{
    HttpServletRequestSimulator req;
    HttpSession session;
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        req = (HttpServletRequestSimulator)getRequest();
        session = getSession();   
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    /**
     * Constructor for TestWebUtil.
     * @param name
     */
    public TestWebUtil(String name)
    {
        super(name);
    }

    public void testReadToolAccessModeParam()
    {
        addRequestParameter("mode", ToolAccessMode.LEARNER.toString());
        ToolAccessMode mode = WebUtil.readToolAccessModeParam(req,"mode",false);
        assertTrue("Validating the reading of tool access mode",mode==ToolAccessMode.LEARNER);
    }

    public void testReadIllegalToolAccessModeParam()
    {
        addRequestParameter("mode", "IllegalMode");
        try
        {
            ToolAccessMode mode = WebUtil.readToolAccessModeParam(req,"mode",false);
            fail("IllegalArgumentException expected");
        }
        catch (RuntimeException e)
        {
            if (e instanceof IllegalArgumentException)
                assertTrue(true);
            else
                fail("IllegalArgumentException expected");
        }
    }
    
    public void testReadNullToolAccessModeParam()
    {
        try
        {
            ToolAccessMode mode = WebUtil.readToolAccessModeParam(req,"mode",false);
            fail("IllegalArgumentException expected");
        }
        catch (RuntimeException e)
        {
            if (e instanceof IllegalArgumentException)
                assertTrue(true);
            else
                fail("IllegalArgumentException expected");
        }
    }
    
    public void testGetStrutsForwardNameFromPath()
    {
        String name = WebUtil.getStrutsForwardNameFromPath("/DisplayParallelActivity.do");
        assertEquals("displayParallelActivity",name);

        name = WebUtil.getStrutsForwardNameFromPath("/DisplayOptionsActivity.do");
        assertEquals("displayOptionsActivity",name);
        
        name = WebUtil.getStrutsForwardNameFromPath("/LoadToolActivity.do");
        assertEquals("loadToolActivity",name);
        
        name = WebUtil.getStrutsForwardNameFromPath("/parallelWait.do");
        assertEquals("parallelWait",name);

        name = WebUtil.getStrutsForwardNameFromPath("/lessonComplete.do");
        assertEquals("lessonComplete",name);
        
        name = WebUtil.getStrutsForwardNameFromPath("/requestDisplay.do");
        assertEquals("requestDisplay",name);
        
        name = WebUtil.getStrutsForwardNameFromPath("/TestDispaly.do?url=11&id=2");
        assertEquals("testDispaly",name);
    }
}
