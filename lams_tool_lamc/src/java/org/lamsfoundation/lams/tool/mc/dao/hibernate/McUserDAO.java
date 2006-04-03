/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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
/* $$Id$$ */
package org.lamsfoundation.lams.tool.mc.dao.hibernate;

import java.util.List;

import org.hibernate.FlushMode;
import org.lamsfoundation.lams.tool.mc.dao.IMcUserDAO;
import org.lamsfoundation.lams.tool.mc.pojos.McQueUsr;
import org.lamsfoundation.lams.tool.mc.pojos.McSession;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author Ozgur Demirtas
 * <p>Hibernate implementation for database access to Mc users (learners) for the mc tool.</p>
 */
public class McUserDAO extends HibernateDaoSupport implements IMcUserDAO {
    
	private static final String FIND_MC_USR_CONTENT = "from " + McQueUsr.class.getName() + " as mcu where que_usr_id=?";
	
	private static final String COUNT_USERS_IN_SESSION = "select mu.queUsrId from McQueUsr mu where mu.mcSession= :mcSession";
	
	private static final String COUNT_USERS = "select mu.queUsrId from McQueUsr";
	
	private static final String LOAD_USER_FOR_SESSION = "from mcQueUsr in class McQueUsr where  mcQueUsr.mcSessionId= :mcSessionId";
   
    
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
	
	
	public List getMcUserBySessionOnly(final McSession mcSession)
    {
        HibernateTemplate templ = this.getHibernateTemplate();
        List list = getSession().createQuery(LOAD_USER_FOR_SESSION)
		.setLong("mcSessionId", mcSession.getUid().longValue())				
		.list();
		return list;
    }
	
	public McQueUsr getMcUserBySession(final Long queUsrId, final Long mcSessionId)
	{
		
		String strGetUser = "from mcQueUsr in class McQueUsr where mcQueUsr.queUsrId=:queUsrId and mcQueUsr.mcSessionId=:mcSessionId";
        HibernateTemplate templ = this.getHibernateTemplate();
		List list = getSession().createQuery(strGetUser)
			.setLong("queUsrId", queUsrId.longValue())
			.setLong("mcSessionId", mcSessionId.longValue())				
			.list();
		
		if(list != null && list.size() > 0){
			McQueUsr usr = (McQueUsr) list.get(0);
			return usr;
		}
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
    
    
    public int getTotalNumberOfUsers() {
		String query="from obj in class McQueUsr"; 
		return this.getHibernateTemplate().find(query).size();
	}
    
}
