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



package org.lamsfoundation.lams.confidencelevel.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.confidencelevel.ConfidenceLevel;
import org.lamsfoundation.lams.confidencelevel.dao.IConfidenceLevelDAO;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;

public class ConfidenceLevelDAO extends LAMSBaseDAO implements IConfidenceLevelDAO {

    private static final String FIND_CONFIDENCE_BY_AND_USER_AND_ITEM = "FROM " + ConfidenceLevel.class.getName()
	    + " AS r where r.learner.userId=? AND r.questionUid=?";

    private static final String FIND_RATING_BY_CRITERIA_AND_USER = "FROM " + ConfidenceLevel.class.getName()
	    + " AS r where r.ratingCriteria.ratingCriteriaId=? AND r.learner.userId=?";

    private static final String FIND_RATINGS_BY_ITEM = "FROM " + ConfidenceLevel.class.getName()
	    + " AS r where r.ratingCriteria.toolContentId=? AND r.toolSessionId=? AND r.questionUid=?";

    private static final String FIND_CONFIDENCES_BY_USER = "FROM " + ConfidenceLevel.class.getName()
	    + " AS r where r.learner.userId=? AND r.toolSessionId=?";
    
    private static final String FIND_CONFIDENCES_BY_QUESTION_AND_SESSION = "FROM " + ConfidenceLevel.class.getName()
	    + " AS r where r.questionUid=? AND r.toolSessionId=?";

    @Override
    public void saveOrUpdate(Object object) {
	getSession().saveOrUpdate(object);
	getSession().flush();
    }

    public void delete(Object object) {
	getSession().delete(object);
	getSession().flush();
    }

    @Override
    public ConfidenceLevel getConfidenceLevel(Integer userId, Long questionUid) {
	List<ConfidenceLevel> list = (List<ConfidenceLevel>) doFind(FIND_CONFIDENCE_BY_AND_USER_AND_ITEM,
		new Object[] { userId, questionUid });
	if (list.size() > 0) {
	    return list.get(0);
	} else {
	    return null;
	}
    }

    @Override
    public List<ConfidenceLevel> getConfidenceLevelsByItem(Long contentId, Long toolSessionId, Long questionUid) {
	return super.find(FIND_RATINGS_BY_ITEM, new Object[] { contentId, toolSessionId, questionUid });
    }

    // method is not used at the moment
    private ConfidenceLevel getConfidenceLevel(Long ratingCriteriaId, Integer userId) {
	List<ConfidenceLevel> list = (List<ConfidenceLevel>) doFind(FIND_RATING_BY_CRITERIA_AND_USER,
		new Object[] { ratingCriteriaId, userId });
	if (list.size() > 0) {
	    return list.get(0);
	} else {
	    return null;
	}
    }

    @Override
    public List<ConfidenceLevel> getConfidenceLevelsByUser(Integer userId, Long toolSessionId) {
	return (List<ConfidenceLevel>) doFind(FIND_CONFIDENCES_BY_USER, new Object[] { userId, toolSessionId });
    }
    
    @Override
    public List<ConfidenceLevel> getConfidenceLevelsByQuestionAndSession(Long questionUid, Long toolSessionId) {
	return (List<ConfidenceLevel>) doFind(FIND_CONFIDENCES_BY_QUESTION_AND_SESSION, new Object[] { questionUid, toolSessionId });
    }

    @Override
    public ConfidenceLevel get(Long uid) {
	if (uid != null) {
	    Object o = super.find(ConfidenceLevel.class, uid);
	    return (ConfidenceLevel) o;
	} else {
	    return null;
	}
    }
	    
}
