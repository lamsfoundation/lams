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
/* $$Id$$ */
package org.lamsfoundation.lams.tool.deploy.libraryActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.lamsfoundation.lams.tool.deploy.DeployLanguageFilesTask;

/**
 * @author mtruong
 *
 * Deployer for library activities that contain more than one tool activity.
 * Currently, is only supported for the complex activity types: parallel,
 * options, sequence
 * 
 * The main logic is adopted from Deploy.java
 * See org.lamsfoundation.lams.tool.deploy.Deploy
 */
public class DeployLibrary {

    public DeployLibrary() {
        
    }
 
    public static void main (String[] args)
    {
        if ((args.length < 1) || (args[0] == null))
        {
            throw new IllegalArgumentException("Usage: Deployer <properties_file_path> <language file directory>." );
        }
        
        String languageFileDirectoryName = args[1]; 
        File languageFileDirectory = null;
        if ( languageFileDirectoryName != null && languageFileDirectoryName.trim().length() > 0) {
        	languageFileDirectory = new File(languageFileDirectoryName);
        	if ( ! languageFileDirectory.exists() || ! languageFileDirectory.isDirectory() 
        			|| ! languageFileDirectory.canRead() ) {
        		throw new IllegalArgumentException("Unable to access language files. Directory "+languageFileDirectory
        				+" is missing, is not a directory or is not readable.");
        	}
        }

        System.out.println("Starting Library Deploy");
        
       try
       {
            DeployLibraryConfig config= new DeployLibraryConfig(args[0]);
            LibraryDBDeployTask deployTask = new LibraryDBDeployTask(config);
            Map<String,Object> deployValues = deployTask.execute();
            System.out.println("Updated database");

            if ( languageFileDirectory != null ) {
            	String[] files = languageFileDirectory.list();
	            if ( files != null && files.length > 0 ) {
	            	// generate package name in which the language files are placed.
	            	Long learningLibraryId = (Long) deployValues.get(LibraryDBDeployTask.LEARNING_LIBRARY_ID);
	            	String packagePath = "org.lamsfoundation.lams.library.llid"+learningLibraryId.toString();
	            		
	            	// copy the files to the lams-dictionary.jar
		            DeployLanguageFilesTask deployLanguageFilesTask = new DeployLanguageFilesTask();
		            deployLanguageFilesTask.setLamsEarPath(config.getLamsEarPath());
		            deployLanguageFilesTask.setDictionaryPacket(packagePath);
		            List<String> fileList = createLanguageFilesList(languageFileDirectoryName,files);
		            deployLanguageFilesTask.setDeployFiles(fileList);
		            deployLanguageFilesTask.execute();
	            }
	            System.out.println("Copied language files");
            }
            
            System.out.println("Library Deployed");
            
       }
       catch (Exception ex)
       {
           System.out.println("TOOL DEPLOY FAILED");
           ex.printStackTrace();
       }
       
        
        
    }
    
    /**
     * Sets the list of file paths to operate on.
     * @param deployFiles New value of property deployFiles.
     */
    private static List<String> createLanguageFilesList(String path, String[] deployFilesArray)
    {
    	List<String> deployFiles = new ArrayList<String>(deployFilesArray!=null?deployFilesArray.length:0);
        for ( String filename: deployFilesArray ) {
        	deployFiles.add(path+File.separator+filename);
        }
        return deployFiles;
    }



}
