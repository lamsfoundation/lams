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
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.QaUsrResp;
import org.lamsfoundation.lams.tool.qa.dao.IQaUsrRespDAO;
import org.springframework.stereotype.Repository;

/**
 * @author Ozgur Demirtas
 */
@Repository
public class QaUsrRespDAO extends LAMSBaseDAO implements IQaUsrRespDAO {
    private static final String LOAD_ATTEMPT_FOR_USER_AND_QUESTION = "from qaUsrResp in class QaUsrResp "
	    + "where qaUsrResp.qaQueUser.queUsrId=:queUsrId and qaUsrResp.qaQuestion.uid=:questionId";

    private static final String LOAD_ATTEMPT_FOR_SESSION_AND_QUESTION = "from qaUsrResp in class QaUsrResp "
	    + " where qaUsrResp.qaQueUser.qaSession.qaSessionId=:qaSessionId and qaUsrResp.qaQuestion.uid=:questionId";

    private static final String LOAD_ATTEMPT_FOR_SESSION_AND_QUESTION_LIMIT = "from qaUsrResp in class QaUsrResp "
	    + "where qaUsrResp.qaQueUser.qaSession.qaSessionId=:qaSessionId AND qaUsrResp.qaQuestion.uid=:questionId AND qaUsrResp.qaQueUser.queUsrId!=:excludeUserId order by ";
    
    private static final String LOAD_ATTEMPT_FOR_USER = "from qaUsrResp in class QaUsrResp "
	    + "where qaUsrResp.qaQueUser.uid=:userUid order by qaUsrResp.qaQuestion.displayOrder asc";

    private static final String GET_COUNT_RESPONSES_BY_QACONTENT = "SELECT COUNT(*) from " + QaUsrResp.class.getName()
	    + " as r where r.qaQuestion.qaContent.qaContentId=?";

    private static final String GET_COUNT_RESPONSES_FOR_SESSION_AND_QUESTION = "SELECT COUNT(*) from "
	    + QaUsrResp.class.getName()
	    + " as r where r.qaQueUser.qaSession.qaSessionId=? and r.qaQuestion.uid=? AND r.qaQueUser.queUsrId!=?";

    public void createUserResponse(QaUsrResp qaUsrResp) {
    	getSession().setFlushMode(FlushMode.AUTO);
    	getSession().save(qaUsrResp);
    }

    public QaUsrResp getResponseById(Long responseId) {
	return (QaUsrResp) getSession().get(QaUsrResp.class, responseId);
    }

    /**
     * @see org.lamsfoundation.lams.tool.qa.dao.IQaUsrRespDAO#updateUserResponse(org.lamsfoundation.lams.tool.qa.QaUsrResp)
     */
    public void updateUserResponse(QaUsrResp resp) {
	getSessionFactory().getCurrentSession().setFlushMode(FlushMode.AUTO);
	getSession().update(resp);
    }

    public void removeUserResponse(QaUsrResp resp) {
	getSessionFactory().getCurrentSession().setFlushMode(FlushMode.AUTO);
	getSession().delete(resp);
    }

    @Override
    public QaUsrResp getResponseByUserAndQuestion(final Long queUsrId, final Long questionId) {
	List<QaUsrResp> list = getSessionFactory().getCurrentSession().createQuery(LOAD_ATTEMPT_FOR_USER_AND_QUESTION)
		.setLong("queUsrId", queUsrId.longValue()).setLong("questionId", questionId.longValue()).list();
	if (list == null || list.size() == 0) {
	    return null;
	} else {
	    return (QaUsrResp) list.get(list.size() - 1);
	}
    }

    @Override
    public List<QaUsrResp> getResponseBySessionAndQuestion(final Long qaSessionId, final Long questionId) {
	return getSessionFactory().getCurrentSession().createQuery(LOAD_ATTEMPT_FOR_SESSION_AND_QUESTION)
		.setLong("qaSessionId", qaSessionId.longValue()).setLong("questionId", questionId.longValue()).list();
    }

    @Override
    public List<QaUsrResp> getResponsesForTablesorter(final Long qaSessionId, final Long questionId, final Long excludeUserId,
	    int page, int size, int sorting) {
	String sortingOrder = "";
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
	}
	
	return getSessionFactory().getCurrentSession()
		.createQuery(LOAD_ATTEMPT_FOR_SESSION_AND_QUESTION_LIMIT + sortingOrder)
		.setLong("qaSessionId", qaSessionId.longValue()).setLong("questionId", questionId.longValue())
		.setLong("excludeUserId", excludeUserId.longValue()).setFirstResult(page * size).setMaxResults(size)
		.list();
    }

    @Override
    public List<QaUsrResp> getResponsesByUserUid(final Long userUid) {
	List<QaUsrResp> list = getSessionFactory().getCurrentSession().createQuery(LOAD_ATTEMPT_FOR_USER).setLong("userUid", userUid.longValue())
		.list();
	return list;
    }

    public int getCountResponsesByQaContent(final Long qaContentId) {

	List list = doFind(GET_COUNT_RESPONSES_BY_QACONTENT, new Object[] { qaContentId });
	if (list == null || list.size() == 0) {
	    return 0;
	}
	return ((Number) list.get(0)).intValue();
    }

    public int getCountResponsesBySessionAndQuestion(final Long qaSessionId, final Long questionId, final Long excludeUserId) {

	List list = doFind(GET_COUNT_RESPONSES_FOR_SESSION_AND_QUESTION,
		new Object[] { qaSessionId, questionId, excludeUserId });
	if (list == null || list.size() == 0) {
	    return 0;
	}
	return ((Number) list.get(0)).intValue();
    }

}
