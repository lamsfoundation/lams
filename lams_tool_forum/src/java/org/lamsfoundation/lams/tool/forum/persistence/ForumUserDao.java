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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $$Id$$ */	

package org.lamsfoundation.lams.tool.forum.persistence;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class ForumUserDao extends HibernateDaoSupport{

	private static final String SQL_QUERY_FIND_BY_USER_ID_SESSION_ID = "from " + ForumUser.class.getName() + " as f" 
									+ " where user_id=? and f.session.sessionId=?";
	
	private static final String SQL_QUERY_FIND_BY_USER_ID = "from " + ForumUser.class.getName() + " as f" 
							+ " where user_id=? and session_id is null";
	
	private static final String SQL_QUERY_FIND_BY_SESSION_ID = "from " + 
									ForumUser.class.getName() + " as f " + 
									" where f.session.sessionId=?";


	public List getBySessionId(Long sessionID) {
		return this.getHibernateTemplate().find(SQL_QUERY_FIND_BY_SESSION_ID, sessionID);
	}
	
	public void save(ForumUser forumUser){
		this.getHibernateTemplate().save(forumUser);
		this.getHibernateTemplate().flush();
	}
	public ForumUser getByUserIdAndSessionId(Long userId, Long sessionId) {
		List list =  this.getHibernateTemplate().find(SQL_QUERY_FIND_BY_USER_ID_SESSION_ID
				, new Object[]{userId,sessionId});
		
		if(list == null || list.isEmpty())
			return null;
		
		return (ForumUser) list.get(0);
	}

	public ForumUser getByUserId(Long userId) {
		List list =  this.getHibernateTemplate().find(SQL_QUERY_FIND_BY_USER_ID, userId);
		
		if(list == null || list.isEmpty())
			return null;
		
		return (ForumUser) list.get(0);
	}
	public ForumUser getByUid(Long userUid) {
		
		return (ForumUser) this.getHibernateTemplate().get(ForumUser.class,userUid);
	}

	public void delete(ForumUser user) {
		this.getHibernateTemplate().delete(user);
		this.getHibernateTemplate().flush();
	}


}
