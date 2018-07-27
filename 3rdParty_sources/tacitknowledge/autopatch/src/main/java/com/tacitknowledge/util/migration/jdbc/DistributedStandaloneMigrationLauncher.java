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

import com.tacitknowledge.util.migration.jdbc.util.ConfigurationUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Launches the migration process as a standalone application.
 * <p/>
 * This class expects the following Java environment parameters:
 * <ul>
 * <li>migration.systemname - the name of the logical system being migrated</li>
 * </ul>
 * <p/>
 * Below is an example of how this class can be configured in build.xml:
 * <pre>
 *   ...
 *  &lt;target name="patch.database" description="Runs the migration system"&gt;
 *   &lt;java
 *       fork="true"
 *       classpathref="patch.classpath"
 *       failonerror="true"
 *       classname=
 *         "com.tacitknowledge.util.migration.jdbc.DistributedStandaloneMigrationLauncher"&gt;
 *     &lt;sysproperty key="migration.systemname" value="${application.name}"/&gt;
 *   &lt;/java&gt;
 * &lt;/target&gt;
 *   ...
 * </pre>
 *
 * @author Mike Hardy (mike@tacitknowledge.com)
 * @see com.tacitknowledge.util.migration.DistributedMigrationProcess
 */
public class DistributedStandaloneMigrationLauncher
{
    /**
     * Class logger
     */
    private static Log log = LogFactory.getLog(DistributedStandaloneMigrationLauncher.class);

    /**
     * Private constructor - this object shouldn't be instantiated
     */
    private DistributedStandaloneMigrationLauncher()
    {
        // does nothing
    }

    /**
     * Run the migrations for the given system name
     *
     * @param arguments the command line arguments, if any (none are used)
     * @throws Exception if anything goes wrong
     */
    public static void main(String[] arguments) throws Exception
    {
        String systemName = ConfigurationUtil.getRequiredParam("migration.systemname",
                System.getProperties(), arguments);

        String migrationSettings = ConfigurationUtil.getOptionalParam("migration.settings",
                System.getProperties(), arguments, 1);

        // The MigrationLauncher is responsible for handling the interaction
        // between the PatchTable and the underlying MigrationTasks; as each
        // task is executed, the patch level is incremented, etc.
        try
        {
            DistributedJdbcMigrationLauncherFactory factory =
                    new DistributedJdbcMigrationLauncherFactory();
            JdbcMigrationLauncher launcher = null;

            if (migrationSettings == null)
            {
                log.info("Using migration.properties (default)");
                launcher = factory.createMigrationLauncher(systemName);
            }
            else
            {
                log.info("Using " + migrationSettings);
                launcher = factory.createMigrationLauncher(systemName, migrationSettings);
            }
            launcher.doMigrations();

        }
        catch (Exception e)
        {
            log.error(e);
            throw e;
        }
    }
}
