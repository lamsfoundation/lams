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

package com.tacitknowledge.util.migration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * Discovers and executes a sequence of system patches. Patches take the form of
 * <code>MigrationTask</code> instances, each of which performs an atomic
 * migration or patch transaction. <code>MigrationTask</code>s are executed
 * sequentially based on the result of <code>MigrationTask.getOrder</code>.
 * No two tasks can return the same result for <code>getOrder</code>, and
 * this class will throw a <code>MigrationException</code> should such a
 * situation occur.
 * <p/>
 * One useful pre-defined <code>MigrationTask</code> is
 * <code>SqlScriptMigrationTask</code>, which wraps a .SQL file and executes
 * all statements inside it. Any file in the migration task search path that
 * matches the pattern "^patch(\\d++)(?!-rollback)_?(.+)?\\.sql" will be wrapped with the
 * <code>SqlScriptMigrationTask</code>.  Also, you rollback scripts are specified by
 * matching the pattern "^patch(\\d+)-rollback_(.+)\\.sql." The rollback scripts
 * are meant to reverse a patch and will reduce the level of the system to the
 * previous patch level.  The execution order for these tasks
 * is defined by the number immediately following the "patch" part of the SQL
 * script file name.
 * <p/>
 * Example:
 * <p/>
 * <pre>
 *    // Find the patches
 *    migrationRunner.addResourcePackage(&quot;com.example.myapp.migration&quot;);
 *    migrationRunner.addResourceDirectory(&quot;db/sql&quot;);
 *
 *    try
 *    {
 *        &lt;i&gt;... figure out the current patch level...&lt;/i&gt;
 *        migrationRunner.doMigration(currentLevel, context);
 *        &lt;i&gt;... update patch level&lt;/i&gt;
 *        &lt;i&gt;... commit MigrationContext ...&lt;/i&gt;
 *    }
 *    catch (MigrationException e)
 *    {
 *        &lt;i&gt;... rollback MigrationContext ...&lt;/i&gt;
 *    }
 * </pre>
 *
 * @author Scott Askew (scott@tacitknowledge.com)
 * @author Hemri Herrera (hemri@tacitknowledge.com)
 * @author Ulises Pulido (upulido@tacitknowledge.com)
 */
public class MigrationProcess
{
    /**
     * Class logger
     */
    private static Log log = LogFactory.getLog(MigrationProcess.class);

    /**
     * The list of package names containing the <code>MigrationTask</code>s
     * and SQL scripts to execute as patches
     */
    private List patchResourcePackages = new ArrayList();

    /**
     * The list of package names containing <code>MigrationTask</code>s and
     * SQL scripts to execute after patch execution
     */
    private List postPatchResourcePackages = new ArrayList();

    /**
     * Migration task providers
     */
    private List<MigrationTaskSource> migrationTaskSources = new ArrayList<MigrationTaskSource>();

    /**
     * Used to broadcast migration task notifications
     */
    private MigrationBroadcaster broadcaster = null;

    /**
     * Used to broadcast rollback task notifications
     */
    private RollbackBroadcaster rollbackBroadcaster = new RollbackBroadcaster();

    /**
     * Holds the strategy used to work with the different patches
     */
    private MigrationRunnerStrategy migrationRunnerStrategy = null;


    /**
     * Whether we actually want to apply patches, or just look
     */
    private boolean readOnly = false;

    /**
     * Creates a new <code>Migration</code> instance.
     */
    public MigrationProcess()
    {
        addMigrationTaskSource(new ClassMigrationTaskSource());
        setMigrationBroadcaster(new MigrationBroadcaster());
    }


    public void setMigrationRunnerStrategy(MigrationRunnerStrategy migrationRunnerStrategy)
    {
        this.migrationRunnerStrategy = migrationRunnerStrategy;
    }


    /**
     * Sets the <code>MigrationBroadcaster</code> for the current instance.
     *
     * @param migrationBroadcaster with the <code>MigrationBroadcaster</code> to be set
     */
    protected void setMigrationBroadcaster(MigrationBroadcaster migrationBroadcaster)
    {
        this.broadcaster = migrationBroadcaster;
    }

    /**
     * Adds the given package to the migration task search path.
     *
     * @param packageName the name of the package to add to the search path
     */
    public void addPatchResourcePackage(String packageName)
    {
        patchResourcePackages.add(packageName);
    }

    /**
     * Adds the given classpath-relative directory to the migration task search
     * path.
     *
     * @param dir the name of the directory to add to the post-patch search path
     */
    public void addPatchResourceDirectory(String dir)
    {
        // Make the path package-name-like so that
        // ClassLoader.getResourceAsStream
        // will work correctly
        String packageName = dir.replace('/', '.').replace('\\', '.');
        addPatchResourcePackage(packageName);
    }

    /**
     * Adds the given package to the post-patch migration task search path.
     *
     * @param packageName the name of the package to add to the search path
     */
    public void addPostPatchResourcePackage(String packageName)
    {
        postPatchResourcePackages.add(packageName);
    }

    /**
     * Adds the given classpath-relative directory to the post-patch migration
     * task search path.
     *
     * @param dir the name of the directory to add to the post-patch search path
     */
    public void addPostPatchResourceDirectory(String dir)
    {
        // Make the path package-name-like so that
        // ClassLoader.getResourceAsStream
        // will work correctly
        String packageName = dir.replace('/', '.').replace('\\', '.');
        addPostPatchResourcePackage(packageName);
    }

    /**
     * Adds a <code>MigrationTaskSource</code> to the list of sources that
     * provide this instance with <code>MigrationTask</code>s.
     *
     * @param source the <code>MigrationTaskSource</code> to add; may not be
     *               <code>null</code>
     */
    public void addMigrationTaskSource(MigrationTaskSource source)
    {
        if (source == null)
        {
            throw new IllegalArgumentException("source cannot be null.");
        }
        else
        {
            migrationTaskSources.add(source);
        }
    }

    /**
     * Applies rollbacks to move the patch level from the current level to the
     * rollback level.
     *
     * @param currentPatchInfoStore
     * @param rollbackLevels        an integer indicating the level the desired patch level
     * @param context               information and resources that are available to the migration tasks
     * @return the count of patches which were rolled back
     * @throws MigrationException
     */
    public int doRollbacks(PatchInfoStore currentPatchInfoStore, int[] rollbackLevels, MigrationContext context,
            boolean forceRollback) throws MigrationException
    {
        log.trace("Starting doRollbacks");
        List<MigrationTask> allTasks = getMigrationTasks();
        validateTasks(allTasks);

        List<MigrationTask> rollbackCandidates = getMigrationRunnerStrategy()
                .getRollbackCandidates(allTasks, rollbackLevels, currentPatchInfoStore);


        boolean isPatchSetRollbackable = isPatchSetRollbackable(rollbackCandidates);
        rollbackDryRun(rollbackCandidates, context);
        if (isPatchSetRollbackable || forceRollback)
        {
            // See if we should execute
            if (isReadOnly())
            {
                throw new MigrationException("Unapplied rollbacks exist, but read-only flag is set");
            }

            // the list of patches is rollbackable now actually perform the
            // rollback
            log.info("A total of " + rollbackCandidates.size() + " rollbacks will execute.");
            for (MigrationTask rollbackTask : rollbackCandidates)
            {
                RollbackableMigrationTask task = (RollbackableMigrationTask) rollbackTask;

                log.info("Will rollback patch task '" + getTaskLabel(task) + "'");
                log.debug("Task will rollback in context '" + context + "'");

                applyRollback(context, task, true);
            }
        }
        else
        {
            // Can I list the tasks which are not rollbackable?
            log.info("Could not complete rollback because one or more of the tasks " +
                    "is not rollbackable.The system is still at patch level " + currentPatchInfoStore + ".");
        }

        List<MigrationTask> rollbacksNotApplied = getMigrationRunnerStrategy()
                .getRollbackCandidates(rollbackCandidates, rollbackLevels, currentPatchInfoStore);
        if (rollbacksNotApplied.isEmpty())
        {
            log.info("Rollback complete.  The system is now at the desired patch level.");
        }
        else
        {
            log.info("The system was not able to rollback the patches.");
        }
        log.trace("Ending doRollbacks");
        return rollbackCandidates.size() - rollbacksNotApplied.size();
    }

    /**
     * Helper method to determine if the set of migration tasks is rollbackable
     *
     * @param migrations a <code>List</code> of MigrationTasks
     * @return a boolean value indicating if all of the tasks can be rolledback
     */
    protected boolean isPatchSetRollbackable(List migrations)
    {
        boolean isRollbackable = true;

        for (Iterator i = migrations.iterator(); i.hasNext() && isRollbackable;)
        {
            // for each migration check if the task is able to be rolledback
            // think about type
            RollbackableMigrationTask task = null;
            try
            {
                task = (RollbackableMigrationTask) i.next();
            }
            catch (ClassCastException cce)
            {
                isRollbackable = false;
                log.info("The task " + task.getName() + " is not rollbackable.");
            }
            if (!task.isRollbackSupported())
            {
                isRollbackable = false;
                log.info("The task " + task.getName() + " is not rollbackable.");
            }

        }
        return isRollbackable;
    }

    /**
     * Applies necessary patches to the system.
     *
     * @param patchInfoStore used to execute migrations
     * @param context        information and resources that are available to the migration tasks
     * @return the number of <code>MigrationTask</code>s that have executed
     * @throws MigrationException if a migration fails
     */
    public int doMigrations(PatchInfoStore patchInfoStore,
            MigrationContext context) throws MigrationException
    {

        log.trace("Starting doMigrations");
        List<MigrationTask> migrations = getMigrationTasks();
        validateTasks(migrations);
        Collections.sort(migrations);
        int taskCount = dryRun(patchInfoStore, context, migrations);

        // See if we should execute
        if (isReadOnly())
        {
            if (taskCount > 0)
            {
                throw new MigrationException("Unapplied patches exist, but read-only flag is set");
            }

            log.info("In read-only mode - skipping patch application");
            return 0;
        }

        // Now apply them
        taskCount = 0;
        for (MigrationTask task : migrations)
        {
            if (migrationRunnerStrategy.shouldMigrationRun(task.getLevel(), patchInfoStore))
            {
                applyPatch(context, task, true);
                taskCount++;
            }
        }

        if (taskCount > 0)
        {
            log.info("Patching complete (" + taskCount + " patch tasks executed)");
        }
        else
        {
            log.info("System up-to-date.  No patch tasks executed.");
        }

        return taskCount;
    }

    /**
     * Performs a dry run of rollbacks.  This method determines which tasks will rollback
     * and logs this information.
     *
     * @param migrations a <code>List</code> of migrations
     * @param context    the <codde>MigrationContext</code> where rollbacks would occer
     * @return the count of tasks which would rollback
     */
    private int rollbackDryRun(List<MigrationTask> migrations, MigrationContext context)
    {
        int taskCount = 0;
        // Roll through once, just printing out what we'll do
        for (MigrationTask migrationTask : migrations)
        {
            RollbackableMigrationTask task = (RollbackableMigrationTask) migrationTask;

            log.info("Will execute rollback for task '" + getTaskLabel(task) + "'");
            log.debug("Task will execute in context '" + context + "'");
            taskCount++;

        }
        if (taskCount > 0)
        {
            log.info("A total of " + taskCount + " patch tasks will rollback.");
        }
        else
        {
            log.info("System up-to-date.  No patch tasks will execute.");
        }
        return taskCount;
    }

    /**
     * Run post-migration tasks
     *
     * @param context the context to use for the post-patch migrations
     * @return the number of <code>MigrationTask</code>s that executed
     * @throws MigrationException if a post-patch task fails
     */
    public int doPostPatchMigrations(MigrationContext context) throws MigrationException
    {
        log.info("Running post-patch tasks...");
        List postMigrationTasks = getPostPatchMigrationTasks();
        validateTasks(postMigrationTasks);
        Collections.sort(postMigrationTasks);

        if (postMigrationTasks.size() == 0)
        {
            log.info("No post-patch tasks found.");
            return 0;
        }

        // Roll through once, just printing out what we'll do
        int taskCount = 0;
        for (Iterator i = postMigrationTasks.iterator(); i.hasNext(); taskCount++)
        {
            MigrationTask task = (MigrationTask) i.next();
            log.info("Will execute post-patch task '" + getTaskLabel(task) + "'");
        }
        log.info("A total of " + taskCount + " post-patch tasks will execute.");

        // See if we should execute
        // FIXME test read-only mode with no patches skipping post-patch tasks
        if (isReadOnly())
        {
            log.info("In read-only mode - skipping post-patch task execution");
            return 0;
        }

        // Now execute them
        taskCount = 0;
        for (Iterator i = postMigrationTasks.iterator(); i.hasNext(); taskCount++)
        {
            MigrationTask task = (MigrationTask) i.next();
            applyPatch(context, task, false);
        }
        log.info("Post-patch tasks complete (" + taskCount + " tasks executed)");

        return taskCount;
    }

    /**
     * This method applies a single Rollback to the system.
     *
     * @param context   the <code>MigrationContext</code> in which the task is rolled back
     * @param task      the <code>RollbackableMigrationTask</code> which is to be rolled back
     * @param broadcast a boolean indicating if the execution of this rollback should be broadcast to listeners
     * @throws MigrationException is thrown in case of encountering an exception
     */
    public void applyRollback(MigrationContext context, RollbackableMigrationTask task,
            boolean broadcast) throws MigrationException
    {
        String label = getTaskLabel(task);
        int rollbackLevel = task.getLevel().intValue();

        if (broadcast)
        {
            rollbackBroadcaster.notifyListeners(task, context, MigrationBroadcaster.TASK_START,
                    rollbackLevel);
            log
                    .debug("broadcaster has " + rollbackBroadcaster.getListeners().size()
                            + " listeners");
        }
        log.info("Executing patch task \"" + label + "\"...");

        try
        {
            long startTime = System.currentTimeMillis();
            task.down(context);
            long duration = System.currentTimeMillis() - startTime;
            log.info("Finished patch task \"" + label + "\" (" + duration + " millis.)");
            if (broadcast)
            {
                rollbackBroadcaster.notifyListeners(task, context,
                        MigrationBroadcaster.TASK_SUCCESS, rollbackLevel);
            }
            context.commit();
        }
        catch (MigrationException e)
        {
            if (broadcast)
            {
                rollbackBroadcaster.notifyListeners(task, context, e,
                        MigrationBroadcaster.TASK_FAILED, rollbackLevel);
            }
            try
            {
                context.rollback();
                log.info("Patch task failed; rollback successful");
            }
            catch (MigrationException me)
            {
                log.info("Patch task failed; COULD NOT ROLL BACK TRANSACTION", me);
            }
            throw e;
        }
    }

    /**
     * Apply a single patch
     *
     * @param context   the context the patch will need during application
     * @param task      the application task to carry out
     * @param broadcast whether to broadcast to listeners that the patch applied
     * @throws MigrationException if the patch application fails
     */
    public void applyPatch(MigrationContext context, MigrationTask task, boolean broadcast)
            throws MigrationException
    {
        String label = getTaskLabel(task);
        if (broadcast)
        {
            broadcaster.notifyListeners(task, context, MigrationBroadcaster.TASK_START);
            log.debug("broadcaster has " + broadcaster.getListeners().size() + " listeners");
        }
        log.info("Executing patch task \"" + label + "\"...");

        try
        {
            long startTime = System.currentTimeMillis();
            task.migrate(context);
            long duration = System.currentTimeMillis() - startTime;
            log.info("Finished patch task \"" + label + "\" (" + duration + " millis.)");
            if (broadcast)
            {
                broadcaster.notifyListeners(task, context, MigrationBroadcaster.TASK_SUCCESS);
            }
            context.commit();
        }
        catch (MigrationException e)
        {
            if (broadcast)
            {
                broadcaster.notifyListeners(task, context, e, MigrationBroadcaster.TASK_FAILED);
            }
            try
            {
                context.rollback();
                log.info("Patch task failed; rollback successful");
            }
            catch (MigrationException me)
            {
                log.info("Patch task failed; COULD NOT ROLL BACK TRANSACTION", me);
            }
            throw e;
        }
    }

    /**
     * Returns a list of all migration tasks, regardless of patch level.
     *
     * @return a list of all migration tasks
     * @throws MigrationException if one or more migration tasks could not be created
     */
    public List<MigrationTask> getMigrationTasks() throws MigrationException
    {
        return getTasksFromPackages(patchResourcePackages);
    }

    /**
     * Returns a list of all post-patch migration tasks
     *
     * @return a list of all post-patch migration tasks
     * @throws MigrationException if one or more post-patch migration tasks could not be created
     */
    public List getPostPatchMigrationTasks() throws MigrationException
    {
        return getTasksFromPackages(postPatchResourcePackages);
    }

    /**
     * Instantiate all the MigrationTask objects in the given resource packages
     *
     * @param resourcePackages a List of Strings specifying package names to look for tasks in
     * @return List of MigrationTask objects instantiated from the given packages
     * @throws MigrationException if one or more post-patch migration tasks could not be
     *                            created
     */
    private List<MigrationTask> getTasksFromPackages(List<String> resourcePackages) throws MigrationException
    {
        List tasks = new ArrayList();
        for (String packageName : resourcePackages)
        {
            log.debug("Searching for patch tasks in package " + packageName);

            for (MigrationTaskSource source : migrationTaskSources)
            {
                List<MigrationTask> sourceTasks = source.getMigrationTasks(packageName);
                if (sourceTasks.size() > 0)
                {
                    log.debug("Source [" + source + "] found " + sourceTasks.size()
                            + " patch tasks: " + sourceTasks);
                }
                else
                {
                    log.debug("Source [" + source + "] returned 0 patch tasks.");
                }

                tasks.addAll(sourceTasks);
            }
        }

        // Its difficult to tell what's going on when you don't see any patches.
        // This will help people realize they don't have patches, and perhaps
        // help them discover why.
        if (tasks.size() == 0)
        {
            log.info("No patch tasks were discovered in your classpath. "
                    + "Run with DEBUG logging enabled for patch task search details.");
        }

        return tasks;
    }

    /**
     * Returns the patch level which is previous to the current level
     *
     * @param currentLevel the current patch level
     * @return the level of the patch that was applied previous to the current patch l
     * @throws MigrationException if there is an error retrieving the previous patch level
     */
    public int getPreviousPatchLevel(int currentLevel) throws MigrationException
    {
        boolean isCurrentPatchFound = false;
        List tasks = getMigrationTasks();
        int previousTaskLevel = 0;

        Collections.sort(tasks);

        for (ListIterator patchIterator = tasks.listIterator(); patchIterator.hasNext()
                && !isCurrentPatchFound;)
        {
            int previousIndex = patchIterator.previousIndex();

            //get the current patch
            MigrationTask task = (MigrationTask) patchIterator.next();

            if (currentLevel == task.getLevel().intValue())
            {
                //the current patch level is found
                isCurrentPatchFound = true;

                if (previousIndex != -1)
                {
                    MigrationTask previousTask = (MigrationTask) tasks.get(previousIndex);
                    previousTaskLevel = previousTask.getLevel().intValue();
                }
            }
        }
        return previousTaskLevel;
    }

    /**
     * Returns the number to use when creating the next patch.
     *
     * @return the number to use when creating the next patch
     * @throws MigrationException if the existing tasks are invalid
     */
    public int getNextPatchLevel() throws MigrationException
    {
        List tasks = getMigrationTasks();

        if (tasks.size() == 0)
        {
            return 1;
        }

        Collections.sort(tasks);
        validateTasks(tasks);
        MigrationTask lastTask = (MigrationTask) tasks.get(tasks.size() - 1);

        return lastTask.getLevel().intValue() + 1;
    }

    /**
     * Registers the given <code>MigrationListener</code> as being interested
     * in migration task events.
     *
     * @param listener the listener to add; may not be <code>null</code>
     */
    public void addListener(MigrationListener listener)
    {
        broadcaster.addListener(listener);
        if (listener instanceof RollbackListener)
            rollbackBroadcaster.addListener((RollbackListener) listener);
    }

    /**
     * Removes the given <code>MigrationListener</code> from the list of
     * listeners associated with this <code>Migration</code> instance.
     *
     * @param listener the listener to add; may not be <code>null</code>
     * @return <code>true</code> if the listener was located and removed,
     *         otherwise <code>false</code>.
     */
    public boolean removeListener(MigrationListener listener)
    {
        return broadcaster.removeListener(listener);
    }

    /**
     * Get all of the MigrationListeners
     *
     * @return List of MigrationListeners
     */
    public List getListeners()
    {
        return broadcaster.getListeners();
    }

    /**
     * Returns a user-friendly label for the specified task.
     *
     * @param task the task to create a label for
     * @return a user-friendly label for the specified task
     */
    protected String getTaskLabel(MigrationTask task)
    {
        return task.getName() + " [" + task.getClass().getName() + "]";
    }

    /**
     * Ensures that no two <code>MigrationTasks</code> have the same ordering.
     *
     * @param migrations the list of defined migration tasks
     * @throws MigrationException if the migration tasks are not correctly defined
     */
    public void validateTasks(List migrations) throws MigrationException
    {
        Map usedOrderNumbers = new HashMap();
        for (Iterator i = migrations.iterator(); i.hasNext();)
        {
            MigrationTask task = (MigrationTask) i.next();

            Integer level = task.getLevel();
            if (level == null)
            {
                throw new MigrationException("Patch task '" + getTaskLabel(task)
                        + "' does not have a patch level defined.");
            }

            if (usedOrderNumbers.containsKey(level))
            {
                MigrationTask otherTask = (MigrationTask) usedOrderNumbers.get(level);
                throw new MigrationException("Patch task " + getTaskLabel(task)
                        + " has a conflicting patch level with " + getTaskLabel(otherTask)
                        + "; both are configured for patch level " + level);
            }

            usedOrderNumbers.put(level, task);
        }
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
     * Registers the given <code>MigrationListeners</code> as being interested
     * in migration task events.
     *
     * @param listeners the listeners to add;
     */
    public void addListeners(List listeners)
    {
        for (Iterator it = listeners.iterator(); it.hasNext();)
        {
            MigrationListener listener = (MigrationListener) it.next();
            broadcaster.addListener(listener);
            if (listener instanceof RollbackListener)
                rollbackBroadcaster.addListener((RollbackListener) listener);
        }
    }

    public int dryRun(PatchInfoStore patchInfoStore, MigrationContext migrationContext, List migrations) throws MigrationException
    {
        int taskCount = 0;
        // Roll through once, just printing out what we'll do
        for (Iterator i = migrations.iterator(); i.hasNext();)
        {
            MigrationTask task = (MigrationTask) i.next();
            if (migrationRunnerStrategy
                    .shouldMigrationRun(task.getLevel().intValue(), patchInfoStore))
            {
                log.info("Will execute patch task '" + getTaskLabel(task) + "'");
                log.debug("Task will execute in context '" + migrationContext + "'");
                taskCount++;
            }
        }
        if (taskCount > 0)
        {
            log.info("A total of " + taskCount + " patch tasks will execute.");
        }
        else
        {
            log.info("System up-to-date.  No patch tasks will execute.");
        }
        return taskCount;
    }

    public MigrationRunnerStrategy getMigrationRunnerStrategy()
    {
        return migrationRunnerStrategy;
    }
}
