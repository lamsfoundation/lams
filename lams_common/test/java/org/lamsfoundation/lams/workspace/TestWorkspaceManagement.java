/*
 * Created on Apr 4, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.workspace;

import java.io.IOException;
import java.util.Date;

import org.lamsfoundation.lams.AbstractLamsTestCase;
import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;
import org.lamsfoundation.lams.usermanagement.dao.IWorkspaceFolderDAO;
import org.lamsfoundation.lams.workspace.service.WorkspaceManagementService;

/**
 * @author Minhas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestWorkspaceManagement extends AbstractLamsTestCase {
	
	protected WorkspaceManagementService workspaceManagementService;
	protected IWorkspaceFolderDAO workspaceFolderDAO;
	
	public TestWorkspaceManagement(String name){
		super(name);
	}
	public void setUp() throws Exception {
		super.setUp();		
		workspaceManagementService =(WorkspaceManagementService)context.getBean("workspaceManagementService");
		workspaceFolderDAO = (IWorkspaceFolderDAO)context.getBean("workspaceFolderDAO");
	}
	protected String[] getContextConfigLocation() {
		return new String[] {"WEB-INF/spring/authoringApplicationContext.xml",
							 "WEB-INF/spring/workspaceApplicationContext.xml",
							 "WEB-INF/spring/learningDesignApplicationContext.xml",
							 "WEB-INF/spring/applicationContext.xml"};		
	}
	protected String getHibernateSessionFactoryName() {
		return "coreSessionFactory";
	}
	public void testGetFolderContents()throws Exception{
		String packet = workspaceManagementService.getFolderContents(new Integer(4),new Integer(4),WorkspaceManagementService.AUTHORING);		
		System.out.println(packet);
	}
	public void testCopyFolder() throws IOException{
		String packet = workspaceManagementService.copyFolder(new Integer(2),new Integer(8),new Integer(4));
		System.out.println(packet);
	}
	public void testDeleteFolder() throws IOException{
		String message = workspaceManagementService.deleteFolder(new Integer(7), new Integer(4));
		System.out.println(message);
	}
	public void testDeleteLearningDesign() throws Exception{
		String message = workspaceManagementService.deleteLearningDesign(new Long(1), new Integer(4));
		System.out.println(message);
	}
	public void testMoveFolder()throws Exception{
		String message = workspaceManagementService.moveFolder(new Integer(6),new Integer(2), new Integer(4));
		System.out.println(message);
		WorkspaceFolder workspaceFolder = workspaceFolderDAO.getWorkspaceFolderByID(new Integer(6));
		assertTrue(workspaceFolder.getParentWorkspaceFolder().getWorkspaceFolderId().equals(new Integer(2)));
	}
	public void testCreateWorkspaceFolderContent()throws Exception{
		String message = workspaceManagementService.createWorkspaceFolderContent(new Integer(1),
																"Manpreet Minhas","Manpreet's Description",
																new Date(),new Date(),
																new Integer(3),
																"TXT","c:/MMinhas.TXT");
		System.out.println(message);
	}
	public void testUpdateWorkspaceFolderContent() throws Exception{
		String message = workspaceManagementService.updateWorkspaceFolderContent(new Long(7),"c:/MMinhas2.txt");
		System.out.println(message);
	}
	public void testDeleteWorkspaceFolderContent() throws Exception{
		String message = workspaceManagementService.deleteWorkspaceFolderContent(new Long(7));
		System.out.println(message);
	}
}
