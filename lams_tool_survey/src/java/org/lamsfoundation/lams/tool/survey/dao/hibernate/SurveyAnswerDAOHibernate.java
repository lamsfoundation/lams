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
package org.lamsfoundation.lams.tool.survey.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.survey.SurveyConstants;
import org.lamsfoundation.lams.tool.survey.dao.SurveyAnswerDAO;
import org.lamsfoundation.lams.tool.survey.model.SurveyAnswer;
import org.springframework.stereotype.Repository;

@Repository
public class SurveyAnswerDAOHibernate extends LAMSBaseDAO implements SurveyAnswerDAO {
    private static final String GET_LEARNER_ANSWER = "FROM " + SurveyAnswer.class.getName()
	    + " AS a WHERE a.surveyQuestion.uid=? AND a.user.uid=?";
    private static final String GET_SESSION_ANSWER = "FROM " + SurveyAnswer.class.getName() + " AS a "
	    + " WHERE a.user.session.sessionId=? AND a.surveyQuestion.uid=?";
    private static final String GET_BY_TOOL_CONTENT_ID_AND_USER_ID = "FROM " + SurveyAnswer.class.getName() + " AS a "
	    + " WHERE a.user.session.survey.contentId = ? AND a.user.userId = ?";

    private static final String LOAD_ATTEMPT_FOR_SESSION_AND_QUESTION_LIMIT = "SELECT r.answerText FROM "
	    + SurveyAnswer.class.getName() + " AS r "
	    + "WHERE r.user.session.sessionId=:sessionId AND r.surveyQuestion.uid=:questionUid AND r.answerText<>'' order by ";

    private static final String GET_COUNT_RESPONSES_FOR_SESSION_AND_QUESTION = "SELECT COUNT(*) FROM "
	    + SurveyAnswer.class.getName() + " AS r "
	    + "WHERE r.user.session.sessionId=? AND r.surveyQuestion.uid=? AND r.answerText<>''";

    private static final String GET_COUNT_RESPONSES_FOR_SESSION_QUESTION_CHOICE = "SELECT COUNT(*) FROM "
	    + SurveyAnswer.class.getName() + " AS r " + " WHERE r.user.session.sessionId=? AND r.surveyQuestion.uid=? "
	    + " AND ( r.answerChoices like ? OR r.answerChoices like ? )";

    @Override
    public SurveyAnswer getAnswer(Long questionUid, Long userUid) {
	List list = doFind(GET_LEARNER_ANSWER, new Object[] { questionUid, userUid });
	if (list.size() > 0) {
	    return (SurveyAnswer) list.get(0);
	} else {
	    return null;
	}
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<SurveyAnswer> getSessionAnswer(Long sessionId, Long questionUid) {
	return (List<SurveyAnswer>) doFind(GET_SESSION_ANSWER, new Object[] { sessionId, questionUid });
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<SurveyAnswer> getByToolContentIdAndUserId(Long toolContentId, Long userId) {
	return (List<SurveyAnswer>) doFind(GET_BY_TOOL_CONTENT_ID_AND_USER_ID, new Object[] { toolContentId, userId });
    }

    @Override
    public List<String> getOpenResponsesForTablesorter(final Long sessionId, final Long questionUid, int page, int size,
	    int sorting) {
	String sortingOrder = "";
	switch (sorting) {
	    case SurveyConstants.SORT_BY_DEAFAULT:
		sortingOrder = "r.updateDate";
		break;
	    case SurveyConstants.SORT_BY_ANSWER_ASC:
		sortingOrder = "r.answerText ASC";
		break;
	    case SurveyConstants.SORT_BY_ANSWER_DESC:
		sortingOrder = "r.answerText DESC";
		break;
	}
	String sqlQuery = LOAD_ATTEMPT_FOR_SESSION_AND_QUESTION_LIMIT + sortingOrder;

	return getSession().createQuery(sqlQuery).setParameter("sessionId", sessionId.longValue())
		.setParameter("questionUid", questionUid.longValue()).setFirstResult(page * size).setMaxResults(size)
		.list();
    }

    @Override
    public int getCountResponsesBySessionAndQuestion(final Long sessionId, final Long questionId) {

	List list = doFind(GET_COUNT_RESPONSES_FOR_SESSION_AND_QUESTION, new Object[] { sessionId, questionId });
	if (list == null || list.size() == 0) {
	    return 0;
	}
	return ((Number) list.get(0)).intValue();
    }

    /** Get a count of the number of times this particular choice has been selected for this question. */
    @Override
    @SuppressWarnings("rawtypes")
    public Integer getAnswerCount(Long sessionId, Long questionId, String choice) {

	String choice1 = choice + "&%";
	String choice2 = "%&" + choice1;

	String sql = GET_COUNT_RESPONSES_FOR_SESSION_QUESTION_CHOICE;
	List list = doFind(sql, new Object[] { sessionId, questionId, choice1, choice2 });
	if (list == null || list.size() == 0) {
	    return 0;
	}
	return ((Number) list.get(0)).intValue();
    }

}
