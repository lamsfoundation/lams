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

package org.lamsfoundation.lams.tool.forum.persistence.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.forum.persistence.ForumUser;
import org.lamsfoundation.lams.tool.forum.persistence.IForumUserDAO;
import org.springframework.stereotype.Repository;

@Repository
public class ForumUserDao extends LAMSBaseDAO implements IForumUserDAO {

	private static final String SQL_QUERY_FIND_BY_USER_ID_SESSION_ID = "from " + ForumUser.class.getName() + " as f" 
									+ " where user_id=? and f.session.sessionId=?";
	
	private static final String SQL_QUERY_FIND_BY_USER_ID = "from " + ForumUser.class.getName() + " as f" 
							+ " where user_id=? and session_id is null";
	
	private static final String SQL_QUERY_FIND_BY_SESSION_ID = "from " + 
									ForumUser.class.getName() + " as f " + 
									" where f.session.sessionId=?";


	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.forum.persistence.hibernate.IForumUserDAO#getBySessionId(java.lang.Long)
	 */
	@Override
	public List getBySessionId(Long sessionID) {
		return this.doFind(SQL_QUERY_FIND_BY_SESSION_ID, sessionID);
	}
	
	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.forum.persistence.hibernate.IForumUserDAO#save(org.lamsfoundation.lams.tool.forum.persistence.ForumUser)
	 */
	@Override
	public void save(ForumUser forumUser){
		this.getSession().save(forumUser);
	}
	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.forum.persistence.hibernate.IForumUserDAO#getByUserIdAndSessionId(java.lang.Long, java.lang.Long)
	 */
	@Override
	public ForumUser getByUserIdAndSessionId(Long userId, Long sessionId) {
		List list =  this.doFind(SQL_QUERY_FIND_BY_USER_ID_SESSION_ID
				, new Object[]{userId,sessionId});
		
		if(list == null || list.isEmpty())
			return null;
		
		return (ForumUser) list.get(0);
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.forum.persistence.hibernate.IForumUserDAO#getByUserId(java.lang.Long)
	 */
	@Override
	public ForumUser getByUserId(Long userId) {
		List list =  this.doFind(SQL_QUERY_FIND_BY_USER_ID, userId);
		
		if(list == null || list.isEmpty())
			return null;
		
		return (ForumUser) list.get(0);
	}
	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.forum.persistence.hibernate.IForumUserDAO#getByUid(java.lang.Long)
	 */
	@Override
	public ForumUser getByUid(Long userUid) {
		
		return (ForumUser) this.getSession().get(ForumUser.class,userUid);
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.forum.persistence.hibernate.IForumUserDAO#delete(org.lamsfoundation.lams.tool.forum.persistence.ForumUser)
	 */
	@Override
	public void delete(ForumUser user) {
		this.getSession().delete(user);
	}


}
