/*
 * InsertToolDBRecordsTask.java
 *
 * Created on 24 March 2005, 11:01
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
            activateTool(toolId, conn);
            activateLibrary(learningLibraryId, conn);
            activateActivity(learningLibraryId, conn);
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
    
    private void activateActivity(long libraryId, Connection conn) throws SQLException
    {
        PreparedStatement stmt = null;
        try
        {
            stmt = conn.prepareStatement("UPDATE lams_learning_activity SET valid_flag = 1 WHERE learning_library_id  = ?");
            stmt.setLong(1, libraryId);
            stmt.execute();
        }
        finally
        {
            DbUtils.closeQuietly(stmt);
        }
        
    }
    
}
