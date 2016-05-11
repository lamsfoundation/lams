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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */


package org.lamsfoundation.lams.tool.deploy.libraryActivity;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.lamsfoundation.lams.tool.deploy.CreatePackageTask;
import org.lamsfoundation.lams.tool.deploy.DeployException;
import org.xml.sax.SAXException;

/**
 * @author Mailing Truong
 *
 *         Creates a library deployment package. Ant Task.
 *         Specifies the implementation for the Ant's Task execute() method.
 *         Other methods used are inherited from parent class CreatePackageTask
 *
 *         Files created: deploy.xml
 *
 *         A deploy.xml file is created. It is built from two sources -
 *         a configuration file supplied at runtime and ant task parameter.
 *
 *         The configuration file supplied, should contain the list of learning libraries
 *         to deploy and all tool activities that are a part of the learning library.
 *         Settings these values via an Ant Task is not yet supported. However the ant task
 *         parameters can be used to set the database values.
 *
 *         The DeployLibraryConfig object is created and the specified configuration file
 *         will set up values for the LearningLibrary, ToolActivity objects and if you want,
 *         the db settings. If the program is running as an ant task, then the task parameters
 *         (usually for the db settings) are checked and added to the DeployLibraryConfig
 *         object.
 *
 *         Ant task parameters:
 *         <UL>
 *         <LI>(Mandatory) outputPath: path for deploy.xml file.</LI>
 *         <LI>(Optional) configFile: path to the configuration file that
 *         contains parameters.</LI>
 *         <LI>(Optional) dbUsername, dbPassword, dbDriverClass, dbDriverUrl:
 *         define the db settings</LI>
 *         <LI>(Optional) all other parameters go into the deploy.xml file.</LI>
 *         </UL>
 *
 */
public class CreateLibraryPackageTask extends CreatePackageTask {

    /* ************** ANT Task Methods *****************************/
    @Override
    public void execute() {
	log("Create Library Deployment Package.");

	// create the new configuration file, using the template
	//deployConfig = new DeployToolConfig(getTemplateDeployName());
	deployConfig = new DeployLibraryConfig(outputPath.toString());
	// override with values from an optional config file
	if (configFile != null && configFile.length() > 0) {
	    log("Applying configuration file " + configFile.getAbsolutePath());
	    //deployConfig.updateConfiguration(configFile.getAbsolutePath(), false);
	    try {
		deployConfig.updateConfigurationProperties(configFile.getAbsolutePath());
	    } catch (ParserConfigurationException e) {
		throw new DeployException("Could not parse the XML file" + e.getMessage(), e);
	    } catch (IOException e) {
		throw new DeployException("Please check the file or file path. Error is:" + e.getMessage(), e);
	    } catch (SAXException e) {
		throw new DeployException(e.getMessage());
	    }
	}

	applyParameters();
	deployConfig.validateProperties();
	try {
	    writeConfigFile(); //change this to write out xml file instead
	} catch (IOException e) {
	    throw new DeployException(e.getMessage());
	}
    }

    /* ************** End Interface/Inherited Methods *****************************/

}
