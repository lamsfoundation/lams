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

import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.Activity;
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
	
    private ILearnerService learnerService;
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
    
    private final Long TEST_ACTIVITY_ID1_NB = new Long(600);
    private final Long TEST_ACTIVITY_ID2_NB = new Long(601);
    private final Long TEST_ACTIVITY_ID3_SURVEY = new Long(602);
    private final Long TEST_ACTIVITY_ID4_NB = new Long(603);
    private final Long TEST_ACTIVITY_ID5_NB = new Long(604);
    private final Long TEST_ACTIVITY_ID6_OPTIONAL = new Long(605);
    private final Long TEST_ACTIVITY_ID7_OPT_NB = new Long(606);
    private final Long TEST_ACTIVITY_ID8_OPT_SURVEY = new Long(607);
    private final Long TEST_ACTIVITY_ID9_NB = new Long(608);    
    
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
                "classpath:/org/lamsfoundation/lams/tool/toolApplicationContext.xml",
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
        learnerService = (ILearnerService)this.context.getBean("learnerService");
        usermanageService = (IUserManagementService)this.context.getBean("userManagementService");
        lessonDao = (LessonDAO)this.context.getBean("lessonDAO");
        learnerProgressDao = (LearnerProgressDAO)this.context.getBean("learnerProgressDAO");
        toolSessionDao = (ToolSessionDAO)this.context.getBean("toolSessionDAO");
        transitionDao = (TransitionDAO)this.context.getBean("transitionDAO");
        exportService = (IExportPortfolioService) this.context.getBean("exportService");
        testUser = usermanageService.getUserById(TEST_USER_ID);
        testLesson = lessonDao.getLesson(TEST_LESSON_ID);
        
        testLesson_fromCommon = lessonDao.getLesson(TEST_LESSON_ID_fromCommon);
        testUser_fromCommon = usermanageService.getUserById(TEST_USER_ID_fromCommon);
   }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
        
    }
  
    /**
     * Tests the export service method getOrderedActivityList(LearningDesign), used
     * when the student is conducting the export. The ordered list of activities 
     * returned by this method, is compared with the correct ordered list that 
     * should be returned.
     */
    public void testGetOrderedListByLearningDesign()
    {
    	Long id = testLesson.getLearningDesign().getFirstActivity().getActivityId();
    	Vector sortedActivityList = exportService.getOrderedActivityList(testLesson.getLearningDesign());
    	Vector correctOrderedList = getTestOrderedActivityIdList();
    	Iterator i = sortedActivityList.iterator();
    	Iterator j = correctOrderedList.iterator();
    	
    	while(i.hasNext() && j.hasNext())
    	{
    		Activity a = (Activity)i.next();
    		Long testId = (Long)j.next();
    		// ensure that the activityId from the test ordered list and the ordered list returned from the method is the same        	
    		assertEquals(testId, a.getActivityId()); 
    	}     	
    	
    }
    
    /**
     * Tests the export service method getOrderedActivityList(learnerProgressId), used when
     * a student is conducting the export. The ordered list of completed activities returned
     * by this method is compared with the correct ordered list that should be returned.
     * @return
     */
    public void testGetOrderedListByLearnerProgressId()
    {
        testProgress=learnerProgressDao.getLearnerProgressByLearner(testUser,testLesson);
    	
    	Vector sortedActivityList = exportService.getOrderedActivityList(testProgress);
    	Vector testActivityIdList = getTestOrderedCompletedActivityIdList();
    	Iterator i = sortedActivityList.iterator();
    	Iterator j = testActivityIdList.iterator();
    	while(i.hasNext() && j.hasNext())
    	{
    		Activity a = (Activity)i.next();
    		Long testId = (Long)j.next();
    		assertEquals(testId, a.getActivityId());
    		
    	} 
    }
    
    /**
     * Helper method which creates the ordered activity list, based on the test data
     * inserted by export_test_data.sql script. This list is used as a comparison 
     * between the ordered list obtained by calling getOrderedActivityList(LearningDesign)
     * See export_test_data.sql for the list of activities.
     * 
     * @return the ordered list of activities in the learning design
     */
    private Vector getTestOrderedActivityIdList()
    {
    	Vector orderedIdList = new Vector();
    	orderedIdList.add(TEST_ACTIVITY_ID1_NB);
    	orderedIdList.add(TEST_ACTIVITY_ID2_NB);
    	orderedIdList.add(TEST_ACTIVITY_ID3_SURVEY);
    	orderedIdList.add(TEST_ACTIVITY_ID4_NB);
    	orderedIdList.add(TEST_ACTIVITY_ID5_NB);
    	//orderedIdList.add(TEST_ACTIVITY_ID6_OPTIONAL); //not added because its not a toolactivity, only child activities are added
    	orderedIdList.add(TEST_ACTIVITY_ID7_OPT_NB);
    	orderedIdList.add(TEST_ACTIVITY_ID8_OPT_SURVEY);
    	orderedIdList.add(TEST_ACTIVITY_ID9_NB);
    	return orderedIdList;
    }
    
    /**
     * Helper method which creates the ordered list of completed activities by the learner,
     * based on the test data inserted by export_test_data.sql script. This list is used as a comparison 
     * between the ordered list obtained by calling getOrderedActivityList(learnerProgressId)
     * See export_test_data.sql for the list of completed activities.
     * 
     * @return the ordered list of completed activities by the test learner
     */
    private Vector getTestOrderedCompletedActivityIdList()
    {
    	Vector orderedIdList = new Vector();
    	orderedIdList.add(TEST_ACTIVITY_ID1_NB);
    	orderedIdList.add(TEST_ACTIVITY_ID2_NB);
    	orderedIdList.add(TEST_ACTIVITY_ID3_SURVEY);
    	orderedIdList.add(TEST_ACTIVITY_ID4_NB);
    	orderedIdList.add(TEST_ACTIVITY_ID5_NB);
    	//orderedIdList.add(TEST_ACTIVITY_ID6_OPTIONAL); //not added because its not a toolactivity, only child activities are added
    	orderedIdList.add(TEST_ACTIVITY_ID7_OPT_NB);
    	
    	return orderedIdList;
    }
    
 /*
    //method is not used as it doesnt work. get the lazy initialisation error
    public void testGetOrderedListByLearnerProgress2()
    {
    	log.info("==================Inside testGetOrderedList(LearnerProgress)==================");
    	testProgress=learnerProgressDao.getLearnerProgressByLearner(testUser,testLesson);
    	
    	Vector sortedActivityList = exportService.getOrderedActivityList(testProgress);
    	Vector testActivityIdList = getTestOrderedCompletedActivityIdList();
    	Iterator i = sortedActivityList.iterator();
    	Iterator j = testActivityIdList.iterator();
    	while(i.hasNext() && j.hasNext())
    	{
    		Activity a = (Activity)i.next();
    		Long testId = (Long)j.next();
    		assertEquals(testId, a.getActivityId());
    		log.info("Activity Id: " + a.getActivityId());
    	} 
    
        
    } */
    
    /*
    public void testSetupPortfoliosFromCommonByTeacher() throws LamsToolServiceException
    {
    	log.info("==================Inside testSetupPortfolios by teacher==================");
    	Long id = testLesson_fromCommon.getLearningDesign().getFirstActivity().getActivityId();
    	Vector sortedActivityList = exportService.getOrderedActivityList(testLesson_fromCommon.getLearningDesign());
    	Portfolio[] portfolioArray = exportService.setupPortfolios(sortedActivityList, ToolAccessMode.TEACHER, null); 
    	Portfolio individualPortfolio = null;
    	assertNotNull(portfolioArray);
    	
    	int arraySize = portfolioArray.length;
    	assertTrue(arraySize!= 0);
    	for (int i=0; i<arraySize; i++)
    	{
    		individualPortfolio = portfolioArray[i];
    		assertNotNull(individualPortfolio);
    		log.info("Activity id: " + individualPortfolio.getActivityId());
    		log.info("Activity Name: " + individualPortfolio.getActivityName());
    		log.info("Activity Description: " + individualPortfolio.getActivityDescription());
    		log.info("Activity exportURL: " + individualPortfolio.getExportUrl());
    	} 
    }
  
    public void testSetupPortfoliosFromCommonByLearner() throws LamsToolServiceException
    {
    	log.info("==================Inside testSetupPortfolios By Learner==================");
    	testProgress=learnerProgressDao.getLearnerProgressByLearner(testUser_fromCommon,testLesson_fromCommon);
    	
    	Vector sortedActivityList = exportService.getOrderedActivityList(testProgress);
    	Portfolio[] portfolioArray = exportService.setupPortfolios(sortedActivityList, ToolAccessMode.LEARNER, testUser_fromCommon); 
    	Portfolio individualPortfolio = null;
    	assertNotNull(portfolioArray);
    	
    	int arraySize = portfolioArray.length;
    	assertTrue(arraySize!= 0);
    	for (int i=0; i<arraySize; i++)
    	{
    		individualPortfolio = portfolioArray[i];
    		assertNotNull(individualPortfolio);
    		log.info("Activity id: " + individualPortfolio.getActivityId());
    		log.info("Activity Name: " + individualPortfolio.getActivityName());
    		log.info("Activity Description: " + individualPortfolio.getActivityDescription());
    		log.info("Activity exportURL: " + individualPortfolio.getExportUrl());
    	} 
    }
    */
    
  
   /* 
    public void testZipPortfolio()
    {
    	String directoryName = "C:\\TestDir";
    	String zipFileName = "testzip.zip";
    	
    	//create directory first
    //	boolean created = exportService.createTempDirectory(directoryName);
    	//assertTrue(created);
    	String zipFileNameReturned = exportService.zipPortfolio(zipFileName, directoryName);
    	assertNotNull(zipFileNameReturned);
    	assertEquals(zipFileNameReturned, zipFileName);
    } */
    
  /*  public void testConnectToUrl()
    {
        //Set up the portfolio, then try to connect to the tool
        String exportUrl = "http://localhost:8080/lams/tool/lanb11/portfolioExport?mode=teacher&toolContentId=358&directoryName=/JunitTest";
        String mainFileName = exportService.connectToToolViaExportURL(exportUrl);
      //  System.out.println(mainFileName);
    }
*/
}
