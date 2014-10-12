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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Iterator;
import java.util.Map;

/**
 * Allows you to force-unlock a migration table with an orphaned lock. Should
 * be used in the same way that MigrationInformation is used
 *
 * @author Mike Hardy (mike@tacitknowledge.com)
 * @see com.tacitknowledge.util.migration.jdbc.MigrationInformation
 */
public class MigrationTableUnlock
{
    /**
     * Class logger
     */
    private static Log log = LogFactory.getLog(MigrationTableUnlock.class);

    /**
     * Get the migration level information for the given system name
     *
     * @param arguments the command line arguments, if any (none are used)
     * @throws Exception if anything goes wrong
     */
    public static void main(String[] arguments) throws Exception
    {
        MigrationTableUnlock unlock = new MigrationTableUnlock();
        String migrationName = System.getProperty("migration.systemname");
        if (migrationName == null)
        {
            if ((arguments != null) && (arguments.length > 0))
            {
                migrationName = arguments[0].trim();
            }
            else
            {
                throw new IllegalArgumentException("The migration.systemname "
                        + "system property is required");
            }
        }
        unlock.tableUnlock(migrationName);
    }

    /**
     * unlock the patch table for the given system name
     *
     * @param systemName the name of the system
     * @throws Exception if anything goes wrong
     */
    public void tableUnlock(String systemName) throws Exception
    {
        tableUnlock(systemName, MigrationContext.MIGRATION_CONFIG_FILE);
    }

    /**
     * unlock the patch table for the given system name
     *
     * @param systemName        the name of the system
     * @param migrationSettings migration settings file
     * @throws Exception if anything goes wrong
     */
    public void tableUnlock(String systemName, String migrationSettings) throws Exception
    {
        // The MigrationLauncher is responsible for handling the interaction
        // between the PatchTable and the underlying MigrationTasks; as each
        // task is executed, the patch level is incremented, etc.
        try
        {   //TODO should be injected
            JdbcMigrationLauncherFactory launcherFactory =
                    new JdbcMigrationLauncherFactoryLoader().createFactory();
            JdbcMigrationLauncher launcher = launcherFactory.createMigrationLauncher(systemName, migrationSettings);

            // Print out information for all contexts
            Map contextMap = launcher.getContexts();
            for (Iterator contextIter = contextMap.keySet().iterator(); contextIter.hasNext();)
            {
                JdbcMigrationContext context = (JdbcMigrationContext) contextIter.next();
                launcher.createPatchStore(context).unlockPatchStore();
            }
        }
        catch (Exception e)
        {
            log.error(e);
            throw e;
        }
    }
}
