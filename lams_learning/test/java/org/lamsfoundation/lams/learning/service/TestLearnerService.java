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
package org.lamsfoundation.lams.learning.service;

import org.lamsfoundation.lams.AbstractLamsTestCase;

import org.lamsfoundation.lams.learning.progress.ProgressException;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.dao.ILearnerProgressDAO;
import org.lamsfoundation.lams.lesson.dao.ILessonDAO;
import org.lamsfoundation.lams.lesson.dao.hibernate.LearnerProgressDAO;
import org.lamsfoundation.lams.lesson.dao.hibernate.LessonDAO;
import org.lamsfoundation.lams.tool.service.LamsToolServiceException;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;


/**
 * 
 * @author Jacky Fang 2005-2-22
 * 
 */
public class TestLearnerService extends AbstractLamsTestCase
{
    private ILearnerService learnerService;
    private IUserManagementService usermanageService;
    private ILessonDAO lessonDao; 
    private ILearnerProgressDAO learnerProgressDao;
    //---------------------------------------------------------------------
    // Testing Data - Constants
    //---------------------------------------------------------------------
    private final Integer TEST_USER_ID = new Integer(2);
    private final Long Test_Lesson_ID = new Long(1);
    //---------------------------------------------------------------------
    // Testing Data - Instance Variables
    //---------------------------------------------------------------------
    private User testUser;
    private Lesson testLesson;
    private LearnerProgress testProgress;
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        learnerService = (ILearnerService)this.context.getBean("learnerService");
        usermanageService = (IUserManagementService)this.context.getBean("userManagementService");
        lessonDao = (LessonDAO)this.context.getBean("lessonDAO");
        learnerProgressDao = (LearnerProgressDAO)this.context.getBean("learnerProgressDAO");
        
        testUser = usermanageService.getUserById(TEST_USER_ID);
        testLesson = lessonDao.getLesson(Test_Lesson_ID);
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    /**
     * Constructor for TestLearnerService.
     * @param name
     */
    public TestLearnerService(String name)
    {
        super(name);
    }
    protected String[] getContextConfigLocation()
    {
        return new String[] { "/WEB-INF/spring/learningApplicationContext.xml",
  			  				  "/org/lamsfoundation/lams/lesson/lessonApplicationContext.xml",
  			  				  "/org/lamsfoundation/lams/tool/toolApplicationContext.xml",					  
                              "/org/lamsfoundation/lams/tool/survey/dataAccessContext.xml",
                              "/org/lamsfoundation/lams/tool/survey/surveyApplicationContext.xml",          					  
        					  "applicationContext.xml"};
    }
    
    public void testJoinLesson() throws ProgressException,LamsToolServiceException
    {
        learnerService.joinLesson(testUser,testLesson);
        
        testProgress=learnerProgressDao.getLearnerProgressByLearner(testUser,testLesson);
        
        assertNotNull(testProgress);
        assertNotNull("verify next activity",testProgress.getNextActivity());
        assertEquals("verify id of next activity-survey",20,testProgress.getNextActivity().getActivityId().longValue());
        assertNotNull("verify current activity",testProgress.getCurrentActivity());
        assertEquals("verify id of current activity-survey",20,testProgress.getCurrentActivity().getActivityId().longValue());
        assertEquals("verify attempted activity",1,testProgress.getAttemptedActivities().size());
        assertEquals("verify completed activity",0,testProgress.getCompletedActivities().size());
        assertNotNull("verify correspondent tool session for next activity",
                      ((ToolActivity)testProgress.getNextActivity()).getToolSessions());
        assertEquals("verify number of tool sessions created",1,
                     ((ToolActivity)testProgress.getNextActivity()).getToolSessions().size());
        assertTrue(true);
    }

}
