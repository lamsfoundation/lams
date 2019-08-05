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

package org.lamsfoundation.lams.tool.qa.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.query.Query;
import org.hibernate.type.IntegerType;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.dao.IQaUsrRespDAO;
import org.lamsfoundation.lams.tool.qa.model.QaUsrResp;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.springframework.stereotype.Repository;

/**
 * @author Ozgur Demirtas
 */
@Repository
public class QaUsrRespDAO extends LAMSBaseDAO implements IQaUsrRespDAO {
    private static final String LOAD_ATTEMPT_FOR_USER_AND_QUESTION = "from qaUsrResp in class QaUsrResp "
	    + "where qaUsrResp.qaQueUser.queUsrId=:queUsrId and qaUsrResp.qbToolQuestion.uid=:questionId";

    private static final String LOAD_ATTEMPT_FOR_SESSION_AND_QUESTION = "from qaUsrResp in class QaUsrResp "
	    + " where qaUsrResp.qaQueUser.qaSession.qaSessionId=:qaSessionId and qaUsrResp.qbToolQuestion.uid=:questionId";

    private static final String LOAD_ATTEMPT_FOR_USER = "from qaUsrResp in class QaUsrResp "
	    + "where qaUsrResp.qaQueUser.uid=:userUid order by qaUsrResp.qbToolQuestion.displayOrder asc";

    private static final String GET_COUNT_RESPONSES_BY_QACONTENT = "SELECT COUNT(*) from " + QaUsrResp.class.getName()
	    + " as r where r.qbToolQuestion.toolContentId=?";

    @Override
    public void createUserResponse(QaUsrResp qaUsrResp) {
	getSession().save(qaUsrResp);
    }

    @Override
    public QaUsrResp getResponseById(Long responseId) {
	return getSession().get(QaUsrResp.class, responseId);
    }

    /**
     * @see org.lamsfoundation.lams.tool.qa.dao.IQaUsrRespDAO#updateUserResponse(org.lamsfoundation.lams.tool.qa.model.QaUsrResp)
     */
    @Override
    public void updateUserResponse(QaUsrResp resp) {
	getSession().update(resp);
    }

    @Override
    public void removeUserResponse(QaUsrResp resp) {
	getSession().delete(resp);
    }

    @SuppressWarnings("unchecked")
    @Override
    public QaUsrResp getResponseByUserAndQuestion(final Long queUsrId, final Long questionId) {
	List<QaUsrResp> list = getSessionFactory().getCurrentSession().createQuery(LOAD_ATTEMPT_FOR_USER_AND_QUESTION)
		.setParameter("queUsrId", queUsrId.longValue()).setParameter("questionId", questionId.longValue())
		.list();
	if (list == null || list.size() == 0) {
	    return null;
	} else {
	    return list.get(list.size() - 1);
	}
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<QaUsrResp> getResponseBySessionAndQuestion(final Long qaSessionId, final Long questionId) {
	return getSessionFactory().getCurrentSession().createQuery(LOAD_ATTEMPT_FOR_SESSION_AND_QUESTION)
		.setParameter("qaSessionId", qaSessionId.longValue()).setParameter("questionId", questionId.longValue())
		.list();
    }

    private String buildNameSearch(String searchString, String userRef) {
	String filteredSearchString = null;
	if (!StringUtils.isBlank(searchString)) {
	    StringBuilder searchStringBuilder = new StringBuilder("");
	    String[] tokens = searchString.trim().split("\\s+");
	    for (String token : tokens) {
		String escToken = StringEscapeUtils.escapeSql(token);
		searchStringBuilder.append(" AND (" + userRef + ".fullname LIKE '%").append(escToken)
			.append("%' OR " + userRef + ".username LIKE '%").append(escToken).append("%') ");
	    }
	    filteredSearchString = searchStringBuilder.toString();
	}
	return filteredSearchString;
    }

    private static final String SQL_LOAD_ATTEMPT_FOR_SESSION_AND_QUESTION_LIMIT_WITH_NAME_SEARCH_AVG_RATING_SELECT = "SELECT resp.*, AVG(rating.rating) avg_rating ";
    private static final String SQL_LOAD_ATTEMPT_FOR_SESSION_AND_QUESTION_LIMIT_WITH_NAME_SEARCH_AVG_RATING_FROM = " FROM tl_laqa11_usr_resp resp ";
    private static final String SQL_LOAD_ATTEMPT_FOR_SESSION_AND_QUESTION_LIMIT_WITH_NAME_SEARCH_AVG_RATING_JOIN = " JOIN lams_qb_tool_answer ans ON ans.uid = resp.uid "
	    + " AND ans.answer IS NOT NULL AND ans.tool_question_uid = :questionId JOIN tl_laqa11_que_usr usr"
	    + " ON resp.que_usr_id = usr.uid AND usr.que_usr_id!=:excludeUserId "
	    + " JOIN tl_laqa11_session sess ON usr.qa_session_id = sess.uid AND sess.qa_session_id = :qaSessionId";

    private static final String SQL_LOAD_ATTEMPT_FOR_SESSION_AND_QUESTION_LIMIT_WITH_NAME_SEARCH_AVG_RATING_JOIN_RATING = " LEFT JOIN ("
	    + " 	SELECT rat.item_id, rat.rating FROM lams_rating rat JOIN lams_rating_criteria crit"
	    + " 	ON rat.rating_criteria_id = crit.rating_criteria_id AND crit.tool_content_id = :toolContentId"
	    + " 	) rating ON rating.item_id = resp.uid GROUP BY resp.uid ORDER BY ";

    private static final String SQL_LOAD_ATTEMPT_FOR_SESSION_AND_QUESTION_LIMIT_WITH_COMMENT_COUNT_SELECT = "SELECT resp.*, COUNT(rating_comment.item_id) count_comment ";
    private static final String SQL_LOAD_ATTEMPT_FOR_SESSION_AND_QUESTION_LIMIT_WITH_COMMENT_COUNT_FROM = " FROM tl_laqa11_usr_resp resp ";
    private static final String SQL_LOAD_ATTEMPT_FOR_SESSION_AND_QUESTION_LIMIT_WITH_COMMENT_COUNT_JOIN = " JOIN lams_qb_tool_answer ans ON ans.uid = resp.uid "
	    + " AND ans.answer IS NOT NULL AND ans.tool_question_uid = :questionId "
	    + " JOIN tl_laqa11_que_usr usr ON resp.que_usr_id = usr.uid AND usr.que_usr_id!=:excludeUserId "
	    + " JOIN tl_laqa11_session sess "
	    + " ON usr.qa_session_id = sess.uid AND sess.qa_session_id = :qaSessionId LEFT JOIN ("
	    + " 	SELECT ratcom.item_id FROM lams_rating_comment ratcom JOIN lams_rating_criteria crit"
	    + " 	ON ratcom.rating_criteria_id = crit.rating_criteria_id AND crit.tool_content_id = :toolContentId"
	    + " 	) rating_comment " + " ON rating_comment.item_id = resp.uid ";
    private static final String SQL_LOAD_ATTEMPT_FOR_SESSION_AND_QUESTION_LIMIT_WITH_COMMENT_COUNT_ORDER_BY = " GROUP BY resp.uid ORDER BY count_comment ASC, "
	    + "resp.uid ASC";

    private static final String LOAD_ATTEMPT_FOR_SESSION_AND_QUESTION_LIMIT_WITH_NAME_SEARCH_SELECT = "SELECT resp.* ";
    private static final String LOAD_ATTEMPT_FOR_SESSION_AND_QUESTION_LIMIT_WITH_NAME_SEARCH_FROM = " FROM tl_laqa11_usr_resp resp ";
    private static final String LOAD_ATTEMPT_FOR_SESSION_AND_QUESTION_LIMIT_WITH_NAME_SEARCH_JOIN1 = " JOIN lams_qb_tool_answer ans ON ans.uid = resp.uid "
	    + " AND ans.answer IS NOT NULL AND ans.tool_question_uid = :questionId JOIN tl_laqa11_que_usr usr"
	    + " ON resp.que_usr_id = usr.uid " + " AND usr.que_usr_id!=:excludeUserId "
	    + " JOIN tl_laqa11_session sess "
	    + " ON usr.qa_session_id = sess.uid AND sess.qa_session_id = :qaSessionId ";
    private static final String LOAD_ATTEMPT_FOR_SESSION_AND_QUESTION_LIMIT_WITH_NAME_SEARCH_JOIN2 = " AND sess.qa_group_leader_uid = usr.uid ";
    private static final String LOAD_ATTEMPT_FOR_SESSION_AND_QUESTION_LIMIT_WITH_NAME_SEARCH_ORDER_BY = " order by ";

    @SuppressWarnings("unchecked")
    @Override
    public List<QaUsrResp> getResponsesForTablesorter(final Long toolContentId, final Long qaSessionId,
	    final Long questionId, final Long excludeUserId, boolean isOnlyLeadersIncluded, int page, int size,
	    int sorting, String searchString, IUserManagementService userManagementService) {
	String sortingOrder;

	switch (sorting) {
	    case QaAppConstants.SORT_BY_NO:
		sortingOrder = " attempt_time";
		break;
	    case QaAppConstants.SORT_BY_ANSWER_ASC:
		sortingOrder = " answer ASC";
		break;
	    case QaAppConstants.SORT_BY_ANSWER_DESC:
		sortingOrder = " answer DESC";
		break;
	    case QaAppConstants.SORT_BY_USERNAME_ASC:
		sortingOrder = " usr.fullname ASC";
		break;
	    case QaAppConstants.SORT_BY_USERNAME_DESC:
		sortingOrder = " usr.fullname DESC";
		break;
	    case QaAppConstants.SORT_BY_RATING_ASC:
		sortingOrder = " avg_rating ASC";
		break;
	    case QaAppConstants.SORT_BY_RATING_DESC:
		sortingOrder = " avg_rating DESC";
		break;
	    case QaAppConstants.SORT_BY_COMMENT_COUNT:
		// LDEV-4399: Only valid if there are no numeric ratings and no name search. Sort order is in SQL string
		sortingOrder = "";
		break;
	    default:
		sortingOrder = " resp.attempt_time"; // default if we get an unexpected sort order
	}

	StringBuilder queryText = null;
	String[] portraitStrings = userManagementService.getPortraitSQL("usr.que_usr_id");
	boolean needsToolContentId = false;

	if (sorting == QaAppConstants.SORT_BY_RATING_ASC || sorting == QaAppConstants.SORT_BY_RATING_DESC) {

	    String filteredSearchString = buildNameSearch(searchString, "usr");
	    queryText = new StringBuilder(
		    SQL_LOAD_ATTEMPT_FOR_SESSION_AND_QUESTION_LIMIT_WITH_NAME_SEARCH_AVG_RATING_SELECT)
			    .append(portraitStrings[0])
			    .append(SQL_LOAD_ATTEMPT_FOR_SESSION_AND_QUESTION_LIMIT_WITH_NAME_SEARCH_AVG_RATING_FROM)
			    .append(SQL_LOAD_ATTEMPT_FOR_SESSION_AND_QUESTION_LIMIT_WITH_NAME_SEARCH_AVG_RATING_JOIN)
			    .append(filteredSearchString != null ? filteredSearchString : "").append(portraitStrings[1])
			    .append(SQL_LOAD_ATTEMPT_FOR_SESSION_AND_QUESTION_LIMIT_WITH_NAME_SEARCH_AVG_RATING_JOIN_RATING)
			    .append(sortingOrder);
	    needsToolContentId = true;

	} else if (sorting == QaAppConstants.SORT_BY_COMMENT_COUNT) {

	    queryText = new StringBuilder(SQL_LOAD_ATTEMPT_FOR_SESSION_AND_QUESTION_LIMIT_WITH_COMMENT_COUNT_SELECT)
		    .append(portraitStrings[0])
		    .append(SQL_LOAD_ATTEMPT_FOR_SESSION_AND_QUESTION_LIMIT_WITH_COMMENT_COUNT_FROM)
		    .append(SQL_LOAD_ATTEMPT_FOR_SESSION_AND_QUESTION_LIMIT_WITH_COMMENT_COUNT_JOIN)
		    .append(portraitStrings[1])
		    .append(SQL_LOAD_ATTEMPT_FOR_SESSION_AND_QUESTION_LIMIT_WITH_COMMENT_COUNT_ORDER_BY);
	    needsToolContentId = true;

	} else {
	    String filteredSearchString = buildNameSearch(searchString, "usr");
	    queryText = new StringBuilder(LOAD_ATTEMPT_FOR_SESSION_AND_QUESTION_LIMIT_WITH_NAME_SEARCH_SELECT)
		    .append(portraitStrings[0])
		    .append(LOAD_ATTEMPT_FOR_SESSION_AND_QUESTION_LIMIT_WITH_NAME_SEARCH_FROM)
		    .append(LOAD_ATTEMPT_FOR_SESSION_AND_QUESTION_LIMIT_WITH_NAME_SEARCH_JOIN1)
		    .append(filteredSearchString != null ? filteredSearchString : "")
		    .append(isOnlyLeadersIncluded ? LOAD_ATTEMPT_FOR_SESSION_AND_QUESTION_LIMIT_WITH_NAME_SEARCH_JOIN2
			    : "")
		    .append(portraitStrings[1])
		    .append(LOAD_ATTEMPT_FOR_SESSION_AND_QUESTION_LIMIT_WITH_NAME_SEARCH_ORDER_BY).append(sortingOrder);

	}

	Query<Object[]> query = getSessionFactory().getCurrentSession().createSQLQuery(queryText.toString())
		.addEntity(QaUsrResp.class).addScalar("portraitId", IntegerType.INSTANCE);

	if (needsToolContentId) {
	    query.setParameter("toolContentId", toolContentId.longValue());
	}
	query.setParameter("questionId", questionId.longValue()).setParameter("qaSessionId", qaSessionId.longValue())
		.setParameter("excludeUserId", excludeUserId.longValue());

	if (size > 0) {
	    query.setFirstResult(page * size);
	    query.setMaxResults(size);
	}

	List<Object[]> list = query.list();
	List<QaUsrResp> respList = new ArrayList<>(list.size());
	for (Object[] row : list) {
	    QaUsrResp resp = (QaUsrResp) row[0];
	    if (row.length > 1 && row[1] != null) {
		resp.setPortraitId(((Number) row[1]).longValue());
	    }
	    respList.add(resp);
	}
	return respList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<QaUsrResp> getResponsesByUserUid(final Long userUid) {
	List<QaUsrResp> list = getSessionFactory().getCurrentSession().createQuery(LOAD_ATTEMPT_FOR_USER)
		.setParameter("userUid", userUid.longValue()).list();
	return list;
    }

    @Override
    public int getCountResponsesByQaContent(final Long qaContentId) {

	List<?> list = doFind(GET_COUNT_RESPONSES_BY_QACONTENT, new Object[] { qaContentId });
	if (list == null || list.size() == 0) {
	    return 0;
	}
	return ((Number) list.get(0)).intValue();
    }

    private static final String GET_COUNT_RESPONSES_FOR_SESSION_AND_QUESTION_WITH_NAME_SEARCH = "SELECT COUNT(*) FROM "
	    + QaUsrResp.class.getName()
	    + " AS r WHERE r.answer IS NOT NULL AND r.qaQueUser.qaSession.qaSessionId=? AND r.qbToolQuestion.uid=? AND r.qaQueUser.queUsrId!=?";
    private static final String GET_COUNT_RESPONSES_FOR_SESSION_AND_QUESTION_WITH_NAME_SEARCH2 = " AND r.qaQueUser.qaSession.groupLeader.queUsrId=r.qaQueUser.queUsrId ";

    @Override
    public int getCountResponsesBySessionAndQuestion(final Long qaSessionId, final Long questionId,
	    final Long excludeUserId, boolean isOnlyLeadersIncluded, String searchString) {

	String filteredSearchString = buildNameSearch(searchString, "r.qaQueUser");
	String queryText = GET_COUNT_RESPONSES_FOR_SESSION_AND_QUESTION_WITH_NAME_SEARCH
		+ (isOnlyLeadersIncluded ? GET_COUNT_RESPONSES_FOR_SESSION_AND_QUESTION_WITH_NAME_SEARCH2 : "");
	if (filteredSearchString != null) {
	    queryText += filteredSearchString;
	}

	List<?> list = doFind(queryText, new Object[] { qaSessionId, questionId, excludeUserId });
	if (list == null || list.size() == 0) {
	    return 0;
	}
	return ((Number) list.get(0)).intValue();
    }

}
