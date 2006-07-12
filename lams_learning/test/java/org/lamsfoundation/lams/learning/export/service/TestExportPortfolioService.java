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
package org.lamsfoundation.lams.learning.export.service;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learning.service.ICoreLearnerService;
import org.lamsfoundation.lams.learningdesign.dao.ITransitionDAO;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.TransitionDAO;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.dao.ILearnerProgressDAO;
import org.lamsfoundation.lams.lesson.dao.ILessonDAO;
import org.lamsfoundation.lams.lesson.dao.hibernate.LearnerProgressDAO;
import org.lamsfoundation.lams.lesson.dao.hibernate.LessonDAO;
import org.lamsfoundation.lams.test.AbstractLamsTestCase;
import org.lamsfoundation.lams.tool.dao.IToolSessionDAO;
import org.lamsfoundation.lams.tool.dao.hibernate.ToolSessionDAO;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;


/**
 * @author mtruong
 *
 */
public class TestExportPortfolioService extends AbstractLamsTestCase  {

	
	 //---------------------------------------------------------------------
    // Instance variables
    //---------------------------------------------------------------------
	private static Logger log = Logger.getLogger(TestExportPortfolioService.class);
	
    private ICoreLearnerService learnerService;
    private IUserManagementService usermanageService;
    private ILessonDAO lessonDao; 
    private ILearnerProgressDAO learnerProgressDao;
    private IToolSessionDAO toolSessionDao;
    private IExportPortfolioService exportService;
    
    private ITransitionDAO transitionDao;
    //---------------------------------------------------------------------
    // Testing Data - Constants
    //---------------------------------------------------------------------
    private final Integer TEST_USER_ID = new Integer(1250);
    private final Long TEST_LESSON_ID = new Long(900);
    
    //original test data from lams_common
    private final Integer TEST_USER_ID_fromCommon = new Integer(1);
    private final Long TEST_LESSON_ID_fromCommon = new Long(1);
    //---------------------------------------------------------------------
    // Testing Data - Instance Variables
    //---------------------------------------------------------------------

    //  test data which corresponds to test-data entered from running insert-test-data from lams_learning
    private User testUser; 
    private Lesson testLesson;
    
    //  test data which corresponds to test-data entered from running insert-test-data from lams_common
    private User testUser_fromCommon;
    private Lesson testLesson_fromCommon;
    
    private static LearnerProgress testProgress;
    
    public TestExportPortfolioService(String name)
	{
		super(name);
		
	}
	
	protected String[] getContextConfigLocation()
    {
        return new String[] { "classpath:/org/lamsfoundation/lams/localApplicationContext.xml",
                "classpath:/org/lamsfoundation/lams/lesson/lessonApplicationContext.xml",
                "classpath:/org/lamsfoundation/lams/toolApplicationContext.xml",
                "classpath:/org/lamsfoundation/lams/learning/learningApplicationContext.xml",
                "classpath:*/applicationContext.xml"};
    }
	 
	protected String getHibernateSessionFactoryName()
	{
	        return "coreSessionFactory";
	}
	
	protected void setUp() throws Exception
    {
        super.setUp();
        learnerService = (ICoreLearnerService)this.context.getBean("learnerService");
        usermanageService = (IUserManagementService)this.context.getBean("userManagementService");
        lessonDao = (LessonDAO)this.context.getBean("lessonDAO");
        learnerProgressDao = (LearnerProgressDAO)this.context.getBean("learnerProgressDAO");
        toolSessionDao = (ToolSessionDAO)this.context.getBean("toolSessionDAO");
        transitionDao = (TransitionDAO)this.context.getBean("transitionDAO");
        exportService = (IExportPortfolioService) this.context.getBean("exportService");
 /*      Commented out as usermanageService.getUserById has been removed 
  * 	 testUser = usermanageService.getUserById(TEST_USER_ID);
        testLesson = lessonDao.getLesson(TEST_LESSON_ID);
        
        testLesson_fromCommon = lessonDao.getLesson(TEST_LESSON_ID_fromCommon);
        testUser_fromCommon = usermanageService.getUserById(TEST_USER_ID_fromCommon); */
   }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
        
    }
  

}
