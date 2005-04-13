/*
 * Created on Apr 4, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.workspace;

import java.io.IOException;

import org.lamsfoundation.lams.AbstractLamsTestCase;
import org.lamsfoundation.lams.workspace.service.WorkspaceManagementService;

/**
 * @author Minhas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestWorkspaceManagement extends AbstractLamsTestCase {
	
	protected WorkspaceManagementService workspaceManagementService;
	
	public TestWorkspaceManagement(String name){
		super(name);
	}
	public void setUp() throws Exception {
		super.setUp();		
		workspaceManagementService =(WorkspaceManagementService)context.getBean("workspaceManagementService");
	}
	protected String[] getContextConfigLocation() {
		return new String[] {"WEB-INF/spring/authoringApplicationContext.xml",
							 "WEB-INF/spring/workspaceApplicationContext.xml",
							 "WEB-INF/spring/learningDesignApplicationContext.xml",
							 "WEB-INF/spring/applicationContext.xml"};		
	}
	public void testGetFolderContents()throws IOException{
		String packet = workspaceManagementService.getFolderContents(new Integer(4),new Integer(2),WorkspaceManagementService.AUTHORING);		
		System.out.println(packet);
	}
	public void testCopyFolder() throws IOException{
		String packet = workspaceManagementService.copyFolder(new Integer(2),new Integer(8),new Integer(4));
		System.out.println(packet);
	}

}
