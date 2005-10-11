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

package org.lamsfoundation.lams.tool.mc.dao.hibernate;

import java.util.List;

import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.lamsfoundation.lams.tool.mc.McContent;
import org.lamsfoundation.lams.tool.mc.McQueUsr;
import org.lamsfoundation.lams.tool.mc.McSession;
import org.lamsfoundation.lams.tool.mc.dao.IMcSessionDAO;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author ozgurd
 * <p>Hibernate implementation for database access to Mc sessions for the mc tool.</p>
 */

public class McSessionDAO extends HibernateDaoSupport implements IMcSessionDAO {
	
	private static final String FIND_MC_SESSION_CONTENT = "from " + McSession.class.getName() + " as mcs where mc_session_id=?";
	
    private static final String LOAD_MCSESSION_BY_USER = "select ms from McSession ms left join fetch "
        + "ms.mcQueUsers user where user.queUsrId=:userId";
    
    private static final String GET_SESSIONS_FROM_CONTENT = "select ms.mcSessionId from McSession ms where ms.mcContent= :mcContent";
    
    /** @see org.lamsfoundation.lams.tool.mc.dao.IMcSessionDAO#getMcSessionByUID(java.lang.Long) */
    public McSession getMcSessionByUID(Long uid)
	{
		 return (McSession) this.getHibernateTemplate()
         .get(McSession.class, uid);
	}
	
    /** @see org.lamsfoundation.lams.tool.mc.dao.IMcSessionDAO#findMcSessionById(java.lang.Long) */
    public McSession findMcSessionById(Long mcSessionId)
    {
    	String query = "from McSession mcs where mcs.mcSessionId=?";
    	
		return (McSession) getSession().createQuery(query)
		.setLong(0,mcSessionId.longValue())
		.uniqueResult();
	}
    
    
    /** @see org.lamsfoundation.lams.tool.mc.dao.IMcSessionDAO#saveMcSession(org.lamsfoundation.lams.tool.mc.McSession) */
    public void saveMcSession(McSession mcSession)
    {
    	this.getHibernateTemplate().save(mcSession);
    }
    
	
    /** @see org.lamsfoundation.lams.tool.mc.dao.IMcSessionDAO#updateMcSession(org.lamsfoundation.lams.tool.mc.McSession) */
    public void updateMcSession(McSession mcSession)
    {
    	this.getHibernateTemplate().update(mcSession);
    }

   
    /** @see org.lamsfoundation.lams.tool.mc.dao.IMcSessionDAO#removeMcSessionByUID(java.lang.Long) */
    public void removeMcSessionByUID(Long uid)
    {
        McSession ms = (McSession)getHibernateTemplate().get(McSession.class, uid);
    	this.getHibernateTemplate().delete(ms);
    }
    
    /** @see org.lamsfoundation.lams.tool.mc.dao.IMcSessionDAO#removeMcSession(java.lang.Long) */
    public void removeMcSessionById(Long mcSessionId)
    {
        String query = "from McSession as mcs where mcs.mcSessionId =";
        
		HibernateTemplate templ = this.getHibernateTemplate();
		if ( mcSessionId != null) {
			List list = getSession().createQuery(FIND_MC_SESSION_CONTENT)
				.setLong(0,mcSessionId.longValue())
				.list();
			
			if(list != null && list.size() > 0){
				McSession mcs = (McSession) list.get(0);
				this.getSession().setFlushMode(FlushMode.AUTO);
				templ.delete(mcs);
				templ.flush();
			}
		}
        
        
    }
    /** @see org.lamsfoundation.lams.tool.mc.dao.IMcSessionDAO#removeMcSession(org.lamsfoundation.lams.tool.mc.McSession) */
    public void removeMcSession(McSession mcSession)
    {
        this.getHibernateTemplate().delete(mcSession);
    }

    
    /** @see org.lamsfoundation.lams.tool.mc.dao.IMcSessionDAO#getMcSessionByUser(java.lang.Long) */
    public McSession getMcSessionByUser(final Long userId)
	{
		 return (McSession) getHibernateTemplate().execute(new HibernateCallback()
                {

                    public Object doInHibernate(Session session) throws HibernateException
                    {
                        return session.createQuery(LOAD_MCSESSION_BY_USER)
                                      .setLong("userId",
                                      		userId.longValue())
                                      .uniqueResult();
                    }
                });
	}
	
	 
    /** @see org.lamsfoundation.lams.tool.mc.dao.IMcSessionDAO#removeMcUsers(org.lamsfoundation.lams.tool.mc.McSession) */
    public void removeMcUsers(McSession mcSession)
    {
    	this.getHibernateTemplate().deleteAll(mcSession.getMcQueUsers());
    }
	
    
    /** @see org.lamsfoundation.lams.tool.mc.dao.IMcSessionDAO#addMcUsers(java.lang.Long, org.lamsfoundation.lams.tool.mc.McSession) */
    public void addMcUsers(Long mcSessionId, McQueUsr user)
	{
    	McSession session = findMcSessionById(mcSessionId);
	    user.setMcSession(session);
	    session.getMcQueUsers().add(user);
	    this.getHibernateTemplate().saveOrUpdate(user);
	    this.getHibernateTemplate().saveOrUpdate(session);
	    	    
	}
	
    /** @see org.lamsfoundation.lams.tool.mc.dao.IMcSessionDAO#getSessionsFromContent(org.lamsfoundation.lams.tool.mc.McSession) */
	public List getSessionsFromContent(McContent mcContent)
	{
	    return (getHibernateTemplate().findByNamedParam(GET_SESSIONS_FROM_CONTENT,
	            "mcContent",
				mcContent));
	}
    	
}
