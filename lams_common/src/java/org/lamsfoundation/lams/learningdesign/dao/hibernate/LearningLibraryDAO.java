/*
 * Created on Dec 4, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.learningdesign.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.learningdesign.LearningLibrary;
import org.lamsfoundation.lams.learningdesign.dao.ILearningLibraryDAO;

/**
 * @author manpreet
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LearningLibraryDAO extends BaseDAO implements ILearningLibraryDAO {

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.learningdesign.dao.interfaces.ILearningLibraryDAO#getLearningLibraryById(java.lang.Long)
	 */
	public LearningLibrary getLearningLibraryById(Long learningLibraryId) {
		return (LearningLibrary)super.find(LearningLibrary.class,learningLibraryId);
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.learningdesign.dao.interfaces.ILearningLibraryDAO#getLearningLibraryByTitle(java.lang.String)
	 */
	public LearningLibrary getLearningLibraryByTitle(String title) {
		return (LearningLibrary)super.find(LearningLibrary.class,title);
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.learningdesign.dao.interfaces.ILearningLibraryDAO#getAllLearningLibraries()
	 */
	public List getAllLearningLibraries() {
		return super.findAll(LearningLibrary.class);
	}
}
