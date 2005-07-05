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
/**
 * @author mtruong
 */
public class NoticeboardUserDAO extends HibernateDaoSupport implements INoticeboardUserDAO {
    
    /**
	 * <p>Return the persistent instance of a NoticeboardUser 
	 * with the given identifier <code>uid</code>, returns null if not found. </p>
	 * 
	 * @param uid an identifier for the NoticeboardUser.
	 * @return the persistent instance of a NoticeboardUser or null if not found
	 */
	public NoticeboardUser getNbUserByUID(Long uid)
	{
		 return (NoticeboardUser) this.getHibernateTemplate()
         .get(NoticeboardUser.class, uid);
	}
	
	/**
	 * <p> Return the persistent instance of a NoticeboardUser
	 * with the given user id <code>userId</code>,
	 * returns null if not found.</p>
	 * 
	 * @param userId The id of a NoticeboardUser
	 * @return the persistent instance of a NoticeboardUser or null if not found.
	 */
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

	/**
	 * <p>Persist the given persistent instance of NoticeboardUser.</p>
	 * 
	 * @param nbUser The instance of NoticeboardUser to persist.
	 */
	public void saveNbUser(NoticeboardUser nbUser)
    {
    	this.getHibernateTemplate().save(nbUser);
    }
	
	/**
     * <p>Update the given persistent instance of NoticeboardUser.</p>
     * 
     * @param nbUser The instance of NoticeboardUser to persist.
     */
    public void updateNbUser(NoticeboardUser nbUser)
    {
    	this.getHibernateTemplate().update(nbUser);
    }
    
    /**
     * <p>Delete the given instance of NoticeboardUser with the
     * given user id <code>userId</code>
     * 
     * @param userId The noticeboard user id.
     */
    public void removeNbUser(Long userId)
    {
        String query = "from NoticeboardUser as user where user.userId =";
        StringBuffer sb = new StringBuffer(query);
        sb.append(userId.longValue());
        
        String queryString = sb.toString();
           
        this.getHibernateTemplate().delete(queryString);
      
    }
    /**
     * <p>Delete the given instance of NoticeboardUser</p>
     * 
     * @param nbUser The instance of NoticeboardUser to delete. 
     */
    public void removeNbUser(NoticeboardUser nbUser)
    {
        this.getHibernateTemplate().delete(nbUser);
    }
    
    
}
