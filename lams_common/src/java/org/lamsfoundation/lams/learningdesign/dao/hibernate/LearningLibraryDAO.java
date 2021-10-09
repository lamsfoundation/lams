/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.learningdesign.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.learningdesign.LearningLibrary;
import org.lamsfoundation.lams.learningdesign.dao.ILearningLibraryDAO;
import org.springframework.stereotype.Repository;

/**
 * @author Manpreet Minhas
 */
@Repository
public class LearningLibraryDAO extends LAMSBaseDAO implements ILearningLibraryDAO {

    private static final String FIND_VALID_LIB = "from " + LearningLibrary.class.getName()
	    + " l where l.validLibrary=true";
    private static final String FIND_ALL_LIB = "from " + LearningLibrary.class.getName();

    @Override
    public LearningLibrary getLearningLibraryById(Long learningLibraryId) {
	return super.find(LearningLibrary.class, learningLibraryId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<LearningLibrary> getAllLearningLibraries() {
	return getSession().createQuery(LearningLibraryDAO.FIND_VALID_LIB).setCacheable(true).list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<LearningLibrary> getAllLearningLibraries(boolean valid) {
	if (valid) {
	    return getAllLearningLibraries();
	} else {
	    return doFindCacheable(LearningLibraryDAO.FIND_ALL_LIB);
	}
    }
}