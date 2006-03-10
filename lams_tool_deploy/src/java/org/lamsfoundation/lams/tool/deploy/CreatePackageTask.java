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

/*
 * Created on 29/11/2005
 */
package org.lamsfoundation.lams.tool.deploy;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.DynamicConfigurator;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
/**
 * @author mtruong, Original design by Fiona Malikoff
 *
 * Parent class to all tasks related to the creation of the deployment package.
 * 
 */
public abstract class CreatePackageTask extends Task implements DynamicConfigurator {
    
    public static final String MODE = "mode";
    public static final String MODE_DEVELOPMENT = "development";
    public static final String MODE_PRODUCTION = "production";
    
    public static final String CONFIG_FILE = "configFile";
    public static final String OUTPUT_PATH = "outputPath";
    public static final String SCRIPT_PATH = "scriptPath";
    
    protected DeployConfig deployConfig;
    
    private Properties inputProperties;
    private Properties deployProperties;
    
   // private static String defaultFilename = "templateDeployTool.xml";
    private static String outputFilename = "deploy.xml";
    
    /* Ant Task Attributes */ 
    public String mode = MODE_PRODUCTION;
    public File outputPath = null;
    public File configFile = null;
    
    /* Dependent on outputPath, and set when outputPath is set */
    private File outputPathLib = null;
    private File outputPathSql = null;

    public CreatePackageTask()
    {
        super();
        deployConfig = null;
        deployProperties = null;
        inputProperties = new Properties();
    }
    
    /* ************** Dynamic Configurator Methods *****************************/
    public void setDynamicAttribute(String name, String value) {
        inputProperties.setProperty(name, value);
    }
    
    public Object createDynamicElement(String name) throws BuildException {
        throw new BuildException("CreatePackage does not support elements");
    } 	
    
    public abstract void execute();
    
    /**
     * add the ant defined properties to deployConfig
     */
    protected void applyParameters() {

        log("Applying task properties");
        
        Iterator iter = inputProperties.keySet().iterator();
        while ( iter.hasNext() ) {
            String key = (String) iter.next();
            // any keys not known to the deployConfig are ignored, so it doesn't matter if we pass mode, etc.
            // The only vectors will be filesets.
           	deployConfig.setProperty(key, (String) inputProperties.getProperty(key));
        }
    }

    protected void applyFilesets(String key, Vector<FileSet> filesets) {
    	ArrayList<String> filenames = new ArrayList<String>();
    	for ( FileSet fileset : filesets) {
    		DirectoryScanner ds = fileset.getDirectoryScanner(getProject());
    		String[] files = ds.getIncludedFiles();
    		for ( String filename: files ) {
    			filenames.add(ds.getBasedir()+File.separator+filename);
    		}
    	}
    	
       	deployConfig.setFilenames(key, filenames);
    }

    protected void createDirectory(File dir) {
        if ( dir.exists() ) {
            if ( ! dir.isDirectory() ) {
                throw new BuildException("Unable to write out deploy.properties - path "
                        +dir+" exists but is not a directory.");
            }
            if ( ! dir.canWrite() ) {
                throw new BuildException("Unable to write out deploy.properties - path "
                        +dir+" exists but is read only.");
            }
        } else {
            dir.mkdirs();
        }
    }
    
    /**
     * @param deployConfig
     */
    protected void writeConfigFile() throws IOException {
        
        String outputName = outputPath+File.separator+outputFilename;
        
        BufferedWriter out=null;
        try {
            out = new BufferedWriter(new FileWriter(outputName));
            deployConfig.writePropertiesToFile(out);
        } catch (Exception e) {
            throw new BuildException("Unable to write out "
                    +outputName+". Error "+e.getMessage(),e);
        }
        log("File "+outputName+" written.");
        out.close();
    }

  /*  private URL getTemplateDeployName() {
	    URL url =  CreatePackageTask_original.class.getResource(defaultFilename);
	    return url;
	} */
	
    /**
     * @param configFile The configFile to set.
     */
    public void setConfigFile(File configFile) {
        this.configFile = configFile;
    }
    /**
     * @param mode The mode to set.
     */
    public void setMode(String mode) {
        this.mode = mode;
    }
    /**
     * @param outputPath The outputPath to set.
     */
    public void setOutputPath(File outputPath) {
        this.outputPath = outputPath;
        if ( outputPath != null ) {
            this.outputPathLib = new File(outputPath.getAbsoluteFile()+File.separator+"lib");
            this.outputPathSql = new File(outputPath.getAbsoluteFile()+File.separator+"sql");
        } else {
            this.outputPathLib = null;
            this.outputPathSql = null;
        }
    }
    
    
        
    

}
