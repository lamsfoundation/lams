/*
 * Created on Dec 4, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.learningdesign.dao;
import org.lamsfoundation.lams.learningdesign.BaseTestCase;
import java.util.Date;
import java.util.List;

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
	
	/*public void testInsert(){
		library = new LearningLibrary();		
		library.setDescription("Description for 2nd Trial");
		library.setTitle("Title for 2nd Trial");
		library.setCreateDateTime(new Date("20040111"));
		
		libraryDAO.insert(library);
		//assertNotNull(libraryDAO.getLearningLibraryByTitle("Title for 1st Trial"));
	}*/
	public void testGetAllLibraries(){
		//List list = libraryDAO.getAllLearningLibraries();
		LearningLibrary lib = libraryDAO.getLearningLibraryById(new Long(8));
		assertNotNull(lib.getTitle());
		System.out.println(lib.getTitle());		
	}
}
