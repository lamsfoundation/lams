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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lamsfoundation.lams.tool.scratchie.dao.ScratchieAnswerVisitDAO;
import org.lamsfoundation.lams.tool.scratchie.model.Scratchie;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieAnswerVisitLog;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieItem;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieSession;

public class ScratchieAnswerVisitDAOHibernate extends BaseDAOHibernate implements ScratchieAnswerVisitDAO {

    private static final String FIND_BY_ITEM_AND_USER = "from " + ScratchieAnswerVisitLog.class.getName()
	    + " as r where r.user.userId = ? and r.scratchieAnswer.uid=?";

    private static final String FIND_BY_SESSION_AND_USER = "from " + ScratchieAnswerVisitLog.class.getName()
	    + " as r where r.sessionId = ? and r.user.userId=? order by r.accessDate asc";

    private static final String FIND_VIEW_COUNT_BY_USER = "select count(*) from "
	    + ScratchieAnswerVisitLog.class.getName() + " as r where  r.sessionId=? and  r.user.userId =?";
    
    private static final String FIND_VIEW_COUNT_BY_USER_AND_ITEM = "select count(*) from "
	    + ScratchieAnswerVisitLog.class.getName() + " as l where l.sessionId=? and l.user.userId =? and l.scratchieAnswer.scratchieItem.uid=?";

    private static final String FIND_SUMMARY = "select v.scratchieAnswer.uid, count(v.scratchieAnswer) from  "
	    + ScratchieAnswerVisitLog.class.getName() + " as v , " + ScratchieSession.class.getName() + " as s, "
	    + Scratchie.class.getName() + "  as r " + " where v.sessionId = s.sessionId "
	    + " and s.scratchie.uid = r.uid " + " and r.contentId =? " + " group by v.sessionId, v.scratchieAnswer.uid ";

    public ScratchieAnswerVisitLog getScratchieAnswerLog(Long answerUid, Long userId) {
	List list = getHibernateTemplate().find(FIND_BY_ITEM_AND_USER, new Object[] { userId, answerUid });
	if (list == null || list.size() == 0)
	    return null;
	return (ScratchieAnswerVisitLog) list.get(0);
    }

    public int getLogCountTotal(Long toolSessionId, Long userId) {
	List list = getHibernateTemplate().find(FIND_VIEW_COUNT_BY_USER, new Object[] { toolSessionId, userId });
	if (list == null || list.size() == 0)
	    return 0;
	return ((Number) list.get(0)).intValue();
    }
    
    public int getLogCountPerItem(Long toolSessionId, Long userId, Long itemUid) {
	List list = getHibernateTemplate().find(FIND_VIEW_COUNT_BY_USER_AND_ITEM, new Object[] { toolSessionId, userId, itemUid });
	if (list == null || list.size() == 0)
	    return 0;
	return ((Number) list.get(0)).intValue();
    }

    public Map<Long, Integer> getSummary(Long contentId) {

	// Note: Hibernate 3.1 query.uniqueResult() returns Integer, Hibernate 3.2 query.uniqueResult() returns Long
	List<Object[]> result = getHibernateTemplate().find(FIND_SUMMARY, contentId);
	Map<Long, Integer> summaryList = new HashMap<Long, Integer>(result.size());
	for (Object[] list : result) {
	    if (list[1] != null) {
		summaryList.put((Long) list[0], new Integer(((Number) list[1]).intValue()));
	    }
	}
	return summaryList;

    }

    public List<ScratchieAnswerVisitLog> getLogsBySessionAndUser(Long sessionId, Long userId) {
	return getHibernateTemplate().find(FIND_BY_SESSION_AND_USER, new Object[] { sessionId, userId });
    }

}
