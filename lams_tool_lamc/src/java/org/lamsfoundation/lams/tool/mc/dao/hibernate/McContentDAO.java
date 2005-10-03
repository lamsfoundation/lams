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

package org.lamsfoundation.lams.tool.mc.dao.hibernate;

import java.util.List;
import java.lang.Long;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.lamsfoundation.lams.tool.mc.McContent;
import org.lamsfoundation.lams.tool.mc.McSession;
import org.lamsfoundation.lams.tool.mc.dao.IMcContentDAO;
import org.springframework.orm.hibernate.HibernateCallback;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;

/**
 * @author ozgur
 * <p>Hibernate implementation for database access to Mc content for the mc tool.</p>
 */

public class McContentDAO extends HibernateDaoSupport implements IMcContentDAO {
	
	
	private static final String LOAD_NB_BY_SESSION = "select mc from McContent mc left join fetch "
        + "mc.mcSessions session where session.mcSessionId=:sessionId";

	

	/** @see org.lamsfoundation.lams.tool.mc.dao.IMcContentDAO#getMcContentByUID(java.lang.Long) */
	public McContent getMcContentByUID(Long uid)
	{
		 return (McContent) this.getHibernateTemplate()
         .get(McContent.class, uid);
	}
	
	/** @see org.lamsfoundation.lams.tool.mc.dao.IMcContentDAO#findMcContentById(java.lang.Long) */
	public McContent findMcContentById(Long mcContentId)
	{
	    String query = "from McContent as mc where mc.mcContentId = ?";
		List content = getHibernateTemplate().find(query,mcContentId);
			
		if(content!=null && content.size() == 0)
		{			
			return null;
		}
		else
		{
			return (McContent)content.get(0);
		}
	
	}
    	
	/** @see org.lamsfoundation.lams.tool.mc.dao.IMcContentDAO#getMcContentBySession(java.lang.Long) */
	public McContent getMcContentBySession(final Long mcSessionId)
	{
		 return (McContent) getHibernateTemplate().execute(new HibernateCallback()
                {

                    public Object doInHibernate(Session session) throws HibernateException
                    {
                        return session.createQuery(LOAD_NB_BY_SESSION)
                                      .setLong("sessionId",
                                      		mcSessionId.longValue())
                                      .uniqueResult();
                    }
                });
	}
	
	
	/** @see org.lamsfoundation.lams.tool.mc.dao.IMcContentDAO#saveMcContent(org.lamsfoundation.lams.tool.mc.McContent) */
	public void saveMcContent(McContent mcContent)
    {
    	this.getHibernateTemplate().save(mcContent);
    }
    
	/** @see org.lamsfoundation.lams.tool.mc.dao.IMcContentDAO#updateMcContent(org.lamsfoundation.lams.tool.mc.McContent) */
	public void updateMcContent(McContent mcContent)
    {
    	this.getHibernateTemplate().update(mcContent);
    }

   
	/** @see org.lamsfoundation.lams.tool.mc.dao.IMcContentDAO#removeMc(java.lang.Long)*/
	public void removeMc(Long mcContentId)
    {
       
       String query = "from McContent as mc where mc.mcContentId=";
       StringBuffer sb = new StringBuffer(query);
       sb.append(mcContentId.longValue());
       String queryString = sb.toString();
          
       this.getHibernateTemplate().delete(queryString);
    }
    
	/** @see org.lamsfoundation.lams.tool.mc.dao.IMcContentDAO#removeMc(org.lamsfoundation.lams.tool.mc.McContent)*/
    public void removeMc(McContent mcContent)
    {
        this.getHibernateTemplate().delete(mcContent);
    }
   
    /** @see org.lamsfoundation.lams.tool.mc.dao.IMcContentDAO#removeMcSessions(org.lamsfoundation.lams.tool.mc.McContent)*/
    public void removeMcSessions(McContent mcContent)
    {
    	this.getHibernateTemplate().deleteAll(mcContent.getMcSessions());
    }
    
    /** @see org.lamsfoundation.lams.tool.mc.dao.IMcContentDAO#addMcSession(java.lang.Long, org.lamsfoundation.lams.tool.mc.McSession) */
    public void addMcSession(Long mcContentId, McSession mcSession)
    {
        McContent content = findMcContentById(mcContentId);
        mcSession.setMcContent(content);
        content.getMcSessions().add(mcSession);
        this.getHibernateTemplate().saveOrUpdate(mcSession);
        this.getHibernateTemplate().saveOrUpdate(content);
        
    }
  
}
