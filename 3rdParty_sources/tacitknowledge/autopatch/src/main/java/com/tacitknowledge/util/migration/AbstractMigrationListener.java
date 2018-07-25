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
 * Abstract base class for MigrationListener authors that aren't interested in
 * implementing all the MigrationListener events.
 *
 * @author Alex Soto (apsoto@gmail.com)
 */
public abstract class AbstractMigrationListener implements MigrationListener
{

    /**
     * @see com.tacitknowledge.util.migration.MigrationListener#migrationFailed(com.tacitknowledge.util.migration.MigrationTask, com.tacitknowledge.util.migration.MigrationContext, com.tacitknowledge.util.migration.MigrationException)
     */
    public void migrationFailed(MigrationTask task, MigrationContext context,
            MigrationException e) throws MigrationException
    {
    }

    /**
     * @see com.tacitknowledge.util.migration.MigrationListener#migrationStarted(com.tacitknowledge.util.migration.MigrationTask, com.tacitknowledge.util.migration.MigrationContext)
     */
    public void migrationStarted(MigrationTask task, MigrationContext context)
            throws MigrationException
    {
    }

    /**
     * @see com.tacitknowledge.util.migration.MigrationListener#migrationSuccessful(com.tacitknowledge.util.migration.MigrationTask, com.tacitknowledge.util.migration.MigrationContext)
     */
    public void migrationSuccessful(MigrationTask task, MigrationContext context)
            throws MigrationException
    {
    }

}
