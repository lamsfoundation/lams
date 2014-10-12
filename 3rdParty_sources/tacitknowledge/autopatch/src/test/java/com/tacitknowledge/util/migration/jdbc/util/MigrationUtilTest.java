/* Copyright 2011 Tacit Knowledge
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

package com.tacitknowledge.util.migration.jdbc.util;


import com.tacitknowledge.util.migration.MigrationException;
import com.tacitknowledge.util.migration.jdbc.JdbcMigrationLauncher;
import com.tacitknowledge.util.migration.jdbc.JdbcMigrationLauncherFactory;
import junit.framework.TestCase;
import org.easymock.classextension.IMocksControl;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createStrictControl;

public class MigrationUtilTest extends TestCase{

    private static final int PATCHES_APPLIED = 2;
    private static final int ROLLBACK_LEVEL = 3;
    private static final int[] ROLLBACK_LEVELS = new int[]{ROLLBACK_LEVEL};
    private MigrationUtil migrationUtil;
    private IMocksControl mockControl;
    private JdbcMigrationLauncherFactory launcherFactoryMock;
    private JdbcMigrationLauncher launcherMock;
    private static final String MIGRATION_NAME = "mysystem";
    private static final boolean FORCE_ROLLBACK = false;
    private static final String MIGRATION_SETTINGS = "mymigrationsettings";

    protected void setUp() throws Exception {
        super.setUp();

        mockControl = createStrictControl();
        launcherFactoryMock = mockControl.createMock(JdbcMigrationLauncherFactory.class);
        launcherMock = mockControl.createMock( JdbcMigrationLauncher.class );
        migrationUtil = new MigrationUtil();
        migrationUtil.setLauncherFactory(launcherFactoryMock);
    }

    public void testDoRollbacksActionWithoutMigrationSettings() throws MigrationException {

        expect( launcherFactoryMock.createMigrationLauncher(MIGRATION_NAME)).andReturn(launcherMock);
        expect(launcherMock.doRollbacks(ROLLBACK_LEVELS, FORCE_ROLLBACK)).andReturn(PATCHES_APPLIED);
        mockControl.replay();


        migrationUtil.doRollbacks(MIGRATION_NAME, null, ROLLBACK_LEVELS, FORCE_ROLLBACK);
        mockControl.verify();

    }

    public void testDoRollbackActionWithMigrationSettings() throws MigrationException{

        expect( launcherFactoryMock.createMigrationLauncher(MIGRATION_NAME, MIGRATION_SETTINGS)).andReturn(launcherMock);
        expect( launcherMock.doRollbacks(ROLLBACK_LEVELS, FORCE_ROLLBACK)).andReturn(PATCHES_APPLIED);
        mockControl.replay();

        migrationUtil.doRollbacks(MIGRATION_NAME, MIGRATION_SETTINGS,ROLLBACK_LEVELS,FORCE_ROLLBACK);
        mockControl.verify();

    }
}
