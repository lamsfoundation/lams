/*
 * ToolDBActivateTask.java
 *
 * Created on 24 March 2005, 15:04
 */

package org.lamsfoundation.lams.tool.deploy;

import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import org.apache.commons.dbutils.DbUtils;

/**
 * Task creates all the necessary db items for the tool, but leaves them
 * inactive (use ToolDBActivateTask to activate the tool).
 * @author chris
 */
public class ToolDBDeployTask extends DBTask
{
    
    /**
     * Holds value of property toolInsertScript.
     */
    private File toolInsertScript;
    
    /**
     * Holds value of property toolLibraryInsertScript.
     */
    private File toolLibraryInsertScript;
    
    /**
     * Holds value of property toolTablesScript.
     */
    private File toolTablesScript;
    
    /**
     * Holds value of property toolId.
     */
    private long toolId;
    
    /**
     * Holds value of property learningLibraryId.
     */
    private long learningLibraryId;
    
    /**
     * Holds value of property toolActivityInsertScript.
     */
    private File toolActivityInsertScript;
    
    private long defaultContentId;
    
    /** Creates a new instance of ToolDBActivateTask */
    public ToolDBDeployTask()
    {
    }
    
    /**
     * Setter for property toolInsertFile.
     * @param toolInsertFile New value of property toolInsertFile.
     */
    public void setToolInsertScript(java.io.File toolInsertScript)
    
    {
        
        this.toolInsertScript = toolInsertScript;
    }
    
    /**
     * Setter for property toolLibraryInsertFile.
     * @param toolLibraryInsertFile New value of property toolLibraryInsertFile.
     */
    public void setToolLibraryInsertScript(java.io.File toolLibraryInsertScript)
    
    {
        
        this.toolLibraryInsertScript = toolLibraryInsertScript;
    }
    
    /**
     * Setter for property toolTablesCreatScript.
     * @param toolTablesCreatScript New value of property toolTablesCreatScript.
     */
    public void setToolTablesScript(java.io.File toolTablesScript)
    
    
    {

        this.toolTablesScript = toolTablesScript;
    }
    
    public void execute() throws DeployException
    {
        //get a connection
        Connection conn = getConnection();
        
        
        try
        {
            conn.setAutoCommit(false);
            //run tool insert and get tool id back
            toolId = runToolInsertScript(readFile(toolInsertScript), conn);
            
            //get a default tool content id
            defaultContentId = getNewToolContentId(toolId, conn);
            
            //add the default content id to the tool record
            updateToolDefaultContentId(toolId, defaultContentId, conn);
            
            //put the tool id into the tool library script
            Map replacementMap = new HashMap(1);
            replacementMap.put("tool_id", Long.toString(toolId));
            FileTokenReplacer libraryScriptReplacer = new FileTokenReplacer(toolLibraryInsertScript, replacementMap);
            String libraryScriptSQL = libraryScriptReplacer.replace();
            
            //run tool library script and get the library id back
            learningLibraryId = runLibraryScript(libraryScriptSQL, conn);
            
            //update tool record to include library id
            updateToolLibraryId(toolId, learningLibraryId, conn);
            
            //put the library id into the activity insert script
            replacementMap = new HashMap(1);
            replacementMap.put("tool_id", Long.toString(toolId));
            replacementMap.put("learning_library_id", Long.toString(learningLibraryId));
            FileTokenReplacer activityScriptReplacer = new FileTokenReplacer(toolActivityInsertScript, replacementMap);
            String activityScriptSQL = activityScriptReplacer.replace();
            
            //run the activity insert script
            runScript(activityScriptSQL, conn);
            
            //put the tool id and and defualt content id into
            //the tool tables script
            replacementMap = new HashMap(1);
            replacementMap.put("tool_id", Long.toString(toolId));
            replacementMap.put("default_content_id", Long.toString(defaultContentId));
            FileTokenReplacer toolTablesScriptReplacer = new FileTokenReplacer(toolTablesScript, replacementMap);
            String toolTablesScriptSQL = toolTablesScriptReplacer.replace();
            
            //run the tool table script
            runScript(toolTablesScriptSQL, conn);
            
            //commit transaction
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
                throw new DeployException("Attempted to rollback because of "+sqlex+" but failed - cleanup maybe required (see root cause)", sqlex2);
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
                throw new DeployException("Attempted to rollback because of "+dex+" but failed - cleanup maybe required (see root cause)", sqlex);
            }
            throw dex;
        }
        finally
        {
            DbUtils.closeQuietly(conn);
        }
    }
    
    /**
     * Getter for property toolId.
     * @return Value of property toolId.
     */
    public long getToolId()
    {
        
        return this.toolId;
    }
    
    /**
     * Getter for property learningLibraryId.
     * @return Value of property learningLibraryId.
     */
    public long getLearningLibraryId()
    {
        
        return this.learningLibraryId;
    }
    
    /**
     * Setter for property toolActivityInsertScript.
     * @param toolActivityInsertScript New value of property toolActivityInsertScript.
     */
    public void setToolActivityInsertScript(File toolActivityInsertScript)
    {
        
        this.toolActivityInsertScript = toolActivityInsertScript;
    }
    
    private long getNewToolContentId(long toolId, Connection conn) throws DeployException
    {
        PreparedStatement stmt = null;
        ResultSet results = null;
        try
        {
            stmt = conn.prepareStatement("INSERT INTO lams_tool_content (tool_id) VALUES (?)");
            stmt.setLong(1, toolId);
            stmt.execute();
            stmt = conn.prepareStatement("SELECT LAST_INSERT_ID() FROM lams_tool_content");
            results = stmt.executeQuery();
            if (results.next())
            {
                return results.getLong("LAST_INSERT_ID()");
            }
            else
            {
                throw new DeployException("No tool content id found");
            }
            
        }
        catch (SQLException sqlex)
        {
            throw new DeployException("Could not get new tool content id", sqlex);
        }
        finally
        {
            DbUtils.closeQuietly(stmt);
            DbUtils.closeQuietly(results);
        }
    }
    
    private void updateToolDefaultContentId(long toolId, long defaultContentId, Connection conn) throws DeployException
    {
        PreparedStatement stmt = null;
        try
        {
            stmt = conn.prepareStatement("UPDATE lams_tool SET default_content_id = ? WHERE tool_id = ?");
            stmt.setLong(1, defaultContentId);
            stmt.setLong(2, toolId);
            stmt.execute();
            
        }
        catch (SQLException sqlex)
        {
            throw new DeployException("Could not update default content id into tool", sqlex);
        }
        finally
        {
            DbUtils.closeQuietly(stmt);
        }
    }
    
    private void updateToolLibraryId(long toolId, long libraryId, Connection conn) throws DeployException
    {
        PreparedStatement stmt = null;
        try
        {
            stmt = conn.prepareStatement("UPDATE lams_tool SET learning_library_id = ? WHERE tool_id = ?");
            stmt.setLong(1, libraryId);
            stmt.setLong(2, toolId);
            stmt.execute();
            
        }
        catch (SQLException sqlex)
        {
            throw new DeployException("Could not update library id into tool", sqlex);
        }
        finally
        {
            DbUtils.closeQuietly(stmt);
        }
    }
    
    private void runScript(final String scriptSQL, final Connection conn) throws DeployException
    {
        ScriptRunner runner = new ScriptRunner(scriptSQL, conn);
        runner.run();
    }
    
    private long runLibraryScript (final String scriptSQL, final Connection conn) throws DeployException
    {
        runScript(scriptSQL, conn);
        PreparedStatement stmt = null;
        ResultSet results = null;
        try
        {
            stmt  = conn.prepareStatement("SELECT LAST_INSERT_ID() FROM lams_learning_library");
            results = stmt.executeQuery();
            if (results.next())
            {
                return results.getLong("LAST_INSERT_ID()");
            }
            else
            {
                throw new DeployException("Could not get learning_library_id");
            }
        }
        catch (SQLException sqlex)
        {
            throw new DeployException("Failed to run learning library script",sqlex);
        }
        finally
        {
            DbUtils.closeQuietly(stmt);
            DbUtils.closeQuietly(results);
        }
        
    }
    
    private long runToolInsertScript (final String scriptSQL, final Connection conn) throws DeployException
    {
        runScript(scriptSQL, conn);
        PreparedStatement stmt = null;
        ResultSet results = null;
        try
        {
            stmt  = conn.prepareStatement("SELECT LAST_INSERT_ID() FROM lams_tool");
            results = stmt.executeQuery();
            if (results.next())
            {
                return results.getLong("LAST_INSERT_ID()");
            }
            else
            {
                throw new DeployException("Could not get learning_library_id");
            }
        }
        catch (SQLException sqlex)
        {
            throw new DeployException("Failed to run tool insert script",sqlex);
        }
        finally
        {
            DbUtils.closeQuietly(stmt);
            DbUtils.closeQuietly(results);
        }
        
    }

    /**
     * Getter for property defaultContentId.
     * @return Value of property defaultContentId.
     */
    public long getDefaultContentId()
    {

        return this.defaultContentId;
    }
}
