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
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.lamsfoundation.lams.tool.vote.dao.IVoteSessionDAO;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueUsr;
import org.lamsfoundation.lams.tool.vote.pojos.VoteSession;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author ozgurd
 * <p>Hibernate implementation for database access to Vote sessions for the voting tool.</p>
 */

public class VoteSessionDAO extends HibernateDaoSupport implements IVoteSessionDAO {
	
	private static final String FIND_VOTE_SESSION_CONTENT = "from " + VoteSession.class.getName() + " as votes where vote_session_id=?";
	
    private static final String LOAD_VOTESESSION_BY_USER = "select votes from VoteSession votes left join fetch "
        + "votes.voteQueUsers user where user.queUsrId=:userId";
    
    private static final String GET_SESSIONS_FROM_CONTENT = "select votes.voteSessionId from VoteSession vote where vote.voteContent=:voteContent";
    
    private static final String COUNT_SESSION_COMPLETE = "from voteSession in class  where voteSession.sessionStatus='COMPLETE'";
    
    private static final String COUNT_SESSION_INCOMPLETE = "from voteSession in class  where voteSession.sessionStatus='INCOMPLETE'";
    
    private static final String GET_SESSIONNAMES_FROM_CONTENT 	= "select votes.session_name from VoteSession votes where votes.voteContent=:voteContent order by votes.voteSessionId";
    
    public VoteSession getVoteSessionByUID(Long uid)
	{
		 return (VoteSession) this.getHibernateTemplate().get(VoteSession.class, uid);
	}
	
    public VoteSession findVoteSessionById(Long voteSessionId)
    {
    	String query = "from VoteSession votes where votes.voteSessionId=?";
		
		HibernateTemplate templ = this.getHibernateTemplate();
		List list = getSession().createQuery(query)
		.setLong(0,voteSessionId.longValue())
		.list();
		
		if(list != null && list.size() > 0){
			VoteSession votes = (VoteSession) list.get(0);
			return votes;
		}
		return null;
	}
    
    public int countSessionComplete()
    {
    	HibernateTemplate templ = this.getHibernateTemplate();
		List list = getSession().createQuery(COUNT_SESSION_COMPLETE)
		.list();
		
		if(list != null && list.size() > 0){
			return list.size();
		}
		else return 0;
	}

    public int countSessionIncomplete()
    {
    	HibernateTemplate templ = this.getHibernateTemplate();
		List list = getSession().createQuery(COUNT_SESSION_INCOMPLETE)
		.list();
		
		if(list != null && list.size() > 0){
			return list.size();
		}
		else return 0;
	}
    
    
    public void saveVoteSession(VoteSession voteSession)
    {
    	this.getHibernateTemplate().save(voteSession);
    }
    
	
    public void updateVoteSession(VoteSession voteSession)
    {
    	this.getSession().setFlushMode(FlushMode.AUTO);
    	this.getHibernateTemplate().update(voteSession);
    }

   
    public void removeVoteSessionByUID(Long uid)
    {
        VoteSession votes = (VoteSession)getHibernateTemplate().get(VoteSession.class, uid);
    	this.getHibernateTemplate().delete(votes);
    }
    
    public void removeVoteSessionById(Long voteSessionId)
    {
        String query = "from VoteSession as votes where votes.voteSessionId =";
        
		HibernateTemplate templ = this.getHibernateTemplate();
		if ( voteSessionId != null) {
			List list = getSession().createQuery(FIND_VOTE_SESSION_CONTENT)
				.setLong(0,voteSessionId.longValue())
				.list();
			
			if(list != null && list.size() > 0){
				VoteSession vote = (VoteSession) list.get(0);
				this.getSession().setFlushMode(FlushMode.AUTO);
				templ.delete(vote);
				templ.flush();
			}
		}
        
        
    }

    public void removeVoteSession(VoteSession voteSession)
    {
		this.getSession().setFlushMode(FlushMode.AUTO);
        this.getHibernateTemplate().delete(voteSession);
    }

    

    public VoteSession getVoteSessionByUser(final Long userId)
	{
		 return (VoteSession) getHibernateTemplate().execute(new HibernateCallback()
                {

                    public Object doInHibernate(Session session) throws HibernateException
                    {
                        return session.createQuery(LOAD_VOTESESSION_BY_USER)
                                      .setLong("userId",
                                      		userId.longValue())
                                      .uniqueResult();
                    }
                });
	}
	
	 
    public void removeVoteUsers(VoteSession voteSession)
    {
    	this.getHibernateTemplate().deleteAll(voteSession.getVoteQueUsers());
    }
	
    
    public void addVoteUsers(Long voteSessionId, VoteQueUsr user)
	{
    	VoteSession session = findVoteSessionById(voteSessionId);
	    user.setVoteSession(session);
	    session.getVoteQueUsers().add(user);
	    this.getHibernateTemplate().saveOrUpdate(user);
	    this.getHibernateTemplate().saveOrUpdate(session);
	    	    
	}
	

	public List getSessionsFromContent(VoteContent voteContent)
	{
	    return (getHibernateTemplate().findByNamedParam(GET_SESSIONS_FROM_CONTENT,
	            "voteContent",
	            voteContent));
	}
	
	public List getSessionNamesFromContent(VoteContent voteContent)
	{
	    return (getHibernateTemplate().findByNamedParam(GET_SESSIONNAMES_FROM_CONTENT,
	            "voteContent",
	            voteContent));
	}
}
