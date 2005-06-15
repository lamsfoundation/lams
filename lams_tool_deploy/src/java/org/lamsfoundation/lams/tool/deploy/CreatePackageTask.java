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
package org.lamsfoundation.lams.tool.deploy;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.Properties;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DynamicConfigurator;
import org.apache.tools.ant.Task;

/**
 * Create a deployment package. Ant Task.
 * 
 * This class can be used to generate just enough of the deployment (i.e. the
 * properties file) to run the deploy as an ant ask, or can create 
 * the whole deploy package for sending to someone else. 
 * 
 * Files created: deploy.properties
 * 
 * A deploy.properties file is created. It is built from three sources - templateDeploy.properties
 * in this class' package, a configuration file supplied at runtime and ant task parameter.
 * 
 * First the templateDeploy.properties file is read into a DeployConfig object. 
 * Then the configuration file is read and the DeployConfig object updated, with the configuration 
 * file values override the templateDeploy.properties. If the program is running as an ant task, 
 * the task parameters are checked and all task parameters except for mode and configFile are 
 * added to the DeployConfig object. So the customised properties can be set up in either
 * a configuration file (handy if you aren't using ant) or in a mixture of a configuration file 
 * and build.xml. Finally, the DeployConfig object is written out to a deploy.properties.
 * 
 * Ant task parameters:
 * <UL>
 * <LI>(Mandatory) outputPath: path for deploy.properties file.</LI>
 * <LI>(Optional) mode: Values are either development or production. 
 * If "development" just creates the properties file. 
 * If "production" then creates the entire package. 
 * Defaults to "development".</LI>
 * <LI>(Optional) configFile: path to the configuration file that 
 * contains parameters.</LI>
 * <LI>(Optional) scriptPath: source path for sql files e.g db/sql.
 * Must be set if any of the sql script names are set (see next entry).</LI>
 * <LI>(Optional) toolTablesScript, toolActivityInsertScript, 
 * toolLibraryInsertScript, toolInsertScript: define the name of the 
 * scripts to be copied from the scriptPath directory to deploy/sql.
 * Generates the toolInsertScriptPath, toolLibraryInsertScriptPath,
 * toolActivityInsertScriptPath and toolTablesScriptPath entries.</LI>
 * <LI>(Optional) all other parameters go into the deploy.properties file.</LI> 
 * </UL>
 *
 * @author Fiona Malikoff
 */
public class CreatePackageTask extends Task implements DynamicConfigurator {

    public static final String MODE = "mode";
    public static final String MODE_DEVELOPMENT = "development";
    public static final String MODE_PRODUCTION = "production";
    
    public static final String CONFIG_FILE = "configFile";
    public static final String OUTPUT_PATH = "outputPath";
    public static final String SCRIPT_PATH = "scriptPath";

    private static String defaultFilename = "templateDeploy.properties";
    private static String outputFilename = "deploy.properties";
    private DeployConfig deployConfig;
    private Properties inputProperties;
    private Properties deployProperties;
    
    /* Ant Task Attributes */ 
    public String mode = MODE_DEVELOPMENT;
    public File outputPath = null;
    public File configFile = null;
    
    /* Dependent on outputPath, and set when outputPath is set */
    private File outputPathLib = null;
    private File outputPathSql = null;
    /**
     * 
     */
    public CreatePackageTask() {
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
    
    /* ************** ANT Task Methods *****************************/
    public void execute() {
        log("Create Deployment Package.");
        
        // create the new configuration file, using the template
        deployConfig = new DeployConfig(getTemplateDeployName(),false);
        
        // override with values from an optional config file
        if ( configFile != null && configFile.length() > 0 ) {
            log("Applying configuration file "+configFile.getAbsolutePath());
            deployConfig.updateConfiguration(configFile.getAbsolutePath(), false);
        }
        
        applyParameters();
        deployConfig.validateProperties();
        writeConfigFile();
        
    }
    
    /* ************** End Interface/Inherited Methods *****************************/

    /**
     * add the ant defined properties to deployConfig
     */
    private void applyParameters() {

        log("Applying task properties");
        
        Iterator iter = inputProperties.keySet().iterator();
        while ( iter.hasNext() ) {
            String key = (String) iter.next();
            // any keys not known to the deployConfig are ignored, so it doesn't matter if we pass mode, etc.
            deployConfig.setProperty(key, inputProperties.getProperty(key));
        }
    }

    private void createDirectory(File dir) {
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
    private void writeConfigFile() {
        
        String outputName = outputPath+File.separator+outputFilename;
        
        FileOutputStream os=null;
        try {
            os = new FileOutputStream(outputName);
            deployConfig.writeProperties(os);
        } catch (Exception e) {
            throw new BuildException("Unable to write out "
                    +outputName+". Error "+e.getMessage(),e);
        }
        log("File "+outputName+" written.");
    }

    private URL getTemplateDeployName() {
	    URL url =  CreatePackageTask.class.getResource(defaultFilename);
	    return url;
	}
	
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
