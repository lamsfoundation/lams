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

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;

import junit.framework.TestCase;

import org.mockejb.jndi.MockContextFactory;

import com.mockrunner.mock.jdbc.MockDataSource;
import com.mockrunner.mock.web.MockServletContext;
import com.tacitknowledge.util.migration.MigrationException;

/**
 * Tests that the factory successfully returns parameters found in the 
 * servlet context.
 * 
 * @author Chris A. (chris@tacitknowledge.com)
 */
public class WebAppServletContextFactoryTest extends TestCase
{
    /**
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() throws Exception
    {
        super.setUp();
        MockContextFactory.setAsInitial();
        InitialContext context = new InitialContext();
        context.createSubcontext("java");
    }
    
    /**
     * @see junit.framework.TestCase#tearDown()
     */
    public void tearDown() throws Exception
    {
        super.tearDown();
        InitialContext context = new InitialContext();
        context.destroySubcontext("java");
    }
    
    /**
     * Tests that the new mechanism for configuring a launcher from a 
     * servlet context works.
     * 
     * @throws NamingException a problem with the test
     */
    public void testConfiguredContext() throws NamingException
    {
        JdbcMigrationLauncherFactory launcherFactory = new JdbcMigrationLauncherFactory();
        MockServletContext sc = new MockServletContext();
        
        String dbType = "mysql";
        String sysName = "testSystem";
        
        sc.setInitParameter("migration.systemname", sysName);
        sc.setInitParameter("migration.readonly", "true");
        sc.setInitParameter("migration.databasetype", dbType);
        sc.setInitParameter("migration.patchpath", "patches");
        sc.setInitParameter("migration.datasource", "java:comp/env/jdbc/testsource");
        
        MockDataSource ds = new MockDataSource();
        InitialContext context = new InitialContext();
        context.bind("java:comp/env/jdbc/testsource", ds);
        ServletContextEvent sce = new ServletContextEvent(sc);
        JdbcMigrationLauncher launcher = null;
        try
        {
            launcher = launcherFactory.createMigrationLauncher(sce);
        } 
        catch (MigrationException e)
        {
            e.printStackTrace();
            fail("There should not have been an exception");
        }

        JdbcMigrationContext jdbcContext = 
            (JdbcMigrationContext) launcher.getContexts().keySet().iterator().next();
        assertEquals(dbType, jdbcContext.getDatabaseType().getDatabaseType());
        assertEquals(sysName, jdbcContext.getSystemName());
        assertEquals(true, launcher.isReadOnly());
    }

    /**
     * Tests that the new mechanism for configuring a launcher from a 
     * servlet context works, with multi-node configurations
     * 
     * @throws NamingException a problem with the test
     */
    public void testConfiguredMultiNodeContext() throws NamingException
    {
        JdbcMigrationLauncherFactory launcherFactory = new JdbcMigrationLauncherFactory();
        MockServletContext sc = new MockServletContext();
        
        String dbType1 = "mysql";
        String dbType2 = "sybase";
        String sysName = "testSystem";
        
        sc.setInitParameter("migration.systemname", sysName);
        sc.setInitParameter("migration.readonly", "true");
        sc.setInitParameter("migration.jdbc.systems", "system1,system2");
        sc.setInitParameter("migration.system1.databasetype", dbType1);
        sc.setInitParameter("migration.system1.datasource", "java:comp/env/jdbc/testsource1");
        sc.setInitParameter("migration.system2.databasetype", dbType2);
        sc.setInitParameter("migration.system2.datasource", "java:comp/env/jdbc/testsource2");
        sc.setInitParameter("migration.patchpath", "patches");
        
        MockDataSource ds = new MockDataSource();
        MockContextFactory.setAsInitial();
        InitialContext context = new InitialContext();
        context.bind("java:comp/env/jdbc/testsource1", ds);
        context.bind("java:comp/env/jdbc/testsource2", ds);
        ServletContextEvent sce = new ServletContextEvent(sc);
        JdbcMigrationLauncher launcher = null;
        try
        {
            launcher = launcherFactory.createMigrationLauncher(sce);
        } 
        catch (MigrationException e)
        {
            e.printStackTrace();
            fail("There should not have been an exception");
        }

        Iterator contextIter = launcher.getContexts().keySet().iterator();
        JdbcMigrationContext jdbcContext1 = (JdbcMigrationContext) contextIter.next();
        JdbcMigrationContext jdbcContext2 = (JdbcMigrationContext) contextIter.next();
        assertEquals(dbType1, jdbcContext1.getDatabaseType().getDatabaseType());
        assertEquals(sysName, jdbcContext1.getSystemName());
        assertEquals(dbType2, jdbcContext2.getDatabaseType().getDatabaseType());
        assertEquals(sysName, jdbcContext2.getSystemName());
        
        assertEquals(true, launcher.isReadOnly());
    }
}
