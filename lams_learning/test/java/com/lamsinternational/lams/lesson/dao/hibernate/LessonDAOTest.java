/*
 * Created on Nov 26, 2004
 *
 * Last modified on Nov 26, 2004
 */
package com.lamsinternational.lams.lesson.dao.hibernate;

import java.util.Iterator;

import net.sf.hibernate.LockMode;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;

import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learning.service.TestLearnerService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.orm.hibernate.SessionFactoryUtils;
import org.springframework.orm.hibernate.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.lamsinternational.lams.BaseTestCase;
import com.lamsinternational.lams.lesson.*;
import com.lamsinternational.lams.lesson.dao.ILessonDAO;
import com.lamsinternational.lams.usermanagement.User;

import junit.framework.TestCase;

/**
 * TODO Add description here
 *
 * <p>
 * <a href="OrganisationDAOTest.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class LessonDAOTest extends BaseTestCase {
	
	private static String CONFIG_PATH = "test/java/";
	
	private String errorMessage = "";
	private ILessonDAO lessonDAO = null;
	private ApplicationContext ctx;
	
	protected void setUp() throws Exception{
		super.setUp();
		lessonDAO = (LessonDAO)getBean("lessonDAO");
	}
	
	protected void tearDown() throws Exception{
		lessonDAO = null;
	    super.tearDown();
	}

	public void testGetLesson() throws Exception {
		ILearnerService learnerService = (ILearnerService)getBean("learnerService");
		Lesson lesson = lessonDAO.getLesson(new Long(1));
		boolean success = lesson != null;
		Iterator i = lesson.getLearners().iterator();
		while (i.hasNext()) {
			User user = (User)i.next();
			success = success && (user != null);
			System.out.println(user.getUserId());
		}
		assertTrue(success);
	}

}
