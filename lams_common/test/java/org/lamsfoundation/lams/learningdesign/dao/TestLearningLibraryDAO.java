/*
 * Created on Dec 4, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.learningdesign.dao;
import org.lamsfoundation.lams.learningdesign.BaseTestCase;

import org.lamsfoundation.lams.learningdesign.LearningLibrary;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.LearningLibraryDAO;


/**
 * @author manpreet
 */
public class TestLearningLibraryDAO extends BaseTestCase {
	
	protected LearningLibraryDAO libraryDAO;
	protected LearningLibrary library;
	
	public void setUp() throws Exception{
		libraryDAO =(LearningLibraryDAO) context.getBean("learningLibraryDAO");
	}
	public void testGetAllLibraries(){
		LearningLibrary lib = libraryDAO.getLearningLibraryById(new Long(8));
		assertNotNull(lib.getTitle());
		System.out.println(lib.getTitle());		
	}
}
