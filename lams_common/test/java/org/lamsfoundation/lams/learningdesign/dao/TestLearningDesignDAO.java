/*
 * Created on Dec 6, 2004
 */
package org.lamsfoundation.lams.learningdesign.dao;

import org.lamsfoundation.lams.AbstractLamsTestCase;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.LearningDesignDAO;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;
import org.lamsfoundation.lams.usermanagement.dao.hibernate.UserDAO;
import org.lamsfoundation.lams.usermanagement.dao.hibernate.WorkspaceFolderDAO;

/**
 * @author MMINHAS
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestLearningDesignDAO extends AbstractLamsTestCase {
	
	
	private LearningDesignDAO learningDesignDAO;	
	protected WorkspaceFolderDAO workspaceFolderDAO;
	private UserDAO userDAO;
	private User user;
	private LearningDesign learningDesign;
	
	public TestLearningDesignDAO(String name) {
		super(name);
	}	
	public void setUp() throws Exception{
		super.setUp();
		learningDesignDAO =(LearningDesignDAO)context.getBean("learningDesignDAO");
		workspaceFolderDAO =(WorkspaceFolderDAO)context.getBean("workspaceFolderDAO");
	}
	/*public void testGetLearningDesignByUserId(){
		List list = learningDesignDAO.getLearningDesignByUserId(new Long(1));
		LearningDesign d = (LearningDesign) list.get(0);
		System.out.println("Size: " + list.size());
		System.out.println("Title: " + d.getTitle());
		assertNotNull(list);
	}*/
	/*public TestLearningDesignDAO(){
		learningDesignDAO =(LearningDesignDAO)context.getBean("learningDesignDAO");		
		userDAO =(UserDAO)context.getBean("userDAO");
		initUserData();
		initLearningDesignData();
		
	}
	public void initUserData(){
		user = userDAO.getUserById(new Integer(1));
	}
	public void initLearningDesignData(){
		learningDesign = new LearningDesign();
		learningDesign.setId(new Integer(20));		
		learningDesign.setTitle("Title for 20");	
		learningDesign.setDescription("Description for 20");
		learningDesign.setFirstActivityId(new Long(1));
		learningDesign.setMaxId(new Integer(4));
		learningDesign.setDateReadOnly(new Date());
		learningDesign.setReadAccess(new Long(1));
		learningDesign.setWriteAccess(new Long(1));
		learningDesign.setHelpText("Help");
		learningDesign.setOpenDateTime(new Date());
		learningDesign.setCloseDateTime(new Date());
		learningDesign.setValidDesign(new Boolean(true));
		learningDesign.setReadOnly(new Boolean(false));
		learningDesign.setLessonCopy(new Boolean(false));
		learningDesign.setCreateDateTime(new Date());
		learningDesign.setVersion("1.1");
		learningDesign.setLearningDesign(null);
		learningDesign.setUser(user);		
	}
	
	public void testSaveLearningDesign(){		
		learningDesignDAO.insert(learningDesign);		
		//assertNotNull(learningDesignDAO.getLearningDesignById(new Long(20)));
	}
	/*public void testDeleteLearningDesign(){
		learningDesignDAO.delete(learningDesignDAO.getLearningDesignById(new Long(20)));		
	}	
	public void testGetLearningDesignByID(){
		learningDesign = learningDesignDAO.getLearningDesignById(new Long(1));
		assertNotNull(learningDesign);		
	}*/
	public void testWorkspaceFolderDAO(){
		WorkspaceFolder workspaceFolder = workspaceFolderDAO.getWorkspaceFolderByID(new Integer(1));
		System.out.print(workspaceFolder.getName());		
		
	}
	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.AbstractLamsTestCase#getContextConfigLocation()
	 */
	protected String[] getContextConfigLocation() {
		return new String[] {"/org/lamsfoundation/lams/learningdesign/learningDesignApplicationContext.xml",
		 "applicationContext.xml"};
	}
}
