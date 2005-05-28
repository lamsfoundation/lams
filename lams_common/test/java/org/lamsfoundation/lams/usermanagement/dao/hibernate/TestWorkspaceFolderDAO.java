/*
 * Created on May 28, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.usermanagement.dao.hibernate;

import org.lamsfoundation.lams.AbstractLamsTestCase;
import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;

/**
 * @author Manpreet Minhas
 */
public class TestWorkspaceFolderDAO extends AbstractLamsTestCase {
	
	protected WorkspaceFolderDAO workspaceFolderDAO;
	
	public TestWorkspaceFolderDAO(String name){
		super(name);
	}
	protected void setUp()throws Exception{
		super.setUp();
		workspaceFolderDAO = (WorkspaceFolderDAO)context.getBean("workspaceFolderDAO");
	}

	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.AbstractLamsTestCase#getContextConfigLocation()
	 */
	protected String[] getContextConfigLocation() {
		return new String[] {"org/lamsfoundation/lams/applicationContext.xml"};
	}

	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.AbstractLamsTestCase#getHibernateSessionFactoryName()
	 */
	protected String getHibernateSessionFactoryName() {
		return "coreSessionFactory";
	}
	public void testGetRunSequencesFolderForUser(){
		WorkspaceFolder folder = workspaceFolderDAO.getRunSequencesFolderForUser(new Integer(4));
		assertEquals(folder.getWorkspaceFolderId(),new Integer(7));
	}

}
