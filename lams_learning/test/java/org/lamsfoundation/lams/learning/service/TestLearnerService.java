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
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.dao.ILessonDAO;
import org.lamsfoundation.lams.lesson.dao.hibernate.LessonDAO;
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
    //---------------------------------------------------------------------
    // Testing Data - Constants
    //---------------------------------------------------------------------
    private final Integer TEST_USER_ID = new Integer(1);
    private final Long Test_Lesson_ID = new Long(1);
    //---------------------------------------------------------------------
    // Testing Data - Instance Variables
    //---------------------------------------------------------------------
    private User testUser;
    private Lesson testLesson;
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        learnerService = (ILearnerService)this.context.getBean("learnerService");
        usermanageService = (IUserManagementService)this.context.getBean("userManagementService");
        lessonDao = (LessonDAO)this.context.getBean("lessonDAO");
        
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
        					  "applicationContext.xml"};
    }
    public void testJoinLesson() throws ProgressException
    {
        learnerService.joinLesson(testUser,testLesson);
        assertTrue(true);
    }

}
