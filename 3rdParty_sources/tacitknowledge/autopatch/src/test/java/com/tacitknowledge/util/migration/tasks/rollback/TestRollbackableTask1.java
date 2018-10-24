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
import com.tacitknowledge.util.migration.RollbackableMigrationTask;
import com.tacitknowledge.util.migration.TestMigrationContext;

public class TestRollbackableTask1 extends BaseTestRollbackableMigrationTask
	implements RollbackableMigrationTask
	{
    
    public TestRollbackableTask1()
    {
        super("TestRollbackableTask1", 8);
    }
    
    public TestRollbackableTask1(int level)
    {
        super("TestRollbackableTask1", level);
    }
    
    public boolean isRollbackSupported()
    {
        return false;
    }
    
    /**
     * @see MigrationTaskSupport#migrate(MigrationContext)
     */
    public void migrate(MigrationContext context) throws MigrationException
    {
        if (context instanceof TestMigrationContext)
        {
            TestMigrationContext ctx = (TestMigrationContext) context;
            ctx.recordExecution(getName());
        }
    }
    
	}
