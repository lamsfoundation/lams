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

/**
 * Receives notifications regarding task rollbacks.
 *
 * @author Artie Pesh-Imam (apeshimam@tacitknowledge.com)
 */
public interface RollbackListener extends MigrationListener
{
    /**
     * Initialize the rollback listener.  This provides an opportunity
     * for the RollbackListener to initialize itself before patching
     * begins.
     *
     * @param properties The properties loaded from migration.properties
     */
    public void initialize(String systemName, Properties properties) throws MigrationException;

    /**
     * Notifies the listener that the given rollback is about to start execution.
     *
     * @param task    the recently finished task
     * @param context the migration context
     * @throws MigrationException if an unrecoverable error occurs
     */
    public void rollbackStarted(RollbackableMigrationTask task, MigrationContext context)
            throws MigrationException;

    /**
     * Notifies the listener that the given roolback has completed execution.
     *
     * @param task    the recently finished task
     * @param context the migration context
     * @throws MigrationException if an unrecoverable error occurs
     */
    public void rollbackSuccessful(RollbackableMigrationTask task, int rollbackLevel,
            MigrationContext context)
            throws MigrationException;

    /**
     * Notifies the listener that the given rollback has completed execution.
     *
     * @param task    the recently finished task
     * @param context the migration context
     * @param e       the <code>MigrationException</code> thrown by the task
     * @throws MigrationException if an unrecoverable error occurs
     */
    public void rollbackFailed(RollbackableMigrationTask task,
            MigrationContext context, MigrationException e) throws MigrationException;

}

