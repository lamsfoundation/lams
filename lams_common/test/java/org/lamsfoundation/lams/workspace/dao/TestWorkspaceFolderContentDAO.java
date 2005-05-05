/*
 * Created on Apr 20, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.workspace.dao;

import java.io.FileInputStream;
import java.util.Date;
import java.util.List;
import org.lamsfoundation.lams.AbstractLamsTestCase;
import org.lamsfoundation.lams.contentrepository.ICredentials;
import org.lamsfoundation.lams.contentrepository.ITicket;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.SimpleCredentials;
import org.lamsfoundation.lams.contentrepository.service.IRepositoryService;
import org.lamsfoundation.lams.contentrepository.service.RepositoryProxy;
import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;
import org.lamsfoundation.lams.usermanagement.dao.IWorkspaceFolderDAO;
import org.lamsfoundation.lams.workspace.WorkspaceFolderContent;

/**
 * @author Manpreet Minhas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestWorkspaceFolderContentDAO extends AbstractLamsTestCase {
	
	protected IWorkspaceFolderContentDAO workspaceFolderContentDAO;
	protected IWorkspaceFolderDAO workspaceFolderDAO;
	protected WorkspaceFolder workspaceFolder;
	protected IRepositoryService repositoryService;	
	
	protected String workspaceUser = "workspaceManager";
	protected String password = "flashClient";
	protected String workspaceName = "FlashClientsWorkspace";
	
	protected ICredentials credentials;
	protected ITicket ticket;
	
	public TestWorkspaceFolderContentDAO(String name){
		super(name);
	}
	public void setUp()throws Exception{
		super.setUp();
		workspaceFolderContentDAO = (IWorkspaceFolderContentDAO)context.getBean("workspaceFolderContentDAO");
		workspaceFolderDAO = (IWorkspaceFolderDAO)context.getBean("workspaceFolderDAO");				
		repositoryService = RepositoryProxy.getLocalRepositoryService();
	}

	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.AbstractLamsTestCase#getContextConfigLocation()
	 */
	protected String[] getContextConfigLocation() {
		return new String[] {"WEB-INF/spring/applicationContext.xml",
				"org/lamsfoundation/lams/contentrepository/contentRepositoryLocalApplicationContext.xml"};
	}

	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.AbstractLamsTestCase#getHibernateSessionFactoryName()
	 */
	protected String getHibernateSessionFactoryName() {
		return "coreSessionFactory";
	}	
	public void testInsertWorkspaceFolderContent(){		
		workspaceFolder = workspaceFolderDAO.getWorkspaceFolderByID(new Integer(4));
		WorkspaceFolderContent workspaceFolderContent = new WorkspaceFolderContent(new Integer(1),
																				   "Test File","Test Description",
																				   new Date(), new Date(),
																				   "TXT",workspaceFolder);
		workspaceFolderContentDAO.insert(workspaceFolderContent);
		assertNotNull(workspaceFolderContent.getFolderContentID());
	}
	public void testGetContentByWorkspaceFolder(){
		List list = workspaceFolderContentDAO.getContentByWorkspaceFolder(new Long(3));		
		assertEquals(list.size(),4);
	}	
	public void testGetContentByTypeFromWorkspaceFolder(){
		List list = workspaceFolderContentDAO.getContentByTypeFromWorkspaceFolder(new Long(3),"TXT");
		assertEquals(list.size(),2);		
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
		FileInputStream input = new FileInputStream("C:/test/Text File.txt");
		NodeKey nodeKey = repositoryService.addFileItem(ticket,input,workspaceFolderContent.getName(),workspaceFolderContent.getMimeType(),null);
		workspaceFolderContent.setUuid(nodeKey.getUuid());
		workspaceFolderContent.setVersionID(nodeKey.getVersion());
		workspaceFolderContentDAO.update(workspaceFolderContent);
		assertNotNull(workspaceFolderContent.getUuid());
	}
	public void testUpdateFile() throws Exception{
		WorkspaceFolderContent workspaceFolderContent = workspaceFolderContentDAO.getWorkspaceFolderContentByID(new Long(7));
		FileInputStream input = new FileInputStream("C:/test/Text File.txt");
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
