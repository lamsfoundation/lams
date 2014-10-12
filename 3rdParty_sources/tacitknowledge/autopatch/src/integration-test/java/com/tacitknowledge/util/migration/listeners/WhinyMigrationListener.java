package com.tacitknowledge.util.migration.listeners;


import com.tacitknowledge.util.migration.MigrationContext;
import com.tacitknowledge.util.migration.MigrationException;
import com.tacitknowledge.util.migration.MigrationListener;
import com.tacitknowledge.util.migration.MigrationTask;
import com.tacitknowledge.util.migration.jdbc.JdbcMigrationContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Properties;


/**
 * @author Alex Soto (apsoto@gmail.com)
 */
public class WhinyMigrationListener implements MigrationListener
{
    /**
     * Class logger
     */
    private static Log log = LogFactory.getLog(WhinyMigrationListener.class);

    protected String getTaskInfo(MigrationTask task, MigrationContext context)
    {
        String ctxInfo = "";
        if (context instanceof JdbcMigrationContext)
        {
            JdbcMigrationContext ctx = (JdbcMigrationContext) context;
            ctxInfo += ctx.getSystemName() + " : " + ctx.getDatabaseName();
        }

        return "Task => (" + task.toString() + "), Context => (" + ctxInfo + ")";
    }

    public void migrationFailed(MigrationTask task, MigrationContext context, MigrationException e) throws MigrationException
    {
        log.debug("MIGRATION FAILED, " + getTaskInfo(task, context) + " WAHHH!!!");
    }

    public void migrationStarted(MigrationTask task, MigrationContext context) throws MigrationException
    {
        log.debug("MIGRATION STARTED, " + getTaskInfo(task, context) + " WAHHH!!!");
    }

    public void migrationSuccessful(MigrationTask task, MigrationContext context) throws MigrationException
    {
        log.debug("MIGRATION SUCCEEDED, " + getTaskInfo(task, context) + " WAHHH!!!");
    }

    /**
     * @see com.tacitknowledge.util.migration.MigrationListener#initialize(Properties)
     */
    public void initialize(String systemName, Properties properties) throws MigrationException
    {
        log.debug("MIGRATION LISTENER INTIALIZED FOR " + systemName + " SYSTEM, WAHHH!!!");
    }
}
