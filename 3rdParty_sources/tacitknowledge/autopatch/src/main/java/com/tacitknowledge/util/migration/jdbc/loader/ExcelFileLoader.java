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
import com.tacitknowledge.util.migration.jdbc.DataSourceMigrationContext;
import com.tacitknowledge.util.migration.jdbc.util.SqlUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * This is a utility class for reading excel files and
 * performing a database insert based upon a cell value
 * provided.
 *
 * @author Chris A. (chris@tacitknowledge.com)
 */
public abstract class ExcelFileLoader extends MigrationTaskSupport
{
    /**
     * Class logger
     */
    private static Log log = LogFactory.getLog(ExcelFileLoader.class);

    /**
     * Obtains a database connection, reads a file assumed to be in Excel
     * format based on the name provided <code>getName()</code>.  Calls the
     * abstract method <code>processWorkbook()</code>
     *
     * @param ctx the <code>JdbcMigrationContext</code>
     * @throws MigrationException if an unexpected error occurs
     */
    public void migrate(MigrationContext ctx) throws MigrationException
    {
        DataSourceMigrationContext context = (DataSourceMigrationContext) ctx;
        FileLoadingUtility utility = new FileLoadingUtility(getName());
        Connection conn = null;

        try
        {
            conn = context.getConnection();
            POIFSFileSystem fs = new POIFSFileSystem(utility.getResourceAsStream());
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            processWorkbook(wb, conn);
            context.commit();
        }
        catch (IOException e)
        {
            log.error("An IO Exception occurred while trying to parse the Excel file.", e);
            context.rollback();
            throw new MigrationException("Error reading file.", e);
        }
        catch (SQLException e)
        {
            log.error("Caught a SQLException when trying to obtain a database connection");
            context.rollback();
            throw new MigrationException("Error obtaining database connection", e);
        }
        finally
        {
            SqlUtil.close(conn, null, null);
        }
    }

    /**
     * Process workbook by overwriting this method
     *
     * @param wb   the excel workbook to process
     * @param conn the database connection to use for data loading
     * @throws MigrationException if something goes wrong
     */
    public abstract void processWorkbook(HSSFWorkbook wb, Connection conn)
            throws MigrationException;
}
