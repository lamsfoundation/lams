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
       if (args.length != 1)
       {
           throw new Exception("Usage: Deployer <properties_file_path>");
       }
       DeployConfig config =  new DeployConfig(args[1]);
       
       //db deploy
       
       //add required elements to the application xml
       AddWebAppToApplicationXmlTask addWebAppTask =  new AddWebAppToApplicationXmlTask();
       addWebAppTask.setLamsEarPath(config.getLamsEarPath());
       addWebAppTask.setContextRoot(config.getToolContextRoot());
       addWebAppTask.setWebUri(config.getToolWebUri());
       addWebAppTask.execute();
       
       //deploy files
       DeployFilesTask deployFilesTask = new DeployFilesTask();
       deployFilesTask.setLamsEarPath(config.getLamsEarPath());
       deployFilesTask.setDeployFiles(config.getDeployFiles());
       deployFilesTask.execute();
       
       //db activation
    }
    
}
