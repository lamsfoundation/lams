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
import org.lams.lams.tool.wiki.dao.IWikiUserDAO;
import org.lams.lams.tool.wiki.WikiContent;
import org.lams.lams.tool.wiki.WikiUser;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.lams.lams.tool.wiki.WikiSession;

/**
 * @author mtruong
 * <p>Hibernate implementation for database access to Wiki users (learners) for the wiki tool.</p>
 */
public class WikiUserDAO extends HibernateDaoSupport implements IWikiUserDAO {
    
	private static final String FIND_NB_USER = "from " + WikiUser.class.getName() + " as wiki where wiki.userId=?";
	
	private static final String FIND_NB_USER_BY_SESSION = "from " + WikiUser.class.getName() + " as wiki where wiki.userId=? and wiki.wikiSession.wikiSessionId=?";
	
    private static final String COUNT_USERS_IN_SESSION = "select nu.userId from WikiUser nu where nu.wikiSession= :wikiSession";
   
  
	/** @see org.lams.lams.tool.wiki.dao.IWikiUserDAO#getWikiUserByID(java.lang.Long) */
	public WikiUser getWikiUser(Long userId, Long sessionId)
	{
	    String query = "from WikiUser user where user.userId=? and user.wikiSession.wikiSessionId=?";
	    Object[] values = new Object[2];
	    values[0] = userId;
	    values[1] = sessionId;
	    List users = getHibernateTemplate().find(query,values);
		if(users!=null && users.size() == 0)
		{			
			return null;
		}
		else
		{
			return (WikiUser)users.get(0);
		}
	}
	
	/** @see org.lams.lams.tool.wiki.dao.IWikiUserDAO#getWikiUserBySession(java.lang.Long, java.lang.Long)*/
	public WikiUser getWikiUserBySession(Long userId, Long sessionId)
	{	
		List usersReturned = getSession().createQuery(FIND_NB_USER_BY_SESSION)
			.setLong(0,userId.longValue())
			.setLong(1, sessionId.longValue())
			.list();
	
		if(usersReturned != null && usersReturned.size() > 0){
			WikiUser wiki = (WikiUser) usersReturned.get(0);
			return wiki;
		}
		else
			return null;

	}

	/** @see org.lams.lams.tool.wiki.dao.IWikiUserDAO#saveWikiUser(org.lams.lams.tool.wiki.WikiUser) */
	public void saveWikiUser(WikiUser wikiUser)
    {
    	this.getHibernateTemplate().save(wikiUser);
    }
	
	/** @see org.lams.lams.tool.wiki.dao.IWikiUserDAO#updateWikiUser(org.lams.lams.tool.wiki.WikiUser) */
    public void updateWikiUser(WikiUser wikiUser)
    {
    	this.getHibernateTemplate().update(wikiUser);
    }
    
    /** @see org.lams.lams.tool.wiki.dao.IWikiUserDAO#removeWikiUser(java.lang.Long) */
    public void removeWikiUser(Long userId)
    {
		if ( userId != null) {
			//String query = "from org.lams.lams.tool.wiki.WikiContent as wiki where wiki.wikiContentId=?";
			List list = getSession().createQuery(FIND_NB_USER)
				.setLong(0,userId.longValue())
				.list();
			
			if(list != null && list.size() > 0){
				WikiUser wiki = (WikiUser) list.get(0);
				this.getSession().setFlushMode(FlushMode.AUTO);
				this.getHibernateTemplate().delete(wiki);
				this.getHibernateTemplate().flush();
			}
		}
      
    }
    
    /** @see org.lams.lams.tool.wiki.dao.IWikiUserDAO#removeWikiUser(org.lams.lams.tool.wiki.WikiUser) */
    public void removeWikiUser(WikiUser wikiUser)
    {
       // this.getHibernateTemplate().delete(wikiUser);
    	removeWikiUser(wikiUser.getUserId());
    }
    
    /** @see org.lams.lams.tool.wiki.dao.IWikiUserDAO#getNumberOfUsers((org.lams.lams.tool.wiki.WikiSession) */
    public int getNumberOfUsers(WikiSession wikiSession)
    {
        return (getHibernateTemplate().findByNamedParam(COUNT_USERS_IN_SESSION,
	            "wikiSession",
				wikiSession)).size();
    }
    
    public List getWikiUsersBySession(Long sessionId) {
    	String query = "from WikiUser user where user.wikiSession.wikiSessionId=?";
	    return getHibernateTemplate().find(query,sessionId);
    }
}
