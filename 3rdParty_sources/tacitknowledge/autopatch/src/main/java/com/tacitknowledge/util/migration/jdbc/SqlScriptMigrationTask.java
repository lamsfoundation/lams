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

import com.tacitknowledge.util.migration.MigrationContext;
import com.tacitknowledge.util.migration.MigrationException;
import com.tacitknowledge.util.migration.MigrationTaskSupport;
import com.tacitknowledge.util.migration.jdbc.util.SqlUtil;
import com.tacitknowledge.util.migration.jdbc.util.SybaseUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Pattern;

/**
 * Adaptss a SQL or DDL database patch for use with the AutoPatch framework.
 *
 * @author Scott Askew (scott@tacitknowledge.com)
 */
public class SqlScriptMigrationTask extends MigrationTaskSupport
{
    /**
     * Class logger
     */
    private static Log log = LogFactory.getLog(SqlScriptMigrationTask.class);

    /**
     * The SQL to execute
     */
    private String sql = null;

    /**
     * SQL to migrate down a patch level
     */
    private String downSql = null;


    /**
     * Creates a new <code>SqlScriptMigrationTask</code>.
     */
    public SqlScriptMigrationTask()
    {
        // do nothing
    }

    /**
     * Creates a new <code>SqlScriptMigrationTask</code>
     *
     * @param name    the name of the SQL script to execute; this is just an
     *                identifier and does not have to correspond to a file name
     * @param level   the patch level of the migration task
     * @param upSql   the SQL to execute to migrate the patch level up one
     * @param downSql the SQL to execute to rollback to this patch
     */
    public SqlScriptMigrationTask(String name, int level, String upSql, String downSql)
    {
        setName(name);
        setLevel(new Integer(level));
        this.sql = upSql;
        this.downSql = downSql;

        setRollbackSupported(!"".equals(downSql));
    }

    /**
     * Creates a new <code>SqlScriptMigrationTask</code>
     *
     * @param name  the name of the SQL script to execute; this is just an
     *              identifier and does not have to correspond to a file name
     * @param level the patch level of the migration task
     * @param sql   the SQL to execute
     */
    public SqlScriptMigrationTask(String name, int level, String sql)
    {
        setName(name);
        setLevel(new Integer(level));
        this.sql = sql;
        this.downSql = "";
    }

    /**
     * Creates a new <code>SqlScriptMigrationTask</code> containing the SQL
     * contained in the given <code>InputStream</code>.
     *
     * @param name  the name of the SQL script to execute; this is just an
     *              identifier and does not have to correspond to a file name
     * @param level the patch level of the migration task
     * @param is    the source of the SQL to execute
     * @throws IOException if there was problem reading the input stream
     */
    public SqlScriptMigrationTask(String name, int level, InputStream is) throws IOException
    {
        setName(name);
        setLevel(new Integer(level));
        StringBuffer sqlBuffer = new StringBuffer();
        BufferedReader buf = new BufferedReader(new InputStreamReader(is));
        String line = buf.readLine();
        while (line != null)
        {
            sqlBuffer.append(line).append("\n");
            line = buf.readLine();
        }
        sql = sqlBuffer.toString();
    }

    /**
     * {@inheritDoc}
     */
    public void up(MigrationContext context) throws MigrationException
    {
        executeSql(context, sql);
    }

    /**
     * {@inheritDoc}
     */
    public void down(MigrationContext context) throws MigrationException
    {
        executeSql(context, downSql);
    }

    /**
     * Executes the passed sql in the passed context.
     *
     * @param ctx       the <code>MigrationContext> to execute the SQL in
     * @param sqlToExec the SQL to execute
     * @throws MigrationException thrown if there is an error when executing the SQL
     */
    private void executeSql(MigrationContext ctx, String sqlToExec)
            throws MigrationException
    {
        JdbcMigrationContext context = (JdbcMigrationContext) ctx;

        Connection conn = null;
        Statement stmt = null;
        String sqlStatement = "";
        ListIterator listIterator = null;
        try
        {
            conn = context.getConnection();

            // cleaning the slate before we execute the patch.
            // This was inspired by a Sybase ASE server that did not allow
            // ALTER TABLE statements in multi-statement transactions.  Instead of putting
            // a if(sybase) conditional, we decided to clean the slate for everyone.
            context.commit();

            List sqlStatements = getSqlStatements(context, sqlToExec);
            for (listIterator = sqlStatements.listIterator(); listIterator.hasNext();)
            {
                sqlStatement = (String) listIterator.next();
                log.debug(getName() + ": Attempting to execute: " + sqlStatement);

                stmt = conn.createStatement();

                // handle sybase special case with illegal commands in multi
                // command transactions
                if (isSybase(context)
                        && SybaseUtil.containsIllegalMultiStatementTransactionCommand(sqlStatement))
                {
                    log.warn("Committing current transaction since patch " + getName()
                            + " contains commands that are not allowed in multi statement"
                            + " transactions.  If the patch contains errors, this patch may"
                            + " not be rolled back cleanly.");
                    context.commit();
                    stmt.execute(sqlStatement);
                    context.commit();
                }
                else // regular case
                {
                    stmt.execute(sqlStatement);
                }

                SqlUtil.close(null, stmt, null);
            }

            context.commit();
        }
        catch (Exception e)
        {
            String message = getName() + ": Error running SQL at statement number "
                    + listIterator.previousIndex() + " \"" + sqlStatement + "\"";
            log.error(message, e);

            if (e instanceof SQLException)
            {
                if (((SQLException) e).getNextException() != null)
                {
                    log.error("Chained SQL Exception", ((SQLException) e).getNextException());
                }
            }

            context.rollback();
            throw new MigrationException(message, e);
        }
        finally
        {
            SqlUtil.close(null, stmt, null);
        }
    }

    public List getSqlStatements(JdbcMigrationContext context)
    {
        return getSqlStatements(context, sql);
    }

    /**
     * Parses the SQL/DDL to execute and returns a list of individual statements.  For database
     * types that support mulitple statements in a single <code>Statement.execute</code> call,
     * this method will return a one-element <code>List</code> containing the entire SQL
     * file.
     *
     * @param context the MigrationContext, to figure out db type and if it
     *                can handle multiple statements at once
     * @return a list of SQL and DDL statements to execute
     */
    public List getSqlStatements(JdbcMigrationContext context, String sqlStatements)
    {
        List statements = new ArrayList();
        if (context.getDatabaseType().isMultipleStatementsSupported())
        {
            statements.add(sqlStatements);
            return statements;
        }

        StringBuffer currentStatement = new StringBuffer();
        boolean inQuotedString = false;
        boolean inComment = false;
        char[] sqlChars = sqlStatements.toCharArray();
        for (int i = 0; i < sqlChars.length; i++)
        {
            if (sqlChars[i] == '\n')
            {
                inComment = false;
            }

            if (!inComment)
            {
                switch (sqlChars[i])
                {
                    case '-':
                    case '/':
                        if (!inQuotedString && i + 1 < sqlChars.length
                                && sqlChars[i + 1] == sqlChars[i])
                        {
                            inComment = true;
                        }
                        else
                        {
                            currentStatement.append(sqlChars[i]);
                        }
                        break;
                    case '\'':
                        inQuotedString = !inQuotedString;
                        currentStatement.append(sqlChars[i]);
                        break;
                    case ';':
                        if (!inQuotedString)
                        {
                            // If we're in a stored procedure, just keep rolling
                            if (isStoredProcedure(context.getDatabaseType().getDatabaseType(),
                                    currentStatement.toString()))
                            {
                                currentStatement.append(sqlChars[i]);
                            }
                            else
                            {
                                statements.add(currentStatement.toString().trim());
                                currentStatement = new StringBuffer();
                            }
                        }
                        else
                        {
                            currentStatement.append(sqlChars[i]);
                        }
                        break;
                    /* sybase uses 'GO' as it's statement delimiter */
                    case 'g':
                    case 'G':
                        /*
                         * Build up a string, reading backwards from the current index to
                         * the previous newline (or beginning of sequence) and from the
                         * current index up to the next newline.  If it matches the regex
                         * for the GO delimiter, then add the statement otherwise
                         * just append the current index's character to currentStatement
                         */
                        if (context.getDatabaseType().getDatabaseType().equals("sybase"))
                        {
                            // read from current index to previous line terminator 
                            // or start of sequence
                            StringBuffer previous = new StringBuffer();
                            for (int j = i - 1; j >= 0; j--)
                            {
                                char c = sqlChars[j];
                                previous.append(c);
                                if (isLineTerminator(c))
                                {
                                    break;
                                }
                            }

                            // reverse previous, since we've been walking backwards, but appending
                            previous = previous.reverse();

                            // read from current index to upcoming line terminator 
                            // or end of sequence.  If it is the GO delimiter, 
                            // we skip up to line terminator
                            StringBuffer after = new StringBuffer();
                            int newIndex = 0;
                            for (int k = i + 1; k < sqlChars.length; k++)
                            {
                                char c = sqlChars[k];
                                after.append(c);
                                newIndex = k;
                                if (isLineTerminator(c))
                                {
                                    break;
                                }
                            }

                            // check against the pattern if its a GO delimiter
                            String possibleDelimiter = previous
                                    .append(sqlChars[i]).append(after).toString();
                            final String delimiterPattern = "^\\s*[Gg][Oo]\\s*$";

                            if (Pattern.matches(delimiterPattern, possibleDelimiter))
                            {
                                // if it's blank, don't bother adding it since Sybase
                                // will complain about empty queries.
                                // This happens if there are two GO's with no
                                // actual SQL to run between them.
                                if (!StringUtils.isBlank(currentStatement.toString().trim()))
                                {
                                    statements.add(currentStatement.toString().trim());
                                }
                                currentStatement = new StringBuffer();
                                // skip up to next line terminator
                                i = newIndex;
                            }
                            else // not a delimiter, so just append
                            {
                                currentStatement.append(sqlChars[i]);
                            }
                        }
                        else // not a sybase db, so just append
                        {
                            currentStatement.append(sqlChars[i]);
                        }
                        break;
                    default:
                        currentStatement.append(sqlChars[i]);
                        break;
                }
            }
        }
        if (currentStatement.toString().trim().length() > 0)
        {
            statements.add(currentStatement.toString().trim());
        }

        return statements;
    }

    /**
     * Return true if the string represents a stored procedure
     *
     * @param databaseType the type of the database
     * @param statement    the statement that may be a stored procedure
     * @return true if the statement is a stored procedure for the given db type
     */
    protected boolean isStoredProcedure(String databaseType, String statement)
    {
        String currentStatement = statement.trim().toLowerCase();
        if ("oracle".equals(databaseType)
                && (currentStatement.startsWith("begin")
                || currentStatement.startsWith("create or replace method")
                || currentStatement.startsWith("create or replace function")
                || currentStatement.startsWith("create or replace procedure")
                || currentStatement.startsWith("create or replace package")))
        {
            return true;
        }
        if ("mysql".equals(databaseType)
                && (currentStatement.startsWith("create procedure")
                || currentStatement.startsWith("create function")))
        {
            return true;
        }

        return false;
    }

    /**
     * return true if c is a line terminator as detailed in
     * http://java.sun.com/j2se/1.5.0/docs/api/java/util/regex/Pattern.html
     *
     * @param c the char to test
     * @return true if it is a line terminator
     */
    protected boolean isLineTerminator(char c)
    {
        return (c == '\n') // newline
                || (c == '\r') // carriage return
                || (c == '\u0085') // next-line
                || (c == '\u2028') // line-separator
                || (c == '\u2029'); // paragraph separator
    }

    /**
     * Check if the current migration context is against a sybase database.
     *
     * @param context the context to check.
     * @return true if context is in a sybase database.
     */
    protected boolean isSybase(JdbcMigrationContext context)
    {
        return context.getDatabaseType().getDatabaseType().equalsIgnoreCase("sybase");
    }

    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        return getName();
    }
}
