/* ********************************************************************************
 *  Copyright Notice
 *  =================
 * This file contains propriety information of LAMS Foundation. 
 * Copying or reproduction with prior written permission is prohibited.
 * Copyright (c) 2005 
 * Created on 8/02/2005
 ******************************************************************************** */

package org.lamsfoundation.lams.tool;

import java.util.Date;

import org.lamsfoundation.lams.AbstractLamsTestCase;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.dao.IActivityDAO;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.ActivityDAO;
import org.lamsfoundation.lams.tool.dao.IToolContentDAO;
import org.lamsfoundation.lams.tool.dao.IToolDAO;
import org.lamsfoundation.lams.tool.dao.IToolSessionDAO;
import org.lamsfoundation.lams.tool.dao.hibernate.ToolContentDAO;
import org.lamsfoundation.lams.tool.dao.hibernate.ToolDAO;
import org.lamsfoundation.lams.tool.dao.hibernate.ToolSessionDAO;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dao.IUserDAO;
import org.lamsfoundation.lams.usermanagement.dao.hibernate.UserDAO;


/**
 * 
 * @author Jacky Fang 8/02/2005
 * 
 */
public class ToolDataAccessTestCase extends AbstractLamsTestCase
{

    protected IToolContentDAO toolContentDao;
    protected IToolDAO toolDao;
    protected IToolSessionDAO toolSessionDao;
    protected IUserDAO userDao;
	protected IActivityDAO activityDAO;
    //Test tool id - survey tool
    protected final Long TEST_TOOL_ID = new Long(6);
    protected Tool testTool;
    protected ToolSession ngToolSession;
    protected ToolSession gToolSession;
    protected User testUser;
    protected ToolActivity testActivity;
    
    private final Integer TEST_USER_ID = new Integer(1);
    private final Long TEST_ACTIVITY_ID = new Long(20);
    /*
     * @see AbstractLamsCommonTestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        
        toolContentDao = (ToolContentDAO)this.context.getBean("toolContentDAO");
        toolDao = (ToolDAO)this.context.getBean("toolDAO");
        toolSessionDao = (ToolSessionDAO)this.context.getBean("toolSessionDAO");
        activityDAO =(ActivityDAO) context.getBean("activityDAO");
        
        userDao = (UserDAO) this.context.getBean("userDAO");
        //retrieve test domain data
        testUser = userDao.getUserById(TEST_USER_ID);
        testActivity = (ToolActivity)activityDAO.getActivityByActivityId(TEST_ACTIVITY_ID);
    }

    /*
     * @see AbstractLamsCommonTestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    /**
     * Constructor for ToolDataAccessTestCase.
     * @param arg0
     */
    public ToolDataAccessTestCase(String arg0)
    {
        super(arg0);
    }
    /**
     * @see org.lamsfoundation.lams.AbstractLamsTestCase#getContextConfigLocation()
     */
    protected String[] getContextConfigLocation()
    {
        return new String[] { "/org/lamsfoundation/lams/tool/toolApplicationContext.xml",
                			  "/org/lamsfoundation/lams/learningdesign/learningDesignApplicationContext.xml",
        					  "applicationContext.xml"};
    }
    
    public void initTestToolSession()
    {
        this.ngToolSession=this.initNGToolSession();
    }
    
    public ToolSession initNGToolSession()
    {
        ToolSession toolSession = new NonGroupedToolSession(testActivity,
                                                  new Date(System.currentTimeMillis()),
                                                  ToolSession.STARTED_STATE,
                                                  testUser);
        return toolSession;
    }
    
   
    
}
