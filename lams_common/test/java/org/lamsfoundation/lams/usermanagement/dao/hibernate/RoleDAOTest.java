/*
 * Created on Nov 26, 2004
 *
 * Last modified on Nov 26, 2004
 */
package org.lamsfoundation.lams.usermanagement.dao.hibernate;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import org.lamsfoundation.lams.usermanagement.Role;

import junit.framework.TestCase;

/**
 * TODO Add description here
 *
 * <p>
 * <a href="RoleDAOTest.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class RoleDAOTest extends TestCase {

	private Role role = null;
	private String errorMessage = "";
	private RoleDAO roleDAO = null;
	private ApplicationContext ctx;
	
	protected void setUp() throws Exception{
		ctx = new FileSystemXmlApplicationContext("applicationContext.xml");
		roleDAO = (RoleDAO)ctx.getBean("roleDAO");
	}
	
	protected void tearDown() throws Exception{
		roleDAO = null;
	}
	
	public void testGetAllRoles(){
		assertTrue(roleDAO.getAllRoles().size()==5);
	}

	public void testGetRoleById(){
		errorMessage = "The name of the role gotten by Id 1 is not SYSADMIN";
		role = roleDAO.getRoleById(new Integer(1));
		assertEquals(errorMessage,"SYSADMIN",role.getName());
	}
	
	public void testGetRoleByName(){
		errorMessage = "The Id of the role gotten by name 'SYSADMIN' is not 1";
		role = roleDAO.getRoleByName("SYSADMIN");
		assertEquals(errorMessage,new Integer(1),role.getRoleId());
	}
}
