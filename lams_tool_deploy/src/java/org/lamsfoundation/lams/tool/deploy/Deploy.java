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


package org.lamsfoundation.lams.tool.deploy;

import java.util.List;

/**
 * Tool Deployer Main Class
 *
 * Command Line Parameters: properties_file_path: mandatory forcedb: optional, defaults to false. if true, deletes any
 * old entries in db.
 *
 * Only use forceDB for development - not designed for production. If forceDB is set, then toolSignature is needed.
 *
 * @author Chris Perfect, modifications by Fiona Malikoff, Luke Foxton
 */
public class Deploy {

    /** Creates a new instance of Deploy */
    public Deploy() {
    }

    /**
     * Runs the Deploy
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) throws Exception {

	if (args.length < 1 || args[0] == null) {
	    throw new IllegalArgumentException("Usage: Deployer <properties_file_path> <forceDB>. n"
		    + "\nforceDB deletes the old database entries before creating the new entries."
		    + "\nSo it should be set to false for production and true for development");
	}
	Boolean forceDB = Boolean.FALSE;
	if (args.length >= 2 && args[1] != null) {
	    forceDB = new Boolean(args[1]);
	}
	Boolean noDB = Boolean.FALSE;
	if (args.length >= 3 && args[2] != null) {
		noDB = new Boolean(args[2]);
	}

	System.out.println("Starting Tool Deploy:forceDB=" + forceDB + ",noDB=" + noDB);
	try {
	    System.out.println("Reading Configuration File " + args[0]);
	    DeployToolConfig config = new DeployToolConfig(null, args[0]);

	    String toolSignature = config.getToolSignature();
	    String toolVersionStr = config.getToolVersion();

	    /**
	     * Checking if the tool is installed, if not continue to install the new tool If it is, check if the tool to
	     * be installed is newer than the current If it is newer, update the db, jar and war files, otherwise put up
	     * a message that the tool is already up to date and do nothing
	     */
	    ToolDBUpdater dbUpdater = new ToolDBUpdater();
	    dbUpdater.setDbUsername(config.getDbUsername());
	    dbUpdater.setDbPassword(config.getDbPassword());
	    dbUpdater.setDbDriverClass(config.getDbDriverClass());
	    dbUpdater.setDbDriverUrl(config.getDbDriverUrl());
	    dbUpdater.setToolSignature(config.getToolSignature());
	    dbUpdater.setToolVersion(config.getToolVersion());
	    dbUpdater.setToolCompatibleVersion(config.getMinServerVersionNumber());
	    dbUpdater.checkInstalledVersion();
	    if (dbUpdater.getToolExists() && !forceDB && !noDB) {
		if (dbUpdater.getToolNewer()) {
		    System.out.println("Updating tool: " + toolSignature + " with version " + toolVersionStr);

		    // Disabling the tool while update takes place
		    System.out.println("Disabling tool for update, valid flags set to 0.");
		    dbUpdater.activateTool(toolSignature, 0);

		    // updates the lams_tool table with the lams_version
		    dbUpdater.execute();

		    // deploy the jar and war files
		    System.out.println("Deploying files to ear");
		    DeployFilesTask deployFilesTask = new DeployFilesTask();
		    deployFilesTask.setLamsEarPath(config.getLamsEarPath());
		    deployFilesTask.setDeployFiles(config.getDeployFiles());
		    deployFilesTask.execute();

		    // deploy the language files
		    List<String> files = config.getLanguageFiles();
		    if (files != null && files.size() > 0) {
			DeployLanguageFilesTask deployLanguageFilesTask = new DeployLanguageFilesTask();
			deployLanguageFilesTask.setLamsEarPath(config.getLamsEarPath());
			deployLanguageFilesTask.setDictionaryPacket(config.getLanguageFilesPackage());
			deployLanguageFilesTask.setDeployFiles(config.getLanguageFiles());
			deployLanguageFilesTask.execute();
		    }

		    // Enabling the tool so it can now be used by LAMS (if not to be hidden)
		    if (config.getHideTool() == false) {
			System.out.println("Enabling Tool, valid flags set to 1");
			dbUpdater.activateTool(toolSignature, 1);
		    } else {
			System.out.println("Hiding Tool, valid flags set to 0");
			dbUpdater.hideTool(toolSignature);
		    }

		    System.out.println("Tool update completed");
		    System.exit(0);
		} else {
		    System.out.println("The tool to be installed: " + toolSignature + " " + toolVersionStr
			    + " is already up to date.");
		    System.exit(0);
		}
	    } else if (dbUpdater.getToolExists() && noDB) {

			// deploy the jar and war files
			System.out.println("[mafeng]Deploying files to assembly, file name:" + config.getDeployFiles());
			DeployFilesTask deployFilesTask = new DeployFilesTask();
			deployFilesTask.setLamsEarPath(config.getLamsEarPath());
			deployFilesTask.setDeployFiles(config.getDeployFiles());
			deployFilesTask.execute();

			// deploy the language files
			List<String> files = config.getLanguageFiles();
			if (files != null && files.size() > 0) {
				DeployLanguageFilesTask deployLanguageFilesTask = new DeployLanguageFilesTask();
				deployLanguageFilesTask.setLamsEarPath(config.getLamsEarPath());
				deployLanguageFilesTask.setDictionaryPacket(config.getLanguageFilesPackage());
				deployLanguageFilesTask.setDeployFiles(config.getLanguageFiles());
				deployLanguageFilesTask.execute();
			}

			System.out.println("[mafeng]Tool update completed");
			System.exit(0);
	    } else {
		System.out.println("The tool to be installed: " + toolSignature + " does not exist in database");
		System.out.println("Continuing with full install");
		// Do nothing, continue with full install
	    }

	    System.out.println("Running Tool DB Deploy");
	    ToolDBDeployTask dbDeployTask = new ToolDBDeployTask();
	    dbDeployTask.setDbUsername(config.getDbUsername());
	    dbDeployTask.setDbPassword(config.getDbPassword());
	    dbDeployTask.setDbDriverClass(config.getDbDriverClass());
	    dbDeployTask.setDbDriverUrl(config.getDbDriverUrl());
	    dbDeployTask.setToolInsertScriptPath(config.getToolInsertScriptPath());
	    dbDeployTask.setToolLibraryInsertScriptPath(config.getToolLibraryInsertScriptPath());
	    dbDeployTask.setToolActivityInsertScriptPath(config.getToolActivityInsertScriptPath());
	    dbDeployTask.setToolTablesScriptPath(config.getToolTablesScriptPath());
	    if (config.getToolDBVersionScriptPath() != null
		    && config.getToolDBVersionScriptPath().trim().length() > 0) {
		dbDeployTask.setToolDBVersionScriptPath(config.getToolDBVersionScriptPath());
	    }
	    dbDeployTask.execute();

	    System.out.println("Deploying files to ear");
	    DeployFilesTask deployFilesTask = new DeployFilesTask();
	    deployFilesTask.setLamsEarPath(config.getLamsEarPath());
	    deployFilesTask.setDeployFiles(config.getDeployFiles());
	    deployFilesTask.execute();

	    List<String> files = config.getLanguageFiles();
	    if (files != null && files.size() > 0) {
		DeployLanguageFilesTask deployLanguageFilesTask = new DeployLanguageFilesTask();
		deployLanguageFilesTask.setLamsEarPath(config.getLamsEarPath());
		deployLanguageFilesTask.setDictionaryPacket(config.getLanguageFilesPackage());
		deployLanguageFilesTask.setDeployFiles(config.getLanguageFiles());
		deployLanguageFilesTask.execute();
	    }

	    AddWebAppToApplicationXmlTask addWebAppTask = new AddWebAppToApplicationXmlTask();
	    addWebAppTask.setLamsEarPath(config.getLamsEarPath());
	    addWebAppTask.setContextRoot(config.getToolContext());
	    addWebAppTask.setWebUri(config.getToolWebUri());
	    addWebAppTask.execute();

	    /*
	     * LDEV-2174: This update is no longer needed since we use shared Spring context.
	     *
	     * List<String> warFiles = new LinkedList<String>(); warFiles.add("lams-central.war");
	     * warFiles.add("lams-learning.war"); warFiles.add("lams-monitoring.war"); InsertToolContextClasspathTask
	     * updateWebXmlTask = new InsertToolContextClasspathTask();
	     * updateWebXmlTask.setLamsEarPath(config.getLamsEarPath()); updateWebXmlTask.setArchivesToUpdate(warFiles);
	     * updateWebXmlTask.setApplicationContextPath(config.getToolApplicationContextPath());
	     * updateWebXmlTask.setJarFileName(config.getToolJarFileName()); updateWebXmlTask.execute();
	     */

	    if (config.getHideTool() == false) {
		System.out.println("Activating Tool: " + config.getToolSignature());
		dbUpdater.activateTool(config.getToolSignature(), 1);
	    } else {
		System.out.println("Hiding tool: " + config.getToolSignature());
		dbUpdater.hideTool(config.getToolSignature());
	    }

	    System.out.println("Tool Deployed");
	} catch (Exception ex) {
	    System.out.println("TOOL DEPLOY FAILED");
	    ex.printStackTrace();
	}
    }

}
