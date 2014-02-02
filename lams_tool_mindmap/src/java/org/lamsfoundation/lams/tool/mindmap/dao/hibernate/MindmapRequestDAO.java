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

import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.tool.mindmap.dao.IMindmapRequestDAO;
import org.lamsfoundation.lams.tool.mindmap.model.MindmapRequest;

/**
 * MindmapRequestDAO
 * @author Ruslan Kazakov
 */
public class MindmapRequestDAO extends BaseDAO implements IMindmapRequestDAO {
    private static final String SQL_QUERY_FIND_REQUESTS_AFTER_GLOBAL_ID =
	" from " + MindmapRequest.class.getName() + " mr where mr.globalId > ? and " +
	" mr.mindmap.uid = ? and mr.user.uid <> ? and mr.user.mindmapSession.sessionId = ? order by mr.globalId ";
    
    private static final String SQL_QUERY_FIND_REQUEST_BY_UNIQUE_ID =
	" from " + MindmapRequest.class.getName() + " mr where mr.uniqueId = ? and mr.user.uid = ? and " +
	" mr.mindmap.uid = ? and mr.globalId > ? ";
    
    private static final String SQL_QUERY_FIND_LAST_GLOBAL_ID_BY_MINDMAP =
	" select mr.globalId from " + MindmapRequest.class.getName() + " mr where mr.mindmap.uid = ? and " +
	" mr.user.mindmapSession.sessionId = ? order by mr.globalId desc limit 1 ";
    
    private static final String SQL_QUERY_FIND_REQUESTS_BY_USER_ID =
		" from " + MindmapRequest.class.getName() + " mr where mr.user.userId = ? ";
    
    public void saveOrUpdate(MindmapRequest mindmapRequest) {
	this.getHibernateTemplate().saveOrUpdate(mindmapRequest);
    }
    
    public List getLastRequestsAfterGlobalId(Long globalId, Long mindmapId, Long userId, Long sessionId) {
	return this.getHibernateTemplate().find(SQL_QUERY_FIND_REQUESTS_AFTER_GLOBAL_ID, 
		new Object[]{globalId, mindmapId, userId, sessionId});
    }
    
    public MindmapRequest getRequestByUniqueId(Long uniqueId, Long userId, Long mindmapId, Long globalId) {
	List list = this.getHibernateTemplate().find(SQL_QUERY_FIND_REQUEST_BY_UNIQUE_ID, 
		new Object[]{uniqueId, userId, mindmapId, globalId}); 
	if (list != null && list.size() > 0)
	    return (MindmapRequest) list.get(list.size()-1);
	else
	    return null;
    }
    
    public Long getLastGlobalIdByMindmapId(Long mindmapId, Long sessionId) {
	List list = this.getHibernateTemplate().find(SQL_QUERY_FIND_LAST_GLOBAL_ID_BY_MINDMAP, 
		new Object[]{mindmapId, sessionId}); 
	if (list != null && list.size() > 0)
	    return ((Number) list.get(0)).longValue();
	else
	    return 0l;
    }
    
    @SuppressWarnings("unchecked")
    public List<MindmapRequest> getRequestsByUserId(Long userId) {
	return this.getHibernateTemplate().find(SQL_QUERY_FIND_REQUESTS_BY_USER_ID, userId);
    }
}
