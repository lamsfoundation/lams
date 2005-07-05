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
import org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardSessionDAO;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;
import org.springframework.orm.hibernate.HibernateCallback;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

/**
 * @author mtruong
 * Hibernate implementation for database access to Noticeboard sessions for the noticeboard tool.
 */

public class NoticeboardSessionDAO extends HibernateDaoSupport implements INoticeboardSessionDAO {
	
    private static final String LOAD_NBSESSION_BY_USER = "select ns from NoticeboardSession ns left join fetch "
        + "ns.nbUsers user where user.userId=:userId";

    
    /**
	 * <p>Return the persistent instance of a NoticeboardSession  
	 * with the given identifier <code>uid</code>, returns null if not found. </p>
	 * 
	 * @param uid an identifier for the NoticeboardSession object.
	 * @return the persistent instance of a NoticeboardSession or null if not found
	 */
	public NoticeboardSession getNbSessionByUID(Long uid)
	{
		 return (NoticeboardSession) this.getHibernateTemplate()
         .get(NoticeboardSession.class, uid);
	}
	
	
	/**
	 * <p> Return the persistent instance of a NoticeboardSession
	 * with the given tool session id <code>nbSessionId</code>,
	 * returns null if not found.</p>
	 * 
	 * @param nbSessionId The tool session id
	 * @return the persistent instance of a NoticeboardSession or null if not found.
	 */
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
	
	
	/**
	 * <p>Persist the given persistent instance of NoticeboardSession.</p>
	 * 
	 * @param nbSession The instance of NoticeboardSession to persist.
	 */
	public void saveNbSession(NoticeboardSession nbSession)
    {
    	this.getHibernateTemplate().save(nbSession);
    }
    
	/**
     * <p>Update the given persistent instance of NoticeboardSession.</p>
     * 
     * @param nbContent The instance of NoticeboardSession to persist.
     */
    public void updateNbSession(NoticeboardSession nbSession)
    {
    	this.getHibernateTemplate().update(nbSession);
    }

    /**
     * <p>Delete the given instance of NoticeboardSession with the
     * given identifier <code>uid</code>
     * 
     * @param uid an identifier for the NoticeboardSession instance. 
     */
    public void removeNbSessionByUID(Long uid)
    {
        NoticeboardSession nb = (NoticeboardSession)getHibernateTemplate().get(NoticeboardSession.class, uid);
    	this.getHibernateTemplate().delete(nb);
    }
    
    /**
     * <p>Delete the given instance of NoticeboardSession with the
     * given tool session id <code>nbSessionid</code>
     * 
     * @param nbSessionId The tool session Id. 
     */
    public void removeNbSession(Long nbSessionId)
    {
        String query = "from NoticeboardSession as nbS where nbS.nbSessionId =";
        StringBuffer sb = new StringBuffer(query);
        sb.append(nbSessionId.longValue());
        
        String queryString = sb.toString();
           
        this.getHibernateTemplate().delete(queryString);
      
    }
    /**
     * <p>Delete the given instance of NoticeboardSession</p>
     * 
     * @param nbSession The instance of NoticeboardSession to delete. 
     */
    public void removeNbSession(NoticeboardSession nbSession)
    {
        this.getHibernateTemplate().delete(nbSession);
    }

    /**
     * <p> Returns the persistent instance of NoticeboardSession
     * associated with the given noticeboard user, with user id <code>userId</code>, 
     * returns null if not found.
     * 
     * @param userId The noticeboard user id
     * @return a persistent instance of NoticeboardSessions or null if not found.
     */	
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
	
	 /**
     * <p>Deletes all instances of NoticeboardUser that are associated
     * with the given instance of NoticeboardSession</p>
     * 
     * @param nbSession The instance of NoticeboardSession in which corresponding instances of NoticeboardUser should be deleted.
     */
	public void removeNbUsers(NoticeboardSession nbSession)
    {
    	this.getHibernateTemplate().deleteAll(nbSession.getNbUsers());
    }
	
	
    	
}
