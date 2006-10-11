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

package org.lamsfoundation.lams.tool.vote.pojos;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.ItemNotFoundException;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;

/**
 * <p>Persistent  object/bean that defines the uploaded file for the MCQ tool.
 * Provides accessors and mutators to get/set attributes
 * It maps to database table: tl_lavote11_uploadedfile
 * </p>
 * 
 * @author Ozgur Demirtas
 */
public class VoteUploadedFile implements Serializable, Comparable
{
	static Logger logger = Logger.getLogger(VoteUploadedFile.class.getName());
	/** identifier field */
    private Long submissionId;
    
    /** persistent field */
    private String uuid;

    /** persistent field */
    private boolean fileOnline;
    
    /** persistent field */
    private String fileName;
    
    /** persistent field */
    private VoteContent voteContent;
    
    public VoteUploadedFile(){};

    /** full constructor */
    public VoteUploadedFile(Long submissionId,
    					String uuid, 
    					boolean fileOnline, 
    					String fileName,
						VoteContent voteContent)  
    {
    	this.submissionId=submissionId;
        this.uuid = uuid;
        this.fileOnline = fileOnline;
        this.fileName = fileName;
        this.voteContent=voteContent;
    }
    
    
    public static VoteUploadedFile newInstance(IToolContentHandler toolContentHandler, VoteUploadedFile voteUploadedFile,
			VoteContent newMcContent) throws ItemNotFoundException, RepositoryCheckedException
			
	{
    	VoteUploadedFile newMcUploadedFile=null;

        newMcUploadedFile = new VoteUploadedFile(voteUploadedFile.getUuid(),
        			voteUploadedFile.isFileOnline(),
        			voteUploadedFile.getFileName(),
					newMcContent);

		
    	return newMcUploadedFile;
	}
    
    

    public VoteUploadedFile(String uuid, 
    					boolean fileOnline, 
    					String fileName,
						VoteContent voteContent)  
    {
    	logger.debug("constructor gets called.");
        this.uuid = uuid;
        this.fileOnline = fileOnline;
        this.fileName = fileName;
        this.voteContent=voteContent;
    }
    
    
    public String toString() {
        return new ToStringBuilder(this)
            .append("uuid: ", getUuid())
			.append("fileName: ", getFileName())
			.toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof VoteUploadedFile) ) return false;
        VoteUploadedFile castOther = (VoteUploadedFile) other;
        return new EqualsBuilder()
            .append(this.getUuid(), castOther.getUuid())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getUuid())
            .toHashCode();
    }
  
    
        /**
	 * @return Returns the fileName.
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * @param fileName The fileName to set.
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	/**
	 * @return Returns the voteContent.
	 */
	public VoteContent getVoteContent() {
		return voteContent;
	}
	/**
	 * @param voteContent The voteContent to set.
	 */
	public void setVoteContent(VoteContent voteContent) {
		this.voteContent = voteContent;
	}

	/**
	 * @return Returns the submissionId.
	 */
	public Long getSubmissionId() {
		return submissionId;
	}
	/**
	 * @param submissionId The submissionId to set.
	 */
	public void setSubmissionId(Long submissionId) {
		this.submissionId = submissionId;
	}
	/**
	 * @return Returns the uuid.
	 */
	public String getUuid() {
		return uuid;
	}
	/**
	 * @param uuid The uuid to set.
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	/**
	 * @return Returns the fileOnline.
	 */
	public boolean isFileOnline() {
		return fileOnline;
	}
	/**
	 * @param fileOnline The fileOnline to set.
	 */
	public void setFileOnline(boolean fileOnline) {
		this.fileOnline = fileOnline;
	}
	
	public int compareTo(Object o)
    {
		VoteUploadedFile file = (VoteUploadedFile) o;
        //if the object does not exist yet, then just return any one of 0, -1, 1. Should not make a difference.
        if (submissionId == null)
        	return 1;
		else
			return (int) (submissionId.longValue() - file.submissionId.longValue());
    }
	public String getFileProperty() {
		   if (isFileOnline())
	        {
	            return IToolContentHandler.TYPE_ONLINE;
	        }
	        else
	            return IToolContentHandler.TYPE_OFFLINE;
	}

	public void setFileProperty(String fileProperty) {
		if(StringUtils.equals(IToolContentHandler.TYPE_ONLINE,fileProperty))
			this.fileOnline = true;
		else
			this.fileOnline = false;
	}	
}
