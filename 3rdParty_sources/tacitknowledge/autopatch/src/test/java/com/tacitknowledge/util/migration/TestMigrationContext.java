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

import java.util.HashMap;
import java.util.Map;

import com.tacitknowledge.util.migration.jdbc.DataSourceMigrationContext;

/**
 * Extends <code>MigrationContext</code> by adding a log of test executions.
 * 
 * @author  Scott Askew (scott@tacitknowledge.com)
 */
public class TestMigrationContext extends DataSourceMigrationContext
{
    /**
     * A record of task executions
     */
    private Map executionLog = new HashMap();
    
    /**
     * Records a successful task execution
     * 
     * @param taskName the name of the task
     */
    public void recordExecution(String taskName)
    {
        executionLog.put(taskName, Boolean.TRUE);
    }
    
    /**
     * Determines if the given task has been executed
     * 
     * @param  taskName the name of the task to validate
     * @return <code>true</code> if the task has successfully executed
     */
    public boolean hasExecuted(String taskName)
    {
        return executionLog.containsKey(taskName);
    }

    /**
     * @see com.tacitknowledge.util.migration.MigrationContext#commit()
     */
    public void commit() throws MigrationException
    {
        // does nothing
    }

    /**
     * @see com.tacitknowledge.util.migration.MigrationContext#rollback()
     */
    public void rollback() throws MigrationException
    {
        // does nothing
    }
}
