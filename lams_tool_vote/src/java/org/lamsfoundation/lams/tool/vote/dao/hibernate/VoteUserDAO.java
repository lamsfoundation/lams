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
    
	private static final String FIND_VOTE_USR_CONTENT = "from " + VoteQueUsr.class.getName() + " as voteu where que_usr_id=?";
	
	private static final String COUNT_USERS_IN_SESSION = "select voteu.queUsrId from VoteQueUsr voteu where voteu.voteSession= :voteSession";
	
	private static final String COUNT_USERS = "select voteu.queUsrId from VoteQueUsr";
	
	private static final String LOAD_USER_FOR_SESSION = "from voteQueUsr in class VoteQueUsr where  voteQueUsr.voteSessionId= :voteSessionId";
   
    
   public VoteQueUsr getVoteUserByUID(Long uid)
	{
		 return (VoteQueUsr) this.getHibernateTemplate()
         .get(VoteQueUsr.class, uid);
	}
	
	
	public VoteQueUsr findVoteUserById(Long userId)
	{
		String query = "from VoteQueUsr user where user.queUsrId=?";
	
		HibernateTemplate templ = this.getHibernateTemplate();
		List list = getSession().createQuery(query)
		.setLong(0,userId.longValue())
		.list();
		
		if(list != null && list.size() > 0){
			VoteQueUsr voteu = (VoteQueUsr) list.get(0);
			return voteu;
		}
		return null;
	}
	
	
	public List getVoteUserBySessionOnly(final VoteSession voteSession)
    {
        HibernateTemplate templ = this.getHibernateTemplate();
        List list = getSession().createQuery(LOAD_USER_FOR_SESSION)
		.setLong("voteSessionId", voteSession.getUid().longValue())				
		.list();
		return list;
    }
	
	public VoteQueUsr getVoteUserBySession(final Long queUsrId, final Long voteSessionId)
	{
		
		String strGetUser = "from voteQueUsr in class VoteQueUsr where voteQueUsr.queUsrId=:queUsrId and voteQueUsr.voteSessionId=:voteSessionId";
        HibernateTemplate templ = this.getHibernateTemplate();
		List list = getSession().createQuery(strGetUser)
			.setLong("queUsrId", queUsrId.longValue())
			.setLong("voteSessionId", voteSessionId.longValue())				
			.list();
		
		if(list != null && list.size() > 0){
			VoteQueUsr usr = (VoteQueUsr) list.get(0);
			return usr;
		}
		return null;
	}

	
	public void saveVoteUser(VoteQueUsr voteUser)
    {
    	this.getHibernateTemplate().save(voteUser);
    }
	

    public void updateVoteUser(VoteQueUsr voteUser)
    {
    	this.getHibernateTemplate().update(voteUser);
    }
    

    public void removeVoteUserById(Long userId)
    {
    	HibernateTemplate templ = this.getHibernateTemplate();
		if ( userId != null) {
			List list = getSession().createQuery(FIND_VOTE_USR_CONTENT)
				.setLong(0,userId.longValue())
				.list();
			
			if(list != null && list.size() > 0){
				VoteQueUsr voteu = (VoteQueUsr) list.get(0);
				this.getSession().setFlushMode(FlushMode.AUTO);
				templ.delete(voteu);
				templ.flush();
			}
		}
      
    }
    

    public void removeVoteUser(VoteQueUsr voteUser)
    {
		this.getSession().setFlushMode(FlushMode.AUTO);
        this.getHibernateTemplate().delete(voteUser);
    }
    

    public int getNumberOfUsers(VoteSession voteSession)
    {
        return (getHibernateTemplate().findByNamedParam(COUNT_USERS_IN_SESSION,
	            "voteSession",
				voteSession)).size();
    }  
    
    
    public int getTotalNumberOfUsers() {
		String query="from obj in class VoteQueUsr"; 
		return this.getHibernateTemplate().find(query).size();
	}
    
}
