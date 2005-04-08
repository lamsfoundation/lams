/*
 * Deploy.java
 *
 * Created on 04 April 2005, 10:36
 */

package org.lamsfoundation.lams.tool.deploy;

/**
 * Tool Deployer
 * @author chris
 */
public class Deploy
{
    
    /** Creates a new instance of Deploy */
    public Deploy()
    {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception
    {
        
        if ((args.length != 1) || (args[0] == null))
        {
            throw new IllegalArgumentException("Usage: Deployer <properties_file_path>");
        }
        
        System.out.println("Starting Tool Deploy");
        try
        {
            System.out.println("Reading Configuration File");
            DeployConfig config =  new DeployConfig(args[0]);
            
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
            dbDeployTask.execute();
            
            System.out.println("Updating application.xml");
            AddWebAppToApplicationXmlTask addWebAppTask =  new AddWebAppToApplicationXmlTask();
            addWebAppTask.setLamsEarPath(config.getLamsEarPath());
            addWebAppTask.setContextRoot(config.getToolContextRoot());
            addWebAppTask.setWebUri(config.getToolWebUri());
            addWebAppTask.execute();
            
            System.out.println("Deploying files to ear");
            DeployFilesTask deployFilesTask = new DeployFilesTask();
            deployFilesTask.setLamsEarPath(config.getLamsEarPath());
            deployFilesTask.setDeployFiles(config.getDeployFiles());
            deployFilesTask.execute();
            
            System.out.println("Activating Tool in LAMS");
            ToolDBActivateTask dbActivateTask = new ToolDBActivateTask();
            dbActivateTask.setDbUsername(config.getDbUsername());
            dbActivateTask.setDbPassword(config.getDbPassword());
            dbActivateTask.setDbDriverClass(config.getDbDriverClass());
            dbActivateTask.setDbDriverUrl(config.getDbDriverUrl());
            dbActivateTask.setLearningLibraryId(dbDeployTask.getLearningLibraryId());
            dbActivateTask.setToolId(dbDeployTask.getToolId());
            dbActivateTask.execute();
            
            System.out.println("Tool Deployed");
        }
        catch (Exception ex)
        {
            System.out.println("TOOL DEPLOY FAILED");
            ex.printStackTrace();
        }
    }
    
}
