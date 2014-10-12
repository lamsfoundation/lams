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
import com.tacitknowledge.util.migration.MigrationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Creates an DistributedAutoPatch environment using a configuration supplied by dependency
 * injection. Exports a hook that can be called to execute AutoPatch after configuration.
 *
 * @author Mike Hardy (mike@tacitknowledge.com)
 */
public class DistributedAutoPatchService extends DistributedJdbcMigrationLauncherFactory
{
    /**
     * Class logger
     */
    private static Log log = LogFactory.getLog(AutoPatchService.class);

    /**
     * The name of the schema to patch
     */
    private String systemName = null;

    /**
     * The data source used to store data about all the systems being patched
     */
    private DataSource dataSource = null;

    /**
     * The type of database
     */
    private String databaseType = null;

    /**
     * The AutoPatchServices this object should control
     */
    private AutoPatchService[] controlledSystems = null;

    /**
     * The patch to the post-patch tasks
     */
    private String postPatchPath = null;

    /**
     * Whether we actually want to apply patches, or just look
     */
    private boolean readOnly = false;

    /**
     * The number of times to wait for the lock before overriding it. -1 is infinite
     */
    private int lockPollRetries = -1;

    /**
     * Patches all of the databases in your distributed system, if necessary.
     *
     * @throws MigrationException if an unexpected error occurs
     */
    public void patch() throws MigrationException
    {
        DistributedJdbcMigrationLauncher launcher = getLauncher();

        try
        {
            log.info("Applying patches....");
            int patchesApplied = launcher.doMigrations();
            log.info("Applied " + patchesApplied + " "
                    + (patchesApplied == 1 ? "patch" : "patches") + ".");
        }
        catch (MigrationException e)
        {
            throw new MigrationException("Error applying patches", e);
        }
    }

    /**
     * Configure and return a DistributedJdbcMigrationLauncher to use for patching
     *
     * @return DistributedJdbcMigrationLauncher configured from injected properties
     */
    public DistributedJdbcMigrationLauncher getLauncher()
    {
        DistributedJdbcMigrationLauncher launcher = getDistributedJdbcMigrationLauncher();
        launcher.addContext(getContext());

        // Grab the controlled systems and subjugate them
        HashMap controlledLaunchers = new HashMap();
        for (int i = 0; i < controlledSystems.length; i++)
        {
            AutoPatchService controlledSystem = controlledSystems[i];
            JdbcMigrationLauncher subLauncher = controlledSystem.getLauncher();

            // We need the system name, all of the contexts should be the same, take the first
            // FIXME should the system name be on the launcher instead of the context?
            Map subContextMap = subLauncher.getContexts();
            String subSystemName =
                    ((JdbcMigrationContext) subContextMap.keySet().iterator().next()).getSystemName();
            controlledLaunchers.put(subSystemName, subLauncher);

            // Make sure the controlled migration process gets migration events
            launcher.getMigrationProcess().addListener(subLauncher);
        }

        ((DistributedMigrationProcess) launcher.getMigrationProcess())
                .setControlledSystems(controlledLaunchers);
        launcher.setPostPatchPath(getPostPatchPath());
        launcher.setReadOnly(isReadOnly());
        launcher.setLockPollRetries(getLockPollRetries());

        return launcher;
    }

    /**
     * Configure and return a DataSourceMigrationContext from this object's
     * injected properties
     *
     * @return DataSourceMigrationContext configured from injected properties
     */
    private DataSourceMigrationContext getContext()
    {
        DataSourceMigrationContext context = getDataSourceMigrationContext();
        context.setSystemName(getSystemName());
        context.setDatabaseType(new DatabaseType(getDatabaseType()));
        context.setDataSource(getDataSource());
        return context;
    }

    /**
     * @return Returns the dataSource.
     */
    public DataSource getDataSource()
    {
        return dataSource;
    }

    /**
     * @param dataSource The dataSource to set.
     */
    public void setDataSource(DataSource dataSource)
    {
        this.dataSource = dataSource;
    }

    /**
     * @return Returns the systemName.
     */
    public String getSystemName()
    {
        return systemName;
    }

    /**
     * @param systemName The systemName to set.
     */
    public void setSystemName(String systemName)
    {
        this.systemName = systemName;
    }

    /**
     * @return Returns the databaseType.
     */
    public String getDatabaseType()
    {
        return databaseType;
    }

    /**
     * @param dialect The databaseType to set.
     */
    public void setDatabaseType(String dialect)
    {
        this.databaseType = dialect;
    }

    /**
     * @return the controlled AutoPatchService objects
     */
    public AutoPatchService[] getControlledSystems()
    {
        return controlledSystems;
    }

    /**
     * Takes an Array of AutoPatchService objects to control when patching
     *
     * @param controlledSystems the AutoPatchService objects to control
     */
    public void setControlledSystems(AutoPatchService[] controlledSystems)
    {
        this.controlledSystems = controlledSystems;
    }

    /**
     * @return Returns the postPatchPath.
     */
    public String getPostPatchPath()
    {
        return postPatchPath;
    }

    /**
     * @param postPatchPath The postPatchPath to set.
     */
    public void setPostPatchPath(String postPatchPath)
    {
        this.postPatchPath = postPatchPath;
    }

    /**
     * See if we are actually applying patches, or if it is just readonly
     *
     * @return boolean true if we will skip application
     */
    public boolean isReadOnly()
    {
        return readOnly;
    }

    /**
     * Set whether or not to actually apply patches
     *
     * @param readOnly boolean true if we should skip application
     */
    public void setReadOnly(boolean readOnly)
    {
        this.readOnly = readOnly;
    }

    /**
     * Return the number of times to poll the lock before overriding it. -1 is infinite
     *
     * @return int either -1 for infinite or number of times to poll before override
     */
    public int getLockPollRetries()
    {
        return lockPollRetries;
    }

    /**
     * Set the number of times to poll the lock before overriding it. -1 is infinite
     *
     * @param lockPollRetries either -1 for infinite or number of times to poll before override
     */
    public void setLockPollRetries(int lockPollRetries)
    {
        this.lockPollRetries = lockPollRetries;
    }
}
