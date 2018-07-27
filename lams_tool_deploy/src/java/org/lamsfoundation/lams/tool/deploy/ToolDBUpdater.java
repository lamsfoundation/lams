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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;

/**
 * Checks if the tool version to be installed exists and is a newer version
 * Updates the database with the new tool version
 * 
 * @author Luke Foxton
 */
public class ToolDBUpdater extends DBTask {
    // Holds value of property toolSignature
    private String toolSignature;

    // Holds value of property toolVersion
    private String toolVersion;

    // Holds value of property toolCompatibleversion
    private String toolCompatibleVersion;

    // Holds value of property toolExists
    private boolean toolExists;

    // Holds value of property toolNewer
    private boolean toolNewer;

    // Holds value of property compatibleVersion
    private boolean compatibleVersion;

    /**
     * Creates instance of ToolDBUpdater
     */
    public ToolDBUpdater() {

    }

    /**
     * Updates the lams_tool table
     * TODO: possibly add a update_date collumn for system admin use
     */
    @Override
    public void execute() {
	Connection conn = getConnection();
	PreparedStatement stmt = null;
	try {
	    stmt = conn.prepareStatement("UPDATE lams_tool SET tool_version = \"" + toolVersion
		    + "\" WHERE tool_signature  = \"" + toolSignature + "\"");
	    stmt.execute();
	} catch (SQLException sqlex) {
	    throw new DeployException("Could not set tool version", sqlex);
	} finally {
	    DbUtils.closeQuietly(stmt);
	}
    }

    /**
     * Checks the tool is installed in the db, sets property toolExists
     * Checks the tool version in the db, sets the property toolNewer
     * Checks the database Sever version is higher than the install one
     */
    public void checkInstalledVersion() {
	Connection conn = getConnection();
	PreparedStatement stmt = null;
	ResultSet results = null;
	try {

	    stmt = conn.prepareStatement(
		    "SELECT tool_version FROM lams_tool WHERE tool_signature=\"" + toolSignature + "\"");
	    results = stmt.executeQuery();
	    if (results.first()) {
		toolExists = true;
		double dbVersion = java.lang.Double.parseDouble(results.getString("tool_version"));
		double instVersion = java.lang.Double.parseDouble(toolVersion);

		if (instVersion > dbVersion) {
		    toolNewer = true;
		} else {
		    toolNewer = false;
		}
	    } else {
		toolExists = false;
	    }

	    // Checking the server version, uses the date section of the version for accuracy
	    // eg 2.0.200612051427 becomes 200612051427
	    stmt = conn.prepareStatement(
		    "SELECT config_value FROM lams_configuration WHERE config_key=\"ServerVersionNumber\"");
	    results = stmt.executeQuery();
	    if (results.first()) {

		String dbVersionStr = results.getString("config_value");
		double dbVersion = java.lang.Double
			.parseDouble(dbVersionStr.substring(dbVersionStr.lastIndexOf('.') + 1));
		double instVersion = java.lang.Double
			.parseDouble(toolCompatibleVersion.substring(toolCompatibleVersion.lastIndexOf('.') + 1));
		if (dbVersion < instVersion) {
		    System.out.println("The minimum ServerVersionNumber \"" + toolCompatibleVersion
			    + "\" for install tool " + this.toolSignature + " " + this.toolVersion
			    + " is higher than the current installed \"" + dbVersionStr + "\"");
		    System.out.println("BUILD FAILED");
		    System.exit(0);
		} else {
		    this.compatibleVersion = true;
		}
	    } else {
		throw new DeployException("Could not get the ServerVersionNumber from the database.");
	    }

	    conn.close();
	} catch (SQLException sqlex) {
	    throw new DeployException("Problem checking tool version compatibility", sqlex);
	} finally {
	    DbUtils.closeQuietly(stmt);
	    DbUtils.closeQuietly(results);
	}
    }

    /**
     * Activates/de-activates tool in db based on the flag
     * 
     * @param toolSig
     *            The signature of the tool to activate/de-activate
     * @param flag
     *            Set to 1 for activate, 0 for de-activate
     * @throws SQLException
     */
    public void activateTool(String toolSig, int flag) throws SQLException {
	Connection conn = getConnection();
	PreparedStatement stmt = null;
	ResultSet results = null;
	try {
	    stmt = conn.prepareStatement(
		    "UPDATE lams_tool SET valid_flag = " + flag + " WHERE tool_signature  = \"" + toolSig + "\"");
	    stmt.execute();

	    stmt = conn.prepareStatement(
		    "SELECT learning_library_id FROM lams_tool WHERE tool_signature=\"" + toolSignature + "\"");
	    results = stmt.executeQuery();

	    if (results.first()) {
		double llid = results.getDouble("learning_library_id");

		stmt = conn.prepareStatement("UPDATE lams_learning_library SET valid_flag = " + flag
			+ " WHERE learning_library_id  = \"" + llid + "\"");
		stmt.execute();
	    }
	} catch (SQLException se) {
	    throw new DeployException("Could not activate/de-activate tool for update");
	} finally {
	    DbUtils.closeQuietly(stmt);
	}
    }

    /**
     * Hides tool in db based on the flag
     * 
     * @param toolSig
     *            The signature of the tool to activate/de-activate
     * @throws SQLException
     */
    public void hideTool(String toolSig) throws SQLException {
	Connection conn = getConnection();
	PreparedStatement stmt = null;
	ResultSet results = null;
	try {
	    stmt = conn.prepareStatement(
		    "UPDATE lams_tool SET valid_flag = 1 WHERE tool_signature  = \"" + toolSig + "\"");
	    stmt.execute();

	    stmt = conn.prepareStatement(
		    "SELECT learning_library_id FROM lams_tool WHERE tool_signature=\"" + toolSignature + "\"");
	    results = stmt.executeQuery();

	    if (results.first()) {
		double llid = results.getDouble("learning_library_id");

		stmt = conn.prepareStatement(
			"UPDATE lams_learning_library SET valid_flag = 0 WHERE learning_library_id  = \"" + llid
				+ "\"");
		stmt.execute();
	    }
	} catch (SQLException se) {
	    throw new DeployException("Error hiding tool: " + toolSig);
	} finally {
	    DbUtils.closeQuietly(stmt);
	}
    }

    public String queryTool(String toolSig, String column) {
	Connection conn = getConnection();
	PreparedStatement stmt = null;
	ResultSet results = null;
	try {
	    stmt = conn
		    .prepareStatement("select " + column + " from lams_tool where tool_signature= \"" + toolSig + "\"");
	    System.out.println("SQL stmt: " + stmt);
	    results = stmt.executeQuery();

	    if (results.first()) {
		return results.getString(column);

	    }
	} catch (SQLException se) {

	    throw new DeployException("Could not get entry from lams_tool: " + column + "\n" + se.getMessage());

	} finally {
	    DbUtils.closeQuietly(stmt);
	}
	return "ERROR";
    }

    /**
     * set method for toolSignature
     * 
     * @param sig
     *            The toolSignature to be set
     */
    public void setToolSignature(String sig) {
	this.toolSignature = sig;
    }

    /**
     * set method for toolVersion
     * 
     * @param ver
     *            the toolVersion to be set
     */
    public void setToolVersion(String ver) {
	this.toolVersion = ver;
    }

    /**
     * get method for toolExists
     * 
     * @return True if the tool signature exists in the database
     */
    public boolean getToolExists() {
	return this.toolExists;
    }

    /**
     * get method for toolNewer
     * 
     * @return True if the tool to be installed is a newer version than the version in the datbase
     */
    public boolean getToolNewer() {
	return this.toolNewer;
    }

    /**
     * @return Returns the toolCompatibleVersion
     */
    public String getToolCompatibleVersion() {
	return this.toolCompatibleVersion;
    }

    /**
     * @param ver
     *            sets toolCompatibleVersion
     */
    public void setToolCompatibleVersion(String ver) {
	this.toolCompatibleVersion = ver;
    }

    /**
     * @return True if tool is compatible with the server version in the db
     */
    public boolean getToolCompatible() {
	return this.compatibleVersion;
    }

}
