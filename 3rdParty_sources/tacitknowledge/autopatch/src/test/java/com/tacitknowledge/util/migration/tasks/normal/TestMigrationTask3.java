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

package com.tacitknowledge.util.migration.tasks.normal;

import com.tacitknowledge.util.migration.MigrationContext;
import com.tacitknowledge.util.migration.MigrationException;
import com.tacitknowledge.util.migration.tasks.BaseTestMigrationTask;

/**
 * Basic test migration task.
 * 
 * @author  Scott Askew (scott@tacitknowledge.com)
 */
public class TestMigrationTask3 extends BaseTestMigrationTask
{
    /**
     * Determines if the task should simulate a MigrationException
     */
    private static boolean fail = false;
     
    /**
     * Creates a new <code>TestMigrationTask3</code>.
     */
    public TestMigrationTask3()
    {
        super("TestTask3", 6);
    }
    
    /**
     * @see BaseTestMigrationTask#migrate(MigrationContext)
     */
    public void migrate(MigrationContext context) throws MigrationException
    {
        if (fail)
        {
            throw new MigrationException("Test exception");
        }
        super.migrate(context);
    }
    
    /**
     * Determins if the task should simulate a MigrationException 
     * 
     * @param f <code>true</code>if the task should simulate a MigrationException
     */
    public static void setFail(boolean f)
    {
        TestMigrationTask3.fail = f;
    }
}
