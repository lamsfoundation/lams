/* ********************************************************************************
 *  Copyright Notice
 *  =================
 * This file contains propriety information of LAMS Foundation. 
 * Copying or reproduction with prior written permission is prohibited.
 * Copyright (c) 2005 
 * Created on 9/02/2005
 ******************************************************************************** */

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
        toolContentIDGenerator = (ToolContentIDGenerator)this.ac.getBean("toolContentIDGenerator");
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
        Long newId = toolContentIDGenerator.getToolContentIDFor(testTool);
        assertNotNull("verify the new id has been created",newId);
        id = newId.longValue();
        newId = toolContentIDGenerator.getToolContentIDFor(testTool);
        assertNotNull("verify the new id has been created",newId);
        nextId = newId.longValue();
        assertTrue("verify the new id is larger than old one",nextId==id+1);        
    }

}
