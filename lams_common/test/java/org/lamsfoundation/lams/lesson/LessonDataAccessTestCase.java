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

import org.lamsfoundation.lams.AbstractLamsCommonTestCase;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.dao.ILearningDesignDAO;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.LearningDesignDAO;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dao.IOrganisationDAO;
import org.lamsfoundation.lams.usermanagement.dao.IUserDAO;
import org.lamsfoundation.lams.usermanagement.dao.hibernate.OrganisationDAO;
import org.lamsfoundation.lams.usermanagement.dao.hibernate.UserDAO;

/**
 * 
 * @author Jacky Fang 2/02/2005
 * 
 */
public class LessonDataAccessTestCase extends AbstractLamsCommonTestCase
{

    //---------------------------------------------------------------------
    // DAO instances for initializing data
    //---------------------------------------------------------------------
    protected IUserDAO userDao;
    protected ILearningDesignDAO learningDesignDao;
    protected IOrganisationDAO orgDao;
    //---------------------------------------------------------------------
    // Domain Object instances
    //---------------------------------------------------------------------
    protected Lesson testLesson;
    protected User testUser;
    protected LearningDesign testLearningDesign;
    protected Organisation testOrg;
    protected LessonClass testLessonClass;
    //---------------------------------------------------------------------
    // Class level constants
    //---------------------------------------------------------------------
    private final Integer TEST_USER_ID = new Integer(1);
    private final Long TEST_LEARNING_DESIGN_ID = new Long(1);
    private final Integer TEST_ORGANIZATION_ID = new Integer(1);
    private final int TEST_GROUP_ORDER_ID = 0;
    private final Long TEST_CLASS_GROUPING_ID = new Long(1);

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
        userDao = (UserDAO) this.ac.getBean("userDAO");
        learningDesignDao = (LearningDesignDAO) this.ac.getBean("learningDesignDAO");
        orgDao = (OrganisationDAO) this.ac.getBean("organisationDAO");

        //retrieve test domain data
        testUser = userDao.getUserById(TEST_USER_ID);
        testLearningDesign = learningDesignDao.getLearningDesignById(TEST_LEARNING_DESIGN_ID);
        testOrg = orgDao.getOrganisationById(TEST_ORGANIZATION_ID);
    }

    /**
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();

    }

    /**
     * @see org.lamsfoundation.lams.AbstractLamsCommonTestCase#getContextConfigLocation()
     */
    protected String[] getContextConfigLocation()
    {
        return new String[] { "/org/lamsfoundation/lams/lesson/lessonApplicationContext.xml" };
    }

    //---------------------------------------------------------------------
    // Data initialization and finalization methods
    //---------------------------------------------------------------------

    protected void initLessonData()
    {

        testLesson = new Lesson(new Date(System.currentTimeMillis()),
                                testUser,
                                Lesson.NOT_STARTED_STATE,
                                testLearningDesign,
                                testLessonClass,//lesson class
                                testOrg,
                                new HashSet());

        testLesson.setLessonClass(createTestLessonClass(testLesson));
    }

    protected void initLessonClassData()
    {
        testLessonClass = new LessonClass(TEST_CLASS_GROUPING_ID, //grouping id
                                          Grouping.CLASS_GROUPING_TYPE,
                                          new HashSet(),//groups
                                          testLearningDesign.getActivities(),
                                          null, //staff group 
                                          testLesson);
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
        Group learnerClassGroup = new Group(null,//group id 
                                            TEST_GROUP_ORDER_ID,
                                            testLessonClass,
                                            testOrg.getUsers(),
                                            new HashSet());//tool session, should be empty now

        learnergroups.add(learnerClassGroup);
    }
    //---------------------------------------------------------------------
    // Helper methods
    //---------------------------------------------------------------------
    /**
     * Create test lesson class based on the give lesson
     * @param lesson the lesson for the class we are going to create
     * @return the new Lesson Class transiant hibernate object
     */
    private LessonClass createTestLessonClass(Lesson lesson)
    {

        return testLessonClass;
    }
}