/****************************************************************
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
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.tool.dao;

import java.util.List;

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
    	testTool = toolDao.getToolBySignature(TEST_TOOL_SIG);
        assertEquals(TEST_TOOL_ID,testTool.getToolId());
    }

    public void testGetToolByID()
    {
        testTool = toolDao.getToolByID(super.TEST_TOOL_ID);
        assertEquals("verify signature",TEST_TOOL_SIG,testTool.getToolSignature());
        assertEquals("verify service name",TEST_TOOL_SERVICE_NAME,testTool.getServiceName());
        assertEquals("verify display name",TEST_TOOL_DISPLAY_NAME,testTool.getToolDisplayName());
        assertEquals("verify default content id",TEST_TOOL_DEFAULT_CONTENT_ID,testTool.getDefaultToolContentId());
        assertTrue("verify support define later",testTool.getSupportsDefineLater());
        assertEquals("verify identifier",TEST_TOOL_IDENTIFIER,testTool.getToolIdentifier());
        assertEquals("verify version",TEST_TOOL_VERSION,testTool.getToolVersion());
    }

    public void testGetAllTools(){
    	List list = toolDao.getAllTools();
    	assertNotNull(list);    	
    }

    
}
