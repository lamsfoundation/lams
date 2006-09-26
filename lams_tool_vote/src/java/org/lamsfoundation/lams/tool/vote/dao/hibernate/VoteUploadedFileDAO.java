/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
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
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteUploadedFile;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;


/**
 * @author ozgurd
 * <p>Hibernate implementation for database access to VoteUploadedFile for the voting tool.</p>
 */


public class VoteUploadedFileDAO extends HibernateDaoSupport implements IVoteUploadedFileDAO {
 	static Logger logger = Logger.getLogger(VoteUploadedFileDAO.class.getName());

 	
    private static final String GET_UPLOADED_FILES = "from VoteUploadedFile voteUploadedFile where voteUploadedFile.voteContent.voteContentId = :contentId";
    
 	private static final String DELETE_FILES_META_DATA = "from voteUploadedFile in class VoteUploadedFile";
 	
 	public VoteUploadedFile getUploadedFileById(long submissionId)
    {
        return (VoteUploadedFile) this.getHibernateTemplate()
                                   .load(VoteUploadedFile.class, new Long(submissionId));
    }
 	
 	/**
 	 * 
 	 * return null if not found
 	 */
 	public VoteUploadedFile loadUploadedFileById(long submissionId)
    {
    	return (VoteUploadedFile) this.getHibernateTemplate().get(VoteUploadedFile.class, new Long(submissionId));
    }

 	
 	
 	public void updateUploadFile(VoteUploadedFile voteUploadedFile)
    {
 		this.getSession().setFlushMode(FlushMode.AUTO);
        this.getHibernateTemplate().update(voteUploadedFile);
    }
 	
     
    public void saveUploadFile(VoteUploadedFile voteUploadedFile) 
    {
    	this.getSession().setFlushMode(FlushMode.AUTO);
    	this.getHibernateTemplate().save(voteUploadedFile);
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

    
    public void cleanUploadedFilesMetaData()
    {
		HibernateTemplate templ = this.getHibernateTemplate();
		List list = getSession().createQuery(DELETE_FILES_META_DATA)
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
    
    public void removeUploadFile(Long submissionId)
    {
    	if (submissionId != null ) {
    		
    		String query = "from uploadedFile in class org.lamsfoundation.lams.tool.vote.pojos.VoteUploadedFile"
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
    
    public List retrieveVoteUploadedFiles(VoteContent vote)
    {
      List listFilenames=null;
      	listFilenames=(getHibernateTemplate().findByNamedParam(GET_UPLOADED_FILES, "contentId", vote.getVoteContentId()));	
	  return listFilenames;	
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