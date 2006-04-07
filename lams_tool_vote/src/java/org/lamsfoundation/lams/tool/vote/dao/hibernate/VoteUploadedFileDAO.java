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

package org.lamsfoundation.lams.tool.vote.dao.hibernate;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.FlushMode;
import org.lamsfoundation.lams.tool.vote.dao.IVoteUploadedFileDAO;
import org.lamsfoundation.lams.tool.vote.pojos.VoteUploadedFile;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;


/**
 * @author ozgurd
 * <p>Hibernate implementation for database access to McUploadedFile for the mc tool.</p>
 */
public class VoteUploadedFileDAO extends HibernateDaoSupport implements IVoteUploadedFileDAO {
 	static Logger logger = Logger.getLogger(VoteUploadedFileDAO.class.getName());
 	
 	private static final String GET_ONLINE_FILENAMES_FOR_CONTENT = "select mcUploadedFile.filename from VoteUploadedFile mcUploadedFile where mcUploadedFile.mcContentId = :mcContentId and mcUploadedFile.fileOnline=1";
 	private static final String GET_OFFLINE_FILENAMES_FOR_CONTENT = "select mcUploadedFile.filename from VoteUploadedFile mcUploadedFile where mcUploadedFile.mcContentId = :mcContentId and mcUploadedFile.fileOnline=0";
 	
 	private static final String GET_ONLINE_FILES_UUID = "select mcUploadedFile.uuid from VoteUploadedFile mcUploadedFile where mcUploadedFile.mcContentId = :mcContentId and mcUploadedFile.fileOnline=1";
 	private static final String GET_ONLINE_FILES_NAME ="select mcUploadedFile.filename from VoteUploadedFile mcUploadedFile where mcUploadedFile.mcContentId = :mcContentId and mcUploadedFile.fileOnline=1 order by mcUploadedFile.uuid";
 	
 	private static final String GET_OFFLINE_FILES_UUID = "select mcUploadedFile.uuid from VoteUploadedFile mcUploadedFile where mcUploadedFile.mcContentId = :mcContentId and mcUploadedFile.fileOnline=0";
 	private static final String GET_OFFLINE_FILES_NAME ="select mcUploadedFile.filename from VoteUploadedFile mcUploadedFile where mcUploadedFile.mcContentId = :mcContentId and mcUploadedFile.fileOnline=0 order by mcUploadedFile.uuid";
 	
 	private static final String GET_FILES_UUID ="select mcUploadedFile.uuid from VoteUploadedFile mcUploadedFile where mcUploadedFile.filename=:filename";
 	
 	private static final String GET_OFFLINE_FILES_UUIDPLUSFILENAME = "select (mcUploadedFile.uuid + '~' + mcUploadedFile.filename)   from VoteUploadedFile mcUploadedFile where mcUploadedFile.mcContentId = :mcContentId and mcUploadedFile.fileOnline=0";
 	
 	private static final String FIND_ALL_UPLOADED_FILE_DATA = "from mcUploadedFile in class VoteUploadedFile";
 	
 	private static final String IS_UUID_PERSISTED ="select mcUploadedFile.uuid from VoteUploadedFile mcUploadedFile where mcUploadedFile.uuid=:uuid";
 	
 	private static final String GET_ONLINE_FILES_METADATA = "from mcUploadedFile in class VoteUploadedFile where mcUploadedFile.mcContentId = :mcContentId and mcUploadedFile.fileOnline=1";
 	
 	private static final String GET_OFFLINE_FILES_METADATA = "from mcUploadedFile in class VoteUploadedFile where mcUploadedFile.mcContentId = :mcContentId and mcUploadedFile.fileOnline=0";
 	
 	private static final String IS_OFFLINE_FILENAME_PERSISTED ="select mcUploadedFile from VoteUploadedFile mcUploadedFile where mcUploadedFile.mcContentId = :mcContentId and mcUploadedFile.filename=:filename and mcUploadedFile.fileOnline=0";
 	
 	private static final String IS_ONLINE_FILENAME_PERSISTED ="select mcUploadedFile from VoteUploadedFile mcUploadedFile where mcUploadedFile.mcContentId = :mcContentId and mcUploadedFile.filename=:filename and mcUploadedFile.fileOnline=1";
 	
 	
 	public VoteUploadedFile getUploadedFileById(long submissionId)
    {
        return (VoteUploadedFile) this.getHibernateTemplate()
                                   .load(VoteUploadedFile.class, new Long(submissionId));
    }
 	
 	/**
 	 * 
 	 * return null if not found
 	 */
 	public VoteUploadedFile loadUploadedFileById(long uid)
    {
    	return (VoteUploadedFile) this.getHibernateTemplate().get(VoteUploadedFile.class, new Long(uid));
    }

 	
 	
 	public void updateUploadFile(VoteUploadedFile mcUploadedFile)
    {
 		this.getSession().setFlushMode(FlushMode.AUTO);
        this.getHibernateTemplate().update(mcUploadedFile);
		this.getSession().setFlushMode(FlushMode.AUTO);
    }
 	
     
    public void saveUploadFile(VoteUploadedFile mcUploadedFile) 
    {
    	this.getSession().setFlushMode(FlushMode.AUTO);
    	this.getHibernateTemplate().save(mcUploadedFile);
		this.getSession().setFlushMode(FlushMode.AUTO);
    }
    
    public void createUploadFile(VoteUploadedFile mcUploadedFile) 
    {
    	this.getSession().setFlushMode(FlushMode.AUTO);
    	this.getHibernateTemplate().save(mcUploadedFile);
    }
    
    public void UpdateUploadFile(VoteUploadedFile mcUploadedFile)
    {
    	this.getSession().setFlushMode(FlushMode.AUTO);
    	this.getHibernateTemplate().update(mcUploadedFile);	
    }

    
    public void removeOffLineFile(String filename, Long mcContentId)
    {
    	HibernateTemplate templ = this.getHibernateTemplate();
		List list = getSession().createQuery(IS_OFFLINE_FILENAME_PERSISTED)
			.setString("filename", filename)
			.setLong("mcContentId", mcContentId.longValue())
			.list();
		
				if(list != null && list.size() > 0){
			Iterator listIterator=list.iterator();
	    	while (listIterator.hasNext())
	    	{
	    		VoteUploadedFile mcFile=(VoteUploadedFile)listIterator.next();
				this.getSession().setFlushMode(FlushMode.AUTO);
	    		templ.delete(mcFile);
	    		templ.flush();
	    	}
		}
    }

    public void removeOnLineFile(String filename, Long mcContentId)
    {
    	HibernateTemplate templ = this.getHibernateTemplate();
		List list = getSession().createQuery(IS_ONLINE_FILENAME_PERSISTED)
			.setString("filename", filename)
			.setLong("mcContentId", mcContentId.longValue())
			.list();

		if(list != null && list.size() > 0){
			Iterator listIterator=list.iterator();
	    	while (listIterator.hasNext())
	    	{
	    		VoteUploadedFile mcFile=(VoteUploadedFile)listIterator.next();
				this.getSession().setFlushMode(FlushMode.AUTO);
	    		templ.delete(mcFile);
	    		templ.flush();
	    	}
		}
	}
    
    public boolean isOffLineFilePersisted(String filename)
    {
    	HibernateTemplate templ = this.getHibernateTemplate();
		List list = getSession().createQuery(IS_OFFLINE_FILENAME_PERSISTED)
			.setString("filename", filename)
			.list();

		if (list != null && list.size() > 0)
		{
			return true;
		}
		return false;
    }


    public boolean isOnLineFilePersisted(String filename)
    {
    	HibernateTemplate templ = this.getHibernateTemplate();
		List list = getSession().createQuery(IS_ONLINE_FILENAME_PERSISTED)
			.setString("filename", filename)
			.list();

		if (list != null && list.size() > 0)
		{
			return true;
		}
		return false;
    }

    
    public List getOnlineFilesMetaData(Long mcContentId)
    {
    	HibernateTemplate templ = this.getHibernateTemplate();
		List list = getSession().createQuery(GET_ONLINE_FILES_METADATA)
			.setLong("mcContentId", mcContentId.longValue())
			.list();

		return list;
    }
    

    public List getOfflineFilesMetaData(Long mcContentId)
    {
    	HibernateTemplate templ = this.getHibernateTemplate();
		List list = getSession().createQuery(GET_OFFLINE_FILES_METADATA)
			.setLong("mcContentId", mcContentId.longValue())
			.list();

		return list;
    }
    
    
    public boolean isUuidPersisted(String uuid)
    {
      HibernateTemplate templ = this.getHibernateTemplate();
      List list = getSession().createQuery(IS_UUID_PERSISTED)
			.setString("uuid", uuid)
			.list();
      
      if (list != null && list.size() > 0)
      {
      	return true;
      }
      return false;
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
	    		VoteUploadedFile mcFile=(VoteUploadedFile)listIterator.next();
				this.getSession().setFlushMode(FlushMode.AUTO);
	    		templ.delete(mcFile);
	    		templ.flush();
	    	}
		}
    }

    
    public String getFileUuid(String filename)
    {
      HibernateTemplate templ = this.getHibernateTemplate();
      List list = getSession().createQuery(GET_FILES_UUID)
			.setString("filename", filename)
			.list();
      
      if (list != null && list.size() > 0){
      	Iterator listIterator=list.iterator();
    	while (listIterator.hasNext())
    	{
    		String uuid=(String)listIterator.next();
    		logger.debug("uuid :" + uuid);
			return uuid; 
		}
      }
      else
      {
      	return null;	
      }
      return null;
	}

    
    public void removeUploadFile(Long uid)
    {
    	if (uid != null ) {
    		
    		String query = "from uploadedFile in class org.lamsfoundation.lams.tool.mc.VoteUploadedFile"
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
    
    
    public void deleteUploadFile(VoteUploadedFile mcUploadedFile)
    {
    		this.getSession().setFlushMode(FlushMode.AUTO);
            this.getHibernateTemplate().delete(mcUploadedFile);
	}
    
    public void flush()
    {
        this.getHibernateTemplate().flush();
    }
} 