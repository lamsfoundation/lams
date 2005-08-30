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
package org.lamsfoundation.lams.usermanagement.dao.hibernate;

import java.util.Date;

import org.lamsfoundation.lams.test.AbstractCommonTestCase;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.UserOrganisation;
import org.lamsfoundation.lams.usermanagement.UserOrganisationRole;


/**
 * <p>
 * <a href="UserDAOTest.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class UserDAOTest extends AbstractCommonTestCase {
	private User user = null;
	private UserDAO userDAO = null;
	protected RoleDAO roleDAO;
	protected OrganisationDAO organisationDAO;
	protected OrganisationTypeDAO organisationTypeDAO;	
	protected AuthenticationMethodDAO authenticationMethodDAO;
	
	protected UserOrganisationDAO userOrganisationDAO;
	protected UserOrganisationRoleDAO userOrganisationRoleDAO;
	
	
	public UserDAOTest(String name){
		super(name);
	}	
	protected void setUp() throws Exception{	
		super.setUp();
		userDAO = (UserDAO)context.getBean("userDAO");
		organisationDAO = (OrganisationDAO)context.getBean("organisationDAO");	
		
		organisationTypeDAO =(OrganisationTypeDAO)context.getBean("organisationTypeDAO");		
		authenticationMethodDAO =(AuthenticationMethodDAO)context.getBean("authenticationMethodDAO");
		
		roleDAO = (RoleDAO)context.getBean("roleDAO");		
		userOrganisationDAO = (UserOrganisationDAO)context.getBean("userOrganisationDAO");
		userOrganisationRoleDAO = (UserOrganisationRoleDAO)context.getBean("userOrganisationRoleDAO");
	}	
	public void testGetUser(){
		user = userDAO.getUserById(new Integer(2));
		assertNotNull(user.getLogin());	
	}
	public void testIsMember(){
		Organisation organisation = organisationDAO.getOrganisationById(new Integer(4));
		user = userDAO.getUserById(new Integer(4));		
		boolean member = user.isMember(organisation);
		assertTrue(member);
	}
	public void testSaveUser(){
		User user = new User();
		user.setLogin("MiniMinhas");
		user.setPassword("MiniMinhas");
		user.setDisabledFlag(new Boolean(false));
		user.setCreateDate(new Date());
		user.setAuthenticationMethod(authenticationMethodDAO.getAuthenticationMethodById(new Integer(2)));
		user.setBaseOrganisation(organisationDAO.getOrganisationById(new Integer(1)));		
		userDAO.saveUser(user);
		createUserOrganisation(user);
	}
	private Integer createUserOrganisation(User user){
		UserOrganisation userOrganisation = new UserOrganisation();
		userOrganisation.setOrganisation(user.getBaseOrganisation());
		userOrganisation.setUser(user);		
		userOrganisationDAO.saveUserOrganisation(userOrganisation);	
		userOrganisation.addUserOrganisationRole(createUserOrganisationRole(userOrganisation));
		userOrganisationDAO.saveOrUpdateUserOrganisation(userOrganisation);
		return userOrganisation.getUserOrganisationId();
	}
	private UserOrganisationRole createUserOrganisationRole(UserOrganisation userOrganisation){
		UserOrganisationRole userOrganisationRole = new UserOrganisationRole();
		userOrganisationRole.setUserOrganisation(userOrganisation);
		userOrganisationRole.setRole(roleDAO.getRoleByName(Role.STAFF));
		userOrganisationRoleDAO.saveUserOrganisationRole(userOrganisationRole);
		return userOrganisationRole;
	}	
}
