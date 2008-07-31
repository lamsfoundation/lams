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
package org.lamsfoundation.lams.tool.daco.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.tool.daco.dao.DacoQuestionVisitDAO;
import org.lamsfoundation.lams.tool.daco.model.Daco;
import org.lamsfoundation.lams.tool.daco.model.DacoQuestionVisitLog;
import org.lamsfoundation.lams.tool.daco.model.DacoSession;

public class DacoQuestionVisitDAOHibernate extends BaseDAOHibernate implements DacoQuestionVisitDAO {

	private static final String FIND_BY_QUESTION_AND_USER = "from " + DacoQuestionVisitLog.class.getName()
			+ " as r where r.user.userId = ? and r.dacoQuestion.uid=?";

	private static final String FIND_BY_QUESTION_AND_SESSION = "from " + DacoQuestionVisitLog.class.getName()
			+ " as r where r.sessionId = ? and r.dacoQuestion.uid=?";

	private static final String FIND_VIEW_COUNT_BY_USER = "select count(*) from " + DacoQuestionVisitLog.class.getName()
			+ " as r where  r.sessionId=? and  r.user.userId =?";

	private static final String FIND_SUMMARY = "select v.dacoQuestion.uid, count(v.dacoQuestion) from  "
			+ DacoQuestionVisitLog.class.getName() + " as v , " + DacoSession.class.getName() + " as s, " + Daco.class.getName()
			+ "  as r " + " where v.sessionUid = s.uid " + " and s.daco.uid = r.uid " + " and r.contentId =? "
			+ " group by v.sessionUid, v.dacoQuestion.uid ";

	public DacoQuestionVisitLog getDacoQuestionLog(Long questionUid, Long userId) {
		List list = getHibernateTemplate().find(DacoQuestionVisitDAOHibernate.FIND_BY_QUESTION_AND_USER,
				new Object[] { userId, questionUid });
		if (list == null || list.size() == 0) {
			return null;
		}
		return (DacoQuestionVisitLog) list.get(0);
	}

	public List<DacoQuestionVisitLog> getDacoQuestionLogBySession(Long sessionId, Long questionUid) {

		return getHibernateTemplate().find(DacoQuestionVisitDAOHibernate.FIND_BY_QUESTION_AND_SESSION,
				new Object[] { sessionId, questionUid });
	}

}
