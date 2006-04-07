/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */
package org.lamsfoundation.lams.tool.vote.dao;

import java.util.List;

import org.lamsfoundation.lams.tool.vote.pojos.VoteUploadedFile;

/**
 * 
 * @author Ozgur Demirtas
 * <p></p>
 *
 */
public interface IVoteUploadedFileDAO
{
	 	public VoteUploadedFile loadUploadedFileById(long submissionId);

	 	public void updateUploadFile(VoteUploadedFile mcUploadedFile);

	    public void saveUploadFile(VoteUploadedFile mcUploadedFile);

	    public void createUploadFile(VoteUploadedFile mcUploadedFile); 

	    public void UpdateUploadFile(VoteUploadedFile mcUploadedFile);

	    public void removeUploadFile(Long submissionId);

	    public void deleteUploadFile(VoteUploadedFile mcUploadedFile);
	    
	    public String getFileUuid(String filename);

	    public List retrieveMcUploadedFiles(Long mcContentId, boolean fileOnline);
	    
	    public List retrieveMcUploadedOfflineFilesUuid(Long mcContentId);

	    public List retrieveMcUploadedOnlineFilesUuid(Long mcContentId);
	    
	    public List retrieveMcUploadedOfflineFilesName(Long mcContentId);
	    
	    public List retrieveMcUploadedOnlineFilesName(Long mcContentId);

	    public List retrieveMcUploadedOfflineFilesUuidPlusFilename(Long mcContentId);

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
