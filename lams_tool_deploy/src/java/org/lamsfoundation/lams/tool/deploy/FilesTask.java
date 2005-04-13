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
 * Parent of Tasks that deal with the deploy files.
 * @author chris
 */
public abstract class FilesTask implements Task
{
    
    /**
     * Holds value of property lamsEarPath.
     */
    protected String lamsEarPath;
    
    /**
     * Holds value of property deployFiles.
     */
    protected List deployFiles;
    
    /** Creates a new instance of DeployWarTask */
    public FilesTask()
    {
    }
    
    /**
     * Sets the path to the lams ear.
     * @param lamsEarPath New value of property lamsEarPath.
     */
    public void setLamsEarPath(String lamsEarPath)
    {
        
        this.lamsEarPath = lamsEarPath;
    }
    
    /**
     * Sets the list of file paths to operate on.
     * @param deployFiles New value of property deployFiles.
     */
    public void setDeployFiles(List deployFiles)
    {
        
        this.deployFiles = deployFiles;
    }
    
    
}
