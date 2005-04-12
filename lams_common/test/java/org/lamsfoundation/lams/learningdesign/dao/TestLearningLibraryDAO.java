/*
 * Created on Dec 4, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.learningdesign.dao;


import java.util.Set;

import org.lamsfoundation.lams.AbstractLamsTestCase;
import org.lamsfoundation.lams.learningdesign.LearningLibrary;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.LearningLibraryDAO;


/**
 * @author manpreet
 */
public class TestLearningLibraryDAO extends AbstractLamsTestCase {
	
	protected LearningLibraryDAO libraryDAO;
	protected LearningLibrary library;
	
	public TestLearningLibraryDAO(String name) {
		super(name);
	}
	
	public void setUp() throws Exception{
		super.setUp();
		libraryDAO =(LearningLibraryDAO) context.getBean("learningLibraryDAO");
	}
	public void testGetAllLibraries(){
		LearningLibrary lib = libraryDAO.getLearningLibraryById(new Long(8));
		assertNotNull(lib.getTitle());
		Set set = lib.getActivities();		
		System.out.println(lib.getTitle());
		System.out.println(set.size());
	}
	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.AbstractLamsTestCase#getContextConfigLocation()
	 */
	protected String[] getContextConfigLocation() {
		return new String[] {"/org/lamsfoundation/lams/learningdesign/learningDesignApplicationContext.xml",
		 "applicationContext.xml"};
	}

    /**
     * @see org.lamsfoundation.lams.AbstractLamsTestCase#getHibernateSessionFactoryName()
     */
    protected String getHibernateSessionFactoryName()
    {
        return "coreSessionFactory";
    }
}
