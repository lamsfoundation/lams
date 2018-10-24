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

import com.mockrunner.mock.jdbc.MockDataSource;
import com.tacitknowledge.util.migration.jdbc.AutoPatchService;
import com.tacitknowledge.util.migration.jdbc.DatabaseType;
import com.tacitknowledge.util.migration.jdbc.DistributedAutoPatchService;
import com.tacitknowledge.util.migration.jdbc.TestAutoPatchService;
import com.tacitknowledge.util.migration.jdbc.TestDataSourceMigrationContext;
import com.tacitknowledge.util.migration.jdbc.TestDistributedAutoPatchService;
import com.tacitknowledge.util.migration.jdbc.DistributedJdbcMigrationLauncherFactoryTest;

/**
 * Test the Distributed auto patch service to make sure it configures
 * and runs correctly
 * 
 * @author Mike Hardy (mike@tacitknowledge.com)
 */
public class DistributedAutoPatchServiceTest extends DistributedJdbcMigrationLauncherFactoryTest
{
    /**
     * Just delegates to the superclass
     * 
     * @param name of the test to run
     */
    public DistributedAutoPatchServiceTest(String name)
    {
        super(name);
    }
    
    /**
     * Configures a DistributedAutoPatchService and it's child AutoPatchService objects
     * to match the "migration.properties" configuration in the AutoPatch test suite.
     * This let's us reuse the actual functionality checks that verify the configuration
     * was correct, as the launcher is the same, it's just the adapter that's different.
     * 
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        // configure the controlled AutoPatchService, first by calling super
        super.setUp();
        
        // core sub-system
        AutoPatchService coreService = new TestAutoPatchService();
        coreService.setSystemName("core");
        coreService.setDatabaseType("postgres");
        coreService.setDataSource(new MockDataSource());
        coreService.setPatchPath("patch.core:com.tacitknowledge.util.migration.jdbc.test");
        
        // orders: patch path patches.orders.com.tacitknowledge.util.migration.tasks.normal
        AutoPatchService ordersService = new TestAutoPatchService();
        ordersService.setSystemName("orders");
        ordersService.setDatabaseType("postgres");
        ordersService.setDataSource(new MockDataSource());
        ordersService.setPatchPath("patch.orders:com.tacitknowledge.util.migration.tasks.normal");
        
        // catalog: patch path patches.catalog
        AutoPatchService catalogService = new TestAutoPatchService();
        catalogService.setPatchPath("patch.catalog");
        
        // make catalog a multi-node patch service
        TestDataSourceMigrationContext catalogContext1 = new TestDataSourceMigrationContext();
        TestDataSourceMigrationContext catalogContext2 = new TestDataSourceMigrationContext();
        catalogContext1.setSystemName("catalog");
        catalogContext2.setSystemName("catalog");
        catalogContext1.setDatabaseType(new DatabaseType("postgres"));
        catalogContext2.setDatabaseType(new DatabaseType("postgres"));
        catalogContext1.setDataSource(new MockDataSource());
        catalogContext2.setDataSource(new MockDataSource());
        catalogService.addContext(catalogContext1);
        catalogService.addContext(catalogContext2);
         
        
        // configure the DistributedAutoPatchService
        DistributedAutoPatchService distributedPatchService = new TestDistributedAutoPatchService();
        distributedPatchService.setSystemName("orchestration");
        distributedPatchService.setDatabaseType("postgres");
        distributedPatchService.setReadOnly(false);
        AutoPatchService[] controlledSystems = new AutoPatchService[3];
        controlledSystems[0] = coreService;
        controlledSystems[1] = ordersService;
        controlledSystems[2] = catalogService;
        distributedPatchService.setControlledSystems(controlledSystems);
        distributedPatchService.setDataSource(new MockDataSource());
        
        // instantiate everything
        setLauncher(distributedPatchService.getLauncher());
        
        // set ourselves up as a listener for any migrations that run
        getLauncher().getMigrationProcess().addListener(this);
    }
}
