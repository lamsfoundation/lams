/*
 * Created on Dec 4, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.learningdesign.dao;

import java.util.List;

import org.lamsfoundation.lams.learningdesign.LearningLibrary;

/**
 * @author manpreet
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface ILearningLibraryDAO extends IBaseDAO {
	
	public LearningLibrary getLearningLibraryById(Long learningLibraryId);
	public LearningLibrary getLearningLibraryByTitle(String title);
	public List getAllLearningLibraries();

}
