/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
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
