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

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.dao.IQaContentDAO;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;


/**
 * @author Ozgur Demirtas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class QaContentDAO extends HibernateDaoSupport implements IQaContentDAO {
	 	static Logger logger = Logger.getLogger(QaContentDAO.class.getName());
	 	
	 	private static final String LOAD_QA_BY_SESSION = "select qa from QaContent qa left join fetch "
            + "qa.qaSessions session where session.qaSessionId=:sessionId";

    
	 	private static final String COUNT_USER_RESPONSED = "select distinct u.userId from QaQueUsr u left join fetch"
            + " u.qaQueContent as ques where ques.qaContent = :qa group by u.userId";
	 	
	 	
	 	public QaContentDAO() {}

	 	public QaContent getQaById(long qaId)
	    {
	        return (QaContent) this.getHibernateTemplate()
	                                   .load(QaContent.class, new Long(qaId));
	    }
	 	
	 	/**
	 	 * 
	 	 * return null if not found
	 	 */
	 	public QaContent loadQaById(long qaId)
	    {
	    	return (QaContent) this.getHibernateTemplate().get(QaContent.class, new Long(qaId));
	    }

	 	
	 	
	 	public void updateQa(QaContent qa)
	    {
	        this.getHibernateTemplate().update(qa);
	    }
	 	

	 	 
	     public QaContent getQaBySession(final Long sessionId)
	     {
	         return (QaContent) getHibernateTemplate().execute(new HibernateCallback()
                              {

                                  public Object doInHibernate(Session session) throws HibernateException
                                  {
                                      return session.createQuery(LOAD_QA_BY_SESSION)
                                                    .setLong("sessionId",
                                                     sessionId.longValue())
                                                    .uniqueResult();
                                  }
                              });
	     }

	    public void saveQa(QaContent qa) 
	    {
	    	this.getHibernateTemplate().save(qa);
	    }
	    
	    public void createQa(QaContent qa) 
	    {
	    	this.getHibernateTemplate().save(qa);
	    }
	    
	    public void UpdateQa(QaContent qa)
	    {
	    	this.getHibernateTemplate().update(qa);	
	    }

	    public int countUserResponsed(QaContent qa)
	    {
    	   return (getHibernateTemplate().findByNamedParam(COUNT_USER_RESPONSED,
                "qa",
                qa)).size();
	    }
	    
	    

	    /** GETS CALLED BY CONTRACT
	     */
	    public void removeAllQaSession(QaContent qaContent){
	    	this.getHibernateTemplate().deleteAll(qaContent.getQaSessions());	
	    }
	    
	    public void removeQa(Long qaContentId)
	    {
	    	if ( qaContentId != null ) {
		    	String query = "from qa in class org.lamsfoundation.lams.tool.qa.QaContent"
		            + " where qa.qaContentId = ?";
		    	Object obj = getSession().createQuery(query)
					.setLong(0,qaContentId.longValue())
					.uniqueResult();
		    	getHibernateTemplate().delete(obj);
	    	}
    	}
	    
	    public void deleteQa(QaContent qaContent)
	    {
	            this.getHibernateTemplate().delete(qaContent);
    	}

	    public void removeQaById(Long qaId)
	    {
	    	removeQa(qaId);
	    }

	    
	    public void flush()
	    {
	        this.getHibernateTemplate().flush();
	    }
	
	    
} 