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

package org.lamsfoundation.lams.tool.forum.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.forum.dao.IForumToolSessionDAO;
import org.lamsfoundation.lams.tool.forum.model.Forum;
import org.lamsfoundation.lams.tool.forum.model.ForumToolSession;
import org.springframework.stereotype.Repository;

@Repository
public class ForumToolSessionDao extends LAMSBaseDAO implements IForumToolSessionDAO {

    private static final String SQL_QUERY_FIND_BY_SESSION_ID = "from " + ForumToolSession.class.getName()
	    + " where session_id=?";
    private static final String SQL_QUERY_FIND_BY_CONTENT_ID = "select s from " + Forum.class.getName() + " as f, "
	    + ForumToolSession.class.getName() + " as s" + " where f.contentId=? and s.forum.uid=f.uid";

    @Override
    public ForumToolSession getBySessionId(Long sessionId) {
	List list = this.doFind(SQL_QUERY_FIND_BY_SESSION_ID, sessionId);
	if (list == null || list.isEmpty()) {
	    return null;
	}
	return (ForumToolSession) list.get(0);
    }

    @Override
    public void saveOrUpdate(ForumToolSession session) {
	this.getSession().saveOrUpdate(session);
    }

    @Override
    public List<ForumToolSession> getByContentId(Long contentID) {
	List list = this.doFindCacheable(SQL_QUERY_FIND_BY_CONTENT_ID, contentID);
	return list;
    }

    @Override
    public void delete(Long sessionId) {
	ForumToolSession session = getBySessionId(sessionId);
	this.getSession().delete(session);
    }

    @Override
    public void delete(ForumToolSession session) {
	this.getSession().delete(session);
    }
}
