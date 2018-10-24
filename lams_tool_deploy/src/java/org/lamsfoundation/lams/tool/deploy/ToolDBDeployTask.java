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


package org.lamsfoundation.lams.tool.deploy;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;

/**
 * Task creates all the necessary db items for the tool, but leaves them
 * inactive (use ToolDBActivateTask to activate the tool).
 * 
 * @author chris
 */
public class ToolDBDeployTask extends DBTask {
    /**
     * Holds value of property toolInsertScriptPath.
     */
    private String toolInsertScriptPath;

    /**
     * Holds value of property toolLibraryInsertScriptPath.
     */
    private String toolLibraryInsertScriptPath;

    /**
     * Holds value of property toolTablesScriptPath.
     */
    private String toolTablesScriptPath;

    /**
     * File path to insert autopatch version script
     */
    private String toolDBVersionScriptPath;

    /**
     * Holds value of property toolId.
     */
    private long toolId;

    /**
     * Holds value of property learningLibraryId.
     */
    private long learningLibraryId;

    /**
     * Holds value of property toolActivityInsertScriptPath.
     */
    private String toolActivityInsertScriptPath;

    private long defaultContentId;

    /** Creates a new instance of ToolDBActivateTask */
    public ToolDBDeployTask() {
    }

    /**
     * Setter for property toolInsertFile.
     * 
     * @param toolInsertFile
     *            New value of property toolInsertFile.
     */
    public void setToolInsertScriptPath(String toolInsertScriptPath)

    {

	this.toolInsertScriptPath = toolInsertScriptPath;
    }

    /**
     * Setter for property toolLibraryInsertFile.
     * 
     * @param toolLibraryInsertFile
     *            New value of property toolLibraryInsertFile.
     */
    public void setToolLibraryInsertScriptPath(String toolLibraryInsertScriptPath)

    {

	this.toolLibraryInsertScriptPath = toolLibraryInsertScriptPath;
    }

    /**
     * Setter for property toolTablesCreatScript.
     * 
     * @param toolTablesCreatScript
     *            New value of property toolTablesCreatScript.
     */
    public void setToolTablesScriptPath(String toolTablesScriptPath)

    {

	this.toolTablesScriptPath = toolTablesScriptPath;
    }

    /**
     * Setter for property toolDBVersionScriptPath.
     * 
     * @param toolDBVersionScriptPath
     *            New value of property toolDBVersionScriptPath.
     */
    public void setToolDBVersionScriptPath(String toolDBVersionScriptPath) {
	this.toolDBVersionScriptPath = toolDBVersionScriptPath;
    }

    @Override
    public void execute() throws DeployException {
	toolInsertScript = new File(toolInsertScriptPath);
	toolLibraryInsertScript = new File(toolLibraryInsertScriptPath);
	toolActivityInsertScript = new File(toolActivityInsertScriptPath);
	toolTablesScript = new File(toolTablesScriptPath);
	if (toolDBVersionScriptPath != null && toolDBVersionScriptPath.trim().length() > 0) {
	    toolDBVersionScript = new File(toolDBVersionScriptPath);
	}
	//get a connection
	Connection conn = getConnection();
	try {
	    conn.setAutoCommit(false);
	    //run tool insert and get tool id back
	    toolId = runToolInsertScript(readFile(toolInsertScript), conn);

	    //get a default tool content id
	    defaultContentId = getNewToolContentId(toolId, conn);

	    //add the default content id to the tool record
	    updateToolDefaultContentId(toolId, defaultContentId, conn);

	    //put the tool id into the tool library script
//            Map replacementMap = new HashMap(1);
//            replacementMap.put("tool_id", Long.toString(toolId));
//            FileTokenReplacer libraryScriptReplacer = new FileTokenReplacer(toolLibraryInsertScript, replacementMap);
//            String libraryScriptSQL = libraryScriptReplacer.replace();

	    //run tool library script and get the library id back
	    learningLibraryId = runLibraryScript(readFile(toolLibraryInsertScript), conn);

	    //update tool record to include library id
	    updateToolLibraryId(toolId, learningLibraryId, conn);

	    //put the library id into the activity insert script
	    Map<String, String> replacementMap = new HashMap<String, String>(1);
	    replacementMap.put("tool_id", Long.toString(toolId));
	    replacementMap.put("learning_library_id", Long.toString(learningLibraryId));
	    FileTokenReplacer activityScriptReplacer = new FileTokenReplacer(toolActivityInsertScript, replacementMap);
	    String activityScriptSQL = activityScriptReplacer.replace();

	    //run the activity insert script
	    runScript(activityScriptSQL, conn);

	    //put the tool id and and defualt content id into
	    //the tool tables script
	    replacementMap = new HashMap<String, String>(1);
	    replacementMap.put("tool_id", Long.toString(toolId));
	    replacementMap.put("default_content_id", Long.toString(defaultContentId));
	    FileTokenReplacer toolTablesScriptReplacer = new FileTokenReplacer(toolTablesScript, replacementMap);
	    String toolTablesScriptSQL = toolTablesScriptReplacer.replace();

	    //run the tool table script
	    runScript(toolTablesScriptSQL, conn);

	    //run the db version script
	    if (toolDBVersionScriptPath != null && toolDBVersionScriptPath.trim().length() > 0) {
		runScript(readFile(toolDBVersionScript), conn);
	    }

	    //commit transaction
	    conn.commit();
	} catch (SQLException sqlex) {
	    try {
		DbUtils.rollback(conn);
	    } catch (SQLException sqlex2) {
		throw new DeployException("Attempted to rollback because of " + sqlex
			+ " but failed - cleanup maybe required (see root cause)", sqlex2);
	    }
	    throw new DeployException("Execute failed", sqlex);
	} catch (DeployException dex) {
	    try {
		DbUtils.rollback(conn);
	    } catch (SQLException sqlex) {
		throw new DeployException("Attempted to rollback because of " + dex
			+ " but failed - cleanup maybe required (see root cause)", sqlex);
	    }
	    throw dex;
	} finally {
	    DbUtils.closeQuietly(conn);
	}
    }

    /**
     * Getter for property toolId.
     * 
     * @return Value of property toolId.
     */
    public long getToolId() {

	return this.toolId;
    }

    /**
     * Getter for property learningLibraryId.
     * 
     * @return Value of property learningLibraryId.
     */
    public long getLearningLibraryId() {

	return this.learningLibraryId;
    }

    /**
     * Setter for property toolActivityInsertScript.
     * 
     * @param toolActivityInsertScript
     *            New value of property toolActivityInsertScript.
     */
    public void setToolActivityInsertScriptPath(String toolActivityInsertScriptPath)

    {

	this.toolActivityInsertScriptPath = toolActivityInsertScriptPath;
    }

    private long getNewToolContentId(long newToolId, Connection conn) throws DeployException {
	PreparedStatement stmt = null;
	ResultSet results = null;
	try {
	    stmt = conn.prepareStatement("INSERT INTO lams_tool_content (tool_id) VALUES (?)");
	    stmt.setLong(1, newToolId);
	    stmt.execute();
	    stmt = conn.prepareStatement("SELECT LAST_INSERT_ID() FROM lams_tool_content");
	    results = stmt.executeQuery();
	    if (results.next()) {
		return results.getLong("LAST_INSERT_ID()");
	    } else {
		throw new DeployException("No tool content id found");
	    }

	} catch (SQLException sqlex) {
	    throw new DeployException("Could not get new tool content id", sqlex);
	} finally {
	    DbUtils.closeQuietly(stmt);
	    DbUtils.closeQuietly(results);
	}
    }

    private void updateToolDefaultContentId(long toolId, long defaultContentId, Connection conn)
	    throws DeployException {
	PreparedStatement stmt = null;
	try {
	    stmt = conn.prepareStatement("UPDATE lams_tool SET default_tool_content_id = ? WHERE tool_id = ?");
	    stmt.setLong(1, defaultContentId);
	    stmt.setLong(2, toolId);
	    stmt.execute();

	} catch (SQLException sqlex) {
	    throw new DeployException("Could not update default content id into tool", sqlex);
	} finally {
	    DbUtils.closeQuietly(stmt);
	}
    }

    private void updateToolLibraryId(long newToolId, long libraryId, Connection conn) throws DeployException {
	PreparedStatement stmt = null;
	try {
	    stmt = conn.prepareStatement("UPDATE lams_tool SET learning_library_id = ? WHERE tool_id = ?");
	    stmt.setLong(1, libraryId);
	    stmt.setLong(2, newToolId);
	    stmt.execute();

	} catch (SQLException sqlex) {
	    throw new DeployException("Could not update library id into tool", sqlex);
	} finally {
	    DbUtils.closeQuietly(stmt);
	}
    }

    private void runScript(final String scriptSQL, final Connection conn) throws DeployException {
	ScriptRunner runner = new ScriptRunner(scriptSQL, conn);
	runner.run();
    }

    private long runLibraryScript(final String scriptSQL, final Connection conn) throws DeployException {
	runScript(scriptSQL, conn);
	PreparedStatement stmt = null;
	ResultSet results = null;
	try {
	    stmt = conn.prepareStatement("SELECT LAST_INSERT_ID() FROM lams_learning_library");
	    results = stmt.executeQuery();
	    if (results.next()) {
		return results.getLong("LAST_INSERT_ID()");
	    } else {
		throw new DeployException("Could not get learning_library_id");
	    }
	} catch (SQLException sqlex) {
	    throw new DeployException("Failed to run learning library script", sqlex);
	} finally {
	    DbUtils.closeQuietly(stmt);
	    DbUtils.closeQuietly(results);
	}

    }

    private long runToolInsertScript(final String scriptSQL, final Connection conn) throws DeployException {
	runScript(scriptSQL, conn);
	PreparedStatement stmt = null;
	ResultSet results = null;
	try {
	    stmt = conn.prepareStatement("SELECT LAST_INSERT_ID() FROM lams_tool");
	    results = stmt.executeQuery();
	    if (results.next()) {
		return results.getLong("LAST_INSERT_ID()");
	    } else {
		throw new DeployException("Could not get learning_library_id");
	    }
	} catch (SQLException sqlex) {
	    throw new DeployException("Failed to run tool insert script", sqlex);
	} finally {
	    DbUtils.closeQuietly(stmt);
	    DbUtils.closeQuietly(results);
	}

    }

    /**
     * Getter for property defaultContentId.
     * 
     * @return Value of property defaultContentId.
     */
    public long getDefaultContentId() {

	return this.defaultContentId;
    }

    private File toolInsertScript;

    private File toolActivityInsertScript;

    private File toolLibraryInsertScript;

    private File toolTablesScript;

    private File toolDBVersionScript;
}
