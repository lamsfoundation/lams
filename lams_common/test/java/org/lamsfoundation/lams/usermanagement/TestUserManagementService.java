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
/* $Id$ */
package org.lamsfoundation.lams.usermanagement;

import java.util.Date;
import java.util.HashSet;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.test.AbstractCommonTestCase;
import org.lamsfoundation.lams.usermanagement.service.UserManagementService;

/**
 * @author Manpreet Minhas
 */
public class TestUserManagementService extends AbstractCommonTestCase {
	
	protected UserManagementService userManagementService;
	protected static final String WORKSPACE_NAME = "Test Workspace";
	protected static final Integer MMM_USER_ID = new Integer(4);
	
	private IBaseDAO baseDAO;
	
	public TestUserManagementService(String name){
		super(name);
	}
	protected void setUp()throws Exception{
		super.setUp();
		baseDAO =(BaseDAO)context.getBean("baseDAO");
		userManagementService = (UserManagementService)context.getBean("userManagementService");
	}

	public void testCreateWorkspace(){
		Long now = new Long(System.currentTimeMillis());
		String name = WORKSPACE_NAME + now.toString();
		Workspace workspace = userManagementService.createWorkspaceForOrganisation(name, MMM_USER_ID, new Date());

		// is it all okay in the db? Not 100% test as this just checks what Hibernate has, not what the db has.
		Workspace retrievedWorkspace = (Workspace) baseDAO.find(Workspace.class, workspace.getWorkspaceId());
		assertNotNull("Retrieved saved workspace id="+workspace.getWorkspaceId(), retrievedWorkspace);
		assertEquals("Workspace name as expected "+name, name, retrievedWorkspace.getName());
		assertNotNull("Root folder exists",retrievedWorkspace.getDefaultFolder());
		assertNotNull("Default run sequences folder exists", retrievedWorkspace.getDefaultRunSequencesFolder());

	}
/*	public void testSaveOrganisation(){
		Organisation organisation = new Organisation("Test Organisation",
													 "Test Organisation Description",													
													 new Date(),													 
													 organisationTypeDAO.getOrganisationTypeById(new Integer(1)));
		
		Integer organisationID = userManagementService.saveOrganisation(organisation,new Integer(1));		
		assertNotNull(organisationID);
	}
	public void testSaveUser(){
		User user = new User();
		user.setLogin("Monu");
		user.setPassword("Monu");
		user.setDisabledFlag(new Boolean(false));
		user.setCreateDate(new Date());
		user.setAuthenticationMethod(authenticationMethodDAO.getAuthenticationMethodById(new Integer(2)));
		assertNotNull(userManagementService.saveUser(user, new Integer(3)));		
	}*/
	/**
	public void testGetUsersFromOrganisationByRole() throws IOException{
		String packet = userManagementService.getUsersFromOrganisationByRole(new Integer(4),"AUTHOR");
		System.out.println(packet);
	}
	*/
}
