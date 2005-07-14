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
import java.lang.Long;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardSession;
import org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardContentDAO;
import org.springframework.orm.hibernate.HibernateCallback;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;

/**
 * @author mtruong
 * Hibernate implementation for database access to Noticeboard content for the noticeboard tool.
 */

public class NoticeboardContentDAO extends HibernateDaoSupport implements INoticeboardContentDAO {
	
	
	private static final String LOAD_NB_BY_SESSION = "select nb from NoticeboardContent nb left join fetch "
        + "nb.nbSessions session where session.nbSessionId=:sessionId";

	

	/**
	 * <p>Return the persistent instance of a NoticeboardContent  
	 * with the given identifier <code>uid</code>, returns null if not found. </p>
	 * 
	 * @param uid an identifier for the NoticeboardContent instance.
	 * @return the persistent instance of a NoticeboardContent or null if not found
	 */
	public NoticeboardContent getNbContentByUID(Long uid)
	{
		 return (NoticeboardContent) this.getHibernateTemplate()
         .get(NoticeboardContent.class, uid);
	}
	
	/**
	 * <p> Return the persistent instance of a NoticeboardContent
	 * with the given tool content id <code>nbContentId</code>,
	 * returns null if not found.</p>
	 * 
	 * @param nbContentId The tool content id
	 * @return the persistent instance of a NoticeboardContent or null if not found.
	 */
	public NoticeboardContent findNbContentById(Long nbContentId)
	{
	    String query = "from NoticeboardContent as nb where nb.nbContentId = ?";
		List content = getHibernateTemplate().find(query,nbContentId);
			
		if(content!=null && content.size() == 0)
		{			
			return null;
		}
		else
		{
			return (NoticeboardContent)content.get(0);
		}
	
	}
    /**
     * <p> Returns the persistent instance of NoticeboardContent
     * with the given tool session id <code>nbSessionId</code>, returns null if not found.
     * 
     * @param nbSessionId The tool session id
     * @return a persistent instance of NoticeboardContent or null if not found.
     */	
	public NoticeboardContent getNbContentBySession(final Long nbSessionId)
	{
		 return (NoticeboardContent) getHibernateTemplate().execute(new HibernateCallback()
                {

                    public Object doInHibernate(Session session) throws HibernateException
                    {
                        return session.createQuery(LOAD_NB_BY_SESSION)
                                      .setLong("sessionId",
                                      		nbSessionId.longValue())
                                      .uniqueResult();
                    }
                });
	}
	
	/**
	 * <p>Persist the given persistent instance of NoticeboardContent.</p>
	 * 
	 * @param nbContent The instance of NoticeboardContent to persist.
	 */
    public void saveNbContent(NoticeboardContent nbContent)
    {
    	this.getHibernateTemplate().save(nbContent);
    }
    
    /**
     * <p>Update the given persistent instance of NoticeboardContent.</p>
     * 
     * @param nbContent The instance of NoticeboardContent to persist.
     */
    public void updateNbContent(NoticeboardContent nbContent)
    {
    	this.getHibernateTemplate().update(nbContent);
    }

    /**
     * <p>Delete the given instance of NoticeboardContent with the
     * given tool content id <code>nbContentId</code>
     * 
     * @param nbContentId The tool content Id. 
     */
    public void removeNoticeboard(Long nbContentId)
    {
       
       String query = "from NoticeboardContent as nb where nb.nbContentId=";
       StringBuffer sb = new StringBuffer(query);
       sb.append(nbContentId.longValue());
       String queryString = sb.toString();
          
       this.getHibernateTemplate().delete(queryString);
    }
    
    /**
     * <p>Delete the given instance of NoticeboardContent</p>
     * 
     * @param nbContent The instance of NoticeboardContent to delete. 
     */
    public void removeNoticeboard(NoticeboardContent nbContent)
    {
        this.getHibernateTemplate().delete(nbContent);
    }
    
    /**
     * <p>Deletes all instances of NoticeboardSession that are associated
     * with the given instance of NoticeboardContent</p>
     * 
     * @param nbContent The instance of NoticeboardContent in which corresponding instances of NoticeboardSession should be deleted.
     */
    public void removeNbSessions(NoticeboardContent nbContent)
    {
    	this.getHibernateTemplate().deleteAll(nbContent.getNbSessions());
    }
    
    /**
     * <p>Creates a persistent instance of NoticeboardSession which is associated
     * with the NoticeboardContent with tool content id <code>nbContentId</code> 
     * </p>
     * 
     * @param nbContentId The tool content id
     * @param nbSession The instance of NoticeboardSession to add
     */
    public void addNbSession(Long nbContentId, NoticeboardSession nbSession)
    {
        NoticeboardContent content = findNbContentById(nbContentId);
        nbSession.setNbContent(content);
        content.getNbSessions().add(nbSession);
        this.getHibernateTemplate().saveOrUpdate(nbSession);
        this.getHibernateTemplate().saveOrUpdate(content);
        
    }
  
}
