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

import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.lams.lams.tool.wiki.WikiContent;
import org.lams.lams.tool.wiki.WikiSession;
import org.lams.lams.tool.wiki.WikiUser;
import org.lams.lams.tool.wiki.dao.IWikiSessionDAO;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author mtruong
 * <p>Hibernate implementation for database access to Wiki sessions for the wiki tool.</p>
 */

public class WikiSessionDAO extends HibernateDaoSupport implements IWikiSessionDAO {
	
	private static final String FIND_NB_SESSION = "from " + WikiSession.class.getName() + " as wiki where wiki.wikiSessionId=?";
	
    private static final String LOAD_NBSESSION_BY_USER = "select ns from WikiSession ns left join fetch "
        + "ns.wikiUsers user where user.userId=:userId";
    
    private static final String GET_SESSIONS_FROM_CONTENT = "select ns.wikiSessionId from WikiSession ns where ns.wikiContent= :wikiContent";
  
	
    /** @see org.lams.lams.tool.wiki.dao.IWikiSessionDAO#findWikiSessionById(java.lang.Long) */
    public WikiSession findWikiSessionById(Long wikiSessionId)
	{
	    String query = "from WikiSession wikiS where wikiS.wikiSessionId=?";
	    List session = getHibernateTemplate().find(query,wikiSessionId);
		
		if(session!=null && session.size() == 0)
		{			
			return null;
		}
		else
		{
			return (WikiSession)session.get(0);
		}
	}
	
    /** @see org.lams.lams.tool.wiki.dao.IWikiSessionDAO#saveWikiSession(org.lams.lams.tool.wiki.WikiSession) */
    public void saveWikiSession(WikiSession wikiSession)
    {
    	this.getHibernateTemplate().save(wikiSession);
    }
    
	
    /** @see org.lams.lams.tool.wiki.dao.IWikiSessionDAO#updateWikiSession(org.lams.lams.tool.wiki.WikiSession) */
    public void updateWikiSession(WikiSession wikiSession)
    {
    	this.getHibernateTemplate().update(wikiSession);
    }

 
    /** @see org.lams.lams.tool.wiki.dao.IWikiSessionDAO#removeWikiSession(java.lang.Long) */
    public void removeWikiSession(Long wikiSessionId)
    {
       
    	HibernateTemplate templ = this.getHibernateTemplate();
		if ( wikiSessionId != null) {
			//String query = "from org.lams.lams.tool.wiki.WikiContent as wiki where wiki.wikiContentId=?";
			List list = getSession().createQuery(FIND_NB_SESSION)
				.setLong(0,wikiSessionId.longValue())
				.list();
			
			if(list != null && list.size() > 0){
				WikiSession wiki = (WikiSession) list.get(0);
				this.getSession().setFlushMode(FlushMode.AUTO);
				templ.delete(wiki);
				templ.flush();
			}
		}
      
    }
    
    /** @see org.lams.lams.tool.wiki.dao.IWikiSessionDAO#removeWikiSession(org.lams.lams.tool.wiki.WikiSession) */
    public void removeWikiSession(WikiSession wikiSession)
    {
    	removeWikiSession(wikiSession.getWikiSessionId());
        //this.getHibernateTemplate().delete(wikiSession);
    }

    
    /** @see org.lams.lams.tool.wiki.dao.IWikiSessionDAO#getWikiSessionByUser(java.lang.Long) */
    public WikiSession getWikiSessionByUser(final Long userId)
	{
		 return (WikiSession) getHibernateTemplate().execute(new HibernateCallback()
                {

                    public Object doInHibernate(Session session) throws HibernateException
                    {
                        return session.createQuery(LOAD_NBSESSION_BY_USER)
                                      .setLong("userId",
                                      		userId.longValue())
                                      .uniqueResult();
                    }
                });
	}
	
	 
    /** @see org.lams.lams.tool.wiki.dao.IWikiSessionDAO#removeWikiUsers(org.lams.lams.tool.wiki.WikiSession) */
    public void removeWikiUsers(WikiSession wikiSession)
    {
    	this.getHibernateTemplate().deleteAll(wikiSession.getWikiUsers());
    }
	
    
    /** @see org.lams.lams.tool.wiki.dao.IWikiSessionDAO#addWikiUsers(java.lang.Long, org.lams.lams.tool.wiki.WikiSession) */
    public void addWikiUsers(Long wikiSessionId, WikiUser user)
	{
	    WikiSession session = findWikiSessionById(wikiSessionId);
	    user.setWikiSession(session);
	    session.getWikiUsers().add(user);
	    this.getHibernateTemplate().saveOrUpdate(user);
	    this.getHibernateTemplate().merge(session);	    
	}
	
    /** @see org.lams.lams.tool.wiki.dao.IWikiSessionDAO#getSessionsFromContent(org.lams.lams.tool.wiki.WikiSession) */
	public List getSessionsFromContent(WikiContent wikiContent)
	{
	    return (getHibernateTemplate().findByNamedParam(GET_SESSIONS_FROM_CONTENT,
	            "wikiContent",
				wikiContent));
	}
    	
}
