/* 
  Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation; either version 2 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
  USA

  http://www.gnu.org/licenses/gpl.txt 
*/

package org.lamsfoundation.lams.contentrepository.dao.file;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.FileException;
import org.lamsfoundation.lams.contentrepository.dao.IFileDAO;


/**
 * Manages the reading and writing of files to the repository directories.
 * Note: this does not involve the database - so no Hibernate!
 * 
 * @author Fiona Malikoff
 */
public class FileDAO implements IFileDAO {

	private String repositoryLocation;
	protected Logger log = Logger.getLogger(FileDAO.class);

	/** 
	 * @return array of two strings - the first is the directory, the second is the 
	 * file path.
	 */
	protected String[] generateFilePath(Long uuid, Long versionId) 
						throws FileException  {

		if ( repositoryLocation == null )
			throw new FileException("Repository location unknown. Cannot access files. This should have been configured in the Spring context.");

		String directoryPath = repositoryLocation;
		
		if ( uuid == null || uuid.longValue() < 1 )
			throw new FileException("Unable to generate new filename for uuid="+uuid);
		
		String uuidString = uuid.toString();
		if ( uuidString.length() % 2 != 0 ) {
			uuidString = "0"+uuidString;
		}
		for ( int i=0; i<uuidString.length(); i+=2 ) {
			directoryPath = directoryPath + File.separator + uuidString.charAt(i) + uuidString.charAt(i+1); 
		}
		
		return new String[] {directoryPath, directoryPath+File.separator+versionId};
	}
	
	// TODO can the writing out of the file be made more efficient
	// with a larger buffer
	/** 
	 * Write out a file to the repository. Closes the stream.
	 * @return the path to which the file was written
	 */ 
	public String writeFile(Long uuid, Long versionId, InputStream is) 
						throws FileException {
		
		String paths[] = generateFilePath(uuid, versionId);
		
		boolean writeErrorOccured = false;

		// check that the file doesn't already exist
		File file = new File(paths[1]);
		if ( file.exists() ) {

			String msg = "File for uuid "+uuid+" versionId "+versionId
				+" already exists. Generated path was "+paths[1];
			  
			log.error(msg
				+". If this occurs again, it could be that there is a file on"
				+" disk but there is no record in the database. To check"
				+" if a record exists in the database, run the sql\n"
				+" \"select * from lams_cr_node node, lams_cr_node_version nv"
				+" where node.node_id = "+uuid
				+" and node.node_id = nv.node_id"
				+" and nv.version_id = "+versionId
				+"\". \nIf no rows are found, then move the problem file over to "
				+" a storage area (just in case it needs to be restored later)."
				+" If this problem re-occurs, then a bug report to LAMS should be submitted.");

			throw new FileException("File for uuid "+uuid+" versionId "+versionId
				+" already exists. Generated path was "+paths[1]
				+". See log file for more help with this problem.");
		}
		
		// check that the directory path exists and create if necessary
		File dir  = new File(paths[0]); 
		dir.mkdirs(); 
		
		OutputStream os = null;
		try {
			
			if ( log.isDebugEnabled() ) {
				log.debug("Writing out file to "+file.getAbsolutePath());
			}
			
			int bufLen = 1024; // 1 Kbyte
			byte[] buf = new byte[1024]; // output buffer
			os = new FileOutputStream(file);

			BufferedInputStream in = new BufferedInputStream(is);
			int len = 0;
		    while((len = in.read(buf,0,bufLen)) != -1){
		    	os.write(buf,0,len);
		    }	
			
		} catch ( IOException e ) {
			String message = "Unable to write file for uuid "+uuid+" versionId "+versionId
				+" due to an IOException. Generated path was "	
				+paths[1];
			log.error(message,e);
			throw new FileException(message, e);
			
		} finally {

			try {
				if ( is != null )
					is.close();
			} catch (IOException e1) {
				String message = "Unable to close supplied filestream due to IOException";
				log.error(message,e1);
				new FileException(message, e1);
			}

			try {
				if ( os != null )
					os.close();
			} catch (IOException e2) {
				String message = "Unable to close file in repository due to IOException";
				log.error(message,e2);
				new FileException(message, e2);
			}
		}
		
		return paths != null ? paths[0] : null;
	}
	
	/** 
	 * Gets a file from the repository. 
	 * @param uuid node id
	 * @param versionId version id
	 */ 
	public InputStream getFile(Long uuid, Long versionId) 
						throws FileException {
		
		String paths[] = generateFilePath(uuid, versionId);
		try {
			return new FileInputStream(paths[1]);
		} catch ( IOException e ) {
			e.printStackTrace();
			throw new FileException("Unable to read file for uuid "+uuid+" versionId "+versionId
					+" due to an IOException. Generated path was "+paths[1],e);
		}
	}

	/** Delete a file from the repository. If this makes the directory
	 * empty, then the directory should be deleted. 
	 * @returns 1 if deleted okay, 0 if file not found, -1 if file found but a delete error occured.
	 */ 
	public int delete(Long uuid, Long versionId)
						throws FileException {
		
		String paths[] = generateFilePath(uuid, versionId);
		int retValue = 0;
		
		// check the file exists before we try to delete it!
		File file = new File(paths[1]);
		if ( file.exists() ) {
			retValue = file.delete() ? 1 : -1;
		} 
		
		switch (retValue) {
			case 0: log.error("Unable to delete file "+file.getPath()+" as the file does not exist.");
					break;
			case -1: log.error("Unable to delete file "+file.getPath()+". File does exist but can't be deleted for some (unknown) reason.");
					break;
		}  

		// is the directory empty? if so, delete
		File dir  = new File(paths[0]);
		String[] files = dir.list();
		if ( files != null && files.length == 0 ) {
			dir.delete();
		}
		
		return retValue;
		
	}
						
	/** 
	 * Get the actual path of the file ie the path on disk 
	 * @throws FileException
	 */ 
	public String getFilePath(Long uuid, Long versionId) throws FileException {
		String paths[] = generateFilePath(uuid, versionId);
		return paths[1];
	}

	/**
	 * @return Returns the repositoryLocation.
	 */
	public String getRepositoryLocation() {
		return repositoryLocation;
	}
	/**
	 * @param repositoryLocation The repositoryLocation to set.
	 */
	public void setRepositoryLocation(String repositoryLocation) {
		this.repositoryLocation = repositoryLocation;
	}
}
