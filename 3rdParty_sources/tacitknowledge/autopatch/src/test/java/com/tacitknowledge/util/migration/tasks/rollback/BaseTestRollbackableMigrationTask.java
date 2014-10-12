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

package com.tacitknowledge.util.migration.tasks.rollback;

import com.tacitknowledge.util.migration.MigrationContext;
import com.tacitknowledge.util.migration.MigrationException;
import com.tacitknowledge.util.migration.MigrationTaskSupport;
import com.tacitknowledge.util.migration.TestMigrationContext;

/**
 * Base class for rollback task tests.
 * 
 * @author Artie Pesh-Imam (apeshimam@tacitknowledge.com)
 */
public abstract class BaseTestRollbackableMigrationTask extends	MigrationTaskSupport
{
    /**
     * Create a new <code>BaseTestRollbackableMigrationTask</code>.
     * 
     * @param name
     *                the name of the task
     * @param level
     *                the patch level of the task
     */
    protected BaseTestRollbackableMigrationTask(String name, int level)
    {
	setName(name);
	setLevel(new Integer(level));
    }

    /**
     * Perform a rollback
     * @param context migration context
     * @throws MigrationException if an error happens
     */
    public void down(MigrationContext context) throws MigrationException
    {
	if (context instanceof TestMigrationContext)
	{
	    TestMigrationContext ctx = (TestMigrationContext) context;
	    ctx.recordExecution(getName());
	}
    }

    /**
     * Return true indicating that rollback is supported
     * @return true if rollback is supported.
     */
    public boolean isRollbackSupported()
    {
	return true;
    }

}
