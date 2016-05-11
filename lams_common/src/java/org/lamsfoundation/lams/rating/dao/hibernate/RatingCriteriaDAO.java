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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */


package org.lamsfoundation.lams.rating.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.rating.dao.IRatingCriteriaDAO;
import org.lamsfoundation.lams.rating.model.AuthoredItemRatingCriteria;
import org.lamsfoundation.lams.rating.model.LearnerItemRatingCriteria;
import org.lamsfoundation.lams.rating.model.LessonRatingCriteria;
import org.lamsfoundation.lams.rating.model.RatingCriteria;
import org.lamsfoundation.lams.rating.model.ToolActivityRatingCriteria;
import org.springframework.dao.DataRetrievalFailureException;

public class RatingCriteriaDAO extends LAMSBaseDAO implements IRatingCriteriaDAO {

    private static final String FIND_BY_TOOL_CONTENT_ID = "FROM " + RatingCriteria.class.getName()
	    + " AS r WHERE r.toolContentId=? order by r.orderId asc";

    private static final String IS_COMMENTS_ENABLED_FOR_TOOL_CONTENT_ID = "SELECT COUNT(*) FROM "
	    + RatingCriteria.class.getName() + " AS r WHERE r.toolContentId=? AND r.commentsEnabled=1";

    private static final String GET_COMMENTS_MIN_WORDS_LIMIT_FOR_TOOL_CONTENT_ID = "SELECT r.commentsMinWordsLimit FROM "
	    + RatingCriteria.class.getName() + " AS r WHERE r.toolContentId=? AND r.commentsEnabled=1";

    @Override
    public void saveOrUpdate(RatingCriteria criteria) {
	getSession().saveOrUpdate(criteria);
	getSession().flush();
    }

    @Override
    public void deleteRatingCriteria(Long ratingCriteriaId) {
	this.deleteById(RatingCriteria.class, ratingCriteriaId);
    }

    @Override
    public List<RatingCriteria> getByToolContentId(Long toolContentId) {
	return (List<RatingCriteria>) (doFind(FIND_BY_TOOL_CONTENT_ID, new Object[] { toolContentId }));
    }

    @Override
    public RatingCriteria getByRatingCriteriaId(Long ratingCriteriaId) {
	if (ratingCriteriaId == null) {
	    return null;
	}

	RatingCriteria criteria = (RatingCriteria) super.find(RatingCriteria.class, ratingCriteriaId);

	/**
	 * we must return the real activity, not a Hibernate proxy. So relook it up. This should be quick as it should
	 * be in the cache.
	 */
	if (criteria != null) {
	    Integer criteriaType = criteria.getRatingCriteriaTypeId();
	    if (criteriaType != null) {
		switch (criteriaType.intValue()) {
		    case RatingCriteria.TOOL_ACTIVITY_CRITERIA_TYPE:
			return getByRatingCriteriaId(ratingCriteriaId, ToolActivityRatingCriteria.class);
		    case RatingCriteria.AUTHORED_ITEM_CRITERIA_TYPE:
			return getByRatingCriteriaId(ratingCriteriaId, AuthoredItemRatingCriteria.class);
		    case RatingCriteria.LEARNER_ITEM_CRITERIA_TYPE:
			return getByRatingCriteriaId(ratingCriteriaId, LearnerItemRatingCriteria.class);
		    case RatingCriteria.LESSON_CRITERIA_TYPE:
			return getByRatingCriteriaId(ratingCriteriaId, LessonRatingCriteria.class);
		    default:
			break;
		}
	    }
	    throw new DataRetrievalFailureException(
		    "Unable to get RatingCriteria as the RatingCriteria type is unknown or missing. RatingCriteria type is "
			    + criteriaType);
	}
	return null;
    }

    @Override
    public RatingCriteria getByRatingCriteriaId(Long ratingCriteriaId, Class clasz) {
	return (RatingCriteria) super.find(clasz, ratingCriteriaId);
    }

    @Override
    public boolean isCommentsEnabledForToolContent(Long toolContentId) {
	List list = super.find(IS_COMMENTS_ENABLED_FOR_TOOL_CONTENT_ID, new Object[] { toolContentId });
	return ((Number) list.get(0)).intValue() == 1;
    }

    @Override
    public int getCommentsMinWordsLimitForToolContent(Long toolContentId) {
	List list = super.find(GET_COMMENTS_MIN_WORDS_LIMIT_FOR_TOOL_CONTENT_ID, new Object[] { toolContentId });
	if (list == null || list.size() == 0) {
	    return 0;
	}
	return ((Number) list.get(0)).intValue();
    }
}
