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

import java.util.Iterator;
import java.util.Map;

/**
 * Launches the migration process as a standalone application.
 * <p/>
 * This class expects the following Java environment parameters:
 * <ul>
 * <li>migration.systemname - the name of the logical system being migrated</li>
 * <li>migration.settings (optional) - the name of the settings file to use for migration</li>
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
 *       classname="com.tacitknowledge.util.migration.jdbc.MigrationInformation"&gt;
 *     &lt;sysproperty key="migration.systemname" value="${application.name}"/&gt;
 *   &lt;/java&gt;
 * &lt;/target&gt;
 *   ...
 * </pre>
 *
 * @author Mike Hardy (mike@tacitknowledge.com)
 * @see com.tacitknowledge.util.migration.MigrationProcess
 * @see com.tacitknowledge.util.migration.jdbc.JdbcMigrationLauncherFactory
 */
public class MigrationInformation
{
    /**
     * Class logger
     */
    private static Log log = LogFactory.getLog(MigrationInformation.class);

    /**
     * Get the migration level information for the given system name
     *
     * @param arguments the command line arguments, if any (none are used)
     * @throws Exception if anything goes wrong
     */
    public static void main(String[] arguments) throws Exception
    {
        MigrationInformation info = new MigrationInformation();
        String migrationSystemName = ConfigurationUtil.getRequiredParam("migration.systemname",
                System.getProperties(), arguments, 0);
        String migrationSettings = ConfigurationUtil.getOptionalParam("migration.settings",
                System.getProperties(), arguments, 1);

        info.getMigrationInformation(migrationSystemName, migrationSettings);
    }

    /**
     * Get the migration level information for the given system name
     *
     * @param migrationSystemName the name of the system
     * @return returns the current highest source code patch number
     * @throws Exception if anything goes wrong
     */
    public int getMigrationInformation(String migrationSystemName) throws Exception
    {
        return getMigrationInformation(migrationSystemName, null);
    }

    /**
     * Get the migration level information for the given system name
     *
     * @param migrationSystemName the name of the system
     * @param migrationSettings   the name of the settings file to use for migration; if
     *                            <code>null</code> is passed then the default name for migration settings will be used
     * @return returns the current highest source code patch number
     * @throws Exception if anything goes wrong
     */
    public int getMigrationInformation(String migrationSystemName, String migrationSettings)
            throws Exception
    {
        // The MigrationLauncher is responsible for handling the interaction
        // between the PatchTable and the underlying MigrationTasks; as each
        // task is executed, the patch level is incremented, etc.
        int highestPatch = 0;

        try
        {   //TODO should be injected
            JdbcMigrationLauncherFactory launcherFactory =
                    new JdbcMigrationLauncherFactoryLoader().createFactory();
            JdbcMigrationLauncher launcher = null;

            if (migrationSettings == null)
            {
                log.info("Using migration.properties (default)");
                launcher = launcherFactory.createMigrationLauncher(migrationSystemName);
            }
            else
            {
                log.info("Using " + migrationSettings);
                launcher = launcherFactory.createMigrationLauncher(migrationSystemName, migrationSettings);
            }

            // Print out information for all contexts
            Map contextMap = launcher.getContexts();
            for (Iterator contextIter = contextMap.keySet().iterator(); contextIter.hasNext();)
            {
                JdbcMigrationContext context = (JdbcMigrationContext) contextIter.next();

                int currentLevel = launcher.getDatabasePatchLevel(context);
                int nextPatchLevel = launcher.getNextPatchLevel();
                log.info("Current Database patch level is        : " + currentLevel);
                int unappliedPatches = nextPatchLevel - launcher.getDatabasePatchLevel(context) - 1;
                log.info("Current number of unapplied patches is : " + unappliedPatches);
                log.info("The next patch to author should be     : " + nextPatchLevel);
                if ((nextPatchLevel - 1) > highestPatch)
                {
                    highestPatch = nextPatchLevel - 1;
                }
            }
            return highestPatch;
        }
        catch (Exception e)
        {
            log.error(e);
            throw e;
        }
    }
}
