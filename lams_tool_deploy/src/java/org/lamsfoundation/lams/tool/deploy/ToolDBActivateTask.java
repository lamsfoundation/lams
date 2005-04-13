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

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import org.apache.commons.dbutils.DbUtils;

/**
 * Ant task runs the creates & inserts for the LAMS aand Tool DB tables
 * @author chris
 *
 *
 */
public class ToolDBActivateTask extends DBTask
{
    private long defaultContentId;
    
    private long toolId;
    
    /**
     * Holds value of property learningLibraryId.
     */
    private long learningLibraryId;
    
    /** Creates a new instance of InsertToolDBRecordsTask */
    public ToolDBActivateTask()
    {
    }
    
    public void execute() throws DeployException
    {
        Connection conn = getConnection();
        try
        {
            conn.setAutoCommit(false);
            activateTool(toolId, conn);
            activateLibrary(learningLibraryId, conn);
            //activateActivity(learningLibraryId, conn);
            conn.commit();
        }
         catch (SQLException sqlex)
        {
            try
            {
                DbUtils.rollback(conn);
            }
            catch (SQLException sqlex2)
            {
                throw new DeployException("Attempted to rollback db activate because of "+sqlex+" but failed - cleanup maybe required (see root cause)", sqlex2);
            }
            throw new DeployException("Execute failed", sqlex);
        }
        catch (DeployException dex)
        {
            try
            {
                DbUtils.rollback(conn);
            }
            catch (SQLException sqlex)
            {
                throw new DeployException("Attempted to rollback db activate because of "+dex+" but failed - cleanup maybe required (see root cause)", sqlex);
            }
            throw dex;
        }
        finally
        {
            DbUtils.closeQuietly(conn);
        }
    }
    
    /**
     * Setter for property toolId.
     * @param toolId New value of property toolId.
     */
    public void setToolId(long toolId)
    {
        
        this.toolId = toolId;
    }
    
    /**
     * Setter for property learningLibraryId.
     * @param learningLibraryId New value of property learningLibraryId.
     */
    public void setLearningLibraryId(long learningLibraryId)
    {
        
        this.learningLibraryId = learningLibraryId;
    }
    
    private void activateTool(long toolId, Connection conn) throws SQLException
    {
        PreparedStatement stmt = null;
        try
        {
            stmt = conn.prepareStatement("UPDATE lams_tool SET valid_flag = 1 WHERE tool_id  = ?");
            stmt.setLong(1, toolId);
            stmt.execute();
        }
        finally
        {
            DbUtils.closeQuietly(stmt);
        }
        
    }
    
    private void activateLibrary(long libraryId, Connection conn) throws SQLException
    {
        PreparedStatement stmt = null;
        try
        {
            stmt = conn.prepareStatement("UPDATE lams_learning_library SET valid_flag = 1 WHERE learning_library_id  = ?");
            stmt.setLong(1, libraryId);
            stmt.execute();
        }
        finally
        {
            DbUtils.closeQuietly(stmt);
        }
        
    }
    
//    private void activateActivity(long libraryId, Connection conn) throws SQLException
//    {
//        PreparedStatement stmt = null;
//        try
//        {
//            stmt = conn.prepareStatement("UPDATE lams_learning_activity SET valid_flag = 1 WHERE learning_library_id  = ?");
//            stmt.setLong(1, libraryId);
//            stmt.execute();
//        }
//        finally
//        {
//            DbUtils.closeQuietly(stmt);
//        }
//        
//    }
    
}
