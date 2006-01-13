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
package org.lamsfoundation.lams.tool.mc.pojos;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.ItemNotFoundException;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;

/**
 * <p>Persistent  object/bean that defines the uploaded file for the MCQ tool.
 * Provides accessors and mutators to get/set attributes
 * It maps to database table: tl_lamc11_uploadedFile
 * </p>
 * 
 * @author Ozgur Demirtas
 */
public class McUploadedFile implements Serializable, Comparable
{
	static Logger logger = Logger.getLogger(McUploadedFile.class.getName());
	
	/** identifier field */
    private Long uid;
    
    /** persistent field */
    private String uuid;

    /** persistent field */
    private boolean fileOnline;
    
    /** persistent field */
    private String filename;
    
    
    private Long mcContentId;
    
    /** persistent field */
    private McContent mcContent;
    
    public McUploadedFile(){};

    /** full constructor */
    public McUploadedFile(Long uid,
    					String uuid, 
    					boolean fileOnline, 
    					String filename,
						McContent mcContent)  
    {
    	this.uid=uid;
        this.uuid = uuid;
        this.fileOnline = fileOnline;
        this.filename = filename;
        this.mcContent=mcContent;
    }

    public McUploadedFile(String uuid, 
    					boolean fileOnline, 
    					String filename,
						McContent mcContent)  
    {
        this.uuid = uuid;
        this.fileOnline = fileOnline;
        this.filename = filename;
        this.mcContent=mcContent;
    }
    
    public McUploadedFile(String uuid, 
			boolean fileOnline, 
			String filename,
			Long mcContentId)  
    {
		this.uuid = uuid;
		this.fileOnline = fileOnline;
		this.filename = filename;
		this.mcContentId=mcContentId;
    }
    
    
    public static McUploadedFile newInstance(IToolContentHandler toolContentHandler, McUploadedFile mcUploadedFile,
			McContent newMcContent) throws ItemNotFoundException, RepositoryCheckedException
			
	{
    	McUploadedFile newMcUploadedFile=null;

    	try
		{
    		NodeKey copiedNodeKey =  toolContentHandler.copyFile(new Long(mcUploadedFile.getUuid()));
        	logger.debug("copied NodeKey: " + copiedNodeKey);
        	logger.debug("copied NodeKey uuid: " + copiedNodeKey.getUuid().toString());
        	newMcUploadedFile = new McUploadedFile(copiedNodeKey.getUuid().toString(),
					mcUploadedFile.isFileOnline(),
					mcUploadedFile.getFilename(),
					newMcContent);

		}
		catch(RepositoryCheckedException e)
		{
			logger.debug("error occurred: " + e);
		}
		
    	return newMcUploadedFile;
	}
    
    
    public String toString() {
        return new ToStringBuilder(this)
            .append("uuid: ", getUuid())
			.toString();
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
	 * @return Returns the mcContentId.
	 */
	public Long getMcContentId() {
		return mcContentId;
	}
	/**
	 * @param mcContentId The mcContentId to set.
	 */
	public void setMcContentId(Long mcContentId) {
		this.mcContentId = mcContentId;
	}
	/**
	 * @return Returns the uid.
	 */
	public Long getSubmissionId() {
		return uid;
	}
	/**
	 * @param uid The uid to set.
	 */
	public void setSubmissionId(Long uid) {
		this.uid = uid;
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
	/**
	 * @return Returns the uid.
	 */
	public Long getUid() {
		return uid;
	}
	/**
	 * @param uid The uid to set.
	 */
	public void setUid(Long uid) {
		this.uid = uid;
	}
	/**
	 * @return Returns the filename.
	 */
	public String getFilename() {
		return filename;
	}
	/**
	 * @param filename The filename to set.
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public int compareTo(Object o)
    {
		McUploadedFile optContent = (McUploadedFile) o;
        //if the object does not exist yet, then just return any one of 0, -1, 1. Should not make a difference.
        if (uid == null)
        	return 1;
		else
			return (int) (uid.longValue() - optContent.uid.longValue());
    }
}
