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

import com.tacitknowledge.util.migration.MigrationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Creates an AutoPatch environment using a configuration supplied by dependency
 * injection. Exports a hook that can be called to execute AutoPatch after configuration
 *
 * @author Scott Askew (scott@tacitknowledge.com)
 */
public class AutoPatchService extends JdbcMigrationLauncherFactory
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
     * The data source used to patch the schema
     */
    private DataSource dataSource = null;

    /**
     * The type of database
     */
    private String databaseType = null;

    /**
     * The path to the SQL patches
     */
    private String patchPath = null;

    /**
     * The patch to the post-patch tasks
     */
    private String postPatchPath = null;

    /**
     * Whether we really want to apply the patches, or just look
     */
    private boolean readOnly = false;

    /**
     * The number of times to wait for the lock before overriding it. -1 is infinite
     */
    private int lockPollRetries = -1;

    /**
     * A set of contexts, in case you want multi-node patches
     */
    private List contexts = new ArrayList();

    /**
     * Patches the database, if necessary.
     *
     * @throws MigrationException if an unexpected error occurs
     */
    public void patch() throws MigrationException
    {
        JdbcMigrationLauncher launcher = getLauncher();

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
     * Configure and return a JdbcMigrationLauncher to use for patching
     *
     * @return JdbcMigrationLauncher configured from injected properties
     */
    public JdbcMigrationLauncher getLauncher()
    {
        JdbcMigrationLauncher launcher = getJdbcMigrationLauncher();

        // If no one has added a collection of contexts to the service,
        // then take the single-context property configuration and set it in
        if (contexts.size() == 0)
        {
            launcher.addContext(getContext());
        }
        // otherwise, add the collection of contexts in there
        else
        {
            for (Iterator i = contexts.iterator(); i.hasNext();)
            {
                launcher.addContext((JdbcMigrationContext) i.next());
            }
        }
        launcher.setPatchPath(getPatchPath());
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
    protected DataSourceMigrationContext getContext()
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
     * @return Returns the patchPath.
     */
    public String getPatchPath()
    {
        return patchPath;
    }

    /**
     * @param patchPath The patchPath to set.
     */
    public void setPatchPath(String patchPath)
    {
        this.patchPath = patchPath;
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

    /**
     * Get the list of database contexts for multi-node configuration
     *
     * @return List of JdbcMigrationContext objects for multi-node, or empty List
     */
    public List getContexts()
    {
        return contexts;
    }

    /**
     * Set the list of database contexts for multi-node configuration
     *
     * @param contexts List of JdbcMigrationContext objects for multi-node, or empty list
     */
    public void setContexts(List contexts)
    {
        this.contexts = contexts;
    }

    /**
     * Add a database context to the list of database contexts for multi-node configuration
     *
     * @param context the JdbcMigrationContext to use for multi-node patch application
     */
    public void addContext(JdbcMigrationContext context)
    {
        contexts.add(context);
    }
}
