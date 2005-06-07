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

import org.lamsfoundation.lams.tool.noticeboard.NoticeboardSession;
import org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardSessionDAO;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;

/**
 * @author mtruong
 * Hibernate implementation for database access for the noticeboard tool.
 */

public class NoticeboardSessionDAO extends HibernateDaoSupport implements INoticeboardSessionDAO {
	
	
	/** Finds a noticeboard by its noticeboard id and returns
	 * null if not found
	 */
	
	public NoticeboardSession getNbSessionById(Long nbSessionId)
	{
		 return (NoticeboardSession) this.getHibernateTemplate()
         .get(NoticeboardSession.class, nbSessionId);
	}
	
	public NoticeboardSession loadNbSessionById(Long nbSessionId)
	{
		 return (NoticeboardSession) this.getHibernateTemplate()
         .load(NoticeboardSession.class, nbSessionId);
	}
    	
    public void saveNbSession(NoticeboardSession nbSession)
    {
    	this.getHibernateTemplate().save(nbSession);
    }
    
    public void updateNbSession(NoticeboardSession nbSession)
    {
    	this.getHibernateTemplate().update(nbSession);
    }

    public void removeNbSession(Long nbSessionId)
    {
    	NoticeboardSession nb = (NoticeboardSession)getHibernateTemplate().get(NoticeboardSession.class, nbSessionId);
    	getHibernateTemplate().delete(nb);
    }

}
