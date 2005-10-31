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

package org.lamsfoundation.lams.tool.mc.dao.hibernate;

import java.util.List;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.mc.McContent;
import org.lamsfoundation.lams.tool.mc.McUploadedFile;
import org.lamsfoundation.lams.tool.mc.dao.IMcUploadedFileDAO;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;


/**
 * @author Ozgur Demirtas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class McUploadedFileDAO extends HibernateDaoSupport implements IMcUploadedFileDAO {
	 	static Logger logger = Logger.getLogger(McUploadedFileDAO.class.getName());
	 	
	 	private static final String GET_ONLINE_FILENAMES_FOR_CONTENT = "select mcUploadedFile.fileName from McUploadedFile mcUploadedFile where mcUploadedFile.mcContentId = :mc and mcUploadedFile.fileOnline=1";
	 	private static final String GET_OFFLINE_FILENAMES_FOR_CONTENT = "select mcUploadedFile.fileName from McUploadedFile mcUploadedFile where mcUploadedFile.mcContentId = :mc and mcUploadedFile.fileOnline=0";
	 	
	 	private static final String GET_ONLINE_FILES_UUID = "select mcUploadedFile.uuid from McUploadedFile mcUploadedFile where mcUploadedFile.mcContentId = :mc and mcUploadedFile.fileOnline=1";
	 	private static final String GET_ONLINE_FILES_NAME ="select mcUploadedFile.fileName from McUploadedFile mcUploadedFile where mcUploadedFile.mcContentId = :mc and mcUploadedFile.fileOnline=1 order by mcUploadedFile.uuid";
	 	
	 	private static final String GET_OFFLINE_FILES_UUID = "select mcUploadedFile.uuid from McUploadedFile mcUploadedFile where mcUploadedFile.mcContentId = :mc and mcUploadedFile.fileOnline=0";
	 	private static final String GET_OFFLINE_FILES_NAME ="select mcUploadedFile.fileName from McUploadedFile mcUploadedFile where mcUploadedFile.mcContentId = :mc and mcUploadedFile.fileOnline=0 order by mcUploadedFile.uuid";
	 	
	 	public McUploadedFile getUploadedFileById(long submissionId)
	    {
	        return (McUploadedFile) this.getHibernateTemplate()
	                                   .load(McUploadedFile.class, new Long(submissionId));
	    }
	 	
	 	/**
	 	 * 
	 	 * return null if not found
	 	 */
	 	public McUploadedFile loadUploadedFileById(long submissionId)
	    {
	    	return (McUploadedFile) this.getHibernateTemplate().get(McUploadedFile.class, new Long(submissionId));
	    }

	 	
	 	
	 	public void updateUploadFile(McUploadedFile mcUploadedFile)
	    {
	        this.getHibernateTemplate().update(mcUploadedFile);
	    }
	 	
	     
	    public void saveUploadFile(McUploadedFile mcUploadedFile) 
	    {
	    	this.getHibernateTemplate().save(mcUploadedFile);
	    }
	    
	    public void createUploadFile(McUploadedFile mcUploadedFile) 
	    {
	    	this.getHibernateTemplate().save(mcUploadedFile);
	    }
	    
	    public void UpdateUploadFile(McUploadedFile mcUploadedFile)
	    {
	    	this.getHibernateTemplate().update(mcUploadedFile);	
	    }

	    
	    public void cleanUploadedFilesMetaData()
	    {
	    	String query = "from uploadedFile in class org.lamsfoundation.lams.tool.mc.McUploadedFile";
	            this.getHibernateTemplate().delete(query);
	    }
	    
	    public void removeUploadFile(Long submissionId)
	    {
	    	if (submissionId != null ) {
	    		
	    		String query = "from uploadedFile in class org.lamsfoundation.lams.tool.mc.McUploadedFile"
	            + " where uploadedFile.submissionId = ?";
	    		Object obj = this.getSession().createQuery(query)
					.setLong(0,submissionId.longValue())
					.uniqueResult();
	    		if ( obj != null ) { 
	    			this.getHibernateTemplate().delete(obj);
	    		}
	    	}
    	}
	    
	    public List retrieveMcUploadedFiles(McContent mc, boolean fileOnline)
	    {
	      List listFilenames=null;
	    	
	      if (fileOnline)
	      {
	      	listFilenames=(getHibernateTemplate().findByNamedParam(GET_ONLINE_FILENAMES_FOR_CONTENT,
	                "mc",
	                mc));	
	      }
	      else
	      {
	      	listFilenames=(getHibernateTemplate().findByNamedParam(GET_OFFLINE_FILENAMES_FOR_CONTENT,
	                "mc",
	                mc));  	
	      }
		  return listFilenames;	
	    }
	    
	    
	    public List retrieveMcUploadedOfflineFilesUuid(McContent mc)
	    {
	      List listFilesUuid=null;
	    	
	      listFilesUuid=(getHibernateTemplate().findByNamedParam(GET_OFFLINE_FILES_UUID,
	                "mc",
	                mc));	
	      
		  return listFilesUuid;
	    }
	    
	    public List retrieveMcUploadedOnlineFilesUuid(McContent mc)
	    {
	      List listFilesUuid=null;
	    	
	      listFilesUuid=(getHibernateTemplate().findByNamedParam(GET_ONLINE_FILES_UUID,
	                "mc",
	                mc));	
	      
		  return listFilesUuid;
	    }
	    
	    
	    public List retrieveMcUploadedOfflineFilesName(McContent mc)
	    {
	      List listFilesUuid=null;
	    	
	      listFilesUuid=(getHibernateTemplate().findByNamedParam(GET_OFFLINE_FILES_NAME,
	                "mc",
	                mc));	
	      
		  return listFilesUuid;
	    }
	    
	    public List retrieveMcUploadedOnlineFilesName(McContent mc)
	    {
	      List listFilesUuid=null;
	    	
	      listFilesUuid=(getHibernateTemplate().findByNamedParam(GET_ONLINE_FILES_NAME,
	                "mc",
	                mc));	
	      
		  return listFilesUuid;
	    }
	    
	    
	    
	    public void deleteUploadFile(McUploadedFile mcUploadedFile)
	    {
	            this.getHibernateTemplate().delete(mcUploadedFile);
    	}
	    
	    public void flush()
	    {
	        this.getHibernateTemplate().flush();
	    }
	    
} 