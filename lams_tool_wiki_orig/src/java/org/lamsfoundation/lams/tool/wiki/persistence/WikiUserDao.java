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

package org.lamsfoundation.lams.tool.wiki.persistence;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class WikiUserDao extends HibernateDaoSupport{

	private static final String SQL_QUERY_FIND_BY_USER_ID_SESSION_ID = "from " + WikiUser.class.getName() + " as f" 
									+ " where user_id=? and f.session.sessionId=?";
	
	private static final String SQL_QUERY_FIND_BY_USER_ID = "from " + WikiUser.class.getName() + " as f" 
							+ " where user_id=? and session_id is null";
	
	private static final String SQL_QUERY_FIND_BY_SESSION_ID = "from " + 
									WikiUser.class.getName() + " as f " + 
									" where f.session.sessionId=?";


	public List getBySessionId(Long sessionID) {
		return this.getHibernateTemplate().find(SQL_QUERY_FIND_BY_SESSION_ID, sessionID);
	}
	
	public void save(WikiUser wikiUser){
		this.getHibernateTemplate().save(wikiUser);
	}
	public WikiUser getByUserIdAndSessionId(Long userId, Long sessionId) {
		List list =  this.getHibernateTemplate().find(SQL_QUERY_FIND_BY_USER_ID_SESSION_ID
				, new Object[]{userId,sessionId});
		
		if(list == null || list.isEmpty())
			return null;
		
		return (WikiUser) list.get(0);
	}

	public WikiUser getByUserId(Long userId) {
		List list =  this.getHibernateTemplate().find(SQL_QUERY_FIND_BY_USER_ID, userId);
		
		if(list == null || list.isEmpty())
			return null;
		
		return (WikiUser) list.get(0);
	}
	public WikiUser getByUid(Long userUid) {
		
		return (WikiUser) this.getHibernateTemplate().get(WikiUser.class,userUid);
	}

	public void delete(WikiUser user) {
		this.getHibernateTemplate().delete(user);
	}


}
