/*
 * Created on 15/03/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool.qa.dao.hibernate;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.qa.QaQueUsr;
import org.lamsfoundation.lams.tool.qa.QaSession;
import org.lamsfoundation.lams.tool.qa.dao.IQaQueUsrDAO;
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
		
		
		public int countSessionUser(QaSession qaSession)
	    {
		   return (getHibernateTemplate().findByNamedParam(COUNT_SESSION_USER,
	            "qaSession",
				qaSession)).size();
	    }
	 	
	 	
	 	public QaQueUsr getQaQueUsrById(long qaQueUsrId)
	 	{
	 		return (QaQueUsr) this.getHibernateTemplate().load(QaQueUsr.class, new Long(qaQueUsrId));
	 	}
	 	
	 	public QaQueUsr loadQaQueUsrById(long qaQueUsrId)
	 	{
	 		return (QaQueUsr) this.getHibernateTemplate().get(QaQueUsr.class, new Long(qaQueUsrId));
	 	}
	 	
	 	public void createUsr(QaQueUsr usr) 
	    {
	    	this.getHibernateTemplate().save(usr);
	    }
	 	
	 	public void deleteQaQueUsr(QaQueUsr qaQueUsr) 
	    {
	    	this.getHibernateTemplate().delete(qaQueUsr);
	    }
	 	
	 	
} 