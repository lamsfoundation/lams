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
package org.lamsfoundation.lams.util.zipfile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.zip.ZipOutputStream;

import junit.framework.TestCase;

import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.FileUtilException;

/**
 * Test that the zip file util will expand a zip file, delete the
 * zip file temporary directory and that it won't delete an
 * arbitrary directory.
 * 
 * @author Fiona Malikoff
 */
public class TestZipFileUtil extends TestCase {

    //  this file must exist in the same package as the test class.
    private static String zipFileName = "bof.zip"; 
    
    private String testFilename = "test.txt";
    private byte[] testFileContents = "This is my test file\n".getBytes();
    private String tempSysDirName = System.getProperty( "java.io.tmpdir" );
   
	private static InputStream getTestZipFile() throws FileNotFoundException {
	    URL url =  TestZipFileUtil.class.getResource(zipFileName);
	    if ( url == null ) 
	        fail("Unable to get url of resource "+zipFileName);
	    
	    String path = url.getPath();
	    File myResource = new File(path);
	    return new FileInputStream(myResource);
	}

    public void testExpandDeleteZip() {
        
        try {
            
			InputStream is = getTestZipFile();
	        assertTrue("File input stream can be created.", is != null );
	        
			String tempDirectory = ZipFileUtil.expandZip(is, zipFileName);
			
			assertTrue("Temporary name returned created", tempDirectory != null);
			File dir = new File(tempDirectory);
			assertTrue("Temporary directory created", dir.exists());
			String[] filenames = dir.list();
			assertTrue("Temporary directory contains files", filenames != null && filenames.length > 0 );
	
			boolean successful = ZipFileUtil.deleteDirectory(tempDirectory);
			assertTrue("All files in temporary directory deleted", successful);
			dir = new File(tempDirectory);
			assertTrue("Temporary name deleted", !dir.exists());

        } catch (Exception e) {
            fail("Exception thrown "+e.getMessage());
        }
    }

    /** Tests the public delete directory method - checks with a directory
     * with the prefix. (This method should only delete a directory
     *  if the directoryname starts with the special prefix.)
     */
    public void testDeleteDirectoryWithPrefix() {

        try {
            String testDirToDelete = ZipFileUtil.prepareTempDirectory("todelete.zip");
            writeFile(testDirToDelete, testFilename, testFileContents);
        
            ZipFileUtil.deleteDirectory(testDirToDelete);
            File dir= new File(testDirToDelete);
            assertTrue("Directory deletion suceeded.", ! dir.exists());

        } catch (Exception e) {
            fail("Unexpected exception thrown"+e.getMessage());
        }
        
    }
    
    /** Tests the public delete directory method - checks with a directory
     * without the prefix. (This method should only delete a directory
     *  if the directoryname starts with the special prefix.)
     */
    public void testDeleteDirectoryWithoutPrefix() {

        String testDirToKeep = tempSysDirName + "KeepDirectory";
        File dir = new File(testDirToKeep);
        if ( ! dir.exists() ) {
            dir.mkdirs();
        }
        
        try {
            writeFile(testDirToKeep, testFilename, testFileContents);
        
            ZipFileUtil.deleteDirectory(testDirToKeep);

        } catch (ZipFileUtilException e) {
            assertTrue("ZipFileUtilException thrown as expected", true);
        } catch (Exception e) {
            fail("Unexception thrown"+e.getMessage());
        }
        
        dir = new File(testDirToKeep);
        assertTrue("Directory deletion failed as expected - directory still exists.", dir.exists());
    }    
    
    /** Test the clean up of old files. Create three directories - two with the special 
     * name and one with a different name. Create three files - two with 
     * the special name and one with a different name. Of the dir/files
     * with the special name, set one file and one directory to be five
     * days old (via Calendar). Set one directory to be 4 days old.
     * Then run the delete. Only the directory that is four days old should be deleted - 
     * all the others should be fine.
     */
    public void testCleanupOldFiles() {


        try {
            // clean up any old directories so they don't interfere with test.
            ZipFileUtil.cleanupOldFiles(5);
            
    		Calendar foureDaysAgo = new GregorianCalendar();
    		foureDaysAgo.add(Calendar.DAY_OF_MONTH,-4);
    		long fourDaysAgoMilliseconds = foureDaysAgo.getTimeInMillis();

    		// create the three directories and stick a file in each directory to 
            // test the recursive delete.
            String testDirToDelete = ZipFileUtil.prepareTempDirectory("todelete.zip");
            writeFile(testDirToDelete, testFilename, testFileContents);
            File file = new File(testDirToDelete);
            file.setLastModified(fourDaysAgoMilliseconds);
            
            String testDirToKeep1 = ZipFileUtil.prepareTempDirectory("tokeep.zip");
            writeFile(testDirToKeep1, testFilename, testFileContents);
            
            String testDirToKeep2 = tempSysDirName + "differentDirectory";
            File dir = new File(testDirToKeep2);
            if ( ! dir.exists() ) {
                dir.mkdirs();
            }
            writeFile(testDirToKeep2, testFilename, testFileContents);

            // now create the three files.
            String testFileToKeep1 = ZipFileUtil.prefix+"ToKeepFile1.txt";
            writeFile(tempSysDirName, testFileToKeep1, testFileContents);
            file = new File(tempSysDirName+File.separator+testFileToKeep1);
            file.setLastModified(fourDaysAgoMilliseconds);
            String testFileToKeep2 = ZipFileUtil.prefix+"ToKeepFile2.txt";
            writeFile(tempSysDirName, testFileToKeep2, testFileContents);
            String testFileToKeep3 = "ToKeepFile3.txt";
            writeFile(tempSysDirName, testFileToKeep3, testFileContents);

            // now sleep (so that the 4 days ago test works) and then delete...
            Thread.sleep(5);
            int numDeleted = ZipFileUtil.cleanupOldFiles(4);
            
            // only the "todelete" directory should be deleted
            // as the test almost done, be nice and clean up our test files.
            assertTrue("One directory deleted ",numDeleted==1);
            
            file = new File(testDirToDelete);
            assertTrue("Directory "+testDirToDelete+" has been deleted ",!file.exists());

            file = new File(testDirToKeep1);
            assertTrue("Directory "+testDirToKeep1+" has not been deleted ",file.exists());
            FileUtil.deleteDirectory(file);

            file = new File(testDirToKeep2);
            assertTrue("Directory "+testDirToKeep2+" has not been deleted ",file.exists());
            FileUtil.deleteDirectory(file);

            file = new File(tempSysDirName+File.separator+testFileToKeep1);
            assertTrue("File "+testFileToKeep1+" has not been deleted ",file.exists());
            file.delete();
            
            file = new File(tempSysDirName+File.separator+testFileToKeep2);
            assertTrue("File "+testFileToKeep2+" has not been deleted ",file.exists());
            file.delete();

            file = new File(tempSysDirName+File.separator+testFileToKeep3);
            assertTrue("File "+testFileToKeep3+" has not been deleted ",file.exists());
            file.delete();
           
            
        } catch (Exception e) {
            fail("Exception thrown "+e.getMessage());
        }
    }

    private void writeFile(String directoryName, String filename, byte[] testFileContents) throws IOException {
        FileOutputStream file = new FileOutputStream(directoryName+File.separator+filename);
        try {
            file.write(testFileContents);
            file.close();
        } catch ( IOException e ) {
            if ( file != null )
                file.close();
            throw e;
        }
    }
   
    private boolean createTestDirectoryAndFiles(String testDirName) throws FileUtilException, IOException
    {
       String testSubDirName = testDirName + "/subDirectory";       	
    
       	boolean created = FileUtil.createDirectory(testSubDirName);
       	boolean filesCreated = false;
       	if (created)
       	{
       		//add some files
       		File file1 = new File(testDirName, "testFile1.txt");
       		File file2 = new File(testDirName, "testFile2.txt");
       		File file3 = new File(testDirName, "testFile3.txt");
       		File file4 = new File(testSubDirName, "testFile4.txt");
       		File file5 = new File(testSubDirName, "testFile5.txt");
       		
       		filesCreated = file1.createNewFile() && file2.createNewFile() && file3.createNewFile()
								&& file4.createNewFile() && file5.createNewFile();
       	}
       	
       	if (created && filesCreated)
       		return true;
       	else
       		return false;
       	
         	
    } 
    
    public void testCreateZipFile() throws FileUtilException, IOException, ZipFileUtilException
    {
    	String directoryName = "testDirectory";
    	boolean created = createTestDirectoryAndFiles(directoryName);
    	if (created)
    	{
	    	String zipFileName = "test.zip";
	    	
	    	ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFileName)));
	    	File directory = new File(directoryName);
	        assertTrue(directory.exists());
	    	File[] files = directory.listFiles();
	  
	    	ZipFileUtil.zipFiles(out, files);  
	    	
	    	out.close(); 
	    	
	    	FileUtil.deleteDirectory(directoryName);
    	}
    	else
    	{
    		assertFalse("Failed to run test", true);
    	}
    	
    } 
    
   public void testCreateZipFile2() throws FileUtilException, IOException, ZipFileUtilException
    {
    	  	
	    	String directoryName = "/MaiTestDirectory";
	    	boolean created = createTestDirectoryAndFiles(directoryName);
	    	
	    	if (created)
	    	{
	    	   	
		    	String zipFileName= "maitest.zip";
		    	
		    	ZipFileUtil.createZipFile(zipFileName, directoryName);
		    	
		
		    	
		    	FileUtil.deleteDirectory(directoryName);
	    	}
	    	else
	    	{
	    		assertFalse("Failed to run test", true);
	    	} 
	
    } 
    
 
}
