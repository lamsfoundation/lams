/* 
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
USA

http://www.gnu.org/licenses/gpl.txt 
*/

/*
 * Created on Jul 1, 2005
 */
package org.lamsfoundation.lams.tool.noticeboard.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardUserDAO;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardUser;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardSession;

/**
 * @author mtruong
 * <p>Hibernate implementation for database access to Noticeboard users (learners) for the noticeboard tool.</p>
 */
public class NoticeboardUserDAO extends HibernateDaoSupport implements INoticeboardUserDAO {
    
    private static final String COUNT_USERS_IN_SESSION = "select nu.userId from NoticeboardUser nu where nu.nbSession= :nbSession";
   
    
    
    /** @see org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardUserDAO#getNbUserByUID(java.lang.Long) */
	public NoticeboardUser getNbUserByUID(Long uid)
	{
		 return (NoticeboardUser) this.getHibernateTemplate()
         .get(NoticeboardUser.class, uid);
	}
	
	/** @see org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardUserDAO#getNbUserByID(java.lang.Long) */
	public NoticeboardUser getNbUserByID(Long userId)
	{
	    String query = "from NoticeboardUser user where user.userId=?";
	    List users = getHibernateTemplate().find(query,userId);
		
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
		String query = "select nu from NoticeboardUser nu where nu.userId=? and nu.nbSession.nbSessionId=?";
		Long[] bindingValues = new Long[2];
		bindingValues[0] = userId;
		bindingValues[1] = sessionId;
		List usersReturned = getHibernateTemplate().find(query, bindingValues); //although only one or no users should be returned
		
		if (usersReturned!= null && usersReturned.size() == 0)
		{
			return null;
		}
		else
		{
			return (NoticeboardUser)usersReturned.get(0);
		}

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
        String query = "from NoticeboardUser as user where user.userId =";
        StringBuffer sb = new StringBuffer(query);
        sb.append(userId.longValue());
        
        String queryString = sb.toString();
           
        this.getHibernateTemplate().delete(queryString);
      
    }
    
    /** @see org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardUserDAO#removeNbUser(org.lamsfoundation.lams.tool.noticeboard.NoticeboardUser) */
    public void removeNbUser(NoticeboardUser nbUser)
    {
        this.getHibernateTemplate().delete(nbUser);
    }
    
    /** @see org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardUserDAO#getNumberOfUsers((org.lamsfoundation.lams.tool.noticeboard.NoticeboardSession) */
    public int getNumberOfUsers(NoticeboardSession nbSession)
    {
        return (getHibernateTemplate().findByNamedParam(COUNT_USERS_IN_SESSION,
	            "nbSession",
				nbSession)).size();
    }
    
    
}
