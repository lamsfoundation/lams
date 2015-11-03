/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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
/* $$Id$$ */
package org.lamsfoundation.lams.tool.qa.dao.hibernate;

import java.util.List;

import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.QaUsrResp;
import org.lamsfoundation.lams.tool.qa.dao.IQaUsrRespDAO;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author Ozgur Demirtas
 */
public class QaUsrRespDAO extends HibernateDaoSupport implements IQaUsrRespDAO {
    
    private static final String LOAD_ATTEMPT_FOR_USER_AND_QUESTION = "from qaUsrResp in class QaUsrResp "
	    + " where qaUsrResp.qaQueUser.queUsrId=:queUsrId and qaUsrResp.qaQuestion.uid=:questionId";

    private static final String LOAD_ATTEMPT_FOR_SESSION_AND_QUESTION = "from qaUsrResp in class QaUsrResp "
	    + " where qaUsrResp.qaQueUser.qaSession.qaSessionId=:qaSessionId and qaUsrResp.qaQuestion.uid=:questionId";

    private static final String LOAD_ATTEMPT_FOR_SESSION_AND_QUESTION_LIMIT_WITH_NAME_SEARCH = "from qaUsrResp in class QaUsrResp "
	    + " where qaUsrResp.qaQueUser.qaSession.qaSessionId=:qaSessionId AND qaUsrResp.qaQuestion.uid=:questionId AND qaUsrResp.qaQueUser.queUsrId!=:excludeUserId "
	    + " AND qaUsrResp.qaQueUser.fullname LIKE CONCAT('%', :searchString, '%') "
	    + " order by ";

    private static final String SQL_LOAD_ATTEMPT_FOR_SESSION_AND_QUESTION_LIMIT_WITH_NAME_SEARCH_AVG_RATING = "SELECT resp.*, AVG(rating.rating) avg_rating"
	    + " FROM tl_laqa11_usr_resp resp"
	    + " JOIN tl_laqa11_que_usr usr"
	    + " ON resp.qa_que_content_id = :questionId AND resp.que_usr_id = usr.uid "
	    + " AND usr.que_usr_id!=:excludeUserId "
	    + " JOIN tl_laqa11_session sess "
	    + " ON usr.qa_session_id = sess.uid AND sess.qa_session_id = :qaSessionId "
	    + " AND usr.fullname LIKE CONCAT('%', :searchString, '%')"
	    + " LEFT JOIN ("
	    + " 	SELECT rat.item_id, rat.rating FROM lams_rating rat"
	    + "         JOIN lams_rating_criteria crit"
	    + " 	ON rat.rating_criteria_id = crit.rating_criteria_id AND crit.tool_content_id = :toolContentId"
	    + " 	) rating"
	    + " ON rating.item_id = resp.response_id"
	    + " GROUP BY response_id"
	    + " ORDER BY ";
	    
    private static final String LOAD_ATTEMPT_FOR_USER = "from qaUsrResp in class QaUsrResp "
	    + "where qaUsrResp.qaQueUser.uid=:userUid order by qaUsrResp.qaQuestion.displayOrder asc";

    private static final String GET_COUNT_RESPONSES_BY_QACONTENT = "SELECT COUNT(*) from " + QaUsrResp.class.getName()
	    + " as r where r.qaQuestion.qaContent.qaContentId=?";

    private static final String GET_COUNT_RESPONSES_FOR_SESSION_AND_QUESTION_WITH_NAME_SEARCH = "SELECT COUNT(*) from "
	    + QaUsrResp.class.getName()
	    + " as r where r.qaQueUser.qaSession.qaSessionId=? and r.qaQuestion.uid=? AND r.qaQueUser.queUsrId!=?"
	    + " and r.qaQueUser.fullname LIKE CONCAT('%', ?, '%') ";

    public void createUserResponse(QaUsrResp qaUsrResp) {
	this.getSession().setFlushMode(FlushMode.AUTO);
	this.getHibernateTemplate().save(qaUsrResp);
    }

    public QaUsrResp getResponseById(Long responseId) {
	return (QaUsrResp) this.getHibernateTemplate().get(QaUsrResp.class, responseId);
    }

    /**
     * @see org.lamsfoundation.lams.tool.qa.dao.IQaUsrRespDAO#updateUserResponse(org.lamsfoundation.lams.tool.qa.QaUsrResp)
     */
    public void updateUserResponse(QaUsrResp resp) {
	this.getSession().setFlushMode(FlushMode.AUTO);
	this.getHibernateTemplate().update(resp);
    }

    public void removeUserResponse(QaUsrResp resp) {
	this.getSession().setFlushMode(FlushMode.AUTO);
	this.getHibernateTemplate().delete(resp);
    }

    @Override
    public QaUsrResp getResponseByUserAndQuestion(final Long queUsrId, final Long questionId) {
	List<QaUsrResp> list = getSession().createQuery(LOAD_ATTEMPT_FOR_USER_AND_QUESTION)
		.setLong("queUsrId", queUsrId.longValue()).setLong("questionId", questionId.longValue()).list();
	if (list == null || list.size() == 0) {
	    return null;
	} else {
	    return (QaUsrResp) list.get(list.size() - 1);
	}
    }

    @Override
    public List<QaUsrResp> getResponseBySessionAndQuestion(final Long qaSessionId, final Long questionId) {
	return getSession().createQuery(LOAD_ATTEMPT_FOR_SESSION_AND_QUESTION)
		.setLong("qaSessionId", qaSessionId.longValue()).setLong("questionId", questionId.longValue()).list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<QaUsrResp> getResponsesForTablesorter(final Long toolContentId, final Long qaSessionId, final Long questionId, final Long excludeUserId,
	    int page, int size, int sorting, String searchString) {
	String sortingOrder;
	boolean useAverageRatingSort = false;
	switch (sorting) {
	case QaAppConstants.SORT_BY_NO:
	    sortingOrder = "qaUsrResp.attemptTime";
	    break;
	case QaAppConstants.SORT_BY_ANSWER_ASC:
	    sortingOrder = "answer ASC";
	    break;
	case QaAppConstants.SORT_BY_ANSWER_DESC:
	    sortingOrder = "answer DESC";
	    break;
	case QaAppConstants.SORT_BY_USERNAME_ASC:
	    sortingOrder = "qaUsrResp.qaQueUser.fullname ASC";
	    break;
	case QaAppConstants.SORT_BY_USERNAME_DESC:
	    sortingOrder = "qaUsrResp.qaQueUser.fullname DESC";
	    break;
	case QaAppConstants.SORT_BY_RATING_ASC:
	    sortingOrder = " avg_rating ASC";
	    useAverageRatingSort = true;
	    break;
	case QaAppConstants.SORT_BY_RATING_DESC:
	    sortingOrder = " avg_rating DESC";
	    useAverageRatingSort = true;
	    break;
	default: 
	    sortingOrder = " resp.attempt_time"; // default if we get an unexpected sort order    
	}

	Query query = null;
	
	if ( useAverageRatingSort ) {
	    query = getSession()
		    .createSQLQuery(SQL_LOAD_ATTEMPT_FOR_SESSION_AND_QUESTION_LIMIT_WITH_NAME_SEARCH_AVG_RATING+sortingOrder).addEntity(QaUsrResp.class)
		    .setLong("toolContentId", toolContentId.longValue());
	} else {
	    query = getSession().createQuery(LOAD_ATTEMPT_FOR_SESSION_AND_QUESTION_LIMIT_WITH_NAME_SEARCH+sortingOrder);
	}
	
	query.setLong("questionId", questionId.longValue());
	query.setLong("qaSessionId", qaSessionId.longValue());
	query.setLong("excludeUserId", excludeUserId.longValue());

	// support for custom search from a toolbar
	searchString = searchString == null ? "" : searchString;
	query.setString("searchString", searchString);

	query.setFirstResult(page * size);
	query.setMaxResults(size);
	return query.list();
    }

    @Override
    public List<QaUsrResp> getResponsesByUserUid(final Long userUid) {
	List<QaUsrResp> list = getSession().createQuery(LOAD_ATTEMPT_FOR_USER).setLong("userUid", userUid.longValue())
		.list();
	return list;
    }

    public int getCountResponsesByQaContent(final Long qaContentId) {

	List list = getHibernateTemplate().find(GET_COUNT_RESPONSES_BY_QACONTENT, new Object[] { qaContentId });
	if (list == null || list.size() == 0) {
	    return 0;
	}
	return ((Number) list.get(0)).intValue();
    }

    public int getCountResponsesBySessionAndQuestion(final Long qaSessionId, final Long questionId, final Long excludeUserId, String searchString) {

	String filter = searchString != null ? searchString.trim() : "";
	List list = getHibernateTemplate().find(GET_COUNT_RESPONSES_FOR_SESSION_AND_QUESTION_WITH_NAME_SEARCH,
			new Object[] { qaSessionId, questionId, excludeUserId, filter });
	if (list == null || list.size() == 0) {
	    return 0;
	}
	return ((Number) list.get(0)).intValue();
    }

}
