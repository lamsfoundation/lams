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
        toolSessionDao.saveToolSession(this.gToolSession);
    }

    /*
     * @see ToolDataAccessTestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
        toolSessionDao.removeToolSession(this.ngToolSession);
        toolSessionDao.removeToolSession(this.gToolSession);
    }

    /**
     * Constructor for TestToolSessionDAO.
     * @param arg0
     */
    public TestToolSessionDAO(String testName)
    {
        super(testName);
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
        ToolSession toolSession = toolSessionDao.getToolSessionByLearner(testUser,testNonGroupedActivity);
        
        assertNotNull(toolSession);
        assertEquals("verify tool session state",ToolSession.STARTED_STATE,toolSession.getToolSessionStateId());
        assertEquals("verify tool session type",ToolSession.NON_GROUPED_TYPE,toolSession.getToolSessionTypeId());
    }
    
    public void testGetToolSessionByGroup()
    {
        ToolSession toolSession = toolSessionDao.getToolSessionByLearner(testUser,testGroupedActivity);
        
        
        assertNotNull(toolSession);
        assertEquals("verify tool session state",ToolSession.STARTED_STATE,toolSession.getToolSessionStateId());
        assertEquals("verify tool session type",ToolSession.GROUPED_TYPE,toolSession.getToolSessionTypeId());
    }
    
/*    public void testGetToolSessionByClassGroup()
    {
        ToolSession toolSession = toolSessionDao.getToolSessionByClassGroup(testGroup,testGroupedActivity);
        
        assertNotNull(toolSession);
    } */

    public void testSaveNonGroupedToolSession()
    {
        ToolSession testToolSession=initNGToolSession();
        toolSessionDao.saveToolSession(this.ngToolSession);
    }

    public void testUpdateToolSession()
    {
        ToolSession toolSession = toolSessionDao.getToolSessionByLearner(testUser,testGroupedActivity);
        assertEquals("verify original state",ToolSession.STARTED_STATE,toolSession.getToolSessionStateId());
        
        toolSession.setToolSessionStateId(ToolSession.ENDED_STATE);
        toolSessionDao.updateToolSession(toolSession);
        ToolSession updatedToolSession = toolSessionDao.getToolSessionByLearner(testUser,testGroupedActivity);
        
        assertEquals("verify new state",ToolSession.ENDED_STATE,updatedToolSession.getToolSessionStateId());

    }
}
