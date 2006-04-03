/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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
/* $$Id$$ */
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
