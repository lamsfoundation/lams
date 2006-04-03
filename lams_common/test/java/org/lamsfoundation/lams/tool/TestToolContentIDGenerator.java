/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0 
 * as published by the Free Software Foundation.
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
package org.lamsfoundation.lams.tool;


/**
 * 
 * @author Jacky Fang 9/02/2005
 * 
 */
public class TestToolContentIDGenerator extends ToolDataAccessTestCase
{

    private ToolContentIDGenerator toolContentIDGenerator;
    /*
     * @see ToolDataAccessTestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        testTool = toolDao.getToolByID(super.TEST_TOOL_ID);
        toolContentIDGenerator = (ToolContentIDGenerator)this.context.getBean("toolContentIDGenerator");
    }

    /*
     * @see ToolDataAccessTestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    /**
     * Constructor for TestToolContentIDGenerator.
     * @param arg0
     */
    public TestToolContentIDGenerator(String arg0)
    {
        super(arg0);
    }

    public void testGetToolContentIDFor()
    {
        long id;
        long nextId;
        Long newId = toolContentIDGenerator.getNextToolContentIDFor(testTool);
        assertNotNull("verify the new id has been created",newId);
        id = newId.longValue();
        newId = toolContentIDGenerator.getNextToolContentIDFor(testTool);
        assertNotNull("verify the new id has been created",newId);
        nextId = newId.longValue();
        assertTrue("verify the new id is larger than old one",nextId==id+1);        
    }

}
