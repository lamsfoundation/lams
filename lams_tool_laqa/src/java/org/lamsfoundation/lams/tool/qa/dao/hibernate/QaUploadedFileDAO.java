/*
 * Created on 15/03/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool.qa.dao.hibernate;

import net.sf.hibernate.Hibernate;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.qa.QaUploadedFile;
import org.lamsfoundation.lams.tool.qa.dao.IQaUploadedFileDAO;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;


/**
 * @author Ozgur Demirtas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class QaUploadedFileDAO extends HibernateDaoSupport implements IQaUploadedFileDAO {
	 	static Logger logger = Logger.getLogger(QaUploadedFileDAO.class.getName());
	 	
	 	public QaUploadedFile getUploadedFileById(long submissionId)
	    {
	        return (QaUploadedFile) this.getHibernateTemplate()
	                                   .load(QaUploadedFile.class, new Long(submissionId));
	    }
	 	
	 	/**
	 	 * 
	 	 * return null if not found
	 	 */
	 	public QaUploadedFile loadUploadedFileById(long submissionId)
	    {
	    	return (QaUploadedFile) this.getHibernateTemplate().get(QaUploadedFile.class, new Long(submissionId));
	    }

	 	
	 	
	 	public void updateUploadFile(QaUploadedFile qaUploadedFile)
	    {
	        this.getHibernateTemplate().update(qaUploadedFile);
	    }
	 	
	     
	    public void saveUploadFile(QaUploadedFile qaUploadedFile) 
	    {
	    	this.getHibernateTemplate().save(qaUploadedFile);
	    }
	    
	    public void createUploadFile(QaUploadedFile qaUploadedFile) 
	    {
	    	this.getHibernateTemplate().save(qaUploadedFile);
	    }
	    
	    public void UpdateUploadFile(QaUploadedFile qaUploadedFile)
	    {
	    	this.getHibernateTemplate().update(qaUploadedFile);	
	    }

	    
	    public void removeUploadFile(Long submissionId)
	    {
	    	String query = "from uploadedFile in class org.lamsfoundation.lams.tool.qa.QaUploadedFile"
	            + " where uploadedFile.submissionId = ?";
	            this.getHibernateTemplate().delete(query,submissionId,Hibernate.LONG);
    	}
	    
	    public void deleteUploadFile(QaUploadedFile qaUploadedFile)
	    {
	            this.getHibernateTemplate().delete(qaUploadedFile);
    	}
	    
	    public void flush()
	    {
	        this.getHibernateTemplate().flush();
	    }
	    
} 