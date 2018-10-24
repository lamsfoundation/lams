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

import junit.framework.TestCase;

/**
 * Exercise the MigrationBroadcaster
 * 
 * @author Mike Hardy (mike@tacitknowledge.com)
 */
public class MigrationBroadcasterTest extends TestCase implements MigrationListener
{
    /** whether we've been started */
    private boolean started = false;
    
    /** whether we've succeeded */
    private boolean succeeded = false;
    
    /** whether we've failed */
    private boolean failed = false;

    /** The broadcaster being tested */
    private MigrationBroadcaster broadcaster = null;
    
    /**
     * Set up our test state
     */
    public void setUp()
    {
        broadcaster = new MigrationBroadcaster();
        broadcaster.addListener(this);
    }
    
    /**
     * Test how the broadcaster deals with null listeners
     */
    public void testNullListeners()
    {
        try
        {
            broadcaster.addListener(null);
            fail("We should have failed - adding null listeners is illegal");
        }
        catch (IllegalArgumentException iae)
        {
            // we expect this
        }
        
        try
        {
            broadcaster.removeListener(null);
            fail("We should have failed - removing null listeners is illegal");
        }
        catch (IllegalArgumentException iae)
        {
            // we expect this
        }
    }
    
    /**
     * Test add/remove listeners
     */
    public void testAddRemoveListeners()
    {
        // we're already there (from setUp), so try removing us
        assertTrue(broadcaster.removeListener(this));
        
        // now we're not there, make sure it fails
        assertFalse(broadcaster.removeListener(this));
    }
    
    /**
     * Test broadcast of the basic events
     * 
     * @exception MigrationException if the notify fails
     */
    public void testBroadcast() throws MigrationException
    {
        assertFalse(started);
        broadcaster.notifyListeners(null, null, MigrationBroadcaster.TASK_START);
        assertTrue(started);
        assertFalse(succeeded);
        broadcaster.notifyListeners(null, null, MigrationBroadcaster.TASK_SUCCESS);
        assertTrue(succeeded);
        assertFalse(failed);
        broadcaster.notifyListeners(null, null, MigrationBroadcaster.TASK_FAILED);
        assertTrue(failed);
        try
        {
            broadcaster.notifyListeners(null, null, Integer.MAX_VALUE);
            fail("We threw an unknown event - it should have failed");
        }
        catch (IllegalArgumentException iae)
        {
            // we expect this
        }
    }
    
    /**
     * @see MigrationListener#migrationStarted(MigrationTask, MigrationContext)
     */
    public void migrationStarted(MigrationTask task, MigrationContext context)
    {
        started = true;
    }

    /**
     * @see MigrationListener#migrationSuccessful(MigrationTask, MigrationContext)
     */
    public void migrationSuccessful(MigrationTask task, MigrationContext context)
    {
        succeeded = true;
    }

    /**
     * @see MigrationListener#migrationFailed(MigrationTask, MigrationContext, MigrationException)
     */
    public void migrationFailed(MigrationTask task, 
                                MigrationContext context,
                                MigrationException e)
    {
        failed = true;
    }

    /**
     * @see com.tacitknowledge.util.migration.MigrationListener#initialize(Properties)
     */
    public void initialize(String systemName, Properties properties) throws MigrationException
    {
    }
}
