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
package org.lamsfoundation.lams.contentrepository.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;



/**
 * Gives access to the content package zip file used for testing 
 * 
 * @author Fiona Malikoff
 */
public class CRResources {

    public static final String singleFileName = "TextFile.txt";
    public static final String singleFileMimeType = "text/plain";

    public static final String zipFileName = "bopcp.zip";
	public static final String zipFileIncludesFilename = "index.html";
	public static final int zipFileNumFiles = 6;

    /** Get a single, text file */
    public static InputStream getSingleFile() throws FileNotFoundException {
	    URL url =  CRResources.class.getResource(singleFileName);
	    String path = url.getPath();
	    File myResource = new File(path);
	    return new FileInputStream(myResource);
    }
    
    /** Get a zip file. This can be used to test packages or a single binary file */
    public static InputStream getZipFile() throws FileNotFoundException {
	    URL url =  CRResources.class.getResource(zipFileName);
	    String path = url.getPath();
	    File myResource = new File(path);
	    return new FileInputStream(myResource);
	}
	

}
