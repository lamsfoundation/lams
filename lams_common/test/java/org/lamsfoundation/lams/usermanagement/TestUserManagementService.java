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
package org.lamsfoundation.lams.usermanagement;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.lamsfoundation.lams.contentrepository.dao.IWorkspaceDAO;
import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.test.AbstractCommonTestCase;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.UserManagementService;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

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
		
		Workspace workspace =  new Workspace(WORKSPACE_NAME);
		userManagementService.save(workspace);
		Integer workspaceId = workspace.getWorkspaceId();

		// set up the root folder
		WorkspaceFolder rootWorkspaceFolder = new WorkspaceFolder(workspace.getName(),workspace.getWorkspaceId(),
				MMM_USER_ID, new Date(), new Date(), WorkspaceFolder.NORMAL);
		userManagementService.save(rootWorkspaceFolder);
		workspace.setRootFolder(rootWorkspaceFolder);
		
		// set up the default workspace folder, under the root folder
		String sequenceFolderName = workspace.getName()+"_Sequences";
		WorkspaceFolder defRunSequencesFolder = new WorkspaceFolder(sequenceFolderName,workspace.getWorkspaceId(),
				MMM_USER_ID,new Date(), new Date(), WorkspaceFolder.RUN_SEQUENCES);
		userManagementService.save(defRunSequencesFolder);
		workspace.setDefaultRunSequencesFolder(defRunSequencesFolder);
		userManagementService.save(workspace);

		// is it all okay in the db? Not 100% test as this just checks what Hibernate has, not what the db has.
		Workspace retrievedWorkspace = (Workspace) baseDAO.find(Workspace.class, workspaceId);
		assertNotNull("Retrieved saved workspace id="+workspaceId, retrievedWorkspace);
		assertEquals("Workspace name as expected "+WORKSPACE_NAME, WORKSPACE_NAME, retrievedWorkspace.getName());
		assertNotNull("Root folder exists",retrievedWorkspace.getRootFolder());
		assertEquals("Root folder has expected id "+rootWorkspaceFolder.getWorkspaceFolderId(), rootWorkspaceFolder.getWorkspaceFolderId(), retrievedWorkspace.getRootFolder().getWorkspaceFolderId());
		assertNotNull("Default run sequences folder exists", retrievedWorkspace.getDefaultRunSequencesFolder());
		assertEquals("Default run sequences folder has expected id "+defRunSequencesFolder.getWorkspaceFolderId(), defRunSequencesFolder.getWorkspaceFolderId(), retrievedWorkspace.getDefaultRunSequencesFolder().getWorkspaceFolderId());
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
