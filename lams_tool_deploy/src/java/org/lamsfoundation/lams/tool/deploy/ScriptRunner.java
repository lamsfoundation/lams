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
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.dbutils.DbUtils;

/**
 * Used to Run a SQL script.
 * Expects statements to be terminated by ;
 * and comment to start with -- or #.
 * Also note that there can be no SELECT statements in the script!.
 * 
 * @author chris
 */
public class ScriptRunner {
    public static final String STATEMENT_DELIMITER = ";";
    public static final String HASH_COMMENT_PATTERN = "#.*";
    public static final String DASH_COMMENT_PATTERN = "\\-{2,}.*";
    public static final String LINE_BREAK_PATTERN = "\\r\\n|\\n|\\r|\\u0085|\\u2028|\\u2029";
    public static final String LARGE_WHITE_SPACE_PATTERN = "\\s{2,}";

    protected Connection conn;
    protected String sqlScript;

    /** Creates a new instance of ScriptRunner */
    public ScriptRunner(final String sqlScript, final Connection conn) {
	this.conn = conn;
	this.sqlScript = sqlScript;
    }

    /**
     * Runs the script.
     * Note that it will *WILL NOT* close the connection, commit or roll back.
     * 
     * @throws DeployException
     *             if it fails.
     */
    public void run() throws DeployException {
	String[] statements = parseScript(sqlScript);
	executeStatements(statements, conn);
    }

    /**
     * Parses the script into statements
     * suitable for using with JDBC Statement interface.
     * 
     * @return String[] and array of the statements.
     * @throws DeployException
     *             if the script file in not suitable.
     */
    protected String[] parseScript(String script) throws DeployException {
	//remove comments (beginning with # & --)
	script = script.replaceAll(HASH_COMMENT_PATTERN, "");
	script = script.replaceAll(DASH_COMMENT_PATTERN, "");

	//remove line breaks
	script = script.replaceAll(LINE_BREAK_PATTERN, "");

	//remove excess whitespace
	script = script.replaceAll(LARGE_WHITE_SPACE_PATTERN, " ");

	//split on ;
	return script.split(STATEMENT_DELIMITER);

    }

    /**
     * Executes an array of statements againt a connection.
     * Note that it will *WILL NOT* close the connection, commit or roll back.
     */
    protected void executeStatements(String[] statements, Connection newConn) throws DeployException {
	Statement stmt = null;
	try {
	    stmt = newConn.createStatement();
	    for (int i = 0, length = statements.length; i < length; i++) {
		stmt.addBatch(statements[i]);
	    }

	    stmt.executeBatch();

	} catch (SQLException sqlex) {
	    throw new DeployException("Could not execute statements", sqlex);
	} finally {
	    DbUtils.closeQuietly(stmt);
	}
    }

}
