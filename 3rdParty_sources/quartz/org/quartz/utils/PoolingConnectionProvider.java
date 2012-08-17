/* 
 * Copyright 2004-2005 OpenSymphony 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not 
 * use this file except in compliance with the License. You may obtain a copy 
 * of the License at 
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0 
 *   
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT 
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the 
 * License for the specific language governing permissions and limitations 
 * under the License.
 * 
 */

/*
 * Previously Copyright (c) 2001-2004 James House
 */
package org.quartz.utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;

/**
 * <p>
 * A <code>ConnectionProvider</code> implementation that creates it's own
 * pool of connections.
 * </p>
 * 
 * <p>
 * This class uses <a href="http://jakarta.apache.org/commons/index.html">DBCP
 * </a>, an Apache-Jakarta-Commons product.
 * </p>
 * 
 * @see DBConnectionManager
 * @see ConnectionProvider
 * 
 * @author Sharada Jambula
 * @author James House
 * @author Mohammad Rezaei
 */
public class PoolingConnectionProvider implements ConnectionProvider {

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Constants.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    public static final String DB_PROPS_PREFIX = "org.quartz.db.";

    public static final String DB_JNDI_DATASOURCE_URL = "jndiURL";

    // The JDBC database driver
    public static final String DB_DRIVER = "driver";

    // The JDBC database URL
    public static final String DB_URL = "URL";

    // The database user name
    public static final String DB_USER = "user";

    // The database user password
    public static final String DB_PASSWORD = "password";

    public static final String DB_MAX_CONNECTIONS = "maxConnections";

    public static final String DB_VALIDATION_QUERY = "validationQuery";

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Data members.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private BasicDataSource datasource;

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Constructors.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    public PoolingConnectionProvider(String dbDriver, String dbURL,
            String dbUser, String dbPassword, int maxConnections,
            String dbValidationQuery) throws SQLException {

        initialize(dbDriver, dbURL, dbUser, dbPassword, maxConnections,
                dbValidationQuery);

    }

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Interface.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private void initialize(String dbDriver, String dbURL, String dbUser,
            String dbPassword, int maxConnections, String dbValidationQuery)
            throws SQLException {
        if (dbDriver == null)
                throw new SQLException("DB driver class name cannot be null!");
        if (dbURL == null) throw new SQLException("DB URL cannot be null!");
        if (maxConnections < 0)
                throw new SQLException(
                        "Max connections must be greater than zero!");

        datasource = new BasicDataSource();
        datasource.setDriverClassName(dbDriver);
        datasource.setUrl(dbURL);
        datasource.setUsername(dbUser);
        datasource.setPassword(dbPassword);
        datasource.setMaxActive(maxConnections);
        if (dbValidationQuery != null)
                datasource.setValidationQuery(dbValidationQuery);
    }

    /**
     * <p>
     * Create a connection pool using the given properties.
     * </p>
     * 
     * <p>
     * The properties passed should contain either
     * <UL>
     * <LI>JNDI DataSource URL {@link #DB_JNDI_DATASOURCE_URL}
     * </UL>
     * or
     * <UL>
     * <LI>{@link #DB_DRIVER}- The database driver class name
     * <LI>{@link #DB_URL}- The database URL
     * <LI>{@link #DB_USER}- The database user
     * <LI>{@link #DB_PASSWORD}- The database password
     * <LI>{@link #DB_MAX_CONNECTIONS}- The maximum # connections in the pool
     * </UL>
     * <P>
     * 
     * @param config
     *          configuration properties
     * @exception SQLException
     *              if an error occurs
     */
    public PoolingConnectionProvider(Properties config) throws SQLException {
        PropertiesParser cfg = new PropertiesParser(config);
        String url = config.getProperty(DB_URL);
        try {
            initialize(config.getProperty(DB_DRIVER), url, config
                    .getProperty(DB_USER), config.getProperty(DB_PASSWORD), cfg
                    .getIntProperty(DB_MAX_CONNECTIONS, 3), cfg
                    .getStringProperty(DB_VALIDATION_QUERY));
        } catch (Exception e) {
            throw new SQLException("DBPool '" + url
                    + "' could not be created: " + e.toString());
        }
    }

    public Connection getConnection() throws SQLException {
        return this.datasource.getConnection();
    }
    
    public void shutdown() throws SQLException {
        this.datasource.close();
    }    
}
