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

package org.lamsfoundation.lams.tool.assessment.dao.hibernate;

import java.util.List;

import org.hibernate.query.Query;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.assessment.dao.AssessmentQuestionResultDAO;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestionResult;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentResult;
import org.springframework.stereotype.Repository;

@Repository
public class AssessmentQuestionResultDAOHibernate extends LAMSBaseDAO implements AssessmentQuestionResultDAO {

    private static final String FIND_BY_UID = "FROM " + AssessmentQuestionResult.class.getName()
	    + " AS r WHERE r.uid = ?";

    private static final String FIND_BY_ASSESSMENT_QUESTION_AND_USER = "FROM "
	    + AssessmentQuestionResult.class.getName() + " AS q, " + AssessmentResult.class.getName() + " AS r "
	    + " WHERE q.assessmentResult.uid = r.uid and r.assessment.uid = ? AND r.user.userId =? AND q.qbToolQuestion.uid =? ORDER BY r.startDate ASC";

    private static final String FIND_BY_QUESTION_UID = "FROM " + AssessmentQuestionResult.class.getName()
	    + " AS queRes " + " WHERE queRes.qbToolQuestion.uid =:questionUid ORDER BY queRes.finishDate ASC";
    
    private static final String FIND_WRONG_ANSWERS_NUMBER = "SELECT COUNT(q) FROM  "
	    + AssessmentQuestionResult.class.getName() + " AS q, " + AssessmentResult.class.getName() + " AS r "
	    + " WHERE q.assessmentResult.uid = r.uid AND r.assessment.uid = ? AND r.user.userId =? AND q.qbToolQuestion.uid =? AND (ROUND(q.mark + q.penalty) < q.maxMark) AND (r.finishDate != null)";

    private static final String GET_ANSWER_MARK = "SELECT q.mark FROM  " + AssessmentQuestionResult.class.getName()
	    + " AS q, " + AssessmentResult.class.getName() + " AS r "
	    + " WHERE q.assessmentResult.uid = r.uid AND r.assessment.uid = :assessmentUid AND (r.finishDate != null) AND r.user.userId =:userId AND q.qbToolQuestion.displayOrder =:questionDisplayOrder ORDER BY r.startDate DESC";

    @Override
    public int getNumberWrongAnswersDoneBefore(Long assessmentUid, Long userId, Long questionUid) {
	List list = doFind(FIND_WRONG_ANSWERS_NUMBER, new Object[] { assessmentUid, userId, questionUid });
	if (list == null || list.size() == 0) {
	    return 0;
	} else {
	    return ((Number) list.get(0)).intValue();
	}
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Object[]> getAssessmentQuestionResultList(Long assessmentUid, Long userId, Long questionUid) {
	return (List<Object[]>) doFind(FIND_BY_ASSESSMENT_QUESTION_AND_USER,
		new Object[] { assessmentUid, userId, questionUid });
    }

    @Override
    public AssessmentQuestionResult getAssessmentQuestionResultByUid(Long questionResultUid) {
	List list = doFind(FIND_BY_UID, new Object[] { questionResultUid });
	if (list == null || list.size() == 0) {
	    return null;
	}
	return (AssessmentQuestionResult) list.get(0);
    }

    @Override
    public List<AssessmentQuestionResult> getQuestionResultsByQuestionUid(final Long questionUid) {
	String FIND_BY_QUESTION_UID = "FROM " + AssessmentQuestionResult.class.getName()
		    + " AS queRes " + " WHERE queRes.qbToolQuestion.uid =:questionUid ORDER BY queRes.finishDate ASC";
	Query<AssessmentQuestionResult> q = getSession().createQuery(FIND_BY_QUESTION_UID,
		AssessmentQuestionResult.class);
	q.setParameter("questionUid", questionUid);
	return q.getResultList();
    }

    @Override
    public Float getQuestionResultMark(final Long assessmentUid, final Long userId, final int questionDisplayOrder) {
	Query<Number> q = getSession().createQuery(GET_ANSWER_MARK, Number.class);
	q.setParameter("assessmentUid", assessmentUid);
	q.setParameter("userId", userId);
	q.setParameter("questionDisplayOrder", questionDisplayOrder);
	q.setMaxResults(1);
	Number result = q.uniqueResult();
	return result != null ? result.floatValue() : null;
    }
}
