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

import com.tacitknowledge.util.migration.DistributedMigrationProcess;
import com.tacitknowledge.util.migration.MigrationContext;
import com.tacitknowledge.util.migration.MigrationException;
import com.tacitknowledge.util.migration.jdbc.util.ConfigurationUtil;
import com.tacitknowledge.util.migration.jdbc.util.NonPooledDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * Creates and configures a new <code>DistributedJdbcMigrationContext</code> based on the values
 * in the <em>migration.properties</em> file for the given system.  This is a convenience
 * class for systems that need to initialize the autopatch framework but do not to or can not
 * configure the framework themselves.
 * <p>
 * This factory expects a file named <code>migration.properties</code> to be in the
 * root of the  class path.  This file must contain these properties (where <i>systemName</i>
 * is the name of the system being patched):
 * <table>
 * <tr><th>Key</th><th>description</th></tr>
 * <tr><td><i>systemName</i>.context</td><td>The context to use for orchestration</td></tr>
 * <tr><td><i>systemName</i>.controlled.systems</td><td>comma-delimited systems to manage</td></tr>
 * </table>
 * <p>
 * For each system in the controlled systems list, the properties file should contain
 * information as directed in the documenation for JdbcMigrationLauncher.
 * <p/>
 * <p>
 * The <i>systemName</i>.listeners property only applies to the top level system name which manages
 * all the sub-systems.
 * </p>
 * <p/>
 * If a new database node is detected in the migration.properties, the default behaviour is to
 * stop the patch process.  To force the new node to be 'brought up to date' with the other
 * nodes, then set a system property named 'forcesync'.  The value is not important, merely the
 * existance is sufficient.
 *
 * @author Mike Hardy (mike@tacitknowledge.com)
 * @author Alex Soto (apsoto@gmail.com)
 * @see com.tacitknowledge.util.migration.jdbc.JdbcMigrationLauncher
 */
public class DistributedJdbcMigrationLauncherFactory extends JdbcMigrationLauncherFactory
{
    /**
     * Class logger
     */
    private static Log log = LogFactory.getLog(DistributedJdbcMigrationLauncherFactory.class);

    /**
     * Creates and configures a new <code>JdbcMigrationLauncher</code> based on the
     * values in the <em>migration.properties</em> file for the given system.
     *
     * @param systemName the system to patch
     * @param propFile   name of the properties file in the classpath
     * @return a fully configured <code>DistributedJdbcMigrationLauncher</code>.
     * @throws MigrationException if an unexpected error occurs
     */
    public JdbcMigrationLauncher createMigrationLauncher(String systemName, String propFile)
            throws MigrationException
    {
        log.info("Creating DistributedJdbcMigrationLauncher for system " + systemName);
        DistributedJdbcMigrationLauncher launcher = getDistributedJdbcMigrationLauncher();
        configureFromMigrationProperties(launcher, systemName, propFile);
        return launcher;
    }

    /**
     * Creates and configures a new <code>JdbcMigrationLauncher</code> based on the
     * values in the <em>migration.properties</em> file for the given system.
     *
     * @param systemName the system to patch
     * @return a fully configured <code>DistributedJdbcMigrationLauncher</code>.
     * @throws MigrationException if an unexpected error occurs
     */
    public JdbcMigrationLauncher createMigrationLauncher(String systemName)
            throws MigrationException
    {
        log.info("Creating DistributedJdbcMigrationLauncher for system " + systemName);
        DistributedJdbcMigrationLauncher launcher = getDistributedJdbcMigrationLauncher();
        configureFromMigrationProperties(launcher, systemName,
                MigrationContext.MIGRATION_CONFIG_FILE);
        return launcher;
    }

    /**
     * Get a new DistributedJdbcMigrationLauncher
     *
     * @return DistributedJdbcMigrationLauncher
     */
    public DistributedJdbcMigrationLauncher getDistributedJdbcMigrationLauncher()
    {
        return new DistributedJdbcMigrationLauncher();
    }

    /**
     * Loads the configuration from the migration config properties file.
     *
     * @param launcher   the launcher to configure
     * @param systemName the name of the system
     * @param propFile   the name of the properties file on the classpath
     * @throws MigrationException if an unexpected error occurs
     */
    private void configureFromMigrationProperties(DistributedJdbcMigrationLauncher launcher,
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
                Properties props = new Properties();
                props.load(is);

                configureFromMigrationProperties(launcher, systemName, props, propFile);
            }
            catch (IOException e)
            {
                throw new MigrationException("Error reading in migration properties file", e);
            }
            finally
            {
                try
                {
                    is.close();
                }
                catch (IOException ioe)
                {
                    throw new MigrationException("Error closing migration properties file", ioe);
                }
            }
        }
        else
        {
            throw new MigrationException("Unable to find migration properties file '"
                    + propFile + "'");
        }
    }

    /**
     * Configure the launcher from the provided properties, system name
     *
     * @param launcher     The launcher to configure
     * @param systemName   The name of the system we're configuring
     * @param props        The Properties object with our configuration information
     * @param propFileName the property file name for configuration
     * @throws IllegalArgumentException if a required parameter is missing
     * @throws MigrationException       if there is problem setting the context into the launcher
     */
    void configureFromMigrationProperties(DistributedJdbcMigrationLauncher launcher,
            String systemName, Properties props,
            String propFileName)
            throws IllegalArgumentException, MigrationException
    {
        // Get the name of the context to use for our patch information
        String patchContext =
                ConfigurationUtil.getRequiredParam(props, systemName + ".context");

        // Set up the data source
        NonPooledDataSource ds = new NonPooledDataSource();
        ds.setDriverClass(ConfigurationUtil.getRequiredParam(props, patchContext + ".jdbc.driver"));
        ds.setDatabaseUrl(ConfigurationUtil.getRequiredParam(props, patchContext + ".jdbc.url"));
        ds.setUsername(ConfigurationUtil.getRequiredParam(props, patchContext + ".jdbc.username"));
        ds.setPassword(ConfigurationUtil.getRequiredParam(props, patchContext + ".jdbc.password"));

        launcher.setMigrationStrategy(props.getProperty("migration.strategy"));
        // Get any post-patch task paths
        launcher.setPostPatchPath(props.getProperty(patchContext + ".postpatch.path"));

        // See if they want to run in read-only mode
        launcher.setReadOnly(false);
        if ("true".equals(props.getProperty(systemName + ".readonly")))
        {
            launcher.setReadOnly(true);
        }

        // See if they want to override the lock after a certain amount of time
        String lockPollRetries = props.getProperty(systemName + ".lockPollRetries");
        if (lockPollRetries != null)
        {
            launcher.setLockPollRetries(Integer.parseInt(lockPollRetries));
        }

        // see if forcesync specified.  Value doesn't matter, just presence of system property enables syncing
        String forceSync = ConfigurationUtil.getOptionalParam("forcesync", System.getProperties(), null, 0);
        if (forceSync != null)
        {
            ((DistributedMigrationProcess) launcher.getMigrationProcess()).setForceSync(true);
        }

        // Set up the JDBC migration context; accepts one of two property names
        DataSourceMigrationContext context = getDataSourceMigrationContext();
        String databaseType = ConfigurationUtil.getRequiredParam(props,
                patchContext + ".jdbc.database.type", patchContext + ".jdbc.dialect");
        context.setDatabaseType(new DatabaseType(databaseType));

        // Finish setting up the context
        context.setSystemName(systemName);
        context.setDataSource(ds);

        // setup the user-defined listeners
        List userDefinedListeners = loadMigrationListeners(systemName, props);
        launcher.getMigrationProcess().addListeners(userDefinedListeners);

        // done reading in config, set launcher's context
        // FIXME only using one context here, would a distributed one ever go into multiple nodes?
        launcher.addContext(context);

        // Get our controlled systems, and instantiate their launchers
        HashMap controlledSystems = new HashMap();
        String[] controlledSystemNames =
                ConfigurationUtil.getRequiredParam(props,
                        systemName + ".controlled.systems").split(",");
        for (int i = 0; i < controlledSystemNames.length; i++)
        {
            log.info("Creating controlled patch executor for system " + controlledSystemNames[i]);
            //TODO should be injected
            JdbcMigrationLauncherFactory factory =
                    new JdbcMigrationLauncherFactoryLoader().createFactory();
            JdbcMigrationLauncher subLauncher =
                    factory.createMigrationLauncher(controlledSystemNames[i], propFileName);

            controlledSystems.put(controlledSystemNames[i], subLauncher);

            // Make sure the controlled migration process gets migration events
            launcher.getMigrationProcess().addListener(subLauncher);
        }

        // communicate our new-found controlled systems to the migration process
        ((DistributedMigrationProcess) launcher.getMigrationProcess())
                .setControlledSystems(controlledSystems);
    }
}
