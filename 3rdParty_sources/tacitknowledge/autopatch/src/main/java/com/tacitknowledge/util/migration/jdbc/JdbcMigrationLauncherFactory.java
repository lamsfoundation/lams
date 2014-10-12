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
import com.tacitknowledge.util.migration.MigrationListener;
import com.tacitknowledge.util.migration.jdbc.util.ConfigurationUtil;
import com.tacitknowledge.util.migration.jdbc.util.NonPooledDataSource;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Creates and configures new <code>JdbcMigrationContext</code> objects based on the values
 * in the <em>migration.properties</em> file for the given system.  This is a convenience
 * class for systems that need to initialize the autopatch framework but do not to or can not
 * configure the framework themselves.
 * <p/>
 * This factory expects a file named <code>migration.properties</code> to be in the root of the
 * class path.  This file must contain these properties (where <i>systemName</i> is the name of
 * the system being patched):
 * <table>
 * <tr><th>Key</th><th>description</th></tr>
 * <tr><td><i>systemName</i>.patch.path</td><td></td></tr>
 * <tr><td><i>systemName</i>.jdbc.database.type</td>
 * <td>The database type; also accepts <i>systemName</i>.jdbc.dialect</td></tr>
 * <tr><td><i>systemName</i>.jdbc.driver</td><td>The JDBC driver to use</td></tr>
 * <tr><td><i>systemName</i>.jdbc.url</td><td>The JDBC URL to the database</td></tr>
 * <tr><td><i>systemName</i>.jdbc.username</td><td>The database user name</td></tr>
 * <tr><td><i>systemName</i>.jdbc.password</td><td>The database password</td></tr>
 * </table>
 * <p/>
 * Optional properties include:
 * <table>
 * <tr><td><i>systemName</i>.postpatch.path</td><td></td></tr>
 * <tr><td><i>systemName</i>.readonly</td><td>boolean true to skip patch application</td></tr>
 * <tr><td><i>systemName</i>.jdbc.systems</td>
 * <td>Set of names for multiple JDBC connections that
 * should all have patches applied. Names will be
 * looked up in the same properties file as
 * <i>systemName.jdbcname</i>.database.type, where
 * all of the jdbc entries above should be present</td>
 * </tr>
 * <tr><td><i>systemName</i>.listeners</td><td>Comma separated list of fully qualified java class names that implement {@link MigrationListener}</td></tr>
 * </table>
 *
 * @author Scott Askew (scott@tacitknowledge.com)
 * @author Alex Soto (apsoto@gmail.com)
 */
public class JdbcMigrationLauncherFactory
{
    /**
     * Class logger
     */
    private static Log log = LogFactory.getLog(JdbcMigrationLauncherFactory.class);

    /**
     * Creates and configures a new <code>JdbcMigrationLauncher</code> based on the
     * values in the <em>migration.properties</em> file for the given system.
     *
     * @param systemName the system to patch
     * @param propFile   the name of the property file to configure from
     * @return a fully configured <code>JdbcMigrationLauncher</code>.
     * @throws MigrationException if an unexpected error occurs
     */
    public JdbcMigrationLauncher createMigrationLauncher(String systemName, String propFile)
            throws MigrationException
    {
        log.info("Creating JdbcMigrationLauncher for system " + systemName);
        JdbcMigrationLauncher launcher = getJdbcMigrationLauncher();
        configureFromMigrationProperties(launcher, systemName, propFile);
        return launcher;
    }

    /**
     * Creates and configures a new <code>JdbcMigrationLauncher</code> based on the
     * values in the <em>migration.properties</em> file for the given system.
     *
     * @param systemName the system to patch
     * @return a fully configured <code>JdbcMigrationLauncher</code>.
     * @throws MigrationException if an unexpected error occurs
     */
    public JdbcMigrationLauncher createMigrationLauncher(String systemName)
            throws MigrationException
    {
        log.info("Creating JdbcMigrationLauncher for system " + systemName);
        JdbcMigrationLauncher launcher = getJdbcMigrationLauncher();
        configureFromMigrationProperties(launcher, systemName);
        return launcher;
    }

    /**
     * Creates and configures a new <code>JdbcMigrationLauncher</code> based on the
     * values in the servlet context and JNDI for a web-application.
     *
     * @param sce the name of the context event to use in getting properties
     * @return a fully configured <code>JdbcMigrationLauncher</code>.
     * @throws MigrationException if an unexpected error occurs
     */
    public JdbcMigrationLauncher createMigrationLauncher(ServletContextEvent sce)
            throws MigrationException
    {
        JdbcMigrationLauncher launcher = getJdbcMigrationLauncher();
        configureFromServletContext(launcher, sce);
        return launcher;
    }

    /**
     * Used to configure the migration launcher with properties from a servlet
     * context.  You do not need migration.properties to use this method.
     *
     * @param launcher the launcher to configure
     * @param sce      the event to get the context and associated parameters from
     * @throws MigrationException if a problem with the look up in JNDI occurs
     */
    private void configureFromServletContext(JdbcMigrationLauncher launcher,
            ServletContextEvent sce) throws MigrationException
    {
        String readOnly = sce.getServletContext().getInitParameter("migration.readonly");
        launcher.setReadOnly(false);
        if ("true".equals(readOnly))
        {
            launcher.setReadOnly(true);
        }

        // See if they want to override the lock after a certain amount of time
        String lockPollRetries =
                sce.getServletContext().getInitParameter("migration.lockPollRetries");
        if (lockPollRetries != null)
        {
            launcher.setLockPollRetries(Integer.parseInt(lockPollRetries));
        }

        String patchPath = ConfigurationUtil.getRequiredParam("migration.patchpath", sce, this);
        launcher.setPatchPath(patchPath);

        String postPatchPath = sce.getServletContext().getInitParameter("migration.postpatchpath");
        launcher.setPostPatchPath(postPatchPath);

        String databases = sce.getServletContext().getInitParameter("migration.jdbc.systems");
        String[] databaseNames;
        if ((databases == null) || "".equals(databases))
        {
            databaseNames = new String[1];
            databaseNames[0] = "";
            log.debug("jdbc.systems was null or empty, not multi-node");
        }
        else
        {
            databaseNames = databases.split(",");
            log.debug("jdbc.systems was set to " + databases + ", configuring multi-node");
        }

        for (int i = 0; i < databaseNames.length; i++)
        {
            String databaseName = databaseNames[i];
            if (databaseName != "")
            {
                databaseName = databaseName + ".";
            }
            String databaseType =
                    ConfigurationUtil.getRequiredParam("migration." + databaseName + "databasetype",
                            sce, this);
            String systemName =
                    ConfigurationUtil.getRequiredParam("migration.systemname",
                            sce, this);
            String dataSource =
                    ConfigurationUtil.getRequiredParam("migration." + databaseName + "datasource",
                            sce, this);

            DataSourceMigrationContext context = getDataSourceMigrationContext();
            context.setSystemName(systemName);
            context.setDatabaseType(new DatabaseType(databaseType));

            try
            {
                Context ctx = new InitialContext();
                if (ctx == null)
                {
                    throw new IllegalArgumentException("A jndi context must be "
                            + "present to use this configuration.");
                }
                DataSource ds = (DataSource) ctx.lookup("java:comp/env/" + dataSource);
                context.setDataSource(ds);
                log.debug("adding context with datasource " + dataSource
                        + " of type " + databaseType);
                launcher.addContext(context);
            }
            catch (NamingException e)
            {
                throw new MigrationException("Problem with JNDI look up of " + dataSource, e);
            }
        }
    }

    /**
     * Loads the configuration from the migration config properties file.
     *
     * @param launcher   the launcher to configure
     * @param systemName the name of the system
     * @throws MigrationException if an unexpected error occurs
     */
    private void configureFromMigrationProperties(JdbcMigrationLauncher launcher,
            String systemName)
            throws MigrationException
    {
        configureFromMigrationProperties(launcher,
                systemName,
                MigrationContext.MIGRATION_CONFIG_FILE);
    }

    /**
     * Loads the configuration from the migration config properties file.
     *
     * @param launcher   the launcher to configure
     * @param systemName the name of the system
     * @param propFile   the name of the prop file to configure from
     * @throws MigrationException if an unexpected error occurs
     */
    private void configureFromMigrationProperties(JdbcMigrationLauncher launcher,
            String systemName,
            String propFile)
            throws MigrationException
    {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream is = cl.getResourceAsStream(propFile);
        if (is != null)
        {
            try
            {
                Properties props = loadProperties(is);

                configureFromMigrationProperties(launcher, systemName, props);
            }
            catch (IOException e)
            {
                throw new MigrationException("Error reading in autopatch properties file", e);
            }
            finally
            {
                try
                {
                    is.close();
                }
                catch (IOException ioe)
                {
                    throw new MigrationException("Error closing autopatch properties file", ioe);
                }
            }
        }
        else
        {
            throw new MigrationException("Unable to find autopatch properties file '"
                    + propFile + "'");
        }
    }

    protected Properties loadProperties(InputStream is) throws IOException
    {
        Properties props = new Properties();
        props.load(is);
        return props;
    }

    /**
     * Configure the launcher from the provided properties, system name
     *
     * @param launcher The launcher to configure
     * @param system   The name of the system we're configuring
     * @param props    The Properties object with our configuration information
     * @throws IllegalArgumentException if a required parameter is missing
     * @throws MigrationException
     */
    void configureFromMigrationProperties(JdbcMigrationLauncher launcher, String system,
            Properties props)
            throws IllegalArgumentException, MigrationException
    {

        launcher.setMigrationStrategy(props.getProperty("migration.strategy"));

        launcher.setPatchPath(ConfigurationUtil.getRequiredParam(props, system + ".patch.path"));
        launcher.setPostPatchPath(props.getProperty(system + ".postpatch.path"));
        launcher.setReadOnly(false);
        if ("true".equals(props.getProperty(system + ".readonly")))
        {
            launcher.setReadOnly(true);
        }

        // See if they want to override the lock after a certain amount of time
        String lockPollRetries = props.getProperty(system + ".lockPollRetries");
        if (lockPollRetries != null)
        {
            launcher.setLockPollRetries(Integer.parseInt(lockPollRetries));
        }

        // See if they want to change the amount of time to wait between lock polls
        String lockPollMillis = props.getProperty(system + ".lockPollMillis");
        if (lockPollMillis != null)
        {
            launcher.setLockPollMillis(Integer.parseInt(lockPollMillis));
        }

        // TODO refactor the database name extraction from this and the servlet example
        String databases = props.getProperty(system + ".jdbc.systems");
        String[] databaseNames;
        if ((databases == null) || "".equals(databases))
        {
            databaseNames = new String[1];
            databaseNames[0] = "jdbc";
        }
        else
        {
            databaseNames = databases.split(",");
        }

        for (int i = 0; i < databaseNames.length; i++)
        {
            String db = databaseNames[i];
            if (db != "")
            {
                db = "." + db;
            }

            // Set up the data source
            NonPooledDataSource dataSource = new NonPooledDataSource();
            dataSource.setDriverClass(ConfigurationUtil.getRequiredParam(props,
                    system + db + ".driver"));
            dataSource.setDatabaseUrl(ConfigurationUtil.getRequiredParam(props,
                    system + db + ".url"));
            dataSource.setUsername(ConfigurationUtil.getRequiredParam(props,
                    system + db + ".username"));
            dataSource.setPassword(ConfigurationUtil.getRequiredParam(props,
                    system + db + ".password"));

            // Set up the JDBC migration context; accepts one of two property names
            DataSourceMigrationContext context = getDataSourceMigrationContext();
            String databaseType =
                    ConfigurationUtil.getRequiredParam(props,
                            system + db + ".database.type",
                            system + db + ".dialect");
            log.debug("setting type to " + databaseType);
            context.setDatabaseType(new DatabaseType(databaseType));

            context.setDatabaseName(databaseNames[i]);

            // Finish setting up the context
            context.setSystemName(system);
            context.setDataSource(dataSource);

            // setup the user-defined listeners
            List userDefinedListeners = loadMigrationListeners(system, props);


            launcher.getMigrationProcess().addListeners(userDefinedListeners);

            // done reading in config, set launcher's context
            launcher.addContext(context);
        }
    }

    /**
     * Get a DataSourceMigrationContext
     *
     * @return DataSourceMigrationContext for use with the launcher
     */
    public DataSourceMigrationContext getDataSourceMigrationContext()
    {
        return new DataSourceMigrationContext();
    }

    /**
     * Get a JdbcMigrationLauncher
     *
     * @return JdbcMigrationLauncher
     */
    public JdbcMigrationLauncher getJdbcMigrationLauncher()
    {
        return new JdbcMigrationLauncher();
    }

    /**
     * Returns a list of MigrationListeners for the systemName specified in the properties.
     *
     * @param systemName The name of the system to load MigrationListeners for.
     * @param properties The properties that has migration listeners specified.
     * @return A List of zero or more MigrationListeners
     * @throws MigrationException if unable to load listeners.
     */
    protected List loadMigrationListeners(String systemName, Properties properties)
            throws MigrationException
    {
        try
        {
            List listeners = new ArrayList();
            String[] listenerClassNames = null;

            String commaSeparatedList = properties.getProperty(systemName + ".listeners");
            // if it's blank, then no listeners configured
            if (StringUtils.isNotBlank(commaSeparatedList))
            {
                listenerClassNames = commaSeparatedList.split(",");

                for (Iterator it = Arrays.asList(listenerClassNames).iterator(); it.hasNext();)
                {
                    String className = ((String) it.next()).trim();
                    // if it's blank, then there is likely a leading or trailing comma
                    if (StringUtils.isNotBlank(className))
                    {
                        Class c = Class.forName(className);
                        MigrationListener listener = (MigrationListener) c.newInstance();
                        listener.initialize(systemName, properties);
                        listeners.add(listener);
                    }
                }
            }

            return listeners;
        }
        catch (Exception e)
        {
            throw new MigrationException("Exception while loading migration listeners", e);
        }
    }
}

