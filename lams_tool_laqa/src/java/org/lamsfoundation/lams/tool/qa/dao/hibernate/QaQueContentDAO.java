/*
 * Created on 15/03/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool.qa.dao.hibernate;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.qa.QaQueContent;
import org.lamsfoundation.lams.tool.qa.dao.IQaQueContentDAO;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;




/**
 * @author ozgurd
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class QaQueContentDAO extends HibernateDaoSupport implements IQaQueContentDAO {
	 	static Logger logger = Logger.getLogger(QaQueContentDAO.class.getName());
	 	
	 	public QaQueContent getQaQueById(long qaQueContentId)
	 	{
	 		return (QaQueContent) this.getHibernateTemplate().load(QaQueContent.class, new Long(qaQueContentId));
	 	}
	 	
		public void createQueContent(QaQueContent queContent) 
	    {
	    	this.getHibernateTemplate().save(queContent);
	    }
		
		public void removeQueContent(long qaQueContentId) 
	    {
			QaQueContent qaQueContent= (QaQueContent) this.getHibernateTemplate().load(QaQueContent.class, new Long(qaQueContentId));
	    	this.getHibernateTemplate().delete(qaQueContent);
	    }
} 