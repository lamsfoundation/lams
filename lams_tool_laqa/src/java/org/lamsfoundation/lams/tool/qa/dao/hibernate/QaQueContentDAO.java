/*
 * Created on 15/03/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool.qa.dao.hibernate;

import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaQueContent;
import org.lamsfoundation.lams.tool.qa.dao.IQaQueContentDAO;
import org.springframework.orm.hibernate.HibernateCallback;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;




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
	    	this.getHibernateTemplate().save(queContent);
	    }
		
		public void removeQueContent(long qaQueContentId) 
	    {
			QaQueContent qaQueContent= (QaQueContent) this.getHibernateTemplate().load(QaQueContent.class, new Long(qaQueContentId));
	    	this.getHibernateTemplate().delete(qaQueContent);
	    }
} 