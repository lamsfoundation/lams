/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
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
package org.lamsfoundation.lams.tool.service;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.ToolDataAccessTestCase;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.dao.hibernate.ToolSessionDAO;
import org.lamsfoundation.lams.tool.exception.LamsToolServiceException;
import org.lamsfoundation.lams.web.util.AttributeNames;


/**
 * 
 * @author Jacky Fang
 * @since  2005-3-15
 * @version
 * 
 */
public class TestLamsCoreToolService extends ToolDataAccessTestCase
{
	private static Logger log = Logger.getLogger(TestLamsCoreToolService.class);
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
    public TestLamsCoreToolService(String arg0)
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
    
    public void testGetToolLearnerURL() throws LamsToolServiceException
    {
        String learnerUrl = toolService.getToolLearnerURL(testNonGroupedActivity,testUser);
        assertNotNull(learnerUrl);
        assertTrue(learnerUrl.indexOf("?")>0);
        assertTrue(learnerUrl.indexOf(AttributeNames.PARAM_TOOL_SESSION_ID)>0);
        log.info("learner url:"+learnerUrl);
    }
    
    public void testGetToolLearnerProgressURL() throws LamsToolServiceException
    {
        String monitorUrl = toolService.getToolLearnerProgressURL(testNonGroupedActivity,testUser);
        
        assertNotNull(monitorUrl);
        log.info("monitor url:"+monitorUrl);
        assertTrue(monitorUrl.indexOf("?")>0);
        assertTrue(monitorUrl.indexOf(AttributeNames.PARAM_TOOL_SESSION_ID)>0);
        assertTrue(monitorUrl.indexOf(AttributeNames.PARAM_USER_ID)>0);
    }
    
    public void testGetToolLearnerPreviewURL() throws LamsToolServiceException
    {
        String authorUrl = toolService.getToolLearnerPreviewURL(testNonGroupedActivity,testUser);
        
        assertNotNull(authorUrl);
        log.info("author url:"+authorUrl);
        assertTrue(authorUrl.indexOf("?")>0);
        assertTrue(authorUrl.indexOf(AttributeNames.PARAM_TOOL_SESSION_ID)>0);

    }
    
    public void testGetToolLearnerURLForGroupedTool() throws LamsToolServiceException
    {
        String learnerUrl = toolService.getToolLearnerURL(testGroupedActivity,testUser);
        
        assertNotNull(learnerUrl);
        log.info("Non grouped activity learner url:"+learnerUrl);
        assertTrue(learnerUrl.indexOf("?")>0);
        assertTrue(learnerUrl.indexOf(ToolAccessMode.LEARNER.toString())!=-1);
        assertTrue(learnerUrl.indexOf(AttributeNames.PARAM_MODE)>0);

    }
    
    public void testSetupToolURLWithToolContent()
    {
        String testUrl = "http://localhost:8080/lams/test.do?method=test";
        String testUrlWithContent = toolService.setupToolURLWithToolContent(testNonGroupedActivity,testUrl);
        log.info("test url with content id:"+testUrlWithContent);
        assertTrue(testUrlWithContent.indexOf(AttributeNames.PARAM_TOOL_CONTENT_ID)>0);

    }
}
