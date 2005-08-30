/* 
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
USA

http://www.gnu.org/licenses/gpl.txt 
*/
package org.lamsfoundation.lams.tool;

import java.util.Date;

import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.dao.IActivityDAO;
import org.lamsfoundation.lams.learningdesign.dao.IGroupDAO;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.ActivityDAO;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.GroupDAO;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.dao.ILessonDAO;
import org.lamsfoundation.lams.lesson.dao.hibernate.LessonDAO;
import org.lamsfoundation.lams.test.AbstractCommonTestCase;
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
public class ToolDataAccessTestCase extends AbstractCommonTestCase
{

    protected IToolContentDAO toolContentDao;
    protected IToolDAO toolDao;
    protected IToolSessionDAO toolSessionDao;
    protected IUserDAO userDao;
	protected IActivityDAO activityDAO;
    protected IGroupDAO groupDao;
	protected ILessonDAO lessonDao;
    
    //Test tool id - survey tool
    protected final Long TEST_TOOL_ID = new Long(6);
    protected final String TEST_TOOL_SIG = "survey_signature";
    protected final String TEST_TOOL_SERVICE_NAME = "surveyService";
    protected final String TEST_TOOL_IDENTIFIER = "survey";
    protected final String TEST_TOOL_VERSION = "1.1";
    protected final String TEST_TOOL_DISPLAY_NAME = "Survey";
    protected final long TEST_TOOL_DEFAULT_CONTENT_ID = 6;

    protected Tool testTool;
    protected ToolSession ngToolSession;
    protected ToolSession gToolSession;
    protected User testUser;
    protected Group testGroup;
    protected ToolActivity testNonGroupedActivity;
    protected ToolActivity testGroupedActivity;
    protected Lesson testLesson;
    
    private final static Integer TEST_USER_ID = new Integer(1);
    private final static Long TEST_NON_GROUP_ACTIVITY_ID = new Long(20);
    private static final Long TEST_GROUPED_ACTIVITY_ID = new Long(19);
    private static final Long TEST_GROUP_ID = new Long(88);
    private static final Long TEST_GROUPING_ID = new Long(100);
    public static final Long TEST_LESSON_ID = new Long(1);
    
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
        groupDao = (GroupDAO)this.context.getBean("groupDAO");
        lessonDao = (LessonDAO)this.context.getBean("lessonDAO");
        
        //retrieve test domain data
        testUser = userDao.getUserById(TEST_USER_ID);
        testNonGroupedActivity = (ToolActivity)activityDAO.getActivityByActivityId(TEST_NON_GROUP_ACTIVITY_ID);
        testGroupedActivity = (ToolActivity)activityDAO.getActivityByActivityId(TEST_GROUPED_ACTIVITY_ID);
        testGroup = (Group)groupDao.getGroupById(TEST_GROUP_ID);
        testLesson = lessonDao.getLesson(TEST_LESSON_ID);
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
    protected String[] getContextConfigLocation()
    {
        return new String[] { "/org/lamsfoundation/lams/lesson/lessonApplicationContext.xml",
                			  "/org/lamsfoundation/lams/tool/toolApplicationContext.xml",
                			  "org/lamsfoundation/lams/localApplicationContext.xml"};
    }
    
    public void initTestToolSession()
    {
        this.ngToolSession=this.initNGToolSession();
        this.gToolSession = this.initGToolSession();
    }
    
    public ToolSession initNGToolSession()
    {
        return new NonGroupedToolSession(testNonGroupedActivity,
                                         new Date(System.currentTimeMillis()),
                                         ToolSession.STARTED_STATE,
                                         testUser,
                                         testLesson);
    }
    
    
    public ToolSession initGToolSession()
    {
        return new GroupedToolSession(testGroupedActivity,
                                      new Date(System.currentTimeMillis()),
                                      ToolSession.STARTED_STATE,
                                      testGroup,
                                      testLesson);
    }

 }
