package com.tacitknowledge.util.migration.jdbc.util;

import com.tacitknowledge.util.migration.MigrationException;
import com.tacitknowledge.util.migration.jdbc.JdbcMigrationLauncher;
import com.tacitknowledge.util.migration.jdbc.JdbcMigrationLauncherFactory;
import com.tacitknowledge.util.migration.jdbc.JdbcMigrationLauncherFactoryLoader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContextEvent;

/**
 * A utility class for migration initialization needs
 *
 * @author Petric Coroli (pcoroli@tacitknowledge.com)
 */
public class MigrationUtil
{

    private static Log log = LogFactory.getLog(MigrationUtil.class);

    public JdbcMigrationLauncherFactory getLauncherFactory()
    {
        return launcherFactory;
    }

    public void setLauncherFactory(JdbcMigrationLauncherFactory launcherFactory)
    {
        this.launcherFactory = launcherFactory;
    }

    private JdbcMigrationLauncherFactory launcherFactory;


    /**
     * Helper method to initiate the migration process.
     *
     * @param sce the <code>ServletContextEvent</code> being handled
     * @throws MigrationException
     */
    public static void doMigrations(final ServletContextEvent sce) throws MigrationException
    {
        JdbcMigrationLauncherFactory launcherFactory =
                new JdbcMigrationLauncherFactoryLoader().createFactory();
        JdbcMigrationLauncher launcher = launcherFactory.createMigrationLauncher(sce);
        launcher.doMigrations();
    }

    /**
     * Helper method to initiate the migration process.
     *
     * @param migrationSystemName the name of the system to migrate
     * @param migrationSettings   additional properties for migration
     * @throws MigrationException
     */
    public static void doMigrations(final String migrationSystemName,
            final String migrationSettings) throws MigrationException
    {
        JdbcMigrationLauncherFactory launcherFactory = new JdbcMigrationLauncherFactoryLoader()
                .createFactory();
        JdbcMigrationLauncher launcher = null;

        if (migrationSettings == null)
        {
            log.info("Using migration.properties (default)");
            launcher = launcherFactory.createMigrationLauncher(migrationSystemName);
        }
        else
        {
            log.info("Using " + migrationSettings);
            launcher = launcherFactory.createMigrationLauncher(migrationSystemName,
                    migrationSettings);
        }

        launcher.doMigrations();
    }

    /**
     * Helper method to initiate the migration process.
     *
     * @param migrationSystemName the name of the system to migrate
     * @param migrationSettings   additional properties for migration
     * @throws MigrationException
     */
    public void doRollbacks(final String migrationSystemName,
            final String migrationSettings, final int[] rollbackLevel, final boolean forceRollback)
            throws MigrationException
    {

        JdbcMigrationLauncher launcher = null;

        if (migrationSettings == null)
        {
            log.info("Using migration.properties (default)");
            launcher = getLauncherFactory().createMigrationLauncher(migrationSystemName);
        }
        else
        {
            log.info("Using " + migrationSettings);
            launcher = getLauncherFactory().createMigrationLauncher(migrationSystemName,
                    migrationSettings);
        }

        launcher.doRollbacks(rollbackLevel, forceRollback);
    }
}
