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

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.FlushMode;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaUploadedFile;
import org.lamsfoundation.lams.tool.qa.dao.IQaUploadedFileDAO;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;


/**
 * @author Ozgur Demirtas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class QaUploadedFileDAO extends HibernateDaoSupport implements IQaUploadedFileDAO {
	 	static Logger logger = Logger.getLogger(QaUploadedFileDAO.class.getName());
	 	
        private static final String GET_UPLOADED_FILES = "from QaUploadedFile qaUploadedFile where qaUploadedFile.qaContent.qaContentId = :contentId";
        
	 	private static final String DELETE_FILES_META_DATA = "from qaUploadedFile in class QaUploadedFile";
	 	
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
	 		this.getSession().setFlushMode(FlushMode.AUTO);
	        this.getHibernateTemplate().update(qaUploadedFile);
	    }
	 	
	     
	    public void saveUploadFile(QaUploadedFile qaUploadedFile) 
	    {
	    	this.getSession().setFlushMode(FlushMode.AUTO);
	    	this.getHibernateTemplate().save(qaUploadedFile);
	    }
	    
	    public void createUploadFile(QaUploadedFile qaUploadedFile) 
	    {
	    	this.getSession().setFlushMode(FlushMode.AUTO);
	    	this.getHibernateTemplate().save(qaUploadedFile);
	    }
	    
	    public void UpdateUploadFile(QaUploadedFile qaUploadedFile)
	    {
	    	this.getSession().setFlushMode(FlushMode.AUTO);
	    	this.getHibernateTemplate().update(qaUploadedFile);	
	    }

	    
	    public void cleanUploadedFilesMetaData()
	    {
	    	/*
	    	String query = "from uploadedFile in class org.lamsfoundation.lams.tool.qa.QaUploadedFile";
	    	this.getSession().setFlushMode(FlushMode.AUTO);
	        this.getHibernateTemplate().delete(query);
	        */
			HibernateTemplate templ = this.getHibernateTemplate();
			List list = getSession().createQuery(DELETE_FILES_META_DATA)
				.list();

			if(list != null && list.size() > 0){
				Iterator listIterator=list.iterator();
		    	while (listIterator.hasNext())
		    	{
		    		QaUploadedFile mcFile=(QaUploadedFile)listIterator.next();
					this.getSession().setFlushMode(FlushMode.AUTO);
		    		templ.delete(mcFile);
		    		templ.flush();
		    	}
			}
	    }
	    
	    public void removeUploadFile(Long submissionId)
	    {
	    	if (submissionId != null ) {
	    		
	    		String query = "from uploadedFile in class org.lamsfoundation.lams.tool.qa.QaUploadedFile"
	            + " where uploadedFile.submissionId = ?";
	    		Object obj = this.getSession().createQuery(query)
					.setLong(0,submissionId.longValue())
					.uniqueResult();
	    		if ( obj != null ) { 
	    			this.getSession().setFlushMode(FlushMode.AUTO);
	    			this.getHibernateTemplate().delete(obj);
	    		}
	    	}
    	}
	    
	    public List retrieveQaUploadedFiles(QaContent qa)
	    {
	      List listFilenames=null;
	      	listFilenames=(getHibernateTemplate().findByNamedParam(GET_UPLOADED_FILES, "contentId", qa.getQaContentId()));	
		  return listFilenames;	
	    }
	    
	    public void deleteUploadFile(QaUploadedFile qaUploadedFile)
	    {
	    		this.getSession().setFlushMode(FlushMode.AUTO);
	            this.getHibernateTemplate().delete(qaUploadedFile);
    	}
	    
	    public void flush()
	    {
	        this.getHibernateTemplate().flush();
	    }
	    
} 