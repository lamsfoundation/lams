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

import java.util.Properties;

import com.mockrunner.jdbc.JDBCTestCaseAdapter;

/**
 * This class helps with testing AutoPatch by providing a listener which implements
 * both the MigrationListener and RollbackListener interfaces.  The action which still
 * class takes upon receiving a message of an event is to increment a particular count.
 * 
 * Test classes can retrieve this count to see if the correct action occurred.
 * 
 * @author  Mike Hardy (mike@tacitknowledge.com)
 * @author  Artie Pesh-Imam (apeshimam@tacitknowledge.com)
 */
public class MigrationListenerTestBase extends JDBCTestCaseAdapter implements RollbackListener
{
    /** the count of times "migration started" was broadcast */
    private int migrationStartedCount = 0;

    /** the count of times "migration success" was broadcast */
    private int migrationSuccessCount = 0;

    /** the count of times "migration failed" was broadcast */
    private int migrationFailedCount = 0;

    /** the count of times "migration started" was broadcast */
    private int rollbackStartedCount = 0;

    /** the count of times "migration success" was broadcast */
    private int rollbackSuccessCount = 0;

    /** the count of times "migration failed" was broadcast */
    private int rollbackFailedCount = 0;

    /**
     * Constructor for MigrationTest.
     * 
     * @param name the name of the test to run
     */
    public MigrationListenerTestBase(String name)
    {
        super(name);
    }

    /** {@inheritDoc} */
    public void rollbackStarted(RollbackableMigrationTask task, MigrationContext con)
    {
        setRollbackStartedCount(getRollbackStartedCount() + 1);
    }

    /** {@inheritDoc} */
    public void rollbackSuccessful(RollbackableMigrationTask taskn, int rollbackLevel,
            MigrationContext con)
    {
        setRollbackSuccessCount(getRollbackSuccessCount() + 1);
    }

    /** {@inheritDoc} */
    public void rollbackFailed(RollbackableMigrationTask task, MigrationContext con,
            MigrationException me)
    {
        setRollbackFailedCount(getRollbackFailedCount() + 1);
    }

    /**
     * Returns the number of rollbacks started
     * @return the count of rollbacks that were started
     */
    public int getRollbackStartedCount()
    {
        return rollbackStartedCount;
    }

    /**
     * Set the rollback started count attribute
     * @param rollbackStartedCount
     */
    public void setRollbackStartedCount(int rollbackStartedCount)
    {
        this.rollbackStartedCount = rollbackStartedCount;
    }

    /**
     * Returns the number of rollbacks that succeeded
     * @return the count of rollbacks that succeeded
     */
    public int getRollbackSuccessCount()
    {
        return rollbackSuccessCount;
    }

    /**
     * Set the rollback started count attribute
     * @param rollbackSuccessCount
     */
    public void setRollbackSuccessCount(int rollbackSuccessCount)
    {
        this.rollbackSuccessCount = rollbackSuccessCount;
    }

    /**
     * Get the number of failed rollbacks
     * @return the count of rollbacks that failed
     */
    public int getRollbackFailedCount()
    {
        return rollbackFailedCount;
    }

    /**
     * Set the rollbackFailedCount attribute
     * @param rollbackFailedCount
     */
    public void setRollbackFailedCount(int rollbackFailedCount)
    {
        this.rollbackFailedCount = rollbackFailedCount;
    }

    /**
     * Implements the migration started listener
     *
     * @param task the task that ran
     * @param con the context for the task
     */
    public void migrationStarted(MigrationTask task, MigrationContext con)
    {
        setMigrationStartedCount(getMigrationStartedCount() + 1);
    }

    /**
     * Implements the migration succeeded listener
     *
     * @param task the task that ran
     * @param con the context for the task
     */
    public void migrationSuccessful(MigrationTask task, MigrationContext con)
    {
        setMigrationSuccessCount(getMigrationSuccessCount() + 1);
    }

    /**
     * Implements the migration failed listener
     *
     * @param task the task that ran
     * @param con the context for the task
     * @param exception the exception that ocurred
     */
    public void migrationFailed(MigrationTask task, MigrationContext con,
            MigrationException exception)
    {
        setMigrationFailedCount(getMigrationFailedCount() + 1);
    }

    /**
     * Reset all of the counters
     */
    public void resetMigrationListenerState()
    {
        setMigrationFailedCount(0);
        setMigrationStartedCount(0);
        setMigrationSuccessCount(0);
    }

    /**
     * @return Returns the migrationFailedCount.
     */
    public int getMigrationFailedCount()
    {
        return migrationFailedCount;
    }

    /**
     * @param migrationFailedCount The migrationFailedCount to set.
     */
    public void setMigrationFailedCount(int migrationFailedCount)
    {
        this.migrationFailedCount = migrationFailedCount;
    }

    /**
     * @return Returns the migrationStartedCount.
     */
    public int getMigrationStartedCount()
    {
        return migrationStartedCount;
    }

    /**
     * @param migrationStartedCount The migrationStartedCount to set.
     */
    public void setMigrationStartedCount(int migrationStartedCount)
    {
        this.migrationStartedCount = migrationStartedCount;
    }

    /**
     * @return Returns the migrationSuccessCount.
     */
    public int getMigrationSuccessCount()
    {
        return migrationSuccessCount;
    }

    /**
     * @param migrationSuccessCount The migrationSuccessCount to set.
     */
    public void setMigrationSuccessCount(int migrationSuccessCount)
    {
        this.migrationSuccessCount = migrationSuccessCount;
    }

    /**
     * Get JUnit to shut up.
     */
    public void testNoOp()
    {
        // does nothing
    }

    /**
     * @see com.tacitknowledge.util.migration.MigrationListener#initialize(Properties)
     */
    public void initialize(String systemName, Properties properties) throws MigrationException
    {
    }
}
