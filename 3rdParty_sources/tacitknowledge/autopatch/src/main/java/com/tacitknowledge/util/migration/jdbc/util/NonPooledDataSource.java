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

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * A partial <code>DataSource</code> implementation that can be used in environments
 * where the containing application (usually an applications server) does not
 * provide a pooled DataSource.  This can be used to run migrations from standalone
 * applications.
 * <p/>
 * Only the two <code>getConnection</code> methods are supported.
 *
 * @author Scott Askew (scott@tacitknowledge.com)
 */
public class NonPooledDataSource implements DataSource
{
    /**
     * The message used in <code>UnsupportedOperationException</code>s.
     */
    public static final String UNSUPPORTED_OPERATION_EXCEPTION_MSG
            = NonPooledDataSource.class
            + " is not a fully functioning DataSource and only"
            + " supports the getConnection methods.";

    /**
     * The name of the database driver class
     */
    private String driverClass = null;

    /**
     * The JDBC URL
     */
    private String databaseUrl = null;

    /**
     * The user to login as
     */
    private String username = null;

    /**
     * The database password
     */
    private String password = null;

    /**
     * Creates a new <code>BasicDataSource</code>.
     */
    public NonPooledDataSource()
    {
        // Default constructor
    }

    /**
     * {@inheritDoc}
     */
    public Connection getConnection() throws SQLException
    {
        return getConnection(getUsername(), getPassword());
    }

    /**
     * {@inheritDoc}
     */
    public Connection getConnection(String user, String pass) throws SQLException
    {
        try
        {
            return SqlUtil.getConnection(getDriverClass(), getDatabaseUrl(),
                    getUsername(), getPassword());
        }
        catch (ClassNotFoundException e)
        {
            throw new SQLException("Could not locate JDBC driver " + driverClass);
        }
    }

    /**
     * {@inheritDoc}
     */
    public PrintWriter getLogWriter() throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_EXCEPTION_MSG);
    }

    /**
     * {@inheritDoc}
     */
    public void setLogWriter(PrintWriter arg0) throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_EXCEPTION_MSG);
    }

    /**
     * {@inheritDoc}
     */
    public int getLoginTimeout() throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_EXCEPTION_MSG);
    }

    /**
     * {@inheritDoc}
     */
    public void setLoginTimeout(int arg0) throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_EXCEPTION_MSG);
    }

    /**
     * Get the database URL
     *
     * @return the database url to use
     */
    public String getDatabaseUrl()
    {
        return databaseUrl;
    }

    /**
     * @param databaseUrl The databaseUrl to set.
     */
    public void setDatabaseUrl(String databaseUrl)
    {
        this.databaseUrl = databaseUrl;
    }

    /**
     * @return Returns the driverClass.
     */
    public String getDriverClass()
    {
        return driverClass;
    }

    /**
     * @param driverClass The driverClass to set.
     */
    public void setDriverClass(String driverClass)
    {
        this.driverClass = driverClass;
    }

    /**
     * @return Returns the password.
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * @param password The password to set.
     */
    public void setPassword(String password)
    {
        this.password = password;
    }

    /**
     * @return Returns the username.
     */
    public String getUsername()
    {
        return username;
    }

    /**
     * @param username The username to set.
     */
    public void setUsername(String username)
    {
        this.username = username;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isWrapperFor(Class iface)
    {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public Object unwrap(Class iface)
    {
        return null;
    }

    /**
     * Useful for debugging
     *
     * @return String with state
     */
    public String toString()
    {
        return
                "NonPooledDataSource["
                        + getDriverClass() + "/"
                        + getDatabaseUrl() + "/"
                        + getUsername() + "/"
                        + "(password omitted)" + "]";
    }
}
