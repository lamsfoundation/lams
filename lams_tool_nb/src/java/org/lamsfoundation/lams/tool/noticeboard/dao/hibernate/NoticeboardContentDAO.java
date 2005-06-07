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

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;
import org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardContentDAO;
import org.springframework.orm.hibernate.HibernateCallback;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;

/**
 * @author mtruong
 * Hibernate implementation for database access for the noticeboard tool.
 */

public class NoticeboardContentDAO extends HibernateDaoSupport implements INoticeboardContentDAO {
	
	
	private static final String LOAD_NB_BY_SESSION = "select nb from NoticeboardContent nb left join fetch "
        + "nb.nbSessions session where session.nbSessionId=:sessionId";

	
	/** Finds a noticeboard by its noticeboard id and returns
	 * null if not found
	 */
	
	public NoticeboardContent getNbContentById(Long nbContentId)
	{
		 return (NoticeboardContent) this.getHibernateTemplate()
         .get(NoticeboardContent.class, nbContentId);
	}
	
	public NoticeboardContent loadNbContentById(Long nbContentId)
	{
		 return (NoticeboardContent) this.getHibernateTemplate()
         .load(NoticeboardContent.class, nbContentId);
	}
    	
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
	
    public void saveNbContent(NoticeboardContent nbContent)
    {
    	this.getHibernateTemplate().save(nbContent);
    }
    
    public void updateNbContent(NoticeboardContent nbContent)
    {
    	this.getHibernateTemplate().update(nbContent);
    }

    public void removeNoticeboard(Long nbContentId)
    {
    	NoticeboardContent nb = (NoticeboardContent)getHibernateTemplate().get(NoticeboardContent.class, nbContentId);
    	getHibernateTemplate().delete(nb);
    }
    
    public void removeNbSessions(NoticeboardContent nbContent)
    {
    	this.getHibernateTemplate().deleteAll(nbContent.getNbSessions());
    }
  
}
