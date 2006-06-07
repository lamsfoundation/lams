/***************************************************************************
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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ***********************************************************************/
/* $$Id$$ */
package org.lamsfoundation.lams.tool.qa.dao.hibernate;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.FlushMode;
import org.lamsfoundation.lams.tool.qa.QaQueUsr;
import org.lamsfoundation.lams.tool.qa.QaSession;
import org.lamsfoundation.lams.tool.qa.dao.IQaQueUsrDAO;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;



/**
 * @author Ozgur Demirtas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class QaQueUsrDAO extends HibernateDaoSupport implements IQaQueUsrDAO {
	 	static Logger logger = Logger.getLogger(QaQueUsrDAO.class.getName());
	 	
	 private static final String COUNT_SESSION_USER = "select qaQueUsr.queUsrId from QaQueUsr qaQueUsr where qaQueUsr.qaSessionId= :qaSession";
	 private static final String LOAD_USER_FOR_SESSION = "from qaQueUsr in class QaQueUsr where  qaQueUsr.qaSessionId= :qaSessionId";
		
	   public QaQueUsr getQaUserByUID(Long uid)
	   {
			 return (QaQueUsr) this.getHibernateTemplate()
	         .get(QaQueUsr.class, uid);
	   }

		
		public int countSessionUser(QaSession qaSession)
	    {
		   return (getHibernateTemplate().findByNamedParam(COUNT_SESSION_USER,
	            "qaSession",
				qaSession)).size();
	    }
	 	

		public QaQueUsr getQaQueUsrById(long qaQueUsrId)
		{
			String query = "from QaQueUsr user where user.queUsrId=?";
			
				HibernateTemplate templ = this.getHibernateTemplate();
				List list = getSession().createQuery(query)
				.setLong(0,qaQueUsrId)
				.list();
				
				if(list != null && list.size() > 0){
					QaQueUsr qu = (QaQueUsr) list.get(0);
					return qu;
				}
				return null;
		}


		public QaQueUsr getQaUserBySession(final Long queUsrId, final Long qaSessionId)
		{
			
			String strGetUser = "from qaQueUsr in class QaQueUsr where qaQueUsr.queUsrId=:queUsrId and qaQueUsr.qaSessionId=:qaSessionId";
	        HibernateTemplate templ = this.getHibernateTemplate();
			List list = getSession().createQuery(strGetUser)
				.setLong("queUsrId", queUsrId.longValue())
				.setLong("qaSessionId", qaSessionId.longValue())				
				.list();
			
			if(list != null && list.size() > 0){
				QaQueUsr usr = (QaQueUsr) list.get(0);
				return usr;
			}
			return null;
		}

		
		
		
		public List getUserBySessionOnly(final QaSession qaSession)
	    {
	        HibernateTemplate templ = this.getHibernateTemplate();
	        List list = getSession().createQuery(LOAD_USER_FOR_SESSION)
			.setLong("qaSessionId", qaSession.getUid().longValue())				
			.list();
			return list;
	    }
		
		
		public QaQueUsr loadQaQueUsrById(long qaQueUsrId)
	 	{
	 		return getQaQueUsrById(qaQueUsrId);
	 	}

		
	 	public void createUsr(QaQueUsr usr) 
	    {
	 		this.getSession().setFlushMode(FlushMode.AUTO);
	    	this.getHibernateTemplate().save(usr);
	    }
	 	
	 	public void updateUsr(QaQueUsr usr) 
	    {
	 		this.getSession().setFlushMode(FlushMode.AUTO);
	    	this.getHibernateTemplate().update(usr);
	    }

	 	
	 	public void deleteQaQueUsr(QaQueUsr qaQueUsr) 
	    {
	 		this.getSession().setFlushMode(FlushMode.AUTO);
	    	this.getHibernateTemplate().delete(qaQueUsr);
	    }
	 	
	    public int getTotalNumberOfUsers() {
			String query="from obj in class QaQueUsr"; 
			return this.getHibernateTemplate().find(query).size();
		}
} 