/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
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
 * ***********************************************************************/
/*
 * @author ozgurd
 */
package org.lamsfoundation.lams.tool.mc.dao.hibernate;

import java.util.List;

import org.hibernate.FlushMode;
import org.lamsfoundation.lams.tool.mc.McQueUsr;
import org.lamsfoundation.lams.tool.mc.McSession;
import org.lamsfoundation.lams.tool.mc.dao.IMcUserDAO;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author ozgurd
 * <p>Hibernate implementation for database access to Mc users (learners) for the mc tool.</p>
 */
public class McUserDAO extends HibernateDaoSupport implements IMcUserDAO {
    
	private static final String FIND_MC_USR_CONTENT = "from " + McQueUsr.class.getName() + " as mcu where que_usr_id=?";
	
	private static final String COUNT_USERS_IN_SESSION = "select mu.queUsrId from McQueUsr mu where mu.mcSession= :mcSession";
   
    
   public McQueUsr getMcUserByUID(Long uid)
	{
		 return (McQueUsr) this.getHibernateTemplate()
         .get(McQueUsr.class, uid);
	}
	
	
	public McQueUsr findMcUserById(Long userId)
	{
		String query = "from McQueUsr user where user.queUsrId=?";
	
		HibernateTemplate templ = this.getHibernateTemplate();
		List list = getSession().createQuery(query)
		.setLong(0,userId.longValue())
		.list();
		
		if(list != null && list.size() > 0){
			McQueUsr mcu = (McQueUsr) list.get(0);
			return mcu;
		}
		return null;
	}
	
	
	public McQueUsr getMcUserBySession(Long userId, Long sessionId)
	{
		/*
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
		*/
		return null;
	}

	
	public void saveMcUser(McQueUsr mcUser)
    {
    	this.getHibernateTemplate().save(mcUser);
    }
	

    public void updateMcUser(McQueUsr mcUser)
    {
    	this.getHibernateTemplate().update(mcUser);
    }
    

    public void removeMcUserById(Long userId)
    {
    	HibernateTemplate templ = this.getHibernateTemplate();
		if ( userId != null) {
			List list = getSession().createQuery(FIND_MC_USR_CONTENT)
				.setLong(0,userId.longValue())
				.list();
			
			if(list != null && list.size() > 0){
				McQueUsr mcu = (McQueUsr) list.get(0);
				this.getSession().setFlushMode(FlushMode.AUTO);
				templ.delete(mcu);
				templ.flush();
			}
		}
      
    }
    

    public void removeMcUser(McQueUsr mcUser)
    {
		this.getSession().setFlushMode(FlushMode.AUTO);
        this.getHibernateTemplate().delete(mcUser);
    }
    

    public int getNumberOfUsers(McSession mcSession)
    {
        return (getHibernateTemplate().findByNamedParam(COUNT_USERS_IN_SESSION,
	            "mcSession",
				mcSession)).size();
    }  
    
}
