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

import org.apache.log4j.Logger;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.lamsfoundation.lams.tool.mc.dao.IMcContentDAO;
import org.lamsfoundation.lams.tool.mc.pojos.McContent;
import org.lamsfoundation.lams.tool.mc.pojos.McSession;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author Ozgur Demirtas
 * <p>Hibernate implementation for database access to McContent for the mc tool.</p>
 */

public class McContentDAO extends HibernateDaoSupport implements IMcContentDAO {
	static Logger logger = Logger.getLogger(McContentDAO.class.getName());
	
	private static final String FIND_MC_CONTENT = "from " + McContent.class.getName() + " as mc where content_id=?";
	
	private static final String LOAD_MC_BY_SESSION = "select mc from McContent mc left join fetch "
        + "mc.mcSessions session where session.mcSessionId=:sessionId";

	public McContent getMcContentByUID(Long uid)
	{
		return (McContent) this.getHibernateTemplate().get(McContent.class, uid);
	}
	
	public McContent findMcContentById(Long mcContentId)
	{
		String query = "from McContent as mc where mc.mcContentId = ?";
	    HibernateTemplate templ = this.getHibernateTemplate();
		List list = getSession().createQuery(query)
			.setLong(0,mcContentId.longValue())
			.list();
		
		if(list != null && list.size() > 0){
			McContent mc = (McContent) list.get(0);
			return mc;
		}
		return null;
	}
    	

	public McContent getMcContentBySession(final Long mcSessionId)
	{
		 return (McContent) getHibernateTemplate().execute(new HibernateCallback()
                {

                    public Object doInHibernate(Session session) throws HibernateException
                    {
                        return session.createQuery(LOAD_MC_BY_SESSION)
                                      .setLong("sessionId",
                                      		mcSessionId.longValue())
                                      .uniqueResult();
                    }
                });
	}
	
	
	public void saveMcContent(McContent mcContent)
    {
		this.getSession().setFlushMode(FlushMode.AUTO);
		logger.debug("before saveOrUpdate");
    	this.getHibernateTemplate().saveOrUpdate(mcContent);
    	logger.debug("after saveOrUpdate");
    }
    
	public void updateMcContent(McContent mcContent)
    {
		this.getSession().setFlushMode(FlushMode.AUTO);
    	this.getHibernateTemplate().update(mcContent);
    }

   
	public void removeMcById(Long mcContentId)
    {
		HibernateTemplate templ = this.getHibernateTemplate();
		if ( mcContentId != null) {
			List list = getSession().createQuery(FIND_MC_CONTENT)
				.setLong(0,mcContentId.longValue())
				.list();
			
			if(list != null && list.size() > 0){
				McContent mc = (McContent) list.get(0);
				this.getSession().setFlushMode(FlushMode.AUTO);
				templ.delete(mc);
				templ.flush();
			}
		}
    }
	
    public void removeMc(McContent mcContent)
    {
        this.getHibernateTemplate().delete(mcContent);
    }
   

    public void removeMcSessions(McContent mcContent)
    {
    	this.getHibernateTemplate().deleteAll(mcContent.getMcSessions());
    }
    
    public void addMcSession(Long mcContentId, McSession mcSession)
    {
        McContent content = findMcContentById(mcContentId);
        mcSession.setMcContent(content);
        content.getMcSessions().add(mcSession);
        this.getHibernateTemplate().saveOrUpdate(mcSession);
        this.getHibernateTemplate().saveOrUpdate(content);
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
