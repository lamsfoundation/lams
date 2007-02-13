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
/* $$Id$$ */
package org.lamsfoundation.lams.tool.deploy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Checks if the tool version to be installed exists and is a newer version
 * Updates the database with the new tool version
 * @author Luke Foxton
 */
public class ToolDBUpdater extends DBTask
{
	// Holds value of property toolId
	private long toolId;
    
    // Holds value of property learningLibraryId.
    private long learningLibraryId;
    
    // Holds value of property toolSignature
    private String toolSignature;
	
    // Holds value of property toolVersion
    private String toolVersion;
    
    // Creates instance of ToolDBUpdater
    public ToolDBUpdater () {}
    
    /**
     * Updates the lams_tool table and the learning library
     */
    public void execute()
    {
    	
    }
    
    /**
     * Checks if the tool is installed by checking the db
     * @param toolSig The tool signature of the tool to be installed
     * @return True if the tool signature is present in the db
     */
    public boolean checkInstalled(String toolSig)
    {
    	return false;
    }
    
    /**
     * Checks if the currently installed version is equal or newer than the 
     * version to be installed
     * @param toolVer String of the version to be installed
     * @return True if the current version is older than the version to be installed
     */
    public boolean checkVersion(String toolVer)
    {
    	return false;
    }
    
    
    // set method for toolId
    public void setToolId(long toolId) {this.toolId = toolId;}
    
    // set method for learningLibraryId
    public void setLearningLibraryId(long id) {this.learningLibraryId = id;}
    
    // set method for toolSignature
    public void setToolSignature(String sig) {this.toolSignature = sig;}
    
    // set method for toolVersion
    public void setToolVersion(String ver) {this.toolVersion = ver;}
 }
