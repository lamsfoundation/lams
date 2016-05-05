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

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;

/**
 * Task removes all the entries for tool from the tool tables. Only use during
 * development - too dangerous to run in production
 *
 * @author Fiona Malikoff
 */
public class ToolDBRemoveToolEntriesTask extends DBTask {

    /**
     * Holds value of property toolTablesDeleteScriptPath - deletes the
     * tool's tables.
     */
    private String toolTablesDeleteScriptPath;

    private File toolTablesDeleteScript;

    /**
     * Holds value of the tool signature
     */
    private String toolSignature;

    /** Creates a new instance of ToolDBActivateTask */
    public ToolDBRemoveToolEntriesTask() {
    }

    /**
     * Setter for property toolTablesDeleteCreatScript.
     *
     * @param toolTablesCreatScript
     *            New value of property toolTablesDeleteCreatScript.
     */
    public void setToolTablesDeleteScriptPath(String toolTablesDeleteScriptPath) {

	this.toolTablesDeleteScriptPath = toolTablesDeleteScriptPath;
    }

    /**
     * Setter for property toolSignature.
     *
     * @param toolSignature
     *            New value of property toolSignature.
     */
    public void setToolSignature(String toolSignature) {

	this.toolSignature = toolSignature;
    }

    /**
     * Remove the tool and the related library activity.
     */
    @Override
    public void execute() throws DeployException {
	long toolId = 0;
	long libId = 0;

	toolTablesDeleteScript = new File(toolTablesDeleteScriptPath);

	// get a connection
	Connection conn = getConnection();
	try {
	    conn.setAutoCommit(false);
	    toolId = getToolId(conn);
	    if (toolId != 0) {
		libId = getLearningLibraryId(toolId, conn);

		System.out.println("Deleting rows where toolId = " + toolId + " and learningLibraryId = " + libId);
		cleanupToolTables(toolId, libId, conn);

		// run the tool table delete script
		if (toolTablesDeleteScriptPath == null || toolTablesDeleteScriptPath.length() == 0) {
		    System.err.println("Unable to run tool delete script as not specified. Later calls may fail.");
		} else {
		    ScriptRunner runner = new ScriptRunner(readFile(toolTablesDeleteScript), conn);
		    runner.run();
		}
	    }

	    // commit transaction
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

    private void cleanupToolTables(long toolId, long libraryId, Connection conn) throws DeployException {
	PreparedStatement stmt = null;
	try {
	    stmt = conn.prepareStatement("DELETE FROM lams_learning_activity WHERE tool_id = ?");
	    stmt.setLong(1, toolId);
	    stmt.execute();

	    stmt = conn.prepareStatement("DELETE FROM lams_tool_content WHERE tool_id = ?");
	    stmt.setLong(1, toolId);
	    stmt.execute();

	    stmt = conn.prepareStatement("DELETE FROM lams_tool WHERE tool_id = ?");
	    stmt.setLong(1, toolId);
	    stmt.execute();

	    stmt = conn.prepareStatement("DELETE FROM lams_learning_library WHERE learning_library_id = ?");
	    stmt.setLong(1, libraryId);
	    stmt.execute();

	    stmt = conn.prepareStatement("DELETE FROM patches WHERE system_name = ?");
	    stmt.setString(1, toolSignature);
	    stmt.execute();

	} catch (SQLException sqlex) {
	    throw new DeployException("Could not remove tool entries from core tool tables.", sqlex);
	} finally {
	    DbUtils.closeQuietly(stmt);
	}
    }

    private long getToolId(final Connection conn) throws DeployException {
	PreparedStatement stmt = null;
	ResultSet results = null;
	try {
	    stmt = conn.prepareStatement("SELECT tool_id FROM lams_tool where tool_signature = ?");
	    stmt.setString(1, toolSignature);
	    results = stmt.executeQuery();
	    if (results.next()) {
		return results.getLong("tool_id");
	    }
	} catch (SQLException sqlex) {
	    throw new DeployException("Failed to run tool insert script", sqlex);
	} finally {
	    DbUtils.closeQuietly(stmt);
	    DbUtils.closeQuietly(results);
	}
	return 0;
    }

    private long getLearningLibraryId(long toolId, final Connection conn) throws DeployException {
	PreparedStatement stmt = null;
	ResultSet results = null;
	try {
	    stmt = conn.prepareStatement("SELECT learning_library_id FROM lams_learning_activity where tool_id = ?");
	    stmt.setLong(1, toolId);
	    results = stmt.executeQuery();
	    if (results.next()) {
		return results.getLong("learning_library_id");
	    }
	} catch (SQLException sqlex) {
	    throw new DeployException("Failed to run tool insert script", sqlex);
	} finally {
	    DbUtils.closeQuietly(stmt);
	    DbUtils.closeQuietly(results);
	}
	return 0;
    }
}
