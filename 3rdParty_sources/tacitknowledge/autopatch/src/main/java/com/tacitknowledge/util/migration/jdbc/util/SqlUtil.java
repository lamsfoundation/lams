/* Copyright 2004 Tacit Knowledge
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tacitknowledge.util.migration.jdbc.util;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Utility class for dealing with JDBC.
 *
 * @author Scott Askew (scott@tacitknowledge.com)
 */
public final class SqlUtil
{
    /**
     * Class logger
     */
    private static Log log = LogFactory.getLog(SqlUtil.class);

    /**
     * Hidden constructor for utility class
     */
    private SqlUtil()
    {
        // Hidden
    }

    
    private static void closeResultSet(ResultSet rs) {
    	 if (rs != null)
         {
             try
             {
                 boolean rsIsOpen = true;
                 try
                 {
                     rsIsOpen = !rs.isClosed();
                 }
                 catch (AbstractMethodError e)
                 {
                     log.debug("AbstractMethodError closing ResultSet.  ResultSet might be a DelegatingResultSet with a badly implemented (i.e. missing) delegation to isClosed().", e);
                 }
                 if (rsIsOpen)
                 {
                     log.debug("Closing ResultSet: " + rs.toString());
                     rs.close();
                 }
                 else
                 {
                     log.debug("ResultSet (" + rs.toString() + ") already closed.");
                 }
             }
             catch (SQLException e)
             {
                 log.error("Error closing ResultSet", e);
             }
         }
    }
    
    private static void closeStatement(Statement stmt) {
    	if (stmt != null)
        {
            try
            {
                boolean stmtIsOpen = true;
                try
                {
                    stmtIsOpen = !stmt.isClosed();
                }
                catch (AbstractMethodError e)
                {
                    log.debug("AbstractMethodError closing Statement.", e);
                }
                catch (SQLException e)
                {
                    //ignore - we don't care if isClosed() throws an SQLException. And it happens that
                	//org.jboss.jca.adapters.jdbc.WrappedStatement.isClosed() does.
                }
                if (stmtIsOpen)
                {
                    log.debug("Closing Statement: " + stmt.toString());
                    stmt.close();
                }
                else
                {
                    log.debug("Statement (" + stmt.toString() + ") already closed.");
                }
            }
            catch (SQLException e)
            {
                log.error("Error closing Statement", e);
            }
        }
    }
    
    private static void closeConnection(Connection conn) {
    	if (conn != null)
        {
            try
            {
                boolean connIsOpen = true;
                try
                {
                    connIsOpen = !conn.isClosed();
                }
                catch (AbstractMethodError e)
                {
                    log.debug("AbstractMethodError closing Connection.", e);
                }
                if (connIsOpen)
                {
                    log.debug("Closing Connection " + conn.toString());
                    conn.close();
                }
                else
                {
                    log.debug("Connection (" + conn.toString() + ") already closed.");
                }
            }
            catch (SQLException e)
            {
                log.error("Error closing Connection", e);
            }
        }
    }
    
    /**
     * Ensures the given connection, statement, and result are properly closed.
     *
     * @param conn the connection to close; may be <code>null</code>
     * @param stmt the statement to close; may be <code>null</code>
     * @param rs   the result set to close; may be <code>null</code>
     */
    public static void close(Connection conn, Statement stmt, ResultSet rs)
    {
		closeResultSet(rs);
		closeStatement(stmt);
		closeConnection(conn);
	}

    /**
     * Established and returns a connection based on the specified parameters.
     *
     * @param driver the JDBC driver to use
     * @param url    the database URL
     * @param user   the username
     * @param pass   the password
     * @return a JDBC connection
     * @throws ClassNotFoundException if the driver could not be loaded
     * @throws SQLException           if a connnection could not be made to the database
     */
    public static Connection getConnection(String driver, String url, String user, String pass)
            throws ClassNotFoundException, SQLException
    {
        Connection conn = null;
        try
        {
            Class.forName(driver);
            log.debug("Getting Connection to " + url);
            conn = DriverManager.getConnection(url, user, pass);
        }
        catch (Exception e)
        {
            /* work around for DriverManager 'feature'.  
             * In some cases, the jdbc driver jar is injected into a new 
             * child classloader (for example, maven provides different 
             * class loaders for different build lifecycle phases). 
             * 
             * Since DriverManager uses the calling class' loader instead 
             * of the current context's loader, it fails to find the driver.
             * 
             * Our work around is to give the current context's class loader 
             * a shot at finding the driver in cases where DriverManager fails.  
             * This 'may be' a security hole which is why DriverManager implements 
             * things in such a way that it doesn't use the current thread context class loader.
             */
            try
            {
                Class driverClass =
                        Class.forName(driver, true, Thread.currentThread().getContextClassLoader());
                Driver driverImpl = (Driver) driverClass.newInstance();
                Properties props = new Properties();
                props.put("user", user);
                props.put("password", pass);
                conn = driverImpl.connect(url, props);
            }
            catch (InstantiationException ie)
            {
                log.debug(ie);
                throw new SQLException(ie.getMessage());
            }
            catch (IllegalAccessException iae)
            {
                log.debug(iae);
                throw new SQLException(iae.getMessage());
            }
        }

        return conn;
    }
}
