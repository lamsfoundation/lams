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
package org.lamsfoundation.lams.util;

import java.io.File;

import org.apache.log4j.Logger;

/**
 * General File Utilities
 */
public class FileUtil {

	private static Logger log = Logger.getLogger(FileUtil.class);

	/** 
	 * Deleting a directory using File.delete() only works if
	 * the directory is empty. This method deletes a directory and 
	 * all of its contained files.
	 * 
	 * This method is not transactional - if it fails to delete some
	 * contained files or directories, it will continue deleting all 
	 * the other files in the directory. If only a partial deletion
	 * is done, then the files and directories that could not be
	 * deleted are listed in the log file, and the method returns false.
	 * 
	 * This method has not been tested in Linux or Unix systems, 
	 * so the behaviour across symbolic links is unknown.
	 */
	public static boolean deleteDirectory(File directory) {
	    boolean retValue = true;

		File[] files = directory.listFiles();
		if ( files != null ) {
			for ( int i=0; i<files.length; i++ ) {
				File file = files[i];
				if ( file.isDirectory() ) {
					deleteDirectory(file);
				} else if ( ! file.delete() ) {
					log.error("Unable to delete file "+file.getName());
					retValue = false;
				}
			}
		}
		if ( directory.delete() ) {
		    return retValue;
		} else {
		    return false;
		}
	}

}
