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
package org.lamsfoundation.lams.workspace.dao;

import java.io.FileInputStream;
import java.util.Date;

import org.lamsfoundation.lams.contentrepository.ICredentials;
import org.lamsfoundation.lams.contentrepository.ITicket;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.SimpleCredentials;
import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;
import org.lamsfoundation.lams.workspace.WorkspaceFolderContent;
import org.lamsfoundation.lams.workspace.service.BaseWorkspaceTest;

/**
 * @author Manpreet Minhas
 */
public class TestWorkspaceFolderContentDAO extends BaseWorkspaceTest {
	
	protected WorkspaceFolder workspaceFolder;
	
	protected String workspaceUser = "workspaceManager";
	protected String password = "flashClient";
	protected String workspaceName = "FlashClientsWorkspace";
	
	protected ICredentials credentials;
	protected ITicket ticket;
	
	public TestWorkspaceFolderContentDAO(String name){
		super(name);
	}

	public void testInsertWorkspaceFolderContent(){			
		workspaceFolder = workspaceFolderDAO.getWorkspaceFolderByID(new Integer(4));
		WorkspaceFolderContent workspaceFolderContent = new WorkspaceFolderContent(
				WorkspaceFolderContent.CONTENT_TYPE_FILE,
				testFileString, "Test Description",
				new Date(), new Date(),
				"TXT",workspaceFolder);
		workspaceFolderContentDAO.insert(workspaceFolderContent);
		assertNotNull(workspaceFolderContent.getFolderContentID());
	}
	public void testAddFlashClientWorkspaceCredentials()throws Exception{		
		credentials = new SimpleCredentials(workspaceUser,password.toCharArray());
		//repositoryService.createCredentials(credentials);
		//repositoryService.addWorkspace(credentials,workspaceName);		
		ticket = repositoryService.login(credentials,workspaceName);
		assertNotNull("Add workspace succeeded - can login to workspace. Ticket is "+ticket, ticket);
		testAddFile(ticket);
	}
	private void testAddFile(ITicket ticket)throws Exception{
		WorkspaceFolderContent workspaceFolderContent = workspaceFolderContentDAO.getWorkspaceFolderContentByID(new Long(7));
		FileInputStream input = new FileInputStream(testFileString);
		NodeKey nodeKey = repositoryService.addFileItem(ticket,input,workspaceFolderContent.getName(),workspaceFolderContent.getMimeType(),null);
		workspaceFolderContent.setUuid(nodeKey.getUuid());
		workspaceFolderContent.setVersionID(nodeKey.getVersion());
		workspaceFolderContentDAO.update(workspaceFolderContent);
		assertNotNull(workspaceFolderContent.getUuid());
	}
	public void testUpdateFile() throws Exception{
		WorkspaceFolderContent workspaceFolderContent = workspaceFolderContentDAO.getWorkspaceFolderContentByID(new Long(7));
		FileInputStream input = new FileInputStream(testFileString);
		credentials = new SimpleCredentials(workspaceUser,password.toCharArray());
		ticket = repositoryService.login(credentials,workspaceName);
		NodeKey nodeKey = repositoryService.updateFileItem(ticket,workspaceFolderContent.getUuid(),workspaceFolderContent.getName(),
										input,workspaceFolderContent.getMimeType(),null);
		workspaceFolderContent.setVersionID(nodeKey.getVersion());
		workspaceFolderContent.setUuid(nodeKey.getUuid());
		workspaceFolderContentDAO.update(workspaceFolderContent);
		System.out.println(nodeKey.getVersion() + ": " + workspaceFolderContent.getVersionID());
	}	
}
