/*
 * Created on 15/03/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
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
		
		
		public QaQueUsr loadQaQueUsrById(long qaQueUsrId)
	 	{
	 		return getQaQueUsrById(qaQueUsrId);
	 	}

		
	 	public void createUsr(QaQueUsr usr) 
	    {
	 		this.getSession().setFlushMode(FlushMode.AUTO);
	    	this.getHibernateTemplate().save(usr);
	    }
	 	
	 	public void deleteQaQueUsr(QaQueUsr qaQueUsr) 
	    {
	 		this.getSession().setFlushMode(FlushMode.AUTO);
	    	this.getHibernateTemplate().delete(qaQueUsr);
	    }
	 	
	 	
} 