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
package org.lamsfoundation.lams.tool.noticeboard.dao.hibernate;

import java.util.List;

import org.hibernate.FlushMode;
import org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardUserDAO;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardUser;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardSession;

/**
 * @author mtruong
 * <p>Hibernate implementation for database access to Noticeboard users (learners) for the noticeboard tool.</p>
 */
public class NoticeboardUserDAO extends HibernateDaoSupport implements INoticeboardUserDAO {
    
	private static final String FIND_NB_USER = "from " + NoticeboardUser.class.getName() + " as nb where nb.userId=?";
	
	private static final String FIND_NB_USER_BY_SESSION = "from " + NoticeboardUser.class.getName() + " as nb where nb.userId=? and nb.nbSession.nbSessionId=?";
	
    private static final String COUNT_USERS_IN_SESSION = "select nu.userId from NoticeboardUser nu where nu.nbSession= :nbSession";
   
  
	/** @see org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardUserDAO#getNbUserByID(java.lang.Long) */
	public NoticeboardUser getNbUser(Long userId, Long sessionId)
	{
	    String query = "from NoticeboardUser user where user.userId=? and user.nbSession.nbSessionId=?";
	    Object[] values = new Object[2];
	    values[0] = userId;
	    values[1] = sessionId;
	    List users = getHibernateTemplate().find(query,values);
		if(users!=null && users.size() == 0)
		{			
			return null;
		}
		else
		{
			return (NoticeboardUser)users.get(0);
		}
	}
	
	/** @see org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardUserDAO#getNbUserBySession(java.lang.Long, java.lang.Long)*/
	public NoticeboardUser getNbUserBySession(Long userId, Long sessionId)
	{	
		List usersReturned = getSessionFactory().getCurrentSession().createQuery(FIND_NB_USER_BY_SESSION)
			.setLong(0,userId.longValue())
			.setLong(1, sessionId.longValue())
			.list();
	
		if(usersReturned != null && usersReturned.size() > 0){
			NoticeboardUser nb = (NoticeboardUser) usersReturned.get(0);
			return nb;
		}
		else
			return null;

	}

	/** @see org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardUserDAO#saveNbUser(org.lamsfoundation.lams.tool.noticeboard.NoticeboardUser) */
	public void saveNbUser(NoticeboardUser nbUser)
    {
    	this.getHibernateTemplate().save(nbUser);
    }
	
	/** @see org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardUserDAO#updateNbUser(org.lamsfoundation.lams.tool.noticeboard.NoticeboardUser) */
    public void updateNbUser(NoticeboardUser nbUser)
    {
    	this.getHibernateTemplate().update(nbUser);
    }
    
    /** @see org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardUserDAO#removeNbUser(java.lang.Long) */
    public void removeNbUser(Long userId)
    {
		if ( userId != null) {
			//String query = "from org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent as nb where nb.nbContentId=?";
			List list = getSessionFactory().getCurrentSession().createQuery(FIND_NB_USER)
				.setLong(0,userId.longValue())
				.list();
			
			if(list != null && list.size() > 0){
				NoticeboardUser nb = (NoticeboardUser) list.get(0);
				getSessionFactory().getCurrentSession().setFlushMode(FlushMode.AUTO);
				this.getHibernateTemplate().delete(nb);
				this.getHibernateTemplate().flush();
			}
		}
      
    }
    
    /** @see org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardUserDAO#removeNbUser(org.lamsfoundation.lams.tool.noticeboard.NoticeboardUser) */
    public void removeNbUser(NoticeboardUser nbUser)
    {
       // this.getHibernateTemplate().delete(nbUser);
    	removeNbUser(nbUser.getUserId());
    }
    
    /** @see org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardUserDAO#getNumberOfUsers((org.lamsfoundation.lams.tool.noticeboard.NoticeboardSession) */
    public int getNumberOfUsers(NoticeboardSession nbSession)
    {
        return (getHibernateTemplate().findByNamedParam(COUNT_USERS_IN_SESSION,
	            "nbSession",
				nbSession)).size();
    }
    
    public List getNbUsersBySession(Long sessionId) {
    	String query = "from NoticeboardUser user where user.nbSession.nbSessionId=?";
	    return getHibernateTemplate().find(query,sessionId);
    }
}