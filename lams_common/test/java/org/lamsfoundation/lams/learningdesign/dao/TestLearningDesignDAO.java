/*
 * Created on Dec 6, 2004
 */
package org.lamsfoundation.lams.learningdesign.dao;

import java.util.Date;
import org.lamsfoundation.lams.AbstractLamsTestCase;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.LearningDesignDAO;
import org.lamsfoundation.lams.usermanagement.User;
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
		userDAO = (UserDAO)context.getBean("userDAO");
	}	
	public void testInsertLearningDesign(){
		LearningDesign design = new LearningDesign();
				
		design.setValidDesign(new Boolean (true));
		design.setReadOnly(new Boolean (false));
		design.setUser(userDAO.getUserById(new Integer(1)));
		design.setCopyTypeID(new Integer(1));
		design.setCreateDateTime(new Date());		
		learningDesignDAO.insert(design);		
	}
	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.AbstractLamsTestCase#getContextConfigLocation()
	 */
	protected String[] getContextConfigLocation() {
		return new String[] {"/org/lamsfoundation/lams/learningdesign/learningDesignApplicationContext.xml",
		 "applicationContext.xml"};
	}
}
