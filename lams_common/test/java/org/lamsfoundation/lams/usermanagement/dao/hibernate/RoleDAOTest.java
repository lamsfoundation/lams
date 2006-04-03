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
package org.lamsfoundation.lams.usermanagement.dao.hibernate;
import org.lamsfoundation.lams.test.AbstractCommonTestCase;
import org.lamsfoundation.lams.usermanagement.Role;
import org.springframework.context.ApplicationContext;


/**
 * <p>
 * <a href="RoleDAOTest.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class RoleDAOTest extends AbstractCommonTestCase {

	private Role role = null;
	private String errorMessage = "";
	private RoleDAO roleDAO = null;
	private ApplicationContext ctx;
	
	public RoleDAOTest(String name){
		super(name);
	}
	protected void setUp() throws Exception{
		super.setUp();		
		roleDAO = (RoleDAO)context.getBean("roleDAO");
	}
	
	protected void tearDown() throws Exception{
		roleDAO = null;
	}
	
	public void testGetAllRoles(){
		assertTrue(roleDAO.getAllRoles().size()>0);
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
