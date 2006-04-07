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
package org.lamsfoundation.lams.tool.vote.dao.hibernate;

import java.util.List;

import org.hibernate.FlushMode;
import org.lamsfoundation.lams.tool.vote.dao.IVoteUserDAO;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueUsr;
import org.lamsfoundation.lams.tool.vote.pojos.VoteSession;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author Ozgur Demirtas
 * <p>Hibernate implementation for database access to Voting users (learners) for the voting tool.</p>
 */
public class VoteUserDAO extends HibernateDaoSupport implements IVoteUserDAO {
    
	private static final String FIND_VOTE_USR_CONTENT = "from " + VoteQueUsr.class.getName() + " as mcu where que_usr_id=?";
	
	private static final String COUNT_USERS_IN_SESSION = "select mu.queUsrId from VoteQueUsr mu where mu.mcSession= :mcSession";
	
	private static final String COUNT_USERS = "select mu.queUsrId from VoteQueUsr";
	
	private static final String LOAD_USER_FOR_SESSION = "from mcQueUsr in class VoteQueUsr where  mcQueUsr.mcSessionId= :mcSessionId";
   
    
   public VoteQueUsr getMcUserByUID(Long uid)
	{
		 return (VoteQueUsr) this.getHibernateTemplate()
         .get(VoteQueUsr.class, uid);
	}
	
	
	public VoteQueUsr findMcUserById(Long userId)
	{
		String query = "from VoteQueUsr user where user.queUsrId=?";
	
		HibernateTemplate templ = this.getHibernateTemplate();
		List list = getSession().createQuery(query)
		.setLong(0,userId.longValue())
		.list();
		
		if(list != null && list.size() > 0){
			VoteQueUsr mcu = (VoteQueUsr) list.get(0);
			return mcu;
		}
		return null;
	}
	
	
	public List getMcUserBySessionOnly(final VoteSession mcSession)
    {
        HibernateTemplate templ = this.getHibernateTemplate();
        List list = getSession().createQuery(LOAD_USER_FOR_SESSION)
		.setLong("mcSessionId", mcSession.getUid().longValue())				
		.list();
		return list;
    }
	
	public VoteQueUsr getMcUserBySession(final Long queUsrId, final Long mcSessionId)
	{
		
		String strGetUser = "from mcQueUsr in class VoteQueUsr where mcQueUsr.queUsrId=:queUsrId and mcQueUsr.mcSessionId=:mcSessionId";
        HibernateTemplate templ = this.getHibernateTemplate();
		List list = getSession().createQuery(strGetUser)
			.setLong("queUsrId", queUsrId.longValue())
			.setLong("mcSessionId", mcSessionId.longValue())				
			.list();
		
		if(list != null && list.size() > 0){
			VoteQueUsr usr = (VoteQueUsr) list.get(0);
			return usr;
		}
		return null;
	}

	
	public void saveMcUser(VoteQueUsr mcUser)
    {
    	this.getHibernateTemplate().save(mcUser);
    }
	

    public void updateMcUser(VoteQueUsr mcUser)
    {
    	this.getHibernateTemplate().update(mcUser);
    }
    

    public void removeMcUserById(Long userId)
    {
    	HibernateTemplate templ = this.getHibernateTemplate();
		if ( userId != null) {
			List list = getSession().createQuery(FIND_VOTE_USR_CONTENT)
				.setLong(0,userId.longValue())
				.list();
			
			if(list != null && list.size() > 0){
				VoteQueUsr mcu = (VoteQueUsr) list.get(0);
				this.getSession().setFlushMode(FlushMode.AUTO);
				templ.delete(mcu);
				templ.flush();
			}
		}
      
    }
    

    public void removeMcUser(VoteQueUsr mcUser)
    {
		this.getSession().setFlushMode(FlushMode.AUTO);
        this.getHibernateTemplate().delete(mcUser);
    }
    

    public int getNumberOfUsers(VoteSession mcSession)
    {
        return (getHibernateTemplate().findByNamedParam(COUNT_USERS_IN_SESSION,
	            "mcSession",
				mcSession)).size();
    }  
    
    
    public int getTotalNumberOfUsers() {
		String query="from obj in class VoteQueUsr"; 
		return this.getHibernateTemplate().find(query).size();
	}
    
}
