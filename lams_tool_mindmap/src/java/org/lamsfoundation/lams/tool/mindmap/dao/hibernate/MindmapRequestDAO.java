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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $$Id$$ */
package org.lamsfoundation.lams.tool.mindmap.dao.hibernate;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.mindmap.dao.IMindmapRequestDAO;
import org.lamsfoundation.lams.tool.mindmap.model.MindmapRequest;
import org.springframework.stereotype.Repository;

/**
 * MindmapRequestDAO
 * 
 * @author Ruslan Kazakov
 */
@Repository
public class MindmapRequestDAO extends LAMSBaseDAO implements IMindmapRequestDAO {
	private static final String SQL_QUERY_FIND_REQUESTS_AFTER_GLOBAL_ID = " from " + MindmapRequest.class.getName()
			+ " mr where mr.globalId > ? and "
			+ " mr.mindmap.uid = ? and mr.user.uid <> ? and mr.user.mindmapSession.sessionId = ? order by mr.globalId ";

	private static final String SQL_QUERY_FIND_REQUEST_BY_UNIQUE_ID = " from " + MindmapRequest.class.getName()
			+ " mr where mr.uniqueId = ? and mr.user.uid = ? and " + " mr.mindmap.uid = ? and mr.globalId > ? ";

	private static final String SQL_QUERY_FIND_LAST_GLOBAL_ID_BY_MINDMAP = " select mr.globalId from "
			+ MindmapRequest.class.getName() + " mr where mr.mindmap.uid = ? and "
			+ " mr.user.mindmapSession.sessionId = ? order by mr.globalId desc";

	private static final String SQL_QUERY_FIND_REQUESTS_BY_USER_ID = " from " + MindmapRequest.class.getName()
			+ " mr where mr.user.userId = ? ";

	public void saveOrUpdate(MindmapRequest mindmapRequest) {
		getSession().saveOrUpdate(mindmapRequest);
	}

	public List getLastRequestsAfterGlobalId(Long globalId, Long mindmapId, Long userId, Long sessionId) {
		return this.doFind(SQL_QUERY_FIND_REQUESTS_AFTER_GLOBAL_ID,
				new Object[] { globalId, mindmapId, userId, sessionId });
	}

	public MindmapRequest getRequestByUniqueId(Long uniqueId, Long userId, Long mindmapId, Long globalId) {
		List list = this.doFind(SQL_QUERY_FIND_REQUEST_BY_UNIQUE_ID,
				new Object[] { uniqueId, userId, mindmapId, globalId });
		if (list != null && list.size() > 0)
			return (MindmapRequest) list.get(list.size() - 1);
		else
			return null;
	}

	public Long getLastGlobalIdByMindmapId(Long mindmapId, Long sessionId) {
		Query q = getSession().createQuery(SQL_QUERY_FIND_LAST_GLOBAL_ID_BY_MINDMAP);
		q.setParameter(0, mindmapId);
		q.setParameter(1, sessionId);
		q.setMaxResults(1);
		Object result = q.uniqueResult();
		return result != null ? ((Number) result).longValue() : null;
	}

	@SuppressWarnings("unchecked")
	public List<MindmapRequest> getRequestsByUserId(Long userId) {
		return (List<MindmapRequest>) this.doFind(SQL_QUERY_FIND_REQUESTS_BY_USER_ID, userId);
	}
}
