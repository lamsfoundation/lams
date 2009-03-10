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
package org.lamsfoundation.lams.tool.assessment.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.tool.assessment.dao.AssessmentQuestionResultDAO;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestionResult;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentResult;

public class AssessmentQuestionResultDAOHibernate extends BaseDAOHibernate implements AssessmentQuestionResultDAO {

//    private static final String FIND_BY_QUESTION_AND_USER = "from " + AssessmentQuestionResult.class.getName()
//	    + " as r where r.user.userId = ? and r.assessmentQuestion.uid=?";
//
    private static final String FIND_BY_ASSESSMENT_QUESTION_AND_USER = "from "
	    + AssessmentQuestionResult.class.getName()
	    + " as q, "
	    + AssessmentResult.class.getName()
	    + " as r "
	    + " where q.resultUid = r.uid and r.assessment.uid = ? and r.user.userId =? and q.assessmentQuestion.uid =? order by r.startDate asc";

////    private static final String FIND_VIEW_COUNT_BY_USER = "select count(*) from "
////	    + AssessmentQuestionResult.class.getName() + " as r where  r.sessionId=? and  r.user.userId =?";
//
//    private static final String FIND_WRONG_ANSWERS_NUMBER = "select v.assessmentQuestion.uid, count(v.assessmentQuestion) from  "
//	    + AssessmentQuestionResult.class.getName() + " as v , " + AssessmentSession.class.getName() + " as s, "
//	    + Assessment.class.getName() + "  as r " + " where v.sessionId = s.sessionId "
//	    + " and s.assessment.uid = r.uid " + " and r.contentId =? "
//	    + " group by v.sessionId, v.assessmentQuestion.uid ";

    private static final String FIND_WRONG_ANSWERS_NUMBER = "select count(q) from  "
	    + AssessmentQuestionResult.class.getName()
	    + " as q, "
	    + AssessmentResult.class.getName()
	    + " as r "
	    + " where q.resultUid = r.uid and r.assessment.uid = ? and r.user.userId =? and q.assessmentQuestion.uid =? and q.mark < q.assessmentQuestion.defaultGrade";
    
//    public AssessmentQuestionResult getAssessmentQuestionResult(Long questionUid, Long userId) {
//	List list = getHibernateTemplate().find(FIND_BY_QUESTION_AND_USER, new Object[] { userId, questionUid });
//	if (list == null || list.size() == 0)
//	    return null;
//	return (AssessmentQuestionResult) list.get(0);
//    }
//
//    public int getUserViewLogCount(Long toolSessionId, Long userUid) {
//	List list = getHibernateTemplate().find(FIND_VIEW_COUNT_BY_USER, new Object[] { toolSessionId, userUid });
//	if (list == null || list.size() == 0)
//	    return 0;
//	return ((Number) list.get(0)).intValue();
//    }
//
//    public Map<Long, Integer> getSummary(Long contentId) {
//
//	// Note: Hibernate 3.1 query.uniqueResult() returns Integer, Hibernate 3.2 query.uniqueResult() returns Long
//	List<Object[]> result = getHibernateTemplate().find(FIND_WRONG_ANSWERS_NUMBER, contentId);
//	Map<Long, Integer> summaryList = new HashMap<Long, Integer>(result.size());
//	for (Object[] list : result) {
//	    if (list[1] != null) {
//		summaryList.put((Long) list[0], new Integer(((Number) list[1]).intValue()));
//	    }
//	}
//	return summaryList;
//
//    }
//
//    public List<AssessmentQuestionResult> getAssessmentQuestionResultBySession(Long sessionId, Long questionUid) {
//	return getHibernateTemplate().find(FIND_BY_ASSESSMENT_QUESTION_AND_USER, new Object[] { sessionId, questionUid });
//    }

    public int getNumberWrongAnswersDoneBefore(Long assessmentUid, Long userId, Long questionUid) {
	List list = getHibernateTemplate().find(FIND_WRONG_ANSWERS_NUMBER, new Object[] { assessmentUid, userId, questionUid });
	if (list == null || list.size() == 0) {
	    return 0;
	} else {
	    return ((Number) list.get(0)).intValue();
	}
    }

    public List<AssessmentQuestionResult> getAssessmentQuestionResultList(Long assessmentUid, Long userId, Long questionUid) {
	return getHibernateTemplate().find(FIND_BY_ASSESSMENT_QUESTION_AND_USER, new Object[] { assessmentUid, userId, questionUid });
    }

}
