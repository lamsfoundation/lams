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

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import com.tacitknowledge.util.migration.MigrationProcess;
import com.tacitknowledge.util.migration.builders.MockBuilder;
import junit.framework.TestCase;

import com.tacitknowledge.util.migration.MigrationException;
import com.tacitknowledge.util.migration.MigrationListener;
import com.tacitknowledge.util.migration.test.listeners.TestListener1;
import org.easymock.classextension.IMocksControl;

import static org.easymock.classextension.EasyMock.*;

public class JdbcMigrationLauncherFactoryTest extends TestCase
{
    private static final String MIGRATION_STRATEGY = "migrationStrategy";
    /** class under test */
    JdbcMigrationLauncherFactory factory = null;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        factory = new DistributedJdbcMigrationLauncherFactory();
    }
    
    public void testConfigureMigrationListeners() throws IOException
    {
        String systemName = "test-system";
        Properties properties = new Properties();
        properties.put(systemName + ".listeners", "com.tacitknowledge.util.migration.test.listeners.TestListener1");
        
        factory = new JdbcMigrationLauncherFactory();
        
        List listeners = null;
        try
        {
            listeners = factory.loadMigrationListeners(systemName, properties);
            assertNotNull(listeners);
            assertEquals(1, listeners.size());
            TestListener1 listener = (TestListener1) listeners.get(0);
            assertTrue(listener.getSystemName().equals(systemName));
            
        } catch (MigrationException e)
        {
            fail("Unexpected exception while loading Migration Listeners for " + systemName);
        }
    }

    public void testConfigureMigrationListenersDetectsInvalidListeners()
    {
        String systemName = "test-system";
        Properties properties = new Properties();
        properties.put(systemName + ".listeners", "java.lang.String");
        
        factory = new JdbcMigrationLauncherFactory();
        
        try
        {
            factory.loadMigrationListeners(systemName, properties);
            fail("An exception reporting an invalid listener class should have occurred");
        } catch (MigrationException e)
        {
            // expected
            assertTrue(e.getCause() instanceof ClassCastException);
        }
    }
    
    public void testConfigureMigrationListenersReportsIfItCannotFindListeners()
    {
        String systemName = "test-system";
        Properties properties = new Properties();
        properties.put(systemName + ".listeners", "com.foo.listener");
        
        factory = new JdbcMigrationLauncherFactory();
        
        try
        {
            factory.loadMigrationListeners(systemName, properties);
            fail("An exception reporting an invalid listener class should have occurred");
        } catch (MigrationException e)
        {
            // expected
            assertTrue(e.getCause() instanceof ClassNotFoundException);
        }
    }
    
    public void testConfigureMigrationListenersLoadsMultipleListeners()
    {
        String systemName = "test-system";
        Properties properties = new Properties();
        // notice the spaces and the commas.  I want to make sure it can parse classnames out correctly regardless of whitespace, etc
        properties.put(systemName + ".listeners", " ,com.tacitknowledge.util.migration.test.listeners.TestListener1, com.tacitknowledge.util.migration.test.listeners.TestListener2 , ");
        
        factory = new JdbcMigrationLauncherFactory();
        
        List listeners = null;
        try
        {
            listeners = factory.loadMigrationListeners(systemName, properties);
            assertNotNull(listeners);
            assertEquals(2, listeners.size());
            assertTrue(listeners.get(0) instanceof MigrationListener);
            assertTrue(listeners.get(1) instanceof MigrationListener);
        } catch (MigrationException e)
        {
            fail("Unexpected exception while loading Migration Listeners for " + systemName);
        }
    }
    
    public void testConfigureMigrationListenersCanHandleNoConfiguredListeners()
    {
        String systemName = "test-system";
        Properties properties = new Properties();
        factory = new JdbcMigrationLauncherFactory();
        
        List listeners = null;
        try
        {
            listeners = factory.loadMigrationListeners(systemName, properties);
            assertNotNull(listeners);
            assertEquals(0, listeners.size());
        } catch (MigrationException e)
        {
            fail("Unexpected exception while loading Migration Listeners for " + systemName);
        }
    }
    
    public void testConfigureMigrationListenersCanHandleNullParameters()
    {
        factory = new JdbcMigrationLauncherFactory();
        
        try
        {
            factory.loadMigrationListeners(null, null);
            fail("An exception was expected because the parameters are null");
        } catch (MigrationException e)
        {
            // expected
        }
        
    }

    public void testConfigureMigrationLauncherFactorySetsMigrationStrategy() throws MigrationException {
        factory = new JdbcMigrationLauncherFactory();

        IMocksControl launcherControl = createNiceControl();
        JdbcMigrationLauncher launcher = launcherControl.createMock(JdbcMigrationLauncher.class);

        launcher.setMigrationStrategy(MIGRATION_STRATEGY);

        MigrationProcess migrationProcess = new MigrationProcess();

        expect(launcher.getMigrationProcess()).andReturn(migrationProcess);

        launcherControl.replay();

        String system = "anySystem";
        Properties properties = MockBuilder.getPropertiesWithSystemConfiguration("anySystem",MIGRATION_STRATEGY);



        factory.configureFromMigrationProperties(launcher, system, properties);


        launcherControl.verify();

    }

}
