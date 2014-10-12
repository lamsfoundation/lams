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
 * A partial <code>DataSource</code> implementation that simply wraps a single,
 * already opened <code>Connection</code>.
 * <p/>
 * Only the two <code>getConnection</code> methods are supported.
 *
 * @author Scott Askew (scott@tacitknowledge.com)
 */
public class ConnectionWrapperDataSource implements DataSource
{
    /**
     * The message used in <code>UnsupportedOperationException</code>s.
     */
    public static final String UNSUPPORTED_OPERATION_EXCEPTION_MSG
            = ConnectionWrapperDataSource.class
            + " is not a fully functioning DataSource and only"
            + " supports the getConnection methods.";

    /**
     * The underlying connection
     */
    private Connection connection = null;

    /**
     * Creates a new <code>ConnectionWrapperDataSource</code>.
     *
     * @param connection the connection to use for this data source
     */
    public ConnectionWrapperDataSource(Connection connection)
    {
        this.connection = connection;
    }

    /**
     * {@inheritDoc}
     */
    public Connection getConnection() throws SQLException
    {
        return connection;
    }

    /**
     * {@inheritDoc}
     */
    public Connection getConnection(String user, String pass) throws SQLException
    {
        return connection;
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
     * {@inheritDoc}
     */
    public boolean isWrapperFor(Class iface)
    {
        return connection != null && iface.isAssignableFrom(connection.getClass());
    }

    /**
     * {@inheritDoc}
     */
    public Object unwrap(Class iface)
    {
        return connection;
    }
}
