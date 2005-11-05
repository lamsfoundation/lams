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

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.FlushMode;
import org.lamsfoundation.lams.tool.mc.McUploadedFile;
import org.lamsfoundation.lams.tool.mc.dao.IMcUploadedFileDAO;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;


/**
 * @author Ozgur Demirtas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class McUploadedFileDAO extends HibernateDaoSupport implements IMcUploadedFileDAO {
	 	static Logger logger = Logger.getLogger(McUploadedFileDAO.class.getName());
	 	
	 	private static final String GET_ONLINE_FILENAMES_FOR_CONTENT = "select mcUploadedFile.filename from McUploadedFile mcUploadedFile where mcUploadedFile.mcContentId = :mcContentId and mcUploadedFile.fileOnline=1";
	 	private static final String GET_OFFLINE_FILENAMES_FOR_CONTENT = "select mcUploadedFile.filename from McUploadedFile mcUploadedFile where mcUploadedFile.mcContentId = :mcContentId and mcUploadedFile.fileOnline=0";
	 	
	 	private static final String GET_ONLINE_FILES_UUID = "select mcUploadedFile.uuid from McUploadedFile mcUploadedFile where mcUploadedFile.mcContentId = :mcContentId and mcUploadedFile.fileOnline=1";
	 	private static final String GET_ONLINE_FILES_NAME ="select mcUploadedFile.filename from McUploadedFile mcUploadedFile where mcUploadedFile.mcContentId = :mcContentId and mcUploadedFile.fileOnline=1 order by mcUploadedFile.uuid";
	 	
	 	private static final String GET_OFFLINE_FILES_UUID = "select mcUploadedFile.uuid    from McUploadedFile mcUploadedFile where mcUploadedFile.mcContentId = :mcContentId and mcUploadedFile.fileOnline=0";
	 	private static final String GET_OFFLINE_FILES_NAME ="select mcUploadedFile.filename from McUploadedFile mcUploadedFile where mcUploadedFile.mcContentId = :mcContentId and mcUploadedFile.fileOnline=0 order by mcUploadedFile.uuid";
	 	
	 	
	 	private static final String GET_OFFLINE_FILES_UUIDPLUSFILENAME = "select (mcUploadedFile.uuid + '~' + mcUploadedFile.filename)   from McUploadedFile mcUploadedFile where mcUploadedFile.mcContentId = :mcContentId and mcUploadedFile.fileOnline=0";
	 	
	 	private static final String FIND_ALL_UPLOADED_FILE_DATA = "from mcUploadedFile in class McUploadedFile";
		
	 	
	 	public McUploadedFile getUploadedFileById(long submissionId)
	    {
	        return (McUploadedFile) this.getHibernateTemplate()
	                                   .load(McUploadedFile.class, new Long(submissionId));
	    }
	 	
	 	/**
	 	 * 
	 	 * return null if not found
	 	 */
	 	public McUploadedFile loadUploadedFileById(long uid)
	    {
	    	return (McUploadedFile) this.getHibernateTemplate().get(McUploadedFile.class, new Long(uid));
	    }

	 	
	 	
	 	public void updateUploadFile(McUploadedFile mcUploadedFile)
	    {
	        this.getHibernateTemplate().update(mcUploadedFile);
			this.getSession().setFlushMode(FlushMode.AUTO);
	    }
	 	
	     
	    public void saveUploadFile(McUploadedFile mcUploadedFile) 
	    {
	    	this.getHibernateTemplate().save(mcUploadedFile);
			this.getSession().setFlushMode(FlushMode.AUTO);
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
	    	HibernateTemplate templ = this.getHibernateTemplate();
			List list = getSession().createQuery(FIND_ALL_UPLOADED_FILE_DATA)
				.list();

			if(list != null && list.size() > 0){
				Iterator listIterator=list.iterator();
		    	while (listIterator.hasNext())
		    	{
		    		McUploadedFile mcFile=(McUploadedFile)listIterator.next();
					this.getSession().setFlushMode(FlushMode.AUTO);
		    		templ.delete(mcFile);
		    		templ.flush();
		    	}
			}
	    }
	    
	    public void removeUploadFile(Long uid)
	    {
	    	if (uid != null ) {
	    		
	    		String query = "from uploadedFile in class org.lamsfoundation.lams.tool.mc.McUploadedFile"
	            + " where uploadedFile.uid = ?";
	    		Object obj = this.getSession().createQuery(query)
					.setLong(0,uid.longValue())
					.uniqueResult();
	    		if ( obj != null ) { 
	    			this.getHibernateTemplate().delete(obj);
	    		}
	    	}
    	}
	    
	    public List retrieveMcUploadedFiles(Long mcContentId, boolean fileOnline)
	    {
	      List listFilenames=null;
	    	
	      if (fileOnline)
	      {
	      	listFilenames=(getHibernateTemplate().findByNamedParam(GET_ONLINE_FILENAMES_FOR_CONTENT,
	                "mcContentId",
					mcContentId));	
	      }
	      else
	      {
	      	listFilenames=(getHibernateTemplate().findByNamedParam(GET_OFFLINE_FILENAMES_FOR_CONTENT,
	                "mcContentId",
					mcContentId));  	
	      }
		  return listFilenames;	
	    }
	    
	    
	    public List retrieveMcUploadedOfflineFilesUuid(Long mcContentId)
	    {
	      HibernateTemplate templ = this.getHibernateTemplate();
	      List list = getSession().createQuery(GET_OFFLINE_FILES_UUID)
				.setLong("mcContentId", mcContentId.longValue())
				.list();
	      
	      
		  return list;
	    }
	    
	    public List retrieveMcUploadedOnlineFilesUuid(Long mcContentId)
	    {
	      HibernateTemplate templ = this.getHibernateTemplate();
	      List list = getSession().createQuery(GET_ONLINE_FILES_UUID)
				.setLong("mcContentId", mcContentId.longValue())
				.list();
	      
		  return list;
	    }
	    
	    public List retrieveMcUploadedOfflineFilesUuidPlusFilename(Long mcContentId)
	    {
	      HibernateTemplate templ = this.getHibernateTemplate();
	      List list = getSession().createQuery(GET_OFFLINE_FILES_UUIDPLUSFILENAME)
				.setLong("mcContentId", mcContentId.longValue())
				.list();
	      
		  return list;
	    }
	    
	    
	    
	    
	    public List retrieveMcUploadedOfflineFilesName(Long mcContentId)
	    {
	      HibernateTemplate templ = this.getHibernateTemplate();
	      List list = getSession().createQuery(GET_OFFLINE_FILES_NAME)
				.setLong("mcContentId", mcContentId.longValue())
				.list();
	      
		  return list;
	    }

	    
	    public List retrieveMcUploadedOnlineFilesName(Long mcContentId)
	    {
	      HibernateTemplate templ = this.getHibernateTemplate();
	      List list = getSession().createQuery(GET_ONLINE_FILES_NAME)
				.setLong("mcContentId", mcContentId.longValue())
				.list();
	      
		  return list;
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