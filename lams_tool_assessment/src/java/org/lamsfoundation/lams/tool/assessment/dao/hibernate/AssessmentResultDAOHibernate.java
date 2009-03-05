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
 
/* $Id$ */  
package org.lamsfoundation.lams.tool.assessment.dao.hibernate;  

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lamsfoundation.lams.tool.assessment.dao.AssessmentQuestionResultDAO;
import org.lamsfoundation.lams.tool.assessment.dao.AssessmentResultDAO;
import org.lamsfoundation.lams.tool.assessment.model.Assessment;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestionResult;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentResult;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentSession;
 
public class AssessmentResultDAOHibernate extends BaseDAOHibernate implements AssessmentResultDAO {

    private static final String FIND_BY_ASSESSMENT_AND_USER = "from " + AssessmentResult.class.getName()
	    + " as r where r.user.userId = ? and r.assessment.uid=? order by start_date asc";
    
    private static final String FIND_BY_ASSESSMENT_AND_SESSION = "from " + AssessmentResult.class.getName()
	    + " as r where r.sessionId = ? and r.assessment.uid=?";

    private static final String FIND_ASSESSMENT_RESULT_COUNT_BY_ASSESSMENT_AND_USER = "select count(*) from "
	    + AssessmentResult.class.getName() + " as r where r.user.userId=? and r.assessment.uid=?";

// private static final String FIND_SUMMARY = "select v.assessmentQuestion.uid, count(v.assessmentQuestion) from  "
//	    + AssessmentQuestionResult.class.getName() + " as v , " + AssessmentSession.class.getName() + " as s, "
//	    + Assessment.class.getName() + "  as r " + " where v.sessionId = s.sessionId "
//	    + " and s.assessment.uid = r.uid " + " and r.contentId =? "
//	    + " group by v.sessionId, v.assessmentQuestion.uid ";

    public List<AssessmentResult> getAssessmentResult(Long assessmentUid, Long userId) {
	return getHibernateTemplate().find(FIND_BY_ASSESSMENT_AND_USER, new Object[] { userId, assessmentUid });
    }
    
    public AssessmentResult getLastAssessmentResult(Long assessmentUid, Long userId) {
	List list = getHibernateTemplate().find(FIND_BY_ASSESSMENT_AND_USER, new Object[] { userId, assessmentUid });
	if (list == null || list.size() == 0) {
	    return null;
	} else {
	    return (AssessmentResult) list.get(list.size()-1);
	}
    }

    public int getAssessmentResultCount(Long assessmentUid, Long userId) {
	List list = getHibernateTemplate().find(FIND_ASSESSMENT_RESULT_COUNT_BY_ASSESSMENT_AND_USER, new Object[] { userId, assessmentUid });
	if (list == null || list.size() == 0) {
	    return 0;   
	} else {
	    return ((Number) list.get(0)).intValue();
	}
    }

//    public Map<Long, Integer> getSummary(Long contentId) {
//	// Note: Hibernate 3.1 query.uniqueResult() returns Integer, Hibernate 3.2 query.uniqueResult() returns Long
//	List<Object[]> result = getHibernateTemplate().find(FIND_SUMMARY, contentId);
//	Map<Long, Integer> summaryList = new HashMap<Long, Integer>(result.size());
//	for (Object[] list : result) {
//	    if (list[1] != null) {
//		summaryList.put((Long) list[0], new Integer(((Number) list[1]).intValue()));
//	    }
//	}
//	return summaryList;
//
//    }

//    public List<AssessmentResult> getAssessmentResultBySession(Long sessionId, Long assessmentUid) {
//	return getHibernateTemplate().find(FIND_BY_ASSESSMENT_AND_SESSION, new Object[] { sessionId, assessmentUid });
//    }

}

 