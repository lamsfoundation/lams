/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
package org.lamsfoundation.lams.learningdesign.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.LearningLibrary;
import org.lamsfoundation.lams.learningdesign.dao.ILearningLibraryDAO;

/**
 * @author Manpreet Minhas
 */
public class LearningLibraryDAO extends BaseDAO implements ILearningLibraryDAO {

	private static final String FIND_VALID_LIB ="from "+LearningLibrary.class.getName()
		+" l where l.validLibrary=true";

	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.learningdesign.dao.interfaces.ILearningLibraryDAO#getLearningLibraryById(java.lang.Long)
	 */
	public LearningLibrary getLearningLibraryById(Long learningLibraryId) {
		return (LearningLibrary)super.find(LearningLibrary.class,learningLibraryId);
	}

	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.learningdesign.dao.interfaces.ILearningLibraryDAO#getAllLearningLibraries()
	 */
	public List getAllLearningLibraries() {
			return getSession().createQuery(FIND_VALID_LIB).list();
	}
}
