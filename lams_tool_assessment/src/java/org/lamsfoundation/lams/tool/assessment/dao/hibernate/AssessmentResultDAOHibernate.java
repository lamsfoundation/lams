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

import java.util.List;

import org.lamsfoundation.lams.tool.assessment.dao.AssessmentResultDAO;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentResult;
 
public class AssessmentResultDAOHibernate extends BaseDAOHibernate implements AssessmentResultDAO {
    
    private static final String FIND_BY_ASSESSMENT_AND_USER = "from " 
	    + AssessmentResult.class.getName()
	    + " as r where r.user.userId = ? and r.assessment.uid=? order by r.startDate asc";
    
    private static final String FIND_BY_ASSESSMENT_AND_USER_AND_FINISHED = "from "
	    + AssessmentResult.class.getName()
	    + " as r where r.user.userId = ? and r.assessment.uid=? and (r.finishDate != null) order by r.startDate asc";

    private static final String FIND_BY_SESSION_AND_USER_AND_FINISHED = "from "
	    + AssessmentResult.class.getName()
	    + " as r where r.user.userId = ? and r.sessionId=? and (r.finishDate != null) order by r.startDate asc";

    private static final String FIND_ASSESSMENT_RESULT_COUNT_BY_ASSESSMENT_AND_USER = "select count(*) from "
	    + AssessmentResult.class.getName()
	    + " as r where r.user.userId=? and r.assessment.uid=? and (r.finishDate != null)";

    private static final String FIND_BY_UID = "from " + AssessmentResult.class.getName() + " as r where r.uid = ?";

    @SuppressWarnings("unchecked")
    public List<AssessmentResult> getAssessmentResults(Long assessmentUid, Long userId) {
	return (List<AssessmentResult>) getHibernateTemplate().find(FIND_BY_ASSESSMENT_AND_USER_AND_FINISHED, new Object[] { userId, assessmentUid });
    }
    
    @SuppressWarnings("unchecked")
    public List<AssessmentResult> getAssessmentResultsBySession(Long sessionId, Long userId) {
	return (List<AssessmentResult>) getHibernateTemplate().find(FIND_BY_SESSION_AND_USER_AND_FINISHED, new Object[] { userId, sessionId });
    }
    
    public AssessmentResult getLastAssessmentResult(Long assessmentUid, Long userId) {
	List list = getHibernateTemplate().find(FIND_BY_ASSESSMENT_AND_USER, new Object[] { userId, assessmentUid });
	if (list == null || list.size() == 0) {
	    return null;
	} else {
	    return (AssessmentResult) list.get(list.size()-1);
	}
    }
    
    public AssessmentResult getLastFinishedAssessmentResult(Long assessmentUid, Long userId) {
	List list = getHibernateTemplate().find(FIND_BY_ASSESSMENT_AND_USER_AND_FINISHED, new Object[] { userId, assessmentUid });
	if (list == null || list.size() == 0) {
	    return null;
	} else {
	    return (AssessmentResult) list.get(list.size()-1);
	}
    }
    
    public AssessmentResult getLastFinishedAssessmentResultBySessionId(Long sessionId, Long userId) {
	List list = getHibernateTemplate().find(FIND_BY_SESSION_AND_USER_AND_FINISHED, new Object[] { userId, sessionId });
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
    
    public AssessmentResult getAssessmentResultByUid(Long assessmentResultUid) {
	List list = getHibernateTemplate().find(FIND_BY_UID, new Object[] { assessmentResultUid });
	if (list == null || list.size() == 0)
	    return null;
	return (AssessmentResult) list.get(0);
    }

}

 