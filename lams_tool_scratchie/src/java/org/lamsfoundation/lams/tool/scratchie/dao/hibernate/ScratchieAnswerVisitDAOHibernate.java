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
package org.lamsfoundation.lams.tool.scratchie.dao.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.scratchie.dao.ScratchieAnswerVisitDAO;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieAnswer;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieAnswerVisitLog;
import org.springframework.stereotype.Repository;

@Repository
public class ScratchieAnswerVisitDAOHibernate extends LAMSBaseDAO implements ScratchieAnswerVisitDAO {

	private static final String FIND_BY_SESSION_AND_ANSWER = "from " + ScratchieAnswerVisitLog.class.getName()
			+ " as r where r.sessionId = ? and r.scratchieAnswer.uid=?";

	private static final String FIND_BY_SESSION_AND_ITEM = "from " + ScratchieAnswerVisitLog.class.getName()
			+ " as r where r.sessionId=? and r.scratchieAnswer.scratchieItem.uid = ?  order by r.accessDate asc";

	private static final String FIND_FIRST_SCRATCHED_ANSWER_BY_SESSION_AND_ITEM = "SELECT r.scratchieAnswer from "
			+ ScratchieAnswerVisitLog.class.getName()
			+ " as r where r.sessionId=? and r.scratchieAnswer.scratchieItem.uid = ?  order by r.accessDate asc";

	private static final String FIND_BY_SESSION = "from " + ScratchieAnswerVisitLog.class.getName()
			+ " as r where r.sessionId=? order by r.accessDate asc";

	private static final String FIND_VIEW_COUNT_BY_SESSION = "select count(*) from "
			+ ScratchieAnswerVisitLog.class.getName() + " as r where  r.sessionId=?";

	@Override
	public ScratchieAnswerVisitLog getLog(Long answerUid, Long sessionId) {
		List list = doFind(FIND_BY_SESSION_AND_ANSWER, new Object[] { sessionId, answerUid });
		if (list == null || list.size() == 0)
			return null;
		return (ScratchieAnswerVisitLog) list.get(0);
	}

	@Override
	public int getLogCountTotal(Long sessionId) {
		List list = doFind(FIND_VIEW_COUNT_BY_SESSION, new Object[] { sessionId });
		if (list == null || list.size() == 0)
			return 0;
		return ((Number) list.get(0)).intValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ScratchieAnswerVisitLog> getLogsBySessionAndItem(Long sessionId, Long itemUid) {
		return (List<ScratchieAnswerVisitLog>) doFind(FIND_BY_SESSION_AND_ITEM,
				new Object[] { sessionId, itemUid });
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ScratchieAnswerVisitLog> getLogsBySession(Long sessionId) {
		return (List<ScratchieAnswerVisitLog>) doFind(FIND_BY_SESSION, new Object[] { sessionId });
	}

	@Override
	public ScratchieAnswer getFirstScratchedAnswerBySessionAndItem(Long sessionId, Long itemUid) {
		Query q = getSession().createQuery(FIND_FIRST_SCRATCHED_ANSWER_BY_SESSION_AND_ITEM);
		q.setParameter(0, sessionId);
		q.setParameter(1, itemUid);
		q.setMaxResults(1);
		return (ScratchieAnswer) q.uniqueResult();
	}

}
