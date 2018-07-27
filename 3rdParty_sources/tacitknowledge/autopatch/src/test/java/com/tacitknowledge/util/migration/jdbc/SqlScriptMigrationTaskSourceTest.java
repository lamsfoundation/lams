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

import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tacitknowledge.util.migration.MigrationException;
import com.tacitknowledge.util.migration.RollbackableMigrationTask;

/**
 * Exercise the SqlScriptMigrationTaskSource
 * 
 * @author Mike Hardy (mike@tacitknowledge.com)
 */
public class SqlScriptMigrationTaskSourceTest extends TestCase
{
    /** Class logger */
    private static Log log = LogFactory.getLog(SqlScriptMigrationTaskSource.class);
    
    /**
     * Test loading up all the scripts in our test package
     */
    public void testScriptLoad()
    {
        SqlScriptMigrationTaskSource source = new SqlScriptMigrationTaskSource();
        List tasks = null;
        try
        {
            tasks = source.getMigrationTasks(this.getClass().getPackage().getName() + ".test");
        }
        catch (MigrationException me)
        {
            log.info("Unexpectedly caught: "+ me);
            fail("There shouldn't have been a problem loading the tasks: "+ me);
        }
        
        // There are 3 scripts in our package of scripts, make sure they are all here
        assertEquals(3, tasks.size());
    }
    
    /**
     * Test that a Migration which does not have a rollback script correctly 
     * returns false for isRollbackSupported.
     */
    public void testNonRollbackableScript() 
    {
        SqlScriptMigrationTaskSource source = new SqlScriptMigrationTaskSource();
        List tasks = null;
        RollbackableMigrationTask task = null;
        try 
        {
            tasks = source.getMigrationTasks(this.getClass().getPackage().getName() + ".test");
            
            for(Iterator i=tasks.iterator(); i.hasNext(); ) 
            {
                //patch with ID 2 has no rollback
                task = (RollbackableMigrationTask) i.next();
                if(task.getLevel().equals(Integer.valueOf(2)))
                {
                    assertFalse(task.isRollbackSupported());
                }
                else
                {    
                    assertTrue(task.isRollbackSupported());
                }
            }
            
        }
        catch (MigrationException me)
        {
            log.info("Unexpectedly caught: "+ me);
            fail("There shouldn't have been a problem loading the tasks: "+ me);
        }
    }
}