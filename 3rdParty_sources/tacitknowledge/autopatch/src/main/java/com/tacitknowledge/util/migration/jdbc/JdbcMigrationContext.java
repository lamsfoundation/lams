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

package com.tacitknowledge.util.migration.jdbc;

import com.tacitknowledge.util.migration.MigrationContext;
import com.tacitknowledge.util.migration.MigrationException;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Contains the configuration and resources for a database patch run.
 *
 * @author Scott Askew (scott@tacitknowledge.com)
 */
public interface JdbcMigrationContext extends MigrationContext
{
    /**
     * Max length for the systemName columne
     */
    public static final int MAX_SYSTEMNAME_LENGTH = 30;

    /**
     * Returns a database connection to use. The creator
     * of the JdbcMigrationContext are responsible for closing the connection.
     *
     * @return the database connection to use
     * @throws SQLException if an unexpected error occurs
     */
    public Connection getConnection() throws SQLException;

    /**
     * {@inheritDoc}
     */
    public void commit() throws MigrationException;

    /**
     * {@inheritDoc}
     */
    public void rollback() throws MigrationException;

    /**
     * @return the name of the system to patch
     */
    public String getSystemName();

    /**
     * @return In a federated distributed configuration this should be some
     *         unique name to identify each node in the system.  In other
     *         configurations where is typically only one node per system,
     *         the system name should suffice.
     */
    public String getDatabaseName();

    /**
     * @return Returns the database type.
     */
    public DatabaseType getDatabaseType();
}