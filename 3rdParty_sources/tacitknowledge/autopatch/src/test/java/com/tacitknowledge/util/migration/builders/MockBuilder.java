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

package com.tacitknowledge.util.migration.builders;

import com.tacitknowledge.util.migration.MigrationException;
import com.tacitknowledge.util.migration.PatchInfoStore;
import org.easymock.classextension.IMocksControl;

import java.util.Properties;
import java.util.Set;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createStrictControl;

/**
 * MockBuilder to retrieve simple objects used in different tests
 *
 * @author Oscar Gonzalez (oscar@tacitknowledge.com)
 */
public class MockBuilder
{

    public static PatchInfoStore getPatchInfoStore(int patchLevel) throws MigrationException
    {
        return getPatchInfoStore(patchLevel, null);
    }

    public static PatchInfoStore getPatchInfoStore(int patchLevel, Set<Integer> patchesApplied)
            throws MigrationException
    {
        IMocksControl patchInfoStoreControl = createStrictControl();
        PatchInfoStore patchInfoStoreMock = patchInfoStoreControl.createMock(PatchInfoStore.class);
        expect(patchInfoStoreMock.getPatchLevel()).andReturn(patchLevel).anyTimes();
        expect(patchInfoStoreMock.getPatchesApplied()).andReturn(patchesApplied);
        patchInfoStoreControl.replay();
        return patchInfoStoreMock;
    }

    public static Properties getPropertiesWithSystemConfiguration(String system, String strategy)
    {
        Properties properties = new Properties();
        properties.setProperty(system + ".jdbc.database.type", "hsqldb");
        properties.setProperty(system + ".patch.path", "systemPath");
        properties.setProperty(system + ".jdbc.driver", "jdbcDriver");
        properties.setProperty(system + ".jdbc.url", "jdbcUrl");
        properties.setProperty(system + ".jdbc.username", "jdbcUsername");
        properties.setProperty(system + ".jdbc.password", "jdbcPassword");
        properties.setProperty(system + ".jdbc.dialect", "hsqldb");
        properties.setProperty("migration.strategy", strategy);
        return properties;
    }

    public static Properties getPropertiesWithDistributedSystemConfiguration(String system,
            String strategy, String subsystems)
    {
        Properties properties = getPropertiesWithSystemConfiguration(system, strategy);
        properties.setProperty(system + ".context", system);
        properties.setProperty(system + ".controlled.systems", subsystems);

        return properties;
    }


}
