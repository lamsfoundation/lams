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

/**
 * A single, idempotent and migration task, which also supports rollbacks.
 *
 * @author Artie Pesh-Imam (apeshimam@tacitknowledge.com)
 */
public interface RollbackableMigrationTask extends MigrationTask
{

    /**
     * Performs a migration
     *
     * @param context the <code>MigrationContext</code> for this run.
     * @throws MigrationException if an unexpected error occurs
     */
    public void up(MigrationContext context) throws MigrationException;

    /**
     * Performs a rollback
     *
     * @param context the <code>MigrationContext</code> for this run.
     * @throws MigrationException if an unexpected error occurrs
     */
    public void down(MigrationContext context) throws MigrationException;

    /**
     * Returns a boolean indicating if this task can be rolled back.
     *
     * @return a boolean indicating if the task can be rolled back.
     */
    public boolean isRollbackSupported();

    /**
     * Returns the name of this migration task.
     *
     * @return the name of this migration task
     */
    public String getName();

    /**
     * Returns the relative order in which this migration should occur.
     *
     * @return the relative order in which this migration should occur; may never
     *         return <code>null</code>
     */
    public Integer getLevel();
}
