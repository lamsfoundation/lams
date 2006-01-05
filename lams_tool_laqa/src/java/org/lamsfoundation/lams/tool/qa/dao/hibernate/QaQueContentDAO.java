/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
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

package org.lamsfoundation.lams.tool.qa.dao.hibernate;

import java.util.List;

import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaQueContent;
import org.lamsfoundation.lams.tool.qa.dao.IQaQueContentDAO;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;




/**
 * @author Ozgur Demirtas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class QaQueContentDAO extends HibernateDaoSupport implements IQaQueContentDAO {
	 	static Logger logger = Logger.getLogger(QaQueContentDAO.class.getName());
	 	private static final String LOAD_QUESTION_CONTENT_BY_CONTENT_ID = "from qaQueContent in class QaQueContent where qaQueContent.qaContentId=:qaContentId";
	 	private static final String GET_QUESTION_IDS_FOR_CONTENT = "select qaQueContent.qaQueContentId from QaQueContent qaQueContent where qaQueContent.qaContentId = :qa";
		
	 	public QaQueContent getToolDefaultQuestionContent(final long qaContentId)
	    {
	        return (QaQueContent) getHibernateTemplate().execute(new HibernateCallback()
	         {
	             public Object doInHibernate(Session session) throws HibernateException
	             {
	                 return session.createQuery(LOAD_QUESTION_CONTENT_BY_CONTENT_ID)
	                               .setLong("qaContentId", qaContentId)
	                               .uniqueResult();
	             }
	         });
	    }

	 	public List getQuestionIndsForContent(QaContent qa)
	    {
	    	   
			  List listDefaultQuestionIds=(getHibernateTemplate().findByNamedParam(GET_QUESTION_IDS_FOR_CONTENT,
	                "qa",
	                qa));
			  
			  return listDefaultQuestionIds;
	    }
	 	
	 	
	 	public QaQueContent getQaQueById(long qaQueContentId)
	 	{
	 		return (QaQueContent) this.getHibernateTemplate().load(QaQueContent.class, new Long(qaQueContentId));
	 	}
	 	
		public void createQueContent(QaQueContent queContent) 
	    {
			this.getSession().setFlushMode(FlushMode.AUTO);
	    	this.getHibernateTemplate().save(queContent);
	    }
		
		public void removeQueContent(long qaQueContentId) 
	    {
			QaQueContent qaQueContent= (QaQueContent) this.getHibernateTemplate().load(QaQueContent.class, new Long(qaQueContentId));
			this.getSession().setFlushMode(FlushMode.AUTO);
	    	this.getHibernateTemplate().delete(qaQueContent);
	    }
        
        public List getQaQueContentsByContentId(long qaContentId){
            return getHibernateTemplate().findByNamedParam(LOAD_QUESTION_CONTENT_BY_CONTENT_ID, "qaContentId", new Long(qaContentId));
        }
} 