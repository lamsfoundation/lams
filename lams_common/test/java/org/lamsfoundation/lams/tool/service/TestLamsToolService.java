/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
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

package org.lamsfoundation.lams.tool.service;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.ToolDataAccessTestCase;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.dao.hibernate.ToolSessionDAO;


/**
 * 
 * @author Jacky Fang
 * @since  2005-3-15
 * @version
 * 
 */
public class TestLamsToolService extends ToolDataAccessTestCase
{
	private static Logger log = Logger.getLogger(TestLamsToolService.class);
    private ILamsCoreToolService toolService;
    
    /*
     * @see ToolDataAccessTestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        toolService = (ILamsCoreToolService) this.context.getBean("lamsCoreToolService");
        
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
     * Constructor for TestLamsToolService.
     * @param arg0
     */
    public TestLamsToolService(String arg0)
    {
        super(arg0);
    }

    public void testGetNGToolSessionByActivityId() throws LamsToolServiceException
    {
        ToolSession tngToolSession = toolService.getToolSessionByActivity(this.testUser,
                                                                           this.testNonGroupedActivity);
        assertNotNull(tngToolSession);
    }

    public void testGetGroupedToolSessionByActivityId()throws LamsToolServiceException
    {
        ToolSession tgroupedToolSession = toolService.getToolSessionByActivity(this.testUser,
                                                                                this.testGroupedActivity);
        assertNotNull(tgroupedToolSession);
    }
    
    public void testGetToolURLByLearnerMode() throws LamsToolServiceException
    {
        String learnerUrl = toolService.getToolURLByMode(testNonGroupedActivity,testUser,ToolAccessMode.LEARNER);
        
        assertNotNull(learnerUrl);
        log.info("learner url:"+learnerUrl);
    }
    
    public void testGetToolURLByTeacherMode() throws LamsToolServiceException
    {
        String monitorUrl = toolService.getToolURLByMode(testNonGroupedActivity,testUser,ToolAccessMode.TEACHER);
        
        assertNotNull(monitorUrl);
        log.info("learner url:"+monitorUrl);
    }
    
    public void testGetToolURLByAuthorMode() throws LamsToolServiceException
    {
        String authorUrl = toolService.getToolURLByMode(testNonGroupedActivity,testUser,ToolAccessMode.AUTHOR);
        
        assertNotNull(authorUrl);
        log.info("learner url:"+authorUrl);
    }
}
