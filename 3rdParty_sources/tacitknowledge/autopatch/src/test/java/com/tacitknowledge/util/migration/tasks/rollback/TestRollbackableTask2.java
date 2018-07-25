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
import com.tacitknowledge.util.migration.RollbackableMigrationTask;
import com.tacitknowledge.util.migration.TestMigrationContext;

public class TestRollbackableTask2 extends BaseTestRollbackableMigrationTask 
    implements RollbackableMigrationTask
{
    private static Integer patchLevelOverride = new Integer(9);
    
    public static void setPatchLevelOverride(Integer i) 
    {
        patchLevelOverride = (i);
    }
    
    public Integer getLevel() 
    {
        return patchLevelOverride;
    }
    
    public static void reset() 
    {
        patchLevelOverride = new Integer(9);
    }
    
    public TestRollbackableTask2()
    {
        super("TestRollbackableTask2", 9);
    }
    
    public void down(MigrationContext context) throws MigrationException
    {
        if (context instanceof TestMigrationContext)
        {
            TestMigrationContext ctx = (TestMigrationContext) context;
            ctx.recordExecution(getName());
        }
    }
    
    public void up(MigrationContext context) throws MigrationException 
    {
        if (context instanceof TestMigrationContext)
        {
            TestMigrationContext ctx = (TestMigrationContext) context;
            ctx.recordExecution(getName());
        }
    }
}
