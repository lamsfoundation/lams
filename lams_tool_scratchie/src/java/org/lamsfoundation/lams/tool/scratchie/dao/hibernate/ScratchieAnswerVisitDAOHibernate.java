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

package org.lamsfoundation.lams.tool.scratchie.dao.hibernate;

import java.util.List;

import org.hibernate.query.Query;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.scratchie.dao.ScratchieAnswerVisitDAO;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieAnswerVisitLog;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieSession;
import org.springframework.stereotype.Repository;

@Repository
public class ScratchieAnswerVisitDAOHibernate extends LAMSBaseDAO implements ScratchieAnswerVisitDAO {
    private static final String FIND_COUNT_BY_SESSION = "SELECT COUNT(*) FROM "
	    + ScratchieAnswerVisitLog.class.getName() + " AS r WHERE  r.sessionId=?";
    
    private static final String FIND_COUNT_BY_SESSION_AND_ITEM = "SELECT COUNT(*) FROM "
	    + ScratchieAnswerVisitLog.class.getName() + " AS r WHERE  r.sessionId=? AND r.qbToolQuestion.uid = ?";

    @Override
    public ScratchieAnswerVisitLog getLog(Long optionUid, Long itemUid, Long sessionId) {
	final String FIND_BY_SESSION_AND_OPTION = "FROM " + ScratchieAnswerVisitLog.class.getName()
		+ " AS r WHERE r.sessionId = ? AND r.qbToolQuestion.uid = ? AND r.qbOption.uid=?";
	
	List list = doFind(FIND_BY_SESSION_AND_OPTION, new Object[] { sessionId, itemUid, optionUid });
	if (list == null || list.size() == 0) {
	    return null;
	}
	return (ScratchieAnswerVisitLog) list.get(0);
    }
    
    @Override
    public ScratchieAnswerVisitLog getLog(Long sessionId, Long itemUid, boolean isCaseSensitive, String answer) {
	final String FIND_BY_SESSION_AND_ANSWER = "FROM " + ScratchieAnswerVisitLog.class.getName()
		+ " AS r WHERE r.sessionId = :sessionId AND r.qbToolQuestion.uid = :itemUid AND "
		+ (isCaseSensitive ? "CAST(r.answer AS binary)=CAST(:answer AS binary)" : "r.answer=:answer");
	
	Query<ScratchieAnswerVisitLog> q = getSession().createQuery(FIND_BY_SESSION_AND_ANSWER, ScratchieAnswerVisitLog.class);
	q.setParameter("sessionId", sessionId);
	q.setParameter("itemUid", itemUid);
	q.setParameter("answer", answer);
	return q.uniqueResult();
    }

    @Override
    public int getLogCountTotal(Long sessionId) {
	List list = doFind(FIND_COUNT_BY_SESSION, new Object[] { sessionId });
	if (list == null || list.size() == 0) {
	    return 0;
	}
	return ((Number) list.get(0)).intValue();
    }
    
    @Override
    public int getLogCountPerItem(Long sessionId, Long itemUid) {
	List list = doFind(FIND_COUNT_BY_SESSION_AND_ITEM, new Object[] { sessionId, itemUid });
	if (list == null || list.size() == 0) {
	    return 0;
	}
	return ((Number) list.get(0)).intValue();
    }

    @Override
    public List<ScratchieAnswerVisitLog> getLogsBySessionAndItem(Long sessionId, Long itemUid) {
	final String FIND_BY_SESSION_AND_ITEM = "FROM " + ScratchieAnswerVisitLog.class.getName()
		+ " AS r WHERE r.sessionId=:sessionId AND r.qbToolQuestion.uid =:itemUid  ORDER BY r.accessDate ASC";

	Query<ScratchieAnswerVisitLog> query = getSession().createQuery(FIND_BY_SESSION_AND_ITEM,
		ScratchieAnswerVisitLog.class);
	query.setParameter("sessionId", sessionId);
	query.setParameter("itemUid", itemUid);
	return query.list();
    }

    @Override
    public List<ScratchieAnswerVisitLog> getLogsBySession(Long sessionId) {
	final String FIND_BY_SESSION = "FROM " + ScratchieAnswerVisitLog.class.getName()
		    + " AS r WHERE r.sessionId=:sessionId ORDER BY r.accessDate ASC";
	
	Query<ScratchieAnswerVisitLog> query = getSession().createQuery(FIND_BY_SESSION, ScratchieAnswerVisitLog.class);
	query.setParameter("sessionId", sessionId);
	return query.list();
    }
    
    @Override
    public List<ScratchieAnswerVisitLog> getLogsByScratchieUid(Long scratchieUid) {
	final String FIND_BY_SCRATCHIE_UID = "SELECT log FROM " + ScratchieAnswerVisitLog.class.getName() + " AS log, "
		+ ScratchieSession.class.getName() + " AS session "
		+ " WHERE log.sessionId = session.sessionId AND session.scratchie.uid=:scratchieUid ORDER BY log.accessDate ASC";

	Query<ScratchieAnswerVisitLog> query = getSession().createQuery(FIND_BY_SCRATCHIE_UID, ScratchieAnswerVisitLog.class);
	query.setParameter("scratchieUid", scratchieUid);
	return query.list();
    }

}
