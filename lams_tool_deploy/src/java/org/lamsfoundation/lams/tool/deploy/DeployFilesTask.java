/*
 * DeployWarTask.java
 *
 * Created on 29 March 2005, 16:57
 */

package org.lamsfoundation.lams.tool.deploy;
import java.util.List;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

/**
 * Copies the required file to the lams ear directory.
 * @author chris
 */
public class DeployFilesTask extends FilesTask
{
    

    
    /**
     *Executes the task
     */
    public void execute() throws DeployException
    {
        File lamsEar =  new File(lamsEarPath);
        if (!lamsEar.exists())
        {
            throw new DeployException("Could not find "+lamsEarPath);
        }
        else if (!lamsEar.isDirectory())
        {
            throw new DeployException(lamsEarPath+" is not a directory");
        }
        else if (!lamsEar.canWrite())
        {
            throw new DeployException(lamsEarPath+" is not writable");
        }
        
        for (int i = 0, size = deployFiles.size(); i < size; i++)
        {
            copyFile((String) deployFiles.get(i), lamsEar);
        }
    }
    
    /**
     *
     */
    protected void copyFile(String fileName, File directory) throws DeployException
    {
        try
        {
            File original = new File(fileName);
            FileUtils.copyFileToDirectory(original, directory);
        }
        catch (IOException ioex)
        {
            throw new DeployException("Could not copy file "+fileName+" to "+lamsEarPath, ioex);
        }
        
    }
}
