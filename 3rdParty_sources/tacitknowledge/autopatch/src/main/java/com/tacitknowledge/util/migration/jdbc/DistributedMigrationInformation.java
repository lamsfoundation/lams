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

import java.util.Map;

/**
 * Launches the migration process as a standalone application.
 * <p/>
 * This class expects the following Java environment parameters:
 * <ul>
 * <li>migration.systemname - the name of the logical system being migrated</li>
 * </ul>
 * <p/>
 * Alternatively, you can pass the migration system name on the command line as the
 * first argument.
 * <p/>
 * Below is an example of how this class can be configured in an Ant build.xml file:
 * <pre>
 *   ...
 *  &lt;target name="patch.information" description="Prints out information about patch levels"&gt;
 *   &lt;java
 *       fork="true"
 *       classpathref="patch.classpath"
 *       failonerror="true"
 *       classname="com.tacitknowledge.util.migration.jdbc.DistributedMigrationInformation"&gt;
 *     &lt;sysproperty key="migration.systemname" value="${application.name}"/&gt;
 *   &lt;/java&gt;
 * &lt;/target&gt;
 *   ...
 * </pre>
 *
 * @author Mike Hardy (mike@tacitknowledge.com)
 * @see com.tacitknowledge.util.migration.DistributedMigrationProcess
 * @see com.tacitknowledge.util.migration.jdbc.DistributedJdbcMigrationLauncherFactory
 */
public class DistributedMigrationInformation
{
    /**
     * Class logger
     */
    private static Log log = LogFactory.getLog(DistributedMigrationInformation.class);

    /**
     * Get the migration level information for the given system name
     *
     * @param arguments the command line arguments, if any (none are used)
     * @throws Exception if anything goes wrong
     */
    public static void main(String[] arguments) throws Exception
    {
        DistributedMigrationInformation info = new DistributedMigrationInformation();
        String migrationName = System.getProperty("migration.systemname");
        String migrationSettings = ConfigurationUtil.getOptionalParam("migration.settings",
                System.getProperties(), arguments, 1);

        if (migrationName == null)
        {
            if ((arguments != null) && (arguments.length > 0))
            {
                migrationName = arguments[0].trim();
            }
            else
            {
                throw new IllegalArgumentException("The migration.systemname "
                        + "system property is required");
            }
        }
        // info.getMigrationInformation(migrationName);
        info.getMigrationInformation(migrationName, migrationSettings);
    }

    /**
     * Get the migration level information for the given system name
     *
     * @param systemName the name of the system
     * @return returns the current highest source code patch number
     * @throws Exception if anything goes wrong
     */
    public int getMigrationInformation(String systemName) throws Exception
    {
        return getMigrationInformation(systemName, null);
    }

    /**
     * Get the migration level information for the given system name
     *
     * @param systemName        the name of the system
     * @param migrationSettings name of alternate migration.properties file to use
     * @return returns the current highest source code patch number
     * @throws Exception if anything goes wrong
     */
    public int getMigrationInformation(String systemName, String migrationSettings) throws Exception
    {
        // The MigrationLauncher is responsible for handling the interaction
        // between the PatchTable and the underlying MigrationTasks; as each
        // task is executed, the patch level is incremented, etc.
        try
        {
            DistributedJdbcMigrationLauncherFactory factory =
                    new DistributedJdbcMigrationLauncherFactory();
            DistributedJdbcMigrationLauncher launcher = null;

            if (migrationSettings == null)
            {
                log.info("Using migration.properties (default)");
                launcher = (DistributedJdbcMigrationLauncher) factory.createMigrationLauncher(systemName);
            }
            else
            {
                log.info("Using " + migrationSettings);
                launcher = (DistributedJdbcMigrationLauncher) factory.createMigrationLauncher(systemName, migrationSettings);
            }
            // FIXME test that the migration information is correct
            Map contextMap = launcher.getContexts();

            JdbcMigrationContext context =
                    (JdbcMigrationContext) contextMap.keySet().iterator().next();

            int currentLevel = launcher.getDatabasePatchLevel(context);
            int nextPatchLevel = launcher.getNextPatchLevel();
            log.info("Current Database patch level is        : " + currentLevel);
            int unappliedPatches = nextPatchLevel - launcher.getDatabasePatchLevel(context) - 1;
            log.info("Current number of unapplied patches is : " + unappliedPatches);
            log.info("The next patch to author should be     : " + nextPatchLevel);
            return (nextPatchLevel - 1);
        }
        catch (Exception e)
        {
            log.error(e);
            throw e;
        }
    }
}
