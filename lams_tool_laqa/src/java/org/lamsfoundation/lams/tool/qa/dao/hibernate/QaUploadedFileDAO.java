/*
 * Created on 15/03/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool.qa.dao.hibernate;

import java.util.List;

import net.sf.hibernate.Hibernate;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.qa.QaContent;
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
	 	
	 	private static final String GET_ONLINE_FILENAMES_FOR_CONTENT = "select qaUploadedFile.fileName from QaUploadedFile qaUploadedFile where qaUploadedFile.qaContentId = :qa and qaUploadedFile.fileOnline=1";
	 	private static final String GET_OFFLINE_FILENAMES_FOR_CONTENT = "select qaUploadedFile.fileName from QaUploadedFile qaUploadedFile where qaUploadedFile.qaContentId = :qa and qaUploadedFile.fileOnline=0";
	 	
	 	private static final String GET_ONLINE_FILES_UUID = "select qaUploadedFile.uuid from QaUploadedFile qaUploadedFile where qaUploadedFile.qaContentId = :qa and qaUploadedFile.fileOnline=1";
	 	private static final String GET_ONLINE_FILES_NAME ="select qaUploadedFile.fileName from QaUploadedFile qaUploadedFile where qaUploadedFile.qaContentId = :qa and qaUploadedFile.fileOnline=1 order by qaUploadedFile.uuid";
	 	
	 	private static final String GET_OFFLINE_FILES_UUID = "select qaUploadedFile.uuid from QaUploadedFile qaUploadedFile where qaUploadedFile.qaContentId = :qa and qaUploadedFile.fileOnline=0";
	 	private static final String GET_OFFLINE_FILES_NAME ="select qaUploadedFile.fileName from QaUploadedFile qaUploadedFile where qaUploadedFile.qaContentId = :qa and qaUploadedFile.fileOnline=0 order by qaUploadedFile.uuid";
	 	
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

	    
	    public void cleanUploadedFilesMetaData()
	    {
	    	String query = "from uploadedFile in class org.lamsfoundation.lams.tool.qa.QaUploadedFile";
	            this.getHibernateTemplate().delete(query);
	    }
	    
	    public void removeUploadFile(Long submissionId)
	    {
	    	String query = "from uploadedFile in class org.lamsfoundation.lams.tool.qa.QaUploadedFile"
	            + " where uploadedFile.submissionId = ?";
	            this.getHibernateTemplate().delete(query,submissionId,Hibernate.LONG);
    	}
	    
	    public List retrieveQaUploadedFiles(QaContent qa, boolean fileOnline)
	    {
	      List listFilenames=null;
	    	
	      if (fileOnline)
	      {
	      	listFilenames=(getHibernateTemplate().findByNamedParam(GET_ONLINE_FILENAMES_FOR_CONTENT,
	                "qa",
	                qa));	
	      }
	      else
	      {
	      	listFilenames=(getHibernateTemplate().findByNamedParam(GET_OFFLINE_FILENAMES_FOR_CONTENT,
	                "qa",
	                qa));  	
	      }
		  return listFilenames;	
	    }
	    
	    
	    public List retrieveQaUploadedOfflineFilesUuid(QaContent qa)
	    {
	      List listFilesUuid=null;
	    	
	      listFilesUuid=(getHibernateTemplate().findByNamedParam(GET_OFFLINE_FILES_UUID,
	                "qa",
	                qa));	
	      
		  return listFilesUuid;
	    }
	    
	    public List retrieveQaUploadedOnlineFilesUuid(QaContent qa)
	    {
	      List listFilesUuid=null;
	    	
	      listFilesUuid=(getHibernateTemplate().findByNamedParam(GET_ONLINE_FILES_UUID,
	                "qa",
	                qa));	
	      
		  return listFilesUuid;
	    }
	    
	    
	    public List retrieveQaUploadedOfflineFilesName(QaContent qa)
	    {
	      List listFilesUuid=null;
	    	
	      listFilesUuid=(getHibernateTemplate().findByNamedParam(GET_OFFLINE_FILES_NAME,
	                "qa",
	                qa));	
	      
		  return listFilesUuid;
	    }
	    
	    public List retrieveQaUploadedOnlineFilesName(QaContent qa)
	    {
	      List listFilesUuid=null;
	    	
	      listFilesUuid=(getHibernateTemplate().findByNamedParam(GET_ONLINE_FILES_NAME,
	                "qa",
	                qa));	
	      
		  return listFilesUuid;
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