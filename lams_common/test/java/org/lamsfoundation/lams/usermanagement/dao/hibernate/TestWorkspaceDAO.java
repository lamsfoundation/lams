/*
 * Created on Apr 15, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.usermanagement.dao.hibernate;

import org.lamsfoundation.lams.AbstractLamsTestCase;
import org.lamsfoundation.lams.usermanagement.Workspace;
import org.lamsfoundation.lams.usermanagement.dao.IWorkspaceDAO;

/**
 * @author Minhas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestWorkspaceDAO extends AbstractLamsTestCase {
	
	protected IWorkspaceDAO workspaceDAO;
	
	public TestWorkspaceDAO(String name){
		super(name);
	}
	protected String[] getContextConfigLocation() {
		return new String[] {"org/lamsfoundation/lams/applicationContext.xml"};
	}	
	protected String getHibernateSessionFactoryName() {
		return "coreSessionFactory";
	}
	protected void setUp() throws Exception{	
		super.setUp();
		workspaceDAO =(IWorkspaceDAO)context.getBean("userManagementWorkspaceDAO");
	}
	public void testGetWorkspaceByRootFolderID(){
		boolean rootFolder = false;
		Workspace workspace =workspaceDAO.getWorkspaceByRootFolderID(new Integer(6));
		if(workspace!=null)
			rootFolder=true;
		assertTrue(rootFolder);
	}
}
