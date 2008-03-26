/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0 
 * as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $$Id$$ */
package org.lams.lams.tool.wiki.dao.hibernate;

import java.util.List;
import java.lang.Long;

import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.lams.lams.tool.wiki.WikiContent;
import org.lams.lams.tool.wiki.WikiSession;
import org.lams.lams.tool.wiki.dao.IWikiContentDAO;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author mtruong
 * <p>Hibernate implementation for database access to Wiki content for the wiki tool.</p>
 */

public class WikiContentDAO extends HibernateDaoSupport implements IWikiContentDAO {
	
	private static final String FIND_NB_CONTENT = "from " + WikiContent.class.getName() + " as wiki where wiki.wikiContentId=?";
	
	
	private static final String LOAD_NB_BY_SESSION = "select wiki from WikiContent wiki left join fetch "
        + "wiki.wikiSessions session where session.wikiSessionId=:sessionId";

	
	/** @see org.lams.lams.tool.wiki.dao.IWikiContentDAO#getWikiContentByUID(java.lang.Long) */
	public WikiContent getWikiContentByUID(Long uid)
	{
		 return (WikiContent) this.getHibernateTemplate()
         .get(WikiContent.class, uid);
	}
	
	/** @see org.lams.lams.tool.wiki.dao.IWikiContentDAO#findWikiContentById(java.lang.Long) */
	public WikiContent findWikiContentById(Long wikiContentId)
	{
	    String query = "from WikiContent as wiki where wiki.wikiContentId = ?";
		List content = getHibernateTemplate().find(query,wikiContentId);
			
		if(content!=null && content.size() == 0)
		{			
			return null;
		}
		else
		{
			return (WikiContent)content.get(0);
		}
	
	}
    	
	/** @see org.lams.lams.tool.wiki.dao.IWikiContentDAO#getWikiContentBySession(java.lang.Long) */
	public WikiContent getWikiContentBySession(final Long wikiSessionId)
	{
		 return (WikiContent) getHibernateTemplate().execute(new HibernateCallback()
                {

                    public Object doInHibernate(Session session) throws HibernateException
                    {
                        return session.createQuery(LOAD_NB_BY_SESSION)
                                      .setLong("sessionId",
                                      		wikiSessionId.longValue())
                                      .uniqueResult();
                    }
                });
	}
	
	
	/** @see org.lams.lams.tool.wiki.dao.IWikiContentDAO#saveWikiContent(org.lams.lams.tool.wiki.WikiContent) */
	public void saveWikiContent(WikiContent wikiContent)
    {
    	this.getHibernateTemplate().save(wikiContent);
    }
    
	/** @see org.lams.lams.tool.wiki.dao.IWikiContentDAO#updateWikiContent(org.lams.lams.tool.wiki.WikiContent) */
	public void updateWikiContent(WikiContent wikiContent)
    {
    	this.getHibernateTemplate().update(wikiContent);
    }

   
	/** @see org.lams.lams.tool.wiki.dao.IWikiContentDAO#removeWiki(java.lang.Long)*/
	public void removeWiki(Long wikiContentId)
    {       
       	if ( wikiContentId != null) {
			//String query = "from org.lams.lams.tool.wiki.WikiContent as wiki where wiki.wikiContentId=?";
			List list = getSession().createQuery(FIND_NB_CONTENT)
				.setLong(0,wikiContentId.longValue())
				.list();
			
			if(list != null && list.size() > 0){
				WikiContent wiki = (WikiContent) list.get(0);
				this.getSession().setFlushMode(FlushMode.AUTO);
				this.getHibernateTemplate().delete(wiki);
				this.getHibernateTemplate().flush();
			}
		}
    }
    
	/** @see org.lams.lams.tool.wiki.dao.IWikiContentDAO#removeWiki(org.lams.lams.tool.wiki.WikiContent)*/
    public void removeWiki(WikiContent wikiContent)
    {
    	removeWiki(wikiContent.getWikiContentId());
    }
   
    /** @see org.lams.lams.tool.wiki.dao.IWikiContentDAO#removeWikiSessions(org.lams.lams.tool.wiki.WikiContent)*/
    public void removeWikiSessions(WikiContent wikiContent)
    {
    	this.getHibernateTemplate().deleteAll(wikiContent.getWikiSessions());
    }
    
    /** @see org.lams.lams.tool.wiki.dao.IWikiContentDAO#addWikiSession(java.lang.Long, org.lams.lams.tool.wiki.WikiSession) */
    public void addWikiSession(Long wikiContentId, WikiSession wikiSession)
    {
        WikiContent content = findWikiContentById(wikiContentId);
        wikiSession.setWikiContent(content);
        content.getWikiSessions().add(wikiSession);
        this.getHibernateTemplate().saveOrUpdate(wikiSession);
        this.getHibernateTemplate().saveOrUpdate(content);        
    }
  
}
