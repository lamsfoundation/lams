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

import org.hibernate.FlushMode;
import org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardUserDAO;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardUser;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
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
		List usersReturned = getSession().createQuery(FIND_NB_USER_BY_SESSION)
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
			List list = getSession().createQuery(FIND_NB_USER)
				.setLong(0,userId.longValue())
				.list();
			
			if(list != null && list.size() > 0){
				NoticeboardUser nb = (NoticeboardUser) list.get(0);
				this.getSession().setFlushMode(FlushMode.AUTO);
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
    
    
}
