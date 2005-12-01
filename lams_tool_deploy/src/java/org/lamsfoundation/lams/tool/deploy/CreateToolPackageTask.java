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
 * Created on 30/11/2005
 */
package org.lamsfoundation.lams.tool.deploy;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/**
 * @author Mailing Truong, Original design by Fiona Malikoff
 * Creates a tool deployment package. Ant Task.
 * Specifies the implementation for the Ant's Task execute() method.
 * Other methods used are inherited from parent class CreatePackageTask
 *
 * Files created: deploy.xml
 * 
 * A deploy.xml file is created. It is built from two sources - 
 * a configuration file supplied at runtime and ant task parameter.
 * 
 * The DeployToolConfig object is created and if the configuration file is specified, 
 * then the configuration file is read and the 
 * DeployToolConfig object is updated. If the program is running as an ant task, 
 * the task parameters are checked and all task parameters except for mode and configFile are 
 * added to the DeployToolConfig object. So the customised properties can be set up in either
 * a configuration file (handy if you aren't using ant) or in a mixture of a configuration file 
 * and build.xml. Finally, the DeployToolConfig object is written out to the deploy.xml file.
 * 
 * Ant task parameters:
 * <UL>
 * <LI>(Mandatory) outputPath: path for deploy.xml file.</LI>
 * <LI>(Optional) mode: Values are either development or production. 
 * If "development" just creates the xml file. 
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
 */
public class CreateToolPackageTask extends CreatePackageTask {
    
    public void execute() {
        log("Create Deployment Package.");
        
        // create the new configuration file, using the template
        //deployConfig = new DeployToolConfig(getTemplateDeployName());
        deployConfig = new DeployToolConfig();
        // override with values from an optional config file
        if ( configFile != null && configFile.length() > 0 ) {
            log("Applying configuration file "+configFile.getAbsolutePath());
            //deployConfig.updateConfiguration(configFile.getAbsolutePath(), false);
            try
            {
                deployConfig.updateConfigurationProperties(configFile.getAbsolutePath());
            }
            catch(ParserConfigurationException e)
            {
                throw new DeployException("Could not parse the XML file" + e.getMessage(), e);
            }
            catch(IOException e)
            {
                throw new DeployException("Please check the file or file path. Error is:" + e.getMessage(), e);
            }
            catch(SAXException e)
            {
                throw new DeployException(e.getMessage());
            }
        }
        
        applyParameters();
        deployConfig.validateProperties();
        try
        {
            writeConfigFile(); //change this to write out xml file instead
        }
        catch(IOException e)
        {
            throw new DeployException(e.getMessage());
        }
    }

}
