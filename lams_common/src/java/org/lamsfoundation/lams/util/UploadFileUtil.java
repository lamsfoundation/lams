/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * =============================================================
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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.util;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.log4j.Logger;


/**
 * Wraps up interface to the Commons FileUpload as much as possible. Reads configuration 
 * information such the maximum size of a file from lams.xml using the ConfigurationService.
 * 
 * @author fmalikoff
 * 			Fei Yang
 * 
 */
public class UploadFileUtil {
	
	private static Logger log = Logger.getLogger(UploadFileUtil.class);

	private static final int DEFAULT_MAX_SIZE = 5000000; // 5 Meg
	private static final int DEFAULT_MEMORY_SIZE = 4096;
	
	/** Given the current servlet request and the name of a temporary directory,
	 * get a list of all the uploaded items from a multipart form. This includes any 
	 * uploaded files and the "normal" input fields on the form. Gets the temporary 
	 * directory from the configuration file. Equivalent to getUploadItems(request, useLargeFileSize, null)
	 * @param request - current servlet request
	 * @param useLargeFileSize - use the large file size. If not true, use the standard file size.
	 * @return List of items, of type FileItem
	 */
	public static List getUploadItems(HttpServletRequest request, boolean useLargeFileSize )
		throws FileUploadException, Exception
	{
		return getUploadItems(request,useLargeFileSize,null);
	}

	/** Given the current servlet request and the name of a temporary directory,
	 * get a list of all the uploaded items from a multipart form. This includes any 
	 * uploaded files and the "normal" input fields on the form.
	 * @param request - current servlet request
	 * @param useLargeFileSize - use the large file size. If not true, use the standard file size.
	 * @param tempDirName - the name of the directory into which temporary files can be written
	 * @return List of items, of type FileItem
	 */
	public static List getUploadItems(HttpServletRequest request, boolean useLargeFileSize, String tempDirNameInput)
		throws FileUploadException, Exception
	{
	
		int max_size = DEFAULT_MAX_SIZE;
		int max_memory_size;
		String tempDirName = null;

		int tempInt=-1;
		
		if ( useLargeFileSize )
		{
			tempInt = Configuration.getAsInt(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE);
			if(tempInt!=-1)
			{
				max_size = tempInt;
			}
			else
			{
				log.warn("Default Large Max Size for file upload missing, using "+DEFAULT_MAX_SIZE);
			}	
		}
		else
		{
			tempInt = Configuration.getAsInt(ConfigurationKeys.UPLOAD_FILE_MAX_SIZE);
			if ( tempInt != -1 )
			{
				max_size = tempInt;
			}
			else
			{
				log.warn("Default Max Size for file upload missing, using "+DEFAULT_MAX_SIZE);
			}	
		}

		tempInt = Configuration.getAsInt(ConfigurationKeys.UPLOAD_FILE_MAX_MEMORY_SIZE);
		if ( tempInt != -1 )
		{
			max_memory_size = tempInt;
		}
		else
		{
			log.warn("Default Max Memory Size for file upload missing, using "+DEFAULT_MEMORY_SIZE);
			max_memory_size = DEFAULT_MEMORY_SIZE;
		}	
		
		if ( tempDirNameInput != null )
		{
			tempDirName = tempDirNameInput;
		}
		else
		{
			tempDirName = Configuration.get(ConfigurationKeys.LAMS_TEMP_DIR);
			if ( tempDirName == null )
			{
				log.warn("Default Temporary Directory missing, using null");
			}	
		}
		// would be nice to only do this once! never mind.
		if ( tempDirName != null )
		{
			File dir = new File(tempDirName);
			if ( ! dir.exists() )
				dir.mkdirs();
		}
		

	    // Create a new file upload handler
	    DiskFileUpload upload = new DiskFileUpload();
	
	    // Set upload parameters
	    upload.setSizeMax(max_size);
	    upload.setSizeThreshold(max_memory_size);
	    upload.setRepositoryPath(tempDirName);
	
	    // Parse the request
	    List items = upload.parseRequest(request);
	    return items;
	}
	

	/** 
	 * @return Maximum size (in megabytes) that a file may be. Standard size, use during running
	 * of sequence.
	 */
	public static float getMaxFileSize()
	{
		int max_size = DEFAULT_MAX_SIZE;
		
		int tempInt = Configuration.getAsInt(ConfigurationKeys.UPLOAD_FILE_MAX_SIZE);
		if ( tempInt != -1 )
		{
			max_size = tempInt;
		}

		return ( max_size != 0 ? (float) max_size / 1024 / 1024 : (float) 0 );		
	}

	/** 
	 * @return Maximum size (in megabytes) that a file may be. Large size, use during 
	 * authoring only.
	 */
	public static float getMaxLargeFileSize()
	{
		int max_size = DEFAULT_MAX_SIZE;
		
		int tempInt = Configuration.getAsInt(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE);
		if ( tempInt != -1 )
		{
			max_size = tempInt;
		}

		return ( max_size != 0 ? (float) max_size / 1024 / 1024 : (float) 0 );		
	}

}
