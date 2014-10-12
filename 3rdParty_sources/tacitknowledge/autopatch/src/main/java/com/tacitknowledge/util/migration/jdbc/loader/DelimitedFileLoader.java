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

import com.tacitknowledge.util.migration.jdbc.SqlLoadMigrationTask;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

/**
 * Loads files assumed to be in a delimited format and representing a
 * table in the database.  The file name should begin with the prefix:
 * "&lt;tablename&gt;_tb".  The file's first row should represent the name of
 * each column in the table that the underlying data elements (rows 2
 * through n) will be mapped to.
 *
 * @author Chris A. (chris@tacitknowledge.com)
 */
public abstract class DelimitedFileLoader extends SqlLoadMigrationTask
{
    /**
     * The path separator
     */
    public static final String PATH_SEPARATOR = File.separator;

    /**
     * Class logger
     */
    private static Log log = LogFactory.getLog(DelimitedFileLoader.class);

    /**
     * Private variable that indicates if the header has been parsed or not.
     */
    private boolean parsedHeader = false;

    /**
     * Gets the expected file delimiter.  A pipe-delimited
     * reader should return "|", for example.
     *
     * @return the delimiter string used to separate columns
     */
    public abstract String getDelimiter();

    /**
     * Should return the name of the file to load.  This will
     * support either an absolute name, or the name of a file
     * in the classpath of the application in which this is executed.
     *
     * @return the absolute path name of the table to load
     */
    public abstract String getName();

    /**
     * Parses a line of data, and sets the prepared statement with the
     * values.  If a token contains "&lt;null&gt;" then a null value is passed
     * in.
     *
     * @param data the tokenized string that is mapped to a row
     * @param stmt the statement to populate with data to be inserted
     * @return false if the header is returned, true otherwise
     * @throws SQLException if an error occurs while inserting data into the database
     */
    protected boolean insert(String data, PreparedStatement stmt) throws SQLException
    {
        if (!parsedHeader)
        {
            parsedHeader = true;
            log.info("Header returned: " + data);
            return false;
        }
        StringTokenizer st = new StringTokenizer(data, getDelimiter());
        int counter = 1;
        log.info("Row being parsed: " + data);
        while (st.hasMoreTokens())
        {
            String colVal = st.nextToken();
            if (colVal.equalsIgnoreCase("<null>"))
            {
                stmt.setString(counter, null);
            }
            else
            {
                stmt.setString(counter, colVal);
            }
            counter++;
        }
        return true;
    }

    /**
     * Returns the table name from the full path name
     * by parsing it out of a file in the format
     * name_db&lt;period&gt;(some extension)
     *
     * @return the name of the table to add data to
     */
    protected String getTableFromName()
    {
        String name = getName();
        int startTable = name.lastIndexOf(PATH_SEPARATOR);
        int endTable = name.indexOf("_db", startTable);
        return name.substring((startTable + 1), endTable);
    }

    /**
     * Parses the table name from the file name, and the column names
     * from the header (first row) of the delimited file.  Creates an
     * insert query to insert a single row.
     *
     * @return query in the format: INSERT INTO tablename (colname, colname)...
     */
    protected String getStatmentSql()
    {
        try
        {
            String columnHeader = getHeader(getResourceAsStream());
            String delimiter = getDelimiter();
            StringTokenizer st = new StringTokenizer(columnHeader, delimiter);
            ArrayList columnNames = new ArrayList();
            while (st.hasMoreTokens())
            {
                columnNames.add((st.nextToken().trim()));
            }
            StringBuffer query = new StringBuffer("INSERT INTO ");
            query.append(getTableFromName());
            query.append(" (");
            Iterator it = columnNames.iterator();
            boolean firstTime = true;
            while (it.hasNext())
            {
                if (!firstTime)
                {
                    query.append(", ");
                }
                else
                {
                    firstTime = false;
                }
                query.append((String) it.next());
            }
            query.append(") VALUES (");
            for (int i = 0; i < columnNames.size(); i++)
            {
                if (i > 0)
                {
                    query.append(", ");
                }
                query.append("?");
            }
            query.append(")");
            return query.toString();
        }
        catch (IOException e)
        {
            log.error("No header was found for file: " + getName(), e);
        }
        return null;
    }

    /**
     * Gets an input stream by first checking the current classloader,
     * then trying to use the system classloader, and finally, trying
     * to access the file on the file system.  If the file is not found,
     * an <code>IllegalArgumentException</code> will be thrown.
     *
     * @return the file as an input stream
     */
    protected InputStream getResourceAsStream()
    {
        FileLoadingUtility utility = new FileLoadingUtility(getName());
        return utility.getResourceAsStream();
    }

    /**
     * Returns the header (first line) of the file.
     *
     * @param is the input stream containing the data to load
     * @return the first row
     * @throws IOException if the input stream could not be read
     */
    protected String getHeader(InputStream is) throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        return reader.readLine();
    }
}
