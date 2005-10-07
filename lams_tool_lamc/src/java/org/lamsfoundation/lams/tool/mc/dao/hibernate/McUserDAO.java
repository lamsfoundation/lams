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
 * @author ozgurd
 */
package org.lamsfoundation.lams.tool.mc.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.tool.mc.McQueUsr;
import org.lamsfoundation.lams.tool.mc.McSession;
import org.lamsfoundation.lams.tool.mc.dao.IMcUserDAO;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;

/**
 * @author ozgurd
 * <p>Hibernate implementation for database access to Mc users (learners) for the mc tool.</p>
 */
public class McUserDAO extends HibernateDaoSupport implements IMcUserDAO {
    
    private static final String COUNT_USERS_IN_SESSION = "select mu.queUsrId from McQueUsr mu where mu.mcSession= :mcSession";
   
    
    
    /** @see org.lamsfoundation.lams.tool.mc.dao.IMcUserDAO#getMcUserByUID(java.lang.Long) */
	public McQueUsr getMcUserByUID(Long uid)
	{
		 return (McQueUsr) this.getHibernateTemplate()
         .get(McQueUsr.class, uid);
	}
	
	
	
	/** @see org.lamsfoundation.lams.tool.mc.dao.IMcUserDAO#getMcUserByID(java.lang.Long) */
	public McQueUsr findMcUserById(Long userId)
	{
	    String query = "from McQueUsr user where user.queUsrId=?";
	    List users = getHibernateTemplate().find(query,userId);
		
		if(users!=null && users.size() == 0)
		{			
			return null;
		}
		else
		{
			return (McQueUsr)users.get(0);
		}
	}
	
	
	/** @see org.lamsfoundation.lams.tool.mc.dao.IMcUserDAO#getMcUserBySession(java.lang.Long, java.lang.Long)*/
	public McQueUsr getMcUserBySession(Long userId, Long sessionId)
	{
		String query = "select mu from McQueUsr mu where mu.queUsrId=? and mu.mcSession.mcSessionId=?";
		Long[] bindingValues = new Long[2];
		bindingValues[0] = userId;
		bindingValues[1] = sessionId;
		List usersReturned = getHibernateTemplate().find(query, bindingValues); 
		
		if (usersReturned!= null && usersReturned.size() == 0)
		{
			return null;
		}
		else
		{
			return (McQueUsr)usersReturned.get(0);
		}

	}

	
		/** @see org.lamsfoundation.lams.tool.mc.dao.IMcUserDAO#saveMcUser(org.lamsfoundation.lams.tool.mc.McUser) */
	public void saveMcUser(McQueUsr mcUser)
    {
    	this.getHibernateTemplate().save(mcUser);
    }
	
	/** @see org.lamsfoundation.lams.tool.mc.dao.IMcUserDAO#updateMcUser(org.lamsfoundation.lams.tool.mc.McUser) */
    public void updateMcUser(McQueUsr mcUser)
    {
    	this.getHibernateTemplate().update(mcUser);
    }
    
    /** @see org.lamsfoundation.lams.tool.mc.dao.IMcUserDAO#removeMcUser(java.lang.Long) */
    public void removeMcUser(Long userId)
    {
        String query = "from McQueUsr as user where user.queUsrId =";
        StringBuffer sb = new StringBuffer(query);
        sb.append(userId.longValue());
        
        String queryString = sb.toString();
           
        this.getHibernateTemplate().delete(queryString);
      
    }
    
    /** @see org.lamsfoundation.lams.tool.mc.dao.IMcUserDAO#removeMcUser(org.lamsfoundation.lams.tool.mc.McUser) */
    public void removeMcUser(McQueUsr mcUser)
    {
        this.getHibernateTemplate().delete(mcUser);
    }
    
    /** @see org.lamsfoundation.lams.tool.mc.dao.IMcUserDAO#getNumberOfUsers((org.lamsfoundation.lams.tool.mc.McSession) */
    public int getNumberOfUsers(McSession mcSession)
    {
        return (getHibernateTemplate().findByNamedParam(COUNT_USERS_IN_SESSION,
	            "mcSession",
				mcSession)).size();
    }
    
    
}
