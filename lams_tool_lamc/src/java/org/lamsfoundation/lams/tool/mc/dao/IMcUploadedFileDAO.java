/****************************************************************
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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $$Id$$ */
package org.lamsfoundation.lams.tool.mc.dao;

import java.util.List;

import org.lamsfoundation.lams.tool.mc.pojos.McUploadedFile;

/**
 * 
 * @author Ozgur Demirtas
 * <p>Interface for the McUploadedFile DAO, defines methods needed to access/modify mc uploadedFile content</p>
 *
 */
public interface IMcUploadedFileDAO
{
		/**
		 *  * <p>Return the persistent instance of a McUploadedFile  
		 * with the given identifier <code>submissionId</code>, returns null if not found. </p> 
		 * 
		 * @param submissionId
		 * @return McUploadedFile
		 */
	 	public McUploadedFile loadUploadedFileById(long submissionId);

		/**
		 *  * <p>updates McUploadedFile  
		 * with the given identifier <code>mcUploadedFile</code> </p> 
		 * 
		 * @param mcUploadedFile
		 * @return McUploadedFile
		 */
	 	public void updateUploadFile(McUploadedFile mcUploadedFile);

		/**
		 *  * <p>saves McUploadedFile  
		 * with the given identifier <code>mcUploadedFile</code> </p> 
		 * 
		 * @param mcUploadedFile
		 * @return 
		 */
	    public void saveUploadFile(McUploadedFile mcUploadedFile);
	    
		/**
		 *  * <p>creates McUploadedFile  
		 * with the given identifier <code>mcUploadedFile</code> </p> 
		 * 
		 * @param mcUploadedFile
		 * @return 
		 */
	    public void createUploadFile(McUploadedFile mcUploadedFile); 

		/**
		 *  * <p>Updates McUploadedFile  
		 * with the given identifier <code>mcUploadedFile</code> </p> 
		 * 
		 * @param mcUploadedFile
		 * @return 
		 */
	    public void UpdateUploadFile(McUploadedFile mcUploadedFile);

		/**
		 *  * <p>removes McUploadedFile  
		 * with the given identifier <code>mcUploadedFile</code> </p> 
		 * 
		 * @param submissionId
		 * @return 
		 */	    
	    public void removeUploadFile(Long submissionId);
		
	    /**
		 *  * <p>deletes McUploadedFile  
		 * with the given identifier <code>mcUploadedFile</code> </p> 
		 * 
		 * @param mcUploadedFile
		 * @return 
		 */	    
	    public void deleteUploadFile(McUploadedFile mcUploadedFile);
	    
	    /**
		 *  * <p>returns file's uuid 
		 * with the given identifier <code>filename</code> </p> 
		 * 
		 * @param filename
		 * @return 
		 */	    
	    public String getFileUuid(String filename);

	    /**
		 *  * <p>returns a list of McUploadedFiles
		 * with the given identifier <code>mcContentId</code> and <code>fileOnline</code>  </p> 
		 * 
		 * @param mcContentId
		 * @param fileOnline
		 * @return List 
		 */
	    public List retrieveMcUploadedFiles(Long mcContentId, boolean fileOnline);
	    
	    /**
		 *  * <p>returns a list of offline McUploadedFiles
		 * with the given identifier <code>mcContentId</code> </p> 
		 * 
		 * @param mcContentId
		 * @return List 
		 */	    
	    public List retrieveMcUploadedOfflineFilesUuid(Long mcContentId);

	    /**
		 *  * <p>returns a list of online McUploadedFiles
		 * with the given identifier <code>mcContentId</code> </p> 
		 * 
		 * @param mcContentId
		 * @return List 
		 */	    
	    public List retrieveMcUploadedOnlineFilesUuid(Long mcContentId);
	    
	    /**
		 *  * <p>returns a list of offline McUploadedFile filenames
		 * with the given identifier <code>mcContentId</code> </p> 
		 * 
		 * @param mcContentId
		 * @return List 
		 */	    
	    public List retrieveMcUploadedOfflineFilesName(Long mcContentId);
	    
	    /**
		 *  * <p>returns a list of online McUploadedFile filenames
		 * with the given identifier <code>mcContentId</code> </p> 
		 * 
		 * @param mcContentId
		 * @return List 
		 */
	    public List retrieveMcUploadedOnlineFilesName(Long mcContentId);
	    
	    /**
		 *  * <p>returns a list of offline McUploadedFile uuid and filenames
		 * with the given identifier <code>mcContentId</code> </p> 
		 * 
		 * @param mcContentId
		 * @return List 
		 */
	    public List retrieveMcUploadedOfflineFilesUuidPlusFilename(Long mcContentId);

	    /**
		 *  * <p>removes files meta data
		 * 
		 * @param mcContentId
		 * @return List 
		 */	    
	    
	    
	    public void removeOffLineFile(String filename, Long mcContentId);
	    
	    public void removeOnLineFile(String filename, Long mcContentId);
	    
	    public boolean isOffLineFilePersisted(String filename);
	    
	    public boolean isOnLineFilePersisted(String filename);
	    
	    public boolean isUuidPersisted(String uuid);
	    
	    public List getOnlineFilesMetaData(Long mcContentId);
	    
	    public List getOfflineFilesMetaData(Long mcContentId);
	    
	    public void cleanUploadedFilesMetaData();
	    
	    public void flush();
}
