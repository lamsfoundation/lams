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

import com.tacitknowledge.util.migration.AutopatchRegistry;
import com.tacitknowledge.util.migration.MigrationException;
import com.tacitknowledge.util.migration.jdbc.util.ConfigurationUtil;
import com.tacitknowledge.util.migration.jdbc.util.MigrationUtil;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.picocontainer.PicoContainer;

/**
 * Launches the migration process as a standalone application.
 * <p/>
 * This class expects the following Java environment parameters:
 * <ul>
 * <li>migration.systemname - the name of the logical system being migrated</li>
 * <li>migration.settings (optional) - the name of the settings file to use for
 * migration</li>
 * </ul>
 * <p/>
 * Below is an example of how this class can be configured in build.xml:
 * <p/>
 * <pre>
 *   ...
 *  &lt;target name=&quot;patch.database&quot; description=&quot;Runs the migration system&quot;&gt;
 *   &lt;java
 *       fork=&quot;true&quot;
 *       classpathref=&quot;patch.classpath&quot;
 *       failonerror=&quot;true&quot;
 *       classname=&quot;com.tacitknowledge.util.migration.jdbc.StandaloneMigrationLauncher&quot;&gt;
 *     &lt;sysproperty key=&quot;migration.systemname&quot; value=&quot;${application.name}&quot;/&gt;
 *   &lt;/java&gt;
 * &lt;/target&gt;
 *   ...
 * </pre>
 *
 * @author Mike Hardy (mike@tacitknowledge.com)
 * @see com.tacitknowledge.util.migration.MigrationProcess
 */
public final class StandaloneMigrationLauncher
{
    /**
     * The force rollback parameter
     */
    private static final String FORCE_ROLLBACK = "-force";

    /**
     * The rollback parameter
     */
    private static final String ROLLBACK = "-rollback";

    /**
     * Class logger
     */
    private static Log log = LogFactory.getLog(StandaloneMigrationLauncher.class);

    private MigrationUtil migrationUtil;

    public StandaloneMigrationLauncher(MigrationUtil migrationUtil)
    {
        this.migrationUtil = migrationUtil;
    }

    /**
     * Run the migrations for the given system name
     *
     * @param arguments the command line arguments, if any
     * @throws Exception if anything goes wrong
     */
    public static void main(final String[] arguments) throws Exception
    {
        AutopatchRegistry registry = new AutopatchRegistry();
        PicoContainer pico = registry.configureContainer();

        JdbcMigrationLauncherFactory launcherFactory = new JdbcMigrationLauncherFactoryLoader().createFactory();
        StandaloneMigrationLauncher migrationLauncher = pico.getComponent(StandaloneMigrationLauncher.class);
        migrationLauncher.getMigrationUtil().setLauncherFactory(launcherFactory);
        migrationLauncher.run(arguments);
        registry.destroyContainer();
    }

    void run(String[] arguments) throws Exception
    {

        String migrationSystemName = ConfigurationUtil.getRequiredParam("migration.systemname",
                System.getProperties(), arguments, 0);
        String migrationSettings = ConfigurationUtil.getOptionalParam("migration.settings", System
                .getProperties(), arguments, 1);

        boolean isRollback = false;
        int[] rollbackLevels = new int[]{};
        boolean forceRollback = false;

        for (int i = 0; i < arguments.length; i++)
        {
            String argument1 = arguments[i];

            if (ROLLBACK.equals(argument1))
            {
                isRollback = true;

                if (i + 2 <= arguments.length)
                {
                    String argument2 = arguments[i + 1];

                    if (argument2 != null)
                    {
                        try
                        {
                            rollbackLevels = getRollbackLevels(argument2);
                        }
                        catch (NumberFormatException nfe)
                        {
                            throw new MigrationException("The rollbacklevels should be integers separated by a comma");
                        }
                    }
                }

                if (rollbackLevels.length == 0)
                {
                    // this indicates that the rollback level has not been set
                    throw new MigrationException(
                            "The rollback flag requires a following integer parameter or a list of integer parameters separated by comma to indicate the rollback level(s).");
                }
            }

            if (FORCE_ROLLBACK.equals(argument1))
            {
                forceRollback = true;
            }

        }

        // The MigrationLauncher is responsible for handling the interaction
        // between the PatchTable and the underlying MigrationTasks; as each
        // task is executed, the patch level is incremented, etc.
        try
        {

            if (isRollback)
            {
                String infoMessage = "Found rollback flag. AutoPatch will attempt to rollback the system to patch level(s) "
                        + ArrayUtils.toString(rollbackLevels) + ".";
                log.info(infoMessage);

                migrationUtil.doRollbacks(migrationSystemName, migrationSettings, rollbackLevels, forceRollback);
            }
            else
            {
                migrationUtil.doMigrations(migrationSystemName, migrationSettings);
            }
        }
        catch (Exception e)
        {
            log.error(e);
            throw e;
        }
    }

    private int[] getRollbackLevels(String rollbackLevelsString) throws NumberFormatException
    {
        String[] rollbackLevelsStringArray = rollbackLevelsString.split(",");
        int[] rollbackLevels = new int[rollbackLevelsStringArray.length];

        for (int i = 0; i < rollbackLevelsStringArray.length; i++)
        {
            rollbackLevels[i] = Integer.parseInt(rollbackLevelsStringArray[i]);
        }
        return rollbackLevels;
    }

    public void setMigrationUtil(MigrationUtil migrationUtil)
    {
        this.migrationUtil = migrationUtil;
    }

    public MigrationUtil getMigrationUtil()
    {
        return migrationUtil;
    }

}
