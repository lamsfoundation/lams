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

package com.tacitknowledge.util.migration.jdbc.loader;

import com.tacitknowledge.util.migration.MigrationContext;
import com.tacitknowledge.util.migration.MigrationException;
import com.tacitknowledge.util.migration.MigrationTaskSupport;
import com.tacitknowledge.util.migration.jdbc.JdbcMigrationContext;
import com.tacitknowledge.util.migration.jdbc.util.SqlUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;

/**
 * A loader class that supports DbUnit's FlatXmlDataSet format.
 * The data in the xml file is loaded via a dbunit INSERT operation.
 *
 * @author Alex Soto (apsoto@gmail.com)
 */
public class FlatXmlDataSetMigrationTask extends MigrationTaskSupport
{
    /**
     * Class logger
     */
    private static Log log = LogFactory.getLog(FlatXmlDataSetMigrationTask.class);

    /**
     * Default ctor
     */
    public FlatXmlDataSetMigrationTask()
    {
        // does nothing
    }

    /**
     * Run the migration using the given context.
     *
     * @param context the context to run under
     * @throws MigrationException if an unexpected error occurs
     */
    public void migrate(MigrationContext context) throws MigrationException
    {
        log.debug("Executing patch " + getLevel());
        // down casting, technically not safe, but everyone else is doing it.
        JdbcMigrationContext jdbcContext = (JdbcMigrationContext) context;
        // used to close connection in finally block
        Connection contextConnection = null;
        try
        {
            FlatXmlDataSet xmlDataSet = new FlatXmlDataSet(getXmlAsStream());
            // Set contextConnection so it can be accessed in the finally block.
            contextConnection = jdbcContext.getConnection();

            // run the data load
            IDatabaseConnection connection = new DatabaseConnection(contextConnection);
            DatabaseOperation.INSERT.execute(connection, xmlDataSet);
            context.commit();

            // Closing here instead of in finally block to keep the signature of this from throwing
            // a SqlException.  Exceptional condition handled in finally block to make sure
            // we don't leak a connection.
            connection.close();
        }
        catch (Exception e)
        {
            log.debug("Unable to patch due to " + e.getMessage());
            context.rollback();
            throw new MigrationException("Unable to patch", e);
        }
        finally
        {
            // Might already be closed if everything worked fine and connection.close was called 
            // above, in that case, calling close again shouldn't do any harm.  However, if an 
            // exception occurred the DBUnit based connection wrapper didn't get closed, so we 
            // catch that case here.
            SqlUtil.close(contextConnection, null, null);
        }
    }

    /**
     * get the file with name getName() as a stream.
     *
     * @return a Stream
     * @throws IOException if unable to load file
     */
    protected InputStream getXmlAsStream() throws IOException
    {
        try
        {
            FileLoadingUtility utility = new FileLoadingUtility(getName());
            return utility.getResourceAsStream();
        }
        catch (NullPointerException npe)
        {
            throw new IOException("Unable to find xml file named '" + getName() + "'");
        }
    }


}
