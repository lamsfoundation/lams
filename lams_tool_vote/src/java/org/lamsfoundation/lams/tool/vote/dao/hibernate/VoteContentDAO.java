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

import org.apache.log4j.Logger;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.lamsfoundation.lams.tool.vote.dao.IVoteContentDAO;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteSession;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author Ozgur Demirtas
 * <p>Hibernate implementation for database access to VoteContent for the voting tool.</p>
 */

public class VoteContentDAO extends HibernateDaoSupport implements IVoteContentDAO {
	static Logger logger = Logger.getLogger(VoteContentDAO.class.getName());
	
	private static final String FIND_VOTE_CONTENT = "from " + VoteContent.class.getName() + " as mc where content_id=?";
	
	private static final String LOAD_VOTE_BY_SESSION = "select mc from VoteContent mc left join fetch "
        + "mc.mcSessions session where session.mcSessionId=:sessionId";

	public VoteContent getMcContentByUID(Long uid)
	{
		return (VoteContent) this.getHibernateTemplate().get(VoteContent.class, uid);
	}
	
	public VoteContent findMcContentById(Long mcContentId)
	{
		String query = "from VoteContent as mc where mc.mcContentId = ?";
	    HibernateTemplate templ = this.getHibernateTemplate();
		List list = getSession().createQuery(query)
			.setLong(0,mcContentId.longValue())
			.list();
		
		if(list != null && list.size() > 0){
			VoteContent mc = (VoteContent) list.get(0);
			return mc;
		}
		return null;
	}
    	

	public VoteContent getMcContentBySession(final Long mcSessionId)
	{
		 return (VoteContent) getHibernateTemplate().execute(new HibernateCallback()
                {

                    public Object doInHibernate(Session session) throws HibernateException
                    {
                        return session.createQuery(LOAD_VOTE_BY_SESSION)
                                      .setLong("sessionId",
                                      		mcSessionId.longValue())
                                      .uniqueResult();
                    }
                });
	}
	
	
	public void saveMcContent(VoteContent mcContent)
    {
		this.getSession().setFlushMode(FlushMode.AUTO);
		logger.debug("before saveOrUpdate");
    	this.getHibernateTemplate().saveOrUpdate(mcContent);
    	logger.debug("after saveOrUpdate");
    }
    
	public void updateMcContent(VoteContent mcContent)
    {
		this.getSession().setFlushMode(FlushMode.AUTO);
    	this.getHibernateTemplate().update(mcContent);
    }

   
	public void removeMcById(Long mcContentId)
    {
		HibernateTemplate templ = this.getHibernateTemplate();
		if ( mcContentId != null) {
			List list = getSession().createQuery(FIND_VOTE_CONTENT)
				.setLong(0,mcContentId.longValue())
				.list();
			
			if(list != null && list.size() > 0){
				VoteContent mc = (VoteContent) list.get(0);
				this.getSession().setFlushMode(FlushMode.AUTO);
				templ.delete(mc);
				templ.flush();
			}
		}
    }
	
    public void removeMc(VoteContent mcContent)
    {
        this.getHibernateTemplate().delete(mcContent);
    }
   

    public void removeMcSessions(VoteContent mcContent)
    {
    	this.getHibernateTemplate().deleteAll(mcContent.getVoteSessions());
    }
    
    public void addMcSession(Long mcContentId, VoteSession mcSession)
    {
        /*
        VoteContent content = findMcContentById(mcContentId);
        mcSession.setMcContent(content);
        content.getMcSessions().add(mcSession);
        this.getHibernateTemplate().saveOrUpdate(mcSession);
        this.getHibernateTemplate().saveOrUpdate(content);
        */
    }
    
    public List findAll(Class objClass) {
		String query="from obj in class " + objClass.getName(); 
		return this.getHibernateTemplate().find(query);
	}
    
    public void flush()
    {
        this.getHibernateTemplate().flush();
    }
}
