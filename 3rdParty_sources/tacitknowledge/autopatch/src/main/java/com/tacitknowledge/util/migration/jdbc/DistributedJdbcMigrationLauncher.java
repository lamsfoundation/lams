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
import com.tacitknowledge.util.migration.MigrationProcess;
import com.tacitknowledge.util.migration.MigrationRunnerFactory;

/**
 * Core starting point for a distributed database migration run.
 * This class obtains a connection to the orchestration database,
 * checks its patch level, delegates the actual execution of the
 * migration tasks to a <code>MigrationProcess</code> instance,
 * and then commits and cleans everything up at the end.
 * <p/>
 * This class is <b>NOT</b> threadsafe.
 *
 * @author Mike Hardy (mike@tacitknowledge.com)
 */
public class DistributedJdbcMigrationLauncher extends JdbcMigrationLauncher
{
    /**
     * Create a new MigrationProcess and add a SqlScriptMigrationTaskSource
     */
    public DistributedJdbcMigrationLauncher()
    {
        super();
    }

    /**
     * Create a new <code>MigrationLancher</code>.
     *
     * @param context the <code>JdbcMigrationContext</code> to use.
     */
    public DistributedJdbcMigrationLauncher(JdbcMigrationContext context)
    {
        super(context);
    }

    /**
     * Override the sub-class so we get a DistributedMigrationProcess instead of the
     * normal one
     *
     * @return DistributedMigrationProcess
     */
    public MigrationProcess getNewMigrationProcess()
    {
        DistributedMigrationProcess migrationProcess = new DistributedMigrationProcess();
        migrationProcess.setMigrationRunnerStrategy
                (MigrationRunnerFactory.getMigrationRunnerStrategy(getMigrationStrategy()));
        return migrationProcess;
    }

    /**
     * Starts the application migration process across all configured contexts
     *
     * @return the number of patches applied
     * @throws MigrationException if an unrecoverable error occurs during
     *                            the migration
     */
    public int doMigrations() throws MigrationException
    {
        if (getContexts().size() == 0)
        {
            throw new MigrationException("You must configure a migration context");
        }

        return super.doMigrations();
    }

    /**
     * Starts the application migration process across all configured contexts
     *
     * @return the number of patches applied
     * @throws MigrationException if an unrecoverable error occurs during
     *                            the migration
     */
    public int doRollbacks(int rollbackLevel) throws MigrationException
    {
        if (getContexts().size() == 0)
        {
            throw new MigrationException("You must configure a migration context");
        }

        return super.doRollbacks(new int[]{rollbackLevel});
    }
}
