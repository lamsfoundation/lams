/* ********************************************************************************
 *  Copyright Notice
 *  =================
 * This file contains propriety information of LAMS Foundation. 
 * Copying or reproduction with prior written permission is prohibited.
 * Copyright (c) 2005 
 * Created on 11/02/2005
 ******************************************************************************** */

package org.lamsfoundation.lams.tool.dao;

import org.lamsfoundation.lams.tool.NonGroupedToolSession;
import org.lamsfoundation.lams.tool.ToolDataAccessTestCase;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.dao.hibernate.ToolSessionDAO;


/**
 * 
 * @author Jacky Fang 11/02/2005
 * 
 */
public class TestToolSessionDAO extends ToolDataAccessTestCase
{
    private IToolSessionDAO toolSessionDao;
    /*
     * @see ToolDataAccessTestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        toolSessionDao = (ToolSessionDAO)this.context.getBean("toolSessionDAO");
        super.initTestToolSession();
        toolSessionDao.saveToolSession(this.ngToolSession);
    }

    /*
     * @see ToolDataAccessTestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
        toolSessionDao.removeToolSession(this.ngToolSession);
    }

    /**
     * Constructor for TestToolSessionDAO.
     * @param arg0
     */
    public TestToolSessionDAO(String arg0)
    {
        super(arg0);
    }

    public void testGetToolSession()
    {
        NonGroupedToolSession toolSession = (NonGroupedToolSession)toolSessionDao.getToolSession(this.ngToolSession.getToolSessionId());
        
        assertNotNull(toolSession);
        assertEquals("verify tool session state",ToolSession.STARTED_STATE,toolSession.getToolSessionStateId());
        assertEquals("verify tool session type",ToolSession.NON_GROUPED_TYPE,toolSession.getToolSessionTypeId());
        assertNotNull(toolSession.getUser());
    }

    public void testGetToolSessionByLearner()
    {
        ToolSession toolSession = toolSessionDao.getToolSessionByLearner(testUser,testActivity);
        
        assertNotNull(toolSession);
    }
    
    public void testSaveNonGroupedToolSession()
    {
        ToolSession testToolSession=initNGToolSession();
        toolSessionDao.saveToolSession(this.ngToolSession);
    }


    
}
