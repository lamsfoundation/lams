/*
 * Created on Nov 22, 2004
 *
 * Last modified on Nov 22, 2004
 */
package org.lamsfoundation.lams.usermanagement.dao.hibernate;

import junit.framework.TestCase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import org.lamsfoundation.lams.usermanagement.User;

/**
 * TODO Add description here
 *
 * <p>
 * <a href="UserDAOTest.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class UserDAOTest extends TestCase {
	private User user = null;
	private UserDAO userDAO = null;
	private ApplicationContext ctx;
	
	protected void setUp() throws Exception{
		ctx = new FileSystemXmlApplicationContext("applicationContext.xml");
		userDAO = (UserDAO)ctx.getBean("userDAO");
	}
	
	protected void tearDown() throws Exception{
		userDAO = null;
	}
	
	public void testGetAllUsers(){
		assertTrue(userDAO.getAllUsers().size()==0);
	}
	
}
