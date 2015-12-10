/*
  Copyright (c) 2007, 2014, Oracle and/or its affiliates. All rights reserved.

  The MySQL Connector/J is licensed under the terms of the GPLv2
  <http://www.gnu.org/licenses/old-licenses/gpl-2.0.html>, like most MySQL Connectors.
  There are special exceptions to the terms and conditions of the GPLv2 as it is applied to
  this software, see the FOSS License Exception
  <http://www.mysql.com/about/legal/licensing/foss-exception.html>.

  This program is free software; you can redistribute it and/or modify it under the terms
  of the GNU General Public License as published by the Free Software Foundation; version 2
  of the License.

  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
  See the GNU General Public License for more details.

  You should have received a copy of the GNU General Public License along with this
  program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth
  Floor, Boston, MA 02110-1301  USA

 */

package com.mysql.jdbc.interceptors;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSetInternalMethods;
import com.mysql.jdbc.Statement;
import com.mysql.jdbc.StatementInterceptor;
import com.mysql.jdbc.Util;

public class ServerStatusDiffInterceptor implements StatementInterceptor {

    private Map<String, String> preExecuteValues = new HashMap<String, String>();

    private Map<String, String> postExecuteValues = new HashMap<String, String>();

    public void init(Connection conn, Properties props) throws SQLException {

    }

    public ResultSetInternalMethods postProcess(String sql, Statement interceptedStatement, ResultSetInternalMethods originalResultSet, Connection connection)
            throws SQLException {

        if (connection.versionMeetsMinimum(5, 0, 2)) {
            populateMapWithSessionStatusValues(connection, this.postExecuteValues);

            connection.getLog().logInfo("Server status change for statement:\n" + Util.calculateDifferences(this.preExecuteValues, this.postExecuteValues));
        }

        return null; // we don't actually modify a result set

    }

    private void populateMapWithSessionStatusValues(Connection connection, Map<String, String> toPopulate) throws SQLException {
        java.sql.Statement stmt = null;
        java.sql.ResultSet rs = null;

        try {
            toPopulate.clear();

            stmt = connection.createStatement();
            rs = stmt.executeQuery("SHOW SESSION STATUS");
            Util.resultSetToMap(toPopulate, rs);
        } finally {
            if (rs != null) {
                rs.close();
            }

            if (stmt != null) {
                stmt.close();
            }
        }
    }

    public ResultSetInternalMethods preProcess(String sql, Statement interceptedStatement, Connection connection) throws SQLException {

        if (connection.versionMeetsMinimum(5, 0, 2)) {
            populateMapWithSessionStatusValues(connection, this.preExecuteValues);
        }

        return null; // we don't actually modify a result set
    }

    public boolean executeTopLevelOnly() {
        return true;
    }

    public void destroy() {

    }
}
