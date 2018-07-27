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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.lamsfoundation.lams.tool.deploy.DBTask;
import org.lamsfoundation.lams.tool.deploy.DeployException;

/**
 * Task to update a learning activity's language file column with the
 * appropriate generated value.
 *
 */
public class ActivityDBLanguageUpdateTask extends DBTask {

    private long activityId;
    private String languageFilename;

    /** Creates a new instance of InsertToolDBRecordsTask */
    public ActivityDBLanguageUpdateTask() {
    }

    /**
     * Activates the tool and library.
     */
    @Override
    public void execute() throws DeployException {
	Connection conn = getConnection();
	try {
	    conn.setAutoCommit(false);
	    updateActivity(conn);
	    conn.commit();
	} catch (SQLException sqlex) {
	    try {
		DbUtils.rollback(conn);
	    } catch (SQLException sqlex2) {
		throw new DeployException("Attempted to rollback db update because of " + sqlex
			+ " but failed - cleanup maybe required (see root cause)", sqlex2);
	    }
	    throw new DeployException("Execute failed", sqlex);
	} catch (DeployException dex) {
	    try {
		DbUtils.rollback(conn);
	    } catch (SQLException sqlex) {
		throw new DeployException("Attempted to rollback db update because of " + dex
			+ " but failed - cleanup maybe required (see root cause)", sqlex);
	    }
	    throw dex;
	} finally {
	    DbUtils.closeQuietly(conn);
	}
    }

    private void updateActivity(Connection conn) throws SQLException {
	PreparedStatement stmt = null;
	try {
	    stmt = conn.prepareStatement(
		    "UPDATE lams_learning_activity SET language_file = ? WHERE learning_library_id  = ?");
	    stmt.setString(1, languageFilename);
	    stmt.setLong(2, activityId);
	    stmt.execute();
	} finally {
	    DbUtils.closeQuietly(stmt);
	}

    }

    public long getActivityId() {
	return activityId;
    }

    public void setActivityId(long activityId) {
	this.activityId = activityId;
    }

    public String getLanguageFilename() {
	return languageFilename;
    }

    public void setLanguageFilename(String languageFilename) {
	this.languageFilename = languageFilename;
    }

}
