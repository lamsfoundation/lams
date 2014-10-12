/* Copyright 2004 Tacit Knowledge
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0                                                                    get
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tacitknowledge.util.migration.jdbc;

import com.tacitknowledge.util.migration.*;
import com.tacitknowledge.util.migration.jdbc.loader.FlatXmlDataSetTaskSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * Core starting point for a database migration run. This class obtains a
 * connection to the database, checks its patch level, delegates the actual
 * execution of the migration tasks to a <code>MigrationProcess</code>
 * instance, and then commits and cleans everything up at the end.
 * <p/>
 * This class is <b>NOT</b> threadsafe.
 *
 * @author Scott Askew (scott@tacitknowledge.com)
 */
public class JdbcMigrationLauncher implements RollbackListener
{
    /**
     * Class logger
     */
    private static Log log = LogFactory.getLog(JdbcMigrationLauncher.class);

    /**
     * The <code>MigrationProcess</code> responsible for applying the patches
     */
    private MigrationProcess migrationProcess = null;

    /**
     * The amount time, in milliseconds, between attempts to obtain a lock on
     * the patches table. Defaults to 15 seconds.
     */
    private long lockPollMillis = 15000;

    /**
     * The number of times to wait for the lock before overriding it. -1 is
     * infinite
     */
    private int lockPollRetries = -1;

    /**
     * The path containing directories and packages to search through to locate
     * patches.
     */
    private String patchPath = null;

    /**
     * The path containing directories and packages to search for post-patch
     * tasks.
     */
    private String postPatchPath = null;

    /**
     * The <code>MigrationContext</code> objects to use for all migrations.
     * Each one is the key in the map, with the PatchInfoStore being the value
     */
    private LinkedHashMap<JdbcMigrationContext, PatchInfoStore> contexts = new LinkedHashMap<JdbcMigrationContext, PatchInfoStore>();

    /**
     * Holds the migration strategy to use during migration process
     */
    private String migrationStrategy;

    /**
     * Create a new MigrationProcess and add a SqlScriptMigrationTaskSource
     */
    public JdbcMigrationLauncher()
    {
    }

    /**
     * Create a new <code>MigrationLancher</code>.
     *
     * @param context the <code>JdbcMigrationContext</code> to use.
     */
    public JdbcMigrationLauncher(JdbcMigrationContext context)
    {
        this();
        addContext(context);
    }

    /**
     * Get the MigrationProcess we'll use to migrate things
     *
     * @return MigrationProcess for migration control
     */
    public MigrationProcess getNewMigrationProcess()
    {
        MigrationProcess migrationProcess = new MigrationProcess();
        migrationProcess.setMigrationRunnerStrategy
                (MigrationRunnerFactory.getMigrationRunnerStrategy(getMigrationStrategy()));
        return migrationProcess;
    }

    /**
     * Starts the application migration process.
     *
     * @return the number of patches applied
     * @throws MigrationException if an unrecoverable error occurs during the migration
     */
    public int doMigrations() throws MigrationException
    {
        if (contexts.size() == 0)
        {
            throw new MigrationException("You must configure a migration context");
        }

        try
        {
            Iterator contextIter = contexts.keySet().iterator();
            int migrationCount = 0;
            while (contextIter.hasNext())
            {
                JdbcMigrationContext context =
                        (JdbcMigrationContext) contextIter.next();
                migrationCount = doMigrations(context);
                log.info("Executed " + migrationCount + " patches for context "
                        + context);
            }
            return migrationCount;
        }
        catch (SQLException e)
        {
            throw new MigrationException("SqlException during migration", e);
        }
    }

    /**
     * Performs the application rollbacks
     *
     * @param context       the database context to run the patches in
     * @param rollbackLevel the level the system should rollback to
     * @return the number of patches applied
     * @throws SQLException       if an unrecoverable database error occurs while working with the patches table.
     * @throws MigrationException if an unrecoverable error occurs during the migration
     */
    public int doRollbacks(JdbcMigrationContext context, int[] rollbackLevel)
            throws SQLException, MigrationException
    {
        return doRollbacks(context, rollbackLevel, false);
    }

    /**
     * Performs the application rollbacks
     *
     * @param context       the database context to run the patches in
     * @param rollbackLevel the level the system should rollback to
     * @param forceRollback is a boolean indicating if the application should ignore a check to see if all patches are rollbackable
     * @return the number of patches applied
     * @throws SQLException       if an unrecoverable database error occurs while working with the patches table.
     * @throws MigrationException if an unrecoverable error occurs during the migration
     */
    public int doRollbacks(JdbcMigrationContext context, int[] rollbackLevel, boolean forceRollback)
            throws SQLException, MigrationException
    {
        PatchInfoStore patchTable = createPatchStore(context);

        lockPatchStore(context);

        // Now apply the patches
        int executedPatchCount = 0;
        try
        {

            // remember the auto-commit state, and turn auto-commit off
            Connection conn = context.getConnection();
            boolean commitState = conn.getAutoCommit();
            conn.setAutoCommit(false);

            // run the rollbacks
            try
            {
                executedPatchCount = migrationProcess.doRollbacks(patchTable, rollbackLevel, context, forceRollback);
            }

            // restore autocommit state
            finally
            {
                if ((conn != null) && !conn.isClosed())
                {
                    conn.setAutoCommit(commitState);
                }
            }
        }
        catch (MigrationException me)
        {
            // If there was any kind of error, we don't want to eat it, but we do
            // want to unlock the patch store. So do that, then re-throw.
            patchTable.unlockPatchStore();
            throw me;
        }

        // Do any post-patch tasks
        try
        {
            migrationProcess.doPostPatchMigrations(context);
            return executedPatchCount;
        }
        finally
        {
            try
            {
                patchTable.unlockPatchStore();
            }
            catch (MigrationException e)
            {
                log.error("Error unlocking patch table: ", e);
            }
        }
    }

    /**
     * Initiates the application rollback process.
     *
     * @param rollbackLevel the patch level the system should rollback to
     * @return an integer indicating how many patches were rolled back
     * @throws MigrationException is thrown in case of an error while rolling back
     */
    public int doRollbacks(int[] rollbackLevel) throws MigrationException
    {

        if (contexts.size() == 0)
        {
            throw new MigrationException(
                    "You must configure a migration context");
        }
        int rollbackCount = 0;

        try
        {

            for (JdbcMigrationContext context : contexts.keySet())
            {
                rollbackCount = doRollbacks(context, rollbackLevel);
                log.info("Executed " + rollbackCount + " patches for context " + context);
            }
        }
        catch (SQLException se)
        {
            throw new MigrationException("SqlException during rollback", se);
        }

        return rollbackCount;
    }

    /**
     * Initiates the application rollback process.
     *
     * @param rollbackLevel the patch level the system should rollback to
     * @param forceRollback a boolean indcating if the check for all tasks being rollbackable should be ignored
     * @return an integer indicating how many patches were rolled back
     * @throws MigrationException is thrown in case of an error while rolling back
     */
    public int doRollbacks(int[] rollbackLevel, boolean forceRollback) throws MigrationException
    {
        if (contexts.size() == 0)
        {
            throw new MigrationException(
                    "You must configure a migration context");
        }

        int rollbackCount = 0;

        try
        {

            for (JdbcMigrationContext context : contexts.keySet())
            {
                rollbackCount = doRollbacks(context, rollbackLevel, forceRollback);
                log.info("Executed " + rollbackCount + " patches for context " + context);
            }

        }
        catch (SQLException se)
        {
            throw new MigrationException("SqlException during rollback", se);
        }


        return rollbackCount;
    }

    /**
     * Returns the colon-separated path of packages and directories within the
     * class path that are sources of patches.
     *
     * @return a colon-separated path of packages and directories within the
     *         class path that are sources of patches
     */
    public String getPatchPath()
    {
        return patchPath;
    }

    /**
     * Sets the colon-separated path of packages and directories within the
     * class path that are sources of patches.
     *
     * @param searchPath a colon-separated path of packages and directories within
     *                   the class path that are sources of patches
     */
    public void setPatchPath(String searchPath)
    {
        this.patchPath = searchPath;
        StringTokenizer st = new StringTokenizer(searchPath, ":");
        while (st.hasMoreTokens())
        {
            String path = st.nextToken();
            if (path.indexOf('/') > -1)
            {
                getMigrationProcess().addPatchResourceDirectory(path);
            }
            else
            {
                getMigrationProcess().addPatchResourcePackage(path);
            }
        }
    }

    /**
     * Returns the colon-separated path of packages and directories within the
     * class path that are sources of post-patch tasks
     *
     * @return a colon-separated path of packages and directories within the
     *         class path that are sources of post-patch tasks
     */
    public String getPostPatchPath()
    {
        return postPatchPath;
    }

    /**
     * Sets the colon-separated path of packages and directories within the
     * class path that are sources of post-patch tasks
     *
     * @param searchPath a colon-separated path of packages and directories within
     *                   the class path that are sources of post-patch tasks
     */
    public void setPostPatchPath(String searchPath)
    {
        this.postPatchPath = searchPath;
        if (searchPath == null)
        {
            return;
        }
        StringTokenizer st = new StringTokenizer(searchPath, ":");
        while (st.hasMoreTokens())
        {
            String path = st.nextToken();
            if (path.indexOf('/') > -1)
            {
                migrationProcess.addPostPatchResourceDirectory(path);
            }
            else
            {
                migrationProcess.addPostPatchResourcePackage(path);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void migrationStarted(MigrationTask task, MigrationContext ctx) throws MigrationException
    {
        log.debug("Started task " + task.getName() + " for context " + ctx);
    }

    /**
     * {@inheritDoc}
     */
    public void migrationSuccessful(MigrationTask task, MigrationContext ctx) throws MigrationException
    {
        log.debug("Task " + task.getName() + " was successful for context " + ctx + " in launcher " + this);
        int patchLevel = task.getLevel().intValue();

        // update all of our controlled patch tables
        for (Iterator patchTableIter = contexts.entrySet().iterator(); patchTableIter.hasNext();)
        {
            PatchInfoStore store = (PatchInfoStore) ((Map.Entry) patchTableIter.next()).getValue();
            MigrationRunnerStrategy strategy = getMigrationProcess().getMigrationRunnerStrategy();
            if (strategy.shouldMigrationRun(patchLevel, store))
            {
                store.updatePatchLevel(patchLevel);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void migrationFailed(MigrationTask task, MigrationContext ctx, MigrationException e)
            throws MigrationException
    {
        log.debug("Task " + task.getName() + " failed for context " + ctx, e);
    }

    /**
     * Get the patch level from the database
     *
     * @param ctx the migration context to get the patch level for
     * @return int representing the current database patch level
     * @throws MigrationException if there is a database connection error, or the patch level can't be determined
     */
    public int getDatabasePatchLevel(MigrationContext ctx) throws MigrationException
    {
        PatchInfoStore patchTable = (PatchInfoStore) contexts.get(ctx);
        return patchTable.getPatchLevel();
    }

    /**
     * Get the next patch level, for use when creating a new patch
     *
     * @return int representing the first unused patch number
     * @throws MigrationException if the next patch level can't be determined
     */
    public int getNextPatchLevel() throws MigrationException
    {
        return migrationProcess.getNextPatchLevel();
    }

    /**
     * Sets the <code>JdbcMigrationContext</code> used for the migrations.
     *
     * @param context the <code>JdbcMigrationContext</code> used for the migrations
     */
    public void addContext(JdbcMigrationContext context)
    {
        PatchInfoStore patchTable = new PatchTable(context);
        log.debug("Adding context " + context + " with patch table " + patchTable + " in launcher " + this);
        contexts.put(context, patchTable);
    }

    /**
     * Returns the <code>JdbcMigrationContext</code> objects used for the migrations.
     *
     * @return Map of <code>JdbcMigrationContext</code> and
     *         <code>PatchInfoStore</code> objects used in the migrations
     */
    public LinkedHashMap getContexts()
    {
        return contexts;
    }

    /**
     * Performs the application migration process in one go
     *
     * @param context the database context to run the patches in
     * @return the number of patches applied
     * @throws SQLException       if an unrecoverable database error occurs while working with the patches table.
     * @throws MigrationException if an unrecoverable error occurs during the migration
     */
    protected int doMigrations(JdbcMigrationContext context) throws SQLException, MigrationException
    {
        PatchInfoStore patchTable = createPatchStore(context);

        lockPatchStore(context);

        // Now apply the patches
        int executedPatchCount = 0;
        try
        {

            // remember the auto-commit state, and turn auto-commit off
            Connection conn = context.getConnection();
            boolean commitState = conn.getAutoCommit();
            conn.setAutoCommit(false);

            // run the migrations
            try
            {
                executedPatchCount = migrationProcess.doMigrations(patchTable,
                        context);
            }

            // restore autocommit state
            finally
            {
                if ((conn != null) && !conn.isClosed())
                {
                    conn.setAutoCommit(commitState);
                }
            }
        }
        catch (MigrationException me)
        {
            // If there was any kind of error, we don't want to eat it, but we do
            // want to unlock the patch store. So do that, then re-throw.
            patchTable.unlockPatchStore();
            throw me;
        }

        // Do any post-patch tasks
        try
        {
            migrationProcess.doPostPatchMigrations(context);
            return executedPatchCount;
        }
        finally
        {
            try
            {
                patchTable.unlockPatchStore();
            }
            catch (MigrationException e)
            {
                log.error("Error unlocking patch table: ", e);
            }
        }
    }

    /**
     * Lock the patch store. This is done safely, such that we safely handle the
     * case where other migration launchers are patching at the same time.
     *
     * @param context the context to lock the store in
     * @throws MigrationException if the reading or setting lock state fails
     */
    private void lockPatchStore(JdbcMigrationContext context) throws MigrationException
    {
        // Patch locks ensure that only one system sharing a patch store will patch
        // it at the same time.
        boolean lockObtained = false;
        while (!lockObtained)
        {
            waitForFreeLock(context);

            PatchInfoStore piStore = (PatchInfoStore) contexts.get(context);
            piStore.getPatchLevel();
            try
            {
                piStore.lockPatchStore();
                lockObtained = true;
            }
            catch (IllegalStateException ise)
            {
                log.error("IllegalStateException when trying to lock the patch info store", ise);
                // this happens when someone woke up at the same time,
                // raced us to the lock and won. We re-sleep and try again.
            }
        }
    }

    /**
     * create a patch table object for use in migrations
     *
     * @param context the context to create the store in
     * @return PatchTable object for use in accessing patch state information
     * @throws MigrationException if unable to create the store
     */
    protected PatchInfoStore createPatchStore(JdbcMigrationContext context) throws MigrationException
    {
        // Make sure the table is created before claiming it exists by returning
        return (PatchInfoStore) contexts.get(context);
    }

    /**
     * Pauses until the patch lock become available.
     *
     * @param context the context related to the store
     * @throws MigrationException if an unrecoverable error occurs
     */
    private void waitForFreeLock(JdbcMigrationContext context) throws MigrationException
    {
        PatchInfoStore piStore = (PatchInfoStore) contexts.get(context);
        log.debug("about to wait for free lock");
        for (int i = 0; piStore.isPatchStoreLocked(); i++)
        {
            // Have we exceeded our threshold of time to wait?
            if ((getLockPollRetries() != -1) && (i >= getLockPollRetries()))
            {
                log.info("Reached maximum lock poll retries (" + getLockPollRetries() + "), overriding patch lock");
                piStore.unlockPatchStore();
            }
            else
            {
                log.info("Waiting for migration lock for system \"" + context.getSystemName() + "\"");
                log.info("  If this isn't from a long-running patch, but a stale lock, either:");
                log.info("    1) run MigrationTableUnlock (probably 'ant patch.unlock')");
                log.info("    2) set the lockPollRetries property so the lock times out");
                log.info("       (this is dangerous in combination with long-running patches)");
                log.info("    3) set the 'patch_in_progress' in the patches table to 'F'");

                if (getLockPollRetries() != -1)
                {
                    log.info("'lockPollRetries' is set, will poll lock " + (getLockPollRetries() - i)
                            + " more times before overriding lock.");
                }
                try
                {
                    Thread.sleep(getLockPollMillis());
                }
                catch (InterruptedException e)
                {
                    log.error("Received InterruptedException while waiting for patch lock", e);
                }
            }
        }
        log.debug("done waiting for free lock");
    }

    /**
     * Get how long to wait for the patch store lock
     *
     * @return the wait time for the patch store, in milliseconds
     */
    public long getLockPollMillis()
    {
        return lockPollMillis;
    }

    /**
     * Set how long to wait for the patch store lock
     *
     * @param lockPollMillis the wait time for the patch store, in milliseconds
     */
    public void setLockPollMillis(long lockPollMillis)
    {
        this.lockPollMillis = lockPollMillis;
    }

    /**
     * Get the migration process to use for migrations
     *
     * @return MigrationProcess to use for migrations
     */
    public MigrationProcess getMigrationProcess()
    {
        if (migrationProcess == null)
        {
            setMigrationProcess(getNewMigrationProcess());

        }

        return migrationProcess;
    }

    /**
     * Set the migration process to use for migrations
     *
     * @param migrationProcess the MigrationProcess to use for migrations
     */
    public void setMigrationProcess(MigrationProcess migrationProcess)
    {
        this.migrationProcess = migrationProcess;

        // Make sure this class is notified when a patch is applied so that
        // the patch level can be updated (see #migrationSuccessful).
        this.migrationProcess.addListener(this);

        this.migrationProcess.addMigrationTaskSource(new SqlScriptMigrationTaskSource());

        this.migrationProcess.addMigrationTaskSource(new FlatXmlDataSetTaskSource());

    }

    /**
     * See if we are actually applying patches, or if it is just readonly
     *
     * @return boolean true if we will skip application
     */
    public boolean isReadOnly()
    {
        return getMigrationProcess().isReadOnly();
    }

    /**
     * Set whether or not to actually apply patches
     *
     * @param readOnly boolean true if we should skip application
     */
    public void setReadOnly(boolean readOnly)
    {
        getMigrationProcess().setReadOnly(readOnly);
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
     * Explicitly set the contexts.
     *
     * @param contexts the collection of contexts that is a map of JDBCMigrationContext -> PatchInfoStore.
     */
    public void setContexts(LinkedHashMap contexts)
    {
        this.contexts = contexts;
    }

    /**
     * @see com.tacitknowledge.util.migration.MigrationListener#initialize(String, Properties)
     */
    public void initialize(String systemName, Properties properties) throws MigrationException
    {
    }

    /**
     * {@inheritDoc}
     */
    public void rollbackFailed(RollbackableMigrationTask task, MigrationContext context, MigrationException e)
            throws MigrationException
    {
        log.debug("Task " + task.getName() + " failed for context " + context, e);
    }

    /**
     * {@inheritDoc}
     */
    public void rollbackStarted(RollbackableMigrationTask task, MigrationContext context) throws MigrationException
    {
        log.debug("Started rollback " + task.getName() + " for context " + context);
    }

    /**
     * {@inheritDoc}
     */
    public void rollbackSuccessful(RollbackableMigrationTask task, int rollbackLevel, MigrationContext context) throws MigrationException
    {
        log.debug("Rollback of task " + task.getName() + " was successful for context " + context + " in launcher " + this);
        int patchLevel = task.getLevel().intValue();

        // update all of our controlled patch tables
        for (Iterator patchTableIter = contexts.entrySet().iterator(); patchTableIter.hasNext();)
        {
            PatchInfoStore store = (PatchInfoStore) ((Map.Entry) patchTableIter.next()).getValue();
            store.updatePatchLevelAfterRollBack(rollbackLevel);
        }
    }

    public void setMigrationStrategy(String migrationStrategy)
    {
        this.migrationStrategy = migrationStrategy;
    }

    public String getMigrationStrategy()
    {
        return migrationStrategy;
    }
}
