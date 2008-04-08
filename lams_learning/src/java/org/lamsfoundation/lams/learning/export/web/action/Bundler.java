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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */
package org.lamsfoundation.lams.learning.export.web.action;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Superclass for any files "bundled" as part of the export portfolio. For code reuse.
 *
 */
public class Bundler {

	protected static Logger log = Logger.getLogger(Bundler.class);

	/**
	 * 
	 */
	public Bundler() {
		super();
	}

	protected void createDirectories(List<String> directoriesRequired) {
		
		for ( String directoryPath: directoriesRequired) {
			File dir = new File(directoryPath);
			if ( ! dir.mkdirs() )  {
				log.error("Unable to create directory for export portfolio: "+directoryPath);
			}
		}
	}

	protected void copyFile(String filePath, File file) throws IOException {
		
		FileInputStream is = new FileInputStream(file);
		OutputStream os = null;
		try {
			
			int bufLen = 1024; // 1 Kbyte
			byte[] buf = new byte[1024]; // output buffer
			os = new FileOutputStream(filePath);
	
			BufferedInputStream in = new BufferedInputStream(is);
			int len = 0;
		    while((len = in.read(buf,0,bufLen)) != -1){
		    	os.write(buf,0,len);
		    }	
			
		} catch ( IOException e ) {
			String message = "Unable to write out file needed for export portfolio. File was "+filePath;	
			log.error(message,e);
			throw e;
			
		} finally {
	
			try {
				if ( is != null )
					is.close();
			} catch (IOException e1) {
				String message = "Unable to close input export portfolio file due to IOException";
				log.warn(message,e1);
			}
	
			try {
				if ( os != null )
					os.close();
			} catch (IOException e2) {
				String message = "Unable to close output export portfolio file due to IOException";
				log.warn(message,e2);
			}
		}
		
	
	}

}