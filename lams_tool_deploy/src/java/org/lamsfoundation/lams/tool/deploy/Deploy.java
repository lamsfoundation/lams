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
        
       DeployConfig config =  new DeployConfig(args[1]);
    }
    
}
