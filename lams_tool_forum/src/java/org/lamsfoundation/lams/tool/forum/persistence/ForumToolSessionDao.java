/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */
package org.lamsfoundation.lams.tool.forum.persistence;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class ForumToolSessionDao extends HibernateDaoSupport {

	private static final String SQL_QUERY_FIND_BY_SESSION_ID = "from " + ForumToolSession.class.getName() + " where session_id=?";
	private static final String SQL_QUERY_FIND_BY_CONTENT_ID = "select s from " + Forum.class.getName()+ " as f, " +
													ForumToolSession.class.getName() + " as s" + 
													" where f.contentId=? and s.forum.uid=f.uid";
	
	public ForumToolSession getBySessionId(Long sessionId) {
		List list = this.getHibernateTemplate().find(SQL_QUERY_FIND_BY_SESSION_ID,sessionId);
		if(list == null || list.isEmpty())
			return null;
		return (ForumToolSession) list.get(0);
	}
	
	public void saveOrUpdate(ForumToolSession session){
		this.getHibernateTemplate().saveOrUpdate(session);
		this.getHibernateTemplate().flush();
	}

	public List getByContentId(Long contentID) {
		List list = this.getHibernateTemplate().find(SQL_QUERY_FIND_BY_CONTENT_ID,contentID);
		return list;
	}

}
