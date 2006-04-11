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
 * <p>Hibernate implementation for database access to VoteUploadedFile for the voting tool.</p>
 */
public class VoteUploadedFileDAO extends HibernateDaoSupport implements IVoteUploadedFileDAO {
 	static Logger logger = Logger.getLogger(VoteUploadedFileDAO.class.getName());
 	
 	private static final String GET_ONLINE_FILENAMES_FOR_CONTENT = "select voteUploadedFile.filename from VoteUploadedFile voteUploadedFile where voteUploadedFile.voteContentId = :voteContentId and voteUploadedFile.fileOnline=1";
 	private static final String GET_OFFLINE_FILENAMES_FOR_CONTENT = "select voteUploadedFile.filename from VoteUploadedFile voteUploadedFile where voteUploadedFile.voteContentId = :voteContentId and voteUploadedFile.fileOnline=0";
 	
 	private static final String GET_ONLINE_FILES_UUID = "select voteUploadedFile.uuid from VoteUploadedFile voteUploadedFile where voteUploadedFile.voteContentId = :voteContentId and voteUploadedFile.fileOnline=1";
 	private static final String GET_ONLINE_FILES_NAME ="select voteUploadedFile.filename from VoteUploadedFile voteUploadedFile where voteUploadedFile.voteContentId = :voteContentId and voteUploadedFile.fileOnline=1 order by voteUploadedFile.uuid";
 	
 	private static final String GET_OFFLINE_FILES_UUID = "select voteUploadedFile.uuid from VoteUploadedFile voteUploadedFile where voteUploadedFile.voteContentId = :voteContentId and voteUploadedFile.fileOnline=0";
 	private static final String GET_OFFLINE_FILES_NAME ="select voteUploadedFile.filename from VoteUploadedFile voteUploadedFile where voteUploadedFile.voteContentId = :voteContentId and voteUploadedFile.fileOnline=0 order by voteUploadedFile.uuid";
 	
 	private static final String GET_FILES_UUID ="select voteUploadedFile.uuid from VoteUploadedFile voteUploadedFile where voteUploadedFile.filename=:filename";
 	
 	private static final String GET_OFFLINE_FILES_UUIDPLUSFILENAME = "select (voteUploadedFile.uuid + '~' + voteUploadedFile.filename)   from VoteUploadedFile voteUploadedFile where voteUploadedFile.voteContentId = :voteContentId and voteUploadedFile.fileOnline=0";
 	
 	private static final String FIND_ALL_UPLOADED_FILE_DATA = "from voteUploadedFile in class VoteUploadedFile";
 	
 	private static final String IS_UUID_PERSISTED ="select voteUploadedFile.uuid from VoteUploadedFile voteUploadedFile where voteUploadedFile.uuid=:uuid";
 	
 	private static final String GET_ONLINE_FILES_METADATA = "from voteUploadedFile in class VoteUploadedFile where voteUploadedFile.voteContentId = :voteContentId and voteUploadedFile.fileOnline=1";
 	
 	private static final String GET_OFFLINE_FILES_METADATA = "from voteUploadedFile in class VoteUploadedFile where voteUploadedFile.voteContentId = :voteContentId and voteUploadedFile.fileOnline=0";
 	
 	private static final String IS_OFFLINE_FILENAME_PERSISTED ="select voteUploadedFile from VoteUploadedFile voteUploadedFile where voteUploadedFile.voteContentId = :voteContentId and voteUploadedFile.filename=:filename and voteUploadedFile.fileOnline=0";
 	
 	private static final String IS_ONLINE_FILENAME_PERSISTED ="select voteUploadedFile from VoteUploadedFile voteUploadedFile where voteUploadedFile.voteContentId = :voteContentId and voteUploadedFile.filename=:filename and voteUploadedFile.fileOnline=1";
 	
 	
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

 	
 	
 	public void updateUploadFile(VoteUploadedFile voteUploadedFile)
    {
 		this.getSession().setFlushMode(FlushMode.AUTO);
        this.getHibernateTemplate().update(voteUploadedFile);
		this.getSession().setFlushMode(FlushMode.AUTO);
    }
 	
     
    public void saveUploadFile(VoteUploadedFile voteUploadedFile) 
    {
    	this.getSession().setFlushMode(FlushMode.AUTO);
    	this.getHibernateTemplate().save(voteUploadedFile);
		this.getSession().setFlushMode(FlushMode.AUTO);
    }
    
    public void createUploadFile(VoteUploadedFile voteUploadedFile) 
    {
    	this.getSession().setFlushMode(FlushMode.AUTO);
    	this.getHibernateTemplate().save(voteUploadedFile);
    }
    
    public void UpdateUploadFile(VoteUploadedFile voteUploadedFile)
    {
    	this.getSession().setFlushMode(FlushMode.AUTO);
    	this.getHibernateTemplate().update(voteUploadedFile);	
    }

    
    public void removeOffLineFile(String filename, Long voteContentId)
    {
    	HibernateTemplate templ = this.getHibernateTemplate();
		List list = getSession().createQuery(IS_OFFLINE_FILENAME_PERSISTED)
			.setString("filename", filename)
			.setLong("voteContentId", voteContentId.longValue())
			.list();
		
			if(list != null && list.size() > 0){
			Iterator listIterator=list.iterator();
	    	while (listIterator.hasNext())
	    	{
	    		VoteUploadedFile voteFile=(VoteUploadedFile)listIterator.next();
				this.getSession().setFlushMode(FlushMode.AUTO);
	    		templ.delete(voteFile);
	    		templ.flush();
	    	}
		}
    }

    public void removeOnLineFile(String filename, Long voteContentId)
    {
    	HibernateTemplate templ = this.getHibernateTemplate();
		List list = getSession().createQuery(IS_ONLINE_FILENAME_PERSISTED)
			.setString("filename", filename)
			.setLong("voteContentId", voteContentId.longValue())
			.list();

		if(list != null && list.size() > 0){
			Iterator listIterator=list.iterator();
	    	while (listIterator.hasNext())
	    	{
	    		VoteUploadedFile voteFile=(VoteUploadedFile)listIterator.next();
				this.getSession().setFlushMode(FlushMode.AUTO);
	    		templ.delete(voteFile);
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

    
    public List getOnlineFilesMetaData(Long voteContentId)
    {
    	HibernateTemplate templ = this.getHibernateTemplate();
		List list = getSession().createQuery(GET_ONLINE_FILES_METADATA)
			.setLong("voteContentId", voteContentId.longValue())
			.list();

		return list;
    }
    

    public List getOfflineFilesMetaData(Long voteContentId)
    {
    	HibernateTemplate templ = this.getHibernateTemplate();
		List list = getSession().createQuery(GET_OFFLINE_FILES_METADATA)
			.setLong("voteContentId", voteContentId.longValue())
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
	    		VoteUploadedFile voteFile=(VoteUploadedFile)listIterator.next();
				this.getSession().setFlushMode(FlushMode.AUTO);
	    		templ.delete(voteFile);
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
    		
    		String query = "from uploadedFile in class org.lamsfoundation.lams.tool.vote.VoteUploadedFile"
            + " where uploadedFile.uid = ?";
    		Object obj = this.getSession().createQuery(query)
				.setLong(0,uid.longValue())
				.uniqueResult();
    		if ( obj != null ) { 
    			this.getHibernateTemplate().delete(obj);
    		}
    	}
	}
    
    public List retrieveVoteUploadedFiles(Long voteContentId, boolean fileOnline)
    {
      List listFilenames=null;
    	
      if (fileOnline)
      {
      	listFilenames=(getHibernateTemplate().findByNamedParam(GET_ONLINE_FILENAMES_FOR_CONTENT,
                "voteContentId",
				voteContentId));	
      }
      else
      {
      	listFilenames=(getHibernateTemplate().findByNamedParam(GET_OFFLINE_FILENAMES_FOR_CONTENT,
                "voteContentId",
				voteContentId));  	
      }
	  return listFilenames;	
    }
    
    
    public List retrieveVoteUploadedOfflineFilesUuid(Long voteContentId)
    {
      HibernateTemplate templ = this.getHibernateTemplate();
      List list = getSession().createQuery(GET_OFFLINE_FILES_UUID)
			.setLong("voteContentId", voteContentId.longValue())
			.list();
      
      
	  return list;
    }
    
    public List retrieveVoteUploadedOnlineFilesUuid(Long voteContentId)
    {
      HibernateTemplate templ = this.getHibernateTemplate();
      List list = getSession().createQuery(GET_ONLINE_FILES_UUID)
			.setLong("voteContentId", voteContentId.longValue())
			.list();
      
	  return list;
    }
    
    public List retrieveVoteUploadedOfflineFilesUuidPlusFilename(Long voteContentId)
    {
      HibernateTemplate templ = this.getHibernateTemplate();
      List list = getSession().createQuery(GET_OFFLINE_FILES_UUIDPLUSFILENAME)
			.setLong("voteContentId", voteContentId.longValue())
			.list();
      
	  return list;
    }
    
    
    public List retrieveVoteUploadedOfflineFilesName(Long voteContentId)
    {
      HibernateTemplate templ = this.getHibernateTemplate();
      List list = getSession().createQuery(GET_OFFLINE_FILES_NAME)
			.setLong("voteContentId", voteContentId.longValue())
			.list();
      
	  return list;
    }

    
    public List retrieveVoteUploadedOnlineFilesName(Long voteContentId)
    {
      HibernateTemplate templ = this.getHibernateTemplate();
      List list = getSession().createQuery(GET_ONLINE_FILES_NAME)
			.setLong("voteContentId", voteContentId.longValue())
			.list();
      
	  return list;
    }
    
    
    public void deleteUploadFile(VoteUploadedFile voteUploadedFile)
    {
    		this.getSession().setFlushMode(FlushMode.AUTO);
            this.getHibernateTemplate().delete(voteUploadedFile);
	}
    
    public void flush()
    {
        this.getHibernateTemplate().flush();
    }
} 