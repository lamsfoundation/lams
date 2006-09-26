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
package org.lamsfoundation.lams.tool.vote.dao;

import java.util.List;

import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteUploadedFile;

/**
 * @author Ozgur Demirtas
 * 
 * <p> Interface that defines the contract for IVoteUploadedFile access </p>
 */

public interface IVoteUploadedFileDAO
{
	 	public VoteUploadedFile loadUploadedFileById(long submissionId);

	 	public void updateUploadFile(VoteUploadedFile voteUploadedFile);
	 	
	    public void saveUploadFile(VoteUploadedFile voteUploadedFile);
	    
	    public void createUploadFile(VoteUploadedFile voteUploadedFile); 
	    
	    public void UpdateUploadFile(VoteUploadedFile voteUploadedFile);

	    public void removeUploadFile(Long submissionId);
	    
	    public void deleteUploadFile(VoteUploadedFile voteUploadedFile);
        
        public List retrieveVoteUploadedFiles(VoteContent vote);
	    
	    public void cleanUploadedFilesMetaData();
	    
	    public void flush();
}
