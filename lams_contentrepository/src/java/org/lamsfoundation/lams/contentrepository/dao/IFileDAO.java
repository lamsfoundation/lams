/*
 * Created on Jan 10, 2005
 */
package org.lamsfoundation.lams.contentrepository.dao;

import java.io.InputStream;

import org.lamsfoundation.lams.contentrepository.FileException;


/**
 * Manages the reading and writing of files to the repository directories.
 * 
 * @author Fiona Malikoff
 */
public interface IFileDAO {

	/** 
	 * Write out a file to the repository. Closes the stream.
	 * @return the path to which the file was written
	 */ 
	public String writeFile(Long uuid, Long versionId, InputStream is) 
						throws FileException;

	/** 
	 * Gets a file from the repository. 
	 */ 
	public InputStream getFile(Long uuid, Long versionId) 
						throws FileException;

	/** Delete a file from the repository. If this makes the directory
	 * empty, then the directory should be deleted. 
	 * @returns 1 if deleted okay, 0 if file not found, -1 if file found but a delete error occured.
	 */ 
	public int delete(Long uuid, Long versionId)
						throws FileException;
	
	/** 
	 * Get the actual path of the file ie the path on disk 
	 */ 
	public String getFilePath(Long uuid, Long versionId) 
						throws FileException;

}