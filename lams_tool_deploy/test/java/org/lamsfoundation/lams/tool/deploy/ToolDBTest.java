/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */

package org.lamsfoundation.lams.tool.deploy;

import junit.framework.*;
import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
/**
 *
 * @author chris
 */
public abstract class ToolDBTest extends TestCase
{
    protected PropertiesConfiguration props = null;
    
    /** Creates a new instance of ToolDBTest */
    public ToolDBTest(String testName)
    {
        super(testName);
    }
    
    protected void setUp() throws java.lang.Exception
    {
        props = new PropertiesConfiguration("test/file/test.properties");
        dropLamsDB("test/file/test.properties");
        createLamsDB("test/file/test.properties");
    }
    
    protected void tearDown() throws java.lang.Exception
    {
        dropLamsDB("test/file/test.properties");
    }
    
    protected void createLamsDB(String propsFilePath) throws Exception
    {
        Connection conn = null;
        try
        {
            DBConnector connector =  new DBConnector(propsFilePath);
            conn = connector.connect();
            conn.setAutoCommit(false);
            File createLamsTables = new File("test/file/sql/lams_common/create_lams_11_tables.sql");
            File insertTypesData = new File("test/file/sql/lams_common/insert_types_data.sql");
            
            ScriptRunner createRunner = new ScriptRunner(FileUtils.readFileToString(createLamsTables, "UTF8"),conn);
            createRunner.run();
            
            ScriptRunner typesRunner = new ScriptRunner(FileUtils.readFileToString(insertTypesData, "UTF8"),conn);
            typesRunner.run();
            conn.commit();
        }
        catch (Exception ex)
        {
            conn.rollback();
            throw ex;
        }
        finally
        {
            DbUtils.closeQuietly(conn);
        }
    }
    
    protected void dropLamsDB(String propsFilePath) throws Exception
    {
        Connection conn = null;
        try
        {
            DBConnector connector =  new DBConnector(propsFilePath);
            conn = connector.connect();
            conn.setAutoCommit(false);
            File dropLamsTables = new File("test/file/sql/lams_common/drop_lams_11_tables.sql");
            ScriptRunner dropRunner = new ScriptRunner(FileUtils.readFileToString(dropLamsTables, "UTF8"),conn);
            dropRunner.run();
            conn.commit();
        }
        catch (Exception ex)
        {
            conn.rollback();
            throw ex;
        }
        finally
        {
            DbUtils.closeQuietly(conn);
        }
    }
    
    protected void insertTestRecords(String propsFilePath) throws Exception
    {
        Connection conn = null;
        try
        {
            DBConnector connector =  new DBConnector(propsFilePath);
            conn = connector.connect();
            conn.setAutoCommit(false);
            File insertTestRecords = new File("test/file/sql/insert_test_records.sql");
            ScriptRunner insertRunner = new ScriptRunner(FileUtils.readFileToString(insertTestRecords , "UTF8"),conn);
            insertRunner.run();
            conn.commit();
        }
        catch (Exception ex)
        {
            conn.rollback();
            throw ex;
        }
        finally
        {
            DbUtils.closeQuietly(conn);
        }
    }
    
}
