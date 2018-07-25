package com.tacitknowledge.util.migration;

import java.sql.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import junit.framework.TestCase;

import com.tacitknowledge.util.migration.MigrationContext;
import com.tacitknowledge.util.migration.jdbc.JdbcMigrationLauncher;
import com.tacitknowledge.util.migration.jdbc.JdbcMigrationLauncherFactory;

/**
 * Pin-points a race condition when the AutoPatcher is run from multiple servers on the same schema.
 *
 * @author Jeff Kolesky (jeff@kolesky.com)
 */
public class MultiServerRaceConditionTest extends TestCase
{
    private static Log log = LogFactory.getLog(MultiServerRaceConditionTest.class);

    /**
     * Constructor
     *
     * @param name the name of the test to run
     */
    public MultiServerRaceConditionTest(String name)
    {
        super(name);
    }

    public void tearDown() throws Exception
    {
        super.tearDown();
        Connection conn = DriverManager.getConnection("jdbc:hsqldb:mem:race", "sa", "");
        Statement stmt = conn.createStatement();
        stmt.execute("SHUTDOWN");
    }

    /**
     * Tests that two servers both booting up the AutoPatcher will not both try to apply the same patches
     *
     * @exception Exception if anything goes wrong
     */
    public void testMultiServerRaceCondition() throws Exception
    {
        log.debug("Testing multi server race condition");
        MigrationThread a = new MigrationThread("A");
        MigrationThread b = new MigrationThread("B");

        a.start();
        b.start();

        boolean success = true;

        success &= finish(a);
        success &= finish(b);

        if (!success)
        {
            fail("shouldn't have thrown any exceptions");
        }
    }

    private boolean finish(MigrationThread t) throws Exception
    {
        t.join();
        Exception error = t.getError();
        if (error != null)
        {
            return false;
        }
        return true;
    }

    private static class MigrationThread extends Thread
    {
        private JdbcMigrationLauncherFactory factory;
        private JdbcMigrationLauncher launcher;
        private Exception error;

        private MigrationThread(String name) throws Exception
        {
            super(name);
            this.factory = new JdbcMigrationLauncherFactory();
            this.launcher = this.factory.createMigrationLauncher("race", "multiserver-inttest-migration.properties");
            // initialize the patch table *not* in parallel
            this.launcher.getDatabasePatchLevel((MigrationContext)this.launcher.getContexts().keySet().iterator().next());
        }

        public Exception getError()
        {
            return this.error;
        }

        public void run()
        {
            try
            {
                this.launcher.doMigrations();
            }
            catch (Exception e)
            {
                this.error = e;
                log.error("Unexpected error", this.error);
            }
        }
    }
}

