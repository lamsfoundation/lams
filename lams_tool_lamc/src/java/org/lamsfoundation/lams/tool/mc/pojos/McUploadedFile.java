/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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
 * ****************************************************************
 */
/* $$Id$$ */	
package org.lamsfoundation.lams.tool.mc.pojos;

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
 * 
 * @author Ozgur Demirtas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * McQueContent Value Object
 * The value object that maps to our model database table: tl_lamc11_que_content
 * The relevant hibernate mapping resides in: McQueContent.hbm.xml
 *
 * Holds question content within a particular content  
 */

public class McUploadedFile implements Serializable, Comparable
{
	static Logger logger = Logger.getLogger(McUploadedFile.class.getName());
	/** identifier field */
    private Long submissionId;
    
    /** persistent field */
    private String uuid;

    /** persistent field */
    private boolean fileOnline;
    
    /** persistent field */
    private String fileName;
    
    /** persistent field */
    private McContent mcContent;
    
    public McUploadedFile(){};

    /** full constructor */
    public McUploadedFile(Long submissionId,
    					String uuid, 
    					boolean fileOnline, 
    					String fileName,
						McContent mcContent)  
    {
    	this.submissionId=submissionId;
        this.uuid = uuid;
        this.fileOnline = fileOnline;
        this.fileName = fileName;
        this.mcContent=mcContent;
    }
    
    
    public static McUploadedFile newInstance(IToolContentHandler toolContentHandler, McUploadedFile mcUploadedFile,
			McContent newMcContent) throws ItemNotFoundException, RepositoryCheckedException
			
	{
    	McUploadedFile newMcUploadedFile=null;

    	try
		{
    		String fileUuid = mcUploadedFile.getUuid(); 
    		if(toolContentHandler != null){
	    		NodeKey copiedNodeKey =  toolContentHandler.copyFile(new Long(mcUploadedFile.getUuid()));
	        	logger.debug("copied NodeKey: " + copiedNodeKey);
	        	logger.debug("copied NodeKey uuid: " + copiedNodeKey.getUuid().toString());
	        	fileUuid = copiedNodeKey.getUuid().toString();
    		}
        	newMcUploadedFile = new McUploadedFile(fileUuid,
        			mcUploadedFile.isFileOnline(),
        			mcUploadedFile.getFileName(),
					newMcContent);

		}
		catch(RepositoryCheckedException e)
		{
			logger.debug("error occurred: " + e);
		}
		
    	return newMcUploadedFile;
	}
    
    

    public McUploadedFile(String uuid, 
    					boolean fileOnline, 
    					String fileName,
						McContent mcContent)  
    {
    	logger.debug("constructor gets called.");
        this.uuid = uuid;
        this.fileOnline = fileOnline;
        this.fileName = fileName;
        this.mcContent=mcContent;
    }
    
    
    public String toString() {
        return new ToStringBuilder(this)
            .append("uuid: ", getUuid())
			.append("fileName: ", getFileName())
			.toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof McUploadedFile) ) return false;
        McUploadedFile castOther = (McUploadedFile) other;
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
	 * @return Returns the mcContent.
	 */
	public McContent getMcContent() {
		return mcContent;
	}
	/**
	 * @param mcContent The mcContent to set.
	 */
	public void setMcContent(McContent mcContent) {
		this.mcContent = mcContent;
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
		McUploadedFile file = (McUploadedFile) o;
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
