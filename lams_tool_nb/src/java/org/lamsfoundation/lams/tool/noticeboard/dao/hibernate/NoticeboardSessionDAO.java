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

package org.lamsfoundation.lams.tool.noticeboard.dao.hibernate;

import java.util.List;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardSession;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardUser;
import org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardSessionDAO;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;
import org.springframework.orm.hibernate.HibernateCallback;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

/**
 * @author mtruong
 * <p>Hibernate implementation for database access to Noticeboard sessions for the noticeboard tool.</p>
 */

public class NoticeboardSessionDAO extends HibernateDaoSupport implements INoticeboardSessionDAO {
	
    private static final String LOAD_NBSESSION_BY_USER = "select ns from NoticeboardSession ns left join fetch "
        + "ns.nbUsers user where user.userId=:userId";
    
    private static final String GET_SESSIONS_FROM_CONTENT = "select ns.nbSessionId from NoticeboardSession ns where ns.nbContent= :nbContent";
    
    /** @see org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardSessionDAO#getNbSessionByUID(java.lang.Long) */
    public NoticeboardSession getNbSessionByUID(Long uid)
	{
		 return (NoticeboardSession) this.getHibernateTemplate()
         .get(NoticeboardSession.class, uid);
	}
	
    /** @see org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardSessionDAO#findNbSessionById(java.lang.Long) */
    public NoticeboardSession findNbSessionById(Long nbSessionId)
	{
	    String query = "from NoticeboardSession nbS where nbS.nbSessionId=?";
	    List session = getHibernateTemplate().find(query,nbSessionId);
		
		if(session!=null && session.size() == 0)
		{			
			return null;
		}
		else
		{
			return (NoticeboardSession)session.get(0);
		}
	}
	
    /** @see org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardSessionDAO#saveNbSession(org.lamsfoundation.lams.tool.noticeboard.NoticeboardSession) */
    public void saveNbSession(NoticeboardSession nbSession)
    {
    	this.getHibernateTemplate().save(nbSession);
    }
    
	
    /** @see org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardSessionDAO#updateNbSession(org.lamsfoundation.lams.tool.noticeboard.NoticeboardSession) */
    public void updateNbSession(NoticeboardSession nbSession)
    {
    	this.getHibernateTemplate().update(nbSession);
    }

   
    /** @see org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardSessionDAO#removeNbSessionByUID(java.lang.Long) */
    public void removeNbSessionByUID(Long uid)
    {
        NoticeboardSession nb = (NoticeboardSession)getHibernateTemplate().get(NoticeboardSession.class, uid);
    	this.getHibernateTemplate().delete(nb);
    }
    
    /** @see org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardSessionDAO#removeNbSession(java.lang.Long) */
    public void removeNbSession(Long nbSessionId)
    {
        String query = "from NoticeboardSession as nbS where nbS.nbSessionId =";
        StringBuffer sb = new StringBuffer(query);
        sb.append(nbSessionId.longValue());
        
        String queryString = sb.toString();
           
        this.getHibernateTemplate().delete(queryString);
      
    }
    /** @see org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardSessionDAO#removeNbSession(org.lamsfoundation.lams.tool.noticeboard.NoticeboardSession) */
    public void removeNbSession(NoticeboardSession nbSession)
    {
        this.getHibernateTemplate().delete(nbSession);
    }

    
    /** @see org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardSessionDAO#getNbSessionByUser(java.lang.Long) */
    public NoticeboardSession getNbSessionByUser(final Long userId)
	{
		 return (NoticeboardSession) getHibernateTemplate().execute(new HibernateCallback()
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
	
	 
    /** @see org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardSessionDAO#removeNbUsers(org.lamsfoundation.lams.tool.noticeboard.NoticeboardSession) */
    public void removeNbUsers(NoticeboardSession nbSession)
    {
    	this.getHibernateTemplate().deleteAll(nbSession.getNbUsers());
    }
	
    
    /** @see org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardSessionDAO#addNbUsers(java.lang.Long, org.lamsfoundation.lams.tool.noticeboard.NoticeboardSession) */
    public void addNbUsers(Long nbSessionId, NoticeboardUser user)
	{
	    NoticeboardSession session = findNbSessionById(nbSessionId);
	    user.setNbSession(session);
	    session.getNbUsers().add(user);
	    this.getHibernateTemplate().saveOrUpdate(user);
	    this.getHibernateTemplate().saveOrUpdateCopy(session);	    
	}
	
    /** @see org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardSessionDAO#getSessionsFromContent(org.lamsfoundation.lams.tool.noticeboard.NoticeboardSession) */
	public List getSessionsFromContent(NoticeboardContent nbContent)
	{
	    return (getHibernateTemplate().findByNamedParam(GET_SESSIONS_FROM_CONTENT,
	            "nbContent",
				nbContent));
	}
    	
}
