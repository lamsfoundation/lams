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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.lesson.service;

import java.util.HashSet;
import java.util.Set;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.LessonClass;
import org.lamsfoundation.lams.lesson.dao.ILessonClassDAO;
import org.lamsfoundation.lams.lesson.dao.ILessonDAO;
import org.lamsfoundation.lams.test.AbstractLamsTestCase;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.usermanagement.service.UserManagementService;

public class TestLessonService extends AbstractLamsTestCase {
	
	private ILessonDAO lessonDAO;
	private ILessonClassDAO lessonClassDAO;
	private ILessonService lessonService;
	private IBaseDAO baseDAO;
	protected IUserManagementService userManagementService;
	
	private static final Long TEST_ORG_ID = new Long(1);
	protected static final String MMM_USER_LOGIN = "mmm";
	protected static final String TEST1_USER_LOGIN = "test1";
	
	public TestLessonService(String name) {
		super(name);
	}
		
	protected void setUp()throws Exception{
		super.setUp();
		lessonService =(ILessonService)context.getBean("lessonService");
		lessonDAO =(ILessonDAO)context.getBean("lessonDAO");
		lessonClassDAO =(ILessonClassDAO)context.getBean("lessonClassDAO");
		baseDAO =(IBaseDAO)context.getBean("baseDAO");
		userManagementService = (UserManagementService)context.getBean("userManagementService");
	}
	
	protected String getHibernateSessionFactoryName() {
		return "coreSessionFactory";		
	}
	protected String[] getContextConfigLocation() {
		return new String[] {"org/lamsfoundation/lams/localApplicationContext.xml",
				 "org/lamsfoundation/lams/lesson/lessonApplicationContext.xml"};
	}

	public void testAddUser() throws Exception {
		
		// set up the lesson and lesson class
		
		User mmm = userManagementService.getUserByLogin(MMM_USER_LOGIN);
		User test1 = userManagementService.getUserByLogin(TEST1_USER_LOGIN);

		// this lesson isn't valid as it doesn't have a learning design
	      Lesson newLesson = Lesson.createNewLessonWithoutClass("test lesson", "test lesson", mmm, true, null);
	      lessonDAO.saveLesson(newLesson);
	      
	      Organisation organisation = (Organisation) baseDAO.find(Organisation.class, TEST_ORG_ID);

	      Set<User> staff = new HashSet<User>();
	      staff.add(mmm);
	      
	       LessonClass newLessonClass = new LessonClass(null, //grouping id
                   new HashSet(),//groups
                   null, // activities 
                   null, //staff group 
                   newLesson);

	      lessonClassDAO.saveLessonClass(newLessonClass);

	      //setup staff group
	      newLessonClass.setStaffGroup(Group.createStaffGroup(newLessonClass,"staffGroupName",
	                                                            new HashSet(staff)));
	      //setup learner group
	      newLessonClass.getGroups()
	               .add(Group.createLearnerGroup(newLessonClass, "learnerGroupName", 
	            		   	new HashSet()));
	        
	      newLesson.setLessonClass(newLessonClass);
	      newLesson.setOrganisation(organisation);
	        
	      lessonClassDAO.updateLessonClass(newLessonClass);

	      lessonDAO.updateLesson(newLesson);
	      
	      // add some users.
	      checkCount(newLessonClass, "initial setup", 0,1);
	      
	      lessonService.addLearner(newLesson.getLessonId(), mmm);
	      
	      lessonService.addLearner(newLesson.getLessonId(), test1);
	      checkCount(newLessonClass, "added test1 to learner:", 1,1);
	      lessonService.addStaffMember(newLesson.getLessonId(), test1);
	      checkCount(newLessonClass, "added test1 to staff:", 2,1);
	       
	      lessonService.addLearner(newLesson.getLessonId(), test1);
	      checkCount(newLessonClass, "added test1 to learner again:", 1,1); // no change as already in group
	      lessonService.addStaffMember(newLesson.getLessonId(), test1);
	      checkCount(newLessonClass, "added test1 to staff:", 2,1); // no change as already in group
	      
 	  }

	private void checkCount(LessonClass lessonClass, String test, int numberLearners, int numberStaff) {
	  assertEquals(test+": Expected 1 lesson group", lessonClass.getGroups().size(), 1);
      assertEquals(test+": Expected number of learners", lessonClass.getLearners().size(), numberLearners);
      assertEquals(test+": Expected number of staff", lessonClass.getStaffGroup().getUsers().size(), numberStaff);
	}
	
}
