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


package org.lamsfoundation.lams.tool.deploy.libraryActivity;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.lamsfoundation.lams.tool.deploy.DBTask;
import org.lamsfoundation.lams.tool.deploy.DeployException;
import org.lamsfoundation.lams.tool.deploy.FileTokenReplacer;
import org.lamsfoundation.lams.tool.deploy.ScriptRunner;

/**
 * @author mtruong
 *
 *         Task to deploy a learning library that contains one or
 *         more tool activities.
 *
 */
public class LibraryDBDeployTask extends DBTask {

    private static Log log = LogFactory.getLog(LibraryDBDeployTask.class);

    private ArrayList learningLibraries;

    public static final String LEARNING_LIBRARY_ID = "learningLibraryId";

    public LibraryDBDeployTask() {
    }

    public LibraryDBDeployTask(DeployLibraryConfig config) {
	this.setDbDriverClass(config.getDbDriverClass());
	this.setDbDriverUrl(config.getDbDriverUrl());
	this.setDbPassword(config.getDbPassword());
	this.setDbUsername(config.getDbUsername());
	this.learningLibraries = config.getLearningLibraryList();
    }

    /**
     * Loop through the library activities
     * 1) insert library script -> returns the learning_library_id
     * 2) update library activity script with learning_library_id
     * 3) run library activity script -> returns the activity_id
     * 4) Loop through tool activities
     * a) Find the tool_id that matches the tool_signature
     * b) Update tool activity script with tool_id and parent_activity_id equal to the activity_id from step 3
     * c) run the tool activity script
     */

    @Override
    public void execute() throws DeployException {
	Connection conn = getConnection();
	try {
	    conn.setAutoCommit(false);

	    Iterator i = learningLibraries.iterator();
	    //iterating through learning libraries
	    while (i.hasNext()) {
		LearningLibrary learningLibrary = (LearningLibrary) i.next();

		String libraryInsertScriptPath = learningLibrary.getLibraryInsertScriptPath();
		File libraryInsertScript = new File(libraryInsertScriptPath);
		long learningLibraryId = runLibraryScript(readFile(libraryInsertScript), conn, "lams_learning_library");

		log.debug("The learning_library_id is learningLibraryId");

		//put the library id into the library activity insert script
		Map<String, String> replacementMap = new HashMap<String, String>(1);
		replacementMap.put("learning_library_id", Long.toString(learningLibraryId));
		FileTokenReplacer activityScriptReplacer = new FileTokenReplacer(
			new File(learningLibrary.getTemplateActivityInsertScriptPath()), replacementMap);
		String libraryActivityScriptSQL = activityScriptReplacer.replace();

		long parentActivityId = runLibraryScript(libraryActivityScriptSQL, conn, "lams_learning_activity");
		log.debug("Parent Learning Activity ID is: " + parentActivityId);

		learningLibrary.setLearningLibraryId(learningLibraryId);
		learningLibrary.setParentActivityId(learningLibraryId);

		ArrayList toolActivities = learningLibrary.getToolActivityList();

		Iterator j = toolActivities.iterator();

		//iterating through tool activities within a particular learning library
		while (j.hasNext()) {
		    ToolActivity toolActivity = (ToolActivity) j.next();
		    //lookup tool_id from toolSignature
		    long toolId = getToolId(toolActivity.getToolSignature(), conn);

		    //put the tool_id and parent_activity_id into the tool activity insert script
		    replacementMap = new HashMap<String, String>(1);
		    replacementMap.put("tool_id", Long.toString(toolId));
		    replacementMap.put("parent_activity_id", Long.toString(parentActivityId));
		    FileTokenReplacer toolTablesScriptReplacer = new FileTokenReplacer(
			    new File(toolActivity.getToolActivityInsertScriptPath()), replacementMap);
		    String toolTablesScriptSQL = toolTablesScriptReplacer.replace();

		    runScript(toolTablesScriptSQL, conn);

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

    /**
     * Looks up current tool installations for the tool with <code>toolSignature</code>
     * The tool_id for the tool with the matching tool signature will be returned.
     * 
     * @param toolSignature
     * @param conn
     * @return
     */
    public long getToolId(String toolSignature, Connection conn) {
	PreparedStatement stmt = null;
	ResultSet results = null;
	try {
	    stmt = conn.prepareStatement("SELECT tool_id FROM lams_tool WHERE tool_signature=?");
	    stmt.setString(1, toolSignature);
	    stmt.execute();

	    results = stmt.executeQuery();
	    if (results.next()) {
		return results.getLong("tool_id");
	    } else {
		throw new DeployException("Tool id for tool signature " + toolSignature + " not found.");
	    }

	} catch (SQLException sqlex) {
	    throw new DeployException("Tool id for tool signature " + toolSignature + " not found.", sqlex);
	} finally {
	    DbUtils.closeQuietly(stmt);
	    DbUtils.closeQuietly(results);
	}
    }

    /**
     * Runs the sql statements and will return the last inserted id from the particular
     * table.
     * 
     * @param scriptSQL
     *            The sql to run
     * @param conn
     * @param tablename
     *            The table which to query for the last inserted id.
     * @return the last inserted id from <code>tablename</code>
     * @throws DeployException
     */
    private long runLibraryScript(final String scriptSQL, final Connection conn, String tablename)
	    throws DeployException {
	runScript(scriptSQL, conn);
	PreparedStatement stmt = null;
	ResultSet results = null;
	try {
	    stmt = conn.prepareStatement("SELECT LAST_INSERT_ID() FROM " + tablename);
	    results = stmt.executeQuery();
	    if (results.next()) {
		return results.getLong("LAST_INSERT_ID()");
	    } else {
		throw new DeployException("Could not get the last inserted id from table " + tablename);
	    }
	} catch (SQLException sqlex) {
	    throw new DeployException("Failed to run learning library script", sqlex);
	} finally {
	    DbUtils.closeQuietly(stmt);
	    DbUtils.closeQuietly(results);
	}

    }

    private void runScript(final String scriptSQL, final Connection conn) throws DeployException {
	ScriptRunner runner = new ScriptRunner(scriptSQL, conn);
	runner.run();
    }

    /**
     * @return Returns the learningLibraries.
     */
    public ArrayList getLearningLibraries() {
	return learningLibraries;
    }

    /**
     * @param learningLibraries
     *            The learningLibraries to set.
     */
    public void setLearningLibraries(ArrayList learningLibraries) {
	this.learningLibraries = learningLibraries;
    }

}
