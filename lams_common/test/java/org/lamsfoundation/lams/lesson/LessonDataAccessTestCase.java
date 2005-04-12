/* ********************************************************************************
 *  Copyright Notice
 *  =================
 * This file contains propriety information of LAMS Foundation. 
 * Copying or reproduction with prior written permission is prohibited.
 * Copyright (c) 2005 
 * Created on 2/02/2005
 ******************************************************************************** */

package org.lamsfoundation.lams.lesson;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import net.sf.hibernate.HibernateException;

import org.lamsfoundation.lams.AbstractLamsTestCase;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.dao.ILearningDesignDAO;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.LearningDesignDAO;
import org.lamsfoundation.lams.lesson.dao.ILearnerProgressDAO;
import org.lamsfoundation.lams.lesson.dao.ILessonClassDAO;
import org.lamsfoundation.lams.lesson.dao.ILessonDAO;
import org.lamsfoundation.lams.lesson.dao.hibernate.LearnerProgressDAO;
import org.lamsfoundation.lams.lesson.dao.hibernate.LessonClassDAO;
import org.lamsfoundation.lams.lesson.dao.hibernate.LessonDAO;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dao.IOrganisationDAO;
import org.lamsfoundation.lams.usermanagement.dao.IUserDAO;
import org.lamsfoundation.lams.usermanagement.dao.hibernate.OrganisationDAO;
import org.lamsfoundation.lams.usermanagement.dao.hibernate.UserDAO;


/**
 * The super class for lesson related data access test. It defines services, 
 * such as initialize lesson data and clean up lesson data, for its descendents.
 * It also act as a good example of creating and deleting lesson object that
 * can be used by client.
 * @author Jacky Fang 2/02/2005
 * 
 */
public class LessonDataAccessTestCase extends AbstractLamsTestCase
{

    //---------------------------------------------------------------------
    // DAO instances for initializing data
    //---------------------------------------------------------------------
    protected IUserDAO userDao;
    protected ILearningDesignDAO learningDesignDao;
    protected IOrganisationDAO orgDao;
    protected ILessonDAO lessonDao;
    protected ILessonClassDAO lessonClassDao;
    protected ILearnerProgressDAO learnerProgressDao;
    
    //---------------------------------------------------------------------
    // Domain Object instances
    //---------------------------------------------------------------------
    protected Lesson testLesson;
    protected User testUser;
    protected LearningDesign testLearningDesign;
    protected Organisation testOrg;
    protected LessonClass testLessonClass;
    protected LearnerProgress testLearnerProgress;
    
    //---------------------------------------------------------------------
    // Class level constants
    //---------------------------------------------------------------------
    private final Integer TEST_USER_ID = new Integer(1);
    private final Long TEST_LEARNING_DESIGN_ID = new Long(1);
    private final Integer TEST_ORGANIZATION_ID = new Integer(1);
    private final int TEST_GROUP_ORDER_ID = 0;


    //---------------------------------------------------------------------
    // Overidden methods
    //---------------------------------------------------------------------
    /**
     * @param name
     */
    public LessonDataAccessTestCase(String name)
    {
        super(name);
    }

    /**
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();

        userDao = (UserDAO) this.context.getBean("userDAO");
        learningDesignDao = (LearningDesignDAO) this.context.getBean("learningDesignDAO");
        orgDao = (OrganisationDAO) this.context.getBean("organisationDAO");

        //retrieve test domain data
        testUser = userDao.getUserById(TEST_USER_ID);
        testLearningDesign = learningDesignDao.getLearningDesignById(TEST_LEARNING_DESIGN_ID);
        testOrg = orgDao.getOrganisationById(TEST_ORGANIZATION_ID);
   
        //get lesson related daos
        lessonDao = (LessonDAO)this.context.getBean("lessonDAO");
        lessonClassDao = (LessonClassDAO)this.context.getBean("lessonClassDAO");
        learnerProgressDao = (LearnerProgressDAO)this.context.getBean("learnerProgressDAO");
    }

    /**
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();

    }

    /**
     * @see org.lamsfoundation.lams.AbstractLamsTestCase#getContextConfigLocation()
     */
    protected String[] getContextConfigLocation()
    {
        return new String[] { "/org/lamsfoundation/lams/lesson/lessonApplicationContext.xml",
                			  "/org/lamsfoundation/lams/learningdesign/learningDesignApplicationContext.xml",
                			  "applicationContext.xml"};
    }

    //---------------------------------------------------------------------
    // Data initialization and finalization methods
    //---------------------------------------------------------------------
    /**
     * Initialize the whole lesson object. This also act as an example of 
     * creating a new lesson for a learning design.
     * 
     * We have to creat lesson class before create group due to the not null
     * field(grouping_id) in the group object.
     * @throws HibernateException
     * 
     */
    protected void initializeTestLesson() throws HibernateException
    {
        this.initLessonClassData();
        lessonClassDao.saveLessonClass(this.testLessonClass);

        this.setUpGroupsForClass();
        lessonClassDao.updateLessonClass(this.testLessonClass);

        this.initLessonData();
        lessonDao.saveLesson(testLesson);
        
        super.getSession().flush();
    }
    
    /**
     * Remove the target lesson from database. We have to clean up the groups 
     * first. We are using hibernate transparent persistant mechanism. To make
     * the deletion of groups work, we have to setup "all-delete-orphan" for
     * groups collection
     * @param lesson the lesson needs to be removed.
     */
    protected void cleanUpLesson(Lesson lesson)
    {
        lessonDao.deleteLesson(lesson);
    }
    
    protected void cleanUpTestLesson() throws HibernateException
    {
        //super.initializeHibernateSession();
        //super.getSession().lock(testLesson,LockMode.READ);
        this.cleanUpLesson(testLesson);
        
        //super.finalizeHibernateSession();
    }
    /**
     * Create a lesson class with empty group information.
     */
    protected void initLessonClassData()
    {
        //make a copy of lazily initialized activities
        Set activities = new HashSet(testLearningDesign.getActivities());
        testLessonClass = new LessonClass(null, //grouping id
                                          new HashSet(),//groups
                                          activities,
                                          null, //staff group 
                                          testLesson);
    }
    
    /**
     * Setup groups(learner group and staff group) fro created lesson class.
     */
    protected void setUpGroupsForClass()
    {
        //create a new staff group
        Set staffs = new HashSet();
        staffs.add(testUser);
        Group staffGroup = new Group(null,//group id 
                                     TEST_GROUP_ORDER_ID,
                                     testLessonClass,
                                     staffs,
                                     new HashSet());
        testLessonClass.setStaffGroup(staffGroup);

        //create learner class group
        Set learnergroups = new HashSet();
        //make a copy of lazily initialized users
        Set users = new HashSet(testOrg.getUsers());
        Group learnerClassGroup = new Group(null,//group id 
                                            testLessonClass.getNextGroupOrderId(),
                                            testLessonClass,
                                            users,
                                            new HashSet());//tool session, should be empty now

        learnergroups.add(learnerClassGroup);
        learnergroups.add(staffGroup);
        testLessonClass.setGroups(learnergroups);

    }
    
    /**
     * Create lesson based on the information we initialized.
     */
    protected void initLessonData()
    {
        testLesson = new Lesson("Test Lesson",
                                "Test lesson description",
                                new Date(System.currentTimeMillis()),
                                testUser,
                                Lesson.CREATED,
                                testLearningDesign,
                                testLessonClass,//lesson class
                                testOrg,
                                new HashSet());//learner progress
    }
  

    /**
     * This init method is called when we need to learner progress testing.
     *
     */
    protected void initLearnerProgressData()
    {
        //pre-condition
        if(testLesson == null)
            throw new IllegalArgumentException("Can't initialize progress without " +
            		"lesson");
        testLearnerProgress = new LearnerProgress(testUser,testLesson);
    }
    //---------------------------------------------------------------------
    // Helper methods
    //---------------------------------------------------------------------
    /**
     * Helper method to validate the created lesson class. This validation
     * method can be reused by sub-classes.
     * @param lessonClass 
     */
    protected void assertLessonClass(LessonClass lessonClass)
    {
        assertEquals("check up number of activities",11,lessonClass.getActivities().size());
        assertEquals("check up staff groups",1,lessonClass.getStaffGroup().getUsers().size());
        assertEquals("check up grouping types, should be class grouping",Grouping.CLASS_GROUPING_TYPE,lessonClass.getGroupingTypeId());
        assertEquals("check up groups",2,lessonClass.getGroups().size());
    }
    /**
     * Helper method to validate the created lesson. This validation
     * method can be reused by sub-classes.
     * @param lesson 
     */
    protected void assertLesson(Lesson lesson)
    {
        assertEquals("check up the lesson name","Test Lesson",lesson.getLessonName());
        assertEquals("check up the lesson description","Test lesson description",lesson.getLessonDescription());
        assertEquals("check up creation time",testLesson.getCreateDateTime().toString(),
                     						  lesson.getCreateDateTime().toString());
        assertEquals("check up user who created this lesson",testUser.getLogin(),lesson.getUser().getLogin());
        assertEquals("check up the lesson state",Lesson.CREATED,lesson.getLessonStateId());
        assertEquals("check up the learning design that used to create lesson",
                     							testLearningDesign.getTitle(),
                     							lesson.getLearningDesign().getTitle());
        assertEquals("check up the organization", testOrg.getName(),lesson.getOrganisation().getName());
        assertEquals("check up the learner progresses",0,lesson.getLearnerProgresses().size());
        
    }

    /**
     * @see org.lamsfoundation.lams.AbstractLamsTestCase#getHibernateSessionFactoryName()
     */
    protected String getHibernateSessionFactoryName()
    {
        return "coreSessionFactory";
    }
}